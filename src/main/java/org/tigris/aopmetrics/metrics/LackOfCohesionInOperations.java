package org.tigris.aopmetrics.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.ajdt.internal.compiler.ast.AdviceDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AjMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeFieldDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.lookup.InterTypeFieldBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.tigris.aopmetrics.Logger;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * <p><b>LCO (Lack of Cohesion in Operations):</b> Pairs of operations
 *  working on different class fields minus pairs
 *  of operations working on common fields (zero if negative).</p>	
 *
 * <p><i>Similarly to the LCOM (Lack of Cohesion in Methods)
 * OO metric, LCO is associated with the pairwise dissimilarity
 * between different operations belonging to the same module.
 * Operations working on separate subsets of the module
 * fields are considered dissimilar and contribute to the increase
 * of the metric's value. LCO will be low if all operations
 * in a class or an aspect share a common data structure
 * being manipulated or accessed.</i></p>
 * 
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)</p>
 * 
 * <p>Rules:<ul>
 *   <li>only instance variables are counted, static fields are ommited
 * </ul></p>
 *
 * TODO: introductions are not correctly handled!
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: LackOfCohesionInOperations.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class LackOfCohesionInOperations implements MetricsCalculator {

	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);

		int result = countCohesion((Type)source);
		return new Measurement[] { new Measurement(Metrics.LCO, result) };
	}
	
	private int countCohesion(Type type){
		TypeDeclaration decl = type.getTypeDeclaration();

		CohesionVisitor visitor = new CohesionVisitor();
		decl.traverse(visitor, (CompilationUnitScope)null);

		return visitor.countLackOfCohesion();
	}
	
	class CohesionVisitor extends ASTVisitor {
		// ASTNode is used, since this is common subtype of FieldDeclaration and InterTypeFieldDeclaration
		private Map<AbstractMethodDeclaration, Set<ASTNode>> fieldAccess = 
				new HashMap<AbstractMethodDeclaration, Set<ASTNode>>();
		private AbstractMethodDeclaration currentMethod;

		public int countLackOfCohesion() {
			int p = 0, q = 0;
			boolean allFieldAccessEmpty = true;
			for(AbstractMethodDeclaration methodA: fieldAccess.keySet()){
				Set<ASTNode> fieldsA = fieldAccess.get(methodA); 

				if (!fieldsA.isEmpty())
					allFieldAccessEmpty = false;

				for(AbstractMethodDeclaration methodB: fieldAccess.keySet()){
					/* omitting situation when:
					 *  -  A = B
					 *  -  (B,A) when (A,B) was processed before  (assuming that bodyStart is constant) */
					if (methodB == methodA || methodB.bodyStart <= methodA.bodyStart)
						continue;

					Set<ASTNode> fieldsB = fieldAccess.get(methodB);
					
					if (isProductEmpty(fieldsA, fieldsB)) {
						p++;
					} else  {
						q++;
					}
				}
			}
			
			debugPrintFAccess();
			
			if (allFieldAccessEmpty)
				return 0;
			else
				return (p > q) ?  (p - q) : 0;
		}

		private <T> boolean isProductEmpty(Set<T> fieldsA, Set<T> fieldsB) {
			Set<T> diff = new HashSet(fieldsA);
			diff.removeAll(fieldsB);
			return diff.size() == fieldsA.size();
		}

		public boolean visit(MethodDeclaration decl, ClassScope scope) {
			if ( decl.getClass() == InterTypeMethodDeclaration.class
					|| decl.getClass() == AdviceDeclaration.class
					|| decl.getClass() == AjMethodDeclaration.class ) {
				currentMethod = decl;
				fieldAccess.put(currentMethod, new HashSet());
			}
			
			return super.visit(decl, scope);
		}
		public void endVisit(MethodDeclaration decl, ClassScope scope) {
			currentMethod = null;
			super.endVisit(decl, scope);
		}

		private void addFieldAccess(FieldBinding field) {
			if (field.isStatic() == false && currentMethod != null 
					&& field.declaringClass == currentMethod.binding.declaringClass) {
				if (field instanceof InterTypeFieldBinding) {
					InterTypeFieldBinding itField = (InterTypeFieldBinding)field;
					fieldAccess.get(currentMethod).add(itField.sourceMethod);
				} else
					fieldAccess.get(currentMethod).add(field.sourceField());
			}
		}

		public boolean visit(FieldReference ref, BlockScope scope) {
			addFieldAccess(ref.binding);
			return super.visit(ref, scope);
		}


		public boolean visit(SingleNameReference ref, BlockScope scope) {
			if (ref.localVariableBinding() == null && !ref.isTypeReference()) {
				addFieldAccess(ref.fieldBinding());
			}
			return super.visit(ref, scope);
		}
		
		private void debugPrintFAccess() {
			if (Logger.isDebugMode()) {
				for(AbstractMethodDeclaration method: fieldAccess.keySet()) {
					//Logger.debug(new String(method.binding.readableName()) + ": ");
					for(ASTNode field: fieldAccess.get(method)) {
						if (field instanceof FieldDeclaration) {
							FieldDeclaration decl = (FieldDeclaration)field;
							Logger.debug(" - " + new String(decl.binding.readableName()));
						} else if (field instanceof InterTypeFieldDeclaration) {
							InterTypeFieldDeclaration decl = (InterTypeFieldDeclaration)field;
							Logger.debug(" - " + new String(decl.binding.readableName()));
						}
					}
				}
			}
		}
	}
}

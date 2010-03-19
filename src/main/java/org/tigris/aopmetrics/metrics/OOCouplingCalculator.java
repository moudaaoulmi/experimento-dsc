package org.tigris.aopmetrics.metrics;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.tigris.aopmetrics.Logger;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * <p>Calculates couplings metrics: Coupling between Modules,
 * Coupling on Method Call and Coupling on Field Access.
 * 
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 * 
 * <p><b>CMC (Coupling on Method Call)</b>: Number of modules or interfaces declaring methods
 *  that are possibly called by a given module.
 *  
 *  <p><i>This metric descends from the OO metric CBO (Coupling Between Objects), which
 *  was split into two (CMC and CFA) to distinguish coupling on operations from coupling
 *  on attributes. Aspect introductions must be taken into account when the possibly invoked
 *  methods are determined. Usage of a high number of methods from many
 *  different modules indicates that the function of the given module cannot be
 *  easily isolated from the others. High coupling is associated with a high
 *  dependence from the functions in other modules.</i>
 *  
 *  <p><b>CFA (Coupling on Field Access)</b>: Number of modules or interfaces declaring fields
 *   that are accessed by a given module.
 *   
 *  <p><i>Similarly to CMC, CFA measures the dependences of a given module on other modules,
 *   but in terms of accessed fields, instead of methods. In OO systems this metric is usually
 *   close to zero, but in AOP, aspects might access class fields to perform their function,
 *   so observing the new value in aspectized software may be important to assess the coupling
 *   of an aspect with other classes/aspects.</i></p>
 *
 *  <p><b>Coupling between Modules</b> - Number of modules or interfaces declaring methods
 *  or fields that are possibly called or accessed by a given module. 
 * 
 * <p>Rules:<ul>
 *   <li>Constructor calls are also counted as a <i>Method Call</i>
 *   <li>Calls and accesses to <i>this</i> type are ignored
 * </ul>
 * 
 * TODO: review introductions
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: OOCouplingCalculator.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class OOCouplingCalculator implements MetricsCalculator {
	/** 
	 * @see org.tigris.aopmetrics.metrics.MetricsCalculator#calculate(org.tigris.aopmetrics.source.MetricsSource, org.tigris.aopmetrics.source.Project)
	 */
	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);

		return countCoupling((Type)source);
	}

	private Measurement[] countCoupling(Type type) {
		TypeDeclaration decl = type.getTypeDeclaration();
		
		CouplingVisitor visitor = new CouplingVisitor(decl.binding);
		decl.traverse(visitor, (CompilationUnitScope)null);
		
		return new Measurement[] {
			new Measurement(Metrics.CFA, visitor.getFieldAccessCount()),
			new Measurement(Metrics.CMC, visitor.getMethodCallsCount()),
			new Measurement(Metrics.CBM, visitor.getCouplingCount()),
		};
	}

	static class CouplingVisitor extends ASTVisitor {
		private Set<TypeBinding> methodCalls = new HashSet<TypeBinding>();
		private Set<TypeBinding> fieldAccess = new HashSet<TypeBinding>();

		public CouplingVisitor(TypeBinding thisType) {
			methodCalls.add(thisType);
			fieldAccess.add(thisType);
		}
		public int getMethodCallsCount() {
			printSetOfTypeBindings("Method calls: ", methodCalls);
			return methodCalls.size() - 1;
		}
		public int getFieldAccessCount() {
			printSetOfTypeBindings("Field accesses: ", fieldAccess);
			return fieldAccess.size() - 1;
		}
		public int getCouplingCount() {
			Set<TypeBinding> coupling = new HashSet<TypeBinding>(fieldAccess);
			coupling.addAll(methodCalls);
			printSetOfTypeBindings("C: ", coupling);
			return coupling.size() - 1;
		}

		private void printSetOfTypeBindings(String title, Set<TypeBinding> coupling) {
			if (Logger.isDebugMode()) {
				Logger.debug(title);
				for(TypeBinding m : coupling){
					Logger.debug(" " + new String(m.readableName()));
				}
			}
		}

		private void addFieldAccess(TypeBinding type) {
			if (!CalculatorUtils.isJavaLibPackage(type.getPackage()))
				fieldAccess.add(type);
		}
		private void addMethodCall(TypeBinding type) {
			if (!CalculatorUtils.isJavaLibPackage(type.getPackage()))
				methodCalls.add(type);
		}

		public boolean visit(QualifiedNameReference ref, BlockScope scope) {
			// adding type of first element 
			//  - when it's a local variable or local field, then 'this' class is added
			//  - when it's a type ref (i.e Class.field), then the type is added 
			addFieldAccess(ref.actualReceiverType);
			
			// otherBindins == null to when no other types are refered in the reference (ie. ThisClass.field)
			if (ref.otherBindings != null) {
				for(FieldBinding b : ref.otherBindings){
					// if b.declaringClass == null then it is a primitive type
					if (b.declaringClass != null) {
						addFieldAccess(b.declaringClass);
					}
				}
			} 

			return super.visit(ref, scope);
		}

		public boolean visit(MessageSend msgSend, BlockScope scope) {
			addMethodCall(msgSend.actualReceiverType);
			return super.visit(msgSend, scope);
		}
		
		public boolean visit(AllocationExpression alloc, BlockScope scope) {
			// if enumConstant == null, then we encounter a enum initialization -> skip it
			if (alloc.enumConstant == null) {
				addMethodCall(alloc.type.resolvedType);
			}
			return super.visit(alloc, scope);
		}
	}
}

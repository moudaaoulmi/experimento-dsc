package org.tigris.aopmetrics.metrics;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.ajdt.internal.compiler.ast.AdviceDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AjMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeMethodDeclaration;
import org.aspectj.asm.IRelationship;
import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.aspectj.weaver.AsmRelationshipProvider;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.Member;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.MetricsSourceVisitor;
import org.tigris.aopmetrics.source.MetricsSourceWithAST;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;

/**
 * <p><b>RFM (Response For a Module)</b>: Methods and advices
 *   potentially executed in response to a message received
 *   by a given module.
 *
 * <p><i>Similarly to the related OO metric, RFM measures the
 *  potential communication between the given module and the
 *  other ones. The main adaptation necessary to apply it to
 *  AOP software is associated with the implicit responses that
 *  are triggered whenever a pointcut intercepts an operation of
 *  the given module.</i>
 * 
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 * 
 * <p>Rules:<ul>
 *   <li>RFM for a module is a sum of: number of methods in the module,
 *     number of methods invoked from methods, number of advices
 *     implicitly invoked.
 *   <li>constructors, constructor's invocations, methods invoked from constructors,
 *      advises on constructors are not counted
 *   <li>similarly for static initialisation blocks
 * </ul>
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: ResponseForAModule.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class ResponseForAModule implements MetricsCalculator {
	/** 
	 * @see org.tigris.aopmetrics.metrics.MetricsCalculator#calculate(org.tigris.aopmetrics.source.MetricsSource, org.tigris.aopmetrics.source.Project)
	 */
	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);

		int result = calculateResponse((Type)source, project);
		return new Measurement[] { new Measurement(Metrics.RFM, result) };
	}

	private int calculateResponse(Type type, Project project) {
		if (type.getKind() == MetricsSource.Kind.INTERFACE)
			return 0;
		
		TypeDeclaration decl = type.getTypeDeclaration();

		CountResponseMethodsVisitor mVisitor = new CountResponseMethodsVisitor();
		decl.traverse(mVisitor, (CompilationUnitScope)null);

		CountInfluencingAdvicesVisitor aVisitor = new CountInfluencingAdvicesVisitor(project);
		type.traverse(aVisitor, null);
		
		return mVisitor.getMethodsCount() + aVisitor.getAdvicesCount();
	}
	
	static class CountResponseMethodsVisitor extends ASTVisitor {
		private Set<MethodBinding> methods = new HashSet<MethodBinding>();

		public int getMethodsCount() {
			return methods.size();
		}

		public boolean visit(MessageSend msgSend, BlockScope scope) {
			MethodScope methodScope = scope.methodScope();
			if (methodScope != null && methodScope.referenceMethod() != null){
				if (methods.contains(methodScope.referenceMethod().binding)
						&& !CalculatorUtils.isCallToJavaLib(msgSend)) {
					methods.add(msgSend.binding);
				}
			}
			return super.visit(msgSend, scope);
		}

		public boolean visit(MethodDeclaration decl, ClassScope scope) {
			if (decl.getClass() == AjMethodDeclaration.class ||
					decl.getClass() == InterTypeMethodDeclaration.class){
				methods.add(decl.binding);
			}

			return super.visit(decl, scope);
		}
	}

	static class CountInfluencingAdvicesVisitor extends MetricsSourceVisitor {
		private Project project;
		private Set<MethodBinding> advices = new HashSet<MethodBinding>();
		
		public CountInfluencingAdvicesVisitor(Project project) {
			this.project = project;
		}

		public int getAdvicesCount(){
			return advices.size();
		}

		public void visit(Member member, Type type) {
			if (member.getKind() == MetricsSource.Kind.METHOD)
				gatherAdvices(member);
			super.visit(member, type);
		}

		private void gatherAdvices(MetricsSourceWithAST member) {
			for(IRelationship relation : member.getAspectRelations()) {
				if (isAdvisedByRelation(relation)) {
					for(Object target : relation.getTargets()) {
						MetricsSourceWithAST source = project.getSourceUsingHandle((String)target);

						/** the source of 'ADVISED_BY' can also be 'declare soft' */ 
						if (source.getAST() instanceof AdviceDeclaration) {
							AdviceDeclaration advice = (AdviceDeclaration)source.getAST();
							advices.add(advice.binding);
						} 
					}
				}
			}
		}

		private boolean isAdvisedByRelation(IRelationship relation) {
			String name = relation.getName();
			return (name == AsmRelationshipProvider.ADVISED_BY);
		}
	}
}

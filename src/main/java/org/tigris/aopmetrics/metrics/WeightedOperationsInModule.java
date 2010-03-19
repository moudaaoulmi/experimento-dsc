package org.tigris.aopmetrics.metrics;

import org.aspectj.ajdt.internal.compiler.ast.AdviceDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AjMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeConstructorDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeMethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * <p><b>WOM (Weighted Operations in Module)</b>: Number of operations in a given module.
 * 
 * <p><i>Similarly to the related OO metric, WOM captures the internal complexity of
 *  a module in terms of the number of implemented functions. 
 *  A more refined version of this metric can be obtained by giving different
 *  weights to operations with different internal complexity.</i>
 *  
 * <p><i>Since the proposed metrics apply both to classes and aspects, in the following
 *  the term module will be used to indicate either of the two modularization units.
 *  Similarly, the term operation subsumes class methods and aspect advices/introductions.</i>
 *  
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 *
 * <p>Rules: <ul>
 *  <li>constructors and methods are counted
 *  <li>advices and introductions of methods or constructors are counted (as a member of an aspect)
 * </ul>
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: WeightedOperationsInModule.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class WeightedOperationsInModule implements MetricsCalculator {

	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);
		
		int result = countOperationsInModule((Type)source);
		return new Measurement[] {new Measurement(Metrics.WOM, result) };
	}

	private int countOperationsInModule(Type type) {
		TypeDeclaration decl = type.getTypeDeclaration();
		WOMVisitor visitor = new WOMVisitor();
		
		decl.traverse(visitor, decl.scope);
		return visitor.getWOMValue();
	}
	
	static class WOMVisitor extends ASTVisitor {
		private int value;

		public void endVisit(ConstructorDeclaration decl, ClassScope scope) {
			if (!decl.isDefaultConstructor)
				value++;

			super.endVisit(decl, scope);
		}

		public boolean visit(MethodDeclaration decl, ClassScope scope) {
			Class declClass = decl.getClass();
			if ( declClass == InterTypeMethodDeclaration.class
					|| declClass == InterTypeConstructorDeclaration.class
					|| declClass == AdviceDeclaration.class
					|| declClass == AjMethodDeclaration.class ) {
				value++;
			}

			return super.visit(decl, scope);
		}

		public int getWOMValue() {
			return this.value;
		}
	}
}

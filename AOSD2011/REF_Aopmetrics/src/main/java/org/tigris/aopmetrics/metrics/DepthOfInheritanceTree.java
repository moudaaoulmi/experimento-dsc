package org.tigris.aopmetrics.metrics;

import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;



/**
 * <p><b>DIT (Depth of Inheritance Tree)</b>: Length of the longest path from a given module 
 * to the class/aspect hierarchy root.
 * 
 * <p><i>Similarly to the related OO metric, DIT measures the scope of the properties.
 *  The deeper a class/aspect is in the hierarchy, the greater the number of operations
 *  it might inherit, thus making it more complex to understand and change. Since aspects
 *  can alter the inheritance relationship by means of static crosscutting, such effects
 *  of aspectization must be taken into account when computing this metric.</i>
 *  
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 *  
 * <p>Rules:<ul>
 *   <li>Every class and aspect in inheritance hierarchy of a class or an aspect is counted (excluding Object)
 *   <li>DIT for interfaces is always zero.
 * </ul>
 *  
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: DepthOfInheritanceTree.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class DepthOfInheritanceTree implements MetricsCalculator {

	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);

		int result = countDepth((Type)source);
		return new Measurement[] {new Measurement(Metrics.DIT, result) };
	}

	private int countDepth(Type type) {
		TypeDeclaration decl = type.getTypeDeclaration();
		ReferenceBinding aType = decl.binding;

		int result = 0;
		while(true) {
			ReferenceBinding superType = getSuperClass(aType);
			

			if (superType != null) {
				result++;
			} else {
				// Object class is not counted
				result--;
				break;
			}

			aType = superType;
		} 
		
		return result;
	}

	private ReferenceBinding getSuperClass(ReferenceBinding type) {
		if (type instanceof SourceTypeBinding) {
			return ((SourceTypeBinding)type).superclass();
		} else if (type instanceof ParameterizedTypeBinding) {
			return ((ParameterizedTypeBinding)type).superclass();
		} else
			throw new RuntimeException("not expected type (DIT): " + type.getClass());
	}
}

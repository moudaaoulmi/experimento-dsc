package org.tigris.aopmetrics.metrics;

import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.MetricsSourceVisitor;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;



/**
 * <p><b>NOC (Number Of Children):</b> Number of immediate subclasses or
 *   sub-aspects of a given module.
 *   
 * <p><i>Similarly to DIT, NOC measures the scope of the properties, but
 *  in the reverse direction with respect to DIT. The number of children
 *  of a module indicates the proportion of modules potentially dependent
 *  on properties inherited from the given one.</i>
 *  
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 * 
 *  
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: NumberOfChildren.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class NumberOfChildren implements MetricsCalculator {
	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);
		
		int result = countChildren(project, (Type)source);
		
		return new Measurement[] {new Measurement(Metrics.NOC, result)};
	}

	private int countChildren(Project project, Type type) {
		CountChildrenVisitor visitor = new CountChildrenVisitor(type);
		project.traverse(visitor);
		
		return visitor.getResult();
	}
	static class CountChildrenVisitor extends MetricsSourceVisitor {
		private ReferenceBinding supertype;
		private int result;

		public CountChildrenVisitor(Type type) {
			this.supertype = type.getTypeDeclaration().binding;
		}
		public int getResult() {
			return result;
		}

		public void visit(Type type, Package pkg) {
			ReferenceBinding aSuperType = type.getTypeDeclaration().binding.superclass();
			
			if (supertype.equals(aSuperType))
				result++;
			
			super.visit(type, pkg);
		}
	}
}

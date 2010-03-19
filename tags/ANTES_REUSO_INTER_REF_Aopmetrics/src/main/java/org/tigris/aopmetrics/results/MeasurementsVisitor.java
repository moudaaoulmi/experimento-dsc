package org.tigris.aopmetrics.results;

/**
 * Empty implementation of visitor design pattern for measurements hierarchy.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: MeasurementsVisitor.java,v 1.1 2006/04/20 23:37:18 misto Exp $
 */
abstract public class MeasurementsVisitor {
	public void visit(TypeMeasurements type, PackageMeasurements pkg) {}
	public void endVisit(TypeMeasurements type, PackageMeasurements pkg) {}

	public void visit(PackageMeasurements pkg, ProjectMeasurements project) {}
	public void endVisit(PackageMeasurements pkg, ProjectMeasurements project) {}
	
	public void visit(ProjectMeasurements project) {}
	public 	void endVisit(ProjectMeasurements project) {}

}

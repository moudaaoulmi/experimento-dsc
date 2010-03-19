package org.tigris.aopmetrics.results;

import java.util.LinkedList;
import java.util.List;

/**
 * @author misto
 */
public class ProjectMeasurements extends MeasurementsBag {
	private List<PackageMeasurements> packages = new LinkedList<PackageMeasurements>();
	
	public ProjectMeasurements(String name) {
		super(name);
	}
	
	public void addPackage(PackageMeasurements pkgm){
		packages.add(pkgm);
	}
	
	public void traverse(MeasurementsVisitor visitor) {
		visitor.visit(this);
		for(PackageMeasurements pkg : packages){
			pkg.traverse(visitor, this);
		}
		visitor.endVisit(this);
	}
}

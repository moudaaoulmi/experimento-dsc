package org.tigris.aopmetrics.results;

import java.util.LinkedList;
import java.util.List;

/**
 * @author misto
 */
public class PackageMeasurements extends MeasurementsBag {
	private List<TypeMeasurements> types = new LinkedList<TypeMeasurements>();
	
	public PackageMeasurements(String name) {
		super(name);
	}
	
	public void addType(TypeMeasurements type) {
		types.add(type);
	}
	
	public void traverse(MeasurementsVisitor visitor, ProjectMeasurements project) {
		visitor.visit(this, project);
		for(TypeMeasurements type : types){
			type.traverse(visitor, this);
		}
		visitor.endVisit(this, project);
	}
}

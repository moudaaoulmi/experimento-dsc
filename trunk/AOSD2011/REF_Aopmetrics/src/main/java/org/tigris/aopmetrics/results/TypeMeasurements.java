package org.tigris.aopmetrics.results;


/**
 * @author misto
 */
public class TypeMeasurements extends MeasurementsBag {
	private String kind;

	public TypeMeasurements(String name) {
		super(name);
	}

	public void setKind(String kind) {	this.kind = kind; }
	public String getKind() {
		return kind;
	}
	
	public void traverse(MeasurementsVisitor visitor, PackageMeasurements pkg) {
		visitor.visit(this, pkg);
		visitor.endVisit(this, pkg);
	}
}

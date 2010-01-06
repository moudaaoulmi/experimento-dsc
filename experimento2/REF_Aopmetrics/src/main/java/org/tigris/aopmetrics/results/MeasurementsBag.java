package org.tigris.aopmetrics.results;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author misto
 */
abstract public class MeasurementsBag {
	private Collection<Measurement> measurements = new LinkedList<Measurement>();
	private String name;

	protected MeasurementsBag(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addMeasurement(Measurement m){
		measurements.add(m);
	}
	public void addMeasurements(Measurement[] ms){
		for(Measurement m : ms) {
			measurements.add(m);
		}
	}
	public Collection<Measurement> getMeasurements(){
		return this.measurements;
	}
}

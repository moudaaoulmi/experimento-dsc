package org.tigris.aopmetrics.results;


public class Measurement {
	private String name;

	private int valuei;
	private double valuef;
	
	
	public Measurement(String name, int value) {
		this.name = name;
		this.valuei = value;
	}
	public Measurement(String name, double value) {
		this.name = name;
		this.valuef = value;
	}

	public String getName() {
		return name;
	}
	public double getValuef() {
		return valuef;
	}
	public int getValuei() {
		return valuei;
	}
	public String getValueAsString() {
		if (isFloatValue())
			return Double.toString(this.valuef);
		else
			return Integer.toString(valuei);
	}
	public double getValueAsDouble() {
		return isFloatValue() ?  valuef : valuei;
	}

	public boolean isFloatValue() {
		return (this.valuei == 0 && this.valuef != 0);
	}


	public String toString() {
		return this.name + ": " + getValueAsString();
	}
}

package org.tigris.aopmetrics.metrics;

public class Metrics {
	public static final String LOCC = "LOCC";
	public static final String WOM = "WOM";
	public static final String DIT = "DIT";
	public static final String NOC = "NOC";

	public static final String CFA = "CFA";
	public static final String CMC = "CMC";
	public static final String CBM = "CBM";
	public static final String RFM = "RFM";
	public static final String LCO = "LCO";
	
	public static final String CDA = "CDA";
	public static final String CAE = "CAE";

	public static final String NOT = "NOT";
	public static final String A = "A";
	public static final String RMARTIN_CE = "RMartin Ce";
	public static final String RMARTIN_CA = "RMartin Ca";
	public static final String RMARTIN_I = "RMartin I";
	public static final String RMARTIN_D = "RMartin D";
	public static final String CE = "Ce";
	public static final String CA = "Ca";
	public static final String I = "I";
	public static final String DN = "Dn";

	public static final MetricsCalculator[] typeMetricsCalculators = {
		new LinesOfClassCode(),
		new WeightedOperationsInModule(),
		new DepthOfInheritanceTree(),
		new NumberOfChildren(),
		new OOCouplingCalculator(),
		new AspectualAffectedAndEffectedCalculator(),
		new ResponseForAModule(),
		new LackOfCohesionInOperations()
	};

	public static final MetricsCalculator[] packageMetricsCalculators = {
		new MartinsMetricsCalculator()
	};
	
	private Metrics() {}
}

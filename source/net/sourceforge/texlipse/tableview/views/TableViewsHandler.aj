package net.sourceforge.texlipse.tableview.views;

public privileged aspect TableViewsHandler {

	pointcut internalSumHandler(): execution(private double TexRowList.internalSum(double,  int,  TexRow));

	double around(double value): internalSumHandler() && args(value,..) {
		try {
			proceed(value);
		} catch (NumberFormatException nfe) {
			// this is expected to happen because of text strings
		}
		return value;
	}

}

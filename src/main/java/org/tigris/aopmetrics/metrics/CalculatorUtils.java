package org.tigris.aopmetrics.metrics;

import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Type;


/**
 * Helper class for metrics calculator.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: CalculatorUtils.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class CalculatorUtils {
	static boolean isJavaLibPackage(PackageBinding pkg) {
		// primitive type have null as a package ref
		if (pkg == null)
			return true;
		
		String pkgName = new String(pkg.readableName());
		return pkgName.startsWith("java.") || pkgName.startsWith("javax.");
	}
	static boolean isCallToJavaLib(MessageSend msg) {
		return isJavaLibPackage(msg.binding.declaringClass.getPackage());
	}

	static void assertSourceIsAPackage(MetricsSource source) {
		if (!(source instanceof Package))
			throw new InvalidMetricsSourceException("This metric's source should be a package!.");
	}
	static void assertSourceIsAType(MetricsSource source) {
		if (!(source instanceof Type))
			throw new InvalidMetricsSourceException("This metric's source should be a type!.");
	}
}

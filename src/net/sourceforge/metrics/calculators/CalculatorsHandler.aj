package net.sourceforge.metrics.calculators;

import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.core.Metric;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect CalculatorsHandler {

	pointcut internalCalculateHandler(): execution(private String McCabe.internalCalculate(AbstractMetricSource,String));

	pointcut internalCalculateHandler2(): execution(private void Norm.internalCalculate(AbstractMetricSource, IType,IType[]));

	pointcut containsSuperCallHandler(): execution(private boolean Norm.containsSuperCall(IMethod));

	pointcut internalCalculateHandler3(): execution(private void NumberOfAttributes.internalCalculate(AbstractMetricSource));

	pointcut internalCalculateHandler4(): execution(private void NumberOfMethods.internalCalculate(AbstractMetricSource));

	pointcut internalCalculateHandler5(): execution(private void LackOfCohesion.internalCalculate(AbstractMetricSource));

	pointcut internalVisitMethodsHandler(): execution(private void LackOfCohesion.internalVisitMethods(IMethod[], boolean,int, String));

	pointcut internalInitBucketsHandler(): execution(private void LackOfCohesion.internalInitBuckets(IField[]));

	pointcut acceptHandler(): execution(public void RMartinCouplings.EfferentCollector.accept(IResource , int , int ,IJavaElement, int));

	pointcut internalGetPackageNameHandler(): execution(private String RMartinCouplings.EfferentCollector.internalGetPackageName(int, int,ICompilationUnit));

	declare soft: JavaModelException: internalCalculateHandler()||internalCalculateHandler2()||containsSuperCallHandler()||internalCalculateHandler3()||internalCalculateHandler4()||internalCalculateHandler5()||internalVisitMethodsHandler()||internalInitBucketsHandler()||internalGetPackageNameHandler();
	declare soft: InvalidInputException: internalVisitMethodsHandler();

	String around(): internalGetPackageNameHandler() {
		try {
			return proceed();
		} catch (JavaModelException e) {
			return null;
		}
	}

	void around(): acceptHandler(){
		try {
			proceed();
		} catch (StringIndexOutOfBoundsException x) {
			//XXX LOG  - não generalizado
			Log.logError("Ce: Error getting package name.", x);
		}

	}

	void around(): internalInitBucketsHandler() {
		try {
			proceed();
		} catch (JavaModelException e) {
		}
	}

	void around(String methodName): internalVisitMethodsHandler() && args(..,methodName) {
		try {
			proceed(methodName);
		} catch (JavaModelException e) {
			System.err
					.println("LCOM:Can't get source for method " + methodName);
		} catch (InvalidInputException e) {
			System.err.println("LCOM:Invalid scanner input for method"
					+ methodName);
		}
	}

	void around(): internalCalculateHandler5() {
		try {
			proceed();
		} catch (JavaModelException e) {
		}
	}

	void around(AbstractMetricSource source): internalCalculateHandler4() && args(source){
		try {
			proceed(source);
		} catch (JavaModelException e) {
			source.setValue(new Metric(NumberOfMethods.NUM_METHODS, 0));
			source.setValue(new Metric(NumberOfMethods.NUM_STAT_METHODS, 0));
		}
	}

	void around(AbstractMetricSource source): internalCalculateHandler3() && args(source){
		try {
			proceed(source);
		} catch (JavaModelException e) {
			source.setValue(new Metric(NumberOfAttributes.NUM_FIELDS, 0));
			source.setValue(new Metric(NumberOfAttributes.NUM_STAT_FIELDS, 0));
		}
	}

	boolean around(): containsSuperCallHandler() {
		try {
			return proceed();
		} catch (JavaModelException e) {
			return false;
		}
	}

	void around(): internalCalculateHandler2() {
		try {
			proceed();
		} catch (JavaModelException e) {
		}
	}

	String around(AbstractMetricSource source, String sourceCode): internalCalculateHandler() && args(source,sourceCode) {
		try {
			return proceed(source, sourceCode);
		} catch (JavaModelException e) {
			Log.logError("No sourcecode for " + source.getHandle(), e);
		}
		return sourceCode;
	}

}

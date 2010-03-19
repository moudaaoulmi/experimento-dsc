package org.tigris.aopmetrics;

import org.tigris.aopmetrics.metrics.Metrics;
import org.tigris.aopmetrics.metrics.MetricsCalculator;
import org.tigris.aopmetrics.results.PackageMeasurements;
import org.tigris.aopmetrics.results.ProjectMeasurements;
import org.tigris.aopmetrics.results.TypeMeasurements;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * @author misto
 */
public class Engine {
	public ProjectMeasurements calculateMetrics(Project project) {
		ProjectMeasurements projectMeasures = new ProjectMeasurements(project.getName());
		
		for( Package pkg : project.getPackages() ){
			PackageMeasurements packageMeasures = calculateMetricsForPackage(pkg, project);
			projectMeasures.addPackage(packageMeasures);
		}
		
		return projectMeasures;
	}

	private PackageMeasurements calculateMetricsForPackage(Package pkg, Project project) {
		PackageMeasurements packageMeasures = new PackageMeasurements(pkg.getName());

		for( Type type : pkg.getTypes()){
			TypeMeasurements typeMeasures = calculateMetricsForType(type, project);
			packageMeasures.addType(typeMeasures);
		}

		for(MetricsCalculator calc : Metrics.packageMetricsCalculators) {
			packageMeasures.addMeasurements(calc.calculate(pkg, project));
		}

		return packageMeasures;
	}

	private TypeMeasurements calculateMetricsForType(Type type, Project project) {
		TypeMeasurements typeMeasures = new TypeMeasurements(type.getName());
		typeMeasures.setKind(type.getKind().name().toLowerCase());
		
		for(MetricsCalculator calc : Metrics.typeMetricsCalculators) {
			typeMeasures.addMeasurements(calc.calculate(type, project));
		}

		return typeMeasures;
	}
}

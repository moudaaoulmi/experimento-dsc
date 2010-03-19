package org.tigris.aopmetrics.metrics;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.NormalAnnotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.MetricsSourceVisitor;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * <p>Calculates Robert Martin's metrics.</p>
 * 
 * <p>The calculator calculates two versions of Martin's metrics.
 * First one is unmodified version of Robert Martin's metrics
 * (like in 'OO Design Quality Metrics, An Analysis of Dependencies').
 * Second one is version like in RefactorIT.</p>
 *
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: MartinsMetricsCalculator.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class MartinsMetricsCalculator implements MetricsCalculator {
	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAPackage(source);
		Package pkg = (Package)source;

		ThisPackageVisitor thisPkgVisitor = new ThisPackageVisitor();
		pkg.traverse(thisPkgVisitor, project);

		OtherPackagesVisitor otherPkgVisitor = new OtherPackagesVisitor(pkg);
		project.traverse(otherPkgVisitor);


		return prepareResults(thisPkgVisitor, otherPkgVisitor);
	}
	private Measurement[] prepareResults(ThisPackageVisitor thisPkg, OtherPackagesVisitor otherPkg) {
		double abstractness = ((double)thisPkg.getNumberOfAbstract()) / thisPkg.getNumberOfTypes();

		int rmCe = thisPkg.getRMartinCe();
		int ce = thisPkg.getCe();
		int ca = otherPkg.getCa();

		double rmInstability = countInstability(rmCe, ca);
		double instability = countInstability(ce, ca);

		double rmDistance = countDistanceFromMainSequence(abstractness, rmInstability);
		double distance = countDistanceFromMainSequence(abstractness, instability);
		
		return new Measurement[] {
				new Measurement(Metrics.NOT, thisPkg.getNumberOfTypes()),
				new Measurement(Metrics.A, abstractness),

				new Measurement(Metrics.RMARTIN_CE, rmCe),
				new Measurement(Metrics.RMARTIN_CA, ca),
				new Measurement(Metrics.RMARTIN_I, rmInstability),
				new Measurement(Metrics.RMARTIN_D, rmDistance),

				new Measurement(Metrics.CE, ce),
				new Measurement(Metrics.CA, ca),
				new Measurement(Metrics.I, instability),
				new Measurement(Metrics.DN, distance)
			};
	}

	private double countDistanceFromMainSequence(double abstractness, double instability) {
		return Math.abs(abstractness + instability - 1);
	}
	private double countInstability(int ce, int ca) {
		return (ce + ca != 0) ?  ce / (double)(ce + ca) : 0;
	}

	class ThisPackageVisitor extends MetricsSourceVisitor {
		private int numberOfTypes;
		private int numberOfAbstractTypes;
		private Set<TypeBinding> dependencyTargets = new HashSet<TypeBinding>();
		private Set<TypeBinding> dependencySources = new HashSet<TypeBinding>();
		
		public void visit(Type type, Package pkg) {
			final SourceTypeBinding binding = type.getTypeDeclaration().binding;
			if (binding.isAbstract() || binding.isInterface())
				numberOfAbstractTypes++;

			numberOfTypes++;

			DependenciesVisitor visitor = new DependenciesVisitor() {
				public void addDependency(TypeBinding source, TypeBinding target) {
					if (target.getPackage() != binding.getPackage() 
							&& !target.isBaseType()
							&& !CalculatorUtils.isJavaLibPackage(target.getPackage())) {
						dependencyTargets.add(target);
						dependencySources.add(source);
					}
				}
			};
			type.getTypeDeclaration().traverse(visitor, (CompilationUnitScope)null);

			super.visit(type, pkg);
		}

		public int getRMartinCe() {
			return dependencySources.size();
		}
		public int getCe() {
			return dependencyTargets.size();
		}
		public int getNumberOfAbstract() { 
			return numberOfAbstractTypes; 
		}
		public int getNumberOfTypes() {
			return numberOfTypes;
		}
	}

	class OtherPackagesVisitor extends MetricsSourceVisitor {
		private Set<TypeBinding> usedBy = new HashSet<TypeBinding>();
		
		private Package thisPkg;
		private PackageBinding thisPkgBinding;
		public OtherPackagesVisitor(Package pkg) {
			this.thisPkg = pkg;
			
			Type aType = pkg.getTypes().iterator().next();
			this.thisPkgBinding = aType.getTypeDeclaration().binding.getPackage();
		}

		public void visit(Type type, Package pkg) {
			if (pkg == thisPkg)
				return;

			DependenciesVisitor visitor = new DependenciesVisitor() {
				public void addDependency(TypeBinding source, TypeBinding target) {
					if (target.getPackage() == thisPkgBinding) {
						usedBy.add(source);
					}
				}
			};
			type.getTypeDeclaration().traverse(visitor, (CompilationUnitScope)null);

			super.visit(type, pkg);
		}
		
		public int getCa() {
			return usedBy.size();
		}
	}

	
	abstract class DependenciesVisitor extends ASTVisitor {
		private TypeBinding current;

		abstract void addDependency(TypeBinding source, TypeBinding target);
		
		private void analyseTypeDecl(TypeDeclaration decl ){
			if (decl.superclass != null)
				addDependency(decl.binding, decl.superclass.resolvedType);

			if (decl.superInterfaces != null)
				for(TypeReference iface : decl.superInterfaces)
					addDependency(decl.binding, iface.resolvedType);
			
			if (decl.annotations != null)
				for(Annotation ann : decl.annotations) 
					addDependency(current, ann.type.resolvedType);
			
			current = decl.binding;
		}

		public boolean visit(TypeDeclaration decl, BlockScope scope) {
			analyseTypeDecl(decl);
			return super.visit(decl, scope);
		}
		public boolean visit(TypeDeclaration decl, ClassScope scope) {
			analyseTypeDecl(decl);
			return super.visit(decl, scope);
		}
		public boolean visit(TypeDeclaration decl, CompilationUnitScope scope) {
			analyseTypeDecl(decl);
			return super.visit(decl, scope);
		}


		private void analyseTypeRef(TypeReference ref) {
			addDependency(current, ref.resolvedType);
		}
		public boolean visit(ParameterizedQualifiedTypeReference ref, BlockScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(ParameterizedQualifiedTypeReference ref, ClassScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(ParameterizedSingleTypeReference ref, BlockScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(ParameterizedSingleTypeReference ref, ClassScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(QualifiedTypeReference ref, BlockScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(QualifiedTypeReference ref, ClassScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(SingleTypeReference ref, BlockScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}
		public boolean visit(SingleTypeReference ref, ClassScope scope) {
			analyseTypeRef(ref);
			return super.visit(ref, scope);
		}

		public boolean visit(QualifiedNameReference ref, BlockScope scope) {
			// adding type of whole reference
			addDependency(current, ref.resolvedType);

			if (ref.actualReceiverType != current)
				// adding referenced type (i.e E.a.msg())
				addDependency(current, ref.actualReceiverType);

			return super.visit(ref, scope);
		}

		public boolean visit(SingleNameReference ref, BlockScope scope) {
			// we are adding something only when type was referenced in SNR (i.e E.msg())
			if (ref.binding instanceof SourceTypeBinding)
				addDependency(current, (TypeBinding)ref.binding);
			return super.visit(ref, scope);
		}

		public boolean visit(MarkerAnnotation ann, BlockScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}
		public boolean visit(MarkerAnnotation ann, CompilationUnitScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}
		public boolean visit(NormalAnnotation ann, BlockScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}
		public boolean visit(NormalAnnotation ann, CompilationUnitScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}
		public boolean visit(SingleMemberAnnotation ann, BlockScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}
		public boolean visit(SingleMemberAnnotation ann, CompilationUnitScope scope) {
			analyseTypeRef(ann.type);
			return super.visit(ann, scope);
		}

/*		
		private void printNode(ASTNode node) {
			System.out.println(node.getClass().getSimpleName() + " : " + node.print(0, new StringBuffer()));
		}*/
	}
}

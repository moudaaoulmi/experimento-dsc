package org.tigris.aopmetrics.metrics;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.asm.IRelationship;
import org.aspectj.weaver.AsmRelationshipProvider;
import org.tigris.aopmetrics.Logger;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.Member;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.MetricsSourceVisitor;
import org.tigris.aopmetrics.source.MetricsSourceWithAST;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * This metrics calculator calculates <i>Crosscutting Degree of an Aspect</i>
 * and <i>Coupling on Advice Execution</i>.  
 * 
 * 
 * <p>(from Measuring the Effects of Software Aspectization, Mariano Ceccato and Paolo Tonella)
 * 
 * <p><b>CDA (Crosscutting Degree of an Aspect)</b>: Number of modules affected by
 * the pointcuts and by the introductions in a given aspect.
 * 
 * <p><i>This is a brand new metric, specific to AOP software, that must be 
 * introduced as a completion of the CIM metric. While CIM considers only 
 * explicitly named modules, CDA measures all modules possibly affected by
 * an aspect. This gives an idea of the overall impact an aspect has on
 * the other modules. Moreover, the difference between CDA and CIM gives
 * the number of modules that are affected by an aspect without being 
 * referenced explicitly by the aspect, which might indicate the degree
 * of generality of an aspect, in terms of its independence from specific
 * classes/aspects. High values of CDA and low values of CIM are usually
 * desirable.</i>
 * 
 * 
 * <p><b>CAE (Coupling on Advice Execution)</b>: Number of aspects
 *    containing advices possibly triggered by the execution
 *    of operations in a given module.
 *
 * <p><i>If the behavior of an operation can be altered by an aspect
 *  advice, due to a pointcut intercepting it, there is an (implicit)
 *  dependence of the operation from the advice. Thus,
 *  the given module is coupled with the aspect containing the
 *  advice and a change of the latter might impact the former.
 *  Such kind of coupling is absent in OO systems.</i>
 *
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: AspectualAffectedAndEffectedCalculator.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class AspectualAffectedAndEffectedCalculator implements MetricsCalculator {
	/** 
	 * @see org.tigris.aopmetrics.metrics.MetricsCalculator#calculate(org.tigris.aopmetrics.source.MetricsSource, org.tigris.aopmetrics.source.Project)
	 */
	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);
		
		int result[] = calculateDegree(project, (Type)source);
		return new Measurement[] { 
				new Measurement(Metrics.CDA, result[0]),
				new Measurement(Metrics.CAE, result[1])	 };
	}

	private int[] calculateDegree(Project project, Type type) {
		CountAffectedModulesVisitor visitor = new CountAffectedModulesVisitor(project);
		type.traverse(visitor, null);

		return new int[] { 
				visitor.getNumberOfAffectedModules(),
				visitor.getNumberOfEffectedAspects() };
	}
	
	class CountAffectedModulesVisitor extends MetricsSourceVisitor {
		private Project project;
		private Set<Type> affectedModules = new HashSet<Type>();
		private Set<Type> effectedAspects = new HashSet<Type>();
		
		public CountAffectedModulesVisitor(Project project) {
			this.project = project;
		}
		public int getNumberOfAffectedModules(){
			Logger.debug("Affected by aspects:" + affectedModules);
			return affectedModules.size();
		}
		public int getNumberOfEffectedAspects(){
			Logger.debug("Effected aspects:" + effectedAspects);
			return effectedAspects.size();
		}

		public void visit(Member member, Type type) {
			gatherAffectedModules(member);
			super.visit(member, type);
		}
		public void visit(Type type, Package pkg) {
			gatherAffectedModules(type);
			super.visit(type, pkg);
		}

		private void gatherAffectedModules(MetricsSourceWithAST member) {
			for(IRelationship relation : member.getAspectRelations()) {
				if (isAspectualEffectRelation(relation))
					addAllTargetsTo(relation, affectedModules);
				if (isAspectualAffectRelation(relation))
					addAllTargetsTo(relation, effectedAspects);
			}
		}

		private void addAllTargetsTo(IRelationship relation, Set<Type> set) {
			for(Object target : relation.getTargets()) {
				MetricsSource source = project.getSourceUsingHandle((String)target);
				
				if (!source.getKind().isType())
					source = source.getParent();

				set.add((Type)source);
			}
		}

		private boolean isAspectualEffectRelation(IRelationship relation) {
			String name = relation.getName();

			return (name == AsmRelationshipProvider.ADVISES
						|| name == AsmRelationshipProvider.DECLARES_ON
						|| name == AsmRelationshipProvider.ANNOTATES
						|| name == AsmRelationshipProvider.INTER_TYPE_DECLARES
						|| name == AsmRelationshipProvider.MATCHES_DECLARE);
		}
		private boolean isAspectualAffectRelation(IRelationship relation) {
			String name = relation.getName();

			return (name == AsmRelationshipProvider.ADVISED_BY
						|| name == AsmRelationshipProvider.DECLAREDY_BY
						|| name == AsmRelationshipProvider.ANNOTATED_BY
						|| name == AsmRelationshipProvider.INTER_TYPE_DECLARED_BY
						|| name == AsmRelationshipProvider.MATCHED_BY);
		}

	}
}

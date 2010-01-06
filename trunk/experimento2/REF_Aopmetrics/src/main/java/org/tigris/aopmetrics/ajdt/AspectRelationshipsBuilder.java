package org.tigris.aopmetrics.ajdt;

import java.util.List;

import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.tigris.aopmetrics.source.Member;
import org.tigris.aopmetrics.source.MetricsSourceVisitor;
import org.tigris.aopmetrics.source.MetricsSourceWithAST;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * Adds relationships information to source metric hierarchy. 
 * This must be a separate phase of build process, since
 * ASM relationship map is collected while weaving.
 * 
 * <p>Builder collects also all join points within given program element.
 *  For example, a method contains also relationships of all join points, which
 *  are lies in the method.
 *  
 * <p>Builder collects also data for Project.getSourceUsingHandle method.
 *  It collects all source handles (i.e. path/pkg/ModelCoverage.java|59|0|893)
 *  of program element and their join points.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: AspectRelationshipsBuilder.java,v 1.1 2006/04/20 23:37:20 misto Exp $
 */
public class AspectRelationshipsBuilder extends MetricsSourceVisitor {
	private Project project;

	public void visit(Project project) {
		this.project = project;
	}
	public void visit(Type type, Package pkg) {
		addRelationshipsToSource(type, type.getASM());
		super.visit(type, pkg);
	}
	public void visit(Member member, Type type) {
		addRelationshipsToSource(member, member.getASM());
		super.visit(member, type);
	}

	private void addRelationshipsToSource(MetricsSourceWithAST source, IProgramElement element) {
		List relations = AsmManager.getDefault().getRelationshipMap().get(element);
		if (relations != null) {
			for(Object relation : relations){
				source.addAspectRelation((IRelationship) relation);
			}
		}

		List children = element.getChildren();
		if (children != null){
			for(Object o : children){
				IProgramElement child = (IProgramElement)o;

				/* add relationships of children, only when there are code elements
				 * (like method call, exception handler etc)  */
				if (child.getKind() == IProgramElement.Kind.CODE) {
					addRelationshipsToSource(source, child);
				}
			}
		}

		this.project.addMetricsSourceHandle(element.getHandleIdentifier(), source);
	}
}

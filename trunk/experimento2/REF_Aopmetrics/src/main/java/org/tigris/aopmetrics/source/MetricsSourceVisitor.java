package org.tigris.aopmetrics.source;

/**
 * Implementacja wzorca projektowego Visitor dla hierarchii MetricsSource.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: MetricsSourceVisitor.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
abstract public class MetricsSourceVisitor {
	public void visit(Member member, Type type) {}
	public void endVisit(Member member, Type type) {}

	public void visit(Type type, Package pkg) {}
	public void endVisit(Type type, Package pkg) {}

	public void visit(Package pkg, Project project) {}
	public void endVisit(Package pkg, Project project) {}
	
	public void visit(Project project) {}
	public 	void endVisit(Project project) {}

}

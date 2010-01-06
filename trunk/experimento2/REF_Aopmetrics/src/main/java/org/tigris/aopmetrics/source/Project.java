package org.tigris.aopmetrics.source;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.tigris.aopmetrics.Logger;

/**
 * MetricsSource representation for project. This is the root of whole hierarchy.
 * Contains all packages. 
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: Project.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
public class Project extends MetricsSource {
	private Map<String,Package> packages = new HashMap<String,Package>();
	private Map<String,MetricsSourceWithAST> sourceHandles = new HashMap<String,MetricsSourceWithAST>();

	public Project(String name) {
		super(name);
		super.setKind(Kind.PROJECT);
	}

	public void addPackage(Package pack) {
		packages.put(pack.getName(), pack);
		pack.setParent(this);
	}
	public Package getPackage(String name) {
		return packages.get(name);
	}
	public Collection<Package> getPackages() {
		return packages.values();
	}
	
	/** Gets metrics source using source handle (from ASM).
	 *  Very useful when searching for targets of relationships.
	 */
	public MetricsSourceWithAST getSourceUsingHandle(String sourceHandle){
		return this.sourceHandles.get(sourceHandle);
	}
	public void addMetricsSourceHandle(String sourceHandle, MetricsSourceWithAST source){
		Logger.debug("add source handle: " + sourceHandle);
		this.sourceHandles.put(sourceHandle, source);
	}
	
	public void traverse(MetricsSourceVisitor visitor) {
		visitor.visit(this);
		for(Package p : packages.values()){
			p.traverse(visitor, this);
		}
		visitor.endVisit(this);
	}
	

	public String printHierarchy() {
		ToStringVisitor visitor = new ToStringVisitor();
		this.traverse(visitor);
		return visitor.getResult();
	}

	public static class ToStringVisitor extends MetricsSourceVisitor {
		private StringBuffer buf = new StringBuffer();
		
		public StringBuffer getBuffer(){
			return this.buf;
		}
		public String getResult(){
			return this.buf.toString();
		}

		public void visit(Project project) {
			buf.append(project.getName()).append("\n");
			super.visit(project);
		}
		public void visit(Package pkg, Project project) {
			buf.append("\t").append(pkg.getName()).append("\n");
			super.visit(pkg, project);
		}
		public void visit(Type type, Package pkg) {
			buf.append("\t\t").append(type.getName())
				.append(type.getAspectRelations()).append("\n");
			super.visit(type, pkg);
		}
		public void visit(Member member, Type type) {
			buf.append("\t\t\t").append(member.getName())
				.append(member.getAspectRelations()).append("\n");
			super.visit(member, type);
		}
	}
}

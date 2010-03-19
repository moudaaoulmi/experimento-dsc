package org.tigris.aopmetrics.source;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tigris.aopmetrics.source.Project.ToStringVisitor;


/**
 * MetricSource node that represents package. 
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: Package.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
public class Package extends MetricsSource {
	private Map<String, Type> types = new HashMap<String, Type>();

	public Package(String name) {
		super(name);
		setKind(Kind.PACKAGE);
	}

	public void addType(Type type) {
		types.put(type.getName(), type);
		type.setParent(this);
	}
	public Type getType(String typeName) {
		return types.get(typeName);
	}
	public Collection<Type> getTypes() {
		return types.values();
	}
	

	public String printHierarchy() {
		ToStringVisitor visitor = new ToStringVisitor();
		traverse(visitor, null);
		return visitor.getResult();
	}

	public void traverse(MetricsSourceVisitor visitor, Project project) {
		visitor.visit(this, project);
		for(Type type : types.values()){
			type.traverse(visitor, this);
		}
		visitor.endVisit(this, project);
	}
}

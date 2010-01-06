package org.tigris.aopmetrics.source;

/**
 * @author misto
 */
abstract public class MetricsSource {
	private String name;
	private Kind kind;
	private MetricsSource parent;
	
	public MetricsSource(String name) {
		this.name = name;
	}
	
	protected void setName(String name) { this.name = name; }
	public String getName() {
		return name;
	}

	protected void setKind(Kind kind) { this.kind = kind; }
	public Kind getKind() {
		return kind;
	}

	protected void setParent(MetricsSource parent) { this.parent = parent; }
	public MetricsSource getParent() {
		return parent;
	}
	
	public String toString(){
		return this.name;
	}

	public enum Kind {
		PROJECT, PACKAGE,
		CLASS, ASPECT, ENUM, INTERFACE,

		ENUM_VALUE, ANNOTATION,
		INITIALIZER,
		INTER_TYPE_FIELD, INTER_TYPE_METHOD, INTER_TYPE_CONSTRUCTOR, INTER_TYPE_PARENT,
		CONSTRUCTOR, METHOD, FIELD,
		POINTCUT, ADVICE,
		DECLARE_PARENTS, DECLARE_WARNING, DECLARE_ERROR, DECLARE_SOFT, DECLARE_PRECEDENCE;
		
		public boolean isMember() {
			return this == FIELD
			|| this == METHOD
			|| this == CONSTRUCTOR
			|| this == POINTCUT
			|| this == ADVICE
			|| this == ENUM_VALUE;
		}
		
		public boolean isInterTypeMember() {
			return this == INTER_TYPE_CONSTRUCTOR
			|| this == INTER_TYPE_FIELD
			|| this == INTER_TYPE_METHOD;
		}
		
		public boolean isType() {
			return this == CLASS
			|| this == INTERFACE
			|| this == ASPECT
			|| this == ANNOTATION
			|| this == ENUM;
		}
	};
}

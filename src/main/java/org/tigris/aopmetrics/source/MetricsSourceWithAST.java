package org.tigris.aopmetrics.source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;

/**
 * Metrics source with a reference to its internal AJDT AST (TypeDeclaration or MethodDeclaration)
 * and to ASM node (IProgramElement).
 *
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: MetricsSourceWithAST.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
public class MetricsSourceWithAST extends MetricsSource {
	private ASTNode ast;
	private IProgramElement asm;
	private List<IRelationship> aspectRelations = new ArrayList<IRelationship>();
	
	public MetricsSourceWithAST(String name, ASTNode ast) {
		super(name);
		this.ast = ast;
	}


	public void setAST(ASTNode type) { this.ast = type; }
	public ASTNode getAST() {
		return this.ast;
	}

	public void setASM(IProgramElement asm) {
		this.asm = asm; 
		setKind(convertProgramElementKind(asm));
	}
	
	static private Kind convertProgramElementKind(IProgramElement asm) {
		IProgramElement.Kind kind = asm.getKind();
		if (kind == IProgramElement.Kind.ADVICE)
			return Kind.ADVICE;
		else if (kind == IProgramElement.Kind.CLASS)
			return Kind.CLASS;
		else if (kind == IProgramElement.Kind.ASPECT)
			return Kind.ASPECT;
		else if (kind == IProgramElement.Kind.ENUM)
			return Kind.ENUM;
		else if (kind == IProgramElement.Kind.INTERFACE)
			return Kind.INTERFACE;
		else if (kind == IProgramElement.Kind.ENUM_VALUE)
			return Kind.ENUM_VALUE;
		else if (kind == IProgramElement.Kind.ANNOTATION)
			return Kind.ANNOTATION;
		else if (kind == IProgramElement.Kind.INITIALIZER)
			return Kind.INITIALIZER;
		else if (kind == IProgramElement.Kind.INTER_TYPE_FIELD)
			return Kind.INTER_TYPE_FIELD;
		else if (kind == IProgramElement.Kind.INTER_TYPE_METHOD)
			return Kind.INTER_TYPE_METHOD;
		else if (kind == IProgramElement.Kind.INTER_TYPE_CONSTRUCTOR)
			return Kind.INTER_TYPE_CONSTRUCTOR;
		else if (kind == IProgramElement.Kind.INTER_TYPE_PARENT)
			return Kind.INTER_TYPE_PARENT;
		else if (kind == IProgramElement.Kind.CONSTRUCTOR)
			return Kind.CONSTRUCTOR;
		else if (kind == IProgramElement.Kind.METHOD)
			return Kind.METHOD;
		else if (kind == IProgramElement.Kind.FIELD)
			return Kind.FIELD;
		else if (kind == IProgramElement.Kind.POINTCUT)
			return Kind.POINTCUT;
		else if (kind == IProgramElement.Kind.ADVICE)
			return Kind.ADVICE;
		else if (kind == IProgramElement.Kind.DECLARE_PARENTS)
			return Kind.DECLARE_PARENTS;
		else if (kind == IProgramElement.Kind.DECLARE_WARNING)
			return Kind.DECLARE_WARNING;
		else if (kind == IProgramElement.Kind.DECLARE_ERROR)
			return Kind.DECLARE_ERROR;
		else if (kind == IProgramElement.Kind.DECLARE_SOFT)
			return Kind.DECLARE_SOFT;
		else if (kind == IProgramElement.Kind.DECLARE_PRECEDENCE)
			return Kind.DECLARE_PRECEDENCE;
		else
			return null;
	}


	public IProgramElement getASM() {
		return this.asm;
	}

	public void addAspectRelation(IRelationship relation) { 
		this.aspectRelations.add(relation);
	}
	public Collection<IRelationship> getAspectRelations() {
		return this.aspectRelations;
	}
}

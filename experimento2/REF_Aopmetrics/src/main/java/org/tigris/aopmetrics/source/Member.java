package org.tigris.aopmetrics.source;

import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;

/**
 *
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: Member.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
public class Member extends MetricsSourceWithAST {
	public Member(String name, ASTNode decl) {
		super(name, decl);
	}

	public void traverse(MetricsSourceVisitor visitor, Type type) {
		visitor.visit(this, type);
		visitor.endVisit(this, type);
	}
}

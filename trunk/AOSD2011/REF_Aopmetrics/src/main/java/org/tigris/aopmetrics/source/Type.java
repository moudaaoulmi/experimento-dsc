package org.tigris.aopmetrics.source;

import java.util.ArrayList;
import java.util.Collection;

import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.tigris.aopmetrics.source.Project.ToStringVisitor;


/**
 * Metrics source representation for type. Contains reference to its internal
 * AJDT AST (TypeDeclaration) and to ASM node (IProgramElement).
 *
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: Type.java,v 1.1 2006/04/20 23:37:24 misto Exp $
 */
public class Type extends MetricsSourceWithAST {
	private Collection<Member> members = new ArrayList<Member>();
	
	public Type(String name, TypeDeclaration ast) {
		super(name, ast);
	}

	public TypeDeclaration getTypeDeclaration() {
		return (TypeDeclaration)this.getAST();
	}

	public void addMember(Member member) {
		members.add(member);
		member.setParent(this);
	}
	public Collection<Member> getMembers() {
		return members;
	}

	public String printHierarchy() {
		ToStringVisitor visitor = new ToStringVisitor();
		traverse(visitor, null);
		return visitor.getResult();
	}

	public void traverse(MetricsSourceVisitor visitor, Package pkg) {
		visitor.visit(this, pkg);

		for(Member member : members){
			member.traverse(visitor, this);
		}
		
		visitor.endVisit(this, pkg);
	}
}

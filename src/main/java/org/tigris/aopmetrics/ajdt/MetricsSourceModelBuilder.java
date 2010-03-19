package org.tigris.aopmetrics.ajdt;

import java.util.Iterator;
import java.util.Stack;

import org.aspectj.ajdt.internal.core.builder.AsmHierarchyBuilder;
import org.aspectj.asm.IProgramElement;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.tigris.aopmetrics.source.Member;
import org.tigris.aopmetrics.source.Package;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * Builds MetricsSource hierachy. Connects AST and ASM nodes, and stores
 * their references in proper MetricsSource node. 
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: MetricsSourceModelBuilder.java,v 1.1 2006/04/20 23:37:20 misto Exp $
 */
public class MetricsSourceModelBuilder extends AsmHierarchyBuilder {
	private Project project;
	private Stack<Type> metricsSourceStack = new Stack<Type>();
	private Stack<Integer> anonymousCount = new Stack<Integer>();

	public MetricsSourceModelBuilder(String projectName) {
		this.project = new Project(projectName);
	}
	
	public Project getProject() {
		return this.project;
	}
	
	private void addType(TypeDeclaration decl) {
		Package p = findPackage(decl);
		
		String typeName = new String(decl.name);
		addType(p, typeName, decl);
	}

	private void addAnonymousType(TypeDeclaration decl, BlockScope scope) {
		Type enclosingType = metricsSourceStack.peek();
		Package pkg = (Package)enclosingType.getParent();

		int count = anonymousCount.pop() + 1;
		anonymousCount.push(count);
		String typeName = enclosingType.getName() + "$" + count;

		addType(pkg, typeName, decl);
	}

	private void addInnerType(TypeDeclaration decl) {
		Package p = findPackage(decl);

		String constantPoolName = new String(decl.binding.constantPoolName());
		String typeName = constantPoolName.substring(constantPoolName.lastIndexOf('/') + 1);

		addType(p, typeName, decl);
	}

	private void addType(Package p, String typeName, TypeDeclaration decl) {
		if (p.getType(typeName) != null) {
			throw new RuntimeException("duplicate type: " + typeName);
		}

		Type type = new Type(typeName, decl);
		p.addType(type);
		
		/* having last processed type on top of the stack, we can add members to it */
		metricsSourceStack.push(type);
		anonymousCount.push(0);
	}

	private Package findPackage(TypeDeclaration decl) {
		String packageName = new String(decl.scope.getCurrentPackage().readableName());
		Package p = project.getPackage(packageName);
		if (p == null) {
			p = new Package(packageName);
			project.addPackage(p);
		}
		return p;
	}

	private void setTypeASM() {
		Type type = metricsSourceStack.pop();

		/* storing ASM element of the type (which is known after superclass visit call) */
		IProgramElement asm = (IProgramElement)super.stack.peek();
		type.setASM(asm);
		
		anonymousCount.pop();
	}
	

	public boolean visit(TypeDeclaration decl, BlockScope scope) {
		addAnonymousType(decl, scope);
		return super.visit(decl, scope);
	}
	public boolean visit(TypeDeclaration decl, ClassScope scope) {
		addInnerType(decl);
		return super.visit(decl, scope);
	}
	public boolean visit(TypeDeclaration decl, CompilationUnitScope scope) {
		addType(decl);
		return super.visit(decl, scope);
	}

	public void endVisit(TypeDeclaration decl, BlockScope scope) {
		setTypeASM();
		super.endVisit(decl, scope);
	}
	public void endVisit(TypeDeclaration decl, ClassScope scope) {
		setTypeASM();
		super.endVisit(decl, scope);
	}
	public void endVisit(TypeDeclaration decl, CompilationUnitScope scope) {
		setTypeASM();
		super.endVisit(decl, scope);
	}


	public void endVisit(ConstructorDeclaration decl, ClassScope scope) {
		addMember(decl);
		super.endVisit(decl, scope);
	}
	public void endVisit(FieldDeclaration decl, MethodScope scope) {
		addMember(decl);
		super.endVisit(decl, scope);
	}
	public void endVisit(MethodDeclaration decl, ClassScope scope) {
		addMember(decl);
		super.endVisit(decl, scope);
	}
	private void addMember(ASTNode  decl) {
		IProgramElement memberASM = (IProgramElement)super.stack.peek();
		
		// member ASM == null, when default constructor is processed
		if (memberASM != null) {
			Member member = new Member(generateMemberName(memberASM), decl);
			member.setASM(memberASM);

			Type enclosingType = metricsSourceStack.peek();
			enclosingType.addMember(member);
		}
	}

	private String generateMemberName(IProgramElement memberASM) {
		StringBuffer name = new StringBuffer();
		name.append(memberASM.getName());
		if (memberASM.getParameterTypes() != null){
			name.append("(");
			for(Iterator parTypes = memberASM.getParameterTypes().iterator() ; parTypes.hasNext();){
				name.append(parTypes.next());
				if (parTypes.hasNext())
					name.append(",");
			}
			name.append(")");
		}
		
		if (memberASM.getKind().isDeclare()
				|| memberASM.getKind().equals(IProgramElement.Kind.ADVICE)){
			name.append(" : ").append(memberASM.getDetails());
		}
		
		return name.toString();
	}

/*
	private void printASMDebugInfo(IProgramElement memberASM) {
		System.out.println("-------------------------------");
		System.out.println("name: " + memberASM.getName());
		System.out.println("bytecode name: " + memberASM.getBytecodeName());
		System.out.println("bytecode sig: " + memberASM.getBytecodeSignature());
		System.out.println("source sig: " + memberASM.getSourceSignature());
		System.out.println("details: " + memberASM.getDetails());
		System.out.println("formal comment: " + memberASM.getFormalComment());
		System.out.println("handle id: " + memberASM.getHandleIdentifier());
		System.out.println("types: " + memberASM.getParameterTypes());
		System.out.println("msg: " + memberASM.getMessage());
		System.out.println("xtra info: " + memberASM.getExtraInfo());
		System.out.println("corresp type: " + memberASM.getCorrespondingType());
		System.out.println("decl type: " + memberASM.getDeclaringType());
		System.out.println("kind: " + memberASM.getKind());
		System.out.println("src loc: " + memberASM.getSourceLocation());
		System.out.println("is declare: " + memberASM.getKind().isDeclare());
	}
	
	private void printDebugInfo(TypeDeclaration decl,  SourceTypeBinding binding) {
		System.out.println("----------------------------------");
		System.out.println("decl: " + decl);
		System.out.println("decl.name: " + (decl.name == null ? "null" : new String(decl.name)));
		System.out.println("constantPoolName: " + (binding.constantPoolName() == null ? "null" : new String(binding.constantPoolName())));
		System.out.println("compoundName: " + binding.debugName());
		System.out.println("sourceName: " + (binding.sourceName == null ? "null" : new String(binding.sourceName)));
		System.out.println("sourceName(): " + (binding.sourceName() == null ? "null" : new String(binding.sourceName())));
		System.out.println("signature(): " + (binding.signature() == null ? "null" : new String(binding.signature())));
		System.out.println("shortReadableName(): " + (binding.shortReadableName() == null ? "null" : new String(binding.shortReadableName())));
		System.out.println("readableName(): " + (binding.readableName() == null ? "null" : new String(binding.readableName())));
		System.out.println("qualifiedSourceName(): " + (binding.qualifiedSourceName() == null ? "null" : new String(binding.qualifiedSourceName())));
		System.out.println("getFileName(): " + (binding.getFileName() == null ? "null" : new String(binding.getFileName())));
	}*/
}

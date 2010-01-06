package org.tigris.aopmetrics.metrics;

import org.aspectj.ajdt.internal.compiler.ast.InterTypeFieldDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ExtendedStringLiteral;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;
import org.tigris.aopmetrics.source.Type;


/**
 * Counts 'perfect' lines of class code. The calculator processes
 * the internal string representation of AJDT/JDT compiler. The result
 * is independent from orignal code styling etc.
 * 
 * <p>Rules: <ul>
 *  <li>Class header is counted as one line.</li>
 *  <li>Imports and all comments are not counted.</li>
 *  <li>Methods header is counted as one line.</li>
 *  <li>Every statement is in a single line.</li>
 *  <li>Newline is not created for the opening curly braces</li>
 *  <li>Newline is created for the opening curly braces</li>
 *  <li>String constants are counted as a single line (even when contains new lines)</li>
 * </ul>
 * <p>Disadvantages which are corrected by Visitor: <ul>
 *   <li>Default constructor (3 lines of code) is added when it doesn't exist.</li>
 *   <li>Newlines in string constants are also counted as a line.</li>
 *   <li>Default static constructor (class init) (3 lines of code) is added to every *aspect*.</li>
 *   <li>Field introductions are changed to methods (3 lines of code instead of 1).</li>
 * </ul>
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: LinesOfClassCode.java,v 1.1 2006/04/20 23:37:23 misto Exp $
 */
public class LinesOfClassCode implements MetricsCalculator {

	public Measurement[] calculate(MetricsSource source, Project project) {
		CalculatorUtils.assertSourceIsAType(source);

		int result = countLinesOfSource((Type)source);
		return new Measurement[] { new Measurement("LOCC", result) };
	}

	private int countLinesOfSource(Type typeSrc) {
		int result = countLinesOfSource(typeSrc.getAST());
		result += countResultCorrection(typeSrc);

		return result;
	}

	private int countLinesOfSource(ASTNode node) {
		StringBuffer buf = new StringBuffer();
		node.print(0, buf);

		return countNewlines(buf.toString());
	}

	static private int countNewlines(String perfectSource) {
		char[] chars = perfectSource.toCharArray();
		int result = 0;
		for(char c : chars)
			if (c == '\n') result ++;
		
		// last line is without '\n'
		result++;

		return result;
	}

	private int countResultCorrection(Type source) {
		CorrectorVisitor corrector = new CorrectorVisitor();
		source.getTypeDeclaration().traverse(corrector, (CompilationUnitScope)null);
		return  corrector.getValue();
	}
	
	static class CorrectorVisitor extends ASTVisitor {
		private int value;
		public int getValue() {
			return this.value;
		}

		/**
		 * cl_init block is add to a class or an aspect, if it contains static fields.
		 * cl_init block has 2 lines. 
		 */
		public boolean visit(Clinit decl, ClassScope scope) {
			if (decl.isClinit())
				value -= 2;

			return super.visit(decl, scope);
		}

		/**
		 * Default constructor is added when class doesn't contain any constructor. 
		 * Default constructor has 3 lines.
		 */
		public boolean visit(ConstructorDeclaration decl, ClassScope scope) {
			if (decl.isDefaultConstructor()) 
				value -= 3;

			return super.visit(decl, scope);
		}

		/**
		 * When extended string (sum of string literals) is found, count it
		 * as a single line (like other strings).    
		 */
		public boolean visit(ExtendedStringLiteral decl, BlockScope scope) {
			int newlines = countNewlines(decl.printExpression(0, new StringBuffer()).toString());
			value -= newlines - 1;
			return super.visit(decl, scope);
		}

		/** When inter-type field introduction found in an aspect,
		 *  then increase correction value by two (method has 3 lines,
		 *  field introduction has 1 line).
		 */
		public boolean visit(MethodDeclaration decl, ClassScope scope) {
			if (decl instanceof InterTypeFieldDeclaration) 
				value -= 2;

			return super.visit(decl, scope);
		}
		
	}
}

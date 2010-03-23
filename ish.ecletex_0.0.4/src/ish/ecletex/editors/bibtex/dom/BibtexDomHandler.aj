package ish.ecletex.editors.bibtex.dom;

import java.io.PrintWriter;

public privileged aspect BibtexDomHandler {

	pointcut printBibtexHandler(): execution(public void BibtexString.printBibtex(PrintWriter));

	void around(BibtexString bib, PrintWriter writer): printBibtexHandler() && this(bib)&& args(writer) {
		// is this really a number?
		try {
			proceed(bib, writer);
		} catch (NumberFormatException nfe) {
			writer.print('{');
			// for (int begin = 0; begin < content.length();) {
			// int end = content.indexOf('\n', begin);
			// if (end < 0) {
			// if (begin > 0)
			// writer.print(content.substring(begin, content.length()));
			// else
			// writer.print(content);
			//
			// break;
			// }
			// writer.println(content.substring(begin, end));
			// writer.print("\t\t");
			// begin = end + 1;
			// }
			writer.print(bib.content);
			writer.print('}');
		}
	}

}

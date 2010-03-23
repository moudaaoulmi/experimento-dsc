/*
 * Created on 18-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders;

import ish.ecletex.builders.latexlogparser.Model;
import ish.ecletex.builders.latexlogparser.Parser;
import ish.ecletex.utils.SafeExec;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class ecletexLatexBuildManager {

	private String ARGUMENTS = "-quiet";
	private String LINUX_ARGUMENT = "-interaction=batchmode";
	private String FULL_TARGET;
	private String TEX_BIN;
	private String PROJECT_DIR;

	public ecletexLatexBuildManager(String fullTarget, String texBin,
			String fullProjectDirectory) {
		this.FULL_TARGET = fullTarget;
		this.TEX_BIN = texBin;
		this.PROJECT_DIR = fullProjectDirectory;
	}

	private Model buildLatex() {
		// System.out.println("Building Latex...");
		String command = "";

		IPath latexCommandWin32 = new Path(TEX_BIN);
		latexCommandWin32 = latexCommandWin32.append("latex.exe");

		IPath latexCommanLinux = new Path(TEX_BIN);
		latexCommanLinux = latexCommanLinux.append("latex");

		if (latexCommandWin32.toFile().exists()) {
			command += latexCommandWin32.toFile().toString() + " ";
			command += ARGUMENTS + " ";
			command += "\"" + FULL_TARGET + "\"";
		} else {
			command += latexCommanLinux.toFile().toString() + " ";
			command += LINUX_ARGUMENT + " ";
			command += FULL_TARGET;
		}

		// System.out.println("Building Latex: "+command);

		return internalBuildLatex(command);
		// return null;
	}

	private Model internalBuildLatex(String command) {
		SafeExec exec = new SafeExec(command, new String[0], new File(
				PROJECT_DIR));
		exec.start();
		exec.WaitFor();

		IPath log = new Path(FULL_TARGET).removeFileExtension();
		log = log.addFileExtension("log");
		// System.out.println("Parsing Logfile: "+log.toString());
		Parser parser = new Parser(log.toString());
		parser.parse();
		Model model = parser.getModel();
		return model;
		// System.out.println(model.toString());
		// Process p = Runtime.getRuntime().exec(command,new String[0],new
		// File(PROJECT_DIR));
		// p.waitFor();
	}

	public Model build() {
		return buildLatex();
	}
}

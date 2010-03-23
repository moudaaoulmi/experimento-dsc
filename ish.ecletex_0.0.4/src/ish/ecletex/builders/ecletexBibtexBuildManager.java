/*
 * Created on 20-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders;

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
public class ecletexBibtexBuildManager {
	private String FULL_TARGET;
	private String TEX_BIN;
	private String PROJECT_DIR;
	private String ARGUMENTS = "-quiet -include-directory=";
	private String LINUX_ARGUMENTS = "";
	private boolean AUX_EXISTS = false;

	public ecletexBibtexBuildManager(String fullTarget, String texBin,
			String fullProjectDirectory) {
		IPath target = new Path(fullTarget);
		target = target.removeFileExtension();
		this.FULL_TARGET = target.toString();
		this.TEX_BIN = texBin;
		this.PROJECT_DIR = fullProjectDirectory;

		IPath aux = target.addFileExtension("aux");
		if (aux.toFile().exists())
			AUX_EXISTS = true;

	}

	private TeXBuildEvent[] buildBibtex() {
		if (AUX_EXISTS) {
			System.out.println("Building Bibtex...");
			String command = "";

			IPath latexCommandWin32 = new Path(TEX_BIN);
			latexCommandWin32 = latexCommandWin32.append("bibtex.exe");

			IPath latexCommanLinux = new Path(TEX_BIN);
			latexCommanLinux = latexCommanLinux.append("bibtex");

			if (latexCommandWin32.toFile().exists()) {
				command += "\"" + latexCommandWin32.toFile().toString() + "\" ";
				command += ARGUMENTS
						+ "\""
						+ new Path(PROJECT_DIR).addTrailingSeparator()
								.toString() + "\" ";
			} else {
				command += latexCommanLinux.toFile().toString() + " ";
				command += LINUX_ARGUMENTS + " ";
			}
			command += "\"" + FULL_TARGET + "\"";

			System.out.println("Building Bibtex: " + command);

			internalBuildBibtex(command);

		} else {
			System.out.println("AUX file doesn't exisit not building bibtex.");
		}
		return null;
	}

	private void internalBuildBibtex(String command) {
		// Process p = Runtime.getRuntime().exec(command,new String[0],new
		// File(PROJECT_DIR));
		// int exitvalue = p.exitValue();
		// p.waitFor();
		SafeExec exec = new SafeExec(command, new String[0], new File(
				PROJECT_DIR));
		exec.start();
		exec.WaitFor();
	}

	public TeXBuildEvent[] build() {
		return buildBibtex();
	}
}

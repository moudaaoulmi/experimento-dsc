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
public class ecletexPDFBuildManager {
	private String ARGUMENTS = "-q -dSAFER -dNOPAUSE -sDEVICE=pdfwrite -dBATCH -sOutputFile=";
	private String FULL_TARGET;
	private String TARGET_PDF;
	private String GS_BIN;

	private String PROJECT_DIR;
	private IPath relativeTarget;
	private IPath relativePDF;

	public ecletexPDFBuildManager(String fullTarget, String gsBin,
			String fullProjectDirectory) {
		IPath target = new Path(fullTarget);
		IPath project = new Path(fullProjectDirectory);
		target = target.removeFileExtension();
		IPath pdfTarget = target.addFileExtension("pdf");
		target = target.addFileExtension("ps");
		this.FULL_TARGET = target.toString();
		this.TARGET_PDF = pdfTarget.toString();
		this.GS_BIN = gsBin;
		this.PROJECT_DIR = project.addTrailingSeparator().toString();
		relativeTarget = getRelative(project, target);
		relativePDF = getRelative(project, pdfTarget);
	}

	private IPath getRelative(IPath project, IPath target) {
		int matching = target.matchingFirstSegments(project);
		IPath rel = new Path("");
		for (int i = matching; i < target.segmentCount(); i++)
			rel = rel.append(target.segment(i));

		System.out.println("Project: " + project.toString());
		System.out.println("Target:  " + target.toString());
		System.out.println("Relative:" + rel.toString());
		return rel;
	}

	private TeXBuildEvent[] buildPDF() {
		// System.out.println("Building PDF...");
		String command = "";
		String path = null;

		IPath latexCommandWin32 = new Path(GS_BIN);
		latexCommandWin32 = latexCommandWin32.append("gswin32c.exe");

		IPath latexCommanLinux = new Path(GS_BIN);
		latexCommanLinux = latexCommanLinux.append("gs");

		if (latexCommandWin32.toFile().exists()) {
			command += latexCommandWin32.toFile().toString() + " ";
			// command = "gswin32c.exe ";
			path = "PATH=\"" + GS_BIN + "\"";
		} else {
			command += latexCommanLinux.toFile().toString() + " ";
			// command = "gs ";
		}

		command += ARGUMENTS + relativePDF.toString() + " "
				+ relativeTarget.toString();

		// System.out.println("Building PDF: " + command);
		// System.out.println("Environment: "+path);

		return internalBuildPDF(command, path);

	}

	private TeXBuildEvent[] internalBuildPDF(String command, String path) {
		if (path == null) {

			// Process p =
			// Runtime.getRuntime().exec(
			// command,
			// new String[0],
			// new File(PROJECT_DIR));
			// p.waitFor();

			SafeExec exec = new SafeExec(command, new String[0], new File(
					PROJECT_DIR));
			exec.start();
			exec.WaitFor();

		} else {
			// Process p = Runtime.getRuntime().exec(command,new
			// String[]{path},new File(PROJECT_DIR));
			// p.waitFor();

			SafeExec exec = new SafeExec(command, new String[] { path },
					new File(PROJECT_DIR));
			exec.start();
			exec.WaitFor();
		}
		return null;
	}

	public TeXBuildEvent[] build() {
		return buildPDF();
	}

}

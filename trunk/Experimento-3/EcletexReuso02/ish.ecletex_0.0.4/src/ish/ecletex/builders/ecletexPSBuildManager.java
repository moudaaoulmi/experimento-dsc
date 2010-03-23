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
public class ecletexPSBuildManager {
	private String FULL_TARGET;
	private String TEX_BIN;
	private String PROJECT_DIR;
	private String ARGUMENTS = "";
	private IPath relativeTarget;

	public ecletexPSBuildManager(String fullTarget, String texBin,
			String fullProjectDirectory) {
		IPath target = new Path(fullTarget);
		IPath project = new Path(fullProjectDirectory);
		target = target.removeFileExtension();
		target = target.addFileExtension("dvi");
		this.FULL_TARGET = target.toString();
		this.TEX_BIN = texBin;
		this.PROJECT_DIR = fullProjectDirectory;
		relativeTarget = getRelative(project, target);
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

	private TeXBuildEvent[] buildPS() {
		System.out.println("Building PS...");
		String command = "";
		String path = null;

		IPath latexCommandWin32 = new Path(TEX_BIN);
		latexCommandWin32 = latexCommandWin32.append("dvips.exe");

		IPath latexCommanLinux = new Path(TEX_BIN);
		latexCommanLinux = latexCommanLinux.append("dvips");

		if (latexCommandWin32.toFile().exists()) {
			command += latexCommandWin32.toFile().toString() + " ";
			// command = "dvips.exe ";
			path = "PATH=\"" + TEX_BIN + "\"";
		} else {
			command += latexCommanLinux.toFile().toString() + " ";
			command = "dvips ";
		}

		command += ARGUMENTS + " " + relativeTarget.toString();

		System.out.println("Building PS: " + command);

		return internalBuildPS(command, path);
	}

	private TeXBuildEvent[] internalBuildPS(String command, String path) {
		if (path == null) {
			// Process p = Runtime.getRuntime().exec(command,new String[0],new
			// File(PROJECT_DIR));
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
		return buildPS();
	}
}

/*
 * Created on 17-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.utils;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexExternalToolsPreferencePage;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class GhostScript {

	private static String GS_COMMMAND = null;
	private static String GS_PATH = null;

	private static void setGSCommand() {

		if (GS_COMMMAND == null) {
			String GS_BIN = ecletexPlugin.getDefault().getPreferenceStore()
					.getString(TexExternalToolsPreferencePage.GS_BIN_DIR);
			IPath latexCommandWin32 = new Path(GS_BIN);
			latexCommandWin32 = latexCommandWin32.append("gswin32c.exe");

			IPath latexCommanLinux = new Path(GS_BIN);
			latexCommanLinux = latexCommanLinux.append("gs");

			if (latexCommandWin32.toFile().exists()) {
				GS_COMMMAND = latexCommandWin32.toFile().toString() + " ";
				GS_PATH = "PATH=\"" + GS_BIN + "\"";
			} else {
				GS_COMMMAND = latexCommanLinux.toFile().toString() + " ";
			}
		}
	}

	public static boolean ConvertToPng(String PSFile, String TargetDirectory) {

		IPath file = new Path(new Path(PSFile).lastSegment());
		file = file.removeFileExtension();
		file = file.addFileExtension("png");
		IPath target = (new Path(TargetDirectory)).append(file);
		IPath psfile = new Path(PSFile);

		String arguments = "-sDEVICE=png16m -r72 -sOutputFile="
				+ target.toOSString() + " " + psfile.toOSString();
		RunCommand(arguments);

		if (target.toFile().exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static Rectangle getBoundingBox(String PSFile) {
		String arguments = "-sDEVICE=bbox " + PSFile;
		String output = RunCommand(arguments);
		// System.out.println("GSOut: "+output);
		String[] lines = output.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("%%BoundingBox:")) {
				String bbline = lines[i].substring(15, lines[i].length());
				System.out.println(bbline);
				String[] nums = bbline.split(" ");
				Rectangle r = new Rectangle(Integer.parseInt(nums[0]), Integer
						.parseInt(nums[1]), Integer.parseInt(nums[2]), Integer
						.parseInt(nums[3]));
				System.out.println("BBOX: " + r.toString());
				return r;
			}
		}
		return null;
	}

	private static String RunCommand(String arguments) {
		setGSCommand();
		String command = GS_COMMMAND + " -q -dNOPAUSE -dBATCH " + arguments;
		return internalRunCommand(command);
	}

	private static String internalRunCommand(String command) {
		if (GS_PATH == null) {
			SafeExec exec = new SafeExec(command, new String[0]);
			exec.start();
			exec.WaitFor();
			return exec.getError();

		} else {
			SafeExec exec = new SafeExec(command, new String[] { GS_PATH });
			exec.start();
			exec.WaitFor();
			return exec.getError();
		}
	}

}

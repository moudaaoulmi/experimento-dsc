/*
 * Created on 11-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class SafeExec extends Thread {

	class StreamConsumer extends Thread {
		InputStream is;
		String type;
		OutputStream os;
		String outString = "";

		public StreamConsumer(InputStream is, String type) {
			this(is, type, null);
		}

		public StreamConsumer(InputStream is, String type, OutputStream redirect) {
			this.is = is;
			this.type = type;
			this.os = redirect;
		}

		public String getOutputString() {
			return outString;
		}

		public void run() {
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null)
					pw.println(line);

				// System.out.println(type + ">" + line);
				outString += (line + "\n");
			}

			if (pw != null)
				pw.flush();
		}
	}

	private String command = null;
	private String[] cmdarray = null;
	private String[] envp = null;
	private File dir = null;
	private OutputStream os = null;
	private Process process;

	private boolean ProcessInitFailed = false;

	StreamConsumer errorConsumer;
	StreamConsumer outputConsumer;

	public SafeExec(String command) {
		this.command = command;
	}

	public SafeExec(String command, OutputStream os) {
		this.command = command;
		this.os = os;
	}

	public SafeExec(String[] cmdarray) {
		this.cmdarray = cmdarray;
	}

	public SafeExec(String[] cmdarray, OutputStream os) {
		this.cmdarray = cmdarray;
		this.os = os;

	}

	public SafeExec(String[] cmdarray, String[] envp) {
		this.cmdarray = cmdarray;
		this.envp = envp;

	}

	public SafeExec(String[] cmdarray, String[] envp, OutputStream os) {
		this.cmdarray = cmdarray;
		this.envp = envp;
		this.os = os;
	}

	public SafeExec(String[] cmdarray, String[] envp, File dir) {
		this.cmdarray = cmdarray;
		this.envp = envp;
		this.dir = dir;
	}

	public SafeExec(String[] cmdarray, String[] envp, File dir, OutputStream os) {
		this.cmdarray = cmdarray;
		this.envp = envp;
		this.dir = dir;
		this.os = os;
	}

	public SafeExec(String cmd, String[] envp) {
		this.command = cmd;
		this.envp = envp;
	}

	public SafeExec(String cmd, String[] envp, OutputStream os) {
		this.command = cmd;
		this.envp = envp;
		this.os = os;
	}

	public SafeExec(String command, String[] envp, File dir) {
		this.command = command;
		this.envp = envp;
		this.dir = dir;
	}

	public SafeExec(String command, String[] envp, File dir, OutputStream os) {
		this.command = command;
		this.envp = envp;
		this.dir = dir;
		this.os = os;

	}

	public int WaitFor() {
		while (process == null && !ProcessInitFailed)
			Thread.sleep(20);
		if (process == null)
			return 2;
		else
			return process.waitFor();
	}

	public String getOutput() {
		return outputConsumer.getOutputString();
	}

	public String getError() {
		return errorConsumer.getOutputString();
	}

	public void run() {
		ProcessInitFailed = false;
		Runtime rt = Runtime.getRuntime();

		if ((command != null) && (envp == null)) {
			process = rt.exec(command);
		} else if ((command != null) && (envp != null) && (dir == null)) {
			process = rt.exec(command, envp);
		} else if ((command != null) && (envp != null) && (dir != null)) {
			process = rt.exec(command, envp, dir);
		} else if ((cmdarray != null) && (envp == null)) {
			process = rt.exec(cmdarray);
		} else if ((cmdarray != null) && (envp != null) && (dir == null)) {
			process = rt.exec(cmdarray, envp);
		} else if ((cmdarray != null) && (envp != null) && (dir != null)) {
			process = rt.exec(cmdarray, envp, dir);
		}

		errorConsumer = new StreamConsumer(process.getErrorStream(), "ERROR");
		outputConsumer = new StreamConsumer(process.getInputStream(), "OUTPUT",
				os);

		errorConsumer.start();
		outputConsumer.start();

		// process.waitFor();
		if (os != null) {
			os.flush();
		}
	}

}

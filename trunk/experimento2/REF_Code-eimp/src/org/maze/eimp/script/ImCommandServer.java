/*******************************************************************************
 * Copyright (c) 2003, Loya Liu
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the MAZE.ORG nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *******************************************************************************/

package org.maze.eimp.script;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMAdapter;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;

/**
 * @author hliu
 * 
 * 
 */
public class ImCommandServer {

	Runner jythonRunner;
	Connection conn;
	ICSProcessor evt;
	String passwd = null;

	class RespCmd extends Thread {
		String cmd;
		Session ss = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (ss == null)
				return;

			StringBuffer buf = new StringBuffer();

			internalRun(buf);

			ss.sendMessage(new MimeMessage(buf.toString()));
		}

		private void internalRun(StringBuffer buf) {
			Process p = Runtime.getRuntime().exec(cmd);
			buf.append(getOut(p.getInputStream()));
			buf.append("\r\n");
			buf.append(getOut(p.getErrorStream()));
			// p.wait(1000*30);
			// p.
		}

		private String getOut(InputStream ins) {
			StringBuffer t;
			int c = 0;

			t = new StringBuffer();

			InputStreamReader isr = null;

			// try {
			// isr = new InputStreamReader(ins, System
			// .getProperty("file.encoding"));
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// return "";
			// }

			isr = internalGetOut(ins, isr);
			if (isr == null)
				return "";

			internalGetOut2(t, c, isr);

			return t.toString();
		}

		private void internalGetOut2(StringBuffer t, int c,
				InputStreamReader isr) {
			c = isr.read();

			while (c > -1) {
				t.append((char) c);

				c = isr.read();
			}
		}

		private InputStreamReader internalGetOut(InputStream ins,
				InputStreamReader isr) {
			return new InputStreamReader(ins, System
					.getProperty("file.encoding"));
		}

	}

	class ICSProcessor extends IMAdapter {
		public void instantMessageReceived(Session ss, Buddy friend,
				MimeMessage mime) {
			String msg = mime.getMessageString();

			if (msg.startsWith("!sh")) {
				String cmd = parseMsg(msg.substring(3));
				if (cmd == null)
					return;
				RespCmd cmde = new RespCmd();
				cmde.cmd = cmd;
				cmde.ss = ss;
				cmde.start();
			}
		}
	}

	private String parseMsg(String s) {
		boolean stop = false;
		StringBuffer buf = new StringBuffer();
		int l = s.length();
		int i = 0;
		boolean blank = false;
		while (!stop && i < l) {
			if (s.charAt(i) == ' ') {
				blank = true;
				i++;
				continue;
			}
			if (blank == true && buf.length() > 0) {
				stop = true;
				i++;
				continue;
			}
			blank = false;
			buf.append(s.charAt(i));
			i++;
		}
		if (i >= l)
			return null;

		// enable immediately after change the value of the perference
		passwd = Environment.getInstance().getPrefStore().getString(
				EimpConsts.STO_CMDSERVER_PASSWD);
		if (!buf.toString().equals(passwd))
			return null;
		return s.substring(i - 1);
	}

	public ImCommandServer() {

	}

	public void init(Connection conn) {
		// jythonRunner = new JythonRunner();
		// this.conn = conn;
		evt = new ICSProcessor();
		conn.addIMListener(evt);

	}

}

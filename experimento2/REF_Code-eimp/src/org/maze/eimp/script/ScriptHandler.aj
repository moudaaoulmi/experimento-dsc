package org.maze.eimp.script;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public privileged aspect ScriptHandler {

	pointcut internalRunHandler(): execution( private void ImCommandServer.RespCmd.internalRun(StringBuffer));

	pointcut internalGetOutHandler(): execution(private InputStreamReader ImCommandServer.RespCmd.internalGetOut(InputStream,
			InputStreamReader));

	declare soft: Exception: internalRunHandler();
	declare soft: UnsupportedEncodingException: internalGetOutHandler();

	void around(StringBuffer buf): internalRunHandler()&& args(buf){
		try {
			proceed(buf);
		} catch (Exception e) {
			buf.append("\r\n");
			buf.append(e.getMessage());
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s);
			e.printStackTrace(p);
			buf.append(s.getBuffer());
		}
	}

	InputStreamReader around(): internalGetOutHandler() {
		try {
			return proceed();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}
}

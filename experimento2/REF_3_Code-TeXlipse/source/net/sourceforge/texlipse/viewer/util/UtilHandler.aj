package net.sourceforge.texlipse.viewer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.aspectj.lang.SoftException;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public privileged aspect UtilHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut writeToSocketHandler(): execution(public void FileLocationClient.writeToSocket());

	pointcut internalParseCommandLineHandler(): execution(private static int FileLocationClient.internalParseCommandLine(String, int) );

	pointcut internalParseCommandLineHandlerSoft(): execution(public static FileLocationClient FileLocationClient.parseCommandLine(String[]) );

	pointcut internalParseCommandLine2Handler(): execution(private static int FileLocationClient.internalParseCommandLine2(String, int));

	pointcut intenalStopHandler(): execution(private void FileLocationServer.intenalStop() );

	pointcut internalRun2Handler(): execution(private void FileLocationServer.internalRun2() );

	pointcut internalParseLineHandler(): execution(private int FileLocationServer.internalParseLine(String, int) );

	pointcut internalRunHandler1(): execution(private void ViewerErrorScanner.internalRun(BufferedReader, ArrayList, String));

	pointcut internalRunHandler2(): execution(private int ViewerErrorScanner.internalRun(int));
	
	public pointcut emptyBlockException(): internalRunHandler1();

	declare soft: IOException: writeToSocketHandler()||intenalStopHandler()|| internalRun2Handler()||internalRunHandler1();
	declare soft:InterruptedException:internalRunHandler2();

	int around(int exitCode): internalRunHandler2()&&args(exitCode) {
		try {
			return proceed(exitCode);
		} catch (InterruptedException e) {
		}
		return exitCode;
	}

//	void around(): internalRunHandler1() {
//		try {
//			proceed();
//		} catch (IOException e) {
//		}
//	}

	int around(int lineNumber): internalParseLineHandler()&& args(*, lineNumber) {
		try {
			proceed(lineNumber);
		} catch (NumberFormatException e) {
		}
		return lineNumber;
	}

	void around(FileLocationServer file): internalRun2Handler() && this(file) {
		try {
			proceed(file);
		} catch (IOException e) {
			file.ssocket = null;
			TexlipsePlugin.log("Server error: ", e);
		}
	}

	void around(): intenalStopHandler() {
		try {
			proceed();
		} catch (UnknownHostException e) {
			// shouldn't happen
		} catch (IOException e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("Stopping FileLocation server: ", e);
		}
	}

	int around(int port): internalParseCommandLine2Handler()&& args(*, port) {
		try {
			return proceed(port);
		} catch (NumberFormatException e) {
		}
		return port;
	}

	FileLocationClient around(): internalParseCommandLineHandlerSoft() {
		try {
			return proceed();
		} catch (SoftException e) {
			return null;
		}
	}

	int around(String lineNum): internalParseCommandLineHandler()&& args(lineNum,*) {
		try {
			return proceed(lineNum);
		} catch (NumberFormatException e) {
			System.out.println("Invalid lineNumber: " + lineNum);
			// return null;
			throw new SoftException(null);
		}
	}

	void around(FileLocationClient file): writeToSocketHandler() && this(file){
		try {
			proceed(file);
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host: " + file.hostName);
		} catch (IOException e) {
			System.out.println("Couldn't get I/O for the connection to: "
					+ file.hostName);
		}
	}

}

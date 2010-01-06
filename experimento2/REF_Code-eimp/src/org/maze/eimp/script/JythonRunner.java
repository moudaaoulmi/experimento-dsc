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

//import java.io.IOException;
import java.io.InputStream;
//import java.util.Properties;
//
//import org.eclipse.core.runtime.Platform;
//import org.python.core.PyModule;
//import org.python.core.imp;
//import org.python.util.PythonInterpreter;

/**
 * @author hliu
 *
 * $Id: JythonRunner.java,v 1.2 2003/06/27 07:20:47 loya Exp $
 */
public class JythonRunner implements Runner {
//	PythonInterpreter runner;
//	static{
//		Properties c=new Properties();
//		try {
//			c.put("python.home",Platform.resolve(Platform.getPlugin("org.jython").getDescriptor().getInstallURL()).getFile());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PythonInterpreter.initialize(System.getProperties(),c,null);
//	}
//	
//	public JythonRunner(){
//		runner = new PythonInterpreter();//Properties a = System.getProperties();
//		PyModule mod = imp.addModule("__main__");
//		runner.setLocals(mod.__dict__);
//		runner.setOut(System.out);
//		
//		//runner.exec("__name__='__main__'");
//		//runner.setOut(eimpPlugin.getDefault().getWorkbench().get);
//	}
//
	/* (non-Javadoc)
	 * @see org.maze.eimp.script.Runner#execfile(java.io.InputStream)
	 */
	public void execfile(InputStream s) {
//		runner.execfile(s);
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.script.Runner#execfile(java.lang.String)
	 */
	public void execfile(String s) {
		// TODO Auto-generated method stub
		//runner.execfile(s);
		
	}

}

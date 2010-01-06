package org.maze.eimp.app;

import java.io.FileNotFoundException;


public privileged aspect AppHandler {	

	pointcut loadfileHander(): execution(private void AboutDialog.loadfile());		

	declare soft: FileNotFoundException: loadfileHander();

	void around(): loadfileHander(){
		try {
			proceed();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
}

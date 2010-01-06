package org.maze.eimp.im;

public privileged aspect ImHandler {

	pointcut internalGetConnectionHandler(): execution( private void Account.internalGetConnection());

	declare soft: InstantiationException : internalGetConnectionHandler();

	declare soft:IllegalAccessException: internalGetConnectionHandler();

	declare soft:ClassNotFoundException:internalGetConnectionHandler();
	
	void around(): internalGetConnectionHandler(){
		try{
			proceed();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}

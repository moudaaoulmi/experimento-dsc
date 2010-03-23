package ish.ecletex.utils;


public privileged aspect UtilsHandler {

	pointcut internalRunCommandHandler(): execution(private String GhostScript.internalRunCommand(String));
	
	pointcut WaitForHandler(): execution(public int SafeExec.WaitFor());
	
	pointcut runHandler2(): execution(public void SafeExec.run());

	declare soft: Exception: internalRunCommandHandler() || WaitForHandler();	
	declare soft: Throwable: runHandler2();
	
	void around(SafeExec safe): runHandler2() && this(safe){
		try {
			proceed(safe);
		} catch (Throwable t) {
			t.printStackTrace();
			if (safe.process == null)
				safe.ProcessInitFailed = true;
		}
	}

	int around(): WaitForHandler() {
		try {
			return proceed();
		} catch (Exception ie) {
			ie.printStackTrace();
		}
		return 2;

	}

	

	String around(): internalRunCommandHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return "";
		}
	}

}

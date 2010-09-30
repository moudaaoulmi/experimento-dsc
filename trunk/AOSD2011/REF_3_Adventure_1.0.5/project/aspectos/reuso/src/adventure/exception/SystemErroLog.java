package adventure.exception;

import br.upe.dsc.reusable.exception.ILogObject;

public class SystemErroLog implements ILogObject {

	public void logGeneral(String msg, Throwable t) {
		System.err.println(msg + t.getMessage());
	}

}

package org.jhotdraw.util;

import java.io.IOException;

import javax.jdo.PersistenceManager;

import org.jhotdraw.exception.GeneralException;
import org.jhotdraw.framework.JHotDrawRuntimeException;

public class UtilHandler extends GeneralException {
	
	public void createCollectionsFactoryHandler(Exception e){
			throw new JHotDrawRuntimeException(e);
	}
	
	public Object loadImageResourceHandler () {
		return null;
	}
	
	public void trowIOException (String msg) throws IOException {
		throw new IOException(msg);
	}
	
	public void iconkitLoadRegisteredImages(){
		// ignore: do nothing
	}
	
	public void ignore(){
		//ignore
	}
}

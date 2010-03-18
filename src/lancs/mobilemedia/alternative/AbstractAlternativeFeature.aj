/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 21 Aug 2008
 * 
 */
package lancs.mobilemedia.alternative;

import lancs.mobilemedia.core.ui.MainUIMidlet;

/**
 * @author Eduardo Figueiredo
 *
 */
public abstract aspect AbstractAlternativeFeature {

	//public void startApp()
	public pointcut startApp(MainUIMidlet midlet):
		execution( public void MainUIMidlet.startApp() ) && this(midlet);

}

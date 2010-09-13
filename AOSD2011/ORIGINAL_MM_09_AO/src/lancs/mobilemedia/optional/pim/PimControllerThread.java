package lancs.mobilemedia.optional.pim;

import lancs.mobilemedia.core.ui.MainUIMidlet;
//import javax.microedition.pim.*;

public class PimControllerThread implements Runnable {
	
	MainUIMidlet mid;
	PimController controller = null;
	String list;

	public PimControllerThread(MainUIMidlet midlet, PimController control){//, String name){
		mid = midlet;
		controller = control;
		//list = name;
	}
	
	public void run(){	
		try{
			new PimSeed();
			controller.displayPim();
		}catch(Exception e){
			//TO DO
		}
	}

}

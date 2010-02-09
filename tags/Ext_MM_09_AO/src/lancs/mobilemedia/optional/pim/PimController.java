package lancs.mobilemedia.optional.pim;

import java.util.Enumeration;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import javax.microedition.pim.*;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;




public class PimController extends AbstractController{
	
	private ContactList contList = null;
	private Enumeration contacts = null;
	
	MediaController control = null;
	PimSeed seed = null;
	
	private List nNameList;
	
	Command add = new Command("Yes - Add", Command.OK, 0);
	Command ok = new Command("OK", Command.OK, 0);
	Command noadd = new Command("Cancel", Command.EXIT, 1);
		
	public PimController(MainUIMidlet midlet){//, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet);//, albumData, albumListScreen);
	}
	
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		
		if (label.equals("Add to Contact List")) {
			PimListScreen pimscreen = new PimListScreen("Contact List");
			pimscreen.setCommandListener(this);
			this.setCurrentScreen(pimscreen);
			PimControllerThread control = new PimControllerThread(midlet,this);//,seed.getNameList());
			Thread pimThread = new Thread(control);
			pimThread.start();
		 return true;	
		}else if(label.equals("Yes - Add")){
//			setPIMItem(getIndex(),'/images/'+getCurrentPhotoName+'.jpg');
			Alert alert = new Alert("Information", "The photo was succesfully added", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			alert.addCommand(ok);
			
			//setCurrentScreen( ScreenSingleton.getInstance().getMainMenu());
				return true;
		}else if(label.equals("OK")){
			setCurrentScreen( ScreenSingleton.getInstance().getMainMenu());
			return true;
		}
		return false;
		
	}
	
	public void displayPim(){
		
		internalPimInst();
		
		if(contacts==null){
			System.out.println("Contact List is empty");
		}
		
		nNameList = new List("List of Contacts",List.IMPLICIT);
				
		while(contacts.hasMoreElements()){
			Contact tCont = (Contact) contacts.nextElement();
			String [] nameValues = tCont.getStringArray(Contact.NAME, 0);
			String firstName = nameValues[Contact.NAME_GIVEN];
			String lastName = nameValues[Contact.NAME_FAMILY];
			nNameList.append(lastName + ", " + firstName, null);
		}
		
		nNameList.addCommand(add);
		nNameList.addCommand(noadd);
		
		nNameList.setCommandListener(this);
		
		Display.getDisplay(midlet).setCurrent(nNameList);
		
	}

	private void internalPimInst() {
		PIM pimInst = PIM.getInstance();
		contList = (ContactList) pimInst.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
		contacts = contList.items();
	}

}
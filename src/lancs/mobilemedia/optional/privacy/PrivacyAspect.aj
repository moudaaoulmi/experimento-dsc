package lancs.mobilemedia.optional.privacy;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.NewLabelScreen;
import lancs.mobilemedia.core.ui.screens.PasswordScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.InvalidAlbumNameException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


/*************
 * 
 * @author Chico Dantas
 *
 */

public privileged aspect PrivacyAspect {
	
	public NewLabelScreen AlbumController.albumName = null;
	public PasswordScreen AlbumController.password = null;
	public String AlbumController.albumtodelete = " ";
	public String MediaAccessor.password_label;
	public RecordStore MediaAccessor.passwordRS = null;
	
	public String MediaListController.passwd;
	public String MediaListController.ps2;
	
	
//	declare precedence: 
	
	/********* AlbumListScreen ***************/
	
	public static final Command addPassword = new Command("Add Password", Command.ITEM, 1);

	pointcut initMenu(AlbumListScreen screen):
		execution(public void AlbumListScreen.initMenu()) && this(screen);

	before(AlbumListScreen screen) : initMenu(screen) {
		screen.addCommand(addPassword);
	}
	
	/************ NewLabelScreen **************/
	
	pointcut newLabelScreenConst(NewLabelScreen label):
		execution(public NewLabelScreen.new()) && this(label); 
	
	before(NewLabelScreen label):newLabelScreenConst(label){
		label.ok = new Command("OK", Command.SCREEN, 0);
	}
	
	/************* AlbumData ***********************/
	
	public void AlbumData.addPassword(String albumname, String passwd){
		mediaAccessor.addPassword(albumname, passwd);
	}
	
	
	public String AlbumData.getPassword(String albumname){
		return mediaAccessor.getPassword(albumname);  
	}
	
	/************* MediaAccessor **********************/
	
	public void MediaAccessor.addPassword(String albumname, String passwd)  {		
		passwordRS = RecordStore.openRecordStore("mpp-"+albumname, true);
		passwordRS.addRecord(passwd.getBytes(), 0, passwd.getBytes().length);
		passwordRS.closeRecordStore();
	}
		
	public String MediaAccessor.getPassword(String albumname){
		String password = null;

		password = internalGetPassword(albumname, password);
		
		return password;
	}

	private String MediaAccessor.internalGetPassword(String albumname, String password) {
		passwordRS = RecordStore.openRecordStore("mpp-"+albumname, false);
		if(passwordRS!=null){
			password = new String(passwordRS.getRecord(1));
		}
		return password;
	}
	
	
	/*************** AlbumController ****************/
	
	pointcut handleCommandAction(AlbumController controller, Command c):
		execution(public boolean AlbumController.handleCommand(Command)) && args(c) && this(controller);

	boolean around(AlbumController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		if (handled) {
			System.out.println("entrou no handled");
			return true;
		}
		
		String label = c.getLabel();
		System.out.println("<* PrivacyAspect.around handleCommandAction *> " + label);
		
		if(label.equals("Add Password")){
			PasswordScreen pwd = new PasswordScreen("Define a Password",3);//, NewLabelScreen.NEW_ALBUM);
			pwd.setCommandListener(controller);
			controller.setCurrentScreen(pwd);
			pwd = null;
			return true;
		}else if(label.equals("Store")){
			PasswordScreen password = (PasswordScreen) controller.getCurrentScreen();
			controller.getAlbumData().addPassword(ScreenSingleton.getInstance().getCurrentStoreName(), password.getPassword());
			controller.goToPreviousScreen();
			return true;
		}else if(label.equals("OK")){
			controller.albumName = (NewLabelScreen)controller.getCurrentScreen();
			String message = "Would you like to define a password to this Album";
			Alert definePassword = new Alert("Define a Password", message,null,AlertType.CONFIRMATION);
			definePassword.setTimeout(Alert.FOREVER);
			definePassword.addCommand(new Command("Yes", Command.OK, 2));
			definePassword.addCommand(new Command("No", Command.EXIT, 2));
			controller.setAlbumListAsCurrentScreen(definePassword);
			definePassword.setCommandListener(controller);
			return true;	
		}else if(label.equals("Yes")){
			PasswordScreen pwd = new PasswordScreen("Define a Password",0);//, NewLabelScreen.NEW_ALBUM);
			pwd.setCommandListener(controller);
			controller.setCurrentScreen(pwd);
			pwd = null;
			return true;
		}else if(label.equals("No")){
			if (internalAroundHandleCommandAction2(controller)) {
				return true;
			}
			
			controller.goToPreviousScreen();
			return true;
		}else if(label.equals("Confirm")){
			System.out.println( "<* AlbumController.handleCommand() *>: " + label);
			PasswordScreen password = (PasswordScreen) controller.getCurrentScreen();
			String passwd =controller.getAlbumData().getPassword(controller.getCurrentStoreName());
			if(password.getPassword().equals(passwd)){
				internalAroundHandleCommandAction3(controller);
				
				controller.goToPreviousScreen();
			}else{
					Alert alert = new Alert( "Error", "Invalid Password", null, AlertType.ERROR);
					Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			}
			return true;
		}
		//controller.goToPreviousScreen();
		return false;
	}

	private void internalAroundHandleCommandAction3(AlbumController controller) {
		controller.getAlbumData().deleteAlbum(ScreenSingleton.getInstance().getCurrentStoreName());
	}

	private boolean internalAroundHandleCommandAction2(AlbumController controller) {
		controller.getAlbumData().createNewAlbum(controller.albumName.getLabelName());
		// the following line was added because of the new EH strategy
		return false;
	}
		
	pointcut saveAction(AlbumController controller): execution(public boolean AlbumController.saveDefault()) && this(controller);

	boolean around(AlbumController controller): saveAction(controller){
		if (internalAroundSaveAction(controller)) {
			return true;
		}
		controller.goToPreviousScreen();
		return true;
	}

	private boolean internalAroundSaveAction(AlbumController controller) {
		controller.password = (PasswordScreen) controller.getCurrentScreen();
		controller.getAlbumData().createNewAlbum(
				controller.albumName.getLabelName());
		controller.getAlbumData().addPassword(
				controller.albumName.getLabelName(),
				controller.password.getPassword());
		return false;
	}

	pointcut deleteAction(AlbumController controller):
			execution(public boolean AlbumController.deleteDefault()) && this(controller);

	boolean around(AlbumController controller): deleteAction(controller){
		String passwd;
		controller.albumtodelete = ScreenSingleton.getInstance()
				.getCurrentStoreName();
		passwd = controller.getAlbumData()
				.getPassword(controller.albumtodelete);
		if (passwd != null) {
			PasswordScreen pwd = new PasswordScreen("Password", 1);
			pwd.setCommandListener(controller);
			controller.setCurrentScreen(pwd);
			pwd = null;
		} else {
			internalAroundHandleCommandAction4(controller);

			controller.goToPreviousScreen();
		}
		return true;
	}

	private void internalAroundHandleCommandAction4(AlbumController controller) {
		controller.getAlbumData().deleteAlbum(
				ScreenSingleton.getInstance().getCurrentStoreName());
	}

	/******************* MediaListController **************************/

	pointcut handleCommandMediaAction(MediaListController controller, Command c):
			execution(public boolean MediaListController.handleCommand(Command)) && args(c) && this(controller);

	boolean around(MediaListController controller, Command c): handleCommandMediaAction(controller, c) {
		boolean handled = proceed(controller, c);
		if (handled)
			return true;
		String label = c.getLabel();
		System.out.println("<* PrivacyAspect.around handleCommandAction *> "
				+ label);

		if (label.equals("Confirm")) {
			PasswordScreen password = (PasswordScreen) controller
					.getCurrentScreen();
			controller.passwd = controller.getAlbumData().getPassword(
					controller.getCurrentStoreName());
			if (password.getPassword().equals(controller.passwd)) {
				controller.showMediaList(controller.getCurrentStoreName());// ,
																			// false,
																			// false);
				ScreenSingleton.getInstance().setCurrentScreenName(
						Constants.IMAGELIST_SCREEN);
			} else {
				Alert alert = new Alert("Error", "Invalid Password", null,
						AlertType.ERROR);
				Display.getDisplay(controller.midlet).setCurrent(alert,
						Display.getDisplay(controller.midlet).getCurrent());
			}
			return true;
		}
		return false;
	}

	pointcut selectAction(MediaListController controller):
			execution(public boolean MediaListController.selectDefault()) && this(controller);

	boolean around(MediaListController controller): selectAction(controller){

		List down = (List) Display.getDisplay(controller.midlet).getCurrent();
		ScreenSingleton.getInstance().setCurrentStoreName(
				down.getString(down.getSelectedIndex()));

		controller.passwd = ScreenSingleton.getInstance().getCurrentStoreName();
		controller.ps2 = controller.getAlbumData().getPassword(
				controller.passwd);

		if (controller.ps2 == null) {
			controller.showMediaList(ScreenSingleton.getInstance()
					.getCurrentStoreName());// , false, false);
			ScreenSingleton.getInstance().setCurrentScreenName(
					Constants.IMAGELIST_SCREEN);
		} else {
			PasswordScreen pwd = new PasswordScreen("Password", 1);
			pwd.setCommandListener(controller);
			controller.setCurrentScreen(pwd);
			pwd = null;
			return true;
		}

		return true;
	}
}
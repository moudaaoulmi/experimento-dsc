package lancs.mobilemedia.optional.capturephoto;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import lancs.mobilemedia.optional.copySMS.PhotoViewController;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

import javax.microedition.lcdui.Display;

public privileged aspect CapturePhotoAspect {
	
	// ********  MediaController  ********* //
	
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);

	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);

		if (handled)
			return true;

		String label = c.getLabel();
		System.out.println("<* CapturePhotoAspect.around handleCommandAction *> ::handleCommand: " + label);

		if (label.equals("Capture Photo")) {
			CaptureVideoScreen playscree = new CaptureVideoScreen(controller.midlet, CAPTUREPHOTO);
			playscree.setVisibleVideo();
			PhotoViewController newcontroller = new PhotoViewController(controller.midlet, controller.getAlbumData(), (AlbumListScreen) controller.getAlbumListScreen(), "New photo");
			newcontroller.setCpVideoScreen(playscree);
			controller.setNextController(newcontroller);
			playscree.setCommandListener(controller);
			return true;		
		}
		return false;
	}
	
	// ********  MediaController  ********* //
	
	private CaptureVideoScreen  PhotoViewController.cpVideoScreen = null;
	
	public CaptureVideoScreen PhotoViewController.getCpVideoScreen() {
		return cpVideoScreen;
	}

	public void PhotoViewController.setCpVideoScreen(CaptureVideoScreen cpVideoScreen) {
		this.cpVideoScreen = cpVideoScreen;
	}
	
	pointcut handleCommandActionMC(PhotoViewController controller, Command c):
		execution(public boolean PhotoViewController.handleCommand(Command)) && args(c) && this(controller);

	boolean around(PhotoViewController controller, Command c): handleCommandActionMC(controller, c) {
		boolean handled = proceed(controller, c);

		if (handled)
			return true;

		String label = c.getLabel();
		System.out.println("<* CapturePhotoAspect.around handleCommandAction *> " + label);
		if (label.equals("Take photo")){
			System.out.println("Olha para a captura"+controller.cpVideoScreen);
			byte[] newfoto = controller.cpVideoScreen.takePicture();
			AddMediaToAlbum copyPhotoToAlbum = new AddMediaToAlbum("Copy Photo to Album");
			copyPhotoToAlbum.setItemName("New picture");
			copyPhotoToAlbum.setLabePath("Copy to Album:");
			copyPhotoToAlbum.setCommandListener(controller);
			copyPhotoToAlbum.setCapturedMedia(newfoto);
	        Display.getDisplay(controller.midlet).setCurrent(copyPhotoToAlbum);
			return true;
		}
		return false;
	}
	// ********  CaptureVideoScreen  ********* //
	
	Command takephoto = new Command("Take photo", Command.EXIT, 1);

	public final static int CAPTUREPHOTO = 1;

	//public CaptureVideoScreen.CaptureVideoScreen(Image)
	pointcut constructor() :
		call(CaptureVideoScreen.new(..));

	after() returning (CaptureVideoScreen listScreen): constructor() {
		System.out.println("<* CapturePhotoAspect.after() new CaptureVideoScreen() *> "+ listScreen.typescreen + "=" + CAPTUREPHOTO);
		// [NC] Added in the scenario 08
		if (listScreen.typescreen == CAPTUREPHOTO){
			listScreen.addCommand(takephoto);
		}
	}

	public byte[] CaptureVideoScreen.takePicture() {
		try {
			Alert alert = new Alert("Error", "The mobile database is full", null, AlertType.INFO);
			alert.setTimeout(5000);
			display.setCurrent(alert);
			byte[] imageArray = videoControl.getSnapshot(null);
			return imageArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ********  MediaListScreen  ********* //
	
	// [NC] Added in the scenario 08 
	Command capturePhotoCommand = new Command("Capture Photo", Command.ITEM, 1);
	
	pointcut initMenu(MediaListScreen screen):
		execution(public void MediaListScreen.initMenu()) && this(screen);
	
	after(MediaListScreen screen) : initMenu(screen) {
		// [NC] Added in the scenario 08 
		if (screen.typeOfScreen == MediaListScreen.SHOWPHOTO) {
			screen.addCommand(capturePhotoCommand);
		}
	}
}

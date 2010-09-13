package lancs.mobilemedia.optional.sms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

import lancs.mobilemedia.alternative.photo.PhotoViewScreen;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;

public privileged aspect SMSAspect {

	// ********  PhotoViewScreen  ********* //
	
	// TODO [EF] This code may belong to SMS and Copy?
	
	 private byte[] PhotoViewScreen.byteImage = null;
	 
	 public byte[] PhotoViewScreen.getImage(){
		 return byteImage;
	 }
		
	 public void PhotoViewScreen.setImage(byte[] img){
		 byteImage = img;
	 }

	/* [EF] Added in scenario 06 */
	public static final Command smscopyCommand = new Command("Send Photo by SMS", Command.ITEM, 1);
	
	pointcut constructor(Image image) :
		call(PhotoViewScreen.new(Image)) && args(image);

	after(Image image) returning (PhotoViewScreen f): constructor(image) {
		f.addCommand(smscopyCommand);
	}

	private boolean PhotoViewScreen.fromSMS = false;
		
	public boolean PhotoViewScreen.isFromSMS() {
		return fromSMS;
	}
	
	public void PhotoViewScreen.setFromSMS(boolean fromSMS) {
		this.fromSMS = fromSMS;
	}
	
	pointcut loadImage(PhotoViewScreen screen):
		execution(public void PhotoViewScreen.loadImage()) && this(screen);

	void around(PhotoViewScreen screen): loadImage(screen) {
		if (screen.fromSMS){
		   return;
		}else {
		   proceed(screen);
		}
	}

	// ********  AddMediaToAlbum  ********* //	
	
	private Image AddMediaToAlbum.image = null;
	
	public Image AddMediaToAlbum.getImage() {
		return image;
	}

	public void AddMediaToAlbum.setImage(Image image) {
		this.image = image;
	}
	
	// ********  MainUIMidlet  ********* //
	
	pointcut startApplication(MainUIMidlet midlet):
		execution(public void MainUIMidlet.startApp())
		&& this(midlet);
	
	after(MainUIMidlet midlet): startApplication(midlet) {
		BaseController imageRootController = midlet.imageRootController;
		AlbumData imageModel = midlet.imageModel;
		
		AlbumListScreen albumListScreen = (AlbumListScreen)imageRootController.getAlbumListScreen(); // [EF]
		SmsReceiverController controller = new SmsReceiverController(midlet, imageModel, albumListScreen);
		controller.setNextController(imageRootController);
		SmsReceiverThread smsR = new SmsReceiverThread(midlet, imageModel, albumListScreen, controller);
		new Thread(smsR).start();
	}


	// ********  MediaController  ********* //
	
	//public AbstractController PhotoController.getMediaController(String mediaName)
	pointcut getMediaController(MediaController controller, String imageName): 
		 (call(public AbstractController MediaController.getMediaController(String)) && this(controller))&& args (imageName);
	
	AbstractController around (MediaController controller, String imageName): getMediaController(controller, imageName) {
		AbstractController nextcontroller = proceed(controller, imageName);
		SmsSenderController smscontroller = new SmsSenderController(controller.midlet, controller.getAlbumData(), controller.getAlbumListScreen(), imageName);
		smscontroller.setNextController(nextcontroller);
		return smscontroller;
	}
}

package lancs.mobilemedia.optional.sms;


import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;

/**
 * 
 */
public class SmsReceiverThread implements Runnable { //extends BaseThread {
	
	SmsReceiverController controller = null;
	String[] connections; 		//Connections detected at start up.
	String smsPort; 			//The port on which we listen for SMS messages
	MessageConnection smsconn; 	//SMS message connection for inbound text messages.
	Message msg; 				//Current message read from the network.
	String senderAddress; 		//Address of the message's sender
	
    Command acceptPhotoCommand = new Command("Accept Photo", Command.ITEM, 1);
    Command rejectPhotoCommand = new Command("Reject Photo", Command.ITEM, 1);
    Command errorNotice = new Command("Ok", Command.ITEM, 1);

	/**
	 * Initialize the MIDlet with the current display object and graphical
	 * components.
	 */
	public SmsReceiverThread(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen, SmsReceiverController controller) {
		this.controller = controller;
		smsPort = "1000"; //getAppProperty("SMS-Port");
	}

	/**
	 * Initialize the MIDlet with the current display object and graphical
	 * components.
	 * 
	 * Pass in the controller so we can notify it when a photo/message is received via SMS
	 */
	/** Message reading thread. */
	public void run() {
		SmsMessaging smsMessenger = new SmsMessaging();
		while(true){
			internalRun(smsMessenger);
		}
	}

	private void internalRun(SmsMessaging smsMessenger) {
		smsMessenger.setSmsReceivePort(smsPort);
		byte[] receivedData = null;

		receivedData = internalRun2(smsMessenger);
		
		Alert alert = new Alert("New Incoming Photo");
		alert.setString("A MobileMedia user has sent you a Photo. Do you want to accept it?");
		alert.addCommand(acceptPhotoCommand);
		alert.addCommand(rejectPhotoCommand);
		controller.setIncommingData(receivedData);
		alert.setCommandListener(controller);
		controller.setCurrentScreen(alert);
	}

	private byte[] internalRun2(SmsMessaging smsMessenger) {
		byte[] receivedData;
		receivedData = smsMessenger.receiveImage();
		return receivedData;
	}
}
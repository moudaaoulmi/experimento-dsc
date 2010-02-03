package lancs.mobilemedia.alternative.video;

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;

public class PlayVideoScreen extends GameCanvas {

	private Player player = null;
	private VideoControl videoControl = null;

	private Command start = new Command("Start", Command.EXIT, 1);
	private Command back = new Command("Back", Command.ITEM, 1);
	private Command stop = new Command("Stop", Command.ITEM, 1);

	private Display display = null;
	private int dy = 2;

	public void startVideo() {
		player.start();
	}

	public void stopVideo() {
		if (player != null)
			player.stop();
	}

	public PlayVideoScreen(MainUIMidlet midlet, InputStream storedVideo,
			String type, AbstractController controller) {
		super(false); // do not supress key events

		// create the alerts, canvas and displays
		display = Display.getDisplay(midlet);

		this.addCommand(start);
		this.addCommand(stop);
		this.addCommand(back);

		// load the Player and then the Volume and VideoControl
		internalPlayVideoScreenHandler();
		
		internalPlayVideoScreenHandler2(controller);
		
		int halfCanvasWidth = this.getWidth();
		int halfCanvasHeight = this.getHeight();

		internalPlayVideoScreenHandler3(halfCanvasWidth, halfCanvasHeight); 
	}

	private void internalPlayVideoScreenHandler3(int halfCanvasWidth, int halfCanvasHeight) {
		videoControl.setDisplayFullScreen(false);
		videoControl.setDisplaySize(halfCanvasWidth - 10,
				halfCanvasHeight - 10);
		videoControl.setDisplayLocation(5, 5);
	}

	private void internalPlayVideoScreenHandler2(AbstractController controller) {
		this.setCommandListener(controller);
		System.out.println("Crio os comandos e vai iniciar o display");
		// initialize the VideoControl display
		videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
	}

	private void internalPlayVideoScreenHandler() {
		player = Manager.createPlayer(getClass().getResourceAsStream(
				"/images/fish.mpg"), "video/mpeg");
		player.realize();

		videoControl = (VideoControl) player.getControl("VideoControl");
	}

	public void paint(Graphics g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight());
		flushGraphics();
	}

	public void keyPressed(int keyCode) {
		int gameAction = getGameAction(keyCode);
		int y = videoControl.getDisplayY();
		if (gameAction == UP) {
			y -= dy;
		} else if (gameAction == DOWN) {
			y += dy;
		}
		videoControl.setDisplayLocation(videoControl.getDisplayX(), y);
		repaint();
	}

	public void setVisibleVideo() {
		display.setCurrent(this);
		videoControl.setVisible(true);
	}
}

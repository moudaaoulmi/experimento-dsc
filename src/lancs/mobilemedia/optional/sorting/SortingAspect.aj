/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 7 Aug 2007
 * 
 */
package lancs.mobilemedia.optional.sorting;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.core.util.MediaUtil;

/**
 * @author Eduardo Figueiredo
 * 
 */
public privileged aspect SortingAspect {

	// ******** MediaController ********* //

	// TODO [EF] This pointcut is already defined in the ControllerAspectEH aspect
	// public void PhotoController.showImage() block 1 - Scenario 3
	public pointcut showImage(MediaController controler, String selectedImageName):
		(execution(public void MediaController.showImage(String)) ||
		execution(public boolean MediaController.playMultiMedia(String))) && this(controler) && args(selectedImageName);

	after(MediaController controler, String selectedImageName): showImage(controler, selectedImageName) {
		// [EF] Added in the scenario 02
		// TODO Nelio, how can we aspectize this EH?
		internalAfterShowImage(controler, selectedImageName);
	}

	private void internalAfterShowImage(MediaController controler, String selectedImageName) {
		MediaData image = controler.getAlbumData().getMediaInfo(selectedImageName);
		image.increaseNumberOfViews();
		controler.updateMedia(image);
		System.out.println("<* BaseController.handleCommand() *> Image = " + selectedImageName + "; # views = " + image.getNumberOfViews());
	}

	// public boolean PhotoController.handleCommand(Command, Displayable)
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);

	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		if (handled)
			return true;
		String label = c.getLabel();
		System.out.println("<* SortingAspect.around handleCommandAction *> " + label);

		/***********************************************************************
		 * Case: Sort photos by number of views [EF] Added in the scenario 02
		 **********************************************************************/
		if (label.equals("Sort by Views")) {
			sort = true;
			controller.showMediaList(controller.getCurrentStoreName());
			ScreenSingleton.getInstance().setCurrentStoreName(Constants.IMAGELIST_SCREEN);
			return true;
		}
		return false;
	}

	boolean sort = false;

	// ******** PhotoListController ********* //

	// public void PhotoListController.appendImages(ImageData[], PhotoListScreen)
	pointcut appendMedias(MediaListController controller, MediaData[] medias,
			MediaListScreen mediaList):
		call(public void MediaListController.appendMedias(MediaData[], MediaListScreen)) && args(medias, mediaList) && this(controller);

	before(MediaListController controller, MediaData[] medias,
			MediaListScreen mediaList): appendMedias(controller, medias, mediaList) {
		// [EF] Check if sort is true (Add in the Scenario 02)
		if (sort) {
			bubbleSort(medias);
		}
		sort = false;
	}

	/**
	 * @param images
	 * @param pos1
	 * @param pos2
	 */
	private void exchange(MediaData[] medias, int pos1, int pos2) {
		MediaData tmp = medias[pos1];
		medias[pos1] = medias[pos2];
		medias[pos2] = tmp;
	}

	/**
	 * Sorts an int array using basic bubble sort
	 * 
	 * @param numbers
	 *            the int array to sort
	 */
	public void bubbleSort(MediaData[] medias) {
		System.out.print("Sorting by BubbleSort...");
		for (int end = medias.length; end > 1; end--) {
			for (int current = 0; current < end - 1; current++) {
				if (medias[current].getNumberOfViews() > medias[current + 1]
						.getNumberOfViews()) {
					exchange(medias, current, current + 1);
				}
			}
		}
		System.out.println("done.");
	}

	// ******** ImageData ********* //

	// [EF] Added in the scenario 02
	private int MediaData.numberOfViews = 0;

	/**
	 * [EF] Added in the scenario 02
	 */
	public void MediaData.increaseNumberOfViews() {
		this.numberOfViews++;
	}

	/**
	 * [EF] Added in the scenario 02
	 * 
	 * @return the numberOfViews
	 */
	public int MediaData.getNumberOfViews() {
		return numberOfViews;
	}

	/**
	 * [EF] Added in the scenario 02
	 * 
	 * @param views
	 */
	public void MediaData.setNumberOfViews(int views) {
		this.numberOfViews = views;
	}

	// ******** PhotoListScreen ********* //

	public static final Command sortCommand = new Command("Sort by Views", Command.ITEM, 1);

	// public void PhotoListScreen.initMenu()
	pointcut initMenu(MediaListScreen screen):
		execution(public void MediaListScreen.initMenu()) && this(screen);

	after(MediaListScreen screen) : initMenu(screen) {
		// [EF] Added in the scenario 02
		screen.addCommand(sortCommand);
	}

	// ******** ImageUtil ********* //

	// ImageData ImageUtil.createImageData(String, String, String, int, String)
	pointcut createMediaData(MediaUtil mediaUtil, String fidString,
			String albumLabel, String mediaLabel, int endIndex, String iiString):
		execution(MediaData MediaUtil.createMediaData(String, String, String, int, String)) && args(fidString, albumLabel, mediaLabel, endIndex, iiString) && this(mediaUtil);

	MediaData around(MediaUtil mediaUtil, String fidString, String albumLabel,
			String mediaLabel, int endIndex, String iiString) : createMediaData(mediaUtil, fidString, albumLabel, mediaLabel, endIndex, iiString) {
		// [EF] Number of Views (Scenario 02)
		int startIndex = mediaUtil.endIndex + 1;
		mediaUtil.endIndex = iiString.indexOf(MediaUtil.DELIMITER, startIndex);
		if (mediaUtil.endIndex == -1)
			mediaUtil.endIndex = iiString.length();
		// [EF] Added in the scenario 02
		int numberOfViews = 0;
		try {
			numberOfViews = Integer.parseInt(iiString.substring(startIndex, mediaUtil.endIndex));
		} catch (RuntimeException e) {
			numberOfViews = 0;
			e.printStackTrace();
		}
		MediaData mediaData = proceed(mediaUtil, fidString, albumLabel, mediaLabel, mediaUtil.endIndex, iiString);
		mediaData.setNumberOfViews(numberOfViews);
		return mediaData;
	}

	// TODO [EF] This pointcut is already defined in the UtilAspectEH aspect
	// Method public String ImageUtil.getBytesFromImageInfo(ImageData ii) 1-
	// block - Scenario 1
	pointcut getBytesFromImageInfo(MediaData ii): 
		 execution(public String MediaUtil.getBytesFromMediaInfo(MediaData)) && args(ii);

	String around(MediaData ii) : getBytesFromImageInfo(ii) {
		String byteString = proceed(ii);
		// [EF] Added in scenatio 02
		byteString = byteString.concat(MediaUtil.DELIMITER);
		byteString = byteString.concat("" + ii.getNumberOfViews());
		return byteString;
	}
}

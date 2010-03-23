/*
 * Created on 11-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex;

import java.util.HashMap;

import ish.ecletex.utils.GhostScript;
import ish.ecletex.utils.ProjectUtils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImageProvider {

	private static HashMap bboxes = new HashMap();
	
	
	public static Rectangle getBoundingBox(IPath PSImage){
		if(!bboxes.containsKey(PSImage.toOSString())){
			bboxes.put(PSImage.toOSString(),GhostScript.getBoundingBox(PSImage.toOSString()));
		}
		return (Rectangle)bboxes.get(PSImage.toOSString());
	}
	
	
	public static ImageData loadPSImage(IPath imagePath){

		String tempdir = ProjectUtils.getCurrentTempDir();
		System.out.println("TempDir: " + tempdir);
		IPath PSFile = imagePath;
		IPath PNGFile = new Path(tempdir).append(PSFile.removeFileExtension().addFileExtension("png").lastSegment());
		
		System.out.println("PSFile: " + PSFile.toOSString());
		System.out.println("PNGFile: " + PNGFile.toOSString());

		if (PSFile.toFile().exists()) {
			if (!PNGFile.toFile().exists()) {
				PNGFile.removeLastSegments(1).toFile().mkdirs();
				if (GhostScript
					.ConvertToPng(
						PSFile.toOSString(),
						PNGFile.removeLastSegments(1).toOSString())) {
					System.out.println("Made: " + PNGFile.toOSString());
					ImageLoader loader = new ImageLoader();
					return loader.load(PNGFile.toOSString())[0];
				} else {
					System.out.println(
						"Failed to make: " + PNGFile.toOSString());
				}
			} else {
				ImageLoader loader = new ImageLoader();
				return loader.load(PNGFile.toOSString())[0];
			}
		}
		return null;
	}
	
	
}

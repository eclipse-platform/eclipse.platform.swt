package org.eclipse.swt.examples.controlexample;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.ResourceBundle;

/**
* <code>Images</code> contains every image
* that is used by the example.
*/
class Images {
	static Image CLOSED_FOLDER_IMAGE;
	static Image OPEN_FOLDER_IMAGE;
	static Image TARGET_IMAGE;
	private static ResourceBundle resControl = ResourceBundle.getBundle("examples_control");
/**
* Free the images.
*/
public static void freeImages () {
	CLOSED_FOLDER_IMAGE.dispose ();
	OPEN_FOLDER_IMAGE.dispose ();
	TARGET_IMAGE.dispose ();
}
/**
 * Load the images.
 */
public static void loadImages () {
	Class clazz = Images.class;
	try {
		ImageData source = new ImageData(clazz.getResourceAsStream ("folder.gif"));
		ImageData mask = source.getTransparencyMask();
		CLOSED_FOLDER_IMAGE = new Image (null, source, mask);
		
		source = new ImageData(clazz.getResourceAsStream ("folderOpen.gif"));
		mask = source.getTransparencyMask();
		OPEN_FOLDER_IMAGE = new Image (null, source, mask);
		
		source = new ImageData(clazz.getResourceAsStream ("stop.gif"));
		mask = source.getTransparencyMask();
		TARGET_IMAGE = new Image (null, source, mask);
	} catch (Throwable ex) {
		System.out.println (resControl.getString("Images_failed"));
	}
}
}

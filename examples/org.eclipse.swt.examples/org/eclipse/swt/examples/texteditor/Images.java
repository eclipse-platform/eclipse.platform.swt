package org.eclipse.swt.examples.texteditor;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Images {

	// Bitmap Images
	public Image Bold;
	public Image Red;
	public Image Green;
	public Image Blue;
	public Image Erase;
	
	Image  [] AllBitmaps;

Images () {
}

public void freeAll () {
	for (int i=0; i<AllBitmaps.length; i++) AllBitmaps [i].dispose ();
	AllBitmaps = null;
}

Image createBitmapImage(Display display, String fileName) {
	ImageData source = new ImageData(Images.class.getResourceAsStream(fileName+".bmp"));
	ImageData mask = new ImageData(Images.class.getResourceAsStream(fileName+"_mask"+".bmp"));
	return new Image (display, source, mask);
}

public void loadAll (Display display) {
	// Bitmap Images
	Bold = createBitmapImage (display, "bold");
	Red = createBitmapImage (display, "red");
	Green = createBitmapImage (display, "green");
	Blue = createBitmapImage (display, "blue");
	Erase = createBitmapImage (display, "erase");
	
	AllBitmaps = new Image [] {
		Bold,
		Red,
		Green,
		Blue,
		Erase,
	};
}
}

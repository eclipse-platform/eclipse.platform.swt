package org.eclipse.swt.examples.texteditor;

import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Images {

	// Bitmap Images
	public static Image Bold;
	public static Image Red;
	public static Image Green;
	public static Image Blue;
	public static Image Erase;
	
	static Image  [] AllBitmaps;

public static void freeAll () {
	for (int i=0; i<AllBitmaps.length; i++) AllBitmaps [i].dispose ();
	AllBitmaps = null;
}

static Image createBitmapImage(Display display, String fileName) {
	ImageData source = new ImageData(Images.class.getResourceAsStream(fileName+".bmp"));
	ImageData mask = new ImageData(Images.class.getResourceAsStream(fileName+"_mask"+".bmp"));
	return new Image (display, source, mask);
}
public static void loadAll (Display display) {

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

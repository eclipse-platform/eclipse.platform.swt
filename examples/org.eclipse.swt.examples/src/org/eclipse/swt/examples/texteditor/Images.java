/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.texteditor;


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

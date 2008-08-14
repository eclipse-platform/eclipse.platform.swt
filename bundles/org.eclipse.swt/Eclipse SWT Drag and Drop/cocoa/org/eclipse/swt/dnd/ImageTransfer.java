/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Outhink - support for typeFileURL
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;

/**
 * The class <code>ImageTransfer</code> provides a platform specific mechanism 
 * for converting an Image represented as a java <code>ImageData</code> to a 
 * platform specific representation of the data and vice versa.  
 * 
 * <p>An example of a java <code>ImageData</code> is shown below:</p>
 * 
 * <code><pre>
 *     Image image = new Image(display, "C:\temp\img1.gif");
 *	   ImageData imgData = image.getImageData();
 * </code></pre>
 *
 * @see Transfer
 * 
 * @since 3.4
 */
public class ImageTransfer extends ByteArrayTransfer {

static ImageTransfer _instance = new ImageTransfer();
static final String TIFF = OS.NSTIFFPboardType.getString();
static final int TIFFID = registerType(TIFF);

ImageTransfer() {
}

/**
 * Returns the singleton instance of the ImageTransfer class.
 *
 * @return the singleton instance of the ImageTransfer class
 */
public static ImageTransfer getInstance() {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts an ImageData object represented
 * by java <code>ImageData</code> to a platform specific representation.
 * 
 * @param object a java <code>ImageData</code> containing the ImageData to be converted
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!checkImage(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	ImageData imgData = (ImageData) object;
	Image image = new Image(Display.getCurrent(), imgData);
	NSImage handle = image.handle;
	transferData.data = handle.TIFFRepresentation();
	image.dispose();
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of an image to java <code>ImageData</code>.  
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>ImageData</code> of the image if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	NSData data = (NSData) transferData.data;
	if (data.length() == 0) return null;
	NSImage nsImage = (NSImage) new NSImage().alloc();
	nsImage.initWithData(data);
	//TODO: Image representation wrong???
	Image image = Image.cocoa_new(Display.getCurrent(), SWT.BITMAP, nsImage);
	ImageData imageData = image.getImageData();
	image.dispose();
	return imageData;
}

protected int[] getTypeIds() {
	return new int[] { TIFFID };
}

protected String[] getTypeNames() {
	return new String[] { TIFF };
}

boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData)) return false;
	return true;
}

protected boolean validate(Object object) {
	return checkImage(object);
}
}

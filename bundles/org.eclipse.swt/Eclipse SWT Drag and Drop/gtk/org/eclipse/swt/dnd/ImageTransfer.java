/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
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
	
	private static ImageTransfer _instance = new ImageTransfer();
	
	private static final String JPEG = "image/jpeg"; //$NON-NLS-1$
	private static final int JPEG_ID = registerType(JPEG);
	private static final String PNG = "image/png"; //$NON-NLS-1$
	private static final int PNG_ID = registerType(PNG);
	private static final String BMP = "image/bmp"; //$NON-NLS-1$
	private static final int BMP_ID = registerType(BMP);
	private static final String EPS = "image/eps"; //$NON-NLS-1$
	private static final int EPS_ID = registerType(EPS);
	private static final String PCX = "image/pcx"; //$NON-NLS-1$
	private static final int PCX_ID = registerType(PCX);
	private static final String PPM = "image/ppm"; //$NON-NLS-1$
	private static final int PPM_ID = registerType(PPM);
	private static final String RGB = "image/ppm"; //$NON-NLS-1$
	private static final int RGB_ID = registerType(RGB);
	private static final String TGA = "image/tga"; //$NON-NLS-1$
	private static final int TGA_ID = registerType(TGA);
	private static final String XBM = "image/xbm"; //$NON-NLS-1$
	private static final int XBM_ID = registerType(XBM);
	private static final String XPM = "image/xpm"; //$NON-NLS-1$
	private static final int XPM_ID = registerType(XPM);
	private static final String XV = "image/xv"; //$NON-NLS-1$
	private static final int XV_ID = registerType(XV);	
	
private ImageTransfer() {}

/**
 * Returns the singleton instance of the ImageTransfer class.
 *
 * @return the singleton instance of the ImageTransfer class
 */
public static ImageTransfer getInstance () {
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
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) return;
	
	ImageData imgData = (ImageData)object;
	if (imgData == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	Image image = new Image(Display.getCurrent(), imgData);	
 	int /*long*/ pixbuf = ImageList.createPixbuf(image);
	if (pixbuf != 0) {
		String typeStr = "";
		if (transferData.type ==  JPEG_ID) typeStr = "jpeg";
		else if (transferData.type ==  PNG_ID) typeStr = "png";
		else if (transferData.type ==  BMP_ID) typeStr = "bmp";
		else if (transferData.type ==  EPS_ID) typeStr = "eps";
		else if (transferData.type ==  PCX_ID) typeStr = "pcx";
		else if (transferData.type ==  PPM_ID) typeStr = "ppm";
		else if (transferData.type ==  RGB_ID) typeStr = "rgb";
		else if (transferData.type ==  TGA_ID) typeStr = "tga";
		else if (transferData.type ==  XBM_ID) typeStr = "xbm";
		else if (transferData.type ==  XPM_ID) typeStr = "xpm";
		else if (transferData.type ==  XV_ID) typeStr = "xv";
		byte[] type = Converter.wcsToMbcs(null, typeStr , true);
		int /*long*/ [] buffer = new int /*long*/ [1];
		int /*long*/ [] len = new int /*long*/ [1];
		if (type == null) return;
		OS.gdk_pixbuf_save_to_bufferv(pixbuf, buffer, len, type, null, null, null);
		OS.g_object_unref(pixbuf);
		transferData.pValue = buffer[0];
		transferData.length = (int)(len[0] + 3) / 4 * 4;
		transferData.result = 1;
		transferData.format = 32;
	}
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
	ImageData imgData = null;
	if (transferData.length > 0)
	{
		int /*long*/ loader = OS.gdk_pixbuf_loader_new();
		OS.gdk_pixbuf_loader_write(loader, transferData.pValue, transferData.length, null);
		OS.gdk_pixbuf_loader_close(loader, null);
		int /*long*/ pixbuf = OS.gdk_pixbuf_loader_get_pixbuf(loader);
		if (pixbuf != 0) {
			int /*long*/ [] pixmap_return = new int /*long*/ [1];
			OS.gdk_pixbuf_render_pixmap_and_mask(pixbuf, pixmap_return, null, 0);
			int /*long*/ handle = pixmap_return[0];
			if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			OS.g_object_unref(loader);
			Image img = Image.gtk_new(Display.getCurrent(), SWT.BITMAP, handle, 0);		
			imgData = img.getImageData();
			img.dispose();
		}		
	}
	return imgData;
}

protected int[] getTypeIds(){
	return new int[]{PNG_ID, BMP_ID, EPS_ID, JPEG_ID, PCX_ID, PPM_ID, RGB_ID, TGA_ID, XBM_ID, XPM_ID, XV_ID};	
}

protected String[] getTypeNames(){
	return new String[]{PNG, BMP, EPS, JPEG, PCX, PPM, RGB, TGA, XBM, XPM, XV};
}

boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData)) return false;
	return true;
}

protected boolean validate(Object object) {
	return checkImage(object);
}
}

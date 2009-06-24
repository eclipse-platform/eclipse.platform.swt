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
import org.eclipse.swt.internal.carbon.*;
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
static final String PICT = "PICT"; //$NON-NLS-1$
static final String TIFF = "TIFF"; //$NON-NLS-1$
static final int PICTID = registerType(PICT);
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
	transferData.result = -1;

	ImageData imgData = (ImageData) object;
	Image image = new Image(Display.getCurrent(), imgData);
	int handle = image.handle;
	int width = OS.CGImageGetWidth(handle);
	int height = OS.CGImageGetHeight(handle);
	int alphaInfo = OS.CGImageGetAlphaInfo(handle);
	int bpr = OS.CGImageGetBytesPerRow(handle);

	Rect rect = new Rect();
	rect.left = 0;
	rect.top = 0;
	rect.right = (short) width;
	rect.bottom = (short) height;

	int[] gWorld = new int[1];
	int format = OS.k24RGBPixelFormat;
	if (alphaInfo != OS.kCGImageAlphaNoneSkipFirst) {
		format = OS.k32ARGBPixelFormat;
	}
	OS.NewGWorldFromPtr(gWorld, format, rect, 0, 0, 0, image.data, bpr);
	int[] curPort = new int[1];
	int[] curGWorld = new int[1];
	OS.GetGWorld(curPort, curGWorld);
	OS.SetGWorld(gWorld[0], curGWorld[0]);
	int pictHandle = OS.OpenPicture(rect);
	int portBitMap = OS.GetPortBitMapForCopyBits(gWorld[0]);
	OS.CopyBits(portBitMap, portBitMap, rect, rect, (short) OS.srcCopy, 0);
	OS.ClosePicture();
	OS.SetGWorld(curPort[0], curGWorld[0]);
	OS.DisposeGWorld(gWorld[0]);
	int length = OS.GetHandleSize(pictHandle);
	OS.HLock(pictHandle);
	int[] buffer = new int[1];
	OS.memmove(buffer, pictHandle, 4);
	byte[] pictData = new byte[length];
	OS.memmove(pictData, buffer[0], length);
	OS.HUnlock(pictHandle);
	OS.KillPicture(pictHandle);
	image.dispose();

	transferData.data = new byte[][] { pictData };
	transferData.result = OS.noErr;
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
	if (!isSupportedType(transferData) || transferData.data == null)
		return null;
	if (transferData.data.length == 0)
		return null;
	byte[] dataArr = transferData.data[0];
	int size = dataArr.length;
	int pictPtr = OS.NewPtr(size);
	OS.memmove(pictPtr, dataArr, size);
	int dataProvider = OS.CGDataProviderCreateWithData(0, pictPtr, size, 0);
	if (dataProvider != 0) {
		int pictDataRef = OS.QDPictCreateWithProvider(dataProvider);
		// get bounds for the image
		CGRect rect = new CGRect();
		OS.QDPictGetBounds(pictDataRef, rect);
		int width = (int) rect.width;
		int height = (int) rect.height;

		/* Create the image */
		int bpr = width * 4;
		int dataSize = height * bpr;
		int data = OS.NewPtr(dataSize);
		if (data == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);
		int provider = OS
				.CGDataProviderCreateWithData(0, data, dataSize, 0);
		if (provider == 0) {
			OS.DisposePtr(data);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		int colorspace = OS.CGColorSpaceCreateDeviceRGB();
		if (colorspace == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);
		int handle = OS.CGImageCreate(width, height, 8, 32, bpr,
				colorspace, OS.kCGImageAlphaNoneSkipFirst, provider, null,
				true, 0);
		OS.CGDataProviderRelease(provider);
		if (handle == 0) {
			OS.DisposePtr(data);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		int bpc = OS.CGImageGetBitsPerComponent(handle);
		int context = OS.CGBitmapContextCreate(data, width, height, bpc,
				bpr, colorspace, OS.kCGImageAlphaNoneSkipFirst);
		if (context == 0) {
			OS.CGImageRelease(handle);
			OS.DisposePtr(data);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		int status = OS.QDPictDrawToCGContext(context, rect, pictDataRef);
		ImageData imgData = null;
		if (status == 0) {
			Image image = Image.carbon_new(Display.getCurrent(),
					SWT.BITMAP, handle, data);
			imgData = image.getImageData();
			image.dispose();
		}
		OS.CGContextRelease(context);
		OS.QDPictRelease(pictDataRef);
		return imgData;
	}
	return null;
}

protected int[] getTypeIds() {
	return new int[] { PICTID };
}

protected String[] getTypeNames() {
	return new String[] { PICT };
}

boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData)) return false;
	return true;
}

protected boolean validate(Object object) {
	return checkImage(object);
}
}

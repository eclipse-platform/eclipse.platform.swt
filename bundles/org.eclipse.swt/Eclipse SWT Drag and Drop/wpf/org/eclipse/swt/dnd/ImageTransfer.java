/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.internal.wpf.OS;

/**
 * The class <code>ImageTransfer</code> provides a platform specific mechanism 
 * for converting a Image represented as a java <code>ImageData</code> to a 
 * platform specific representation of the data and vice versa.  
 * See <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>ImageData</code> is shown 
 * below:</p>
 * 
 * <code><pre>
 *     Image image = new Image("C:\temp\img1.gif");
 *	   ImageData imgData = image.getImageData();
 * </code></pre>
 */
public class ImageTransfer extends ByteArrayTransfer {
	
private static ImageTransfer _instance = new ImageTransfer();
private static int TYPEID = getTypeId();

static int getTypeId() {
	int format = OS.DataFormats_Bitmap();
	String name = createJavaString(format);
	OS.GCHandle_Free(format);
	return registerType(name);
}

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
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>ImageData</code> containing the ImageData to be 
 * converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!checkImage(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	ImageData imgData = (ImageData)object;
	if (imgData == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

	Image image = new Image(null, imgData);
	transferData.pValue = image.handle;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of an <code>ImageData</code> to java.  
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>ImageData</code> the imageData of the image if
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData) || transferData.pValue == 0) return null;

	Image image = Image.wpf_new (null, SWT.BITMAP, transferData.pValue);
	ImageData imageData = image.getImageData ();
	image.dispose ();
	return imageData;
}

protected int[] getTypeIds(){
	return new int[]{TYPEID};
}

boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData))  return false; 
	return true;
}

protected boolean validate(Object object) {
	return checkImage(object);
}
}

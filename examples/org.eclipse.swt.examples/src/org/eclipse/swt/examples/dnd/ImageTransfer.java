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
package org.eclipse.swt.examples.dnd;

 
/**
 * Transfer type to transfer SWT ImageData objects.
 */

import java.io.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ImageTransfer extends ByteArrayTransfer {
	
	private static final String TYPENAME = "imagedata";
	private static final int TYPEID = registerType(TYPENAME);
	private static ImageTransfer _instance = new ImageTransfer();

public static ImageTransfer getInstance () {
	return _instance;
}

public void javaToNative (Object object, TransferData transferData) {
	if (object == null || !(object instanceof ImageData)) return;	
	if (!isSupportedType(transferData)) return;
	
	ImageData imdata = (ImageData)object;
	try {
		// write data to a byte array and then ask super to convert to pMedium
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream writeOut = new DataOutputStream(out);
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData [] {imdata};
		loader.save(writeOut, SWT.IMAGE_BMP);
		writeOut.close();
		byte[] buffer = out.toByteArray();
		super.javaToNative(buffer, transferData);
		out.close();
	} 
	catch (IOException e) {
	}
}
public Object nativeToJava(TransferData transferData){	
	if (!isSupportedType(transferData)) return null;
		
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	
	ImageData imdata;
	try {
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		DataInputStream readIn = new DataInputStream(in);
		imdata = new ImageData(readIn);
		readIn.close();
	} catch (IOException ex) {
		return null;
	}
	return imdata;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME};
}
protected int[] getTypeIds(){
	return new int[] {TYPEID};
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Drag and Drop example snippet: define my own data transfer type
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import java.io.*;
import org.eclipse.swt.dnd.*;

public class Snippet79 extends ByteArrayTransfer {

	/* The data being transferred is an array of type MyType which is define as: */
	 public static class MyType {
	 	String fileName;
	 	long fileLength;
	 	long lastModified;
	 }
	 
	private static final String MYTYPENAME = "name_for_my_type";
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static Snippet79 _instance = new Snippet79();
	
public static Snippet79 getInstance () {
	return _instance;
}
public void javaToNative (Object object, TransferData transferData) {
	if (object == null || !(object instanceof MyType[])) return;
	
	if (isSupportedType(transferData)) {
		MyType[] myTypes = (MyType[]) object;	
		try {
			// write data to a byte array and then ask super to convert to pMedium
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream writeOut = new DataOutputStream(out);
			for (int i = 0, length = myTypes.length; i < length;  i++){
				byte[] buffer = myTypes[i].fileName.getBytes();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
				writeOut.writeLong(myTypes[i].fileLength);
				writeOut.writeLong(myTypes[i].lastModified);
			}
			byte[] buffer = out.toByteArray();
			writeOut.close();

			super.javaToNative(buffer, transferData);
			
		} catch (IOException e) {
		}
	}
}
public Object nativeToJava(TransferData transferData){	

	if (isSupportedType(transferData)) {
		
		byte[] buffer = (byte[])super.nativeToJava(transferData);
		if (buffer == null) return null;
		
		MyType[] myData = new MyType[0];
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			DataInputStream readIn = new DataInputStream(in);
			while(readIn.available() > 20) {
				MyType datum = new MyType();
				int size = readIn.readInt();
				byte[] name = new byte[size];
				readIn.read(name);
				datum.fileName = new String(name);
				datum.fileLength = readIn.readLong();
				datum.lastModified = readIn.readLong();
				MyType[] newMyData = new MyType[myData.length + 1];
				System.arraycopy(myData, 0, newMyData, 0, myData.length);
				newMyData[myData.length] = datum;
				myData = newMyData;
			}
			readIn.close();
		} catch (IOException ex) {
			return null;
		}
		return myData;
	}

	return null;
}
protected String[] getTypeNames(){
	return new String[]{MYTYPENAME};
}
protected int[] getTypeIds(){
	return new int[] {MYTYPEID};
}
}

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
package org.eclipse.swt.examples.clipboard;

import java.io.*;
import org.eclipse.swt.dnd.*;

public class MyTypeTransfer extends ByteArrayTransfer {
	
	private static final String MYTYPENAME = "name_list"; //$NON-NLS-1$
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static MyTypeTransfer _instance = new MyTypeTransfer();
	
public static MyTypeTransfer getInstance () {
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
				byte[] buffer = myTypes[i].firstName.getBytes();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
				buffer = myTypes[i].firstName.getBytes();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
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
				datum.firstName = new String(name);
				size = readIn.readInt();
				name = new byte[size];
				readIn.read(name);
				datum.lastName = new String(name);
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

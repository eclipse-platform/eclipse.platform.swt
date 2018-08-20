/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.clipboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

public class MyTypeTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "name_list"; //$NON-NLS-1$
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static MyTypeTransfer _instance = new MyTypeTransfer();

public static MyTypeTransfer getInstance () {
	return _instance;
}
@Override
public void javaToNative (Object object, TransferData transferData) {
	if (!checkMyType(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	MyType[] myTypes = (MyType[]) object;
	try {
		// write data to a byte array and then ask super to convert to pMedium
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (DataOutputStream writeOut = new DataOutputStream(out)) {
			for (MyType myType : myTypes) {
				byte[] buffer = myType.firstName.getBytes();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
				buffer = myType.firstName.getBytes();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
			}
			byte[] buffer = out.toByteArray();
			super.javaToNative(buffer, transferData);
		}
	} catch (IOException e) {
	}
}
@Override
public Object nativeToJava(TransferData transferData){
	if (isSupportedType(transferData)) {

		byte[] buffer = (byte[])super.nativeToJava(transferData);
		if (buffer == null) return null;

		MyType[] myData = new MyType[0];
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			try (DataInputStream readIn = new DataInputStream(in)) {
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
			}
		} catch (IOException ex) {
			return null;
		}
		return myData;
	}

	return null;
}
@Override
protected String[] getTypeNames(){
	return new String[]{MYTYPENAME};
}
@Override
protected int[] getTypeIds(){
	return new int[] {MYTYPEID};
}
boolean checkMyType(Object object) {
	if (object == null || !(object instanceof MyType[]) || ((MyType[])object).length == 0) return false;
	MyType[] myTypes = (MyType[])object;
	for (MyType myType : myTypes) {
		if (myType == null ||
			myType.firstName == null ||
			myType.firstName.length() == 0 ||
			myType.lastName == null ||
			myType.lastName.length() == 0) return false;
	}
	return true;
}
@Override
protected boolean validate(Object object) {
	return checkMyType(object);
}
}

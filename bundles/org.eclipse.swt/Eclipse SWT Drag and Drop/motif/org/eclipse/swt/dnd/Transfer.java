package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.widgets.Display;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public abstract class Transfer {
abstract public TransferData[] getSupportedTypes();
abstract public boolean isSupportedType(TransferData transferData);
abstract protected String[] getTypeNames();
abstract protected int[] getTypeIds();
abstract protected void javaToNative (Object object, TransferData transferData);
abstract protected Object nativeToJava(TransferData transferData);
public static int registerType(String formatName){

	int xDisplay = Display.getDefault().xDisplay; // using default because we don't have a particular widget
	byte[] bName = Converter.wcsToMbcs (null, formatName, false);
	int atom = OS.XmInternAtom (xDisplay, bName, false); 
	return atom;
}
}

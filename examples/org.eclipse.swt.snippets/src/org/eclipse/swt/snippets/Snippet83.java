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
 * Drag and Drop example snippet: determine data types available (win32 only)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet83 extends ByteArrayTransfer {

private static Snippet83 _instance = new Snippet83();
private int[] ids;
private String[] names;
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	Canvas canvas = new Canvas(shell, SWT.NONE);
	DropTarget target = new DropTarget(canvas, DND.DROP_DEFAULT | DND.DROP_LINK);
	target.setTransfer(new Transfer[] {Snippet83.getInstance()});
	target.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {			
			String ops = null;
			if ((event.operations & DND.DROP_COPY) != 0) ops += "Copy;";
			if ((event.operations & DND.DROP_MOVE) != 0) ops += "Move;";
			if ((event.operations & DND.DROP_LINK) != 0) ops += "Link;";
			System.out.println("Allowed Operations are "+ops);
			
			TransferData[] data = event.dataTypes;
			for (int i = 0; i < data.length; i++) {
				int id = data[i].type;
				String name = getNameFromId(id);
				System.out.println("Data type is "+id+" "+name);
			}
		}
	});
	
	shell.setSize(400, 400);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

public static Snippet83 getInstance () {
	return _instance;
}
Snippet83() {
	ids = new int[50000];
	names = new String[50000];
	for (int i = 0; i < ids.length; i++) {
		ids[i] = i;
		names[i] = getNameFromId(i);
	}
}
public void javaToNative (Object object, TransferData transferData) {
}
public Object nativeToJava(TransferData transferData){
	return "Hello World";
}
protected String[] getTypeNames(){
	return names;
}
protected int[] getTypeIds(){
	return ids;
}
static String getNameFromId(int id) {
	String name = null;
	int maxSize = 128;
	TCHAR buffer = new TCHAR(0, maxSize);
	int size = COM.GetClipboardFormatName(id, buffer, maxSize);
	String type = null;
	if (size != 0) {
		name = buffer.toString(0, size);
	} else {
		switch (id) {
			case COM.CF_HDROP:
				name = "CF_HDROP";
				break;
			case COM.CF_TEXT:
				name = "CF_TEXT";
				break;
			case COM.CF_BITMAP:
				name = "CF_BITMAP";
				break;
			case COM.CF_METAFILEPICT:
				name = "CF_METAFILEPICT";
				break;
			case COM.CF_SYLK:
				name = "CF_SYLK";
				break;
			case COM.CF_DIF:
				name = "CF_DIF";
				break;
			case COM.CF_TIFF:
				name = "CF_TIFF";
				break;
			case COM.CF_OEMTEXT:
				name = "CF_OEMTEXT";
				break;
			case COM.CF_DIB:
				name = "CF_DIB";
				break;
			case COM.CF_PALETTE:
				name = "CF_PALETTE";
				break;
			case COM.CF_PENDATA:
				name = "CF_PENDATA";
				break;
			case COM.CF_RIFF:
				name = "CF_RIFF";
				break;
			case COM.CF_WAVE:
				name = "CF_WAVE";
				break;
			case COM.CF_UNICODETEXT:
				name = "CF_UNICODETEXT";
				break;
			case COM.CF_ENHMETAFILE:
				name = "CF_ENHMETAFILE";
				break;
			case COM.CF_LOCALE:
				name = "CF_LOCALE";
				break;
			case COM.CF_MAX:
				name = "CF_MAX";
				break;
		}
		
	}
	return name;
}
}

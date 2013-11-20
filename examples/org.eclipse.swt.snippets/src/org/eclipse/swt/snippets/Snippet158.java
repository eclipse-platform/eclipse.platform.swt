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
package org.eclipse.swt.snippets;
 
/*
 * Drag and Drop example snippet: determine native data types available (motif only)
 * NOTE: This snippet uses internal SWT packages that are
 * subject to change without notice.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet158 extends ByteArrayTransfer {

private static Snippet158 _instance = new Snippet158();
private int[] ids;
private String[] names;
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Table control = new Table(shell, SWT.NONE);
	TableItem item = new TableItem(control, SWT.NONE);
	item.setText("Drag data over this site to see the native transfer type.");
	DropTarget target = new DropTarget(control, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_MOVE);
	target.setTransfer(new Transfer[] {Snippet158.getInstance()});
	target.addDropListener(new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent event) {			
			String ops = "";
			if ((event.operations & DND.DROP_COPY) != 0) ops += "Copy;";
			if ((event.operations & DND.DROP_MOVE) != 0) ops += "Move;";
			if ((event.operations & DND.DROP_LINK) != 0) ops += "Link;";
			control.removeAll();
			TableItem item1 = new TableItem(control,SWT.NONE);
			item1.setText("Allowed Operations are "+ops);
			
			if (event.detail == DND.DROP_DEFAULT) {
				if ((event.operations & DND.DROP_COPY) != 0) {
					event.detail = DND.DROP_COPY;
				} else if ((event.operations & DND.DROP_LINK) != 0) {
					event.detail = DND.DROP_LINK;
				} else if ((event.operations & DND.DROP_MOVE) != 0) {
					event.detail = DND.DROP_MOVE;
				}
			}
			
			TransferData[] data = event.dataTypes;
			for (int i = 0; i < data.length; i++) {
				int id = data[i].type;
				String name = getNameFromId(id);
				TableItem item2 = new TableItem(control,SWT.NONE);
				item2.setText("Data type is "+id+" "+name);
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

public static Snippet158 getInstance () {
	return _instance;
}
Snippet158() {
}
@Override
public void javaToNative (Object object, TransferData transferData) {
}
@Override
public Object nativeToJava(TransferData transferData){
	return "Hello World";
}
@Override
protected String[] getTypeNames(){
	return names;
}
static int shellHandle;
@Override
protected int[] getTypeIds(){
	if (ids == null) {
		Display display = Display.getCurrent();
		int widgetClass = OS.topLevelShellWidgetClass ();
		shellHandle = OS.XtAppCreateShell (null, null, widgetClass, display.xDisplay, null, 0);
		OS.XtSetMappedWhenManaged (shellHandle, false);
		OS.XtRealizeWidget (shellHandle);
		
		ids = new int[840];
		names = new String[840];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = i+i;
			names[i] = getNameFromId(i+1);
		}
	}
	return ids;
}
static String getNameFromId(int id) {
	int xDisplay = OS.XtDisplay (shellHandle);
	int ptr = 0;
	try {
		ptr = OS.XmGetAtomName(xDisplay, id);
	} catch (Throwable t) {
	}
	if (ptr == 0) return "invalid "+id;
	int length = OS.strlen(ptr);
	byte[] nameBuf = new byte[length];
	OS.memmove(nameBuf, ptr, length);
	OS.XFree(ptr);
	return new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
}
}

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
package org.eclipse.swt.snippets;

/*
 * Drag and Drop example snippet: drag an object with a custom data transfer
 * type between SWT and AWT/Swing.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.awt.Frame;
import java.awt.datatransfer.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.io.*;
import java.util.Date;
import javax.swing.JLabel;

import org.eclipse.swt.*;
import org.eclipse.swt.awt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet319 {

	/* The data flavor must be MIME type-like */
	static final String MIME_TYPE = "custom/myType"; // $NON-NLS-1$

	/* The object being transferred is defined as: */
	static class MyType {
		String name;
		long time;
	}

public static void main(String[] args) {
	new Snippet319().go();
}

public void go() {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,600,200);

	/* Create SWT controls and add drag source */
	final Label swtLabel = new Label(shell, SWT.BORDER);
	swtLabel.setBounds(10,10,580,50);
	swtLabel.setText("SWT drag source");
	DragSource dragSource = new DragSource(swtLabel, DND.DROP_COPY);
	Transfer[] transfers = new Transfer[] {new MyTypeTransfer()};
	dragSource.setTransfer(transfers);
	dragSource.addDragListener(new DragSourceAdapter() {
		public void dragSetData(DragSourceEvent event) {
			MyType object = new MyType();
			object.name = "content dragged from SWT";
			object.time = System.currentTimeMillis();
			event.data = object;
		}
	});

	/* Create AWT/Swing controls */
	Composite embeddedComposite = new Composite(shell, SWT.EMBEDDED);
	embeddedComposite.setBounds(10, 100, 580, 50);
	embeddedComposite.setLayout(new FillLayout());
	Frame frame = SWT_AWT.new_Frame(embeddedComposite);
	final JLabel jLabel = new JLabel("AWT/Swing drop target");
	frame.add(jLabel);

	/* Register the custom data flavour */
	final DataFlavor flavor = new DataFlavor(MIME_TYPE, "MyType custom flavor");
	/* 
	 * Note that according to jre/lib/flavormap.properties, the preferred way to
	 * augment the default system flavor map is to specify the AWT.DnD.flavorMapFileURL
	 * property in an awt.properties file.
	 * 
	 * This snippet uses the alternate approach below in order to provide a simple
	 * stand-alone snippet that demonstrates the functionality.  This implementation
	 * works well, but if the instanceof check below fails for some reason when used
	 * in a different context then the drop will not be accepted.
	 */
	FlavorMap map = SystemFlavorMap.getDefaultFlavorMap();
	if (map instanceof SystemFlavorMap) {
		SystemFlavorMap systemMap = (SystemFlavorMap)map;
		systemMap.addFlavorForUnencodedNative(MIME_TYPE, flavor);
	}

	/* add drop target */
	DropTargetListener dropTargetListener = new DropTargetAdapter() {
		public void drop(DropTargetDropEvent dropTargetDropEvent) {
			try {
				dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
				ByteArrayInputStream inStream = (ByteArrayInputStream)dropTargetDropEvent.getTransferable().getTransferData(flavor);
				int available = inStream.available();
				byte[] bytes = new byte[available];
				inStream.read(bytes);
				MyType object = restoreFromByteArray(bytes);
				String string = object.name + ": " + new Date(object.time).toString();
				jLabel.setText(string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	new DropTarget(jLabel, dropTargetListener);

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}


/* SWT custom data transfer implementation */

class MyTypeTransfer extends ByteArrayTransfer {

	final int MIME_TYPE_ID = registerType(MIME_TYPE);

	protected int[] getTypeIds() {
		return new int[] {MIME_TYPE_ID};
	}

	protected String[] getTypeNames() {
		return new String[] {MIME_TYPE};
	}

public void javaToNative(Object object, TransferData transferData) {
	if (!checkMyType(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	MyType myType = (MyType)object;
	byte[] bytes = convertToByteArray(myType);
	if (bytes != null) {
		super.javaToNative(bytes, transferData);
	}
}

public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData)) return null;
	byte[] bytes = (byte[])super.nativeToJava(transferData);
	return bytes == null ? null : restoreFromByteArray(bytes);
}

boolean checkMyType(Object object) {
	if (object == null) return false;
	MyType myType = (MyType)object;
	return myType != null && myType.name != null && myType.name.length() > 0 && myType.time > 0;
}

protected boolean validate(Object object) {
	return checkMyType(object);
}
}


/* shared methods for converting instances of MyType <-> byte[] */

static byte[] convertToByteArray(MyType type) {
	DataOutputStream dataOutStream = null;
	try {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		dataOutStream = new DataOutputStream(byteOutStream);
		byte[] bytes = type.name.getBytes();
		dataOutStream.writeInt(bytes.length);
		dataOutStream.write(bytes);
		dataOutStream.writeLong(type.time);
		return byteOutStream.toByteArray();
	} catch (IOException e) {
		return null;
	} finally {
		if (dataOutStream != null) {
			try {
				dataOutStream.close();
			} catch (IOException e) {}
		}
	}
}

static MyType restoreFromByteArray(byte[] bytes) {
	DataInputStream dataInStream = null;
	try {
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(bytes);
		dataInStream = new DataInputStream(byteInStream);
		int size = dataInStream.readInt();
		byte[] name = new byte[size];
		dataInStream.read(name);
		MyType result = new MyType();
		result.name = new String(name);
		result.time = dataInStream.readLong();
		return result;
	} catch (IOException ex) {
		return null;
	} finally {
		if (dataInStream != null) {
			try {
				dataInStream.close();
			} catch (IOException e) {}
		}
	}
}

}

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
package org.eclipse.swt.snippets;

/*
 * Make a dropped data type depend on a target item in table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet185 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	Label label1 = new Label(shell, SWT.BORDER);
	label1.setText("Drag Source");
	final Table table = new Table(shell, SWT.BORDER);
	for (int i = 0; i < 4; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		if (i % 2 == 0) item.setText("Drop a file");
		if (i % 2 == 1) item.setText("Drop text");
	}
	DragSource dragSource = new DragSource(label1, DND.DROP_COPY);
	dragSource.setTransfer(TextTransfer.getInstance(), FileTransfer.getInstance());
	dragSource.addDragListener(new DragSourceAdapter() {
		@Override
		public void dragSetData(DragSourceEvent event) {
			if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
				File file = new File("temp");
				event.data = new String[] {file.getAbsolutePath()};
			}
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = "once upon a time";
			}
		}
	});
	DropTarget dropTarget = new DropTarget(table, DND.DROP_COPY | DND.DROP_DEFAULT);
	dropTarget.setTransfer(TextTransfer.getInstance(), FileTransfer.getInstance());
	dropTarget.addDropListener(new DropTargetAdapter() {
		FileTransfer fileTransfer = FileTransfer.getInstance();
		TextTransfer textTransfer = TextTransfer.getInstance();
		@Override
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		@Override
		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		@Override
		public void dragOver(DropTargetEvent event) {
			event.detail = DND.DROP_NONE;
			TableItem item = (TableItem)event.item;
			if (item == null) return;
			int itemIndex = table.indexOf(item);
			if (itemIndex % 2 == 0) {
				int index = 0;
				while (index < event.dataTypes.length) {
					if (fileTransfer.isSupportedType(event.dataTypes[index])) break;
					index++;
				}
				if (index < event.dataTypes.length) {
					event.currentDataType = event.dataTypes[index];
					event.detail = DND.DROP_COPY;
					return;
				}
			} else {
				int index = 0;
				while (index < event.dataTypes.length) {
					if (textTransfer.isSupportedType(event.dataTypes[index])) break;
					index++;
				}
				if (index < event.dataTypes.length) {
					event.currentDataType = event.dataTypes[index];
					event.detail = DND.DROP_COPY;
					return;
				}
			}
		}

		@Override
		public void drop(DropTargetEvent event) {
			TableItem item = (TableItem)event.item;
			if (item == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			if (fileTransfer.isSupportedType(event.currentDataType)) {
				String[] files = (String[])event.data;
				if (files != null && files.length > 0) {
					item.setText(files[0]);
				}
			}
			if (textTransfer.isSupportedType(event.currentDataType)) {
				String text = (String)event.data;
				if (text != null) {
					item.setText(text);
				}
			}
		}

	});
	shell.setSize(300, 150);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
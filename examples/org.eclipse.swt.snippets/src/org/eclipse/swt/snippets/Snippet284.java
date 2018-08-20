/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
 * Drag and Drop example snippet: drag a URL between two labels.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet284 {

public static void main (String [] args) {

	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setText("URLTransfer");
	shell.setLayout(new FillLayout());
	final Label label1 = new Label (shell, SWT.BORDER);
	label1.setText ("http://www.eclipse.org");
	final Label label2 = new Label (shell, SWT.BORDER);
	setDragSource (label1);
	setDropTarget (label2);
	shell.setSize(600, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
public static void setDragSource (final Label label) {
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	final DragSource source = new DragSource (label, operations);
	source.setTransfer(URLTransfer.getInstance());
	source.addDragListener (new DragSourceListener () {
		@Override
		public void dragStart(DragSourceEvent e) {
		}
		@Override
		public void dragSetData(DragSourceEvent e) {
			e.data = label.getText();
		}
		@Override
		public void dragFinished(DragSourceEvent event) {
		}
	});
}

public static void setDropTarget (final Label label) {
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	DropTarget target = new DropTarget(label, operations);
	target.setTransfer(URLTransfer.getInstance());
	target.addDropListener (new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent e) {
			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void dragOperationChanged(DropTargetEvent e) {
			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			label.setText(((String) event.data));
		}
	});
}
}

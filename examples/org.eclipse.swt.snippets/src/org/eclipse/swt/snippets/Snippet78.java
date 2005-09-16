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
 * Drag and Drop example snippet: drag text between two labels
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet78 {

public static void main (String [] args) {
	
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Label label1 = new Label (shell, SWT.BORDER);
	label1.setText ("TEXT");
	final Label label2 = new Label (shell, SWT.BORDER);
	setDragDrop (label1);
	setDragDrop (label2);
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
public static void setDragDrop (final Label label) {
	
	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	
	final DragSource source = new DragSource (label, operations);
	source.setTransfer(types);
	source.addDragListener (new DragSourceListener () {
		public void dragStart(DragSourceEvent event) {
			event.doit = (label.getText ().length () != 0);
		}
		public void dragSetData (DragSourceEvent event) {
			event.data = label.getText ();
		}
		public void dragFinished(DragSourceEvent event) {
			if (event.detail == DND.DROP_MOVE)
				label.setText ("");
		}
	});

	DropTarget target = new DropTarget(label, operations);
	target.setTransfer(types);
	target.addDropListener (new DropTargetAdapter() {
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			label.setText ((String) event.data);
		}
	});
}
}

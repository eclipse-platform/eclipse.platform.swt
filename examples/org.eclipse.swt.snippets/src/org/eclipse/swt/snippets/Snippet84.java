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
 * Drag and Drop example snippet: define a default operation (in this example, Copy)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet84 {
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Text text = new Text(shell, SWT.BORDER | SWT.MULTI);
	DropTarget target = new DropTarget(text, DND.DROP_DEFAULT | DND.DROP_COPY);
	target.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	target.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		public void drop(DropTargetEvent event) {
			text.setText((String)event.data);
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
}

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
 * Clipboard example snippet: copy and paste data with the clipboard
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet94 {

public static void main( String[] args) {
	Display display = new Display ();
	final Clipboard cb = new Clipboard(display);
	final Shell shell = new Shell (display);
	shell.setLayout(new FormLayout());
	final Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	
	Button copy = new Button(shell, SWT.PUSH);
	copy.setText("Copy");
	copy.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			String textData = text.getSelectionText();
			if (textData.length() > 0) {
				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[]{textData}, new Transfer[]{textTransfer});
			}
		}
	});
	
	Button paste = new Button(shell, SWT.PUSH);
	paste.setText("Paste");
	paste.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			TextTransfer transfer = TextTransfer.getInstance();
			String data = (String)cb.getContents(transfer);
			if (data != null) {
				text.insert(data);
			}
		}
	});
	
	FormData data = new FormData();
	data.left = new FormAttachment(paste, 0, SWT.LEFT);
	data.right = new FormAttachment(100, -5);
	data.top = new FormAttachment(0, 5);
	copy.setLayoutData(data);
	
	data = new FormData();
	data.right = new FormAttachment(100, -5);
	data.top = new FormAttachment(copy, 5);
	paste.setLayoutData(data);
	
	data = new FormData();
	data.left = new FormAttachment(0, 5);
	data.top = new FormAttachment(0, 5);
	data.right = new FormAttachment(paste, -5);
	data.bottom = new FormAttachment(100, -5);
	text.setLayoutData(data);
	
	shell.setSize(200, 200);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	cb.dispose();
	display.dispose();
}
}

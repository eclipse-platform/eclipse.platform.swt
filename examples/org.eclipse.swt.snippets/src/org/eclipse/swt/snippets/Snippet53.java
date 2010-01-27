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
 * Table example snippet: remove selected items (using popup menu)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet53 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
	table.setSize (200, 200);
	for (int i=0; i<128; i++) {
		TableItem item = new TableItem (table, SWT.NONE);
		item.setText ("Item " + i);
	}
	Menu menu = new Menu (shell, SWT.POP_UP);
	table.setMenu (menu);
	MenuItem item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Delete Selection");
	item.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			table.remove (table.getSelectionIndices ());
		}
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

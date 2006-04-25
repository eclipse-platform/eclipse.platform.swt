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
 * Menu example snippet: create a bar and pull down menu (accelerators, mnemonics)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet29 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Menu bar = new Menu (shell, SWT.BAR);
	shell.setMenuBar (bar);
	MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText ("&File");
	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
	fileItem.setMenu (submenu);
	MenuItem item = new MenuItem (submenu, SWT.PUSH);
	item.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			System.out.println ("Select All");
		}
	});
	item.setText ("Select &All\tCtrl+A");
	item.setAccelerator (SWT.MOD1 + 'A');
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

} 

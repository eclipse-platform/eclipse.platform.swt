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
 * ToolBar example snippet: place a drop down menu in a tool bar
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet67 {

public static void main (String [] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	final ToolBar toolBar = new ToolBar (shell, SWT.NONE);
	Rectangle clientArea = shell.getClientArea ();
	toolBar.setLocation(clientArea.x, clientArea.y);
	final Menu menu = new Menu (shell, SWT.POP_UP);
	for (int i=0; i<8; i++) {
		MenuItem item = new MenuItem (menu, SWT.PUSH);
		item.setText ("Item " + i);
	}
	final ToolItem item = new ToolItem (toolBar, SWT.DROP_DOWN);
	item.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			if (event.detail == SWT.ARROW) {
				Rectangle rect = item.getBounds ();
				Point pt = new Point (rect.x, rect.y + rect.height);
				pt = toolBar.toDisplay (pt);
				menu.setLocation (pt.x, pt.y);
				menu.setVisible (true);
			}
		}
	});
	toolBar.pack ();
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	menu.dispose ();
	display.dispose ();
}
} 

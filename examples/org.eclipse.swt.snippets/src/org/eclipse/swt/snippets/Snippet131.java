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
 * Menu example snippet: show a popup menu (wait for it to close)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet131 {
public static void main (String [] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.addListener (SWT.MenuDetect, new Listener () {
		@Override
		public void handleEvent (Event event) {
			Menu menu = new Menu (shell, SWT.POP_UP);
			MenuItem item = new MenuItem (menu, SWT.PUSH);
			item.setText ("Menu Item");
			item.addListener (SWT.Selection, new Listener () {
				@Override
				public void handleEvent (Event e) {
					System.out.println ("Item Selected");
				}
			});
			menu.setLocation (event.x, event.y);
			menu.setVisible (true);
			while (!menu.isDisposed () && menu.isVisible ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			menu.dispose ();
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

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
 * Menu example snippet: enable menu items dynamically (when menu shown)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet73 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	final Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI);
	final Menu menu = new Menu (shell, SWT.POP_UP);
	tree.setMenu (menu);
	for (int i=0; i<12; i++) {
		TreeItem treeItem = new TreeItem (tree, SWT.NONE);
		treeItem.setText ("Item " + i);
		MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText (treeItem.getText ());
	}
	menu.addListener (SWT.Show, new Listener () {
		public void handleEvent (Event event) {
			MenuItem [] menuItems = menu.getItems ();
			TreeItem [] treeItems = tree.getSelection ();
			for (int i=0; i<menuItems.length; i++) {
				String text = menuItems [i].getText ();
				int index = 0;
				while (index<treeItems.length) {
					if (treeItems [index].getText ().equals (text)) break;
					index++;
				}
				menuItems [i].setEnabled (index != treeItems.length);
			}
		}
	});
	Rectangle clientArea = shell.getClientArea ();
	tree.setBounds (clientArea.x, clientArea.y, 200, 200);
	shell.setSize (300, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

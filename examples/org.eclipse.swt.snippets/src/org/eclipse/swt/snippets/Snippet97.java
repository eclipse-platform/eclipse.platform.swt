/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 * Menu example snippet: fill a menu dynamically (when menu shown)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet97 {

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	final Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI);
	final Menu menu = new Menu (shell, SWT.POP_UP);
	tree.setMenu (menu);
	for (int i=0; i<12; i++) {
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText ("Item " + i);
	}
	menu.addListener (SWT.Show, event -> {
		MenuItem [] menuItems = menu.getItems ();
		for (int i1=0; i1<menuItems.length; i1++) {
			menuItems [i1].dispose ();
		}
		TreeItem [] treeItems = tree.getSelection ();
		for (int i2=0; i2<treeItems.length; i2++) {
			MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
			menuItem.setText (treeItems [i2].getText ());
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

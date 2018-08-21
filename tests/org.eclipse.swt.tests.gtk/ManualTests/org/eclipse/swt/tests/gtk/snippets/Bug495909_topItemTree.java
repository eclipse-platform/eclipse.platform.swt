package org.eclipse.swt.tests.gtk.snippets;
/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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


/*
 * List example snippet: print selected items in a widget.list
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Bug495909_topItemTree {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	final Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	for (int i=0; i<128; i++) new TreeItem (tree, 0).setText("Item " + i);
	tree.setTopItem(tree.getItem(40));
	System.out.println(tree.getTopItem().getText());
	Rectangle clientArea = shell.getClientArea ();
	tree.setBounds (clientArea.x, clientArea.y, 100, 100);
	tree.addListener (SWT.MouseWheel, e -> {
		System.out.println(tree.getTopItem().getText());
	});
	tree.addListener (SWT.Selection, e -> {
		System.out.println(tree.getTopItem().getText());
	});
	tree.setTopItem(tree.getItem(20));
	System.out.println(tree.getTopItem().getText());
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

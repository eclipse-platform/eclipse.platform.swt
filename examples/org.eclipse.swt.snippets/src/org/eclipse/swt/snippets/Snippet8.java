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

import java.io.*;

/*
 * Tree example snippet: create a tree (lazy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet8 {

public static void main (String [] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setText ("Lazy Tree");
	shell.setLayout (new FillLayout ());
	final Tree tree = new Tree (shell, SWT.BORDER);
	File [] roots = File.listRoots ();
	for (int i=0; i<roots.length; i++) {
		TreeItem root = new TreeItem (tree, 0);
		root.setText (roots [i].toString ());
		root.setData (roots [i]);
		new TreeItem (root, 0);
	}
	tree.addListener (SWT.Expand, event -> {
		final TreeItem root = (TreeItem) event.item;
		TreeItem [] items = root.getItems ();
		for (int i1= 0; i1<items.length; i1++) {
			if (items [i1].getData () != null) return;
			items [i1].dispose ();
		}
		File file = (File) root.getData ();
		File [] files = file.listFiles ();
		if (files == null) return;
		for (int i2= 0; i2<files.length; i2++) {
			TreeItem item = new TreeItem (root, 0);
			item.setText (files [i2].getName ());
			item.setData (files [i2]);
			if (files [i2].isDirectory()) {
				new TreeItem (item, 0);
			}
		}
	});
	Point size = tree.computeSize (300, SWT.DEFAULT);
	int width = Math.max (300, size.x);
	int height = Math.max (300, size.y);
	shell.setSize (shell.computeSize (width, height));
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

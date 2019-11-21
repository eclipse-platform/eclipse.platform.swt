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
 * Tree example snippet: create a tree (lazy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.io.*;

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
	for (File rootFile : File.listRoots ()) {
		TreeItem root = new TreeItem (tree, 0);
		root.setText (rootFile.toString ());
		root.setData (rootFile);
		new TreeItem (root, 0);
	}
	tree.addListener (SWT.Expand, event -> {
		final TreeItem root = (TreeItem) event.item;
		TreeItem [] items = root.getItems ();
		for (TreeItem item : items) {
			if (item.getData () != null) return;
			item.dispose ();
		}
		File file = (File) root.getData ();
		File [] files = file.listFiles ();
		if (files == null) return;
		for (File rootFile : files) {
			TreeItem item = new TreeItem (root, 0);
			item.setText (rootFile.getName ());
			item.setData (rootFile);
			if (rootFile.isDirectory()) {
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

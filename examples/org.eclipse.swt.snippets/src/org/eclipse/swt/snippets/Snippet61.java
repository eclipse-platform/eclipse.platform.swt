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
 * Tree example snippet: print selected items in a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet61 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	for (int i=0; i<4; i++) {
		TreeItem item0 = new TreeItem (tree, 0);
		item0.setText ("Item " + i);
		for (int j=0; j<4; j++) {
			TreeItem item1 = new TreeItem (item0, 0);
			item1.setText ("SubItem " + i + " " + j);
			for (int k=0; k<4; k++) {
				TreeItem item2 = new TreeItem (item1, 0);
				item2.setText ("SubItem " + i + " " + j + " " + k);
			}
		}
	}
	tree.addListener (SWT.Selection, e -> {
		String string = "";
		TreeItem [] selection = tree.getSelection ();
		for (int i=0; i<selection.length; i++) string += selection [i] + " ";
		System.out.println ("Selection={" + string + "}");
	});
	tree.addListener (SWT.DefaultSelection, e -> {
		String string = "";
		TreeItem [] selection = tree.getSelection ();
		for (int i=0; i<selection.length; i++) string += selection [i] + " ";
		System.out.println ("DefaultSelection={" + string + "}");
	});
	tree.addListener (SWT.Expand, e -> System.out.println ("Expand={" + e.item + "}"));
	tree.addListener (SWT.Collapse, e -> System.out.println ("Collapse={" + e.item + "}"));
	tree.getItems () [0].setExpanded (true);
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

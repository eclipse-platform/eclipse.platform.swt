/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
 * Tree example snippet: search for a string in a tree (recursively)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet287 {

static Tree tree;
public static void main(String[] args) {
	final String SEARCH_STRING = "4b";

	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setBounds (10,10,300,300);
	shell.setLayout (new GridLayout ());

	/* create the Tree */
	tree = new Tree (shell, SWT.FULL_SELECTION);
	tree.setLinesVisible (true);
	tree.setLayoutData (new GridData (GridData.FILL_BOTH));
	for (int i = 0; i < 3; i++) {
		new TreeColumn (tree, SWT.NONE).setWidth (90);
	}
	int index = 0;
	for (int i = 0; i < 3; i++) {
		TreeItem item = createItem (null, index++);
		for (int j = 0; j < i; j++) {
			item = createItem (item, index++);
		}
	}

	Button button = new Button (shell, SWT.PUSH);
	button.setText ("Find '" + SEARCH_STRING + "'");
	button.addListener (SWT.Selection, event -> {
		int itemCount = tree.getItemCount ();
		for (int i = 0; i < itemCount; i++) {
			TreeItem item = tree.getItem (i);
			boolean success = find (item, SEARCH_STRING);
			if (success) {
				System.out.println ("Found it");
				return;
			}
		}
		System.out.println ("Did not find it");
	});
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

/* for creating sample Tree */
static TreeItem createItem (TreeItem parent, int itemIndex) {
	TreeItem newItem = null;
	if (parent == null) {	/* root level item */
		newItem = new TreeItem (tree, SWT.NONE);
	} else {
		newItem = new TreeItem (parent, SWT.NONE);
	}
	String indexString = String.valueOf (itemIndex);
	newItem.setText(new String[] {
		indexString + 'a', indexString + 'b', indexString + 'c'});
	return newItem;
}

/* recursive find */
public static boolean find (TreeItem item, String searchString) {
	/* check this item */
	for (int i = 0; i < tree.getColumnCount (); i++) {
		String contents = item.getText (i);
		if ((contents.toUpperCase ().indexOf (searchString.toUpperCase ())) != -1) {
			tree.setSelection (item);
			return true;
		}
	}

	if (!item.getExpanded ()) return false; /* don't check child items */

	/* check child items */
	int childCount = item.getItemCount ();
	for (int i = 0; i < childCount; i++) {
		TreeItem child = item.getItem (i);
		boolean success = find (child, searchString);
		if (success) return true;
	}

	return false;
}
}

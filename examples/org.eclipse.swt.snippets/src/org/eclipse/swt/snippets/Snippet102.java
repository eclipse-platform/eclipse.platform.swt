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
 * Tree example snippet: insert a tree item (at an index)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet102 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Tree tree = new Tree (shell, SWT.BORDER | SWT.MULTI);
	Rectangle clientArea = shell.getClientArea ();
	tree.setBounds (clientArea.x, clientArea.y, 200, 200);
	for (int i=0; i<12; i++) {
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText ("Item " + i);
	}
	TreeItem item = new TreeItem (tree, SWT.NONE, 1);
	TreeItem [] items = tree.getItems ();
	int index = 0;
	while (index < items.length && items [index] != item) index++;
	item.setText ("*** New Item " + index + " ***");
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}

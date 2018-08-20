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
 * Tree example snippet: detect a selection or check event in a tree (SWT.CHECK)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet114 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Tree tree = new Tree (shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	for (int i=0; i<12; i++) {
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText ("Item " + i);
	}
	Rectangle clientArea = shell.getClientArea ();
	tree.setBounds (clientArea.x, clientArea.y, 100, 100);
	tree.addListener (SWT.Selection, event -> {
		String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
		System.out.println (event.item + " " + string);
	});
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

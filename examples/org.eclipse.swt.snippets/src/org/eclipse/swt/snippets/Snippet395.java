/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - viewport mode
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * VirtualPageSource example snippet: a virtual tree whose children are fetched
 * only when a node is expanded. Nodes whose child count is not known yet report
 * VirtualPageSource.UNKNOWN; while their children load, the node shows one
 * "loading" row that is drawn from the parent's state, not a TreeItem.
 *
 * For a list of all SWT example snippets see
 * https://eclipse.dev/eclipse/swt/snippets/
 *
 * @since 3.137
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet395 {

static final int ROOTS = 1_000;
static final int CHILDREN = 1_000;

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText ("Snippet 395");
	shell.setLayout (new FillLayout ());

	Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
	VirtualPageSource source = new VirtualPageSource (64, 64, 1, VirtualPageSource.TREE, request -> {
		Thread.sleep (20); // a slow source, on a worker thread
		int parent = request.getParentKey ();
		if (parent == VirtualPageSource.ROOT) {
			request.setTotal (ROOTS);
			for (int i = request.getStart (), end = Math.min (ROOTS, i + request.getCount ()); i < end; i++) {
				request.setText (i, 0, "Folder " + i);
				request.setKey (i, i);
				request.setChildCount (i, VirtualPageSource.UNKNOWN);
			}
		} else {
			request.setTotal (CHILDREN);
			for (int i = request.getStart (), end = Math.min (CHILDREN, i + request.getCount ()); i < end; i++) {
				request.setText (i, 0, "File " + parent + "." + i);
				request.setKey (i, -1);
				request.setChildCount (i, 0);
			}
		}
	});
	source.setPlaceholderText ("loading...");
	source.attach (tree);

	shell.setSize (400, 600);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

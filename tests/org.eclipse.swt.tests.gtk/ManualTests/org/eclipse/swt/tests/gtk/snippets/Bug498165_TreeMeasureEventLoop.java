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
 * Tree example snippet: create a widget.tree
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug498165_TreeMeasureEventLoop {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree (shell, SWT.BORDER);
	new TreeItem (tree, 0);

	tree.addListener(SWT.MeasureItem, new Listener() {
		
		int height = -1;
		
		@Override
		public void handleEvent(Event event) {
			if (height == -1) {
				height = ((TreeItem)event.item).getImageBounds(0).height;
			}
			event.height = height + 3;
		}
	});
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

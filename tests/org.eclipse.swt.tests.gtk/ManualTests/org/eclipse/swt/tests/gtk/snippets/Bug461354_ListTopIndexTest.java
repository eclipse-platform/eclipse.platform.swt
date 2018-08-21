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

public class Bug461354_ListTopIndexTest {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	final List list = new List (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	for (int i=0; i<128; i++) list.add ("Item " + i);
	list.setTopIndex(40);
	System.out.println(list.getTopIndex());
	Rectangle clientArea = shell.getClientArea ();
	list.setBounds (clientArea.x, clientArea.y, 100, 100);
	list.addListener (SWT.Selection, e -> {
		int [] selection = list.getSelectionIndices ();
		for (int i=0; i<selection.length; i++) {
		}
		System.out.println ("topIndex" + list.getTopIndex());
	});
	list.addListener (SWT.DefaultSelection, e -> {
		int [] selection = list.getSelectionIndices ();
		for (int i=0; i<selection.length; i++)
		 {
		}
	});
	list.addListener (SWT.MouseWheel, e -> {
		System.out.println(list.getTopIndex());
	});
	list.setTopIndex(4);
//	widget.list.setSelection(60);
//	widget.list.setTopIndex(40);
	System.out.println(list.getTopIndex());
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

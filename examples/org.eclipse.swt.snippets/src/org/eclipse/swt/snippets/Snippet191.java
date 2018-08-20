/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Detect when the user scrolls a text control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

public class Snippet191 {
public static void main(String[] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Text text = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	for (int i=0; i<32; i++) {
		text.append (i + "-This is a line of text in a widget-" + i + "\n");
	}
	text.setSelection (0);
	Listener listener = new Listener () {
		int lastIndex = text.getTopIndex ();
		@Override
		public void handleEvent (Event e) {
			int index = text.getTopIndex ();
			if (index != lastIndex) {
				lastIndex = index;
				System.out.println ("Scrolled, topIndex=" + index);
			}
		}
	};
	/* NOTE: Only detects scrolling by the user */
	text.addListener (SWT.MouseDown, listener);
	text.addListener (SWT.MouseMove, listener);
	text.addListener (SWT.MouseUp, listener);
	text.addListener (SWT.KeyDown, listener);
	text.addListener (SWT.KeyUp, listener);
	text.addListener (SWT.Resize, listener);
	ScrollBar hBar = text.getHorizontalBar();
	if (hBar != null) {
		hBar.addListener (SWT.Selection, listener);
	}
	ScrollBar vBar = text.getVerticalBar();
	if (vBar != null) {
		vBar.addListener (SWT.Selection, listener);
	}
	shell.pack ();
	Point size = shell.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	shell.setSize (size. x - 32, size.y / 2);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
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
 * Composite example snippet: intercept mouse events (drag a button with the mouse)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet46 {
public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	final Composite composite = new Composite (shell, SWT.NONE);
	composite.setEnabled (false);
	composite.setLayout (new FillLayout ());
	Button button = new Button (composite, SWT.PUSH);
	button.setText ("Button");
	composite.pack ();
	composite.setLocation (10, 10);
	final Point [] offset = new Point [1];
	Listener listener = event -> {
		switch (event.type) {
			case SWT.MouseDown:
				Rectangle rect = composite.getBounds ();
				if (rect.contains (event.x, event.y)) {
					Point pt1 = composite.toDisplay (0, 0);
					Point pt2 = shell.toDisplay (event.x, event.y);
					offset [0] = new Point (pt2.x - pt1.x, pt2.y - pt1.y);
				}
				break;
			case SWT.MouseMove:
				if (offset [0] != null) {
					Point pt = offset [0];
					composite.setLocation (event.x - pt.x, event.y - pt.y);
				}
				break;
			case SWT.MouseUp:
				offset [0] = null;
				break;
		}
	};
	shell.addListener (SWT.MouseDown, listener);
	shell.addListener (SWT.MouseUp, listener);
	shell.addListener (SWT.MouseMove, listener);
	shell.setSize (300, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

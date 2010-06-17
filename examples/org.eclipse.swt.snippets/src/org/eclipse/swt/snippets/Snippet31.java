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
 * Tracker example snippet: create a tracker (drag when "torn off")
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet31 {

	static Tracker tracker = null;
public static void main (String [] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setSize (200, 200);
	shell.open ();
	Listener listener = new Listener () {
		Point point = null;
		static final int JITTER = 8;
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.MouseDown:
					point = new Point (event.x, event.y);
					break;
				case SWT.MouseMove:
					if (point == null) return;
					int deltaX = point.x - event.x, deltaY = point.y - event.y;
					if (Math.abs (deltaX) < JITTER && Math.abs (deltaY) < JITTER) {
						return;
					}
					tracker = new Tracker (display, SWT.NONE);
					Rectangle rect = display.map (shell, null, shell.getClientArea ());
					rect.x -= deltaX;
					rect.y -= deltaY;
					tracker.setRectangles (new Rectangle [] {rect});
					tracker.open ();
					//FALL THROUGH
				case SWT.MouseUp:
					point = null;
					if (tracker != null) {
						tracker.dispose ();
						tracker = null;
					}
					break;
			}
		}
	};
	shell.addListener (SWT.MouseDown, listener);
	shell.addListener (SWT.MouseMove, listener);
	shell.addListener (SWT.MouseUp, listener);
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

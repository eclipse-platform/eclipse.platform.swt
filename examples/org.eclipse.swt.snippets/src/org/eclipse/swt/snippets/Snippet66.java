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

/*
 * GC example snippet: implement a simple scribble program
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet66 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	Listener listener = new Listener () {
		int lastX = 0, lastY = 0;
		@Override
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.MouseMove:
					if ((event.stateMask & SWT.BUTTON1) == 0) break;
					GC gc = new GC (shell);
					gc.drawLine (lastX, lastY, event.x, event.y);
					gc.dispose ();
					//FALL THROUGH
				case SWT.MouseDown:
					lastX = event.x;
					lastY = event.y;
					break;
			}
		}
	};
	shell.addListener (SWT.MouseDown, listener);
	shell.addListener (SWT.MouseMove, listener);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

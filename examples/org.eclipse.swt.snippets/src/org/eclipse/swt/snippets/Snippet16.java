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
 * Display example snippet: create one repeating timer (every 500 ms)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet16 {

public static void main (String [] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	final int time = 500;
	Runnable timer = new Runnable () {
		@Override
		public void run () {
			if (shell.isDisposed()) return;
			Point point = display.getCursorLocation ();
			Rectangle rect = shell.getBounds ();
			if (rect.contains (point)) {
				System.out.println ("In");
			} else {
				System.out.println ("Out");
			}
			display.timerExec (time, this);
		}
	};
	display.timerExec (time, timer);
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

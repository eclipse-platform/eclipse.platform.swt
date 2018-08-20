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
 * ProgressBar example snippet: update a progress bar (from the UI thread)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet57 {

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	final ProgressBar bar = new ProgressBar (shell, SWT.SMOOTH);
	Rectangle clientArea = shell.getClientArea ();
	bar.setBounds (clientArea.x, clientArea.y, 200, 32);
	shell.open ();

	display.timerExec(100, new Runnable() {
		int i = 0;
		@Override
		public void run() {
			if (bar.isDisposed()) return;
			bar.setSelection(i++);
			if (i <= bar.getMaximum()) display.timerExec(100, this);
		}
	});

	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

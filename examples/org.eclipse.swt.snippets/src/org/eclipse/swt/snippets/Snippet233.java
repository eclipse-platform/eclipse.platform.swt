/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Shell example snippet: create a dialog shell and position it
 * with upper left corner at cursor position
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class Snippet233 {
	public static void main (String [] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText ("Parent Shell");
		shell.addMouseListener (new MouseAdapter() {
			@Override
			public void mouseDown (MouseEvent e) {
				Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				Point pt = display.getCursorLocation ();
				dialog.setLocation (pt.x, pt.y);
				dialog.setText ("Dialog Shell");
				dialog.setSize (100, 100);
				dialog.open (); 
			}});
		shell.setSize (400, 400);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}

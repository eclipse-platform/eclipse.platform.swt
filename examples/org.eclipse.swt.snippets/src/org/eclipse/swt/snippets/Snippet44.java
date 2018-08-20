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
 * Cursor example snippet: set the hand cursor into a control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet44 {

public static void main (String [] args) {
	Display display = new Display ();
	final Cursor cursor = display.getSystemCursor(SWT.CURSOR_HAND);
	Shell shell = new Shell (display);
	shell.open ();
	final Button b = new Button (shell, 0);
	b.setText("Push to set cursor to hand");
	Rectangle clientArea = shell.getClientArea ();
	b.setBounds (clientArea.x + 10, clientArea.y + 10, 200, 200);
	b.addListener (SWT.Selection, e -> b.setCursor (cursor));
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

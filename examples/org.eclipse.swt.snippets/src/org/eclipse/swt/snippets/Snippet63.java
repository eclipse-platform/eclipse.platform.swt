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
 * Shell example snippet: create a dialog shell (prompt for a value)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet63 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.pack ();
	shell.open ();
	final boolean [] result = new boolean [1];
	final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	dialog.setLayout (new RowLayout ());
	final Button ok = new Button (dialog, SWT.PUSH);
	ok.setText ("OK");
	Button cancel = new Button (dialog, SWT.PUSH);
	cancel.setText ("Cancel");
	Listener listener =event -> {
		result [0] = event.widget == ok;
		dialog.close ();
	};
	ok.addListener (SWT.Selection, listener);
	cancel.addListener (SWT.Selection, listener);
	dialog.pack ();
	dialog.open ();
	System.out.println ("Prompt ...");
	while (!dialog.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	System.out.println ("Result: " + result [0]);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

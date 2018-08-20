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
 * create a dialog Shell and prompt for a text string
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet295 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setText("Shell");
	FillLayout fillLayout = new FillLayout();
	fillLayout.marginWidth = 10;
	fillLayout.marginHeight = 10;
	shell.setLayout(fillLayout);

	Button open = new Button (shell, SWT.PUSH);
	open.setText ("Prompt for a String");
	open.addSelectionListener(widgetSelectedAdapter(e -> {
		final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("Dialog Shell");
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.NONE);
		label.setText ("Type a String:");
		FormData data = new FormData ();
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (100, 0);
		cancel.setLayoutData (data);
		cancel.addSelectionListener (widgetSelectedAdapter(event -> {
			System.out.println("User cancelled dialog");
			dialog.close ();
		}));

		final Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		text.setLayoutData (data);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (widgetSelectedAdapter(event -> {
			System.out.println ("User typed: " + text.getText ());
			dialog.close ();
		}));

		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();
	}));
	shell.pack ();
	shell.open ();

	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

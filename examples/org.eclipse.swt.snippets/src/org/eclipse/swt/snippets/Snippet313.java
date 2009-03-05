/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * FormLayout example snippet: use a form layout to hide/show a composite
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet313 {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new FormLayout());

		final Button button = new Button(shell, SWT.PUSH);
		button.setText(">>");
		FormData data = new FormData ();
		data.top = new FormAttachment (0, 4);
		data.right = new FormAttachment (100, -4);
		button.setLayoutData (data);

		final Composite composite = new Composite(shell, SWT.BORDER);
		final FormData down = new FormData ();
		down.top = new FormAttachment (button, 4, SWT.BOTTOM);
		down.bottom = new FormAttachment (100, -4);
		down.left = new FormAttachment (0, 4);
		down.right = new FormAttachment (100, -4);
		final FormData up = new FormData ();
		up.top = new FormAttachment (0);
		up.bottom = new FormAttachment (0);
		up.left = new FormAttachment (0);
		up.right = new FormAttachment (0);
		composite.setLayoutData (up);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (composite.getLayoutData() == up) {
					composite.setLayoutData (down);
					button.setText("<<");
				} else {
					composite.setLayoutData (up);
					button.setText(">>");
				}
				shell.pack ();
			}
		});

		shell.pack ();
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
}

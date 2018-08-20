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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Control example snippet: allow a multi-line text to process the default button
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet247 {
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new RowLayout());
	Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
	String modifier = SWT.MOD1 == SWT.CTRL ? "Ctrl" : "Command";
	text.setText("Hit " + modifier + "+Return\nto see\nthe default button\nrun");
	text.addTraverseListener(e -> {
		switch (e.detail) {
			case SWT.TRAVERSE_RETURN:
				if ((e.stateMask & SWT.MOD1) != 0) e.doit = true;
		}
	});
	Button button = new Button (shell, SWT.PUSH);
	button.pack();
	button.setText("OK");
	button.addSelectionListener(widgetSelectedAdapter(e->System.out.println("OK selected")));

	shell.setDefaultButton(button);
	shell.pack ();
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

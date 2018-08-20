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
 * Composite example snippet: force radio behavior on two different composites
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet115 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new RowLayout (SWT.VERTICAL));
	Composite c1 = new Composite (shell, SWT.BORDER | SWT.NO_RADIO_GROUP);
	c1.setLayout (new RowLayout ());
	Composite c2 = new Composite (shell, SWT.BORDER | SWT.NO_RADIO_GROUP);
	c2.setLayout (new RowLayout ());
	final Composite [] composites = new Composite [] {c1, c2};
	Listener radioGroup = event -> {
		for (int i=0; i<composites.length; i++) {
			Composite composite = composites [i];
			Control [] children = composite.getChildren ();
			for (int j=0; j<children.length; j++) {
				Control child = children [j];
				if (child instanceof Button) {
					Button button1 = (Button) child;
					if ((button1.getStyle () & SWT.RADIO) != 0) button1.setSelection (false);
				}
			}
		}
		Button button2 = (Button) event.widget;
		button2.setSelection (true);
	};
	for (int i=0; i<4; i++) {
		Button button = new Button (c1, SWT.RADIO);
		button.setText ("Button " + i);
		button.addListener (SWT.Selection, radioGroup);
	}
	for (int i=0; i<4; i++) {
		Button button = new Button (c2, SWT.RADIO);
		button.setText ("Button " + (i + 4));
		button.addListener (SWT.Selection, radioGroup);
	}
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

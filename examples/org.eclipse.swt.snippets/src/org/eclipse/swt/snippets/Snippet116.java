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
 * Text example snippet: stop CR from going to the default button
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet116  {
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
	text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	text.setText("Here is some text");
	text.addSelectionListener(widgetDefaultSelectedAdapter(e-> System.out.println("Text default selected (overrides default button)")));
	text.addTraverseListener(e -> {
		if (e.detail == SWT.TRAVERSE_RETURN) {
			e.doit = false;
			e.detail = SWT.TRAVERSE_NONE;
		}
	});
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Ok");
	button.addSelectionListener(widgetSelectedAdapter(e-> System.out.println("Button selected")));
	shell.setDefaultButton(button);
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}

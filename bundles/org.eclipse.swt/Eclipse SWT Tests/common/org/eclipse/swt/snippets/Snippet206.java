/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

/*
 * Button example snippet: a Button with text and image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet206 {

public static void main(String[] args) {
	Display display = new Display();
	Image image = display.getSystemImage(SWT.ICON_QUESTION);
	Shell shell = new Shell(display);
	shell.setText("Snippet 206");
	shell.setLayout (new GridLayout());
	var button = new CButton(shell, SWT.PUSH);

//	button.addSelectionListener(widgetSelectedAdapter(e -> System.out.println("Received evt: " + e )));
//	button.addSelectionListener(widgetSelectedAdapter(__ -> System.out.println("Another click")));
	button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));
	button.setBackground(new Color(255, 0, 0));
	button.setForeground(new Color(0, 0, 255));

	// When the shell is active and the user presses ENTER, the button is pressed
//	shell.setDefaultButton(button);

	button.setImage(image);
	button.setText("Button hola");
	button.setSize(133, 150);
	button.setLocation(120, 110);

	shell.setSize(300, 500);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
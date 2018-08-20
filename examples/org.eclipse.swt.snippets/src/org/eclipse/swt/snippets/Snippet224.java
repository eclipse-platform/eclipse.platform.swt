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
 * implement radio behavior for setSelection()
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet224 {
public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout (new RowLayout (SWT.VERTICAL));
	for (int i=0; i<8; i++) {
		Button button = new Button (shell, SWT.RADIO);
		button.setText ("B" + i);
		if (i == 0) button.setSelection (true);
	}
	Button button = new Button (shell, SWT.PUSH);
	button.setText ("Set Selection to B4");
	button.addListener (SWT.Selection, event -> {
		Control [] children = shell.getChildren ();
		Button newButton = (Button) children [4];
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			if (child instanceof Button && (child.getStyle () & SWT.RADIO) != 0) {
				((Button) child).setSelection (false);
			}
		}
		newButton.setSelection (true);
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
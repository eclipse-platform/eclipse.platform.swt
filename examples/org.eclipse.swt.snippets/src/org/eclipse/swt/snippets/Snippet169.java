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
 * Make a toggle button have radio behavior
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet169 {
public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	Listener listener = e -> {
		Control [] children = shell.getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			if (e.widget != child && child instanceof Button && (child.getStyle () & SWT.TOGGLE) != 0) {
				((Button) child).setSelection (false);
			}
		}
		((Button) e.widget).setSelection (true);
	};
	for (int i=0; i<20; i++) {
		Button button = new Button (shell, SWT.TOGGLE);
		button.setText ("B" + i);
		button.addListener (SWT.Selection, listener);
		if (i == 0) button.setSelection (true);
	}
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
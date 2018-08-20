/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * CoolBar example snippet: create a cool bar
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet20 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	CoolBar bar = new CoolBar (shell, SWT.BORDER);
	for (int i=0; i<2; i++) {
		CoolItem item = new CoolItem (bar, SWT.NONE);
		Button button = new Button (bar, SWT.PUSH);
		button.setText ("Button " + i);
		Point size = button.computeSize (SWT.DEFAULT, SWT.DEFAULT);
		item.setPreferredSize (item.computeSize (size.x, size.y));
		item.setControl (button);
	}
	Rectangle clientArea = shell.getClientArea ();
	bar.setLocation (clientArea.x, clientArea.y);
	bar.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

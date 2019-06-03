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
 * ToolBar example snippet: create a tool bar (text)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet18 {

public static void main (String [] args) {
	Display display = new Display();
	Shell shell = new Shell (display);
	ToolBar bar = new ToolBar (shell, SWT.BORDER);
	for (int i=0; i<8; i++) {
		ToolItem item = new ToolItem (bar, SWT.PUSH);
		item.setText ("Item " + i);
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

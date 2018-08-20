/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Monitor example snippet: center a shell on the primary monitor
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet120 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setSize (200, 200);
	Monitor primary = display.getPrimaryMonitor ();
	Rectangle bounds = primary.getBounds ();
	Rectangle rect = shell.getBounds ();
	int x = bounds.x + (bounds.width - rect.width) / 2;
	int y = bounds.y + (bounds.height - rect.height) / 2;
	shell.setLocation (x, y);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

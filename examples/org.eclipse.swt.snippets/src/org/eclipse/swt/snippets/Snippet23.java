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
 * Tracker example snippet: create a tracker (drag on mouse down)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet23 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.open ();
	shell.addListener (SWT.MouseDown, e -> {
		Tracker tracker = new Tracker (shell, SWT.NONE);
		tracker.setRectangles (new Rectangle [] {
			new Rectangle (e.x, e.y, 100, 100),
		});
		tracker.open ();
	});
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

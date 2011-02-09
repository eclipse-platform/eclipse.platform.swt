/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
 
/*
 * Sash example snippet: create a sash (allow it to be moved)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet54 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setSize(400, 300);
	final Sash sash = new Sash (shell, SWT.BORDER | SWT.VERTICAL);
	Rectangle clientArea = shell.getClientArea ();
	sash.setBounds (180, clientArea.y, 32, clientArea.height);
	sash.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			sash.setBounds (e.x, e.y, e.width, e.height);
		}
	});
	shell.open ();
	sash.setFocus ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

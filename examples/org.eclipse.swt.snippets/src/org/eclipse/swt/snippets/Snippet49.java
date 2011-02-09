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
 * ToolBar example snippet: create tool bar (wrap on resize)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet49 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	final ToolBar toolBar = new ToolBar (shell, SWT.WRAP);
	for (int i=0; i<12; i++) {
		ToolItem item = new ToolItem (toolBar, SWT.PUSH);
		item.setText ("Item " + i);
	}
	shell.addListener (SWT.Resize, new Listener () {
		public void handleEvent (Event e) {
			Rectangle rect = shell.getClientArea ();
			Point size = toolBar.computeSize (rect.width, SWT.DEFAULT);
			toolBar.setSize (size);
		}
	});
	Rectangle clientArea = shell.getClientArea ();
	toolBar.setLocation (clientArea.x, clientArea.y);
	toolBar.pack ();
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

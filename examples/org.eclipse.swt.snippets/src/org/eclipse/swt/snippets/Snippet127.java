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
 * Control example snippet: prevent Tab from traversing out of a control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

public class Snippet127 {
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Button button1 = new Button(shell, SWT.PUSH);
	button1.setBounds(10,10,180,30);
	button1.setText("no traverse");
	button1.addTraverseListener(new TraverseListener () {
		public void keyTraversed(TraverseEvent e) {
			switch (e.detail) {
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS: {
					e.doit = false;
				}
			}
		}
	});
	Button button2 = new Button (shell, SWT.PUSH);
	button2.setBounds(200,10,180,30);
	button2.setText("can traverse");
	shell.pack ();
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

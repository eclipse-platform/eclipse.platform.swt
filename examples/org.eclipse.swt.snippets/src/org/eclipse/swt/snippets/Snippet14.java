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
 * Control example snippet: detect mouse enter, exit and hover events
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet14 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setSize (100, 100);
	shell.addListener (SWT.MouseEnter, new Listener () {
		public void handleEvent (Event e) {
			System.out.println ("ENTER");
		}
	});
	shell.addListener (SWT.MouseExit, new Listener () {
		public void handleEvent (Event e) {
			System.out.println ("EXIT");
		}
	});
	shell.addListener (SWT.MouseHover, new Listener () {
		public void handleEvent (Event e) {
			System.out.println ("HOVER");
		}
	});
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

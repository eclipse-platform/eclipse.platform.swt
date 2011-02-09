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
 * List example snippet: print selected items in a list
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet59 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	final List list = new List (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	for (int i=0; i<128; i++) list.add ("Item " + i);
	Rectangle clientArea = shell.getClientArea ();
	list.setBounds (clientArea.x, clientArea.y, 100, 100);
	list.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			String string = "";
			int [] selection = list.getSelectionIndices ();
			for (int i=0; i<selection.length; i++) string += selection [i] + " ";
			System.out.println ("Selection={" + string + "}");
		}
	});
	list.addListener (SWT.DefaultSelection, new Listener () {
		public void handleEvent (Event e) {
			String string = "";
			int [] selection = list.getSelectionIndices ();
			for (int i=0; i<selection.length; i++) string += selection [i] + " ";
			System.out.println ("DefaultSelection={" + string + "}");
		}
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

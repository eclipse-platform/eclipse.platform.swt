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
 * Text example snippet: set the selection (start, end)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet12 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Text text = new Text (shell, SWT.BORDER | SWT.V_SCROLL);
	text.setBounds (10, 10, 100, 100);
	for (int i=0; i<16; i++) {
		text.append ("Line " + i + "\n");
	}
	shell.open ();
	text.setSelection (30, 32);
	System.out.println (text.getCaretLocation ());
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
} 
}

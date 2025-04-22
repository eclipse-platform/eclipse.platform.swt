/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * Text example snippet: set the selection (start, end),
 * and then scroll the selection to the top of the client area
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetText {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText("Snippet 12");
	Text text = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	Rectangle clientArea = shell.getClientArea ();
	text.setBounds (clientArea.x + 10, clientArea.y + 10, 100, 100);
	for (int i=0; i<16; i++) {
		text.append ("Line " + i + "\n");
	}
	shell.open ();
	text.setSelection (30, 38);
	text.setTopIndex(30);
	System.out.println ("selection=" + text.getSelection ());
	System.out.println ("selection text=" + text.getSelectionText ());
	System.out.println ("caret position=" + text.getCaretPosition ());
	System.out.println ("caret location=" + text.getCaretLocation ());
	text.setSize(200, 200);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

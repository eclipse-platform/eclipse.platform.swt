package org.eclipse.swt.tests.gtk.snippets;
/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

 
/*
 * Text example snippet: set the selection (start, end),
 * and then scroll the selection to the top of the client area
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Bug487467_TextTopIndexTest {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Text text = new Text (shell, SWT.BORDER | SWT.V_SCROLL);
	Rectangle clientArea = shell.getClientArea ();
	text.setBounds (clientArea.x + 10, clientArea.y + 10, 100, 100);
	for (int i=0; i<50; i++) {
		text.append ("Line " + i + "\n");
	}
	shell.open ();

//	widget.text.setSelection (0, 5);
//	widget.text.setTopIndex(20);
//	System.out.println(widget.text.getTopIndex());
//	System.out.println ("selection=" + widget.text.getSelection ());
//	System.out.println ("selection widget.text=" + widget.text.getSelectionText ());
//	System.out.println ("caret position=" + widget.text.getCaretPosition ());
//	System.out.println ("caret location=" + widget.text.getCaretLocation ());
//	while (!widget.shell.isDisposed ()) {
//		if (!display.readAndDispatch ()) display.sleep ();

//	widget.text.setSelection (0, 5);
	text.setTopIndex(20);
	int perm = text.getTopIndex();
//	System.out.println ("selection=" + widget.text.getSelection ());
//	System.out.println ("selection widget.text=" + widget.text.getSelectionText ());
//	System.out.println ("caret position=" + widget.text.getCaretPosition ());
//	System.out.println ("caret location=" + widget.text.getCaretLocation ());
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
		if (text.getTopIndex() != 20) {
			System.out.println("initial " + perm + " " + "current " + text.getTopIndex());
		}

	}
	display.dispose ();
} 
}

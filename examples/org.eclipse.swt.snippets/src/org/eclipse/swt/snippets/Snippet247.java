/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Control example snippet: allow a multi-line text to process the default button
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet247 {
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new RowLayout());
	Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
	String modifier = SWT.MOD1 == SWT.CTRL ? "Ctrl" : "Command";
	text.setText("Hit " + modifier + "+Return\nto see\nthe default button\nrun");
	text.addTraverseListener(new TraverseListener () {
		@Override
		public void keyTraversed(TraverseEvent e) {
			switch (e.detail) {
				case SWT.TRAVERSE_RETURN:
					if ((e.stateMask & SWT.MOD1) != 0) e.doit = true;
			}
		}
	});
	Button button = new Button (shell, SWT.PUSH);
	button.pack();
	button.setText("OK");
	button.addSelectionListener(new SelectionAdapter () {
		@Override
		public void widgetSelected(SelectionEvent e) {
			System.out.println("OK selected");
		}
	});
	shell.setDefaultButton(button);
	shell.pack ();
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

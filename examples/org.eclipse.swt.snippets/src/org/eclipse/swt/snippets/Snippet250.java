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
 * DateTime example snippet: create a DateTime calendar and a DateTime time.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet250 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new RowLayout ());

	DateTime calendar = new DateTime (shell, SWT.CALENDAR);
	calendar.addSelectionListener (new SelectionAdapter () {
		@Override
		public void widgetSelected (SelectionEvent e) {
			System.out.println ("calendar date changed");
		}
	});

	DateTime time = new DateTime (shell, SWT.TIME);
	time.addSelectionListener (new SelectionAdapter () {
		@Override
		public void widgetSelected (SelectionEvent e) {
			System.out.println ("time changed");
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

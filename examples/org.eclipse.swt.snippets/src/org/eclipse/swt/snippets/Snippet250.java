/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845, 507186
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * DateTime example snippet: create a DateTime calendar and a DateTime time.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet250 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new RowLayout ());

	DateTime calendar = new DateTime (shell, SWT.CALENDAR | SWT.CALENDAR_WEEKNUMBERS);
	calendar.addSelectionListener (widgetSelectedAdapter(e -> System.out.println ("calendar date changed")));

	DateTime calendarDropDown = new DateTime (shell, SWT.DROP_DOWN | SWT.CALENDAR_WEEKNUMBERS);
	calendarDropDown.addSelectionListener (
		widgetSelectedAdapter(e -> System.out.println ("calendar date changed via drop-down")));

	DateTime time = new DateTime (shell, SWT.TIME);
	time.addSelectionListener (widgetSelectedAdapter(e -> System.out.println ("time changed")));

	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

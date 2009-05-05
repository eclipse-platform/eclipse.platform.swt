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
 * DateTime example snippet: create a DateTime calendar, date, and time in a dialog.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet251 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());

	Button open = new Button (shell, SWT.PUSH);
	open.setText ("Open Dialog");
	open.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM);
			dialog.setLayout (new GridLayout (3, false));

			final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
			final DateTime date = new DateTime (dialog, SWT.DATE | SWT.SHORT);
			final DateTime time = new DateTime (dialog, SWT.TIME | SWT.SHORT);

			new Label (dialog, SWT.NONE);
			new Label (dialog, SWT.NONE);
			Button ok = new Button (dialog, SWT.PUSH);
			ok.setText ("OK");
			ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
			ok.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					System.out.println ("Calendar date selected (MM/DD/YYYY) = " + (calendar.getMonth () + 1) + "/" + calendar.getDay () + "/" + calendar.getYear ());
					System.out.println ("Date selected (MM/YYYY) = " + (date.getMonth () + 1) + "/" + date.getYear ());
					System.out.println ("Time selected (HH:MM) = " + time.getHours () + ":" + (time.getMinutes () < 10 ? "0" : "") + time.getMinutes ());
					dialog.close ();
				}
			});
			dialog.setDefaultButton (ok);
			dialog.pack ();
			dialog.open ();
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

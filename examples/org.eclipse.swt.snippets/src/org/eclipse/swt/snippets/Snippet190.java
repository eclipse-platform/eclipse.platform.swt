/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Floating point values in Spinner
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */

public class Snippet190 {
	
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText("Spinner with float values");
	shell.setLayout(new GridLayout());
	final Spinner spinner = new Spinner(shell, SWT.NONE);
	// allow 3 decimal places
	spinner.setDigits(3);
	// set the minimum value to 0.001
	spinner.setMinimum(1);
	// set the maximum value to 20
	spinner.setMaximum(20000);
	// set the increment value to 0.010
	spinner.setIncrement(10);
	// set the seletion to 3.456
	spinner.setSelection(3456);
	spinner.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int selection = spinner.getSelection();
			int digits = spinner.getDigits();
			System.out.println("Selection is "+(selection / Math.pow(10, digits)));
		}
	});
	shell.setSize(200, 200);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
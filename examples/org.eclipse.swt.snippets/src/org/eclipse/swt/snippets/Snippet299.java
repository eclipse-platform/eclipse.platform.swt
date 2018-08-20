/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
 * RowLayout: center alignment
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet299 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		RowLayout layout = new RowLayout();
		layout.center = true;
		shell.setLayout(layout);

		Button button0 = new Button(shell, SWT.PUSH);
		button0.setText("Button 0");

		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("Button 1");
		button1.setLayoutData(new RowData (SWT.DEFAULT, 50));

		Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("Button 2");
		button2.setLayoutData(new RowData (SWT.DEFAULT, 70));

		Button button3 = new Button(shell, SWT.PUSH);
		button3.setText("Button 3");

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

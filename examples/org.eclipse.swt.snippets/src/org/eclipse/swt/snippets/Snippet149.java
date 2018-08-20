/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * TableEditor example snippet: place a progress bar in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;

public class Snippet149 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout (new FillLayout ());
		Table table = new Table (shell, SWT.BORDER);
		table.setHeaderVisible (true);
		table.setLinesVisible(true);
		for (int i=0; i<2; i++) {
			new TableColumn (table, SWT.NONE);
		}
		table.getColumn (0).setText ("Task");
		table.getColumn (1).setText ("Progress");
		for (int i=0; i<40; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Task " + i);
			if ( i % 5 == 0) {
				ProgressBar bar = new ProgressBar (table, SWT.NONE);
				bar.setSelection (i);
				TableEditor editor = new TableEditor (table);
				editor.grabHorizontal = editor.grabVertical = true;
				editor.setEditor (bar, item, 1);
			}
		}
		table.getColumn (0).pack ();
		table.getColumn (1).setWidth (128);
		shell.pack ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}

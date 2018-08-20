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
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Table example snippet: insert a table column (at an index)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet106 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new RowLayout (SWT.VERTICAL));
	final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
	table.setHeaderVisible (true);
	for (int i=0; i<4; i++) {
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText ("Column " + i);
	}
	final TableColumn [] columns = table.getColumns ();
	for (int i=0; i<12; i++) {
		TableItem item = new TableItem (table, SWT.NONE);
		for (int j=0; j<columns.length; j++) {
			item.setText (j, "Item " + i);
		}
	}
	for (int i=0; i<columns.length; i++) columns [i].pack ();
	Button button = new Button (shell, SWT.PUSH);
	final int index = 1;
	button.setText ("Insert Column " + index + "a");
	button.addListener (SWT.Selection, e -> {
		TableColumn column = new TableColumn (table, SWT.NONE, index);
		column.setText ("Column " + index + "a");
		TableItem [] items = table.getItems ();
		for (int i=0; i<items.length; i++) {
			items [i].setText (index, "Item " + i + "a");
		}
		column.pack ();
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}

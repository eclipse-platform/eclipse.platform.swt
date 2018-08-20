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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *******************************************************************************/
package org.eclipse.swt.snippets;


/*
 * TableEditor example snippet: edit the text of a table item (in place)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet88 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Table table = new Table(shell, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	for (int i = 0; i < 10; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"item " + i, "edit this value"});
	}
	column1.pack();
	column2.pack();

	final TableEditor editor = new TableEditor(table);
	//The editor must have the same size as the cell and must
	//not be any smaller than 50 pixels.
	editor.horizontalAlignment = SWT.LEFT;
	editor.grabHorizontal = true;
	editor.minimumWidth = 50;
	// editing the second column
	final int EDITABLECOLUMN = 1;

	table.addSelectionListener(widgetSelectedAdapter(e -> {
			// Clean up any previous editor control
			Control oldEditor = editor.getEditor();
			if (oldEditor != null)
				oldEditor.dispose();

			// Identify the selected row
			TableItem item = (TableItem) e.item;
			if (item == null)
				return;

			// The control that will be the editor must be a child of the Table
			Text newEditor = new Text(table, SWT.NONE);
			newEditor.setText(item.getText(EDITABLECOLUMN));
			newEditor.addModifyListener(me -> {
				Text text = (Text) editor.getEditor();
				editor.getItem().setText(EDITABLECOLUMN, text.getText());
			});
			newEditor.selectAll();
			newEditor.setFocus();
			editor.setEditor(newEditor, item, EDITABLECOLUMN);
		}));
	shell.setSize(300, 300);
	shell.open();

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}

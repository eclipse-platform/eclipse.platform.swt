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
 * Table example snippet: place arbitrary controls in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;

public class Snippet126 {
public static void main(String[] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
	table.setLinesVisible (true);
	for (int i=0; i<3; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth (100);
	}
	for (int i=0; i<12; i++) {
		new TableItem (table, SWT.NONE);
	}
	TableItem [] items = table.getItems ();
	for (int i=0; i<items.length; i++) {
		TableEditor editor = new TableEditor (table);
		CCombo combo = new CCombo (table, SWT.NONE);
		combo.setText("CCombo");
		combo.add("item 1");
		combo.add("item 2");
		editor.grabHorizontal = true;
		editor.setEditor(combo, items[i], 0);
		editor = new TableEditor (table);
		Text text = new Text (table, SWT.NONE);
		text.setText("Text");
		editor.grabHorizontal = true;
		editor.setEditor(text, items[i], 1);
		editor = new TableEditor (table);
		Button button = new Button (table, SWT.CHECK);
		button.pack ();
		editor.minimumWidth = button.getSize ().x;
		editor.horizontalAlignment = SWT.LEFT;
		editor.setEditor (button, items[i], 2);
	}
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

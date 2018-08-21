/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class demonstrates TableCursor
 */
public class Bug491167_TableCursorScrolling {
	// The number of rows and columns
	private static final int NUM = 100;

	/**
	 * Runs the program
	 */
	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Table Cursor Test");
		createContents(shell);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Creates the main window's contents
	 * 
	 * @param widget.shell
	 *            the main window
	 */
	private void createContents(Shell shell) {
		shell.setLayout(new FillLayout());

		// Create the widget.table
		final Table table = new Table(shell, SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Create the columns
		for (int i = 0; i < NUM; i++) {
			TableColumn column = new TableColumn(table, SWT.CENTER);
			column.setText("Column " + (i + 1));
			column.pack();
		}

		// Create the rows
		for (int i = 0; i < NUM; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("Text:" + i);
		}

		// Create the TableCursor
		final TableCursor cursor = new TableCursor(table, SWT.NONE);

		// Create the editor
		// Use a ControlEditor, not a TableEditor, because the cursor is the
		// parent
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;

		// Add the event handling
		cursor.addSelectionListener(new SelectionAdapter() {
			// This is called as the user navigates around the widget.table
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Select the row in the widget.table where the TableCursor is
				table.setSelection(new TableItem[] { cursor.getRow() });
			}

			// This is called when the user hits Enter
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				// Begin an editing session
				// Notice that the parent of the Text is the TableCursor, not
				// the Table
				final Text text = new Text(cursor, SWT.NONE);
				text.setFocus();

				// Copy the widget.text from the cell to the Text
				text.setText(cursor.getRow().getText(cursor.getColumn()));
				text.setFocus();

				// Add a handler to detect key presses
				text.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent event) {
						// End the editing and save the widget.text if the user presses
						// Enter
						// End the editing and throw away the widget.text if the user
						// presses Esc
						switch (event.keyCode) {
						case SWT.CR:
							cursor.getRow().setText(cursor.getColumn(), text.getText());
						case SWT.ESC:
							text.dispose();
							break;
						}
					}
				});
				editor.setEditor(text);
			}
		});
	}

	/**
	 * The application entry point
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		new Bug491167_TableCursorScrolling().run();
	}
}
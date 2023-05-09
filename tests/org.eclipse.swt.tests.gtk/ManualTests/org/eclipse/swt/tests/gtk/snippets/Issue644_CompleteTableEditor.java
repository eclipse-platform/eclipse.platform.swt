/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * This editor has 1 non editable column and 5 editable columns. Type of editable
 * widget is named in column header.
 *
 * Initial data is populated when table is created. Table Editor is created on
 * Selection of the cell.
 *
 * Styled Text Editor should have caret visible when activated for editing.
 *
 * Canvas has a caret and will be visible on single click on canvas.
 */
public class Issue644_CompleteTableEditor {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		int numberOfColumns = 7;
		int nunOfRows = 3;
		for (int i = 0; i < numberOfColumns; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			populateColumnName(i, column);
			column.setWidth(100);
			column.setAlignment(SWT.CENTER);
		}
		for (int rowNum = 0; rowNum < nunOfRows; rowNum++) {
			TableItem item = new TableItem(table, SWT.NONE);
			// initial content to show
			populateData(numberOfColumns, rowNum, item);
		}
		TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		table.addListener(SWT.MouseDown, event -> {
			Rectangle clientArea = table.getClientArea();
			Point pt = new Point(event.x, event.y);
			int rowNum = table.getTopIndex();
			while (rowNum < table.getItemCount()) {
				boolean visible = false;
				final TableItem item = table.getItem(rowNum);
				for (int columnNumber = 0; columnNumber < table.getColumnCount(); columnNumber++) {
					Rectangle rect = item.getBounds(columnNumber);
					if (rect.contains(pt)) {
						if (editor.getEditor() != null) {
							editor.getEditor().dispose();
						}
						createEditor(table, editor, item, columnNumber);
					}
					if (!visible && rect.intersects(clientArea)) {
						visible = true;
					}
				}
				if (!visible)
					return;
				rowNum++;
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected static void populateColumnName(int columnNum, TableColumn column) {
		switch (columnNum) {
		case 0:
			column.setText("non-edit");
			break;
		case 1:
			column.setText("styled text");
			break;
		case 2:
			column.setText("combo button");
			break;
		case 3:
			column.setText("button");
			break;
		case 4:
			column.setText("canvas");
			break;
		case 5:
			column.setText("normal text");
			break;
		case 6:
			column.setText("check box");
			break;
		default:
			break;
		}
	}

	private static void populateData(int numOfColumn, int rowNum, TableItem item) {
		item.setText(new String[] { "" + rowNum, "" + rowNum, "" + rowNum });
		for (int columnNumber = 0; columnNumber < numOfColumn; columnNumber++) {
			switch (columnNumber) {
			case 0:
				item.setText(columnNumber, "1234" + rowNum);
				break;
			case 1:
				item.setText(columnNumber, rowNum + " , " + columnNumber);
				break;
			case 2:
				item.setText(columnNumber, "Select");
				break;
			case 3:
				item.setText(columnNumber, "Click");
				break;
			case 5:
				item.setText(columnNumber, Math.random() + "");
				break;
			default:
				break;
			}
		}
	}

	private static void createEditor(final Table table, TableEditor editor, final TableItem item, int columnNumber) {
		switch (columnNumber) {
		case 1:
			createStyledText(table, editor, item, columnNumber);
			break;
		case 2:
			createCombo(table, editor, item, columnNumber);
			break;
		case 3:
			createButton(table, editor, item, columnNumber);
			break;
		case 4:
			createCanvas(table, editor, item, columnNumber);
			break;
		case 5:
			createText(table, editor, item, columnNumber);
			break;
		case 6:
			createCheckBox(table, editor, item, columnNumber);
			break;
		default:
			break;
		}
	}

	private static void createCheckBox(Table table, TableEditor editor, TableItem item, int columnNumber) {
		final Button button = new Button(table, SWT.CHECK);
		Listener listener = e -> {
			switch (e.type) {
			case SWT.Selection:
				item.setData(button.getSelection());
				break;
			}
		};
		button.addListener(SWT.Selection, listener);
		if(item.getData() != null) {
			button.setSelection((boolean)item.getData());
		}
		editor.setEditor(button, item, columnNumber);
		button.setFocus();

	}

	private static void createText(Table table, TableEditor editor, TableItem item, int columnNumber) {
		final Text text = new Text(table, SWT.NONE);
		Listener listener = e -> {
			switch (e.type) {
			case SWT.FocusOut:
				item.setText(columnNumber, text.getText());
				break;
			case SWT.Traverse:
				switch (e.detail) {
				case SWT.TRAVERSE_RETURN:
					item.setText(columnNumber, text.getText());
				case SWT.TRAVERSE_ESCAPE:
					e.doit = false;
				}
				break;
			}
		};
		text.addListener(SWT.FocusOut, listener);
		text.addListener(SWT.Traverse, listener);

		editor.setEditor(text, item, columnNumber);
		text.setText(item.getText(columnNumber));
		text.setFocus();
	}

	private static void createCanvas(Table table, TableEditor editor, TableItem item, int columnNumber) {
		Canvas canvas = new Canvas(table, SWT.NONE);
		Rectangle parentBounds = canvas.getBounds();
		// make the caret little wider.
		Rectangle caretBound = new Rectangle(parentBounds.x + 20, parentBounds.y, 5, 17);
		Caret caret = new Caret(canvas, SWT.NONE);
		caret.setBounds(caretBound);
		canvas.setCaret(caret);
		canvas.addListener(SWT.MouseDown, event -> {
			canvas.setFocus();
		});
		editor.grabHorizontal = true;
		editor.setEditor(canvas, item, columnNumber);
		// Create a paint handler for the canvas
		canvas.addPaintListener(e -> {
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.drawText("Drawn content", 10, 1);
		});
	}

	private static void createButton(Table table, TableEditor editor, TableItem item, int columnNumber) {
		final Button button = new Button(table, SWT.NONE);
		Listener listener = e -> {
			switch (e.type) {
			case SWT.Selection:
				FileDialog dlg = new FileDialog(button.getShell());
				dlg.open();
				break;
			}
		};
		button.addListener(SWT.Selection, listener);
		button.setText("Click");
		editor.setEditor(button, item, columnNumber);
		button.setFocus();
	}

	private static void createCombo(Table table, TableEditor editor, TableItem item, int columnNumber) {
		final Combo combo = new Combo(table, SWT.NONE);
		Listener listener = e -> {
			switch (e.type) {
			case SWT.FocusOut:
				item.setText(columnNumber, combo.getText());
				break;
			}
		};
		combo.addListener(SWT.FocusOut, listener);
		combo.setText("select");
		combo.add("item 1");
		combo.add("item 2");

		editor.setEditor(combo, item, columnNumber);
		combo.setFocus();
	}

	protected static void createStyledText(final Table table, TableEditor editor, final TableItem item,
			int columnNumber) {
		final StyledText text = new StyledText(table, SWT.NONE);
		Listener listener = e -> {
			switch (e.type) {
			case SWT.FocusOut:
				item.setText(columnNumber, text.getText());
				break;
			case SWT.Traverse:
				switch (e.detail) {
				case SWT.TRAVERSE_RETURN:
					item.setText(columnNumber, text.getText());
				case SWT.TRAVERSE_ESCAPE:
					e.doit = false;
				}
				break;
			}
		};
		text.addListener(SWT.FocusOut, listener);
		text.addListener(SWT.Traverse, listener);

		editor.setEditor(text, item, columnNumber);
		text.setText(item.getText(columnNumber));
		text.setFocus();
	}
}
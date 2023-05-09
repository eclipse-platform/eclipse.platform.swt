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

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
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
	static List<String> headerNames = Arrays.asList(
			"StyledText", "Combo", "CCombo", "Button", "Canvas", "Text", "CheckBox");

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		// "Classic" table with editors created on demand
		final Table table = createTable(shell);

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

		// "Unusual" table with all editors created immediately
		Table table2 = createTable(shell);
		TableItem[] items = table2.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem tableItem = items[i];
			for (int columnNumber = 0; columnNumber < table2.getColumnCount(); columnNumber++) {
				TableEditor ed = new TableEditor(table);
				editor.horizontalAlignment = SWT.CENTER;
				editor.grabHorizontal = true;
				createEditor(table2, ed, tableItem, columnNumber);
			}
		}
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Table createTable(Shell shell) {
		Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		int nunOfRows = 3;
		for (String name : headerNames) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(name);
			column.setWidth(100);
			column.setAlignment(SWT.CENTER);
		}
		for (int rowNum = 0; rowNum < nunOfRows; rowNum++) {
			TableItem item = new TableItem(table, SWT.NONE);
			// initial content to show
			populateData(rowNum, item);
		}
		return table;
	}

	private static void populateData(int rowNum, TableItem item) {
		item.setText(new String[] { "" + rowNum, "" + rowNum, "" + rowNum });
		for (int columnNumber = 0; columnNumber < headerNames.size(); columnNumber++) {
			String name = headerNames.get(columnNumber);
			switch (name) {
			case "StyledText":
				item.setText(columnNumber, rowNum + " , " + columnNumber);
				break;
			case "Combo":
				item.setText(columnNumber, "Select");
				break;
			case "CCombo":
				item.setText(columnNumber, "Select");
				break;
			case "Button":
				item.setText(columnNumber, "Click");
				break;
			case "Text":
				item.setText(columnNumber, Math.random() + "");
				break;
			default:
				break;
			}
		}
	}

	private static void createEditor(final Table table, TableEditor editor, final TableItem item, int columnNumber) {
		String name = headerNames.get(columnNumber);
		Control c = null;
		switch (name) {
		case "StyledText":
			c = createStyledText(table, editor, item, columnNumber);
			break;
		case "Combo":
			c = createCombo(table, editor, item, columnNumber);
			break;
		case "CCombo":
			c = createCCombo(table, editor, item, columnNumber);
			break;
		case "Button":
			c = createButton(table, editor, item, columnNumber);
			break;
		case "Canvas":
			c = createCanvas(table, editor, item, columnNumber);
			break;
		case "Text":
			c = createText(table, editor, item, columnNumber);
			break;
		case "CheckBox":
			c = createCheckBox(table, editor, item, columnNumber);
			break;
		default:
			break;
		}
		if(c != null) {
			editor.grabHorizontal = true;
			editor.setEditor(c, item, columnNumber);
			c.setFocus();
		}
	}

	private static Control createCheckBox(Table table, TableEditor editor, TableItem item, int columnNumber) {
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
		return button;
	}

	private static Control createText(Table table, TableEditor editor, TableItem item, int columnNumber) {
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
		text.setText(item.getText(columnNumber));
		return text;
	}

	private static Control createCanvas(Table table, TableEditor editor, TableItem item, int columnNumber) {
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
		// Create a paint handler for the canvas
		canvas.addPaintListener(e -> {
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.drawText("Drawn content", 10, 1);
		});
		return canvas;
	}

	private static Control createButton(Table table, TableEditor editor, TableItem item, int columnNumber) {
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
		return button;
	}

	private static Control createCombo(Table table, TableEditor editor, TableItem item, int columnNumber) {
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
		return combo;
	}

	private static Control createCCombo(Table table, TableEditor editor, TableItem item, int columnNumber) {
		final CCombo combo = new CCombo(table, SWT.NONE);
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
		return combo;
	}

	protected static Control createStyledText(final Table table, TableEditor editor, final TableItem item,
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
		text.setText(item.getText(columnNumber));
		return text;
	}
}
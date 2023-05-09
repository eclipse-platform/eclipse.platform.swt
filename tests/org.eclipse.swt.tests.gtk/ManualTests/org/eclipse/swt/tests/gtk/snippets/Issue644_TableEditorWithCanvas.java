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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * You should be able to see caret on the canvas with single click.
 *
 * We can manually create and set a caret on canvas.
 */
public class Issue644_TableEditorWithCanvas {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Caret missing on Table Editor");
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 25;
		fillLayout.marginWidth = 25;
		fillLayout.spacing = 10;
		shell.setLayout(fillLayout);

		Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible(true);
		for (int i = 0; i < 1; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setWidth(200);
		}

		// add one row
		for (int i = 0; i < 1; i++) {
			new TableItem(table, SWT.NONE);
		}

		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			// canvas
			TableEditor editor = new TableEditor(table);
			Canvas canvas = createCanvas(table, "Canvas inside table");
			editor.grabHorizontal = true;
			editor.setEditor(canvas, items[i], 0);
			// Create a paint handler for the canvas
		}

		createCanvas(shell, "Canvas outside table");
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Canvas createCanvas(Composite table, String text) {
		Canvas canvas = new Canvas(table, SWT.NONE);
		Rectangle parentBounds = canvas.getBounds();
		Rectangle caretBound = new Rectangle(parentBounds.x + 20, parentBounds.y, 5, 17);
		Caret caret = new Caret(canvas, SWT.NONE);
		caret.setBounds(caretBound);
		canvas.setCaret(caret);
		canvas.addListener(SWT.MouseDown, event -> {
			canvas.setFocus();
		});
		canvas.addPaintListener(e -> {
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.drawText(text, 10, 1);
		});
		return canvas;
	}
}

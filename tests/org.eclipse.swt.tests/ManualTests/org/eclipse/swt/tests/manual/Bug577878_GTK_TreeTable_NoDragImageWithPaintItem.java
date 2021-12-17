/*******************************************************************************
 * Copyright (c) 2021, 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Bug577878_GTK_TreeTable_NoDragImageWithPaintItem {
	static final int NUM_ROWS = 100;
	static final int NUM_COLS = 3;

	static String makeItemText(int iRow, int iCol) {
		return "Item#" + iRow + ":" + iCol;
	}

	static void createTable(Composite parent, boolean isCustomPaint) {
		Table control = new Table (parent, SWT.BORDER | SWT.V_SCROLL);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		control.setHeaderVisible (true);
		for (int iColumn = 0; iColumn < 3; iColumn++) {
			TableColumn column = new TableColumn (control, 0);
			column.setText ("Col#" + iColumn);
			column.setWidth (120);
		}

		control.setItemCount (NUM_ROWS);

		if (isCustomPaint) {
			control.addListener (SWT.MeasureItem, e -> {
				e.width = 120;
				e.height = 20;
			});

			control.addListener (SWT.PaintItem, e -> {
				TableItem item = (TableItem) e.item;
				int iRow = item.getParent ().indexOf (item);
				String text = makeItemText(iRow, e.index);
				e.gc.drawString (text, e.x, e.y, true);
			});
		} else {
			for (int iRow = 0; iRow < NUM_ROWS; iRow++) {
				for (int iCol = 0; iCol < NUM_COLS; iCol++) {
					control.getItem (iRow).setText (iCol, makeItemText(iRow, iCol));
				}
			}
		}

		DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer (TextTransfer.getInstance ());
		dragSource.addDragListener (new DragSourceAdapter ());
	}

	static void createTree(Composite parent, boolean isCustomPaint) {
		Tree control = new Tree (parent, SWT.BORDER | SWT.V_SCROLL);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		control.setHeaderVisible (true);
		for (int iColumn = 0; iColumn < 3; iColumn++) {
			TreeColumn column = new TreeColumn (control, 0);
			column.setText ("Col#" + iColumn);
			column.setWidth (120);
		}

		control.setItemCount (NUM_ROWS);

		if (isCustomPaint) {
			control.addListener (SWT.MeasureItem, e -> {
				e.width = 120;
				e.height = 20;
			});

			control.addListener (SWT.PaintItem, e -> {
				TreeItem item = (TreeItem) e.item;
				TreeItem parentItem = item.getParentItem ();
				int iRow = (parentItem != null) ? parentItem.indexOf (item) : item.getParent ().indexOf (item);
				String text = makeItemText(iRow, e.index);
				e.gc.drawString (text, e.x, e.y, true);
			});
		} else {
			for (int iRow = 0; iRow < NUM_ROWS; iRow++) {
				for (int iCol = 0; iCol < NUM_COLS; iCol++) {
					control.getItem (iRow).setText (iCol, makeItemText(iRow, iCol));
				}
			}
		}

		TreeItem child = new TreeItem (control.getItem(1), 0);
		if (!isCustomPaint) {
			for (int iCol = 0; iCol < NUM_COLS; iCol++) {
				child.setText (iCol, makeItemText(0, iCol));
			}
		}

		DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer (TextTransfer.getInstance ());
		dragSource.addDragListener (new DragSourceAdapter ());
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label(shell, 0);
		hint.setText (
			"1) Run on GTK\n" +
			"2) Drag rows from regular Table/Tree - there will be a drag image\n" +
			"3) Bug 577878: Table/Tree with SWT.PaintItem do not have drag image\n"
		);

		Composite composite = new Composite (shell, 0);
		composite.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		composite.setLayout (new GridLayout (2, true));

		new Label(composite, 0).setText ("Table");
		new Label(composite, 0).setText ("Table+PaintItem");
		createTable(composite, false);
		createTable(composite, true);

		new Label(composite, 0).setText ("Tree");
		new Label(composite, 0).setText ("Tree+PaintItem");
		createTree(composite, false);
		createTree(composite, true);

		shell.setSize (800, 800);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

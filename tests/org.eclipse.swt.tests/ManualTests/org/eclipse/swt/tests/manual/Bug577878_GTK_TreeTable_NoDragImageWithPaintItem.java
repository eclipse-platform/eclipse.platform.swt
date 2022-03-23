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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Bug577878_GTK_TreeTable_NoDragImageWithPaintItem {
	static final int NUM_ROWS = 100;
	static final int NUM_COLS = 2;
	static final int IMG_CX   = 22;
	static final int IMG_CY   = 16;

	static String makeItemText(int iRow, int iCol) {
		return "Item#" + iRow + ":" + iCol;
	}

	static Image makeItemImage(Device device, int iRow) {
		Image image = new Image(device, IMG_CX, IMG_CY);
		GC gc = new GC(image);
		gc.setBackground(new Color(100,255,100));
		gc.fillRectangle(0, 0, IMG_CX-1, IMG_CY-1);
		gc.drawRectangle(0, 0, IMG_CX-1, IMG_CY-1);

		String text = Integer.toString(iRow);
		Point textSize = gc.stringExtent(text);
		gc.drawText(text, (IMG_CX - textSize.x) / 2, (IMG_CY - textSize.y) / 2);

		gc.dispose();
		return image;
	}

	static void paintItem(Event event, Image image, int iRow) {
		if (image != null) {
			event.x += 2;
			int y = event.y + (event.height - image.getBounds().height) / 2;
			event.gc.drawImage(image, event.x, y);
			event.x += image.getBounds().width;
		}

		event.x += 2;
		String text = makeItemText(iRow, event.index);
		Point textSize = event.gc.stringExtent(text);
		int y = event.y + (event.height - textSize.y) / 2;
		event.gc.drawString (text, event.x, y, true);
	}

	static void createTable(Composite parent, boolean isCustomPaint, boolean isImages) {
		Table control = new Table (parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		control.setHeaderVisible (true);
		for (int iColumn = 0; iColumn < NUM_COLS; iColumn++) {
			TableColumn column = new TableColumn (control, 0);
			column.setText ("Col#" + iColumn);
			column.setWidth (120);
		}

		control.setItemCount (NUM_ROWS);

		if (isImages) {
			for (int iItem = 0; iItem < NUM_ROWS; iItem++) {
				control.getItem(iItem).setImage(makeItemImage(parent.getDisplay(), iItem));
			}
		}

		if (isCustomPaint) {
			control.addListener (SWT.MeasureItem, e -> {
				e.width = 120;
				e.height = 20;
			});

			control.addListener (SWT.EraseItem, e -> {
				e.detail &= ~SWT.FOREGROUND;
			});

			control.addListener (SWT.PaintItem, e -> {
				TableItem item = (TableItem) e.item;
				int iRow = item.getParent ().indexOf (item);
				paintItem(e, item.getImage(), iRow);
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

	static void createTree(Composite parent, boolean isCustomPaint, boolean isImages) {
		Tree control = new Tree (parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		control.setHeaderVisible (true);
		for (int iColumn = 0; iColumn < NUM_COLS; iColumn++) {
			TreeColumn column = new TreeColumn (control, 0);
			column.setText ("Col#" + iColumn);
			column.setWidth (120);
		}

		control.setItemCount (NUM_ROWS);

		if (isImages) {
			for (int iItem = 0; iItem < NUM_ROWS; iItem++) {
				control.getItem(iItem).setImage(makeItemImage(parent.getDisplay(), iItem));
			}
		}

		if (isCustomPaint) {
			control.addListener (SWT.MeasureItem, e -> {
				e.width = 120;
				e.height = 20;
			});

			control.addListener (SWT.EraseItem, e -> {
				e.detail &= ~SWT.FOREGROUND;
			});

			control.addListener (SWT.PaintItem, e -> {
				TreeItem item = (TreeItem) e.item;
				TreeItem parentItem = item.getParentItem ();
				int iRow = (parentItem != null) ? parentItem.indexOf (item) : item.getParent ().indexOf (item);
				paintItem(e, item.getImage(), iRow);
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
			"3) Bug 577878: Table/Tree with SWT.PaintItem do not have drag image\n" +
			"\n" +
			"Patch v2:\n" +
			"4) Bug 577878 v2: Tree/Table with image should not have empty space\n" +
			"   to the left of the item\n" +
			"5) Bug 579395: Tree with images and PaintItem overpaints expanders\n" +
			"   Old bug, not fixed in this patch."
		);

		Composite composite = new Composite (shell, 0);
		composite.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		composite.setLayout (new GridLayout (4, true));

		new Label(composite, 0).setText ("Table");
		new Label(composite, 0).setText ("Table+Images");
		new Label(composite, 0).setText ("Table+PaintItem");
		new Label(composite, 0).setText ("Table+PaintItem+Images");
		createTable(composite, false, false);
		createTable(composite, false, true);
		createTable(composite, true, false);
		createTable(composite, true, true);

		new Label(composite, 0).setText ("Tree");
		new Label(composite, 0).setText ("Tree+Images");
		new Label(composite, 0).setText ("Tree+PaintItem");
		new Label(composite, 0).setText ("Tree+PaintItem+Images");
		createTree(composite, false, false);
		createTree(composite, false, true);
		createTree(composite, true, false);
		createTree(composite, true, true);

		shell.setSize (900, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

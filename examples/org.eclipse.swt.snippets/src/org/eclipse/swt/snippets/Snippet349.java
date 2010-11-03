/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/* 
 * Table example snippet: Fill a Table with images with various sizes
 *
 * For more info on custom-drawing TableItem and TreeItem content see 
 * http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet349 {

public static void main(String [] args) {
	final int COLUMN_COUNT = 3;
	final int TEXT_MARGIN = 3;
	final String KEY_WIDTHS = "widths";
	final String KEY_IMAGES = "images";
	Display display = new Display();
	Image[] images = new Image[4];
	images[0] = createImage(display, 16, 16);
	images[1] = createImage(display, 32, 16);
	images[2] = createImage(display, 48, 16);

	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	Table table = new Table(shell, SWT.NONE);
	for (int i = 0; i < COLUMN_COUNT; i++) {
		new TableColumn(table, SWT.NONE);
	}
	for (int i = 0; i < 8; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		Image[] itemImages = new Image[COLUMN_COUNT];
		item.setData(KEY_IMAGES, itemImages);
		for (int j = 0; j < COLUMN_COUNT; j++) {
			item.setText(j, "item " + i + " col " + j);
			itemImages[j] = images[(i * COLUMN_COUNT + j) % images.length];
		}
	}

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	final int itemHeight = table.getItemHeight();
	GC gc = new GC (table);
	FontMetrics metrics = gc.getFontMetrics();
	final int fontHeight = metrics.getHeight();
	gc.dispose();
	Listener paintListener = new Listener() {
		public void handleEvent(Event event) {		
			switch (event.type) {
				case SWT.MeasureItem: {
					int column = event.index;
					TableItem item = (TableItem)event.item;
					Image[] images = (Image[])item.getData(KEY_IMAGES);
					Image image = images[column];
					if (image == null) {
						/* don't change the native-calculated event.width */
						break;
					}
					int[] cachedWidths = (int[])item.getData(KEY_WIDTHS);
					if (cachedWidths == null) {
						cachedWidths = new int[COLUMN_COUNT];
						item.setData(KEY_WIDTHS, cachedWidths);
					}
					if (cachedWidths[column] == 0) {
						int width = image.getBounds().width + 2 * TEXT_MARGIN;
						GC gc = new GC(item.getParent());
						width += gc.stringExtent(item.getText()).x;
						gc.dispose();
						cachedWidths[column] = width;
					}
					event.width = cachedWidths[column];
					break;
				}
				case SWT.EraseItem: {
					int column = event.index;
					TableItem item = (TableItem)event.item;
					Image[] images = (Image[])item.getData(KEY_IMAGES);
					Image image = images[column];
					if (image == null) {
						break;
					}
					/* disable the native drawing of this item */
					event.detail &= ~SWT.FOREGROUND;
					break;
				}
				case SWT.PaintItem: {
					int column = event.index;
					TableItem item = (TableItem)event.item;
					Image[] images = (Image[])item.getData(KEY_IMAGES);
					Image image = images[column];
					if (image == null) {
						/* this item is drawn natively, don't touch it*/
						break;
					}

					int x = event.x;
					event.gc.drawImage(image, x, event.y + (itemHeight - image.getBounds().height) / 2);
					x += image.getBounds().width + TEXT_MARGIN;
					event.gc.drawString(item.getText(column), x, event.y + (itemHeight - fontHeight) / 2);
					break;
				}
			}
		}
	};		
	table.addListener(SWT.MeasureItem, paintListener);
	table.addListener(SWT.EraseItem, paintListener);
	table.addListener(SWT.PaintItem, paintListener);		

	for (int i = 0; i < COLUMN_COUNT; i++) {
		table.getColumn(i).pack();
	}
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	for (int i = 0; i < images.length; i++) {
		if (images[i] != null) {
			images[i].dispose();
		}
	}
	display.dispose();
}

static Image createImage(Display display, int width, int height) {
	Image result = new Image(display, width, height);
	GC gc = new GC(result);
	for (int x = -height; x < width; x += 4) {
		gc.drawLine(x, 0, x + height, height);
	}
	gc.dispose();
	return result;
}
}

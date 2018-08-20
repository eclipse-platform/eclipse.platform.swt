/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and others.
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
 * Table example snippet: Draw left aligned icon, text and selection
 *
 * For more info on custom-drawing TableItem and TreeItem content see
 * http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet283 {
	public static void main(String[] args) {
		Display display = new Display();
		Image image = new Image (display, Snippet283.class.getResourceAsStream("eclipse.png"));
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.FULL_SELECTION);
		for (int i = 0; i < 8; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText ("Item " + i + " with long text that scrolls.");
			if (i % 2 == 1) item.setImage (image);
		}
		table.addListener(SWT.MouseDown, event -> {
			Rectangle rect = table.getClientArea ();
			Point point = new Point (event.x, event.y);
			if (table.getItem(point) != null) return;
			for (int i=table.getTopIndex (); i<table.getItemCount(); i++) {
				TableItem item = table.getItem (i);
				Rectangle itemRect = item.getBounds ();
				if (!itemRect.intersects (rect)) return;
				itemRect.x = rect.x;
				itemRect.width = rect.width;
				if (itemRect.contains (point)) {
					table.setSelection (item);
					Event selectionEvent = new Event ();
					selectionEvent.item = item;
					table.notifyListeners(SWT.Selection, selectionEvent);
					return;
				}
			}
		});
		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		table.addListener(SWT.EraseItem, event -> {
			event.detail &= ~SWT.FOREGROUND;
			String osName = System.getProperty("os.name");
			if (osName != null && osName.indexOf ("Windows") != -1) {
				if (osName.indexOf ("Vista") != -1 || osName.indexOf ("unknown") != -1) {
					return;
				}
			}
			event.detail &= ~(SWT.FOREGROUND | SWT.SELECTED | SWT.HOT | SWT.FOCUSED);
			GC gc = event.gc;
			TableItem item = (TableItem)event.item;
			Rectangle rect = table.getClientArea ();
			Rectangle itemRect = item.getBounds ();
			itemRect.x = rect.x;
			itemRect.width = rect.width;
			gc.setClipping ((Rectangle) null);
			gc.fillRectangle (itemRect);
		});
		table.addListener(SWT.PaintItem, event -> {
			TableItem item = (TableItem)event.item;
			GC gc = event.gc;
			Image image1 = item.getImage (0);
			String text = item.getText (0);
			Point textExtent = gc.stringExtent (text);
			Rectangle imageRect = item.getImageBounds(0);
			Rectangle textRect = item.getTextBounds (0);
			int textY = textRect.y + Math.max (0, (textRect.height - textExtent.y) / 2);
			if (image1 == null) {
				gc.drawString(text, imageRect.x, textY, true);
			} else {
				Rectangle imageExtent = image1.getBounds ();
				int imageY = imageRect.y + Math.max (0, (imageRect.height - imageExtent.height) / 2);
				gc.drawImage (image1, imageRect.x, imageY);
				gc.drawString (text, textRect.x, textY, true);
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
}
}

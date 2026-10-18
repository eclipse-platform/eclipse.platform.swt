/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/*
 * Title: Bug 529126: [Wayland][GTK3] Tree does not notify SWT.MouseDown listeners
 * How to run: launch snippet and click on TreeItems
 * Bug description: No output is printed to the console
 * Expected results: Output with event info should be printed to the console for each
 * mouse down.
 * GTK Version(s): GTK3 (Wayland only)
 */
public class Bug529126_TreeMouseDown {

	// Static =================================================================

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Custom gradient selection for Tree");
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		final int columnCount = 4;
		for (int i = 0; i < columnCount; i++) {
			final TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setText("Column " + i);
		}

		final int itemCount = 5;
		for (int i = 0; i < itemCount; i++) {
			final TreeItem item1 = new TreeItem(tree, SWT.NONE);

			for (int j = 0; j < i; j++) {
				final TreeItem item2 = new TreeItem(item1, SWT.NONE);

				for (int k = 0; k < j; k++) {
					new TreeItem(item2, SWT.NONE);
				}
			}
		}

		tree.addListener(SWT.SetData, event -> {
			final TreeItem item = (TreeItem)event.item;

			final TreeItem parentItem = item.getParentItem();
			final String text;

			if (parentItem != null) {
				final String parentText = (String)parentItem.getData();
				text = parentText + event.index + "/";
			}
			else {
				text = "/";
			}

			item.setData(text);
		});

		tree.addListener(SWT.PaintItem, event -> {
			final TreeItem item = (TreeItem)event.item;
			final String text = (String)item.getData();
			event.gc.drawText(text + " [" + event.index + "]", event.x, event.y, true);
		});

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		tree.addListener(SWT.EraseItem, event -> {
			event.detail &= ~SWT.HOT;
			if ((event.detail & SWT.SELECTED) != 0) {
				final GC gc = event.gc;
				final Rectangle area = tree.getClientArea();
				/*
				 * If you wish to paint the selection beyond the end of
				 * last column, you must change the clipping region.
				 */
				final int columnCount1 = tree.getColumnCount();
				if (event.index == columnCount1 - 1 || columnCount1 == 0) {
					final int width = area.x + area.width - event.x;
					if (width > 0) {
						final Region region = new Region();
						gc.getClipping(region);
						region.add(event.x, event.y, width, event.height);
						gc.setClipping(region);
						region.dispose();
					}
				}
				gc.setAdvanced(true);
				if (gc.getAdvanced()) {
					gc.setAlpha(127);
				}
				final Rectangle rect = event.getBounds();
				final Color foreground = gc.getForeground();
				final Color background = gc.getBackground();
				gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
				gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
				gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
				// restore colors for subsequent drawing
				gc.setForeground(foreground);
				gc.setBackground(background);
				event.detail &= ~SWT.SELECTED;
			}
		});
		tree.getColumn(0).setWidth(200);
		for (int i = 1; i < columnCount; i++) {
			tree.getColumn(i).pack();
		}
		tree.setSelection(tree.getItem(0));

		tree.addListener(SWT.MouseDown, event -> System.out.println("event = " + event));
		shell.setSize(500, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}

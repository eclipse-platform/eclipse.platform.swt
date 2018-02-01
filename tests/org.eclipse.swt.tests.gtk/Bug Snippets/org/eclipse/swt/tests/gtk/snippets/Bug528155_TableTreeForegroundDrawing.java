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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/*
 * Title: Bug 528155: [GTK] Table/Tree forgets to set SWT.SELECTED bit on PaintItem
 * How to run: launch snippet and click the first row of either Table/Tree widget
 * Bug description: The red selection foreground color is not displayed for either Table/Tree
 * Expected results: The selected items of each widget should have a red foreground color
 * GTK Version(s): GTK2, GTK3
 */
public class Bug528155_TableTreeForegroundDrawing {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Color defaultBack = display.getSystemColor(SWT.COLOR_YELLOW);
		final Color defaultFore = display.getSystemColor(SWT.COLOR_DARK_GRAY);
		final Color selectionBack = display.getSystemColor(SWT.COLOR_BLUE);
		final Color selectionBackUnfocused = display.getSystemColor(SWT.COLOR_GRAY);
		final Color selectionFore = display.getSystemColor(SWT.COLOR_RED);

		final Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBackground(defaultBack);
		tree.setForeground(defaultFore);
		final Listener treeListener = event -> {
			if (event.type == SWT.MeasureItem) {
				event.width = 50;
			}
			else if (event.type == SWT.EraseItem) {
				if ((event.detail & SWT.SELECTED) != 0) {
					event.gc.setBackground(tree.isFocusControl() ? selectionBack : selectionBackUnfocused);
					event.gc.fillRectangle(event.x, event.y, event.width, event.height);
					event.detail &= ~SWT.SELECTED;
				}
			}
			else if (event.type == SWT.PaintItem) {
				if ((event.detail & SWT.SELECTED) != 0) {
					event.gc.setForeground(selectionFore);
				}
				event.gc.drawString("Node " + event.index, event.x, event.y, true);
			}
		};
		tree.addListener(SWT.MeasureItem, treeListener);
		tree.addListener(SWT.EraseItem, treeListener);
		tree.addListener(SWT.PaintItem, treeListener);

		new TreeItem(tree, SWT.NONE);
		new TreeItem(tree, SWT.NONE);

		final Table table = new Table(shell, SWT.BORDER | SWT.VIRTUAL);
		table.setBackground(defaultBack);
		table.setForeground(defaultFore);
		table.setHeaderVisible(true);

		final TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Column 1");
		column.setWidth(400);

		final Listener tableListener = event -> {
			if (event.type == SWT.MeasureItem) {
				event.width = 50;
			}
			else if (event.type == SWT.EraseItem) {
				if ((event.detail & SWT.SELECTED) != 0) {
					event.gc.setBackground(table.isFocusControl() ? selectionBack : selectionBackUnfocused);
					event.gc.fillRectangle(event.x, event.y, event.width, event.height);
//						cachedDetail = event.detail;
					event.detail &= ~SWT.SELECTED;
				}
			}
			else if (event.type == SWT.PaintItem) {
//					if ((cachedDetail & SWT.SELECTED) != 0) {
				if ((event.detail & SWT.SELECTED) != 0) {
					event.gc.setForeground(selectionFore);
				}
				event.gc.drawString("Row " + event.index, event.x, event.y, true);
			}
			else if (event.type == SWT.SetData) {
			}
		};
		table.addListener(SWT.SetData, tableListener);
		table.addListener(SWT.MeasureItem, tableListener);
		table.addListener(SWT.EraseItem, tableListener);
		table.addListener(SWT.PaintItem, tableListener);

		table.setItemCount(2);

		shell.setSize(500, 400);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

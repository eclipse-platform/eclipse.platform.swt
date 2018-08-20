/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 * example snippet: Scroll tree when mouse at top or bottom
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet221 {
	static Runnable Heartbeat;
	static boolean Tracking;
	static int ScrollSpeed = 40;

public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.BORDER);
	tree.setHeaderVisible(true);
	TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
	column0.setText("Column 0");
	TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
	column1.setText("Column 1");
	TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
	column2.setText("Column 2");
	for (int i = 0; i < 9; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("item "+i);
		item.setText(1, "column 1 - "+i);
		item.setText(2, "column 2 - "+i);
		for (int j = 0; j < 9; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("item "+i+" "+j);
			subItem.setText(1, "column 1 - "+i+" "+j);
			subItem.setText(2, "column 2 - "+i+" "+j);
			for (int k = 0; k < 9; k++) {
				TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
				subsubItem.setText("item "+i+" "+j+" "+k);
				subsubItem.setText(1, "column 1 - "+i+" "+j+" "+k);
				subsubItem.setText(2, "column 2 - "+i+" "+j+" "+k);
			}
		}
	}
	column0.pack();
	column1.pack();
	column2.pack();

	Heartbeat = () -> {
		if (!Tracking || tree.isDisposed()) return;
		Point cursor = display.getCursorLocation();
		cursor = display.map(null, tree, cursor);
		Scroll(tree, cursor.x, cursor.y);
		display.timerExec(ScrollSpeed, Heartbeat);
	};
	Listener listener = event -> {
		switch (event.type) {
		case SWT.MouseEnter:
			Tracking = true;
			display.timerExec(0, Heartbeat);
			break;
		case SWT.MouseExit:
			Tracking = false;
			break;
		}
	};
	tree.addListener(SWT.MouseEnter, listener);
	tree.addListener(SWT.MouseExit, listener);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
static void Scroll(Tree tree, int x, int y) {
	TreeItem item = tree.getItem(new Point(x, y));
	if (item == null) return;
	Rectangle area = tree.getClientArea();
	int headerHeight = tree.getHeaderHeight();
	int itemHeight= tree.getItemHeight();
	TreeItem nextItem = null;
	if (y < area.y + headerHeight + 2 * itemHeight) {
		nextItem = PreviousItem(tree, item);
	}
	if (y > area.y + area.height - 2 * itemHeight) {
		nextItem = NextItem(tree, item);
	}
	if (nextItem != null) tree.showItem(nextItem);
}

static TreeItem PreviousItem(Tree tree, TreeItem item) {
	if (item == null) return null;
	TreeItem childItem = item;
	TreeItem parentItem = childItem.getParentItem();
	int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
	if (index == 0) {
		return parentItem;
	} else {
		TreeItem nextItem = parentItem == null ? tree.getItem(index-1) : parentItem.getItem(index-1);
		int count = nextItem.getItemCount();
		while (count > 0 && nextItem.getExpanded()) {
			nextItem = nextItem.getItem(count - 1);
			count = nextItem.getItemCount();
		}
		return nextItem;
	}
}
static TreeItem NextItem(Tree tree, TreeItem item) {
	if (item == null) return null;
	if (item.getExpanded()) {
		return item.getItem(0);
	} else {
		TreeItem childItem = item;
		TreeItem parentItem = childItem.getParentItem();
		int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		int count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		while (true) {
			if (index + 1 < count) {
				return parentItem == null ? tree.getItem(index + 1) : parentItem.getItem(index + 1);
			} else {
				if (parentItem == null) {
					return null;
				} else {
					childItem = parentItem;
					parentItem = childItem.getParentItem();
					index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
					count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
				}
			}
		}
	}
}
}

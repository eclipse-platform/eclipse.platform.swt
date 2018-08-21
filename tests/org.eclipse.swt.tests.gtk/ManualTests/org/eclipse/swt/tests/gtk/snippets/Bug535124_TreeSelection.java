/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug535124_TreeSelection {

	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		final Color red = display.getSystemColor(SWT.COLOR_RED);
		final Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		final Tree tree = new Tree(shell, SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		new TreeColumn(tree, SWT.NONE).setWidth(100);
		new TreeColumn(tree, SWT.NONE).setWidth(100);
		new TreeColumn(tree, SWT.NONE).setWidth(100);
		for (int i = 0; i < 5; i++) {
		    TreeItem item = new TreeItem(tree, SWT.NONE);
		    item.setText(0, "item " + i + " col 0");
		    item.setText(1, "item " + i + " col 1");
		    item.setText(2, "item " + i + " col 2");
		}
		tree.pack();
		tree.addListener(SWT.EraseItem, event -> {
		    event.detail &= ~SWT.HOT;
		    if ((event.detail & SWT.SELECTED) == 0) {
		        return; /* item not selected */
		    }
		    int clientWidth = tree.getClientArea().width;
		    GC gc = event.gc;
		    Color oldForeground = gc.getForeground();
		    Color oldBackground = gc.getBackground();
		    gc.setForeground(red);
		    gc.setBackground(yellow);
		    gc.fillGradientRectangle(0, event.y, clientWidth, event.height, false);
		    gc.setForeground(oldForeground);
		    gc.setBackground(oldBackground);
		    event.detail &= ~SWT.SELECTED;
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug305864_ListItemHeight {

	public static void main(String[] args) {
		testTree();
		testList();
		testTable();
	}

	private static void testTree() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		shell.setLayout(layout);

		final Tree tree = new Tree(shell, SWT.BORDER);

		for (int i=0; i<32; i++) {
			TreeItem iItem = new TreeItem (tree, 0);
			iItem.setText ("TreeItem (1) -" + (i+1));
			for (int j=0; j<4; j++) {
				TreeItem jItem = new TreeItem (iItem, 0);
				jItem.setText ("TreeItem (2) -" + (j+1));
				for (int k=0; k<4; k++) {
					TreeItem kItem = new TreeItem (jItem, 0);
					kItem.setText ("TreeItem (3) -" + (k+1));
					for (int l=0; l<4; l++) {
						TreeItem lItem = new TreeItem (kItem, 0);
						lItem.setText ("TreeItem (4) -" + (l+1));
					}
				}
			}
		}

		final Button button = new Button (shell, SWT.PUSH);
		button.setText("Visible Items []");
		button.addListener(SWT.Selection, e -> {
			Rectangle rect = tree.getClientArea();
			int itemHeight = tree.getItemHeight();
			double visibleCount = ((double) rect.height) / itemHeight;
			button.setText("Visible Items [" + Math.round(visibleCount) + "]");
		});
		shell.setSize(200, 250);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();


	}
	private static void testList() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		shell.setLayout(layout);

		final List list = new List(shell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		list.setData("name", "list");

		for (int i = 0; i < 100; i++) {
			list.add("Item " + (i + 1) + " is quite long");
		}

		final Button button = new Button (shell, SWT.PUSH);
		button.setText("Visible Items []");
		button.addListener(SWT.Selection, e -> {
			Rectangle rect = list.getClientArea();
			int itemHeight = list.getItemHeight();
			double visibleCount = ((double) rect.height) / itemHeight;
			button.setText("Visible Items [" + Math.round(visibleCount) + "]");
		});
		shell.setSize(200, 250);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void testTable() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		shell.setLayout(layout);
		final Table table = new Table(shell, SWT.NONE);
		for (int i = 0; i < 32; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("Item " + (i + 1) + " is quite long");
		}
		final Button button = new Button (shell, SWT.PUSH);
		button.setText("Visible Items []");
		button.addListener(SWT.Selection, e -> {
			Rectangle rect = table.getClientArea();
			int itemHeight = table.getItemHeight();
			int headerHeight = table.getHeaderHeight();
			double visibleCount = ((double) (rect.height - headerHeight)) / itemHeight;
			button.setText("Visible Items [" + Math.round(visibleCount) + "]");
		});
		shell.setSize(200, 250);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}

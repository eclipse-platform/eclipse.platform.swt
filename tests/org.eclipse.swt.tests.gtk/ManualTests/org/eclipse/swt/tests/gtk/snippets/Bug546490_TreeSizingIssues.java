/*******************************************************************************
 * Copyright (c) 2019 Patrick Tasse and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug546490_TreeSizingIssues {

	private static final int NUM_COL = 5;
	private static final int NUM_ROW = 5;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		shell.setLayout(new RowLayout(SWT.VERTICAL));

		Label label1 = new Label(shell, SWT.NONE);
		label1.setText("Tree #1 with no items, 5 columns packed");
		Tree tree1 = new Tree(shell, SWT.NO_SCROLL);
		tree1.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		tree1.setHeaderVisible(true);
		tree1.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TreeColumn column = new TreeColumn(tree1, SWT.CENTER);
			column.setText("Column " + col);
			column.pack();
		}

		Label label2 = new Label(shell, SWT.NONE);
		label2.setText("Tree #2 with no items, 5 columns width=0");
		Tree tree2 = new Tree(shell, SWT.NO_SCROLL);
		tree2.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		tree2.setHeaderVisible(true);
		tree2.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TreeColumn column = new TreeColumn(tree2, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(0);
		}

		Label label3 = new Label(shell, SWT.NONE);
		label3.setText("Tree #3 with no items, 5 columns width=100");
		Tree tree3 = new Tree(shell, SWT.NO_SCROLL);
		tree3.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		tree3.setHeaderVisible(true);
		tree3.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TreeColumn column = new TreeColumn(tree3, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(100);
		}

		Label label4 = new Label(shell, SWT.NONE);
		label4.setText("Tree #4 with 5 items, 5 columns width=100");
		Tree tree4 = new Tree(shell, SWT.NO_SCROLL);
		tree4.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		tree4.setHeaderVisible(true);
		tree4.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TreeColumn column = new TreeColumn(tree4, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(100);
		}
		for (int row = 1; row <= NUM_ROW; row++) {
			TreeItem item = new TreeItem(tree4, SWT.NONE);
			if (row % 2 == 0) {
				item.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			} else {
				item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			}
			for (int col = 0; col < NUM_COL; col++) {
				item.setText(col, "R" + row + "C" + col);
			}
		}

		Label label5 = new Label(shell, SWT.NONE);
		label5.setText("Tree #5 with 5 items, 5 columns packed");
		Tree tree5 = new Tree(shell, SWT.NO_SCROLL);
		tree5.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		tree5.setHeaderVisible(true);
		tree5.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TreeColumn column = new TreeColumn(tree5, SWT.CENTER);
			column.setText("Column " + col);
		}
		for (int row = 1; row <= NUM_ROW; row++) {
			TreeItem item = new TreeItem(tree5, SWT.NONE);
			if (row % 2 == 0) {
				item.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			} else {
				item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			}
			for (int col = 0; col < NUM_COL; col++) {
				item.setText(col, "R" + row + "C" + col);
			}
		}
		for (TreeColumn column : tree5.getColumns()) {
			column.pack();
		}

		Label label = new Label(shell, SWT.NONE);
		label.setText("Resize shell to see tree sizes change!");

		shell.setText("Tree, never resized");
		shell.pack();
		shell.open();
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				shell.setText("Tree, has been resized");
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
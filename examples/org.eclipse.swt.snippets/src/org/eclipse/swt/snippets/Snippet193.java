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
 * Tree example snippet: allow user to reorder columns by dragging and programmatically.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet193 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		final Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
		tree.setLayoutData(new RowData(-1, 300));
		tree.setHeaderVisible(true);
		TreeColumn column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Column 0");
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("Column 1");
		column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Column 2");
		column = new TreeColumn(tree, SWT.RIGHT);
		column.setText("Column 3");
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("Column 4");
		for (int i = 0; i < 5; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			String[] text = new String[]{i+":0", i+":1", i+":2", i+":3", i+":4"};
			item.setText(text);
			for (int j = 0; j < 5; j++) {
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				text = new String[]{i+","+j+":0", i+","+j+":1", i+","+j+":2", i+","+j+":3", i+","+j+":4"};
				subItem.setText(text);
				for (int k = 0; k < 5; k++) {
					TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
					text = new String[]{i+","+j+","+k+":0", i+","+j+","+k+":1", i+","+j+","+k+":2", i+","+j+","+k+":3", i+","+j+","+k+":4"};
					subsubItem.setText(text);
				}
			}
		}
		Listener listener = e -> System.out.println("Move "+e.widget);
		TreeColumn[] columns = tree.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].setWidth(100);
			columns[i].setMoveable(true);
			columns[i].addListener(SWT.Move, listener);
		}
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invert column order");
		b.addListener(SWT.Selection, e -> {
			int[] order = tree.getColumnOrder();
			for (int i = 0; i < order.length / 2; i++) {
				int temp = order[i];
				order[i] = order[order.length - i - 1];
				order[order.length - i - 1] = temp;
			}
			tree.setColumnOrder(order);
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

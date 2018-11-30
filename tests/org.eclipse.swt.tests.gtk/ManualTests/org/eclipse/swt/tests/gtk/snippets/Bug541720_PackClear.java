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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug541720_PackClear {

	private static final int NUM_ROW = 4;
	private static final int NUM_COL = 5;

	public static void main(String[] args) {
		Display display = new Display();
		String gtkVersion = System.getProperty("org.eclipse.swt.internal.gtk.version");
		Shell shell = new Shell(display);
		shell.setText("Table Column packing gtk " + gtkVersion);

		shell.setLayout(new FillLayout());

		final int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION;
		Table table = new Table(shell, style);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table, SWT.CENTER);
			column.setText("Column " + col);
			column.pack();
		}
		for (int row = 0; row < NUM_ROW; row++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int col = 0; col < 5; col++) {
				item.setText(col, "Text for Row " + row + " Column " + col);
			}
		}
		table.setItemCount(NUM_ROW);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point pt = new Point(e.x, e.y);
				TableItem item = table.getItem(pt);
				if (item == null) {
					return;
				}
				for (int col = 0; col < NUM_COL; col++) {
					if (item.getBounds(col).contains(pt)) {
						TableColumn column = table.getColumns()[col];
						column.pack();
					}
				}
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
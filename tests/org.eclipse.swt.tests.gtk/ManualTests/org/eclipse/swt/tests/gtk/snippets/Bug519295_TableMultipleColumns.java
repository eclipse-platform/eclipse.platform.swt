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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/*
 * Title: Bug 519295: [GTK3] Invisible settings button in validation preferences
 * How to run: launch snippet and observe TableItem columns
 * Bug description: Image only appears in one column
 * Expected results: Images should appear at least once in all columns
 * GTK Version(s): GTK3
 */
public class Bug519295_TableMultipleColumns {
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Table tree = new Table(shell, SWT.NONE);
		tree.setHeaderVisible(true);

		TableColumn column1 = new TableColumn(tree, SWT.LEFT);
		column1.setText("Column 1");
		column1.setWidth(50);
		TableColumn column2 = new TableColumn(tree, SWT.LEFT);
		column2.setText("Column 2");
		column2.setWidth(50);
		TableColumn column3 = new TableColumn(tree, SWT.LEFT);
		column3.setText("Column 3");
		column3.setWidth(50);

		int W = 100, H = 100;
		final Image xImage = new Image(display, W, H);
		GC gc = new GC(xImage);
		gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gc.drawLine(0, 0, W - 1, H - 1);
		gc.drawLine(0, H - 1, W - 1, 0);
		gc.drawOval(1, 1, W - 2, H - 2);
		gc.dispose();

		int Wz = 40, Hz = 40;
		final Image zImage = new Image(display, Wz, Hz);
		GC gcz = new GC(zImage);
		gcz.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gcz.drawLine(0, 0, Wz - 1, Hz - 1);
		gcz.drawLine(0, Hz - 1, Wz - 1, 0);
		gcz.drawOval(1, 1, Wz - 2, Hz - 2);
		gcz.dispose();

		for (int i = 0; i < 12; i++) {
			if (i != 3 && i != 6) {
				TableItem item = new TableItem(tree, SWT.NONE);
				item.setImage(i, xImage);
				item.setText(i, "Test");
			}
		}

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}

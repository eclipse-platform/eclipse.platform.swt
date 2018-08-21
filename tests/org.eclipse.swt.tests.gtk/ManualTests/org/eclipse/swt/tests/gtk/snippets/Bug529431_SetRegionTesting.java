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
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 529431: [GTK3.10+] Snippet294 fails to draw Region
 * How to run: launch snippet and look for the region set
 * Bug description: anything without a red bg and circular inner regions is broken
 * Expected results: the region is applied correctly
 * GTK Version(s): GTK3.10+
 */
public class Bug529431_SetRegionTesting {

	static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int)Math.sqrt(r*r - x*x);
			polygon[2*i] = offsetX + x;
			polygon[2*i+1] = offsetY + y;
			polygon[8*r - 2*i - 2] = offsetX + x;
			polygon[8*r - 2*i - 1] = offsetY - y;
		}
		return polygon;
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Regions on a Control");
		shell.setLayout(new FillLayout());
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));


		/*
		 * Working: Canvas, Button, Shell, Combo, List, Spinner, DateTime, Text, ToolBar,
		 * CCombo, TabFolder
		 * Broken on 3.8: Link, Label, Scale, ProgressBar, ExpandBar, StyledText
		 * Broken on 3.22 (i.e. different from 3.8): Group, ProgressBar, Table/Tree, StyledText
		 */

		// Common widget with text case
		List widget = new List (shell, SWT.None);
//		widget.setText("Basic widget test");
		widget.add("Basic widget test");

		// Uncomment below to test a widget with items (ExpandBar, Table/Tree, etc)
//		ExpandItem item = new ExpandItem (widget, SWT.None);
//		item.setText("Item test");

		// Original case form Snippet294
//		Button b2 = new Button(widget, SWT.PUSH);
//		b2.setText("Button with Regions");
//		item.setControl(b2);

		// Table/Tree type cases
//		TableItem tableItem = new TableItem (widget, SWT.None);
//		tableItem.setText("This is a table/tree item test with a longer string");
//		TableItem tableItem2 = new TableItem (widget, SWT.None);
//		tableItem2.setText("This is a second table/tree item test with a longer string");


		// define a region that looks like a circle with two holes in ot
		Region region = new Region();
		region.add(circle(67, 87, 77));
		region.subtract(circle(20, 87, 47));
		region.subtract(circle(20, 87, 113));

		// define the shape of the button using setRegion
		widget.setRegion(region);

		// test input handling
		widget.addListener(SWT.Selection, e -> System.out.println("SWT.Selection sent"));
		widget.addListener(SWT.MouseDown, e -> System.out.println("SWT.MouseDown sent"));
		widget.addListener(SWT.MouseExit, e -> System.out.println("SWT.MouseExit sent"));
		widget.addListener(SWT.MouseEnter, e -> System.out.println("SWT.MouseEnter sent"));

		shell.setSize(200,200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		region.dispose();
		display.dispose();
	}

}

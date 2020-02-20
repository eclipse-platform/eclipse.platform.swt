/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Bug560316_DarkTextBorders {
	static final boolean useDarkTheme    = true;
	static final boolean useWsBorderAll  = true;
	static final boolean useWsBorderText = false;

	static void moveControl(Control control, Point point) {
		Point preferredSize = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		control.setBounds(point.x, point.y, preferredSize.x, preferredSize.y);
	}

	static void setColors(Control control, Color backColor, Color foreColor) {
		control.setBackground(backColor);
		control.setForeground(foreColor);

		if (control instanceof Table) {
			Table table = (Table)control;
			table.setHeaderBackground(backColor);
			table.setHeaderForeground(foreColor);
		}

		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				setColors(child, backColor, foreColor);
			}
		}
	}

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);

		// Theme setup
		if (useWsBorderAll)
			display.setData("org.eclipse.swt.internal.win32.all.use_WS_BORDER", true);

		if (useWsBorderText)
			display.setData("org.eclipse.swt.internal.win32.Text.use_WS_BORDER", true);

		// Create various controls.
		// Nothing special here, really.
		{
			Point pos = new Point(10, 10);

			final Text labelInfo = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
			labelInfo.setText(
					"Notice that Text borders are too bright.\n" +
					"Other controls are given to see the effect of new changes.\n" +
					"I suggest to make and compare screenshots."
			);
			moveControl(labelInfo, pos);
			pos.y += 60;

			// Label
			Label label1 = new Label(shell, SWT.BORDER);
			label1.setText("Label: Normal");
			moveControl(label1, pos);
			pos.x += 150;

			// Text
			Text text1 = new Text(shell, SWT.BORDER);
			text1.setText("Text: Normal");
			moveControl(text1, pos);
			pos.x += 150;
			Text text2 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
			text2.setText("Text: SWT.READ_ONLY");
			moveControl(text2, pos);
			pos.x += 150;

			// Combo
			Combo combo1 = new Combo(shell, SWT.BORDER);
			combo1.setText("Combo: Normal");
			moveControl(combo1, pos);
			pos.x += 150;
			Combo combo2 = new Combo(shell, SWT.BORDER | SWT.READ_ONLY);
			combo2.add("Combo: SWT.READ_ONLY");
			combo2.select(0);
			moveControl(combo2, pos);

			// New row
			pos.x = 10;
			pos.y += 40;

			// Button
			Button button1 = new Button(shell, SWT.PUSH);
			button1.setText("Button: Normal");
			moveControl(button1, pos);
			pos.x += 150;
			Button button2 = new Button(shell, SWT.PUSH | SWT.FLAT);
			button2.setText("Button: SWT.FLAT");
			moveControl(button2, pos);
			pos.x += 150;

			// Spinner
			Spinner spinner = new Spinner(shell, SWT.BORDER);
			moveControl(spinner, pos);

			// New row
			pos.x = 10;
			pos.y += 40;

			// Table
			final Table table = new Table(shell, SWT.BORDER);
			table.setHeaderVisible(true);
			TableColumn column1 = new TableColumn(table, 0);
			column1.setText("TableColumn");
			column1.setWidth(100);
			TableColumn column2 = new TableColumn(table, 0);
			column2.setText("TableColumn");
			column2.setWidth(100);
			TableItem item1 = new TableItem(table, 0);
			item1.setText("Table");
			moveControl(table, pos);
			pos.x += 300;

			// Tree
			final Tree tree = new Tree(shell, SWT.BORDER);
			final TreeItem treeItem1 = new TreeItem(tree, 0);
			treeItem1.setText("Tree");
			final TreeItem treeItem2 = new TreeItem(treeItem1, 0);
			treeItem2.setText("Item");
			moveControl(tree, pos);
			pos.x += 150;

			// List
			final List list = new List(shell, SWT.BORDER);
			list.add("List: Item 1");
			list.add("List: Item 2");
			list.add("List: Item 3");
			moveControl(list, pos);

			// New row
			pos.x = 10;
			pos.y += 80;

			// Canvas
			final Canvas canvas = new Canvas(shell, SWT.BORDER);
			final Label canvasLabel = new Label(canvas, 0);
			canvasLabel.setText("Canvas");
			moveControl(canvasLabel, new Point(0, 0));
			moveControl(canvas, pos);
			pos.x += 150;

			// Composite
			final Composite composite = new Composite(shell, SWT.BORDER);
			final Label compositeLabel = new Label(composite, 0);
			compositeLabel.setText("Composite");
			moveControl(compositeLabel, new Point(0, 0));
			moveControl(composite, pos);
		}

		// Theme setup
		if (useDarkTheme) {
			Color backColor = new Color(display, 0x30, 0x30, 0x30);
			Color foreColor = new Color(display, 0xD0, 0xD0, 0xD0);
			setColors(shell, backColor, foreColor);
		}

		// Pack and show shell
		Point shellSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		shellSize.x += 10;
		shellSize.y += 10;
		shell.setSize(shellSize);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

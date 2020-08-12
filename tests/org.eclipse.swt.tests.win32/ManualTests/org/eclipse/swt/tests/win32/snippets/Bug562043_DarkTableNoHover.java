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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug562043_DarkTableNoHover {
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

		// Enable dark scrollbars.
		// Side effect: reverts Table to classic theme.
		display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", true);

		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout(1, true));

		new Label(shell, 0).setText(
			"The problem here is that Table doesn't highlight selected\n" +
			"and hovered items with dark scrollbars enabled."
		);

		// Table
		final Table table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);
		TableColumn column1 = new TableColumn(table, 0);
		column1.setText("TableColumn");
		column1.setWidth(100);
		for (int x = 0; x < 8; x++)
		{
			TableItem item1 = new TableItem(table, 0);
			item1.setText("Table item");
		}

		// This is what triggers the problem
		table.addListener(SWT.EraseItem, event -> {
			event.detail &= ~SWT.FOREGROUND;
		});

		// With '~SWT.FOREGROUND', items need to be painted.
		// 'isTranspent=true' in 'drawText()' is important, otherwise
		// it will use background colro set by Windows, which happen to
		// be the selection color, partially hiding the problem.
		table.addListener(SWT.PaintItem, event -> {
			TableItem item = (TableItem)event.item;
			event.gc.drawText(item.getText(), event.x, event.y, true);
		});

		new Label(shell, 0).setText(
			"For comparison, Tree works fine."
		);

		// Tree
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int x = 0; x < 8; x++)
		{
			final TreeItem treeItem1 = new TreeItem(tree, 0);
			treeItem1.setText("Tree item");
			final TreeItem treeItem2 = new TreeItem(treeItem1, 0);
			treeItem2.setText("Tree subitem");
		}

		// Apply theme colors
		Color backColor = new Color(display, 0x30, 0x30, 0x30);
		Color foreColor = new Color(display, 0xD0, 0xD0, 0xD0);
		setColors(shell, backColor, foreColor);

		// Pack and show shell
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

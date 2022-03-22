/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
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

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Arrays;

public final class Bug456602_macOS_AIOOBE_Table_selectedRowIndex {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Label hint = new Label(shell, 0);
		hint.setText(
			"Bug 456602\n" +
			"----------\n" +
			"1) Use macOS\n" +
			"2) Click 'Delete items' button\n" +
			"3) Press left mouse button on last table item and HOLD the button\n" +
			"4) When item is deleted, release the button\n" +
			"5) Bug 456602: SWT will have an internal AIOOBE\n" +
			"6) Bug 456602: More exceptions are reported if you set focus to\n" +
			"   Text beforehand and move the mouse after\n" +
			"7) In real world, user would clicks faster, but crashes will\n" +
			"   still happen sometimes\n" +
			"\n" +
			"Bug 355200 and Bug 289483\n" +
			"-------------------------\n" +
			"Just to ensure that old workarounds are still working fine:\n" +
			"1) Click any Table item when multiple items are selected\n" +
			"2) SWT.Selection shall report clicked item\n" +
			"3) The order shall be: SWT.MouseDown, SWT.Selection, SWT.MouseUp"
		);

		Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.addListener(SWT.MouseDown, e -> {
			System.out.println(System.currentTimeMillis() + " SWT.MouseDown: " + Arrays.toString(table.getSelectionIndices()));
		});
		table.addListener(SWT.MouseUp, e -> {
			System.out.println(System.currentTimeMillis() + " SWT.MouseUp:   " + Arrays.toString(table.getSelectionIndices()));
		});
		table.addListener(SWT.Selection, e -> {
			System.out.println(System.currentTimeMillis() + " SWT.Selection: " + Arrays.toString(table.getSelectionIndices()));
		});

		for (int iItem = 0; iItem < 10; iItem++) {
			new TableItem(table, 0).setText("Item #" + iItem);
		}
		table.selectAll();

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Delete items after 2000ms");
		button.addListener(SWT.Selection, e -> {
			display.timerExec(2000, () -> {
				table.setItemCount(table.getItemCount() / 2);
			});
		});

		new Text(shell, 0).setText("I'm a text field");

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

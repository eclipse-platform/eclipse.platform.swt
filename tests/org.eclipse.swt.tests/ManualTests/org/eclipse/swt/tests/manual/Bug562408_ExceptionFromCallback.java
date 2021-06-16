/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug562408_ExceptionFromCallback {
	public static void main(String[] args) {
		Display display = new Display();
		Shell mainShell = new Shell(display);
		mainShell.setLayout(new GridLayout(1, true));

		final Button btnExceptionDragStart = new Button(mainShell, SWT.PUSH);
		btnExceptionDragStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnExceptionDragStart.setText("macOS: exception from dragStart() [Bug 395381]");
		btnExceptionDragStart.addListener(SWT.Selection, e -> {
			Shell shell = new Shell(mainShell);
			shell.setLayout(new GridLayout(1, true));

			final Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
			hint.setText(
				"macOS:\n" +
				"1. Drag the Table/Tree item below\n" +
				"2. JVM will hang with 100% CPU and then crash in ~5 minutes"
			);

			DragSourceAdapter throwingSource = new DragSourceAdapter() {
				@Override
				public void dragStart(DragSourceEvent event) {
					System.out.println("Note: throwing RuntimeException (DragSourceAdapter)");
					throw new RuntimeException("Test exception");
				}
			};

			// Table
			{
				final Table table = new Table(shell, SWT.BORDER);
				new TableItem(table, 0).setText("TableItem: Drag me");

				DragSource tableSource = new DragSource(table, DND.DROP_MOVE);
				tableSource.setTransfer(TextTransfer.getInstance());
				tableSource.addDragListener(throwingSource);
			}

			// Tree
			{
				final Tree tree = new Tree(shell, SWT.BORDER);
				new TreeItem(tree, 0).setText("TableItem: Drag me");

				DragSource treeSource = new DragSource(tree, DND.DROP_MOVE);
				treeSource.setTransfer(TextTransfer.getInstance());
				treeSource.addDragListener(throwingSource);
			}

			shell.pack();
			shell.open();
		});

		final Button btnExceptionMouseDown = new Button(mainShell, SWT.PUSH);
		btnExceptionMouseDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnExceptionMouseDown.setText("macOS: exception from SWT.MouseDown");
		btnExceptionMouseDown.addListener(SWT.Selection, e -> {
			Shell shell = new Shell(mainShell);
			shell.setLayout(new GridLayout(1, true));

			final Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
			hint.setText(
				"macOS:\n" +
				"1. Click the Table/Tree below\n" +
				"2. JVM will hang with 100% CPU and then crash in ~5 minutes"
			);

			Listener throwingListener = new Listener() {
				@Override
				public void handleEvent(Event event) {
					System.out.println("Note: throwing RuntimeException (SWT.MouseDown)");
					throw new RuntimeException("Test exception");
				}
			};

			// Table
			{
				final Table table = new Table(shell, SWT.BORDER);
				new TableItem(table, 0).setText("Table");
				table.addListener(SWT.MouseDown, throwingListener);
			}

			// Tree
			{
				final Tree tree = new Tree(shell, SWT.BORDER);
				new TreeItem(tree, 0).setText("Tree");
				tree.addListener(SWT.MouseDown, throwingListener);
			}

			shell.pack();
			shell.open();
		});

		final Button btnExceptionMenuArm = new Button(mainShell, SWT.PUSH);
		btnExceptionMenuArm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnExceptionMenuArm.setText("Windows, macOS: exception from SWT.Arm for menu");
		btnExceptionMenuArm.addListener(SWT.Selection, e -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(new GridLayout(1, true));

			final Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
			hint.setText(
				"Windows:\n" +
				"1. Do not expand any menus before the test\n" +
				"2. Hover mouse over 'Test | Exception on SWT.Arm'\n" +
				"3. Without closing the menu, hover mouse over 'Menu 1'\n" +
				"4. The entire popup menu will fail to render\n" +
				"\n" +
				"macOS:\n" +
				"1. If menu is not responsive, switch to other app and back (see Bug 562535)\n" +
				"2. Hover mouse over 'Test | Exception on SWT.Arm'\n" +
				"3. App will hang forever"
			);

			final Menu menuBar = new Menu(shell, SWT.BAR);
			menuBar.setVisible(true);
			shell.setMenuBar(menuBar);

			final Menu menuTest = new Menu(menuBar);
			final MenuItem itemTest = new MenuItem(menuBar, SWT.CASCADE);
			itemTest.setMenu(menuTest);
			itemTest.setText("Test");
			final MenuItem itemTestException = new MenuItem(menuTest, SWT.NONE);
			itemTestException.setText("Exception on SWT.Arm");
			itemTestException.addListener(SWT.Arm, e2 -> {
				System.out.println("Note: throwing RuntimeException (SWT.Arm)");
				throw new RuntimeException("Test exception");
			});

			for (int iTopMenu = 1; iTopMenu < 3; iTopMenu++) {
				final Menu menuTopI = new Menu(menuBar);
				final MenuItem menuItemTopI = new MenuItem(menuBar, SWT.CASCADE);
				menuItemTopI.setMenu(menuTopI);
				menuItemTopI.setText(String.format("Menu %d", iTopMenu));

				for (int iItem = 0; iItem < 5; iItem++) {
					final MenuItem menuItemTopIJ = new MenuItem(menuTopI, SWT.NONE);
					menuItemTopIJ.setText(String.format("Item %d:%d", iTopMenu, iItem));
				}
			}

			shell.pack();
			shell.open();
		});

		final Button btnConcurrentExceptions = new Button(mainShell, SWT.PUSH);
		btnConcurrentExceptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnConcurrentExceptions.setText("All: concurrent exceptions");
		btnConcurrentExceptions.addListener(SWT.Selection, e -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(new GridLayout(1, true));

			final Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
			hint.setText(
				"1. Before the patch, SWT stopped executing callbacks on first exception.\n" +
				"2. After the patch, it executes everything, so multiple exceptions can occur.\n" +
				"3. This test features a Table that will throw one exception per TableItem on paint.\n" +
				"4. You should now see the first exception as main and others as suppressed.\n" +
				"5. Inspect 'catch' clause in code and console output to verify"
			);

			final Table table = new Table(shell, 0);
			for (int i = 0; i < 5; i++)
			{
				TableItem tableItem = new TableItem(table, 0);
				tableItem.setText("TableItem#" + i);
			}

			table.addListener(SWT.PaintItem, e2 -> {
				System.out.println("Note: throwing RuntimeException (SWT.Paint)");
				String itemText = e2.item.toString();
				throw new RuntimeException("Test exception for [" + itemText + "]");
			});

			shell.pack();
			shell.open();
		});

		mainShell.pack();
		mainShell.open();
		while (!mainShell.isDisposed()) {
			try {
				if (!display.readAndDispatch())
					display.sleep();
			} catch (Exception ex) {
				System.out.format("Exception caught (with %d suppressed exceptions):%n", ex.getSuppressed().length);
				ex.printStackTrace();
			}
		}
		display.dispose();
	}
}

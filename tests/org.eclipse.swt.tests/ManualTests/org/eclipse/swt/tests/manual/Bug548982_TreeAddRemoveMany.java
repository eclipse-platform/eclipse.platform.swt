/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others.
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Bug548982_TreeAddRemoveMany {
	// Options
	// Java only
	private static boolean 	CREATE_CTOR_2PARAM 		= false;
	// Native & Java
	private static boolean 	LOCK_REDRAW 			= false;
	private static boolean 	CREATE_AFTER_EXPAND 	= false;
	private static boolean 	CREATE_REVERSE_ORDER	= false;
	private static boolean 	DELETE_PARENT_ITEM 		= false;
	private static boolean 	DELETE_AFTER_COLLAPSE 	= false;
	private static int 		NUM_ITEMS 				= 20000;

	private static Tree 	tree;
	private static TreeItem rootItem;
	private static int 		iteration = 0;

	private static void CreateRootItem() {
		rootItem = new TreeItem(tree, SWT.NONE, 0);
		rootItem.setText("Root");

		CreateExpanderSubItem();
	}

	private static void CreateExpanderSubItem() {
		new TreeItem(rootItem, SWT.NONE);
	}

	private static void CreateTreeItems() {
		iteration++;

		final long start = System.currentTimeMillis();

		if (LOCK_REDRAW) {
			tree.setRedraw(false);
		}

		// Delete artificial item from 'CreateExpanderSubItem'
		rootItem.removeAll();

		if (CREATE_REVERSE_ORDER) {
			for (int i = NUM_ITEMS - 1; i >= 0; i--) {
				TreeItem child = new TreeItem(rootItem, SWT.NONE, 0);
				child.setText("Item:" + iteration + ":" + i);
			}
		} else {
			for (int i = 0; i < NUM_ITEMS; i++) {
				TreeItem child;

				if (CREATE_CTOR_2PARAM)
					child = new TreeItem(rootItem, SWT.NONE);
				else
					child = new TreeItem(rootItem, SWT.NONE, i);

				child.setText("Item:" + iteration + ":" + i);
			}
		}

		if (LOCK_REDRAW) {
			tree.setRedraw(true);
		}

		final long end = System.currentTimeMillis();
		System.out.println("CreateTreeItems: " + (end - start) + " ms");
	}

	private static void DeleteTreeItems() {
		final long start = System.currentTimeMillis();

		if (LOCK_REDRAW) {
			tree.setRedraw(false);
		}

		if (DELETE_PARENT_ITEM) {
			rootItem.dispose();
			CreateRootItem();
		} else {
			rootItem.removeAll();
			CreateExpanderSubItem();
		}

		if (LOCK_REDRAW) {
			tree.setRedraw(true);
		}

		final long end = System.currentTimeMillis();
		System.out.println("DeleteTreeItems: " + (end - start) + " ms");
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setSize(800, 600);

		Label lblHint = new Label(shell, 0);
		lblHint.setText("Expand/collapse tree item. Timings will be printed to console. Various options are available in code near // Options.");

		tree = new Tree(shell, SWT.BORDER | SWT.VIRTUAL);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		final TreeColumn col = new TreeColumn(tree, SWT.NONE);
		col.setText("Column 1");
		col.setWidth(400);

		tree.addListener(SWT.Collapse, event -> {
			if (!DELETE_AFTER_COLLAPSE) {
				DeleteTreeItems();
			} else {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						DeleteTreeItems();
					}
				});
			}
		});

		tree.addListener(SWT.Expand, event -> {
			if (!CREATE_AFTER_EXPAND) {
				CreateTreeItems();
			} else {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						CreateTreeItems();
					}
				});
			}
		});

		CreateRootItem();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
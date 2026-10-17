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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug575787_Tree_SetItemCount_Perf {
	final static int 	 PERF_NUM_ITEMS 		= 100_000;

	public static void main (String[] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout ());

		Button btnTestPerf = new Button(shell, 0);
		btnTestPerf.setText ("Test Tree/Table .setItemCount() performance");
		btnTestPerf.addListener (SWT.Selection, e -> {
			for (int iIter = 0; iIter < 20; iIter++) {
				StringBuilder sb = new StringBuilder ();

				// Tree
				for (int iVirtual = 0; iVirtual < 2; iVirtual++)
				{
					Tree tree;
					if (iVirtual != 0) {
						tree = new Tree (shell, SWT.VIRTUAL);
						sb.append ("Tree-VIRTUAL:{");
					} else {
						tree = new Tree (shell, 0);
						sb.append ("Tree-REGULAR:{");
					}

					// The best way to insert many items is to do so when
					// 1) Redraw is disabled
					// 2) Parent item is collapsed
					// see documentation in TreeItem.TreeItem(org.eclipse.swt.widgets.TreeItem, int, int)
					final TreeItem treeItem = new TreeItem (tree, 0);
					tree.setRedraw (false);

					sb.append (String.format (
						"%.2fsec=setItemCount ",
						measureTime (() -> treeItem.setItemCount (PERF_NUM_ITEMS))
					));

					sb.append (String.format (
						"%.2fsec=dispose",
						measureTime (() -> tree.dispose ())
					));

					sb.append ("} ");
				}

				// Table
				for (int iVirtual = 0; iVirtual < 2; iVirtual++)
				{
					Table table;
					if (iVirtual != 0) {
						table = new Table (shell, SWT.VIRTUAL);
						sb.append ("Table-VIRTUAL:{");
					} else {
						table = new Table (shell, 0);
						sb.append ("Table-REGULAR:{");
					}

					// The best way to insert many items is to do so when redraw is disabled.
					table.setRedraw (false);

					sb.append (String.format (
						"%.2fsec=setItemCount ",
						measureTime (() -> table.setItemCount (PERF_NUM_ITEMS))
					));

					sb.append (String.format (
						"%.2fsec=dispose",
						measureTime (() -> table.dispose ())
					));

					sb.append ("} ");
				}

				System.out.println (sb);

				// Avoid OOM due to growing 'Display.skinList'
				display.readAndDispatch ();
			}
		});

		Button btnTestColumns = new Button(shell, 0);
		btnTestColumns.setText ("Test correctness");
		btnTestColumns.addListener (SWT.Selection, e -> {
			final Shell shellTest = new Shell (display);
			shellTest.setLayout (new GridLayout (2, true));

			final Label hint = new Label (shellTest, 0);
			GridData gridData = new GridData (SWT.FILL, SWT.FILL, true, false);
			gridData.horizontalSpan = 2;
			hint.setLayoutData (gridData);
			hint.setText ("Both Trees are expected to show increasing numbers");

			new Label(shellTest, 0).setText ("REGULAR");
			new Label(shellTest, 0).setText ("VIRTUAL");

			for (int isVirtual = 0; isVirtual < 2; isVirtual++) {
				int virtual = (isVirtual == 0) ? 0 : SWT.VIRTUAL;
				final Tree tree = new Tree (shellTest, virtual | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				tree.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

				// Test root items
				{
					new TreeItem (tree, 0).setText ("0");
					new TreeItem (tree, 0).setText ("2");
					new TreeItem (tree, 0, 1).setText ("1");

					tree.setItemCount (6);

					tree.getItem (3).setText ("3");
					tree.getItem (4).setText ("4");
					tree.getItem (5).setText ("5");

					new TreeItem (tree, 0).setText ("6");
					new TreeItem (tree, 0).setText ("8");
					new TreeItem (tree, 0, 7).setText ("7");

					tree.setItemCount (12);

					tree.getItem (9).setText ("9");
					tree.getItem (10).setText ("10");
					tree.getItem (11).setText ("11");
				}

				// Test child items
				{
					TreeItem item = tree.getItem (0);

					new TreeItem (item, 0).setText ("0");
					new TreeItem (item, 0).setText ("2");
					new TreeItem (item, 0, 1).setText ("1");

					item.setItemCount (6);

					item.getItem (3).setText ("3");
					item.getItem (4).setText ("4");
					item.getItem (5).setText ("5");

					new TreeItem (item, 0).setText ("6");
					new TreeItem (item, 0).setText ("8");
					new TreeItem (item, 0, 7).setText ("7");

					item.setItemCount (12);

					item.getItem (9).setText ("9");
					item.getItem (10).setText ("10");
					item.getItem (11).setText ("11");

					item.setExpanded (true);
				}
			}

			shellTest.pack ();
			shellTest.open ();
		});

		shell.pack ();
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}

		display.dispose ();
	}

	static double measureTime (Runnable runnable) {
		System.gc ();

		long time1 = System.nanoTime ();
		runnable.run ();
		long time2 = System.nanoTime ();

		return (time2 - time1) / 1_000_000_000f;
	}
}

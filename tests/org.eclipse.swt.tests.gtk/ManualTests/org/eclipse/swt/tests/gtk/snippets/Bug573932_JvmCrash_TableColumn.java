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

package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug573932_JvmCrash_TableColumn {
	final static boolean PERF_TEST_TABLE 		= true;
	final static boolean PERF_TEST_TREE 		= true;
	final static int 	 PERF_NUM_ITEMS 		= 10000;

	public static void main (String[] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout ());

		Button btnTestCrash = new Button(shell, 0);
		btnTestCrash.setText ("Test crash (fixed in Bug 573932)");
		btnTestCrash.addListener (SWT.Selection, e -> {
			final Shell shellTest = new Shell (display);
			shellTest.setLayout (new GridLayout ());

			final Label hint = new Label (shell, 0);
			hint.setText (
				"1) Click the button below\n" +
				"2) There will be a JVM crash"
			);

			final Table table = new Table (shellTest, SWT.VIRTUAL);

			table.addListener (SWT.SetData, event -> {
				TableItem item = (TableItem) event.item;

				// Crash #1: item.getBoundsinPixels()
				item.getBounds();

				// Crash #2: item.setText()
				for (int i = 0; i < table.getColumnCount (); i++) {
					item.setText (i, "text");
				}
			});

			new TableColumn (table, 0).setWidth (100);
			table.setItemCount (5);

			Button button = new Button(shellTest, 0);
			button.setText ("Click me to crash");
			button.addListener (SWT.Selection, e2 -> {
				table.clearAll ();
				new TableColumn (table, 0);
			});

			shellTest.pack ();
			shellTest.open ();
		});

		Button btnTestPerf = new Button(shell, 0);
		btnTestPerf.setText ("Test performance (improved in Bug 573932)");
		btnTestPerf.addListener (SWT.Selection, e -> {
			final Shell shellTest = new Shell (display);
			shellTest.setLayout (new GridLayout ());

			final Label hint = new Label (shellTest, 0);
			hint.setText (
				"Note about growing Tree timing :\n" +
				"1) Bug 573932 has nothing to do with it\n" +
				"2) This is a GTK issue; see:\n" +
				"   https://gitlab.gnome.org/GNOME/gtk/-/issues/4223\n" +
				""
			);

			Button button = new Button(shellTest, SWT.PUSH);
			button.setText ("Test");
			button.addListener (SWT.Selection, e2 -> {
				for (int iIter = 0; iIter < 20; iIter++) {
					StringBuilder sb = new StringBuilder ();

					if (PERF_TEST_TABLE) {
						final Table table = new Table (shellTest, 0);
						new TableColumn (table, 0);

						sb.append (String.format (
							"Table:SetItemCount=%.2fsec ",
							measureTime(() -> table.setItemCount (PERF_NUM_ITEMS))
						));

						sb.append (String.format (
							"Table:AddColumn=%.2fsec ",
							measureTime(() -> new TableColumn (table, 0))
						));

						table.dispose ();
					}

					if (PERF_TEST_TREE) {
						final Tree tree = new Tree (shellTest, 0);
						new TreeColumn (tree, 0);

						sb.append (String.format (
							"Tree:SetItemCount=%.2fsec ",
							measureTime(() -> tree.setItemCount (PERF_NUM_ITEMS))
						));

						sb.append (String.format (
							"Tree:AddColumn=%.2fsec ",
							measureTime(() -> new TreeColumn (tree, 0))
						));

						tree.dispose ();
					}

					System.out.println (sb);

					// Avoid OOM due to accumulating Tables in 'Display.skinList'
					display.readAndDispatch ();
				}
			});

			shellTest.pack ();
			shellTest.open ();
		});

		Button btnTestColumns = new Button(shell, 0);
		btnTestColumns.setText ("Test correctness (unchanged)");
		btnTestColumns.addListener (SWT.Selection, e -> {
			final Shell shellTest = new Shell (display);
			shellTest.setLayout (new GridLayout (4, true));

			final Label hint = new Label (shellTest, 0);
			GridData gridData = new GridData (SWT.FILL, SWT.FILL, true, true);
			gridData.horizontalSpan = 4;
			hint.setLayoutData (gridData);
			hint.setText (
				"What to pay attention to :\n" +
				"1) Each time 4 columns are added on top of max old column count,\n" +
				"   models are extended (this is when item selections are lost).\n" +
				"   Item labels/colors shall be preserved.\n" +
				"2) When all columns are removed, models are resized to 1 column.\n" +
				"   Item labels/colors shall be preserved as before removing the last column.\n" +
				""
			);

			new Label(shellTest, 0).setText ("Table(0)");
			new Label(shellTest, 0).setText ("Table(SWT.VIRTUAL)");
			new Label(shellTest, 0).setText ("Tree(0)");
			new Label(shellTest, 0).setText ("Tree(SWT.VIRTUAL)");

			Table tableN = createTable(shellTest, false);
			Table tableV = createTable(shellTest, true);
			Tree treeN = createTree(shellTest, false);
			Tree treeV = createTree(shellTest, true);
			final int[] columnID = new int[] {2};

			Button btnAddColumn = new Button(shellTest, SWT.PUSH);
			gridData = new GridData (SWT.FILL, SWT.FILL, true, true);
			gridData.horizontalSpan = 4;
			btnAddColumn.setLayoutData (gridData);
			btnAddColumn.setText ("Add column");
			btnAddColumn.addListener (SWT.Selection, e2 -> {
				addColumn (tableN, columnID[0]);
				addColumn (tableV, columnID[0]);
				addColumn (treeN,  columnID[0]);
				addColumn (treeV,  columnID[0]);
				columnID[0]++;
			});

			Button btnDelColumn = new Button(shellTest, SWT.PUSH);
			gridData = new GridData (SWT.FILL, SWT.FILL, true, true);
			gridData.horizontalSpan = 4;
			btnDelColumn.setLayoutData (gridData);
			btnDelColumn.setText ("Del left column");
			btnDelColumn.addListener (SWT.Selection, e2 -> {
				if (0 != tableN.getColumnCount ())
					tableN.getColumn (0).dispose ();

				if (0 != tableV.getColumnCount ())
					tableV.getColumn (0).dispose ();

				if (0 != treeN.getColumnCount ())
					treeN.getColumn (0).dispose ();

				if (0 != treeV.getColumnCount ())
					treeV.getColumn (0).dispose ();
			});

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

	static Table createTable(Shell shell, boolean isVirtual) {
		final int virtual = isVirtual ? SWT.VIRTUAL : 0;
		final Table control = new Table (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | virtual);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		control.setHeaderVisible (true);

		control.setItemCount (10);
		addColumn (control, 0);
		addColumn (control, 1);

		for (int i = 0; i < control.getItemCount (); i++) {
			control.getItem (i).setForeground (Display.getCurrent ().getSystemColor (SWT.COLOR_RED + i));
		}

		return control;
	}

	static Tree createTree(Shell shell, boolean isVirtual) {
		final int virtual = isVirtual ? SWT.VIRTUAL : 0;
		final Tree control = new Tree (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | virtual);
		control.setHeaderVisible (true);
		control.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		control.setItemCount (10);
		addColumn (control, 0);
		addColumn (control, 1);

		for (int i = 0; i < control.getItemCount (); i++) {
			control.getItem (i).setForeground (Display.getCurrent ().getSystemColor (SWT.COLOR_RED + i));
		}

		return control;
	}

	static double measureTime (Runnable runnable) {
		System.gc ();

		long time1 = System.nanoTime ();
		runnable.run ();
		long time2 = System.nanoTime ();

		return (time2 - time1) / 1_000_000_000f;
	}

	static void addColumn(Table control, int id) {
		TableColumn column = new TableColumn (control, 0);
		column.setWidth (50);
		column.setText ("Col#" + id);

		int columnIndex = control.getColumnCount () - 1;
		for (int i = 0; i < control.getItemCount (); i++) {
			control.getItem (i).setText (columnIndex, i + ":" + id);
		}
	}

	static void addColumn(Tree control, int id) {
		TreeColumn column = new TreeColumn (control, 0);
		column.setWidth (50);
		column.setText ("Col#" + id);

		int columnIndex = control.getColumnCount () - 1;
		for (int i = 0; i < control.getItemCount (); i++) {
			control.getItem (i).setText (columnIndex, i + ":" + id);
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug182598_GTK_crash_set_item_count_in_SetData_callback {

	// If we want to have a Java dump on crash, we need to set a different number of tree items on SWT.SetData callback.
	// I.e. =false -> reproduce https://bugs.eclipse.org/bugs/show_bug.cgi?id=182598
	//      =true  -> reproduce https://bugs.eclipse.org/bugs/show_bug.cgi?id=545919
	private static final boolean CRASH_WITH_DUMP = false;

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(300, 200);
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.VIRTUAL);
		tree.addListener(SWT.SetData, event -> {
			int itemCount = CRASH_WITH_DUMP ? 0 : 1;
			// JVM will crash now without the patch for bug 182598
			tree.setItemCount(itemCount);
		});

		// NB: Sometimes this N must be changed to crash reliably - in range from 5 to 20
		int N = 15;
		for (int i = 0; i < N; ++i) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			int c = i + 1;
			item.setText("item " + c);
			if (i >= 5) {
				item.setText("another item " + c);
			}
			item.setData("something " + c);
			tree.setSelection(item);
		}

		Button button = new Button(shell, SWT.NONE);
		button.setText("Tree.clearAll()");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tree.clearAll(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
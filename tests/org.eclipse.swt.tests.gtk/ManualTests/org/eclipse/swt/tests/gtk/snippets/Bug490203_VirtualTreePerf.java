/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug490203_VirtualTreePerf {
	static final int COUNT = 10_000;

	public static void main(String[] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new RowLayout (SWT.VERTICAL));
		final Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		TreeItem[] top = { null };
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			if (item.getParentItem() == null) {
				top[0] = item;
				item.setText("top");
			} else {
				int index = top[0].indexOf (item);
				item.setText ("Item " + index);
			}
			System.out.println (item.getText ());
		});
		tree.setLayoutData (new RowData (200, 200));
		Button button = new Button (shell, SWT.PUSH);
		button.setText ("&Add Items");
		final Label label = new Label(shell, SWT.NONE);
		button.addListener (SWT.Selection, event -> {
			long t1 = System.currentTimeMillis ();
			top[0].setItemCount (COUNT);
			top[0].setExpanded(true);
			long t2 = System.currentTimeMillis ();
			label.setText ("Items: " + COUNT + ", Time: " + (t2 - t1) + " (ms)");
			shell.layout ();
		});
		tree.setItemCount (1);
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}

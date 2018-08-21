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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug133691_PaintItemColumnWidth {
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10,10,500,300);
		final Tree tree = new Tree(shell, SWT.NONE);
		tree.setHeaderVisible(true);
		tree.setBounds(10,10,450,200);
		for (int i = 0; i < 4; i++) {
			new TreeColumn(tree, SWT.NONE).setWidth(100);
		}
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(new String[] {"root 0", "root 1", "root 2", "root 3"});
		Button button = new Button(shell, SWT.PUSH);
		button.setBounds(10,230,200,30);
		button.setText("Add PaintItem Listener");
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				tree.addListener(SWT.PaintItem, new Listener() {	// <---
					@Override
					public void handleEvent(Event event) {
						// do nothing
					}
				});
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}

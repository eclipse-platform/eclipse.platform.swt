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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug182598_TreeAssertionFailed {
	public static void main(String[] args) {
		
		final Display display = new Display();
		Shell shell = new Shell(display);
		final Tree tree = new Tree(shell, SWT.VIRTUAL);
		tree.setItemCount(2);
		tree.setSelection(tree.getItem(1));
		tree.clearAll(true);
		tree.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				if (item.getParentItem() == null) {
					item.setText("item - " + event.index);
				} else { 
					TreeItem parent = item.getParentItem();
					int parentIndex = tree.indexOf(parent);
					item.setText("child " + parentIndex + "/" + event.index);
				}
				if (event.index == 1) {
					Thread.dumpStack();
					tree.setItemCount(1);							
				}
			}
		});
		shell.setSize(250, 150);
		tree.setBounds(10, 10, 200, 100);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	
}

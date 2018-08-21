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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug372607_TreeArrowExpand {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		
		Tree tree = new Tree(shell, SWT.MULTI | SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		for (int i = 0; i < 10; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("Item " + i);
			for (int j = 0; j < 10; j++) {
				TreeItem jtem = new TreeItem(item, SWT.NONE);
				jtem.setText("Item " + i + ", " + j);
				for (int k = 0; k < 10; k++) {
					TreeItem ktem = new TreeItem(jtem, SWT.NONE);
					ktem.setText("Item " + i + ", " + j + ", " + k);
				}
			}
		}
		tree.addTreeListener(new TreeListener() {
			@Override
			public void treeExpanded(TreeEvent e) {
				System.out.println(" Expanded:  " + e.item + ". getExpanded = " + ((TreeItem) e.item).getExpanded());
				System.out.println(" " + e);
			}
			
			@Override
			public void treeCollapsed(TreeEvent e) {
				System.out.println(" Collapsed: " + e.item + ". getExpanded = " + ((TreeItem) e.item).getExpanded());
				System.out.println(" " + e);
			}
		});
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("  Selected: " + e);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("  DefaultSelected: " + e);
			}
		});
		tree.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("Up:   " + e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Down: " + e);
			}
		});

		shell.setSize(200, 320);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

}

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.FillLayout;

public class Bug244274_ShellNoFocus {
	private Shell sShell = null;
	private Tree tree = null;

	private void createTree() {
		tree = new Tree(sShell, SWT.NONE);		   
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Bug244274_ShellNoFocus thisClass = new Bug244274_ShellNoFocus();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(new FillLayout());
		createTree();
		populateTree();
		sShell.setSize(new Point(300, 200));
	}

	private void populateTree() {
		if(tree!=null && !tree.isDisposed()){
			TreeItem rootItem = new TreeItem(tree, SWT.NULL);
			rootItem.setText("Root");
			
			TreeItem item11 = new TreeItem(rootItem, SWT.NULL);
			item11.setText("1");
			
			TreeItem item111 = new TreeItem(item11, SWT.NULL);
			item111.setText("1_1");
			TreeItem item112 = new TreeItem(item11, SWT.NULL);
			item112.setText("1_2");
			
			TreeItem item1111 = new TreeItem(item111, SWT.NULL);
			item1111.setText("1_1_1");
			TreeItem item1112 = new TreeItem(item111, SWT.NULL);
			item1112.setText("1_1_2");
			
			item1111.setExpanded(true);
			item1112.setExpanded(true);
			item111.setExpanded(true);
			item112.setExpanded(true);
			item11.setExpanded(true);
			rootItem.setExpanded(true);
		}
	}

}
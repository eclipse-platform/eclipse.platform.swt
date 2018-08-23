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
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug538114_ExpandBarEmptyAll {
	public static void main(String[] args) {
	    final Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setSize(400, 600);
	    shell.setLayout(new FillLayout());
	    shell.setText("ExpandBar Bug");

	    expandBar(shell);

	    shell.open();

	    while (!shell.isDisposed()) {
	            if (!display.readAndDispatch())
	                    display.sleep();
	    }
	    display.dispose();
	}

	private static void expandBar(Shell shell) {
        ExpandBar bar = new ExpandBar(shell, SWT.NONE);

        Composite expandControl = new Composite(bar, SWT.NONE);
        expandControl.setLayout(new FillLayout(SWT.VERTICAL));

        ExpandItem expandItem = new ExpandItem(bar, SWT.NONE, 0);
        expandItem.setText("expand");
        expandItem.setControl(expandControl);
        expandItem.setExpanded(false);
        expandItem.setHeight(shell.getSize().y - 50);

        Label listLabel = new Label(expandControl, SWT.NONE);
        listLabel.setText("List");
		List list = new List(expandControl, SWT.NONE);
		for (int i = 0; i < 3; ++i) {
			list.add("list item " + i);
		}

		Label tableLabel = new Label(expandControl, SWT.NONE);
		tableLabel.setText("Table");
		Table table = new Table(expandControl, SWT.NONE);
		for (int i = 0; i < 3; ++i) {
			TableItem item1 = new TableItem(table, SWT.NONE);
			item1.setText("table item " + i);
		}

		Label treeLabel = new Label(expandControl, SWT.NONE);
		treeLabel.setText("Table");
		Tree tree = new Tree(expandControl, SWT.NONE);
		for (int i = 0; i < 3; ++i) {
			TreeItem item1 = new TreeItem(tree, SWT.NONE);
			item1.setText("tree item " + i);
		}
	}
}
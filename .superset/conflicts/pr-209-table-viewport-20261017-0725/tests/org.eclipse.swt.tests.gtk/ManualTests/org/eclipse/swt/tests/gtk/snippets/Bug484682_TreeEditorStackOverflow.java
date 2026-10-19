/*******************************************************************************
 * Copyright (c) 2020  Mark Peters and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Mark Peters - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
/*
 * Title: Bug 484682: [GTK3] Stack overflow due to tree editor resizes
 * How to run: Run snippet.
 * Bug description: A stack overflow exception is thrown, during a tree editor resize.
 * Expected results: No stack overflow occurs.
 * GTK Version(s): GTK3.22.10
 */
public class Bug484682_TreeEditorStackOverflow {

    public static void main(String[] args) {
        Display display = new Display();

        Shell shell = new Shell(display, SWT.DIALOG_TRIM);
        shell.setLayout(new FillLayout());

        // Create a tree
        Tree tree = new Tree(shell, SWT.NONE);
        tree.setHeaderVisible(true);

        new TreeColumn(tree, SWT.NONE);
        TreeColumn lastColumn = new TreeColumn(tree, SWT.NONE);

        shell.pack();

        // Need to have vertical scrollbar and not have horizontal scrollbar
        shell.setSize(2000, 100);

        shell.open();

        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText(0, "test");
        new TreeEditor(tree).setEditor(new Canvas(tree, SWT.NONE), item, 1);

        lastColumn.pack();

        // Create rows
        for (int i = 0; i < 10; ++i) {
            new TreeItem(tree, SWT.NONE);
        }

        lastColumn.pack();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
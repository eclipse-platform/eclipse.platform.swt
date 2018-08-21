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
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Bug480926_CellEditorPosition {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        final Tree tree = new Tree(shell, SWT.FULL_SELECTION);
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);

        TreeColumn column1 = new TreeColumn(tree, 0);
        column1.setText("Column 1");
        column1.setWidth(150);

        TreeColumn column2 = new TreeColumn(tree, 0);
        column2.setText("Column 2");
        column2.setWidth(150);

        TreeItem item = new TreeItem(tree, SWT.NONE, 0);
        item.setText("Row 1");

        final TreeEditor treeEditor = new TreeEditor(tree);
        treeEditor.horizontalAlignment = SWT.LEFT;
        treeEditor.grabHorizontal = true;

        tree.addMouseListener(new MouseAdapter() {
            @Override
			public void mouseUp(MouseEvent e) {
                TreeItem item = tree.getSelection()[0];
                Text text = new Text(tree, SWT.NONE);
                treeEditor.setEditor(text, item, 1);
            }
        });

        tree.addListener(SWT.PaintItem, new Listener() {
            @Override
            public void handleEvent(Event event) {

            }
        });

        shell.setSize(600, 400);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }
}

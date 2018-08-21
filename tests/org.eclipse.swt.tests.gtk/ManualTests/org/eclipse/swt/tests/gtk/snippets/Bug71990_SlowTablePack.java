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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug71990_SlowTablePack {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        long start = System.currentTimeMillis();

        final Composite parent = new Composite(shell, SWT.NONE);
        parent.setLayout(new FillLayout());
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));
        final StackLayout layout = new StackLayout();
        parent.setLayout(layout);

        final Composite c1 = new Composite(parent, SWT.NONE);
        c1.setLayout(new FillLayout());
        final Composite c2 = new Composite(parent, SWT.NONE);
        c2.setLayout(new FillLayout());

        final Button button = new Button(shell, SWT.NONE);
        button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        button.setText("Switch");

        final Table t1 = makeTable(c1);
        final Table t2 = makeTable(c2);

        button.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                long st = System.currentTimeMillis();

                if (layout.topControl == c1) {
                    layout.topControl = c2;
                    makeColumns(t2, false);
                } else {
                    layout.topControl = c1;
                    makeColumns(t1, false);
                }

                parent.layout();

                System.out.println("Layout: "
                        + (System.currentTimeMillis() - st));

            }
        });

        layout.topControl = c1;

        long packTime = System.currentTimeMillis();

        shell.pack();
        shell.setSize(400, 400);
        shell.open();

        System.out.println("Pack: " + (System.currentTimeMillis() - packTime));

        System.out.println("Finish: " + (System.currentTimeMillis() - start));
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    public static Table makeTable(Composite parent) {
        Table table = new Table(parent, SWT.MULTI | SWT.BORDER
                | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        String[] titles = { " ", "C", "!", "Description", "Resource",
                "In Folder", "Location" };
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NULL);
            column.setText(titles[i]);
        }
        makeColumns(table, true);
        table.pack();
        return table;
    }

    public static void makeColumns(Table table, boolean pack) {
        int count = 1024;

        long time = System.currentTimeMillis();

        table.removeAll();

        System.out.println("Time RemoveAll: "
                + (System.currentTimeMillis() - time));

        long time0 = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            TableItem item = new TableItem(table, SWT.NULL);
            item.setText(0, "x");
            item.setText(1, "y");
            item.setText(2, "!");
            item.setText(3, "this stuff behaves the way I expect");
            item.setText(4, "almost everywhere");
            item.setText(5, "some.folder");
            item.setText(6, "line " + i + " in nowhere");
        }

        long time1 = System.currentTimeMillis();

        System.out.println("Time add Items: " + (time1 - time0));

        if (pack) {
            for (int i = 0; i < table.getColumnCount(); i++)
                table.getColumn(i).pack();
            long time2 = System.currentTimeMillis();
            System.out.println("Time column pack: " + (time2 - time1));
        }

    }
}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Table example snippet: sort a table by column
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import java.text.*;
import java.util.*;

public class Snippet2 {

public static void main (String [] args) {
    Display display = new Display ();
    Shell shell = new Shell (display);
    shell.setLayout(new FillLayout());
    final Table table = new Table(shell, SWT.BORDER);
    table.setHeaderVisible(true);
    TableColumn column1 = new TableColumn(table, SWT.NONE);
    column1.setText("Column 1");
    TableColumn column2 = new TableColumn(table, SWT.NONE);
    column2.setText("Column 2");
    TableItem item = new TableItem(table, SWT.NONE);
    item.setText(new String[] {"a", "3"});
    item = new TableItem(table, SWT.NONE);
    item.setText(new String[] {"b", "2"});
    item = new TableItem(table, SWT.NONE);
    item.setText(new String[] {"c", "1"});
    column1.pack();
    column2.pack();
    column1.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
            // sort column 1
            TableItem[] items = table.getItems();
            Collator collator = Collator.getInstance(Locale.getDefault());
            for (int i = 1; i < items.length; i++) {
                String value1 = items[i].getText(0);
                for (int j = 0; j < i; j++){
                    String value2 = items[j].getText(0);
                    if (collator.compare(value1, value2) < 0) {
                        String[] values = {items[i].getText(0), items[i].getText(1)};
                        items[i].dispose();
                        TableItem item = new TableItem(table, SWT.NONE, j);
                        item.setText(values);
                        items = table.getItems();
                        break;
                    }
                }
            }
        }
    });
    column2.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
            // sort column 2
            TableItem[] items = table.getItems();
            Collator collator = Collator.getInstance(Locale.getDefault());
            for (int i = 1; i < items.length; i++) {
                String value1 = items[i].getText(1);
                for (int j = 0; j < i; j++){
                    String value2 = items[j].getText(1);
                    if (collator.compare(value1, value2) < 0) {
                        String[] values = {items[i].getText(0), items[i].getText(1)};
                        items[i].dispose();
                        TableItem item = new TableItem(table, SWT.NONE, j);
                        item.setText(values);
                        break;
                    }
                }
            }
        }
    });
    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}

}

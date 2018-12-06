package org.eclipse.swt.tests.gtk.snippets;
/*******************************************************************************
 * Copyright (c) 2000, 2018 Red Hat and others.
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
/*
 * Title: Bug 535978: [GTK3] Bad Scrolling behaviour of tables with Checkbox
 * How to run: Run snippet, and resize the window such that vertical scroll bars appear
 * Bug description: When scrolling down, the TableEditor widgets will draw over the table headers
 * Expected results: No widgets should be drawn on top of the table headers, ever
 * GTK Version(s): GTK3 all
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Bug535978_TableEditorHeader {
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    for (int i = 0; i < 3; i++) {
      TableColumn column = new TableColumn(table, SWT.NONE);
      column.setText("C"+i);
      column.setWidth(100);
    }
    for (int i = 0; i < 12; i++) {
      new TableItem(table, SWT.NONE);
    }
    TableItem[] items = table.getItems();
    for (int i = 0; i < items.length; i++) {
    	if (i < 6) {
    	      TableEditor editor = new TableEditor(table);
    	      Text text = new Text(table, SWT.NONE);
    	      text.setText("Text");
    	      editor.grabHorizontal = true;
    	      editor.setEditor(text, items[i], 1);
    	      editor = new TableEditor(table);
    	      Button button = new Button(table, SWT.CHECK);
    	      button.pack();
    	      editor.minimumWidth = button.getSize().x;
    	      editor.horizontalAlignment = SWT.LEFT;
    	      editor.setEditor(button, items[i], 0);
    	} else {
    		items[i].setText("item text");
    	}
    }
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
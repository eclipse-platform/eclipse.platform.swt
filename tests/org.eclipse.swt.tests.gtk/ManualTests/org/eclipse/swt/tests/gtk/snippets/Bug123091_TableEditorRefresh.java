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


import org.eclipse.swt.*;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
* Displays a widget.table
*/
public class Bug123091_TableEditorRefresh {
 public static void main(String[] args) {
   new Bug123091_TableEditorRefresh().run();
 }

 public void run() {
   Display display = new Display();
   Shell shell = new Shell(display);
   shell.setLayout(new FillLayout());
   Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
   table.setHeaderVisible(true);
   table.setLinesVisible(true);
   for (int i = 0; i < 6; i++) {
     TableColumn column = new TableColumn(table, SWT.NONE);
     column.setMoveable(true);
     column.setResizable(true);
     column.setWidth(100);
   }

   for (int i = 0; i < 10; i++) {
     TableItem item = new TableItem(table, SWT.NONE);
     for (int j = 0; j < 5; j++) {
       item.setText(j, "Row " + i + ", Column " + j);
     }
     ProgressBar pbar = new ProgressBar(table, SWT.SMOOTH|SWT.RESIZE);
     pbar.setMinimum(0);
   pbar.setSelection(60);
   TableEditor editor = new TableEditor(table);
   editor.grabHorizontal = editor.grabVertical = true;
   editor.setEditor(pbar,item,5);

   }
   shell.pack();
   shell.open();
   while (!shell.isDisposed()) {
     if (!display.readAndDispatch()) {
       display.sleep();
     }
   }
   display.dispose();
 }
}
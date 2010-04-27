/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This example shows how to use AccessibleTableListener and
 * AccessibleTableCellListener to provide information to an AT.
 * See CTable, CTableColumn, and CTableItem for details.
 */
public class AccessibleTableExample {
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_accessibility"); //$NON-NLS-1$
	static CTable table1;
	
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}			
	}
	static final String [] itemText = new String [] {
			AccessibleTableExample.getResourceString("color1"),
			AccessibleTableExample.getResourceString("color2"),
			AccessibleTableExample.getResourceString("color3"),
			AccessibleTableExample.getResourceString("color4"),
			AccessibleTableExample.getResourceString("color5"),};

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("Accessible Table Example");
		
		Group group = new Group(shell, SWT.NONE);
		group.setText("Tables With Accessible Cell Children");
		group.setLayout(new GridLayout());

		new Label(group, SWT.NONE).setText("CTable with column headers");
		
		table1 = new CTable(group, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		table1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table1.setHeaderVisible(true);
		table1.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			CTableColumn column = new CTableColumn(table1, SWT.NONE);
			column.setText("Col " + col);
			column.setWidth(50);
		}
		for (int row = 0; row < 4; row++) {
			CTableItem item = new CTableItem(table1, SWT.NONE);
			item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		}
		
		Composite btnGroup = new Composite(group, SWT.NONE);
		btnGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Button btn = new Button(btnGroup, SWT.PUSH);
		btn.setText("Add rows");
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int currSize = table1.getItemCount();
				int colCount = table1.getColumnCount();
				CTableItem item = new CTableItem(table1, SWT.NONE);
				String[] cells = new String[colCount];
				
				for (int i = 0; i < colCount; i++) {
					cells[i] = "C" + i + "R" + currSize;
				}
				item.setText(cells);
			}
		});
		btn = new Button(btnGroup, SWT.PUSH);
		btn.setText("Remove rows");
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int currSize = table1.getItemCount();
				if (currSize > 0) {
					table1.remove(currSize - 1);
				}
			}
		});
		btn = new Button(btnGroup, SWT.PUSH);
		btn.setText("Remove selected rows");
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				CTableItem[] selectedItems = table1.getSelection();
				for (int i = 0; i < selectedItems.length; i++) {
					selectedItems[i].dispose();
				}
			}
		});
		btn = new Button(btnGroup, SWT.PUSH);
		btn.setText("Add column");
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int currSize = table1.getColumnCount();
				CTableColumn item = new CTableColumn(table1, SWT.NONE);
				item.setText("Col " + currSize);
				item.setWidth(50);
			}
		});
		btn = new Button(btnGroup, SWT.PUSH);
		btn.setText("Remove last column");
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int colCount = table1.getColumnCount();
				
				if (colCount > 0) {
					CTableColumn column = table1.getColumn(colCount - 1);
					column.dispose();
				}
			}
		});
		
		new Label(group, SWT.NONE).setText("CTable used as a list");
		
		CTable table2 = new CTable(group, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		table2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table2.setLinesVisible(true);
		for (int row = 0; row < itemText.length; row++) {
			CTableItem item = new CTableItem(table2, SWT.NONE);
			item.setText(itemText[row]);
		}
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
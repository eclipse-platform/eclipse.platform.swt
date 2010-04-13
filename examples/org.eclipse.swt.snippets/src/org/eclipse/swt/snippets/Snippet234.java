/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
/* 
 * Table example snippet: Fixed first column and horizontal scroll remaining columns
 *
 * For more info on custom-drawing TableItem and TreeItem content see 
 * http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet234 {
public static void main (String [] args) {
	int rowCount = 40;
	int columnCount = 15;
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	
	Composite parent = new Composite(shell, SWT.BORDER);
	GridLayout layout = new GridLayout(2, false);
	layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
	parent.setLayout(layout);
	final Table leftTable = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION);
	leftTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
	leftTable.setHeaderVisible(true);
	final Table rightTable = new Table(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	rightTable.setHeaderVisible(true);
	GridData table2Data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
	rightTable.setLayoutData(table2Data);
	// Create columns
	TableColumn column1 = new TableColumn(leftTable, SWT.NONE);
	column1.setText("Name");
	column1.setWidth(150);
	for (int i = 0; i < columnCount; i++) {
		TableColumn column = new TableColumn(rightTable, SWT.NONE);
		column.setText("Value "+i);
		column.setWidth(200);
	}
	// Create rows
	for (int i = 0; i < rowCount; i++) {
		TableItem item = new TableItem(leftTable, SWT.NONE);
		item.setText("item "+i);
		item = new TableItem(rightTable, SWT.NONE);
		for (int j = 0; j < columnCount; j++) {
			item.setText(j, "Item "+i+" value @ "+j);
		}
	}
	// Make selection the same in both tables
	leftTable.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			rightTable.setSelection(leftTable.getSelectionIndices());
		}
	});
	rightTable.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			leftTable.setSelection(rightTable.getSelectionIndices());
		}
	});
	// On Windows, the selection is gray if the table does not have focus.
	// To make both tables appear in focus, draw the selection background here.
	// This part only works on version 3.2 or later.
	Listener eraseListener = new Listener() {
		public void handleEvent(Event event) {
			event.detail &= ~SWT.HOT;
			if((event.detail & SWT.SELECTED) != 0) {
				GC gc = event.gc;
				Rectangle rect = event.getBounds();
				gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
				gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
				gc.fillRectangle(rect);
				event.detail &= ~SWT.SELECTED;					
			}
		}
	};
	
	leftTable.addListener(SWT.EraseItem, eraseListener);
	rightTable.addListener(SWT.EraseItem, eraseListener);
	// Make vertical scrollbars scroll together
	ScrollBar vBarLeft = leftTable.getVerticalBar();
	vBarLeft.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			rightTable.setTopIndex(leftTable.getTopIndex());
		}
	});
	ScrollBar vBarRight = rightTable.getVerticalBar();
	vBarRight.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			leftTable.setTopIndex(rightTable.getTopIndex());
		}
	});
	// Horizontal bar on second table takes up a little extra space.
	// To keep vertical scroll bars in sink, force table1 to end above
	// horizontal scrollbar
	ScrollBar hBarRight = rightTable.getHorizontalBar();
	Label spacer = new Label(parent, SWT.NONE);
	GridData spacerData = new GridData();
	spacerData.heightHint = hBarRight.getSize().y;
	spacer.setVisible(false);
	parent.setBackground(leftTable.getBackground());
	
	shell.setSize(600, 400);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

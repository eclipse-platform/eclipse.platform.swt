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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Bug191137_ActiveSelectionLost {

	public static void main(String[] args) {
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    Table table = new Table(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	    
		TableItem item1 = new TableItem(table, SWT.NONE);
		item1.setText("First select this Item (click me first)");
		TableItem item2 = new TableItem(table, SWT.NONE);
		item2.setText("In Windows the above is blue but in Linux it is gray after step 3");
		
		final Table dTable = new Table(table, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		TableItem dItem = new TableItem(dTable, SWT.NONE);
		dTable.setBounds(10,60,375,175);
		dItem.setText("Second select this item (notice first selection is gray)");
		Button button = new Button(dTable, SWT.NONE);
		button.setText("Third click me to dispose this widget.table.");
	    button.setBounds(10,40,350,60);
	    button.addMouseListener( new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
			@Override
			public void mouseDown(MouseEvent arg0) {
		        dTable.dispose();
			}
			@Override
			public void mouseUp(MouseEvent arg0) {}
	    });
		
	    table.setBounds(0,0,400,200);

	    shell.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch())
	                display.sleep();
	    }
	    display.dispose();
	}
}

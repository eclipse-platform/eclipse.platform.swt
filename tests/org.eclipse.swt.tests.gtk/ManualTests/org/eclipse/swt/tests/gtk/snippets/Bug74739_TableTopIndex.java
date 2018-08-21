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
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/*
 * Created on Sep 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author jcalder
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Bug74739_TableTopIndex {

	  public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		
		Font font = new Font(display, "Courier", 10, SWT.NORMAL);
	
		// create a a widget.table with 3 columns and fill with data
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		
		table.setFont(font);
		
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		for (int i = 0; i < 500; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { "cell "+i+" 0", "cell "+i+" 1", "cell "+i+" 2"});
		}
		column1.pack();
		column2.pack();
		column3.pack();
	
		// create a TableCursor to navigate around the widget.table
		final TableCursor cursor = new TableCursor(table, SWT.NONE);
		// create an editor to edit the cell when the user hits "ENTER" 
		// while over a cell in the widget.table
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
	
		cursor.addSelectionListener(new SelectionAdapter() {
			// when the TableEditor is over a cell, select the corresponding row in 
			// the widget.table
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSelection(new TableItem[] {cursor.getRow()});
			}
			// when the user hits "ENTER" in the TableCursor, pop up a widget.text editor so that 
			// they can change the widget.text of the cell
			@Override
			public void widgetDefaultSelected(SelectionEvent e){
				final Text text = new Text(cursor, SWT.NONE);
				TableItem row = cursor.getRow();
				int column = cursor.getColumn();
				text.setText(row.getText(column));
				text.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						// close the widget.text editor and copy the data over 
						// when the user hits "ENTER"
						if (e.character == SWT.CR) {
							TableItem row = cursor.getRow();
							int column = cursor.getColumn();
							row.setText(column, text.getText());
							text.dispose();
						}
						// close the widget.text editor when the user hits "ESC"
						if (e.character == SWT.ESC) {
							text.dispose();
						}
					}
				});
				editor.setEditor(text);
				text.setFocus();
			}
		});
		// Hide the TableCursor when the user hits the "MOD1" or "MOD2" key.
		// This alows the user to select multiple items in the widget.table.
		cursor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.MOD1 || 
				    e.keyCode == SWT.MOD2 || 
				    (e.stateMask & SWT.MOD1) != 0 || 
				    (e.stateMask & SWT.MOD2) != 0) {
					cursor.setVisible(false);
				}
			}
		});
		// Show the TableCursor when the user releases the "MOD2" or "MOD1" key.
		// This signals the end of the multiple selection task.
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.MOD1 && (e.stateMask & SWT.MOD2) != 0) return;
				if (e.keyCode == SWT.MOD2 && (e.stateMask & SWT.MOD1) != 0) return;
				if (e.keyCode != SWT.MOD1 && (e.stateMask & SWT.MOD1) != 0) return;
				if (e.keyCode != SWT.MOD2 && (e.stateMask & SWT.MOD2) != 0) return;
			
				TableItem[] selection = table.getSelection();
				TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
				table.showItem(row);
				cursor.setSelection(row, 0);
				cursor.setVisible(true);
				cursor.setFocus();
			}
		});
		
		
		ScrollBar scrollBar = table.getVerticalBar();
		scrollBar.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				int tableIndex = table.getTopIndex();
				System.out.println("widget.table index is: " + tableIndex);
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}});
	
		table.setTopIndex(20);
		System.out.println("getTopIndex: " + table.getTopIndex());
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		font.dispose();
		display.dispose();
	}
 


	
}

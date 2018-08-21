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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class Bug202395_TableGetTopIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.open ();
		
		Table table = new Table(shell,SWT.BORDER|SWT.FULL_SELECTION);
		
		new TableColumn(table, SWT.RIGHT);
		
		TableItem tmp;
		TableItem selection = null;
		
		for( int i = 0; i < 100; i++ ) {
			tmp = new TableItem(table,SWT.NONE);
			tmp.setText(i + "");
			if( i == 90 ) {
				selection = tmp;
			}
		}
		
		table.setSelection(new TableItem[] { selection });
		shell.layout(true);
		
//		while( widget.table.getDisplay().readAndDispatch () ) {
//			
//		}
		
		System.err.println("TOP-INDEX: " + table.getTopIndex());
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}

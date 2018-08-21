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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug216245_ScrollbarInconsistency {

	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new FormLayout());

		Button button = new Button(shell, SWT.BORDER);
		button.setText("Press Me");
		FormData data = new FormData();
		data.top = new FormAttachment(0);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(20);
		button.setLayoutData(data);

		final Canvas canvas = new Canvas(shell, SWT.BORDER);
		data = new FormData();
		data.top = new FormAttachment(button);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		canvas.setLayoutData(data);

		canvas.setLayout(new FillLayout());
		final Table table = new Table(canvas, SWT.NONE);
		table.setHeaderVisible(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column1");
		column1.setWidth(100);

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column2");
		column2.setWidth(200);

		for (int i = 0; i < 1000; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("item" + i);
		}

		button.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				ScrollBar verticalScrollBar = table.getVerticalBar();
				if(verticalScrollBar != null && !verticalScrollBar.isDisposed())
				{
					if(verticalScrollBar.isEnabled()) {
//						widget.table.setEnabled(false);
						verticalScrollBar.setEnabled(false);
					} else {
//						widget.table.setEnabled(true);
						verticalScrollBar.setEnabled(true);
					}
				}
			}});
		shell.setSize(300, 400);
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}

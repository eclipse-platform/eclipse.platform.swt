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
 *     Thomas Singer - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug127132_VirtualTablePackNPE {

	// Constants ==============================================================

	static final int COUNT1 = 1000;
	static final int COUNT2 = 100;

	private static boolean packPending = true;

	// Static =================================================================

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		final Button button = new Button(shell, SWT.NONE);
		final Button packButton = new Button(shell, SWT.NONE);

		final Table table = new Table(shell, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		button.setText("Change content");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setItemCount(table.getItemCount() == COUNT1 ? COUNT2 : COUNT1);
			}
		});

		packButton.setText("Pack");
		packButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (TableColumn c : table.getColumns()) {
					c.pack();
				}
			}
		});

		table.setHeaderVisible(true);
		final Listener listener = event -> {
			final TableItem item = (TableItem)event.item;
			if (event.type == SWT.EraseItem) {
				if (event.index < 2) {
					event.detail &= ~SWT.FOREGROUND;
				}
			}
			else if (event.type == SWT.PaintItem) {
				if (event.index == 0) {
					event.gc.drawText((String)item.getData(), event.x, event.y, true);
				}
				else if (event.index == 1) {
					final String text1 = (String)item.getData();
					final Point size = event.gc.textExtent(text1);
					final Rectangle bounds = item.getBounds(event.index);
					event.gc.drawText(text1, bounds.x + bounds.width - size.x, bounds.y + bounds.height - size.y, true);
				}
			}
			else if (event.type == SWT.MeasureItem) {
				final String text2 = (String)item.getData();
				final Point point = event.gc.textExtent(text2);
				event.height = point.y * 2;
				event.width = Math.max(event.width, point.x);
			}
			else if (event.type == SWT.SetData) {
				final int index = table.indexOf(item);
				final String data = "Item " + index;
				item.setText(2, data);
				item.setData(data);

				if (packPending) {
					packPending = false;
					display.asyncExec(() -> {
						table.setRedraw(false);
						for (TableColumn column : table.getColumns()) {
							column.pack();
						}
						table.setRedraw(true);
					});
				}
			}
		};
		table.addListener(SWT.EraseItem, listener);
		table.addListener(SWT.SetData, listener);
		table.addListener(SWT.MeasureItem, listener);
		table.addListener(SWT.PaintItem, listener);

		createColumn(SWT.LEFT, "OD Left", table);
		createColumn(SWT.RIGHT, "OD Right", table);
		createColumn(SWT.LEFT, "Primitive", table);

		table.setItemCount(COUNT1);
		table.setTopIndex(50000);

		shell.setSize(400, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createColumn(int style, String text, Table table) {
		final TableColumn tableColumn = new TableColumn(table, style);
		tableColumn.setText(text);
		tableColumn.setMoveable(true);
		tableColumn.setWidth(20);
	}
}
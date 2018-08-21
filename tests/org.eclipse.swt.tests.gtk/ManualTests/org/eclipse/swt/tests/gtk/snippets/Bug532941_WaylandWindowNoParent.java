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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Bug532941_WaylandWindowNoParent {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.NO_FOCUS | SWT.ON_TOP);
		shell.setLayout(new FillLayout());

		final Table table = new Table(shell, SWT.VIRTUAL | SWT.V_SCROLL);

		final TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Column");

		final Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.MeasureItem) {
					final Point size = event.gc.stringExtent(getText(event));
					event.width = size.x;
					event.height = size.y;
				} else if (event.type == SWT.PaintItem) {
					event.gc.drawString(getText(event), event.x, event.y);
				} else if (event.type == SWT.SetData) {
					event.item.setData("Row " + event.index);
				} else if (event.type == SWT.MouseDown) {
					if (event.button == 3) {
						shell.dispose();
					}
				}
			}

			private String getText(Event event) {
				return String.valueOf(event.item.getData());
			}
		};
		table.addListener(SWT.SetData, listener);
		table.addListener(SWT.MeasureItem, listener);
		table.addListener(SWT.PaintItem, listener);
		table.addListener(SWT.MouseDown, listener);
		table.setItemCount(20);
		table.clearAll();

		shell.layout(true, false);

		tableColumn.pack();
		final int width = tableColumn.getWidth();
		System.out.println("column width = " + width);

		final int max = Math.min(10, table.getItemCount());
		final Rectangle lastRowBounds = table.getItem(max - 1).getBounds();
		final Rectangle rectangle = table.computeTrim(0, 0, width, lastRowBounds.y + lastRowBounds.height);

		final Rectangle shellBounds = shell.computeTrim(0, 0, rectangle.width, rectangle.height);
		shell.setSize(shellBounds.width, shellBounds.height);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
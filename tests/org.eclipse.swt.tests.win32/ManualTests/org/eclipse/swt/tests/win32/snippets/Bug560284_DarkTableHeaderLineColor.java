/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class Bug560284_DarkTableHeaderLineColor {
	static class EmptyListener implements Listener {
		@Override
		public void handleEvent(Event event) {
		}
	}

	static EmptyListener emptyListener = new EmptyListener();

	static void setColors(Control control, Color backColor, Color foreColor) {
		control.setBackground(backColor);
		control.setForeground(foreColor);

		if (control instanceof Table) {
			Table table = (Table)control;
			table.setHeaderBackground(backColor);
			table.setHeaderForeground(foreColor);
		}

		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				setColors(child, backColor, foreColor);
			}
		}
	}

	public static void setTableHeaderLineColor(Table table, Color color) {
		table.getDisplay().setData("org.eclipse.swt.internal.win32.Table.headerLineColor", color);
		table.redraw();

		// Hack to trigger header redraw
		table.setSize(table.getSize());
	}

	public static void main (String [] args) {
		Display display = new Display ();

		Shell shell = new Shell (display);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		shell.setLayout(layout);

		final Text labelInfo = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		labelInfo.setText(
				"Notice that table header delimiter lines are too bright"
		);

		final Table table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column");
		column1.setWidth(100);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column");
		column2.setWidth(100);
		TableItem item1 = new TableItem(table, SWT.NONE);
		item1.setText("Item");

		Composite options = new Composite(shell, SWT.NONE);
		options.setLayout(new RowLayout(SWT.HORIZONTAL));
		{
			Group grpTableOwnerDraw = new Group(options, SWT.NONE);
			grpTableOwnerDraw.setText("Owner draw");
			grpTableOwnerDraw.setLayout(new RowLayout(SWT.VERTICAL));
			{
				Button btnNo = new Button(grpTableOwnerDraw, SWT.RADIO);
				btnNo.setText("No");
				btnNo.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					table.removeListener(SWT.EraseItem, emptyListener);
					table.removeListener(SWT.MeasureItem, emptyListener);
					table.removeListener(SWT.PaintItem, emptyListener);

					// owner draw is only disabled after last item was removed
					table.removeAll();
					new TableItem(table, SWT.NONE).setText("Item");
				});

				Button btnYes = new Button(grpTableOwnerDraw, SWT.RADIO);
				btnYes.setText("Yes");
				btnYes.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					table.addListener(SWT.EraseItem, emptyListener);
					table.addListener(SWT.MeasureItem, emptyListener);
					table.addListener(SWT.PaintItem, emptyListener);
				});

				btnNo.setSelection(true);
			}

			Group grpTableLinesVisible = new Group(options, SWT.NONE);
			grpTableLinesVisible.setText("Table lines");
			grpTableLinesVisible.setLayout(new RowLayout(SWT.VERTICAL));
			{
				Button btnNo = new Button(grpTableLinesVisible, SWT.RADIO);
				btnNo.setText("No");
				btnNo.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					table.setLinesVisible(false);
				});

				Button btnYes = new Button(grpTableLinesVisible, SWT.RADIO);
				btnYes.setText("Yes");
				btnYes.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					table.setLinesVisible(true);
				});

				btnNo.setSelection(true);
			}

			Group grpTheme = new Group(options, SWT.NONE);
			grpTheme.setText("Theme");
			grpTheme.setLayout(new RowLayout(SWT.VERTICAL));
			{
				Button btnDefault = new Button(grpTheme, SWT.RADIO);
				btnDefault.setText("Default");
				btnDefault.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					setColors(shell, null, null);
				});

				Button btnLight = new Button(grpTheme, SWT.RADIO);
				btnLight.setText("Light");
				btnLight.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					Color backColor = new Color(display, 0xFF, 0xFF, 0xFF);
					Color foreColor = new Color(display, 0x00, 0x00, 0x00);
					setColors(shell, backColor, foreColor);
				});

				Button btnDark = new Button(grpTheme, SWT.RADIO);
				btnDark.setText("Dark");
				btnDark.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;

					Color backColor = new Color(display, 0x30, 0x30, 0x30);
					Color foreColor = new Color(display, 0xD0, 0xD0, 0xD0);
					setColors(shell, backColor, foreColor);

					// Somehow fixes table's border. Another bug?
					table.redraw();
				});

				btnDefault.setSelection(true);
			}

			Group grpTableHeaderLineColor = new Group(options, SWT.NONE);
			grpTableHeaderLineColor.setText("Header lines");
			grpTableHeaderLineColor.setLayout(new RowLayout(SWT.VERTICAL));
			{
				Button btnDefault = new Button(grpTableHeaderLineColor, SWT.RADIO);
				btnDefault.setText("Default");
				btnDefault.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;
					setTableHeaderLineColor(table, null);
				});

				Button btnLight = new Button(grpTableHeaderLineColor, SWT.RADIO);
				btnLight.setText("For light");
				btnLight.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;
					setTableHeaderLineColor(table, new Color(display, 0xE5, 0xE5, 0xE5));
				});

				Button btnDark = new Button(grpTableHeaderLineColor, SWT.RADIO);
				btnDark.setText("For dark");
				btnDark.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;
					setTableHeaderLineColor(table, new Color(display, 0x50, 0x50, 0x50));
				});

				Button btnRed = new Button(grpTableHeaderLineColor, SWT.RADIO);
				btnRed.setText("Red");
				btnRed.addListener(SWT.Selection, event -> {
					if (!((Button)event.widget).getSelection()) return;
					setTableHeaderLineColor(table, new Color(display, 0xFF, 0x00, 0x00));
				});

				btnDefault.setSelection(true);
			}
		}

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

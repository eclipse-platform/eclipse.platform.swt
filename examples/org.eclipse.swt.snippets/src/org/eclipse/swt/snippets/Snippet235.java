/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
/*
 * example snippet: detect a system settings change
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet235 {


public static void main(String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("The SWT.Settings Event");
	shell.setLayout(new GridLayout());
	Label label = new Label(shell, SWT.WRAP);
	label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	label.setText("Change a system setting and the table below will be updated.");
	final Table table = new Table(shell, SWT.BORDER);
	table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	TableColumn column = new TableColumn(table, SWT.NONE);
	column = new TableColumn(table, SWT.NONE);
	column.setWidth(150);
	column = new TableColumn(table, SWT.NONE);
	for (int i = 0; i < colorIds.length; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		Color color = display.getSystemColor(colorIds[i]);
		item.setText(0, colorNames[i]);
		item.setBackground(1, color);
		item.setText(2, color.toString());
	}
	TableColumn[] columns = table.getColumns();
	columns[0].pack();
	columns[2].pack();
	display.addListener(SWT.Settings, event -> {
		for (int i = 0; i < colorIds.length; i++) {
			Color color = display.getSystemColor(colorIds[i]);
			TableItem item = table.getItem(i);
			item.setBackground(1, color);
		}
		TableColumn[] columns1 = table.getColumns();
		columns1[0].pack();
		columns1[2].pack();
	});

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
static int[] colorIds = new int[] {SWT.COLOR_INFO_BACKGROUND,
		SWT.COLOR_INFO_FOREGROUND,
		SWT.COLOR_LINK_FOREGROUND,
		SWT.COLOR_LIST_BACKGROUND,
		SWT.COLOR_LIST_FOREGROUND,
		SWT.COLOR_LIST_SELECTION,
		SWT.COLOR_LIST_SELECTION_TEXT,
		SWT.COLOR_TITLE_BACKGROUND,
		SWT.COLOR_TITLE_BACKGROUND_GRADIENT,
		SWT.COLOR_TITLE_FOREGROUND,
		SWT.COLOR_TITLE_INACTIVE_BACKGROUND,
		SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT,
		SWT.COLOR_TITLE_INACTIVE_FOREGROUND,
		SWT.COLOR_WIDGET_BACKGROUND,
		SWT.COLOR_WIDGET_BORDER,
		SWT.COLOR_WIDGET_DARK_SHADOW,
		SWT.COLOR_WIDGET_FOREGROUND,
		SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW,
		SWT.COLOR_WIDGET_LIGHT_SHADOW,
		SWT.COLOR_WIDGET_NORMAL_SHADOW,};
static String [] colorNames = new String[] {"SWT.COLOR_INFO_BACKGROUND",
		"SWT.COLOR_INFO_FOREGROUND",
		"SWT.COLOR_LINK_FOREGROUND",
		"SWT.COLOR_LIST_BACKGROUND",
		"SWT.COLOR_LIST_FOREGROUND",
		"SWT.COLOR_LIST_SELECTION",
		"SWT.COLOR_LIST_SELECTION_TEXT",
		"SWT.COLOR_TITLE_BACKGROUND",
		"SWT.COLOR_TITLE_BACKGROUND_GRADIENT",
		"SWT.COLOR_TITLE_FOREGROUND",
		"SWT.COLOR_TITLE_INACTIVE_BACKGROUND",
		"SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT",
		"SWT.COLOR_TITLE_INACTIVE_FOREGROUND",
		"SWT.COLOR_WIDGET_BACKGROUND",
		"SWT.COLOR_WIDGET_BORDER",
		"SWT.COLOR_WIDGET_DARK_SHADOW",
		"SWT.COLOR_WIDGET_FOREGROUND",
		"SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW",
		"SWT.COLOR_WIDGET_LIGHT_SHADOW",
		"SWT.COLOR_WIDGET_NORMAL_SHADOW",};
}

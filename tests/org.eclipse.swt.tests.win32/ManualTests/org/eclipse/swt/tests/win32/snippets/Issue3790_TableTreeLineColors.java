/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Standalone manual visual test; this class has no automated assertions.
 * Compare native and custom header/grid colors. Resize and reorder columns,
 * scroll both controls, and toggle colors and visibility after selecting a row.
 * Clearing custom colors must restore the native rendering.
 * Automated regression coverage is in org.eclipse.swt.widgets.LineColorsWin32Tests.
 */
public class Issue3790_TableTreeLineColors {

	public static void main(String[] args) {
		Display display = new Display();
		display.setDarkThemePreferred(true);
		display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", true);
		Shell shell = new Shell(display);
		shell.setText("Table and Tree line colors - #3790");
		shell.setLayout(new GridLayout(2, true));
		Color background = new Color(47, 47, 47);
		Color foreground = new Color(232, 232, 232);
		Color headerBackground = new Color(56, 61, 63);
		Color headerLines = new Color(96, 102, 106);
		Color gridLines = new Color(65, 70, 73);
		shell.setBackground(background);
		shell.setForeground(foreground);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		for (String name : new String[] {"Tree", "Table"}) {
			Label label = new Label(shell, SWT.NONE);
			label.setText(name);
			label.setForeground(foreground);
		}
		Tree tree = new Tree(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tree.setHeaderVisible(true);
		table.setHeaderVisible(true);
		tree.setHeaderBackground(headerBackground);
		table.setHeaderBackground(headerBackground);
		tree.setHeaderForeground(foreground);
		table.setHeaderForeground(foreground);
		for (Control control : new Control[] {tree, table}) {
			control.setBackground(background);
			control.setForeground(foreground);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.widthHint = 360;
			data.heightHint = 260;
			control.setLayoutData(data);
		}
		for (String name : new String[] {"Name", "Value", "Status"}) {
			TreeColumn tc = new TreeColumn(tree, SWT.NONE);
			tc.setText(name);
			tc.setWidth(160);
			tc.setMoveable(true);
			TableColumn lc = new TableColumn(table, SWT.NONE);
			lc.setText(name);
			lc.setWidth(160);
			lc.setMoveable(true);
		}
		TreeItem root = new TreeItem(tree, SWT.NONE);
		root.setText(new String[] {"Report", "Quarterly", "Ready"});
		new TableItem(table, SWT.NONE).setText(new String[] {"Report", "Quarterly", "Ready"});
		for (int i = 1; i <= 40; i++) {
			String[] row = {"Row " + i, "Value " + i, "Ready"};
			new TreeItem(root, SWT.NONE).setText(row);
			new TableItem(table, SWT.NONE).setText(row);
		}
		root.setExpanded(true);
		Button colors = new Button(shell, SWT.CHECK);
		colors.setForeground(foreground);
		colors.setText("Custom header and grid colors");
		colors.setSelection(true);
		Runnable applyColors = () -> {
			for (Control control : new Control[] {tree, table}) {
				String prefix = "org.eclipse.swt.internal.win32." + (control instanceof Tree ? "Tree." : "Table.");
				control.setData(prefix + "headerLineColor", colors.getSelection() ? headerLines : null);
				control.setData(prefix + "gridLineColor", colors.getSelection() ? gridLines : null);
			}
		};
		colors.addListener(SWT.Selection, event -> applyColors.run());
		applyColors.run();
		Button lines = new Button(shell, SWT.CHECK);
		lines.setForeground(foreground);
		lines.setText("Show grid lines");
		lines.setSelection(true);
		lines.addListener(SWT.Selection, event -> {
			tree.setLinesVisible(lines.getSelection());
			table.setLinesVisible(lines.getSelection());
		});
		tree.setLinesVisible(true);
		table.setLinesVisible(true);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}

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
package org.eclipse.swt.tests.cocoa.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class Bug569529_macOS_LinkAndTextHogCPU {
	public static void main(String[] args) {
		System.setProperty("org.eclipse.swt.display.useSystemTheme", "true");

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, 0);
		hint.setText(
			"1) Use macOS with Dark Theme\n" +
			"2) Notice that snippet constantly hogs CPU\n" +
			"3) On macOS 10.15, hiding controls helps but not on BigSur\n" +
			"4) Destroying controls helps"
		);

		final Composite compColumns = new Composite(shell, 0);
		compColumns.setLayout(new GridLayout(2, true));

		{
			final Composite parent = new Composite(compColumns, 0);
			parent.setLayout(new GridLayout(1, true));

			final Composite itemsParent = new Composite(parent, 0);
			itemsParent.setLayout(new GridLayout(1, true));

			final ArrayList<Link> items = new ArrayList<>();
			final Runnable createDestroy = () -> {
				if (items.size() != 0) {
					for (Link item : items)
					{
						item.dispose();
					}
					items.clear();
				} else {
					for (int i = 0; i < 10; i++)
					{
						Link item = new Link(itemsParent, 0);
						items.add(item);
						item.setText("This is <a>Link#" + i + "</a>");
					}

					itemsParent.layout(true, true);
				}
			};

			Button btnCreateDestroy = new Button(parent, SWT.PUSH);
			btnCreateDestroy.setText("Create/Destroy");
			btnCreateDestroy.addListener(SWT.Selection, event -> {
				createDestroy.run();
			});

			Button btnShowHide = new Button(parent, SWT.PUSH);
			btnShowHide.setText("Show/hide");
			btnShowHide.addListener(SWT.Selection, event -> {
				for (Link item : items)
				{
					item.setVisible(!item.getVisible());
				}
			});

			Button btnEnableDisable = new Button(parent, SWT.PUSH);
			btnEnableDisable.setText("Enable/disable");
			btnEnableDisable.addListener(SWT.Selection, event -> {
				for (Link item : items)
				{
					item.setEnabled(!item.getEnabled());
				}
			});

			Button btnSetRemoveColors = new Button(parent, SWT.PUSH);
			btnSetRemoveColors.setText("Set/remove colors");
			final boolean[] hasColors = new boolean[]{false};
			btnSetRemoveColors.addListener(SWT.Selection, event -> {
				hasColors[0] = !hasColors[0];
				for (Link item : items)
				{
					if (!hasColors[0]) {
						item.setForeground(null);
						item.setBackground(null);
					} else {
						item.setForeground(display.getSystemColor(SWT.COLOR_RED));
						item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					}
				}
			});

			createDestroy.run();
		}

		{
			final Composite parent = new Composite(compColumns, 0);
			parent.setLayout(new GridLayout(1, true));

			final Composite itemsParent = new Composite(parent, 0);
			itemsParent.setLayout(new GridLayout(1, true));

			final ArrayList<Text> items = new ArrayList<>();
			final Runnable createDestroy = () -> {
				if (items.size() != 0) {
					for (Text item : items)
					{
						item.dispose();
					}
					items.clear();
				} else {
					for (int i = 0; i < 10; i++)
					{
						Text item = new Text(itemsParent, SWT.MULTI);
						items.add(item);
						item.setText("This is Text#" + i);
					}

					itemsParent.layout(true, true);
				}
			};

			Button btnCreateDestroy = new Button(parent, SWT.PUSH);
			btnCreateDestroy.setText("Create/Destroy");
			btnCreateDestroy.addListener(SWT.Selection, event -> {
				createDestroy.run();
			});

			Button btnShowHide = new Button(parent, SWT.PUSH);
			btnShowHide.setText("Show/hide");
			btnShowHide.addListener(SWT.Selection, event -> {
				for (Text item : items)
				{
					item.setVisible(!item.getVisible());
				}
			});

			Button btnEnableDisable = new Button(parent, SWT.PUSH);
			btnEnableDisable.setText("Enable/disable");
			btnEnableDisable.addListener(SWT.Selection, event -> {
				for (Text item : items)
				{
					item.setEnabled(!item.getEnabled());
				}
			});

			Button btnSetRemoveColors = new Button(parent, SWT.PUSH);
			btnSetRemoveColors.setText("Set/remove colors");
			final boolean[] hasColors = new boolean[]{false};
			btnSetRemoveColors.addListener(SWT.Selection, event -> {
				hasColors[0] = !hasColors[0];
				for (Text item : items)
				{
					if (!hasColors[0]) {
						item.setForeground(null);
						item.setBackground(null);
					} else {
						item.setForeground(display.getSystemColor(SWT.COLOR_RED));
						item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					}
				}
			});

			createDestroy.run();
		}

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
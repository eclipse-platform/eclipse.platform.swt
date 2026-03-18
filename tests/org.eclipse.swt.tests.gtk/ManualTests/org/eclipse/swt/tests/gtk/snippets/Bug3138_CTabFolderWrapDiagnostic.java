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
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Diagnostic snippet for CTabFolder topRight WRAP overflow on GTK.
 *
 * Reproduces the issue where setTopRight(control, SWT.RIGHT | SWT.WRAP)
 * wraps to a second row even when there is sufficient horizontal space.
 * This particularly affects composites containing a Text widget (search field).
 *
 * Run this and observe:
 * 1. Whether the toolbar+text renders inline or wrapped below the tabs
 * 2. The computed sizes printed to stdout
 * 3. Compare behavior with SWT.RIGHT only (no WRAP) vs SWT.RIGHT | SWT.WRAP
 *
 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3138
 */
public class Bug3138_CTabFolderWrapDiagnostic {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800, 400);
		shell.setText("Bug 3138 - CTabFolder WRAP Diagnostic");
		shell.setLayout(new GridLayout(1, false));

		// --- Case 1: SWT.RIGHT | SWT.WRAP (the problematic case) ---
		createTestCase(shell, "SWT.RIGHT | SWT.WRAP", SWT.RIGHT | SWT.WRAP);

		// --- Case 2: SWT.RIGHT only (works but never wraps) ---
		createTestCase(shell, "SWT.RIGHT only", SWT.RIGHT);

		shell.open();

		// Log sizes after layout
		shell.getDisplay().asyncExec(() -> {
			System.out.println("=== Shell size: " + shell.getSize() + " ===");
			System.out.println();
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createTestCase(Composite parent, String label, int alignment) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(label);
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		CTabFolder folder = new CTabFolder(group, SWT.BORDER);

		// Create tabs similar to Eclipse views
		for (int i = 1; i <= 3; i++) {
			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText("Tab " + i);
			Label content = new Label(folder, SWT.NONE);
			content.setText("Content for tab " + i);
			item.setControl(content);
		}
		folder.setSelection(0);

		// Create topRight composite mimicking Eclipse view toolbar with search field
		Composite topRight = new Composite(folder, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		topRight.setLayout(gl);

		// ToolBar with a few buttons
		ToolBar toolbar = new ToolBar(topRight, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem ti = new ToolItem(toolbar, SWT.PUSH);
			ti.setText("B" + i);
		}

		// Text widget (search field) - this is the key contributor to inflated size
		Text searchText = new Text(topRight, SWT.BORDER | SWT.SEARCH);
		searchText.setMessage("Filter...");
		searchText.setLayoutData(new GridData(100, SWT.DEFAULT));

		folder.setTopRight(topRight, alignment);

		// Diagnostic logging
		parent.getDisplay().asyncExec(() -> {
			if (topRight.isDisposed()) return;

			Point topRightSize = topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Point toolbarSize = toolbar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Point textSize = searchText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Point textSize100 = searchText.computeSize(100, SWT.DEFAULT);

			System.out.println("--- " + label + " ---");
			System.out.println("  topRight.computeSize(DEFAULT,DEFAULT) = " + topRightSize);
			System.out.println("  toolbar.computeSize(DEFAULT,DEFAULT)  = " + toolbarSize);
			System.out.println("  text.computeSize(DEFAULT,DEFAULT)     = " + textSize);
			System.out.println("  text.computeSize(100,DEFAULT)         = " + textSize100);
			System.out.println("  folder.getSize()                      = " + folder.getSize());

			// Compute what CTabFolder sees as available width
			int folderWidth = folder.getSize().x;
			int itemWidth = 0;
			for (CTabItem item : folder.getItems()) {
				itemWidth += item.getBounds().width;
			}
			System.out.println("  total tab item width                  = " + itemWidth);
			System.out.println("  remaining width (folder - items)      = " + (folderWidth - itemWidth));
			System.out.println("  topRight preferred vs remaining       = " + topRightSize.x
					+ " vs " + (folderWidth - itemWidth));
			System.out.println("  WOULD OVERFLOW?                       = " + (topRightSize.x >= (folderWidth - itemWidth)));
			System.out.println();
		});
	}
}

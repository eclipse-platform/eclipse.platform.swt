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


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Adding items in combo box grows at O(n^2).
 * how to run: Run on Gtk2/3 with/without fix to observe different run times.
 * Gtk2,  +-20 ms
 * Gtk3,  +- 1000ms
 * Gtk3 with fix: += 10ms.
 *
 * Once snippet launched, try 'refill' button, which tests time it takes to remove entries.
 *
 */
public class Bug489640_ComboPerformanceTest {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		test_setItems(shell);
		test_addItems(shell);

		shell.setSize(400, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void test_addItems(final Shell shell) {
		final Combo combo = new Combo(shell, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final long start = System.currentTimeMillis();
		int count = 200;
		for (int i = 0; i < count; i++) {
			combo.add("item " + i);     // <<< via add.
		}
		System.out.println("Combo.add(..)     took " + (System.currentTimeMillis() - start) + " ms.   (1000ms = bad.  20ms = good.)");
		combo.select(0);
	}

	private static void test_setItems(final Shell shell) {
		final Combo combo = new Combo(shell, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		int count = 200;
		final String[] items = new String[count];
		for (int i = 0; i < count; i++) {
			items[i] = new String("item " + i);
		}
		final long start = System.currentTimeMillis();
		combo.setItems(items);
		System.out.println("Combo.setItems(.) took " + (System.currentTimeMillis() - start) + " ms.   (1000ms = bad.  20ms = good.)");
		combo.select(0);

		final Button button = new Button(shell, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		button.setText("Refill Combo at runtime");
		button.addListener(SWT.Selection, event -> {
			final Map<String, Charset> charSets = Charset.availableCharsets();
			final List<String> encodings = new ArrayList<>(charSets.keySet());
			Collections.sort(encodings);
			final String[] items1 = encodings.toArray(new String[encodings.size()]);
			System.out.println("Setting widget.combo with " + items1.length + " items");
			final long start1 = System.currentTimeMillis();
			combo.setItems(items1);
			final long end = System.currentTimeMillis();
			System.out.println("took " + (end - start1) + " ms.");
			combo.select(0);
		});
	}
}

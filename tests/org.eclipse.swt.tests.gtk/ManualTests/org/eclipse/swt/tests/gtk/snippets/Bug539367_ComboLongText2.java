/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug539367_ComboLongText2 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(250, 150);
		shell.setText("Bug combo text");
		shell.setLayout(new FillLayout());

		Composite parent = new Composite(shell, SWT.BORDER);
		parent.setLayout(new GridLayout());

		Combo combo = new Combo(shell, SWT.READ_ONLY | SWT.DROP_DOWN);
		GridData comboGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		combo.setLayoutData(comboGridData);
		combo.add("some long long long long combo text");
		combo.select(0);

		Composite lastComposite = new Composite(shell, SWT.BORDER);
		lastComposite.setLayout(new FillLayout());
		Text text = new Text(lastComposite, SWT.NONE);
		text.setText("my text");

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
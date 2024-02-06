package org.eclipse.swt.snippets;

/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/

import org.eclipse.swt.*;
/**
 * Snippet to test custom drawing behavior for Button, Tree and Table in a multi-zoom setting.
 * Mainly motivated by custom drawing behavior in the win32 implementation
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet383 {
	public static void main(String[] args) {
		System.setProperty("swt.autoScale", "quarter");
		System.setProperty("swt.autoScale.updateOnRuntime", "true");
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());
		shell.setText("Snippet 383");

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button button = new Button(composite, SWT.CHECK);
		button.setForeground(display.getSystemColor(SWT.COLOR_CYAN));
		button.setText("Checkbox");

		Table table = new Table(composite, SWT.CHECK);
		for (int i = 0; i < 4; i++) {
			TableItem item = new TableItem (table, SWT.CHECK);
			item.setChecked(true);
			item.setText("Node_" + (i + 1));
		}

		Tree tree  = new Tree(composite, SWT.CHECK);
		for (int i = 0; i < 4; i++) {
			TreeItem item = new TreeItem (tree, SWT.NONE);
			item.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			item.setText("Node_" + (i + 1));
		}

		shell.setLocation(200, 200);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

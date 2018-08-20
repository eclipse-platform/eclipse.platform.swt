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
 * Combo example snippet: add a new number item to a combo box
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet289  {
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Combo combo = new Combo(shell, SWT.NONE);
	combo.setItems("1111", "2222", "3333", "4444");
	combo.setText(combo.getItem(0));
	combo.addVerifyListener(e -> {
		String text = combo.getText();
		String newText = text.substring(0, e.start) + e.text + text.substring(e.end);
		try {
			if (newText.length() != 0) Integer.parseInt(newText);
		} catch (NumberFormatException ex) {
			e.doit = false;
		}
	});
	combo.addTraverseListener(e -> {
		if (e.detail == SWT.TRAVERSE_RETURN) {
			e.doit = false;
			e.detail = SWT.TRAVERSE_NONE;
			String newText = combo.getText();
			try {
				Integer.parseInt(newText);
				combo.add(newText);
				combo.setSelection(new Point(0, newText.length()));
			} catch (NumberFormatException ex) {
			}
		}
	});

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}

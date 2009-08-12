/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Combo example snippet: add a new number item to a combo
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet289  {
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Combo combo = new Combo(shell, SWT.NONE);
	combo.setItems(new String [] {"1111", "2222", "3333", "4444"});
	combo.setText(combo.getItem(0));
	combo.addVerifyListener(new VerifyListener() {
		public void verifyText(VerifyEvent e) {
			String text = combo.getText();
			String newText = text.substring(0, e.start) + e.text + text.substring(e.end);
			try {
				if (newText.length() != 0) Integer.parseInt(newText);
			} catch (NumberFormatException ex) {
				e.doit = false;
			}
		}
	});
	combo.addTraverseListener(new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
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

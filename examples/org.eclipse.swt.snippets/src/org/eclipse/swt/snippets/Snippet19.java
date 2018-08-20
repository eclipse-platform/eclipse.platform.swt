/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 530764
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Text example snippet: verify input (only allow digits)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet19 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Text text = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
		Rectangle clientArea = shell.getClientArea();
		text.setBounds(clientArea.x + 10, clientArea.y + 10, 200, 200);
		text.addVerifyListener(Snippet19::ensureTextContainsOnlyDigits);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void ensureTextContainsOnlyDigits(VerifyEvent e) {
		String string = e.text;
		e.doit = string.matches("\\d*");
		return;
	}
}

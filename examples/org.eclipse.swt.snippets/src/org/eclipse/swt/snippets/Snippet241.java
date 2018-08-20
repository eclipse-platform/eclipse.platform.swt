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
 * Text snippet: override Tab behavior to traverse out of a text control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet241 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,200,200);
	Text text1 = new Text(shell, SWT.MULTI | SWT.WRAP);
	Rectangle clientArea = shell.getClientArea();
	text1.setBounds(clientArea.x+10,clientArea.y+10,150,50);
	text1.setText("Tab will traverse out from here.");
	text1.addTraverseListener(e -> {
		if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			e.doit = true;
		}
	});
	Text text2 = new Text(shell, SWT.MULTI | SWT.WRAP);
	text2.setBounds(10,100,150,50);
	text2.setText("But Tab will NOT traverse out from here (Ctrl+Tab will).");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

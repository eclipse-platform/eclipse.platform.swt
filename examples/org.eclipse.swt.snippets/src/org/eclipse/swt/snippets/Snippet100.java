/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * Font example snippet: create a large font for use by a text widget
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet100 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10, 10, 200, 200);
	Text text = new Text(shell, SWT.MULTI);
	Rectangle clientArea = shell.getClientArea();
	text.setBounds(clientArea.x + 10, clientArea.y + 10, 150, 150);
	Font initialFont = text.getFont();
	FontData[] fontData = initialFont.getFontData();
	for (int i = 0; i < fontData.length; i++) {
		fontData[i].setHeight(24);
	}
	Font newFont = new Font(display, fontData);
	text.setFont(newFont);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	newFont.dispose();
	display.dispose();
}
}

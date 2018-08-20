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
 * TextLayout example snippet: text with underline and strike through
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet256 {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
	shell.setText("Underline, Strike Out");
	Font font = shell.getFont();
	String text = "Here is some text that is underlined or struck out or both.";
	final TextLayout layout = new TextLayout(display);
	layout.setText(text);
	TextStyle style1 = new TextStyle(font, null, null);
	style1.underline = true;
	layout.setStyle(style1, 26, 35);
	TextStyle style2 = new TextStyle(font, null, null);
	style2.strikeout = true;
	layout.setStyle(style2, 40, 49);
	TextStyle style3 = new TextStyle(font, null, null);
	style3.underline = true;
	style3.strikeout = true;
	layout.setStyle(style3, 54, 57);
	shell.addListener(SWT.Paint, event -> {
		Point point = new Point(10, 10);
		int width = shell.getClientArea().width - 2 * point.x;
		layout.setWidth(width);
		layout.draw(event.gc, point.x, point.y);
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	layout.dispose();
	display.dispose();
}
}

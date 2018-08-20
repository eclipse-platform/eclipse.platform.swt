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
 * Cursor snippet: Hide the Cursor over a control.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet242 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10, 10, 200, 200);
	Canvas canvas = new Canvas(shell, SWT.BORDER);
	canvas.setBounds(10,50,150,100);
	canvas.addPaintListener(e -> e.gc.drawString("hide Cursor here", 10, 10));

	// create a cursor with a transparent image
	Color white = display.getSystemColor (SWT.COLOR_WHITE);
	Color black = display.getSystemColor (SWT.COLOR_BLACK);
	PaletteData palette = new PaletteData (white.getRGB(), black.getRGB());
	ImageData sourceData = new ImageData (16, 16, 1, palette);
	sourceData.transparentPixel = 0;
	Cursor cursor = new Cursor(display, sourceData, 0, 0);

	shell.open();
	canvas.setCursor(cursor);
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	cursor.dispose();
	display.dispose();
}
}

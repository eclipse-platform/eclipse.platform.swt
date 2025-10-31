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
 * Caret example snippet: create a caret (using an image)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet43 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText("Snippet 43");
	Caret caret = new Caret (shell, SWT.NONE);
	Color white = display.getSystemColor (SWT.COLOR_WHITE);
	Color black = display.getSystemColor (SWT.COLOR_BLACK);
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground (black);
		gc.fillRectangle (0, 0, 20, 20);
		gc.setForeground (white);
		gc.drawLine (0, 0, 19, 19);
		gc.drawLine (19, 0, 0, 19);
	};
	final Image image = new Image (display, imageGcDrawer, 20, 20);
	caret.setLocation (10, 10);
	caret.setImage (image);
	caret.setVisible (true);
	shell.addListener(SWT.Paint, event -> {
		GC gc1 = event.gc;
		gc1.drawImage (image, 10, 64);
		gc1.drawString ("Test", 12, 12);
	});
	shell.open ();

	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}
}

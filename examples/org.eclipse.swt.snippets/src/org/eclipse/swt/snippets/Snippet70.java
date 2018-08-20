/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
 * GC example snippet: create an icon (in memory)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet70 {

public static void main (String [] args) {
	Display display = new Display ();
	Color red = display.getSystemColor (SWT.COLOR_RED);
	Color white = display.getSystemColor (SWT.COLOR_WHITE);
	Color black = display.getSystemColor (SWT.COLOR_BLACK);

	Image image = new Image (display, 20, 20);
	GC gc = new GC (image);
	gc.setBackground (red);
	gc.fillRectangle (5, 5, 10, 10);
	gc.dispose ();
	ImageData imageData = image.getImageData ();

	PaletteData palette = new PaletteData (new RGB (0, 0, 0),new RGB (0xFF, 0xFF, 0xFF));
	ImageData maskData = new ImageData (20, 20, 1, palette);
	Image mask = new Image (display, maskData);
	gc = new GC (mask);
	gc.setBackground (black);
	gc.fillRectangle (0, 0, 20, 20);
	gc.setBackground (white);
	gc.fillRectangle (5, 5, 10, 10);
	gc.dispose ();
	maskData = mask.getImageData ();

	Image icon = new Image (display, imageData, maskData);
	Shell shell = new Shell (display);
	Button button = new Button (shell, SWT.PUSH);
	button.setImage (icon);
	button.setSize (60, 60);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	icon.dispose ();
	image.dispose ();
	mask.dispose ();
	display.dispose ();
}
}

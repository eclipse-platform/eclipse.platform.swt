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
 * ToolBar example snippet: create tool bar (normal, hot and disabled images)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet47 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText("Snippet 47");

	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		Color color = display.getSystemColor(SWT.COLOR_BLUE);
		gc.setBackground(color);
		gc.fillRectangle(new Rectangle(0, 0, 20, 20));
	};
	Image image = new Image(display, imageGcDrawer, 20, 20);

	imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		Color color = display.getSystemColor(SWT.COLOR_GREEN);
		gc.setBackground(color);
		gc.fillRectangle(new Rectangle(0, 0, 20, 20));
	};
	Image disabledImage = new Image(display, imageGcDrawer, 20, 20);

	imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		Color color = display.getSystemColor(SWT.COLOR_RED);
		gc.setBackground(color);
		gc.fillRectangle(new Rectangle(0, 0, 20, 20));
	};
	Image hotImage = new Image(display, imageGcDrawer, 20, 20);

	ToolBar bar = new ToolBar (shell, SWT.BORDER | SWT.FLAT);
	Rectangle clientArea = shell.getClientArea ();
	bar.setBounds (clientArea.x, clientArea.y, 200, 32);
	for (int i=0; i<12; i++) {
		ToolItem item = new ToolItem (bar, 0);
		item.setImage (image);
		item.setDisabledImage (disabledImage);
		item.setHotImage (hotImage);
		if (i % 3 == 0) item.setEnabled (false);
	}

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	disabledImage.dispose ();
	hotImage.dispose ();
	display.dispose ();
}
}

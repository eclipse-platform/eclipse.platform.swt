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
 * Label example snippet: create a label (with an image)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet34 {

public static void main (String[] args) {
	Display display = new Display();
	Color color = display.getSystemColor (SWT.COLOR_RED);
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground (color);
		gc.fillRectangle (new Rectangle(0,0,imageWidth, imageHeight));
	};
	Image image = new Image (display, imageGcDrawer, 16, 16);
	Shell shell = new Shell (display);
	shell.setText("Snippet 34");
	Label label = new Label (shell, SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	label.setLocation (clientArea.x, clientArea.y);
	label.setImage (image);
	label.pack ();
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}

}

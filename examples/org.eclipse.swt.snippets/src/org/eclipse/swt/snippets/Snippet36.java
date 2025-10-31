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
 * ToolBar example snippet: create a flat tool bar (images)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet36 {

public static void main (String [] args) {
	Display display = new Display();
	Color color = display.getSystemColor (SWT.COLOR_RED);
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground(color);
		gc.fillRectangle(0, 0, imageWidth, imageHeight);

	};
	Image image = new Image(display, imageGcDrawer, 16, 16);
	Shell shell = new Shell (display);
	shell.setText("Snippet 36");
	ToolBar toolBar = new ToolBar (shell, SWT.FLAT | SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	toolBar.setLocation (clientArea.x, clientArea.y);
	for (int i=0; i<12; i++) {
		int style = i % 3 == 2 ? SWT.DROP_DOWN : SWT.PUSH;
		ToolItem item = new ToolItem (toolBar, style);
		item.setImage (image);
	}
	toolBar.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}

}
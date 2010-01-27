/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	Image image = new Image (display, 16, 16);
	Color color = display.getSystemColor (SWT.COLOR_RED);
	GC gc = new GC (image);
	gc.setBackground (color);
	gc.fillRectangle (image.getBounds ());
	gc.dispose ();
	Shell shell = new Shell (display);
	ToolBar toolBar = new ToolBar (shell, SWT.FLAT | SWT.BORDER);
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

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
 * Image example snippet: display an image in a group
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet112 {

public static void main (String [] args) {
	Display display = new Display ();
	final Image image = new Image (display, 20, 20);
	Color color = display.getSystemColor (SWT.COLOR_RED);
	GC gc = new GC (image);
	gc.setBackground (color);
	gc.fillRectangle (image.getBounds ());
	gc.dispose ();

	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	Group group = new Group (shell, SWT.NONE);
	group.setLayout (new FillLayout ());
	group.setText ("a square");
	Canvas canvas = new Canvas (group, SWT.NONE);
	canvas.addPaintListener (new PaintListener () {
		public void paintControl (PaintEvent e) {
			e.gc.drawImage (image, 0, 0);
		}
	});

	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ())
			display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}
}

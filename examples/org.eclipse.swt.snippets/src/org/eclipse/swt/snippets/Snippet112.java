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
 * Image example snippet: display an image in a group
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
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
	canvas.addPaintListener (e -> e.gc.drawImage (image, 0, 0));

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

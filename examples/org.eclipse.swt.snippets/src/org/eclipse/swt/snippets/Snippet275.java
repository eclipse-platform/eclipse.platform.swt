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
 * Canvas snippet: update a portion of a Canvas frequently
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet275 {

static String value;
public static void main (String[] args) {
	final int INTERVAL = 888;
	final Display display = new Display ();
	final Image image = new Image (display, 750, 750);
	GC gc = new GC (image);
	gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
	gc.fillRectangle (image.getBounds ());
	gc.dispose ();

	Shell shell = new Shell (display);
	shell.setBounds (10, 10, 790, 790);
	final Canvas canvas = new Canvas (shell, SWT.NONE);
	canvas.setBounds (10, 10, 750, 750);
	canvas.addListener (SWT.Paint, event -> {
		value = String.valueOf (System.currentTimeMillis ());
		event.gc.drawImage (image, 0, 0);
		event.gc.drawString (value, 10, 10, true);
	});
	display.timerExec (INTERVAL, new Runnable () {
		@Override
		public void run () {
			if (canvas.isDisposed ()) return;
			// canvas.redraw (); // <-- bad, damages more than is needed
			GC gc = new GC (canvas);
			Point extent = gc.stringExtent (value + '0');
			gc.dispose ();
			canvas.redraw (10, 10, extent.x, extent.y, false);
			display.timerExec (INTERVAL, this);
		}
	});
	shell.open ();
	while (!shell.isDisposed ()){
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}

}

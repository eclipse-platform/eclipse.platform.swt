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
 * Composite example snippet: scroll a child control automatically
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet9 {
	
public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.H_SCROLL | SWT.V_SCROLL);
	final Composite composite = new Composite (shell, SWT.BORDER);
	composite.setSize (700, 600);
	final Color red = display.getSystemColor (SWT.COLOR_RED);
	composite.addPaintListener (new PaintListener() {
		public void paintControl (PaintEvent e) {
			e.gc.setBackground (red);
			e.gc.fillOval (5, 5, 690, 590);
		}
	});
	final ScrollBar hBar = shell.getHorizontalBar ();
	hBar.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			Point location = composite.getLocation ();
			location.x = -hBar.getSelection ();
			composite.setLocation (location);
		}
	});
	final ScrollBar vBar = shell.getVerticalBar ();
	vBar.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			Point location = composite.getLocation ();
			location.y = -vBar.getSelection ();
			composite.setLocation (location);
		}
	});
	shell.addListener (SWT.Resize,  new Listener () {
		public void handleEvent (Event e) {
			Point size = composite.getSize ();
			Rectangle rect = shell.getClientArea ();
			hBar.setMaximum (size.x);
			vBar.setMaximum (size.y);
			hBar.setThumb (Math.min (size.x, rect.width));
			vBar.setThumb (Math.min (size.y, rect.height));
			int hPage = size.x - rect.width;
			int vPage = size.y - rect.height;
			int hSelection = hBar.getSelection ();
			int vSelection = vBar.getSelection ();
			Point location = composite.getLocation ();
			if (hSelection >= hPage) {
				if (hPage <= 0) hSelection = 0;
				location.x = -hSelection;
			}
			if (vSelection >= vPage) {
				if (vPage <= 0) vSelection = 0;
				location.y = -vSelection;
			}
			composite.setLocation (location);
		}
	});
	shell.setSize(600, 500);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}

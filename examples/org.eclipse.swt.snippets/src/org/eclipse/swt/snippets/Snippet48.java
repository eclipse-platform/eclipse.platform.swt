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
 * Canvas example snippet: scroll an image (flicker free, no double buffering)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet48 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	Image originalImage = null;
	FileDialog dialog = new FileDialog (shell, SWT.OPEN);
	dialog.setText ("Open an image file or cancel");
	String string = dialog.open ();
	if (string != null) {
		originalImage = new Image (display, string);
	}
	if (originalImage == null) {
		int width = 150, height = 200;
		originalImage = new Image (display, width, height);
		GC gc = new GC (originalImage);
		gc.fillRectangle (0, 0, width, height);
		gc.drawLine (0, 0, width, height);
		gc.drawLine (0, height, width, 0);
		gc.drawText ("Default Image", 10, 10);
		gc.dispose ();
	}
	final Image image = originalImage;
	final Point origin = new Point (0, 0);
	final Canvas canvas = new Canvas (shell, SWT.NO_BACKGROUND |
			SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
	final ScrollBar hBar = canvas.getHorizontalBar ();
	hBar.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			int hSelection = hBar.getSelection ();
			int destX = -hSelection - origin.x;
			Rectangle rect = image.getBounds ();
			canvas.scroll (destX, 0, 0, 0, rect.width, rect.height, false);
			origin.x = -hSelection;
		}
	});
	final ScrollBar vBar = canvas.getVerticalBar ();
	vBar.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			int vSelection = vBar.getSelection ();
			int destY = -vSelection - origin.y;
			Rectangle rect = image.getBounds ();
			canvas.scroll (0, destY, 0, 0, rect.width, rect.height, false);
			origin.y = -vSelection;
		}
	});
	canvas.addListener (SWT.Resize,  new Listener () {
		public void handleEvent (Event e) {
			Rectangle rect = image.getBounds ();
			Rectangle client = canvas.getClientArea ();
			hBar.setMaximum (rect.width);
			vBar.setMaximum (rect.height);
			hBar.setThumb (Math.min (rect.width, client.width));
			vBar.setThumb (Math.min (rect.height, client.height));
			int hPage = rect.width - client.width;
			int vPage = rect.height - client.height;
			int hSelection = hBar.getSelection ();
			int vSelection = vBar.getSelection ();
			if (hSelection >= hPage) {
				if (hPage <= 0) hSelection = 0;
				origin.x = -hSelection;
			}
			if (vSelection >= vPage) {
				if (vPage <= 0) vSelection = 0;
				origin.y = -vSelection;
			}
			canvas.redraw ();
		}
	});
	canvas.addListener (SWT.Paint, new Listener () {
		public void handleEvent (Event e) {
			GC gc = e.gc;
			gc.drawImage (image, origin.x, origin.y);
			Rectangle rect = image.getBounds ();
			Rectangle client = canvas.getClientArea ();
			int marginWidth = client.width - rect.width;
			if (marginWidth > 0) {
				gc.fillRectangle (rect.width, 0, marginWidth, client.height);
			}
			int marginHeight = client.height - rect.height;
			if (marginHeight > 0) {
				gc.fillRectangle (0, rect.height, client.width, marginHeight);
			}
		}
	});
	Rectangle rect = image.getBounds ();
	shell.setSize (Math.max(200, rect.width - 100), Math.max(150, rect.height - 100));
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	originalImage.dispose();
	display.dispose ();
}

} 

/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug404448_ImageButtons {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);

		Image image = new Image (display, 20, 20);
		Color color = display.getSystemColor (SWT.COLOR_BLUE);
		GC gc = new GC (image);
		gc.setBackground (color);
		gc.fillRectangle (image.getBounds ());
		gc.dispose ();
		
		Image disabledImage = new Image (display, 20, 20);
		color = display.getSystemColor (SWT.COLOR_GREEN);
		gc = new GC (disabledImage);
		gc.setBackground (color);
		gc.fillRectangle (disabledImage.getBounds ());
		gc.dispose ();
		
		Image hotImage = new Image (display, 20, 20);
		color = display.getSystemColor (SWT.COLOR_RED);
		gc = new GC (hotImage);
		gc.setBackground (color);
		gc.fillRectangle (hotImage.getBounds ());
		gc.dispose ();
		
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

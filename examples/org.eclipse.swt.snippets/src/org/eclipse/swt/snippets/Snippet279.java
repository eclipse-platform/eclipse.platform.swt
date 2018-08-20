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
 * draw a reflection of an image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet279 {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setLayout(new FillLayout ());
		final Image image = display.getSystemImage (SWT.ICON_QUESTION);
		shell.addListener (SWT.Paint, e -> {
			Rectangle rect = image.getBounds ();
			GC gc = e.gc;
			int x = 10, y = 10;
			gc.drawImage (image, x, y);
			Transform tr = new Transform (e.display);
			tr.setElements (1, 0, 0, -1, 1, 2*(y+rect.height));
			gc.setTransform (tr);
			gc.drawImage (image, x, y);
			gc.setTransform (null);
			Color background = gc.getBackground ();
			Pattern p = new Pattern (e.display, x, y+rect.height, x, y+(2*rect.height), background, 0, background, 255);
			gc.setBackgroundPattern (p);
			gc.fillRectangle (x, y+rect.height, rect.width, rect.height);
			p.dispose ();
			tr.dispose ();
		});
		shell.setSize (600, 400);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}

}


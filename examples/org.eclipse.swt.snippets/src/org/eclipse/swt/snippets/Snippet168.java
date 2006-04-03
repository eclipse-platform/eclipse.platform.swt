/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Draw lines and polygons with different cap and join styles
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet168 {

public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			int x = 20, y = 20, w = 120, h = 60;
			GC gc = event.gc;
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.setLineWidth(10);
			int[] caps = {SWT.CAP_FLAT, SWT.CAP_ROUND, SWT.CAP_SQUARE};
			for (int i = 0; i < caps.length; i++) {
				gc.setLineCap(caps[i]);
				gc.drawLine(x, y, x + w, y);
				y += 20;
			}
			int[] joins = {SWT.JOIN_BEVEL, SWT.JOIN_MITER, SWT.JOIN_ROUND};
			for (int i = 0; i < joins.length; i++) {
				gc.setLineJoin(joins[i]);
				gc.drawPolygon(new int[] {x, y, x + w/2, y + h, x + w, y});
				y += h + 20;
			}
		}
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

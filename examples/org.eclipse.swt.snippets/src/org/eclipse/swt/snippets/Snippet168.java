/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Draw lines with specific extremities
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet168 {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("PR");
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				GC gc = event.gc;
				gc.setLineWidth(10);
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
				int[] caps = {SWT.CAP_FLAT, SWT.CAP_ROUND, SWT.CAP_SQUARE};
				for (int i = 0; i < caps.length; i++) {
					gc.setLineCap(caps[i]);
					gc.drawLine(10,10 + i * 20, 100, 10 + i * 20);
				}
				int[] joins = {SWT.JOIN_BEVEL, SWT.JOIN_MITER, SWT.JOIN_ROUND};
				for (int i = 0; i < joins.length; i++) {
					gc.setLineJoin(joins[i]);
					gc.drawPolygon(new int[] {10,80 + i * 60, 50, 120 + i * 60, 100, 80 + i * 60});
				}
			}
		});
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}		
	}
}

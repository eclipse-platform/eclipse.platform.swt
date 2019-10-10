/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug551982_BlankCanvas {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Canvas Example");
		shell.setLayout(new FillLayout());
		final Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addPaintListener(e -> {
			GC gc = new GC(canvas);
			// Un-commenting this will fix the bug
//			GC gc = e.gc;
			gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			gc.drawFocus(5, 5, 200, 10);
			gc.drawText("You can draw text directly on a canvas", 60, 60);
			gc.dispose();
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
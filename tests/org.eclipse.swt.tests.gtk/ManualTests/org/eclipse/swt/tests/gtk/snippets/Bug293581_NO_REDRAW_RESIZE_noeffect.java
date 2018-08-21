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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug293581_NO_REDRAW_RESIZE_noeffect {

	static int counter = 0;
	public static void main(String[] args) {
	    final String IMAGE_FILENAME = "/home/ipun/Desktop/0ba.jpg";
	    final Display display = new Display();
	    Image image = new Image(display, IMAGE_FILENAME);
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
	    shell.setBackgroundImage(image);
	    Canvas canvas = new Canvas(shell, SWT.NO_REDRAW_RESIZE);
	    canvas.addListener(SWT.Paint, new Listener() {
	        @Override
			public void handleEvent(Event event) {
	            event.gc.drawString(String.valueOf(counter++), 0, 0);
	        }
	    });
	    shell.setBounds(10,10,200,200);
	    shell.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    image.dispose();
	    display.dispose();
	}
}



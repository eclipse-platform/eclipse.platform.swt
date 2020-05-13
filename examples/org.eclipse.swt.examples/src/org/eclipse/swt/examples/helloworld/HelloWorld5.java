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
package org.eclipse.swt.examples.helloworld;



import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * This example builds on HelloWorld1 and demonstrates how to draw directly
 * on an SWT Control.
 */
public class HelloWorld5 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new HelloWorld5 ().open (display);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public Shell open (Display display) {
	final Color red = new Color(0xFF, 0, 0);
	final Shell shell = new Shell (display);
	shell.addPaintListener(event -> {
		GC gc = event.gc;
		gc.setForeground(red);
		Rectangle rect = shell.getClientArea();
		gc.drawRectangle(rect.x + 10, rect.y + 10, rect.width - 20, rect.height - 20);
		gc.drawString(resHello.getString("Hello_world"), rect.x + 20, rect.y + 20);
	});
	shell.addDisposeListener (e -> red.dispose());
	shell.open ();
	return shell;
}
}

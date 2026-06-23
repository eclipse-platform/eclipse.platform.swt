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
 *     SAP SE		   - skia support
 *******************************************************************************/
package org.eclipse.swt.examples.skia;

/*
 * Fill a shape with a predefined pattern
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetPattern {

	final static boolean USE_SKIA = true;

	static int style = USE_SKIA ? SWT.SKIA : SWT.NONE;
	final static int WIDTH = 100;
	final static int HEIGHT = 100;

public static void main(String[] args) {

	Display display = new Display();
	//define a pattern on an image
	final Image image = new Image(display, WIDTH, HEIGHT);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
	Color white = display.getSystemColor(SWT.COLOR_WHITE);
	GC gc = new GC(image);
	gc.setBackground(white);
	gc.setForeground(yellow);
	gc.fillGradientRectangle(0, 0, 1000, 1000, true);
	for (int i=-500; i<1000; i+=10) {
		gc.setForeground(blue);
		gc.drawLine(i, 0, 500 + i, 1000);
		gc.drawLine(500 + i, 0, i, 1000);
	}
	gc.drawRectangle(5, 5, WIDTH - 11, HEIGHT -11);
	gc.dispose();
	final Pattern pattern;
	try {
		pattern = new Pattern(display, image);
//		pattern = new Pattern(display, 0, 0, 100, 100, blue, yellow);
	} catch (SWTException e) {
		//Advanced Graphics not supported.
		//This new API requires the Cairo Vector engine on GTK and GDI+ on Windows.
		System.out.println(e.getMessage());
		display.dispose();
		return;
	}

	Shell shell = new Shell(display);
	shell.setText("SnippetPattern");
	shell.setLayout(new FillLayout());
	Canvas c = new Canvas(shell, SWT.DOUBLE_BUFFERED | style);
	c.addListener(SWT.Paint, event -> {
		Rectangle r = ((Composite)event.widget).getClientArea();
		GC gc1 = event.gc;
		gc1.setBackgroundPattern(pattern);
		gc1.fillRectangle(5, 5, r.width - 10, r.height - 10);
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	image.dispose();
	pattern.dispose();
	display.dispose();
}
}
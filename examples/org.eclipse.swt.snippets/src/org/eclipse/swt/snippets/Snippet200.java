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
 * Fill a shape with a predefined pattern
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet200 {
public static void main(String[] args) {
	Display display = new Display();
	//define a pattern on an image
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		gc.setBackground(white);
		gc.setForeground(yellow);
		gc.fillGradientRectangle(0, 0, 1000, 1000, true);
		for (int i=-500; i<1000; i+=10) {
			gc.setForeground(blue);
			gc.drawLine(i, 0, 500 + i, 1000);
			gc.drawLine(500 + i, 0, i, 1000);
		}
	};
	final Image image = new Image(display, imageGcDrawer, 1000, 1000);

	final Pattern pattern;
	try {
		pattern = new Pattern(display, image);
	} catch (SWTException e) {
		//Advanced Graphics not supported.
		//This new API requires the Cairo Vector engine on GTK and GDI+ on Windows.
		System.out.println(e.getMessage());
		display.dispose();
		return;
	}

	Shell shell = new Shell(display);
	shell.setText("Snippet 200");
	shell.setLayout(new FillLayout());
	Composite c = new Composite(shell, SWT.DOUBLE_BUFFERED);
	c.addListener(SWT.Paint, event -> {
		Rectangle r = ((Composite)event.widget).getClientArea();
		GC gc1 = event.gc;
		gc1.setBackgroundPattern(pattern);
		gc1.fillOval(5, 5, r.width - 10, r.height - 10);
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

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
	final Image image = new Image(display, 1000, 1000);
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
	gc.dispose();
	final Pattern pattern;
	try {
		pattern = new Pattern(display, image);
	} catch (SWTException e) {
		//Advanced Graphics not supported.  
		//This new API requires the Cairo Vector engine on GTK and Motif and GDI+ on Windows.
		System.out.println(e.getMessage());
		display.dispose();
		return;
	}
	
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	Composite c = new Composite(shell, SWT.DOUBLE_BUFFERED);
	c.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			Rectangle r = ((Composite)event.widget).getClientArea();
			GC gc = event.gc;
			gc.setBackgroundPattern(pattern);
			gc.fillOval(5, 5, r.width - 10, r.height - 10);	
		}
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

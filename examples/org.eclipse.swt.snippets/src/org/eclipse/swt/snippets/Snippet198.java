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
 * Create a path from some text
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet198 {
public static void main(String[] args) {
	Display display = new Display();
	FontData data = display.getSystemFont().getFontData()[0];
	Font font = new Font(display, data.getName(), 96, SWT.BOLD | SWT.ITALIC);
	final Color green = display.getSystemColor(SWT.COLOR_GREEN);
	final Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	final Path path;
	try {
		path = new Path(display);
		path.addString("SWT", 0, 0, font);
	} catch (SWTException e) {
		//Advanced Graphics not supported.  
		//This new API requires the Cairo Vector engine on GTK and Motif and GDI+ on Windows.
		System.out.println(e.getMessage());
		display.dispose();
		return;
	}
	Shell shell = new Shell(display);
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {			
			GC gc = e.gc;
			gc.setBackground(green);
			gc.setForeground(blue);
			gc.fillPath(path);
			gc.drawPath(path);
		}
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	path.dispose();
	font.dispose();
	display.dispose();
}
}

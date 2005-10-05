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
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet198 {
public static void main(String[] args) {
	final Display display = new Display();
	FontData data = display.getSystemFont().getFontData()[0];
	final Font font = new Font(display, data.getName(), 96, SWT.BOLD | SWT.ITALIC);
	final Color green = display.getSystemColor(SWT.COLOR_GREEN);
	final Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	Shell shell = new Shell(display);
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {			
			GC gc = e.gc;
			Path path = new Path(display);
			path.addString("SWT", 0, 0, font);
			gc.setBackground(green);
			gc.setForeground(blue);
			gc.fillPath(path);
			gc.drawPath(path);
			path.dispose();
		}
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	font.dispose();
	display.dispose();
}
}

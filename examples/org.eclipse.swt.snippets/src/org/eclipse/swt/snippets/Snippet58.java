/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * ToolBar example snippet: place a combo box in a tool bar
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet58 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	ToolBar bar = new ToolBar (shell, SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	bar.setLocation (clientArea.x, clientArea.y);
	for (int i=0; i<4; i++) {
		ToolItem item = new ToolItem (bar, 0);
		item.setText ("Item " + i);
	}
	ToolItem sep = new ToolItem (bar, SWT.SEPARATOR);
	int start = bar.getItemCount ();
	for (int i=start; i<start+4; i++) {
		ToolItem item = new ToolItem (bar, 0);
		item.setText ("Item " + i);
	}
	Combo combo = new Combo (bar, SWT.READ_ONLY);
	for (int i=0; i<4; i++) {
		combo.add ("Item " + i);
	}
	combo.pack ();
	sep.setWidth (combo.getSize ().x);
	sep.setControl (combo);
	bar.pack ();
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

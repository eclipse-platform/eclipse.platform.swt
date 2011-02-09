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
 * Table example snippet: create a table (no columns, no headers)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet35 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Table table = new Table (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	for (int i=0; i<12; i++) {
		TableItem item = new TableItem (table, 0);
		item.setText ("Item " + i);
	}
	Rectangle clientArea = shell.getClientArea ();
	table.setBounds (clientArea.x, clientArea.y, 100, 100);
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

} 

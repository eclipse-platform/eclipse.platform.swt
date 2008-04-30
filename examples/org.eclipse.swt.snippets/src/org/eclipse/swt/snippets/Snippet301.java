/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * NO_SCROLL snippet: Create a table with the NO_SCROLL bit.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet301 {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		shell.setLayout(layout);
		Table table = new Table (shell, SWT.BORDER | SWT.NO_SCROLL);

		for (int i=0; i<10; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Item " + i);
		}
		
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}


}

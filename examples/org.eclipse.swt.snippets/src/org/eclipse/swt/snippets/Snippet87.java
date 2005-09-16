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
 * PocketPC Shell example snippet: resize automatically as SIP is on or off
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet87 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display, SWT.RESIZE);
	shell.setText("Main");
	Menu mb = new Menu(shell, SWT.BAR);
	shell.setMenuBar(mb);
	/* Add a button to make the resize more visual */
	FillLayout layout = new FillLayout();
	shell.setLayout(layout);
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Main");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}

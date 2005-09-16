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
 * PocketPC example snippet: Hello World
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet85 {

public static void main(String[] args) {
	Display display = new Display();

	/* 
	 * Create a Shell with the default style
	 * i.e. full screen, no decoration on PocketPC.
	 */
	Shell shell = new Shell(display);

	/* 
	 * Set a text so that the top level Shell
	 * also appears in the PocketPC task list
	 */
	shell.setText("Main");

	/*
	 * Set a menubar to follow UI guidelines
	 * on PocketPC
	 */
	Menu mb = new Menu(shell, SWT.BAR);
	shell.setMenuBar(mb);

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}

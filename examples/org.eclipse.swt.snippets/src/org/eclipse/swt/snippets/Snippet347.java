//
/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/*
 * Display snippet: get the AppMenuBar on Cocoa or keep going on other platforms.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

public class Snippet347 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
	
		Menu appMenuBar = display.getAppMenuBar();
		
		if (appMenuBar == null) appMenuBar = new Menu(shell);
		
		MenuItem item = new MenuItem(appMenuBar, SWT.PUSH);
		item.setText("Sample");
	
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Test");
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

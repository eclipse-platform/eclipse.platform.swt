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

/*
 * Tool shell snippet: create a tool shell
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet344 {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Click me");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Shell shell2 = new Shell(SWT.TOOL | SWT.RESIZE | SWT.CLOSE | SWT.MAX);
				shell2.setLayout(new GridLayout(1, false));
				shell2.setText("Palette");
				Label l = new Label(shell2, SWT.LEFT);
				l.setText("This is a SWT.TOOL Shell");
				Point origin = shell.getLocation();
				origin.x += 100;
				origin.y += 100;
				shell2.pack();
				shell2.open();
			}
		});
				
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
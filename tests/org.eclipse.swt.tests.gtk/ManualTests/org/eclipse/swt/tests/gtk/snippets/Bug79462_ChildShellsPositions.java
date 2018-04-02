/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug79462_ChildShellsPositions {
public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.addListener (SWT.MouseDown, new Listener () {
		@Override
		public void handleEvent(Event event) {
			Shell dialog = new Shell (shell, SWT.DIALOG_TRIM);
			dialog.setBounds (10, 10, 100, 100);
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}			
		}
	});	
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
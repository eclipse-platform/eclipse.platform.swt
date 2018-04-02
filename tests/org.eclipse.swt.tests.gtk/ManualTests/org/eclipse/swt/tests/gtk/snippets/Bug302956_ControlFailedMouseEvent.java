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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug302956_ControlFailedMouseEvent {

	public static void main(String[] args) {
	    final Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setBounds(10, 10, 800, 500);
	    Shell shell2 = new Shell(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM);
	    shell2.setBounds(200, 200, 125, 125);
	    Button button = new Button(shell2, SWT.PUSH);
	    button.setBounds(50, 50, 50, 50);
	    button.addMouseTrackListener(new MouseTrackAdapter() {
	        @Override
			public void mouseEnter(MouseEvent e) {
	            System.out.println("ENTER");
	        }
	        @Override
			public void mouseExit(MouseEvent e) {
	            System.out.println("EXIT");
	        }
	    });
	    shell.open();
	    shell2.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    display.dispose();
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.helloworld;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.util.ResourceBundle;

/*
 * This example builds on HelloWorld2 and demonstrates how to resize the 
 * Label when the Shell resizes using a Listener mechanism.
 */
public class HelloWorld3 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new HelloWorld3 ().open (display);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public Shell open (Display display) {
	final Shell shell = new Shell (display);
	final Label label = new Label (shell, SWT.CENTER);
	label.setText (resHello.getString("Hello_world"));
	label.pack();
	shell.addControlListener(new ControlAdapter() {
		public void controlResized(ControlEvent e) {
			label.setBounds (shell.getClientArea ());
		}
	});
	shell.pack();
	shell.open ();
	return shell;
}
}

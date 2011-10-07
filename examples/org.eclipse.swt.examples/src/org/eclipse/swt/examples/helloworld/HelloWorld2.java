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
import java.util.ResourceBundle;

/*
 * This example builds on HelloWorld1 and demonstrates the minimum amount 
 * of code required to open an SWT Shell with a Label and process the events.
 */
public class HelloWorld2 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new HelloWorld2 ().open (display);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public Shell open (Display display) {
	Shell shell = new Shell (display);
	Label label = new Label (shell, SWT.CENTER);
	label.setText (resHello.getString("Hello_world"));
	label.setBounds (shell.getClientArea ());
	shell.open ();
	return shell;
}
}

package org.eclipse.swt.examples.helloworld;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.widgets.*;

/*
 * This example demonstrates the minimum amount of code required
 * to open an SWT Shell and process the events.
 */
public class HelloWorld1 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new HelloWorld1 ().open (display);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
	
public Shell open (Display display) {
	Shell shell = new Shell (display);
	shell.open ();
	return shell;
}
}

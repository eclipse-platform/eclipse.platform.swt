package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
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

package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;

/*
 * This example demonstrates the minimum amount of code required to open an SWT Shell
 * and process the events.
 */
public class HelloWorld1 {
	public static Display display;
	private static Shell shell;

public static void main (String [] args) {
	display = new Display ();
	new HelloWorld1 ().open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
	
public void open () {
	shell = new Shell (display);
	shell.open ();
}
}

package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import java.util.ResourceBundle;

/*
 * This example builds on HelloWorld1 and demonstrates the minimum amount 
 * of code required to open an SWT Shell with a Label and process the events.
 */
public class HelloWorld2 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	public static Display display;
	private static Shell shell;
	
public static void main (String [] args) {
	display = new Display ();
	new HelloWorld2 ().open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public void open () {
	shell = new Shell (display);
	Label label = new Label (shell, SWT.CENTER);
	label.setText (resHello.getString("Hello_world"));
	label.setBounds (shell.getClientArea ());
	shell.open ();
}
}

package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import java.util.ResourceBundle;

/*
 * This example builds on HelloWorld2 and demonstrates how to resize the 
 * Label when the Shell resizes using a Layout.
 */
public class HelloWorld4 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new HelloWorld4 ().open (display);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public Shell open (Display display) {
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	Label label = new Label (shell, SWT.CENTER);
	label.setText (resHello.getString("Hello_world"));
	shell.pack ();
	shell.open ();
	return shell;
}
}

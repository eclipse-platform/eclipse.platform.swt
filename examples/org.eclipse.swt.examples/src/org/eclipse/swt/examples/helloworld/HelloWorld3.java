package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */

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
	public static Display display;
	private static Shell shell;
	
public static void main (String [] args) {
	Display display = new Display ();
	new HelloWorld3 ().open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public void open () {
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
}
}

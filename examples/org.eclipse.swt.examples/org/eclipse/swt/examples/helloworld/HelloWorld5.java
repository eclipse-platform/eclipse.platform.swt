package org.eclipse.swt.examples.helloworld;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import java.util.ResourceBundle;

/*
 * This example builds on HelloWorld1 and demonstrates how to draw directly
 * on an SWT Control.
 */
public class HelloWorld5 {
	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	public static Display display;
	private static Shell shell;	

public static void main (String [] args) {
	display = new Display ();
	new HelloWorld5 ().open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public void open () {
	final Color red = new Color(display, 0xFF, 0, 0);
	shell = new Shell (display);
	shell.addPaintListener(new PaintListener () {
		public void paintControl(PaintEvent event){
			GC gc = event.gc;
			gc.setForeground(red);
			Rectangle rect = shell.getClientArea();
			gc.drawRectangle(rect.x + 10, rect.y + 10, rect.width - 20, rect.height - 20);
			gc.drawString(resHello.getString("Hello_world"), rect.x + 20, rect.y + 20);
		}
	});
	shell.addShellListener (new ShellAdapter () {
		public void shellClosed (ShellEvent e) {
			red.dispose();
		}
	});
	shell.open ();
}
}

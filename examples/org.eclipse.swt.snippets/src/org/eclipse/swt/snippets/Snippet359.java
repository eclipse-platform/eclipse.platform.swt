/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This Example Content is intended to demonstrate
 * usage of Eclipse technology. It is provided to you under the terms and
 * conditions of the Eclipse Distribution License v1.0 which is available
 * at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Combo getCaretLocation and getCaretPosition example
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet359 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Combo combo = new Combo(shell, SWT.DROP_DOWN);
		combo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			   if (e.keyCode == SWT.CR) {
				   combo.add(combo.getText());
			   }
			}
			public void keyPressed(KeyEvent e) {
				System.out.println("caret position: " + combo.getCaretPosition());
				System.out.println("caret location: " + combo.getCaretLocation()); 
			}
		});
	  shell.pack();
	  shell.open ();
	  while (!shell.isDisposed()) {
	   if (!display.readAndDispatch ()) display.sleep ();
	  }
	  display.dispose ();
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * UI Automation (for testing tools) snippet: post mouse events
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet142 {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	final Button button = new Button(shell,SWT.NONE);
	button.setSize(100,100);
	button.setText("Click");
	shell.pack();
	shell.open();
	button.addListener(SWT.MouseDown, e -> System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")"));
	final Point pt = display.map(shell, null, 50, 50);
	new Thread(){
		Event event;
		@Override
		public void run(){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {}
			event = new Event();
			event.type = SWT.MouseMove;
			event.x = pt.x;
			event.y = pt.y;
			display.post(event);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {}
			event.type = SWT.MouseDown;
			event.button = 1;
			display.post(event);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {}
			event.type = SWT.MouseUp;
			display.post(event);
		}
	}.start();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.accessibility.*;

/**
 * This example shows a very common, simple use of the SWT Accessibility API:
 * giving an accessible name to a button that has only an image (and no text).
 */
public class AccessibleNameExample {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("Accessible Name");
		
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button"); // the first button's accessible name is "Button"
		
		Image image = new Image(display, AccessibleNameExample.class.getResourceAsStream("run.gif"));
		button = new Button(shell, SWT.PUSH);
		button.setImage(image);
		button.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = "Running man"; // the second button's accessible name is "Running man"
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}
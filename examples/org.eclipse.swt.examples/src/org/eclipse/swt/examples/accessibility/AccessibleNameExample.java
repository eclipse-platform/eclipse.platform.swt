/*******************************************************************************
 * Copyright (c) 2008, 2013 IBM Corporation and others.
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
			@Override
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
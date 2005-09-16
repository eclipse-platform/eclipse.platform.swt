/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Override the text that is spoken for a native Button.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.widgets.*;

public class Snippet164 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10, 10, 200, 200);

	Button button1 = new Button (shell, SWT.PUSH);
	button1.setText("&Typical button");
	button1.setBounds(10,10,180,30);
	Button button2 = new Button (shell, SWT.PUSH);
	button2.setText("&Overidden button");
	button2.setBounds(10,50,180,30);
	button2.getAccessible().addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			e.result = "Speak this instead of the button text";
		}
	});
	
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

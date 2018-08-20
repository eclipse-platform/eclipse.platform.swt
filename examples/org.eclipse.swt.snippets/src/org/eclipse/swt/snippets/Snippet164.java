/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * Provide text that will be spoken for an image Button.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet164 {

public static void main (String[] args) {
	Display display = new Display( );
	Shell shell = new Shell (display);
	shell.setLayout (new GridLayout ());
	Image image = new Image (display, Snippet164.class.getResourceAsStream ("eclipse.png"));

	Button button1 = new Button (shell, SWT.PUSH);
	button1.setText ("&Typical button");

	Button button2 = new Button (shell, SWT.PUSH);
	button2.setImage (image);
	button2.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
		@Override
		public void getName (AccessibleEvent e) {
			e.result = "Eclipse logo";
		}
	});

	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	display.dispose ();
}
}

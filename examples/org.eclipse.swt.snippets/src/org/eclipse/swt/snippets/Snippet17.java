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
 * Slider example snippet: print scroll event details
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet17 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Slider slider = new Slider (shell, SWT.HORIZONTAL);
	Rectangle clientArea = shell.getClientArea ();
	slider.setBounds (clientArea.x + 10, clientArea.y + 10, 200, 32);
	slider.addListener (SWT.Selection, event -> {
		String string = "SWT.NONE";
		switch (event.detail) {
			case SWT.DRAG: string = "SWT.DRAG"; break;
			case SWT.HOME: string = "SWT.HOME"; break;
			case SWT.END: string = "SWT.END"; break;
			case SWT.ARROW_DOWN: string = "SWT.ARROW_DOWN"; break;
			case SWT.ARROW_UP: string = "SWT.ARROW_UP"; break;
			case SWT.PAGE_DOWN: string = "SWT.PAGE_DOWN"; break;
			case SWT.PAGE_UP: string = "SWT.PAGE_UP"; break;
		}
		System.out.println ("Scroll detail -> " + string);
	});
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug497707_ShellAlpha {

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		// Set alpha to whatever
		int alpha = 128;
		shell.setAlpha(alpha);
		System.out.println("Alpha is set to " + alpha);
		System.out.println("Shell.getAlpha() returns: " + shell.getAlpha());
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}

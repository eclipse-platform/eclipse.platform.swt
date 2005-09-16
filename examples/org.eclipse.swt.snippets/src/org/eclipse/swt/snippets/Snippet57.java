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
 * ProgressBar example snippet: update a progress bar (from the UI thread)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet57 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	ProgressBar bar = new ProgressBar (shell, SWT.SMOOTH);
	bar.setBounds (10, 10, 200, 32);
	shell.open ();
	for (int i=0; i<=bar.getMaximum (); i++) {
		try {Thread.sleep (100);} catch (Throwable th) {}
		bar.setSelection (i);
	}
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

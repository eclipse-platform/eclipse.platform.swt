/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
 * SegmentListener example snippet: create tool bar (wrap on resize)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet900 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	final Button button = new Button(shell, SWT.PUSH);
	button.setText("def");

	button.addListener(SWT.Segments, new Listener() {

		@Override
		public void handleEvent(Event event) {
			button.setText("abc");
		}
		
	});
	button.pack();
	button.setLocation(10, 180);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

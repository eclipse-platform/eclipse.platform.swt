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
 * Create two ScrolledComposites that scroll in tandem.
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet167 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());

	final ScrolledComposite sc1 = new ScrolledComposite (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	Button button1 = new Button (sc1, SWT.PUSH);
	button1.setText ("Button 1");
	button1.setSize (400, 400);
	sc1.setContent (button1);

	final ScrolledComposite sc2 = new ScrolledComposite (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	Button button2 = new Button(sc2, SWT.PUSH);
	button2.setText ("Button 2");
	button2.setSize (400, 400);
	sc2.setContent (button2);

	final ScrollBar vBar1 = sc1.getVerticalBar ();
	final ScrollBar vBar2 = sc2.getVerticalBar ();
	final ScrollBar hBar1 = sc1.getHorizontalBar ();
	final ScrollBar hBar2 = sc2.getHorizontalBar ();
	SelectionListener listener1 = new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			sc2.setOrigin (
				hBar1.getSelection (),
				vBar1.getSelection ());
		}
	};
	SelectionListener listener2 = new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			sc1.setOrigin (
				hBar2.getSelection (),
				vBar2.getSelection ());
		}
	};
	vBar1.addSelectionListener (listener1);
	hBar1.addSelectionListener (listener1);
	vBar2.addSelectionListener (listener2);
	hBar2.addSelectionListener (listener2);

	shell.setSize (600, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

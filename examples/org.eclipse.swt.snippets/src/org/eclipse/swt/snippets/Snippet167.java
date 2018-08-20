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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *******************************************************************************/
package org.eclipse.swt.snippets;


/*
 * Create two ScrolledComposites that scroll in tandem.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

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
	button1.setSize (400, 300);
	sc1.setContent (button1);

	final ScrolledComposite sc2 = new ScrolledComposite (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	Button button2 = new Button(sc2, SWT.PUSH);
	button2.setText ("Button 2");
	button2.setSize (300, 400);
	sc2.setContent (button2);

	final ScrollBar vBar1 = sc1.getVerticalBar ();
	final ScrollBar vBar2 = sc2.getVerticalBar ();
	final ScrollBar hBar1 = sc1.getHorizontalBar ();
	final ScrollBar hBar2 = sc2.getHorizontalBar ();
	SelectionListener listener1 = widgetSelectedAdapter(e -> {
			int x =  hBar1.getSelection() * (hBar2.getMaximum() - hBar2.getThumb()) / Math.max(1, hBar1.getMaximum() - hBar1.getThumb());
			int y =  vBar1.getSelection() * (vBar2.getMaximum() - vBar2.getThumb()) / Math.max(1, vBar1.getMaximum() - vBar1.getThumb());
			sc2.setOrigin (x, y);
		});
	SelectionListener listener2 = widgetSelectedAdapter(e -> {
			int x =  hBar2.getSelection() * (hBar1.getMaximum() - hBar1.getThumb()) / Math.max(1, hBar2.getMaximum() - hBar2.getThumb());
			int y =  vBar2.getSelection() * (vBar1.getMaximum() - vBar1.getThumb()) / Math.max(1, vBar2.getMaximum() - vBar2.getThumb());
			sc1.setOrigin (x, y);
		});
	vBar1.addSelectionListener (listener1);
	hBar1.addSelectionListener (listener1);
	vBar2.addSelectionListener (listener2);
	hBar2.addSelectionListener (listener2);

	shell.setSize (400, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

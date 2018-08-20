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
 * Sash example snippet: implement a simple splitter (with a 20 pixel limit)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet107 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	Button button1 = new Button (shell, SWT.PUSH);
	button1.setText ("Button 1");
	final Sash sash = new Sash (shell, SWT.VERTICAL);
	Button button2 = new Button (shell, SWT.PUSH);
	button2.setText ("Button 2");

	final FormLayout form = new FormLayout ();
	shell.setLayout (form);

	FormData button1Data = new FormData ();
	button1Data.left = new FormAttachment (0, 0);
	button1Data.right = new FormAttachment (sash, 0);
	button1Data.top = new FormAttachment (0, 0);
	button1Data.bottom = new FormAttachment (100, 0);
	button1.setLayoutData (button1Data);

	final int limit = 20, percent = 50;
	final FormData sashData = new FormData ();
	sashData.left = new FormAttachment (percent, 0);
	sashData.top = new FormAttachment (0, 0);
	sashData.bottom = new FormAttachment (100, 0);
	sash.setLayoutData (sashData);
	sash.addListener (SWT.Selection, e -> {
		Rectangle sashRect = sash.getBounds ();
		Rectangle shellRect = shell.getClientArea ();
		int right = shellRect.width - sashRect.width - limit;
		e.x = Math.max (Math.min (e.x, right), limit);
		if (e.x != sashRect.x)  {
			sashData.left = new FormAttachment (0, e.x);
			shell.layout ();
		}
	});

	FormData button2Data = new FormData ();
	button2Data.left = new FormAttachment (sash, 0);
	button2Data.right = new FormAttachment (100, 0);
	button2Data.top = new FormAttachment (0, 0);
	button2Data.bottom = new FormAttachment (100, 0);
	button2.setLayoutData (button2Data);

	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

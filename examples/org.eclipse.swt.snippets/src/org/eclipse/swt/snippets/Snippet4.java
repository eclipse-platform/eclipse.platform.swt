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
 * Shell example snippet: prevent escape from closing a dialog
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet4 {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Open Dialog ...");
		b.pack();
		Rectangle clientArea = shell.getClientArea();
		b.setLocation(clientArea.x + 10, clientArea.y + 10);
		b.addSelectionListener(widgetSelectedAdapter(e -> {
			Shell dialog = new Shell(shell, SWT.DIALOG_TRIM);
			dialog.addListener(SWT.Traverse, t -> {
				if (t.detail == SWT.TRAVERSE_ESCAPE) {
					t.doit = false;
				}
			});
			dialog.open();
		}));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

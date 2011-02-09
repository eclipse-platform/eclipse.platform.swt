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
 * Shell example snippet: prevent escape from closing a dialog
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
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
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				Shell dialog = new Shell(shell, SWT.DIALOG_TRIM);
				dialog.addListener(SWT.Traverse, new Listener() {
					public void handleEvent(Event e) {
						if (e.detail == SWT.TRAVERSE_ESCAPE) {
							e.doit = false;
						}
					}
				});
				dialog.open();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

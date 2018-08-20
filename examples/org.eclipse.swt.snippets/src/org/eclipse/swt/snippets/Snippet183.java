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
 * Link example snippet: detect selection events in a link widget
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet183 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Link link = new Link(shell, SWT.NONE);
		String text = "The SWT component is designed to provide <a>efficient</a>, <a>portable</a> <a href=\"native\">access to the user-interface facilities of the operating systems</a> on which it is implemented.";
		link.setText(text);
		Rectangle clientArea = shell.getClientArea();
		link.setBounds(clientArea.x, clientArea.y, 400, 400);
		link.addListener (SWT.Selection, event -> System.out.println("Selection: " + event.text));
		shell.pack ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
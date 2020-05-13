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
 * Link example snippet: create a link widget and
 * provide buttons to set the link color.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet182 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 182");
		shell.setLayout(new RowLayout());

		Link link = new Link(shell, SWT.BORDER);
		link.setText("This a very simple <a>link</a> widget.");

		Button setButton = new Button(shell, SWT.PUSH);
		setButton.setText("Choose link color");
		setButton.addSelectionListener(widgetSelectedAdapter(e -> {
			System.out.println("default link color " + link.getLinkForeground());
			ColorDialog colorDialog = new ColorDialog(shell);
			RGB color = colorDialog.open();
			link.setLinkForeground(new Color(color));
			System.out.println("user selected link color " + link.getLinkForeground());
		}));

		Button resetButton = new Button(shell, SWT.PUSH);
		resetButton.setText("Reset link color");
		resetButton.addSelectionListener(widgetSelectedAdapter(e -> {
			System.out.println("link color reset to system default");
			link.setLinkForeground(null);
		}));

		shell.pack ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
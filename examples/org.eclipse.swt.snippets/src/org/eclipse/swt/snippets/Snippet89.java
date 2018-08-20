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
 * Menu example snippet: create a menu with radio items
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet89 {
public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Menu menu = new Menu (shell, SWT.POP_UP);
	for (int i = 0; i < 4; i++) {
			MenuItem item = new MenuItem(menu, SWT.RADIO);
			item.setText("Item " + i);
			item.addSelectionListener(widgetSelectedAdapter(e -> {
				MenuItem menuItem = (MenuItem) e.widget;
				if (menuItem.getSelection()) {
					System.out.println(menuItem + " selected");
				} else {
					System.out.println(menuItem + " unselected");
				}
			}));
		}
	shell.setMenu (menu);
	shell.setSize (300, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

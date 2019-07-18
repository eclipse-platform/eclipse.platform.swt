/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug549376_JvmCrashMenuRadio {
	public static void main (String [] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		FillLayout layout = new FillLayout();
		layout.marginHeight = layout.marginWidth = 10;
		shell.setLayout(layout);

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		MenuItem menu = new MenuItem(menuBar, SWT.CASCADE);
		menu.setText("Menu");

		final Menu subMenu = new Menu(menu);
		menu.setMenu(subMenu);

		// Creating a RADIO item during SWT.FocusIn is one way to crash
		shell.addListener(SWT.FocusIn, e -> {
			new MenuItem(subMenu, SWT.RADIO);
		});

		Label hintLabel = new Label(shell, SWT.NONE);
		hintLabel.setText("If you see this text, the crash didn't reproduce");

		shell.pack();
		shell.open();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

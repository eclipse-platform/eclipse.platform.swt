/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug479200_MenuTest {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Text t = new Text(shell, SWT.BORDER | SWT.MULTI);
		t.setText ("here is some widget.text to be selected");
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		MenuItem editItem = new MenuItem (bar, SWT.CASCADE);
		editItem.setText ("Edit");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		editItem.setMenu (submenu);

		MenuItem item = new MenuItem (submenu, SWT.PUSH);
		item.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event e) {
				t.selectAll();
			}
		});
		item.setText ("Select &All\tCtrl+A");
		item.setAccelerator (SWT.MOD1 + 'A');
		
//		Test for error in count
		item.setEnabled(false);
		System.out.println(bar.getItemCount());
		
		// Note that as long as your application has not overridden 
		// the global accelerators for copy, paste, and cut 
		//(CTRL+C or CTRL+INSERT, CTRL+V or SHIFT+INSERT, and CTRL+X or SHIFT+DELETE)
		// these behaviours are already available by default.
		// If your application overrides these accelerators,
		// you will need to call Text.copy(), Text.paste() and Text.cut()
		// from the Selection callback for the accelerator when the 
		// widget.text widget has focus.

		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug185538_ComboNoFocus {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Shell");

		final Combo combo = new Combo(shell, SWT.NONE);
		combo.setItems(new String[] { "do nothing", "dispose this widget.combo" });
		Point size = combo.computeSize(-1, -1);
		combo.setBounds(5, 5, size.x, size.y);
		combo.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (combo.getSelectionIndex() == 1)
					combo.dispose();
			}
		});
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				String type = event.type == SWT.FocusIn ? "FocusIn" : "FocusOut";
				System.out.println(type + " " + event.widget);
			}
		};
		shell.addListener(SWT.FocusIn, listener);
		shell.addListener(SWT.FocusOut, listener);
		combo.addListener(SWT.FocusIn, listener);
		combo.addListener(SWT.FocusOut, listener);

		shell.setSize(300, 200);
		shell.open();

		while (!shell.isDisposed()) {
			boolean dispatch = display.readAndDispatch();
			if (!dispatch)
				display.sleep();
		}
		display.dispose();
	}
	
}

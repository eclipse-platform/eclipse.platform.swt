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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Based on Snippet #26. Run this as an SWT Application, and then select the
 * first "A" from the widget.combo. Text will be printed to the console. Select the
 * second "A" from the widget.combo, widget.text will not be printed. Open the widget.combo again,
 * and the first "A" is still selected.
 */
public class Bug113798_ComboSelectionEvent {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(new String[] { "A", "A", "B", "C" });
		combo.setSize(200, 200);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Widget selected");
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}


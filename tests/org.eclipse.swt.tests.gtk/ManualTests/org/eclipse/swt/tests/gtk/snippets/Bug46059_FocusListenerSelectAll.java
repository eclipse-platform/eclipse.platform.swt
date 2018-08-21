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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug46059_FocusListenerSelectAll {

	public static final void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());
		final Text text1 = new Text(shell, SWT.NULL);
		text1.setText("Set focus, but select all fails."); //$NON-NLS-1$
		text1.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				text1.setSelection(0, text1.getText().length());
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Do nothing.
			}
		});
		final Text text2 = new Text(shell, SWT.NULL);
		text2.setText("Use me to change focus."); //$NON-NLS-1$
		text2.setFocus();
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug516480_KeydownRussianlayout {
	/**
	 * @author Thomas Singer
	 */
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final StyledText text = new StyledText(shell, SWT.BORDER);
		text.setText("hello world");

		text.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("event.character = " + (int)event.character);
				System.out.println("event.keyCode = " + event.keyCode);
			}
		});

		shell.setSize(300, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug302171_AltGr {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final Text text = new Text(shell, SWT.BORDER);
		text.setSize(text.computeSize(150, SWT.DEFAULT));
		shell.pack();
		shell.open();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.err.println(e);
				}

				String string = "q23";

				for (int i = 0; i < string.length(); i++) {
					char ch = string.charAt(i);

					/* Press the character */
					Event event = new Event();
					event.type = SWT.KeyDown;
					event.character = ch;
					display.post(event);

					/* Release the character */
					event.type = SWT.KeyUp;
					display.post(event);

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						System.err.println(e);
					}

					/* Press SHIFT + ch */
					event = new Event();
					event.type = SWT.KeyDown;
					event.character = ch;
					event.stateMask = SWT.SHIFT;
					display.post(event);

					/* Release the character */
					event.type = SWT.KeyUp;
					display.post(event);

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						System.err.println(e);
					}

					/* Press ALT_GR + ch */
					event = new Event();
					event.type = SWT.KeyDown;
					event.character = ch;
					event.stateMask = SWT.ALT_GR;
					display.post(event);

					/* Release the character */
					event.type = SWT.KeyUp;
					display.post(event);

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						System.err.println(e);
					}


				}
			}
		}.start();


		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}

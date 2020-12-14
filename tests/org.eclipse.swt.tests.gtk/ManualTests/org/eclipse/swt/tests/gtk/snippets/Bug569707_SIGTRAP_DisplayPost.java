/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug569707_SIGTRAP_DisplayPost {
	static void sendKey(Display display, char key) {
		final Event e = new Event();
		e.keyCode = key;
		e.character = key;
		e.type = SWT.KeyDown;
		display.post(e);
	}

	static void sendMouse(Display display, Point point) {
		final Event e = new Event();

		e.type = SWT.MouseMove;
		e.x = point.x;
		e.y = point.y;
		display.post(e);

		e.type = SWT.MouseDown;
		e.button = 1;
		display.post(e);

		e.type = SWT.MouseUp;
		display.post(e);
	}

	public static void main(String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Text hint = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		hint.setText(
			"1) Without patch, you won't even see this text, because JVM terminates with SIGTRAP\n" +
			"2) There will also be console errors:\n" +
			"   Gdk-WARNING **: losing last reference to undestroyed window\n" +
			"   Gdk-ERROR **: attempted to destroy root window"
		);

		// Before the patch, it will crash here, because there's no
		// focused window at this time
		sendKey(display, 'a');

		final Text text = new Text(shell, 0);
		text.setFocus();

		Button btnSendKey = new Button(shell, 0);
		btnSendKey.setText("Send 'x' to Text");
		btnSendKey.addListener(SWT.Selection, event -> {
			text.setFocus();
			sendKey(display, 'x');
		});

		Button btnSendMouse = new Button(shell, 0);
		btnSendMouse.setText("Emulate mouse click on hint text");
		btnSendMouse.addListener(SWT.Selection, event -> {
			Point point = hint.toDisplay(20, 20);
			sendMouse(display, point);
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
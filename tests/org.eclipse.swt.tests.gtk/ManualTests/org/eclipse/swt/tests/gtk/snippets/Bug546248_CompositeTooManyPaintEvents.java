/*******************************************************************************
 * Copyright (c) 2019 Thomas Singer and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug546248_CompositeTooManyPaintEvents {

	public static void main(String[] args) {
		final Display display = new Display();

		final StringBuilder buffer = new StringBuilder();

		final int period = 250;
		display.timerExec(period, new Runnable() {
			@Override
			public void run() {
				if (buffer.length() > 0) {
					System.out.println(DateFormat.getTimeInstance().format(new Date()) + ": " + buffer);
					buffer.setLength(0);
				}
				if (!display.isDisposed()) {
					display.timerExec(period, this);
				}
			}
		});

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Composite composite = new Composite(shell, SWT.DOUBLE_BUFFERED | SWT.V_SCROLL);
		final Listener listener = event -> {
			if (event.type == SWT.Paint) {
				buffer.append('p');
				event.gc.drawText("Hello world", 0, 0);
			}
			else if (event.type == SWT.MouseMove) {
				buffer.append('m');
			}
			else if (event.type == SWT.MouseHover) {
				buffer.append('h');
			}
		};
		composite.addListener(SWT.Paint, listener);
		composite.addListener(SWT.MouseMove, listener);
		composite.addListener(SWT.MouseHover, listener);

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

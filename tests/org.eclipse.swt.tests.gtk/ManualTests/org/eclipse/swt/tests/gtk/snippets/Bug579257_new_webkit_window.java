/*******************************************************************************
 * Copyright (c) 2022 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.VisibilityWindowAdapter;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug579257_new_webkit_window {

	public static void main(String[] args) {
		String webPageHtml = "<html><head></head><body>"
				+ "<a href=\"https://www.eclipse.org\" target=\"new\">link with target property set to \"new\"</a>"
				+ "</body></html>";
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(600, 400);
		shell.setLayout(new FillLayout());
		shell.setText("Bug579257_new_webkit_window");

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());

		Browser b = new Browser(composite, SWT.NONE);
		b.setText(webPageHtml);
		b.addOpenWindowListener(event -> {
			Shell newWindowShell = new Shell(shell, SWT.SHELL_TRIM);
			newWindowShell.setLayout(new FillLayout());
			Browser newWindowBrowser = new Browser(newWindowShell, SWT.NONE);
			newWindowBrowser.addVisibilityWindowListener(new VisibilityWindowAdapter() {
				@Override
				public void show(WindowEvent e) {
					newWindowShell.setSize(400, 200);
					newWindowShell.open();
				}
			});
			event.browser = newWindowBrowser;
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
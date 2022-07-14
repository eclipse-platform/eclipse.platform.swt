/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Browser snippet: bring up a browser with pop-up blocker
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet173 {

static final int BROWSER_STYLE = SWT.NONE;

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Main Window");
	shell.setLayout(new FillLayout());
	final Browser browser;
	try {
		browser = new Browser(shell, BROWSER_STYLE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	initialize(display, browser);
	shell.open();
	/* any website with popups */
	browser.setUrl("http://www.popuptest.com/");
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
		}
		display.dispose();
	}

/* register WindowEvent listeners */
static void initialize(final Display display, Browser browser) {
	browser.addOpenWindowListener(event -> {
		Shell shell = new Shell(display);
		shell.setText("New Window");
		shell.setLayout(new FillLayout());
		Browser browser1 = new Browser(shell, BROWSER_STYLE);
		initialize(display, browser1);
		event.browser = browser1;
	});
	browser.addVisibilityWindowListener(new VisibilityWindowListener() {
		@Override
		public void hide(WindowEvent event) {
			Browser browser = (Browser)event.widget;
			Shell shell = browser.getShell();
			shell.setVisible(false);
		}
		@Override
		public void show(WindowEvent event) {
			Browser browser = (Browser)event.widget;
			final Shell shell = browser.getShell();
			/* popup blocker - ignore windows with no style */
			boolean isOSX = SWT.getPlatform().equals ("cocoa");
			if (!event.addressBar && !event.statusBar && !event.toolBar && (!event.menuBar || isOSX)) {
				System.out.println("Popup blocked.");
				event.display.asyncExec(shell::close);
				return;
			}
			if (event.location != null) shell.setLocation(event.location);
			if (event.size != null) {
				Point size = event.size;
				shell.setSize(shell.computeSize(size.x, size.y));
			}
			shell.open();
		}
	});
	browser.addCloseWindowListener(event -> {
		Browser browser1 = (Browser)event.widget;
		Shell shell = browser1.getShell();
		shell.close();
	});
}
}
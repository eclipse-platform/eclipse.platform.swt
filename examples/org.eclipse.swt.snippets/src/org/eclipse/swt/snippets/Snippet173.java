/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;

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
	browser.setUrl("http://www.cnn.com");
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
		}
		display.dispose();
	}

/* register WindowEvent listeners */
static void initialize(final Display display, Browser browser) {
	browser.addOpenWindowListener(new OpenWindowListener() {
		public void open(WindowEvent event) {
			Shell shell = new Shell(display);
			shell.setText("New Window");
			shell.setLayout(new FillLayout());
			Browser browser = new Browser(shell, BROWSER_STYLE);
			initialize(display, browser);
			event.browser = browser;
		}
	});
	browser.addVisibilityWindowListener(new VisibilityWindowListener() {
		public void hide(WindowEvent event) {
			Browser browser = (Browser)event.widget;
			Shell shell = browser.getShell();
			shell.setVisible(false);
		}
		public void show(WindowEvent event) {
			Browser browser = (Browser)event.widget;
			final Shell shell = browser.getShell();
			/* popup blocker - ignore windows with no style */
			if (!event.addressBar && !event.menuBar && !event.statusBar && !event.toolBar) {
				System.out.println("Popup blocked.");
				event.display.asyncExec(new Runnable() {
					public void run() {
						shell.close();
					}
				});
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
	browser.addCloseWindowListener(new CloseWindowListener() {
		public void close(WindowEvent event) {
			Browser browser = (Browser)event.widget;
			Shell shell = browser.getShell();
			shell.close();
		}
	});
}
}
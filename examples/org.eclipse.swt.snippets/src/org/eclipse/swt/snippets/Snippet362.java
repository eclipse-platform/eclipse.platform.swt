/*******************************************************************************
 * Copyright (c) 2012, 2017 IBM Corporation and others.
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
 * Browser example snippet: listen for DOM mousedown events with javascript (improved
 * implementation for Eclipse/SWT 3.5 and newer).
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet362 {

public static void main(String [] args) {
	final String SCRIPT = "document.onmousedown = function(e) {if (!e) {e = window.event;} if (e) {mouseDownHappened(e.clientX, e.clientY);}}";
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	browser.addProgressListener(ProgressListener.completedAdapter(event -> {
		final BrowserFunction function = new CustomFunction(browser, "mouseDownHappened");
		browser.execute(SCRIPT);
		browser.addLocationListener(new LocationAdapter() {
			@Override
			public void changed(LocationEvent event) {
				browser.removeLocationListener(this);
				function.dispose();
			}
		});
	}));

	browser.setUrl("eclipse.org");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

static class CustomFunction extends BrowserFunction {
	CustomFunction (Browser browser, String name) {
		super (browser, name);
	}
	@Override
	public Object function (Object[] arguments) {
		System.out.println ("mouseDown: " + ((Number)arguments[0]).intValue() + "," + ((Number)arguments[1]).intValue());
		return null;
	}
}
}

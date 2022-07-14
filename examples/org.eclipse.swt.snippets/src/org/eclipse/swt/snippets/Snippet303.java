/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 * Browser example snippet: listen for DOM mousedown events with javascript
 * (Eclipse/SWT 3.4 and earlier).  For an improved implementation of this
 * snippet that can be used with Eclipse/SWT 3.5 and newer see
 * https://github.com/eclipse-platform/eclipse.platform.swt/tree/master/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet362.java .
 *
 * This snippet works for all supported browser types (ie.- Browsers created with style
 * SWT.NONE).
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet303 {

public static void main(String [] args) {
	final String SCRIPT = "document.onmousedown = function(e) {if (!e) {e = window.event;} if (e) {window.status = 'MOUSEDOWN: ' + e.clientX + ',' + e.clientY;}}";
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Snippet 303");
	shell.setLayout(new FillLayout());
	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	browser.addProgressListener(ProgressListener.completedAdapter(event ->	browser.execute(SCRIPT)));
	browser.addStatusTextListener(event -> {
		if (event.text.startsWith("MOUSEDOWN: ")) {
			System.out.println(event.text);
			browser.execute("window.status = '';");
		}
	});
	browser.setUrl("eclipse.org");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * Browser example snippet: listen for DOM mousedown events with javascript
 *
 * This snippet works for all supported browser types (ie.- Browsers created with style
 * SWT.NONE).  For a Browser created with style SWT.MOZILLA the cleaner approach would
 * be to use JavaXPCOM to hook the DOM listeners. 
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
	shell.setLayout(new FillLayout());
	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	browser.addProgressListener(new ProgressAdapter() {
		public void completed(ProgressEvent event) {
			browser.execute(SCRIPT);
		}
	});
	browser.addStatusTextListener(new StatusTextListener() {
		public void changed(StatusTextEvent event) {
			if (event.text.startsWith("MOUSEDOWN: ")) {
				System.out.println(event.text);
				browser.execute("window.status = '';");
			}
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

/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * Browser example snippet: send custom headers and post data with HTTP requests
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;

public class Snippet330 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(2, true));

	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	GridData data = new GridData(GridData.FILL_BOTH);
	data.horizontalSpan = 2;
	browser.setLayoutData(data);

	Button headersButton = new Button(shell, SWT.PUSH);
	headersButton.setText("Send custom headers");
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	headersButton.setLayoutData(data);
	headersButton.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			browser.setUrl(
				"http://www.httpviewer.net",
				null,
				new String[] {"User-agent: SWT Browser","Custom-header: this is just a demo"});
		}
	});
	Button postDataButton = new Button(shell, SWT.PUSH);
	postDataButton.setText("Send post data");
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	postDataButton.setLayoutData(data);
	postDataButton.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			browser.setUrl(
				"https://bugs.eclipse.org/bugs/buglist.cgi",
				"emailassigned_to1=1&bug_severity=enhancement&bug_status=NEW&email1=platform-swt-inbox&emailtype1=substring",
				null);
		}
	});

	shell.setBounds(10,10,600,600);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
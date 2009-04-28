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
 * Browser example snippet: provide credentials for a basic authentication challenge
 * 
 * Note that the KNOWN_HOST, KNOWN_USER and KNOWN_PASSWORD fields in the snippet
 * below require valid values in order to fully demonstrate the functionality.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */
import java.net.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;

public class Snippet317 {

static String KNOWN_HOST = "www.eclipse.org";
static String KNOWN_USER = "anonymous";
static String KNOWN_PASSWORD = "anonymousPassword";

public static void main(String [] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 2;
	shell.setLayout(gridLayout);
	final Text location = new Text(shell, SWT.BORDER);
	GridData data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	location.setLayoutData(data);
	Button go = new Button(shell, SWT.PUSH);
	go.setText("Go");

	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	data = new GridData();
	data.horizontalAlignment = data.verticalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = data.grabExcessVerticalSpace = true;
	data.horizontalSpan = 2;
	browser.setLayoutData(data);
	browser.setUrl ("eclipse.org");
	browser.addLocationListener(new LocationAdapter() {
		public void changed(LocationEvent event) {
			location.setText(event.location);
		}
	});

	Listener navigateListener = new Listener() {
		public void handleEvent(Event event) {
			browser.setUrl(location.getText());
		}
	};
	go.addListener(SWT.Selection, navigateListener);
	location.addListener(SWT.DefaultSelection, navigateListener);

	browser.addAuthenticationListener(new AuthenticationListener(){
		public void authenticate(AuthenticationEvent event) {
			try {
				URL url = new URL(event.location);
				if (url.getHost().equals(KNOWN_HOST)) {
					event.user = KNOWN_USER;
					event.password = KNOWN_PASSWORD;
				} else {
					/* do nothing, let default prompter run */
				}
			} catch (MalformedURLException e) {
				/* should not happen, let default prompter run */
			}
		}
	});

	shell.setBounds(10,10,500,500);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}

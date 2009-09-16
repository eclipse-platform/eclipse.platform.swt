/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * Browser example snippet: Provide a page from memory in response to a link click.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet327 {

	final static String PREAMBLE = "SNIPPET327LINK";

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());

	final Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	browser.setText(createPage(0));
    browser.addLocationListener(new LocationAdapter() {
	    public void changing(LocationEvent event) {
			String location = event.location;
			int index = location.indexOf(PREAMBLE); 
			if (index != -1) {
				int pageNumber = Integer.valueOf(location.substring(index + PREAMBLE.length())).intValue();
				browser.setText(createPage(pageNumber));
				event.doit = false;
			}
	    }
	});

	shell.setBounds(10,10,200,200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose ();
}

static String createPage(int index) {
	return "<html><body>This is page " + index + "<p><a href=\"#" + PREAMBLE + ++index + "\">Go to page " + index + "</a></body></html>";
}

}

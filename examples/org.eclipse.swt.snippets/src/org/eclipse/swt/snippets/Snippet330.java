/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 * Browser example snippet: Send custom headers and post data with HTTP requests
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet330 {

	static int BUTTONS_PER_ROW = 1;

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(BUTTONS_PER_ROW, true));

		final Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = BUTTONS_PER_ROW;
		browser.setLayoutData(data);

		{
			Button setHeaders = new Button(shell, SWT.PUSH);
			setHeaders.setText("Send custom headers");
			setHeaders.addListener(SWT.Selection,event ->
				browser.setUrl("http://www.ericgiguere.com/tools/http-header-viewer.html", null,
							new String[] { "User-agent: SWT Browser", "Custom-header: this is just a demo" }));
		}

		{
			Button postTextPlain = new Button(shell, SWT.PUSH);
			postTextPlain.setText("Post data as 'text/plain'");
			postTextPlain.addListener(SWT.Selection, event -> browser.setUrl("http://httpbin.org/post",
					"Plain text passed as postData", new String[] { "content-type: text/plain" }));
		}

		{
			Button postTextPlainUtf8 = new Button(shell, SWT.PUSH);
			postTextPlainUtf8.setText("Post data as 'text/plain' and specify encoding.");
			postTextPlainUtf8.addListener(SWT.Selection, event -> browser.setUrl("http://httpbin.org/post",
					"Plain text passed as postData", new String[] { "content-type: text/plain; charset=UTF-8" }));
		}

		{
			Button postUrlEncoded = new Button(shell, SWT.PUSH);
			postUrlEncoded.setText("Post data with url encoding key=value");
			postUrlEncoded.addListener(SWT.Selection, event -> browser.setUrl("http://httpbin.org/post", "MyKey1=MyValue1&MyKey2=MyValue2",
					new String[] { "content-type: application/x-www-form-urlencoded" }));
		}

		{
			Button postDataBugzilla = new Button(shell, SWT.PUSH);
			postDataBugzilla.setText("Send post data to bugzilla. url encoding is used as default");
			postDataBugzilla.addListener(SWT.Selection, event -> browser.setUrl(
					"https://bugs.eclipse.org/bugs/buglist.cgi",
					"emailassigned_to1=1&bug_severity=enhancement&bug_status=NEW&email1=platform-swt-inbox&emailtype1=substring",
					null));
		}

		{
			Button postDataBugzillaLongQuery = new Button(shell, SWT.PUSH);
			postDataBugzillaLongQuery.setText("Send post data to bugzilla. Very slow response");
			postDataBugzillaLongQuery.addListener(SWT.Selection, event -> browser.setUrl(
					"https://bugs.eclipse.org/bugs/buglist.cgi",
					"emailassigned_to1=1&bug_severity=enhancement&bug_status=NEW",
					null));
		}

		shell.setSize(1000, 1000);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
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
 * Browser example snippet: modify HTML title tag
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet159 {
	public static void main(String [] args) {
		final String newTitle = "New Value for Title";
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
		browser.addTitleListener(event -> {
			System.out.println("TitleEvent: "+event.title);
			shell.setText(event.title);
		});
		browser.addProgressListener(ProgressListener.completedAdapter(event -> {
			/* Set HTML title tag using JavaScript and DOM when page has been loaded */
			boolean result = browser.execute("document.title='" + newTitle + "'");
			if (!result) {
				/* Script may fail or may not be supported on certain platforms. */
				System.out.println("Script was not executed.");
			}
		}));
		/* Load an HTML document */
		browser.setUrl("http://www.eclipse.org");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}



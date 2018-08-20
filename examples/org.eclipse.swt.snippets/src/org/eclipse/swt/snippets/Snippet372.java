/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lucas Bullen (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Browser: Resize the shell to the fit the html content after it's loaded.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 4.8
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet372 {
	static Double width = null;
	public static void main(String [] args) {
		int maximumWidth = 200;
		int maximumHeight= 2000;

		String html = "<HTML><HEAD><TITLE>HTML Test</TITLE></HEAD><BODY>";
		for (int i = 0; i < 15; i++) html += "<P>This is line "+i+"</P>";
		html += "</BODY></HTML>";

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		browser.setText(html);
		browser.addProgressListener(ProgressListener.completedAdapter(event -> {
			// Set the display to something known to be smaller than the content
			shell.setSize(50, 50);
			browser.execute("document.getElementsByTagName(\"html\")[0].style.whiteSpace = \"nowrap\""); //$NON-NLS-1$
			// Save the width to either be a decided maximum or the browser's content width plus the margin
			Double width = Math.min(maximumWidth, 10 + (Double) browser.evaluate("return document.body.scrollWidth;")); //$NON-NLS-1$
			shell.setSize(width.intValue(), 0);
			browser.execute("document.getElementsByTagName(\"html\")[0].style.whiteSpace = \"normal\""); //$NON-NLS-1$
			shell.layout();
			// Set the height to either be a decided maximum or the browser's content height plus the margin
			Double height = Math.min(maximumHeight, 5 + (Double) browser.evaluate("return document.body.scrollHeight;")); //$NON-NLS-1$
			shell.setSize(width.intValue(), height.intValue());
		}));

		shell.open();
		while (!shell.isDisposed()) {
			shell.layout();
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

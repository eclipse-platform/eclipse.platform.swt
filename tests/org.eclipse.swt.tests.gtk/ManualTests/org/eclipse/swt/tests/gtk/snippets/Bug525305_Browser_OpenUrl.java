/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Handle files or URLs from eclipse launcher or gdbus.
 * How to run:
 * - Launch snippet.
 * - In terminal, run like:
 * 	 gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.FileOpen "['/tmp/hi','http://www.eclipse.org']"
 * - Expect output:
 *    "OpenUrl with .. "
 *    "OpenDocument with .. "
 * Bug description:
 * Expected results: Browser opens URLs, filenames printed.
 * GTK Version(s): 3.22/2.24
 */
public class Bug525305_Browser_OpenUrl {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 600);
		shell.setLayout(new FillLayout());

		final Browser browser = new Browser(shell, SWT.NONE);
		browser.setText("hello <b>world!</b>");

		display.addListener(SWT.OpenUrl, e -> {
			System.out.println("OpenUrl with:" + e.text);
			browser.setUrl(e.text);
		});

		display.addListener(SWT.OpenDocument, e -> {
			System.out.println("OpenDocument with: " + e.text);
			browser.setText("OpenDocument was called with arg: " + e.text);
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

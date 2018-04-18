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
 * Title: Bug 459462 - [WebKit] Implement Download functionality
 * How to run: launch snippet
 * Bug description: Download functionality doesn't work on WebKit2.
 * Expected results: Download functionality now works on WebKit2.
 * GTK version(s): GTK3.x
 */
public class Bug525946_DownloadFunction {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setBounds(10, 10, 400, 400);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
//		browser.setUrl("http://download.eclipse.org/eclipse/downloads/drops4/I20180416-2000/download.php?dropFile=eclipse-test-framework-I20180416-2000.zip"); // 2.3 mb
		browser.setUrl("http://download.eclipse.org/tools/orbit/downloads/drops/R20170516192513/orbit-buildrepo-R20170516192513.zip"); // 400mb
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
		display.dispose();
	}
}
/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
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
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Bug531634_LogoffListener {
	private static boolean isUserClosed = false;

	public static void main (String [] args) {
		Display display = new Display ();

		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		shell.setSize(300, 200);

		display.addListener(SWT.Close, event -> {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setMessage("QueryEndSession received.\nGive 'ready' hint? Time limit to answer is 1 sec");
			int answer = dialog.open();
			event.doit = (SWT.YES == answer);
		});

		display.addListener(SWT.Dispose, event -> {
			if (!isUserClosed && !shell.isDisposed()) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION);
				dialog.setMessage("EndSession received.\nI will exit when you close this box. Time limit is 10 sec.");
				dialog.open();
			}
		});

		final Label label = new Label(shell, SWT.WRAP | SWT.CENTER);
		label.setText("\n\n\nWhen you logoff/shutdown, I will give you messages on 'QueryEndSession' and 'EndSession'");

		// The first version of the patch had deadlock when System.exit() was called in response to UI action.
		// This happened because 'SWT.Close' is called from inside 'OS.g_main_context_iteration' and
		// Platform.lock is held by main thread, locking all OS.xxx calls from other threads.
		shell.addListener(SWT.Close, event -> {
			isUserClosed = true;
			display.dispose();
			System.exit(0);
		});

		shell.open ();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

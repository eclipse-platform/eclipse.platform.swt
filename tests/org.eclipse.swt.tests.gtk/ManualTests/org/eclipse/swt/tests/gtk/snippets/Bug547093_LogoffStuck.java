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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug547093_LogoffStuck {
	public static void main (String [] args) {
		Display display = new Display ();

		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		shell.setSize(300, 200);

		display.addListener(SWT.Dispose, event -> {
			/*
			 * System.exit() prevents org.eclipse.swt.internal.SessionManagerDBus
			 * from sending reply to session manager and it gets stuck waiting for
			 * that reply.
			 */
			System.exit(0);
		});

		final Label label = new Label(shell, SWT.WRAP | SWT.CENTER);
		label.setText("\n\n\nWhen you logoff, GNOME session manager will get stuck for 90 seconds");

		// Test for deadlock with shutdown hook on regular closing
		shell.addListener(SWT.Close, event -> {
			System.exit(0);
		});

		shell.open ();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

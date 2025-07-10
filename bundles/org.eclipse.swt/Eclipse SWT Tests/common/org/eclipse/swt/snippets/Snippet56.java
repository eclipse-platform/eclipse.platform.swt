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
 * ProgressBar example snippet: update a progress bar (from another thread)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet56 {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 56");
//		var bar = new ProgressBar_Old(shell, SWT.INDETERMINATE);
		var bar = new ProgressBar(shell, SWT.SMOOTH | SWT.VERTICAL | SWT.INDETERMINATE);

		bar.setState(SWT.ERROR);
		Rectangle ca = shell.getClientArea();
		bar.setBounds(ca.x, ca.y, 200, 32);
		bar.setMaximum(100);
		shell.open();
		final int maximum = bar.getMaximum();

		Button b = new Button(shell, SWT.NONE);
		b.setText("Text");
		b.setBounds(ca.x, ca.y + 40, 200, 30);

		new Thread() {
			@Override
			public void run() {
				for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
				try {Thread.sleep (100);} catch (Throwable th) {}
					if (display.isDisposed()) return;
					display.asyncExec(() -> {
					if (bar.isDisposed ()) return;
						bar.setSelection(i[0]);
					});
				}
			}
		}.start();
		while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	}
}
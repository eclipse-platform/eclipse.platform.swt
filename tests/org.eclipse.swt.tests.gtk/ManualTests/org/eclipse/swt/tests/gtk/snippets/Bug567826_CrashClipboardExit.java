/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Bug567826_CrashClipboardExit {
	public static void main (String [] args) {
		Display display = new Display ();

		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;

		Shell shell = new Shell(display);
		shell.setLayout(layout);

		Label hint = new Label(shell, SWT.NONE);
		hint.setText(
			"1) Close this Shell\n" +
			"2) There will be a 10 sec Thread.sleep()\n" +
			"3) During the sleep, switch to other app and copy some text to clipboard\n" +
			"4) JVM will crash.\n"
		);

		Clipboard clipboard = new Clipboard(display);
		Object[] data = new Object[]{"Test clipboard contents"};
		Transfer[] types = new Transfer[]{TextTransfer.getInstance()};
		clipboard.setContents(data, types, DND.CLIPBOARD);

		final String clipboardProxyID = "CLIPBOARD PROXY OBJECT";
		if (null == display.getData(clipboardProxyID))
			System.out.println("Unexpected: ClipboardProxy is not present");

		display.addListener(SWT.Dispose, event -> {
			System.out.println("SWT.Dispose for Display: waiting");
			if (null != display.getData(clipboardProxyID))
				System.out.println("ClipboardProxy is not disposed in SWT.Dispose. Shouldn't happen without the patch.");

			// Give time to switch to other window
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			System.out.println("SWT.Dispose for Display: continuing");
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}

		display.dispose ();
	}
}

/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug571740_IAE_AccentChars {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Label hint = new Label(shell, 0);
		hint.setText(
			"1) Use GTK 3.24.26 or higher\n" +
			"2) Do not use GNOME, because SWT forces 'ibus' IM there\n" +
			"3) Do not have any IM method configured, so that GTK's default is used\n" +
			"4) Install Spanish keyboard layout\n" +
			"5) Switch to Spanish keyboard layout\n" +
			"6) In StyledText below, type ' key according to US layout\n" +
			"7) Before the patch, SWT will throw IAE\n" +
			"8) After the patch or with XFCE, ´ character will (correctly) appear\n" +
			"9) After the patch or with XFCE, if you now type A, ´ will (correctly) be replaced with á"
		);

		new StyledText(shell, SWT.BORDER);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}

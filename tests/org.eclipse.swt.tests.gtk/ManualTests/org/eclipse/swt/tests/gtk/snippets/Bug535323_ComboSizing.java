/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug535323_ComboSizing {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Rectangle clientArea = shell.getClientArea();
		Combo combo1 = new Combo(shell, SWT.BORDER);
		combo1.setItems("Alpha", "Bravo", "Charlie");
		Point prefSize = combo1.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		combo1.setBounds(clientArea.x, clientArea.y, prefSize.x, prefSize.y);
		Combo combo2 = new Combo(shell, SWT.BORDER);
		combo2.setItems("Alpha", "Bravo", "Charlie");
		combo2.setBounds(clientArea.x, clientArea.y, prefSize.x, prefSize.y);
		combo2.setBounds(clientArea.x, clientArea.y + prefSize.y, (prefSize.x / 2) - 1, prefSize.y);
		Combo combo3 = new Combo(shell, SWT.BORDER);
		combo3.setItems("Alpha", "Bravo", "Charlie");
		combo3.setBounds(clientArea.x, clientArea.y + prefSize.y * 2, (prefSize.x / 2) + 1, prefSize.y);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug517214_ComboReadOnlyWrongText {
	public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Combo combo = new Combo (shell, SWT.READ_ONLY);
	combo.setItems ("Alpha");
	Rectangle clientArea = shell.getClientArea ();
	combo.setBounds (clientArea.x, clientArea.y, 200, 200);
	MouseAdapter clickListener = new MouseAdapter() {
		@Override
		public void mouseDown(MouseEvent e) {
			System.out.println("hello");
		}
	};
	shell.addMouseListener(clickListener);
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}

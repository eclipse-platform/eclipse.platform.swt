/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * Canvas snippet: ignore 2nd mouse up event after double-click
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class Snippet290 {

public static void main(String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseUp(MouseEvent e) {
			if (e.count == 1) {
				System.out.println("Mouse up");
			}
		}
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			System.out.println("Double-click");
		}
	});
	shell.setBounds(10, 10, 200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}

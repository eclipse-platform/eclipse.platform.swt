/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug436085_TextEntryTabs {

	public static void main (String [] args) {
		  Display display = new Display ();
		  Shell shell = new Shell(display);
		  Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		  text.setBounds (10, 10, 1000, 60);
		  text.setTabs(1);
		  text.append("1\t12\t123\t1234\t12345\t123456\t1234567\t12345678\t12345678");
		  shell.open ();
		  while (!shell.isDisposed()) {
		     if (!display.readAndDispatch()) display.sleep();
		   }
		  display.dispose();
}
}

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug542475_DateTimeWrongIncrement {
	 public static void main(String args[]) {

	        Display display = Display.getDefault();
	        Shell shell = new Shell(display, SWT.SHELL_TRIM);

	        shell.setLayout(new GridLayout());

	        new DateTime(shell, SWT.DATE | SWT.SHORT);

	        new DateTime(shell, SWT.DATE | SWT.MEDIUM);

	        new DateTime(shell, SWT.DATE | SWT.LONG);

	        shell.pack();
	        shell.open();

	        while (!shell.isDisposed()) {
	            if (!display.readAndDispatch()) {
	                display.sleep();
	            }
	        }
	    }
}
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Bug 486068: [GTK3.20+] Allocation warnings printed in error console
 * How to run: launch snippet and check console for warnings
 * Bug description: GTK assertion warnings all over the place for GtkScrollbar
 * Expected results: clean console output, no warnings
 * GTK Version(s): GTK3.20+
 */
public class Bug486068_AllocationWarningsScrollBar {

	public static void main (String [] args) {
			Display display = new Display ();
			Shell shell = new Shell (display);
			Text text = new Text (shell, SWT.V_SCROLL);
			text.setBounds(0, 0, 200, 30);
			shell.pack ();
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			display.dispose ();
	}
}
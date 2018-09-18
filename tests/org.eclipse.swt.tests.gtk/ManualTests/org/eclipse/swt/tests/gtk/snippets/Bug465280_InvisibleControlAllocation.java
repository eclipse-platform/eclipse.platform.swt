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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 465280 - [GTK3] OS.gtk_widget_get_allocation returns (0,0) for invisible controls
 * How to run: launch snippet and observe coordinates printed in console
 * Bug description: The coordinates are (0,0)
 * Expected results: The coordinates should not be (0,0) if the widget is hidden.
 * GTK Version(s): GTK3.8+
 */
public class Bug465280_InvisibleControlAllocation {
	public static void main(String[] args) {
        Display display = new Display ();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Label descriptionHint = new Label(shell, SWT.WRAP);
        descriptionHint.setText("This is a very very very very very very long string");
        descriptionHint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        descriptionHint.setVisible(false);
        shell.open();
        System.err.println(descriptionHint.getSize());
        while (!shell.isDisposed ()) {
                if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
}

}

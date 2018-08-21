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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug319612_getSizeResizeOnTop {

public static void main (String [] args) {
    Display display = new Display ();
    Shell shell = new Shell (display, SWT.RESIZE | SWT.ON_TOP);

    GridLayout layout= new GridLayout(1, false);
    layout.marginHeight= 0;
    layout.marginWidth= 0;
    shell.setLayout(layout);

    shell.setSize (200, 200);
    shell.open ();

    for (int i = 0; i < 50; i++) {
       while (display.readAndDispatch ()) {

       }
        Point p = shell.getSize();
        shell.setSize(p.x, p.y);

        try {
            Thread.currentThread();
			Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    while (!shell.isDisposed()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}

}
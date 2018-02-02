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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 483791 - [GTK3] setBackground overrides GC drawing in PaintListener
 * How to run: launch snippet and press button "CLICK"
 * Bug description: Label will have Cyan background but no line drawn through it
 * Expected results: Label should draw line through it regardless of bg color
 * GTK Version(s): 3.10+
 */
public final class Bug483791_setBackgroundGC {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        final Label l = new Label(shell, SWT.None);
        l.setText("ASDQWE");
        l.addPaintListener(arg0 -> arg0.gc.drawLine(0, 0, arg0.width, arg0.height));

        final Button b = new Button(shell, SWT.PUSH);
        b.setText("CLICK");
        b.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                l.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
                // these don't help
                /*
                l.redraw();
                l.update();
                 */
                MessageBox mb = new MessageBox(shell);
                mb.setMessage("Background should not override GC drawing, but it does");
                mb.open();
            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private Bug483791_setBackgroundGC() {
    }
}
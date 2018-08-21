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


import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

public class Bug286244_ShellActivateLoop {

    public static void main(String[] args) {
        final Display display= new Display();
        final Shell shell= new Shell(display);
        shell.addShellListener(new ShellAdapter() {
            @Override
			public void shellDeactivated(ShellEvent e) {
                System.out.println("shellDeactivated()");
                display.timerExec(2000, new Runnable() {
                    @Override
					public void run() {
                        System.out.println("forcing active");
                        shell.forceActive();
                        System.out.println("widget.shell is active: "
                                + (display.getActiveShell() == shell));
                        System.out.println("forced active");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
			public void shellActivated(ShellEvent e) {
                System.out.println("shellActivated()");
            }
        });

        shell.setSize(160, 100);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
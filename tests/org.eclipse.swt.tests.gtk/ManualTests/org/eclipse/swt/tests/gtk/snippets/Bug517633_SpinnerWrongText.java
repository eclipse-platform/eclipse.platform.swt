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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class Bug517633_SpinnerWrongText {
    public static void main(String[] args) {
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        Label info = new Label(shell, SWT.NONE);
        info.setText("Select '1' in spinner text and type '2'");
        info.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        Spinner spin = new Spinner(shell, SWT.BORDER);
        spin.setValues(1000, 0, 10000, 0, 1, 10);
        spin.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        spin.addListener(SWT.Selection, e -> info.setText("Value changed: " + spin.getSelection()));
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

}

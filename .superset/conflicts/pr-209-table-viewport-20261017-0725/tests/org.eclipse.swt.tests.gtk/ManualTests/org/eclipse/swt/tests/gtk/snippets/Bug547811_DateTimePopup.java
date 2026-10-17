/*******************************************************************************
 * Copyright (c) 2019  Ilnur Zalyaev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Ilnur Zalyaev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug547811_DateTimePopup {

    public static void main(String[] args) {
        Display d = Display.getDefault();
        Shell sh = new Shell(d);
        sh.setSize(500, 400);
        sh.setLayout(new GridLayout(2, true));

        Button btn = new Button(sh, SWT.PUSH);
        btn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        btn.setText("Open sub shell");
        btn.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            Shell ssh = new Shell(sh, SWT.ON_TOP);
            ssh.setLayout(new GridLayout(2, true));
            new DateTime(ssh, SWT.DATE | SWT.DROP_DOWN);
            new DateTime(ssh, SWT.DATE | SWT.DROP_DOWN);
            ssh.setSize(500, 400);
            ssh.setLocation(600, 100);
            ssh.open();
        }));

        new DateTime(sh, SWT.DATE | SWT.DROP_DOWN);
        new DateTime(sh, SWT.DATE | SWT.DROP_DOWN);

        sh.open();
        while (!sh.isDisposed()) {
            if (!d.readAndDispatch()) {
                d.sleep();
            }
        }
    }

}
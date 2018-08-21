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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug150921_TextCaretPosition {
    
    public static void main(String [] args) {
       
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        Text single = new Text(shell, SWT.SINGLE | SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 20;
        gd.widthHint = 300;
        single.setLayoutData(gd);
        single.addModifyListener(new TestListener());
        Text multi = new Text(shell, SWT.MULTI | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 100;
        gd.widthHint = 300;
        multi.setLayoutData(gd);
        multi.addModifyListener(new TestListener());
        shell.open();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }
    
    private static class TestListener implements ModifyListener {

        @Override
		public void modifyText(ModifyEvent e) {
            Text text = (Text) e.widget;
            System.out.println("Text: \"" + text.getText() +"\"");
            System.out.println("Caret Position:" + text.getCaretPosition());
        }
       
    }

}
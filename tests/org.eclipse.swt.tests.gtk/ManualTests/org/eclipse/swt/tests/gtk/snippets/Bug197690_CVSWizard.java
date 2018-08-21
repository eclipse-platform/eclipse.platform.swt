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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug197690_CVSWizard {
	public static void main(String[] args) {
	    final Display display = new Display();
	    final Shell shell1 = new Shell(display);
	    shell1.setLayout(new FillLayout());
	    shell1.setBounds(10, 10, 300, 300);
	    final DateTime dt = new DateTime(shell1, SWT.DATE);
	    dt.addSelectionListener(new SelectionAdapter(){
	        @Override
			public void widgetSelected(SelectionEvent e) {
	            System.out.println(dt.getMonth());
	            System.out.println(dt.getDay());
	            System.out.println(dt.getYear());
	        }
	    });
	    shell1.open();
	    while (!shell1.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    display.dispose();
	}
}

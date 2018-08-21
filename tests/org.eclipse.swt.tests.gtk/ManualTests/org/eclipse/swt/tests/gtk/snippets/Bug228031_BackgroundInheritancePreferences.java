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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class Bug228031_BackgroundInheritancePreferences {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		TabFolder parent = new TabFolder(shell, SWT.NONE);
		
		Composite c1 = new Composite(parent, SWT.NONE);
		c1.setLayout(new FillLayout());

		// inherits gtk bg graphics.color
//		Composite child = new Composite(c1, SWT.NONE);
		
		new Composite(c1, SWT.H_SCROLL|SWT.V_SCROLL); 
		
		
		TabItem item = new TabItem(parent, SWT.NONE);
		item.setText("tab item");
		item.setControl(c1);
		
		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

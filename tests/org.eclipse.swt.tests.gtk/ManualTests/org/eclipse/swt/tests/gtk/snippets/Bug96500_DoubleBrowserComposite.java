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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug96500_DoubleBrowserComposite {
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		Browser browser;
		
		browser = new Browser (shell, SWT.NONE);
		browser.setBounds (10, 10, 500, 200);
		browser.setText("<html><body><A HREF=\"www\">lala</a> <AHREF=\"www\">lala</a><br><input type=\"widget.text\"  value=''	maxlength=256></body></html>");
		browser.getAccessible();
		browser = new Browser (shell, SWT.NONE);
		browser.setBounds (10, 220, 500, 200);
		browser.setText("<html><body><A HREF=\"www\">lala</a> <A HREF=\"www\">lala</a><br><input type=\"widget.text\"  value='' maxlength=256></body></html>");
		
		Button button= new Button (shell, SWT.PUSH);
		button.setBounds(10, 500, 70, 28);
		button.setText("Button");
		
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}

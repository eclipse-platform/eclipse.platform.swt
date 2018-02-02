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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 221611 - [Accessibility] Radio button being read as check boxes
 * How to run: launch snippet
 * Bug description: Checking button 2, button 3, will cause Orca to read button 3
 * as a checkbox button
 * Expected results: All buttons should be read as radio buttons
 * GTK Version(s): GTK2
 */

public class Bug221611_RadioButtonAccessibility {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setLayout(new GridLayout());

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new GridLayout());

		Button button1 = new Button(parent, SWT.RADIO);
		button1.setText("Button 1");
		Button button2 = new Button(parent, SWT.RADIO);
		button2.setText("Button 2");
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((Button) e.widget).getAccessible();
			}
		});
		Button button3 = new Button(parent, SWT.RADIO);
		button3.setText("Button 3");
		Button button4 = new Button(parent, SWT.RADIO);
		button4.setText("Button 4");

		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}
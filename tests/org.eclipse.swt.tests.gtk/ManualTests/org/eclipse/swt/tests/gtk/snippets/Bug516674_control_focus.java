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


import java.awt.AWTException;
import java.awt.Robot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class Bug516674_control_focus
{
	public static void main(String[] args)
	{
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("TabItem");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		composite.setLayout(new GridLayout());
		Text text = new Text(composite, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		shell.pack();
		shell.setSize(500, 500);
		shell.open();

		tabFolder.setFocus();

		Thread thread = new Thread() {
			@Override
			public void run() {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						text.getShell().forceActive();
						text.getShell().forceFocus();
						text.forceFocus();
					}
				});

				try {
					Thread.sleep(1000);
					Robot robot = new Robot();
					String string = "this is a test".toUpperCase();
					for (int i = 0; i < string.length(); i++) {
						char c = string.charAt(i);
						robot.keyPress(c);
						robot.keyRelease(c);
						Thread.sleep(50);
					}
				} catch (AWTException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
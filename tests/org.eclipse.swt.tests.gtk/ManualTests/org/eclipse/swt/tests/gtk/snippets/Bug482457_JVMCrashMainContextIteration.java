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
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug482457_JVMCrashMainContextIteration {

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Button button = new Button(shell, SWT.PUSH);
		button.setText("buttontext");
		button.setToolTipText("tooltiptext");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (int i = 0; i<3; i++) {
				final Shell s = new Shell(display, SWT.TOOL);
				s.addShellListener(new ShellListener() {

					@Override
					public void shellIconified(ShellEvent e) {}

					@Override
					public void shellDeiconified(ShellEvent e) {}

					

					@Override
					public void shellDeactivated(ShellEvent e) {
						if (s != null && !s.isDisposed()) {
							s.close();
						}
					}

					@Override
					public void shellClosed(ShellEvent e) {}

					@Override
					public void shellActivated(ShellEvent e) {}
				});

				Composite c = new Composite(s, SWT.NONE);
				c.setLayout(new GridLayout(1, true));
			
				Label l = new Label(c, SWT.NONE);
				
				l.setText("this is a widget.label");
				c.pack();
				
				
				s.layout();
				s.pack();
				s.setLocation(250+i*20, 100+1*20);
				s.open();
				}
			}

		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

}
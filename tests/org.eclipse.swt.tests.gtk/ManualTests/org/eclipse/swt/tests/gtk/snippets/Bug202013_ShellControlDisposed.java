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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug202013_ShellControlDisposed {

	public static void main(String[] args) {
		Display display = new Display();
		final Listener listener = event -> System.out.println(event);
		display.addFilter(SWT.KeyDown, listener);
		display.addFilter(SWT.Traverse, listener);
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setSize(300, 75);

		final Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new FillLayout());

		Button b = new Button(c, SWT.PUSH);
		b.addListener(SWT.Selection, event -> c.dispose());

		shell.open();

		while (!shell.isDisposed()) {
			if (display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}

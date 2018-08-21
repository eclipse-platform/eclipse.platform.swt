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


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * @author Thomas Singer
 */
public class Bug490280_BackgroundInheritanceTest {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Composite panel = new Composite(shell, SWT.NO_RADIO_GROUP);
		panel.setLayout(new FillLayout());
		panel.setBackgroundMode(SWT.INHERIT_DEFAULT);
		panel.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		final Label textLabel = new Label(panel, SWT.LEFT);
		textLabel.setText("hello");

		shell.setSize(300, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
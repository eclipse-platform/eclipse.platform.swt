/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class Bug490280_ColorInheritanceTest {

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

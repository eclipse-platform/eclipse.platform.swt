/*******************************************************************************
 * Copyright (c) 2021 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This snippet demonstrates the usage of {@link BorderLayout}
 */
public class Snippet379 {

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Border Layout Snippet");
		BorderLayout layout = new BorderLayout();
		layout.marginHeight = 16;
		layout.marginWidth = 8;
		layout.spacing = 4;
		layout.controlSpacing = 2;
		layout.type = SWT.VERTICAL;
		shell.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		shell.setLayout(layout);
		shell.setSize(800, 600);
		region(new Button(shell, SWT.PUSH), SWT.TOP).setText("North 1");
		region(new Button(shell, SWT.PUSH), SWT.TOP).setText("North 2");
		region(new Button(shell, SWT.PUSH), SWT.BOTTOM).setText("South 1");
		region(new Button(shell, SWT.PUSH), SWT.BOTTOM).setText("South 2");
		region(new Button(shell, SWT.PUSH), SWT.LEFT).setText("West 1");
		region(new Button(shell, SWT.PUSH), SWT.LEFT).setText("West 2");
		region(new Button(shell, SWT.PUSH), SWT.RIGHT).setText("East 1");
		region(new Button(shell, SWT.PUSH), SWT.RIGHT).setText("East 2");
		new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL).setText("Center 1");
		new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL).setText("Center 2");
		shell.pack(true);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static <C extends Control> C region(C control, int region) {
		control.setLayoutData(new BorderData(region));
		return control;
	}

}

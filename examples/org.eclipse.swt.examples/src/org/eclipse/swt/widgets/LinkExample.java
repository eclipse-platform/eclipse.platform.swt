/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class LinkExample {
	static Label label = null;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT Shell Example");
		shell.setSize(200, 200);

		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.marginWidth = 10;
		shellLayout.marginHeight = 10;
		shell.setLayout(shellLayout);

		Color colorWhite = display.getSystemColor(SWT.COLOR_WHITE);

		Composite composite = new Composite(shell, SWT.BORDER);

		GridLayout compositeLayout = new GridLayout(1, false);
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		String labelText = "Link clicked: ";

		Link link = new Link(composite, SWT.NONE);
		link.setText("The SWT component is designed to provide <a href=\"efficientData\"> efficient </a>, <a>portable</a> <a href=\"native\">access to the user-interface \n facilities of the operating systems</a> on which it is implemented.");
		link.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		link.setBackground(colorWhite);
		link.addListener(SWT.Selection, e -> label.setText(labelText + e.text));

		label = new Label(composite, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		colorWhite.dispose();
		display.dispose();
	}
}

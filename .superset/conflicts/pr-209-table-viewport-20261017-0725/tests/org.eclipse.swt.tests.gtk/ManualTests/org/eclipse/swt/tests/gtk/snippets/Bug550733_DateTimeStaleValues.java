/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug550733_DateTimeStaleValues {
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setSize(500, 400);
		shell.setLayout(new GridLayout());
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		DateTime datetime = new DateTime(shell, SWT.BORDER);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Print date");
		Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			label.setText(String.format("%s.%s.%s", datetime.getDay(), datetime.getMonth() + 1, datetime.getYear()));
		}));

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}

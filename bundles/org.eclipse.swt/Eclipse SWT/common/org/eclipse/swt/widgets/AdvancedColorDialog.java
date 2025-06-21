/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class AdvancedColorDialog extends Dialog {
	Color result;

	public AdvancedColorDialog(Shell parent) {
		super(parent);
	}

	public Color open() {
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Pick a Color");
		shell.setLayout(new GridLayout(1, false));

		ColorPickerComposite picker = new ColorPickerComposite(shell, SWT.BORDER);
		picker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		GridLayout buttonLayout = new GridLayout(2, true);
		buttonLayout.marginWidth = 0;
		buttonLayout.marginHeight = 0;
		buttonLayout.horizontalSpacing = 10;
		buttons.setLayout(buttonLayout);

		Button ok = new Button(buttons, SWT.PUSH);
		ok.setText("Select");
		ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ok.addListener(SWT.Selection, e -> {
			shell.close();
		});

		Button cancel = new Button(buttons, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		cancel.addListener(SWT.Selection, e -> {
			result = null;
			shell.close();
		});


		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return picker.selectedColor;
	}
}

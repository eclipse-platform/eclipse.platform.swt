package org.eclipse.swt.tests.manual;

/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Bug568550_ShellActivateOnClose {
	public static void showPopup(Control control) {
		// SWT.ON_TOP causes the problem on CentOS7 (X11), CentOS8 (Wayland)
		Shell popupShell = new Shell(control.getShell(), SWT.ON_TOP);
		popupShell.setLayout(new FillLayout());

		Table table = new Table(popupShell, 0);
		for (int iItem = 0; iItem < 10; iItem++) {
			new TableItem(table, 0).setText("Item " + iItem);
		}

		table.addListener(SWT.DefaultSelection, event -> {
			popupShell.setVisible(false);
			popupShell.dispose();
		});

		popupShell.pack();
		popupShell.setLocation(control.toDisplay(control.getSize()));
		popupShell.open();
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Label hint = new Label(shell, 0);
		hint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		hint.setText(
			"1) Press Ctrl+1 in StyledText to open popup\n" +
			"2) Double-click an item in popup\n" +
			"3) Popup will be closed\n" +
			"4) Notice [Shell active: no] below\n" +
			"5) Notice that caret is not visible on StyledText\n" +
			"6) On some systems such as CentOS8, Shell does not\n" +
			"   become active even after clicking it"
		);

		Label isActive = new Label(shell, 0);
		isActive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		shell.addListener(SWT.Activate,   e -> isActive.setText("[Shell active: yes]"));
		shell.addListener(SWT.Deactivate, e -> isActive.setText("[Shell active: no]"));

		StyledText styledText = new StyledText(shell, 0);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		styledText.setText("This is the StyledText");
		styledText.addListener(SWT.KeyDown, event -> {
			if (event.keyCode == '1' && (event.stateMask & SWT.MODIFIER_MASK) == SWT.MOD1) {
				showPopup(styledText);
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
/*******************************************************************************
 * Copyright (c) 2024 Andrey Loskutov (loskutov@gmx.de) and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrey Loskutov (loskutov@gmx.de) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

/*
 * A handy snippet to test hanging File/Directory dialogs
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Issue1062_FileChooserNativeDialog {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Issue1062_FileChooserNativeDialog");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		Label label = new Label(shell, SWT.WRAP);
		label.setText("Click to open dialog, then right click on some file to open a menu and after that close dialog."
				+ " The shell should not 'hang' after closing dialogs.");

		Button button = new Button(shell, 0);
		button.setText("Open File Dialog");
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			dialog.setText("Right click to open a menu and after that try to close dialog");
			String string = dialog.open();
			System.out.println("Selected: " + string);
		}));

		button = new Button(shell, 0);
		button.setText("Open Directory Dialog");
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
			dialog.setText("Right click to open a menu and after that try to close dialog");
			String string = dialog.open();
			System.out.println("Selected: " + string);
		}));

		shell.setSize(400, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

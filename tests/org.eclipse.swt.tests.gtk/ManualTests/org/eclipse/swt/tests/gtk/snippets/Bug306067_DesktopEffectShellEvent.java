/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/*
 * Title: Bug 306067 - [Widgets] SWT.Deactivate event is not fired to the dropdown shell when enable the Desktop effect
 * How to run: launch snippet and press button
 * Bug description: Shell does not receive deactivate event
 * Expected results: shell should receive deactivate event
 * GTK Version(s): GTK2.x
 */
public class Bug306067_DesktopEffectShellEvent {
	static Display display = null;
	static Shell shell = null;
	static Button button = null;
	static Shell dropDownShell = null;

	public static void main(String[] args) {
		Device.DEBUG = true;
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new RowLayout());

		// create the drop down shell
		dropDownShell = new Shell(shell, SWT.ON_TOP | SWT.DROP_DOWN);
		dropDownShell.setLayout(new RowLayout());
		dropDownShell.setVisible(false);
		dropDownShell.addListener(SWT.Deactivate, event -> {
			System.out.println("dropDownShell entering Deactivate event handler and will hide the dropdown shell");
			hideDropDown();
		});

		dropDownShell.addListener(SWT.Close, event -> hideDropDown());


		// create the button
		button = new Button(shell, SWT.PUSH);
		button.setText("Open");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!dropDownShell.isVisible()) {
					System.out.println("Open button entering widgetSelected event handler and will show the dropdown shell");
					showDropDown();
				}
			}
		});

		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("Open button entering mouseDown event handler");
				super.mouseDown(e);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("Open button entering mouseUp event handler");
				super.mouseUp(e);
			}
		});


		shell.setSize(300, 300);
		shell.addDisposeListener(e -> {
			if (dropDownShell != null && !dropDownShell.isDisposed()) {
				dropDownShell.dispose();
				dropDownShell = null;
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void showDropDown() {
		if (dropDownShell != null && !dropDownShell.isDisposed()) {
			dropDownShell.setText("This is a drop down shell");
			dropDownShell.setSize(100, 200);
			Rectangle buttonRect = button.getBounds();
			Point p = button.getParent().toDisplay(new Point(buttonRect.x, buttonRect.y + buttonRect.height));
			dropDownShell.setLocation(p.x, p.y);
			dropDownShell.setVisible(true);
			dropDownShell.setFocus();
		}
	}

	private static void hideDropDown() {
		dropDownShell.setVisible(false);
	}

}
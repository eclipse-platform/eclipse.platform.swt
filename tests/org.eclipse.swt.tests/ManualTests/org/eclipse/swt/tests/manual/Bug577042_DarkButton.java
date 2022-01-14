/*******************************************************************************
 * Copyright (c) 2021, 2022 Syntevo and others.
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

package org.eclipse.swt.tests.manual;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug577042_DarkButton {
	enum Colors {
		Default,
		Light,
		Dark,
		Green,
	}

	enum Themes {
		Default,
		Dark,
	}

	static void setColors(Control control, Colors color) {
		switch (color) {
			case Default:
				control.setBackground(null);
				control.setForeground(null);
				break;
			case Light:
				control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
				break;
			case Dark:
				control.setBackground(new Color(0x19, 0x19, 0x19));
				control.setForeground(new Color(0xFF, 0xFF, 0xFF));
				break;
			case Green:
				control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_GREEN));
				control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				break;
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final ArrayList<Control> coloredControls = new ArrayList<>();
		final ArrayList<Control> otherControls = new ArrayList<>();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));
		coloredControls.add(shell);

		final Label hint = new Label(shell, 0);
		hint.setText(
			"1) Run on Windows 10 or 11\n" +
			"2) Bug 577042: Focus rectangle position is suboptimal\n" +
			"3) Bug 577042: Colored buttons have thick light frame around them\n" +
			"4) Bug 577042: On Windows 11, rounded corners are broken\n" +
			"5) Bug 577042: Support native dark theme for Button (currently only PUSH and TOGGLE)\n" +
			"NOTE: Click PUSH button to make it default (there could only be one default button)"
		);
		coloredControls.add(hint);

		for (Themes theme : Themes.values())
		for (Colors color : Colors.values())
		{
			display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", theme == Themes.Dark);

			Label label = new Label(shell, 0);
			label.setText("Theme:" + theme.name() + " Color:" + color.name());
			coloredControls.add(label);

			Composite composite = new Composite(shell, 0);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new GridLayout(4, true));
			coloredControls.add(composite);

			Button button;
			button = new Button(composite, SWT.PUSH);
			button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			button.setText("This is a button");
			final Button defaultButton = button;
			button.addListener(SWT.Selection, e -> shell.setDefaultButton(defaultButton));
			otherControls.add(button);
			setColors(button, color);

			button = new Button(composite, SWT.TOGGLE);
			button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			button.setText("This is a toggle");
			otherControls.add(button);
			setColors(button, color);

			button = new Button(composite, SWT.CHECK);
			button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			button.setText("This is a checkbox");
			otherControls.add(button);
			setColors(button, color);

			button = new Button(composite, SWT.RADIO);
			button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			button.setText("This is a radio");
			otherControls.add(button);
			setColors(button, color);
		}

		{
			Composite composite = new Composite(shell, 0);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new GridLayout(Colors.values().length, false));
			coloredControls.add(composite);

			Listener listener = event -> {
				Button button = (Button)event.widget;
				if (!button.getSelection())
					return;

				Colors color = (Colors)button.getData();

				for (Control control : coloredControls) {
					setColors(control, color);
				}

				for (Control control : otherControls) {
					control.redraw();
				}
			};

			for (Colors color : Colors.values()) {
				final Button radio = new Button(composite, SWT.RADIO);
				radio.setText(color.name());
				radio.setData(color);
				radio.addListener(SWT.Selection, listener);
				otherControls.add(radio);

				if (color == Colors.Dark) {
					radio.setSelection(true);
					for (Control control : coloredControls) {
						setColors(control, Colors.Dark);
					}
				}

				coloredControls.add(radio);
			}
		}

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
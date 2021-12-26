/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class Bug577042_DarkButton {
	enum Colors {
		Default,
		Light,
		Dark,
		Green,
	};

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

		for (Colors color : Colors.values())
		{
			Label label = new Label(shell, 0);
			label.setText("Color:" + color.name());
			coloredControls.add(label);

			Composite composite = new Composite(shell, 0);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new GridLayout(4, true));
			coloredControls.add(composite);

			Button button;
			button = new Button(composite, SWT.PUSH);
			button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			button.setText("This is a button");
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
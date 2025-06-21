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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class ColorDialogExample {

	private static RGB[] customColors = {
		    new RGB(255, 0, 0), new RGB(0, 255, 0), new RGB(0, 0, 255),
		    new RGB(255, 255, 0), new RGB(255, 165, 0), new RGB(255, 255, 255),
		    new RGB(128, 0, 128), new RGB(0, 0, 0)
		};

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Color Dialog Example");
        shell.setSize(400, 200);
        shell.setLayout(new GridLayout(2, false));

        Label colorLabel = new Label(shell, SWT.BORDER);
        colorLabel.setText("Click to choose color");
        colorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        colorLabel.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

        Button chooseColorButton = new Button(shell, SWT.PUSH);
        chooseColorButton.setText("Choose Color");

        chooseColorButton.addListener(SWT.Selection, e -> {
            ColorDialog colorDialog = new ColorDialog(shell);
            colorDialog.setText("Pick a Color");

            colorDialog.setRGBs(customColors);

            Color existingColor = (Color) colorLabel.getData("bgColor");
            if (existingColor != null && !existingColor.isDisposed()) {
                colorDialog.setRGB(existingColor.getRGB());
            }

            RGB selectedRGB = colorDialog.open();

            if (selectedRGB != null) {
                Color selectedColor = new Color(display, selectedRGB);
                colorLabel.setBackground(selectedColor);

                // Dispose the old background color
                Color oldColor = (Color) colorLabel.getData("bgColor");
                if (oldColor != null && !oldColor.isDisposed()) {
                    oldColor.dispose();
                }

                colorLabel.setData("bgColor", selectedColor);

                customColors = colorDialog.getRGBs();
            }
        });

        colorLabel.addListener(SWT.Dispose, e -> {
            Color bg = (Color) colorLabel.getData("bgColor");
            if (bg != null && !bg.isDisposed()) {
                bg.dispose();
            }
        });


        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

        display.dispose();
    }
}

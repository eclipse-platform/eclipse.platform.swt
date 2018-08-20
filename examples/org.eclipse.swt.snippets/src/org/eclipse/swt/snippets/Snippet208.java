/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

/*
 * Change hue, saturation and brightness of a color
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet208 {

public static void main (String [] args) {
	PaletteData palette = new PaletteData(0xff, 0xff00, 0xff0000);

	// ImageData showing variations of hue
	ImageData hueData = new ImageData(360, 100, 24, palette);
	float hue = 0;
	for (int x = 0; x < hueData.width; x++) {
		for (int y = 0; y < hueData.height; y++) {
			int pixel = palette.getPixel(new RGB(hue, 1f, 1f));
			hueData.setPixel(x, y, pixel);
		}
		hue += 360f / hueData.width;
	}

	// ImageData showing saturation on x axis and brightness on y axis
	ImageData saturationBrightnessData = new ImageData(360, 360, 24, palette);
	float saturation = 0f;
	float brightness = 1f;
	for (int x = 0; x < saturationBrightnessData.width; x++) {
		brightness = 1f;
		for (int y = 0; y < saturationBrightnessData.height; y++) {
			int pixel = palette.getPixel(new RGB(360f, saturation, brightness));
			saturationBrightnessData.setPixel(x, y, pixel);
			brightness -= 1f / saturationBrightnessData.height;
		}
		saturation += 1f / saturationBrightnessData.width;
	}

	Display display = new Display();
	Image hueImage = new Image(display, hueData);
	Image saturationImage = new Image(display, saturationBrightnessData);
	Shell shell = new Shell(display);
	shell.setText("Hue, Saturation, Brightness");
	GridLayout gridLayout = new GridLayout(2, false);
	gridLayout.verticalSpacing = 10;
	gridLayout.marginWidth = gridLayout.marginHeight = 16;
	shell.setLayout(gridLayout);

	Label label = new Label(shell, SWT.CENTER);
	label.setImage(hueImage);
	GridData data = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
	label.setLayoutData(data);

	label = new Label(shell, SWT.CENTER); //spacer
	label = new Label(shell, SWT.CENTER);
	label.setText("Hue");
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	label.setLayoutData(data);
	label = new Label(shell, SWT.CENTER); //spacer
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
	label.setLayoutData(data);

	label = new Label(shell, SWT.LEFT);
	label.setText("Brightness");
	data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
	label.setLayoutData(data);

	label = new Label(shell, SWT.CENTER);
	label.setImage(saturationImage);
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	label.setLayoutData (data);

	label = new Label(shell, SWT.CENTER); //spacer
	label = new Label(shell, SWT.CENTER);
	label.setText("Saturation");
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	label.setLayoutData(data);

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}
	hueImage.dispose();
	saturationImage.dispose();
	display.dispose();
}

}



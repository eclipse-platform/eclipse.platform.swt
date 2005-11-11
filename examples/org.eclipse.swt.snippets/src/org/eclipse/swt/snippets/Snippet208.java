/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.swt.snippets;

/*
 * Change hue, saturation and brightness of a color
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet208 {	 

public static void main (String [] args) {
	PaletteData palette = new PaletteData(0xff, 0xff00, 0xff0000);	
	
	// ImageData showing variations of Hue	
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
	ImageData saturationData = new ImageData(360, 360, 24, palette);
	float saturation = 0f;
	float brightness = 1f;
	for (int x = 0; x < saturationData.width; x++) {
		brightness = 1f;
		for (int y = 0; y < saturationData.height; y++) {
			int pixel = palette.getPixel(new RGB(360f, saturation, brightness));	
			saturationData.setPixel(x, y, pixel);
			brightness -= 1f / saturationData.height;
		}
		saturation += 1f / saturationData.width;
	}
		
	Display display = new Display();
	Image hueImage = new Image(display, hueData);
	Image saturationImage = new Image(display, saturationData);
	Shell shell = new Shell(display);
	shell.setText("Hue, Saturation, Brightness");
	GridLayout gridLayout = new GridLayout(2, false);
	gridLayout.verticalSpacing = 10;
	gridLayout.marginWidth = gridLayout.marginHeight = 16;
	shell.setLayout(gridLayout);		
	
	
	Label hueImageLabel = new Label(shell, SWT.CENTER);
	hueImageLabel.setImage(hueImage);
	GridData data = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
	data.widthHint = hueData.width;
	data.heightHint = hueData.height;
	hueImageLabel.setLayoutData(data);	
	
	Label label = new Label(shell, SWT.CENTER); //spacer
	label = new Label(shell, SWT.CENTER);
	label.setText("Hue");
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	label.setLayoutData(data);
	
	label = new Label(shell, SWT.LEFT);
	label.setText("Brightness");
	data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
	label.setLayoutData(data);
	
	Label saturationImageLabel = new Label(shell, SWT.CENTER);
	saturationImageLabel.setImage(saturationImage);
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	data.widthHint = saturationData.width;
	data.heightHint = saturationData.height;
	saturationImageLabel.setLayoutData (data);
	
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



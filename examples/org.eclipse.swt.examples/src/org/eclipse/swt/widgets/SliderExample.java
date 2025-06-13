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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class SliderExample {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT Slider Example");
		shell.setSize(800, 600);

		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.marginWidth = 10;
		shellLayout.marginHeight = 10;
		shellLayout.horizontalSpacing = 10;
		shellLayout.verticalSpacing = 10;
		shell.setLayout(shellLayout);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);

		Composite horSlidComp = new Composite(shell, SWT.BORDER);
		horSlidComp.setLayout(shellLayout);
		horSlidComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label title = new Label(horSlidComp, SWT.NONE);
		title.setText("Horizontal Slider");
		title.setLayoutData(gd);

		int max = 25;
		int min = 0;
		int increment = 2;
		int pageIncrement = 5;
		int thumb = 3;
		int selection = 0;

		Slider horSlider = new Slider(horSlidComp, SWT.HORIZONTAL);
		horSlider.setMaximum(max);
		horSlider.setMinimum(min);
		horSlider.setIncrement(increment);
		horSlider.setPageIncrement(pageIncrement);
		horSlider.setThumb(thumb);
		horSlider.setSelection(selection);
		horSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label value = new Label(horSlidComp, SWT.NONE);
		String string = "Selected Value: ";
		value.setText(string);
		value.setLayoutData(gd);

		horSlider.addListener(SWT.Selection, event -> value.setText(string + horSlider.getSelection()));

		Composite vertiSlidComp = new Composite(shell, SWT.BORDER);
		vertiSlidComp.setLayout(shellLayout);
		vertiSlidComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label custtitle = new Label(vertiSlidComp, SWT.NONE);
		custtitle.setText("Vertical Slider");
		custtitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Slider vertiSlider = new Slider(vertiSlidComp, SWT.VERTICAL);
		vertiSlider.setMaximum(max);
		vertiSlider.setMinimum(min);
		vertiSlider.setIncrement(increment);
		vertiSlider.setPageIncrement(pageIncrement);
		vertiSlider.setThumb(thumb);
		vertiSlider.setSelection(selection);
		vertiSlider.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));

		Label custSlidValue = new Label(vertiSlidComp, SWT.NONE);
		custSlidValue.setText(string);
		custSlidValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		vertiSlider.addListener(SWT.Selection, event -> custSlidValue.setText(string + vertiSlider.getSelection()));

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}

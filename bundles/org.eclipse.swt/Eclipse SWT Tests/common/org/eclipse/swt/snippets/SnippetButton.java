/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class SnippetButton {

	static int ZOOM = 200;

	public static void main(String[] args) {
		Display display = new Display();
		Image image1 = display.getSystemImage(SWT.ICON_QUESTION);

		ImageData img = image1.getImageData();

		Shell shell = new Shell(display);
		shell.setText("SnippetButton");
		shell.setLayout(new GridLayout());

		// button.addSelectionListener(widgetSelectedAdapter(e ->
		// System.out.println("Received evt: " + e )));
		// button.addSelectionListener(widgetSelectedAdapter(__ ->
		// System.out.println("Another click")));

		// When the shell is active and the user presses ENTER, the button is
		// pressed
		// shell.setDefaultButton(button);

//		{
//			var out = ImageUtils.createGenericImage(new SkijaImageDataProvider(img)).getImageData(ZOOM);
//			var button = new Button(shell, SWT.PUSH);
//			button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));
//			button.setImage(new Image(display, out));
//			button.setText("Button Question Icon");
//			button.setSize(133, 150);
//			button.setLocation(200, 200);
//		}
		{
			Image image2 = new Image(display, new Rectangle(0, 0, 5, 5));
			var b2 = image2.getBounds();
			var gc2 = new GC(image2);
			gc2.setBackground(display.getSystemColor(SWT.COLOR_RED));
			gc2.fillRectangle(b2);
			var button = new Button(shell, SWT.PUSH);
			button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));

			// When the shell is active and the user presses ENTER, the button is
			// pressed
			// shell.setDefaultButton(button);

			button.setImage(image1);
			button.setText("Button Red");
			button.setSize(100, 100);
			button.setLocation(300, 300);
			button.setImage(image2);
		}

		{
			Image image3 = new Image(display, 5, 5);

			var b3 = image3.getBounds();
			var gc3 = new GC(image3);
			gc3.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc3.fillRectangle(b3);

			var button = new Button(shell, SWT.PUSH);
			button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));

			// When the shell is active and the user presses ENTER, the button is
			// pressed
			// shell.setDefaultButton(button);

			button.setImage(image3);
			button.setText("Button Green");
			button.setSize(100, 100);
			button.setLocation(300, 300);
			button.setImage(image3);
		}

//		{
//			Image image3 = new Image(display, 5, 5);
//			var b3 = image3.getBounds();
//			var gc3 = new GC(image3);
//			gc3.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
//			gc3.fillRectangle(b3);
//			var button = new Button(shell, SWT.PUSH);
//			button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));
//
//			// When the shell is active and the user presses ENTER, the button is
//			// pressed
//			// shell.setDefaultButton(button);
//
//			button.setImage(ImageUtils.createGenericImage(new SkijaImageDataProvider(image3.getImageData())));
//			button.setText("Button Blue");
//			button.setSize(100, 100);
//			button.setLocation(300, 300);
//			button.setImage(image3);
//		}

		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

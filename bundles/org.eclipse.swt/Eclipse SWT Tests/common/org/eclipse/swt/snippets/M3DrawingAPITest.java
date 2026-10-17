/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class M3DrawingAPITest {


	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("M3DrawingAPITest");
		shell.setBounds(10, 10, 1000, 1000);

		SWT.USE_M3 = true;

		shell.addPaintListener(event -> {
			GC m3GC = Drawing.createGraphicsContext(event.gc, shell);
			m3GC.setBackground(display.getSystemColor(SWT.COLOR_GREEN));

			m3GC.drawString("yTjJ", 0, 0, false);
			m3GC.drawString("yTjJ", 0, 20, true);
			m3GC.drawString("yTjJ", 0, 40);
			for (int i = 20; i < 300; i = i + 4) {
				m3GC.drawPoint(100, i);
			}
			m3GC.drawArc(120, 20, 100, 100, 30, 180);
			m3GC.fillArc(120, 160, 100, 100, 30, 180);
			// Test when width and height values are negative
			m3GC.fillGradientRectangle(300, 80, -30, -80, false);
			m3GC.fillGradientRectangle(300, 80, 30, 80, true);
			// Test when both foreground and background colors are same
			m3GC.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
			m3GC.fillGradientRectangle(350, 80, 30, 80, true);
			// Test copyArea
			Image image = new Image(display, new Rectangle(0, 0, 50, 50));
			// Copy the area/rectangle present at (100,100,image.width,image.height) into
			// the image
			m3GC.copyArea(image, 100, 100);
			// Draw the image and check if area/rect is copied to the image
			m3GC.drawImage(image, 500, 150);
			// copy- paste
			m3GC.copyArea(300, 80, 20, 50, 400, 50, false);

			m3GC.copyArea(300, 80, 50, 50, 500, 50, true);
			m3GC.commit();
			image.dispose();
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

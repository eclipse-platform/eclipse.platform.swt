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

public class SkijaDrawingAPITest {


	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SkijaDrawingAPITest");
		shell.setBounds(10, 10, 1000, 1000);

		SWT.USE_SKIJA = true;

		shell.addPaintListener(event -> {
			GC skijaGC = GCFactory.createGraphicsContext(event.gc);
			skijaGC.setBackground(display.getSystemColor(SWT.COLOR_GREEN));

			skijaGC.drawString("yTjJ", 0, 0, false);
			skijaGC.drawString("yTjJ", 0, 20, true);
			skijaGC.drawString("yTjJ", 0, 40);
			for (int i = 20; i < 300; i = i + 4) {
				skijaGC.drawPoint(100, i);
			}
			skijaGC.drawArc(120, 20, 100, 100, 30, 180);
			skijaGC.fillArc(120, 160, 100, 100, 30, 180);
			// Test when width and height values are negative
			skijaGC.fillGradientRectangle(300, 80, -30, -80, false);
			skijaGC.fillGradientRectangle(300, 80, 30, 80, true);
			// Test when both foreground and background colors are same
			skijaGC.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
			skijaGC.fillGradientRectangle(350, 80, 30, 80, true);
			skijaGC.commit();
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

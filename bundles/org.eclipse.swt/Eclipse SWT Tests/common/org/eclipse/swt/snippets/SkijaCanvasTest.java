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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SkijaCanvasTest {


	public static void main (String [] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SkijaTest");
		shell.setBounds(10, 10, 200, 200);

		shell.setVisible(true);

		shell.addPaintListener(event -> {
			SkijaGC skijaGC = new SkijaGC((NativeGC) event.gc.innerGC, false);

			skijaGC.drawText("Hello World Test", 0, 0);
			skijaGC.drawLine(0, 0, 200, 200);

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

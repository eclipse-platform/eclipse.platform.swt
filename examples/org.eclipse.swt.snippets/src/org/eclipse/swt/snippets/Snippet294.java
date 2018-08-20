/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Region on a control: create a non-rectangular button
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.4
 */

public class Snippet294 {

	static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int)Math.sqrt(r*r - x*x);
			polygon[2*i] = offsetX + x;
			polygon[2*i+1] = offsetY + y;
			polygon[8*r - 2*i - 2] = offsetX + x;
			polygon[8*r - 2*i - 1] = offsetY - y;
		}
		return polygon;
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Regions on a Control");
		shell.setLayout(new FillLayout());
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));

		Button b2 = new Button(shell, SWT.PUSH);
		b2.setText("Button with Regions");

		// define a region that looks like a circle with two holes in ot
		Region region = new Region();
		region.add(circle(67, 87, 77));
		region.subtract(circle(20, 87, 47));
		region.subtract(circle(20, 87, 113));

		// define the shape of the button using setRegion
		b2.setRegion(region);
		b2.setLocation(100,50);

		b2.addListener(SWT.Selection, e -> shell.close());

		shell.setSize(200,200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		region.dispose();
		display.dispose();
	}

}

/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 529431: [GTK3.10+] Snippet294 fails to draw Region
 * How to run: launch snippet and look for the resulting Shell
 * Bug description: the Shell is a 200 x 200 square with no region set
 * Expected results: the Shell is circle with inner circular regions
 * GTK Version(s): GTK3.10+
 */
public class Bug529431_ShellSetRegion {

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

		final Shell shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP
				| SWT.NO_FOCUS | SWT.TOOL);

		shell.setSize(200,200);

		// define a region that looks like a circle with two holes in it
		Region region = new Region();
		region.add(circle(67, 87, 77));
		region.subtract(circle(20, 87, 47));
		region.subtract(circle(20, 87, 113));

		// define the shape of the shell using setRegion
		shell.setRegion(region);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		region.dispose();
		display.dispose();
	}

}

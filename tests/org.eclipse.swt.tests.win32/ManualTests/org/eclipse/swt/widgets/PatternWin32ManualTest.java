/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.internal.DPIUtil;

/*
 * This Snippet tests a pattern at multiple zoom levels.
 *
 * It is difficult to test the pattern at multiple zoom level automatically since we also need
 * to test the visual behavior. It is important to make sure that the pattern looks the same
 * regardless of its size as per the zoom level. On the execution, 2 shells are
 * opened at 2 different zoom levels. The pattern is a gradient of 2 colors. On both the
 * shells, the pattern should be uniformly drawn and the size difference of both the patterns
 * should be clearly visible without any visual difference except for the size. The size
 * difference should be equal to the scalingFactor in the snippet.
 *
 */
public class PatternWin32ManualTest {
	private static Display display = Display.getDefault();

	public static void main (String [] args) {
		int zoom = DPIUtil.getDeviceZoom();
		int scalingFactor = 3;
		int scaledZoom = zoom * scalingFactor;
		int width = 400;
		int height = 300;
		final Pattern pat = new Pattern(display, 0, 0, width, height, new Color(null, 200, 200, 200), 0, new Color(null, 255, 0, 0), 255);

		Shell shell1 = createShellWithPattern(width, height, pat, zoom);
		Shell shell2 = createShellWithPattern(width, height, pat, scaledZoom);

		while (!shell1.isDisposed() || !shell2.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		pat.dispose();
		shell1.dispose();
		shell2.dispose();
	}

	private static Shell createShellWithPattern(int width, int height, final Pattern pat, int nativeZoom) {
		Shell shell = new Shell(display);
		shell.nativeZoom = nativeZoom;
		shell.setText("Unscaled shell");
		shell.setSize(width, height);
		shell.addPaintListener(e -> {
			e.gc.setBackground(new Color(null, 100, 200, 0));
			e.gc.fillRectangle(0, 0, shell.getBounds().width, shell.getBounds().height);
			e.gc.setBackground(new Color(null, 255, 0, 0));
			e.gc.setBackgroundPattern(pat);
			e.gc.fillRectangle(0, 0, shell.getBounds().width, shell.getBounds().height);
		});
		shell.open();
		return shell;
	}
}

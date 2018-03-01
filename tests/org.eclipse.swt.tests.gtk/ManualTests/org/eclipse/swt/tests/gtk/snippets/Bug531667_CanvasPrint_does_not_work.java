/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link Canvas#print(GC)} does not print the canvas.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Examine the contents of {@code some_canvas.png}, absolute location is printed on the standard out.</li>
 * </ol>
 * Expected results: The image contains a white circle on a black background, similar to the canvas.
 * Actual results: The image contains only black background.
 */
public class Bug531667_CanvasPrint_does_not_work {

	public static void main(String[] args) {
		String filename = "some_canvas.png";

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(200, 200);
		shell.setText("Bug 531667");

		Composite composite = canvas(display, shell);

		shell.open();

		snapshot(display, composite, filename);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static Composite canvas(Display display, Shell shell) {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		Canvas canvas = new Canvas(composite, SWT.NONE);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		Color white = display.getSystemColor(SWT.COLOR_WHITE);

		canvas.addPaintListener(e -> {
			Rectangle clientArea = canvas.getClientArea();
			e.gc.setBackground(white);
			e.gc.fillOval(0, 0, clientArea.width, clientArea.height);
		});
		return composite;
	}

	private static void snapshot(Display display, Composite composite, String filename) {
		Rectangle bounds = composite.getBounds();
		Image image = new Image(display, bounds);
		GC gc = new GC(image);
		composite.print(gc);
		gc.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		File output = new File(filename);
		output.delete();
		loader.save(filename, SWT.IMAGE_PNG);
		System.out.println("Image saved to: " + output.getAbsolutePath());
	}
}

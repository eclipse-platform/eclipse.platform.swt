/*******************************************************************************
 * Copyright (c) 2019 Simeon Andreev and others. All rights reserved.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link GC#getClipping()} returns wrong results when the user has set a transformation. Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Observe {@link GC#getClipping()} states that the clipping starts at (10, 10), while it actually starts at (6, 18).</li>
 * </ol>
 * Expected results: The user specified and transformed clipping area is painted in dark red.
 *                   The result of {@link GC#getClipping()} is painted in transparent red, the dark red area is bound by {@link GC#getClipping()}.
 *                   The drawn text indicates the correct bounds of {@link GC#getClipping()} result.
 * Actual results:   Drawing is done correctly in regard of API, however
 *                   the bounds returned by {@link GC#getClipping()} are incorrect, in particular the upper-left point is stated to be (10, 10).
 */
public class Bug545226_GC_getClipping_is_wrong {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(600, 450);
		shell.setText("Bug 545226 GC clipping is wrong");
		shell.setLayout(new FillLayout());
		Composite main = new Composite(shell, SWT.NONE);
		main.setLayout(new FillLayout());

		Composite[] squares = showCaseClipping(main);
		for (Composite square : squares) {
			square.layout();
		}

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Composite[] showCaseClipping(Composite main) {
		Composite composite = new Composite(main, SWT.NONE);
		composite.setLayout(new FillLayout());

		Composite left = new Composite(composite, SWT.NONE);
		left.setLayout(new FillLayout(SWT.VERTICAL));
		Composite upperLeft = new Composite(left, SWT.BORDER);
		Composite bottomLeft = new Composite(left, SWT.BORDER);

		Composite right = new Composite(composite, SWT.NONE);
		right.setLayout(new FillLayout(SWT.VERTICAL));
		Composite upperRight = new Composite(right, SWT.BORDER);
		Composite bottomRight = new Composite(right, SWT.BORDER);

		Composite[] squares = { upperLeft, upperRight, bottomLeft, bottomRight };
		for (int i = 0; i < squares.length; ++i) {
			Composite square = squares[i];
			square.setLayout(new FillLayout(SWT.VERTICAL));

			Canvas canvas = new Canvas(square, SWT.BORDER);
			PaintListener paint = new ShowCaseClipping();
			canvas.addPaintListener(paint);
		}

		return squares;
	}

	private static class ShowCaseClipping implements PaintListener {

		@Override
		public void paintControl(PaintEvent event) {
			GC gc = event.gc;
			Device device = gc.getDevice();
			Rectangle originalClipping = gc.getClipping();

			Transform transform = new Transform(device);
			transform.translate(10.f, 10.f);
			transform.scale(0.8f, 0.8f);
			transform.rotate(5.f);
			gc.setTransform(transform);
			gc.setClipping(10, 10, 225, 150);
			gc.setTransform(null);

			gc.setAlpha(155);
			gc.setBackground(device.getSystemColor(SWT.COLOR_DARK_RED));
			gc.fillRectangle(originalClipping);

			Rectangle transformedClipping = gc.getClipping();
			gc.setClipping(gc.getClipping());
			gc.setBackground(device.getSystemColor(SWT.COLOR_DARK_RED));
			gc.setAlpha(75);
			gc.fillRectangle(transformedClipping);
			gc.setBackground(device.getSystemColor(SWT.COLOR_GRAY));
			gc.setAlpha(255);
			gc.drawText(transformedClipping.toString(), 6, 18);
		}
	}
}
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


import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.cairo.Cairo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * Description: Clipping computed by {@link GC#getClipping()} is wrong.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: All canvases are green, indicating that their bounds intersect with the clipping provided by {@link GC#getClipping()}.
 * Actual results: Only the top-left canvas is green, the rest of the canvases are red.
 * Further notes: The clipping of {@link GC#getClipping()} is shown below each colored canvas.
 */
public class Bug421127_Clipping_is_wrong {

	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800, 800);
		shell.setText("Display clipping bounds");
		shell.setLayout(new FillLayout());
		Composite main = new Composite(shell, SWT.NONE);
		main.setLayout(new FillLayout());

		/*
		 * Splits the shell into some composites.
		 *
		 * The composites each contain:
		 * a) A canvas, which paints red or green depending on whether it intersects with GC.getClipping.
		 * b) A text, which displays computed GC.getClipping and Cairo.cairo_get_matrix for the composite.
		 */
		Composite[] squares = showCaseClipping(main);
		for (Composite square : squares) {
			// comment in for a more complex example
			//showCaseClipping(square);
			square.layout();
		}

		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
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

		Composite[] squares = { upperLeft, bottomLeft, upperRight, bottomRight };
		for (Composite square : squares) {
			square.setLayout(new FillLayout(SWT.VERTICAL));

			final Canvas canvas = new Canvas(square, SWT.BORDER);
			final Canvas text = new Canvas(square, SWT.BORDER);

			class PaintCanvas implements PaintListener {
				@Override
				public void paintControl(PaintEvent event) {
					paintCanvas(event, canvas);
				}
			}

			class PrintClipping implements PaintListener {
				@Override
				public void paintControl(PaintEvent event) {
					clippingText(event, text);
				}
			}

			canvas.addPaintListener(new PaintCanvas());
			text.addPaintListener(new PrintClipping());
		}

		return squares;
	}

	private static void paintCanvas(PaintEvent event, Canvas canvas) {
		GC gc = gc(event);
		Rectangle bounds = canvas.getBounds();
		Rectangle clipping = gc.getClipping();
		Device device = gc.getDevice();
		int color = SWT.COLOR_GREEN;
		String text = "CLIPPING AND BOUNDS INTERSECT";
		if (!clipping.intersects(bounds)) {
			color = SWT.COLOR_RED;
			text = "CLIPPING AND BOUNDS DON'T INTERSECT";
		}
		gc.drawText(text, 0, 0);
		gc.setAlpha(25);
		gc.setBackground(device.getSystemColor(color));
		gc.fillRectangle(bounds);
		gc.setAlpha(255);
	}

	private static void clippingText(PaintEvent event, Canvas text) {
		StringBuilder clipping = new StringBuilder();
		appendCairoMatrix(event, clipping);
		clipping.append(System.lineSeparator());
		appendClippingRegion(event, clipping);

		GC gc = gc(event);
		gc.drawText(clipping.toString(), 0, 0);
	}

	private static void appendCairoMatrix(PaintEvent event, StringBuilder clipping) {
		long cairo = cairo(event);
		clipping.append("Cairo Matrix: ");
		if (cairo != 0) {
			double[] matrix = new double[6];
			Cairo.cairo_get_matrix(cairo, matrix);
			clipping.append(Arrays.toString(matrix));
		} else {
			clipping.append("--");
		}
	}

	private static void appendClippingRegion(PaintEvent event, StringBuilder clipping) {
		clipping.append("GC clipping: ");
		Rectangle region = clipping(event);
		if (region != null) {
			clipping.append(region.x);
			clipping.append(' ');
			clipping.append(region.y);
			clipping.append(' ');
			clipping.append(region.width);
			clipping.append(' ');
			clipping.append(region.height);
		} else {
			clipping.append("--");
		}
	}

	private static GC gc(PaintEvent event) {
		if (event != null) {
			return event.gc;
		}
		return null;
	}

	private static Rectangle clipping(PaintEvent event) {
		GC gc = gc(event);
		if (gc != null) {
			Rectangle region = gc.getClipping();
			return region;
		}
		return null;
	}

	private static long cairo(PaintEvent event) {
		GC gc = gc(event);
		if (gc != null) {
			if (event.gc != null) {
				GCData data = event.gc.getGCData();
				if (data != null) {
					return data.cairo;
				}
			}
		}
		return 0;
	}

}

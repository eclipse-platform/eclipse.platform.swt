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
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link Canvas#print(GC)} does not print the canvas.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: The are no red figures drawn.
 * Actual results: Red figures are drawn on each of the bottom composites,
 *                 resulted from drawing on top of other composites due to bad clipping.
 */
public class Bug531667_PaintListener_paints_despite_empty_GC_clipping {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 250);
		shell.setText("Bug 531667 paint listener paints despite empty clipping");
		shell.setLayout(new FillLayout());

		/*
		 * Some paint listeners which will paint a figure on their own composite,
		 * then set clipping in the area of another composite and try to draw over it.
		 * We should be seeing only figures in the upper composites.
		 */

		class RectangleClipping implements BiConsumer<GC, Rectangle> {
			@Override
			public void accept(GC gc, Rectangle boundsOfComposite2) {
				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
				Rectangle rectangle = new Rectangle(0, 0, 50, 50);
				gc.setClipping(rectangle);
				gc.fillRectangle(0, 0, 200, 200);

				Rectangle rectangleInComposite2 = new Rectangle(boundsOfComposite2.x, boundsOfComposite2.y, 50, 50);
				gc.setClipping(rectangleInComposite2);

				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
				gc.fillRectangle(rectangleInComposite2);
			}
		}

		class BoundsClipping implements BiConsumer<GC, Rectangle> {
			@Override
			public void accept(GC gc, Rectangle boundsOfComposite2) {
				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
				gc.setClipping(50, 50, 50, 50);
				gc.fillRectangle(0, 0, 200, 200);

				gc.setClipping(boundsOfComposite2.x, boundsOfComposite2.y, 100, 100);

				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
				gc.fillRectangle(boundsOfComposite2.x + 50, boundsOfComposite2.y + 50, 50, 50);
			}
		}

		class PathClipping implements BiConsumer<GC, Rectangle> {
			@Override
			public void accept(GC gc, Rectangle boundsOfComposite2) {
				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
				Rectangle clipping = gc.getClipping();
				Path path = triangle(0, 0, 100, display);
				gc.setClipping(path);
				gc.fillRectangle(0, 0, 100, 100);
				gc.setClipping(clipping);

				Rectangle rectangle = new Rectangle(boundsOfComposite2.x, boundsOfComposite2.y, 100, 100);
				Path pathInComposite22 = triangle(rectangle.x, rectangle.y, 100, display);
				gc.setClipping(pathInComposite22);

				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
				gc.fillPath(pathInComposite22);
			}
		}

		class RegionClipping implements BiConsumer<GC, Rectangle> {
			@Override
			public void accept(GC gc, Rectangle boundsOfComposite2) {
				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
				Region region = new Region();
				region.add(new Rectangle(0, 0, 50, 50));
				region.add(new Rectangle(50, 50, 50, 50));
				gc.setClipping(region);
				gc.fillRectangle(0, 0, 200, 200);

				region.translate(boundsOfComposite2.x, boundsOfComposite2.y);
				gc.setClipping(region);

				gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
				gc.fillRectangle(boundsOfComposite2);
			}
		}

		List<BiConsumer<GC, Rectangle> > paints = Arrays.asList(
				new RectangleClipping(),
				new BoundsClipping(),
				new PathClipping(),
				new RegionClipping()
		);

		for (BiConsumer<GC, Rectangle> paint : paints) {
			paintedComposite(shell, paint);
		}

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

	private static void paintedComposite(Shell shell, final BiConsumer<GC, Rectangle> paint) {
		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite1 = new Composite(composite, SWT.BORDER);
		Composite composite2 = new Composite(composite, SWT.BORDER);
		composite2.setLayout(new FillLayout(SWT.VERTICAL));

		class PaintComposites implements PaintListener {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setAlpha(125);
				paint.accept(e.gc, composite2.getBounds());
			}
		}

		composite1.addPaintListener(new PaintComposites());


		new Composite(composite2, SWT.NONE);
		Label label21 = new Label(composite2, SWT.NONE);
		label21.setText(paint.getClass().getSimpleName());
		Label label22 = new Label(composite2, SWT.NONE);
		label22.setText("should be empty");

	}

	private static Path triangle(int x, int y, int size, Display display) {
		PathData pathData = new PathData();
		pathData.types = new byte[] { SWT.PATH_MOVE_TO, SWT.PATH_LINE_TO, SWT.PATH_LINE_TO, SWT.PATH_CLOSE };
		pathData.points = new float[] { x + 0.f, y + 0.f,  x + size, y + 0.f, x + (size / 2), y + size };
		Path path = new Path(display, pathData);
		return path;
	}
}
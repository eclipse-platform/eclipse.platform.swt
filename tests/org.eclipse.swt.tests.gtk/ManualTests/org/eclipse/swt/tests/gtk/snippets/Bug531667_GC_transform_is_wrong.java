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
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link GC#setTransform(Transform)} is wrong. Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Resize the shell, so that figures are clipped by the composite.</li>
 * </ol>
 * Expected results: Each square in the composite has a fully visible red figure, which is contained in its composite.
 * Actual results: Only the top left square has a visible figure, the figure is drawn over other composites on resize.
 */
public class Bug531667_GC_transform_is_wrong {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 400);
		shell.setText("Bug 531667 GC transform is wrong");
		shell.setLayout(new FillLayout());
		Composite main = new Composite(shell, SWT.NONE);
		main.setLayout(new FillLayout());

		Composite[] squares = showCaseTransform(main);
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

	private static Composite[] showCaseTransform(Composite main) {
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

		Consumer<Transform> upperLeftTransform = t -> transform(t, 100.f, 25.f, 1.5f, 1.25f, 45.f);
		Consumer<Transform> upperRightTransform = t -> transform(t, 150.f, 25.f, 1.f, 1.5f, 75.f);
		Consumer<Transform> bottomLeftTransform = t -> transform(t, 50.f, 25.f, 0.75f, 0.5f, 15.f);
		Consumer<Transform> bottomRightTransform = t -> transform(t, 150.f, 75.f, 0.5f, 0.75f, 45.f);

		Composite[] squares = { upperLeft, upperRight, bottomLeft, bottomRight };
		List<Consumer<Transform>> transformations = Arrays.asList(upperLeftTransform, upperRightTransform,
				bottomLeftTransform, bottomRightTransform);

		for (int i = 0; i < squares.length; ++i) {
			Composite square = squares[i];
			square.setLayout(new FillLayout(SWT.VERTICAL));

			Consumer<Transform> transformation = transformations.get(i);
			PaintListener paint = new TransformSquare(transformation);
			Canvas canvas = new Canvas(square, SWT.BORDER);
			canvas.addPaintListener(paint);
		}

		return squares;
	}

	private static GC gc(PaintEvent event) {
		if (event != null) {
			return event.gc;
		}
		return null;
	}

	private static void transform(Transform transform, float translateX, float translateY, float scaleX, float scaleY, float rotationAngle) {
		transform.translate(translateX, translateY);
		transform.scale(scaleX, scaleY);
		transform.rotate(rotationAngle);
	}

	private static class TransformSquare implements PaintListener {

		private final Consumer<Transform> transformation;

		TransformSquare(Consumer<Transform> transformation) {
			this.transformation = transformation;
		}

		@Override
		public void paintControl(PaintEvent event) {
			GC gc = gc(event);

			Device device = gc.getDevice();
			Transform transform = new Transform(device);
			transformation.accept(transform);

			// set / get the transformation a few times, to see that this has no unexpected effect
			gc.setTransform(transform);
			gc.getTransform(transform);
			gc.setTransform(transform);

			// set some clipping to ensure we didn't break something clipping+transform related
			Region region = new Region();
			region.add(new Rectangle(0, 0, 200, 200));
			region.subtract(new Rectangle(50, 50, 50, 50));
			gc.setClipping(region);

			int width = 80;
			int height = 80;
			gc.setAlpha(155);
			gc.setBackground(device.getSystemColor(SWT.COLOR_RED));
			int[] points = { 0, 0, width, 0, width, height, 0, height };
			gc.fillPolygon(points);

			gc.setTransform(null);
		}
	}
}

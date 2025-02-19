/*******************************************************************************
 * Copyright (c) Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public final class Drawing {

	private static Color widgetBackground;

	private Drawing() {
	}

	public static GC createGraphicsContext(GC originalGC) {
		if (!SWT.USE_SKIJA) {
			return originalGC;
		}

		if (!(originalGC.innerGC instanceof NativeGC originalNativeGC)) {
			return originalGC;
		}

		GC gc = new GC();
		gc.innerGC = new SkijaGC(originalNativeGC, null);
		return gc;
	}

	/**
	 * Executes the given drawing operation of the given control on a GC. If the
	 * given GC is not null, it is used as the target GC for the rendering result,
	 * but actual rendering may be performed on a different GC whose type depends on
	 * global configuration.
	 *
	 * @param control       the control to execute the operation on
	 * @param originalGC    a GC already created for the given control or null
	 * @param drawOperation the operation that draws the control
	 */
	public static void drawWithGC(Control control, GC originalGC, Consumer<GC> drawOperation) {
		Rectangle bounds = control.getBounds();
		if (originalGC != null && originalGC.innerGC instanceof NativeGC nativeGC) {
			if (nativeGC.drawable != control) {
				throw new IllegalStateException("given GC was not created for given control");
			}
		}

		if (originalGC == null) {
			originalGC = new GC(control);
		}
		originalGC.setFont(control.getFont());
		originalGC.setForeground(control.getForeground());
		originalGC.setBackground(control.getBackground());
		originalGC.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
		originalGC.setAntialias(SWT.ON);

		GC gc = createGraphicsContext(originalGC);

		Image doubleBufferingImage = null;

		if (SWT.getPlatform().equals("win32") || SWT.getPlatform().equals("gtk")) {
			// Extract background color on first execution
			if (widgetBackground == null) {
				extractAndStoreBackgroundColor(bounds, originalGC);
			}
			control.style |= SWT.NO_BACKGROUND;
		}

		if (gc.innerGC instanceof NativeGC) {
			if (SWT.getPlatform().equals("win32")) {
				// Use double buffering on windows
				doubleBufferingImage = new Image(gc.getDevice(), bounds.width, bounds.height);
				originalGC.copyArea(doubleBufferingImage, 0, 0);
				GC doubleBufferingGC = new GC(doubleBufferingImage);
				doubleBufferingGC.setForeground(originalGC.getForeground());
				doubleBufferingGC.setBackground(widgetBackground);
				doubleBufferingGC.setAntialias(SWT.ON);
				doubleBufferingGC.fillRectangle(0, 0, bounds.width, bounds.height);
				gc = doubleBufferingGC;
			}
		}

		try {
			drawOperation.accept(gc);
			gc.commit();
			if (doubleBufferingImage != null) {
				originalGC.drawImage(doubleBufferingImage, 0, 0);
				doubleBufferingImage.dispose();
			}
		} finally {
			gc.dispose();
			originalGC.dispose();
		}
	}

	private static void extractAndStoreBackgroundColor(Rectangle r, GC originalGC) {
		Image backgroundColorImage = new Image(originalGC.getDevice(), r.width, r.height);
		originalGC.copyArea(backgroundColorImage, 0, 0);
		int pixel = backgroundColorImage.getImageData().getPixel(0, 0);
		backgroundColorImage.dispose();
		widgetBackground = SWT.convertPixelToColor(pixel);
	}

	/**
	 * Executes the given non-drawing operation on a GC. The GC is automatically
	 * generated and disposed. The type of GC depends on global configuration. In
	 * case drawing operations are performed, they may not be rendered.
	 *
	 * @param <T>       the type of result of the operation
	 * @param control   the control to execute the operation on
	 * @param operation the operation to execute
	 * @return the result of the given operation
	 */
	public static <T> T executeOnGC(Control control, Function<GC, T> operation) {
		GC originalGC = new GC(control);
		GC gc = createGraphicsContext(originalGC);
		try {
			T result = operation.apply(gc);
			return result;
		} finally {
			gc.dispose();
			originalGC.dispose();
		}
	}

}

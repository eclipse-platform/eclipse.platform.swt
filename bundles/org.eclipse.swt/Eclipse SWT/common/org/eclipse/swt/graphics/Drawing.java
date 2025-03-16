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

	private Drawing() {
	}

	public static GC createGraphicsContext(GC originalGC, Control control) {
		return createGraphicsContext(originalGC, control, false);
	}

	private static GC createGraphicsContext(GC originalGC, Control control, boolean onlyForMeasuring) {
		if (!SWT.USE_SKIJA) {
			return originalGC;
		}

		if (!(originalGC.innerGC instanceof NativeGC originalNativeGC)) {
			return originalGC;
		}

		GC gc = new GC();

		gc.innerGC = onlyForMeasuring
				? SkijaGC.createMeasureInstance(originalNativeGC, control)
				: SkijaGC.createDefaultInstance(originalNativeGC, control);

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
		if (originalGC != null && originalGC.innerGC instanceof NativeGC nativeGC
				&& nativeGC.drawable instanceof Control gcControl) {
			if (gcControl != control) {
				throw new IllegalStateException("given GC was not created for given control");
			}
		}

		boolean usingTemporaryGC = false;
		if (originalGC == null) {
			originalGC = new GC(control);
			usingTemporaryGC = true;
		}
		originalGC.setFont(control.getFont());
		originalGC.setForeground(control.getForeground());
		originalGC.setBackground(control.getBackground());
		originalGC.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
		originalGC.setAntialias(SWT.ON);

		GC gc = createGraphicsContext(originalGC, control);

		try {
			drawOperation.accept(gc);
			gc.commit();
		} finally {
			gc.dispose();
			if (usingTemporaryGC) {
				originalGC.dispose();
			}
		}
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
		GC gc = createGraphicsContext(originalGC, control);
		try {
			T result = operation.apply(gc);
			return result;
		} finally {
			gc.dispose();
			originalGC.dispose();
		}
	}

	public static Point getTextExtent(Control control, String text, int drawFlags) {
		return measure(control, gc -> {
			gc.setFont(control.getFont());
			return gc.textExtent(text, drawFlags);
		});
	}

	/**
	 * Executes the given non-drawing operation on a GC. The GC is automatically
	 * generated and disposed. The type of GC depends on global configuration.
	 *
	 * @param <T>       the type of result of the operation
	 * @param control   the control to execute the operation on
	 * @param operation the operation to execute
	 * @return the result of the given operation
	 */
	public static <T> T measure(Control control, Function<GC, T> operation) {
		GC originalGC = new GC(control);
		GC gc = createGraphicsContext(originalGC, control, true);
		try {
			return operation.apply(gc);
		} finally {
			gc.dispose();
			originalGC.dispose();
		}
	}
}

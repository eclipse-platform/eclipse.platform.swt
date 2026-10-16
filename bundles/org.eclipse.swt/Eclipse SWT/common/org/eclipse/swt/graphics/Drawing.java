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

		GC gc = originalGC;
		Throwable failure = null;
		try {
			gc = createGraphicsContext(originalGC, control);
			gc.setFont(control.getFont());
			gc.setForeground(control.getForeground());
			gc.setBackground(control.getBackground());
			gc.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
			gc.setAntialias(SWT.ON);
			drawOperation.accept(gc);
			gc.commit();
		} catch (RuntimeException | Error problem) {
			failure = problem;
			throw problem;
		} finally {
			disposeOwned(gc, originalGC, usingTemporaryGC, failure);
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
		GC gc = originalGC;
		Throwable failure = null;
		try {
			gc = createGraphicsContext(originalGC, control);
			return operation.apply(gc);
		} catch (RuntimeException | Error problem) {
			failure = problem;
			throw problem;
		} finally {
			disposeOwned(gc, originalGC, true, failure);
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
		GC gc = originalGC;
		Throwable failure = null;
		try {
			originalGC.setFont(control.getFont());
			gc = createGraphicsContext(originalGC, control, true);
			return operation.apply(gc);
		} catch (RuntimeException | Error problem) {
			failure = problem;
			throw problem;
		} finally {
			disposeOwned(gc, originalGC, true, failure);
		}
	}

	/** Release only owned contexts, including setup failure; never dispose a borrowed paint GC. */
	private static void disposeOwned(GC gc, GC originalGC, boolean ownOriginal, Throwable primary) {
		Throwable failure = primary;
		try {
			if (gc != originalGC) failure = dispose(gc, failure);
		} finally {
			// Attempt the original even when releasing its adapter fails.
			if (ownOriginal) failure = dispose(originalGC, failure);
		}
		if (primary == null && failure != null) {
			if (failure instanceof RuntimeException runtime) throw runtime;
			throw (Error) failure;
		}
	}

	private static Throwable dispose(GC gc, Throwable primary) {
		try {
			gc.dispose();
		} catch (RuntimeException | Error cleanup) {
			if (primary == null) return cleanup;
			if (primary != cleanup) primary.addSuppressed(cleanup);
		}
		return primary;
	}

}
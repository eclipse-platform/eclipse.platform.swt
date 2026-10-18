/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.render;

import java.util.function.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Chooses the backend of a drawn plane on win32 from the widget's mode: <code>custom</code> draws with the GC,
 * <code>skija</code> through Skija, <code>gpu</code> through Direct2D. A backend that cannot start falls back
 * (<code>gpu</code> to <code>skija</code> to <code>custom</code>) and says so once in the log.
 */
public final class RowBackends {
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.render.RowBackends");
	/** The Skija backend, compiled from the Eclipse SWT Render Skija source folder against the Skija jars. */
	static final String SKIJA_CANVAS = "org.eclipse.swt.internal.render.skija.SkijaRowCanvas";

	private RowBackends () {
	}

	/**
	 * The backend of a new drawn plane.
	 *
	 * @param mode the widget's mode (<code>CUSTOM</code>, <code>SKIJA</code> or <code>GPU</code>)
	 * @param widget the simple class name, for the log
	 * @param hwnd the plane's window
	 * @param itemThemeClass the uxtheme class of rows (NUL-terminated)
	 * @param zoom the widget's zoom
	 * @param gcFactory makes a GC on a device context the way the widget does
	 */
	public static BufferCanvas create (WidgetMode mode, String widget, long hwnd, char [] itemThemeClass, IntSupplier zoom, LongFunction<GC> gcFactory) {
		if (mode == WidgetMode.GPU) {
			LOG.info (() -> widget + ": the gpu backend (Direct2D) is not available yet; trying skija");
		}
		if (mode == WidgetMode.GPU || mode == WidgetMode.M3) {
			BufferCanvas skija = skija (widget, hwnd, itemThemeClass, zoom);
			if (skija != null) return skija;
		}
		return new GcRowCanvas (hwnd, itemThemeClass, zoom, gcFactory);
	}

	/* the Skija backend is loaded by name: SWT itself has no dependency on the Skija jars */
	private static BufferCanvas skija (String widget, long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		try {
			Class<?> type = Class.forName (SKIJA_CANVAS);
			return (BufferCanvas) type.getConstructor (long.class, char [].class, IntSupplier.class).newInstance (hwnd, itemThemeClass, zoom);
		} catch (ReflectiveOperationException | LinkageError e) {
			LOG.info (() -> widget + ": the skija backend cannot start (" + e + "); drawing with the GC");
			return null;
		}
	}
}

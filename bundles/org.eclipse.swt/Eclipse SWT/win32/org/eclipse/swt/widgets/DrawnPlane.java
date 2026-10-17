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
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The drawn plane of a win32 row widget (Tree, Table, List) in the <code>custom</code>, <code>skija</code> and
 * <code>gpu</code> modes (docs/m3/RENDER_MODES.md, stage 1).
 * <p>
 * The widget keeps its viewport (or its native rows) exactly as in the <code>viewport</code> mode: the native
 * control is the behaviour plane that holds the resident window, takes input, owns the caret and answers
 * geometry. It never paints to the screen. Each <code>WM_PAINT</code> and <code>WM_PRINTCLIENT</code> first lets
 * the native control paint the damage into the back buffer, for its side effects only (data requests, label
 * measurement, custom draw), and then the drawing core paints the same damage over it: the rows of the resident
 * window that intersect the damage, laid out with the native control's own geometry. Subclasses read the rows.
 * </p>
 */
abstract class DrawnPlane {
	final Control control;
	private final long nativeProc;
	final BufferCanvas canvas;
	final RowRenderer renderer;
	final RowPaint row = new RowPaint ();
	final RECT damage = new RECT (), client = new RECT ();
	private final BackBuffer buffer = new BackBuffer ();
	private final PAINTSTRUCT ps = new PAINTSTRUCT ();

	/**
	 * @param control the widget whose <code>handle</code> is the behaviour plane
	 * @param mode the widget's drawn mode
	 * @param widget the widget's simple class name (theme and log)
	 * @param nativeProc the native window procedure of the behaviour plane
	 * @param itemThemeClass the uxtheme class of its rows (NUL-terminated)
	 */
	DrawnPlane (Control control, WidgetMode mode, String widget, long nativeProc, char [] itemThemeClass) {
		this.control = control;
		this.nativeProc = nativeProc;
		this.renderer = new RowRenderer (RowTheme.of (widget));
		this.canvas = RowBackends.create (mode, widget, control.handle, itemThemeClass, control::getAutoscalingZoom, hdc -> {
			GCData data = new GCData ();
			data.device = control.display;
			return control.createNewGC (hdc, data);
		});
	}

	/** True for the drawn modes. */
	static boolean drawn (WidgetMode mode) {
		return mode == WidgetMode.CUSTOM || mode == WidgetMode.SKIJA || mode == WidgetMode.GPU;
	}

	/** True for the messages this plane paints: the behaviour plane's paint and print-client. */
	final boolean paints (long hwnd, int msg) {
		return hwnd == control.handle && (msg == OS.WM_PAINT || msg == OS.WM_PRINTCLIENT);
	}

	/**
	 * Paints the damage of a <code>WM_PAINT</code> (with or without a device context) or a
	 * <code>WM_PRINTCLIENT</code>: behaviour pass into the back buffer, drawn pass over it, present.
	 */
	final long paint (long hwnd, int msg, long wParam, long lParam) {
		boolean own = msg == OS.WM_PAINT && wParam == 0;
		long target = own ? OS.BeginPaint (hwnd, ps) : wParam;
		try {
			OS.GetClientRect (hwnd, client);
			if (own) {
				OS.SetRect (damage, ps.left, ps.top, ps.right, ps.bottom);
			} else {
				OS.GetClipBox (target, damage);
			}
			if (!OS.IntersectRect (damage, damage, client)) return 0;
			buffer.ensure (target, client.right, client.bottom);
			behaviour (hwnd, msg, lParam);
			if (control.isDisposed ()) return 0;
			afterBehaviour (msg);
			if (!clipDamage (hwnd)) return 0;
			canvas.target (buffer);
			canvas.begin (damage.left, damage.top, damage.right - damage.left, damage.bottom - damage.top);
			try {
				drawRows (hwnd);
			} finally {
				canvas.end ();
			}
			buffer.present (target, damage.left, damage.top, damage.right - damage.left, damage.bottom - damage.top);
		} finally {
			if (own) OS.EndPaint (hwnd, ps);
		}
		return 0;
	}

	/* the native control paints the damage into the back buffer: its side effects happen as natively */
	private void behaviour (long hwnd, int msg, long lParam) {
		long dc = buffer.dc;
		int saved = OS.SaveDC (dc);
		OS.IntersectClipRect (dc, damage.left, damage.top, damage.right, damage.bottom);
		if (msg == OS.WM_PRINTCLIENT) {
			OS.CallWindowProc (nativeProc, hwnd, OS.WM_PRINTCLIENT, dc, lParam);
		} else {
			OS.CallWindowProc (nativeProc, hwnd, OS.WM_PAINT, dc, 0);
		}
		OS.RestoreDC (dc, saved);
	}

	/** Runs after the behaviour pass, where the widget's own paint path runs after its native paint. */
	void afterBehaviour (int msg) {
	}

	/** Narrows the damage to the rows area (for example below a header); answers false when nothing is left. */
	boolean clipDamage (long hwnd) {
		return true;
	}

	/** Paints the rows that intersect the damage, top to bottom, and the empty area below them. */
	abstract void drawRows (long hwnd);

	/** Frees the back buffer and the backend. */
	void release () {
		buffer.release ();
		canvas.dispose ();
	}
}

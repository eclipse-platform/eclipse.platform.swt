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

import static org.eclipse.swt.internal.VisibilityGate.*;

import org.eclipse.swt.internal.win32.*;

/**
 * Win32 box model of a control, outside in:
 * <pre>
 * +--------------------------- outer bounds (GetWindowRect) -------+
 * | BORDER (WS_EX_CLIENTEDGE, WS_BORDER, theme)                     |
 * |  +--------------------------------------------+-------------+  |
 * |  | INSET (widget padding)                     | TRACK       |  |
 * |  |  +--------------------------------------+  | (native     |  |
 * |  |  | MARGIN (content margins)             |  |  vertical   |  |
 * |  |  |  +--------------------------------+  |  |  scrollbar, |  |
 * |  |  |  | content viewport (0, 0, w, h)  |  |  |  non-client)|  |
 * |  |  |  +--------------------------------+  |  |             |  |
 * |  |  +--------------------------------------+  |             |  |
 * |  +--------------------------------------------+-------------+  |
 * |  | TRACK (native horizontal scrollbar, non-client)            |  |
 * +----------------------------------------------------------------+
 * </pre>
 * <p>
 * BORDER and TRACK come from one OS reading: window rect minus client rect,
 * split by the WS_VSCROLL/WS_HSCROLL style bits and the system scrollbar
 * metrics at the control's DPI. They therefore agree with
 * <code>computeTrim</code>, <code>getClientArea</code> and
 * <code>getBorderWidth</code>, which read the same OS state. INSET and MARGIN
 * are owner-set packed ints on {@link Scrollable} (8 bits per side, see
 * {@link org.eclipse.swt.internal.VisibilityGate#pack}). An overlay scrollbar
 * has no edge and is not part of the box. All values are device pixels; the
 * content viewport is the client rect minus INSET and MARGIN, so its edges are
 * exact pixels and the rings meet them with no rounding.
 * </p>
 */
//FLLDCIM: HPCPOP - cold path (resize, tests); reusable RECTs on Display, no allocation per call
final class ViewportBox {
	private ViewportBox () {
	}

	static long boxHandle (Control c) {
		return c instanceof Scrollable scrollable ? scrollable.scrolledHandle () : c.topHandle ();
	}

	static boolean edges (Control c, int [] out) {
		long hwnd = boxHandle (c);
		Display d = c.display;
		RECT window = d.boxWindow, client = d.boxClient;
		if (!OS.GetWindowRect (hwnd, window) || !OS.GetClientRect (hwnd, client)) {
			return false;
		}
		OS.MapWindowPoints (hwnd, 0, client, 2);
		if (client.left > client.right) {
			int t = client.left;
			client.left = client.right;
			client.right = t;
		}
		int ncTop = client.top - window.top, ncRight = window.right - client.right;
		int ncBottom = window.bottom - client.bottom, ncLeft = client.left - window.left;
		int style = OS.GetWindowLong (hwnd, OS.GWL_STYLE), ex = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
		int vertical = (style & OS.WS_VSCROLL) != 0 ? c.getSystemMetrics (OS.SM_CXVSCROLL) : 0;
		int horizontal = (style & OS.WS_HSCROLL) != 0 ? c.getSystemMetrics (OS.SM_CYHSCROLL) : 0;
		boolean leftBar = ((ex & OS.WS_EX_LEFTSCROLLBAR) != 0) != ((ex & OS.WS_EX_LAYOUTRTL) != 0);
		int trackRight = leftBar ? 0 : Math.min (vertical, Math.max (0, ncRight));
		int trackLeft = leftBar ? Math.min (vertical, Math.max (0, ncLeft)) : 0;
		int trackBottom = Math.min (horizontal, Math.max (0, ncBottom));
		set (out, BORDER, ncTop, ncRight - trackRight, ncBottom - trackBottom, ncLeft - trackLeft);
		set (out, TRACK, 0, trackRight, trackBottom, trackLeft);
		int inset = c instanceof Scrollable scrollable ? scrollable.boxInset : 0;
		int margin = c instanceof Scrollable scrollable ? scrollable.boxMargin : 0;
		set (out, INSET, unpack (inset, TOP), unpack (inset, RIGHT), unpack (inset, BOTTOM), unpack (inset, LEFT));
		set (out, MARGIN, unpack (margin, TOP), unpack (margin, RIGHT), unpack (margin, BOTTOM), unpack (margin, LEFT));
		return true;
	}

	/**
	 * Content viewport in client pixels: <code>out = {x, y, width, height}</code>,
	 * the client rect minus INSET and MARGIN, exact device pixels.
	 */
	static boolean content (Control c, int [] out) {
		long hwnd = boxHandle (c);
		RECT client = c.display.boxClient;
		if (!OS.GetClientRect (hwnd, client)) {
			return false;
		}
		int inset = c instanceof Scrollable scrollable ? scrollable.boxInset : 0;
		int margin = c instanceof Scrollable scrollable ? scrollable.boxMargin : 0;
		int left = unpack (inset, LEFT) + unpack (margin, LEFT), top = unpack (inset, TOP) + unpack (margin, TOP);
		int right = unpack (inset, RIGHT) + unpack (margin, RIGHT), bottom = unpack (inset, BOTTOM) + unpack (margin, BOTTOM);
		out [0] = client.left + left;
		out [1] = client.top + top;
		out [2] = Math.max (0, client.right - client.left - left - right);
		out [3] = Math.max (0, client.bottom - client.top - top - bottom);
		return true;
	}

	static void set (int [] out, int ring, int top, int right, int bottom, int left) {
		out [ring * 4 + TOP] = Math.max (0, top);
		out [ring * 4 + RIGHT] = Math.max (0, right);
		out [ring * 4 + BOTTOM] = Math.max (0, bottom);
		out [ring * 4 + LEFT] = Math.max (0, left);
	}

	static void setInsets (Control c, int packedInset, int packedMargin) {
		if (c instanceof Scrollable scrollable) {
			scrollable.boxInset = packedInset;
			scrollable.boxMargin = packedMargin;
		}
	}
}

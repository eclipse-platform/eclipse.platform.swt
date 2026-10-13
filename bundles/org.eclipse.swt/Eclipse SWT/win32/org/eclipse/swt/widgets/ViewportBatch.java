/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.eclipse.swt.internal.VisibilityGate.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Per-plane batch scope. Each plane of one widget freezes independently and an
 * operation freezes only the plane it changes; the others stay live.
 * <ul>
 * <li>{@link org.eclipse.swt.internal.VisibilityGate#PLANE_MAIN}: structure and
 * chrome. A Composite defers its layout (<code>setLayoutDeferred</code>) and
 * runs it once at the end; its viewport keeps scrolling and painting.</li>
 * <li>{@link org.eclipse.swt.internal.VisibilityGate#PLANE_VIEWPORT}: content.
 * WM_PAINT of the control's client area records its update region and paints
 * nothing (BeginPaint/EndPaint with the background erase answered as done, so
 * WM_NCPAINT and the native scrollbars stay live); <code>redraw</code> records
 * its rectangle. The end invalidates the recorded damage once, no erase, no
 * frame.</li>
 * <li>{@link org.eclipse.swt.internal.VisibilityGate#PLANE_SCROLLBARS}: native
 * scrollbars update their values without redraw; the end repaints the frame
 * once (RDW_FRAME, no erase).</li>
 * </ul>
 * Depths are packed per entry, 8 bits per plane, so nested scopes are safe.
 * No WM_SETREDRAW: it would hide the whole window, scrollbars included, and the
 * stock end of <code>setRedraw (true)</code> is a full erase and invalidate.
 */
//FLLDCIM: HPCPOP - WM_PAINT/redraw fast path is one int test (display.batchViewports)
final class ViewportBatch {
	static final int DEPTH_MASK = 0xFF;
	static final int FLAG_ALL_CHILDREN = 1;

	private ViewportBatch () {
	}

	static int shift (int plane) {
		return plane == PLANE_MAIN ? 0 : plane == PLANE_VIEWPORT ? 8 : 16;
	}

	static int depth (int packed, int plane) {
		return (packed >>> shift (plane)) & DEPTH_MASK;
	}

	static int indexOf (Display d, Control c) {
		for (int i = 0; i < d.batchCount; i++) {
			if (d.batchControls [i] == c) {
				return i;
			}
		}
		return -1;
	}

	static int entry (Display d, Control c) {
		int index = indexOf (d, c);
		if (index >= 0) {
			return index;
		}
		if (d.batchCount == d.batchControls.length) {
			int n = d.batchCount * 2;
			Control [] controls = new Control [n];
			int [] depths = new int [n], flags = new int [n];
			ViewportDamageRegion [] damage = new ViewportDamageRegion [n];
			System.arraycopy (d.batchControls, 0, controls, 0, d.batchCount);
			System.arraycopy (d.batchDepths, 0, depths, 0, d.batchCount);
			System.arraycopy (d.batchFlags, 0, flags, 0, d.batchCount);
			System.arraycopy (d.batchDamage, 0, damage, 0, d.batchCount);
			d.batchControls = controls;
			d.batchDepths = depths;
			d.batchFlags = flags;
			d.batchDamage = damage;
		}
		index = d.batchCount++;
		d.batchControls [index] = c;
		d.batchDepths [index] = 0;
		d.batchFlags [index] = 0;
		return index;
	}

	static void remove (Display d, int index) {
		int last = --d.batchCount;
		d.batchControls [index] = d.batchControls [last];
		d.batchDepths [index] = d.batchDepths [last];
		d.batchFlags [index] = d.batchFlags [last];
		ViewportDamageRegion region = d.batchDamage [index];
		d.batchDamage [index] = d.batchDamage [last];
		d.batchDamage [last] = region;
		d.batchControls [last] = null;
	}

	/** Begins the planes; nested-safe. */
	static boolean begin (Control c, int planes) {
		Display d = c.display;
		int index = entry (d, c);
		for (int plane = PLANE_MAIN; plane <= PLANE_SCROLLBARS; plane <<= 1) {
			if ((planes & plane) == 0) {
				continue;
			}
			int depth = depth (d.batchDepths [index], plane);
			if (depth == DEPTH_MASK) {
				continue;
			}
			d.batchDepths [index] += 1 << shift (plane);
			if (depth == 0) {
				enter (d, index, c, plane);
			}
		}
		if (d.batchDepths [index] == 0) {
			remove (d, index);
		}
		return true;
	}

	/** Ends the planes; the last end of a plane repaints it once. */
	static boolean end (Control c, int planes) {
		Display d = c.display;
		int index = indexOf (d, c);
		if (index < 0) {
			return true;
		}
		for (int plane = PLANE_MAIN; plane <= PLANE_SCROLLBARS; plane <<= 1) {
			if ((planes & plane) == 0 || depth (d.batchDepths [index], plane) == 0) {
				continue;
			}
			d.batchDepths [index] -= 1 << shift (plane);
			if (depth (d.batchDepths [index], plane) == 0) {
				leave (d, index, c, plane);
				if (c.isDisposed ()) {
					break;
				}
			}
		}
		index = indexOf (d, c);
		if (index >= 0 && (d.batchDepths [index] == 0 || c.isDisposed ())) {
			if (depth (d.batchDepths [index], PLANE_VIEWPORT) > 0) {
				d.batchViewports--;
			}
			if (depth (d.batchDepths [index], PLANE_SCROLLBARS) > 0) {
				d.batchScrollbars--;
			}
			remove (d, index);
		}
		return true;
	}

	static void enter (Display d, int index, Control c, int plane) {
		switch (plane) {
			case PLANE_MAIN -> {
				if (c instanceof Composite composite) {
					composite.setLayoutDeferred (true);
				}
			}
			case PLANE_VIEWPORT -> {
				if (d.batchDamage [index] == null) {
					d.batchDamage [index] = new ViewportDamageRegion ();
				}
				d.batchDamage [index].clear ();
				d.batchFlags [index] &= ~FLAG_ALL_CHILDREN;
				d.batchViewports++;
			}
			default -> d.batchScrollbars++;
		}
	}

	static void leave (Display d, int index, Control c, int plane) {
		switch (plane) {
			case PLANE_MAIN -> {
				if (c instanceof Composite composite) {
					composite.setLayoutDeferred (false);
				}
			}
			case PLANE_VIEWPORT -> {
				d.batchViewports--;
				flushViewport (c, d.batchDamage [index], (d.batchFlags [index] & FLAG_ALL_CHILDREN) != 0);
			}
			default -> {
				d.batchScrollbars--;
				long hwnd = c instanceof Scrollable scrollable ? scrollable.scrolledHandle () : c.topHandle ();
				OS.RedrawWindow (hwnd, null, 0, OS.RDW_FRAME | OS.RDW_INVALIDATE);
			}
		}
	}

	static void flushViewport (Control c, ViewportDamageRegion region, boolean allChildren) {
		if (region == null || region.isEmpty ()) {
			return;
		}
		int flags = OS.RDW_INVALIDATE | (allChildren ? OS.RDW_ALLCHILDREN : 0);
		if (region.overflowed ()) {
			OS.RedrawWindow (c.handle, null, 0, flags);
		} else {
			RECT rect = c.display.boxClient;
			for (int i = 0; i < region.rectCount (); i++) {
				OS.SetRect (rect, region.x (i), region.y (i), region.x (i) + region.width (i), region.y (i) + region.height (i));
				OS.RedrawWindow (c.handle, rect, 0, flags);
			}
		}
		region.clear ();
	}

	static ViewportDamageRegion viewportDamage (Control c) {
		Display d = c.display;
		int index = indexOf (d, c);
		return index >= 0 && depth (d.batchDepths [index], PLANE_VIEWPORT) > 0 ? d.batchDamage [index] : null;
	}

	/** WM_PAINT of a control whose viewport is frozen: record the update region, paint only the frame. */
	static boolean absorbPaint (Control c) {
		ViewportDamageRegion region = viewportDamage (c);
		if (region == null) {
			return false;
		}
		Display d = c.display;
		if (d.batchRgn == 0) {
			d.batchRgn = OS.CreateRectRgn (0, 0, 0, 0);
		}
		if (OS.GetUpdateRgn (c.handle, d.batchRgn, false) > OS.NULLREGION) {
			int bytes = OS.GetRegionData (d.batchRgn, 0, null);
			if (bytes > 0) {
				if (d.damageData.length < (bytes + 3) / 4) {
					d.damageData = new int [(bytes + 3) / 4];
				}
				int [] data = d.damageData;
				OS.GetRegionData (d.batchRgn, bytes, data);
				for (int k = 0; k < data [2]; k++) {
					int base = 8 + k * 4;
					region.add (data [base], data [base + 1], data [base + 2] - data [base], data [base + 3] - data [base + 1]);
				}
			}
		}
		Control previous = d.batchAbsorbing;
		d.batchAbsorbing = c;
		try {
			PAINTSTRUCT ps = d.visPs;
			OS.BeginPaint (c.handle, ps);
			OS.EndPaint (c.handle, ps);
		} finally {
			d.batchAbsorbing = previous;
		}
		return true;
	}

	/** redraw of a control whose viewport is frozen: record instead of invalidating. */
	static boolean captureRedraw (Control c, RECT rect, boolean all) {
		ViewportDamageRegion region = viewportDamage (c);
		if (region == null) {
			return false;
		}
		if (rect == null) {
			RECT client = c.display.boxClient;
			OS.GetClientRect (c.handle, client);
			region.add (client.left, client.top, client.right - client.left, client.bottom - client.top);
		} else {
			region.add (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
		}
		if (all) {
			Display d = c.display;
			d.batchFlags [indexOf (d, c)] |= FLAG_ALL_CHILDREN;
		}
		return true;
	}

	static boolean scrollbarsFrozen (Control c) {
		Display d = c.display;
		int index = indexOf (d, c);
		return index >= 0 && depth (d.batchDepths [index], PLANE_SCROLLBARS) > 0;
	}

	/** Display.release. */
	static void release (Display d) {
		if (d.batchRgn != 0) {
			OS.DeleteObject (d.batchRgn);
			d.batchRgn = 0;
		}
		for (int i = 0; i < d.batchCount; i++) {
			d.batchControls [i] = null;
		}
		d.batchCount = d.batchViewports = d.batchScrollbars = 0;
	}
}

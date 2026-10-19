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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Logical bounds of a control whose bounds leave the coordinate range of native child windows
 * (docs/m3/SCROLL_MEDIATED_VIEWPORT.md).
 * <p>
 * Windows clamps a child window's position to [-32768, 32767] and its size to 65535 pixels, so
 * stock SWT answers clamped bounds and cannot show the end of a large scrolled content. A control
 * that leaves that range keeps its bounds here: a <em>parked</em> control (small, far away) is
 * clamped natively and invisible; a <em>plane</em> (larger than {@link ViewportPlaneGeometry#BAND})
 * has a native window that covers only a band of its logical extent around the part its parent can
 * show, starting at its <em>base</em>. Children of a plane are placed natively at
 * <code>logical - base</code>. Controls that fit natively never get an instance and never reach the
 * methods below, except through a one-reference test.
 * </p>
 * <p>
 * A parent that defers hidden children ({@link Composite#defersHiddenChildren}, the ExpandBar viewport) also
 * gets <em>deferred</em> children: a child moved while both its native rectangle and its new logical one lie off
 * the parent's client area keeps its native window where it is, invisible, and only its logical position moves
 * (with the Move event the native move would have sent). It is placed natively again by its next move that is
 * not deferred (onto the client area, or with a size change), or when a resize or a native scroll of the parent
 * brings either rectangle onto the client area ({@link #exposeDeferred}).
 * </p>
 */
//FLLDCIM: HPCPOP - controls that fit natively keep plane == null; every hook is one null test
final class ViewportPlane {
	/** The mediation is on unless <code>-Dswt.widget.mode.Composite=original</code> (or the global mode) asks for stock geometry. */
	static final boolean ENABLED = WidgetMode.of ("Composite", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;

	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.Composite");
	/* a rebase moves windows without copying or invalidating pixels: every screen pixel stays where it is (measured, RebaseProbe) */
	private static final int QUIET_MOVE = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_NOREDRAW | OS.SWP_NOCOPYBITS;

	/** Per-display scratch of the mediation (reused; no allocation per call). */
	static final class Scratch {
		final RECT rect = new RECT (), nativeRect = new RECT (), client = new RECT ();
		long rgn;
		GC paintGC;
		int offsetX, offsetY;
	}

	/** Logical bounds in pixels, relative to the parent's logical client area. */
	int x, y, width, height;
	/** The logical coordinate shown at native client coordinate 0; non-zero only for a plane. */
	int baseX, baseY;
	/** True while the native window was left off the parent's client area and only the logical position moved ({@link #defer}). */
	boolean deferred;

	/* the flags of a move a deferral may skip natively: no size, z-order, show or hide change */
	private static final int DEFERRABLE = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_DRAWFRAME | OS.SWP_NOCOPYBITS;

	/**
	 * True when <code>setBoundsInPixels</code> must go through {@link #setBounds}: the control is
	 * already mediated, its parent is a plane with a base, or the new bounds do not fit natively.
	 */
	static boolean mediates (Control control, int x, int y, int width, int height, int flags) {
		if (control.plane != null) return true;
		Composite parent = control.parent;
		if (parent == null || control instanceof Decorations) return false;
		ViewportPlane parentPlane = parent.plane;
		if (parentPlane != null && (parentPlane.baseX != 0 || parentPlane.baseY != 0)) return true;
		boolean moves = (flags & OS.SWP_NOMOVE) == 0, sizes = (flags & OS.SWP_NOSIZE) == 0;
		return moves && !(ViewportPlaneGeometry.fitsPosition (x) && ViewportPlaneGeometry.fitsPosition (y))
			|| sizes && (width > ViewportPlaneGeometry.BAND || height > ViewportPlaneGeometry.BAND)
			|| parent.defersHiddenChildren () && defers (control, x, y, width, height, flags);
	}

	/**
	 * True when a move can leave the native window where it is ({@link #defer}): the parent defers hidden children
	 * and has no open batch of deferred window positions, neither the control nor its parent has a base, the size
	 * stays, and both the native rectangle and the new logical one lie off the parent's client area, so no pixel and
	 * no hit test depends on the native position.
	 */
	static boolean defers (Control control, int x, int y, int width, int height, int flags) {
		Composite parent = control.parent;
		if (parent == null || control instanceof Decorations || !parent.defersHiddenChildren () || parent.lpwp != null) return false;
		if ((flags & OS.SWP_NOMOVE) != 0 || (flags & ~DEFERRABLE) != 0 || (flags & OS.SWP_NOZORDER) == 0) return false;
		ViewportPlane parentPlane = parent.plane, plane = control.plane;
		if (parentPlane != null && (parentPlane.baseX != 0 || parentPlane.baseY != 0)) return false;
		if (plane != null && (plane.baseX != 0 || plane.baseY != 0)) return false;
		Scratch scratch = scratch (control.display);
		RECT nativeRect = scratch.nativeRect, client = scratch.client;
		nativeBounds (control, nativeRect);
		int nativeWidth = nativeRect.right - nativeRect.left, nativeHeight = nativeRect.bottom - nativeRect.top;
		if (plane != null && (plane.width != nativeWidth || plane.height != nativeHeight)) return false;
		if ((flags & OS.SWP_NOSIZE) == 0 && (width != nativeWidth || height != nativeHeight)) return false;
		OS.GetClientRect (parent.handle, client);
		return !meets (nativeRect.left, nativeRect.top, nativeRect.right, nativeRect.bottom, client)
			&& !meets (x, y, (long) x + nativeWidth, (long) y + nativeHeight, client);
	}

	/* whether a rectangle shares an area with the client rectangle */
	private static boolean meets (long left, long top, long right, long bottom, RECT client) {
		return left < client.right && right > client.left && top < client.bottom && bottom > client.top;
	}

	/* a deferred move: the logical position only, then the Move event the native move would have sent */
	private static void defer (Control control, int x, int y) {
		ViewportPlane plane = control.plane;
		if (plane == null) {
			RECT nativeRect = scratch (control.display).nativeRect;
			nativeBounds (control, nativeRect);
			plane = control.plane = new ViewportPlane ();
			plane.x = nativeRect.left;
			plane.y = nativeRect.top;
			plane.width = nativeRect.right - nativeRect.left;
			plane.height = nativeRect.bottom - nativeRect.top;
		}
		plane.deferred = true;
		if (plane.x == x && plane.y == y) return;
		plane.x = x;
		plane.y = y;
		if (LOG.isTraceEnabled ()) LOG.trace (() -> control + " deferred at logical " + x + "," + y);
		moved (control);
	}

	/** <code>Control.setBoundsInPixels</code> of a mediated control, in logical pixels. */
	static void setBounds (Control control, int x, int y, int width, int height, int flags, boolean defer) {
		if (control.parent.defersHiddenChildren () && defers (control, x, y, width, height, flags)) {
			defer (control, x, y);
			return;
		}
		ViewportPlane parentPlane = control.parent.plane;
		int parentX = parentPlane == null ? 0 : parentPlane.baseX, parentY = parentPlane == null ? 0 : parentPlane.baseY;
		RECT current = currentBounds (control, parentX, parentY);
		boolean moves = (flags & OS.SWP_NOMOVE) == 0, sizes = (flags & OS.SWP_NOSIZE) == 0;
		int newX = moves ? x : current.left, newY = moves ? y : current.top;
		int newWidth = sizes ? width : current.right - current.left, newHeight = sizes ? height : current.bottom - current.top;
		boolean fits = newWidth <= ViewportPlaneGeometry.BAND && newHeight <= ViewportPlaneGeometry.BAND
			&& ViewportPlaneGeometry.fitsPosition ((long) newX - parentX) && ViewportPlaneGeometry.fitsPosition ((long) newY - parentY);
		if (fits && control.plane == null) {
			/* an in-band child of a plane: the stock path, translated by the parent's base */
			Display display = control.display;
			display.planeDepth++;
			try {
				control.setBoundsInPixels (newX - parentX, newY - parentY, newWidth, newHeight, flags, defer);
			} finally {
				display.planeDepth--;
			}
			return;
		}
		if (control.plane == null) {
			ViewportPlane plane = control.plane = new ViewportPlane ();
			plane.x = current.left;
			plane.y = current.top;
			plane.width = current.right - current.left;
			plane.height = current.bottom - current.top;
		}
		place (control, newX, newY, newWidth, newHeight, parentX, parentY, flags, fits);
	}

	/* the logical bounds of a control: its record, or its native bounds plus its parent's base (in the display scratch) */
	private static RECT currentBounds (Control control, int parentX, int parentY) {
		RECT current = scratch (control.display).rect;
		ViewportPlane plane = control.plane;
		if (plane != null) {
			OS.SetRect (current, plane.x, plane.y, plane.x + plane.width, plane.y + plane.height);
		} else {
			control.forceResize ();
			nativeBounds (control, current);
			OS.OffsetRect (current, parentX, parentY);
		}
		return current;
	}

	/*
	 * Places a plane or parked control: rebase at the old logical position when its band moves, then the
	 * stock native move (a blit plus the exposed strip) without native events, then Move and Resize from
	 * the logical change.
	 */
	private static void place (Control control, int x, int y, int width, int height, int parentX, int parentY, int flags, boolean fits) {
		Display display = control.display;
		ViewportPlane plane = control.plane;
		RECT client = scratch (display).rect;
		OS.GetClientRect (control.parent.handle, client);
		int baseX = ViewportPlaneGeometry.base (x, width, parentX, client.right - client.left, plane.baseX);
		int baseY = ViewportPlaneGeometry.base (y, height, parentY, client.bottom - client.top, plane.baseY);
		if (baseX != plane.baseX || baseY != plane.baseY) rebase (control, plane, baseX, baseY, parentX, parentY);
		int oldX = plane.x, oldY = plane.y, oldWidth = plane.width, oldHeight = plane.height;
		plane.x = x;
		plane.y = y;
		plane.width = width;
		plane.height = height;
		int nativeX = ViewportPlaneGeometry.nativePosition (x, baseX, parentX), nativeY = ViewportPlaneGeometry.nativePosition (y, baseY, parentY);
		int nativeWidth = ViewportPlaneGeometry.nativeExtent (width), nativeHeight = ViewportPlaneGeometry.nativeExtent (height);
		display.planeDepth++;
		display.planeQuiet++;
		try {
			int nativeFlags = flags & ~(OS.SWP_NOMOVE | OS.SWP_NOSIZE);
			if (control instanceof Combo && (control.style & SWT.DROP_DOWN) != 0) {
				/* as Combo.setBoundsInPixels: the height is the drop-down list's, applied by SetWindowPos directly */
				OS.SetWindowPos (control.handle, 0, nativeX, nativeY, nativeWidth, nativeHeight, nativeFlags);
			} else {
				control.setBoundsInPixels (nativeX, nativeY, nativeWidth, nativeHeight, nativeFlags, false);
			}
		} finally {
			display.planeQuiet--;
			display.planeDepth--;
		}
		plane.deferred = false;
		if (control.isDisposed ()) return;
		adoptNativeSize (control, plane);
		if (fits && baseX == 0 && baseY == 0) control.plane = null;
		if (LOG.isTraceEnabled ()) LOG.trace (() -> control + " logical " + x + "," + y + " " + width + "x" + height
			+ " native " + nativeX + "," + nativeY + " " + nativeWidth + "x" + nativeHeight + " base " + baseX + "," + baseY);
		if (plane.x != oldX || plane.y != oldY) {
			moved (control);
			if (control.isDisposed ()) return;
		}
		if (plane.width != oldWidth || plane.height != oldHeight) resized (control);
	}

	/*
	 * On an axis where the control is not a plane its native size is not clamped, so the native size is the
	 * answer stock gives (a drop-down Combo sets the height of its text field, not the requested one).
	 */
	private static void adoptNativeSize (Control control, ViewportPlane plane) {
		RECT rect = scratch (control.display).rect;
		OS.GetWindowRect (control.topHandle (), rect);
		if (plane.width <= ViewportPlaneGeometry.BAND) plane.width = rect.right - rect.left;
		if (plane.height <= ViewportPlaneGeometry.BAND) plane.height = rect.bottom - rect.top;
	}

	/* the work of WM_MOVE for a logical move: Move event; children redrawn when a background image or theme moves with it */
	private static void moved (Control control) {
		if (control.findImageControl () != null) {
			if (control != control.getShell ()) control.redrawChildren ();
		} else if ((control.state & Widget.THEME_BACKGROUND) != 0 && OS.IsWindowVisible (control.handle) && control.findThemeControl () != null) {
			control.redrawChildren ();
		}
		control.sendMove ();
	}

	/* the work of WM_SIZE for a logical resize: Resize event, Composite layout, CANVAS repaint */
	private static void resized (Control control) {
		control.sendResize ();
		if (control.isDisposed ()) return;
		if ((control.state & Widget.CANVAS) != 0 && (control.style & SWT.NO_REDRAW_RESIZE) == 0 && control.hooks (SWT.Paint) && OS.IsWindowVisible (control.handle)) {
			OS.InvalidateRect (control.handle, null, true);
		}
	}

	/*
	 * Moves the band of a plane to a new base at its current logical position: the plane moves by the
	 * base change and its children by its opposite, without copying or invalidating pixels, so the
	 * screen does not change; a pending update region moves with the content.
	 */
	private static void rebase (Control control, ViewportPlane plane, int baseX, int baseY, int parentX, int parentY) {
		Display display = control.display;
		int dx = baseX - plane.baseX, dy = baseY - plane.baseY;
		Scratch scratch = scratch (display);
		if (scratch.rgn == 0) scratch.rgn = OS.CreateRectRgn (0, 0, 0, 0);
		boolean pending = OS.GetUpdateRgn (control.handle, scratch.rgn, false) > OS.NULLREGION;
		display.planeQuiet++;
		try {
			if (control instanceof Composite composite) {
				for (Control child : composite._getChildren ()) {
					rebaseChild (child, plane.baseX, plane.baseY, baseX, baseY);
				}
			}
			int nativeX = ViewportPlaneGeometry.nativePosition (plane.x, baseX, parentX), nativeY = ViewportPlaneGeometry.nativePosition (plane.y, baseY, parentY);
			OS.SetWindowPos (control.topHandle (), 0, nativeX, nativeY, 0, 0, QUIET_MOVE);
		} finally {
			display.planeQuiet--;
		}
		plane.baseX = baseX;
		plane.baseY = baseY;
		if (pending) {
			OS.ValidateRect (control.handle, null);
			OS.OffsetRgn (scratch.rgn, -dx, -dy);
			OS.InvalidateRgn (control.handle, scratch.rgn, true);
		}
		LOG.debug (() -> control + " rebased by " + dx + "," + dy + " to " + baseX + "," + baseY);
	}

	/* places a child of a rebased plane natively relative to the new base; parks it when it leaves the native range */
	private static void rebaseChild (Control child, int oldBaseX, int oldBaseY, int baseX, int baseY) {
		ViewportPlane plane = child.plane;
		int logicalX, logicalY, ownX = 0, ownY = 0;
		RECT rect = scratch (child.display).rect;
		if (plane != null) {
			logicalX = plane.x;
			logicalY = plane.y;
			ownX = plane.baseX;
			ownY = plane.baseY;
		} else {
			child.forceResize ();
			nativeBounds (child, rect);
			logicalX = rect.left + oldBaseX;
			logicalY = rect.top + oldBaseY;
			if (!ViewportPlaneGeometry.fitsPosition ((long) logicalX - baseX) || !ViewportPlaneGeometry.fitsPosition ((long) logicalY - baseY)) {
				plane = child.plane = new ViewportPlane ();
				plane.x = logicalX;
				plane.y = logicalY;
				plane.width = rect.right - rect.left;
				plane.height = rect.bottom - rect.top;
			}
		}
		int nativeX = ViewportPlaneGeometry.nativePosition (logicalX, ownX, baseX), nativeY = ViewportPlaneGeometry.nativePosition (logicalY, ownY, baseY);
		OS.SetWindowPos (child.topHandle (), 0, nativeX, nativeY, 0, 0, QUIET_MOVE);
		if (plane != null) plane.deferred = false;
		if (plane != null && ownX == 0 && ownY == 0 && plane.width <= ViewportPlaneGeometry.BAND && plane.height <= ViewportPlaneGeometry.BAND
				&& ViewportPlaneGeometry.fitsPosition ((long) logicalX - baseX) && ViewportPlaneGeometry.fitsPosition ((long) logicalY - baseY)) {
			child.plane = null;
		}
	}

	/* the native rectangle of a control's top window in its parent's native client coordinates */
	private static void nativeBounds (Control control, RECT rect) {
		OS.GetWindowRect (control.topHandle (), rect);
		OS.MapWindowPoints (0, control.parent.handle, rect, 2);
	}

	/** True when the bounds of the control are answered here: it is mediated or its parent has a base. */
	static boolean answers (Control control) {
		if (control.plane != null) return true;
		ViewportPlane parentPlane = control.parent == null ? null : control.parent.plane;
		return parentPlane != null && (parentPlane.baseX != 0 || parentPlane.baseY != 0);
	}

	/** Logical bounds in pixels (<code>getBoundsInPixels</code>). */
	static Rectangle getBounds (Control control) {
		ViewportPlane plane = control.plane;
		if (plane != null) return new Rectangle (plane.x, plane.y, plane.width, plane.height);
		control.forceResize ();
		RECT rect = new RECT ();
		nativeBounds (control, rect);
		ViewportPlane parentPlane = control.parent.plane;
		return new Rectangle (rect.left + parentPlane.baseX, rect.top + parentPlane.baseY, rect.right - rect.left, rect.bottom - rect.top);
	}

	/** Logical location in pixels (<code>getLocationInPixels</code>). */
	static Point getLocation (Control control) {
		Rectangle bounds = getBounds (control);
		return new Point (bounds.x, bounds.y);
	}

	/** Logical size in pixels (<code>getSizeInPixels</code>); only for a mediated control. */
	static Point getSize (Control control) {
		ViewportPlane plane = control.plane;
		return new Point (plane.width, plane.height);
	}

	/** Logical client area of a mediated control: the native client area grown by the logical size beyond the native one. */
	static Rectangle getClientArea (Scrollable control, Rectangle nativeArea) {
		ViewportPlane plane = control.plane;
		RECT rect = new RECT ();
		OS.GetWindowRect (control.topHandle (), rect);
		nativeArea.width += plane.width - (rect.right - rect.left);
		nativeArea.height += plane.height - (rect.bottom - rect.top);
		return nativeArea;
	}

	/*
	 * Offset from a logical client point of the control to the native client point that native mapping
	 * must start from: minus the control's own base, plus the clamping error (intended minus actual
	 * native position) of the control and of every ancestor, so that native mapping, a translation,
	 * lands where the logical bounds say. Stored in the display scratch.
	 */
	private static Scratch offset (Control control) {
		Scratch scratch = scratch (control.display);
		long x = 0, y = 0;
		ViewportPlane own = control.plane;
		if (own != null) {
			x -= own.baseX;
			y -= own.baseY;
		}
		for (Control c = control; c != null && !(c instanceof Decorations); c = c.parent) {
			ViewportPlane plane = c.plane;
			if (plane == null) continue;
			if (plane.deferred) {
				/* no base on either side: the native window is where it was left, not at the clamped position */
				RECT nativeRect = new RECT ();
				nativeBounds (c, nativeRect);
				x += (long) plane.x - nativeRect.left;
				y += (long) plane.y - nativeRect.top;
				continue;
			}
			ViewportPlane parentPlane = c.parent == null ? null : c.parent.plane;
			long intendedX = (long) plane.x + plane.baseX - (parentPlane == null ? 0 : parentPlane.baseX);
			long intendedY = (long) plane.y + plane.baseY - (parentPlane == null ? 0 : parentPlane.baseY);
			x += intendedX - ViewportPlaneGeometry.clamp (intendedX);
			y += intendedY - ViewportPlaneGeometry.clamp (intendedY);
		}
		scratch.offsetX = (int) x;
		scratch.offsetY = (int) y;
		return scratch;
	}

	/** A logical client point of the control to the native client point for native mapping. */
	static void toNative (Control control, POINT point) {
		Scratch offset = offset (control);
		point.x += offset.offsetX;
		point.y += offset.offsetY;
	}

	/** A native client point of the control, from native mapping, to its logical client point. */
	static void toLogical (Control control, POINT point) {
		Scratch offset = offset (control);
		point.x -= offset.offsetX;
		point.y -= offset.offsetY;
	}

	/** {@link #toNative(Control, POINT)} for both corners of a rectangle. */
	static void toNative (Control control, RECT rect) {
		Scratch offset = offset (control);
		OS.OffsetRect (rect, offset.offsetX, offset.offsetY);
	}

	/** {@link #toLogical(Control, POINT)} for both corners of a rectangle. */
	static void toLogical (Control control, RECT rect) {
		Scratch offset = offset (control);
		OS.OffsetRect (rect, -offset.offsetX, -offset.offsetY);
	}

	/** A logical redraw rectangle of a plane as a new native rectangle. */
	static RECT redrawRect (Control control, RECT rect) {
		ViewportPlane plane = control.plane;
		RECT result = new RECT ();
		OS.SetRect (result, rect.left - plane.baseX, rect.top - plane.baseY, rect.right - plane.baseX, rect.bottom - plane.baseY);
		return result;
	}

	/** A device context of a plane obtained outside of painting (<code>new GC (control)</code>): drawing in logical coordinates. */
	static void shiftDC (Control control, long hDC) {
		ViewportPlane plane = control.plane;
		POINT origin = new POINT ();
		OS.GetWindowOrgEx (hDC, origin);
		OS.SetWindowOrgEx (hDC, origin.x + plane.baseX, origin.y + plane.baseY, null);
	}

	/**
	 * A Composite was resized natively. A child plane keeps the parent's client area inside its band only up
	 * to {@link ViewportPlaneGeometry#VISIBLE_CAP}; a parent larger than that re-places its planes so that
	 * their bands follow the new size. Logical bounds do not change, so no event is sent.
	 */
	static void parentResized (Composite parent) {
		if (parent.defersHiddenChildren ()) exposeDeferred (parent);
		RECT client = scratch (parent.display).rect;
		OS.GetClientRect (parent.handle, client);
		if (client.right - client.left <= ViewportPlaneGeometry.VISIBLE_CAP && client.bottom - client.top <= ViewportPlaneGeometry.VISIBLE_CAP) return;
		for (Control child : parent._getChildren ()) {
			ViewportPlane plane = child.plane;
			if (plane == null || plane.width <= ViewportPlaneGeometry.BAND && plane.height <= ViewportPlaneGeometry.BAND) continue;
			setBounds (child, plane.x, plane.y, plane.width, plane.height, OS.SWP_NOZORDER | OS.SWP_NOACTIVATE, false);
		}
	}

	/**
	 * Before a native scroll of a parent that defers hidden children (<code>ScrollWindowEx</code> with
	 * <code>SW_SCROLLCHILDREN</code> moves every child window by <code>dy</code>): every small child without a base
	 * whose native window is off the client area (a deferred child, or one parked beyond the native range) becomes
	 * deferred and moves logically by <code>dy</code> now, so that the Move event of the native move answers the new
	 * position as stock does. {@link #exposeDeferred} after the scroll places natively those that come into view.
	 */
	static void scrollDeferred (Composite parent, Control [] children, int dy) {
		ViewportPlane parentPlane = parent.plane;
		if (parentPlane != null && (parentPlane.baseX != 0 || parentPlane.baseY != 0)) return;
		Scratch scratch = scratch (parent.display);
		RECT nativeRect = scratch.nativeRect, client = scratch.client;
		OS.GetClientRect (parent.handle, client);
		for (Control child : children) {
			ViewportPlane plane = child.plane;
			if (plane == null || plane.baseX != 0 || plane.baseY != 0 || child.isDisposed ()) continue;
			if (!plane.deferred) {
				if (plane.width > ViewportPlaneGeometry.BAND || plane.height > ViewportPlaneGeometry.BAND) continue;
				nativeBounds (child, nativeRect);
				if (meets (nativeRect.left, nativeRect.top, nativeRect.right, nativeRect.bottom, client)) continue;
				plane.deferred = true;
			}
			plane.y += dy;
		}
	}

	/**
	 * Places natively every deferred child of the parent whose native or logical rectangle meets the parent's
	 * client area, after a native scroll or a resize of the parent. The logical bounds do not change, so no event.
	 */
	static void exposeDeferred (Composite parent) {
		if (parent.display.planeScratch == null) return;
		RECT nativeRect = new RECT (), client = new RECT ();
		for (Control child : parent._getChildren ()) {
			ViewportPlane plane = child.plane;
			if (plane == null || !plane.deferred || child.isDisposed ()) continue;
			OS.GetClientRect (parent.handle, client);
			nativeBounds (child, nativeRect);
			if (meets (nativeRect.left, nativeRect.top, nativeRect.right, nativeRect.bottom, client)
					|| meets (plane.x, plane.y, (long) plane.x + plane.width, (long) plane.y + plane.height, client)) {
				place (child, plane.x, plane.y, plane.width, plane.height, 0, 0, OS.SWP_NOZORDER | OS.SWP_NOACTIVATE,
					ViewportPlaneGeometry.fitsPosition (plane.x) && ViewportPlaneGeometry.fitsPosition (plane.y));
			}
		}
	}

	/** Mouse event of a plane: the location from the native client point plus the base, converted like stock. */
	static void mouse (Control control, Event event, long hwnd, long lParam, int zoom) {
		if (hwnd != control.handle) return;
		ViewportPlane plane = control.plane;
		event.setLocation (DPIUtil.pixelToPoint (OS.GET_X_LPARAM (lParam) + plane.baseX, zoom), DPIUtil.pixelToPoint (OS.GET_Y_LPARAM (lParam) + plane.baseY, zoom));
	}

	/**
	 * Paint event of a plane: the bounds of the native paint rectangle plus the base, converted like
	 * stock, and the GC's window origin moved by the base so that drawing is in logical coordinates.
	 */
	static void paint (Control control, Event event, int left, int top, int width, int height) {
		ViewportPlane plane = control.plane;
		event.setBounds (Win32DPIUtils.pixelToPoint (new Rectangle (left + plane.baseX, top + plane.baseY, width, height), control.getAutoscalingZoom ()));
		shiftOrigin (control, event.gc, plane.baseX, plane.baseY);
	}

	/** {@link #paint} for the buffered paint path, which converts the bounds as floating point. */
	static void paintBuffered (Control control, Event event, int left, int top, int width, int height) {
		ViewportPlane plane = control.plane;
		event.setBounds (Win32DPIUtils.pixelToPoint (new Rectangle.OfFloat (left + plane.baseX, top + plane.baseY, width, height), control.getAutoscalingZoom ()));
		shiftOrigin (control, event.gc, plane.baseX, plane.baseY);
	}

	/** Undoes the origin of the previous {@link #paint} of the same GC (one native paint, several regions). */
	static void unpaint (Control control, Event event) {
		Scratch scratch = scratch (control.display);
		if (scratch.paintGC != null && scratch.paintGC == event.gc) {
			ViewportPlane plane = control.plane;
			shiftOrigin (control, event.gc, -plane.baseX, -plane.baseY);
			scratch.paintGC = null;
		}
	}

	private static void shiftOrigin (Control control, GC gc, int dx, int dy) {
		if (gc == null || dx == 0 && dy == 0) return;
		POINT origin = new POINT ();
		OS.GetWindowOrgEx (gc.handle, origin);
		OS.SetWindowOrgEx (gc.handle, origin.x + dx, origin.y + dy, null);
		scratch (control.display).paintGC = gc;
	}

	static Scratch scratch (Display display) {
		Scratch scratch = display.planeScratch;
		if (scratch == null) scratch = display.planeScratch = new Scratch ();
		return scratch;
	}

	/** Display.release. */
	static void release (Display display) {
		Scratch scratch = display.planeScratch;
		if (scratch != null && scratch.rgn != 0) OS.DeleteObject (scratch.rgn);
		display.planeScratch = null;
	}
}

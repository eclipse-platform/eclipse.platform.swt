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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Win32 effective visibility as packed bits in {@link Widget#state}.
 * <p>
 * Bit allocation (win32 <code>Widget.state</code> bits 25..31 were free):
 * </p>
 * <ul>
 * <li>25 {@link #OCCLUDED}: shell fully covered or on another virtual desktop (shells only)</li>
 * <li>26 {@link #KNOWN}: {@link #OFFSCREEN} is current</li>
 * <li>27 {@link #OFFSCREEN}: hidden chain, minimized shell, zero size or clipped out by an ancestor</li>
 * <li>28..31 {@link #DIRTY_PAINT}, {@link #DIRTY_LAYOUT}, {@link #DIRTY_MEASURE},
 * {@link #DIRTY_DATA}: the {@link VisibilityGate} kinds shifted by {@link #KIND_SHIFT}</li>
 * </ul>
 * <p>
 * A known control answers with bit tests only. Cache invariant:
 * <code>KNOWN(c)</code> implies <code>KNOWN(c.parent)</code>, so invalidation
 * stops at the first unknown node. The cache is invalidated by
 * WM_WINDOWPOSCHANGED, WM_SHOWWINDOW, WM_SETREDRAW and setParent; every first
 * deferral also posts a sweep that recomputes from the OS, so a stale cache
 * cannot hold work back past the next event-loop turn.
 * </p>
 */
//FLLDCIM: HPCPOP - queries are bit tests; recompute is cold (one top-down pass per invalidation)
final class EffectiveVisibility {
	static final int OCCLUDED = 1 << 25;
	static final int KNOWN = 1 << 26;
	static final int OFFSCREEN = 1 << 27;
	static final int KIND_SHIFT = 28;
	static final int DIRTY_PAINT = VisibilityGate.PAINT << KIND_SHIFT;
	static final int DIRTY_LAYOUT = VisibilityGate.LAYOUT << KIND_SHIFT;
	static final int DIRTY_MEASURE = VisibilityGate.MEASURE << KIND_SHIFT;
	static final int DIRTY_DATA = VisibilityGate.DATA << KIND_SHIFT;
	static final int DIRTY_ALL = DIRTY_PAINT | DIRTY_LAYOUT | DIRTY_MEASURE | DIRTY_DATA;
	/** Kinds that an occluded (covered or cloaked) shell also gates. */
	static final int PAINT_KINDS = DIRTY_PAINT | DIRTY_DATA;
	static final int SWP_SHOWWINDOW = 0x40, SWP_HIDEWINDOW = 0x80;
	static final int GEOMETRY_FLAGS = OS.SWP_FRAMECHANGED | SWP_SHOWWINDOW | SWP_HIDEWINDOW;

	/** Kill switch: <code>-Dswt.visibility.gate=false</code> restores every stock path. */
	static final boolean ENABLED = !"false".equalsIgnoreCase (System.getProperty ("swt.visibility.gate")); //$NON-NLS-1$ //$NON-NLS-2$
	/** <code>-Dswt.visibility.occlusion=false</code> disables only the occlusion tracker. */
	static final boolean OCCLUSION = ENABLED && !"false".equalsIgnoreCase (System.getProperty ("swt.visibility.occlusion")); //$NON-NLS-1$ //$NON-NLS-2$

	static final VisibilityGate.Port PORT = new VisibilityGate.Port () {
		@Override
		protected boolean offscreen (Control control, boolean paint) {
			return ENABLED && (paint ? paintGated (control) : hidden (control));
		}

		@Override
		protected boolean defer (Control control, int kinds) {
			return ENABLED && deferKinds (control, kinds << KIND_SHIFT);
		}

		@Override
		protected boolean boxEdges (Control control, int [] out) {
			return ViewportBox.edges (control, out);
		}

		@Override
		protected boolean contentPixels (Control control, int [] out) {
			return ViewportBox.content (control, out);
		}

		@Override
		protected void setBoxEdges (Control control, int packedInset, int packedMargin) {
			ViewportBox.setInsets (control, packedInset, packedMargin);
		}

		@Override
		protected void attachDamageRecorder (Control control, ViewportDamageRecorder recorder) {
			ViewportDamageHook.attach (control, recorder);
		}

		@Override
		protected boolean debounce (Control control, int kind, int delayMs) {
			return VisibilityDebounce.schedule (control, kind, delayMs);
		}
	};

	private EffectiveVisibility () {
	}

	/* ---------------------------------------------------------------- queries */

	/** Hidden chain, minimized, zero size or clipped out. Gates layout and measure. */
	static boolean hidden (Control c) {
		if ((c.state & KNOWN) == 0) {
			recompute (c);
		}
		return (c.state & OFFSCREEN) != 0;
	}

	/** {@link #hidden} or the shell is occluded. Gates paint and data. */
	static boolean paintGated (Control c) {
		return hidden (c) || occluded (c);
	}

	static boolean occluded (Control c) {
		if (c.display.visOccludedShells <= 0) {
			return false;
		}
		Control p = c;
		while (p.parent != null && !(p instanceof Shell)) {
			p = p.parent;
		}
		return (p.state & OCCLUDED) != 0;
	}

	/**
	 * Recomputes KNOWN/OFFSCREEN for the chain from the shell down to the
	 * receiver in one top-down pass over screen rectangles. No allocation
	 * unless the reusable chain array must grow.
	 */
	static void recompute (Control c) {
		Display d = c.display;
		int n = 0;
		for (Control p = c; p != null; p = p instanceof Shell ? null : p.parent) {
			if (n == d.visChain.length) {
				Control [] grown = new Control [n * 2];
				System.arraycopy (d.visChain, 0, grown, 0, n);
				d.visChain = grown;
			}
			d.visChain [n++] = p;
		}
		RECT clip = d.visClip, r = d.visRect;
		Control root = d.visChain [n - 1];
		long top = root.topHandle ();
		OS.GetWindowRect (top, r);
		boolean off = !OS.IsWindowVisible (top) || OS.IsIconic (top) || r.right <= r.left || r.bottom <= r.top;
		if (!off) {
			screenClientRect (root.handle, clip);
			off = !intersect (clip, r);
		}
		root.state = (root.state & ~OFFSCREEN) | KNOWN | (off ? OFFSCREEN : 0);
		for (int i = n - 2; i >= 0; i--) {
			Control node = d.visChain [i];
			if (!off) {
				off = !styleVisible (node);
			}
			if (!off) {
				OS.GetWindowRect (node.topHandle (), r);
				off = !intersect (r, clip);
			}
			if (!off) {
				screenClientRect (node.handle, clip);
				off = !intersect (clip, r);
			}
			node.state = (node.state & ~OFFSCREEN) | KNOWN | (off ? OFFSCREEN : 0);
		}
		for (int i = 0; i < n; i++) {
			d.visChain [i] = null;
		}
	}

	static boolean styleVisible (Control node) {
		long top = node.topHandle ();
		if ((OS.GetWindowLong (top, OS.GWL_STYLE) & OS.WS_VISIBLE) == 0) {
			return false;
		}
		return top == node.handle || (OS.GetWindowLong (node.handle, OS.GWL_STYLE) & OS.WS_VISIBLE) != 0;
	}

	static void screenClientRect (long hwnd, RECT rect) {
		OS.GetClientRect (hwnd, rect);
		OS.MapWindowPoints (hwnd, 0, rect, 2);
		if (rect.left > rect.right) {
			int t = rect.left;
			rect.left = rect.right;
			rect.right = t;
		}
	}

	/** dst := dst intersect src; returns false when empty. */
	static boolean intersect (RECT dst, RECT src) {
		dst.left = Math.max (dst.left, src.left);
		dst.top = Math.max (dst.top, src.top);
		dst.right = Math.min (dst.right, src.right);
		dst.bottom = Math.min (dst.bottom, src.bottom);
		return dst.right > dst.left && dst.bottom > dst.top;
	}

	/* ----------------------------------------------------------- invalidation */

	/** Clears KNOWN on the receiver and every known descendant. */
	static void invalidate (Control c) {
		if ((c.state & KNOWN) == 0) {
			return;
		}
		c.state &= ~(KNOWN | OFFSCREEN);
		if (!(c instanceof Composite)) {
			return;
		}
		Display d = c.display;
		for (long h = OS.GetWindow (c.handle, OS.GW_CHILD); h != 0; h = OS.GetWindow (h, OS.GW_HWNDNEXT)) {
			Control child = d.getControl (h);
			if (child != null && child != c && child.parent == c) {
				invalidate (child);
			}
		}
	}

	/** WM_WINDOWPOSCHANGED on the receiver, before dispatch (DefWindowProc sends WM_SIZE/WM_MOVE from it). */
	static void windowPosChanged (Control c, long lParam) {
		WINDOWPOS pos = c.display.visPos;
		OS.MoveMemory (pos, lParam, WINDOWPOS.sizeof);
		int flags = pos.flags;
		boolean geometry = (flags & (OS.SWP_NOMOVE | OS.SWP_NOSIZE)) != (OS.SWP_NOMOVE | OS.SWP_NOSIZE) || (flags & GEOMETRY_FLAGS) != 0;
		if (c instanceof Shell shell && (geometry || (flags & OS.SWP_NOZORDER) == 0)) {
			clearOccluded (shell);
		}
		if (geometry) {
			invalidate (c);
			VisibilityCatchUp.requestSweep (c.display);
		}
	}

	/** WM_SHOWWINDOW (before the change) and WM_SETREDRAW (after the change). */
	static void visibilityToggled (Control c, boolean visible) {
		c.state |= KNOWN;
		invalidate (c);
		if (visible) {
			VisibilityCatchUp.requestSweep (c.display);
		}
	}

	/** Control.setParent: the cached chain no longer holds. */
	static void reparented (Control c) {
		visibilityToggled (c, true);
	}

	static void clearOccluded (Shell shell) {
		if ((shell.state & OCCLUDED) != 0) {
			shell.state &= ~OCCLUDED;
			shell.display.visOccludedShells--;
			VisibilityCatchUp.requestSweep (shell.display);
		}
	}

	static void setOccluded (Shell shell) {
		if ((shell.state & OCCLUDED) == 0) {
			shell.state |= OCCLUDED;
			shell.display.visOccludedShells++;
		}
	}

	/* ------------------------------------------------------------------ gates */

	/** redraw/redrawInPixels on a control whose WS_VISIBLE chain is set. */
	static boolean deferRedraw (Control c) {
		if (!paintGated (c)) {
			return false;
		}
		VisibilityCatchUp.markDirty (c, DIRTY_PAINT);
		return true;
	}

	/**
	 * WM_PAINT for the receiver's handle. BeginPaint/EndPaint (not ValidateRect)
	 * so the pending WM_NCPAINT still reaches the themed border code; only the
	 * client paint is skipped.
	 */
	static boolean deferPaintMessage (Control c) {
		if (!paintGated (c)) {
			return false;
		}
		PAINTSTRUCT ps = c.display.visPs;
		OS.BeginPaint (c.handle, ps);
		OS.EndPaint (c.handle, ps);
		VisibilityCatchUp.markDirty (c, DIRTY_PAINT);
		return true;
	}

	/** Composite.updateLayout after SWT's own setLayoutDeferred check. */
	static boolean deferLayout (Composite c) {
		if (c.display.visBypass > 0 || !hidden (c)) {
			VisibilityCatchUp.clearLayout (c);
			return false;
		}
		VisibilityCatchUp.markDirty (c, DIRTY_LAYOUT);
		return true;
	}

	/** Consumer kinds: LAYOUT/MEASURE only while hidden, PAINT/DATA also while occluded. */
	static boolean deferKinds (Control c, int bits) {
		if (hidden (c)) {
			VisibilityCatchUp.markDirty (c, bits);
			return true;
		}
		if ((bits & ~PAINT_KINDS) != 0 || !occluded (c)) {
			return false;
		}
		VisibilityCatchUp.markDirty (c, bits);
		return true;
	}

	/** Control.releaseWidget. */
	static void release (Control c) {
		VisibilityCatchUp.clearLayout (c);
		if ((c.state & OCCLUDED) != 0) {
			c.state &= ~OCCLUDED;
			c.display.visOccludedShells--;
		}
	}
}

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
 * Win32 effective-visibility gate. A control that is not effectively visible
 * records skipped paint and layout work as bits in {@link Widget#state} and
 * catches up once when it becomes visible.
 * <p>
 * Bit allocation (win32 <code>Widget.state</code> bits 25..31 were free):
 * 25 {@link #OCCLUDED} (shells only), 26 {@link #KNOWN}, 27 {@link #OFFSCREEN},
 * 28..31 {@link #DIRTY_PAINT}, {@link #DIRTY_LAYOUT}, {@link #DIRTY_MEASURE},
 * {@link #DIRTY_DATA}. The dirty bits are the {@link VisibilityGate} kinds
 * shifted by {@link #KIND_SHIFT}.
 * </p>
 * <p>
 * Cache invariant: <code>KNOWN(c)</code> implies <code>KNOWN(c.parent)</code>,
 * so invalidation stops at the first unknown node.
 * </p>
 */
//FLLDCIM: HPCPOP - queries are bit tests plus IsWindowVisible; recompute and catch-up are cold
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

	/** Kill switch: <code>-Dswt.visibility.gate=false</code> restores every stock path. */
	static final boolean ENABLED = !"false".equalsIgnoreCase (System.getProperty ("swt.visibility.gate")); //$NON-NLS-1$ //$NON-NLS-2$
	/** <code>-Dswt.visibility.occlusion=false</code> disables only the occlusion tracker. */
	static final boolean OCCLUSION = ENABLED && !"false".equalsIgnoreCase (System.getProperty ("swt.visibility.occlusion")); //$NON-NLS-1$ //$NON-NLS-2$

	static final int REDRAW_FLAGS = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;

	static final VisibilityGate.Port PORT = new VisibilityGate.Port () {
		@Override
		protected boolean offscreen (Control control, boolean paint) {
			return ENABLED && (paint ? paintGated (control) : hidden (control));
		}

		@Override
		protected boolean defer (Control control, int kinds) {
			return ENABLED && deferKinds (control, kinds << KIND_SHIFT);
		}
	};

	private EffectiveVisibility () {
	}

	/* ---------------------------------------------------------------- queries */

	/** Hidden chain (live), minimized, zero size or clipped out. Gates layout and measure. */
	static boolean hidden (Control c) {
		return !OS.IsWindowVisible (c.handle) || geometricOffscreen (c);
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

	static boolean geometricOffscreen (Control c) {
		if ((c.state & KNOWN) == 0) {
			recompute (c);
		}
		return (c.state & OFFSCREEN) != 0;
	}

	/**
	 * Recomputes KNOWN/OFFSCREEN for the chain from the shell down to the
	 * receiver in one top-down pass over screen rectangles.
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
		OS.GetWindowRect (root.topHandle (), r);
		boolean off = OS.IsIconic (root.handle) || r.right <= r.left || r.bottom <= r.top;
		if (!off) {
			screenClientRect (root.handle, clip);
			off = !intersect (clip, r);
		}
		root.state = (root.state & ~OFFSCREEN) | KNOWN | (off ? OFFSCREEN : 0);
		for (int i = n - 2; i >= 0; i--) {
			Control node = d.visChain [i];
			if (!off) {
				OS.GetWindowRect (node.topHandle (), r);
				off = !intersect (r, clip);
				if (!off) {
					screenClientRect (node.handle, clip);
					off = !intersect (clip, r);
				}
			}
			node.state = (node.state & ~OFFSCREEN) | KNOWN | (off ? OFFSCREEN : 0);
		}
		for (int i = 0; i < n; i++) {
			d.visChain [i] = null;
		}
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
		if (src.left > dst.left) {
			dst.left = src.left;
		}
		if (src.top > dst.top) {
			dst.top = src.top;
		}
		if (src.right < dst.right) {
			dst.right = src.right;
		}
		if (src.bottom < dst.bottom) {
			dst.bottom = src.bottom;
		}
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

	static final int SWP_SHOWWINDOW = 0x40, SWP_HIDEWINDOW = 0x80;
	static final int GEOMETRY_FLAGS = OS.SWP_FRAMECHANGED | SWP_SHOWWINDOW | SWP_HIDEWINDOW;

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
			requestSweep (c.display);
		}
	}

	static void clearOccluded (Shell shell) {
		if ((shell.state & OCCLUDED) != 0) {
			shell.state &= ~OCCLUDED;
			shell.display.visOccludedShells--;
			requestSweep (shell.display);
		}
	}

	static void setOccluded (Shell shell) {
		if ((shell.state & OCCLUDED) == 0) {
			shell.state |= OCCLUDED;
			shell.display.visOccludedShells++;
		}
	}

	/* ------------------------------------------------------------------ gates */

	static void markDirty (Control c, int bits) {
		int old = c.state;
		c.state = old | bits;
		if ((bits & DIRTY_LAYOUT) != 0 && (old & DIRTY_LAYOUT) == 0) {
			c.display.visPendingLayouts++;
		}
		if ((old & DIRTY_ALL) == 0) {
			Display d = c.display;
			if (d.visDirtyCount == d.visDirty.length) {
				Control [] grown = new Control [d.visDirtyCount * 2];
				System.arraycopy (d.visDirty, 0, grown, 0, d.visDirtyCount);
				d.visDirty = grown;
			}
			d.visDirty [d.visDirtyCount++] = c;
		}
	}

	static void clearLayout (Control c) {
		if ((c.state & DIRTY_LAYOUT) != 0) {
			c.state &= ~DIRTY_LAYOUT;
			c.display.visPendingLayouts--;
		}
	}

	/** redraw/redrawInPixels on a WS_VISIBLE control. */
	static boolean deferRedraw (Control c) {
		if (!geometricOffscreen (c) && !occluded (c)) {
			return false;
		}
		markDirty (c, DIRTY_PAINT);
		return true;
	}

	/** WM_PAINT for the receiver's handle. */
	static boolean deferPaintMessage (Control c) {
		if (!paintGated (c)) {
			return false;
		}
		OS.ValidateRect (c.handle, null);
		markDirty (c, DIRTY_PAINT);
		return true;
	}

	/** Composite.updateLayout after SWT's own setLayoutDeferred check. */
	static boolean deferLayout (Composite c) {
		if (c.display.visBypass > 0 || !hidden (c)) {
			clearLayout (c);
			return false;
		}
		markDirty (c, DIRTY_LAYOUT);
		return true;
	}

	static boolean deferKinds (Control c, int bits) {
		if (hidden (c)) {
			markDirty (c, bits);
			return true;
		}
		int paintBits = bits & PAINT_KINDS;
		if (paintBits == 0 || !occluded (c)) {
			return false;
		}
		if (paintBits != bits) {
			return false;
		}
		markDirty (c, paintBits);
		return true;
	}

	/* --------------------------------------------------------------- catch-up */

	static void requestSweep (Display d) {
		if (d.visDirtyCount == 0 || d.visSweepPosted || d.isDisposed ()) {
			return;
		}
		d.visSweepPosted = true;
		d.asyncExec (d.visSweepRunnable);
	}

	/** Posted sweep: catch up every enlisted control that is visible now. */
	static void sweep (Display d) {
		d.visSweepPosted = false;
		catchUp (d, null);
	}

	/** setVisible(true) / setRedraw(true): catch up the receiver's subtree now. */
	static void shown (Control c) {
		Display d = c.display;
		if (d.visDirtyCount == 0) {
			return;
		}
		invalidate (c);
		if (d.visSweeping) {
			requestSweep (d);
			return;
		}
		catchUp (d, c);
	}

	static void catchUp (Display d, Control root) {
		if (d.visSweeping) {
			return;
		}
		d.visSweeping = true;
		try {
			for (int i = 0; i < d.visDirtyCount; i++) {
				Control c = d.visDirty [i];
				if (c == null) {
					continue;
				}
				if (c.isDisposed () || (c.state & DIRTY_ALL) == 0) {
					d.visDirty [i] = null;
					continue;
				}
				if (root != null && !isAncestor (root, c)) {
					continue;
				}
				catchUpOne (c);
				if (c.isDisposed () || (c.state & DIRTY_ALL) == 0) {
					d.visDirty [i] = null;
				}
			}
		} finally {
			compact (d);
			d.visSweeping = false;
		}
	}

	static void catchUpOne (Control c) {
		boolean structural = hidden (c);
		int ready = structural ? 0 : (occluded (c) ? c.state & DIRTY_ALL & ~PAINT_KINDS : c.state & DIRTY_ALL);
		if (ready == 0) {
			return;
		}
		c.state &= ~(ready & ~DIRTY_LAYOUT);
		if ((ready & DIRTY_LAYOUT) != 0 && c instanceof Composite) {
			clearLayout (c);
			((Composite) c).updateLayout (true, true);
			if (c.isDisposed ()) {
				return;
			}
		} else {
			clearLayout (c);
		}
		if ((ready & DIRTY_PAINT) != 0) {
			OS.RedrawWindow (c.topHandle (), null, 0, REDRAW_FLAGS);
		}
		int kinds = (ready & DIRTY_ALL) >>> KIND_SHIFT;
		if (c.hooks (VisibilityGate.CATCH_UP)) {
			Event event = new Event ();
			event.detail = kinds;
			c.sendEvent (VisibilityGate.CATCH_UP, event);
		}
	}

	static boolean isAncestor (Control root, Control c) {
		for (Control p = c; p != null; p = p.parent) {
			if (p == root) {
				return true;
			}
			if (p instanceof Shell) {
				return false;
			}
		}
		return false;
	}

	static void compact (Display d) {
		int w = 0, pending = 0;
		for (int i = 0; i < d.visDirtyCount; i++) {
			Control c = d.visDirty [i];
			if (c != null && !c.isDisposed () && (c.state & DIRTY_ALL) != 0) {
				d.visDirty [w++] = c;
				if ((c.state & DIRTY_LAYOUT) != 0) {
					pending++;
				}
			}
		}
		for (int i = w; i < d.visDirtyCount; i++) {
			d.visDirty [i] = null;
		}
		d.visDirtyCount = w;
		d.visPendingLayouts = pending;
	}

	/* -------------------------------------------------- on-demand geometry */

	/**
	 * Runs deferred layouts that determine the receiver's geometry, outermost
	 * first, so public geometry queries return stock results.
	 *
	 * @param self <code>true</code> to include the receiver's own layout (print)
	 */
	static void flushGeometry (Control c, boolean self) {
		Display d = c.display;
		if (!ENABLED || d.visPendingLayouts <= 0) {
			return;
		}
		for (int guard = 0; guard < 64; guard++) {
			Control top = null;
			for (Control p = self ? c : c.parent; p != null; p = p.parent) {
				if ((p.state & DIRTY_LAYOUT) != 0) {
					top = p;
				}
				if (p instanceof Shell) {
					break;
				}
			}
			if (!(top instanceof Composite)) {
				return;
			}
			d.visBypass++;
			try {
				((Composite) top).updateLayout (true, true);
			} finally {
				d.visBypass--;
			}
			if (top.isDisposed () || c.isDisposed ()) {
				return;
			}
			clearLayout (top);
		}
	}

	/** Control.releaseWidget. */
	static void release (Control c) {
		clearLayout (c);
		if ((c.state & OCCLUDED) != 0) {
			c.state &= ~OCCLUDED;
			c.display.visOccludedShells--;
		}
	}
}

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

import static org.eclipse.swt.widgets.EffectiveVisibility.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Deferred-work list and the single catch-up pass of the visibility gate.
 * <p>
 * Every control that gains its first dirty bit is appended once to
 * <code>Display.visDirty</code> (reused array, compacted after each sweep). A
 * sweep is one posted <code>asyncExec</code>; at most one is outstanding. Each
 * entry is recomputed from the OS, and a visible entry runs its deferred layout
 * once (outermost first via <code>updateLayout(true, true)</code>), one
 * full invalidate, and one {@link VisibilityGate#CATCH_UP} event for consumer
 * kinds.
 * </p>
 */
//FLLDCIM: HPCPOP - cold path; runs once per visibility transition, not per update
final class VisibilityCatchUp {
	/** Client areas only: the gate never skips WM_NCPAINT, so frames are current. */
	static final int REDRAW_FLAGS = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;

	private VisibilityCatchUp () {
	}

	static void markDirty (Control c, int bits) {
		int old = c.state;
		c.state = old | bits;
		if ((bits & DIRTY_LAYOUT) != 0 && (old & DIRTY_LAYOUT) == 0) {
			c.display.visPendingLayouts++;
		}
		if ((old & DIRTY_ALL) == 0 && !enlisted (c.display, c)) {
			Display d = c.display;
			if (d.visDirtyCount == d.visDirty.length) {
				Control [] grown = new Control [d.visDirtyCount * 2];
				System.arraycopy (d.visDirty, 0, grown, 0, d.visDirtyCount);
				d.visDirty = grown;
			}
			d.visDirty [d.visDirtyCount++] = c;
			requestSweep (d);
		}
	}

	/** An entry whose bits were cleared outside a sweep stays until compaction; never append it twice. */
	static boolean enlisted (Display d, Control c) {
		for (int i = d.visDirtyCount - 1; i >= 0; i--) {
			if (d.visDirty [i] == c) {
				return true;
			}
		}
		return false;
	}

	static void clearLayout (Control c) {
		if ((c.state & DIRTY_LAYOUT) != 0) {
			c.state &= ~DIRTY_LAYOUT;
			c.display.visPendingLayouts--;
		}
	}

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
		recompute (c);
		if ((c.state & OFFSCREEN) != 0) {
			return;
		}
		int ready = occluded (c) ? c.state & DIRTY_ALL & ~PAINT_KINDS : c.state & DIRTY_ALL;
		if (ready == 0) {
			return;
		}
		c.state &= ~(ready & ~DIRTY_LAYOUT);
		clearLayout (c);
		if ((ready & DIRTY_LAYOUT) != 0 && c instanceof Composite composite) {
			composite.updateLayout (true, true);
			if (c.isDisposed ()) {
				return;
			}
		}
		if ((ready & DIRTY_PAINT) != 0) {
			OS.RedrawWindow (c.topHandle (), null, 0, REDRAW_FLAGS);
		}
		if (c.hooks (VisibilityGate.CATCH_UP)) {
			Event event = new Event ();
			event.detail = (ready & DIRTY_ALL) >>> KIND_SHIFT;
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

	/**
	 * Runs deferred layouts that determine the receiver's geometry, outermost
	 * first, so public geometry queries return stock results.
	 *
	 * @param self <code>true</code> to include the receiver's own layout (print, map)
	 */
	static void flushGeometry (Control c, boolean self) {
		Display d = c.display;
		if (!ENABLED || d.visPendingLayouts <= 0 || (!self && c instanceof Shell)) {
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
			if (!(top instanceof Composite composite)) {
				return;
			}
			d.visBypass++;
			try {
				composite.updateLayout (true, true);
			} finally {
				d.visBypass--;
			}
			if (top.isDisposed () || c.isDisposed ()) {
				return;
			}
			clearLayout (top);
		}
	}
}

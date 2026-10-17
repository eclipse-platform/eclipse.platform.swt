/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * The occlusion calculation adapts the algorithm of Chromium's
 * ui/aura/native_window_occlusion_tracker_win.cc and
 * ui/gfx/win/hwnd_util.cc IsWindowVisibleAndFullyOpaque
 * (commit 8255ebcb24fde9e699a1a73006bcda34049fea80),
 * Copyright 2018 The Chromium Authors, BSD-3-Clause.
 * See .m3/swt-visibility-gate-20261013/THIRD_PARTY_NOTICES.md.
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - viewport mode, visibility gate
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Background occlusion tracker for the top-level shells of one Display.
 * <p>
 * Each pass walks the desktop z-order front to back, subtracts fully opaque
 * occluders from this Display's shell client rectangles and reports the shells
 * whose remaining region is empty, or which sit on another virtual desktop.
 * Results are published as a <code>long[]</code> of HWNDs and applied on the UI
 * thread as {@link EffectiveVisibility#OCCLUDED} bits.
 * </p>
 * <p>
 * Natives missing from this SWT build fail closed (never report occlusion):
 * no DWMWA_CLOAKED (cloak-prone classes never occlude), no
 * DWMWA_EXTENDED_FRAME_BOUNDS (sizable occluders shrink by twice the frame),
 * no IVirtualDesktopManager (tracker stops).
 * </p>
 */
//FLLDCIM: HPCPOP - off the UI thread; one asyncExec per published change
final class OcclusionTracker implements Runnable {
	static final int FAST_MS = 100, SLOW_MS = 250;
	static final int SM_CXSIZEFRAME = 32, SM_CXPADDEDBORDER = 92;
	static final int IS_ON_CURRENT_DESKTOP = 3;
	static final char [] CLSID_VDM = "{aa509086-5ca9-4c25-8f95-589d3c07b48a}\0".toCharArray (); //$NON-NLS-1$
	static final char [] IID_VDM = "{a5cd92ff-29be-454c-8d04-d82879fb3f1b}\0".toCharArray (); //$NON-NLS-1$
	static final String [] NEVER_OCCLUDE = {"ApplicationFrameWindow", "Windows.UI.Core.CoreWindow"}; //$NON-NLS-1$ //$NON-NLS-2$

	final Display display;
	final int threadId;
	volatile boolean running = true;
	/** Published occluded HWNDs, replaced (never mutated) by the tracker thread. */
	volatile long [] published = new long [0];
	/** UI-thread copy of the last applied set. */
	long [] applied = new long [0];
	final Runnable apply = this::apply;

	/* tracker-thread scratch, reused every pass */
	long vdm, shape;
	final RECT rect = new RECT (), bounds = new RECT ();
	final char [] className = new char [258];
	final int [] intOut = new int [1], pid = new int [1], key = new int [1];
	final byte [] alpha = new byte [1];
	long [] ours = new long [8], occluded = new long [8], previous = new long [0];

	OcclusionTracker (Display display) {
		this.display = display;
		this.threadId = display.threadId;
	}

	static OcclusionTracker start (Display display) {
		OcclusionTracker tracker = new OcclusionTracker (display);
		Thread thread = new Thread (tracker, "SWT Occlusion Tracker"); //$NON-NLS-1$
		thread.setDaemon (true);
		thread.start ();
		return tracker;
	}

	void stop () {
		running = false;
	}

	@Override
	public void run () {
		OS.OleInitialize (0);
		long covered = OS.CreateRectRgn (0, 0, 0, 0), tmp = OS.CreateRectRgn (0, 0, 0, 0);
		shape = OS.CreateRectRgn (0, 0, 0, 0);
		try {
			if (!createVirtualDesktopManager ()) {
				return;
			}
			while (running) {
				int count = pass (covered, tmp);
				long [] next = stable (count);
				if (!same (next, published)) {
					published = next;
					if (!post ()) {
						return;
					}
				}
				sleep (count > 0 || next.length > 0 ? FAST_MS : SLOW_MS);
			}
		} finally {
			OS.DeleteObject (covered);
			OS.DeleteObject (tmp);
			OS.DeleteObject (shape);
			if (vdm != 0) {
				COM.VtblCall (2, vdm);
			}
			OS.OleUninitialize ();
		}
	}

	boolean createVirtualDesktopManager () {
		GUID clsid = new GUID (), iid = new GUID ();
		if (COM.CLSIDFromString (CLSID_VDM, clsid) != COM.S_OK || COM.IIDFromString (IID_VDM, iid) != COM.S_OK) {
			return false;
		}
		long [] ppv = new long [1];
		int context = COM.CLSCTX_INPROC_SERVER | COM.CLSCTX_LOCAL_SERVER;
		if (COM.CoCreateInstance (clsid, 0, context, iid, ppv) != COM.S_OK || ppv [0] == 0) {
			return false;
		}
		vdm = ppv [0];
		return true;
	}

	/** 1 on the current virtual desktop, 0 on another one, -1 unknown. */
	int onCurrentDesktop (long hwnd) {
		intOut [0] = 1;
		if (COM.VtblCall (IS_ON_CURRENT_DESKTOP, vdm, hwnd, intOut) != COM.S_OK) {
			return -1;
		}
		return intOut [0] != 0 ? 1 : 0;
	}

	/**
	 * One front-to-back pass. Fills {@link #occluded} and returns its count.
	 */
	int pass (long covered, long tmp) {
		int nOurs = collectOurs ();
		if (nOurs == 0) {
			return 0;
		}
		OS.SetRectRgn (covered, 0, 0, 0, 0);
		int n = 0, seen = 0;
		long h = OS.GetWindow (OS.GetDesktopWindow (), OS.GW_CHILD);
		while (h != 0 && seen < nOurs) {
			if (OS.IsWindowVisible (h) && !OS.IsIconic (h)) {
				if (contains (ours, nOurs, h)) {
					seen++;
					if (isOccluded (h, covered, tmp)) {
						occluded = add (occluded, n++, h);
					}
				}
				if (seen < nOurs && isOpaqueOccluder (h)) {
					OS.SetRectRgn (tmp, rect.left, rect.top, rect.right, rect.bottom);
					OS.CombineRgn (covered, covered, tmp, OS.RGN_OR);
				}
			}
			h = OS.GetWindow (h, OS.GW_HWNDNEXT);
		}
		return n;
	}

	/** Visible, non-iconic top-level windows of the Display thread; also sets {@link #bounds}. */
	int collectOurs () {
		int n = 0;
		bounds.left = bounds.top = Integer.MAX_VALUE;
		bounds.right = bounds.bottom = Integer.MIN_VALUE;
		for (long h = OS.GetWindow (OS.GetDesktopWindow (), OS.GW_CHILD); h != 0; h = OS.GetWindow (h, OS.GW_HWNDNEXT)) {
			if (OS.GetWindowThreadProcessId (h, pid) == threadId && OS.IsWindowVisible (h) && !OS.IsIconic (h)) {
				ours = add (ours, n++, h);
				EffectiveVisibility.screenClientRect (h, rect);
				bounds.left = Math.min (bounds.left, rect.left);
				bounds.top = Math.min (bounds.top, rect.top);
				bounds.right = Math.max (bounds.right, rect.right);
				bounds.bottom = Math.max (bounds.bottom, rect.bottom);
			}
		}
		return n;
	}

	boolean isOccluded (long h, long covered, long tmp) {
		EffectiveVisibility.screenClientRect (h, rect);
		if (rect.right <= rect.left || rect.bottom <= rect.top) {
			return false;
		}
		if (onCurrentDesktop (h) == 0) {
			return true;
		}
		OS.SetRectRgn (tmp, rect.left, rect.top, rect.right, rect.bottom);
		return OS.CombineRgn (tmp, tmp, covered, OS.RGN_DIFF) == OS.NULLREGION;
	}

	/** Chromium IsWindowVisibleAndFullyOpaque, fail-closed variant. Leaves the occluder rect in {@link #rect}. */
	boolean isOpaqueOccluder (long h) {
		int ex = OS.GetWindowLong (h, OS.GWL_EXSTYLE);
		if ((ex & OS.WS_EX_TRANSPARENT) != 0) {
			return false;
		}
		int length = OS.GetClassName (h, className, className.length);
		if ((ex & OS.WS_EX_TOOLWINDOW) != 0 && !classIs (length, "Shell_TrayWnd")) { //$NON-NLS-1$
			return false;
		}
		if ((ex & OS.WS_EX_LAYERED) != 0) {
			intOut [0] = 0;
			if (!OS.GetLayeredWindowAttributes (h, key, alpha, intOut)) {
				return false;
			}
			if (((intOut [0] & OS.LWA_ALPHA) != 0 && alpha [0] != (byte) 0xFF) || (intOut [0] & OS.LWA_COLORKEY) != 0) {
				return false;
			}
		}
		if (classIs (length, NEVER_OCCLUDE [0]) || classIs (length, NEVER_OCCLUDE [1]) || classContainsTooltip (length)) {
			return false;
		}
		OS.GetWindowRect (h, rect);
		if ((OS.GetWindowLong (h, OS.GWL_STYLE) & OS.WS_THICKFRAME) != 0 && !OS.IsZoomed (h)) {
			int frame = 2 * (OS.GetSystemMetrics (SM_CXSIZEFRAME) + OS.GetSystemMetrics (SM_CXPADDEDBORDER));
			rect.left += frame;
			rect.right -= frame;
			rect.bottom -= frame;
		}
		if (rect.right <= rect.left || rect.bottom <= rect.top) {
			return false;
		}
		if (rect.right <= bounds.left || rect.left >= bounds.right || rect.bottom <= bounds.top || rect.top >= bounds.bottom) {
			return false;
		}
		if (OS.GetWindowRgn (h, shape) == OS.COMPLEXREGION) {
			return false;
		}
		return onCurrentDesktop (h) == 1;
	}

	boolean classIs (int length, String name) {
		if (length != name.length ()) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (className [i] != name.charAt (i)) {
				return false;
			}
		}
		return true;
	}

	boolean classContainsTooltip (int length) {
		String needle = "tooltip"; //$NON-NLS-1$
		for (int i = 0; i + needle.length () <= length; i++) {
			int j = 0;
			while (j < needle.length () && Character.toLowerCase (className [i + j]) == needle.charAt (j)) {
				j++;
			}
			if (j == needle.length ()) {
				return true;
			}
		}
		return false;
	}

	/** Occlusion must hold for two passes; un-occlusion is immediate. */
	long [] stable (int count) {
		int n = 0;
		long [] result = new long [count];
		for (int i = 0; i < count; i++) {
			if (contains (previous, previous.length, occluded [i])) {
				result [n++] = occluded [i];
			}
		}
		long [] now = new long [count];
		System.arraycopy (occluded, 0, now, 0, count);
		previous = now;
		if (n == count) {
			return result;
		}
		long [] trimmed = new long [n];
		System.arraycopy (result, 0, trimmed, 0, n);
		return trimmed;
	}

	boolean post () {
		try {
			if (display.isDisposed ()) {
				return false;
			}
			display.asyncExec (apply);
			return true;
		} catch (RuntimeException ex) {
			return false;
		}
	}

	/** UI thread. */
	void apply () {
		if (display.isDisposed ()) {
			return;
		}
		long [] next = published;
		for (long h : applied) {
			if (!contains (next, next.length, h) && display.getControl (h) instanceof Shell shell) {
				EffectiveVisibility.clearOccluded (shell);
			}
		}
		for (long h : next) {
			if (display.getControl (h) instanceof Shell shell && !shell.isDisposed ()) {
				EffectiveVisibility.setOccluded (shell);
			}
		}
		applied = next;
	}

	static boolean contains (long [] array, int count, long value) {
		for (int i = 0; i < count; i++) {
			if (array [i] == value) {
				return true;
			}
		}
		return false;
	}

	static long [] add (long [] array, int index, long value) {
		if (index == array.length) {
			long [] grown = new long [array.length * 2 + 1];
			System.arraycopy (array, 0, grown, 0, array.length);
			array = grown;
		}
		array [index] = value;
		return array;
	}

	static boolean same (long [] a, long [] b) {
		if (a.length != b.length) {
			return false;
		}
		for (long v : a) {
			if (!contains (b, b.length, v)) {
				return false;
			}
		}
		return true;
	}

	void sleep (int ms) {
		try {
			Thread.sleep (ms);
		} catch (InterruptedException ex) {
			running = false;
		}
	}
}

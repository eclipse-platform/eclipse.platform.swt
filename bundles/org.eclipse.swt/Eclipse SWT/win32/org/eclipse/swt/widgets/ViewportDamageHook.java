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
 * Test hook that feeds the WM_PAINT update region of a control (and of its
 * descendants) into a {@link ViewportDamageRecorder} as painted damage, in the
 * attached control's content-viewport coordinates. Off unless a recorder is
 * attached: the WM_PAINT fast path is one int test.
 */
//FLLDCIM: HPCPOP - off by default; one int test per WM_PAINT; reused region handle and int[] buffer
final class ViewportDamageHook {
	private ViewportDamageHook () {
	}

	static void attach (Control c, ViewportDamageRecorder recorder) {
		Display d = c.display;
		int index = indexOf (d, c);
		if (recorder == null) {
			if (index >= 0) {
				d.damageHooks--;
				d.damageControls [index] = d.damageControls [d.damageHooks];
				d.damageRecorders [index] = d.damageRecorders [d.damageHooks];
				d.damageControls [d.damageHooks] = null;
				d.damageRecorders [d.damageHooks] = null;
			}
			return;
		}
		if (index < 0) {
			if (d.damageHooks == d.damageControls.length) {
				Control [] controls = new Control [d.damageHooks * 2];
				ViewportDamageRecorder [] recorders = new ViewportDamageRecorder [d.damageHooks * 2];
				System.arraycopy (d.damageControls, 0, controls, 0, d.damageHooks);
				System.arraycopy (d.damageRecorders, 0, recorders, 0, d.damageHooks);
				d.damageControls = controls;
				d.damageRecorders = recorders;
			}
			index = d.damageHooks++;
			d.damageControls [index] = c;
		}
		d.damageRecorders [index] = recorder;
	}

	static int indexOf (Display d, Control c) {
		for (int i = 0; i < d.damageHooks; i++) {
			if (d.damageControls [i] == c) {
				return i;
			}
		}
		return -1;
	}

	/** WM_PAINT about to be painted for <code>hwnd</code> of <code>c</code> (called before BeginPaint). */
	static void paint (Control c, long hwnd) {
		Display d = c.display;
		for (int i = 0; i < d.damageHooks; i++) {
			Control target = d.damageControls [i];
			if (target == null || target.isDisposed () || !VisibilityCatchUp.isAncestor (target, c)) {
				continue;
			}
			record (d, target, d.damageRecorders [i], hwnd);
		}
	}

	static void record (Display d, Control target, ViewportDamageRecorder recorder, long hwnd) {
		if (d.damageRgn == 0) {
			d.damageRgn = OS.CreateRectRgn (0, 0, 0, 0);
		}
		if (OS.GetUpdateRgn (hwnd, d.damageRgn, false) <= OS.NULLREGION) {
			return;
		}
		RECT origin = d.boxWindow;
		OS.SetRect (origin, 0, 0, 0, 0);
		OS.MapWindowPoints (hwnd, ViewportBox.boxHandle (target), origin, 1);
		int [] content = d.damageContent;
		if (!ViewportBox.content (target, content)) {
			return;
		}
		int dx = origin.left - content [0], dy = origin.top - content [1];
		int bytes = OS.GetRegionData (d.damageRgn, 0, null);
		if (bytes <= 0) {
			return;
		}
		if (d.damageData.length < (bytes + 3) / 4) {
			d.damageData = new int [(bytes + 3) / 4];
		}
		int [] data = d.damageData;
		OS.GetRegionData (d.damageRgn, bytes, data);
		int count = data [2];
		for (int k = 0; k < count; k++) {
			int base = 8 + k * 4;
			int left = data [base], top = data [base + 1], right = data [base + 2], bottom = data [base + 3];
			recorder.paint (left + dx, top + dy, right - left, bottom - top);
		}
	}

	/** Display.release. */
	static void release (Display d) {
		if (d.damageRgn != 0) {
			OS.DeleteObject (d.damageRgn);
			d.damageRgn = 0;
		}
		for (int i = 0; i < d.damageHooks; i++) {
			d.damageControls [i] = null;
			d.damageRecorders [i] = null;
		}
		d.damageHooks = 0;
	}
}

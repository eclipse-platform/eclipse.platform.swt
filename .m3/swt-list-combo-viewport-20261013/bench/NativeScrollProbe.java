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
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * Native limits of an LBS_NODATA list box: the largest count LB_SETCOUNT accepts,
 * the vertical scroll range it publishes, and whether WM_VSCROLL thumb messages
 * (16-bit position in HIWORD(wParam)) or the 32-bit track position reach the end.
 */
public class NativeScrollProbe {
	static final int LB_SETCOUNT = 0x1A7;

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 330);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int bits = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | 0x2000 | 0x10;
		long h = OS.CreateWindowEx (0, cls, null, bits, 0, 0, 200, 300, shell.handle, 0, OS.GetModuleHandle (null), null);
		System.out.println ("fact\tvalue\tnote");
		for (long n : new long [] {65_535, 65_536, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000, Integer.MAX_VALUE}) {
			long t0 = System.nanoTime ();
			long rc = OS.SendMessage (h, LB_SETCOUNT, n, 0);
			long t1 = System.nanoTime ();
			System.out.println ("setcount." + n + "\trc=" + rc + " count=" + OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0) + "\t" + String.format ("%.1f ms", (t1 - t0) / 1e6));
		}
		OS.SendMessage (h, LB_SETCOUNT, 10_000_000, 0);
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_ALL;
		OS.GetScrollInfo (h, OS.SB_VERT, info);
		System.out.println ("scrollinfo.10M\tmin=" + info.nMin + " max=" + info.nMax + " page=" + info.nPage + " pos=" + info.nPos + "\tlist box publishes item units");
		OS.SendMessage (h, OS.WM_VSCROLL, OS.SB_BOTTOM, 0);
		System.out.println ("sb_bottom.top\t" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0) + "\tWM_VSCROLL SB_BOTTOM");
		OS.SendMessage (h, OS.LB_SETTOPINDEX, 0, 0);
		int pos16 = 0xFFFF;
		OS.SendMessage (h, OS.WM_VSCROLL, OS.MAKEWPARAM (OS.SB_THUMBPOSITION, pos16), 0);
		System.out.println ("thumbposition.0xFFFF.top\t" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0) + "\tHIWORD carries at most 65535");
		OS.SendMessage (h, OS.LB_SETTOPINDEX, 5_000_000, 0);
		OS.GetScrollInfo (h, OS.SB_VERT, info);
		System.out.println ("settop.5M\ttop=" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0) + " pos=" + info.nPos + "\tLB_SETTOPINDEX and the 32-bit scroll position");
		OS.SendMessage (h, OS.WM_VSCROLL, OS.MAKEWPARAM (OS.SB_THUMBTRACK, 1234), 0);
		OS.GetScrollInfo (h, OS.SB_VERT, info);
		System.out.println ("thumbtrack.1234.top\t" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0) + " trackpos=" + info.nTrackPos + "\tsynthetic SB_THUMBTRACK outside a drag");
		OS.DestroyWindow (h);
		shell.dispose ();
		display.dispose ();
	}
}

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
import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * Measures what the Win32 LISTBOX and COMBOBOX classes do in owner-data mode,
 * before any widget code is written. Prints one TSV row per fact:
 * fact, value, note. Uses raw windows parented to an SWT shell; no SWT widget
 * behaviour is involved.
 */
public class NativeListProbe {
	static final int LBS_NODATA = 0x2000, LBS_OWNERDRAWFIXED = 0x10, LBS_HASSTRINGS = 0x40;
	static final int LB_SETCOUNT = 0x1A7, LB_GETANCHORINDEX = 0x19D, LB_SETANCHORINDEX = 0x19C;
	static final int CBS_OWNERDRAWFIXED = 0x10, CBS_HASSTRINGS = 0x200, CBS_UPPERCASE = 0x2000;
	static final TCHAR LISTBOX = new TCHAR ("LISTBOX", true), COMBOBOX = new TCHAR ("COMBOBOX", true);

	public static void main (String [] args) {
		int big = args.length > 0 ? Integer.parseInt (args [0]) : 10_000_000;
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 300);
		long parent = shell.handle;
		System.out.println ("fact\tvalue\tnote");
		System.out.println ("baseline\t" + ProcStat.header ().replace ('\t', ' ') + "\t" + ProcStat.sample ().row ().replace ('\t', ' '));
		int base = OS.WS_CHILD | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT;
		long font = OS.SendMessage (parent, OS.WM_GETFONT, 0, 0);
		long std = create (LISTBOX, base | OS.LBS_EXTENDEDSEL, parent, font);
		row ("std.itemHeight", OS.SendMessage (std, OS.LB_GETITEMHEIGHT, 0, 0), "standard list box after WM_SETFONT(shell font)");
		row ("font.tmHeight", tmHeight (std, font), "TEXTMETRIC.tmHeight of the same font");
		for (int sel : new int [] {0, OS.LBS_EXTENDEDSEL, OS.LBS_MULTIPLESEL}) {
			String tag = sel == 0 ? "single" : sel == OS.LBS_EXTENDEDSEL ? "extended" : "multiple";
			long nd = create (LISTBOX, base | sel | LBS_NODATA | LBS_OWNERDRAWFIXED, parent, font);
			row ("nodata." + tag + ".style", Integer.toHexString (OS.GetWindowLong (nd, OS.GWL_STYLE)), "GWL_STYLE after create");
			row ("nodata." + tag + ".itemHeight", OS.SendMessage (nd, OS.LB_GETITEMHEIGHT, 0, 0), "owner-draw fixed height before LB_SETITEMHEIGHT");
			ProcStat before = ProcStat.sample ();
			long t0 = System.nanoTime ();
			long rc = OS.SendMessage (nd, LB_SETCOUNT, big, 0);
			long t1 = System.nanoTime ();
			ProcStat after = ProcStat.sample ();
			row ("nodata." + tag + ".setcount", rc, "LB_SETCOUNT(" + big + ") return; " + ms (t1 - t0) + " ms");
			row ("nodata." + tag + ".count", OS.SendMessage (nd, OS.LB_GETCOUNT, 0, 0), "LB_GETCOUNT");
			row ("nodata." + tag + ".privateDeltaMB", String.format ("%.1f", (after.privateBytes - before.privateBytes) / 1048576.0), "process private bytes delta for LB_SETCOUNT");
			row ("nodata." + tag + ".gettextlen", OS.SendMessage (nd, OS.LB_GETTEXTLEN, 5, 0), "LB_GETTEXTLEN(5)");
			row ("nodata." + tag + ".findexact", OS.SendMessage (nd, OS.LB_FINDSTRINGEXACT, -1, new TCHAR ("x", true).chars), "LB_FINDSTRINGEXACT");
			if (sel == 0) {
				row ("nodata.single.setcursel", OS.SendMessage (nd, OS.LB_SETCURSEL, 7, 0), "LB_SETCURSEL(7) return");
				row ("nodata.single.cursel", OS.SendMessage (nd, OS.LB_GETCURSEL, 0, 0), "LB_GETCURSEL after LB_SETCURSEL(7)");
				OS.SendMessage (nd, OS.LB_INSERTSTRING, 0, 0);
				row ("nodata.single.curselAfterInsert0", OS.SendMessage (nd, OS.LB_GETCURSEL, 0, 0), "cursel 7 then insert at 0");
				OS.SendMessage (nd, OS.LB_DELETESTRING, 0, 0);
				row ("nodata.single.curselAfterDelete0", OS.SendMessage (nd, OS.LB_GETCURSEL, 0, 0), "then delete 0");
				OS.SendMessage (nd, LB_SETCOUNT, big - 1, 0);
				row ("nodata.single.curselAfterSetcount", OS.SendMessage (nd, OS.LB_GETCURSEL, 0, 0), "then LB_SETCOUNT(count-1)");
			} else {
				OS.SendMessage (nd, OS.LB_SETSEL, 1, 3);
				OS.SendMessage (nd, OS.LB_SETSEL, 1, big - 1);
				OS.SendMessage (nd, OS.LB_INSERTSTRING, 0, 0);
				row ("nodata." + tag + ".selAfterInsert0", sel (nd), "selected {3,last} then insert at 0");
				t0 = System.nanoTime ();
				OS.SendMessage (nd, OS.LB_DELETESTRING, 0, 0);
				t1 = System.nanoTime ();
				row ("nodata." + tag + ".selAfterDelete0", sel (nd), "then delete 0; " + ms (t1 - t0) + " ms");
				t0 = System.nanoTime ();
				rc = OS.SendMessage (nd, OS.LB_SELITEMRANGEEX, 0, big - 1);
				t1 = System.nanoTime ();
				row ("nodata." + tag + ".selectAllRange", OS.SendMessage (nd, OS.LB_GETSELCOUNT, 0, 0), "LB_SELITEMRANGEEX(0,last) rc=" + rc + "; " + ms (t1 - t0) + " ms");
				OS.SendMessage (nd, OS.LB_SETSEL, 0, -1);
				OS.SendMessage (nd, OS.LB_SETSEL, 1, 9);
				OS.SendMessage (nd, LB_SETCOUNT, big - 1, 0);
				row ("nodata." + tag + ".selAfterSetcount", sel (nd), "select {9} then LB_SETCOUNT(count-1)");
			}
			OS.DestroyWindow (nd);
		}
		typeAhead ("typeahead.single", base, parent, font);
		typeAhead ("typeahead.extended", base | OS.LBS_EXTENDEDSEL, parent, font);
		long nh = create (LISTBOX, base | LBS_NODATA | LBS_OWNERDRAWFIXED, parent, font);
		OS.SendMessage (nh, 0x1A0, 0, 15);
		row ("nodata.setitemheight", OS.SendMessage (nh, OS.LB_GETITEMHEIGHT, 0, 0), "LB_GETITEMHEIGHT after LB_SETITEMHEIGHT(0,15)");
		OS.DestroyWindow (nh);
		long stdBig = create (LISTBOX, base | OS.LBS_EXTENDEDSEL, parent, font);
		row ("std.setcount", OS.SendMessage (stdBig, LB_SETCOUNT, 100, 0), "LB_SETCOUNT on a standard (has-strings) list box");
		OS.DestroyWindow (stdBig);
		comboRow ("combo.ownerdraw", OS.CBS_DROPDOWN | CBS_OWNERDRAWFIXED, parent, font);
		comboRow ("combo.ownerdraw+0x2000", OS.CBS_DROPDOWN | CBS_OWNERDRAWFIXED | CBS_UPPERCASE, parent, font);
		comboRow ("combo.dropdownlist.ownerdraw", OS.CBS_DROPDOWNLIST | CBS_OWNERDRAWFIXED, parent, font);
		OS.DestroyWindow (std);
		System.out.println ("end\t" + ProcStat.header ().replace ('\t', ' ') + "\t" + ProcStat.sample ().row ().replace ('\t', ' '));
		shell.dispose ();
		display.dispose ();
	}

	static void comboRow (String tag, int bits, long parent, long font) {
		long combo = create (COMBOBOX, OS.WS_CHILD | OS.WS_VSCROLL | bits, parent, font);
		COMBOBOXINFO info = new COMBOBOXINFO ();
		info.cbSize = COMBOBOXINFO.sizeof;
		OS.GetComboBoxInfo (combo, info);
		int listStyle = OS.GetWindowLong (info.hwndList, OS.GWL_STYLE);
		row (tag + ".listStyle", Integer.toHexString (listStyle), "drop-down LISTBOX GWL_STYLE; LBS_NODATA bit=" + ((listStyle & LBS_NODATA) != 0));
		row (tag + ".lbSetcount", OS.SendMessage (info.hwndList, LB_SETCOUNT, 1000, 0), "LB_SETCOUNT sent to the drop-down list box");
		OS.SetWindowLong (info.hwndList, OS.GWL_STYLE, listStyle | LBS_NODATA);
		row (tag + ".lbSetcountAfterStyle", OS.SendMessage (info.hwndList, LB_SETCOUNT, 1000, 0), "LB_SETCOUNT after SetWindowLong(+LBS_NODATA)");
		row (tag + ".cbCount", OS.SendMessage (combo, OS.CB_GETCOUNT, 0, 0), "CB_GETCOUNT after the attempts");
		OS.DestroyWindow (combo);
	}

	static void typeAhead (String tag, int bits, long parent, long font) {
		long lb = create (LISTBOX, bits, parent, font);
		for (String item : new String [] {"apple", "Banana", "berry", "blue", "cherry", "bz"}) OS.SendMessage (lb, OS.LB_ADDSTRING, 0, new TCHAR (item, true).chars);
		StringBuilder trace = new StringBuilder ();
		String keys = "bbbBe";
		for (int i = 0; i < keys.length (); i++) {
			OS.SendMessage (lb, OS.WM_CHAR, keys.charAt (i), 0);
			trace.append (keys.charAt (i)).append ("->").append (OS.SendMessage (lb, OS.LB_GETCARETINDEX, 0, 0)).append ('/').append (sel (lb)).append (' ');
		}
		row (tag + ".fast", trace.toString ().trim (), "WM_CHAR without delay: key->caret/selection; items apple,Banana,berry,blue,cherry,bz");
		trace.setLength (0);
		String slow = "bzc";
		for (int i = 0; i < slow.length (); i++) {
			try { Thread.sleep (2500); } catch (InterruptedException e) { return; }
			OS.SendMessage (lb, OS.WM_CHAR, slow.charAt (i), 0);
			trace.append (slow.charAt (i)).append ("->").append (OS.SendMessage (lb, OS.LB_GETCARETINDEX, 0, 0)).append ('/').append (sel (lb)).append (' ');
		}
		row (tag + ".slow", trace.toString ().trim (), "WM_CHAR 2.5 s apart");
		OS.DestroyWindow (lb);
	}

	static long create (TCHAR cls, int bits, long parent, long font) {
		long hwnd = OS.CreateWindowEx (0, cls, null, bits, 0, 0, 200, 200, parent, 0, OS.GetModuleHandle (null), null);
		OS.SendMessage (hwnd, OS.WM_SETFONT, font, 0);
		return hwnd;
	}

	static int tmHeight (long hwnd, long font) {
		long hdc = OS.GetDC (hwnd);
		long old = OS.SelectObject (hdc, font);
		TEXTMETRIC tm = new TEXTMETRIC ();
		OS.GetTextMetrics (hdc, tm);
		OS.SelectObject (hdc, old);
		OS.ReleaseDC (hwnd, hdc);
		return tm.tmHeight;
	}

	static String sel (long hwnd) {
		int n = (int) OS.SendMessage (hwnd, OS.LB_GETSELCOUNT, 0, 0);
		if (n == -1) return "cur=" + OS.SendMessage (hwnd, OS.LB_GETCURSEL, 0, 0);
		if (n < 0 || n > 16) return "count=" + n;
		int [] items = new int [n];
		OS.SendMessage (hwnd, OS.LB_GETSELITEMS, n, items);
		return java.util.Arrays.toString (items);
	}

	static void row (String fact, Object value, String note) {
		System.out.println (fact + "\t" + value + "\t" + note);
	}

	static String ms (long nanos) {
		return String.format ("%.3f", nanos / 1e6);
	}
}

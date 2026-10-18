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
 * Calibrates how a plain (has-strings) LISTBOX moves its caret, anchor, top index and
 * single selection when rows are deleted one by one (ascending at start, or descending)
 * or inserted. The owner-data rebuild path must land on the same values.
 * Prints: mode, op, start, end, caret0, anchor0, cursel0, caret1, anchor1, cursel1, top1.
 */
public class NativeShiftProbe {
	static final int LB_GETANCHORINDEX = 0x19D, LB_SETANCHORINDEX = 0x19C;

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 300);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT;
		int [] positions = {-1, 0, 5, 39, 40, 50, 60, 61, 120, 288, 289, 290, 299};
		int [] [] ranges = {{40, 60}, {0, 9}, {290, 299}, {41, 41}, {0, 0}, {299, 299}};
		System.out.println ("mode\top\tstart\tend\tcaret0\tanchor0\tcursel0\tcaret1\tanchor1\tcursel1\ttop1");
		for (int sel : new int [] {0, OS.LBS_EXTENDEDSEL, OS.LBS_MULTIPLESEL}) {
			String mode = sel == 0 ? "single" : sel == OS.LBS_EXTENDEDSEL ? "extended" : "multiple";
			for (int [] range : ranges) {
				for (int p : positions) {
					for (String op : new String [] {"deleteAsc", "deleteDesc", "insert"}) {
						long h = OS.CreateWindowEx (0, cls, null, base | sel, 0, 0, 200, 200, shell.handle, 0, OS.GetModuleHandle (null), null);
						for (int i = 0; i < 300; i++) OS.SendMessage (h, OS.LB_ADDSTRING, 0, new TCHAR ("row " + i, true).chars);
						if (sel == 0) {
							if (p >= 0) OS.SendMessage (h, OS.LB_SETCURSEL, p, 0);
						} else if (p >= 0) {
							OS.SendMessage (h, OS.LB_SETSEL, 1, p);
							OS.SendMessage (h, LB_SETANCHORINDEX, p, 0);
							OS.SendMessage (h, OS.LB_SETCARETINDEX, p, 0);
						}
						String before = state (h, sel);
						int s = range [0], e = range [1];
						if (op.equals ("insert")) {
							for (int i = 0; i <= e - s; i++) OS.SendMessage (h, OS.LB_INSERTSTRING, s + i, new TCHAR ("new", true).chars);
						} else {
							for (int i = 0; i <= e - s; i++) OS.SendMessage (h, OS.LB_DELETESTRING, op.equals ("deleteAsc") ? s : e - i, 0);
						}
						System.out.println (mode + "\t" + op + "\t" + s + "\t" + e + "\t" + before + "\t" + state (h, sel) + "\t" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0));
						OS.DestroyWindow (h);
					}
				}
			}
		}
		shell.dispose ();
		display.dispose ();
	}

	static String state (long h, int sel) {
		return OS.SendMessage (h, OS.LB_GETCARETINDEX, 0, 0) + "\t" + OS.SendMessage (h, LB_GETANCHORINDEX, 0, 0) + "\t"
			+ (sel == 0 ? OS.SendMessage (h, OS.LB_GETCURSEL, 0, 0) : -9);
	}
}

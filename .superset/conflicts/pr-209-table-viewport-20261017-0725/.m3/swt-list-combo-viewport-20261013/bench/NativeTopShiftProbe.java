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
 * Top row of a plain list box after one LB_DELETESTRING / LB_INSERTSTRING, for a grid of
 * (count, top, row). Prints: op, count, top0, row, top1. 10 rows are visible.
 */
public class NativeTopShiftProbe {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 330);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | OS.LBS_EXTENDEDSEL;
		long font = OS.SendMessage (shell.handle, OS.WM_GETFONT, 0, 0);
		System.out.println ("op\tcount\ttop0\trow\ttop1");
		for (String op : new String [] {"delete", "insert"}) {
			for (int count : new int [] {12, 30, 224}) {
				for (int top0 : new int [] {0, 1, 5, count / 2, count - 11, count - 10}) {
					for (int row : new int [] {0, 1, top0 - 1, top0, top0 + 1, top0 + 9, top0 + 10, count - 1}) {
						if (top0 < 0 || row < 0 || row >= count) continue;
						long h = OS.CreateWindowEx (0, cls, null, base, 0, 0, 200, 150, shell.handle, 0, OS.GetModuleHandle (null), null);
						OS.SendMessage (h, OS.WM_SETFONT, font, 0);
						for (int i = 0; i < count; i++) OS.SendMessage (h, OS.LB_ADDSTRING, 0, new TCHAR ("row " + i, true).chars);
						OS.SendMessage (h, OS.LB_SETTOPINDEX, top0, 0);
						long t0 = OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0);
						if (op.equals ("delete")) OS.SendMessage (h, OS.LB_DELETESTRING, row, 0);
						else OS.SendMessage (h, OS.LB_INSERTSTRING, row, new TCHAR ("new", true).chars);
						System.out.println (op + "\t" + count + "\t" + t0 + "\t" + row + "\t" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0));
						OS.DestroyWindow (h);
					}
				}
			}
		}
		shell.dispose ();
		display.dispose ();
	}
}

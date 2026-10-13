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
 * How LB_SETTOPINDEX clamps a requested top row: plain (has-strings) list box built by
 * LB_ADDSTRING and shrunk by LB_DELETESTRING, versus an LBS_NODATA list box sized by
 * LB_SETCOUNT. Prints requested and resulting top for both.
 */
public class NativeTopProbe {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 330);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | OS.LBS_EXTENDEDSEL;
		long font = OS.SendMessage (shell.handle, OS.WM_GETFONT, 0, 0);
		long plain = OS.CreateWindowEx (0, cls, null, base, 0, 0, 200, 150, shell.handle, 0, OS.GetModuleHandle (null), null);
		long data = OS.CreateWindowEx (0, cls, null, base | 0x2000 | 0x10, 0, 0, 200, 150, shell.handle, 0, OS.GetModuleHandle (null), null);
		OS.SendMessage (plain, OS.WM_SETFONT, font, 0);
		OS.SendMessage (data, OS.WM_SETFONT, font, 0);
		OS.SendMessage (data, 0x1A0, 0, OS.SendMessage (plain, OS.LB_GETITEMHEIGHT, 0, 0));
		for (int i = 0; i < 300; i++) OS.SendMessage (plain, OS.LB_ADDSTRING, 0, new TCHAR ("row " + i, true).chars);
		OS.SendMessage (data, 0x1A7, 300, 0);
		RECT rect = new RECT ();
		OS.GetClientRect (plain, rect);
		System.out.println ("client.plain\t" + rect.bottom + "\titemHeight=" + OS.SendMessage (plain, OS.LB_GETITEMHEIGHT, 0, 0));
		OS.GetClientRect (data, rect);
		System.out.println ("client.data\t" + rect.bottom + "\titemHeight=" + OS.SendMessage (data, OS.LB_GETITEMHEIGHT, 0, 0));
		for (int count : new int [] {300, 149, 40}) {
			while (OS.SendMessage (plain, OS.LB_GETCOUNT, 0, 0) > count) OS.SendMessage (plain, OS.LB_DELETESTRING, 0, 0);
			OS.SendMessage (data, 0x1A7, count, 0);
			StringBuilder b = new StringBuilder ();
			for (int t : new int [] {0, count - 12, count - 10, count - 9, count - 8, count - 1, count + 5}) {
				OS.SendMessage (plain, OS.LB_SETTOPINDEX, t, 0);
				OS.SendMessage (data, OS.LB_SETTOPINDEX, t, 0);
				b.append (t).append ("->").append (OS.SendMessage (plain, OS.LB_GETTOPINDEX, 0, 0)).append ('/').append (OS.SendMessage (data, OS.LB_GETTOPINDEX, 0, 0)).append (' ');
			}
			System.out.println ("settop.count" + count + "\t" + b.toString ().trim () + "\trequested->plain/nodata");
		}
		shell.dispose ();
		display.dispose ();
	}
}

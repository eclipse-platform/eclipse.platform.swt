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

/** Which caret/anchor/count messages an LBS_NODATA list box accepts after LB_SETCOUNT. One line per fact. */
public class NativeCaretProbe {
	static final int LB_SETCOUNT = 0x1A7, LB_GETANCHORINDEX = 0x19D, LB_SETANCHORINDEX = 0x19C;

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 300);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | 0x2000 | 0x10;
		for (int sel : new int [] {0, OS.LBS_EXTENDEDSEL, OS.LBS_MULTIPLESEL}) {
			String mode = sel == 0 ? "single" : sel == OS.LBS_EXTENDEDSEL ? "extended" : "multiple";
			long h = OS.CreateWindowEx (0, cls, null, base | sel, 0, 0, 200, 200, shell.handle, 0, OS.GetModuleHandle (null), null);
			p (mode, "setcount10", OS.SendMessage (h, LB_SETCOUNT, 10, 0), h);
			if (sel == 0) p (mode, "setcursel5", OS.SendMessage (h, OS.LB_SETCURSEL, 5, 0), h);
			else p (mode, "setsel5", OS.SendMessage (h, OS.LB_SETSEL, 1, 5), h);
			p (mode, "setcaret7", OS.SendMessage (h, OS.LB_SETCARETINDEX, 7, 0), h);
			p (mode, "setanchor6", OS.SendMessage (h, LB_SETANCHORINDEX, 6, 0), h);
			p (mode, "setanchor-1", OS.SendMessage (h, LB_SETANCHORINDEX, -1, 0), h);
			p (mode, "delete3", OS.SendMessage (h, OS.LB_DELETESTRING, 3, 0), h);
			p (mode, "insert3", OS.SendMessage (h, OS.LB_INSERTSTRING, 3, 0), h);
			p (mode, "setcount12", OS.SendMessage (h, LB_SETCOUNT, 12, 0), h);
			p (mode, "settop4", OS.SendMessage (h, OS.LB_SETTOPINDEX, 4, 0), h);
			p (mode, "setcount20", OS.SendMessage (h, LB_SETCOUNT, 20, 0), h);
			p (mode, "delete0", OS.SendMessage (h, OS.LB_DELETESTRING, 0, 0), h);
			OS.DestroyWindow (h);
		}
		shell.dispose ();
		display.dispose ();
	}

	static void p (String mode, String op, long rc, long h) {
		System.out.println (mode + "\t" + op + "\trc=" + rc + "\tcount=" + OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0)
			+ "\tcaret=" + OS.SendMessage (h, OS.LB_GETCARETINDEX, 0, 0) + "\tanchor=" + OS.SendMessage (h, LB_GETANCHORINDEX, 0, 0)
			+ "\tcursel=" + OS.SendMessage (h, OS.LB_GETCURSEL, 0, 0) + "\tselcount=" + OS.SendMessage (h, OS.LB_GETSELCOUNT, 0, 0)
			+ "\ttop=" + OS.SendMessage (h, OS.LB_GETTOPINDEX, 0, 0));
	}
}

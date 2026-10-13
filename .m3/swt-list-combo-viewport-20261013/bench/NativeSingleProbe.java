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
 * Follow-up of N7: does a single-selection LBS_NODATA list box accept LB_INSERTSTRING after
 * LB_SETCOUNT when a row was inserted before the LB_SETCOUNT; and which selection messages move
 * the anchor of a multi-selection list box.
 */
public class NativeSingleProbe {
	static final int LB_SETCOUNT = 0x1A7, LB_GETANCHORINDEX = 0x19D;

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 300);
		shell.open ();
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | 0x2000 | 0x10;
		long h = OS.CreateWindowEx (0, cls, null, base, 0, 0, 200, 200, shell.handle, 0, OS.GetModuleHandle (null), null);
		p ("A.ins0", OS.SendMessage (h, OS.LB_INSERTSTRING, 0, 0), h);
		p ("A.setcount10", OS.SendMessage (h, LB_SETCOUNT, 10, 0), h);
		p ("A.ins3", OS.SendMessage (h, OS.LB_INSERTSTRING, 3, 0), h);
		p ("A.ins11", OS.SendMessage (h, OS.LB_INSERTSTRING, 11, 0), h);
		p ("A.setcount1000", OS.SendMessage (h, LB_SETCOUNT, 1000, 0), h);
		p ("A.ins500", OS.SendMessage (h, OS.LB_INSERTSTRING, 500, 0), h);
		p ("B.reset", OS.SendMessage (h, OS.LB_RESETCONTENT, 0, 0), h);
		p ("B.setcount10", OS.SendMessage (h, LB_SETCOUNT, 10, 0), h);
		p ("B.setcount0", OS.SendMessage (h, LB_SETCOUNT, 0, 0), h);
		p ("B.ins0", OS.SendMessage (h, OS.LB_INSERTSTRING, 0, 0), h);
		p ("B.setcount10b", OS.SendMessage (h, LB_SETCOUNT, 10, 0), h);
		p ("B.ins3", OS.SendMessage (h, OS.LB_INSERTSTRING, 3, 0), h);
		OS.DestroyWindow (h);
		h = OS.CreateWindowEx (0, cls, null, base | OS.LBS_EXTENDEDSEL, 0, 0, 200, 200, shell.handle, 0, OS.GetModuleHandle (null), null);
		p ("M.setcount100", OS.SendMessage (h, LB_SETCOUNT, 100, 0), h);
		p ("M.selrangeex10-20", OS.SendMessage (h, OS.LB_SELITEMRANGEEX, 10, 20), h);
		p ("M.setsel1-40", OS.SendMessage (h, OS.LB_SETSEL, 1, 40), h);
		p ("M.selrange50-60", OS.SendMessage (h, 0x19B, 1, OS.MAKELPARAM (50, 60)), h);
		p ("M.delete0", OS.SendMessage (h, OS.LB_DELETESTRING, 0, 0), h);
		p ("M.selrangeex5-5", OS.SendMessage (h, OS.LB_SELITEMRANGEEX, 5, 5), h);
		System.out.println ("M.isSelected5	" + OS.SendMessage (h, OS.LB_GETSEL, 5, 0));
		p ("M.selrangeex70-71", OS.SendMessage (h, OS.LB_SELITEMRANGEEX, 70, 71), h);
		p ("M.selrangeex71-71", OS.SendMessage (h, OS.LB_SELITEMRANGEEX, 71, 71), h);
		System.out.println ("M.isSelected70-71	" + OS.SendMessage (h, OS.LB_GETSEL, 70, 0) + "," + OS.SendMessage (h, OS.LB_GETSEL, 71, 0));
		p ("M.selrangeex80-79", OS.SendMessage (h, OS.LB_SELITEMRANGEEX, 80, 79), h);
		System.out.println ("M.isSelected79-80	" + OS.SendMessage (h, OS.LB_GETSEL, 79, 0) + "," + OS.SendMessage (h, OS.LB_GETSEL, 80, 0));
		OS.DestroyWindow (h);
		shell.dispose ();
		display.dispose ();
	}

	static void p (String op, long rc, long h) {
		System.out.println (op + "\trc=" + rc + "\tcount=" + OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0)
			+ "\tcaret=" + OS.SendMessage (h, OS.LB_GETCARETINDEX, 0, 0) + "\tanchor=" + OS.SendMessage (h, LB_GETANCHORINDEX, 0, 0)
			+ "\tcursel=" + OS.SendMessage (h, OS.LB_GETCURSEL, 0, 0) + "\tselcount=" + OS.SendMessage (h, OS.LB_GETSELCOUNT, 0, 0));
	}
}

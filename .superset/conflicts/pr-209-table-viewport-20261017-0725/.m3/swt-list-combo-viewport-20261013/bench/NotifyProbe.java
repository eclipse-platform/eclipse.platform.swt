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
import org.eclipse.swt.widgets.List;

/** Counts SWT.Selection events from native keyboard and mouse input on a plain and a VIRTUAL list. */
public class NotifyProbe {
	static int events;

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (10, 10, 300, 300);
		shell.open ();
		for (int style : new int [] {SWT.SINGLE, SWT.SINGLE | SWT.VIRTUAL, SWT.MULTI, SWT.MULTI | SWT.VIRTUAL}) {
			List list = new List (shell, style);
			list.setBounds (5, 5, 200, 200);
			list.setItems ("apple", "banana", "berry", "cherry", "date");
			list.addListener (SWT.Selection, e -> events++);
			events = 0;
			boolean focus = list.setFocus ();
			StringBuilder trace = new StringBuilder ();
			OS.SendMessage (list.handle, OS.WM_KEYDOWN, OS.VK_DOWN, 0);
			drain (display, shell);
			trace.append ("down:").append (events).append ('/').append (list.getSelectionIndex ()).append (' ');
			OS.SendMessage (list.handle, OS.WM_CHAR, 'c', 0);
			drain (display, shell);
			trace.append ("char-c:").append (events).append ('/').append (list.getSelectionIndex ()).append (' ');
			int y = list.getItemHeight () * 4 + 3;
			OS.SendMessage (list.handle, OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, OS.MAKELPARAM (10, y));
			OS.SendMessage (list.handle, OS.WM_LBUTTONUP, 0, OS.MAKELPARAM (10, y));
			drain (display, shell);
			trace.append ("click4:").append (events).append ('/').append (list.getSelectionIndex ());
			System.out.println (Integer.toHexString (style) + "\tfocus=" + focus + "\tgetFocus=" + (OS.GetFocus () == list.handle) + "\t" + trace);
			list.dispose ();
		}
		shell.dispose ();
		display.dispose ();
	}

	static void drain (Display display, Shell shell) {
		OS.PostMessage (shell.handle, OS.WM_NULL, 0, 0);
		while (display.readAndDispatch ()) {
			// drain, including deferred events
		}
	}
}

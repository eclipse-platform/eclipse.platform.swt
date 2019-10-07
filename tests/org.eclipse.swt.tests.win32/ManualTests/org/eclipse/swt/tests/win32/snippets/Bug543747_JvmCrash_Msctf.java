/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug543747_JvmCrash_Msctf {
	/**
	 * This test demonstrates a crash where destroying a Shell
	 * accesses context already freed by 'ImmDestroyContext'.
	 */
	public static void reproduceCrash1(Shell parentShell) {
		Shell tempShell = new Shell(parentShell);

		// Create something to catch initial focus, so that
		// text.setFocus() does something. This is only to
		// show that .setFocus() is important.
		new Button(tempShell, SWT.PUSH);

		// This Text will cause crash.
		Text text = new Text(tempShell, 0);

		// Shell must be visible to prevent early return in .setFocus()
		tempShell.setSize(10, 10);
		tempShell.open();

		// ImmAssociateContext() itself is lazy.
		// .setFocus() causes it to start up.
		text.setFocus();

		// Destroying the shell triggers the bug.
		tempShell.dispose();

		// JVM still alive?
		MessageBox msgbox = new MessageBox(parentShell);
		msgbox.setMessage("Crash didn't reproduce");
		msgbox.open();
	}

	/**
	 * This test demonstrates a crash where destroying a Shell
	 * accesses context already freed by 'ImmDestroyContext'
	 * if IME-capable Control has an intermediate parent.
	 */
	public static void reproduceCrash23(Shell parentShell, boolean focusEditOnClose) {
		Shell tempShell = new Shell(parentShell);

		// Create something to catch initial focus, so that
		// text.setFocus() does something. This is only to
		// show that .setFocus() is important.
		Button button = new Button(tempShell, SWT.PUSH);

		// This Text will cause crash.
		// Text needs to have an intermediate parent for this bug.
		Composite composite = new Composite(tempShell, 0);
		Text text = new Text(composite, 0);

		// Shell must be visible to prevent early return in .setFocus()
		tempShell.setSize(10, 10);
		tempShell.open();

		// ImmAssociateContext() itself is lazy.
		// .setFocus() causes it to start up.
		text.setFocus();

		// There are two slightly different crashes depending on focus
		if (!focusEditOnClose)
			button.setFocus();

		// Destroying the shell triggers the bug.
		tempShell.dispose();

		// Give additional instructions
		MessageBox msgbox = new MessageBox(parentShell);
		msgbox.setMessage(
				"Now please do one of:\n" +
				"a) Open Task Manager, go to Users, right-click your user, select Disconnect, log in again\n" +
				"b) Connect Remote Desktop to this machine\n" +
				"\n" +
				"The snippet is expected to crash just after that."
		);
		msgbox.open();
	}

	/**
	 * This test demonstrates a crash where destroying a Composite
	 * with Text inside it crashes, even if 'ImmDestroyContext' wasn't
	 * called yet.
	 */
	public static void reproduceCrash4(Shell parentShell) {
		Shell tempShell = new Shell(parentShell);

		// Create something to catch initial focus, so that
		// text.setFocus() does something. This is only to
		// show that .setFocus() is important.
		// new Button(tempShell, SWT.PUSH);

		// This Text will cause crash.
		// Text needs to have an intermediate parent for this bug.
		Composite composite = new Composite(tempShell, 0);
		Text text = new Text(composite, 0);

		// Shell must be visible to prevent early return in .setFocus()
		tempShell.setSize(10, 10);
		tempShell.open();

		// ImmAssociateContext() itself is lazy.
		// .setFocus() causes it to start up.
		text.setFocus();

		// Destroying the composite triggers the bug.
		composite.dispose();

		// Cleanup
		tempShell.dispose();

		// JVM still alive?
		MessageBox msgbox = new MessageBox(parentShell);
		msgbox.setMessage("Crash didn't reproduce");
		msgbox.open();
	}

	public static void main (String [] args) {
		Display display = new Display ();

		Shell shell = new Shell (display);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		shell.setLayout(layout);

		final Text labelInfo = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		labelInfo.setText(
				"Both of these crashes are only seen on Win10 1809+\n" +
				"\n" +
				"To reproduce reliably, use Application Verifier:\n" +
				"1) Install Application Verifier:\n" +
				"a) Download Windows SDK:\n" +
				"   https://go.microsoft.com/fwlink/p/?LinkID=2033908\n" +
				"b) Install it, selecting Application Verifier. Other components are not required.\n" +
				"\n" +
				"2) Configure Application Verifier\n" +
				"a) Run 'Application Verifier (X64)' from Start menu.\n" +
				"b) Use File | Add application... to add java.exe\n" +
				"c) IMPORTANT: On the right pane, make sure that only 'Basics/Heaps' is selected.\n" +
				"   JVM always crashes with 'Basics/Exceptions' and 'Basics/Memory'\n" +
				"d) Click 'Save'. You can close Application Verifier now\n" +
				"   It will be active until you explicitly disable it.\n" +
				"e) Restart application once.\n" +
				"f) Note: applications run slower and consume more RAM under Application Verifier.\n" +
				"\n" +
				"3) Reproduce the problem\n" +
				"\n" +
				"4) Disable Application Verifier if you want\n" +
				"a) Keep it enabled it if you're ready to tolerate the slowness, but find more bugs\n" +
				"b) Go to Application Verifier again\n" +
				"c) Delete java.exe from the list.\n" +
				"d) Click Save.\n" +
				"e) There's no need to uninstall Application Verifier, but you can do that if you like."
		);

		final Button buttonCrash1 = new Button(shell, SWT.PUSH);
		buttonCrash1.setText("Reproduce crash #1 - Bug 526758");
		buttonCrash1.addListener(SWT.Selection, event -> {reproduceCrash1(shell);});

		final Button buttonCrash2 = new Button(shell, SWT.PUSH);
		buttonCrash2.setText("Reproduce crash #2 - Bug 543747 (A)");
		buttonCrash2.addListener(SWT.Selection, event -> {reproduceCrash23(shell, true);});

		final Button buttonCrash3 = new Button(shell, SWT.PUSH);
		buttonCrash3.setText("Reproduce crash #3 - Bug 543747 (B)");
		buttonCrash3.addListener(SWT.Selection, event -> {reproduceCrash23(shell, false);});

		final Button buttonCrash4 = new Button(shell, SWT.PUSH);
		buttonCrash4.setText("Reproduce crash #4 - Bug 543747 (C)");
		buttonCrash4.addListener(SWT.Selection, event -> {reproduceCrash4(shell);});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}

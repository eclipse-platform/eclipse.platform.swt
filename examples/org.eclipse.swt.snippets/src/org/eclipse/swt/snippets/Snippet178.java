/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * How to access About, Preferences and Quit menus on carbon.
 * NOTE: This snippet uses internal SWT packages that are
 * subject to change without notice.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet178 {

	private static final int kHICommandPreferences = ('p' << 24) + ('r' << 16)
			+ ('e' << 8) + 'f';

	private static final int kHICommandAbout = ('a' << 24) + ('b' << 16)
			+ ('o' << 8) + 'u';

	private static final int kHICommandServices = ('s' << 24) + ('e' << 16)
			+ ('r' << 8) + 'v';

public static void main(String[] arg) {
	Display.setAppName("AppMenu"); // sets name in Dock
	Display display = new Display();
	hookApplicationMenu(display, "About AppMenu");
	Shell shell = new Shell(display);
	shell.setText("Main Window");
	shell.open();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();

	display.dispose();
}

static void hookApplicationMenu(Display display, final String aboutName) {
	// Callback target
	Object target = new Object() {
		int commandProc(int nextHandler, int theEvent, int userData) {
			if (OS.GetEventKind(theEvent) == OS.kEventProcessCommand) {
				HICommand command = new HICommand();
				OS.GetEventParameter(theEvent, OS.kEventParamDirectObject,
						OS.typeHICommand, null, HICommand.sizeof, null,
						command);
				switch (command.commandID) {
				case kHICommandPreferences:
					return handleCommand("Preferences"); //$NON-NLS-1$
				case kHICommandAbout:
					return handleCommand(aboutName);
				default:
					break;
				}
			}
			return OS.eventNotHandledErr;
		}

		int handleCommand(String command) {
			Shell shell = new Shell();
			MessageBox preferences = new MessageBox(shell, SWT.ICON_WARNING);
			preferences.setText(command);
			preferences.open();
			shell.dispose();
			return OS.noErr;
		}
	};

	final Callback commandCallback = new Callback(target, "commandProc", 3); //$NON-NLS-1$
	int commandProc = commandCallback.getAddress();
	if (commandProc == 0) {
		commandCallback.dispose();
		return; // give up
	}

	// Install event handler for commands
	int[] mask = new int[] { OS.kEventClassCommand, OS.kEventProcessCommand };
	OS.InstallEventHandler(OS.GetApplicationEventTarget(), commandProc,
			mask.length / 2, mask, 0, null);

	// create About ... menu command
	int[] outMenu = new int[1];
	short[] outIndex = new short[1];
	if (OS.GetIndMenuItemWithCommandID(0, kHICommandPreferences, 1,
			outMenu, outIndex) == OS.noErr
			&& outMenu[0] != 0) {
		int menu = outMenu[0];

		int l = aboutName.length();
		char buffer[] = new char[l];
		aboutName.getChars(0, l, buffer, 0);
		int str = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault,
				buffer, l);
		OS.InsertMenuItemTextWithCFString(menu, str, (short) 0, 0,
				kHICommandAbout);
		OS.CFRelease(str);

		// add separator between About & Preferences
		OS.InsertMenuItemTextWithCFString(menu, 0, (short) 1,
				OS.kMenuItemAttrSeparator, 0);

		// enable pref menu
		OS.EnableMenuCommand(menu, kHICommandPreferences);

		// disable services menu
		OS.DisableMenuCommand(menu, kHICommandServices);
	}

	// schedule disposal of callback object
	display.disposeExec(new Runnable() {
		public void run() {
			commandCallback.dispose();
		}
	});
}
}

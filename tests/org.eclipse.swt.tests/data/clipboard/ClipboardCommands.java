/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package clipboard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClipboardCommands extends Remote {
	String PORT_MESSAGE = "ClipboardCommands Registry Port: ";
	String ID = "ClipboardCommands";

	/**
	 * Same value as SWT's DND.CLIPBOARD
	 */
	int CLIPBOARD = 1 << 0;
	/**
	 * Same value as SWT's DND.SELECTION_CLIPBOARD
	 */
	int SELECTION_CLIPBOARD = 1 << 1;


	void stop() throws RemoteException;

	/**
	 * @param string string to set as contents
	 * @param clipboardId {@link #CLIPBOARD} or {@value #SELECTION_CLIPBOARD}
	 */
	void setContents(String string, int clipboardId) throws RemoteException;

	void setFocus() throws RemoteException;

	/**
	 * @param clipboardId {@link #CLIPBOARD} or {@value #SELECTION_CLIPBOARD}
	 */
	String getStringContents(int clipboardId) throws RemoteException;

	void waitUntilReady() throws RemoteException;

	void waitForButtonPress() throws RemoteException;
}

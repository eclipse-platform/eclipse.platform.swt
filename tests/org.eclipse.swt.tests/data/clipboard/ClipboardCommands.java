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

	void stop() throws RemoteException;

	void setContents(String string) throws RemoteException;

	void setFocus() throws RemoteException;

	String getStringContents() throws RemoteException;

	void waitUntilReady() throws RemoteException;
}

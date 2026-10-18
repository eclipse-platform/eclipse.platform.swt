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

	/**
	 * The port the RMI will listen on by default
	 */
	int DEFAULT_PORT = 3456;

	void stop() throws RemoteException;

	void setFocus() throws RemoteException;

	void waitUntilReady() throws RemoteException;

	void waitForButtonPress() throws RemoteException;

	/**
	 * @param string string to set as contents on {@link #CLIPBOARD}
	 */
	void setContents(String string) throws RemoteException;

	/**
	 * @param string      string to set as contents
	 * @param clipboardId {@link #CLIPBOARD} or {@value #SELECTION_CLIPBOARD}
	 */
	void setContents(String string, int clipboardId) throws RemoteException;

	void setRtfContents(String string) throws RemoteException;

	String getRtfContents() throws RemoteException;

	void setHtmlContents(String string) throws RemoteException;

	String getHtmlContents() throws RemoteException;

	void setUrlContents(byte[] string) throws RemoteException;

	byte[] getUrlContents() throws RemoteException;

	String getStringContents() throws RemoteException;

	/**
	 * @param clipboardId {@link #CLIPBOARD} or {@value #SELECTION_CLIPBOARD}
	 */
	String getStringContents(int clipboardId) throws RemoteException;

	void setImageContents(byte[] imageContents) throws RemoteException;

	byte[] getImageContents() throws RemoteException;

	void setFileListContents(String[] fileList) throws RemoteException;

	String[] getFileListContents() throws RemoteException;

	void setMyTypeContents(byte[] bytes) throws RemoteException;

	byte[] getMyTypeContents() throws RemoteException;

}

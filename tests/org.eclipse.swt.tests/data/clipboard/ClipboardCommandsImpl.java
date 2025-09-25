/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import javax.swing.SwingUtilities;

public class ClipboardCommandsImpl extends UnicastRemoteObject implements ClipboardCommands {
	private static final long serialVersionUID = 330098269086266134L;
	private ClipboardTest clipboardTest;

	protected ClipboardCommandsImpl(ClipboardTest clipboardTest) throws RemoteException {
		super();
		this.clipboardTest = clipboardTest;
	}

	@Override
	public void waitUntilReady() throws RemoteException {
		invokeAndWait(() -> {
			clipboardTest.log("waitUntilReady()");
		});
	}

	@Override
	public void stop() throws RemoteException {
		invokeAndWait(() -> {
			clipboardTest.log("stop()");
			clipboardTest.dispose();
		});
	}

	@Override
	public void setContents(String text) throws RemoteException {
		invokeAndWait(() -> {
			clipboardTest.log("setContents(\"" + text + "\")");
			StringSelection selection = new StringSelection(text);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

		});
	}

	@Override
	public String getStringContents() throws RemoteException {
		String[] data = new String[] { null };
		invokeAndWait(() -> {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			try {
				data[0] = (String) clipboard.getData(DataFlavor.stringFlavor);
				clipboardTest.log("getStringContents() returned " + data[0]);
			} catch (Exception e) {
				data[0] = null;
				DataFlavor[] availableDataFlavors = clipboard.getAvailableDataFlavors();
				clipboardTest.log("getStringContents() threw " + e.toString()
						+ " and returned null. The clipboard had availableDataFlavors = "
						+ Arrays.asList(availableDataFlavors));
			}
		});
		return data[0];
	}

	@Override
	public void setFocus() throws RemoteException {
		invokeAndWait(() -> {
			clipboardTest.log("setFocus()");
			clipboardTest.requestFocus();
		});
	}

	private void invokeAndWait(Runnable run) throws RemoteException {
		try {
			SwingUtilities.invokeAndWait(run);
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RemoteException("Failed to run in Swing", e);
		}
	}

}
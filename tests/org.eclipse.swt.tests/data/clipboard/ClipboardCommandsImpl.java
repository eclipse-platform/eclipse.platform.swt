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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class ClipboardCommandsImpl extends UnicastRemoteObject implements ClipboardCommands {
	private static final long serialVersionUID = 330098269086266134L;
	private ClipboardTest clipboardTest;
	CountDownLatch buttonPressed;

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
		// Force the test program to quit, but after a short delay so that
		// the return value of stop can make it back to the caller and
		// the JUnit test may destroy the process before this sleep
		// expires anyway
		SwingUtilities.invokeLater(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
			System.exit(0);
		});
	}

	@Override
	public void setContents(String text, int clipboardId) throws RemoteException {
		invokeAndWait(() -> {
			String display = text == null ? "null" : ("\"" + text + "\"");
			clipboardTest.log("setContents(" + display + ", " + clipboardId + ")");
			StringSelection selection = new StringSelection(text);
			getClipboard(clipboardId).setContents(selection, null);

		});
	}

	private Clipboard getClipboard(int clipboardId) {
		Clipboard clipboard;
		if (clipboardId == SELECTION_CLIPBOARD) {
			clipboard = Toolkit.getDefaultToolkit().getSystemSelection();
		} else if (clipboardId == CLIPBOARD) {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} else {
			throw new RuntimeException("Invalid clipboardId " + clipboardId);
		}
		return clipboard;
	}

	@Override
	public String getStringContents(int clipboardId) throws RemoteException {
		String[] data = new String[] { null };
		invokeAndWait(() -> {
			Clipboard clipboard = getClipboard(clipboardId);
			try {
				data[0] = (String) clipboard.getData(DataFlavor.stringFlavor);
				clipboardTest.log("getStringContents(" + clipboardId + ") returned " + data[0]);
			} catch (Exception e) {
				data[0] = null;
				DataFlavor[] availableDataFlavors = clipboard.getAvailableDataFlavors();
				clipboardTest.log("getStringContents(" + clipboardId + ") threw " + e.toString()
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

	@Override
	public void waitForButtonPress() throws RemoteException {
		clipboardTest.log("waitForButtonPress() - START");
		buttonPressed = new CountDownLatch(1);
		try {
			if (buttonPressed.await(10, TimeUnit.SECONDS)) {
				clipboardTest.log("waitForButtonPress() - SUCCESS");
			} else {
				clipboardTest.log("waitForButtonPress() - FAILED Timeout");
				throw new RemoteException("Button not pressed in time");
			}
		} catch (InterruptedException e) {
			clipboardTest.log("waitForButtonPress() - FAILED Interrupted");
			throw new RemoteException("Interrupted while waiting for button press", e);
		}
	}

	private void invokeAndWait(Runnable run) throws RemoteException {
		try {
			SwingUtilities.invokeAndWait(run);
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RemoteException("Failed to run in Swing", e);
		}
	}

}

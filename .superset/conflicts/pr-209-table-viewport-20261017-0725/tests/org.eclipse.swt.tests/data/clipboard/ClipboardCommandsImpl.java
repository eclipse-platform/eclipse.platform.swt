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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
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
	public void setContents(String string) throws RemoteException {
		setContents(string, CLIPBOARD);
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

	@Override
	public void setRtfContents(String text) throws RemoteException {
		invokeAndWait(() -> {
			String display = text == null ? "null" : ("\"" + text + "\"");
			clipboardTest.log("setRtfContents(" + display + ")");
			getClipboard(CLIPBOARD).setContents(new RtfSelection(text.getBytes(StandardCharsets.UTF_8)), null);
		});
	}

	@Override
	public String getRtfContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getRtfContents", RtfSelection.flavor);
		if (!(contents instanceof InputStream stream)) {
			return null;
		}
		try (stream) {
			byte[] allBytes = stream.readAllBytes();
			return new String(allBytes, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RemoteException("Failed to convert stream to String", e);
		}
	}

	@Override
	public void setHtmlContents(String text) throws RemoteException {
		invokeAndWait(() -> {
			String display = text == null ? "null" : ("\"" + text + "\"");
			clipboardTest.log("setHtmlContents(" + display + ")");
			getClipboard(CLIPBOARD).setContents(new HtmlSelection(text.getBytes(StandardCharsets.UTF_8)), null);
		});
	}

	@Override
	public String getHtmlContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getHtmlContents", HtmlSelection.flavor);
		if (!(contents instanceof InputStream stream)) {
			return null;
		}
		try (stream) {
			byte[] allBytes = stream.readAllBytes();
			return new String(allBytes, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RemoteException("Failed to convert stream to String", e);
		}
	}

	@Override
	public void setUrlContents(byte[] bytes) throws RemoteException {
		invokeAndWait(() -> {
			String display = bytes == null ? "null" : ("bytes");
			clipboardTest.log("setUrlContents(" + display + ")");
			getClipboard(CLIPBOARD).setContents(new UrlSelection(bytes), null);
		});
	}

	@Override
	public byte[] getUrlContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getUrlContents", UrlSelection.flavor);
		if (!(contents instanceof InputStream stream)) {
			return null;
		}
		try (stream) {
			return stream.readAllBytes();
		} catch (IOException e) {
			throw new RemoteException("Failed to convert stream to byte[]", e);
		}
	}

	@Override
	public void setMyTypeContents(byte[] bytes) throws RemoteException {
		invokeAndWait(() -> {
			String display = bytes == null ? "null" : ("bytes");
			clipboardTest.log("setMyTypeContents(" + display + ")");
			getClipboard(CLIPBOARD).setContents(new MyTypeSelection(bytes), null);
		});
	}

	@Override
	public byte[] getMyTypeContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getMyTypeContents", MyTypeSelection.flavor);
		if (!(contents instanceof InputStream stream)) {
			return null;
		}
		try (stream) {
			return stream.readAllBytes();
		} catch (IOException e) {
			throw new RemoteException("Failed to convert stream to byte[]", e);
		}
	}

	@Override
	public void setImageContents(byte[] imageContents) throws RemoteException {
		invokeAndWait(() -> {
			BufferedImage img;
			try {
				img = ImageIO.read(new ByteArrayInputStream(imageContents));
				if (img == null) {
					throw new IOException("ImageIO.read unable to convert bytes to image file");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			getClipboard(CLIPBOARD).setContents(new ImageSelection(img), null);
		});
	}

	@Override
	public byte[] getImageContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getImageContents", DataFlavor.imageFlavor);
		if (!(contents instanceof Image image)) {
			return null;
		}
		if (contents instanceof MultiResolutionImage mri) {
			image = mri.getResolutionVariant(image.getWidth(null), image.getHeight(null));
		}
		if (!(image instanceof BufferedImage bufferedImage)) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			throw new RemoteException("Failed to convert image to png byte array", e);
		}
		return baos.toByteArray();
	}

	@Override
	public void setFileListContents(String[] fileList) throws RemoteException {
		invokeAndWait(() -> {
			getClipboard(CLIPBOARD).setContents(new FileListSelection(Arrays.asList(fileList)), null);
		});
	}

	@Override
	public String[] getFileListContents() throws RemoteException {
		Object contents = getContents(CLIPBOARD, "getFileListContents", DataFlavor.javaFileListFlavor);
		if (!(contents instanceof List<?> fileList)) {
			return null;
		}

		return fileList.stream().map(f -> (File) f).map(File::toString).toArray(String[]::new);
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
	public String getStringContents() throws RemoteException {
		return getStringContents(CLIPBOARD);
	}

	@Override
	public String getStringContents(int clipboardId) throws RemoteException {
		return (String) getContents(clipboardId, "getStringContents", DataFlavor.stringFlavor);
	}

	private Object getContents(int clipboardId, String methodName, DataFlavor flavor) throws RemoteException {
		Object[] data = new Object[] { null };
		invokeAndWait(() -> {
			Clipboard clipboard = getClipboard(clipboardId);
			try {
				data[0] = clipboard.getData(flavor);
				clipboardTest.log(methodName + "(" + clipboardId + ") returned " + data[0]);
			} catch (Exception e) {
				data[0] = null;
				DataFlavor[] availableDataFlavors = clipboard.getAvailableDataFlavors();
				clipboardTest.log(methodName + "(" + clipboardId + ") threw " + e.toString()
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

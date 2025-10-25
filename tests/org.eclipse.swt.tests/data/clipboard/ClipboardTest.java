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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.eclipse.swt.tests.junit.Test_org_eclipse_swt_dnd_Clipboard;

/**
 * Test program used by {@link Test_org_eclipse_swt_dnd_Clipboard}.
 *
 *
 */
@SuppressWarnings("serial")
public class ClipboardTest extends JFrame {
	private static final class LocalHostOnlySocketFactory extends RMISocketFactory {
		@Override
		public ServerSocket createServerSocket(int port) throws IOException {
			return new ServerSocket(port, 50, InetAddress.getLoopbackAddress());
		}

		@Override
		public Socket createSocket(String host, int port) throws IOException {
			return new Socket(InetAddress.getLoopbackAddress(), port);
		}
	}

	private static Registry rmiRegistry;
	private JTextArea textArea;
	private ClipboardCommandsImpl commands;

	public ClipboardTest() throws RemoteException {
		super("ClipboardTest");
		registerFlavors();
		commands = new ClipboardCommandsImpl(this);
		rmiRegistry.rebind(ClipboardCommands.ID, commands);

		textArea = new JTextArea(10, 40);
		JScrollPane scrollPane = new JScrollPane(textArea);

		JButton copyButton = new JButton("Copy");
		JButton pasteButton = new JButton("Paste");
		JButton pressToContinue = new JButton("Press To Continue Test");
		copyButton.addActionListener(e -> {
			String text = textArea.getSelectedText();
			if (text != null) {
				StringSelection selection = new StringSelection(text);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			}
		});

		pasteButton.addActionListener(e -> {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			try {
				String data = (String) clipboard.getData(DataFlavor.stringFlavor);
				textArea.insert(data, textArea.getCaretPosition());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ClipboardTest.this, "Could not paste from clipboard", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		pressToContinue.addActionListener(e -> {
			commands.buttonPressed.countDown();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(copyButton);
		buttonPanel.add(pasteButton);
		buttonPanel.add(pressToContinue);

		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null); // Center on screen
		setVisible(true);
	}

	public void log(String log) {
		textArea.insert(log, textArea.getCaretPosition());
		if (!log.endsWith("\n")) {
			textArea.insert("\n", textArea.getCaretPosition());
		}
	}

	public static void main(String[] args) throws IOException {
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");

		// Make sure RMI is localhost only
		RMISocketFactory.setSocketFactory(new LocalHostOnlySocketFactory());
		int chosenPort = getAvailablePort();
		rmiRegistry = LocateRegistry.createRegistry(chosenPort);
		System.out.println(ClipboardCommands.PORT_MESSAGE + chosenPort);

		SwingUtilities.invokeLater(() -> {
			try {
				new ClipboardTest();
			} catch (RemoteException e) {
				System.err.println("Failed to start ClipboardTest");
				e.printStackTrace();
				System.exit(1);
			}
		});
	}

	/**
	 * Because LocateRegistry requires reflection and/or using sun.* packages to get
	 * the running port, use ServerSocket to get a free port.
	 */
	private static int getAvailablePort() throws IOException {
		try (var ss = new java.net.ServerSocket(0)) {
			return ss.getLocalPort();
		}
	}

	static final DataFlavor RTF_InputStream;
	static final DataFlavor HTML_InputStream;
	/**
	 * This flavor matches how text/x-moz-url is documented in
	 * https://developer.mozilla.org/en-US/docs/Web/API/HTML_Drag_and_Drop_API/Drag_data_store
	 * and matches what SWT's URLTransfer implemented on GTK at the time of writing,
	 * which was UTF-16 chars rather than UTF-8
	 */
	static final DataFlavor URL_InputStream;
	static final DataFlavor MyType_InputStream;
	static {
		try {
			RTF_InputStream = new DataFlavor("text/rtf;class=java.io.InputStream");
			HTML_InputStream = new DataFlavor("text/html;class=java.io.InputStream");
			URL_InputStream = new DataFlavor("text/x-moz-url;class=java.io.InputStream");
			MyType_InputStream = new DataFlavor("application/my_type_name;class=java.io.InputStream");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	static void registerFlavors() {
		registerRtfNatives();
		registerHtmlNatives();
		registerUrlNatives();
		registerMyTypeNatives();
	}

	static void registerRtfNatives() {
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String nativeFmt = "Rich Text Format";
			map.addUnencodedNativeForFlavor(RTF_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, RTF_InputStream);
		} else if (os.contains("mac")) {
			String nativeFmt = "public.rtf";
			map.addUnencodedNativeForFlavor(RTF_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, RTF_InputStream);
		} else {
			// X11/Wayland
			for (String nativeFmt : List.of("text/rtf", "application/rtf")) {
				map.addUnencodedNativeForFlavor(RTF_InputStream, nativeFmt);
				map.addFlavorForUnencodedNative(nativeFmt, RTF_InputStream);
			}
		}
	}

	static void registerHtmlNatives() {
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String nativeFmt = "HTML Format";
			map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
		} else if (os.contains("mac")) {
			String nativeFmt = "public.html";
			map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
		} else {
			// X11/Wayland
			for (String nativeFmt : List.of("text/html")) {
				map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
				map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
			}
		}
	}

	static void registerUrlNatives() {
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String nativeFmt = "HTML Format";
			map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
		} else if (os.contains("mac")) {
			String nativeFmt = "public.html";
			map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
			map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
		} else {
			// X11/Wayland
			for (String nativeFmt : List.of("text/html")) {
				map.addUnencodedNativeForFlavor(HTML_InputStream, nativeFmt);
				map.addFlavorForUnencodedNative(nativeFmt, HTML_InputStream);
			}
		}
	}

	static void registerMyTypeNatives() {
		SystemFlavorMap map = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
		map.addUnencodedNativeForFlavor(URL_InputStream, "my_type_name");
		map.addFlavorForUnencodedNative("my_type_name", URL_InputStream);
	}

	static class RtfSelection implements Transferable {
		private final byte[] rtf;

		RtfSelection(byte[] rtf) {
			this.rtf = rtf;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { RTF_InputStream };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor f) {
			return f.equals(RTF_InputStream);
		}

		@Override
		public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException {
			if (f.equals(RTF_InputStream))
				return new ByteArrayInputStream(rtf);
			throw new UnsupportedFlavorException(f);
		}

	}

	static class HtmlSelection implements Transferable {
		private final byte[] html;

		HtmlSelection(byte[] html) {
			this.html = html;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { HTML_InputStream };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor f) {
			return f.equals(HTML_InputStream);
		}

		@Override
		public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException {
			if (f.equals(HTML_InputStream))
				return new ByteArrayInputStream(html);
			throw new UnsupportedFlavorException(f);
		}
	}

	/**
	 * See {@link #URL_InputStream}
	 */
	static class UrlSelection implements Transferable {
		private final byte[] url;

		UrlSelection(byte[] html) {
			this.url = html;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { URL_InputStream };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor f) {
			return f.equals(URL_InputStream);
		}

		@Override
		public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException {
			if (f.equals(URL_InputStream))
				return new ByteArrayInputStream(url);
			throw new UnsupportedFlavorException(f);
		}

	}

	static final class ImageSelection implements Transferable {
		private final Image image;

		ImageSelection(Image image) {
			this.image = image;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return image;
		}
	}

	static final class FileListSelection implements Transferable {
		private final List<String> files;

		FileListSelection(List<String> files) {
			this.files = files;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.javaFileListFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return files;
		}
	}

	static final class MyTypeSelection implements Transferable {
		private final byte[] bytes;

		MyTypeSelection(byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { MyType_InputStream };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return MyType_InputStream.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return new ByteArrayInputStream(bytes);
		}
	}
}

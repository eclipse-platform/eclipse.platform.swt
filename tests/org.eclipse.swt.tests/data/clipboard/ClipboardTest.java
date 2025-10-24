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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	public ClipboardTest() throws Exception {
		super("ClipboardTest");
		registerSelectionTypes();
		commands = new ClipboardCommandsImpl(this);
		rmiRegistry.rebind(ClipboardCommands.ID, commands);

		JLabel label = new JLabel("""
				<html>This window is a AWT/Swing window used to test the SWT Clipboard<br>
				This window will be opened and closed repeatedly while running<br>
				clipboard JUnit tests. Do not close this window (unless the JUnit<br>
				tests have completed)<br>
				<br>
				If the Press Button to Continue Test becomes enabled, press it.</html>
				""");
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

		pressToContinue.setEnabled(false);
		pressToContinue.addActionListener(e -> {
			commands.buttonPressed.countDown();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(copyButton);
		buttonPanel.add(pasteButton);
		buttonPanel.add(pressToContinue);

		add(label, BorderLayout.NORTH);
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

		boolean autoPort = Arrays.asList(args).contains("-autoport");

		// Make sure RMI is localhost only
		RMISocketFactory.setSocketFactory(new LocalHostOnlySocketFactory());
		int chosenPort = autoPort ? getAvailablePort() : ClipboardCommands.DEFAULT_PORT;
		rmiRegistry = LocateRegistry.createRegistry(chosenPort);
		System.out.println(ClipboardCommands.PORT_MESSAGE + chosenPort);

		SwingUtilities.invokeLater(() -> {
			try {
				new ClipboardTest();
			} catch (Exception e) {
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

	static void registerSelectionTypes() throws Exception {
		RtfSelection.register();
		HtmlSelection.register();
		UrlSelection.register();
		MyTypeSelection.register();
	}
}

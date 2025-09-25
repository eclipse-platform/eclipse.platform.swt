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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import clipboard.ClipboardCommands;

/**
 * Automated Test Suite for class org.eclipse.swt.dnd.Clipboard
 *
 * @see org.eclipse.swt.dnd.Clipboard
 * @see Test_org_eclipse_swt_custom_StyledText StyledText tests as it also does
 *      some clipboard tests
 */
public class Test_org_eclipse_swt_dnd_Clipboard {

	private static final int REPEAT_COUNT = 3;
	@TempDir
	static Path tempFolder;
	static int uniqueId = 1;
	private Display display;
	private Shell shell;
	private Clipboard clipboard;
	private TextTransfer textTransfer;
	private RTFTransfer rtfTransfer;
	private ClipboardCommands remote;
	private Process remoteClipboardProcess;

	@BeforeEach
	public void setUp() {
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}

		clipboard = new Clipboard(display);
		textTransfer = TextTransfer.getInstance();
		rtfTransfer = RTFTransfer.getInstance();
	}

	/**
	 * TODO remove all uses of sleep and change them to processEvents with the
	 * suitable conditional, or entirely remove them
	 */
	private void sleep() throws InterruptedException {
		SwtTestUtil.processEvents(1000, null);
	}

	/**
	 * Note: Wayland backend does not allow access to system clipboard from
	 * non-focussed windows. So we have to create/open and focus a window here so
	 * that clipboard operations work.
	 */
	private void openAndFocusShell() throws InterruptedException {
		shell = new Shell(display);
		shell.open();
		shell.setFocus();
		sleep();
	}

	/**
	 * Note: Wayland backend does not allow access to system clipboard from
	 * non-focussed windows. So we have to open and focus remote here so that
	 * clipboard operations work.
	 */
	private void openAndFocusRemote() throws Exception {
		startRemoteClipboardCommands();
		remote.setFocus();
		remote.waitUntilReady();
		sleep();
	}

	@AfterEach
	public void tearDown() throws Exception {
		sleep();
		try {
			stopRemoteClipboardCommands();
		} finally {
			if (clipboard != null) {
				clipboard.dispose();
			}
			if (shell != null) {
				shell.dispose();
			}
			SwtTestUtil.processEvents();
		}
	}

	private void startRemoteClipboardCommands() throws Exception {
		// TODO: Can I get the jar and run it without extracting it?
		// TODO: Can I get all the files without having to list them?
		List.of( //
				"ClipboardTest", //
				"ClipboardCommands", //
				"ClipboardCommandsImpl", //
				"ClipboardTest$LocalHostOnlySocketFactory" //
		).forEach((f) -> {
			// extract the files and put them in the temp directory
			SwtTestUtil.getPath("/clipboard/" + f + ".class", tempFolder.resolve("clipboard/" + f + ".class"));
		});

		String javaHome = System.getProperty("java.home");
		String javaExe = javaHome + "/bin/java";
		assertTrue(Files.exists(Path.of(javaExe)));

		ProcessBuilder pb = new ProcessBuilder(javaExe, "clipboard.ClipboardTest").directory(tempFolder.toFile());
		pb.inheritIO();
		pb.redirectOutput(Redirect.PIPE);
		remoteClipboardProcess = pb.start();

		// Read server output to find the port
		BufferedReader reader = new BufferedReader(new InputStreamReader(remoteClipboardProcess.getInputStream()));
		int port = 0;
		String line;
		// TODO: add a timeout here
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(ClipboardCommands.PORT_MESSAGE)) {
				String[] parts = line.split(":");
				port = Integer.parseInt(parts[1].trim());
				break;
			}
		}
		assertNotEquals(0, port);
		Registry reg = LocateRegistry.getRegistry("127.0.0.1", port);
		long stopTime = System.currentTimeMillis() + 1000;
		do {
			try {
				remote = (ClipboardCommands) reg.lookup(ClipboardCommands.ID);
				break;
			} catch (NotBoundException e) {
				// try again because the remote app probably hasn't bound yet
			}
		} while (System.currentTimeMillis() < stopTime);

		// Run a no-op on the Swing event loop so that we know it is idle
		// and we can continue startup
		remote.waitUntilReady();
	}

	private void stopRemoteClipboardCommands() throws Exception {
		try {
			if (remote != null) {
				remote.stop();
				remote = null;
			}
		} finally {
			if (remoteClipboardProcess != null) {
				try {
					remoteClipboardProcess.waitFor(1, TimeUnit.SECONDS);
				} finally {
					remoteClipboardProcess.destroyForcibly();
					remoteClipboardProcess = null;
				}
			}
		}
	}

	/**
	 * Make sure to always copy/paste unique strings - this ensures that tests run
	 * under {@link RepeatedTest}s don't false pass because of clipboard value on
	 * previous iteration.
	 */
	private String getUniqueTestString() {
		return "Hello World " + uniqueId++;
	}

	/**
	 * Test that the remote application clipboard works
	 */
	@Test
	public void test_Remote() throws Exception {
		System.err.println("Start test_Remote");
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld);
		assertEquals(helloWorld, remote.getStringContents());
	}

	/**
	 * This tests set + get on local clipboard. Remote clipboard can have different
	 * behaviours and has additional tests.
	 */
	@RepeatedTest(value = REPEAT_COUNT)
	public void test_LocalClipboard() throws Exception {
		System.err.println("Start test_LocalClipboard");
		openAndFocusShell();

		String helloWorld = getUniqueTestString();
		clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer });
		assertEquals(helloWorld, clipboard.getContents(textTransfer));
		assertNull(clipboard.getContents(rtfTransfer));

		helloWorld = getUniqueTestString();
		String helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorld, helloWorldRtf }, new Transfer[] { textTransfer, rtfTransfer });
		assertEquals(helloWorld, clipboard.getContents(textTransfer));
		assertEquals(helloWorldRtf, clipboard.getContents(rtfTransfer));

		helloWorld = getUniqueTestString();
		helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorldRtf }, new Transfer[] { rtfTransfer });
		assertNull(clipboard.getContents(textTransfer));
		assertEquals(helloWorldRtf, clipboard.getContents(rtfTransfer));
	}

	@RepeatedTest(value = REPEAT_COUNT)
	public void test_setContents() throws Exception {
		try {
			System.err.println("Start test_setContents");
			openAndFocusShell();
			String helloWorld = getUniqueTestString();

			clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer });
			sleep();

			openAndFocusRemote();
			SwtTestUtil.processEvents(1000, () -> helloWorld.equals(runOperationInThread(remote::getStringContents)));
			String result = runOperationInThread(remote::getStringContents);
			assertEquals(helloWorld, result);
		} catch (Exception | AssertionError e) {
			if (SwtTestUtil.isGTK4 && !SwtTestUtil.isX11) {
				// TODO make the code + test stable
				throw new RuntimeException(
						"This test is really unstable on wayland backend, at least with Ubuntu 25.04", e);
			}
			throw e;
		}
	}

	@RepeatedTest(value = REPEAT_COUNT)
	public void test_getContents() throws Exception {
		System.err.println("Start test_getContents");
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld);

		openAndFocusShell();
		SwtTestUtil.processEvents(1000, () -> {
			return helloWorld.equals(clipboard.getContents(textTransfer));
		});
		assertEquals(helloWorld, clipboard.getContents(textTransfer));
	}

	@FunctionalInterface
	public interface ExceptionalSupplier<T> {
		T get() throws Exception;
	}

	/**
	 * When running some operations, such as requesting remote process read the
	 * clipboard, we need to have the event queue processing otherwise the remote
	 * won't be able to read our clipboard contribution.
	 *
	 * This method starts the supplier in a new thread and runs the event loop until
	 * the thread completes.
	 *
	 * @throws InterruptedException
	 */
	private <T> T runOperationInThread(ExceptionalSupplier<T> supplier) throws RuntimeException {
		Object[] supplierValue = new Object[1];
		Exception[] supplierException = new Exception[1];
		Runnable task = () -> {
			try {
				supplierValue[0] = supplier.get();
			} catch (Exception e) {
				supplierValue[0] = null;
				supplierException[0] = e;
			}
		};
		Thread thread = new Thread(task, this.getClass().getName() + ".runOperationInThread");
		thread.setDaemon(true);
		thread.start();
		BooleanSupplier done = () -> !thread.isAlive();
		try {
			SwtTestUtil.processEvents(2000, done);
		} catch (InterruptedException e) {
			throw new RuntimeException("Failed while running thread", e);
		}
		assertTrue(done.getAsBoolean());
		if (supplierException[0] != null) {
			throw new RuntimeException("Failed while running thread", supplierException[0]);
		}
		@SuppressWarnings("unchecked")
		T result = (T) supplierValue[0];
		return result;
	}
}

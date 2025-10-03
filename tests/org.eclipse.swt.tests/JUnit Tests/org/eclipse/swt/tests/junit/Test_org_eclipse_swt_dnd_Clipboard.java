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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

import clipboard.ClipboardCommands;

/**
 * Automated Test Suite for class org.eclipse.swt.dnd.Clipboard
 *
 * @see org.eclipse.swt.dnd.Clipboard
 * @see Test_org_eclipse_swt_custom_StyledText StyledText tests as it also does
 *      some clipboard tests
 */
public class Test_org_eclipse_swt_dnd_Clipboard {

	private static final int DEFAULT_TIMEOUT_MS = 10000;
	static int uniqueId = 1;
	private Display display;
	private Shell shell;
	private Clipboard clipboard;
	private TextTransfer textTransfer;
	private RTFTransfer rtfTransfer;
	private ClipboardCommands remote;
	private Process remoteClipboardProcess;
	private Path remoteClipboardTempDir;

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

	private void sleep() throws InterruptedException {
		if (SwtTestUtil.isGTK4()) {
			/**
			 * TODO remove all uses of sleep and change them to processEvents with the
			 * suitable conditional, or entirely remove them
			 */
			SwtTestUtil.processEvents(100, null);
		} else {
			SwtTestUtil.processEvents();
		}
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
		/*
		 * The below copy using getPath may be redundant (i.e. it may be possible to run
		 * the class files from where they currently reside in the bin folder or the
		 * jar), but this method of setting up the class files is very simple and is
		 * done the same way that other files are extracted for tests.
		 *
		 * If the ClipboardTest starts to get more complicated, or other tests want to
		 * replicate this design element, then refactoring this is an option.
		 */
		remoteClipboardTempDir = Files.createTempDirectory("swt-test-Clipboard");
		List.of( //
				"ClipboardTest", //
				"ClipboardCommands", //
				"ClipboardCommandsImpl", //
				"ClipboardTest$LocalHostOnlySocketFactory" //
		).forEach((f) -> {
			// extract the files and put them in the temp directory
			SwtTestUtil.copyFile("/clipboard/" + f + ".class",
					remoteClipboardTempDir.resolve("clipboard/" + f + ".class"));
		});

		String javaHome = System.getProperty("java.home");
		String javaExe = javaHome + "/bin/java" + (SwtTestUtil.isWindowsOS ? ".exe" : "");
		assertTrue(Files.exists(Path.of(javaExe)));

		ProcessBuilder pb = new ProcessBuilder(javaExe, "clipboard.ClipboardTest")
				.directory(remoteClipboardTempDir.toFile());
		pb.inheritIO();
		pb.redirectOutput(Redirect.PIPE);
		remoteClipboardProcess = pb.start();

		// Read server output to find the port
		int port = runOperationInThread(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(remoteClipboardProcess.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(ClipboardCommands.PORT_MESSAGE)) {
					String[] parts = line.split(":");
					return Integer.parseInt(parts[1].trim());
				}
			}
			throw new RuntimeException("Failed to get port");
		});
		assertNotEquals(0, port);
		try {
			Registry reg = LocateRegistry.getRegistry("127.0.0.1", port);
			long stopTime = System.currentTimeMillis() + DEFAULT_TIMEOUT_MS;
			do {
				try {
					remote = (ClipboardCommands) reg.lookup(ClipboardCommands.ID);
					break;
				} catch (NotBoundException e) {
					// try again because the remote app probably hasn't bound yet
				}
			} while (System.currentTimeMillis() < stopTime);
		} catch (RemoteException e) {

			Integer exitValue = null;
			boolean waitFor = false;
			try {
				waitFor = remoteClipboardProcess.waitFor(5, TimeUnit.SECONDS);
				if (waitFor) {
					exitValue = remoteClipboardProcess.exitValue();
				}
			} catch (InterruptedException e1) {
				Thread.interrupted();
			}

			String message = "Failed to get remote clipboards command, this seems to happen on macOS on I-build tests. Exception: "
					+ e.toString() + " waitFor: " + waitFor + " exitValue: " + exitValue;

			// Give some diagnostic information to help track down why this fails on build
			// machine. We only hard error on Linux, for other platforms we allow test to
			// just be skipped until we track down what is causing
			// https://github.com/eclipse-platform/eclipse.platform.swt/issues/2553
			assumeTrue(SwtTestUtil.isGTK, message);
			throw new RuntimeException(message, e);
		}
		assertNotNull(remote);

		// Run a no-op on the Swing event loop so that we know it is idle
		// and we can continue startup
		remote.waitUntilReady();
	}

	private void stopRemoteClipboardCommands() throws Exception {
		try {
			stopRemoteProcess();
		} finally {
			deleteRemoteTempDir();
		}
	}

	private void deleteRemoteTempDir() {
		if (remoteClipboardTempDir != null) {
			// At this point the process is ideally destroyed - or at least the test will
			// report a failure if it isn't. Clean up the extracted files, but don't
			// fail test if we fail to delete
			try {
				Files.walkFileTree(remoteClipboardTempDir, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				System.err.println("SWT Warning: Failed to clean up temp directory " + remoteClipboardTempDir
						+ " Error:" + e.toString());
				e.printStackTrace();
			}
		}
	}

	private void stopRemoteProcess() throws RemoteException, InterruptedException {
		try {
			if (remote != null) {
				remote.stop();
				remote = null;
			}
		} finally {
			if (remoteClipboardProcess != null) {
				try {
					remoteClipboardProcess.destroy();
					assertTrue(remoteClipboardProcess.waitFor(10, TimeUnit.SECONDS));
				} finally {
					remoteClipboardProcess.destroyForcibly();
					assertTrue(remoteClipboardProcess.waitFor(10, TimeUnit.SECONDS));
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
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld);
		assertEquals(helloWorld, remote.getStringContents());
	}

	/**
	 * This tests set + get on local clipboard. Remote clipboard can have different
	 * behaviours and has additional tests.
	 */
	@Test
	public void test_LocalClipboard() throws Exception {
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
		if (SwtTestUtil.isCocoa) {
			/*
			 * macOS's pasteboard has some extra functionality that even if you don't
			 * provide a plain text version, the pasteboard will convert the rtf to plain
			 * text. This isn't in SWT's API contract so if this test fails in the future it
			 * can be removed.
			 *
			 * From the apple <a href=
			 * "https://developer.apple.com/documentation/appkit/supporting-writing-tools-via-the-pasteboard"
			 * >docs</a>
			 *
			 * For example, if you provided a requestor object for the NSPasteboardTypeRTF
			 * type, write data to the pasteboard in the RTF format. You don’t need to write
			 * multiple data formats to the pasteboard to ensure interoperability with other
			 * apps.
			 */
			assertEquals(helloWorld, clipboard.getContents(textTransfer));
		} else {
			assertNull(clipboard.getContents(textTransfer));
		}
		assertEquals(helloWorldRtf, clipboard.getContents(rtfTransfer));
	}

	@Test
	public void test_setContents() throws Exception {
		try {
			openAndFocusShell();
			String helloWorld = getUniqueTestString();

			clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer });
			sleep();

			openAndFocusRemote();
			SwtTestUtil.processEvents(1000, () -> helloWorld.equals(runOperationInThread(remote::getStringContents)));
			String result = runOperationInThread(remote::getStringContents);
			assertEquals(helloWorld, result);
		} catch (Exception | AssertionError e) {
			if (SwtTestUtil.isGTK4() && !SwtTestUtil.isX11()) {
				// TODO make the code + test stable
				throw new RuntimeException(
						"This test is really unstable on wayland backend, at least with Ubuntu 25.04", e);
			}
			throw e;
		}
	}

	@Test
	public void test_getContents() throws Exception {
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
	 * the thread completes, or until a timeout is reached.
	 */
	private <T> T runOperationInThread(ExceptionalSupplier<T> supplier) throws RuntimeException {
		return runOperationInThread(DEFAULT_TIMEOUT_MS, supplier);
	}

	/**
	 * When running some operations, such as requesting remote process read the
	 * clipboard, we need to have the event queue processing otherwise the remote
	 * won't be able to read our clipboard contribution.
	 *
	 * This method starts the supplier in a new thread and runs the event loop until
	 * the thread completes, or until a timeout is reached.
	 */
	private <T> T runOperationInThread(int timeoutMs, ExceptionalSupplier<T> supplier) throws RuntimeException {
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
			SwtTestUtil.processEvents(timeoutMs, done);
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

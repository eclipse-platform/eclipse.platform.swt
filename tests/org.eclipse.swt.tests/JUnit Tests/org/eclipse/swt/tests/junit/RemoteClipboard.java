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

import clipboard.ClipboardCommands;

public class RemoteClipboard implements ClipboardCommands {
	/**
	 * Set to true to manually launch the ClipboardTest. This is useful so you can
	 * debug the Swing side.
	 */
	private static boolean DEBUG_REMOTE = false;
	private ClipboardCommands remote;
	private Process remoteClipboardProcess;
	private Path remoteClipboardTempDir;

	public void start() throws Exception {
		assertNull(remote, "Create a new instance to restart");

		int port = DEBUG_REMOTE ? ClipboardCommands.DEFAULT_PORT : launchRemote();
		try {
			Registry reg = LocateRegistry.getRegistry("127.0.0.1", port);
			long stopTime = System.currentTimeMillis() + 10000;
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
		remote.setFocus();
		remote.waitUntilReady();
	}

	private int launchRemote() throws IOException {
		if (SwtTestUtil.isLinux) {
			String display = System.getenv("DISPLAY");
			assumeTrue(display != null && !display.isBlank(), """
					The remote clipboard test requires X11 because Java Swing
					does not have Wayland backend until Project Wakefield is
					implemented, therefore these clipboard related tests are
					skipped.
					""");
		}

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
				"ClipboardTest$LocalHostOnlySocketFactory", //
				"FileListSelection", //
				"HtmlSelection", //
				"ImageSelection", //
				"MyTypeSelection", //
				"RtfSelection", //
				"UrlSelection" //
		).forEach((f) -> {
			// extract the files and put them in the temp directory
			SwtTestUtil.copyFile("/clipboard/" + f + ".class",
					remoteClipboardTempDir.resolve("clipboard/" + f + ".class"));
		});

		String javaHome = System.getProperty("java.home");
		String javaExe = javaHome + "/bin/java" + (SwtTestUtil.isWindowsOS ? ".exe" : "");
		assertTrue(Files.exists(Path.of(javaExe)));

		ProcessBuilder pb = new ProcessBuilder(javaExe, "clipboard.ClipboardTest", "-autoport")
				.directory(remoteClipboardTempDir.toFile());
		pb.inheritIO();
		pb.redirectOutput(Redirect.PIPE);
		remoteClipboardProcess = pb.start();

		// Read server output to find the port
		int port = SwtTestUtil.runOperationInThread(() -> {
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
		return port;
	}

	@Override
	public void stop() throws RemoteException {
		if (DEBUG_REMOTE) {
			return;
		}
		try {
			stopProcess();
		} catch (InterruptedException e) {
			Thread.interrupted();
		} finally {
			deleteRemoteTempDir();
		}
	}

	private void stopProcess() throws RemoteException, InterruptedException {
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

	@Override
	public void setContents(String string) throws RemoteException {
		setContents(string, CLIPBOARD);
	}

	@Override
	public void setContents(String string, int clipboardId) throws RemoteException {
		remote.setContents(string, clipboardId);
	}

	@Override
	public void setFocus() throws RemoteException {
		remote.setFocus();
	}

	@Override
	public String getStringContents() throws RemoteException {
		return getStringContents(CLIPBOARD);
	}

	@Override
	public String getStringContents(int clipboardId) throws RemoteException {
		return remote.getStringContents(clipboardId);
	}

	@Override
	public void waitUntilReady() throws RemoteException {
		remote.waitUntilReady();
	}

	@Override
	public void waitForButtonPress() throws RemoteException {
		remote.waitForButtonPress();
	}

	@Override
	public void setRtfContents(String test) throws RemoteException {
		remote.setRtfContents(test);
	}

	@Override
	public String getRtfContents() throws RemoteException {
		return remote.getRtfContents();
	}

	@Override
	public void setHtmlContents(String test) throws RemoteException {
		remote.setHtmlContents(test);
	}

	@Override
	public String getHtmlContents() throws RemoteException {
		return remote.getHtmlContents();
	}

	@Override
	public void setUrlContents(byte[] test) throws RemoteException {
		remote.setUrlContents(test);
	}

	@Override
	public byte[] getUrlContents() throws RemoteException {
		return remote.getUrlContents();
	}

	@Override
	public void setImageContents(byte[] imageContents) throws RemoteException {
		remote.setImageContents(imageContents);
	}

	@Override
	public byte[] getImageContents() throws RemoteException {
		return remote.getImageContents();
	}

	@Override
	public void setFileListContents(String[] fileList) throws RemoteException {
		remote.setFileListContents(fileList);
	}

	@Override
	public String[] getFileListContents() throws RemoteException {
		return remote.getFileListContents();
	}

	@Override
	public void setMyTypeContents(byte[] bytes) throws RemoteException {
		remote.setMyTypeContents(bytes);
	}

	@Override
	public byte[] getMyTypeContents() throws RemoteException {
		return remote.getMyTypeContents();
	}

}

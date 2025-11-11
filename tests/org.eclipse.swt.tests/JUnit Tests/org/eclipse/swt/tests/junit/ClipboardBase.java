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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import clipboard.ClipboardTest;

/**
 * Base class for tests that test clipboard and transfer types
 */
public class ClipboardBase {

	/**
	 * See {@link #openAndFocusShell(boolean)} - some tests require user to actually
	 * interact with the shell.
	 *
	 * Default to skipping tests requiring "real" activation on GHA and Jenkins.
	 *
	 * <code>true</code>: skip tests <code>false</code>: don't skip tests
	 * <code>null</code>: unknown whether to skip tests yet
	 */
	private static Boolean skipTestsRequiringButtonPress = (SwtTestUtil.isRunningOnGitHubActions()
			|| SwtTestUtil.isRunningOnJenkins()) ? true : null;
	private static int uniqueId = 1;
	protected Display display;
	protected Shell shell;
	protected Clipboard clipboard;
	protected RemoteClipboard remote;

	/**
	 * Return the set of clipboards that are supported on this platform, this method
	 * is referenced in the {@link MethodSource} annotations on the
	 * {@link ParameterizedTest}s within this class.
	 */
	public static List<Integer> supportedClipboardIds() {
		if (SwtTestUtil.isGTK) {
			return List.of(DND.CLIPBOARD, DND.SELECTION_CLIPBOARD);
		}
		return List.of(DND.CLIPBOARD);
	}

	@BeforeEach
	public void setUp() {
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		clipboard = new Clipboard(display);
	}

	/**
	 * Note: Wayland backend does not allow access to system clipboard from
	 * non-focussed windows. So we have to create/open and focus a window here so
	 * that clipboard operations work.
	 *
	 * Additionally, if we want to provide data to the clipboard, we require user
	 * interaction on the created shell. Therefore if forSetContents is true the
	 * tester needs to press a button for test to work.
	 *
	 * If there is no user interaction (button not pressed) then the test is
	 * skipped, rather than failed, and subsequent tests requiring user interaction
	 * as skipped too. See {@link #skipTestsRequiringButtonPress}
	 */
	protected void openAndFocusShell(boolean forSetContents) throws InterruptedException {
		assertNull(shell);
		shell = new Shell(display);

		boolean requireUserPress = forSetContents && SwtTestUtil.isWayland();
		if (requireUserPress) {
			assumeFalse(skipTestsRequiringButtonPress != null && skipTestsRequiringButtonPress,
					"Skipping tests that require user input");

			AtomicBoolean pressed = new AtomicBoolean(false);
			shell.setLayout(new RowLayout(SWT.VERTICAL));
			Button button = new Button(shell, SWT.PUSH);
			button.setText("Press me!");
			button.addListener(SWT.Selection, (e) -> pressed.set(true));
			button.setSize(200, 50);
			Label label = new Label(shell, SWT.NONE);
			label.setText("""
					Press the button to tell Wayland that you really want this window to have access to clipboard.
					This is needed on Wayland because only really focussed programs are allowed to write to the
					global keyboard.

					If you don't press this button soon, the test will be skipped and you won't be asked again.
					""");
			Label timeleft = new Label(shell, SWT.NONE);
			timeleft.setText("Time left to press button: XXXXXXXXXXXXXXXXXXXX seconds");

			SwtTestUtil.openShell(shell);

			// If we know there is a tester pressing the buttons, allow them
			// a little grace on the timeout. If we don't know if there is a
			// tester around, skip tests fairly quickly and don't
			// ask again.
			int timeout = skipTestsRequiringButtonPress == null ? 1500 : 10000;
			long startTime = System.nanoTime();
			SwtTestUtil.processEvents(timeout, () -> {
				long nowTime = System.nanoTime();
				long timeLeft = nowTime - startTime;
				long timeLeftMs = timeout - (timeLeft / 1_000_000);
				double timeLeftS = timeLeftMs / 1_000.0d;
				timeleft.setText("Time left to press button: " + timeLeftS + " seconds");
				return pressed.get();
			});
			boolean userPressedButton = pressed.get();
			if (userPressedButton) {
				skipTestsRequiringButtonPress = false;
			} else {
				skipTestsRequiringButtonPress = true;
				assumeTrue(false, "Skipping tests that require user input");
			}
		} else {
			SwtTestUtil.openShell(shell);
		}

	}

	/**
	 * Note: Wayland backend does not allow access to system clipboard from
	 * non-focussed windows. So we have to open and focus remote here so that
	 * clipboard operations work.
	 */
	protected void openAndFocusRemote() throws Exception {
		assertNull(remote);
		remote = new RemoteClipboard();
		remote.start();

		/*
		 * If/when OpenJDK Project Wakefield gets merged then we may need to wait for
		 * button pressed on the swing app just like the SWT app. This may also be
		 * needed if Wayland implementations get more restrictive on X apps too.
		 */
		// remote.waitForButtonPress();
	}

	@AfterEach
	public void tearDown() throws Exception {
		try {
			if (remote != null) {
				remote.stop();
			}
		} finally {
			if (clipboard != null) {
				supportedClipboardIds().forEach(clipboard::clearContents);
				clipboard.dispose();
			}
			if (shell != null) {
				shell.dispose();
			}
			SwtTestUtil.processEvents();
		}
	}

	protected String addTrailingNulCharacter(String result) {
		return result + '\0';
	}

	/**
	 * Trim trailing nul character - some transfer types require a trailing nul when
	 * copied to the clipboard, so use this method to remove it for tests that
	 * obtain bytes from the {@link ClipboardTest} app.
	 *
	 * @param result to trim terminating nul character from
	 * @return string with the nul character trimmed, or null if result was null.
	 */
	protected String trimTrailingNulCharacter(String result) {
		if (result == null) {
			return null;
		}
		if (result.charAt(result.length() - 1) == '\0') {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * Make sure to always copy/paste unique strings - this ensures that tests run
	 * under {@link RepeatedTest}s don't false pass because of clipboard value on
	 * previous iteration.
	 */
	protected static String getUniqueTestString() {
		return "Hello World " + uniqueId++;
	}
}
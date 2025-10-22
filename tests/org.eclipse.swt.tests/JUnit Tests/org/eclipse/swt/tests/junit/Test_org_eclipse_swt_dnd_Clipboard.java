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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Automated Test Suite for class org.eclipse.swt.dnd.Clipboard
 *
 * @see org.eclipse.swt.dnd.Clipboard
 * @see Test_org_eclipse_swt_custom_StyledText StyledText tests as it also does
 *      some clipboard tests
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_Clipboard {

	/**
	 * See {@link #openAndFocusShell(boolean)} - some tests require user to actually
	 * interact with the shell.
	 *
	 * Default to skipping tests requiring "real" activation on GHA and Jenkins.
	 *
	 * <code>true</code>: skip tests <code>false</code>: don't skip tests
	 * <code>null</code>: unknown whether to skip tests yet
	 */
	private static Boolean skipTestsRequiringButtonPress = (Boolean.parseBoolean(System.getenv("GITHUB_ACTIONS"))
			|| System.getenv("JOB_NAME") != null) ? true : null;
	private static int uniqueId = 1;
	private Display display;
	private Shell shell;
	private Clipboard clipboard;
	private TextTransfer textTransfer;
	private RTFTransfer rtfTransfer;
	private RemoteClipboard remote;

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
	private void openAndFocusShell(boolean forSetContents) throws InterruptedException {
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
	private void openAndFocusRemote() throws Exception {
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
				clipboard.dispose();
			}
			if (shell != null) {
				shell.dispose();
			}
			SwtTestUtil.processEvents();
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

	/**
	 * Test that the remote application clipboard works
	 */
	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_Remote(int clipboardId) throws Exception {
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld, clipboardId);
		assertEquals(helloWorld, remote.getStringContents(clipboardId));
	}

	/**
	 * This tests set + get on local clipboard. Remote clipboard can have different
	 * behaviours and has additional tests.
	 */
	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_LocalClipboard(int clipboardId) throws InterruptedException {
		openAndFocusShell(false);

		String helloWorld = getUniqueTestString();
		clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer }, clipboardId);
		assertEquals(helloWorld, clipboard.getContents(textTransfer, clipboardId));
		assertNull(clipboard.getContents(rtfTransfer, clipboardId));

		helloWorld = getUniqueTestString();
		String helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorld, helloWorldRtf }, new Transfer[] { textTransfer, rtfTransfer },
				clipboardId);
		assertEquals(helloWorld, clipboard.getContents(textTransfer, clipboardId));
		assertEquals(helloWorldRtf, clipboard.getContents(rtfTransfer, clipboardId));

		helloWorld = getUniqueTestString();
		helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorldRtf }, new Transfer[] { rtfTransfer }, clipboardId);
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
			assertEquals(helloWorld, clipboard.getContents(textTransfer, clipboardId));
		} else {
			assertNull(clipboard.getContents(textTransfer, clipboardId));
		}
		assertEquals(helloWorldRtf, clipboard.getContents(rtfTransfer, clipboardId));
	}

	@Order(2)
	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_setContents(int clipboardId) throws Exception {
		openAndFocusShell(true);
		String helloWorld = getUniqueTestString();

		clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer }, clipboardId);

		openAndFocusRemote();
		String result = SwtTestUtil.runOperationInThread(() -> remote.getStringContents(clipboardId));
		assertEquals(helloWorld, result);
	}

	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_getContents(int clipboardId) throws Exception {
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld, clipboardId);

		openAndFocusShell(false);
		assertEquals(helloWorld, clipboard.getContents(textTransfer, clipboardId));
	}

	@Test
	public void test_getContentsBothClipboards() throws Exception {
		assumeTrue(SwtTestUtil.isGTK);

		openAndFocusRemote();
		String helloWorldClipboard = getUniqueTestString();
		remote.setContents(helloWorldClipboard, DND.CLIPBOARD);
		String helloWorldSelection = getUniqueTestString();
		remote.setContents(helloWorldSelection, DND.SELECTION_CLIPBOARD);

		openAndFocusShell(false);
		assertEquals(helloWorldClipboard, clipboard.getContents(textTransfer, DND.CLIPBOARD));
		assertEquals(helloWorldSelection, clipboard.getContents(textTransfer, DND.SELECTION_CLIPBOARD));
	}

	@Order(1)
	@Test
	public void test_setContentsBothClipboards() throws Exception {
		assumeTrue(SwtTestUtil.isGTK);

		openAndFocusShell(true);
		String helloWorldClipboard = getUniqueTestString();
		clipboard.setContents(new Object[] { helloWorldClipboard }, new Transfer[] { textTransfer }, DND.CLIPBOARD);
		String helloWorldSelection = getUniqueTestString();
		clipboard.setContents(new Object[] { helloWorldSelection }, new Transfer[] { textTransfer },
				DND.SELECTION_CLIPBOARD);

		openAndFocusRemote();
		assertEquals(helloWorldClipboard,
				SwtTestUtil.runOperationInThread(() -> remote.getStringContents(DND.CLIPBOARD)));
		assertEquals(helloWorldSelection,
				SwtTestUtil.runOperationInThread(() -> remote.getStringContents(DND.SELECTION_CLIPBOARD)));
	}

	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_getContentsAsync(int clipboardId) throws Exception {
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld, clipboardId);

		openAndFocusShell(false);

		// Multiple ways of using the API
		// 1: Spin the event loop manually waiting for future to complete
		CompletableFuture<Object> future = clipboard.getContentsAsync(textTransfer, clipboardId);
		SwtTestUtil.processEvents(1000, () -> {
			return future.isDone();
		});
		assertEquals(helloWorld, future.get());

		// 2: Use CompletableFuture's features to chain
		Object[] result = new Object[] { null };
		CompletableFuture<Object> chained = clipboard.getContentsAsync(textTransfer, clipboardId);
		chained.thenAccept(object -> result[0] = object);
		// Within the test we need to process the event loop so the async can complete,
		// but in applications the method that calls getContentsAsync can return to the
		// main event loop iterations.
		SwtTestUtil.processEvents(1000, () -> {
			return result[0] != null;
		});
		assertEquals(helloWorld, result[0]);
	}

	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_getAvailableTypeNames(int clipboardId) throws Exception {
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld, clipboardId);

		openAndFocusShell(false);
		String[] availableTypeNames = clipboard.getAvailableTypeNames();
		// The actual contents of type names is platform specific, so we just
		// verify that we get something.
		assertNotNull(availableTypeNames);
		assertTrue(availableTypeNames.length > 0);
	}

	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_getAvailableTypes(int clipboardId) throws Exception {
		openAndFocusRemote();
		String helloWorld = getUniqueTestString();
		remote.setContents(helloWorld, clipboardId);

		openAndFocusShell(false);
		TransferData[] availableTypes = clipboard.getAvailableTypes(clipboardId);
		assertTrue(Arrays.stream(availableTypes).anyMatch(textTransfer::isSupportedType));

		helloWorld = getUniqueTestString();
		String helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorld, helloWorldRtf }, new Transfer[] { textTransfer, rtfTransfer },
				clipboardId);
		availableTypes = clipboard.getAvailableTypes(clipboardId);
		assertTrue(Arrays.stream(availableTypes).anyMatch(textTransfer::isSupportedType));
		assertTrue(Arrays.stream(availableTypes).anyMatch(rtfTransfer::isSupportedType));

		helloWorld = getUniqueTestString();
		helloWorldRtf = "{\\rtf1\\b\\i " + helloWorld + "}";
		clipboard.setContents(new Object[] { helloWorldRtf }, new Transfer[] { rtfTransfer }, clipboardId);
		availableTypes = clipboard.getAvailableTypes(clipboardId);
		assertTrue(Arrays.stream(availableTypes).anyMatch(rtfTransfer::isSupportedType));
	}
}

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
public class Test_org_eclipse_swt_dnd_Clipboard {

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
	 */
	private void openAndFocusShell() {
		shell = new Shell(display);
		SwtTestUtil.openShell(shell);
	}

	/**
	 * Note: Wayland backend does not allow access to system clipboard from
	 * non-focussed windows. So we have to open and focus remote here so that
	 * clipboard operations work.
	 */
	public void openAndFocusRemote() throws Exception {
		assertNull(remote);
		remote = new RemoteClipboard();
		remote.start();
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
	public void test_LocalClipboard(int clipboardId) {
		openAndFocusShell();

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

	@ParameterizedTest
	@MethodSource("supportedClipboardIds")
	public void test_setContents(int clipboardId) throws Exception {
		openAndFocusShell();
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

		openAndFocusShell();
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

		openAndFocusShell();
		assertEquals(helloWorldClipboard, clipboard.getContents(textTransfer, DND.CLIPBOARD));
		assertEquals(helloWorldSelection, clipboard.getContents(textTransfer, DND.SELECTION_CLIPBOARD));
	}

	@Test
	public void test_setContentsBothClipboards() throws Exception {
		assumeTrue(SwtTestUtil.isGTK);

		openAndFocusShell();
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

		openAndFocusShell();

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
}

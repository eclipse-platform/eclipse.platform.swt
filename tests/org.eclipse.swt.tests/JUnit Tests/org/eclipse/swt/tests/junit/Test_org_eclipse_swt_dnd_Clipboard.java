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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
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
public class Test_org_eclipse_swt_dnd_Clipboard extends ClipboardBase {

	private TextTransfer textTransfer;
	private RTFTransfer rtfTransfer;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		textTransfer = TextTransfer.getInstance();
		rtfTransfer = RTFTransfer.getInstance();
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

	/**
	 * tear down and start again without clearing off the clipboard
	 */
	private void tearDownAndStartAgain() {
		// we can't call tearDown because we don't want to clear the clipboard
		clipboard.dispose();
		clipboard = null;
		shell.dispose();
		shell = null;
		SwtTestUtil.processEvents();

		// tearDown doesn't dispose on each test, but the error we are checking
		// for happens when Display is disposed (causing ClipboardProxy to be disposed)
		display.dispose();
		display = null;

		// start back up
		setUp();
	}

	private void checkStderrForGTK3Warning(String stderr) {
		if (SwtTestUtil.isGTK3()) {
			if ("***WARNING: Attempt to access SWT clipboard after disposing SWT Display.\n".equals(stderr)) {
				// test passed (VM didn't SIGSEGV) and we exercise the problematic code causing
				// https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
			} else if (stderr.isEmpty()) {
				assumeTrue(false, """
						test passed (VM didn't SIGSEGV) but we didn't exercise the problematic code causing
						https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
						""");
			} else {
				// test passed (VM didn't SIGSEGV) but we don't know why there is other stderr
				// output, log it to the test results for further investigation.
				assertEquals("", stderr);
			}
		} else {
			assertEquals("", stderr);
		}
	}

	private Text setContentsOnClipboardAndDisposeDisplay() throws InterruptedException {
		openAndFocusShell(false);

		String helloWorld = getUniqueTestString();
		clipboard.setContents(new Object[] { helloWorld }, new Transfer[] { textTransfer });
		assertEquals(helloWorld, clipboard.getContents(textTransfer));

		tearDownAndStartAgain();
		shell = new Shell(display);
		Text text = new Text(shell, SWT.SINGLE);
		String textBoxHello = getUniqueTestString();
		text.setText(textBoxHello);
		text.setSelection(0, textBoxHello.length());

		return text;
	}

	/**
	 * Regression test for
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
	 */
	@Test
	public void test_AfterNewDisplay_TextCopy() throws Exception {
		Text text = setContentsOnClipboardAndDisposeDisplay();
		SwtTestUtil.openShell(shell);

		/*
		 * on GTK3 this Text.copy would trigger SIGSEGV on GTK calling disposed callback
		 * ClipboardProxy.clearFunc
		 */
		String stderr = SwtTestUtil.runWithCapturedStderr(() -> text.copy());
		checkStderrForGTK3Warning(stderr);

		// After the test, clear out the partially disposed clipboard by taking
		// ownership and then clearing it
		clipboard.setContents(new Object[] { getUniqueTestString() }, new Transfer[] { textTransfer });
		clipboard.clearContents();
	}

	/**
	 * Regression test for
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
	 */
	@Test
	public void test_AfterNewDisplay_LocalSetContents() throws Exception {
		setContentsOnClipboardAndDisposeDisplay();
		SwtTestUtil.openShell(shell);

		/*
		 * on GTK3 this setContents would lead to undefined behavior because when the
		 * ClipboardProxy called bind on the new ClipboardProxy.clearFunc there was a
		 * pretty good chance it would bind to the same entry in the callback table. But
		 * depending on exact order of operations that table entry could be empty,
		 * leading to SIGSEGV.
		 */
		String stderr = SwtTestUtil.runWithCapturedStderr(
				() -> clipboard.setContents(new Object[] { getUniqueTestString() }, new Transfer[] { textTransfer }));
		checkStderrForGTK3Warning(stderr);
	}

	/**
	 * Regression test for
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
	 */
	@Test
	public void test_AfterNewDisplay_RemoteGetContents() throws Exception {
		setContentsOnClipboardAndDisposeDisplay();
		openAndFocusRemote();
		/*
		 * on GTK3 this getting from the remote would trigger SIGSEGV on GTK calling
		 * disposed callback ClipboardProxy.getFunc
		 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2675
		 *
		 * Caveat: What is available on the clipboard after Display dispose is very
		 * platform and configuration dependent and no guarantees in the SWT API exist.
		 * Therefore the return value of remote.getStringContents() is not checked here.
		 */
		String stderr = SwtTestUtil
				.runWithCapturedStderr(() -> SwtTestUtil.runOperationInThread(() -> remote.getStringContents()));
		checkStderrForGTK3Warning(stderr);

		/*
		 * On GTK3 At this point the old ClipboardProxy has not been disposed because
		 * this process still owns the clipboard from the original setContents done at
		 * the beginning of the test. Therefore set some new contents so that the old
		 * contents can be cleared.
		 */
		SwtTestUtil.openShell(shell);
		stderr = SwtTestUtil.runWithCapturedStderr(
				() -> clipboard.setContents(new Object[] { getUniqueTestString() }, new Transfer[] { textTransfer }));
		checkStderrForGTK3Warning(stderr);
	}
}

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Automated Test Suite for {@link RTFTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_RTFTransfer extends ClipboardBase {
	private RTFTransfer rtfTransfer;

	@BeforeEach
	public void localSetup() {
		rtfTransfer = RTFTransfer.getInstance();
	}

	private String toRtf(String s) {
		return "{\\rtf1\\b\\i " + s + "}";
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, rtfTransfer);
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o, Transfer transfer) {
		clipboard.setContents(new Object[] { o }, new Transfer[] { transfer });
	}

	/**
	 * Convenience method to get the contents with a short timeout
	 */
	private String getContents() throws Exception {
		CompletableFuture<Object> future = clipboard.getContentsAsync(rtfTransfer);
		SwtTestUtil.processEvents(1000, () -> {
			return future.isDone();
		});
		Object o = future.get();
		assertInstanceOf(String.class, o);
		return (String) o;
	}

	@Test
	public void test_Validate() throws Exception {
		openAndFocusShell(false);
		String helloWorld = toRtf(getUniqueTestString());
		setContents(helloWorld);
		assertEquals(helloWorld, getContents());
		setContents(" "); // whitespace only - RTF restrictions, at least within SWT are very mild
		assertEquals(" ", getContents());
		assertThrows(IllegalArgumentException.class, () -> setContents(""));
		assertThrows(IllegalArgumentException.class, () -> setContents(new Object()));
	}

	/**
	 * Indirecty tests getTypeIds. The actual values of getTypeIds are not part of
	 * the API, but that they can be applied to isSupportedType is.
	 */
	@Test
	public void test_isSupportedType() throws Exception {
		openAndFocusShell(false);

		// Put a type on the clipboard and ensure we match it
		setContents(toRtf(getUniqueTestString()));
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(rtfTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(new String[] { getUniqueTestString(), //
				getUniqueTestString() }, FileTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(rtfTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		String test = toRtf(getUniqueTestString());

		openAndFocusShell(false);
		setContents(test);
		String contents = getContents();
		assertEquals(test, contents);
		assertNotSame(test, contents);
	}

	@Test
	public void test_nativeToJava() throws Exception {
		String test = toRtf(getUniqueTestString());

		openAndFocusRemote();
		String trailingNulCharacter = test;
		if (SwtTestUtil.isWindows) {
			// Windows require nul trailing character, but
			// other Platforms don't want that
			trailingNulCharacter = addTrailingNulCharacter(test);
		}

		remote.setRtfContents(trailingNulCharacter);
		openAndFocusShell(false);
		assertEquals(test, getContents());
	}

	@Order(1)
	@Test
	public void test_javaToNative() throws Exception {
		String test = toRtf(getUniqueTestString());

		openAndFocusShell(true);
		setContents(test);
		openAndFocusRemote();
		String result = SwtTestUtil.runOperationInThread(() -> remote.getRtfContents());
		result = trimTrailingNulCharacter(result);
		assertEquals(test, result);
	}
}

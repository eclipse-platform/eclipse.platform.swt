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
import java.util.stream.Stream;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Automated Test Suite for {@link HTMLTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_HTMLTransfer extends ClipboardBase {
	private HTMLTransfer htmlTransfer;

	@BeforeEach
	public void localSetup() {
		htmlTransfer = HTMLTransfer.getInstance();
	}

	private String toHtml(String s) {
		return "<b>" + s + "</b>";
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, htmlTransfer);
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
		CompletableFuture<Object> future = clipboard.getContentsAsync(htmlTransfer);
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
		String helloWorld = toHtml(getUniqueTestString());
		setContents(helloWorld);
		assertEquals(helloWorld, getContents());
		setContents(" "); // whitespace only - HTML restrictions, at least within SWT are very mild
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
		setContents(toHtml(getUniqueTestString()));
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(htmlTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(new String[] { getUniqueTestString(), //
				getUniqueTestString() }, FileTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(htmlTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		String test = toHtml(getUniqueTestString());

		openAndFocusShell(false);
		setContents(test);
		String contents = getContents();
		assertEquals(test, contents);
		assertNotSame(test, contents);
	}

	static Stream<Arguments> testData() {
		return Stream.of(//
				Arguments.of("hello_world", "<b>Hello, World!</b>"), //
				/*
				 * single_char and odd_chars are a regression test specifically for a bug that
				 * if there was only 1 byte in the clipboard the return value of getContents was
				 * null instead of the single character and if there was odd > 1 the last byte
				 * would be dropped. See
				 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2666
				 */
				Arguments.of("single_char", " "), //
				Arguments.of("odd_chars", "12345"), //
				Arguments.of("even_chars", "123456") //
		);
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("testData")
	public void test_nativeToJava(String name, String test) throws Exception {
		openAndFocusRemote();
		remote.setHtmlContents(test);
		openAndFocusShell(false);
		assertEquals(test, getContents());
	}

	@Order(1)
	@Test
	public void test_javaToNative() throws Exception {
		String test = toHtml(getUniqueTestString());

		openAndFocusShell(true);
		setContents(test);
		openAndFocusRemote();
		String result = SwtTestUtil.runOperationInThread(() -> remote.getHtmlContents());
		result = trimTrailingNulCharacter(result);
		assertEquals(test, result);
	}
}

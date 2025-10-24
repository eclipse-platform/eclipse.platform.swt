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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.dnd.URLTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Automated Test Suite for {@link URLTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_URLTransfer extends ClipboardBase {
	private URLTransfer urlTransfer;

	@BeforeEach
	public void localSetup() {
		urlTransfer = URLTransfer.getInstance();
	}

	/**
	 * Returns a string matching text/x-moz-url according to
	 * https://developer.mozilla.org/en-US/docs/Web/API/HTML_Drag_and_Drop_API/Drag_data_store
	 */
	private String getURLTransferString() {
		return "https://eclipse.dev/eclipse/swt/" + "\n" + getUniqueTestString();
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, urlTransfer);
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
		CompletableFuture<Object> future = clipboard.getContentsAsync(urlTransfer);
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
		String mozUrl = getURLTransferString();
		setContents(mozUrl);
		assertEquals(mozUrl, getContents());
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
		setContents(getURLTransferString());
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(urlTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(new String[] { getUniqueTestString(), //
				getUniqueTestString() }, FileTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(urlTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		String test = getURLTransferString();

		openAndFocusShell(false);
		setContents(test);
		String contents = getContents();
		assertEquals(test, contents);
	}

	@Test
	public void test_nativeToJava() throws Exception {
		String test = getURLTransferString();

		openAndFocusRemote();
		// This is an attempt to recreate the same bytestream that Firefox provides when
		// doing text/x-moz-url which appears to be this encoding (specifically no BOM)
		byte[] bytes = test.getBytes(StandardCharsets.UTF_16LE);
		remote.setUrlContents(bytes);
		openAndFocusShell(false);
		assertEquals(test, getContents());
	}

	@Order(1)
	@Test
	public void test_javaToNative() throws Exception {
		String test = getURLTransferString();

		openAndFocusShell(true);
		setContents(test);
		openAndFocusRemote();
		byte[] bytes = SwtTestUtil.runOperationInThread(() -> remote.getUrlContents());

		// This is an attempt to recreate the same bytestream that Firefox provides when
		// doing text/x-moz-url which appears to be this encoding (specifically no BOM)
		String result = new String(bytes, StandardCharsets.UTF_16LE);
		// HtmlTransfer.javaToNative on GTK (as of this writing) purposely adds a \0
		// char at the end of the stream. XXX: I don't think this is needed, and
		// in other transfers such as Text and RTF this is not done. This permissiveness
		// also exists in the implementation of HtmlTransfer.nativeToJava which trims
		// the trailing \0 on incoming transfers.
		if (result.charAt(result.length() - 1) == '\0') {
			result = result.substring(0, result.length() - 1);
		}
		assertEquals(test, result);
	}
}

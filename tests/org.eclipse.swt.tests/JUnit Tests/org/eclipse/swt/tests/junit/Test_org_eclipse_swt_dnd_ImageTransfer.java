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

import static org.eclipse.swt.tests.graphics.ImageDataTestHelper.imageDataComparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Automated Test Suite for class {@link ImageTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_ImageTransfer extends ClipboardBase {
	private ImageTransfer imageTransfer;

	@BeforeEach
	public void localSetup() {
		imageTransfer = ImageTransfer.getInstance();
	}

	private InputStream getImageAsInputStream() {
		return SwtTestUtil.class.getResourceAsStream(SwtTestUtil.imageFilenames[0] + "." + SwtTestUtil.imageFormats[0]);
	}

	private ImageData getImageData() throws IOException {
		try (InputStream stream = getImageAsInputStream()) {
			return new ImageData(stream);
		}
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, imageTransfer);
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
	private ImageData getContents() throws Exception {
		CompletableFuture<Object> future = clipboard.getContentsAsync(imageTransfer);
		SwtTestUtil.processEvents(1000, () -> {
			return future.isDone();
		});
		Object o = future.get();
		assertInstanceOf(ImageData.class, o);
		return (ImageData) o;
	}

	@Test
	public void test_Validate() throws Exception {
		openAndFocusShell(false);

		ImageData imageData = getImageData();
		setContents(imageData);
		assertEquals(0, imageDataComparator().compare(imageData, getContents()));

		assertThrows(IllegalArgumentException.class, () -> setContents(""));
		assertThrows(IllegalArgumentException.class, () -> setContents(new Image(display, imageData)));
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
		setContents(getImageData());
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(imageTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(new String[] { getUniqueTestString(), //
				getUniqueTestString() }, FileTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(imageTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		ImageData imageData = getImageData();

		openAndFocusShell(false);
		setContents(imageData);
		ImageData contents = getContents();
		assertEquals(0, imageDataComparator().compare(imageData, contents));
		assertNotSame(imageData, contents);
	}

	@Test
	public void test_nativeToJava() throws Exception {

		byte[] fileContents;
		try (InputStream stream = getImageAsInputStream()) {
			fileContents = stream.readAllBytes();
		}

		openAndFocusRemote();
		remote.setImageContents(fileContents);
		openAndFocusShell(false);
		ImageData expected = getImageData();
		assertEquals(0, imageDataComparator().compare(expected, getContents()));
	}

	@Order(1)
	@Test
	public void test_javaToNative() throws Exception {
		ImageData expected = getImageData();

		openAndFocusShell(true);
		setContents(expected);
		openAndFocusRemote();
		byte[] fileContents = SwtTestUtil.runOperationInThread(() -> remote.getImageContents());
		ImageData result = new ImageData(new ByteArrayInputStream(fileContents));
		assertEquals(0, imageDataComparator().compare(expected, result));
	}
}

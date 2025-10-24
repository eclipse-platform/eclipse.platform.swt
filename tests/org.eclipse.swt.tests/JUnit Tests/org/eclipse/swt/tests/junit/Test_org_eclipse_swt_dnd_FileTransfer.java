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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Automated Test Suite for {@link FileTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_FileTransfer extends ClipboardBase {
	/**
	 * FileTransfer is slightly mis-named as it takes a list.
	 */
	private FileTransfer fileTransfer;

	private List<Path> tmpFilesToDelete = new ArrayList<>();

	@BeforeEach
	public void localSetup() {
		fileTransfer = FileTransfer.getInstance();
	}

	/**
	 * Return a new name of a tempfile and clean it up at the end of the test
	 *
	 * This is probably more heavy weight than needed, but it is to ensure the file
	 * list has real paths suitable to the platform we are running on.
	 */
	private String tempFile() throws IOException {
		Path tempFile = Files.createTempFile("swt-test", "");
		tmpFilesToDelete.add(tempFile);
		return tempFile.toString();
	}

	@AfterEach
	public void deleteTempFiles() throws IOException {
		for (Path path : tmpFilesToDelete) {
			Files.delete(path);
		}
	}

	private String[] getFileList() throws IOException {
		return new String[] { tempFile(), tempFile(), tempFile() };
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, fileTransfer);
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
	private String[] getContents() throws Exception {
		CompletableFuture<Object> future = clipboard.getContentsAsync(fileTransfer);
		SwtTestUtil.processEvents(1000, () -> {
			return future.isDone();
		});
		Object o = future.get();
		assertInstanceOf(String[].class, o);
		return (String[]) o;
	}

	@Test
	public void test_Validate() throws Exception {
		openAndFocusShell(false);

		String[] fileList = getFileList();
		setContents(fileList);
		assertArrayEquals(fileList, getContents());

		assertThrows(IllegalArgumentException.class, () -> setContents(""));
		assertThrows(IllegalArgumentException.class, () -> setContents(Arrays.asList(getFileList())));
		assertThrows(IllegalArgumentException.class, () -> setContents(new String[0]));
		assertThrows(IllegalArgumentException.class, () -> setContents(new String[] { null }));
		assertThrows(IllegalArgumentException.class, () -> setContents(new String[] { "" }));
		assertThrows(IllegalArgumentException.class, () -> setContents(new String[] { tempFile(), null }));
		assertThrows(IllegalArgumentException.class, () -> setContents(new String[] { tempFile(), "" }));
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
		setContents(getFileList());
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(fileTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(getUniqueTestString(), RTFTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(fileTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		String[] fileList = getFileList();

		openAndFocusShell(false);
		setContents(fileList);
		String[] contents = getContents();
		assertArrayEquals(fileList, contents);
		assertNotSame(fileList, contents);
	}

	@Test
	@DisabledOnOs(value = { OS.WINDOWS, OS.MAC }, disabledReason = """
			AWT's DataFlavor.javaFileListFlavor used to test this method is not quite
			compatible with FileTransfer on Windows or macOS.

			Or the implementation of remote.setFileListContents is incorrect.

			On Windows DataFlavor.javaFileListFlavor does manipulate the contents of the
			strings by replacing long versions of folder names with 8.3 versions, so
			a different test method is needed to make this test stable
			""")
	public void test_nativeToJava() throws Exception {

		String[] fileList = getFileList();

		openAndFocusRemote();
		remote.setFileListContents(fileList);
		openAndFocusShell(false);

		assertArrayEquals(fileList, getContents());
	}

	@Order(1)
	@Test
	public void test_javaToNative() throws Exception {
		String[] fileList = getFileList();

		openAndFocusShell(true);
		setContents(fileList);
		openAndFocusRemote();
		String[] contents = SwtTestUtil.runOperationInThread(() -> remote.getFileListContents());
		assertArrayEquals(fileList, contents);

	}
}

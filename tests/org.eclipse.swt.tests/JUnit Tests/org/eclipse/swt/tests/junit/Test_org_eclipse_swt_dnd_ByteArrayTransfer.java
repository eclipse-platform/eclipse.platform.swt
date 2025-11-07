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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Automated Test Suite for class {@link ByteArrayTransfer}
 *
 * ByteArrayTransfer is abstract, so we test it via a custom subclass based on
 * the javadoc of ByteArrayTransfer, the inner classes below {@link MyType} and
 * {@link MyTypeTransfer}
 */
@Tag("clipboard")
@TestMethodOrder(OrderAnnotation.class) // run tests needing button presses first
public class Test_org_eclipse_swt_dnd_ByteArrayTransfer extends ClipboardBase {
	private MyTypeTransfer myTypeTransfer;

	@BeforeEach
	public void localSetup() {
		myTypeTransfer = MyTypeTransfer.getInstance();
	}

	private MyType getMyTypeInstance() {
		MyType myType = new MyType();
		myType.fileName = getUniqueTestString();
		myType.fileLength = 1234;
		myType.lastModified = 5678;
		return myType;
	}

	/**
	 * Duplicates what is in
	 * {@link MyTypeTransfer#javaToNative(Object, TransferData)}
	 *
	 * It is duplicated so the MyTypeTransfer can stay matching what is in
	 * {@link ByteArrayTransfer} javadoc.
	 */
	private byte[] serializeMyType(MyType myType) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (DataOutputStream writeOut = new DataOutputStream(out)) {
			byte[] fileNameBytes = myType.fileName.getBytes(StandardCharsets.UTF_8);
			writeOut.writeInt(fileNameBytes.length);
			writeOut.write(fileNameBytes);
			writeOut.writeLong(myType.fileLength);
			writeOut.writeLong(myType.lastModified);
			return out.toByteArray();
		}
	}

	/**
	 * Duplicates what is in {@link MyTypeTransfer#nativeToJava(TransferData)}
	 *
	 * It is duplicated so the MyTypeTransfer can stay matching what is in
	 * {@link ByteArrayTransfer} javadoc.
	 */
	private MyType deserializeMyType(byte[] buffer) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		try (DataInputStream readIn = new DataInputStream(in)) {
			MyType myType = new MyType();
			int size = readIn.readInt();
			byte[] name = new byte[size];
			readIn.read(name);
			myType.fileName = new String(name);
			myType.fileLength = readIn.readLong();
			myType.lastModified = readIn.readLong();
			return myType;
		}
	}

	private void assertMyTypeEquals(MyType expected, MyType actual) {
		assertEquals(expected.fileName, actual.fileName);
		assertEquals(expected.fileLength, actual.fileLength);
		assertEquals(expected.lastModified, actual.lastModified);
	}

	/**
	 * Convenience method to set the contents less verbosely
	 */
	private void setContents(Object o) {
		setContents(o, myTypeTransfer);
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
	private MyType getContents() throws Exception {
		CompletableFuture<Object> future = clipboard.getContentsAsync(myTypeTransfer);
		SwtTestUtil.processEvents(1000, () -> {
			return future.isDone();
		});
		Object o = future.get();
		assertInstanceOf(MyType.class, o);
		return (MyType) o;
	}

	@Test
	public void test_Validate() throws Exception {
		openAndFocusShell(false);
		MyType test = getMyTypeInstance();
		setContents(test);
		assertMyTypeEquals(test, getContents());
		assertThrows(IllegalArgumentException.class, () -> setContents(getUniqueTestString()));
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
		setContents(getMyTypeInstance());
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		assertTrue(Arrays.stream(availableTypes).anyMatch(myTypeTransfer::isSupportedType));

		// Put an incompatible type on the clipboard and ensure we don't match it
		setContents(new String[] { getUniqueTestString(), //
				getUniqueTestString() }, FileTransfer.getInstance());
		availableTypes = clipboard.getAvailableTypes();
		assertFalse(Arrays.stream(availableTypes).anyMatch(myTypeTransfer::isSupportedType));
	}

	/**
	 * Tests nativeToJava and javaToNative as a pair to ensure that objects are
	 * cloned as expected within the application.
	 */
	@Test
	public void test_internalClone() throws Exception {
		MyType test = getMyTypeInstance();

		openAndFocusShell(false);

		setContents(test);
		MyType contents = getContents();
		assertMyTypeEquals(test, contents);
		assertNotSame(test, contents);
	}

	@Test
	public void test_nativeToJava() throws Exception {
		MyType test = getMyTypeInstance();

		openAndFocusRemote();
		remote.setMyTypeContents(serializeMyType(test));
		openAndFocusShell(false);
		assertMyTypeEquals(test, getContents());
	}

	@Order(1)
	@Test
	@Tag("gtk4-todo")
	@DisabledOnOs(value = OS.MAC, disabledReason = """
			remote.getMyTypeContents doesn't work properly on macOS, this is
			probably a test only issue on macOS
			""")
	public void test_javaToNative() throws Exception {
		MyType test = getMyTypeInstance();

		openAndFocusShell(true);
		setContents(test);
		openAndFocusRemote();
		byte[] resultBytes = SwtTestUtil.runOperationInThread(() -> remote.getMyTypeContents());
		MyType result = deserializeMyType(resultBytes);
		assertMyTypeEquals(test, result);
	}

	// apart from the static, this should be a straight copy/paste of
	// the code in the ByteArrayTransfer javadoc
	public static class MyType {
		public String fileName;
		public long fileLength;
		public long lastModified;
	}

	// apart from the static, this should be a straight copy/paste of
	// the code in the ByteArrayTransfer javadoc
	public static class MyTypeTransfer extends ByteArrayTransfer {

		private static final String MYTYPENAME = "my_type_name";
		private static final int MYTYPEID = registerType(MYTYPENAME);
		private static MyTypeTransfer _instance = new MyTypeTransfer();

		private MyTypeTransfer() {
		}

		public static MyTypeTransfer getInstance() {
			return _instance;
		}

		@Override
		public void javaToNative(Object object, TransferData transferData) {
			if (!checkMyType(object) || !isSupportedType(transferData)) {
				DND.error(DND.ERROR_INVALID_DATA);
			}

			MyType myType = (MyType) object;
			// write data to a byte array and then ask super to convert to native
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try (DataOutputStream writeOut = new DataOutputStream(out)) {
				byte[] fileNameBytes = myType.fileName.getBytes(StandardCharsets.UTF_8);
				writeOut.writeInt(fileNameBytes.length);
				writeOut.write(fileNameBytes);
				writeOut.writeLong(myType.fileLength);
				writeOut.writeLong(myType.lastModified);
				super.javaToNative(out.toByteArray(), transferData);
			} catch (IOException e) {
			}
		}

		@Override
		public Object nativeToJava(TransferData transferData) {
			if (!isSupportedType(transferData)) {
				return null;
			}

			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null) {
				return null;
			}

			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			try (DataInputStream readIn = new DataInputStream(in)) {
				MyType myType = new MyType();
				int size = readIn.readInt();
				byte[] name = new byte[size];
				readIn.read(name);
				myType.fileName = new String(name);
				myType.fileLength = readIn.readLong();
				myType.lastModified = readIn.readLong();
				return myType;
			} catch (IOException ex) {
			}
			return null;
		}

		@Override
		protected String[] getTypeNames() {
			return new String[] { MYTYPENAME };
		}

		@Override
		protected int[] getTypeIds() {
			return new int[] { MYTYPEID };
		}

		private boolean checkMyType(Object object) {
			return (object instanceof MyType);
		}

		@Override
		protected boolean validate(Object object) {
			return checkMyType(object);
		}
	}
}

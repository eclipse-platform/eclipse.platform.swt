/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc. and others.
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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Capture any output on System.err + System.out
 *
 * This class does not capture output on stdout/stderr from C level, such as
 * Gdk-CRITICAL messages.
 *
 * See {@link SwtTestUtil#runWithCapturedStderr(Runnable)} for a use case, or
 * construct this class directly in a try-with-resources block, such as:
 *
 * <pre>
 * try (var captured = new CapturedOutput()) {
 *    // run your test
 *
 *    assertEquals("expected System.out contents", captured.getOutContent());
 *    assertEquals("expected System.err contents", captured.getErrContent());
 * }
 * </pre>
 */
public class CapturedOutput implements Closeable {

	private PrintStream originalOut;
	private PrintStream originalErr;
	private ByteArrayOutputStream outContent;
	private ByteArrayOutputStream errContent;
	private PrintStream outPrintStream;
	private PrintStream errPrintStream;

	public CapturedOutput() {
		originalOut = System.out;
		originalErr = System.err;
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		outPrintStream = new PrintStream(outContent, true, StandardCharsets.UTF_8);
		System.setOut(outPrintStream);
		errPrintStream = new PrintStream(errContent, true, StandardCharsets.UTF_8);
		System.setErr(errPrintStream);

	}

	public String getOutContent() {
		outPrintStream.flush();
		return outContent.toString(StandardCharsets.UTF_8);
	}

	public String getErrContent() {
		errPrintStream.flush();
		return errContent.toString(StandardCharsets.UTF_8);
	}

	/**
	 * Asserts that System.out + System.err have no content.
	 */
	public void assertNoOutput() {
		assertEquals("", getOutContent());
		assertEquals("", getErrContent());
	}

	@Override
	public void close() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}
}

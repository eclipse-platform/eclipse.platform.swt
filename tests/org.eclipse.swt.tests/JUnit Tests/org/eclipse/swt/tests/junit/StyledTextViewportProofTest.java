/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Distant-viewport proof for very long text in StyledText, in the style of the
 * Nebula million-row Grid proof: top, middle, bottom + horizontal and return
 * scenes over a 10M-line (about 1 GiB) document, each with a control-surface
 * capture, an optional SWT screen capture and a sidecar of logical/measured
 * counts. Opt-in: <code>-Dswt.viewport.styledTextProof=true</code>; size with
 * <code>-Dswt.viewport.styledTextProof.lines</code> and
 * <code>.lineChars</code>; output <code>-Dswt.viewport.screenshots=&lt;dir&gt;</code>;
 * screen pixels with <code>-Dswt.viewport.screenshots.screen=true</code>.
 */
public class StyledTextViewportProofTest {
	private static final int LINES = Integer.getInteger("swt.viewport.styledTextProof.lines", 10_000_000);
	private static final int LINE_CHARS = Integer.getInteger("swt.viewport.styledTextProof.lineChars", 107);
	/** Upper bound on measured lines: viewport + overscan + caret/selection lines, never O(document). */
	private static final int MEASURED_LIMIT = 4096;

	private Display display;
	private Shell shell;
	private StyledText text;
	private Path output;

	@BeforeEach
	public void setUp() throws IOException {
		assumeTrue(Boolean.getBoolean("swt.viewport.styledTextProof"), "opt-in proof lane");
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(900, 600);
		text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		shell.open();
		output = Path.of(System.getProperty("swt.viewport.screenshots", "target/styledtext-viewport"));
		Files.createDirectories(output);
	}

	@AfterEach
	public void tearDown() {
		if (shell != null) {
			shell.dispose();
		}
	}

	@Test
	public void test_tenMillionLinesFixedHeight() throws Exception {
		proof("fixed", false);
	}

	@Test
	public void test_tenMillionLinesWordWrap() throws Exception {
		proof("wrap", true);
	}

	private void proof(String mode, boolean wrap) throws Exception {
		text.setWordWrap(wrap);
		text.setText(document());
		int lineCount = text.getLineCount();
		assertEquals(LINES + 1, lineCount);
		snapshot(mode + "-01-top");
		assertEquals(0, text.getTopIndex());
		assertMeasuredBounded();

		int middle = LINES / 2;
		text.setTopIndex(middle);
		int anchor = text.getOffsetAtLine(middle + 1);
		text.setSelection(anchor, text.getOffsetAtLine(middle + 4));
		snapshot(mode + "-02-middle");
		assertTrue(Math.abs(text.getTopIndex() - middle) <= 2, "middle top index " + text.getTopIndex());
		assertMeasuredBounded();

		text.setTopIndex(lineCount - 1);
		text.setHorizontalPixel(200);
		snapshot(mode + "-03-bottom-horizontal");
		assertTrue(text.getTopIndex() >= lineCount - 200, "bottom top index " + text.getTopIndex());
		assertEquals(new Point(anchor, text.getOffsetAtLine(middle + 4)), text.getSelection(), "pinned selection");
		assertMeasuredBounded();

		text.setHorizontalPixel(0);
		text.setTopIndex(0);
		snapshot(mode + "-04-return");
		assertEquals(0, text.getTopIndex());
		assertEquals(line(0), text.getLine(0));
		assertEquals(line(middle), text.getLine(middle));
		assertMeasuredBounded();
	}

	private static String document() {
		StringBuilder builder = new StringBuilder(LINES * (LINE_CHARS + 1));
		for (int i = 0; i < LINES; i++) {
			builder.append(line(i)).append('\n');
		}
		return builder.toString();
	}

	private static String line(int index) {
		StringBuilder line = new StringBuilder(LINE_CHARS);
		line.append(String.format("%010d ", index));
		while (line.length() < LINE_CHARS) {
			line.append("viewport ");
		}
		line.setLength(LINE_CHARS);
		return line.toString();
	}

	/** Lines with a materialized size entry; reads the renderer reflectively (test-only). */
	private int measuredLines() throws ReflectiveOperationException {
		Field rendererField = StyledText.class.getDeclaredField("renderer");
		rendererField.setAccessible(true);
		Object renderer = rendererField.get(text);
		Field sizesField = renderer.getClass().getDeclaredField("lineSizes");
		sizesField.setAccessible(true);
		Object[] sizes = (Object[]) sizesField.get(renderer);
		int measured = 0;
		for (Object size : sizes) {
			if (size != null) {
				measured++;
			}
		}
		return measured;
	}

	private void assertMeasuredBounded() throws ReflectiveOperationException {
		int measured = measuredLines();
		assertTrue(measured <= MEASURED_LIMIT, "measured lines " + measured + " > " + MEASURED_LIMIT);
	}

	private void flushPaint() {
		text.redraw();
		text.update();
		long deadline = System.currentTimeMillis() + 2000;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// drain bounded idle measurement and paint
		}
	}

	private void snapshot(String name) throws Exception {
		flushPaint();
		Map<String, String> sidecar = new LinkedHashMap<>();
		sidecar.put("logicalLines", Integer.toString(text.getLineCount()));
		sidecar.put("logicalChars", Integer.toString(text.getCharCount()));
		sidecar.put("measuredLines", Integer.toString(measuredLines()));
		sidecar.put("topIndex", Integer.toString(text.getTopIndex()));
		sidecar.put("topPixel", Integer.toString(text.getTopPixel()));
		sidecar.put("horizontalPixel", Integer.toString(text.getHorizontalPixel()));
		sidecar.put("selection", text.getSelection().toString());
		sidecar.put("wordWrap", Boolean.toString(text.getWordWrap()));
		sidecar.put("contentClass", text.getContent().getClass().getSimpleName());
		ScrollBar bar = text.getVerticalBar();
		sidecar.put("vbar", bar.getSelection() + "/" + bar.getMaximum() + "/" + bar.getThumb());
		SwtScreenshotCapture.Result control = SwtScreenshotCapture.captureControl(text, output.resolve(name + ".png"));
		sidecar.put("captureMethod", control.method().name());
		sidecar.put("sha256", control.sha256());
		if (Boolean.getBoolean("swt.viewport.screenshots.screen")) {
			SwtScreenshotCapture.Result screen = SwtScreenshotCapture.captureScreenControl(text, output.resolve(name + "-screen.png"));
			sidecar.put("screen.captureMethod", screen.method().name());
			sidecar.put("screen.sha256", screen.sha256());
		}
		StringBuilder lines = new StringBuilder();
		sidecar.forEach((key, value) -> lines.append(key).append('=').append(value).append('\n'));
		Files.writeString(output.resolve(name + ".txt"), lines, StandardCharsets.UTF_8);
	}
}

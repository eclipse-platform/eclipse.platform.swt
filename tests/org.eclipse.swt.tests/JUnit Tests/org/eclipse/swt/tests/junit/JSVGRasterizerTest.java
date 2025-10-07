/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.svg.JSVGRasterizer;
import org.junit.jupiter.api.Test;

@SuppressWarnings("restriction")
class JSVGRasterizerTest {

	private final JSVGRasterizer rasterizer = new JSVGRasterizer();
	private String svgString = """
			<svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
			    <rect width="100%" height="100%"/>
			</svg>
			""";

	private ByteArrayInputStream svgStream(String svg) {
		return new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));
	}

	private ByteArrayInputStream invalidSvg = new ByteArrayInputStream(new byte[0]);

	@Test
	void testRasterizeWithZoom() {
		ImageData data = rasterizer.rasterizeSVG(svgStream(svgString), 200);
		assertEquals(200, data.width);
		assertEquals(200, data.height);
	}

	@Test
	void testRasterizeWithZoomNegative() {
		try {
			rasterizer.rasterizeSVG(svgStream(svgString), -100);

		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for negative zoom", SWT.ERROR_INVALID_ARGUMENT, e);
		}

	}

	@Test
	void testRasterizeWithZoomWithInvalidSVG() {

		SWTException exception = assertThrows(SWTException.class, () -> {
			rasterizer.rasterizeSVG(invalidSvg, 100);
		});
		assertEquals(SWT.ERROR_INVALID_IMAGE, exception.code);
	}

	@Test
	void testRasterizeWithTargetSize() {
		ImageData data = rasterizer.rasterizeSVG(svgStream(svgString), 300, 150);
		assertEquals(300, data.width);
		assertEquals(150, data.height);
	}

	@Test
	void testRasterizeWithTargetSizeHavingInvalidHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			rasterizer.rasterizeSVG(svgStream(svgString), -1, 150);
		});
	}

	@Test
	void testRasterizeWithTargetSizeHavingInvalidWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			rasterizer.rasterizeSVG(svgStream(svgString), 150, -1);
		});
	}

	@Test
	void testRasterizeWithTargetSizeWithInvalidSVG() {
		SWTException exception = assertThrows(SWTException.class, () -> {
			rasterizer.rasterizeSVG(invalidSvg, 150, 150);
		});
		assertEquals(SWT.ERROR_INVALID_IMAGE, exception.code);
	}

}

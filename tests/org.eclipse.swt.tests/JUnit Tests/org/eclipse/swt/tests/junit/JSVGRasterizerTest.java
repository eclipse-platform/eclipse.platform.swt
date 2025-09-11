/*******************************************************************************
 * Copyright (c) 2024, 2025 Yatta Solutions and others.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.svg.JSVGRasterizer;
import org.junit.jupiter.api.Test;

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

    @Test
    void testRasterizeWithZoom(){
        ImageData data = rasterizer.rasterizeSVG(svgStream(svgString), 200);
        assertEquals(200, data.width);
        assertEquals(200, data.height);
    }

    @Test
    void testRasterizeWithWidthHeight(){
        ImageData data = rasterizer.rasterizeSVG(svgStream(svgString), 300, 150);
        assertEquals(300, data.width);
        assertEquals(150, data.height);
    }


}

/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageFileNameProvider;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

/**
 * When executed locally (outside Tycho build), this tests needs to be run as
 * JUnit plug-in test in order to have the SVGRasterizer fragment on the
 * classpath.
 */
public class Test_org_eclipse_swt_internal_SVGRasterizer {

	@Test
	public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageFileNameProvider() {
		ImageFileNameProvider validImageFileNameProvider = zoom -> getPath("collapseall.svg");
		Image image = new Image(Display.getDefault(), validImageFileNameProvider);
		image.dispose();

		ImageFileNameProvider corruptImageFileNameProvider = zoom -> getPath("corrupt.svg");
		SWTException e = assertThrows(SWTException.class,
				() -> new Image(Display.getDefault(), corruptImageFileNameProvider));
		assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
	}

	@Test
	public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageDataProvider() {
		ImageDataProvider validImageDataProvider = zoom -> {
			String fileName = "collapseall.svg";
			return new ImageData(getPath(fileName));
		};
		Image image = new Image(Display.getDefault(), validImageDataProvider);
		image.dispose();

		ImageDataProvider corruptImageDataProvider = zoom -> {
			String fileName = "corrupt.svg";
			return new ImageData(getPath(fileName));
		};
		SWTException e = assertThrows(SWTException.class,
				() -> new Image(Display.getDefault(), corruptImageDataProvider));
		assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
	}

	String getPath(String fileName) {
		String urlPath = "";
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (pluginPath == null) {
			URL url = getClass().getClassLoader().getResource(fileName);
			if (url == null) {
				fail("URL == null for file " + fileName);
			}
			urlPath = url.getFile();
		} else {
			urlPath = pluginPath + "/data/" + fileName;
		}
		if (File.separatorChar != '/')
			urlPath = urlPath.replace('/', File.separatorChar);
		if (SwtTestUtil.isWindows && urlPath.indexOf(File.separatorChar) == 0)
			urlPath = urlPath.substring(1);
		urlPath = urlPath.replaceAll("%20", " ");
		// Fallback when test is locally executed as plug-in test
		if (!Files.exists(Path.of(urlPath))) {
			urlPath = Path.of("data/" + fileName).toAbsolutePath().toString();
		}
		assertTrue(Files.exists(Path.of(urlPath)), "file not found: " + urlPath);
		return urlPath;
	}
}
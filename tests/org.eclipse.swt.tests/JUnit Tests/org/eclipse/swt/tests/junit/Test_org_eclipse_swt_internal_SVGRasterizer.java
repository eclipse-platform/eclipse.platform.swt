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

import java.io.File;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageFileNameProvider;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

public class Test_org_eclipse_swt_internal_SVGRasterizer {

	Display display;

	ImageFileNameProvider imageFileNameProvider = zoom -> {
		String fileName = "collapseall.svg";
		return getPath(fileName);
	};

	ImageDataProvider imageDataProvider = zoom -> {
		String fileName = "collapseall.svg";
		return new ImageData(getPath(fileName));
	};

	@Before
	public void setUp() {
		display = Display.getDefault();
	}

	String getPath(String fileName) {
		String urlPath = "";
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (pluginPath == null) {
			urlPath = Path.of("data/" + fileName).toAbsolutePath().toString();
		} else {
			urlPath = pluginPath + "/data/" + fileName;
		}
		if (File.separatorChar != '/')
			urlPath = urlPath.replace('/', File.separatorChar);
		if (SwtTestUtil.isWindows && urlPath.indexOf(File.separatorChar) == 0)
			urlPath = urlPath.substring(1);
		urlPath = urlPath.replaceAll("%20", " ");
		return urlPath;
	}

	@Test
	public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageFileNameProvider() {
		// Valid provider
		Image image = new Image(display, imageFileNameProvider);
		image.dispose();
		// Corrupt Image provider
		ImageFileNameProvider provider = zoom -> {
			String fileName = "corrupt.svg";
			return getPath(fileName);
		};
		try {
			image = new Image(display, provider);
			image.dispose();
			fail("No exception thrown for corrupt image file.");
		} catch (SWTException e) {
			assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
		}
	}

	@Test
	public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageDataProvider() {
		// Valid provider
		Image image = new Image(display, imageDataProvider);
		image.dispose();
		// Corrupt Image provider
		ImageDataProvider provider = zoom -> {
			String fileName = "corrupt.svg";
			return new ImageData(getPath(fileName));
		};
		try {
			image = new Image(display, provider);
			image.dispose();
			fail("No exception thrown for corrupt image file.");
		} catch (SWTException e) {
			assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
		}
	}
}
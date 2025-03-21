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
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageFileNameProvider;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

public class Test_org_eclipse_swt_internal_SVGRasterizer {

	@Test
	public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageFileNameProvider() {
		ImageFileNameProvider validImageFileNameProvider = zoom -> {
			String fileName = "collapseall.svg";
			return getPath(fileName);
		};
		Image image = new Image(Display.getDefault(), validImageFileNameProvider);
		image.dispose();

		ImageFileNameProvider corruptImageFileNameProvider = zoom -> {
			String fileName = "corrupt.svg";
			return getPath(fileName);
		};
		try {
			image = new Image(Display.getDefault(), corruptImageFileNameProvider);
			image.dispose();
			fail("No exception thrown for corrupt image file.");
		} catch (SWTException e) {
			assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
		}
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
		try {
			image = new Image(Display.getDefault(), corruptImageDataProvider);
			image.dispose();
			fail("No exception thrown for corrupt image file.");
		} catch (SWTException e) {
			assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
		}
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
		return urlPath;
	}
}
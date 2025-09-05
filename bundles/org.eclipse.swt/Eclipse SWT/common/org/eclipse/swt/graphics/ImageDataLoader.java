/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.DPIUtil.*;

/**
 * Internal class that separates ImageData from ImageLoader
 * to allow removal of ImageLoader from the toolkit.
 */
class ImageDataLoader {

	public static ImageData load(InputStream stream) {
		ImageData[] data = new ImageLoader().load(stream);
		if (data.length < 1) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data[0];
	}

	public static ImageData load(String filename) {
		ImageData[] data = new ImageLoader().load(filename);
		if (data.length < 1) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data[0];
	}

	public static boolean canLoadAtZoom(InputStream stream, int fileZoom, int targetZoom) {
		return ImageLoader.canLoadAtZoom(stream, fileZoom, targetZoom);
	}

	public static boolean canLoadAtZoom(String filename, int fileZoom, int targetZoom) {
		return ImageLoader.canLoadAtZoom(filename, fileZoom, targetZoom);
	}

	public static ElementAtZoom<ImageData> loadByZoom(InputStream stream, int fileZoom, int targetZoom) {
		List<ElementAtZoom<ImageData>> data = new ImageLoader().loadByZoom(stream, fileZoom, targetZoom);
		if (data.isEmpty()) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data.get(0);
	}

	public static ElementAtZoom<ImageData> loadByZoom(String filename, int fileZoom, int targetZoom) {
		List<ElementAtZoom<ImageData>> data = new ImageLoader().loadByZoom(filename, fileZoom, targetZoom);
		if (data.isEmpty()) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data.get(0);
	}

	public static ImageData loadByTargetSize(InputStream stream, int targetWidth, int targetHeight) {
		ImageData data = new ImageLoader().loadByTargetSize(stream, targetWidth, targetHeight);
		if (data == null) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data;
	}

	public static ImageData loadByTargetSize(String filename, int targetWidth, int targetHeight) {
		ImageData data = new ImageLoader().loadByTargetSize(filename, targetWidth, targetHeight);
		if (data == null) SWT.error(SWT.ERROR_INVALID_IMAGE);
		return data;
	}

}

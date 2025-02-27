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

import org.eclipse.swt.internal.DPIUtil.*;

/**
 * Internal class that separates ImageData from ImageLoader
 * to allow removal of ImageLoader from the toolkit.
 */
class ImageDataLoader {

	public static ImageData[] load(InputStream stream) {
		return new ImageLoader().load(stream);
	}

	public static ImageData[] load(String filename) {
		return new ImageLoader().load(filename);
	}

	public static List<ElementAtZoom<ImageData>> load(String filename, int fileZoom, int targetZoom) {
		return new ImageLoader().load(filename, fileZoom, targetZoom);
	}

}

/*******************************************************************************
 * Copyright (c) 2025, 2025 Hannes Wellmann and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hannes Wellmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;

import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.internal.image.*;

class InternalImageLoader {

	static List<ElementAtZoom<ImageData>> load(InputStream stream, ImageLoader imageLoader, int fileZoom, int targetZoom, int flag) {
		return FileFormat.load(stream, imageLoader, fileZoom, targetZoom, flag);
	}

	static void save(OutputStream stream, int format, ImageLoader imageLoader) {
		FileFormat.save(stream, format, imageLoader);
	}
}

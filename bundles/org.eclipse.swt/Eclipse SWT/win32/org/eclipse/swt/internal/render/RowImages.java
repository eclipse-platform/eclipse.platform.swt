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
package org.eclipse.swt.internal.render;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/** Conversions shared by the Skija and Direct2D backends: image pixels and native font attributes. */
public final class RowImages {
	private RowImages () {
	}

	/** SWT image data as premultiplied BGRA bytes, top-down (alpha channel, transparent pixel or mask). */
	public static byte [] bgra (ImageData data) {
		int w = data.width, h = data.height;
		byte [] bgra = new byte [w * h * 4];
		int type = data.getTransparencyType ();
		ImageData mask = type == SWT.TRANSPARENCY_MASK || type == SWT.TRANSPARENCY_PIXEL ? data.getTransparencyMask () : null;
		for (int y = 0, i = 0; y < h; y++) {
			for (int x = 0; x < w; x++, i += 4) {
				RGB rgb = data.palette.getRGB (data.getPixel (x, y));
				int alpha = mask != null ? mask.getPixel (x, y) != 0 ? 255 : 0 : data.getAlpha (x, y);
				bgra [i] = (byte) (rgb.blue * alpha / 255);
				bgra [i + 1] = (byte) (rgb.green * alpha / 255);
				bgra [i + 2] = (byte) (rgb.red * alpha / 255);
				bgra [i + 3] = (byte) alpha;
			}
		}
		return bgra;
	}

	/** The attributes of a native font: answers the face and fills <code>out</code> with {height, weight, italic}. */
	public static String font (long hFont, int [] out) {
		LOGFONT logFont = new LOGFONT ();
		if (hFont == 0 || hFont == -1 || OS.GetObject (hFont, LOGFONT.sizeof, logFont) == 0) {
			out [0] = -12;
			out [1] = 400;
			out [2] = 0;
			return "Segoe UI";
		}
		int length = 0;
		while (length < logFont.lfFaceName.length && logFont.lfFaceName [length] != 0) length++;
		out [0] = logFont.lfHeight;
		out [1] = logFont.lfWeight == 0 ? 400 : logFont.lfWeight;
		out [2] = logFont.lfItalic != 0 ? 1 : 0;
		return new String (logFont.lfFaceName, 0, length);
	}
}

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
package org.eclipse.swt.examples.superset;

import java.util.zip.CRC32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Control;

/**
 * Shared resources and probes for the round-trip reproduction test: a bold
 * font (disposed with its control), system colours and a system image (never
 * disposed), a compact state formatter and the Control.print pixel hash.
 */
final class UiState {

	final Font bold;
	final Color red, yellow;
	final Image image;

	UiState(Control control) {
		FontData fd = control.getFont().getFontData()[0];
		bold = new Font(control.getDisplay(), fd.getName(), fd.getHeight(), SWT.BOLD);
		control.addListener(SWT.Dispose, e -> bold.dispose());
		red = control.getDisplay().getSystemColor(SWT.COLOR_RED);
		yellow = control.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		image = control.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
	}

	/** @return a stable token for a font: "bold" (the probe font), "other" or "null" */
	String font(Font f) {
		return f == null ? "null" : f.equals(bold) ? "bold" : "other";
	}

	static String rgb(Color c) {
		return c == null ? "null" : Integer.toHexString(c.getRGB().hashCode());
	}

	/** @return the control's pixels via Control.print, or null when it cannot print */
	static ImageData print(Control control) {
		CaptureUtil.Capture c = CaptureUtil.printControl(control, false);
		return c == null ? null : c.data();
	}

	/**
	 * @return pixels that differ between two captures outside {@code damage}
	 *         (all differing pixels when damage is null); 0 when either capture
	 *         is missing or the sizes differ
	 */
	static long changedOutside(ImageData a, ImageData b, org.eclipse.swt.graphics.Rectangle damage) {
		if (a == null || b == null || a.width != b.width || a.height != b.height || a.bytesPerLine != b.bytesPerLine) return 0;
		long n = 0;
		for (int y = 0; y < a.height; y++) {
			int off = y * a.bytesPerLine;
			if (java.util.Arrays.equals(a.data, off, off + a.bytesPerLine, b.data, off, off + a.bytesPerLine)) continue;
			for (int x = 0; x < a.width; x++) {
				if (a.getPixel(x, y) != b.getPixel(x, y) && (damage == null || !damage.contains(x, y))) n++;
			}
		}
		return n;
	}

	/** @return CRC32 of the control's printed pixels, or -1 when it cannot print */
	static long pixelHash(Control control) {
		ImageData data = print(control);
		if (data == null) return -1;
		CRC32 crc = new CRC32();
		crc.update(data.data, 0, data.data.length);
		return crc.getValue();
	}

	/** @return pixels that differ between a and b inside the packed {x,y,w,h,...} bands (overlaps counted once) */
	static long changedInBands(ImageData a, ImageData b, int[] bands) {
		if (a == null || b == null || a.width != b.width || a.height != b.height) return -1;
		long n = 0;
		for (int y = 0; y < a.height; y++) {
			for (int x = 0; x < a.width; x++) {
				if (inBands(x, y, bands) && a.getPixel(x, y) != b.getPixel(x, y)) n++;
			}
		}
		return n;
	}

	private static boolean inBands(int x, int y, int[] bands) {
		for (int i = 0; i < bands.length; i += 4) {
			if (x >= bands[i] && y >= bands[i + 1] && x < bands[i] + bands[i + 2] && y < bands[i + 1] + bands[i + 3]) return true;
		}
		return false;
	}
}

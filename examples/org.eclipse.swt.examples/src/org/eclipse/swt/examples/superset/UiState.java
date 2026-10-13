/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.util.zip.CRC32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
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

	/** @return CRC32 of the control's printed pixels, or -1 when it cannot print */
	static long pixelHash(Control control) {
		Point size = control.getSize();
		if (size.x <= 0 || size.y <= 0) return -1;
		Image img = new Image(control.getDisplay(), size.x, size.y);
		try {
			GC gc = new GC(img);
			boolean printed = control.print(gc);
			gc.dispose();
			if (!printed) return -1;
			ImageData data = img.getImageData();
			CRC32 crc = new CRC32();
			crc.update(data.data, 0, data.data.length);
			return crc.getValue();
		} finally {
			img.dispose();
		}
	}
}

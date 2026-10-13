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

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * Screenshot through {@link Control#print(GC)} (works for covered windows on a
 * shared desktop, unlike a screen grab), downscaled to at most
 * {@link #MAX_WIDTH} px and saved as PNG; re-saved smaller while above
 * {@link #MAX_BYTES}.
 */
public final class Snapshot {

	public static final int MAX_WIDTH = 480;
	public static final long MAX_BYTES = 150 * 1024;

	private Snapshot() {
	}

	/** @return the file name written, or "-" when the control cannot print */
	public static String save(Control control, File dir, String name) {
		Point size = control.getSize();
		if (size.x <= 0 || size.y <= 0) return "-";
		Image full = new Image(control.getDisplay(), size.x, size.y);
		try {
			GC gc = new GC(full);
			boolean printed = control.print(gc);
			gc.dispose();
			if (!printed) return "-";
			dir.mkdirs();
			File file = new File(dir, name);
			for (int width = Math.min(MAX_WIDTH, size.x); width >= 120; width = width * 3 / 4) {
				write(full, size, width, file);
				if (file.length() <= MAX_BYTES) break;
			}
			return file.getName();
		} finally {
			full.dispose();
		}
	}

	private static void write(Image full, Point size, int width, File file) {
		int height = Math.max(1, size.y * width / size.x);
		Image small = new Image(full.getDevice(), width, height);
		try {
			GC gc = new GC(small);
			gc.setAntialias(SWT.ON);
			gc.setInterpolation(SWT.HIGH);
			gc.drawImage(full, 0, 0, size.x, size.y, 0, 0, width, height);
			gc.dispose();
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] {small.getImageData()};
			loader.save(file.getPath(), SWT.IMAGE_PNG);
		} finally {
			small.dispose();
		}
	}
}

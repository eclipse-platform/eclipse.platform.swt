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

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT-level capture at every level, shared by the showcase, the matrix runner
 * and other agents (see superset-matrix/README.md):
 * <ul>
 * <li>{@link #printControl} / {@link #printShell}: {@code Control.print(GC)}
 * (WM_PRINT on win32). Works while the window is covered; used for pixel
 * hashes, round-trip reproduction and edge-pixel checks.</li>
 * <li>{@link #copyDisplay}: {@code new GC(display).copyArea} over the shell's
 * display bounds after activating the shell: what is really on screen,
 * including native non-client scrollbars and composition.</li>
 * </ul>
 * Each capture carries its pixels, PNG bytes and SHA-256 of the PNG.
 */
public final class CaptureUtil {

	/** One capture: mode is "print" or "display"; png/sha256 are null when encoding was skipped. */
	public record Capture(String mode, ImageData data, byte[] png, String sha256) {
	}

	private CaptureUtil() {
	}

	/** @return the control printed into an image, or null when it cannot print */
	public static Capture printControl(Control control, boolean encode) {
		Point size = control.getSize();
		if (size.x <= 0 || size.y <= 0) return null;
		Image img = new Image(control.getDisplay(), size.x, size.y);
		try {
			GC gc = new GC(img);
			boolean printed = control.print(gc);
			gc.dispose();
			return printed ? capture("print", img.getImageData(), encode) : null;
		} finally {
			img.dispose();
		}
	}

	public static Capture printShell(Shell shell, boolean encode) {
		return printControl(shell, encode);
	}

	/**
	 * Activates the shell, processes events until idle, then copies its display
	 * bounds from the screen. @return the capture, or null when the shell could
	 * not be made the active shell (another window would be captured instead)
	 */
	public static Capture copyDisplay(Shell shell, boolean encode) {
		return copyDisplay((Control) shell, encode);
	}

	/** Same as {@link #copyDisplay(Shell, boolean)} over one control's display bounds. */
	public static Capture copyDisplay(Control control, boolean encode) {
		Display display = control.getDisplay();
		Shell shell = control.getShell();
		shell.setActive();
		shell.forceActive();
		idle(display);
		if (display.getActiveShell() != shell) return null;
		Rectangle b = control.getBounds();
		if (!(control instanceof Shell)) {
			// outer bounds (border included), the same area Control.print draws
			Point origin = control.getParent().toDisplay(b.x, b.y);
			b = new Rectangle(origin.x, origin.y, b.width, b.height);
		}
		Image img = new Image(display, b.width, b.height);
		try {
			GC gc = new GC(display);
			gc.copyArea(img, b.x, b.y);
			gc.dispose();
			return capture("display", img.getImageData(), encode);
		} finally {
			img.dispose();
		}
	}

	private static void idle(Display display) {
		for (int quiet = 0; quiet < 3;) {
			if (display.readAndDispatch()) {
				quiet = 0;
				continue;
			}
			quiet++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	private static Capture capture(String mode, ImageData data, boolean encode) {
		if (!encode) return new Capture(mode, data, null, null);
		byte[] png = png(data);
		return new Capture(mode, data, png, sha256(png));
	}

	/** @return PNG bytes of {@code data} */
	public static byte[] png(ImageData data) {
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {data};
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		loader.save(out, SWT.IMAGE_PNG);
		return out.toByteArray();
	}

	public static String sha256(byte[] bytes) {
		try {
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	/** @return pixels that differ between two same-sized captures, or -1 when sizes differ */
	public static long diffPixels(ImageData a, ImageData b) {
		if (a == null || b == null || a.width != b.width || a.height != b.height) return -1;
		long n = 0;
		for (int y = 0; y < a.height; y++) {
			for (int x = 0; x < a.width; x++) {
				if (rgb(a, x, y) != rgb(b, x, y)) n++;
			}
		}
		return n;
	}

	/** @return the pixel as 0xRRGGBB whatever the image depth or palette */
	static int rgb(ImageData d, int x, int y) {
		var c = d.palette.getRGB(d.getPixel(x, y));
		return (c.red << 16) | (c.green << 8) | c.blue;
	}
}

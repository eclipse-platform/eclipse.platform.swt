/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.security.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT-native screenshot support for visual regressions.
 *
 * <p>Control capture prefers {@link Control#print(GC)} so the widget paints into
 * an offscreen image. If a platform refuses that path, it falls back to
 * {@link GC#copyArea(Image, int, int)} on the control GC. Native window capture
 * deliberately uses a Display GC so the result includes pixels produced by the
 * platform/window system around the SWT shell.</p>
 */
final class SwtScreenshotCapture {

	enum Method {
		CONTROL_PRINT,
		CONTROL_COPY_AREA,
		DISPLAY_COPY_AREA
	}

	record Result(Path path, Method method, String sha256) {
	}

	private SwtScreenshotCapture () {
	}

	static Result captureControl (Control control, Path path) throws IOException {
		Objects.requireNonNull (control, "control");
		Objects.requireNonNull (path, "path");
		if (control.isDisposed ()) throw new IllegalArgumentException ("disposed control");

		Point size = control.getSize ();
		if (size.x <= 0 || size.y <= 0) {
			throw new IllegalArgumentException ("control has no drawable area");
		}

		Image image = new Image (control.getDisplay (), size.x, size.y);
		Method method = Method.CONTROL_PRINT;
		try {
			GC imageGc = new GC (image);
			try {
				if (!control.print (imageGc)) {
					method = Method.CONTROL_COPY_AREA;
					imageGc.dispose ();
					imageGc = null;
					GC controlGc = new GC (control);
					try {
						controlGc.copyArea (image, 0, 0);
					} finally {
						controlGc.dispose ();
					}
				}
			} finally {
				if (imageGc != null && !imageGc.isDisposed ()) imageGc.dispose ();
			}
			savePng (image, path);
		} finally {
			image.dispose ();
		}
		return new Result (path, method, sha256 (path));
	}

	static Result captureNativeShell (Control control, Path path) throws IOException {
		Objects.requireNonNull (control, "control");
		Objects.requireNonNull (path, "path");
		if (control.isDisposed ()) throw new IllegalArgumentException ("disposed control");

		Shell shell = control.getShell ();
		Rectangle bounds = shell.getBounds ();
		if (bounds.width <= 0 || bounds.height <= 0) {
			throw new IllegalArgumentException ("shell has no drawable area");
		}

		Display display = control.getDisplay ();
		Image image = new Image (display, bounds.width, bounds.height);
		try {
			GC displayGc = new GC (display);
			try {
				displayGc.copyArea (image, bounds.x, bounds.y);
			} finally {
				displayGc.dispose ();
			}
			savePng (image, path);
		} finally {
			image.dispose ();
		}
		return new Result (path, Method.DISPLAY_COPY_AREA, sha256 (path));
	}

	/** Visible outer bounds in display coordinates; reject partially clipped targets. */
	static Rectangle screenBounds (Control control) {
		Objects.requireNonNull (control, "control");
		if (control.isDisposed () || !control.isVisible ()) {
			throw new IllegalArgumentException ("screen capture requires a visible control");
		}
		Display display = control.getDisplay ();
		if (Display.getCurrent () != display) {
			throw new IllegalStateException ("screen capture requires the SWT UI thread");
		}
		Rectangle screen = outerDisplayBounds (control);
		if (screen.width <= 0 || screen.height <= 0 || !screen.equals (screen.intersection (display.getBounds ()))) {
			throw new IllegalArgumentException ("screen capture must be fully on the display");
		}
		for (Composite ancestor = control instanceof Shell ? null : control.getParent ();
				ancestor != null; ancestor = ancestor.getParent ()) {
			Rectangle client = ancestor.getClientArea ();
			Point origin = clientToDisplay (ancestor, client.x, client.y);
			Rectangle visible = new Rectangle (origin.x, origin.y, client.width, client.height);
			if (!screen.equals (screen.intersection (visible))) {
				throw new IllegalArgumentException ("screen capture is clipped by "
						+ ancestor.getClass ().getSimpleName () + ": " + screen + " outside " + visible);
			}
		}
		return screen;
	}

	private static Rectangle outerDisplayBounds (Control control) {
		Rectangle bounds = control.getBounds ();
		Point origin = control instanceof Shell ? new Point (bounds.x, bounds.y)
				: clientToDisplay (control.getParent (), bounds.x, bounds.y);
		return new Rectangle (origin.x, origin.y, bounds.width, bounds.height);
	}

	private static Point clientToDisplay (Composite parent, int x, int y) {
		Point origin = parent.toDisplay (x, y);
		// GTK3 TabFolder normalizes client x/y although its event window includes tab trim.
		if (parent instanceof TabFolder && "gtk".equals (SWT.getPlatform ())
				&& !"1".equals (System.getenv ("SWT_GTK4"))) {
			Rectangle client = parent.getClientArea ();
			Rectangle trim = parent.computeTrim (0, 0, client.width, client.height);
			origin.x -= trim.x;
			origin.y -= trim.y;
		}
		return origin;
	}

	/** Capture actual visible pixels; there is deliberately no offscreen rendering fallback. */
	static Result captureScreenControl (Control control, Path path) throws IOException {
		Objects.requireNonNull (path, "path");
		Rectangle bounds = screenBounds (control);
		Display display = control.getDisplay ();
		Image image = new Image (display, bounds.width, bounds.height);
		try {
			GC displayGc = new GC (display);
			try { displayGc.copyArea (image, bounds.x, bounds.y); }
			finally { displayGc.dispose (); }
			savePng (image, path);
		} finally { image.dispose (); }
		return new Result (path, Method.DISPLAY_COPY_AREA, sha256 (path));
	}

	private static void savePng (Image image, Path path) throws IOException {
		Path parent = path.getParent ();
		if (parent != null) Files.createDirectories (parent);
		ImageLoader loader = new ImageLoader ();
		loader.data = new ImageData[] {image.getImageData ()};
		loader.save (path.toString (), SWT.IMAGE_PNG);
	}

	private static String sha256 (Path path) throws IOException {
		try {
			return HexFormat.of ().formatHex (
					MessageDigest.getInstance ("SHA-256").digest (Files.readAllBytes (path)));
		} catch (NoSuchAlgorithmException impossible) {
			throw new AssertionError (impossible);
		}
	}
}

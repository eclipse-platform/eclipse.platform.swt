/*******************************************************************************
 * Copyright 2026 Hitesh Soliwal and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/
package org.eclipse.swt.awt;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.BitSet;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

/**
 * Display-thread-owned Java2D raster and pixel validity for one visible surface.
 * Geometry and scroll origin are supplied by the existing SWT viewport owners.
 * No native SWT resource is retained here.
 */
final class Java2DSurface implements AutoCloseable {

	private BufferedImage image;
	private BitSet valid = new BitSet ();
	private AffineTransform transform = new AffineTransform ();
	private int backgroundArgb;
	private long generation;

	int width () {
		return image == null ? 0 : image.getWidth ();
	}

	int height () {
		return image == null ? 0 : image.getHeight ();
	}

	void configure (int width, int height, AffineTransform nextTransform, int nextBackgroundArgb) {
		if (width < 0 || height < 0) throw new IllegalArgumentException ("negative raster size");
		Objects.requireNonNull (nextTransform, "transform");
		if (!Double.isFinite (nextTransform.getScaleX ()) || !Double.isFinite (nextTransform.getScaleY ())
				|| !Double.isFinite (nextTransform.getShearX ()) || !Double.isFinite (nextTransform.getShearY ())
				|| !Double.isFinite (nextTransform.getTranslateX ()) || !Double.isFinite (nextTransform.getTranslateY ())) {
			throw new IllegalArgumentException ("non-finite transform");
		}
		if (width == 0 || height == 0) {
			width = height = 0;
		}
		if ((long)width * height > Integer.MAX_VALUE) {
			throw new IllegalArgumentException ("raster exceeds Java array capacity");
		}
		if (width != width () || height != height ()) {
			BufferedImage next = width == 0 ? null
					: new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
			BitSet nextValid;
			try {
				nextValid = new BitSet (width * height);
			} catch (RuntimeException | Error failure) {
				if (next != null) next.flush ();
				throw failure;
			}
			BufferedImage previous = image;
			image = next;
			valid = nextValid;
			generation++;
			if (previous != null) previous.flush ();
		}
		if (!transform.equals (nextTransform) || backgroundArgb != nextBackgroundArgb) {
			transform = new AffineTransform (nextTransform);
			backgroundArgb = nextBackgroundArgb;
			invalidate ();
		}
	}

	void invalidate () {
		valid.clear ();
		generation++;
	}

	void invalidate (int x, int y, int width, int height) {
		Rectangle bounds = clip (x, y, width, height);
		if (bounds.isEmpty ()) return;
		setValid (bounds, false);
		generation++;
	}

	/**
	 * Renders only the missing pixels within the requested exposure. Missing
	 * spans are enclosed by one rectangle, so callbacks may repaint some valid
	 * pixels inside that rectangle. Transforms other than integer translations
	 * regenerate the whole visible raster: shifting a Java2D device origin can
	 * otherwise change affine edge coverage through floating-point rounding.
	 * A failed or invalidated callback commits no validity and will be retried
	 * by a later paint request.
	 *
	 * @return whether the clipped exposure is valid for presentation, including
	 *         a cache hit; {@code false} after reentrant invalidation
	 */
	boolean render (int x, int y, int width, int height, Consumer<Graphics2D> renderer) {
		Rectangle requested = clip (x, y, width, height);
		Rectangle dirty = invalidBounds (requested);
		if (dirty.isEmpty ()) return true;
		if (!isIntegerTranslation ()) dirty = new Rectangle (0, 0, width (), height ());
		setValid (dirty, false);
		BufferedImage current = image;
		long renderingGeneration = generation;
		/* A subimage supplies a device clip that even setClip(null) cannot widen. */
		BufferedImage slice = current.getSubimage (dirty.x, dirty.y, dirty.width, dirty.height);
		Graphics2D graphics = slice.createGraphics ();
		try {
			Color background = new Color (backgroundArgb, true);
			graphics.setBackground (background);
			graphics.setComposite (AlphaComposite.Src);
			graphics.setColor (background);
			graphics.fillRect (0, 0, dirty.width, dirty.height);
			graphics.setComposite (AlphaComposite.SrcOver);
			graphics.setColor (Color.BLACK);
			graphics.translate (-dirty.x, -dirty.y);
			graphics.setClip (dirty);
			graphics.transform (transform);
			if (renderer != null) renderer.accept (graphics);
		} finally {
			graphics.dispose ();
			slice.flush ();
		}
		if (image != current || generation != renderingGeneration) return false;
		setValid (dirty, true);
		return true;
	}

	private boolean isIntegerTranslation () {
		return transform.getScaleX () == 1 && transform.getScaleY () == 1
				&& transform.getShearX () == 0 && transform.getShearY () == 0
				&& transform.getTranslateX () == Math.rint (transform.getTranslateX ())
				&& transform.getTranslateY () == Math.rint (transform.getTranslateY ());
	}

	ImageData copyImageData (int x, int y, int width, int height) {
		Rectangle bounds = clip (x, y, width, height);
		if (bounds.isEmpty ()) throw new IllegalArgumentException ("empty raster region");
		if (!invalidBounds (bounds).isEmpty ()) {
			throw new IllegalStateException ("raster region has not been rendered");
		}
		return toImageData (image, bounds.x, bounds.y, bounds.width, bounds.height);
	}

	private Rectangle clip (int x, int y, int width, int height) {
		if (image == null || width <= 0 || height <= 0) return new Rectangle ();
		long left = Math.max (0L, x);
		long top = Math.max (0L, y);
		long right = Math.min (width (), (long)x + width);
		long bottom = Math.min (height (), (long)y + height);
		if (right <= left || bottom <= top) return new Rectangle ();
		return new Rectangle ((int)left, (int)top, (int)(right - left), (int)(bottom - top));
	}

	private Rectangle invalidBounds (Rectangle bounds) {
		if (bounds.isEmpty ()) return new Rectangle ();
		int rasterWidth = width ();
		int left = bounds.x + bounds.width;
		int top = bounds.y + bounds.height;
		int right = -1, bottom = -1;
		for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
			int row = y * rasterWidth;
			int end = row + bounds.x + bounds.width;
			int first = valid.nextClearBit (row + bounds.x);
			if (first >= end) continue;
			left = Math.min (left, first - row);
			right = Math.max (right, valid.previousClearBit (end - 1) - row + 1);
			top = Math.min (top, y);
			bottom = y + 1;
		}
		return right < 0 ? new Rectangle () : new Rectangle (left, top, right - left, bottom - top);
	}

	private void setValid (Rectangle bounds, boolean value) {
		int rasterWidth = width ();
		for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
			int start = y * rasterWidth + bounds.x;
			valid.set (start, start + bounds.width, value);
		}
	}

	/**
	 * Copies only the requested raster rectangle. Straight ARGB pixels use their
	 * original data array, raster translation and scanline stride; other image
	 * formats use Java2D's normalized getRGB row conversion. The SWT bulk row APIs
	 * retain their authority over the destination RGB and alpha layouts.
	 */
	static ImageData toImageData (BufferedImage source, int x, int y, int width, int height) {
		Objects.requireNonNull (source, "source");
		if (x < 0 || y < 0 || width <= 0 || height <= 0
				|| (long)x + width > source.getWidth () || (long)y + height > source.getHeight ()) {
			throw new IllegalArgumentException ("region outside source raster");
		}
		ImageData result = new ImageData (width, height, 24,
				new PaletteData (0xFF0000, 0x00FF00, 0x0000FF));
		byte [] alpha = new byte [width];
		Raster raster = source.getRaster ();
		int [] pixels = null;
		int offset = 0, stride = width;
		if (!source.isAlphaPremultiplied ()
				&& source.getColorModel () instanceof DirectColorModel colors
				&& colors.getColorSpace ().isCS_sRGB ()
				&& colors.getRedMask () == 0xFF0000 && colors.getGreenMask () == 0x00FF00
				&& colors.getBlueMask () == 0x0000FF && colors.getAlphaMask () == 0xFF000000
				&& raster.getDataBuffer () instanceof DataBufferInt data && data.getNumBanks () == 1
				&& raster.getSampleModel () instanceof SinglePixelPackedSampleModel packed) {
			pixels = data.getData ();
			stride = packed.getScanlineStride ();
			offset = data.getOffset () + packed.getOffset (
					x - raster.getSampleModelTranslateX (), y - raster.getSampleModelTranslateY ());
		}
		boolean direct = pixels != null;
		if (!direct) pixels = new int [width];
		for (int row = 0; row < height; row++) {
			int start = direct ? offset + row * stride : 0;
			if (!direct) source.getRGB (x, y + row, width, 1, pixels, 0, width);
			result.setPixels (0, row, width, pixels, start);
			for (int column = 0; column < width; column++) {
				alpha [column] = (byte)(pixels [start + column] >>> 24);
			}
			result.setAlphas (0, row, width, alpha, 0);
		}
		return result;
	}

	/** Releases raster capacity; a later configure may allocate a new surface. */
	@Override
	public void close () {
		BufferedImage previous = image;
		image = null;
		valid = new BitSet ();
		generation++;
		if (previous != null) previous.flush ();
	}
}

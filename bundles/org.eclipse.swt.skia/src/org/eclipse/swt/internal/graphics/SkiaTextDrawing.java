/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.canvasext.FontProperties;
import org.eclipse.swt.internal.canvasext.Logger;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.skija.FontEdging;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.PaintStrokeCap;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.types.Rect;

public class SkiaTextDrawing {
	// only for debug purpose use text drawing without cache.
	public static boolean USE_TEXT_CACHE = true;

	public static void drawText(SkiaGC gc, String[] splits, int flags, int x, int y) {

		if (USE_TEXT_CACHE) {
			drawTextBlobWithCache(gc, splits, flags, x, y);
			return;
		}

		drawTextBlobNoCache(gc, splits, flags, x, y);

	}

	private static boolean isTransparent(int flags) {
		return (SWT.DRAW_TRANSPARENT & flags) != 0;
	}

	private static void drawTextBlobNoCache(SkiaGC gc, String[] splits, int flags, int x, int y) {

		final var paintManager = gc.getPaintManager();
		final var resources = gc.getSkiaResources();
		final var surface = gc.getSkiaSurface();

		final var scaler = new DpiScalerUtil(resources.getScaler());

		final boolean transparent = isTransparent(flags);
		final boolean antiAlias = gc.getAntialias() != SWT.OFF || gc.getTextAntialias() != SWT.OFF;
		final int[] yPos = new int[1];
		yPos[0] = y;
		final var f = gc.getSkiaResources().getSkiaFont();

		paintManager.performDraw(fgp -> {
			fgp.setAntiAlias(antiAlias);
			fgp.setMode(PaintMode.FILL);
			f.setSubpixel(antiAlias);
			f.setEdging(antiAlias ? FontEdging.SUBPIXEL_ANTI_ALIAS : FontEdging.ALIAS);

			fgp.setStrokeWidth(1);
			fgp.setStrokeCap(PaintStrokeCap.BUTT);
			fgp.setPathEffect(null);
			for (final var text : splits) {

				final var metric = f.getMetrics();
				final var asc = metric.getAscent();
				final var des = metric.getDescent();
				final var leading = metric.getLeading();

				final var ascI = (int) Math.ceil(Math.abs(asc));
				final var desI = (int) Math.ceil(Math.abs(des));
				final var heightI = ascI + desI;
				final var r = scaler.scaleSize(x, yPos[0]);

				if (!transparent) {

					// draw rectangle background for the text.

					// Skia draws a text with a slightly right shift. So textExtent is insufficient
					// in the width to make sure, the background area is big enough
					// So make the area a little bit wider.

					// The background rectangle can be a little bit smaller, and in the worst
					// case a small part of the text is not covered.
					// Similar in windows. Actually for some fonts and sizes this solution is
					// already better than windows.

					// heuristic number. After 0.12 of the ascent, the text is usually sufficiently
					// in the rectangle area.
					final double endOfRectangle = Math.abs(asc) * 0.12;
					final var rect = f.measureText(text, fgp);

					final Point size = new Point((int) Math.ceil(rect.getWidth() + endOfRectangle),
							(int) Math.ceil(heightI + leading));

					paintManager.performDrawFilled(paint -> {
						surface.getCanvas().drawRect(new Rect(r.x, r.y, r.x + size.x, r.y + size.y), paint);
					});

				}
				surface.getCanvas().drawString(text, r.x, r.y + ascI, f, fgp);
				yPos[0] += heightI + leading;
			}
		});

	}

	private static void drawTextBlobWithCache(SkiaGC gc, String[] splits, int flags, int xIn, int yIn) {

		final var paintManager = gc.getPaintManager();
		final var resources = gc.getSkiaResources();
		final var surface = gc.getSkiaSurface();
		final var scaler = new DpiScalerUtil(resources.getScaler());


		final boolean transparent = isTransparent(flags);

		final int x = scaler.autoScaleUp(xIn);
		final int y = scaler.autoScaleUp(yIn);

		final var f = resources.getSkiaFont();
		final FontProperties props = FontProperties.getFontProperties(gc.getFont());
		final int backgroundColor = gc.getAlpha() < 255
				? SkiaColorConverter.convertSWTColorToSkijaColor(gc.getBackground(), gc.getAlpha())
						: SkiaColorConverter.convertSWTColorToSkijaColor(gc.getBackground());
		final int foregroundColor = SkiaColorConverter.convertSWTColorToSkijaColor(gc.getForeground(), gc.getAlpha());
		final boolean antiAlias = gc.getAntialias() != SWT.OFF || gc.getTextAntialias() != SWT.OFF;

		final int[] yPos = new int[1];
		yPos[0] = y;
		for (final var text : splits) {

			final var cachedImage = resources.getTextImage(text, props, transparent, backgroundColor, foregroundColor,
					antiAlias);
			if (cachedImage != null && !cachedImage.isClosed()) {
				surface.getCanvas().drawImage(cachedImage, x, yPos[0]);
				yPos[0] += Math.ceil(cachedImage.getHeight());
				continue;
			}
			paintManager.performDraw(fgp -> {

				fgp.setAntiAlias(antiAlias);
				fgp.setMode(PaintMode.FILL);
				f.setSubpixel(antiAlias);
				f.setEdging(antiAlias ? FontEdging.SUBPIXEL_ANTI_ALIAS : FontEdging.ALIAS);

				fgp.setStrokeWidth(1);
				fgp.setStrokeCap(PaintStrokeCap.BUTT);
				fgp.setPathEffect(null);
				fgp.setBlendMode(BlendMode.SRC_IN);

				final var rect = f.measureText(text, fgp);
				final var metric = f.getMetrics();
				final var asc = metric.getAscent();
				final var des = metric.getDescent();
				final var leading = metric.getLeading();

				final var ascI = (int) Math.ceil(Math.abs(asc));
				final var desI = (int) Math.ceil(Math.abs(des));
				final var heightI = ascI + desI;

				// skija draws a text with a slightly right shift. So textExtent is insufficient
				// in
				// the width. So make the background area a little bit wider.

				// Idea:
				// 1. the support surface should always be wide enough to contain the complete
				// text.
				// 2. the background rectangle can be a little bit smaller, and in the worst
				// case a small part of the text is not covered.
				// Similar in windows. Actually for some fonts and sizes this solution is
				// already better than windows.

				final double additionalArea = Math.abs(asc) * 2;

				final Point size = new Point((int) Math.ceil(rect.getWidth() + additionalArea),
						(int) Math.ceil(heightI + leading));

				final int MAX_SURFACE_WIDTH = 8192; // Documented practical limit
				int surfaceWidth = size.x;
				if (surfaceWidth > MAX_SURFACE_WIDTH) {
					Logger.logException(new IllegalStateException("Surface width restricted: calculated=" + surfaceWidth //$NON-NLS-1$
							+ ", max=" + MAX_SURFACE_WIDTH + ", font=" + props.name + ", size=" + size //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ ", backgroundColor=" + backgroundColor + ", foregroundColor=" + foregroundColor  //$NON-NLS-1$//$NON-NLS-2$
							+ ", antiAlias=" + antiAlias + ", transparent=" + transparent + ", text='" + text + "'"));   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
					surfaceWidth = MAX_SURFACE_WIDTH;
				}
				if (surfaceWidth <= 0 || size.y <= 0) {
					final StringBuilder sb = new StringBuilder();
					sb.append("Calculated text image size is invalid. font=").append(props.name).append(", size=") //$NON-NLS-1$ //$NON-NLS-2$
					.append(size).append(", text='").append(text).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
					Logger.logException(new IllegalStateException(sb.toString()));
					return;
				}
				try (Surface supportSurface = gc.getSkiaExtension().createSupportSurface(surfaceWidth, size.y)) {

					supportSurface.getCanvas().clear(0);
					if (!transparent) {

						// heuristic number. After 0.12 of the ascent, the text is usually sufficiently
						// in the rectangle area.
						final double endOfRectangle = Math.abs(asc) * 0.12;
						// always clear the support surface, then fill a specific rectangle area with
						// the background color, the rest stays transparent.
						try (Paint p = new Paint()) {
							p.setColor(SkiaColorConverter.convertSWTColorToSkijaColor(gc.getBackground(), gc.getAlpha()));
							p.setMode(PaintMode.FILL);
							supportSurface.getCanvas().drawRect(
									new Rect(0, 0, (int) Math.ceil(rect.getWidth() + endOfRectangle), size.y), p);
						}

					} else {
						// very important at text on transparent background. Blend over the source,
						// otherwise the text won't be visible.
						fgp.setBlendMode(BlendMode.SRC_OVER);
					}

					supportSurface.getCanvas().drawString(text, 0, ascI, f, fgp);
					final var image = supportSurface.makeImageSnapshot();
					resources.cacheTextImage(text, props, transparent, backgroundColor, foregroundColor, antiAlias,
							image);
					surface.getCanvas().drawImage(image, x, yPos[0]);
					yPos[0] += Math.ceil(image.getHeight());

				}

			});
		}

	}

}

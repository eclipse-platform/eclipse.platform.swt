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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.FontMetricsExtension;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.internal.canvasext.IExternalGC;
import org.eclipse.swt.internal.canvasext.Logger;
import org.eclipse.swt.internal.skia.DpiScalerUtil;
import org.eclipse.swt.internal.skia.ISkiaCanvasExtension;
import org.eclipse.swt.internal.skia.SkiaResources;
import org.eclipse.swt.widgets.Display;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.EncoderPNG;
import io.github.humbleui.skija.FilterMipmap;
import io.github.humbleui.skija.FilterMode;
import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.GradientStyle;
import io.github.humbleui.skija.Matrix33;
import io.github.humbleui.skija.MipmapMode;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.PathEffect;
import io.github.humbleui.skija.PathFillMode;
import io.github.humbleui.skija.SamplingMode;
import io.github.humbleui.skija.Shader;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.types.Rect;

public class SkiaGC implements IExternalGC {

	private static boolean logImageNullError = true;
	private final Surface surface;
	private int alpha = 255;
	private int antialias;
	private boolean xorModeActive;
	private int lineWidth = 1;
	private int lineCap = SWT.CAP_FLAT;
	private int lineStyle;
	private Pattern foregroundPattern;
	private Pattern backgroundPattern;

	private int fillRule = SWT.FILL_EVEN_ODD;
	private int lineJoin = SWT.JOIN_MITER;
	private float[] lineDashes;
	// TODO: implement -----------------------------------
	private float dashOffset;
	private float miterLimit;
	// -----------------------------------

	private final org.eclipse.swt.widgets.Canvas canvas;
	private final Display device;

	private Matrix33 currentTransform = Matrix33.IDENTITY;

	private SamplingMode interpolationMode = SamplingMode.DEFAULT;

	private boolean isClipSet;
	private Rectangle currentClipBounds;
	private Region currentClipRegion;

	private final ISkiaCanvasExtension skiaExtension;
	private final SkiaResources resources;
	private final int style;
	private int textAntiAlias = SWT.ON;

	private final int initialSaveCount;
	private final SkiaPaintManager paintManager;
	private final DpiScalerUtil dpiScalerUtil;

	public SkiaGC(org.eclipse.swt.widgets.Canvas canvas, ISkiaCanvasExtension exst, int style) {
		this.canvas = canvas;
		device = canvas.getDisplay();
		this.surface = exst.getSurface();
		this.skiaExtension = exst;
		this.resources = skiaExtension.getResources();
		this.style = style;
		// Save the initial canvas state so it can be fully restored on dispose()
		this.initialSaveCount = this.surface.getCanvas().save();
		this.paintManager = new SkiaPaintManager(this);

		this.dpiScalerUtil = new DpiScalerUtil(this.resources.getScaler());

	}

	@Override
	public void dispose() {

		resources.resetBaseColors();
		// Restore all canvas state changes made during the lifetime of this GC,
		// including any clipping or transform layers pushed after construction
		surface.getCanvas().restoreToCount(initialSaveCount);

	}

	@Override
	public Point textExtent(String string) {
		return textExtent(string, SWT.NONE);
	}

	@Override
	public void setBackground(Color color) {
		this.resources.setBackground(color);
	}

	@Override
	public void setForeground(Color color) {
		this.resources.setForeground(color);
	}

	@Override
	public void fillRectangle(Rectangle rect) {
		fillRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	private static void logImageNull(int[] positionData) {
		if (logImageNullError) {
			Logger.logException(new IllegalArgumentException(
					"Image argument is null. Position and size data: " + java.util.Arrays.toString(positionData))); //$NON-NLS-1$
			logImageNullError = false;
		}
	}

	@Override
	public void drawImage(Image image, int x, int y) {

		if (image == null) {
			logImageNull(new int[] { x, y });
			return;
		}

		final var imgBounds = image.getBounds();
		drawImage(image, 0, 0, imgBounds.width, imgBounds.height, x, y, imgBounds.width, imgBounds.height);
	}

	@Override
	public void drawImage(Image image, int destX, int destY, int destWidth, int destHeight) {

		if (image == null) {
			logImageNull(new int[] { destX, destY, destWidth, destHeight });
			return;
		}

		drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, destX, destY, destWidth, destHeight);

	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {

		if (image == null) {
			logImageNull(new int[] { srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight });
			return;
		}

		int sizeFactorInImage = Math.round(Math.max((float) destWidth / srcWidth, (float) destHeight / srcHeight));

		if (sizeFactorInImage == 0) {
			sizeFactorInImage = 1;
		}
		final Canvas canvas = surface.getCanvas();
		final int fac = sizeFactorInImage;

		paintManager.performDraw(paint -> {
			paint.setAlpha(alpha);
			paint.setAntiAlias(true);
			canvas.drawImageRect(
					SwtToSkiaImageConverter.convertSWTImageToSkijaImage(image, getScaler().autoScaleUp(100 * fac),
							resources),
					RectangleConverter.createScaledRectangle(getScaler(), srcX * fac, srcY * fac, srcWidth * fac,
							srcHeight * fac),
					RectangleConverter.createScaledRectangle(getScaler(), destX, destY, destWidth, destHeight),
					this.interpolationMode, paint, false);

		});

	}

	DpiScalerUtil getScaler() {
		return this.dpiScalerUtil;
	}

	/**
	 * Test method for drawing an image.
	 *
	 * @param str
	 * @param image
	 */
	static void writeFile(String str, io.github.humbleui.skija.Image image) {
		final byte[] imageBytes = EncoderPNG.encode(image).getBytes();

		final File f = new File(str);
		if (f.exists()) {
			f.delete();
		}

		try (FileOutputStream fis = new FileOutputStream(f)) {
			fis.write(imageBytes);
		} catch (final Exception e) {
			Logger.logException(e);
		}
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		final float scaledOffsetValue = RectangleConverter.getScaledOffsetValue(getScaler(), lineWidth);

		final var scaler = this.dpiScalerUtil;
		paintManager.performDraw(paint -> surface.getCanvas().drawLine(scaler.autoScaleUp(x1) + scaledOffsetValue,
				scaler.autoScaleUp(y1) + scaledOffsetValue, scaler.autoScaleUp(x2) + scaledOffsetValue,
				scaler.autoScaleUp(y2) + scaledOffsetValue, paint));
	}

	@Override
	public Color getForeground() {
		return this.resources.getForeground();
	}

	@Override
	public void drawText(String string, int x, int y) {
		drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
		if (isTransparent) {
			flags |= SWT.DRAW_TRANSPARENT;
		}
		drawText(string, x, y, flags);
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {
		if (text == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		executeTextDraw(text, flags, x, y);
	}

	private io.github.humbleui.skija.Font getSkiaFont() {
		return this.resources.getSkiaFont();
	}

	private void executeTextDraw(String inputText, int flags, int x, int y) {

		if (this.surface.getWidth() < x || this.surface.getHeight() < y) {
			return;
		}

		final String splits[] = this.resources.getTextSplits(inputText, flags);
		SkiaTextDrawing.drawText(this, splits, flags, x, y);
	}

	SkiaPaintManager getPaintManager() {
		return this.paintManager;
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		paintManager.performDraw(paint -> surface.getCanvas().drawArc(getScaler().autoScaleUp(x),
				getScaler().autoScaleUp(y), getScaler().autoScaleUp(x + width), getScaler().autoScaleUp(y + height),
				-startAngle, -arcAngle, false, paint));
	}

	@Override
	public void drawFocus(int x, int y, int width, int height) {
		paintManager.performDraw(paint -> {
			final var scaledLineWidth = getScaler().autoScaleUp(lineWidth * 1F);

			try (var pe = PathEffect.makeDash(new float[] { 1.5f * scaledLineWidth, 1.5f * scaledLineWidth }, 0.0f)) {
				paint.setPathEffect(pe);
				surface.getCanvas().drawRect(
						RectangleConverter.createScaledRectangleWithOffset(getScaler(), 0, x, y, width, height), paint);
			}

		});
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		paintManager.performDraw(paint -> surface.getCanvas().drawOval(
				RectangleConverter.createScaledRectangleWithOffset(getScaler(), lineWidth, x, y, width, height),
				paint));
	}

	@Override
	public void drawPath(Path path) {
		paintManager.performDraw(paint -> {
			try (io.github.humbleui.skija.Path skijaPath = SkiaPathConverter.convertSWTPathToSkijaPath(path,
					getScaler())) {
				if (skijaPath == null) {
					return;
				}
				// the stroke miter on GC is quite high.
				paint.setStrokeMiter(1000);
				surface.getCanvas().drawPath(skijaPath, paint);
			}
		});
	}

	@Override
	public void drawPoint(int x, int y) {
		paintManager.performDrawFilled(paint -> {
			// points have always one pixel no matter the zoom...
			// fill mode is right for it, not stroke, so we use performDrawFilled
			paint.setColor(SkiaColorConverter.convertSWTColorToSkijaColor(getForeground()));
			final var rec = RectangleConverter.scaleUpRectangle(getScaler(), new Rectangle(x, y, 1, 1));
			final var r = new Rect(rec.x, rec.y, rec.x + 1, rec.y + 1);
			surface.getCanvas().drawRect(r, paint);
		});
	}

	@Override
	public void drawPolygon(int[] inputPointArray) {

		final int[] pointArray = Arrays.copyOf(inputPointArray, inputPointArray.length);

		if (pointArray == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (pointArray.length < 6 || pointArray.length % 2 != 0) {
			return;
		}

		// Handle SWT.MIRRORED style (adjust x-coordinates if needed)
		final int style = getStyle();
		final boolean mirrored = (style & SWT.MIRRORED) != 0;
		final boolean adjustX = mirrored && lineWidth != 0 && lineWidth % 2 == 0;
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]--;
			}
		}

		paintManager.performDraw(paint -> {
			// Create Skija path for the polygon
			try (io.github.humbleui.skija.PathBuilder pathBuilder = new io.github.humbleui.skija.PathBuilder()) {
				// Move to first point
				pathBuilder.moveTo(getScaler().autoScaleUp(pointArray[0]), getScaler().autoScaleUp(pointArray[1]));
				// Add lines to subsequent points
				for (int i = 2; i < pointArray.length; i += 2) {
					pathBuilder.lineTo(getScaler().autoScaleUp(pointArray[i]),
							getScaler().autoScaleUp(pointArray[i + 1]));
				}
				pathBuilder.closePath();
				// Draw the polygon outline

				try (var path = pathBuilder.build()) {
					if (path.isEmpty()) {
						return;
					}
					surface.getCanvas().drawPath(path, paint);
				}
			}
		});
		// Restore x-coordinates if mirrored
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]++;
			}
		}
	}

	@Override
	public void drawPolyline(int[] pointArray) {
		paintManager.performDraw(paint -> surface.getCanvas().drawPolygon(convertToFloat(pointArray), paint));
	}

	private float[] convertToFloat(int[] array) {
		final float[] arrayAsFloat = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			arrayAsFloat[i] = getScaler().autoScaleUp(array[i]);
		}
		return arrayAsFloat;
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {

		paintManager.performDraw(paint -> {
			surface.getCanvas().drawRect(RectangleConverter.createScaledRectangleWithOffset(dpiScalerUtil, lineWidth, x, y, width, height),  paint);
		});

	}

	@Override
	public void drawRectangle(Rectangle rect) {
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		paintManager.performDraw(paint -> surface.getCanvas().drawRRect(
				RectangleConverter.offsetRectangle(getScaler(),lineWidth,RectangleConverter.createScaledRoundRectangle(getScaler(),x, y, width, height, arcWidth / 2.0f, arcHeight / 2.0f)),
				paint));
	}

	@Override
	public void drawString(String string, int x, int y) {
		drawString(string, x, y, false);
	}

	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		drawText(string, x, y, isTransparent);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		paintManager.performDrawFilled(paint -> surface.getCanvas().drawArc(getScaler().autoScaleUp(x),
				getScaler().autoScaleUp(y), getScaler().autoScaleUp(x + width), getScaler().autoScaleUp(y + height),
				-startAngle, -arcAngle, true, paint));
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {

		boolean swapColors = false;
		if (width < 0) {
			x = Math.max(0, x + width);
			width = -width;
			if (!vertical) {
				swapColors = true;
			}
		}
		if (height < 0) {
			y = Math.max(0, y + height);
			height = -height;
			if (vertical) {
				swapColors = true;
			}
		}
		final int x2 = vertical ? x : x + width;
		final int y2 = vertical ? y + height : y;

		final Rect rect = RectangleConverter.createScaledRectangle(getScaler(),x, y, width, height);
		int fromColor = SkiaColorConverter.convertSWTColorToSkijaColor(getForeground());
		int toColor = SkiaColorConverter.convertSWTColorToSkijaColor(getBackground());
		if (fromColor == toColor) {
			paintManager.performDrawFilled(paint -> surface.getCanvas().drawRect(rect, paint));
			return;
		}
		if (swapColors) {
			final int tempColor = fromColor;
			fromColor = toColor;
			toColor = tempColor;
		}

		final var s = getScaler();
		performDrawGradientFilled(paint -> surface.getCanvas().drawRect(rect, paint), s.autoScaleUp(x),
				s.autoScaleUp(y), s.autoScaleUp(x2), s.autoScaleUp(y2), fromColor, toColor);
	}

	private void performDrawGradientFilled(Consumer<Paint> operations, int x, int y, int x2, int y2, int fromColor,
			int toColor) {
		paintManager.performDraw(paint -> {

			try (Shader gradient = convertGradientRectangleToSkijaShader(x, y, x2 - x, y2 - y, false, fromColor,
					toColor)) {
				paint.setShader(gradient);
				paint.setAntiAlias(true);
				paint.setMode(PaintMode.FILL);
				operations.accept(paint);
			}
		});
	}

	private static Shader convertGradientRectangleToSkijaShader(int x, int y, int width, int height, boolean vertical,
			int fromColor, int toColor) {

		final var gs = new GradientStyle(FilterTileMode.REPEAT, true, null);
		final Shader s = Shader.makeLinearGradient(x, y, x + width, y + height, new int[] { fromColor, toColor }, null,
				gs);

		return s;
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		paintManager.performDrawFilled(
				paint -> surface.getCanvas().drawOval(RectangleConverter.createScaledRectangle(getScaler(),x, y, width, height), paint));
	}

	@Override
	public void fillPath(Path path) {
		paintManager.performDrawFilled(paint -> {
			try (io.github.humbleui.skija.Path skijaPath = SkiaPathConverter.convertSWTPathToSkijaPath(path,
					getScaler())) {
				if (skijaPath == null) {
					return;
				}
				skijaPath
				.setFillMode(this.fillRule == SWT.FILL_EVEN_ODD ? PathFillMode.EVEN_ODD : PathFillMode.WINDING);
				paint.setAntiAlias(this.antialias == SWT.ON);
				surface.getCanvas().drawPath(skijaPath, paint);
			}
		});
	}

	@Override
	public void fillPolygon(int[] pointArray) {
		if (pointArray == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (pointArray.length < 6 || pointArray.length % 2 != 0) {
			return;
		}

		paintManager.performDrawFilled(paint -> {

			try (var path = SkiaPathConverter.createPath(pointArray, fillRule, getScaler())) {
				if (path == null || path.isEmpty()) {
					return;
				}
				surface.getCanvas().drawPath(path, paint);
			}

		});
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		paintManager.performDrawFilled(
				paint -> surface.getCanvas().drawRect(RectangleConverter.createScaledRectangle(getScaler(),x, y, width, height), paint));
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		paintManager.performDrawFilled(paint -> surface.getCanvas()
				.drawRRect(RectangleConverter.createScaledRoundRectangle(getScaler(),x, y, width, height, arcWidth / 2.0f, arcHeight / 2.0f), paint));
	}

	@Override
	public Point textExtent(String text, int flags) {
		return getScaler().scaleDown(resources.textExtent(text, flags));
	}

	@Override
	public void setFont(org.eclipse.swt.graphics.Font font) {
		this.resources.setFont(font);
	}

	@Override
	public void setClipping(int x, int y, int width, int height) {
		setClipping(new Rectangle(x, y, width, height));
	}

	@Override
	public void setTransform(Transform transform) {

		final var sc = getScaler();
		this.currentTransform = SkiaTransformConverter.toSkiaMatrix(transform, sc);
		surface.getCanvas().setMatrix(this.currentTransform);
		// Save the canvas state after applying the new transform, so subsequent
		// operations (e.g. clipping) can be stacked and later restored independently
		surface.getCanvas().save();

	}

	@Override
	public void setAlpha(int alpha) {
		alpha = alpha & 0xFF;
		this.alpha = alpha;
	}

	@Override
	public int getAlpha() {
		return this.alpha;
	}

	@Override
	public void setLineWidth(int i) {
		this.lineWidth = i;
	}

	@Override
	public int getAntialias() {
		return antialias;
	}

	@Override
	public void setAntialias(int antialias) {
		if (antialias != SWT.DEFAULT && antialias != SWT.ON && antialias != SWT.OFF) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.antialias = antialias;
	}

	@Override
	public void setAdvanced(boolean enable) {
		// Nothing to do...
	}

	@Override
	public void setLineStyle(int lineStyle) {
		if (lineStyle != SWT.LINE_SOLID && lineStyle != SWT.LINE_DASH && lineStyle != SWT.LINE_DOT
				&& lineStyle != SWT.LINE_DASHDOT && lineStyle != SWT.LINE_DASHDOTDOT && lineStyle != SWT.LINE_CUSTOM) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.lineStyle = lineStyle;
	}

	@Override
	public int getLineStyle() {
		return lineStyle;
	}

	@Override
	public int getLineWidth() {
		return lineWidth;
	}

	@Override
	public LineAttributes getLineAttributes() {
		final LineAttributes attributes = getLineAttributesInPixels();
		attributes.width = getScaler().autoScaleDown(attributes.width);
		if (attributes.dash != null) {
			attributes.dash = getScaler().autoScaleDown(attributes.dash);
		}
		return attributes;
	}

	LineAttributes getLineAttributesInPixels() {
		return new LineAttributes(lineWidth, lineCap, lineJoin, lineStyle, lineDashes, dashOffset, miterLimit);
	}

	@Override
	public Rectangle getClipping() {
		return currentClipBounds;
	}

	@Override
	public Point stringExtent(String string) {
		return textExtent(string);
	}

	@Override
	public int getLineCap() {
		return lineCap;
	}

	@Override
	public void copyArea(Image image, int x, int y) {

		if (image == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		final io.github.humbleui.skija.Image skijaImage = SwtToSkiaImageConverter.convertSWTImageToSkijaImage(image,
				getScaler().getNativeZoom(), resources);
		try (final io.github.humbleui.skija.Image copiedArea = surface.makeImageSnapshot(
				RectangleConverter.createScaledRectangle(getScaler(),x, y, skijaImage.getWidth(), skijaImage.getHeight()).toIRect())) {

			if (copiedArea == null) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}

			try (final Surface imageSurface = surface.makeSurface(skijaImage.getWidth(), skijaImage.getHeight())) {
				imageSurface.getCanvas().drawImage(copiedArea, 0, 0);
				try (final io.github.humbleui.skija.Image snapshot = imageSurface.makeImageSnapshot()) {
					final ImageData imgData = SkijaToSwtImageConverter.convertSkijaImageToImageData(snapshot);
					Image i = null;
					GC gc = null;
					try {
						i = new Image(device, imgData);
						gc = new GC(image);
						gc.drawImage(i, 0, 0);
					} finally {
						if (gc != null) {
							gc.dispose();
						}
						if (i != null) {
							i.dispose();
						}
					}
				}
			}
		}
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
		try (io.github.humbleui.skija.Image copiedArea = surface
				.makeImageSnapshot(RectangleConverter.createScaledRectangle(getScaler(),srcX, srcY, width, height).toIRect())) {
			surface.getCanvas().drawImage(copiedArea, getScaler().autoScaleUp(destX), getScaler().autoScaleUp(destY));
		}
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {

		copyArea(srcX, srcY, width, height, destX, destY);
		if (paint) {
			// Save the canvas state before clipping
			surface.getCanvas().save();
			// Clip to the destination rectangle so only this area is affected
			surface.getCanvas().clipRect(RectangleConverter.createScaledRectangle(getScaler(),srcX, srcY, width, height));
			// Clear the clipped area with transparent background (simulates OS.SW_ERASE)
			surface.getCanvas().clear(SkiaColorConverter.convertSWTColorToSkijaColor(getBackground()));
			// Restore the canvas state
			surface.getCanvas().restore();
			// Trigger redraw for the source area if using SWT Canvas
			canvas.redraw(srcX, srcY, width, height, false);
		}
	}

	@Override
	public boolean isClipped() {
		return isClipSet;
	}

	@Override
	public int getFillRule() {
		return fillRule;
	}

	@Override
	public void getClipping(Region region) {
		if (region == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (region.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		if (this.currentClipBounds != null) {

			region.intersect(this.currentClipBounds);
			region.add(this.currentClipBounds);
			return;
		}

		if (this.currentClipRegion != null) {
			region.intersect(this.currentClipRegion);
			region.add(this.currentClipRegion);
			return;
		}
	}

	@Override
	public int getAdvanceWidth(char ch) {

		final var f = getSkiaFont();
		final AtomicInteger result = new AtomicInteger();
		result.set(-1);

		paintManager.performDraw(paint -> {
			paint.setAntiAlias(false);
			paint.setMode(PaintMode.FILL);

			final var textWidth = f.measureTextWidth(String.valueOf(ch), paint);
			result.set((int) Math.ceil(textWidth));

		});

		return result.get();

	}

	@Override
	public boolean getAdvanced() {
		return true;
	}

	@Override
	public Pattern getBackgroundPattern() {
		return backgroundPattern;
	}

	@Override
	public int getCharWidth(char ch) {
		return getAdvanceWidth(ch);
	}

	@Override
	public Pattern getForegroundPattern() {
		return foregroundPattern;
	}

	@Override
	public GCData getGCData() {
		return null;
	}

	@Override
	public int getInterpolation() {
		if (interpolationMode == SamplingMode.DEFAULT) {
			return SWT.NONE;
		}
		if (interpolationMode == SamplingMode.LINEAR) {
			return SWT.HIGH;
		}
		if (interpolationMode instanceof final FilterMipmap fm) {
			if (fm.getFilterMode() == FilterMode.LINEAR && fm.getMipmapMode() == MipmapMode.LINEAR) {
				return SWT.LOW;
			}
		}
		return SWT.DEFAULT;
	}

	@Override
	public int[] getLineDash() {
		if (lineDashes == null) {
			return null;
		}
		final int[] lineDashesInt = new int[lineDashes.length];
		for (int i = 0; i < lineDashesInt.length; i++) {
			lineDashesInt[i] = getScaler().autoScaleDownToInt(lineDashes[i]);
		}
		return lineDashesInt;
	}

	@Override
	public int getLineJoin() {
		return lineJoin;
	}

	@Override
	public int getStyle() {
		return style;
	}

	@Override
	public int getTextAntialias() {
		return textAntiAlias;
	}

	@Override
	public void getTransform(Transform transform) {
		if (transform == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		SkiaTransformConverter.fromSkiaMatrix(currentTransform, transform, getScaler());
	}

	@Override
	public boolean getXORMode() {
		return xorModeActive;
	}

	@Override
	public void setBackgroundPattern(Pattern pattern) {
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.backgroundPattern = pattern;
	}

	@Override
	public void setClipping(Path path) {
		final Canvas canvas = surface.getCanvas();
		if (isClipSet) {
			canvas.restore(); // pop the previously saved clip layer
			isClipSet = false;
		}
		if (path == null) {
			return;
		}
		if (path.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		try (final io.github.humbleui.skija.Path skijaPath = SkiaPathConverter.convertSWTPathToSkijaPath(path,
				getScaler())) {
			if (skijaPath == null) {
				return;
			}
			skijaPath.setFillMode(fillRule == SWT.FILL_EVEN_ODD ? PathFillMode.EVEN_ODD : PathFillMode.WINDING);
			// Push a new canvas layer so the clip can be undone later with restore()
			canvas.save();
			isClipSet = true;
			canvas.clipPath(skijaPath, ClipMode.INTERSECT, true);
		}
	}

	@Override
	public void setClipping(Rectangle rect) {

		// Skija uses a canvas state stack; each save() pushes a new layer,
		// and restore() pops it, removing the clipping region set in that layer.
		// Since only one clip is tracked at a time, no explicit restore() is needed
		// here
		// because the clip will be cleared when the next setClipping call is made or on
		// dispose.
		final Canvas canvas = surface.getCanvas();
		if (isClipSet) {
			canvas.restore(); // pop the previously saved clip layer
			isClipSet = false;
		}
		if (rect == null) {
			currentClipBounds = null;
			return;
		}
		currentClipBounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
		// Push a new canvas layer so the clip can be undone later with restore()
		canvas.save();
		canvas.clipRect(RectangleConverter.createScaledRectangle(getScaler(),rect));
		isClipSet = true;

	}

	@Override
	public void setClipping(Region region) {

		// Skija uses a canvas state stack; each save() pushes a new layer,
		// and restore() pops it, removing the clipping region set in that layer.
		final Canvas canvas = surface.getCanvas();
		if (isClipSet) {
			canvas.restore(); // pop the previously saved clip layer
			isClipSet = false;
		}
		currentClipBounds = null;
		currentClipRegion = region;

		if (region == null) {
			return;
		}

		final SkiaRegionCalculator calc = new SkiaRegionCalculator(region, skiaExtension);
		// Push a new canvas layer so the clip can be undone later with restore()
		canvas.save();
		try (calc) {
			canvas.clipRegion(calc.getSkiaRegion());
		}
		isClipSet = true;
	}

	@Override
	public void setFillRule(int rule) {
		if (rule != SWT.FILL_EVEN_ODD && rule != SWT.FILL_WINDING) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.fillRule = rule;
	}

	@Override
	public void setForegroundPattern(Pattern pattern) {
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.foregroundPattern = pattern;
	}

	@Override
	public void setInterpolation(int interpolation) {

		// GDI | Skia | description
		// NearestNeighbor SKFilterMode.Nearest hart pixels
		// Low / Bilinear SKFilterMode.Linear simple linear
		// High / Bicubic SKCubicResampler.Mitchell high quality
		// HighQualityBicubic SKCubicResampler.CatmullRom maximum sharp, cubic
		// interpolation

		switch (interpolation) {
		case SWT.NONE -> this.interpolationMode = SamplingMode.DEFAULT; // Nearest neighbor
		case SWT.LOW -> this.interpolationMode = SamplingMode.LINEAR;
		case SWT.DEFAULT -> this.interpolationMode = SamplingMode.MITCHELL;
		case SWT.HIGH -> this.interpolationMode = SamplingMode.CATMULL_ROM;
		default -> SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	@Override
	public void setLineAttributes(LineAttributes attributes) {
		if (attributes == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final float scaledWidth = getScaler().autoScaleUp(attributes.width);
		setLineAttributesInPixels(attributes, scaledWidth);
	}

	private void setLineAttributesInPixels(LineAttributes attributes, float scaledWidth) {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		boolean changed = false;
		if (scaledWidth != this.lineWidth) {
			this.lineWidth = (int) scaledWidth;
			changed = true;
		}
		// Handle line style with validation
		int lineStyle = attributes.style;
		if (lineStyle != this.lineStyle) {
			switch (lineStyle) {
			case SWT.LINE_SOLID:
			case SWT.LINE_DASH:
			case SWT.LINE_DOT:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				break;
			case SWT.LINE_CUSTOM:
				if (attributes.dash == null) {
					lineStyle = SWT.LINE_SOLID;
				}
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.lineStyle = lineStyle;
			changed = true;
		}
		// Handle line cap with validation
		final int cap = attributes.cap;
		if (cap != this.lineCap) {
			switch (cap) {
			case SWT.CAP_FLAT:
			case SWT.CAP_ROUND:
			case SWT.CAP_SQUARE:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.lineCap = cap;
			changed = true;
		}
		// Handle line join with validation
		final int join = attributes.join;
		if (join != this.lineJoin) {
			switch (join) {
			case SWT.JOIN_MITER:
			case SWT.JOIN_ROUND:
			case SWT.JOIN_BEVEL:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.lineJoin = join;
			changed = true;
		}
		// Handle dash pattern with validation and DPI scaling
		final float[] dashes = attributes.dash;
		final float[] currentDashes = this.lineDashes;

		if (dashes != null && dashes.length > 0) {
			boolean dashesChanged = currentDashes == null || currentDashes.length != dashes.length;
			final float[] newDashes = new float[dashes.length];

			for (int i = 0; i < dashes.length; i++) {
				final float dash = dashes[i];
				if (dash <= 0) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}

				// Scale dash values for DPI
				newDashes[i] = getScaler().autoScaleUp(dash);

				if (!dashesChanged && currentDashes != null && currentDashes[i] != newDashes[i]) {
					dashesChanged = true;
				}
			}
			if (dashesChanged) {
				this.lineDashes = newDashes;
				changed = true;
			}
		} else {
			// Clear dash pattern
			if (currentDashes != null && currentDashes.length > 0) {
				this.lineDashes = null;
				changed = true;
			}
		}
		// Handle dash offset - store for use in createPathEffectForLineStyle()
		final float dashOffset = attributes.dashOffset;
		if (this.dashOffset != dashOffset) {
			this.dashOffset = dashOffset;
			changed = true;
		}
		// Handle miter limit - store for use in performDraw()
		final float miterLimit = attributes.miterLimit;
		if (this.miterLimit != miterLimit) {
			this.miterLimit = miterLimit;
			changed = true;
		}
		if (!changed) {
			return;
		}
	}

	@Override
	public void setLineCap(int cap) {
		if (cap != SWT.CAP_FLAT && cap != SWT.CAP_ROUND && cap != SWT.CAP_SQUARE) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.lineCap = cap;
	}

	@Override
	public void setLineDash(int[] dashes) {
		if (dashes != null && dashes.length > 0) {
			boolean changed = this.lineStyle != SWT.LINE_CUSTOM || lineDashes == null
					|| lineDashes.length != dashes.length;
			final float[] newDashes = new float[dashes.length];
			for (int i = 0; i < dashes.length; i++) {
				if (dashes[i] <= 0) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				newDashes[i] = getScaler().autoScaleUp(dashes[i]);
				if (!changed && lineDashes != null && lineDashes[i] != newDashes[i]) {
					changed = true;
				}
			}
			if (!changed) {
				return;
			}
			this.lineDashes = newDashes;
			this.lineStyle = SWT.LINE_CUSTOM;
		} else {
			if (this.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) {
				return;
			}
			this.lineDashes = null;
			this.lineStyle = SWT.LINE_SOLID;
		}
	}

	@Override
	public void setLineJoin(int join) {
		if (join != SWT.JOIN_MITER && join != SWT.JOIN_ROUND && join != SWT.JOIN_BEVEL) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.lineJoin = join;
	}

	@Override
	public void setXORMode(boolean xor) {
		this.xorModeActive = xor;
	}

	@Override
	public void setTextAntialias(int antialias) {
		this.textAntiAlias = antialias;
	}

	@Override
	public Color getBackground() {
		return resources.getBackground();
	}

	@Override
	public Device getDevice() {
		return device;
	}

	@Override
	public org.eclipse.swt.graphics.Font getFont() {
		return this.resources.getFont();
	}

	@Override
	public boolean isDisposed() {
		return surface.isClosed();
	}

	@Override
	public FontMetrics getFontMetrics() {

		final var font = getSkiaFont();
		final var m = font.getMetrics();
		final var fe = new FontMetricsExtension(new SkiaFontMetrics(m, getScaler()));
		return fe;
	}

	@Override
	public Drawable getDrawable() {
		return canvas;
	}

	@Override
	public void textLayoutDraw(TextLayout textLayout, GC gc, int xInPoints, int yInPoints, int selectionStart,
			int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {

		final var rectangle = textLayout.getBounds();

		Image i = null;
		GC nativeGC = null;
		try {
			i = new Image(device, rectangle.width, rectangle.height);
			nativeGC = new GC(i);

			textLayout.draw(nativeGC, 0, 0, selectionStart, selectionEnd, selectionForeground, selectionBackground,
					flags);

			drawImage(i, rectangle.x, rectangle.y, rectangle.width, rectangle.height, xInPoints, yInPoints,
					rectangle.width, rectangle.height);
		} finally {
			if (nativeGC != null) {
				nativeGC.dispose();
			}
			if (i != null) {
				i.dispose();
			}
		}

	}

	SkiaResources getSkiaResources() {
		return this.resources;
	}

	Surface getSkiaSurface() {
		return this.surface;
	}

	ISkiaCanvasExtension getSkiaExtension() {
		return this.skiaExtension;
	}

	float[] getLineDashes() {
		return this.lineDashes;
	}

}

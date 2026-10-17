/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 * 	   Latha Patil (ETAS GmbH) - Implementation of M3GC APIs
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

public class M3GC extends GCHandle {

	private static final Map<FontData, Font> FONT_CACHE = new ConcurrentHashMap<>();

	static final float[] LINE_DOT_PATTERN = new float[]{3, 3};
	static final float[] LINE_DASH_PATTERN = new float[]{18, 6};
	static final float[] LINE_DASHDOT_PATTERN = new float[]{9, 6, 3, 6};
	static final float[] LINE_DASHDOTDOT_PATTERN = new float[]{9, 3, 3, 3, 3, 3};

	public static M3GC createDefaultInstance(NativeGC gc) {
		return new M3GC(gc, gc.drawable, false);
	}

	public static M3GC createDefaultInstance(NativeGC gc, Control control) {
		return new M3GC(gc, control, false);
	}

	public static M3GC createMeasureInstance(NativeGC gc, Control control) {
		return new M3GC(gc, control, true);
	}

	private final Surface surface;

	private NativeGC innerGC;

	private Color background;
	private Color foreground;
	private org.eclipse.swt.graphics.Font swtFont;
	private Font skiaFont;
	private float baseSymbolHeight = 0; // Height of symbol with "usual" height, like "T", to be vertically centered
	private int lineWidth;
	private int lineStyle;
	private int lineCap = SWT.CAP_FLAT;
	private int lineJoin = SWT.JOIN_MITER;
	private float[] lineDashes = null;
	private float dashOffset = 0;
	private float miterLimit = 10;
	private int fillRule = SWT.FILL_EVEN_ODD;
	private int antialias;
	private int alpha = 255;
	private boolean hasAlphaLayer = false;
	private Pattern foregroundPattern;
	private Pattern backgroundPattern;

	private final Point originalDrawingSize;

	private static Map<ColorType, int[]> colorTypeMap = null;
	private Matrix33 currentTransform = Matrix33.IDENTITY;

	private SamplingMode interpolationMode=SamplingMode.DEFAULT;

	private boolean isClipSet;
	private Rectangle currentClipBounds;

	private M3GC(NativeGC gc, Drawable drawable, boolean onlyForMeasuring) {
		innerGC = gc;
		device = gc.device;
		originalDrawingSize = extractSize(drawable);
		currentClipBounds = new Rectangle(0, 0, originalDrawingSize.x, originalDrawingSize.y);
		if (onlyForMeasuring) {
			surface = createMeasureSurface();
		} else {
			surface = createDrawingSurface();
			initializeWithParentBackground();
		}
		initFont();
	}

	private static Point extractSize(Drawable drawable) {
		Point size = new Point(0, 0);
		if (drawable instanceof Image image) {
			var imageBounds = image.getBounds();
			size.x = imageBounds.width;
			size.y = imageBounds.height;
		} else if (drawable instanceof Control control) {
			size = control.getSize();
		} else if (drawable instanceof Device device) {
			var deviceBounds = device.getBounds();
			size.x = deviceBounds.width;
			size.y = deviceBounds.height;
		}
		return size;
	}

	@Override
	void initNonDisposeTracking() {
		// do not yet use resource handling for M3GC
		// TODO use the resource handling and prevent the error messages for not closed
		// resources.
	}

	private static boolean isEmpty(Point area) {
		return area.x <= 0 || area.y <= 0;
	}

	private Surface createDrawingSurface() {
		Point drawingSizeInPixels = DPIUtil.autoScaleUp(originalDrawingSize);
		if (isEmpty(originalDrawingSize)) {
			drawingSizeInPixels = new Point(1, 1);
		}
		return createSurface(drawingSizeInPixels.x, drawingSizeInPixels.y);
	}

	private Surface createMeasureSurface() {
		return createSurface(1, 1);
	}

	private Surface createSurface(int width, int height) {
		return Surface.makeRaster(ImageInfo.makeN32Premul(width, height), 0, new SurfaceProps(PixelGeometry.RGB_H));
	}

	private void initializeWithParentBackground() {
		if (originalDrawingSize.x > 0 && originalDrawingSize.y > 0) {
			Image image = new Image(innerGC.device, originalDrawingSize.x, originalDrawingSize.y);
			innerGC.copyArea(image, 0, 0);
			drawImage(image, 0, 0);
			image.dispose();
		}
	}

	private void initFont() {
		org.eclipse.swt.graphics.Font originalFont = innerGC.getFont();
		if (originalFont == null || originalFont.isDisposed()) {
			originalFont = innerGC.getDevice().getSystemFont();
		}
		setFont(originalFont);
	}

	@Override
	public void dispose() {
		if (hasAlphaLayer) {
			surface.getCanvas().restore();
			hasAlphaLayer = false;
		}
		surface.close();
		innerGC = null;
		skiaFont = null;
		swtFont = null;
	}

	@Override
	public Color getBackground() {
		return background;
	}

	private void performDraw(Consumer<Paint> operations) {
		Paint paint = new Paint();
		if (!hasAlphaLayer) {
			paint.setAlphaf(alpha / 255.0f);
		}
		operations.accept(paint);
		paint.close();
	}

	private void performDrawLine(Consumer<Paint> operations) {
		performDraw(paint -> {
			applyForegroundPattern(paint);
			paint.setMode(PaintMode.STROKE);
			paint.setStrokeWidth(lineWidth > 0 ? DPIUtil.autoScaleUp(lineWidth) : 1);
			paint.setAntiAlias(true);

			// Apply line cap setting
			PaintStrokeCap m3LineCap;
			switch (lineCap) {
				case SWT.CAP_ROUND:
					m3LineCap = PaintStrokeCap.ROUND;
					break;
				case SWT.CAP_SQUARE:
					m3LineCap = PaintStrokeCap.SQUARE;
					break;
				case SWT.CAP_FLAT:
				default:
					m3LineCap = PaintStrokeCap.BUTT;
					break;
			}
			paint.setStrokeCap(m3LineCap);

			// Apply line join setting
			PaintStrokeJoin m3LineJoin;
			switch (lineJoin) {
				case SWT.JOIN_MITER:
					m3LineJoin = PaintStrokeJoin.MITER;
					break;
				case SWT.JOIN_ROUND:
					m3LineJoin = PaintStrokeJoin.ROUND;
					break;
				case SWT.JOIN_BEVEL:
				default:
					m3LineJoin = PaintStrokeJoin.BEVEL;
					break;
			}
			paint.setStrokeJoin(m3LineJoin);
			// Apply line dash pattern based on line style
			PathEffect pathEffect = createPathEffectForLineStyle();
			if (pathEffect != null) {
				paint.setPathEffect(pathEffect);
			}
			operations.accept(paint);
		});
	}

	/**
	 * Creates a PathEffect for the current line style and dash pattern.
	 * @return PathEffect for dashed lines, or null for solid lines
	 */
	private PathEffect createPathEffectForLineStyle() {
		float[] dashPattern = null;
		float effectiveLineWidth = lineWidth > 0 ? DPIUtil.autoScaleUp(lineWidth) : 1;
		
		switch (lineStyle) {
			case SWT.LINE_SOLID:
				return null; // No path effect needed for solid lines
				
			case SWT.LINE_DOT:
				if (effectiveLineWidth == 1) {
					dashPattern = LINE_DOT_PATTERN.clone();
				} else {
					// Scale pattern based on line width
					dashPattern = new float[]{effectiveLineWidth, effectiveLineWidth};
				}
				break;
				
			case SWT.LINE_DASH:
				if (effectiveLineWidth == 1) {
					dashPattern = LINE_DASH_PATTERN.clone();
				} else {
					// Scale pattern based on line width
					dashPattern = new float[]{6 * effectiveLineWidth, 2 * effectiveLineWidth};
				}
				break;
				
			case SWT.LINE_DASHDOT:
				if (effectiveLineWidth == 1) {
					dashPattern = LINE_DASHDOT_PATTERN.clone();
				} else {
					// Scale pattern based on line width
					dashPattern = new float[]{
						3 * effectiveLineWidth, 2 * effectiveLineWidth, 
						effectiveLineWidth, 2 * effectiveLineWidth
					};
				}
				break;
				
			case SWT.LINE_DASHDOTDOT:
				if (effectiveLineWidth == 1) {
					dashPattern = LINE_DASHDOTDOT_PATTERN.clone();
				} else {
					// Scale pattern based on line width
					dashPattern = new float[]{
						3 * effectiveLineWidth, effectiveLineWidth, 
						effectiveLineWidth, effectiveLineWidth, 
						effectiveLineWidth, effectiveLineWidth
					};
				}
				break;
				
			case SWT.LINE_CUSTOM:
				if (lineDashes != null && lineDashes.length > 0) {
					dashPattern = lineDashes.clone();
				}
				break;
				
			default:
				return null;
		}
		
		if (dashPattern != null && dashPattern.length > 0) {
			// Ensure even number of elements (on/off pairs)
			if (dashPattern.length % 2 != 0) {
				float[] evenPattern = new float[dashPattern.length * 2];
				System.arraycopy(dashPattern, 0, evenPattern, 0, dashPattern.length);
				System.arraycopy(dashPattern, 0, evenPattern, dashPattern.length, dashPattern.length);
				dashPattern = evenPattern;
			}
			
			// Apply dash offset (scaled for DPI)
			float scaledDashOffset = DPIUtil.autoScaleUp(dashOffset);
			return PathEffect.makeDash(dashPattern, scaledDashOffset);
		}
		
		return null;
	}

	private void performDrawText(Consumer<Paint> operations) {
		performDraw(paint -> {
			applyForegroundPattern(paint);
			operations.accept(paint);
		});
	}

	private void performDrawFilled(Consumer<Paint> operations) {
		performDraw(paint -> {
			applyBackgroundPattern(paint);
			paint.setMode(PaintMode.FILL);
			paint.setAntiAlias(true);
			operations.accept(paint);
		});
	}

	private void applyBackgroundPattern(Paint paint) {
		if (backgroundPattern != null && !backgroundPattern.isDisposed()) {
			Shader shader = convertSWTPatternToM3Shader(backgroundPattern);
			if (shader != null) {
				paint.setShader(shader);
				return;
			}
		}
		// Fallback to backGround color if no pattern or pattern conversion failed
		paint.setColor(convertSWTColorToM3Color(getBackground()));
	}

	private void performDrawPoint(Consumer<Paint> operations) {
		performDraw(paint -> {
			paint.setColor(convertSWTColorToM3Color(getForeground()));
			paint.setMode(PaintMode.FILL);
			paint.setAntiAlias(false);
			operations.accept(paint);
		});
	}

	private String[] splitString(String text) {
		String[] lines = new String[1];
		int start = 0, pos;
		do {
			pos = text.indexOf('\n', start);
			if (pos == -1) {
				lines[lines.length - 1] = text.substring(start);
			} else {
				boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
				lines[lines.length - 1] = text.substring(start, pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}

	@Override
	public void commit() {
		if (hasAlphaLayer) {
			surface.getCanvas().restore();
			hasAlphaLayer = false;
		}

		if (isEmpty(originalDrawingSize)) {
			return;
		}
		try (io.github.humbleui.skija.Image im = surface.makeImageSnapshot()) {
			byte[] imageBytes = EncoderPNG.encode(im).getBytes();

			Image transferImage = new Image(innerGC.getDevice(), new ByteArrayInputStream(imageBytes));

			Point drawingSizeInPixels = DPIUtil.autoScaleUp(originalDrawingSize);
			innerGC.drawImage(transferImage, 0, 0, drawingSizeInPixels.x, drawingSizeInPixels.y, //
					0, 0, originalDrawingSize.x, originalDrawingSize.y);
			transferImage.dispose();
		}
	}

	@Override
	public Point textExtent(String string) {
		return textExtent(string, SWT.NONE);
	}

	@Override
	public void setBackground(Color color) {
		if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.background = color;
	}

	@Override
	public void setForeground(Color color) {
		if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.foreground = color;
	}

	@Override
	public void fillRectangle(Rectangle rect) {
		fillRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawImage(Image image, int x, int y) {
		Canvas canvas = surface.getCanvas();
		canvas.drawImage(convertSWTImageToM3Image(image), DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {
		if (image == null) {
			System.out.println("M3GC.drawImage(..): Error draw image that is null!!");
			return;
		}
		Canvas canvas = surface.getCanvas();
		canvas.drawImageRect(convertSWTImageToM3Image(image),
				createScaledRectangle(srcX, srcY, srcWidth, srcHeight),
				createScaledRectangle(destX, destY, destWidth, destHeight),interpolationMode,null,true);
	}

	private static ColorType getColorType(ImageData imageData) {
		PaletteData palette = imageData.palette;

		if (imageData.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
			return ColorType.UNKNOWN;
		}

		if (palette.isDirect) {
			int redMask = palette.redMask;
			int greenMask = palette.greenMask;
			int blueMask = palette.blueMask;

			if (redMask == 0xFF0000 && greenMask == 0x00FF00 && blueMask == 0x0000FF) {
				return ColorType.UNKNOWN;
			}

			if (redMask == 0xFF000000 && greenMask == 0x00FF0000
					&& blueMask == 0x0000FF00) {
				return ColorType.RGBA_8888;
			}

			if (redMask == 0xF800 && greenMask == 0x07E0 && blueMask == 0x001F) {
				return ColorType.RGB_565;
			}

			if (redMask == 0xF000 && greenMask == 0x0F00 && blueMask == 0x00F0) {
				return ColorType.ARGB_4444;
			}

			if (redMask == 0x0000FF00 && greenMask == 0x00FF0000 && blueMask == 0xFF000000) {
				return ColorType.BGRA_8888;
			}

			if (redMask == 0x3FF00000 && greenMask == 0x000FFC00 && blueMask == 0x000003FF) {
				return ColorType.RGBA_1010102;
			}

			if (redMask == 0x000003FF && greenMask == 0x000FFC00 && blueMask == 0x3FF00000) {
				return ColorType.BGRA_1010102;
			}

			if (redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000) {
				return ColorType.UNKNOWN;
			}
		} else {
			if (imageData.depth == 8 && palette.colors != null && palette.colors.length <= 256) {
				return ColorType.ALPHA_8;
			}

			if (imageData.depth == 16) {
				// 16-bit indexed images are not directly mappable to common
				// ColorTypes
				return ColorType.ARGB_4444; // Assume for indexed color images
			}

			if (imageData.depth == 24) {
				// Assuming RGB with no alpha channel
				return ColorType.RGB_888X;
			}

			if (imageData.depth == 32) {
				// Assuming 32-bit color with alpha channel
				return ColorType.RGBA_8888;
			}
		}

		return ColorType.UNKNOWN;

//		throw new UnsupportedOperationException("Unsupported SWT ColorType: " + Integer.toBinaryString(palette.redMask)
//				+ "__" + Integer.toBinaryString(palette.greenMask) + "__" + Integer.toBinaryString(palette.blueMask));
	}

	private static io.github.humbleui.skija.Image convertSWTImageToM3Image(Image swtImage) {
		ImageData imageData = swtImage.getImageData(DPIUtil.getDeviceZoom());
		return convertSWTImageToM3Image(imageData);
	}

	static io.github.humbleui.skija.Image convertSWTImageToM3Image(ImageData imageData) {
		int width = imageData.width;
		int height = imageData.height;
		ColorType colType = getColorType(imageData);

		// always prefer the alphaData. If these are set, the bytes data are empty!!
		if (colType.equals(ColorType.UNKNOWN) || imageData.alphaData != null) {
			byte[] bytes = null;
			bytes = convertToRGBA(imageData);
			colType = ColorType.RGBA_8888;
			ImageInfo imageInfo = new ImageInfo(width, height, colType, ColorAlphaType.UNPREMUL);
			return io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo, bytes,
				imageData.width * 4);
		} else {
			ImageInfo imageInfo = new ImageInfo(width, height, colType, ColorAlphaType.UNPREMUL);

			return io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo, imageData.data,
				imageData.width * 4);
		}
	}

	public static byte[] convertToRGBA(ImageData imageData) {
		ImageData transparencyData = imageData.getTransparencyMask();
		byte[] convertedData = new byte[imageData.width * imageData.height * 4];
		byte defaultAlpha = (byte)255;

		var source = imageData.data;
		int bytesPerPixel = source.length / (imageData.width * imageData.height);

		var alphaData = imageData.alphaData;
		if (imageData.alpha != -1) {
			defaultAlpha = (byte) imageData.alpha;
		}

		boolean byteSourceContainsAlpha = bytesPerPixel > 3;

		RGB[] cols = imageData.palette.colors;

		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				int pixel = imageData.getPixel(x, y);
				int arrayPos = (y * imageData.width + x);

				byte r = (byte) ((pixel & imageData.palette.redMask) >>> -imageData.palette.redShift);
				byte g = (byte) ((pixel & imageData.palette.greenMask) >>> -imageData.palette.greenShift);
				byte b = (byte) ((pixel & imageData.palette.blueMask) >>> -imageData.palette.blueShift);

				if (cols != null) {
					RGB rgb = cols[pixel];

					r = (byte) rgb.red;
					b = (byte) rgb.blue;
					g = (byte) rgb.green;
				}

				byte a = (byte)255;
				if (transparencyData != null) {
					if (transparencyData.getPixel(x, y) != 1) {
						a = (byte) 0;
					}
				}

				var index = arrayPos * 4;

				convertedData[index + 0] = (byte) r;
				convertedData[index + 1] = (byte) g;
				convertedData[index + 2] = (byte) b;
				convertedData[index + 3] = (byte) a;

//				if (alphaData != null && alphaData.length > arrayPos) {
//					convertedData[index + 3] = alphaData[arrayPos];
//				} else if (imageData.alpha != -1) {
//					convertedData[index + 3] = defaultAlpha;
//				} else if(!byteSourceContainsAlpha) {
//					convertedData[index + 3] = defaultAlpha;
//				}
			}
		}

		return convertedData;
	}

	static ImageData convertToM3ImageData(io.github.humbleui.skija.Image image) {
		Bitmap bm = Bitmap.makeFromImage(  image);
		var colType = bm.getColorType();
		byte[] alphas = new byte[bm.getHeight() * bm.getWidth()];
		var source = bm.readPixels();
		byte[] convertedData = new byte[bm.getHeight() * bm.getWidth() * 3];

		var colorOrder = getPixelOrder(colType);

		// no alphaType handling support. UNPREMUL and OPAQUE should always work.
//		ColorAlphaType alphaType = bm.getAlphaType();

		for (int y = 0; y < bm.getHeight(); y++) {
			for (int x = 0; x < bm.getWidth(); x++) {
				byte alpha = convertAlphaTo255Range(bm.getAlphaf(x, y));

				int index = (x + y * bm.getWidth()) * 4;

				byte red = (byte) source[index + colorOrder[0]];
				byte green = (byte) source[index + colorOrder[1]];
				byte blue = (byte) source[index + colorOrder[2]];
				alpha = (byte) source[index + colorOrder[3]];

				alphas[x + y * bm.getWidth()] = alpha;

				int target = (x + y * bm.getWidth()) * 3;

				convertedData[target + 0] = (byte) (red);
				convertedData[target + 1] = (byte) (green);
				convertedData[target + 2] = (byte) (blue);
			}
		}

		ImageData d = new ImageData(bm.getWidth(), bm.getHeight(), 24,
				new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));
		d.data = convertedData;
		d.alphaData = alphas;
		d.bytesPerLine = d.width * 3;

		return d;
	}

	public static void writeFile(String str, io.github.humbleui.skija.Image image) {
		byte[] imageBytes = EncoderPNG.encode(image).getBytes();

		File f = new File(str);
		if (f.exists()) {
			f.delete();
		}

		try {
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(imageBytes);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static byte convertAlphaTo255Range(float alphaF) {
		if (alphaF < 0.0f) {
			alphaF = 0.0f;
		}
		if (alphaF > 1.0f) {
			alphaF = 1.0f;
		}

		return (byte) Math.round(alphaF * 255);
	}

	public static byte[] convertPremulToUnpremul(byte[] premulColor) {
		if (premulColor.length != 4) {
			throw new IllegalArgumentException("Input array must have a length of 4.");
		}

		int rPremul = premulColor[0] & 0xFF;
		int gPremul = premulColor[1] & 0xFF;
		int bPremul = premulColor[2] & 0xFF;
		int a = premulColor[3] & 0xFF;

		if (a == 0) {
			// no conversion necessary if alpha is 0
			return new byte[] { (byte) rPremul, (byte) gPremul, (byte) bPremul, (byte) a };
		}

		// convert premul -> unpremul
		int r = (rPremul * 255) / a;
		int g = (gPremul * 255) / a;
		int b = (bPremul * 255) / a;

		// Ensure values are within the valid range [0, 255]
		r = Math.min(255, Math.max(0, r));
		g = Math.min(255, Math.max(0, g));
		b = Math.min(255, Math.max(0, b));

		return new byte[] { (byte) r, (byte) g, (byte) b, (byte) a };
	}

	public static int convertSWTColorToM3Color(Color swtColor) {
		// extract RGB-components
		int red = swtColor.getRed();
		int green = swtColor.getGreen();
		int blue = swtColor.getBlue();
		int alpha = swtColor.getAlpha();

		// create ARGB 32-Bit-color
		int m3Color = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return m3Color;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		float scaledOffsetValue = getScaledOffsetValue();
		performDrawLine(paint -> surface.getCanvas().drawLine(DPIUtil.autoScaleUp(x1) + scaledOffsetValue,
				DPIUtil.autoScaleUp(y1) + scaledOffsetValue, DPIUtil.autoScaleUp(x2) + scaledOffsetValue,
				DPIUtil.autoScaleUp(y2) + scaledOffsetValue, paint));
	}

	@Override
	public Color getForeground() {
		return foreground;
	}

	@Override
	public void drawText(String string, int x, int y) {
		drawText(string, x, y, false);
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
		drawText(string, x, y, isTransparent ? SWT.TRANSPARENT : SWT.NONE);
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {
		if (text == null) {
			return;
		}
		if (text.contains("\t")) {
			text = expandTabs(text, x);
		}
		TextBlob textBlob = buildTextBlob(text);
		if (textBlob == null) {
			return;
		}
		if ((flags & (SWT.TRANSPARENT | SWT.DRAW_TRANSPARENT)) == 0) {
			int textWidth = Math.round(textBlob.getBounds().getWidth());
			int fontHeight = Math.round(skiaFont.getMetrics().getHeight());
			performDrawFilled(
					paint -> surface.getCanvas().drawRect(new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
							DPIUtil.autoScaleUp(x) + textWidth, DPIUtil.autoScaleUp(y) + fontHeight), paint));
		}
		Point point = calculateSymbolCenterPoint(x, y);
		performDrawText(paint -> surface.getCanvas().drawTextBlob(textBlob, point.x, point.y, paint));
	}

    /**
     * Expands tab characters (\t) in the text to position-dependent spaces, so that
     * the next character aligns to the next tab stop (every 8 average character widths by default).
     * The expansion is based on the current x position and the average character width of the font.
     *
     * @param text The input text containing tab characters
     * @param startX The starting x position in pixels (used to calculate tab alignment)
     * @return The text with tabs expanded to spaces, aligned to the next tab stop
     */
	private String expandTabs(String text, int startX) {
		StringBuilder result = new StringBuilder();
		int currentX = 0;
		int spaceWidth = textExtent(" ").x;
		float _avgCharWidth = skiaFont.getMetrics()._avgCharWidth;
		int avgCharWidth = (int) _avgCharWidth;
		if (avgCharWidth <= 0) {
			avgCharWidth = spaceWidth > 0 ? spaceWidth : 1;
		}
		int tabSpacingPx = 8 * avgCharWidth;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\t') {
				int offsetInTab = tabSpacingPx > 0 ? (currentX - startX) % tabSpacingPx : 0;
				int nextTabX = currentX + (tabSpacingPx - offsetInTab);
				while (currentX < nextTabX) {
					result.append(' ');
					currentX += textExtent(" ").x;
				}
			} else {
				String s = String.valueOf(ch);
				int charWidth = textExtent(s).x;
				result.append(ch);
				currentX += charWidth;
			}
		}
		return result.toString();
	}
	// y position in drawTextBlob() is the text baseline, e.g., the bottom of "T"
	// but the middle of "y"
	// So center a base symbol (like "T") in the desired text box (according to
	// parameter y being the top left text box position and the text box height
	// according to font metrics)
	private Point calculateSymbolCenterPoint(int x, int y) {
		int topLeftTextBoxYPosition = DPIUtil.autoScaleUp(y);
		float heightOfTextBoxConsideredByClients = skiaFont.getMetrics().getHeight();
		float heightOfSymbolToCenter = baseSymbolHeight;
		Point point = new Point((int) DPIUtil.autoScaleUp(x),
				(int) (topLeftTextBoxYPosition + heightOfTextBoxConsideredByClients / 2 + heightOfSymbolToCenter / 2));
		return point;
	}

	private TextBlob buildTextBlob(String text) {
		text = replaceMnemonics(text);
		String[] lines = splitString(text);
		TextBlobBuilder blobBuilder = new TextBlobBuilder();
		float lineHeight = skiaFont.getMetrics().getHeight();
		int yOffset = 0;
		for (String line : lines) {
			blobBuilder.appendRun(skiaFont, line, 0, yOffset);
			yOffset += lineHeight;
		}
		TextBlob textBlob = blobBuilder.build();
		blobBuilder.close();
		return textBlob;
	}

	private String replaceMnemonics(String text) {
		int mnemonicIndex = text.lastIndexOf('&');
		if (mnemonicIndex != -1) {
			text = text.replaceAll("&", "");
			// TODO Underline the mnemonic key
		}
		return text;
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		performDrawLine(paint -> surface.getCanvas().drawArc((float) DPIUtil.autoScaleUp(x),
				(float) DPIUtil.autoScaleUp(y), (float) DPIUtil.autoScaleUp(x + width),
				(float) DPIUtil.autoScaleUp(y + height), -startAngle, (float) -arcAngle, false, paint));
	}

	@Override
	public void drawFocus(int x, int y, int width, int height) {
		performDrawLine(paint -> {
			paint.setPathEffect(PathEffect.makeDash(new float[] { 1.5f, 1.5f }, 0.0f));
			surface.getCanvas().drawRect(offsetRectangle(createScaledRectangle(x, y, width, height)), paint);
		});
	}

	void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth,
			int destHeight, boolean simple) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmapTransparentByClipping(long srcHdc, long maskHdc, int srcX, int srcY, int srcWidth, int srcHeight,
			int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		performDrawLine(
				paint -> surface.getCanvas().drawOval(offsetRectangle(createScaledRectangle(x, y, width, height)),
						paint));
	}

	/**
	 * Draws the outline of a path using the current foreground color and line attributes.
	 * The path is rendered as a series of connected lines and curves based on the path data.
	 *
	 * @param path the path to draw, must not be null or disposed
	 * @throws SWTException if the path is null or disposed
	 */
	@Override
	public void drawPath(Path path) {
		io.github.humbleui.skija.Path m3Path = convertSWTPathToM3Path(path);
		if (m3Path == null) return;
		performDrawLine(paint -> surface.getCanvas().drawPath(m3Path, paint));
		m3Path.close();
	}

	@Override
	public void drawPoint(int x, int y) {
		performDrawPoint(paint -> surface.getCanvas().drawRect(createScaledRectangle(x, y, 1, 1), paint));
	}

	@Override
	public void drawPolygon(int[] pointArray) {
		if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		checkGC(innerGC.DRAW);
		if (pointArray.length < 6 || pointArray.length % 2 != 0) return;

		// Handle SWT.MIRRORED style (adjust x-coordinates if needed)
		int style = getStyle();
		boolean mirrored = (style & SWT.MIRRORED) != 0;
		boolean adjustX = mirrored && lineWidth != 0 && lineWidth % 2 == 0;
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]--;
			}
		}

		// Create Skija path for the polygon
		io.github.humbleui.skija.Path path = new io.github.humbleui.skija.Path();	
		// Move to first point
		path.moveTo(DPIUtil.autoScaleUp(pointArray[0]), DPIUtil.autoScaleUp(pointArray[1]));		
		// Add lines to subsequent points
		for (int i = 2; i < pointArray.length; i += 2) {
			path.lineTo(DPIUtil.autoScaleUp(pointArray[i]), DPIUtil.autoScaleUp(pointArray[i + 1]));
		}
		path.closePath();
		// Draw the polygon outline
		performDrawLine(paint -> surface.getCanvas().drawPath(path, paint));	
		path.close();
		// Restore x-coordinates if mirrored
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]++;
			}
		}
	}
	private static float[] convertToFloat(int[] array) {
		float[] arrayAsFloat = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			arrayAsFloat[i] = DPIUtil.autoScaleUp(array[i]);
		}
		return arrayAsFloat;
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		performDrawLine(
				paint -> surface.getCanvas()
						.drawRect(offsetRectangle(createScaledRectangle(x, y, width, height)), paint));
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		performDrawLine(paint -> surface.getCanvas().drawRRect(
				offsetRectangle(createScaledRoundRectangle(x, y, width, height, arcWidth / 2.0f, arcHeight / 2.0f)),
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
		if (!isTransparent) {
			int width = (int) DPIUtil.autoScaleDown(skiaFont.measureTextWidth(string));
			int height = (int) DPIUtil.autoScaleDown(skiaFont.getMetrics().getHeight());
			fillRectangle(x, y, width, height);
		}
		Point point = calculateSymbolCenterPoint(x, y);
		performDrawText(paint -> {
			surface.getCanvas().drawString(string, point.x, point.y, skiaFont, paint);
		});
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		performDrawFilled(paint -> surface.getCanvas().drawArc((float) DPIUtil.autoScaleUp(x),
				(float) DPIUtil.autoScaleUp(y), (float) DPIUtil.autoScaleUp(x + width),
				(float) DPIUtil.autoScaleUp(y + height), (float) -startAngle, (float) -arcAngle, false, paint));
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
		boolean swapColors = false;
		if (width < 0) {
			x = Math.max(0, x + width);
			width = -width;
			if (!vertical)
				swapColors = true;
		}
		if (height < 0) {
			y = Math.max(0, y + height);
			height = -height;
			if (vertical)
				swapColors = true;
		}
		int x2 = vertical ? x : x + width;
		int y2 = vertical ? y + height : y;

		Rect rect = createScaledRectangle(x, y, width, height);
		int fromColor = convertSWTColorToM3Color(getForeground());
		int toColor = convertSWTColorToM3Color(getBackground());
		if (fromColor == toColor) {
			performDrawFilled(paint -> surface.getCanvas().drawRect(rect, paint));
			return;
		}
		if (swapColors) {
			int tempColor = convertSWTColorToM3Color(getForeground());
			fromColor = convertSWTColorToM3Color(getBackground());
			toColor = tempColor;
		}
		performDrawGradientFilled(paint -> surface.getCanvas().drawRect(rect, paint), x, y, x2, y2, fromColor, toColor);
	}

	private void performDrawGradientFilled(Consumer<Paint> operations, int x, int y, int x2, int y2,
			int fromColor, int toColor) {
		performDraw(paint -> {
			try (Shader gradient = Shader.makeLinearGradient(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
					DPIUtil.autoScaleUp(x2), DPIUtil.autoScaleUp(y2), new int[] { fromColor, toColor }, null,
					GradientStyle.DEFAULT)) {
				paint.setShader(gradient);
				paint.setAntiAlias(true);
				operations.accept(paint);
			}
		});
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		performDrawFilled(
				paint -> surface.getCanvas().drawOval(createScaledRectangle(x, y, width, height), paint));
	}

	/**
	 * Fills the interior of a path using the current background color.
	 * The fill operation respects the current fill rule (even-odd or winding).
	 *
	 * @param path the path to fill, must not be null or disposed
	 * @throws SWTException if the path is null or disposed
	 */
	@Override
	public void fillPath(Path path) {
		io.github.humbleui.skija.Path m3Path = convertSWTPathToM3Path(path);
		if (m3Path == null) {
			return;
		}
		m3Path.setFillMode(fillRule == SWT.FILL_EVEN_ODD ? PathFillMode.EVEN_ODD : PathFillMode.WINDING);
		performDrawFilled(paint -> surface.getCanvas().drawPath(m3Path, paint));
		m3Path.close();
	}

	@Override
	public void fillPolygon(int[] pointArray) {
		if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (pointArray.length < 6 || pointArray.length % 2 != 0) return;

		// Create Skija path for the polygon
		io.github.humbleui.skija.Path path = new io.github.humbleui.skija.Path();
		// Move to first point
		path.moveTo(DPIUtil.autoScaleUp(pointArray[0]), DPIUtil.autoScaleUp(pointArray[1]));	
		// Add lines to subsequent points
		for (int i = 2; i < pointArray.length; i += 2) {
			path.lineTo(DPIUtil.autoScaleUp(pointArray[i]), DPIUtil.autoScaleUp(pointArray[i + 1]));
		}	
		// Close the path to form a polygon
		path.closePath();
		path.setFillMode(fillRule == SWT.FILL_EVEN_ODD ? PathFillMode.EVEN_ODD : PathFillMode.WINDING);
		// Fill the polygon
		performDrawFilled(paint -> surface.getCanvas().drawPath(path, paint));		
		path.close();
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		performDrawFilled(
				paint -> surface.getCanvas().drawRect(createScaledRectangle(x, y, width, height), paint));
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		performDrawFilled(paint -> surface.getCanvas()
				.drawRRect(createScaledRoundRectangle(x, y, width, height, arcWidth / 2.0f, arcHeight / 2.0f), paint));
	}

	@Override
	public Point textExtent(String string, int flags) {
		float height = skiaFont.getMetrics().getHeight();
		float width = skiaFont.measureTextWidth(replaceMnemonics(string));
		return new Point(DPIUtil.autoScaleDownToInt(width), DPIUtil.autoScaleDownToInt(height));
	}

	@Override
	public void setFont(org.eclipse.swt.graphics.Font font) {
		if (font != null) {
			if (font.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		} else {
			font = innerGC.getFont();
		}
		this.swtFont = font;
		innerGC.setFont(font);

		this.skiaFont = convertToM3Font(font);
		this.baseSymbolHeight = this.skiaFont.measureText("T").getHeight();
	}

	static Font convertToM3Font(org.eclipse.swt.graphics.Font font) {
		FontData fontData = font.getFontData()[0];
		var cachedFont = FONT_CACHE.get(fontData);
		if (cachedFont != null && cachedFont.isClosed()) {
			FONT_CACHE.remove(fontData);
		}
		return FONT_CACHE.computeIfAbsent(fontData, M3GC::createM3Font);
	}

	static Font createM3Font(FontData fontData) {
		FontStyle style = FontStyle.NORMAL;
		boolean isBold = (fontData.getStyle() & SWT.BOLD) != 0;
		boolean isItalic = (fontData.getStyle() & SWT.ITALIC) != 0;
		if (isBold && isItalic) {
			style = FontStyle.BOLD_ITALIC;
		} else if (isBold) {
			style = FontStyle.BOLD;
		} else if (isItalic) {
			style = FontStyle.ITALIC;
		}
		Font m3Font = new Font(Typeface.makeFromName(fontData.getName(), style));
		int fontSize = DPIUtil.scaleUp(fontData.getHeight(), DPIUtil.getNativeDeviceZoom());
		if (SWT.getPlatform().equals("win32")) {
			fontSize *= m3Font.getSize() / Display.getDefault().getSystemFont().getFontData()[0].getHeight();
		}
		if (SWT.getPlatform().equals("gtk")) {
			// SWT's font size is in points, 1pt = 1/72 inch, adjust m3 font size to this
			fontSize = (fontSize * Display.getDefault().getDPI().y) / 72;
		}
		m3Font.setSize(fontSize);
		m3Font.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
		m3Font.setSubpixel(true);
		return m3Font;
	}

	@Override
	public org.eclipse.swt.graphics.Font getFont() {
		return swtFont;
	}

	@Override
	public void setClipping(int x, int y, int width, int height) {
		setClipping(new Rectangle(x, y, width, height));
	}

	/**
	 * Sets the current transformation matrix for this graphics context.
	 * The transformation matrix affects all subsequent drawing operations by applying
	 * scaling, rotation, translation, and shearing transformations.
	 *
	 * @param transform the transformation to apply, or null to reset to identity transform
	 * @throws SWTException if the transform is disposed
	 */
	@Override
	public void setTransform(Transform transform) {
		if (transform == null) {
			currentTransform = Matrix33.IDENTITY;
			surface.getCanvas().setMatrix(currentTransform);
		} else {
			if (transform.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			float[] elements = new float[6];
			transform.getElements(elements);
			// SWT Transform: [m11, m12, m21, m22, dx, dy]
			// Skija Matrix33: [scaleX, skewX, transX, skewY, scaleY, transY, persp0,
			// persp1, persp2]
			// Correct mapping: SWT [0,1,2,3,4,5] -> Skija [0,2,4,1,3,5,0,0,1]
			float[] m3Mat = new float[] { elements[0], // m11 -> scaleX
					elements[2], // m21 -> skewX
					elements[4], // dx -> transX
					elements[1], // m12 -> skewY
					elements[3], // m22 -> scaleY
					elements[5], // dy -> transY
					0, 0, 1 // perspective elements
			};
			currentTransform = new Matrix33(m3Mat);
			surface.getCanvas().setMatrix(currentTransform);
		}
	}

	/**
	 * Sets the alpha value for drawing operations. The alpha value controls the transparency
	 * of all subsequent drawing operations.
	 * <p>
	 * When alpha is less than 255, a new layer is created with the specified alpha level.
	 * This affects all drawing operations until the alpha is changed again.
	 * </p>
	 *
	 * @param alpha the alpha value, must be between 0 (fully transparent) and 255 (fully opaque)
	 * @throws SWTException if the alpha value is not in the valid range [0, 255]
	 */
	@Override
	public void setAlpha(int alpha) {
        if (alpha < 0 || alpha > 255) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (this.alpha != alpha) {
            if (hasAlphaLayer) {
                surface.getCanvas().restore();
                hasAlphaLayer = false;
            }
            this.alpha = alpha;
            if (alpha < 255) {
                Paint layerPaint = new Paint();
                layerPaint.setAlphaf(alpha / 255.0f);
                surface.getCanvas().saveLayer(null, layerPaint);
                layerPaint.close();
                hasAlphaLayer = true;
            }
        }
    }

    /**
     * Returns the current alpha value used for drawing operations.
     * The alpha value controls the transparency of drawing operations.
     *
     * @return the current alpha value, between 0 (fully transparent) and 255 (fully opaque)
     */
    @Override
    public int getAlpha() {
        return this.alpha;
    }

	@Override
	public void setLineWidth(int i) {
		this.lineWidth = i;
	}

	@Override
	public FontMetrics getFontMetrics() {
		FontMetricsHandle fmh = new M3FontMetrics(skiaFont.getMetrics());

		FontMetrics fm = new FontMetrics();
		fm.innerFontMetrics = fmh;

		return fm;
	}

	@Override
	public int getAntialias() {
		return antialias;
	}

	@Override
	public void setAntialias(int antialias) {
		if (antialias != SWT.DEFAULT
				&& antialias != SWT.ON
				&& antialias != SWT.OFF) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.antialias = antialias;
	}

	@Override
	public void setAdvanced(boolean enable) {
	}

	private Rect offsetRectangle(Rect rect) {
		float scaledOffsetValue = getScaledOffsetValue();
		float widthHightAutoScaleOffset = DPIUtil.autoScaleUp(1) - 1.0f;
		if (scaledOffsetValue != 0f) {
			return new Rect(rect.getLeft() + scaledOffsetValue, rect.getTop() + scaledOffsetValue,
					rect.getRight() + scaledOffsetValue + widthHightAutoScaleOffset,
					rect.getBottom() + scaledOffsetValue + widthHightAutoScaleOffset);
		} else {
			return rect;
		}
	}

	private RRect offsetRectangle(RRect rect) {
		float scaledOffsetValue = getScaledOffsetValue();
		float widthHightAutoScaleOffset = DPIUtil.autoScaleUp(1) - 1.0f;
		if (scaledOffsetValue != 0f) {
			return new RRect(rect.getLeft() + scaledOffsetValue, rect.getTop() + scaledOffsetValue,
					rect.getRight() + scaledOffsetValue + widthHightAutoScaleOffset,
					rect.getBottom() + scaledOffsetValue + widthHightAutoScaleOffset, rect._radii);
		} else {
			return rect;
		}
	}

	private Rect createScaledRectangle(Rectangle r) {
		return createScaledRectangle(r.x, r.y, r.width, r.height);
	}

	private Rect createScaledRectangle(int x, int y, int width, int height) {
		return new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y), DPIUtil.autoScaleUp(x + width),
				DPIUtil.autoScaleUp(y + height));
	}

	private float getScaledOffsetValue() {
		boolean isDefaultLineWidth = lineWidth == 0;
		if (isDefaultLineWidth) {
			return 0.5f;
		}
		int effectiveLineWidth = DPIUtil.autoScaleUp(lineWidth);
		if (effectiveLineWidth % 2 == 1) {
			return DPIUtil.autoScaleUp(0.5f);
		}
		return 0f;
	}

	private RRect createScaledRoundRectangle(int x, int y, int width, int height, float arcWidth, float arcHeight) {
		return new RRect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y), DPIUtil.autoScaleUp(x + width),
				DPIUtil.autoScaleUp(y + height),
				new float[] { DPIUtil.autoScaleUp(arcWidth), DPIUtil.autoScaleUp(arcHeight) });
	}

	@Override
	public void setLineStyle(int lineStyle) {
		if (lineStyle != SWT.LINE_SOLID
				&& lineStyle != SWT.LINE_DASH
				&& lineStyle != SWT.LINE_DOT
				&& lineStyle != SWT.LINE_DASHDOT
				&& lineStyle != SWT.LINE_DASHDOTDOT
				&& lineStyle != SWT.LINE_CUSTOM) {
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
		LineAttributes attributes = getLineAttributesInPixels();
		attributes.width = DPIUtil.autoScaleDown(attributes.width);
		if (attributes.dash != null) {
			attributes.dash = DPIUtil.autoScaleDown(attributes.dash);
		}
		return attributes;
	}

	LineAttributes getLineAttributesInPixels() {
		return new LineAttributes(lineWidth, SWT.CAP_FLAT, SWT.JOIN_MITER, SWT.LINE_SOLID, null, 0, 10);
	}

	@Override
	public Rectangle getClipping() {
		return DPIUtil.autoScaleDown(getClippingInPixels());
	}

	Rectangle getClippingInPixels() {
		if (isDisposed()){
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return new Rectangle(
			DPIUtil.autoScaleUp(currentClipBounds.x),
			DPIUtil.autoScaleUp(currentClipBounds.y),
			DPIUtil.autoScaleUp(currentClipBounds.width),
			DPIUtil.autoScaleUp(currentClipBounds.height)
		);
	}

	@Override
	public Point stringExtent(String string) {
		return textExtent(string);
	}

	/**
	 * Returns the current line cap style used for drawing lines.
	 *
	 * The line cap style determines how the ends of lines are drawn.
	 *
	 * **Returns:**
	 *
	 * - `SWT.CAP_FLAT` – flat cap (default)
	 * - `SWT.CAP_ROUND` – rounded cap
	 * - `SWT.CAP_SQUARE` – square cap
	 *
	 * @return the current line cap style
	 */
	@Override
	public int getLineCap() {
		return lineCap;
	}

	@Override
	public void copyArea(Image image, int x, int y) {
		io.github.humbleui.skija.Image m3Image = convertSWTImageToM3Image(image);
		io.github.humbleui.skija.Image copiedArea = surface.makeImageSnapshot(
				createScaledRectangle(x, y, m3Image.getWidth(), m3Image.getHeight()).toIRect());

		if (copiedArea != null) {
			Surface imageSurface = surface.makeSurface(m3Image.getWidth(), m3Image.getHeight());
			Canvas imageCanvas = imageSurface.getCanvas();
			imageCanvas.drawImage(copiedArea, 0, 0);
			m3Image = imageSurface.makeImageSnapshot();
			ImageData imgData = convertToM3ImageData(m3Image);
			Image i = new Image(device, imgData);

			NativeGC gc = new NativeGC(image);
			gc.drawImage(i, 0,0 );
			gc.dispose();
			i.dispose();
		} else {
			System.err.println(
					"WARN: Area copied at given x,y co-ordinates is null: " + new Throwable().getStackTrace()[0]);
		}
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {

		io.github.humbleui.skija.Image copiedArea = surface
				.makeImageSnapshot(createScaledRectangle(srcX, srcY, width, height).toIRect());
		surface.getCanvas().drawImage(copiedArea, DPIUtil.autoScaleUp(destX), DPIUtil.autoScaleUp(destY));
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
		copyArea(srcX, srcY, width, height, destX, destY);
		if (paint) {
// cut-paste behaviour
//			surface.getCanvas().save();
//			surface.getCanvas().clipRect(createScaledRectangle(srcX, srcY, width, height));
//			surface.getCanvas().clear(0x00000000);
//			surface.getCanvas().restore();
			/** TODO - Implement correct behavior when paint is true **/
		}
	}

	@Override
	void checkGC(int mask) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	boolean isClipped() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	int getFillRule() {
		return fillRule;
	}

	@Override
	void getClipping(Region region) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected int getAdvanceWidth(char ch) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	protected boolean getAdvanced() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	protected Pattern getBackgroundPattern() {
		return backgroundPattern;
	}

	@Override
	protected int getCharWidth(char ch) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	protected Pattern getForegroundPattern() {
		return foregroundPattern;
	}

	@Override
	GCData getGCData() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	@Override
	protected int getInterpolation() {
		if (interpolationMode == SamplingMode.DEFAULT) {
	        return SWT.NONE;
	    } else if (interpolationMode == SamplingMode.LINEAR) {
	        return SWT.HIGH;
	    } else if (interpolationMode == SamplingMode.MITCHELL) {
	        return SWT.INTERPOLATION_MITCHELL;
	    } else if (interpolationMode == SamplingMode.CATMULL_ROM) {
	        return SWT.INTERPOLATION_CATMULL_ROM;
	    } else if (interpolationMode instanceof FilterMipmap) {
	        FilterMipmap fm = (FilterMipmap) interpolationMode;
	        if (fm.getFilterMode() == FilterMode.LINEAR &&
	            fm.getMipmapMode() == MipmapMode.LINEAR) {
	            return SWT.LOW;
	        }
	    }
	    return SWT.DEFAULT;
	}

	@Override
	protected int[] getLineDash() {
		if (lineDashes == null) return null;
		int[] lineDashesInt = new int[lineDashes.length];
		for (int i = 0; i < lineDashesInt.length; i++) {
			lineDashesInt[i] = DPIUtil.autoScaleDownToInt(lineDashes[i]);
		}
		return lineDashesInt;
	}

    /**
     * Returns the current line join style used for drawing lines.
     *
     * The line join style determines how the corners where two lines meet are drawn.
     *
     * **Returns:**
	 *
	 * - `SWT.JOIN_MITER` – sharp, pointed join (default)
	 * - `SWT.JOIN_ROUND` – rounded join
	 * - `SWT.JOIN_BEVEL` – flat, cut-off join
	 *
     * @return the current line join style
     */
    @Override
    protected int getLineJoin() {
        return lineJoin;
    }

	@Override
	int getStyle() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	protected int getTextAntialias() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	/**
	 * Copies the current transformation matrix into the specified Transform object.
	 * The transformation matrix represents the current scaling, rotation, translation,
	 * and shearing transformations applied to this graphics context.
	 *
	 * @param transform the Transform object to fill with the current transformation matrix,
	 *                  must not be null
	 * @throws SWTException if the transform parameter is null
	 */
	@Override
	protected void getTransform(Transform transform) {
		if (transform == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		float[] m = currentTransform.getMat();
		// Skija Matrix33: [scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2]
		// SWT Transform: [m11, m12, m21, m22, dx, dy]
		// Correct inverse mapping: Skija [0,1,2,3,4,5] -> SWT [0,3,1,4,2,5]
		transform.setElements(m[0], m[3], m[1], m[4], m[2], m[5]);
	}

	@Override
	protected boolean getXORMode() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	protected void setBackgroundPattern(Pattern pattern) {
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.backgroundPattern = pattern;
	}

	protected void setClipping(Path path) {
	    Canvas canvas = surface.getCanvas();
	    if (isClipSet) {
	        canvas.restore();
	        isClipSet = false;
	    }
	    if (path == null) {
	        return;
	    }
	    if (path.isDisposed()) {
	        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	    }
	    io.github.humbleui.skija.Path m3Path = convertSWTPathToM3Path(path);
	    if (m3Path == null) {
	        return;
	    }
	    m3Path.setFillMode(fillRule == SWT.FILL_EVEN_ODD
	        ? PathFillMode.EVEN_ODD
	        : PathFillMode.WINDING);
	    canvas.save();
	    isClipSet = true;
	    canvas.clipPath(m3Path, ClipMode.INTERSECT, true);
	}

	@Override
	public void setClipping(Rectangle rect) {
		/**
		 * this is a minimal implementation for set clipping with skija.
		 */

		// m3 seems to work with state layer which will be set on top of each other.
		// if more layers will be used a more complex handling is necessary
		Canvas canvas = surface.getCanvas();
		if (isClipSet) {
			canvas.restore();
			isClipSet = false;
		}	
		if (rect == null) {
			currentClipBounds = new Rectangle(0, 0, originalDrawingSize.x, originalDrawingSize.y);
			return;
		}		
		currentClipBounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
		canvas.save();
		canvas.clipRect(createScaledRectangle(rect));
		isClipSet = true;
	}

	@Override
	protected void setClipping(Region region) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	void setFillRule(int rule) {
		if (rule != SWT.FILL_EVEN_ODD && rule != SWT.FILL_WINDING) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.fillRule = rule;
	}

	@Override
	protected void setForegroundPattern(Pattern pattern) {
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.foregroundPattern = pattern;
	}
	@Override
	protected void setInterpolation(int interpolation) {
		switch (interpolation) {
		case SWT.NONE:
			this.interpolationMode = SamplingMode.DEFAULT; // Nearest neighbor
			break;
		case SWT.LOW:
			this.interpolationMode = new FilterMipmap(FilterMode.LINEAR, MipmapMode.LINEAR);
			break;
		case SWT.HIGH:
		case SWT.DEFAULT:
			this.interpolationMode = SamplingMode.LINEAR;
			break;

		case SWT.INTERPOLATION_MITCHELL:
			this.interpolationMode = SamplingMode.MITCHELL;
			break;
		case SWT.INTERPOLATION_CATMULL_ROM:
			this.interpolationMode = SamplingMode.CATMULL_ROM;
			break;

		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	@Override
	protected void setLineAttributes(LineAttributes attributes) {
		if (attributes == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		float scaledWidth = DPIUtil.autoScaleUp(attributes.width);
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
				if (attributes.dash == null)
					lineStyle = SWT.LINE_SOLID;
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.lineStyle = lineStyle;
			changed = true;
		}	
		// Handle line cap with validation
		int cap = attributes.cap;
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
		int join = attributes.join;
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
		float[] dashes = attributes.dash;
		float[] currentDashes = this.lineDashes;
		
		if (dashes != null && dashes.length > 0) {
			boolean dashesChanged = currentDashes == null || currentDashes.length != dashes.length;
			float[] newDashes = new float[dashes.length];

			for (int i = 0; i < dashes.length; i++) {
				float dash = dashes[i];
				if (dash <= 0)
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);

				// Scale dash values for DPI
				newDashes[i] = DPIUtil.autoScaleUp(dash);

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
		float dashOffset = attributes.dashOffset;
		if (this.dashOffset != dashOffset) {
			this.dashOffset = dashOffset;
			changed = true;
		}
		// Handle miter limit - store for use in performDrawLine()
		float miterLimit = attributes.miterLimit;
		if (this.miterLimit != miterLimit) {
			this.miterLimit = miterLimit;
			changed = true;
		}
		if (!changed){
			return;
		}
	}

	/**
	 * Sets the line cap style for drawing lines.
	 *
	 * The line cap style determines how the ends of lines are drawn.
	 *
	 * @param cap the line cap style to set. Must be one of:
	 * <ul>
	 *   <li>{@link SWT#CAP_FLAT} – flat cap (default)</li>
	 *   <li>{@link SWT#CAP_ROUND} – rounded cap</li>
	 *   <li>{@link SWT#CAP_SQUARE} – square cap</li>
	 * </ul>
	 * @throws SWTException if the cap style is not one of the valid values
	 */
    @Override
    protected void setLineCap(int cap) {
        if (cap != SWT.CAP_FLAT && cap != SWT.CAP_ROUND && cap != SWT.CAP_SQUARE) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        this.lineCap = cap;
    }

	@Override
	protected void setLineDash(int[] dashes) {
		if (dashes != null && dashes.length > 0) {
			boolean changed = this.lineStyle != SWT.LINE_CUSTOM || lineDashes == null || lineDashes.length != dashes.length;
			float[] newDashes = new float[dashes.length];
			for (int i = 0; i < dashes.length; i++) {
				if (dashes[i] <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				newDashes[i] = DPIUtil.autoScaleUp((float) dashes[i]);
				if (!changed && lineDashes != null && lineDashes[i] != newDashes[i]) changed = true;
			}
			if (!changed) return;
			this.lineDashes = newDashes;
			this.lineStyle = SWT.LINE_CUSTOM;
		} else {
			if (this.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) return;
			this.lineDashes = null;
			this.lineStyle = SWT.LINE_SOLID;
		}
	}

    /**
     * Sets the line join style for drawing lines.
     *
     * The line join style determines how the corners where two lines meet are drawn.
     *
     * @param join the line join style to set. Must be one of:
     * <ul>
     *   <li>{@link SWT#JOIN_MITER} – sharp, pointed join (default)</li>
     *   <li>{@link SWT#JOIN_ROUND} – rounded join</li>
     *   <li>{@link SWT#JOIN_BEVEL} – flat, cut-off join</li>
     * </ul>
     * @throws SWTException if the join style is not one of the valid values
     */
    @Override
    protected void setLineJoin(int join) {
        if (join != SWT.JOIN_MITER && join != SWT.JOIN_ROUND && join != SWT.JOIN_BEVEL) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        this.lineJoin = join;
    }

	@Override
	protected void setXORMode(boolean xor) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setTextAntialias(int antialias) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public boolean isDisposed() {
		return surface.isClosed();
	}

	static PaletteData getPaletteData(ColorType colorType) {
		// Note: RGB values here should be representative of a palette.

		// TODO test all mappings here
		switch (colorType) {
			case ALPHA_8:
				return new PaletteData(new RGB[] { new RGB(255, 255, 255), new RGB(0, 0, 0) });
			case RGB_565:
				return new PaletteData(0xF800, 0x07E0, 0x001F); // Mask for RGB565
			case ARGB_4444:
				return new PaletteData(0x0F00, 0x00F0, 0x000F); // Mask for ARGB4444
			case RGBA_8888:
				var p = new PaletteData(0xFF000000, 0x00FF0000, 0x0000FF00); // Standard RGBA masks
				return p;
			case BGRA_8888:
				return new PaletteData(0x0000FF00, 0x00FF0000, 0xFF000000);
			case RGBA_F16:
				return new PaletteData(new RGB[] { new RGB(255, 0, 0), // Example red
					new RGB(0, 255, 0), // Example green
					new RGB(0, 0, 255) }); // Example blue
			case RGBA_F32:
				return new PaletteData(new RGB[] { new RGB(255, 165, 0), // Example orange
					new RGB(0, 255, 255), // Example cyan
					new RGB(128, 0, 128) }); // Example purple
			default:
				throw new IllegalArgumentException("Unknown Skija ColorType: " + colorType);
		}
	}

	static int getImageDepth(ColorType colorType) {
		// TODO test all mappings
		switch (colorType) {
			case ALPHA_8:
				return 8;
			case RGB_565:
				return 16;
			case ARGB_4444:
				return 16;
			case RGBA_8888:
				return 32;
			case BGRA_8888:
				return 32;
			case RGBA_F16:
				// Typically could represent more colors, but SWT doesn't support floating-point
				// depths.
				return 64; // This is theoretical; SWT will usually not handle more than 32
			case RGBA_F32:
				// Same as RGBA_F16 with regards to SWT support
				return 128; // Theoretical; actual handling requires custom treatment
			default:
				throw new IllegalArgumentException("Unknown Skija ColorType: " + colorType);
		}
	}

	static ColorAlphaType determineAlphaType(ImageData imageData) {
		// TODO test all mappings
		if (imageData.alphaData == null && imageData.alpha == -1) {
			// no alpha
			return ColorAlphaType.OPAQUE;
		}

		if (imageData.alphaData != null || imageData.alpha < 255) {
			// alpha data available
			return ColorAlphaType.UNPREMUL;
		}

		// usually without additional information
		return ColorAlphaType.UNPREMUL;
	}

	private static Map<ColorType, int[]> createColorTypeMap() {
		if (colorTypeMap != null) {
			return colorTypeMap;
		}

		colorTypeMap = new HashMap<>();

		// define pixel order for m3 ColorType
		// what to do if the number of ints is not 4?
		colorTypeMap.put(ColorType.ALPHA_8, new int[] { 0 }); // only Alpha
		colorTypeMap.put(ColorType.RGB_565, new int[] { 0, 1, 2 }); // RGB
		colorTypeMap.put(ColorType.ARGB_4444, new int[] { 1, 2, 3, 0 }); // ARGB
		colorTypeMap.put(ColorType.RGBA_8888, new int[] { 0, 1, 2, 3 }); // RGBA
		colorTypeMap.put(ColorType.RGB_888X, new int[] { 0, 1, 2, 3 }); // RGB, ignore X
		colorTypeMap.put(ColorType.BGRA_8888, new int[] { 2, 1, 0, 3 }); // BGRA
		colorTypeMap.put(ColorType.RGBA_1010102, new int[] { 0, 1, 2, 3 }); // RGBA
		colorTypeMap.put(ColorType.BGRA_1010102, new int[] { 2, 1, 0, 3 }); // BGRA
		colorTypeMap.put(ColorType.RGB_101010X, new int[] { 0, 1, 2, 3 }); // RGB, ignore X
		colorTypeMap.put(ColorType.BGR_101010X, new int[] { 2, 1, 0, 3 }); // BGR, ignore X
		colorTypeMap.put(ColorType.RGBA_F16NORM, new int[] { 0, 1, 2, 3 }); // RGBA
	    colorTypeMap.put(ColorType.RGBA_F16, new int[] { 0, 1, 2, 3 }); // RGBA
		colorTypeMap.put(ColorType.RGBA_F32, new int[] { 0, 1, 2, 3 }); // RGBA
		colorTypeMap.put(ColorType.R8G8_UNORM, new int[] { 0, 1 }); // RG
		colorTypeMap.put(ColorType.A16_FLOAT, new int[] { 0 }); // Alpha
		colorTypeMap.put(ColorType.R16G16_FLOAT, new int[] { 0, 1 }); // RG
		colorTypeMap.put(ColorType.A16_UNORM, new int[] { 0 }); // Alpha
		colorTypeMap.put(ColorType.R16G16_UNORM, new int[] { 0, 1 }); // RG
		colorTypeMap.put(ColorType.R16G16B16A16_UNORM, new int[] { 0, 1, 2, 3 }); // RGBA

		return colorTypeMap;
	}

	public static int[] getPixelOrder(ColorType colorType) {
		Map<ColorType, int[]> colorTypeMap = createColorTypeMap();
		return colorTypeMap.get(colorType);
	}

	@Override
	public void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple) {
		// TODO Auto-generated method stub
	}
    /**
     * Converts an SWT Path to a Skija Path.
     */
	private io.github.humbleui.skija.Path convertSWTPathToM3Path(Path swtPath) {
        if (swtPath == null || swtPath.isDisposed()) return null;
        PathData data = swtPath.getPathData();
		io.github.humbleui.skija.Path m3Path = new io.github.humbleui.skija.Path();
        float[] pts = data.points;
        byte[] types = data.types;
        int pi = 0;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case SWT.PATH_MOVE_TO:
                    m3Path.moveTo(DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++]));
                    break;
                case SWT.PATH_LINE_TO:
                    m3Path.lineTo(DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++]));
                    break;
                case SWT.PATH_CUBIC_TO:
                    m3Path.cubicTo(
                        DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++]),
                        DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++]),
                        DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++])
                    );
                    break;
                case SWT.PATH_QUAD_TO:
                    m3Path.quadTo(
                        DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++]),
                        DPIUtil.autoScaleUp(pts[pi++]), DPIUtil.autoScaleUp(pts[pi++])
                    );
                    break;
                case SWT.PATH_CLOSE:
                    m3Path.closePath();
                    break;
                default:
            }
        }
        return m3Path;
    }
	
	@Override
	public void drawPolyline(int[] pointArray) {
		if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		// Check GC for draw operation (optional, for parity)
		checkGC(innerGC.DRAW);
		if (pointArray.length < 4 || pointArray.length % 2 != 0) return;

		// Handle SWT.MIRRORED style (adjust x-coordinates if needed)
		int style = getStyle();
		boolean mirrored = (style & SWT.MIRRORED) != 0;
		boolean adjustX = mirrored && lineWidth != 0 && lineWidth % 2 == 0;
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]--;
			}
		}
		// Draw polyline
		io.github.humbleui.skija.Path path = new io.github.humbleui.skija.Path();
		float[] pts = convertToFloat(pointArray);
		path.moveTo(pts[0], pts[1]);
		for (int i = 2; i < pts.length; i += 2) {
			path.lineTo(pts[i], pts[i + 1]);
		}
		performDrawLine(paint -> surface.getCanvas().drawPath(path, paint));

		// Draw last point if lineWidth <= 1 (to match SetPixel behavior)
		if (pointArray.length >= 2 && lineWidth <= 1) {
			int lastX = pointArray[pointArray.length - 2];
			int lastY = pointArray[pointArray.length - 1];
			drawPoint(lastX, lastY);
		}

		// Restore x-coordinates if mirrored
		if (adjustX) {
			for (int i = 0; i < pointArray.length; i += 2) {
				pointArray[i]++;
			}
		}
	}
	/**
	 * Applies the foreground pattern to the paint object if one is set.
	 * If no pattern is set, uses the foreground color.
	 * 
	 * @param paint the Paint object to apply the pattern to
	 */
	private void applyForegroundPattern(Paint paint) {
		if (foregroundPattern != null && !foregroundPattern.isDisposed()) {
			Shader shader = convertSWTPatternToM3Shader(foregroundPattern);
			if (shader != null) {
				paint.setShader(shader);
				return;
			}
		}
		// Fallback to foreground color if no pattern or pattern conversion failed
		paint.setColor(convertSWTColorToM3Color(getForeground()));
	}
	
	/**
	 * Converts an SWT Pattern to a Skija Shader.
	 * 
	 * @param pattern the SWT Pattern to convert
	 * @return the Skija Shader or null if conversion fails
	 */
	private Shader convertSWTPatternToM3Shader(Pattern pattern) {
		if (pattern == null || pattern.isDisposed()) {
			return null;
		}		
		Image patternImage = pattern.getImage();
		if (patternImage == null || patternImage.isDisposed()) {
			return null;
		}		
		io.github.humbleui.skija.Image m3Image = convertSWTImageToM3Image(patternImage);
		if (m3Image == null) {
			return null;
		}		
		return m3Image.makeShader(FilterTileMode.REPEAT, FilterTileMode.REPEAT, null);
	}
}

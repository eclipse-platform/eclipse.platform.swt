/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.

 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

public class SkijaGC extends GCHandle {
	private final Surface surface;
	private Rectangle clipping;

	private NativeGC innerGC;

	private Font font;
	private float baseSymbolHeight = 0; // Height of symbol with "usual" height, like "T", to be vertically centered
	private int lineWidth;

	private static Map<ColorType, int[]> colorTypeMap = null;

	public SkijaGC(org.eclipse.swt.widgets.Control c, int style) {

		innerGC = new NativeGC(c, style);
		innerGC.setFont(c.getFont());
		innerGC.setBackground(c.getBackground());
		innerGC.setForeground(c.getForeground());

		surface = createSurface(extractBackgroundColor(innerGC));
		clipping = innerGC.getClipping();
		initFont();
	}

	public SkijaGC(org.eclipse.swt.widgets.Control c) {
		this(c, SWT.None);
	}

	public SkijaGC(NativeGC gc, Color backgroundColor) {
		innerGC = gc;
		if (backgroundColor == null)
			backgroundColor = extractBackgroundColor(gc);
		surface = createSurface(backgroundColor);
		clipping = innerGC.getClipping();
		initFont();
	}

	@Override
	void initNonDisposeTracking() {
		// do not yet use resource handling for SkijaGC
		// TODO use the resource handling and prevent the error messages for not closed
		// resources.
	}

	private static Color extractBackgroundColor(NativeGC gc) {
		Rectangle originalGCArea = gc.getClipping();
		// Do not fill when using dummy GC for text extent calculation or when on cocoa
		// (as it does not have proper color)
		if (originalGCArea.isEmpty() || SWT.getPlatform().equals("cocoa")) {
			return null;
		}
		Image colorImage = new Image(gc.getDevice(), originalGCArea.width, originalGCArea.height);
		gc.copyArea(colorImage, 0, 0);
		int pixel = colorImage.getImageData().getPixel(0, 0);
		Color originalColor = SWT.convertPixelToColor(pixel);
		colorImage.dispose();
		return originalColor;
	}

	private Surface createSurface(Color backgroundColor) {
		int width = 1;
		int height = 1;
		Rectangle originalGCArea = innerGC.getClipping();
		if (!originalGCArea.isEmpty()) {
			width = DPIUtil.autoScaleUp(originalGCArea.width);
			height = DPIUtil.autoScaleUp(originalGCArea.height);
		}
		Surface surface = Surface.makeRaster(ImageInfo.makeN32Premul(width, height), 0,
				new SurfaceProps(PixelGeometry.RGB_H));
		if (backgroundColor != null) {
			surface.getCanvas().clear(convertSWTColorToSkijaColor(backgroundColor));
		}
		return surface;
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
		this.innerGC.dispose();
	}

	@Override
	public Color getBackground() {
		return innerGC.getBackground();
	}

	private void performDraw(Consumer<Paint> operations) {
		Paint paint = new Paint();
		operations.accept(paint);
		paint.close();
	}

	private void performDrawLine(Consumer<Paint> operations) {
		performDraw(paint -> {
			paint.setColor(convertSWTColorToSkijaColor(getForeground()));
			paint.setMode(PaintMode.STROKE);
			paint.setStrokeWidth(DPIUtil.autoScaleUp(lineWidth));
			paint.setAntiAlias(true);
			operations.accept(paint);
		});
	}

	private void performDrawText(Consumer<Paint> operations) {
		performDraw(paint -> {
			paint.setColor(convertSWTColorToSkijaColor(getForeground()));
			operations.accept(paint);
		});
	}

	private void performDrawFilled(Consumer<Paint> operations) {
		performDraw(paint -> {
			paint.setColor(convertSWTColorToSkijaColor(getBackground()));
			paint.setMode(PaintMode.FILL);
			paint.setAntiAlias(true);
			operations.accept(paint);
		});
	}

	private void performDrawPoint(Consumer<Paint> operations) {
		performDraw(paint -> {
			paint.setColor(convertSWTColorToSkijaColor(getForeground()));
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
		io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
		byte[] imageBytes = EncoderPNG.encode(im).getBytes();

		Image transferImage = new Image(innerGC.getDevice(), new ByteArrayInputStream(imageBytes));

		Rectangle originalArea = innerGC.getClipping();
		Rectangle scaledArea = DPIUtil.autoScaleUp(originalArea);
		innerGC.drawImage(transferImage, 0, 0, scaledArea.width, scaledArea.height, //
				0, 0, originalArea.width, originalArea.height);
		transferImage.dispose();
		surface.close();
	}

	@Override
	public Point textExtent(String string) {
		return textExtent(string, SWT.NONE);
	}

	@Override
	public void setBackground(Color color) {
		innerGC.setBackground(color);
	}

	@Override
	public void setForeground(Color color) {
		innerGC.setForeground(color);
	}

	@Override
	public void fillRectangle(Rectangle rect) {
		fillRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawImage(Image image, int x, int y) {
		Canvas canvas = surface.getCanvas();
		canvas.drawImage(convertSWTImageToSkijaImage(image), DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {
		Canvas canvas = surface.getCanvas();
		canvas.drawImageRect(convertSWTImageToSkijaImage(image),
				createScaledRectangle(srcX, srcY, srcWidth, srcHeight),
				createScaledRectangle(destX, destY, destWidth, destHeight));
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

		throw new UnsupportedOperationException("Unsupported SWT ColorType: " + Integer.toBinaryString(palette.redMask)
				+ "__" + Integer.toBinaryString(palette.greenMask) + "__" + Integer.toBinaryString(palette.blueMask));
	}

	private static io.github.humbleui.skija.Image convertSWTImageToSkijaImage(Image swtImage) {
		ImageData imageData = swtImage.getImageData(DPIUtil.getDeviceZoom());
		return convertSWTImageToSkijaImage(imageData);
	}

	static io.github.humbleui.skija.Image convertSWTImageToSkijaImage(ImageData imageData) {
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

		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				int pixel = imageData.getPixel(x, y);
				int arrayPos = (y * imageData.width + x);

				byte r = (byte) ((pixel & imageData.palette.redMask) >>> -imageData.palette.redShift);
				byte g = (byte) ((pixel & imageData.palette.greenMask) >>> -imageData.palette.greenShift);
				byte b = (byte) ((pixel & imageData.palette.blueMask) >>> -imageData.palette.blueShift);

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

				if (alphaData != null && alphaData.length > arrayPos) {
					convertedData[index + 3] = alphaData[arrayPos];
				} else if (imageData.alpha != -1) {
					convertedData[index + 3] = defaultAlpha;
				} else if(!byteSourceContainsAlpha) {
					convertedData[index + 3] = defaultAlpha;
				}
			}
		}

		return convertedData;
	}

	static ImageData convertToSkijaImageData(io.github.humbleui.skija.Image image) {
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

	public static int convertSWTColorToSkijaColor(Color swtColor) {
		// extract RGB-components
		int red = swtColor.getRed();
		int green = swtColor.getGreen();
		int blue = swtColor.getBlue();
		int alpha = swtColor.getAlpha();

		// create ARGB 32-Bit-color
		int skijaColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return skijaColor;
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
		return innerGC.getForeground();
	}

	@Override
	public void drawText(String string, int x, int y) {
		drawText(string, x, y, SWT.NONE);
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
		drawText(string, x, y, SWT.TRANSPARENT);
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {
		if (text == null) {
			return;
		}
		performDrawText(paint -> {
			TextBlob textBlob = buildTextBlob(text);
			Point point = calculateSymbolCenterPoint(x, y);
			surface.getCanvas().drawTextBlob(textBlob, point.x, point.y, paint);
		});
	}

	// y position in drawTextBlob() is the text baseline, e.g., the bottom of "T"
	// but the middle of "y"
	// So center a base symbol (like "T") in the desired text box (according to
	// parameter y being the top left text box position and the text box height
	// according to font metrics)
	private Point calculateSymbolCenterPoint(int x, int y) {
		int topLeftTextBoxYPosition = DPIUtil.autoScaleUp(y);
		float heightOfTextBoxConsideredByClients = font.getMetrics().getHeight();
		float heightOfSymbolToCenter = baseSymbolHeight;
		Point point = new Point((int) DPIUtil.autoScaleUp(x),
				(int) (topLeftTextBoxYPosition + heightOfTextBoxConsideredByClients / 2 + heightOfSymbolToCenter / 2));
		return point;
	}

	private TextBlob buildTextBlob(String text) {
		text = replaceMnemonics(text);
		String[] lines = splitString(text);
		TextBlobBuilder blobBuilder = new TextBlobBuilder();
		float lineHeight = font.getMetrics().getHeight();
		int yOffset = 0;
		for (String line : lines) {
			blobBuilder.appendRun(font, line, 0, yOffset);
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

	@Override
	public void drawPath(Path path) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawPoint(int x, int y) {
		performDrawPoint(paint -> surface.getCanvas().drawRect(createScaledRectangle(x, y, 1, 1), paint));
	}

	@Override
	public void drawPolygon(int[] pointArray) {
		performDrawLine(paint -> surface.getCanvas().drawPolygon(convertToFloat(pointArray), paint));
	}

	@Override
	public void drawPolyline(int[] pointArray) {
		performDrawLine(paint -> surface.getCanvas().drawLines(convertToFloat(pointArray), paint));
	}

	private static float[] convertToFloat(int[] array) {
		float[] arrayAsFloat = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			arrayAsFloat[i] = array[i];
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
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		Point point = calculateSymbolCenterPoint(x, y);
		performDrawText(paint -> {
			surface.getCanvas().drawString(string, point.x, point.y, font, paint);
		});
	}

	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (!isTransparent) {
			int width = (int) DPIUtil.autoScaleDown(font.measureTextWidth(string));
			int height = (int) DPIUtil.autoScaleDown(font.getMetrics().getHeight());
			fillRectangle(x, y, width, height);
		}
		drawString(string, x, y);
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
		int fromColor = convertSWTColorToSkijaColor(getForeground());
		int toColor = convertSWTColorToSkijaColor(getBackground());
		if (fromColor == toColor) {
			performDrawFilled(paint -> surface.getCanvas().drawRect(rect, paint));
			return;
		}
		if (swapColors) {
			int tempColor = convertSWTColorToSkijaColor(getForeground());
			fromColor = convertSWTColorToSkijaColor(getBackground());
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

	@Override
	public void fillPath(Path path) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillPolygon(int[] pointArray) {

		boolean isXCoord = true;
		int xCoord = 0;
		List<io.github.humbleui.types.Point> ps = new ArrayList<>();

		for (int i : pointArray) {

			if (isXCoord) {
				xCoord = i;
				isXCoord = false;
			} else {
				ps.add(new io.github.humbleui.types.Point(xCoord, i));
				isXCoord = true;
			}
		}

		performDrawFilled(paint -> surface.getCanvas().drawTriangles(ps.toArray(new io.github.humbleui.types.Point[0]),
				null, paint));
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
		Rect textExtent = this.font.measureText(replaceMnemonics(string));
		return DPIUtil.autoScaleDown(new Point(Math.round(textExtent.getWidth()), getFontMetrics().getHeight()));
	}

	@Override
	public void setFont(org.eclipse.swt.graphics.Font font) {
		innerGC.setFont(font);

		if (font == null)
			font = innerGC.getFont();

		FontData fontData = font.getFontData()[0];
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
		this.font = new Font(Typeface.makeFromName(fontData.getName(), style));
		int fontSize = DPIUtil.scaleUp(fontData.getHeight(), DPIUtil.getNativeDeviceZoom());
		if (SWT.getPlatform().equals("win32")) {
			fontSize *= this.font.getSize() / innerGC.getDevice().getSystemFont().getFontData()[0].getHeight();
		}
		this.font.setSize(fontSize);
		this.font.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
		this.font.setSubpixel(true);
		this.baseSymbolHeight = this.font.measureText("T").getHeight();
	}

	@Override
	public org.eclipse.swt.graphics.Font getFont() {
		return innerGC.getFont();
	}

	@Override
	public void setClipping(int x, int y, int width, int height) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void setTransform(Transform transform) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void setAlpha(int alpha) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public int getAlpha() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	public void setLineWidth(int i) {
		this.lineWidth = i;
	}

	@Override
	public FontMetrics getFontMetrics() {
		FontMetricsHandle fmh = new SkijaFontMetrics(font.getMetrics());

		FontMetrics fm = new FontMetrics();
		fm.innerFontMetrics = fmh;

		return fm;
	}

	@Override
	public int getAntialias() {
		return innerGC.getAntialias();
	}

	@Override
	public void setAntialias(int antialias) {
		innerGC.setAntialias(antialias);
	}

	@Override
	public void setAdvanced(boolean enable) {
		innerGC.setAdvanced(enable);
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
	public void setLineStyle(int lineDot) {
		innerGC.setLineStyle(lineDot);

	}

	@Override
	public int getLineStyle() {
		return innerGC.getLineStyle();
	}

	@Override
	public int getLineWidth() {
		return innerGC.getLineWidth();
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
		return new Rectangle(clipping.x, clipping.y, clipping.width, clipping.height);
	}

	@Override
	public Point stringExtent(String string) {
		return textExtent(string);
	}

	@Override
	public int getLineCap() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	public void copyArea(Image image, int x, int y) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
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
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	@Override
	protected int getCharWidth(char ch) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	protected Pattern getForegroundPattern() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	@Override
	GCData getGCData() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	@Override
	protected int getInterpolation() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	protected int[] getLineDash() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	@Override
	protected int getLineJoin() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
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

	@Override
	protected void getTransform(Transform transform) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected boolean getXORMode() {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	protected void setBackgroundPattern(Pattern pattern) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setClipping(Path path) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	@Override
	protected void setClipping(Rectangle rect) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setClipping(Region region) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	void setFillRule(int rule) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setForegroundPattern(Pattern pattern) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setInterpolation(int interpolation) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setLineAttributes(LineAttributes attributes) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setLineCap(int cap) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setLineDash(int[] dashes) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	protected void setLineJoin(int join) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
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

		// define pixel order for skija ColorType
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
}

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
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

	public final class SkijaGC extends GC {


		SkijaGC() {
			this.surface = null;
			originalDrawingSize = null;
		}

		private static final Map<FontData, Font> FONT_CACHE = new ConcurrentHashMap<>();

		public static SkijaGC createDefaultInstance(GC gc) {
			return new SkijaGC(gc, gc.drawable, false);
		}

		public static SkijaGC createDefaultInstance(GC gc, Control control) {
			return new SkijaGC(gc, control, false);
		}

		public static SkijaGC createMeasureInstance(GC gc, Control control) {
			return new SkijaGC(gc, control, true);
		}

		private Surface surface;

		private GC innerGC;

		private Color background;
		private Color foreground;
		private org.eclipse.swt.graphics.Font swtFont;
		private Font skiaFont;
		private float baseSymbolHeight = 0; // Height of symbol with "usual" height, like "T", to be vertically centered
		private int lineWidth;
		private int lineStyle;
		private int antialias;

		private Point originalDrawingSize;

		private static Map<ColorType, int[]> colorTypeMap = null;

		private SkijaGC(GC gc, Drawable drawable, boolean onlyForMeasuring) {
			innerGC = gc;
			device = gc.device;
			originalDrawingSize = extractSize(drawable);

			this.surface = ((SkiaCanvas)drawable).surface;

//			if (onlyForMeasuring) {
//				surface = createMeasureSurface();
//			} else {
//				surface = createDrawingSurface();
//				initializeWithParentBackground();
//			}
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
			// do not yet use resource handling for SkijaGC
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
			glFinishDrawing();
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
			operations.accept(paint);
			paint.close();
		}

		private void performDrawLine(Consumer<Paint> operations) {
			performDraw(paint -> {
				paint.setColor(convertSWTColorToSkijaColor(getForeground()));
				paint.setMode(PaintMode.STROKE);
				paint.setStrokeWidth(lineWidth > 0 ? DPIUtil.autoScaleUp(lineWidth) : 1);
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

//		@Override
//		public void commit() {
//			if (isEmpty(originalDrawingSize)) {
//				return;
//			}
//			io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
//			byte[] imageBytes = EncoderPNG.encode(im).getBytes();
//
//			Image transferImage = new Image(innerGC.getDevice(), new ByteArrayInputStream(imageBytes));
//
//			Point drawingSizeInPixels = DPIUtil.autoScaleUp(originalDrawingSize);
//			innerGC.drawImage(transferImage, 0, 0, drawingSizeInPixels.x, drawingSizeInPixels.y, //
//					0, 0, originalDrawingSize.x, originalDrawingSize.y);
//			transferImage.dispose();
//		}

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
			canvas.drawImage(convertSWTImageToSkijaImage(image), DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
		}

		@Override
		public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
				int destWidth, int destHeight) {
			if (image == null) {
				System.out.println("SkijaGC.drawImage(..): Error draw image that is null!!");
				return;
			}
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

//			throw new UnsupportedOperationException("Unsupported SWT ColorType: " + Integer.toBinaryString(palette.redMask)
//					+ "__" + Integer.toBinaryString(palette.greenMask) + "__" + Integer.toBinaryString(palette.blueMask));
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
//			ColorAlphaType alphaType = bm.getAlphaType();

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

		@Override
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

//		@Override
//		public void drawRectangle(Rectangle rect) {
//			drawRectangle(rect.x, rect.y, rect.width, rect.height);
//		}

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
			List<io.github.humbleui.types.Point> ps = new ArrayList<>();

			for (int i = 0; i < pointArray.length; i += 2) {
				int x = DPIUtil.autoScaleUp(pointArray[i]);
				int y = DPIUtil.autoScaleUp(pointArray[i + 1]);
				ps.add(new io.github.humbleui.types.Point(x, y));
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
			float height = skiaFont.getMetrics().getHeight();
			float width = skiaFont.measureTextWidth(replaceMnemonics(string));
			return new Point(DPIUtil.autoScaleDown((int)width), DPIUtil.autoScaleDown((int)height));
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

			this.skiaFont = convertToSkijaFont(font);
			this.baseSymbolHeight = this.skiaFont.measureText("T").getHeight();
		}

		static Font convertToSkijaFont(org.eclipse.swt.graphics.Font font) {
			FontData fontData = font.getFontData()[0];
			var cachedFont = FONT_CACHE.get(fontData);
			if (cachedFont != null && cachedFont.isClosed()) {
				FONT_CACHE.remove(fontData);
			}
			return FONT_CACHE.computeIfAbsent(fontData, SkijaGC::createSkijaFont);
		}

		static Font createSkijaFont(FontData fontData) {
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
			Font skijaFont = new Font(Typeface.makeFromName(fontData.getName(), style));
			int fontSize = DPIUtil.scaleUp(fontData.getHeight(), DPIUtil.getNativeDeviceZoom());
			if (SWT.getPlatform().equals("win32")) {
				fontSize *= skijaFont.getSize() / Display.getDefault().getSystemFont().getFontData()[0].getHeight();
			}
			skijaFont.setSize(fontSize);
			skijaFont.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
			skijaFont.setSubpixel(true);
			return skijaFont;
		}

		@Override
		public org.eclipse.swt.graphics.Font getFont() {
			return swtFont;
		}

		@Override
		public void setClipping(int x, int y, int width, int height) {
			setClipping(new Rectangle(x, y, width, height));
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

//		@Override
//		public FontMetrics getFontMetrics() {
//			FontMetricsHandle fmh = new SkijaFontMetrics(skiaFont.getMetrics());
//
//			FontMetrics fm = new FontMetrics();
//			fm.innerFontMetrics = fmh;
//
//			return fm;
//		}

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

		@Override
		LineAttributes getLineAttributesInPixels() {
			return new LineAttributes(lineWidth, SWT.CAP_FLAT, SWT.JOIN_MITER, SWT.LINE_SOLID, null, 0, 10);
		}

		@Override
		public Rectangle getClipping() {
			return innerGC.getClipping();
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
			io.github.humbleui.skija.Image skijaImage = convertSWTImageToSkijaImage(image);
			io.github.humbleui.skija.Image copiedArea = surface.makeImageSnapshot(
					createScaledRectangle(x, y, skijaImage.getWidth(), skijaImage.getHeight()).toIRect());

			if (copiedArea != null) {
				Surface imageSurface = surface.makeSurface(skijaImage.getWidth(), skijaImage.getHeight());
				Canvas imageCanvas = imageSurface.getCanvas();
				imageCanvas.drawImage(copiedArea, 0, 0);
				skijaImage = imageSurface.makeImageSnapshot();

				// This does not work...
				assert(1 == 0);

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
//				surface.getCanvas().save();
//				surface.getCanvas().clipRect(createScaledRectangle(srcX, srcY, width, height));
//				surface.getCanvas().clear(0x00000000);
//				surface.getCanvas().restore();
				/** TODO - Implement correct behavior when paint is true **/
			}
		}

		@Override
		void checkGC(int mask) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public boolean isClipped() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return false;
		}

		@Override
		public int getFillRule() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public
		void getClipping(Region region) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public int getAdvanceWidth(char ch) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public boolean getAdvanced() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return false;
		}

		@Override
		public Pattern getBackgroundPattern() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return null;
		}

		@Override
		public int getCharWidth(char ch) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public Pattern getForegroundPattern() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return null;
		}

		@Override
		public
		GCData getGCData() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return null;
		}

		@Override
		public int getInterpolation() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public int[] getLineDash() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return null;
		}

		@Override
		public int getLineJoin() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public
		int getStyle() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public int getTextAntialias() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return 0;
		}

		@Override
		public void getTransform(Transform transform) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public boolean getXORMode() {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
			return false;
		}

		@Override
		public void setBackgroundPattern(Pattern pattern) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setClipping(Path path) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

		}

		@Override
		public void setClipping(Rectangle rect) {

			/**
			 * this is a minimal implementation for set clipping with skija.
			 */

			// skija seems to work with state layer which will be set on top of each other.
			// if more layers will be used a more complex handling is necessary
			surface.getCanvas().restore();
			if (rect == null) {
				return;
			}

			surface.getCanvas().save();
			surface.getCanvas().clipRect(createScaledRectangle(rect));

		}

		@Override
		public void setClipping(Region region) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public
		void setFillRule(int rule) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setForegroundPattern(Pattern pattern) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setInterpolation(int interpolation) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setLineAttributes(LineAttributes attributes) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setLineCap(int cap) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setLineDash(int[] dashes) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setLineJoin(int join) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setXORMode(boolean xor) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		}

		@Override
		public void setTextAntialias(int antialias) {
			System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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




	@Override
	public void drawRectangle (Rectangle rect) {
		if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		rect = DPIUtil.scaleUp(drawable, rect, getZoom());
		drawRectangleInPixels(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	void init(Drawable drawable, GCData data, long hDC) {
		super.init(drawable, data, hDC);

		surface = ((SkiaCanvas)drawable).surface;
		glPrepareSurface();


	}


	protected void glFinishDrawing() {
		var s =  ( SkiaCanvas )drawable;
		s.skijaContext.flush();
		s.swapBuffers();
	}

	protected void glPrepareSurface() {
		var s =  ( SkiaCanvas )drawable;
		io.github.humbleui.skija.Canvas canvas = s.surface.getCanvas();
		canvas.clear(0xFFFFFFFF);
		s.redraw();
	}



	@Override
	void drawRectangleInPixels (int x, int y, int width, int height) {

		SkiaCanvas c =  ( SkiaCanvas )drawable;


		Paint p = new Paint();

		p.setColor(0xFF0000FF);

		c.surface.getCanvas().drawRect(createScaledRectangle(x,y,width ,height  ), p   );

		p.close();

	}


}

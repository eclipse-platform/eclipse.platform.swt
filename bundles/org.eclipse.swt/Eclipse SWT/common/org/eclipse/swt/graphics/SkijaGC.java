package org.eclipse.swt.graphics;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

public class SkijaGC implements IGraphicsContext {
	private final GC innerGC;
	private final Surface surface;

	private Font font;
	private float baseSymbolHeight = 0; // Height of symbol with "usual" height, like "T", to be vertically centered
	private int lineWidth;

	public SkijaGC(Drawable drawable, int style) {
		this(new GC(drawable, style));
	}

	public SkijaGC(GC gc) {
		innerGC = gc;
		surface = createSurface();
		initFont();
	}

	private Surface createSurface() {
		int width = 1;
		int height = 1;
		Rectangle originalGCArea = innerGC.getClipping();
		if (!originalGCArea.isEmpty()) {
			width = DPIUtil.autoScaleUp(originalGCArea.width);
			height = DPIUtil.autoScaleUp(originalGCArea.height);
		}
		Surface surface = Surface.makeRaster(ImageInfo.makeN32Premul(width, height), 0,
				new SurfaceProps(PixelGeometry.RGB_H));
		fillSurfaceWithDefaultBackground(surface);
		return surface;
	}

	private void fillSurfaceWithDefaultBackground(Surface surface) {
		Rectangle originalGCArea = innerGC.getClipping();
		// Do not fill when using dummy GC for text extent calculation or when on cocoa
		// (as it does not have proper color)
		if (originalGCArea.isEmpty() || SWT.getPlatform().equals("cocoa")) {
			return;
		}
		Image colorImage = new Image(innerGC.getDevice(), originalGCArea.width, originalGCArea.height);
		innerGC.copyArea(colorImage, 0, 0);
		int pixel = colorImage.getImageData().getPixel(0, 0);
		Color originalColor = new Color((pixel & 0xFF000000) >>> 24, (pixel & 0xFF0000) >>> 16, (pixel & 0xFF00) >>> 8);
		surface.getCanvas().clear(convertSWTColorToSkijaColor(originalColor));
		colorImage.dispose();
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
	}

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
				new Rect(DPIUtil.autoScaleUp(srcX), DPIUtil.autoScaleUp(srcY), DPIUtil.autoScaleUp(srcX + srcWidth),
						DPIUtil.autoScaleUp(srcY + srcHeight)),
				new Rect(DPIUtil.autoScaleUp(destX), DPIUtil.autoScaleUp(destY), DPIUtil.autoScaleUp(destX + destWidth),
						DPIUtil.autoScaleUp(destY + destHeight)));

	}

	private static ColorType getSkijaColorType(ImageData imageData) {
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

			if (redMask == 0xFF00000 && greenMask == 0x00FF0000 && blueMask == 0x0000FF00) {
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

		// Additional mappings for more complex or less common types
		// You may need additional logic or assumptions here

		// Fallback for unsupported or unknown types
		throw new UnsupportedOperationException("Unsupported ColorType");
	}

	private static io.github.humbleui.skija.Image convertSWTImageToSkijaImage(Image swtImage) {
		ImageData imageData = swtImage.getImageData(DPIUtil.getDeviceZoom());

		int width = imageData.width;
		int height = imageData.height;
		ColorType colType = getSkijaColorType(imageData);

		if (colType.equals(ColorType.UNKNOWN)) {
			imageData = convertToRGBA(imageData);
			colType = ColorType.RGBA_8888;
		}
		ImageInfo imageInfo = new ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.UNPREMUL);

		return io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo, imageData.data, imageData.bytesPerLine);
	}

	private static ImageData convertToRGBA(ImageData imageData) {
		ImageData transparencyData = imageData.getTransparencyMask();
		byte[] convertedData = new byte[imageData.data.length];
		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				byte alpha = (byte) 255;
				if (transparencyData != null) {
					if (imageData.getTransparencyMask().getPixel(x, y) != 1) {
						alpha = (byte) 0;
					}
				}
				int pixel = imageData.getPixel(x, y);
				byte red = (byte) ((pixel & imageData.palette.redMask) >>> -imageData.palette.redShift);
				byte green = (byte) ((pixel & imageData.palette.greenMask) >>> -imageData.palette.greenShift);
				byte blue = (byte) ((pixel & imageData.palette.blueMask) >>> -imageData.palette.blueShift);

				int index = (x + y * imageData.width) * 4;
				convertedData[index] = red;
				convertedData[index + 1] = green;
				convertedData[index + 2] = blue;
				convertedData[index + 3] = alpha;
			}
		}
		imageData.data = convertedData;
		return imageData;
	}


	// Funktion zur Konvertierung der Farbe
	public static int convertSWTColorToSkijaColor(Color swtColor) {
		// Extrahieren der RGB-Komponenten
		int red = swtColor.getRed();
		int green = swtColor.getGreen();
		int blue = swtColor.getBlue();
		int alpha = swtColor.getAlpha();

		// Erstellen der Skija-Farbe: ARGB 32-Bit-Farbe
		int skijaColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return skijaColor;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setAntiAlias(true);
		p.setStrokeWidth(lineWidth);
		surface.getCanvas().drawLine(x1, y1, x2, y2, p);
		p.close();
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
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {
		if (text == null) {
			return;
		}
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		TextBlob textBlob = buildTextBlob(text);
		// y position in drawTextBlob() is the text baseline, e.g., the bottom of "T"
		// but the middle of "y"
		// So center a base symbol (like "T") in the desired text box (according to
		// parameter y being the top left text box position and the text box height
		// according to font metrics)
		int topLeftTextBoxYPosition = DPIUtil.autoScaleUp(y);
		float heightOfTextBoxConsideredByClients = font.getMetrics().getHeight();
		float heightOfSymbolToCenter = baseSymbolHeight;
		surface.getCanvas().drawTextBlob(textBlob, (int) DPIUtil.autoScaleUp(x),
				(int) (topLeftTextBoxYPosition + heightOfTextBoxConsideredByClients / 2 + heightOfSymbolToCenter / 2),
				p);
		p.close();
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
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawFocus(int x, int y, int width, int height) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setMode(PaintMode.STROKE);
		p.setStrokeWidth(lineWidth);
		p.setAntiAlias(true);
		surface.getCanvas().drawOval(createScaledAndOffsetRectangle(x, y, width, height), p);
		p.close();
	}

	public void drawPath(Path path) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawPoint(int x, int y) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawPolygon(int[] pointArray) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawPolyline(int[] pointArray) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setMode(PaintMode.STROKE);
		p.setStrokeWidth(lineWidth);
		p.setAntiAlias(true);
		surface.getCanvas().drawRect(createScaledAndOffsetRectangle(x, y, width, height), p);
		p.close();
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		drawRectangle(x, y, width, height);
	}

	public void drawString(String string, int x, int y) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawString(String string, int x, int y, boolean isTransparent) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getBackground()));
		p.setMode(PaintMode.FILL);
		p.setAntiAlias(true);
		surface.getCanvas().drawOval(createScaledAndOffsetRectangle(x, y, width, height), p);
		p.close();
	}

	public void fillPath(Path path) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillPolygon(int[] pointArray) {
		System.err.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getBackground()));
		p.setMode(PaintMode.FILL);
		p.setAntiAlias(true);
		surface.getCanvas().drawRect(createScaledAndOffsetRectangle(x, y, width, height), p);
		p.close();
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		fillRectangle(x, y, width, height);
	}

	@Override
	public Point textExtent(String string, int flags) {
		Rect textExtent = this.font.measureText(replaceMnemonics(string));
		return DPIUtil.autoScaleDown(new Point(Math.round(textExtent.getWidth()), getFontMetrics().getHeight()));
	}

	@Override
	public void setFont(org.eclipse.swt.graphics.Font font) {
		innerGC.setFont(font);
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
		int fontSize = DPIUtil.autoScaleUp(fontData.getHeight());
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
	public IFontMetrics getFontMetrics() {
		IFontMetrics fm = new SkijaFontMetrics(font.getMetrics());
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

	private Rect createScaledAndOffsetRectangle(int x, int y, int width, int height) {
		return new Rect(DPIUtil.autoScaleUp(x + 0.5f), DPIUtil.autoScaleUp(y + 0.5f),
				DPIUtil.autoScaleUp(x - 0.5f + width), DPIUtil.autoScaleUp(y - 0.5f + height));
	}

}

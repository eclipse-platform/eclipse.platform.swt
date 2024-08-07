package org.eclipse.swt.widgets;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

public class SkijaGC extends GC {

	public final static int FONT_DEFAULT_SIZE = 12;

	private Surface surface;
	private Rectangle r;
	private GC innerGC;

	public SkijaGC(Drawable drawable, int style) {
		super(drawable, style);

		r = getClipping();
		surface = Surface
				.makeRaster(ImageInfo.makeN32Premul(r.width, r.height));

	}

	public SkijaGC(GC innerGC) {
		this(innerGC.getDevice(), innerGC.getStyle());
		this.innerGC = innerGC;
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
				lines[lines.length - 1] = text.substring(start,
						pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}

	public void commit() {

		// Surface bgSurface = Surface
		// .makeRaster(ImageInfo.makeN32Premul(r.width, r.height));
		//
		//
		// Canvas bgc = bgSurface.getCanvas();
		// Paint bgPaint = new Paint().setColor(getBackground().handle);
		// bgc.drawRect(Rect.makeWH(r.width, r.height), bgPaint);
		// bgPaint.close();
		//
		// io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
		// // bgc.drawImage(im, 0, 0);
		//
		// byte[] imageBytes = EncoderPNG.encode(im).getBytes();
		//
		// Image i = new Image(getDevice(), new
		// ByteArrayInputStream(imageBytes));
		// super.drawImage(i, 0, 0);

		io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
		byte[] imageBytes = EncoderPNG.encode(im).getBytes();

		Image i = new Image(getDevice(), new ByteArrayInputStream(imageBytes));

		if (innerGC != null)
			innerGC.drawImage(i, 0, 0);
		else
			super.drawImage(i, 0, 0);

	}

	@Override
	public Point textExtent(String string) {

		Typeface typeface = Typeface.makeDefault();
		Font f = new Font(typeface);
		Rect mT = f.measureText(string);

		return new Point((int) mT.getHeight(), (int) mT.getWidth());

	}

	@Override
	public void setBackground(Color color) {

		super.setBackground(color);

		// TODO we will need a trigger to ensure redraw

	}

	@Override
	public void setForeground(Color color) {

		super.setForeground(color);

		// TODO i guess there we don't need a trigger for redraw

	}

	@Override
	public void fillRectangle(Rectangle rect) {

		Canvas canvas = surface.getCanvas();
		Paint bgPaint = new Paint().setColor(getBackground().handle);
		canvas.drawRRect(
				new RRect(rect.x, rect.y, rect.width, rect.height, null),
				bgPaint);

		bgPaint.close();
	}

	@Override
	public void drawImage(Image image, int x, int y) {

		Canvas canvas = surface.getCanvas();
		canvas.drawImage(convertSWTImageToSkijaImage(image), x, y);

	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth,
			int srcHeight, int destX, int destY, int destWidth,
			int destHeight) {

		Canvas canvas = surface.getCanvas();
		canvas.drawImageRect(convertSWTImageToSkijaImage(image),
				new Rect(srcX, srcY, srcWidth, srcHeight),
				new Rect(destX, destY, destWidth, destHeight));

	}

	private static io.github.humbleui.skija.Image convertSWTImageToSkijaImage(
			Image swtImage) {
		ImageData imageData = swtImage.getImageData();

		int width = imageData.width;
		int height = imageData.height;

		ImageInfo imageInfo = new ImageInfo(width, height, ColorType.BGRA_8888,
				ColorAlphaType.PREMUL);


		return io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo,
				imageData.data, imageData.bytesPerLine);
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

		Canvas c = surface.getCanvas();

		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		c.drawLine(x1, y1, x2, y2, p);

	}

	@Override
	public void drawText(String string, int x, int y) {

		drawText(string, x, y, 0);

	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {

		Canvas c = surface.getCanvas();

		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));

		// Schriftart und -größe
		Typeface typeface = Typeface.makeDefault();

		Font font = new Font(typeface, FONT_DEFAULT_SIZE);

		// Erstellen eines TextBlob für 2 Zeilen
		TextBlobBuilder blobBuilder = new TextBlobBuilder();

		String[] lines = text == null ? null : splitString(text);

		float lineHeight = font.getMetrics().getHeight();

		float fx = 0;
		float fy = lineHeight;

		if (lines != null)
			for (String line : lines) {
				blobBuilder.appendRun(font, line, fx, fy);
				fy = fy + lineHeight;

			}

		// Erstellen des TextBlob
		TextBlob textBlob = blobBuilder.build();
		blobBuilder.close();

		// Zeichnen des TextBlobs auf dem Canvas

		c.drawTextBlob(textBlob, x, y, p);

		p.close();

	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		super.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawFocus(int x, int y, int width, int height) {
		super.drawFocus(x, y, width, height);
	}

	void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth,
			int srcHeight, int destX, int destY, int destWidth, int destHeight,
			boolean simple) {
	}

	void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth,
			int srcHeight, int destX, int destY, int destWidth, int destHeight,
			boolean simple) {
	}

	void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth,
			int srcHeight, int destX, int destY, int destWidth, int destHeight,
			boolean simple) {
	}

	void drawBitmapTransparentByClipping(long srcHdc, long maskHdc, int srcX,
			int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple, int imgWidth,
			int imgHeight) {
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
	}

	@Override
	public void drawPath(Path path) {
	}
	@Override
	public void drawPoint(int x, int y) {
	}

	@Override
	public void drawPolygon(int[] pointArray) {
	}

	@Override
	public void drawPolyline(int[] pointArray) {
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
	}

	@Override
	public void drawRectangle(Rectangle rect) {
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
	}

	@Override
	public void drawString(String string, int x, int y) {
	}

	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height,
			boolean vertical) {
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
	}

	@Override
	public void fillPath(Path path) {
	}

	@Override
	public void fillPolygon(int[] pointArray) {
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
	}
}

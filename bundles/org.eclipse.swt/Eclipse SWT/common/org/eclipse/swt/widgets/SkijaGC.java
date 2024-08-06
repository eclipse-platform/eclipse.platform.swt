package org.eclipse.swt.widgets;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;

public class SkijaGC extends GC {

	Surface surface;

	public SkijaGC(Drawable drawable, int style) {
		super(drawable, style);


		Rectangle r = null;
		if (drawable instanceof Control) {
			Control c = (Control) drawable;
			r = c.getBounds();
		} else if (drawable instanceof Device) {
			Device d = (Device) drawable;
			r = d.getBounds();
		}

		surface = Surface
				.makeRaster(ImageInfo.makeN32Premul(r.width, r.height));

	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {

		Canvas c = surface.getCanvas();

		Paint p = new Paint();
		p.setColor(getForeground().getIntRGB());
		c.drawLine(x1, y1, x2, y2, p);

		super.drawLine(x1, y1, x2, y2);

		p.close();

	}

	void drawToGc(Canvas c) {

		byte[] imageBytes = surface.makeImageSnapshot().encodeToData()
				.getBytes();

		Image i = new Image(getDevice(), new ByteArrayInputStream(imageBytes));
		super.drawImage(i, 0, 0);

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

	@Override
	public void drawImage(Image image, int x, int y) {

		super.drawImage(image, x, y);

	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth,
			int srcHeight, int destX, int destY, int destWidth,
			int destHeight) {

		super.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY,
				destWidth, destHeight);

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
	public void drawText(String string, int x, int y) {
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
	}

	@Override
	public void drawText(String string, int x, int y, int flags) {
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
	public void fillRectangle(Rectangle rect) {
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
	}

}

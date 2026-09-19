package org.eclipse.swt.internal.skia;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Matrix33;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.skija.Region;
import io.github.humbleui.skija.SamplingMode;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

/**
 * Adapter to wrap Skija's Canvas as ISkCanvas. (Stub for now, can be extended
 * as needed.)
 */
class SkijaCanvasAdapter implements ISkCanvas {
	private final Canvas delegate;

	public SkijaCanvasAdapter(Canvas canvas) {
		this.delegate = canvas;
	}

	@Override
	public int getSaveCount() {
		return delegate.getSaveCount();
	}

	@Override
	public void restoreToCount(int saveCount) {
		delegate.restoreToCount(saveCount);
	}

	@Override
	public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
		delegate.drawLine(x0, y0, x1, y1, paint);
	}

	@Override
	public void drawArc(int left, int top, int right, int bottom, int startAngle, int sweepAngle, boolean useCenter,
			Paint paint) {
		delegate.drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint);
	}

	@Override
	public void drawRect(Rect rect, Paint paint) {
		delegate.drawRect(rect, paint);
	}

	@Override
	public void drawOval(Rect rect, Paint paint) {
		delegate.drawOval(rect, paint);
	}

	@Override
	public void drawPath(Path path, Paint paint) {
		delegate.drawPath(path, paint);
	}

	@Override
	public void drawPolygon(float[] points, Paint paint) {
		delegate.drawPolygon(points, paint);
	}

	@Override
	public void drawRRect(RRect rrect, Paint paint) {
		delegate.drawRRect(rrect, paint);
	}

	@Override
	public void drawImage(ISkImage image, int x, int y) {
		delegate.drawImage(((SkijaImageAdapter)image).getSkijaImage(), x, y);
	}

	@Override
	public void drawImageRect(ISkImage image, Rect src, Rect dst, SamplingMode sampling, Paint paint, boolean strict) {
		delegate.drawImageRect(((SkijaImageAdapter)image).getSkijaImage(), src, dst, sampling, paint, strict);
	}

	@Override
	public void setMatrix(Matrix33 matrix) {
		delegate.setMatrix(matrix);
	}

	@Override
	public void save() {
		delegate.save();
	}

	@Override
	public void clipRect(Rect rect) {
		delegate.clipRect(rect);
	}

	@Override
	public void clear(int color) {
		delegate.clear(color);
	}

	@Override
	public void restore() {
		delegate.restore();
	}

	@Override
	public void clipPath(io.github.humbleui.skija.Path path, io.github.humbleui.skija.ClipMode mode,
			boolean doAntiAlias) {
		delegate.clipPath(path, mode, doAntiAlias);
	}

	@Override
	public void drawString(String text, int x, int i, Font f, Paint fgp) {
		delegate.drawString(text, x, i, f, fgp);

	}

	@Override
	public void clipRegion(Region skiaRegion) {
		delegate.clipRegion(skiaRegion);
	}
}
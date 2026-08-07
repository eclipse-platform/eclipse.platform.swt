package org.eclipse.swt.internal.skia;

import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Matrix33;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.skija.Region;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

/**
 * Abstraction for Skija's Canvas, for future decoupling.
 */
public interface ISkCanvas {
	int getSaveCount();
	void restoreToCount(int saveCount);
	void drawLine(float x0, float y0, float x1, float y1, Paint paint);
	void drawArc(int left, int top, int right, int bottom, int startAngle, int sweepAngle, boolean useCenter, Paint paint);
	void drawRect(Rect rect, Paint paint);
	void drawOval(Rect rect, Paint paint);
	void drawPath(Path path, Paint paint);
	void drawPolygon(float[] points, Paint paint);
	void drawRRect(RRect rrect, Paint paint);
	void drawImage(ISkImage image, int x, int y);
	void drawImageRect(ISkImage image, Rect src, Rect dst, io.github.humbleui.skija.SamplingMode sampling, Paint paint, boolean strict);
	void setMatrix(Matrix33 matrix);
	void save();
	void clipRect(Rect rect);
	void clear(int color);
	void restore();
	void clipPath(Path skijaPath, ClipMode intersect, boolean b);
	void drawString(String text, int x, int i, Font f, Paint fgp);
	void clipRegion(Region skiaRegion);
}
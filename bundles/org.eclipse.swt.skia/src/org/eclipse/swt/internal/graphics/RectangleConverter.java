package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class RectangleConverter {


	public static Rect createScaledRectangle(DpiScalerUtil sc,Rectangle r) {
		return createScaledRectangle( sc,r.x, r.y, r.width, r.height);
	}

	public static Rectangle scaleUpRectangle(DpiScalerUtil sc, Rectangle rectangle) {
		return new Rectangle(sc.autoScaleUp(rectangle.x), sc.autoScaleUp(rectangle.y), sc.autoScaleUp(rectangle.width),
				sc.autoScaleUp(rectangle.height));
	}

	public static Rect createScaledRectangle( DpiScalerUtil sc,  int x, int y, int width, int height) {
		final var r = scaleUpRectangle(sc,new Rectangle(x, y, width, height));
		return new Rect(r.x, r.y, r.width + r.x, r.height + r.y);
	}

	public static float getScaledOffsetValue(DpiScalerUtil sc, int lineWidth) {

		final boolean isDefaultLineWidth = lineWidth == 0;
		if (isDefaultLineWidth) {
			return 0.5f;
		}
		final int effectiveLineWidth = sc.autoScaleUp(lineWidth);
		if (effectiveLineWidth % 2 == 1) {
			return sc.autoScaleUp(0.5f);
		}
		return 0f;
	}

	public static Rect offsetRectangle(DpiScalerUtil sc, int lineWidth, Rect rect) {


		final float scaledOffsetValue = getScaledOffsetValue(sc, lineWidth);
		final float widthHightAutoScaleOffset = sc.autoScaleUp(1) - 1.0f;
		if (scaledOffsetValue > 0f) {
			return new Rect(rect.getLeft() + 0.5f, rect.getTop() + 0.5f,
					rect.getRight() + 0.5f + widthHightAutoScaleOffset,
					rect.getBottom() + 0.5f + widthHightAutoScaleOffset);
		}
		return rect;
	}

	public static RRect offsetRectangle(DpiScalerUtil sc, int lineWidth,RRect rect) {

		final float scaledOffsetValue =getScaledOffsetValue(sc, lineWidth);
		final float widthHightAutoScaleOffset = sc.autoScaleUp(1) - 1.0f;
		if (scaledOffsetValue != 0f) {
			return new RRect(rect.getLeft() + scaledOffsetValue, rect.getTop() + scaledOffsetValue,
					rect.getRight() + scaledOffsetValue + widthHightAutoScaleOffset,
					rect.getBottom() + scaledOffsetValue + widthHightAutoScaleOffset, rect._radii);
		}
		return rect;
	}

	public static Rect createScaledRectangleWithOffset(DpiScalerUtil scaler, int lineWidth, int x, int y,
			int width, int height) {
		final var rect = createScaledRectangle(scaler, x, y, width, height);
		return offsetRectangle(scaler, lineWidth, rect);
	}


	public static RRect createScaledRoundRectangle(DpiScalerUtil sc,int x, int y, int width, int height, float arcWidth, float arcHeight) {
		return new RRect(sc.autoScaleUp(x), sc.autoScaleUp(y), sc.autoScaleUp(x + width),
				sc.autoScaleUp(y + height),
				new float[] { sc.autoScaleUp(arcWidth), sc.autoScaleUp(arcHeight) });
	}

}

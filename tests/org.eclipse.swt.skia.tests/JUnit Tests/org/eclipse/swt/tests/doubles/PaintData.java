package org.eclipse.swt.tests.doubles;

import java.util.Objects;

import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.skija.ColorFilter;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.MaskFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PathEffect;
import io.github.humbleui.skija.Shader;

public class PaintData {

	int color;
	float strokeWidth;
	float strokeMiter;
	int strokeCap;
	int strokeJoin;
	int style;
	int alpha;
	boolean antiAlias;
	boolean dither;
	Shader shader;
	BlendMode blendMode;
	PathEffect pathEffect;
	ImageFilter imageFilter;
	ColorFilter colorFilter;
	MaskFilter maskFilter;

	public static PaintData getData(Paint p) {

		final PaintData data = new PaintData();
		data.color = p.getColor();
		data.strokeWidth = p.getStrokeWidth();
		data.strokeMiter = p.getStrokeMiter();
		data.strokeCap = p.getStrokeCap().ordinal();
		data.strokeJoin = p.getStrokeJoin().ordinal();
		data.style = p.getMode().ordinal();
		data.alpha = p.getAlpha();
		data.antiAlias = p.isAntiAlias();
		data.dither = p.isDither();
		data.shader = p.getShader();
		data.blendMode = p.getBlendMode();
		data.pathEffect = p.getPathEffect();
		data.imageFilter = p.getImageFilter();
		data.colorFilter = p.getColorFilter();
		data.maskFilter = p.getMaskFilter();

		return data;
	}



	@Override
	public String toString() {
		return "PaintData [color=" + color + ", strokeWidth=" + strokeWidth + ", strokeMiter=" + strokeMiter
				+ ", strokeCap=" + strokeCap + ", strokeJoin=" + strokeJoin + ", style=" + style + ", alpha=" + alpha
				+ ", antiAlias=" + antiAlias + ", dither=" + dither + ", shader=" + shader + ", blendMode=" + blendMode
				+ ", pathEffect=" + pathEffect + ", imageFilter=" + imageFilter + ", colorFilter=" + colorFilter
				+ ", maskFilter=" + maskFilter + ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ "]";
	}



	@Override
	public int hashCode() {
		return Objects.hash(alpha, antiAlias, blendMode, color, colorFilter, dither, imageFilter, maskFilter,
				pathEffect, shader, strokeCap, strokeJoin, strokeMiter, strokeWidth, style);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaintData other = (PaintData) obj;
		return alpha == other.alpha && antiAlias == other.antiAlias && blendMode == other.blendMode
				&& color == other.color && Objects.equals(colorFilter, other.colorFilter) && dither == other.dither
				&& Objects.equals(imageFilter, other.imageFilter) && Objects.equals(maskFilter, other.maskFilter)
				&& Objects.equals(pathEffect, other.pathEffect) && Objects.equals(shader, other.shader)
				&& strokeCap == other.strokeCap && strokeJoin == other.strokeJoin
				&& Float.floatToIntBits(strokeMiter) == Float.floatToIntBits(other.strokeMiter)
				&& Float.floatToIntBits(strokeWidth) == Float.floatToIntBits(other.strokeWidth) && style == other.style;
	}

	





}
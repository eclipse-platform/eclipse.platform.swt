/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.graphics;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.PaintStrokeCap;
import io.github.humbleui.skija.PaintStrokeJoin;
import io.github.humbleui.skija.PathEffect;
import io.github.humbleui.skija.Shader;

public class SkiaPaintManager {

	// Skia format: dash/dot widths and whitespace widths must be considered.
	static final float[] LINE_DOT_ZERO = new float[] { 1, 1 };
	static final float[] LINE_DASH_ZERO = new float[] { 3, 1 };
	static final float[] LINE_DASHDOT_ZERO = new float[] { 3, 1, 1, 1 };
	static final float[] LINE_DASHDOTDOT_ZERO = new float[] { 3, 1, 1, 1, 1, 1 };

	private final SkiaGC gc;

	public SkiaPaintManager(SkiaGC gc) {
		this.gc = gc;
	}

	public void performDraw(Consumer<Paint> operations) {
		try (final Paint paint = new Paint()) {
			paint.setAlpha(gc.getAlpha());
			paint.setColor(SkiaColorConverter.convertSWTColorToSkijaColor(gc.getForeground(), gc.getAlpha()));
			paint.setAntiAlias(gc.getAntialias() != SWT.OFF);
			if (gc.getXORMode()) {
				paint.setBlendMode(io.github.humbleui.skija.BlendMode.DIFFERENCE);
			} else {
				paint.setBlendMode(io.github.humbleui.skija.BlendMode.SRC_OVER);
			}
			final var scaledLineWidth = gc.getScaler().autoScaleUp(gc.getLineWidth() * 1F);
			paint.setMode(PaintMode.STROKE);
			paint.setStrokeWidth(scaledLineWidth);
			paint.setStrokeCap(getSkijaLineCap(gc.getLineCap()));
			paint.setStrokeJoin(getSkijaLineJoin(gc.getLineJoin()));

			try (var pathEffect = createPathEffect(scaledLineWidth, gc.getScaler())) {

				paint.setPathEffect(pathEffect);

				if (gc.getForegroundPattern() != null && !gc.getForegroundPattern().isDisposed()) {
					try (Shader shader = convertSWTPatternToSkijaShader(gc.getForegroundPattern())) {
						if (shader != null) {
							paint.setShader(shader);
						}
						operations.accept(paint);
					}
				} else {
					operations.accept(paint);
				}
			}
		}
	}

	private PathEffect createPathEffect(float scaledLineWidth, DpiScalerUtil scaler) {

		final var floats = switch (gc.getLineStyle()) {
		case SWT.LINE_SOLID -> null;
		case SWT.LINE_DOT -> LINE_DOT_ZERO;
		case SWT.LINE_DASH -> LINE_DASH_ZERO;
		case SWT.LINE_DASHDOT -> LINE_DASHDOT_ZERO;
		case SWT.LINE_DASHDOTDOT -> LINE_DASHDOTDOT_ZERO;
		case SWT.LINE_CUSTOM -> gc.getLineDashes();
		default -> null;
		};

		if (floats == null || floats.length == 0) {
			return null;
		}

		if (gc.getLineStyle() == SWT.LINE_CUSTOM) {
			if (floats.length % 2 != 0) {
				// Skia requires even number of elements in the array, so we duplicate the
				// pattern to make it even.
				final float[] newFloats = new float[floats.length * 2];
				System.arraycopy(floats, 0, newFloats, 0, floats.length);
				System.arraycopy(floats, 0, newFloats, floats.length, floats.length);
				return PathEffect.makeDash(getScaledPathFloats(scaler.autoScaleUp(1), newFloats, scaler), 0f);
			}

		}

		final var scaledFloats = getScaledPathFloats(scaledLineWidth, floats, scaler);
		return PathEffect.makeDash(scaledFloats, 0f);

	}

	private static float[] getScaledPathFloats(float scaledLineWidth, float[] lineDotZero, DpiScalerUtil scaler) {
		final float[] result = new float[lineDotZero.length];
		for (int i = 0; i < lineDotZero.length; i++) {
			result[i] = lineDotZero[i] * scaledLineWidth;
		}
		return result;
	}

	public void performDrawFilled(Consumer<Paint> operations) {
		try (final Paint paint = new Paint()) {
			paint.setMode(PaintMode.FILL);
			paint.setColor(SkiaColorConverter.convertSWTColorToSkijaColor(gc.getBackground(), gc.getAlpha()));
			paint.setAntiAlias(gc.getAntialias() != SWT.OFF);
			if (gc.getXORMode()) {
				paint.setBlendMode(io.github.humbleui.skija.BlendMode.DIFFERENCE);
			} else {
				paint.setBlendMode(io.github.humbleui.skija.BlendMode.SRC_OVER);
			}
			if (gc.getBackgroundPattern() != null && !gc.getBackgroundPattern().isDisposed()) {
				try (Shader shader = convertSWTPatternToSkijaShader(gc.getBackgroundPattern())) {
					if (shader != null) {
						paint.setShader(shader);
					}
					operations.accept(paint);
				}
			} else {
				operations.accept(paint);
			}
		}
	}

	public static PaintStrokeCap getSkijaLineCap(int lineCap) {
		if (lineCap == SWT.CAP_SQUARE) {
			return PaintStrokeCap.SQUARE;
		}
		if (lineCap == SWT.CAP_ROUND) {
			return PaintStrokeCap.ROUND;
		}
		return PaintStrokeCap.BUTT;
	}

	public static PaintStrokeJoin getSkijaLineJoin(int lineJoin) {
		if (lineJoin == SWT.JOIN_ROUND) {
			return PaintStrokeJoin.ROUND;
		}
		if (lineJoin == SWT.JOIN_BEVEL) {
			return PaintStrokeJoin.BEVEL;
		}
		return PaintStrokeJoin.MITER;
	}

	/**
	 * Converts an SWT Pattern to a Skija Shader.
	 *
	 * @param pattern the SWT Pattern to convert
	 * @return the Skija Shader or null if conversion fails
	 */
	public Shader convertSWTPatternToSkijaShader(Pattern pattern) {
		final var props = org.eclipse.swt.graphics.PatternProperties.get(pattern);
		final var sc = gc.getScaler();
		if (props.getImage() == null) {
			final int col1 = SkiaColorConverter.convertSWTColorToSkijaColor(props.getColor1(), props.getAlpha1());
			final int col2 = SkiaColorConverter.convertSWTColorToSkijaColor(props.getColor2(), props.getAlpha2());
			final var gs = new io.github.humbleui.skija.GradientStyle(io.github.humbleui.skija.FilterTileMode.REPEAT,
					true, null);
			return Shader.makeLinearGradient(sc.autoScaleUp(props.getBaseX1()), sc.autoScaleUp(props.getBaseY1()),
					sc.autoScaleUp(props.getBaseX2()), sc.autoScaleUp(props.getBaseY2()), new int[] { col1, col2 },
					null, gs);
		}
		try (final var image = SwtToSkiaImageConverter.convertSWTImageToSkijaImage(props.getImage(),
				gc.getScaler().getNativeZoom(), gc.getSkiaResources())) {
			return image.makeShader(io.github.humbleui.skija.FilterTileMode.REPEAT);
		}
	}
}
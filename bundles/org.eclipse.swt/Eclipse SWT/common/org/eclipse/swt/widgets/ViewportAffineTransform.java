/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

/**
 * Lightweight immutable affine transform used by the internal viewport model.
 *
 * <p>This deliberately does not wrap {@code org.eclipse.swt.graphics.Transform}.
 * The public SWT transform remains a native graphics resource, while viewport
 * planning needs a cheap allocation-free value object that can exist without a
 * GC or Device.</p>
 */
final class ViewportAffineTransform {

	record Point (double x, double y) {
	}

	record Bounds (double x, double y, double width, double height) {
	}

	static final ViewportAffineTransform IDENTITY =
			new ViewportAffineTransform (1, 0, 0, 1, 0, 0);

	final double m00;
	final double m10;
	final double m01;
	final double m11;
	final double tx;
	final double ty;

	ViewportAffineTransform (
			double m00, double m10, double m01, double m11, double tx, double ty) {
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.tx = tx;
		this.ty = ty;
	}

	static ViewportAffineTransform translation (double x, double y) {
        if (x == 0 && y == 0) {
            return IDENTITY;
        }
		return new ViewportAffineTransform (1, 0, 0, 1, x, y);
	}

	static ViewportAffineTransform scale (double x, double y) {
		return new ViewportAffineTransform (x, 0, 0, y, 0, 0);
	}

	ViewportAffineTransform concatenate (ViewportAffineTransform after) {
        if (after == null) {
            throw new IllegalArgumentException("null transform");
        }
		return new ViewportAffineTransform (
				after.m00 * m00 + after.m01 * m10,
				after.m10 * m00 + after.m11 * m10,
				after.m00 * m01 + after.m01 * m11,
				after.m10 * m01 + after.m11 * m11,
				after.m00 * tx + after.m01 * ty + after.tx,
				after.m10 * tx + after.m11 * ty + after.ty);
	}

	Point map (double x, double y) {
		return new Point (
				m00 * x + m01 * y + tx,
				m10 * x + m11 * y + ty);
	}

	Bounds mapBounds (long x, long y, long width, long height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("negative bounds");
        }
		/* Ordinary widget scrolling is translation-only. Keep mapped endpoints,
		 * not the original extents: large coordinates can lose unit precision.
		 */
		if (isTranslationOnly ()) {
			double left = (double)x + tx, top = (double)y + ty;
			double right = ((double)x + width) + tx;
			double bottom = ((double)y + height) + ty;
			double minX = Math.min (left, right), minY = Math.min (top, bottom);
			return new Bounds (minX, minY,
					Math.max (left, right) - minX, Math.max (top, bottom) - minY);
		}
		/* Preserve each coordinate's arithmetic and the original min/max reduction.
		 * Corner points are private scratch, not observable widget objects.
		 * Reuse primitive products/slots instead of allocating four Point records.
		 */
		double x0 = x, y0 = y;
		double x1 = (double)x + width, y1 = (double)y + height;
		double a = m00 * x0, b = m00 * x1, c = m01 * y0, d = m01 * y1;
		double p0 = a + c + tx, p1 = b + c + tx;
		double p2 = a + d + tx, p3 = b + d + tx;
		double minX = Math.min (Math.min (p0, p1), Math.min (p2, p3));
		double maxX = Math.max (Math.max (p0, p1), Math.max (p2, p3));
		a = m10 * x0; b = m10 * x1; c = m11 * y0; d = m11 * y1;
		p0 = a + c + ty; p1 = b + c + ty;
		p2 = a + d + ty; p3 = b + d + ty;
		double minY = Math.min (Math.min (p0, p1), Math.min (p2, p3));
		double maxY = Math.max (Math.max (p0, p1), Math.max (p2, p3));
		return new Bounds (minX, minY, maxX - minX, maxY - minY);
	}

	ViewportAffineTransform inverse () {
		double determinant = m00 * m11 - m01 * m10;
		if (determinant == 0 || !Double.isFinite (determinant)) {
			throw new IllegalStateException ("non-invertible transform");
		}
		double inverse = 1.0 / determinant;
		double nm00 = m11 * inverse;
		double nm10 = -m10 * inverse;
		double nm01 = -m01 * inverse;
		double nm11 = m00 * inverse;
		return new ViewportAffineTransform (
				nm00,
				nm10,
				nm01,
				nm11,
				-(nm00 * tx + nm01 * ty),
				-(nm10 * tx + nm11 * ty));
	}

	boolean isIdentity () {
		return this == IDENTITY
				|| (m00 == 1 && m10 == 0 && m01 == 0 && m11 == 1 && tx == 0 && ty == 0);
	}

	boolean isTranslationOnly () {
		return m00 == 1 && m10 == 0 && m01 == 0 && m11 == 1;
	}
}

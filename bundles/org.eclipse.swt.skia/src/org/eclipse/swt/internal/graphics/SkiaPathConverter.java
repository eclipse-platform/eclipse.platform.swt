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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

import io.github.humbleui.skija.PathFillMode;

public class SkiaPathConverter {

	public static io.github.humbleui.skija.Path convertSWTPathToSkijaPath(Path swtPath, DpiScalerUtil sc) {

		if (swtPath == null || swtPath.isDisposed()) {
			return null;
		}
		final PathData data = swtPath.getPathData();

		try (final io.github.humbleui.skija.PathBuilder skijaPathBuilder = new io.github.humbleui.skija.PathBuilder()) {

			final float[] pts = data.points;
			final byte[] types = data.types;
			int pi = 0;
			for (final byte type : types) {
				switch (type) {
				case SWT.PATH_MOVE_TO:
					skijaPathBuilder.moveTo(sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]));
					break;
				case SWT.PATH_LINE_TO:
					skijaPathBuilder.lineTo(sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]));
					break;
				case SWT.PATH_CUBIC_TO:
					skijaPathBuilder.cubicTo(sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]),
							sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]),
							sc.autoScaleUp(pts[pi++]));
					break;
				case SWT.PATH_QUAD_TO:
					skijaPathBuilder.quadTo(sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]),
							sc.autoScaleUp(pts[pi++]), sc.autoScaleUp(pts[pi++]));
					break;
				case SWT.PATH_CLOSE:
					skijaPathBuilder.closePath();
					break;
				default:
				}
			}
			final var p = skijaPathBuilder.build();
			return p;
		}

	}

	public static io.github.humbleui.skija.Path createPath(int[] pointArray,int fillRule , DpiScalerUtil sc) {
		// Create Skija path for the polygon
		try (io.github.humbleui.skija.PathBuilder pathBuilder = new io.github.humbleui.skija.PathBuilder()) { // Move
			// to
			// first
			// point
			pathBuilder.moveTo(sc.autoScaleUp(pointArray[0]), sc.autoScaleUp(pointArray[1]));
			// Add lines to subsequent points
			for (int i = 2; i < pointArray.length; i += 2) {
				pathBuilder.lineTo(sc.autoScaleUp(pointArray[i]), sc.autoScaleUp(pointArray[i + 1]));
			}
			// Close the path to form a polygon
			pathBuilder.closePath();
			pathBuilder.setFillMode(fillRule == SWT.FILL_EVEN_ODD ? PathFillMode.EVEN_ODD : PathFillMode.WINDING);
			// Fill the polygon
			return pathBuilder.build();
		}
	}

}

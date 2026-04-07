/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.graphics;

import java.util.HashMap;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.RegionLog;
import org.eclipse.swt.graphics.RegionLog.OpType;
import org.eclipse.swt.graphics.RegionLog.Operation;
import org.eclipse.swt.internal.skia.DpiScalerUtil;
import org.eclipse.swt.internal.skia.ISkiaCanvasExtension;

import io.github.humbleui.skija.RegionOp;
import io.github.humbleui.types.IRect;

public class SkiaRegionCalculator implements AutoCloseable {

	private final static HashMap<OpType, RegionOp> operationMapping = new HashMap<OpType, RegionOp>();

	static {
		operationMapping.put(OpType.ADD, RegionOp.UNION);
		operationMapping.put(OpType.SUBTRACT, RegionOp.DIFFERENCE);
		operationMapping.put(OpType.INTERSECT, RegionOp.INTERSECT);
	}

	private final org.eclipse.swt.graphics.Region region;
	private io.github.humbleui.skija.Region calculatedRegion;
	private final ISkiaCanvasExtension skiaExtension;

	public SkiaRegionCalculator(org.eclipse.swt.graphics.Region r, ISkiaCanvasExtension skiaExtension) {
		this.region = r;
		this.skiaExtension = skiaExtension;
	}

	io.github.humbleui.skija.Region getSkiaRegion() {

		// just all operations for the region creation will be executed to get the skia
		// region.

		if (calculatedRegion != null) {
			return calculatedRegion;
		}

		final io.github.humbleui.skija.Region reg = new io.github.humbleui.skija.Region();
		final var log = RegionLog.getLog(region);
		for (final var o : log.getOperations()) {
			apply(o, reg);
		}

		calculatedRegion = reg;
		return calculatedRegion;

	}

	@Override
	public void close() {
		if (calculatedRegion != null) {
			calculatedRegion.close();
			calculatedRegion = null;
		}
	}

	private void apply(Operation o, io.github.humbleui.skija.Region reg) {
		final RegionOp skiaOperation = operationMapping.get(o.type());
		if (skiaOperation != null) {
			executeOperation(skiaOperation, o.executionObject(), reg);
			return;
		}

		if (OpType.TRANSLATE.equals(o.type())) {
			if (o.executionObject() instanceof final Point p) {
				reg.translate(p.x, p.y);
				return;
			}
		}

		throw new IllegalStateException("Unknown type and object: " + o); //$NON-NLS-1$

	}

	private void executeOperation(RegionOp skiaOperation, Object ob, io.github.humbleui.skija.Region reg) {
		if (ob instanceof final int[] polygon) {
			final var tempReg = createPolygonSkiaRegion(polygon);
			reg.op(tempReg, skiaOperation);
		} else if (ob instanceof final Rectangle rec) {
			final var irect = createRectRegion(rec);
			reg.op(irect, skiaOperation);
		} else if (ob instanceof final Region otherReg) {
			try (final SkiaRegionCalculator src = new SkiaRegionCalculator(otherReg, skiaExtension)) {
				reg.op(src.getSkiaRegion(), skiaOperation);
			}
		}
	}

	/**
	 * Creates a Skija region from an SWT rectangle by converting it to a polygon.
	 */
	private io.github.humbleui.skija.Region createRectRegion(Rectangle rec) {
		final var rect = new IRect(rec.x, rec.y, rec.x + rec.width, rec.y + rec.height);

		return createPolygonSkiaRegion(new int[] { rect.getLeft(), rect.getTop(), rect.getRight(), rect.getTop(),
				rect.getRight(), rect.getBottom(), rect.getLeft(), rect.getBottom() });

	}

	private io.github.humbleui.skija.Region createPolygonSkiaRegion(int[] polygon) {

		final io.github.humbleui.skija.Region r = new io.github.humbleui.skija.Region();

		final var scaler = new DpiScalerUtil(skiaExtension.getScaler());

		try (final var pathBuilder = new io.github.humbleui.skija.PathBuilder()) {
			pathBuilder.addPolygon(scaler.autoScaleUp(toFloat(polygon)), true);

			final Point maxV = getMax(scaler.autoScaleUp(polygon));

			r.setRect(new IRect(0, 0, maxV.x, maxV.y));

			// a path has to be set for a clipping region.
			// so first we have to create a region big enough for the path and then set the
			// path.
			try (var path = pathBuilder.build()) {
				r.setPath(path, r);
			}

			return r;
		}
	}

	/**
	 *
	 * max value on x and on y in one point.
	 *
	 * @param polygon
	 * @return
	 */
	private static Point getMax(int[] polygon) {

		final var p = new Point(0, 0);

		for (int i = 0; i < polygon.length; i++) {

			if (i % 2 == 0) {
				p.x = Math.max(polygon[i], p.x);
			}
			if (i % 2 == 1) {
				p.y = Math.max(polygon[i], p.y);
			}

		}

		return p;
	}

	private static float[] toFloat(int[] arr) {

		final float[] r = new float[arr.length];

		for (int i = 0; i < arr.length; i++) {
			r[i] = arr[i];
		}

		return r;

	}

}
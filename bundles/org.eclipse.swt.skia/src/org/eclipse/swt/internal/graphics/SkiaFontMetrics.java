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

import java.util.Objects;

import org.eclipse.swt.internal.canvasext.IExternalFontMetrics;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

public class SkiaFontMetrics implements IExternalFontMetrics {

	private final io.github.humbleui.skija.FontMetrics m;
	private final DpiScalerUtil sc;

	SkiaFontMetrics(io.github.humbleui.skija.FontMetrics metrics, DpiScalerUtil dpiScaler) {
		this.sc = dpiScaler;
		this.m = metrics;
	}

	@Override
	public int getAscent() {
		return sc.autoScaleDownToInt(((int) Math.ceil(Math.abs(m.getAscent()  ) - Math.abs(m.getLeading())  )));
	}

	@Override
	public double getAverageCharacterWidth() {
		return sc.autoScaleDown(m.getAvgCharWidth());
	}

	@Override
	public int getAverageCharWidth() {
		return (int) Math.ceil(sc.autoScaleDown(m.getAvgCharWidth()));
	}

	@Override
	public int getDescent() {
		return (int) Math.ceil(sc.autoScaleDown(m.getDescent()));
	}

	@Override
	public int getHeight() {
		return (int) Math.ceil(sc.autoScaleDown(m.getHeight()));
	}

	@Override
	public int getLeading() {
		return sc.autoScaleDownToInt( getHeight() - getAscent() - getDescent());
	}

	@Override
	public int hashCode() {
		return Objects.hash(m);
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
		final SkiaFontMetrics other = (SkiaFontMetrics) obj;
		return Objects.equals(m, other.m);
	}

}

/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.*;

import org.eclipse.swt.internal.*;

public class SkijaFontMetrics extends FontMetricsHandle {

	private io.github.humbleui.skija.FontMetrics metrics;

	SkijaFontMetrics(io.github.humbleui.skija.FontMetrics metrics) {
		this.metrics = metrics;
	}

	@Override
	public int getAscent() {
		// in skija, these are negative usually.
		return Math.abs((int) this.metrics.getAscent());
	}

	@Override
	public int getDescent() {
		return (int) this.metrics.getDescent();
	}

	@Override
	public int getHeight() {
		return (int) this.metrics.getHeight();
	}

	@Override
	public int getLeading() {
		return (int) this.metrics.getLeading();
	}

	@Override
	public int getAverageCharWidth() {
		return (int) this.metrics.getAvgCharWidth();
	}

	@Override
	public int hashCode() {
		return Objects.hash(metrics);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkijaFontMetrics other = (SkijaFontMetrics) obj;
		return Objects.equals(metrics, other.metrics);
	}

	@Override
	public double getAverageCharacterWidth() {
		return this.metrics.getAvgCharWidth();

	}

}

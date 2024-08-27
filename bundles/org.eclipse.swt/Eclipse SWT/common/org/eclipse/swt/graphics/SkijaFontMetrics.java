package org.eclipse.swt.graphics;

import java.util.*;

import org.eclipse.swt.internal.*;

public class SkijaFontMetrics implements IFontMetrics {

	private io.github.humbleui.skija.FontMetrics metrics;

	SkijaFontMetrics(io.github.humbleui.skija.FontMetrics metrics) {
		this.metrics = metrics;
	}

	@Override
	public int getAscent() {
		return (int) DPIUtil.autoScaleDown(this.metrics.getAscent());
	}

	@Override
	public int getDescent() {
		return (int) DPIUtil.autoScaleDown(this.metrics.getDescent());
	}

	@Override
	public int getHeight() {
		return (int) DPIUtil.autoScaleDown(this.metrics.getHeight());
	}

	@Override
	public int getLeading() {
		return (int) DPIUtil.autoScaleDown(this.metrics.getLeading());
	}

	@Override
	public int getAverageCharWidth() {
		return (int) DPIUtil.autoScaleDown(this.metrics.getAvgCharWidth());
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
		return DPIUtil.autoScaleDown(this.metrics.getAvgCharWidth());

	}

}

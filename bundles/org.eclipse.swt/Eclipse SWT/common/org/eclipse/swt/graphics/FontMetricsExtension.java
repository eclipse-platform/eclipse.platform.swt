package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.canvasext.*;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public final class FontMetricsExtension extends FontMetrics {

	private IExternalFontMetrics externalMetrics;

	/**
	 * @noreference This constructor is not intended to be referenced by clients.
	 * @param extMetrics
	 */
	public FontMetricsExtension(IExternalFontMetrics extMetrics) {
		this.externalMetrics = extMetrics;
	}

	@Override
	public int getAscent() {
		return externalMetrics.getAscent();
	}

	@Override
	public double getAverageCharacterWidth() {
		return externalMetrics.getAverageCharacterWidth();
	}

	@Override
	@Deprecated
	public int getAverageCharWidth() {
		return externalMetrics.getAverageCharWidth();
	}

	@Override
	public int getDescent() {
		return externalMetrics.getDescent();
	}

	@Override
	public int getHeight() {
		return externalMetrics.getHeight();
	}

	@Override
	public int getLeading() {
		return externalMetrics.getLeading();
	}

	@Override
	public int hashCode() {
		return externalMetrics.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		return externalMetrics.equals(object);
	}

}
package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.widgets.*;

/**
 *
 * Provides utility methods for the canvas extension to scale values according
 * to the current DPI settings of the OS. This is used internally to scale all
 * values that are used for drawing and layout calculations.
 */
public class DpiScaler implements IDpiScaler {

	public DpiScaler(Canvas canvas) {
	}

	public int getNativeZoom() {
		return 100;
	}

}
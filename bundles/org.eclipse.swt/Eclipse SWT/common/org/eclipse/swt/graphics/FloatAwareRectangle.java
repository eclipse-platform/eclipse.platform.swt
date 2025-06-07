package org.eclipse.swt.graphics;

/**
 * @since 3.130
 */
public sealed class FloatAwareRectangle extends Rectangle permits MonitorAwareRectangle {

	private static final long serialVersionUID = -3006999002677468391L;

	public float residualX, residualY, residualWidth, residualHeight;

	public FloatAwareRectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

}

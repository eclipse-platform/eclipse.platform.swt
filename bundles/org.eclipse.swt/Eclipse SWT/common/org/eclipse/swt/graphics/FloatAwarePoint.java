package org.eclipse.swt.graphics;

/**
 * @since 3.130
 */
public sealed class FloatAwarePoint extends Point permits MonitorAwarePoint {

	private static final long serialVersionUID = -1862062276431597053L;

	public float residualX, residualY;

	public FloatAwarePoint(int x, int y) {
		super(x, y);
	}

}

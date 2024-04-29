package org.eclipse.swt.internal;

public class DPITestUtil {

	public static void setAutoScaleOnRunTime(boolean value) {
		DPIUtil.setAutoScaleOnRuntimeActive(value);
	}

	public static boolean isAutoScaleOnRuntimeActive() {
		return DPIUtil.isAutoScaleOnRuntimeActive();
	}
}

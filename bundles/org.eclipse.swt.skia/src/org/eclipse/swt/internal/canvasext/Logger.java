package org.eclipse.swt.internal.canvasext;

public class Logger {

	public static void logException(Throwable t) {
		t.printStackTrace(System.err);
	}

}

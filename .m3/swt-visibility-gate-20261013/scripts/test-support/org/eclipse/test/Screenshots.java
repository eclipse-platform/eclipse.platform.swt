package org.eclipse.test;

/** Harness stub for org.eclipse.test.Screenshots (Eclipse test framework bundle absent on this host); takes no screenshot. */
public final class Screenshots {
	private Screenshots () {
	}

	public static String takeScreenshot (Class<?> testClass, String name) {
		return "screenshot-stub:" + testClass.getSimpleName () + ":" + name;
	}
}

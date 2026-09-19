package org.eclipse.swt.tests.skia;

import org.eclipse.swt.SWT;

public class SupportedTestPlatforms {

	public static boolean isSupported() {
		return isFittingOS() && isFittingArchitecture();
	}

	public static boolean isFittingOS() {
		return "win32".equals(SWT.getPlatform()) || "gtk".equals(SWT.getPlatform());
	}

	public static boolean isFittingArchitecture() {
		final var arc = arch();
		return "x86_64".equals(arc);
	}

	static String arch() {
		final String osArch = System.getProperty("os.arch"); //$NON-NLS-1$
		if (osArch.equals ("amd64")) { //$NON-NLS-1$
			return "x86_64";
		}
		return osArch;
	}

}

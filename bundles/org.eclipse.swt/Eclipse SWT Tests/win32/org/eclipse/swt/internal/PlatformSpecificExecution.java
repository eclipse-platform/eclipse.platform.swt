package org.eclipse.swt.internal;

import static org.junit.Assume.assumeTrue;

import java.net.*;

public final class PlatformSpecificExecution {
	private PlatformSpecificExecution()  {
	}

	public static void assumeIsFittingPlatform() {
		assumeTrue("test is specific for Windows", isFittingOS());
		assumeTrue("architecture of platform does not match", isFittingArchitecture());
	}

	private static boolean isFittingOS() {
		return Library.os().equals("win32");
	}

	private static boolean isFittingArchitecture() {
		Class<?> thisClass = PlatformSpecificExecution.class;
		String thisClassResourcePath = thisClass.getName().replace('.', '/')  + ".class";
		URL thisClassURL = thisClass.getClassLoader().getResource(thisClassResourcePath); //$NON-NLS-1$
		return thisClassURL.toString().contains(Library.arch());
	}

}


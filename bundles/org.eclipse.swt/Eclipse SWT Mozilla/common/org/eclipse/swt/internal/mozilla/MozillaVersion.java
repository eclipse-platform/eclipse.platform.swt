/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;


public class MozillaVersion {

	public static final int VERSION_BASE = 0;
	public static final int VERSION_XR1_8 = 1;
	public static final int VERSION_XR1_9 = 2;
	public static final int VERSION_XR1_9_1 = 3;
	public static final int VERSION_XR1_9_2 = 4;
	public static final int VERSION_XR10 = 5;
	public static final int VERSION_XR24 = 6;
	public static final int VERSION_XR31 = 7;

	static final int VERSION_LATEST = VERSION_XR31;
	static int CurrentVersion = -1;

	public static boolean CheckVersion (int version) {
		return CheckVersion (version, false);
	}

	public static boolean CheckVersion (int version, boolean exact) {
		return exact ? version == CurrentVersion : version <= CurrentVersion;
	}

	public static int GetCurrentVersion () {
		return CurrentVersion;
	}

	public static int GetLatestVersion () {
		return VERSION_LATEST;
	}

	public static void SetCurrentVersion (int value) {
		CurrentVersion = value;
	}
}

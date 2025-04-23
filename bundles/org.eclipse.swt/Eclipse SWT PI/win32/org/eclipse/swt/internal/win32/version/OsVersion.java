/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.win32.version;

import org.eclipse.swt.internal.*;

/**
 * A separate library only for checking the Windows build version. Required as the general
 * OS library may not be loadable with linkage errors when executed on an unsupported Windows
 * build, such that a version check cannot be performed successfully, but a a linkage error
 * will occur instead. This library only adapts a single OS method that can always be loaded,
 * thus ensuring that the version check can always be executed.
 *
 * @jniclass flags=c
 * */
public class OsVersion extends Platform {
	static {
		Library.loadLibrary ("swt-osversion"); //$NON-NLS-1$
	}

	/**
	 * Always reports the correct build number, regardless of manifest and
	 * compatibility GUIDs. Note that build number alone is sufficient to
	 * identify Windows version.
	 */
	public static final int WIN32_BUILD;
	/**
	 * Values taken from https://en.wikipedia.org/wiki/List_of_Microsoft_Windows_versions
	 */
	public static final int WIN32_BUILD_WIN10_1607 = 14393; // "Windows 10 August 2016 Update"

	/**
	 * The minimum supported Windows build version for using this SWT version
	 */
	public static final int WIN32_MINIMUM_COMPATIBLE_BUILD = WIN32_BUILD_WIN10_1607;

	private static final String DISABLE_WINDOWS_VERSION_CHECK_PROPERTY = "swt.disableWindowsVersionCheck";

	static {
		/*
		 * Starting with Windows 10, GetVersionEx() lies about version unless
		 * application manifest has a proper entry. RtlGetVersion() always
		 * reports true version.
		 */
		OSVERSIONINFOEX osVersionInfoEx = new OSVERSIONINFOEX ();
		osVersionInfoEx.dwOSVersionInfoSize = OSVERSIONINFOEX.sizeof;
		if (0 == OsVersion.RtlGetVersion (osVersionInfoEx)) {
			WIN32_BUILD = osVersionInfoEx.dwBuildNumber;
		} else {
			System.err.println ("SWT: OS: Failed to detect Windows build number");
			WIN32_BUILD = 0;
		}
	}

	public static void checkCompatibleWindowsVersion() {
		String disableVersionCheckPropertyValue = System.getProperty(DISABLE_WINDOWS_VERSION_CHECK_PROPERTY, Boolean.FALSE.toString());
		boolean versionCheckDisabled = "".equals(disableVersionCheckPropertyValue) || Boolean.TRUE.toString().equalsIgnoreCase(disableVersionCheckPropertyValue);
		if (!versionCheckDisabled && WIN32_BUILD < WIN32_MINIMUM_COMPATIBLE_BUILD) {
			System.err.println(String.format("Incompatible OS: Minimum Windows build version is %s but current is %s",
					WIN32_MINIMUM_COMPATIBLE_BUILD, WIN32_BUILD));
			System.exit(1);
		}
	}

	public static final native int OSVERSIONINFOEX_sizeof ();

	/** @method flags=dynamic */
	public static final native int RtlGetVersion (OSVERSIONINFOEX lpVersionInformation);
}

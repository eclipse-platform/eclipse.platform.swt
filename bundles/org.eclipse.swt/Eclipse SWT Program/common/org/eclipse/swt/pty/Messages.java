/*******************************************************************************
 * Copyright (c) 2014, 2015 Wind River Systems, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Martin Oberhuber (Wind River) - [303083] Split out the Spawner
 *******************************************************************************/
package org.eclipse.swt.pty;

class Messages  {
	public static String PTY_FailedToStartConPTY="Failed start a new style ConPTY. This is expected on Windows machines before Windows 10 1904 version and will fall back to WinPTY. Please consider upgrading to a newer version of Windows to take advantage of the new improved console behavior.";
	public static String PTY_FailedToStartNativePTY="Failed to load the PTY library. This may indicate a configuration problem, but can be ignored if no further problems are observed.";
	public static String PTY_FailedToStartPTY="Failed to load the PTY library. This may indicate a configuration problem, but can be ignored if no further problems are observed.";
	public static String PTY_FailedToStartWinPTY="PTY_FailedToStartWinPTY=Failed start WinPTY due to an exception. Please consider upgrading to a newer version of Windows to take advantage of the new improved console behavior.";
	public static String PTY_NoClassDefFoundError="Failed start a new style ConPTY due to NoClassDefFoundError which probably means that the optional dependency on JNA is not available. Consider updating your product to include JNA to enable the ConPTY.";
	public static String Util_exception_cannotCreatePty="Cannot create pty";
	public static String Util_exception_cannotSetTerminalSize="Setting terminal size is not supported";
	public static String Util_error_cannotRun="Cannot run program \"{0}\": {1}";
	public static String Util_exception_closeError="close error";

	private Messages() {
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/* Special sizeof's */
#define SCRIPT_STRING_ANALYSIS_sizeof() sizeof(SCRIPT_STRING_ANALYSIS)
#define PROPVARIANT_sizeof() sizeof(PROPVARIANT)
#define LOGPEN_sizeof() sizeof(LOGPEN)

/* Libraries for dynamic loaded functions */
#define GetDpiForMonitor_LIB "shcore.dll"
#define GetDpiForWindow_LIB "user32.dll"
#define RtlGetVersion_LIB "ntdll.dll"
#define OpenThemeDataForDpi_LIB "uxtheme.dll"
#define GetSystemMetricsForDpi_LIB "user32.dll"
#define GetThreadDpiAwarenessContext_LIB "user32.dll"
#define SetThreadDpiAwarenessContext_LIB "user32.dll"
#define SystemParametersInfoForDpi_LIB "user32.dll"
#define AdjustWindowRectExForDpi_LIB "user32.dll"

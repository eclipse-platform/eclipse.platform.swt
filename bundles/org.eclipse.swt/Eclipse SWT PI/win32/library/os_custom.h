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

/* Libraries for dynamic loaded functions */
#define CloseGestureInfoHandle_LIB "user32.dll"
#define CloseTouchInputHandle_LIB "user32.dll"
#define GetCurrentProcessExplicitAppUserModelID_LIB "shell32.dll"
#define GetDpiForMonitor_LIB "shcore.dll"
#define GetGestureInfo_LIB "user32.dll"
#define GetTouchInputInfo_LIB "user32.dll"
#define IsTouchWindow_LIB "user32.dll"
#define RegisterTouchWindow_LIB "user32.dll"
#define SetCurrentProcessExplicitAppUserModelID_LIB "shell32.dll"
#define SetGestureConfig_LIB "user32.dll"
#define UnregisterTouchWindow_LIB "user32.dll"

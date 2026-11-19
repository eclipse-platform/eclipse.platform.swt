/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
package org.eclipse.swt.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Control;

/*
 * Wraps Win32 API used to bidi enable widgets. Up to 3.104 was used by
 * StyledText widget exclusively. 3.105 release introduced the method
 * #resolveTextDirection, which is used by other widgets as well.
 */
public class BidiUtil {
	// Keyboard language types
	public static final int KEYBOARD_NON_BIDI = 0;
	public static final int KEYBOARD_BIDI = 1;

/*
 * Not implemented.
 */
public static void addLanguageListener (Control control, Runnable runnable) {
}
/*
 * Bidi not supported on emulated platforms.
 */
public static boolean isBidiPlatform() {
	return false;
}
/*
 * Not implemented. Returns 0.
 */
public static int getKeyboardLanguage() {
	return 0;
}
/*
 * Not implemented.
 */
public static void removeLanguageListener (Control control) {
}
/*
 * Not implemented. Returns SWT.NONE.
 */
public static int resolveTextDirection (String text) {
	return SWT.NONE;
}
/*
 * Not implemented.
 */
public static void setKeyboardLanguage(int language) {
}
}

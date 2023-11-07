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
import org.eclipse.swt.graphics.GC;
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

	// bidi rendering input flag constants, not used
	// on emulated platforms
	public static final int CLASSIN = 1;
	public static final int LINKBEFORE = 2;
	public static final int LINKAFTER = 4;

	// bidi rendering/ordering constants, not used on
	// emulated platforms
	public static final int CLASS_HEBREW = 2;
	public static final int CLASS_ARABIC = 2;
	public static final int CLASS_LOCALNUMBER = 4;
	public static final int CLASS_LATINNUMBER = 5;
	public static final int REORDER = 0;
	public static final int LIGATE = 0;
	public static final int GLYPHSHAPE = 0;

/*
 * Not implemented.
 */
public static void addLanguageListener(long hwnd, Runnable runnable) {
}
public static void addLanguageListener (Control control, Runnable runnable) {
}
/*
 * Not implemented.
 */
public static void drawGlyphs(GC gc, char[] renderBuffer, int[] renderDx, int x, int y) {
}
/*
 * Bidi not supported on emulated platforms.
 */
public static boolean isBidiPlatform() {
	return false;
}
/*
 * Not implemented.
 */
public static boolean isKeyboardBidi() {
	return false;
}
/*
 * Not implemented.
 */
public static int getFontBidiAttributes(GC gc) {
	return 0;
}
/*
 *  Not implemented.
 */
public static void getOrderInfo(GC gc, String text, int[] order, byte[] classBuffer, int flags, int [] offsets) {
}
/*
 *  Not implemented. Returns null.
 */
public static char[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags, int[] offsets) {
	return null;
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
public static void removeLanguageListener(long hwnd) {
}
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
/*
 * Not implemented.
 */
public static boolean setOrientation(long hwnd, int orientation) {
	return false;
}
public static boolean setOrientation (Control control, int orientation) {
	return false;
}
}

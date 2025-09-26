/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
/*
 * Wraps Win32 API used to bidi enable widgets. Up to 3.104 was used by
 * StyledText widget exclusively. 3.105 release introduced the method
 * #resolveTextDirection, which is used by other widgets as well.
 */
public class BidiUtil {

	// Keyboard language ids
	public static final int KEYBOARD_NON_BIDI = 0;
	public static final int KEYBOARD_BIDI = 1;

	// bidi flag
	static int isBidiPlatform = -1;

	// getRenderInfo flag values
	public static final int CLASSIN = 1;
	public static final int LINKBEFORE = 2;
	public static final int LINKAFTER = 4;

	// variables used for providing a listener mechanism for keyboard language
	// switching
	static Map<LONG, Runnable> languageMap = new HashMap<> ();
	static Map<LONG, LONG> oldProcMap = new HashMap<> ();
	static Callback callback = new Callback (BidiUtil.class, "windowProc", 4); //$NON-NLS-1$

	// GetCharacterPlacement constants
	static final int GCP_REORDER = 0x0002;
	static final int GCP_GLYPHSHAPE = 0x0010;
	static final int GCP_LIGATE = 0x0020;
	static final int GCP_CLASSIN = 0x00080000;
	static final byte GCPCLASS_ARABIC = 2;
	static final byte GCPCLASS_HEBREW = 2;
	static final byte GCPCLASS_LOCALNUMBER = 4;
	static final byte GCPCLASS_LATINNUMBER = 5;
	static final int GCPGLYPH_LINKBEFORE = 0x8000;
	static final int GCPGLYPH_LINKAFTER = 0x4000;
	// ExtTextOut constants
	static final int ETO_CLIPPED = 0x4;
	static final int ETO_GLYPH_INDEX = 0x0010;
	// Windows primary language identifiers
	static final int LANG_ARABIC = 0x01;
	static final int LANG_HEBREW = 0x0d;
	static final int LANG_FARSI = 0x29;
	// ActivateKeyboard constants
	static final int HKL_NEXT = 1;
	static final int HKL_PREV = 0;

	/*
	 * Public character class constants are the same as Windows
	 * platform constants.
	 * Saves conversion of class array in getRenderInfo to arbitrary
	 * constants for now.
	 */
	public static final int CLASS_HEBREW = GCPCLASS_ARABIC;
	public static final int CLASS_ARABIC = GCPCLASS_HEBREW;
	public static final int CLASS_LOCALNUMBER = GCPCLASS_LOCALNUMBER;
	public static final int CLASS_LATINNUMBER = GCPCLASS_LATINNUMBER;
	public static final int REORDER = GCP_REORDER;
	public static final int LIGATE = GCP_LIGATE;
	public static final int GLYPHSHAPE = GCP_GLYPHSHAPE;

/**
 * Adds a language listener. The listener will get notified when the language of
 * the keyboard changes (via Alt-Shift on Win platforms).  Do this by creating a
 * window proc for the Control so that the window messages for the Control can be
 * monitored.
 *
 * @param hwnd the handle of the Control that is listening for keyboard language
 *  changes
 * @param runnable the code that should be executed when a keyboard language change
 *  occurs
 */
private static void addLanguageListener (long hwnd, Runnable runnable) {
	languageMap.put(new LONG(hwnd), runnable);
	subclass(hwnd);
}
public static void addLanguageListener (Control control, Runnable runnable) {
	addLanguageListener(control.handle, runnable);
}
/**
 * Proc used for OS.EnumSystemLanguageGroups call during isBidiPlatform test.
 */
static long EnumSystemLanguageGroupsProc(long lpLangGrpId, long lpLangGrpIdString, long lpLangGrpName, long options, long lParam) {
	if ((int)lpLangGrpId == OS.LGRPID_HEBREW) {
		isBidiPlatform = 1;
		return 0;
	}
	if ((int)lpLangGrpId == OS.LGRPID_ARABIC) {
		isBidiPlatform = 1;
		return 0;
	}
	return 1;
}

/**
 * Return the active keyboard language type.
 *
 * @return an integer representing the active keyboard language (KEYBOARD_BIDI,
 *  KEYBOARD_NON_BIDI)
 */
public static int getKeyboardLanguage() {
	long layout = OS.GetKeyboardLayout(0);
	return isBidiLang(layout) ? KEYBOARD_BIDI : KEYBOARD_NON_BIDI;
}
/**
 * Return the languages that are installed for the keyboard.
 *
 * @return integer array with an entry for each installed language
 */
private static long[] getKeyboardLanguageList() {
	int maxSize = 10;
	long[] tempList = new long[maxSize];
	int size = OS.GetKeyboardLayoutList(maxSize, tempList);
	long[] list = new long[size];
	System.arraycopy(tempList, 0, list, 0, size);
	return list;
}

private static boolean isBidiLang(long lang) {
	int id = OS.PRIMARYLANGID(OS.LOWORD(lang));
	return id == LANG_ARABIC || id == LANG_HEBREW || id == LANG_FARSI;
}

/**
 * Return whether or not the platform supports a bidi language.  Determine this
 * by looking at the languages that are installed.
 *
 * @return true if bidi is supported, false otherwise. Always
 * 	false on Windows CE.
 */
public static boolean isBidiPlatform() {
	if (isBidiPlatform != -1) return isBidiPlatform == 1; // already set

	isBidiPlatform = 0;

	// The following test is a workaround for bug report 27629. On WinXP,
	// both bidi and complex script (e.g., Thai) languages must be installed
	// at the same time.  Since the bidi platform calls do not support
	// double byte characters, there is no way to run Eclipse using the
	// complex script languages on XP, so constrain this test to answer true
	// only if a bidi input language is defined.  Doing so will allow complex
	// script languages to work (e.g., one can install bidi and complex script
	// languages, but only install the Thai keyboard).
	if (!isKeyboardBidi()) return false;

	Callback callback = new Callback (BidiUtil.class, "EnumSystemLanguageGroupsProc", 5); //$NON-NLS-1$
	OS.EnumSystemLanguageGroups(callback.getAddress (), OS.LGRPID_INSTALLED, 0);
	callback.dispose ();
	return isBidiPlatform == 1;
}
/**
 * Return whether or not the keyboard supports input of a bidi language.  Determine this
 * by looking at the languages that are installed for the keyboard.
 *
 * @return true if bidi is supported, false otherwise.
 */
private static boolean isKeyboardBidi() {
	for (long language : getKeyboardLanguageList()) {
		if (isBidiLang(language)) {
			return true;
		}
	}
	return false;
}
/**
 * Removes the specified language listener.
 *
 * @param hwnd the handle of the Control that is listening for keyboard language changes
 */
private static void removeLanguageListener (long hwnd) {
	languageMap.remove(new LONG(hwnd));
	unsubclass(hwnd);
}
public static void removeLanguageListener (Control control) {
	removeLanguageListener(control.handle);
}

/**
 * Determine the base direction for the given text. The direction is derived
 * from the first strong bidirectional RIGHT_TO_LEFT character. Or if that does
 * not exist from the first strong LEFT_TO_RIGHT character
 *
 * @param text Text base direction should be resolved for.
 * @return SWT#LEFT_RIGHT or SWT#RIGHT_TO_LEFT if the text contains strong
 *         characters and thus the direction can be resolved, SWT#NONE
 *         otherwise.
 * @since 3.105
 */
public static int resolveTextDirection(String text) {
	if (text == null)
		return SWT.NONE;
	int textDirection = SWT.NONE;
	for (int i = 0; i < text.length(); i++) {
		char c = text.charAt(i);
		byte directionality = Character.getDirectionality(c);
		int strongDirection = getStrongDirection(directionality);
		if (strongDirection != SWT.NONE) {
			textDirection = strongDirection;
		}
		if (textDirection == SWT.RIGHT_TO_LEFT) {
			break;
		}
	}
	return textDirection;
}

private static int getStrongDirection(byte directionality) {
	switch (directionality) {
	// Strong bidirectional character types in the Unicode specification:
	case Character.DIRECTIONALITY_LEFT_TO_RIGHT:
		return SWT.LEFT_TO_RIGHT;
	case Character.DIRECTIONALITY_RIGHT_TO_LEFT:
	case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
		return SWT.RIGHT_TO_LEFT;

	// Weak:
//	case Character.DIRECTIONALITY_EUROPEAN_NUMBER: return SWT.NONE;
//	case Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR: return SWT.NONE;
//	case Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR: return SWT.NONE;
	// XXX Arabic Number is not a strong direction, however former windows algorithm
	// would detect some as LEFT_TO_RIGHT and some as RIGHT_TO_LEFT:
//	case Character.DIRECTIONALITY_ARABIC_NUMBER: return SWT.RIGHT_TO_LEFT;
//	case Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR: return SWT.NONE;
//	case Character.DIRECTIONALITY_NONSPACING_MARK: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_BOUNDARY_NEUTRAL: return SWT.NONE;

	// Neutral:
//	case Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_SEGMENT_SEPARATOR: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_WHITESPACE: return SWT.NONE;
//	case Character.DIRECTIONALITY_OTHER_NEUTRALS: return SWT.NONE;

	// Explicit Formatting:
//	case Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_LEFT_TO_RIGHT_ISOLATE: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ISOLATE: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_FIRST_STRONG_ISOLATE: return SWT.LEFT_TO_RIGHT;
//	case Character.DIRECTIONALITY_POP_DIRECTIONAL_ISOLATE: return SWT.LEFT_TO_RIGHT;
	}
	return SWT.NONE;
}

/**
 * Switch the keyboard language to the specified language type.  We do
 * not distinguish between multiple bidi or multiple non-bidi languages, so
 * set the keyboard to the first language of the given type.
 *
 * @param language integer representing language. One of
 * 	KEYBOARD_BIDI, KEYBOARD_NON_BIDI.
 */
public static void setKeyboardLanguage(int language) {
	if (language == getKeyboardLanguage()) return;
	boolean bidi = language == KEYBOARD_BIDI;
	for (long element : getKeyboardLanguageList()) {
		if (bidi == isBidiLang(element)) {
			OS.ActivateKeyboardLayout(element, 0);
			return;
		}
	}
}

/**
 * Override the window proc.
 *
 * @param hwnd control to override the window proc of
 */
private static void subclass(long hwnd) {
	LONG key = new LONG(hwnd);
	if (oldProcMap.get(key) == null) {
		long oldProc = OS.GetWindowLongPtr(hwnd, OS.GWLP_WNDPROC);
		oldProcMap.put(key, new LONG(oldProc));
		OS.SetWindowLongPtr(hwnd, OS.GWLP_WNDPROC, callback.getAddress());
	}
}

/**
 * Remove the overridden the window proc.
 *
 * @param hwnd control to remove the window proc override for
 */
private static void unsubclass(long hwnd) {
	LONG key = new LONG(hwnd);
	if (languageMap.get(key) == null) {
		LONG proc = oldProcMap.remove(key);
		if (proc == null) return;
		OS.SetWindowLongPtr(hwnd, OS.GWLP_WNDPROC, proc.value);
	}
}
/**
 * Window proc to intercept keyboard language switch event (WS_INPUTLANGCHANGE)
 * and widget orientation changes.
 * Run the Control's registered runnable when the keyboard language is switched.
 *
 * @param hwnd handle of the control that is listening for the keyboard language
 *  change event
 * @param msg window message
 */
static long windowProc (long hwnd, long msg, long wParam, long lParam) {
	LONG key = new LONG (hwnd);
	switch ((int)msg) {
		case OS.WM_INPUTLANGCHANGE:
			Runnable runnable = languageMap.get (key);
			if (runnable != null) runnable.run ();
			break;
		}
	LONG oldProc = oldProcMap.get(key);
	return OS.CallWindowProc (oldProc.value, hwnd, (int)msg, wParam, lParam);
}

}

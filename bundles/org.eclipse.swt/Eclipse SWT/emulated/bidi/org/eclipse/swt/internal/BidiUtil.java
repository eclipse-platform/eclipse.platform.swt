package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.GC;

/*
 * Used to bidi enable the StyledText widget.
 */
public class BidiUtil {
	// Keyboard language ids
	public static final int KEYBOARD_LATIN = 0;
	public static final int KEYBOARD_HEBREW = 1;
	public static final int KEYBOARD_ARABIC = 2;

	// getRenderInfo flag
	public static final int CLASSIN = 1;
	public static final int LINKBEFORE = 2;
	public static final int LINKAFTER = 4;

	/*
	 * Public character class constants are the same as Windows 
	 * platform constants. 
	 * Saves conversion of class array in getRenderInfo to arbitrary 
	 * constants for now.
	 */
	public static final int CLASS_HEBREW = 2;
	public static final int CLASS_ARABIC = 2;
	public static final int CLASS_LOCALNUMBER = 4;
	public static final int REORDER = 0;				
	public static final int LIGATE = 0;
	public static final int GLYPHSHAPE = 0;

/*
 * Not implemented.
 */
public static void addLanguageListener(int hwnd, Runnable runnable) {
}
/*
 * Not implemented.
 *
 * gc, renderBuffer, x, y & renderDx are input parameters
 */
public static void drawGlyphs(GC gc, byte[] renderBuffer, int[] renderDx, int x, int y) {
}
/*
 * Not implemented.
 */
public static int getFontStyle(GC gc) {
	return 0;	
}
/*
 *  Not implemented. Returns null.
 *
 *	gc, text & flags are input parameters
 *  classBuffer is input/output parameter
 *	order & dx are output parameters
 */
public static byte[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags) {
	return null;
}
/*
 *  Not implemented. Returns null.
 *
 *	gc, text & flags are input parameters
 *  classBuffer is input/output parameter
 *	order & dx are output parameters
 */
public static byte[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags, int[] offsets) {
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
public static void removeLanguageListener(int hwnd) {
}
/*
 * Not implemented.
 */
public static void setKeyboardLanguage(int language) {
}

}
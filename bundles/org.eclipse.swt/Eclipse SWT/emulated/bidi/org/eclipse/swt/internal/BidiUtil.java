package org.eclipse.swt.internal;
import org.eclipse.swt.graphics.GC;

/*
 * This class is supplied so that the StyledText code that supports bidi text (supported
 * for win platforms) is not platform dependent.  Bidi text is not implemented on 
 * emulated platforms.
 */
public class BidiUtil {
	// Keyboard language ids
	public static final int KEYBOARD_LATIN = 0;
	public static final int KEYBOARD_HEBREW = 1;
	public static final int KEYBOARD_ARABIC = 2;

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
 */
public static void drawGlyphs(GC gc, char[] renderBuffer, int[] renderDx, int x, int y) {
}
/*
 * Bidi not supported on emulated platforms.
 *
 */
public static boolean isBidiPlatform() {
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
 *
 */
public static void getOrderInfo(GC gc, String text, int[] order, byte[] classBuffer, int flags, int [] offsets) {
}
/*
 *  Not implemented. Returns null.
 *
 */
public static char[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags) {
	return null;
}
/*
 *  Not implemented. Returns null.
 *
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
public static void removeLanguageListener(int hwnd) {
}
/*
 * Not implemented.
 */
public static void setKeyboardLanguage(int language) {
}

}
package org.eclipse.swt.internal;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.internal.win32.GCP_RESULTS;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;

import java.util.Hashtable;
/*
 * Wraps Win32 API used to bidi enable the StyledText widget.
 */
public class BidiText {

	// *** BUG need multiple oldProcs ***
	// WM_INPUTLANGCHANGE constants
	static int oldProc;
	static Hashtable map = new Hashtable ();
	static Callback callback = new Callback (BidiText.class, "windowProc", 4);

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
				
	// GetCharacterPlacement constants
	static final int GCP_REORDER = 0x0002;
	static final int GCP_GLYPHSHAPE = 0x0010;
	static final int GCP_LIGATE = 0x0020;
	static final int GCP_CLASSIN = 0x00080000;
	static final byte GCPCLASS_ARABIC = 2;
	static final byte GCPCLASS_HEBREW = 2;	
	static final byte GCPCLASS_LOCALNUMBER = 4;
	static final int GCPGLYPH_LINKBEFORE = 0x8000;
	static final int GCPGLYPH_LINKAFTER = 0x4000;
	// ExtTextOut constants
	static final int ETO_GLYPH_INDEX = 0x0010;
	// Windows primary language identifiers
	static final int LANG_ARABIC = 0x01;
	static final int LANG_HEBREW = 0x0d;
	// ActivateKeyboard constants
	static final int HKL_NEXT = 1;
	static final int HKL_PREV = 0;

/*
 *
 */
public static void addLanguageListener (int hwnd, Runnable runnable) {
	map.put (new Integer (hwnd), runnable);
	oldProc = OS.GetWindowLong (hwnd, OS.GWL_WNDPROC);
	OS.SetWindowLong (hwnd, OS.GWL_WNDPROC, callback.getAddress ());
}
/*
 * Wraps the ExtTextOut function.
 *
 * gc, renderBuffer, x, y & renderDx are input parameters
 */
public static void drawGlyphs(GC gc, byte[] renderBuffer, int[] renderDx, int x, int y) {
	RECT rect = null;

	// why do we have to specify the WORD count, not the byte count?
	// when using the ANSI version of ExtTextOut cbCount is supposed to specify the byte count.
	OS.ExtTextOut(gc.handle, x, y, ETO_GLYPH_INDEX, rect, renderBuffer, renderBuffer.length / 2, renderDx);
}
/*
 *  Wraps GetFontLanguageInfo and GetCharacterPlacement functions.
 *
 *	gc, text & flags are input parameters
 *  classBuffer is input/output parameter
 *	order & dx are output parameters
 */
public static byte[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags) {
	int fontLanguageInfo = OS.GetFontLanguageInfo(gc.handle);
	int hHeap = OS.GetProcessHeap();
	int[] lpCs = new int[8];
	int cs = OS.GetTextCharset(gc.handle);
	OS.TranslateCharsetInfo(cs, lpCs, OS.TCI_SRCCHARSET);
	byte[] textBuffer = Converter.wcsToMbcs(lpCs[1], text, false);
	int byteCount = textBuffer.length;
	boolean linkBefore = (flags & LINKBEFORE) == LINKBEFORE;
	boolean linkAfter = (flags & LINKAFTER) == LINKAFTER;

	GCP_RESULTS result = new GCP_RESULTS();
	result.lStructSize = GCP_RESULTS.sizeof;
	result.nGlyphs = byteCount;

	result.lpOrder = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount * 4);
	result.lpDx = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount * 4);
	result.lpClass = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	result.lpGlyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount * 2);
				
	// set required dwFlags
	int dwFlags = 0;
	int glyphFlags = 0;
	if (((fontLanguageInfo & GCP_REORDER) == GCP_REORDER)) {
		dwFlags |= GCP_REORDER;
	}
	if ((fontLanguageInfo & GCP_LIGATE) == GCP_LIGATE) {
		dwFlags |= GCP_LIGATE;
		glyphFlags |= 0;
	}
	if ((fontLanguageInfo & GCP_GLYPHSHAPE) == GCP_GLYPHSHAPE) {
		dwFlags |= GCP_GLYPHSHAPE;
		if (linkBefore) {
			glyphFlags |= GCPGLYPH_LINKBEFORE;
		}		
		if (linkAfter) {
			glyphFlags |= GCPGLYPH_LINKAFTER;
		}
	}
	byte[] lpGlyphs;
	if (linkBefore || linkAfter) {
		lpGlyphs = new byte[2];
		lpGlyphs[0]=(byte)glyphFlags;
		lpGlyphs[1]=(byte)(glyphFlags >> 8);
	}
	else {
		lpGlyphs = new byte[] {(byte) glyphFlags};
	}
	OS.MoveMemory(result.lpGlyphs, lpGlyphs, lpGlyphs.length);
	
	if ((flags & CLASSIN) == CLASSIN) {
		// set classification values for the substring
		dwFlags |= GCP_CLASSIN;
		OS.MoveMemory(result.lpClass, classBuffer, classBuffer.length);
	}	
	OS.GetCharacterPlacement(gc.handle, textBuffer, byteCount, 0, result, dwFlags);
	
	if (dx != null) OS.MoveMemory(dx, result.lpDx, dx.length * 4);
	if (order != null) OS.MoveMemory(order, result.lpOrder, order.length * 4);
	if (classBuffer != null) OS.MoveMemory(classBuffer, result.lpClass, classBuffer.length);

	byte[] glyphBuffer = new byte[result.nGlyphs * 2];
	OS.MoveMemory(glyphBuffer, result.lpGlyphs, glyphBuffer.length);	

	/* Free the memory that was allocated. */
	OS.HeapFree(hHeap, 0, result.lpGlyphs);
	OS.HeapFree(hHeap, 0, result.lpClass);
	OS.HeapFree(hHeap, 0, result.lpDx);	
	OS.HeapFree(hHeap, 0, result.lpOrder);	
	return glyphBuffer;
}
/*
 * 
 */
public static int getKeyboardLanguage() {
	int layout = OS.GetKeyboardLayout(0);
	// only interested in low 2 bytes, which is the primary
	// language identifier
	layout = layout & 0x000000FF;
	if (layout == LANG_HEBREW) return KEYBOARD_HEBREW;
	if (layout == LANG_ARABIC) return KEYBOARD_ARABIC;
	// return LATIN for all non-bidi languages
	return KEYBOARD_LATIN;
}
/*
 * 
 */
static int[] getKeyboardLanguageList() {
	int maxSize = 10;
	int[] tempList = new int[maxSize];
	int size = OS.GetKeyboardLayoutList(maxSize, tempList);
	int[] list = new int[size];
	System.arraycopy(tempList, 0, list, 0, size);
	return list;
}
/*
 * 
 */
public static void removeLanguageListener (int hwnd) {
	map.remove (new Integer (hwnd));
	OS.SetWindowLong (hwnd, OS.GWL_WNDPROC, oldProc);
}		
/*
 * 
 */
public static void setKeyboardLanguage(int language) {
	// don't switch the keyboard if it doesn't need to be
	if (language == getKeyboardLanguage()) return;
	
	boolean isBidiLang = (language == KEYBOARD_HEBREW) || (language == KEYBOARD_ARABIC);		
	// get the corresponding WIN language id for the
	// language
	if (isBidiLang) {
		int langId;
		if (language == KEYBOARD_HEBREW) langId = LANG_HEBREW;
		else langId = LANG_ARABIC;		
		// get the list of active languages
		int[] list = getKeyboardLanguageList();
		// set to first language of the given type
		for (int i=0; i<list.length; i++) {
			int id = list[i] & 0x000000FF;
			if (id == langId) {
				OS.ActivateKeyboardLayout(list[i], 0);
				return;
			}
		}
	} else {
		// set to the first "Latin" language (anything not
		// hebrew or arabic)
		int[] list = getKeyboardLanguageList();
		for (int i=0; i<list.length; i++) {
			int id = list[i] & 0x000000FF;
			if ((id != LANG_HEBREW) && (id != LANG_ARABIC)) {
				OS.ActivateKeyboardLayout(list[i], 0);
				return;
			}
		}
	}

}
static int windowProc (int hwnd, int msg, int wParam, int lParam) {
	switch (msg) {
		case 0x51 /*OS.WM_INPUTLANGCHANGE*/:
			Runnable runnable = (Runnable) map.get (new Integer (hwnd));
			if (runnable != null) runnable.run ();
			break;
		}
	return OS.CallWindowProc (oldProc, hwnd, msg, wParam, lParam);
}

}
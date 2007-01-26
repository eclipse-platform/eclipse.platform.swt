package org.eclipse.swt.browser;


import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.GREVersionRange;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;

	static final String GRERANGE_LOWER = "1.8"; //$NON-NLS-1$
	static final boolean LowerRangeInclusive = true;
	static final String GRERANGE_UPPER = "1.9"; //$NON-NLS-1$
	static final boolean UpperRangeInclusive = false;

MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int /*long*/ handle) {
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (handle);
}

int /*long*/ getHandle () {
	return browser.handle;
}

public static char[] mbcsToWcs (String codePage, byte[] buffer) {
	char[] chars = new char[buffer.length];
	int charCount = OS.MultiByteToWideChar (OS.CP_ACP, OS.MB_PRECOMPOSED, buffer, buffer.length, chars, chars.length);
	if (charCount == chars.length) return chars;
	char[] result = new char[charCount];
	System.arraycopy (chars, 0, result, 0, charCount);
	return result;
}

public static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	int byteCount;
	char[] chars = new char[string.length()];
	string.getChars (0, chars.length, chars, 0);
	byte[] bytes = new byte[byteCount = chars.length * 2 + (terminate ? 1 : 0)];
	byteCount = OS.WideCharToMultiByte (OS.CP_ACP, 0, chars, chars.length, bytes, byteCount, null, null);
	if (terminate) {
		byteCount++;
	} else {
		if (bytes.length != byteCount) {
			byte[] result = new byte[byteCount];
			System.arraycopy (bytes, 0, result, 0, byteCount);
			bytes = result;
		}
	}
	return bytes;
}

GREVersionRange getGREVersionRange() {
	GREVersionRange range = new GREVersionRange ();
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, GRERANGE_LOWER, true);
	int /*long*/ lower = C.malloc (bytes.length);
	C.memmove (lower, bytes, bytes.length);
	range.lower = lower;
	range.lowerInclusive = LowerRangeInclusive;
	
	bytes = MozillaDelegate.wcsToMbcs (null, GRERANGE_UPPER, true);
	int /*long*/ upper = C.malloc (bytes.length);
	C.memmove (upper, bytes, bytes.length);
	range.upper = upper;
	range.upperInclusive = UpperRangeInclusive;
	return range;
}

void onDispose (int /*long*/ embedHandle) {
}

void setSize (int /*long*/ embedHandle, int width, int height) {
}

}

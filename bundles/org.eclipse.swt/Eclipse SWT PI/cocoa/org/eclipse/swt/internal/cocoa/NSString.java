package org.eclipse.swt.internal.cocoa;

public class NSString extends NSObject {

public NSString(int id) {
	super(id);
}

public static NSString stringWith(char[] chars, int length) {
	int id = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_1length_1, chars, length);
	return id != 0 ? new NSString(id) : null;
}

public static NSString stringWith(String string) {
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	return NSString.stringWith(buffer, buffer.length);
}
}

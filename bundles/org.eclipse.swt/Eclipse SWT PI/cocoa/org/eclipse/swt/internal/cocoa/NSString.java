package org.eclipse.swt.internal.cocoa;

public class NSString extends NSObject {

public NSString(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSString;
}

public void drawAtPoint(NSPoint pt, NSObject attributes) {
	OS.objc_msgSend(id, OS.sel_drawAtPoint_1withAttributes_1, pt, attributes != null ? attributes.id : 0);
}

public void getCharacters(char[] chars) {
	OS.objc_msgSend(id, OS.sel_getCharacters_1, chars);
}

public int length() {
	return OS.objc_msgSend(id, OS.sel_length);
}

public static NSString stringWith(char[] chars, int length) {
	int id = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_1length_1, chars, length);
	return id != 0 ? new NSString(id) : null;
}

public static NSString stringWithUTF8String(String string) {
	int id = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithUTF8String_1, string);
	return id != 0 ? new NSString(id) : null;
}

public static NSString stringWith(String string) {
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	return NSString.stringWith(buffer, buffer.length);
}
}

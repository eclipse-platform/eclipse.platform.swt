package org.eclipse.swt.internal.win32;

public class TCHAR {
	
int codePage;
char [] chars;
byte [] bytes;
int byteCount;

public final static int sizeof = OS.IsUnicode ? 2 : 1;

public TCHAR (int codePage, int length) {
	this.codePage = codePage;
	if (OS.IsUnicode) {
		chars = new char [length];
	} else {
		bytes = new byte [byteCount = length];
	}
}

//BOGUS - optimize or inline
public TCHAR (int codePage, char ch, boolean terminate) {
	this (codePage, String.valueOf (ch), terminate);
}

public TCHAR (int codePage, String string, boolean terminate) {
	this.codePage = codePage;
	int charCount = string.length ();
	char [] chars = new char [charCount + (terminate ? 1 : 0)];
	string.getChars (0, charCount, chars, 0);
	if (OS.IsUnicode) {
		this.chars = chars;
	} else {
		int cp = codePage != 0 ? codePage : OS.CP_ACP;
		bytes = new byte [byteCount = charCount * 2 + (terminate ? 1 : 0)];
		byteCount = OS.WideCharToMultiByte (cp, 0, chars, charCount, bytes, byteCount, null, null);
		if (terminate) byteCount++;
	}
}

public int length () {
	if (OS.IsUnicode) {
		return chars.length;
	} else {
		return byteCount;
	}
}

public int strlen () {
	if (OS.IsUnicode) {
		for (int i=0; i<chars.length; i++) {
			if (chars [i] == '\0') return i;
		}
		return chars.length;
	} else {
		for (int i=0; i<byteCount; i++) {
			if (bytes [i] == '\0') return i;
		}
		return byteCount;
	}
}

public int tcharAt (int index) {
	if (OS.IsUnicode) {
		return chars [index];
	} else {
		int ch = bytes [index] & 0xFF;
		if (OS.IsDBCSLeadByte ((byte) ch)) {
			ch = ch << 8 | (bytes [index + 1] & 0xFF);
		}
		return ch;
	}
}

public String toString () {
	return toString (0, length ());
}

public String toString (int start, int length) {
	if (OS.IsUnicode) {
		return new String (chars, start, length);
	} else {
		byte [] bytes = this.bytes;
		if (start != 0) {
			bytes = new byte [length];
			System.arraycopy (this.bytes, start, bytes, 0, length);
		}
		char [] chars = new char [length];
		int cp = codePage != 0 ? codePage : OS.CP_ACP;
		int charCount = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, bytes, length, chars, length);
		return new String (chars, 0, charCount);
	}
}

}


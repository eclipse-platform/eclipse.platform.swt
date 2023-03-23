/*******************************************************************************
 * Copyright (c) 2020 Nikita Nemkin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin <nikita@nemkin.ru> - initial implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.*;

import org.eclipse.swt.*;

class JSON {

// Note: supported types are limited, see Browser.evaluate and BrowserFunction.function.

static class Reader {
	char[] input;
	int pos, end;
	StringBuilder sb;

	public Reader(char[] input, int start, int end) {
		this.input = input;
		this.pos = start;
		this.end = end;
	}

	static enum Control {
		END, ARRAY_END, COMMA
	}

	char nextChar() {
		return (pos < end) ? input[pos++] : '\0';
	}

	void error() {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [decoding error at " + (pos - 1) +"]");
	}

	void readLiteral(String literal) {
		for (int i = 0, len = literal.length(); i < len; i++) {
			if (nextChar() != literal.charAt(i)) {
				error();
			}
		}
	}

	int nextHexDigit() {
		char c = nextChar();
		if ('0' <= c && c <= '9') return c - '0';
		if ('a' <= c && c <= 'f') return c - 'a' + 10;
		if ('A' <= c && c <= 'F') return c - 'A' + 10;
		error();
		return 0;
	}

	char readEscape() {
		char c = nextChar();
		switch (c) {
		case '"':
		case '\\':
		case '/': break;
		case 'b': c = '\b'; break;
		case 'f': c = '\f'; break;
		case 'n': c = '\n'; break;
		case 'r': c = '\r'; break;
		case 't': c = '\t'; break;
		case 'u':
			c = (char)((nextHexDigit() << 12) | (nextHexDigit() << 8)
					| (nextHexDigit() << 4) | nextHexDigit());
			break;
		default: error();
		}
		return c;
	}

	String readString() {
		char c;
		int start = pos;
		do {
			c = nextChar();
			if (c < 0x20) error();
			if (c == '\\') {
				if (sb == null) sb = new StringBuilder();
				sb.append(input, start, pos - start - 1);
				sb.append(readEscape());
				start = pos;
			}
		} while (c != '"');

		if (sb != null) {
			sb.append(input, start, pos - 1 - start);
			String result = sb.toString();
			sb.setLength(0);
			return result;
		}
		return String.valueOf(input, start, pos - start - 1);
	}

	double readNumber() {
		int start = pos - 1;
		while (true) {
			char c = nextChar();
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
			case 'e':
			case 'E':
			case '+':
			case '-': continue;
			default:
				pos--;
			case '\0':
				try {
					return Double.parseDouble(String.valueOf(input, start, pos - start));
				} catch (NumberFormatException e) {
					error();
				}
			}
		}
	}

	Object readAny() {
		while (true) {
			char c = nextChar();
			switch (c) {
			case ' ':
			case '\t':
			case '\r':
			case '\n': continue;
			case '\0': return Control.END;
			case '[': return readArray();
			case ']': return Control.ARRAY_END;
			case ',': return Control.COMMA;
			case '"': return readString();
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '-': return readNumber();
			case 'n': readLiteral("ull"); return null;
			case 't': readLiteral("rue"); return true;
			case 'f': readLiteral("alse"); return false;
			default: error();
			}
		}
	}

	Object readArray() {
		Object item = readAny();
		if (item == Control.ARRAY_END) return new Object[0];
		if (item instanceof Control) error();
		List<Object> items = new ArrayList<>();
		items.add(item);
		while (true) {
			Object sep = readAny();
			if (sep == Control.ARRAY_END) break;
			if (sep != Control.COMMA) error();
			item = readAny();
			if (item instanceof Control) error();
			items.add(item);
		}
		return items.toArray();
	}

	Object readTop() {
		Object item = readAny();
		if (item instanceof Control) error();
		if (readAny() != Control.END) error();
		return item;
	}
}

static class Writer {
	static final String[] ESCAPED = new String[96];
	static {
		for (int i = 0; i < 0x20; i++) {
			ESCAPED[i] = String.format("\\u%04x", i);
		}
		ESCAPED['\b'] = "\\b";
		ESCAPED['\f'] = "\\f";
		ESCAPED['\n'] = "\\n";
		ESCAPED['\r'] = "\\r";
		ESCAPED['\t'] = "\\t";
		ESCAPED['"'] = "\\\"";
		ESCAPED['\\'] = "\\\\";		
	}

	StringBuilder sb = new StringBuilder();

	public Writer(Object object) {
		writeAny(object);
	}

	void writeAny(Object object) {
		if (object == null) {
			sb.append("null");
		} else if (object instanceof Boolean) {
			sb.append(object.toString());
		} else if (object instanceof Long) {
			sb.append((long)object);
		} else if (object instanceof Integer) {
			sb.append((int)object);
		} else if (object instanceof Short) {
			sb.append((short)object);
		} else if (object instanceof Byte) {
			sb.append((byte)object);
		} else if (object instanceof Double) {
			sb.append((double)object);
		} else if (object instanceof Float) {
			sb.append((float)object);
		} else if (object instanceof String) {
			writeString(object.toString());
		} else if (object instanceof Object[]) {
			writeArray((Object[])object);
		} else {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [object not encodable: " + object.getClass() + "]");
		}
	}

	void writeString(String s) {
		sb.append('"');
		int start = 0;
		for (int i = 0, len = s.length(); i < len; i++) {
			char c = s.charAt(i);
			if (c < ESCAPED.length && ESCAPED[c] != null) {
				sb.append(s, start, i);
				sb.append(ESCAPED[c]);
				start = i + 1;
			}
		}
		sb.append(s, start, s.length());
		sb.append('"');
	}

	void writeArray(Object[] array) {
		sb.append('[');
		boolean first = true;
		for (Object item : array) {
			if (!first) sb.append(',');
			writeAny(item);
			first = false;
		}
		sb.append(']');
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}

public static Object parse(char[] input) {
	return new Reader(input, 0, input.length).readTop();
}

public static Object parse(String input) {
	return parse(input.toCharArray());
}

public static String stringify(Object object) {
	return new Writer(object).toString();
}

}

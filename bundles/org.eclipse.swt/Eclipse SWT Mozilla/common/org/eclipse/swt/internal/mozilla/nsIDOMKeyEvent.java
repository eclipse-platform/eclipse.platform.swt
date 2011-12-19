/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDOMKeyEvent extends nsIDOMUIEvent {

	static final int LAST_METHOD_ID = nsIDOMUIEvent.LAST_METHOD_ID + 7;

	public static final String NS_IDOMKEYEVENT_IID_STR =
		"028e0e6e-8b01-11d3-aae7-0010838a3123";

	public static final String NS_IDOMKEYEVENT_8_IID_STR =
		"e44d7977-20f2-442e-bc13-0f2f52992a4c";
	
	public static final nsID NS_IDOMKEYEVENT_IID =
		new nsID(NS_IDOMKEYEVENT_IID_STR);

	public static final nsID NS_IDOMKEYEVENT_8_IID =
		new nsID(NS_IDOMKEYEVENT_8_IID_STR);
	
	public nsIDOMKeyEvent(int /*long*/ address) {
		super(address);
	}

	public static final int DOM_VK_CANCEL = 3;
	public static final int DOM_VK_HELP = 6;
	public static final int DOM_VK_BACK_SPACE = 8;
	public static final int DOM_VK_TAB = 9;
	public static final int DOM_VK_CLEAR = 12;
	public static final int DOM_VK_RETURN = 13;
	public static final int DOM_VK_ENTER = 14;
	public static final int DOM_VK_SHIFT = 16;
	public static final int DOM_VK_CONTROL = 17;
	public static final int DOM_VK_ALT = 18;
	public static final int DOM_VK_PAUSE = 19;
	public static final int DOM_VK_CAPS_LOCK = 20;
	public static final int DOM_VK_ESCAPE = 27;
	public static final int DOM_VK_SPACE = 32;
	public static final int DOM_VK_PAGE_UP = 33;
	public static final int DOM_VK_PAGE_DOWN = 34;
	public static final int DOM_VK_END = 35;
	public static final int DOM_VK_HOME = 36;
	public static final int DOM_VK_LEFT = 37;
	public static final int DOM_VK_UP = 38;
	public static final int DOM_VK_RIGHT = 39;
	public static final int DOM_VK_DOWN = 40;
	public static final int DOM_VK_PRINTSCREEN = 44;
	public static final int DOM_VK_INSERT = 45;
	public static final int DOM_VK_DELETE = 46;
	public static final int DOM_VK_0 = 48;
	public static final int DOM_VK_1 = 49;
	public static final int DOM_VK_2 = 50;
	public static final int DOM_VK_3 = 51;
	public static final int DOM_VK_4 = 52;
	public static final int DOM_VK_5 = 53;
	public static final int DOM_VK_6 = 54;
	public static final int DOM_VK_7 = 55;
	public static final int DOM_VK_8 = 56;
	public static final int DOM_VK_9 = 57;
	public static final int DOM_VK_SEMICOLON = 59;
	public static final int DOM_VK_EQUALS = 61;
	public static final int DOM_VK_A = 65;
	public static final int DOM_VK_B = 66;
	public static final int DOM_VK_C = 67;
	public static final int DOM_VK_D = 68;
	public static final int DOM_VK_E = 69;
	public static final int DOM_VK_F = 70;
	public static final int DOM_VK_G = 71;
	public static final int DOM_VK_H = 72;
	public static final int DOM_VK_I = 73;
	public static final int DOM_VK_J = 74;
	public static final int DOM_VK_K = 75;
	public static final int DOM_VK_L = 76;
	public static final int DOM_VK_M = 77;
	public static final int DOM_VK_N = 78;
	public static final int DOM_VK_O = 79;
	public static final int DOM_VK_P = 80;
	public static final int DOM_VK_Q = 81;
	public static final int DOM_VK_R = 82;
	public static final int DOM_VK_S = 83;
	public static final int DOM_VK_T = 84;
	public static final int DOM_VK_U = 85;
	public static final int DOM_VK_V = 86;
	public static final int DOM_VK_W = 87;
	public static final int DOM_VK_X = 88;
	public static final int DOM_VK_Y = 89;
	public static final int DOM_VK_Z = 90;
	public static final int DOM_VK_NUMPAD0 = 96;
	public static final int DOM_VK_NUMPAD1 = 97;
	public static final int DOM_VK_NUMPAD2 = 98;
	public static final int DOM_VK_NUMPAD3 = 99;
	public static final int DOM_VK_NUMPAD4 = 100;
	public static final int DOM_VK_NUMPAD5 = 101;
	public static final int DOM_VK_NUMPAD6 = 102;
	public static final int DOM_VK_NUMPAD7 = 103;
	public static final int DOM_VK_NUMPAD8 = 104;
	public static final int DOM_VK_NUMPAD9 = 105;
	public static final int DOM_VK_MULTIPLY = 106;
	public static final int DOM_VK_ADD = 107;
	public static final int DOM_VK_SEPARATOR = 108;
	public static final int DOM_VK_SUBTRACT = 109;
	public static final int DOM_VK_DECIMAL = 110;
	public static final int DOM_VK_DIVIDE = 111;
	public static final int DOM_VK_F1 = 112;
	public static final int DOM_VK_F2 = 113;
	public static final int DOM_VK_F3 = 114;
	public static final int DOM_VK_F4 = 115;
	public static final int DOM_VK_F5 = 116;
	public static final int DOM_VK_F6 = 117;
	public static final int DOM_VK_F7 = 118;
	public static final int DOM_VK_F8 = 119;
	public static final int DOM_VK_F9 = 120;
	public static final int DOM_VK_F10 = 121;
	public static final int DOM_VK_F11 = 122;
	public static final int DOM_VK_F12 = 123;
	public static final int DOM_VK_F13 = 124;
	public static final int DOM_VK_F14 = 125;
	public static final int DOM_VK_F15 = 126;
	public static final int DOM_VK_F16 = 127;
	public static final int DOM_VK_F17 = 128;
	public static final int DOM_VK_F18 = 129;
	public static final int DOM_VK_F19 = 130;
	public static final int DOM_VK_F20 = 131;
	public static final int DOM_VK_F21 = 132;
	public static final int DOM_VK_F22 = 133;
	public static final int DOM_VK_F23 = 134;
	public static final int DOM_VK_F24 = 135;
	public static final int DOM_VK_NUM_LOCK = 144;
	public static final int DOM_VK_SCROLL_LOCK = 145;
	public static final int DOM_VK_COMMA = 188;
	public static final int DOM_VK_PERIOD = 190;
	public static final int DOM_VK_SLASH = 191;
	public static final int DOM_VK_BACK_QUOTE = 192;
	public static final int DOM_VK_OPEN_BRACKET = 219;
	public static final int DOM_VK_BACK_SLASH = 220;
	public static final int DOM_VK_CLOSE_BRACKET = 221;
	public static final int DOM_VK_QUOTE = 222;
	public static final int DOM_VK_META = 224;
	
	/* the follow constants are defined in Mozilla 8 */
	public static final int DOM_VK_KANA = 21;
	public static final int DOM_VK_HANGUL = 21;
	public static final int DOM_VK_JUNJA = 23;
	public static final int DOM_VK_FINAL = 24;
	public static final int DOM_VK_HANJA = 25;
	public static final int DOM_VK_KANJI = 25;
	public static final int DOM_VK_CONVERT = 28;
	public static final int DOM_VK_NONCONVERT = 29;
	public static final int DOM_VK_ACCEPT = 30;
	public static final int DOM_VK_MODECHANGE = 31;
	public static final int DOM_VK_SELECT = 41;
	public static final int DOM_VK_PRINT = 42;
	public static final int DOM_VK_EXECUTE = 43;
	public static final int DOM_VK_CONTEXT_MENU = 93;
	public static final int DOM_VK_SLEEP = 95;


	public int GetCharCode(int[] aCharCode) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 1, getAddress(), aCharCode);
	}

	public int GetKeyCode(int[] aKeyCode) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 2, getAddress(), aKeyCode);
	}

	public int GetAltKey(int[] aAltKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 3, getAddress(), aAltKey);
	}

	public int GetCtrlKey(int[] aCtrlKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 4, getAddress(), aCtrlKey);
	}

	public int GetShiftKey(int[] aShiftKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 5, getAddress(), aShiftKey);
	}

	public int GetMetaKey(int[] aMetaKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 6, getAddress(), aMetaKey);
	}

	public int InitKeyEvent(int /*long*/ typeArg, int canBubbleArg, int cancelableArg, int /*long*/ viewArg, int ctrlKeyArg, int altKeyArg, int shiftKeyArg, int metaKeyArg, int keyCodeArg, int charCodeArg) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 7, getAddress(), typeArg, canBubbleArg, cancelableArg, viewArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, keyCodeArg, charCodeArg);
	}
}

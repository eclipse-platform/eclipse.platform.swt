/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.Qt.KeyboardModifiers;
import com.trolltech.qt.gui.QKeyEvent;

import org.eclipse.swt.SWT;

/**
 * Helper class for Key translation SWT &lt;-&gt; Qt
 */
public class KeyUtil {
	/* Key Mappings */
	private static final int[][] KeyTable = {
	/* Keyboard and Mouse Masks */
	{ Key.Key_Alt.value(), SWT.ALT }, { Key.Key_AltGr.value(), SWT.ALT }, { Key.Key_Meta.value(), SWT.ALT },
			{ Key.Key_Shift.value(), SWT.SHIFT }, { Key.Key_Control.value(), SWT.CONTROL },
			/* Non-Numeric Keypad Keys */
			{ Key.Key_Up.value(), SWT.ARROW_UP }, { Key.Key_Down.value(), SWT.ARROW_DOWN },
			{ Key.Key_Left.value(), SWT.ARROW_LEFT }, { Key.Key_Right.value(), SWT.ARROW_RIGHT },
			{ Key.Key_PageUp.value(), SWT.PAGE_UP }, { Key.Key_PageDown.value(), SWT.PAGE_DOWN },
			{ Key.Key_Home.value(), SWT.HOME }, { Key.Key_End.value(), SWT.END },
			{ Key.Key_Insert.value(), SWT.INSERT },
			/* Virtual and Ascii Keys */
			{ Key.Key_Space.value(), ' ' }, { Key.Key_Backspace.value(), SWT.BS }, { Key.Key_Return.value(), SWT.CR },
			{ Key.Key_Delete.value(), SWT.DEL }, { Key.Key_Escape.value(), SWT.ESC },
			{ Key.Key_Enter.value(), SWT.CR }, { Key.Key_Tab.value(), SWT.TAB }, { Key.Key_Backtab.value(), SWT.TAB },
			/* Functions Keys */
			{ Key.Key_F1.value(), SWT.F1 }, { Key.Key_F2.value(), SWT.F2 }, { Key.Key_F3.value(), SWT.F3 },
			{ Key.Key_F4.value(), SWT.F4 }, { Key.Key_F5.value(), SWT.F5 }, { Key.Key_F6.value(), SWT.F6 },
			{ Key.Key_F7.value(), SWT.F7 }, { Key.Key_F8.value(), SWT.F8 }, { Key.Key_F9.value(), SWT.F9 },
			{ Key.Key_F10.value(), SWT.F10 }, { Key.Key_F11.value(), SWT.F11 }, { Key.Key_F12.value(), SWT.F12 },
			{ Key.Key_F13.value(), SWT.F13 }, { Key.Key_F14.value(), SWT.F14 }, { Key.Key_F15.value(), SWT.F15 },
			/* Other keys */
			{ Key.Key_CapsLock.value(), SWT.CAPS_LOCK }, { Key.Key_NumLock.value(), SWT.NUM_LOCK },
			{ Key.Key_ScrollLock.value(), SWT.SCROLL_LOCK }, { Key.Key_Pause.value(), SWT.PAUSE },
			{ Key.Key_Print.value(), SWT.PRINT_SCREEN }, { Key.Key_Help.value(), SWT.HELP },

	};

	private static final int[][] KeyPadKeyTable = {
	/* Numeric Keypad Keys */
	{ Key.Key_Asterisk.value(), SWT.KEYPAD_MULTIPLY }, { Key.Key_Plus.value(), SWT.KEYPAD_ADD },
			{ Key.Key_Enter.value(), SWT.KEYPAD_CR }, { Key.Key_Minus.value(), SWT.KEYPAD_SUBTRACT },
			{ Key.Key_Period.value(), SWT.KEYPAD_DECIMAL }, { Key.Key_Slash.value(), SWT.KEYPAD_DIVIDE },
			{ Key.Key_0.value(), SWT.KEYPAD_0 }, { Key.Key_1.value(), SWT.KEYPAD_1 },
			{ Key.Key_2.value(), SWT.KEYPAD_2 }, { Key.Key_3.value(), SWT.KEYPAD_3 },
			{ Key.Key_4.value(), SWT.KEYPAD_4 }, { Key.Key_5.value(), SWT.KEYPAD_5 },
			{ Key.Key_6.value(), SWT.KEYPAD_6 }, { Key.Key_7.value(), SWT.KEYPAD_7 },
			{ Key.Key_8.value(), SWT.KEYPAD_8 }, { Key.Key_9.value(), SWT.KEYPAD_9 },
			{ Key.Key_Equal.value(), SWT.KEYPAD_EQUAL }, };

	private KeyUtil() {
		// utility class
	}

	public static int translateKey(int key) {
		for (int i = 0; i < KeyTable.length; i++) {
			if (KeyTable[i][0] == key) {
				return KeyTable[i][1];
			}
		}
		return 0;
	}

	public static int untranslateKey(int key) {
		for (int i = 0; i < KeyTable.length; i++) {
			if (KeyTable[i][1] == key) {
				return KeyTable[i][0];
			}
		}
		return 0;
	}

	// Converts keys from Qt to SWT
	public static final int translateKey(QKeyEvent qEvent) {
		int key = qEvent.key();
		boolean keypad = qEvent.modifiers().isSet(KeyboardModifier.KeypadModifier);
		if (keypad) {
			for (int i = 0; i < KeyPadKeyTable.length; i++) {
				if (KeyPadKeyTable[i][0] == key) {
					return KeyPadKeyTable[i][1];
				}
			}
		}
		for (int i = 0; i < KeyTable.length; i++) {
			if (KeyTable[i][0] == key) {
				return KeyTable[i][1];
			}
		}
		return 0;
	}

	// Converts modifiers from Qt to SWT
	public static final int translateModifiers(KeyboardModifiers nativeModifiers) {
		int modifiers = 0;
		if (nativeModifiers.isSet(KeyboardModifier.AltModifier)) {
			modifiers |= SWT.ALT;
		}
		if (nativeModifiers.isSet(KeyboardModifier.ShiftModifier)) {
			modifiers |= SWT.SHIFT;
		}
		if (nativeModifiers.isSet(KeyboardModifier.ControlModifier)) {
			modifiers |= SWT.CONTROL;
		}
		return modifiers;
	}

}

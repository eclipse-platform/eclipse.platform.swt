/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.events.KeyEvent
 *
 * @see org.eclipse.swt.events.KeyEvent
 */
public class Test_org_eclipse_swt_events_KeyEvent extends KeyboardLayoutTest {
	// Windows layouts suitable for 'LoadKeyboardLayout()', obtained from 'GetKeyboardLayoutName()'
	static char[] LAYOUT_BENGALI        = "00020445\0".toCharArray();
	static char[] LAYOUT_BULGARIAN      = "00030402\0".toCharArray();
	static char[] LAYOUT_ENGLISH_US     = "00000409\0".toCharArray();
	static char[] LAYOUT_ENGLISH_INTL   = "00020409\0".toCharArray();
	static char[] LAYOUT_ENGLISH_DVORAK = "00010409\0".toCharArray();
	static char[] LAYOUT_FRENCH         = "0000040C\0".toCharArray();
	static char[] LAYOUT_GERMAN         = "00000407\0".toCharArray();
	static char[] LAYOUT_HEBREW         = "0002040D\0".toCharArray();
	static char[] LAYOUT_LITHUANIAN     = "00010427\0".toCharArray();
	static char[] LAYOUT_MARATHI        = "0000044E\0".toCharArray();
	static char[] LAYOUT_NEPALI         = "00000461\0".toCharArray();

	static class KeyDescription {
		UsScan scanCode;
		int    keyCode;
		char   charNormal;
		char   charShift;

		public KeyDescription(UsScan scanCode, int keyCode, char charNormal, char charShift) {
			this.scanCode = scanCode;
			this.keyCode = keyCode;
			this.charNormal = charNormal;
			this.charShift = charShift;
		}
	}

	private static char ctrlChar(char c) {
		if ((c >= 'a') && (c <= 'z')) {
			return (char) (c - 0x60);
		} else if ((c >= 0x40) && (c < 0x60)) {
			return (char) (c - 0x40);
		} else {
			return c;
		}
	}

	/**
	 * Various system hotkeys that prevent test from succeeding
	 */
	private boolean isSystemHotkey(int state, UsScan scanCode) {
		switch (scanCode) {
			case Esc:
				switch (state) {
					case __C_:  // Opens Start menu
					case A___:  // Switches between windows
					case __CS:  // Opens Task Manager
					case A__S:  // Switches between windows
					case A_C_:  // Switches between windows
					case A_CS:  // Switches between windows
						return true;
				}
				break;
			case _0:
				if (state == __CS) {
					// Windows bug. Ctrl+Shift+0 is assigned to switch to Japanese layout,
					// even if Japanese layout is not installed. This is due to presence of
					// these registry settings:
					//   [HKEY_CURRENT_USER\Control Panel\Input Method\Hot Keys\00000104]
					//   "Key Modifiers"=hex:06,c0,00,00
					//   "Target IME"=hex:11,04,01,e0
					//   "Virtual Key"=hex:30,00,00,00
					// Such shortcuts are handled by hardcoded hook 'user32!CtfHookProcWorker()'
					// called from kernel. The result is that Windows "steals" WM_KEYDOWN event.
					return true;
				}
				break;
			case Tab:
				switch (state) {
					case A___:  // Switches between windows
					case A__S:  // Switches between windows
					case A_C_:  // Switches between windows
					case A_CS:  // Switches between windows
						return true;
				}
				break;
			case Space:
				switch (state) {
					case A___:  // Opens system menu for a Shell
					case A__S:  // Opens system menu for a Shell
						return true;
				}
				break;
			case F4:
				switch (state) {
					case A___:  // Closes current Shell
					case A__S:  // Closes current Shell
						return true;
				}
				break;
			case Num0:
			case Num1:
			case Num2:
			case Num3:
			case Num4:
			case Num5:
			case Num6:
			case Num7:
			case Num8:
			case Num9:
			case NumDel:
				switch (state) {
					case A___:  // Allows to enter unicode characters
					case A__S:  // Keyboard layout switching
						return true;
				}
				break;
			default:
				break;
		}

		if (scanCode == UsScan.F12) {
			if ((state == ____) || (state == ___S)) {
				// Debugging hotkeys set in kernel, see
				// [HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\AeDebug]
				// Somehow, these keys seem to work fine in tests, so let's test them anyway.
				return false;
			}
		}

		if ((scanCode == UsScan.Del) || (scanCode == UsScan.NumDel)) {
			if (state == A_C_) {
				// The famous Ctrl+Alt+Del.
				// Even though it's ignored when emulated (and test works), let's skip.
				// Because what's the point in testing it?
				return true;
			}
		}

		if (isRegisteredHotkey(state, scanCode)) {
			System.out.println("Skipping global hotkey " + getKeyName(state, scanCode) + " (installed with RegisterHotKey)");
			return true;
		}

		return false;
	}

	/**
	 * Extensive test for English US layout
	 */
	@Test
	public void testEnglishUs_stdKeys() {
		// This table intentionally provides "naive" expected values. Actual
		// expected values have some discrepancies from that, see code below.
		// This approach is used to highlight anything non-trivial.
		final KeyDescription[] testKeys = new KeyDescription[] {
			new KeyDescription(UsScan.Esc,    SWT.ESC,         SWT.ESC, SWT.ESC),
			new KeyDescription(UsScan._1,     '1',             '1',     '!'    ),
			new KeyDescription(UsScan._2,     '2',             '2',     '@'    ),
			new KeyDescription(UsScan._3,     '3',             '3',     '#'    ),
			new KeyDescription(UsScan._4,     '4',             '4',     '$'    ),
			new KeyDescription(UsScan._5,     '5',             '5',     '%'    ),
			new KeyDescription(UsScan._6,     '6',             '6',     '^'    ),
			new KeyDescription(UsScan._7,     '7',             '7',     '&'    ),
			new KeyDescription(UsScan._8,     '8',             '8',     '*'    ),
			new KeyDescription(UsScan._9,     '9',             '9',     '('    ),
			new KeyDescription(UsScan._0,     '0',             '0',     ')'    ),
			new KeyDescription(UsScan.Minus,  '-',             '-',     '_'    ),
			new KeyDescription(UsScan.Equals, '=',             '=',     '+'    ),
			new KeyDescription(UsScan.Backsp, SWT.BS,          SWT.BS,  SWT.BS ),
			new KeyDescription(UsScan.Tab,    SWT.TAB,         SWT.TAB, SWT.TAB),
			new KeyDescription(UsScan.Q,      'q',             'q',     'Q'    ),
			new KeyDescription(UsScan.W,      'w',             'w',     'W'    ),
			new KeyDescription(UsScan.E,      'e',             'e',     'E'    ),
			new KeyDescription(UsScan.R,      'r',             'r',     'R'    ),
			new KeyDescription(UsScan.T,      't',             't',     'T'    ),
			new KeyDescription(UsScan.Y,      'y',             'y',     'Y'    ),
			new KeyDescription(UsScan.U,      'u',             'u',     'U'    ),
			new KeyDescription(UsScan.I,      'i',             'i',     'I'    ),
			new KeyDescription(UsScan.O,      'o',             'o',     'O'    ),
			new KeyDescription(UsScan.P,      'p',             'p',     'P'    ),
			new KeyDescription(UsScan.LBrack, '[',             '[',     '{'    ),
			new KeyDescription(UsScan.RBrack, ']',             ']',     '}'    ),
			new KeyDescription(UsScan.Enter,  SWT.CR,          SWT.CR,  SWT.CR ),
			new KeyDescription(UsScan.A,      'a',             'a',     'A'    ),
			new KeyDescription(UsScan.S,      's',             's',     'S'    ),
			new KeyDescription(UsScan.D,      'd',             'd',     'D'    ),
			new KeyDescription(UsScan.F,      'f',             'f',     'F'    ),
			new KeyDescription(UsScan.G,      'g',             'g',     'G'    ),
			new KeyDescription(UsScan.H,      'h',             'h',     'H'    ),
			new KeyDescription(UsScan.J,      'j',             'j',     'J'    ),
			new KeyDescription(UsScan.K,      'k',             'k',     'K'    ),
			new KeyDescription(UsScan.L,      'l',             'l',     'L'    ),
			new KeyDescription(UsScan.Colon,  ';',             ';',     ':'    ),
			new KeyDescription(UsScan.Quote,  '\'',            '\'',    '"'    ),
			new KeyDescription(UsScan.Tilde,  '`',             '`',     '~'    ),
			new KeyDescription(UsScan.BSlash, '\\',            '\\',    '|'    ),
			new KeyDescription(UsScan.Z,      'z',             'z',     'Z'    ),
			new KeyDescription(UsScan.X,      'x',             'x',     'X'    ),
			new KeyDescription(UsScan.C,      'c',             'c',     'C'    ),
			new KeyDescription(UsScan.V,      'v',             'v',     'V'    ),
			new KeyDescription(UsScan.B,      'b',             'b',     'B'    ),
			new KeyDescription(UsScan.N,      'n',             'n',     'N'    ),
			new KeyDescription(UsScan.M,      'm',             'm',     'M'    ),
			new KeyDescription(UsScan.Comma,  ',',             ',',     '<'    ),
			new KeyDescription(UsScan.Dot,    '.',             '.',     '>'    ),
			new KeyDescription(UsScan.FSlash, '/',             '/',     '?'    ),
			new KeyDescription(UsScan.Space,  ' ',             ' ',     ' '    ),
			new KeyDescription(UsScan.F1,     SWT.F1,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F2,     SWT.F2,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F3,     SWT.F3,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F4,     SWT.F4,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F5,     SWT.F5,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F6,     SWT.F6,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F7,     SWT.F7,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F8,     SWT.F8,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F9,     SWT.F9,          '\0',    '\0'   ),
			new KeyDescription(UsScan.F10,    SWT.F10,         '\0',    '\0'   ),
			new KeyDescription(UsScan.PrtScr, SWT.PRINT_SCREEN,'\0',    '\0'   ),
			new KeyDescription(UsScan.Oem102, '\\',            '\\',    '|'    ),
			new KeyDescription(UsScan.F11,    SWT.F11,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F12,    SWT.F12,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F13,    SWT.F13,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F14,    SWT.F14,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F15,    SWT.F15,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F16,    SWT.F16,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F17,    SWT.F17,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F18,    SWT.F18,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F19,    SWT.F19,         '\0',    '\0'   ),
			new KeyDescription(UsScan.F20,    SWT.F20,         '\0',    '\0'   ),
			new KeyDescription(UsScan.Break,  SWT.BREAK,       '\0',    '\0'   ),
			new KeyDescription(UsScan.Home,   SWT.HOME,        '\0',    '\0'   ),
			new KeyDescription(UsScan.ArrowU, SWT.ARROW_UP,    '\0',    '\0'   ),
			new KeyDescription(UsScan.PgUp,   SWT.PAGE_UP,     '\0',    '\0'   ),
			new KeyDescription(UsScan.ArrowL, SWT.ARROW_LEFT,  '\0',    '\0'   ),
			new KeyDescription(UsScan.ArrowR, SWT.ARROW_RIGHT, '\0',    '\0'   ),
			new KeyDescription(UsScan.End,    SWT.END,         '\0',    '\0'   ),
			new KeyDescription(UsScan.ArrowD, SWT.ARROW_DOWN,  '\0',    '\0'   ),
			new KeyDescription(UsScan.PgDn,   SWT.PAGE_DOWN,   '\0',    '\0'   ),
			new KeyDescription(UsScan.Ins,    SWT.INSERT,      '\0',    '\0'   ),
			new KeyDescription(UsScan.Del,    SWT.DEL,         SWT.DEL, SWT.DEL),
		};

		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			for (int state : COMMON_STATES) {
				for (KeyDescription testKey : testKeys) {
					if (isSystemHotkey(state, testKey.scanCode)) {
						// Avoid emulating hotkeys that interfere with testing
						continue;
					}

					char expectedChar = '\0';
					switch (state) {
						case ____:
						case A___:
							expectedChar = testKey.charNormal;
							break;
						case ___S:
						case A__S:
							expectedChar = testKey.charShift;
							break;
						case __C_:
						case A_C_:
							expectedChar = ctrlChar(testKey.charNormal);
							break;
						case __CS:
						case A_CS:
							expectedChar = ctrlChar(testKey.charShift);
							break;
					}

					if (testKey.scanCode == UsScan.PrtScr) {
						// For some reason, PrintScreen only produces SWT.KeyUp event.
						// Maybe because Windows handles WM_KEYDOWN to copy screen to clipboard?
						String testName = getKeyName(state, testKey.scanCode);
						expectKeyEvents(
							testName,
							() -> emulateScanCode(state, testKey.scanCode),
							expectKeyUp(state, expectedChar, 0, testKey.keyCode)
						);
						continue;
					}

					if ((testKey.scanCode == UsScan.Break) && ((state & SWT.CTRL) == 0)) {
						// Break can only be pressed together with Ctrl. Without it, it's Pause.
						// Pause key has an exotic 8-byte scancode. I didn't find a way to
						// emulate it by scan code. Let's emulate it by virtual key, then.
						testVirtualKey(state, OS.VK_PAUSE, state, '\0', 0, SWT.PAUSE);
						continue;
					}

					testScanCode(state, testKey.scanCode, state, expectedChar, 0, testKey.keyCode);
				}
			}
		});
	}

	/**
	 * Extensive test for English US layout
	 */
	@Test
	public void testEnglishUs_LEDs() {
		final int[] testStates = {
			____,
			___S,
			__C_,
			A___,
			__CS,
			A__S,
			A_C_,
			A_CS,
		};

		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			for (int state : testStates) {
				testScanCode(state, UsScan.Caps, state, '\0', 0, SWT.CAPS_LOCK);

				if (state != __C_) {
					testScanCode(state, UsScan.Scroll, state, '\0', 0, SWT.SCROLL_LOCK);
				} else {
					// On hardware level, Ctrl+ScrollLock is the same as Ctrl+Break
					// See "Why does Ctrl+ScrollLock cancel dialogs?"
					//   https://devblogs.microsoft.com/oldnewthing/20080211-00/?p=23503
					testScanCode(__C_, UsScan.Scroll, __C_, '\0', 0, SWT.BREAK);
				}

				// I didn't find a way to emulate NumLock by scan code.
				// Let's emulate it by virtual key, then.
				testVirtualKey(state, OS.VK_NUMLOCK, state, '\0', SWT.KEYPAD, SWT.NUM_LOCK);
			}
		});
	}

	/**
	 * Test numpad keys for English US layout
	 */
	@Test
	public void testEnglishUs_numpad() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			// Test with both NumLock states
			for (int iToggle = 0; iToggle < 2; iToggle++) {
				toggleLed(OS.VK_NUMLOCK);

				for (int state : COMMON_STATES) {
					// These keys don't depend on NumLock state
					{
						testScanCode(state, UsScan.NumMul, state, '*',    SWT.KEYPAD, SWT.KEYPAD_MULTIPLY);
						testScanCode(state, UsScan.NumMns, state, '-',    SWT.KEYPAD, SWT.KEYPAD_SUBTRACT);
						testScanCode(state, UsScan.NumPls, state, '+',    SWT.KEYPAD, SWT.KEYPAD_ADD);
						testScanCode(state, UsScan.NumEnt, state, SWT.CR, SWT.KEYPAD, SWT.KEYPAD_CR);
						testScanCode(state, UsScan.NumDiv, state, '/',    SWT.KEYPAD, SWT.KEYPAD_DIVIDE);
					}

					// Feature in Windows: when NumLock is enabled and Shift is pressed,
					// Shift is consumed to temporarily disable NumLock effect
					final boolean isNumLock = isLedEnabled(OS.VK_NUMLOCK);
					final boolean isShift   = ((state & SWT.SHIFT) != 0);
					final boolean isDigits  = isNumLock && !isShift;
					final int expectState   = isNumLock ? (state & ~SWT.SHIFT) : state;

					// Alt+NumPad: Enters unicode characters
					// Alt+Shift+NumPad: Keyboard layout switching
					if ((state == A___) || (state == A__S)) continue;

					if (isDigits) {
						testScanCode(state, UsScan.Num7,   expectState, '7',  SWT.KEYPAD, SWT.KEYPAD_7);
						testScanCode(state, UsScan.Num8,   expectState, '8',  SWT.KEYPAD, SWT.KEYPAD_8);
						testScanCode(state, UsScan.Num9,   expectState, '9',  SWT.KEYPAD, SWT.KEYPAD_9);
						testScanCode(state, UsScan.Num4,   expectState, '4',  SWT.KEYPAD, SWT.KEYPAD_4);
						testScanCode(state, UsScan.Num5,   expectState, '5',  SWT.KEYPAD, SWT.KEYPAD_5);
						testScanCode(state, UsScan.Num6,   expectState, '6',  SWT.KEYPAD, SWT.KEYPAD_6);
						testScanCode(state, UsScan.Num1,   expectState, '1',  SWT.KEYPAD, SWT.KEYPAD_1);
						testScanCode(state, UsScan.Num2,   expectState, '2',  SWT.KEYPAD, SWT.KEYPAD_2);
						testScanCode(state, UsScan.Num3,   expectState, '3',  SWT.KEYPAD, SWT.KEYPAD_3);
						testScanCode(state, UsScan.Num0,   expectState, '0',  SWT.KEYPAD, SWT.KEYPAD_0);
						testScanCode(state, UsScan.NumDel, expectState, '.',  SWT.KEYPAD, SWT.KEYPAD_DECIMAL);
					} else {
						testScanCode(state, UsScan.Num7,   expectState, '\0', SWT.KEYPAD, SWT.HOME);
						testScanCode(state, UsScan.Num8,   expectState, '\0', SWT.KEYPAD, SWT.ARROW_UP);
						testScanCode(state, UsScan.Num9,   expectState, '\0', SWT.KEYPAD, SWT.PAGE_UP);
						testScanCode(state, UsScan.Num4,   expectState, '\0', SWT.KEYPAD, SWT.ARROW_LEFT);

						// Num5 with NumLock disabled produces a special virtual key VK_CLEAR.
						// This VK is only produced by this key. SWT generates no event for it
						// on Windows and Linux. On macOS, NumLock state is ignored and always
						// works as if it's enabled.

						testScanCode(state, UsScan.Num6,   expectState, '\0', SWT.KEYPAD, SWT.ARROW_RIGHT);
						testScanCode(state, UsScan.Num1,   expectState, '\0', SWT.KEYPAD, SWT.END);
						testScanCode(state, UsScan.Num2,   expectState, '\0', SWT.KEYPAD, SWT.ARROW_DOWN);
						testScanCode(state, UsScan.Num3,   expectState, '\0', SWT.KEYPAD, SWT.PAGE_DOWN);
						testScanCode(state, UsScan.Num0,   expectState, '\0', SWT.KEYPAD, SWT.INSERT);
						testScanCode(state, UsScan.NumDel, expectState, SWT.DEL, SWT.KEYPAD, SWT.DEL);
					}
				}
			}
		});
	}

	/**
	 * Test Alt+NumPad+Digits unicode input
	 */
	@Test
	public void testEnglishUs_altNumpadDigitsUnicode() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			// Test with both NumLock states
			// Input works in both cases, but intermediate SWT events are different
			for (int iToggle = 0; iToggle < 2; iToggle++) {
				toggleLed(OS.VK_NUMLOCK);

				Event[] expectedEvents;
				if (isLedEnabled(OS.VK_NUMLOCK)) {
					expectedEvents = new Event[] {
						expectKeyDown(A___, '0',  SWT.KEYPAD, SWT.KEYPAD_0),
						expectKeyUp  (A___, '0',  SWT.KEYPAD, SWT.KEYPAD_0),
						expectKeyDown(A___, '1',  SWT.KEYPAD, SWT.KEYPAD_1),
						expectKeyUp  (A___, '1',  SWT.KEYPAD, SWT.KEYPAD_1),
						expectKeyDown(A___, '6',  SWT.KEYPAD, SWT.KEYPAD_6),
						expectKeyUp  (A___, '6',  SWT.KEYPAD, SWT.KEYPAD_6),
						expectKeyDown(A___, '9',  SWT.KEYPAD, SWT.KEYPAD_9),
						expectKeyUp  (A___, '9',  SWT.KEYPAD, SWT.KEYPAD_9),
						// Alt+0169 = copyright character
						expectKeyDown(____, '©',  0, 0)
					};
				} else {
					expectedEvents = new Event[] {
						expectKeyDown(A___, '\0',  SWT.KEYPAD, SWT.INSERT),
						expectKeyUp  (A___, '\0',  SWT.KEYPAD, SWT.INSERT),
						expectKeyDown(A___, '\0',  SWT.KEYPAD, SWT.END),
						expectKeyUp  (A___, '\0',  SWT.KEYPAD, SWT.END),
						expectKeyDown(A___, '\0',  SWT.KEYPAD, SWT.ARROW_RIGHT),
						expectKeyUp  (A___, '\0',  SWT.KEYPAD, SWT.ARROW_RIGHT),
						expectKeyDown(A___, '\0',  SWT.KEYPAD, SWT.PAGE_UP),
						expectKeyUp  (A___, '\0',  SWT.KEYPAD, SWT.PAGE_UP),
						// Alt+0169 = copyright character
						expectKeyDown(____, '©',  0, 0)
					};
				}

				expectKeyEvents(
					"Alt+Num0169",
					() -> {
						emulateState(A___, false);
						emulateScanCode(UsScan.Num0);
						emulateScanCode(UsScan.Num1);
						emulateScanCode(UsScan.Num6);
						emulateScanCode(UsScan.Num9);
						emulateState(A___, true);
					},
					expectedEvents
				);
			}
		});
	}

	/**
	 * Bug 42225: For better cross-platform consistency, SWT changes Windows
	 * Ctrl+Backspace and Ctrl+Enter 'Event.character' to match Linux.
	 */
	@Test
	public void testEnglishUs_CtrlEnter_CtrlBack() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			testScanCode(__C_, UsScan.Backsp, __C_, SWT.BS, 0, SWT.BS);
			testScanCode(__C_, UsScan.Enter , __C_, SWT.CR, 0, SWT.CR);
		});
	}

	/**
	 * Windows sends character = 0x03 for Ctrl+Break. This matches
	 * character for Ctrl+C and is undesired in SWT. The specific
	 * reason why this is seen as bad wasn't documented when this
	 * workaround was added.
	 */
	@Test
	public void testEnglishUs_CtrlBreak() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			testScanCode(__C_, UsScan.Break, __C_, '\0', 0, SWT.BREAK);
		});
	}

	/**
	 * 'English United States-Dvorak' layout has latin keys shuffled around.
	 * Still, keyboard shortcuts shall match Dvorak keyboard labels.
	 * See {@link org.eclipse.swt.widgets.Event#keyCode} for a detailed explanation.
	 */
	@Test
	public void testEnglishDvorak_trivialCases() {
		runWithLayout(LAYOUT_ENGLISH_DVORAK, () -> {
			testScanCode(____, UsScan.Q,  ____, '\'', 0, '\'');
			testScanCode(____, UsScan.W,  ____, ',', 0, ',');
			testScanCode(____, UsScan.E,  ____, '.', 0, '.');
			testScanCode(____, UsScan.R,  ____, 'p', 0, 'p');
			testScanCode(____, UsScan.T,  ____, 'y', 0, 'y');
			testScanCode(____, UsScan.Y,  ____, 'f', 0, 'f');

			testScanCode(____, UsScan._0, ____, '0', 0, '0');
			testScanCode(____, UsScan._1, ____, '1', 0, '1');
			testScanCode(____, UsScan._2, ____, '2', 0, '2');
			testScanCode(____, UsScan._3, ____, '3', 0, '3');
		});
	}

	/**
	 * 'Bulgarian' layout only has cyrillic keys and no latin keys.
	 * Still, these non-latin keys have latin virtual keys assigned to them.
	 * This makes it easy to answer the question "which keyboard shortcut".
	 * See {@link org.eclipse.swt.widgets.Event#keyCode} for a detailed explanation.
	 */
	@Test
	public void testBulgarian_trivialCases() {
		runWithLayout(LAYOUT_BULGARIAN, () -> {
			testScanCode(____, UsScan.Q,  ____, ',', 0, ',');
			testScanCode(____, UsScan.W,  ____, 'у', 0, 'w');
			testScanCode(____, UsScan.E,  ____, 'е', 0, 'e');
			testScanCode(____, UsScan.R,  ____, 'и', 0, 'r');
			testScanCode(____, UsScan.T,  ____, 'ш', 0, 't');
			testScanCode(____, UsScan.Y,  ____, 'щ', 0, 'y');

			testScanCode(____, UsScan._0, ____, '0', 0, '0');
			testScanCode(____, UsScan._1, ____, '1', 0, '1');
			testScanCode(____, UsScan._2, ____, '2', 0, '2');
			testScanCode(____, UsScan._3, ____, '3', 0, '3');
		});
	}

	/**
	 * Same explanation as for {@link #testBulgarian_trivialCases}
	 */
	@Test
	public void testHebrew_trivialCases() {
		runWithLayout(LAYOUT_HEBREW, () -> {
			testScanCode(____, UsScan.Q,  ____, '/', 0, 'q');
			testScanCode(____, UsScan.W,  ____, '\'', 0, 'w');
			testScanCode(____, UsScan.E,  ____, 'ק', 0, 'e');
			testScanCode(____, UsScan.R,  ____, 'ר', 0, 'r');
			testScanCode(____, UsScan.T,  ____, 'א', 0, 't');
			testScanCode(____, UsScan.Y,  ____, 'ט', 0, 'y');

			testScanCode(____, UsScan._0, ____, '0', 0, '0');
			testScanCode(____, UsScan._1, ____, '1', 0, '1');
			testScanCode(____, UsScan._2, ____, '2', 0, '2');
			testScanCode(____, UsScan._3, ____, '3', 0, '3');
		});
	}

	/**
	 * Issue #351: In French, digit keys print digits when Shift is
	 * pressed, and print other characters without Shift. Numpad
	 * however prints digits (when NumLock is on).
	 *
	 * However, many apps bind keyboard shortcuts to latin digits. For
	 * that reason, {@link Event#keyCode} shall report matching latin
	 * digits. See also Bug 285656, which fixes similar problems.
	 *
	 * See {@link org.eclipse.swt.widgets.Event#keyCode} for a detailed explanation.
	 */
	@Test
	public void testFrench_digits() {
		runWithLayout(LAYOUT_FRENCH, () -> {
			for (int state : NORMAL_STATES) {
				final int stateN = state;
				testScanCode(stateN, UsScan._0, stateN, 'à', 0, '0');
				testScanCode(stateN, UsScan._1, stateN, '&', 0, '1');
				testScanCode(stateN, UsScan._2, stateN, 'é', 0, '2');
				testScanCode(stateN, UsScan._3, stateN, '"', 0, '3');
				testScanCode(stateN, UsScan._4, stateN, '\'', 0, '4');
				testScanCode(stateN, UsScan._5, stateN, '(', 0, '5');
				testScanCode(stateN, UsScan._6, stateN, '-', 0, '6');
				testScanCode(stateN, UsScan._7, stateN, 'è', 0, '7');
				testScanCode(stateN, UsScan._8, stateN, '_', 0, '8');
				testScanCode(stateN, UsScan._9, stateN, 'ç', 0, '9');

				final int stateS = state | SWT.SHIFT;
				testScanCode(stateS, UsScan._0, stateS, '0', 0, '0');
				testScanCode(stateS, UsScan._1, stateS, '1', 0, '1');
				testScanCode(stateS, UsScan._2, stateS, '2', 0, '2');
				testScanCode(stateS, UsScan._3, stateS, '3', 0, '3');
				testScanCode(stateS, UsScan._4, stateS, '4', 0, '4');
				testScanCode(stateS, UsScan._5, stateS, '5', 0, '5');
				testScanCode(stateS, UsScan._6, stateS, '6', 0, '6');
				testScanCode(stateS, UsScan._7, stateS, '7', 0, '7');
				testScanCode(stateS, UsScan._8, stateS, '8', 0, '8');
				testScanCode(stateS, UsScan._9, stateS, '9', 0, '9');
			}
		});
	}

	/**
	 * Issue #351: In Lithuanian, digit keys produce latin digits with
	 * AltGr, and accented letters otherwise.
	 *
	 * However, many apps bind keyboard shortcuts to latin digits. For
	 * that reason, {@link Event#keyCode} shall report matching latin
	 * digits. See also Bug 285656, which fixes similar problems.
	 *
	 * Bug 536041: Another problem is that with AltGr pressed, SWT got
	 * confused and produced two different KeyDown events (first one as
	 * if AltGr is not pressed, and the second one for AltGr).
	 */
	@Test
	public void testLithuanian_digits() {
		runWithLayout(LAYOUT_LITHUANIAN, () -> {
			for (int state : NORMAL_STATES) {
				// Test regular
				final int stateN = state;
				testScanCode(stateN, UsScan._1, stateN, 'ą', 0, '1');
				testScanCode(stateN, UsScan._2, stateN, 'č', 0, '2');
				testScanCode(stateN, UsScan._3, stateN, 'ę', 0, '3');
				testScanCode(stateN, UsScan._4, stateN, 'ė', 0, '4');
				testScanCode(stateN, UsScan._5, stateN, 'į', 0, '5');
				testScanCode(stateN, UsScan._6, stateN, 'š', 0, '6');
				testScanCode(stateN, UsScan._7, stateN, 'ų', 0, '7');
				testScanCode(stateN, UsScan._8, stateN, 'ū', 0, '8');
				testScanCode(stateN, UsScan._9, stateN, '9', 0, '9');
				testScanCode(stateN, UsScan._0, stateN, '0', 0, '0');

				// Test Shift (produces capital letters)
				final int stateS = state | SWT.SHIFT;
				testScanCode(stateS, UsScan._1, stateS, 'Ą', 0, '1');
				testScanCode(stateS, UsScan._2, stateS, 'Č', 0, '2');
				testScanCode(stateS, UsScan._3, stateS, 'Ę', 0, '3');
				testScanCode(stateS, UsScan._4, stateS, 'Ė', 0, '4');
				testScanCode(stateS, UsScan._5, stateS, 'Į', 0, '5');
				testScanCode(stateS, UsScan._6, stateS, 'Š', 0, '6');
				testScanCode(stateS, UsScan._7, stateS, 'Ų', 0, '7');
				testScanCode(stateS, UsScan._8, stateS, 'Ū', 0, '8');
				testScanCode(stateS, UsScan._9, stateS, '(', 0, '9');
				testScanCode(stateS, UsScan._0, stateS, ')', 0, '0');
			}

			// Test AltGr (note that SWT reports it as Alt+Ctrl)
			{
				testScanCode(AG__, UsScan._1, A_C_, '1', 0, '1');
				testScanCode(AG__, UsScan._2, A_C_, '2', 0, '2');
				testScanCode(AG__, UsScan._3, A_C_, '3', 0, '3');
				testScanCode(AG__, UsScan._4, A_C_, '4', 0, '4');
				testScanCode(AG__, UsScan._5, A_C_, '5', 0, '5');
				testScanCode(AG__, UsScan._6, A_C_, '6', 0, '6');
				testScanCode(AG__, UsScan._7, A_C_, '7', 0, '7');
				testScanCode(AG__, UsScan._8, A_C_, '8', 0, '8');
				testScanCode(AG__, UsScan._9, A_C_, '9', 0, '9');
				testScanCode(AG__, UsScan._0, A_C_, '0', 0, '0');
			}
		});
	}

	/**
	 * Bug 285656: Bengali uses non-latin digits (not 0123456789).
	 *
	 * However, many apps bind keyboard shortcuts to latin digits. For
	 * that reason, {@link Event#keyCode} shall report matching latin
	 * digits.
	 *
	 * See {@link org.eclipse.swt.widgets.Event#keyCode} for a detailed explanation.
	 */
	@Test
	public void testBengali_digits() {
		runWithLayout(LAYOUT_BENGALI, () -> {
			for (int state : NORMAL_STATES) {
				testScanCode(state, UsScan._0, state, '০', 0, '0');
				testScanCode(state, UsScan._1, state, '১', 0, '1');
				testScanCode(state, UsScan._2, state, '২', 0, '2');
				testScanCode(state, UsScan._3, state, '৩', 0, '3');
				testScanCode(state, UsScan._4, state, '৪', 0, '4');
				testScanCode(state, UsScan._5, state, '৫', 0, '5');
				testScanCode(state, UsScan._6, state, '৬', 0, '6');
				testScanCode(state, UsScan._7, state, '৭', 0, '7');
				testScanCode(state, UsScan._8, state, '৮', 0, '8');
				testScanCode(state, UsScan._9, state, '৯', 0, '9');
			}
		});
	}

	/**
	 * Bug 285656: Marathi uses non-latin digits from Devanagari set.
	 *
	 * See explanation for {@link #testBengali_digits}
	 */
	@Test
	public void testMarathi_digits() {
		runWithLayout(LAYOUT_MARATHI, () -> {
			for (int state : NORMAL_STATES) {
				testScanCode(state, UsScan._0, state, '०', 0, '0');
				testScanCode(state, UsScan._1, state, '१', 0, '1');
				testScanCode(state, UsScan._2, state, '२', 0, '2');
				testScanCode(state, UsScan._3, state, '३', 0, '3');
				testScanCode(state, UsScan._4, state, '४', 0, '4');
				testScanCode(state, UsScan._5, state, '५', 0, '5');
				testScanCode(state, UsScan._6, state, '६', 0, '6');
				testScanCode(state, UsScan._7, state, '७', 0, '7');
				testScanCode(state, UsScan._8, state, '८', 0, '8');
				testScanCode(state, UsScan._9, state, '९', 0, '9');
			}
		});
	}

	/**
	 * Issue #351: Nepali uses non-latin digits.
	 * It also prints Devanagari digits with Shift pressed.
	 * It also prints Latin digits with AltGr (or Ctrl+Alt) pressed.
	 *
	 * Same explanation as for {@link #testBengali_digits}
	 * See also Bug 285656, which fixes similar problems.
	 */
	@Test
	public void testNepali_digits() {
		runWithLayout(LAYOUT_NEPALI, () -> {
			for (int state : NORMAL_STATES) {
				testScanCode(____, UsScan._0, ____, 'ण', 0, '0');

				// Digit 1 is complicated beast, consisting of 3 characters glued together
				expectKeyEvents(
					formatStateMask(state) + "1",
					() -> emulateScanCode(state, UsScan._1),
					expectKeyDown(state, 'ज', 0, '1'),
					expectKeyDown(state, '्', 0, '1'),
					expectKeyDown(state, 'ञ', 0, '1'),
					expectKeyUp  (state, 'ञ', 0, '1')
				);

				testScanCode(state, UsScan._2, state, 'घ', 0, '2');
				testScanCode(state, UsScan._3, state, 'ङ', 0, '3');
				testScanCode(state, UsScan._4, state, 'झ', 0, '4');
				testScanCode(state, UsScan._5, state, 'छ', 0, '5');
				testScanCode(state, UsScan._6, state, 'ट', 0, '6');
				testScanCode(state, UsScan._7, state, 'ठ', 0, '7');
				testScanCode(state, UsScan._8, state, 'ड', 0, '8');
				testScanCode(state, UsScan._9, state, 'ढ', 0, '9');
			}
		});
	}

	/**
	 * Test dead keys (instead of typing a character, a dead key
	 * modifies next key pressed). German layout is a good example.
	 */
	@Test
	public void testGerman_deadKey_basic() {
		runWithLayout(LAYOUT_GERMAN, () -> {
			testDeadKey(____, UsScan.Tilde,  ____, UsScan.E, ____, 'ê', 'e');
			testDeadKey(____, UsScan.Tilde,  ___S, UsScan.E, ___S, 'Ê', 'e');
			testDeadKey(____, UsScan.Tilde,  ____, UsScan.U, ____, 'û', 'u');
			testDeadKey(____, UsScan.Tilde,  ___S, UsScan.U, ___S, 'Û', 'u');
			testDeadKey(____, UsScan.Tilde,  ____, UsScan.I, ____, 'î', 'i');
			testDeadKey(____, UsScan.Tilde,  ___S, UsScan.I, ___S, 'Î', 'i');
			testDeadKey(____, UsScan.Tilde,  ____, UsScan.O, ____, 'ô', 'o');
			testDeadKey(____, UsScan.Tilde,  ___S, UsScan.O, ___S, 'Ô', 'o');
			testDeadKey(____, UsScan.Tilde,  ____, UsScan.A, ____, 'â', 'a');
			testDeadKey(____, UsScan.Tilde,  ___S, UsScan.A, ___S, 'Â', 'a');

			testDeadKey(____, UsScan.Equals, ____, UsScan.E, ____, 'é', 'e');
			testDeadKey(____, UsScan.Equals, ___S, UsScan.E, ___S, 'É', 'e');
			testDeadKey(____, UsScan.Equals, ____, UsScan.U, ____, 'ú', 'u');
			testDeadKey(____, UsScan.Equals, ___S, UsScan.U, ___S, 'Ú', 'u');
			testDeadKey(____, UsScan.Equals, ____, UsScan.I, ____, 'í', 'i');
			testDeadKey(____, UsScan.Equals, ___S, UsScan.I, ___S, 'Í', 'i');
			testDeadKey(____, UsScan.Equals, ____, UsScan.O, ____, 'ó', 'o');
			testDeadKey(____, UsScan.Equals, ___S, UsScan.O, ___S, 'Ó', 'o');
			testDeadKey(____, UsScan.Equals, ____, UsScan.A, ____, 'á', 'a');
			testDeadKey(____, UsScan.Equals, ___S, UsScan.A, ___S, 'Á', 'a');

			testDeadKey(___S, UsScan.Equals, ____, UsScan.E, ____, 'è', 'e');
			testDeadKey(___S, UsScan.Equals, ___S, UsScan.E, ___S, 'È', 'e');
			testDeadKey(___S, UsScan.Equals, ____, UsScan.U, ____, 'ù', 'u');
			testDeadKey(___S, UsScan.Equals, ___S, UsScan.U, ___S, 'Ù', 'u');
			testDeadKey(___S, UsScan.Equals, ____, UsScan.I, ____, 'ì', 'i');
			testDeadKey(___S, UsScan.Equals, ___S, UsScan.I, ___S, 'Ì', 'i');
			testDeadKey(___S, UsScan.Equals, ____, UsScan.O, ____, 'ò', 'o');
			testDeadKey(___S, UsScan.Equals, ___S, UsScan.O, ___S, 'Ò', 'o');
			testDeadKey(___S, UsScan.Equals, ____, UsScan.A, ____, 'à', 'a');
			testDeadKey(___S, UsScan.Equals, ___S, UsScan.A, ___S, 'À', 'a');
		});
	}

	/**
	 * When dead key doesn't match next key, the latter will produce TWO
	 * characters at once. That's how Windows is designed.
	 * Note that SWT sees that as TWO KeyDown events followed by ONE KeyUp.
	 * @see #testGerman_deadKey_basic
	 */
	@Test
	public void testGerman_deadKey_unmatched() {
		// No mods and pure alt are handled similarly in Windows
		// The only difference is WM_DEADCHAR/WM_CHAR vs WM_SYSCHAR/WM_SYSDEADCHAR,
		// but SWT produces the same events for both
		final int[] testStates = {____, A___};

		runWithLayout(LAYOUT_GERMAN, () -> {
			for (int state : testStates) {
				expectKeyEvents(
					formatStateMask(state) + "~W",
					() -> emulateScanCode(state, UsScan.Tilde, UsScan.W),
					// Note that in first KeyDown, character is '^' while keycode is 'w'
					// That looks a bit weird, but is kind of correct, because Windows "eats"
					// the first '^' press and then generates both '^' and 'w' when 'w' is pressed.
					expectKeyDown(state, '^', 0, 'w'),
					expectKeyDown(state, 'w', 0, 'w'),
					expectKeyUp  (state, 'w', 0, 'w')
				);

				expectKeyEvents(
					formatStateMask(state) + "~~",
					() -> emulateScanCode(state, UsScan.Tilde, UsScan.Tilde),
					expectKeyDown(state, '^', 0, '^'),
					expectKeyDown(state, '^', 0, '^'),
					expectKeyUp  (state, '^', 0, '^')
				);
			}
		});
	}

	/**
	 * Dead key shall no longer count as dead when any modifier is pressed.
	 * @see #testGerman_deadKey_basic
	 */
	@Test
	public void testGerman_deadKey_withMods() {
		runWithLayout(LAYOUT_GERMAN, () -> {
			final char char_shift = 0xB0;  // Character '°' is produced by Shift+^ in German
			final char char_cntrl = 0x1E;  // There's a general rule where Ctrl reduces char code by 0x40

			testScanCode(___S, UsScan.Tilde,  ___S, char_shift, 0, '^');
			testScanCode(__C_, UsScan.Tilde,  __C_, char_cntrl, 0, '^');

			// Pure alt works similar to no mods in Windows.
			// This case is handled in @see #testGerman_deadKey_unmatched
			//
			// testScanCode(A___, UsScan.Tilde,  A___, char_norml, 0, '^');

			testScanCode(__CS, UsScan.Tilde,  __CS, char_shift, 0, '^');
			testScanCode(A__S, UsScan.Tilde,  A__S, char_shift, 0, '^');
			testScanCode(A_C_, UsScan.Tilde,  A_C_, char_cntrl, 0, '^');
		});
	}

	/**
	 * Test altGr combinations, German is one of layouts that have them.
	 */
	@Test
	public void testGerman_altGr() {
		// Note that on Windows, Alt+Ctrl is the same as AltGr.

		// Note that SWT on Windows reports AltGr as Alt+Ctrl. I'm not
		// sure if it's worth fixing; could cause all sorts of problems
		// in apps that test for old behavior.

		runWithLayout(LAYOUT_GERMAN, () -> {
			// Test Alt+Ctrl
			testScanCode(A_C_, UsScan._2,     A_C_, '²',  0, '2');
			testScanCode(A_C_, UsScan._3,     A_C_, '³',  0, '3');
			testScanCode(A_C_, UsScan._7,     A_C_, '{',  0, '7');
			testScanCode(A_C_, UsScan._8,     A_C_, '[',  0, '8');
			testScanCode(A_C_, UsScan._9,     A_C_, ']',  0, '9');
			testScanCode(A_C_, UsScan._0,     A_C_, '}',  0, '0');
			// TODO Does it make sense that SWT returns 'ß' in keycode?
			testScanCode(A_C_, UsScan.Minus,  A_C_, '\\', 0, 'ß');
			testScanCode(A_CS, UsScan.Minus,  A_CS, 'ẞ',  0, 'ß');
			testScanCode(A_C_, UsScan.Q,      A_C_, '@',  0, 'q');
			testScanCode(A_C_, UsScan.E,      A_C_, '€',  0, 'e');
			testScanCode(A_C_, UsScan.RBrack, A_C_, '~',  0, '+');
			testScanCode(A_C_, UsScan.Oem102, A_C_, '|',  0, '<');
			testScanCode(A_C_, UsScan.M,      A_C_, 'µ',  0, 'm');

			// Test AltGr
			testScanCode(AG__, UsScan._2,     A_C_, '²',  0, '2');
			testScanCode(AG__, UsScan._3,     A_C_, '³',  0, '3');
			testScanCode(AG__, UsScan._7,     A_C_, '{',  0, '7');
			testScanCode(AG__, UsScan._8,     A_C_, '[',  0, '8');
			testScanCode(AG__, UsScan._9,     A_C_, ']',  0, '9');
			testScanCode(AG__, UsScan._0,     A_C_, '}',  0, '0');
			testScanCode(AG__, UsScan.Minus,  A_C_, '\\', 0, 'ß');
			testScanCode(AG_S, UsScan.Minus,  A_CS, 'ẞ',  0, 'ß');
			testScanCode(AG__, UsScan.Q,      A_C_, '@',  0, 'q');
			testScanCode(AG__, UsScan.E,      A_C_, '€',  0, 'e');
			testScanCode(AG__, UsScan.RBrack, A_C_, '~',  0, '+');
			testScanCode(AG__, UsScan.Oem102, A_C_, '|',  0, '<');
			testScanCode(AG__, UsScan.M,      A_C_, 'µ',  0, 'm');
		});
	}

	/**
	 * Bug 46688: In English-International, Shift+6 is a dead key, while 6
	 * is not. Ensure that such keys are also handled correctly.
	 */
	@Test
	public void testEnglishInternational_deadKey_Shift6() {
		runWithLayout(LAYOUT_ENGLISH_INTL, () -> {
			testDeadKey(___S, UsScan._6, ____, UsScan.E, ____, 'ê', 'e');
			testDeadKey(___S, UsScan._6, ___S, UsScan.E, ___S, 'Ê', 'e');
			testDeadKey(___S, UsScan._6, ____, UsScan.U, ____, 'û', 'u');
			testDeadKey(___S, UsScan._6, ___S, UsScan.U, ___S, 'Û', 'u');
			testDeadKey(___S, UsScan._6, ____, UsScan.I, ____, 'î', 'i');
			testDeadKey(___S, UsScan._6, ___S, UsScan.I, ___S, 'Î', 'i');
			testDeadKey(___S, UsScan._6, ____, UsScan.O, ____, 'ô', 'o');
			testDeadKey(___S, UsScan._6, ___S, UsScan.O, ___S, 'Ô', 'o');
			testDeadKey(___S, UsScan._6, ____, UsScan.A, ____, 'â', 'a');
			testDeadKey(___S, UsScan._6, ___S, UsScan.A, ___S, 'Â', 'a');
		});
	}

	/**
	 * Not yet fixed: SWT reports wrong values in SWT.KeyUp for key
	 * release that isn't paired with key press (due to RegisterHotKey()
	 * or hook or pressing in other app and releasing in SWT).
	 */
	@Ignore("Have been broken for ages, maybe not worth fixing")
	@Test
	public void testEnglishUs_unpairedKeyUp() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			for (int state : COMMON_STATES) {
				// This test needs some state key to be pressed before releasing letter key
				// Otherwise SWT doesn't report any events at all, this is covered in
				// #testEnglishUs_multipleLetters
				if (state == ____) continue;

				// The problem with unpaired keys is that SWT relies upon state variables in
				// Display that are expected to be set up when key is pressed and are cleared
				// when key is released. In a test that only releases keys, tests can
				// interfere with each other through these variables. In order to prevent that,
				// emit a basic keypress.
				testScanCode(____, UsScan.B, ____, 'b', 0, 'b');

				// Now, to the actual test
				expectKeyEvents(
					formatStateMask(state) + "A (up only)",
					() -> {
						emulateState(state, false);
						emulateScanCode(UsScan.A, true);    // Intentionally not paired with key press
						emulateState(state, true);
					},
					// Note that 'Event.character' is missing, because there was no 'WM_CHAR' for it
					expectKeyUp  (state, '\0', 0, 'a')
				);
			}
		});
	}

	/**
	 * Not yet fixed: SWT produces incorrect SWT.KeyUp events for sequence
	 * {Ctrl down, C down, Ctrl up, C up} - that is, Ctrl+C where Ctrl is
	 * released first. Specifically, it didn't produce SWT.KeyUp for 'C' at all,
	 * and also produced SWT.KeyUp for 'Ctrl' with incorrect 'character = 0x03'.
	 */
	@Ignore("Have been broken for ages, maybe not worth fixing")
	@Test
	public void testEnglishUs_unorderedCtrlC() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			expectKeyEvents(
				"{Ctrl down, C down, Ctrl up, C up}",
				() -> {
					emulateState(__C_, false);
					emulateScanCode(UsScan.C, false);
					emulateState(__C_, true);
					emulateScanCode(UsScan.C, true);
				},
				expectKeyDown(__C_, (char)0x03, 0, 'c'),
				expectKeyUp  (____, 'c', 0, 'c')
			);
		});
	}

	/**
	 * Not yet fixed: SWT doesn't report second SWT.KeyUp when two letters
	 * were pressed and released out of order
	 */
	@Ignore("Have been broken for ages, maybe not worth fixing")
	@Test
	public void testEnglishUs_multipleLetters() {
		runWithLayout(LAYOUT_ENGLISH_US, () -> {
			for (int state : NORMAL_STATES) {
				expectKeyEvents(
					formatStateMask(state) + "A+S",
					() -> {
						emulateState(state, false);
						emulateScanCode(UsScan.A, false);
						emulateScanCode(UsScan.S, false);
						emulateScanCode(UsScan.S, true);
						emulateScanCode(UsScan.A, true);
						emulateState(state, true);
					},
					expectKeyDown(state, 'a',  0, 'a'),
					expectKeyDown(state, 's',  0, 's'),
					expectKeyUp  (state, 's',  0, 's'),
					// Note that 'Event.character' is missing, because there was no 'WM_CHAR' for it
					expectKeyUp  (state, '\0', 0, 'a')
				);
			}
		});
	}
}

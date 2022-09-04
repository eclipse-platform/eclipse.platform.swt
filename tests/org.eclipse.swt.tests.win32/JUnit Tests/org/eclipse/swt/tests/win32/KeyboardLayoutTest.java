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
import org.eclipse.swt.internal.win32.INPUT;
import org.eclipse.swt.internal.win32.KEYBDINPUT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Supporting code for testing keyboard layouts
 */
public class KeyboardLayoutTest {
	public boolean dumpEvents = false;  // Can be enabled in debugger

	Display display;
	Shell shell;
	ArrayList<Event> events = new ArrayList<>();

	// Some tests could report many errors at once.
	// For convenience of comparing runs, report all errors.
	boolean collectKeyErrors;
	ArrayList<AssertionError> keyErrors;

	@Rule
	public TestName testName = new TestName();

	// Hardware scan codes, with names according to English US layout
	protected enum UsScan {
		Esc    (0x01),
		_1     (0x02, "1"),
		_2     (0x03, "2"),
		_3     (0x04, "3"),
		_4     (0x05, "4"),
		_5     (0x06, "5"),
		_6     (0x07, "6"),
		_7     (0x08, "7"),
		_8     (0x09, "8"),
		_9     (0x0A, "9"),
		_0     (0x0B, "0"),
		Minus  (0x0C, "-"),
		Equals (0x0D, "="),
		Backsp (0x0E),
		Tab    (0x0F),
		Q      (0x10),
		W      (0x11),
		E      (0x12),
		R      (0x13),
		T      (0x14),
		Y      (0x15),
		U      (0x16),
		I      (0x17),
		O      (0x18),
		P      (0x19),
		LBrack (0x1A, "["),
		RBrack (0x1B, "]"),
		Enter  (0x1C),
		A      (0x1E),
		S      (0x1F),
		D      (0x20),
		F      (0x21),
		G      (0x22),
		H      (0x23),
		J      (0x24),
		K      (0x25),
		L      (0x26),
		Colon  (0x27, "."),
		Quote  (0x28, "\""),
		Tilde  (0x29, "~"),
		BSlash (0x2B, "\\"),
		Z      (0x2C),
		X      (0x2D),
		C      (0x2E),
		V      (0x2F),
		B      (0x30),
		N      (0x31),
		M      (0x32),
		Comma  (0x33, ","),
		Dot    (0x34, "."),
		FSlash (0x35, "/"),
		NumMul (0x37, "Num*"),
		Space  (0x39),
		Caps   (0x3A),
		F1     (0x3B),
		F2     (0x3C),
		F3     (0x3D),
		F4     (0x3E),
		F5     (0x3F),
		F6     (0x40),
		F7     (0x41),
		F8     (0x42),
		F9     (0x43),
		F10    (0x44),
		Pause  (0x45),
		Scroll (0x46),
		Num7   (0x47),
		Num8   (0x48),
		Num9   (0x49),
		NumMns (0x4A, "Num-"),
		Num4   (0x4B),
		Num5   (0x4C),
		Num6   (0x4D),
		NumPls (0x4E, "Num+"),
		Num1   (0x4F),
		Num2   (0x50),
		Num3   (0x51),
		Num0   (0x52),
		NumDel (0x53, "Num."),
		PrtScr (0x54),
		Oem102 (0x56),
		F11    (0x57),
		F12    (0x58),
		F13    (0x64),
		F14    (0x65),
		F15    (0x66),
		F16    (0x67),
		F17    (0x68),
		F18    (0x69),
		F19    (0x6A),
		F20    (0x6B),

		NumEnt (0xE01C),
		NumDiv (0xE035, "Num/"),
		NumLck (0xE045),
		Break  (0xE046),
		Home   (0xE047),
		ArrowU (0xE048),
		PgUp   (0xE049),
		ArrowL (0xE04B),
		ArrowR (0xE04D),
		End    (0xE04F),
		ArrowD (0xE050),
		PgDn   (0xE051),
		Ins    (0xE052),
		Del    (0xE053),

		LCtrl  (0x001D),
		LShift (0x002A),
		LAlt   (0x0038),
		RAltGr (0xE038),
		LWin   (0xE05B);

		private int scanCode;
		private String name;

		UsScan(int scanCode) {
			this.scanCode = scanCode;
			this.name = null;
		}

		UsScan(int scanCode, String name) {
			this.scanCode = scanCode;
			this.name = name;
		}

		public String getName() {
			if (this.name != null) return name;
			return this.name();
		}

		public short getScanCode() {
			return (short)(scanCode & 0x00FF);
		}

		public boolean isExtended() {
			return (scanCode & 0xE000) != 0;
		}
	}

	// Shorthands for state masks
	protected final static int ____ = 0;
	protected final static int ___S = SWT.SHIFT;
	protected final static int __C_ = SWT.CONTROL;
	protected final static int A___ = SWT.ALT;
	protected final static int AG__ = SWT.ALT_GR;
	protected final static int __CS = SWT.CONTROL | SWT.SHIFT;
	protected final static int A__S = SWT.ALT | SWT.SHIFT;
	protected final static int AG_S = SWT.ALT_GR | SWT.SHIFT;
	protected final static int A_C_ = SWT.ALT | SWT.CONTROL;
	protected final static int A_CS = SWT.ALT | SWT.CONTROL | SWT.SHIFT;
	protected final static int AGCS = SWT.ALT_GR | SWT.CONTROL | SWT.SHIFT;

	// All states not involving AltGr
	protected final static int COMMON_STATES[] = {
		____,
		___S,
		__C_,
		A___,
		__CS,
		A__S,
		A_C_,
		A_CS,
	};

	// All states that should produce unmodified characters
	protected final static int NORMAL_STATES[] = {
		____,
		A___,
	};

	@Before
	public void setUp() {
		display = new Display();
		shell = new Shell();

		Listener listener = event -> {
			// For simplicity of writing tests, skip events for pure modifiers such as Ctrl
			if (((event.keyCode & ~SWT.MODIFIER_MASK) == 0) && (event.character == 0)) {
				return;
			}

			Event eventCopy       = new Event();
			eventCopy.type        = event.type;
			eventCopy.keyCode     = event.keyCode;
			eventCopy.keyLocation = event.keyLocation;
			eventCopy.character   = event.character;
			eventCopy.stateMask   = event.stateMask;
			events.add(eventCopy);

			// Suppress whatever default handling to avoid interacting with the test
			event.doit = false;
		};

		shell.addListener(SWT.KeyDown, listener);
		shell.addListener(SWT.KeyUp,   listener);

		shell.setSize(100, 100);
		shell.open();
		shell.forceFocus();
	}

	@After
	public void tearDown() {
		shell.dispose();
		display.dispose();
	}

	protected static void failOnApiError(String name) {
		int error = OS.GetLastError();
		fail("Error in " + name + " : " + error);
	}

	// Wrapper for WINAPI GetKeyboardLayoutList()
	static long[] getCurrentLayouts() {
		int size = OS.GetKeyboardLayoutList(0, null);
		long[] result = new long[size];
		if (0 == OS.GetKeyboardLayoutList(size, result)) {
			failOnApiError("GetKeyboardLayoutList()");
		}
		return result;
	}

	static void activateLayout(long hkl) {
		if (0 == OS.ActivateKeyboardLayout(hkl, 0)) {
			failOnApiError("ActivateKeyboardLayout()");
		}
	}

	protected void runWithLayout(char[] layout, Runnable test) {
		long[] oldLayouts = getCurrentLayouts();
		long oldCurLayout = OS.GetKeyboardLayout(OS.GetCurrentThreadId());
		long newLayout = 0;

		// Reset LEDs to let tests run in stable environment
		final boolean wasCapsLock   = isLedEnabled(OS.VK_CAPITAL);
		if           (wasCapsLock)       toggleLed(OS.VK_CAPITAL);
		final boolean wasNumLock    = isLedEnabled(OS.VK_NUMLOCK);
		if           (wasNumLock)        toggleLed(OS.VK_NUMLOCK);
		final boolean wasScrollLock = isLedEnabled(OS.VK_SCROLL);
		if           (wasScrollLock)     toggleLed(OS.VK_SCROLL);

		try {
			collectKeyErrors = true;

			newLayout = OS.LoadKeyboardLayout(layout, 0);
			activateLayout(newLayout);

			test.run();
		} finally {
			// Loading/activating keyboard layouts has system-wide effect.
			// Undo changes after the test to avoid bothering user.
			activateLayout(oldCurLayout);

			boolean wasPresent = false;
			for (long oldLayout : oldLayouts) {
				if (oldLayout == newLayout) {
					wasPresent = true;
					break;
				}
			}

			if (!wasPresent) {
				// Only unload tested layout if it wasn't present before the test.
				// This is to avoid killing user's layouts.
				OS.UnloadKeyboardLayout(newLayout);
			}

			// Restore LED states
			if (wasCapsLock   != isLedEnabled(OS.VK_CAPITAL)) toggleLed(OS.VK_CAPITAL);
			if (wasNumLock    != isLedEnabled(OS.VK_NUMLOCK)) toggleLed(OS.VK_NUMLOCK);
			if (wasScrollLock != isLedEnabled(OS.VK_SCROLL))  toggleLed(OS.VK_SCROLL);

			// Report errors
			ArrayList<AssertionError> errors = keyErrors;
			collectKeyErrors = false;
			keyErrors = null;
			if (errors != null) {
				AssertionError error0 = errors.get(0);
				for (AssertionError error : errors) {
					if (error != error0) {
						error0.addSuppressed(error);
					}
				}
				throw error0;
			}
		}
	}

	protected static void processEvents() {
		Display display = Display.getCurrent();
		if (display != null && !display.isDisposed()) {
			while (display.readAndDispatch()) {
			}
		}
	}

	/**
	 * If someone is using computer while test is running, simulated keypresses
	 * could end up in unexpected applications, possibly doing weird things
	 * there. The workaround is to just fail tests when Shell loses its focus.
	 */
	protected void ensureShellFocused() {
		// Note that `Shell.isFocusControl()` still returns `true` even if a
		// different app is focused
		if (shell.handle != OS.GetForegroundWindow()) {
			fail("Test Shell lost focus (did you use keyboard/mouse while the test was running?)");
		}
	}

	static KEYBDINPUT keybdinput = new KEYBDINPUT();
	static INPUT input = new INPUT();
	{
		input.type = OS.INPUT_KEYBOARD;
		input.ki = keybdinput;
	}

	/**
	 * Scan codes are emulated in order to be able to test both
	 * (scan code -> virtual key) and (virtual key -> char) translations.
	 * See {@link org.eclipse.swt.widgets.Event#keyCode} for a detailed explanation.
	 */
	protected void emulateScanCode(UsScan scanCode, boolean isUp) {
		keybdinput.wScan = scanCode.getScanCode();
		keybdinput.dwFlags = OS.KEYEVENTF_SCANCODE;

		if (isUp) {
			keybdinput.dwFlags |= OS.KEYEVENTF_KEYUP;
		}

		if (scanCode.isExtended()) {
			keybdinput.dwFlags |= OS.KEYEVENTF_EXTENDEDKEY;
		}

		ensureShellFocused();

		if (0 == OS.SendInput(1, input, INPUT.sizeof)) {
			failOnApiError("SendInput()");
		}
	}

	protected void emulateState(int stateMask, boolean isUp) {
		if ((stateMask & SWT.ALT) != 0)    emulateScanCode(UsScan.LAlt,   isUp);
		if ((stateMask & SWT.ALT_GR) != 0) emulateScanCode(UsScan.RAltGr, isUp);
		if ((stateMask & SWT.CTRL) != 0)   emulateScanCode(UsScan.LCtrl,  isUp);
		if ((stateMask & SWT.SHIFT) != 0 ) emulateScanCode(UsScan.LShift, isUp);
	}

	protected void emulateScanCode(int stateMask, UsScan... scanCodes) {
		emulateState(stateMask, false);

		for (UsScan scanCode : scanCodes) {
			emulateScanCode(scanCode);
		}

		emulateState(stateMask, true);
	}

	protected void emulateScanCode(UsScan scanCode) {
		emulateScanCode(scanCode, false);
		emulateScanCode(scanCode, true);
	}

	protected void emulateVirtualKey(int virtualKey, boolean isUp) {
		keybdinput.wVk = (short)virtualKey;
		keybdinput.dwFlags = 0;

		if (isUp) {
			keybdinput.dwFlags |= OS.KEYEVENTF_KEYUP;
		}

		ensureShellFocused();

		if (0 == OS.SendInput(1, input, INPUT.sizeof)) {
			failOnApiError("SendInput()");
		}
	}

	protected void emulateVirtualKey(int virtualKey) {
		emulateVirtualKey(virtualKey, false);
		emulateVirtualKey(virtualKey, true);
	}

	protected void emulateVirtualKey(int stateMask, int virtualKey) {
		emulateState(stateMask, false);
		emulateVirtualKey(virtualKey);
		emulateState(stateMask, true);
	}

	protected static String formatStateMask(int stateMask) {
		assert(SWT.MODIFIER_MASK == (SWT.ALT | SWT.SHIFT | SWT.CTRL | SWT.COMMAND | SWT.ALT_GR));

		StringBuilder sb = new StringBuilder();
		if ((stateMask & SWT.ALT_GR) != 0)      sb.append("AltGr+");
		if ((stateMask & SWT.ALT) != 0)         sb.append("Alt+");
		if ((stateMask & SWT.COMMAND) != 0)     sb.append("Cmd+");
		if ((stateMask & SWT.CTRL) != 0)        sb.append("Ctrl+");
		if ((stateMask & SWT.SHIFT) != 0)       sb.append("Shift+");

		return sb.toString();
	}

	protected static char formatChar(int character) {
		if ((character >= 0x20) && (character < 0xFFFF)) {
			return (char) character;
		}

		return ' ';
	}

	protected static char formatEventType(Event event) {
		switch(event.type)
		{
			case SWT.KeyUp:
				return '↑';
			case SWT.KeyDown:
				return '↓';
		}

		return '?';
	}

	protected static String formatKeyEvent(Event event) {
		return String.format(
			"%c character=0x%04X (%s) keyCode=0x%04X (%s%s)",
			formatEventType(event),
			(int)event.character,
			formatChar(event.character),
			event.keyCode,
			formatStateMask(event.stateMask),
			formatChar(event.keyCode)
		);
	}

	protected void onKeyError(AssertionError error) {
		if (!collectKeyErrors) {
			throw error;
		} else {
			if (keyErrors == null)
				keyErrors = new ArrayList<>();

			System.out.println(testName.getMethodName() + " : " + error.getMessage());
			keyErrors.add(error);
		}
	}

	protected void expectKeyEvents(String testName, Runnable runnable, Event... expectEvents) {
		events.clear();
		runnable.run();

		// Wait for key presses to arrive
		processEvents();
		// Avoid false positives when focus was switched to another app
		ensureShellFocused();

		if (dumpEvents) {
			System.out.println("Expected events:");
			for (Event event : expectEvents) {
				System.out.println("  " + formatKeyEvent(event));
			}

			System.out.println("Actual events:");
			for (Event event : events) {
				System.out.println("  " + formatKeyEvent(event));
			}
		}

		try {
			String testNamePrefix = "Testing {" + testName + "}: ";

			int iExpect = 0;
			for (int iActual = 0; iActual < events.size(); iActual++) {
				Event actual = events.get(iActual);

				if (expectEvents.length <= iExpect) {
					fail(testNamePrefix + "trailing unexpected {" + formatKeyEvent(actual) + "}");
				}

				Event expect = expectEvents[iExpect];

				if ((expect.type != actual.type) ||
					(expect.keyCode != actual.keyCode) ||
					(expect.character != actual.character) ||
					(expect.stateMask != actual.stateMask))
				{
					fail(testNamePrefix + "Expected#" + iExpect + ": expected {" + formatKeyEvent(expect) + "} got {" + formatKeyEvent(actual) + "}");
				}

				if (expect.keyLocation != actual.keyLocation) {
					fail(testNamePrefix + "Expected#" + iExpect + ": Event.keyLocation wrong for {" + formatKeyEvent(expect) + "}");
				}

				iExpect++;
			}

			if (iExpect < expectEvents.length) {
				fail(testNamePrefix + "Expected#" + iExpect + ": expected {" + formatKeyEvent(expectEvents[iExpect]) + "}; got nothing");
			}
		} catch (AssertionError error) {
			onKeyError(error);
		}
	}

	protected static Event expectKeyDown(int stateMask, char character, int keyLocation, int keyCode) {
		Event event       = new Event();
		event.type        = SWT.KeyDown;
		event.keyCode     = keyCode;
		event.keyLocation = keyLocation;
		event.character   = character;
		event.stateMask   = stateMask;
		return event;
	}

	protected static Event expectKeyUp(int stateMask, char character, int keyLocation, int keyCode) {
		Event event       = new Event();
		event.type        = SWT.KeyUp;
		event.keyCode     = keyCode;
		event.keyLocation = keyLocation;
		event.character   = character;
		event.stateMask   = stateMask;
		return event;
	}

	protected void testKey(String testName, Runnable runnable, int expectStateMask, char expectChar, int expectLocation, int expectKeyCode) {
		expectKeyEvents(
			testName,
			runnable,
			expectKeyDown(expectStateMask, expectChar, expectLocation, expectKeyCode),
			expectKeyUp  (expectStateMask, expectChar, expectLocation, expectKeyCode)
		);
	}

	protected void testScanCode(int pressStateMask, UsScan pressScanCode, int expectStateMask, char expectChar, int expectLocation, int expectKeyCode) {
		String testName = getKeyName(pressStateMask, pressScanCode);
		testKey(
			testName,
			() -> emulateScanCode(pressStateMask, pressScanCode),
			expectStateMask, expectChar, expectLocation, expectKeyCode
		);
	}

	protected void testVirtualKey(int pressStateMask, int virtualKey, int expectStateMask, char expectChar, int expectLocation, int expectKeyCode) {
		String testName = "VK_" + virtualKey;
		testKey(
			testName,
			() -> emulateVirtualKey(pressStateMask, virtualKey),
			expectStateMask, expectChar, expectLocation, expectKeyCode
		);
	}

	protected void testDeadKey(int deadStateMask, UsScan deadKey, int nextStateMask, UsScan nextKey, int expectStateMask, char expectChar, int expectKeyCode) {
		String testName = getKeyName(deadStateMask, deadKey) + getKeyName(nextStateMask, nextKey);
		testKey(
			testName,
			() -> {
				emulateScanCode(deadStateMask, deadKey);
				emulateScanCode(nextStateMask, nextKey);
			},
			expectStateMask, expectChar, 0, expectKeyCode
		);
	}

	protected static String getKeyName(int stateMask, UsScan scanCode) {
		return formatStateMask(stateMask) + scanCode.getName();
	}

	/**
	 * Tests whether given key is a global hotkey registered with WINAPI 'RegisterHotKey()'
	 */
	protected boolean isRegisteredHotkey(int stateMask, UsScan scanCode) {
		int vk = OS.MapVirtualKey(scanCode.getScanCode(), OS.MAPVK_VSC_TO_VK);
		if (vk == 0) {
			System.out.format("Can't map scan code 0x%02X to vk%n", scanCode.getScanCode());
			return false;
		}

		int mods = 0;
		if ((stateMask & SWT.ALT)     != 0) mods |= OS.MOD_ALT;
		if ((stateMask & SWT.CONTROL) != 0) mods |= OS.MOD_CONTROL;
		if ((stateMask & SWT.SHIFT)   != 0) mods |= OS.MOD_SHIFT;

		// Test by attempting to register it for ourselves.
		final int id = 1000;
		if (!OS.RegisterHotKey(0, id, mods, vk)) {
			// Key is already registered by someone else
			return true;
		}

		// Clean up
		OS.UnregisterHotKey(0, id);
		return false;
	}

	protected static boolean isLedEnabled(int virtualKey) {
		return ((OS.GetKeyState(virtualKey) & 0x01) != 0);
	}

	protected void toggleLed(int virtualKey) {
		emulateVirtualKey(virtualKey, false);
		emulateVirtualKey(virtualKey, true);
		// Wait for key presses to arrive
		processEvents();
	}
}

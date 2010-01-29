/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

abstract class WebBrowser {
	Browser browser;
	CloseWindowListener[] closeWindowListeners = new CloseWindowListener[0];
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	TitleListener[] titleListeners = new TitleListener[0];
	VisibilityWindowListener[] visibilityWindowListeners = new VisibilityWindowListener[0];

	static Runnable MozillaClearSessions;
	static Runnable NativeClearSessions;

	/* Key Mappings */
	static final int [][] KeyTable = {
		/* Keyboard and Mouse Masks */
		{18,	SWT.ALT},
		{16,	SWT.SHIFT},
		{17,	SWT.CONTROL},
		{224,	SWT.COMMAND},

		/* Literal Keys */
		{65,	'a'},
		{66,	'b'},
		{67,	'c'},
		{68,	'd'},
		{69,	'e'},
		{70,	'f'},
		{71,	'g'},
		{72,	'h'},
		{73,	'i'},
		{74,	'j'},
		{75,	'k'},
		{76,	'l'},
		{77,	'm'},
		{78,	'n'},
		{79,	'o'},
		{80,	'p'},
		{81,	'q'},
		{82,	'r'},
		{83,	's'},
		{84,	't'},
		{85,	'u'},
		{86,	'v'},
		{87,	'w'},
		{88,	'x'},
		{89,	'y'},
		{90,	'z'},
		{48,	'0'},
		{49,	'1'},
		{50,	'2'},
		{51,	'3'},
		{52,	'4'},
		{53,	'5'},
		{54,	'6'},
		{55,	'7'},
		{56,	'8'},
		{57,	'9'},
		{32,	' '},
		{59,	';'},
		{61,	'='},
		{188,	','},
		{190,	'.'},
		{191,	'/'},
		{219,	'['},
		{221,	']'},
		{222,	'\''},
		{192,	'`'},
		{220,	'\\'},
		{108,	'|'},

		/* Non-Numeric Keypad Keys */
		{37,	SWT.ARROW_LEFT},
		{39,	SWT.ARROW_RIGHT},
		{38,	SWT.ARROW_UP},
		{40,	SWT.ARROW_DOWN},
		{45,	SWT.INSERT},
		{36,	SWT.HOME},
		{35,	SWT.END},
		{46,	SWT.DEL},
		{33,	SWT.PAGE_UP},
		{34,	SWT.PAGE_DOWN},

		/* Virtual and Ascii Keys */
		{8,		SWT.BS},
		{13,	SWT.CR},
		{9,		SWT.TAB},
		{27,	SWT.ESC},
		{12,	SWT.DEL},

		/* Functions Keys */
		{112,	SWT.F1},
		{113,	SWT.F2},
		{114,	SWT.F3},
		{115,	SWT.F4},
		{116,	SWT.F5},
		{117,	SWT.F6},
		{118,	SWT.F7},
		{119,	SWT.F8},
		{120,	SWT.F9},
		{121,	SWT.F10},
		{122,	SWT.F11},
		{123,	SWT.F12},
		{124,	SWT.F13},
		{125,	SWT.F14},
		{126,	SWT.F15},
		{127,	0},
		{128,	0},
		{129,	0},
		{130,	0},
		{131,	0},
		{132,	0},
		{133,	0},
		{134,	0},
		{135,	0},

		/* Numeric Keypad Keys */
		{96,	SWT.KEYPAD_0},
		{97,	SWT.KEYPAD_1},
		{98,	SWT.KEYPAD_2},
		{99,	SWT.KEYPAD_3},
		{100,	SWT.KEYPAD_4},
		{101,	SWT.KEYPAD_5},
		{102,	SWT.KEYPAD_6},
		{103,	SWT.KEYPAD_7},
		{104,	SWT.KEYPAD_8},
		{105,	SWT.KEYPAD_9},
		{14,	SWT.KEYPAD_CR},
		{107,	SWT.KEYPAD_ADD},
		{109,	SWT.KEYPAD_SUBTRACT},
		{106,	SWT.KEYPAD_MULTIPLY},
		{111,	SWT.KEYPAD_DIVIDE},
		{110,	SWT.KEYPAD_DECIMAL},

		/* Other keys */
		{20,	SWT.CAPS_LOCK},
		{144,	SWT.NUM_LOCK},
		{145,	SWT.SCROLL_LOCK},
		{44,	SWT.PRINT_SCREEN},
		{6,		SWT.HELP},
		{19,	SWT.PAUSE},
		{3,		SWT.BREAK},

		/* Safari-specific */
		{186,	';'},
		{187,	'='},
		{189,	'-'},
	};

public void addCloseWindowListener (CloseWindowListener listener) {
	CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length + 1];
	System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, closeWindowListeners.length);
	closeWindowListeners = newCloseWindowListeners;
	closeWindowListeners[closeWindowListeners.length - 1] = listener;
}

public void addLocationListener (LocationListener listener) {
	LocationListener[] newLocationListeners = new LocationListener[locationListeners.length + 1];
	System.arraycopy(locationListeners, 0, newLocationListeners, 0, locationListeners.length);
	locationListeners = newLocationListeners;
	locationListeners[locationListeners.length - 1] = listener;
}

public void addOpenWindowListener (OpenWindowListener listener) {
	OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length + 1];
	System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, openWindowListeners.length);
	openWindowListeners = newOpenWindowListeners;
	openWindowListeners[openWindowListeners.length - 1] = listener;
}

public void addProgressListener (ProgressListener listener) {
	ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length + 1];
	System.arraycopy(progressListeners, 0, newProgressListeners, 0, progressListeners.length);
	progressListeners = newProgressListeners;
	progressListeners[progressListeners.length - 1] = listener;
}

public void addStatusTextListener (StatusTextListener listener) {
	StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length + 1];
	System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, statusTextListeners.length);
	statusTextListeners = newStatusTextListeners;
	statusTextListeners[statusTextListeners.length - 1] = listener;
}

public void addTitleListener (TitleListener listener) {
	TitleListener[] newTitleListeners = new TitleListener[titleListeners.length + 1];
	System.arraycopy(titleListeners, 0, newTitleListeners, 0, titleListeners.length);
	titleListeners = newTitleListeners;
	titleListeners[titleListeners.length - 1] = listener;
}

public void addVisibilityWindowListener (VisibilityWindowListener listener) {
	VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length + 1];
	System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, visibilityWindowListeners.length);
	visibilityWindowListeners = newVisibilityWindowListeners;
	visibilityWindowListeners[visibilityWindowListeners.length - 1] = listener;
}

public abstract boolean back ();

public static void clearSessions () {
	if (NativeClearSessions != null) NativeClearSessions.run ();
	if (MozillaClearSessions != null) MozillaClearSessions.run ();
}

public abstract void create (Composite parent, int style);

public abstract boolean execute (String script);

public abstract boolean forward ();

public abstract String getText ();

public abstract String getUrl ();

public Object getWebBrowser () {
	return null;
}

public abstract boolean isBackEnabled ();

public boolean isFocusControl () {
	return false;
}

public abstract boolean isForwardEnabled ();

public abstract void refresh ();

public void removeCloseWindowListener (CloseWindowListener listener) {
	if (closeWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		if (listener == closeWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (closeWindowListeners.length == 1) {
		closeWindowListeners = new CloseWindowListener[0];
		return;
	}
	CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length - 1];
	System.arraycopy (closeWindowListeners, 0, newCloseWindowListeners, 0, index);
	System.arraycopy (closeWindowListeners, index + 1, newCloseWindowListeners, index, closeWindowListeners.length - index - 1);
	closeWindowListeners = newCloseWindowListeners;
}

public void removeLocationListener (LocationListener listener) {
	if (locationListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < locationListeners.length; i++) {
		if (listener == locationListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (locationListeners.length == 1) {
		locationListeners = new LocationListener[0];
		return;
	}
	LocationListener[] newLocationListeners = new LocationListener[locationListeners.length - 1];
	System.arraycopy (locationListeners, 0, newLocationListeners, 0, index);
	System.arraycopy (locationListeners, index + 1, newLocationListeners, index, locationListeners.length - index - 1);
	locationListeners = newLocationListeners;
}

public void removeOpenWindowListener (OpenWindowListener listener) {
	if (openWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < openWindowListeners.length; i++) {
		if (listener == openWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (openWindowListeners.length == 1) {
		openWindowListeners = new OpenWindowListener[0];
		return;
	}
	OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length - 1];
	System.arraycopy (openWindowListeners, 0, newOpenWindowListeners, 0, index);
	System.arraycopy (openWindowListeners, index + 1, newOpenWindowListeners, index, openWindowListeners.length - index - 1);
	openWindowListeners = newOpenWindowListeners;
}

public void removeProgressListener (ProgressListener listener) {
	if (progressListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < progressListeners.length; i++) {
		if (listener == progressListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (progressListeners.length == 1) {
		progressListeners = new ProgressListener[0];
		return;
	}
	ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length - 1];
	System.arraycopy (progressListeners, 0, newProgressListeners, 0, index);
	System.arraycopy (progressListeners, index + 1, newProgressListeners, index, progressListeners.length - index - 1);
	progressListeners = newProgressListeners;
}

public void removeStatusTextListener (StatusTextListener listener) {
	if (statusTextListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < statusTextListeners.length; i++) {
		if (listener == statusTextListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (statusTextListeners.length == 1) {
		statusTextListeners = new StatusTextListener[0];
		return;
	}
	StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length - 1];
	System.arraycopy (statusTextListeners, 0, newStatusTextListeners, 0, index);
	System.arraycopy (statusTextListeners, index + 1, newStatusTextListeners, index, statusTextListeners.length - index - 1);
	statusTextListeners = newStatusTextListeners;
}

public void removeTitleListener (TitleListener listener) {
	if (titleListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < titleListeners.length; i++) {
		if (listener == titleListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (titleListeners.length == 1) {
		titleListeners = new TitleListener[0];
		return;
	}
	TitleListener[] newTitleListeners = new TitleListener[titleListeners.length - 1];
	System.arraycopy (titleListeners, 0, newTitleListeners, 0, index);
	System.arraycopy (titleListeners, index + 1, newTitleListeners, index, titleListeners.length - index - 1);
	titleListeners = newTitleListeners;
}

public void removeVisibilityWindowListener (VisibilityWindowListener listener) {
	if (visibilityWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < visibilityWindowListeners.length; i++) {
		if (listener == visibilityWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (visibilityWindowListeners.length == 1) {
		visibilityWindowListeners = new VisibilityWindowListener[0];
		return;
	}
	VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length - 1];
	System.arraycopy (visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, index);
	System.arraycopy (visibilityWindowListeners, index + 1, newVisibilityWindowListeners, index, visibilityWindowListeners.length - index - 1);
	visibilityWindowListeners = newVisibilityWindowListeners;
}

public void setBrowser (Browser browser) {
	this.browser = browser;
}

public abstract boolean setText (String html);

public abstract boolean setUrl (String url, String postData, String[] headers);

public abstract void stop ();

int translateKey (int key) {
	for (int i = 0; i < KeyTable.length; i++) {
		if (KeyTable[i][0] == key) return KeyTable[i][1];
	}
	return 0;
}
}

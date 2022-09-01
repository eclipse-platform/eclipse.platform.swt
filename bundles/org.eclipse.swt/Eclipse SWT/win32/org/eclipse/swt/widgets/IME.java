/*******************************************************************************
 * Copyright (c) 2007, 2014 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent input method editors.
 * These are typically in-line pre-edit text areas that allow
 * the user to compose characters from Far Eastern languages
 * such as Japanese, Chinese or Korean.
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>ImeComposition</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.4
 * @noextend This class is not intended to be subclassed by clients.
 */
public class IME extends Widget {
	Canvas parent;
	int caretOffset;
	int startOffset;
	int commitCount;
	String text;
	int [] ranges;
	TextStyle [] styles;

	static final int WM_MSIME_MOUSE = OS.RegisterWindowMessage (new TCHAR (0, "MSIMEMouseOperation", true)); //$NON-NLS-1$

	/* TextLayout has a copy of these constants */
	static final int UNDERLINE_IME_DOT = 1 << 16;
	static final int UNDERLINE_IME_DASH = 2 << 16;
	static final int UNDERLINE_IME_THICK = 3 << 16;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
IME () {
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a canvas control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public IME (Canvas parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

void createWidget () {
	text = ""; //$NON-NLS-1$
	startOffset = -1;
	if (parent.getIME () == null) {
		parent.setIME (this);
	}
}

/**
 * Returns the offset of the caret from the start of the document.
 * -1 means that there is currently no active composition.
 * The caret is within the current composition.
 *
 * @return the caret offset
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretOffset () {
	checkWidget ();
	return startOffset + caretOffset;
}

/**
 * Returns the commit count of the composition.  This is the
 * number of characters that have been composed.  When the
 * commit count is equal to the length of the composition
 * text, then the in-line edit operation is complete.
 *
 * @return the commit count
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see IME#getText
 */
public int getCommitCount () {
	checkWidget ();
	return commitCount;
}

/**
 * Returns the offset of the composition from the start of the document.
 * This is the start offset of the composition within the document and
 * in not changed by the input method editor itself during the in-line edit
 * session.
 *
 * @return the offset of the composition
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCompositionOffset () {
	checkWidget ();
	return startOffset;
}

TF_DISPLAYATTRIBUTE getDisplayAttribute (short langid, int attInfo) {
	long [] ppv = new long [1];
	int hr = COM.CoCreateInstance (COM.CLSID_TF_InputProcessorProfiles, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_ITfInputProcessorProfiles, ppv);
	TF_DISPLAYATTRIBUTE pda = null;
	if (hr == OS.S_OK) {
		ITfInputProcessorProfiles pProfiles = new ITfInputProcessorProfiles (ppv [0]);
		GUID pclsid = new GUID ();
		GUID pguidProfile = new GUID ();
		hr = pProfiles.GetDefaultLanguageProfile (langid, COM.GUID_TFCAT_TIP_KEYBOARD, pclsid, pguidProfile);
		if (hr == OS.S_OK) {
			hr = COM.CoCreateInstance (pclsid, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_ITfDisplayAttributeProvider, ppv);
			if (hr == OS.S_OK) {
				ITfDisplayAttributeProvider pProvider = new ITfDisplayAttributeProvider (ppv [0]);
				hr = pProvider.EnumDisplayAttributeInfo (ppv);
				if (hr == OS.S_OK) {
					IEnumTfDisplayAttributeInfo pEnum = new IEnumTfDisplayAttributeInfo (ppv [0]);
					TF_DISPLAYATTRIBUTE tempPda = new TF_DISPLAYATTRIBUTE ();
					while ((hr = pEnum.Next (1, ppv, null)) == OS.S_OK) {
						ITfDisplayAttributeInfo pDispInfo = new ITfDisplayAttributeInfo (ppv [0]);
						pDispInfo.GetAttributeInfo (tempPda);
						pDispInfo.Release ();
						if (tempPda.bAttr == attInfo) {
							pda = tempPda;
							break;
						}
					}
					pEnum.Release ();
				}
				pProvider.Release ();
			}
		}
		pProfiles.Release ();
	}
	if (pda == null) {
		pda = new TF_DISPLAYATTRIBUTE ();
		switch (attInfo) {
			case OS.TF_ATTR_INPUT:
				pda.lsStyle = OS.TF_LS_SQUIGGLE;
				break;
			case OS.TF_ATTR_CONVERTED:
			case OS.TF_ATTR_TARGET_CONVERTED:
				pda.lsStyle = OS.TF_LS_SOLID;
				pda.fBoldLine = attInfo == OS.TF_ATTR_TARGET_CONVERTED;
				break;
		}
	}
	return pda;
}

/**
 * Returns the ranges for the style that should be applied during the
 * in-line edit session.
 * <p>
 * The ranges array contains start and end pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] and ends at ranges[n+1] uses the style
 * at styles[n/2] returned by <code>getStyles()</code>.
 * </p>
 * @return the ranges for the styles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see IME#getStyles
 */
public int [] getRanges () {
	checkWidget ();
	if (ranges == null) return new int [0];
	int [] result = new int [ranges.length];
	for (int i = 0; i < result.length; i++) {
		result [i] = ranges [i] + startOffset;
	}
	return result;
}

/**
 * Returns the styles for the ranges.
 * <p>
 * The ranges array contains start and end pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] and ends at ranges[n+1] uses the style
 * at styles[n/2].
 * </p>
 *
 * @return the ranges for the styles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see IME#getRanges
 */
public TextStyle [] getStyles () {
	checkWidget ();
	if (styles == null) return new TextStyle [0];
	TextStyle [] result = new TextStyle [styles.length];
	System.arraycopy (styles, 0, result, 0, styles.length);
	return result;
}

/**
 * Returns the composition text.
 * <p>
 * The text for an IME is the characters in the widget that
 * are in the current composition. When the commit count is
 * equal to the length of the composition text, then the
 * in-line edit operation is complete.
 * </p>
 *
 * @return the widget text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return text;
}

/**
 * Returns <code>true</code> if the caret should be wide, and
 * <code>false</code> otherwise.  In some languages, for example
 * Korean, the caret is typically widened to the width of the
 * current character in the in-line edit session.
 *
 * @return the wide caret state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getWideCaret() {
	checkWidget ();
	long layout = OS.GetKeyboardLayout (0);
	short langID = (short)OS.LOWORD (layout);
	return OS.PRIMARYLANGID (langID) == OS.LANG_KOREAN;
}

boolean isInlineEnabled () {
	return OS.IsDBLocale && hooks (SWT.ImeComposition);
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (this == parent.getIME ()) parent.setIME (null);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	text = null;
	styles = null;
	ranges = null;
}

/**
 * Sets the offset of the composition from the start of the document.
 * This is the start offset of the composition within the document and
 * in not changed by the input method editor itself during the in-line edit
 * session but may need to be changed by clients of the IME.  For example,
 * if during an in-line edit operation, a text editor inserts characters
 * above the IME, then the IME must be informed that the composition
 * offset has changed.
 *
 * @param offset the offset of the composition
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCompositionOffset (int offset) {
	checkWidget ();
	if (offset < 0) return;
	if (startOffset != -1) {
		startOffset = offset;
	}
}

LRESULT WM_IME_COMPOSITION (long wParam, long lParam) {
	if (!isInlineEnabled ()) return null;
	ranges = null;
	styles = null;
	caretOffset = commitCount = 0;
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	if (hIMC != 0) {
		char [] buffer = null;
		if ((lParam & OS.GCS_RESULTSTR) != 0) {
			int length = OS.ImmGetCompositionString (hIMC, OS.GCS_RESULTSTR, (char [])null, 0);
			if (length > 0) {
				buffer = new char [length / TCHAR.sizeof];
				OS.ImmGetCompositionString (hIMC, OS.GCS_RESULTSTR, buffer, length);
				if (startOffset == -1) {
					Event event = new Event ();
					event.detail = SWT.COMPOSITION_SELECTION;
					sendEvent (SWT.ImeComposition, event);
					startOffset = event.start;
				}
				Event event = new Event ();
				event.detail = SWT.COMPOSITION_CHANGED;
				event.start = startOffset;
				event.end = startOffset + text.length();
				event.text = text = buffer != null ? new String (buffer) : ""; //$NON-NLS-1$
				commitCount = text.length ();
				sendEvent (SWT.ImeComposition, event);
				String chars = text;
				text = ""; //$NON-NLS-1$
				startOffset = -1;
				commitCount = 0;
				if (event.doit) {
					Display display = this.display;
					display.lastKey = 0;
					display.lastVirtual = display.lastDead = false;
					length = chars.length ();
					for (int i = 0; i < length; i++) {
						char c = chars.charAt (i);
						display.lastAscii = c;
						event = new Event ();
						event.character = c;
						parent.sendEvent (SWT.KeyDown, event);
					}
				}
			}
			if ((lParam & OS.GCS_COMPSTR) == 0) return LRESULT.ONE;
		}
		buffer = null;
		if ((lParam & OS.GCS_COMPSTR) != 0) {
			int length = OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, (char [])null, 0);
			if (length > 0) {
				buffer = new char [length / TCHAR.sizeof];
				OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, buffer, length);
				if ((lParam & OS.GCS_CURSORPOS) != 0) {
					caretOffset = OS.ImmGetCompositionString (hIMC, OS.GCS_CURSORPOS, (char [])null, 0);
				}
				int [] clauses = null;
				if ((lParam & OS.GCS_COMPCLAUSE) != 0) {
					length = OS.ImmGetCompositionString (hIMC, OS.GCS_COMPCLAUSE, (int [])null, 0);
					if (length > 0) {
						clauses = new int [length / 4];
						OS.ImmGetCompositionString (hIMC, OS.GCS_COMPCLAUSE, clauses, length);
					}
				}
				if ((lParam & OS.GCS_COMPATTR) != 0 && clauses != null) {
					length = OS.ImmGetCompositionString (hIMC, OS.GCS_COMPATTR, (byte [])null, 0);
					if (length > 0) {
						byte [] attrs = new byte [length];
						OS.ImmGetCompositionString (hIMC, OS.GCS_COMPATTR, attrs, length);
						length = clauses.length - 1;
						ranges = new int [length * 2];
						styles = new TextStyle [length];
						long layout = OS.GetKeyboardLayout (0);
						short langID = (short)OS.LOWORD (layout);
						TF_DISPLAYATTRIBUTE attr = null;
						TextStyle style = null;
						for (int i = 0; i < length; i++) {
							ranges [i * 2] = clauses [i];
							ranges [i * 2 + 1] = clauses [i + 1] - 1;
							styles [i] = style = new TextStyle ();
							/* Added length check to avoid possibility of AIOOB, bug 444926 */
							if (clauses [i] >= 0 && clauses [i] < attrs.length) {
								attr = getDisplayAttribute (langID, attrs [clauses [i]]);
								if (attr != null) {
									switch (attr.crText.type) {
										case OS.TF_CT_COLORREF:
											style.foreground = Color.win32_new (display, attr.crText.cr);
											break;
										case OS.TF_CT_SYSCOLOR:
											int colorRef = OS.GetSysColor (attr.crText.cr);
											style.foreground = Color.win32_new (display, colorRef);
											break;
									}
									switch (attr.crBk.type) {
										case OS.TF_CT_COLORREF:
											style.background = Color.win32_new (display, attr.crBk.cr);
											break;
										case OS.TF_CT_SYSCOLOR:
											int colorRef = OS.GetSysColor (attr.crBk.cr);
											style.background = Color.win32_new (display, colorRef);
											break;
									}
									switch (attr.crLine.type) {
										case OS.TF_CT_COLORREF:
											style.underlineColor = Color.win32_new (display, attr.crLine.cr);
											break;
										case OS.TF_CT_SYSCOLOR:
											int colorRef = OS.GetSysColor (attr.crLine.cr);
											style.underlineColor = Color.win32_new (display, colorRef);
											break;
									}
									style.underline = attr.lsStyle != OS.TF_LS_NONE;
									switch (attr.lsStyle) {
										case OS.TF_LS_SQUIGGLE:
											style.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
											break;
										case OS.TF_LS_DASH:
											style.underlineStyle = UNDERLINE_IME_DASH;
											break;
										case OS.TF_LS_DOT:
											style.underlineStyle = UNDERLINE_IME_DOT;
											break;
										case OS.TF_LS_SOLID:
											style.underlineStyle = attr.fBoldLine ? UNDERLINE_IME_THICK : SWT.UNDERLINE_SINGLE;
											break;
									}
								}
							}
						}
					}
				}
			}
			OS.ImmReleaseContext (hwnd, hIMC);
		}
		int end = startOffset + text.length();
		if (startOffset == -1) {
			Event event = new Event ();
			event.detail = SWT.COMPOSITION_SELECTION;
			sendEvent (SWT.ImeComposition, event);
			startOffset = event.start;
			end = event.end;
		}
		Event event = new Event ();
		event.detail = SWT.COMPOSITION_CHANGED;
		event.start = startOffset;
		event.end = end;
		event.text = text = buffer != null ? new String (buffer) : ""; //$NON-NLS-1$
		sendEvent (SWT.ImeComposition, event);
		if (text.length() == 0) {
			startOffset = -1;
			ranges = null;
			styles = null;
		}
	}
	return LRESULT.ONE;
}

LRESULT WM_IME_COMPOSITION_START (long wParam, long lParam) {
	return isInlineEnabled () ? LRESULT.ONE : null;
}

LRESULT WM_IME_ENDCOMPOSITION (long wParam, long lParam) {
	// Reset defaults. Otherwise the next composition overwrites the previous one.
	startOffset = -1;
	caretOffset = 0;
	return isInlineEnabled () ? LRESULT.ONE : null;
}

LRESULT WM_KEYDOWN (long wParam, long lParam) {
	if (wParam == OS.VK_HANJA) {
		long hKL = OS.GetKeyboardLayout (0);
		short langID = (short)OS.LOWORD (hKL);
		if (OS.PRIMARYLANGID (langID) == OS.LANG_KOREAN) {
			Event event = new Event ();
			event.detail = SWT.COMPOSITION_SELECTION;
			sendEvent (SWT.ImeComposition, event);
			if (event.start == event.end) {
				event.text = null;
				event.end = event.start + 1;
				sendEvent (SWT.ImeComposition, event);
			}
			if (event.text != null && event.text.length() > 0) {
				int length = event.text.length();
				if (length > 1) {
					event.end = event.start + 1;
				}
				long hwnd = parent.handle;
				long hIMC = OS.ImmGetContext (hwnd);
				TCHAR buffer = new TCHAR (0, event.text, true);
				long rc = OS.ImmEscape(hKL, hIMC, OS.IME_ESC_HANJA_MODE, buffer);
				if (rc != 0) {
					sendEvent (SWT.ImeComposition, event);
				}
			}
		}
	}
	return null;
}

LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	if (!isInlineEnabled ()) return null;
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	if (hIMC != 0) {
		if (OS.ImmGetOpenStatus (hIMC)) {
			OS.ImmNotifyIME (hIMC, OS.NI_COMPOSITIONSTR, OS.CPS_COMPLETE, 0);
		}
		OS.ImmReleaseContext (hwnd, hIMC);
	}
	return null;
}

LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	if (!isInlineEnabled ()) return null;
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	if (hIMC != 0) {
		if (OS.ImmGetOpenStatus (hIMC)) {
			if (OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, (char [])null, 0) > 0) {
				Event event = new Event ();
				event.detail = SWT.COMPOSITION_OFFSET;
				event.setLocationInPixels(OS.GET_X_LPARAM (lParam), OS.GET_Y_LPARAM (lParam));
				sendEvent (SWT.ImeComposition, event);
				int offset = event.index;
				int length = text.length();
				if (offset != -1 && startOffset != -1 && startOffset <= offset && offset < startOffset + length) {
					long imeWnd = OS.ImmGetDefaultIMEWnd (hwnd);
					offset = event.index + event.count - startOffset;
					int trailing = event.count > 0 ? 1 : 2;
					long param = OS.MAKEWPARAM (OS.MAKEWORD (OS.IMEMOUSE_LDOWN, trailing), offset);
					OS.SendMessage (imeWnd, WM_MSIME_MOUSE, param, hIMC);
				} else {
					OS.ImmNotifyIME (hIMC, OS.NI_COMPOSITIONSTR, OS.CPS_COMPLETE, 0);
				}
			}
		}
		OS.ImmReleaseContext (hwnd, hIMC);
	}
	return null;
}

}

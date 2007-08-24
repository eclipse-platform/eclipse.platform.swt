/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 */

public class Canvas extends Composite {
	Caret caret;
	
	static final int WM_MSIME_MOUSE = OS.RegisterWindowMessage (new TCHAR (0, "MSIMEMouseOperation", true));
	
	static final byte [] IID_ITfInputProcessorProfiles = new byte [16];
	static final byte [] IID_ITfDisplayAttributeProvider = new byte [16];
	static final byte [] CLSID_TF_InputProcessorProfiles = new byte [16];
	static final byte [] GUID_TFCAT_TIP_KEYBOARD = new byte [16];
	static {
		OS.IIDFromString ("{1F02B6C5-7842-4EE6-8A0B-9A24183A95CA}\0".toCharArray (), IID_ITfInputProcessorProfiles);
		OS.IIDFromString ("{fee47777-163c-4769-996a-6e9c50ad8f54}\0".toCharArray (), IID_ITfDisplayAttributeProvider);
		OS.IIDFromString ("{33C53A50-F456-4884-B049-85FD643ECFED}\0".toCharArray (), CLSID_TF_InputProcessorProfiles);
		OS.IIDFromString ("{34745C63-B2F0-4784-8B67-5E12C8701A31}\0".toCharArray (), GUID_TFCAT_TIP_KEYBOARD);
	}
	
	/* TextLayout has a copy of these constants */
	static final int UNDERLINE_IME_DOT = 1 << 16;
	static final int UNDERLINE_IME_DASH = 2 << 16;
	static final int UNDERLINE_IME_THICK = 3 << 16;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Canvas () {
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

void clearArea (int x, int y, int width, int height) {
	checkWidget ();
	if (OS.IsWindowVisible (handle)) {
		RECT rect = new RECT ();
		OS.SetRect (rect, x, y, x + width, y + height);
		int /*long*/ hDC = OS.GetDCEx (handle, 0, OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS);
		drawBackground (hDC, rect);
		OS.ReleaseDC (handle, hDC);
	}
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget ();
	return caret;
}

TF_DISPLAYATTRIBUTE getDisplayAttribute (short langid, int attInfo) {
	int /*long*/ [] pProfiles = new int /*long*/ [1];
	int hr = OS.CoCreateInstance (CLSID_TF_InputProcessorProfiles, 0, OS.CLSCTX_INPROC_SERVER, IID_ITfInputProcessorProfiles, pProfiles);
	TF_DISPLAYATTRIBUTE pda = null;
	if (hr == OS.S_OK) {
		byte [] pclsid = new byte [16];
		byte [] pguidProfile = new byte [16];
		/* pProfiles.GetDefaultLanguageProfile () */
		hr = OS.VtblCall (8, pProfiles [0], langid, GUID_TFCAT_TIP_KEYBOARD, pclsid, pguidProfile);
		if (hr == OS.S_OK) {
			int /*long*/ [] pProvider = new int /*long*/ [1];
			hr = OS.CoCreateInstance (pclsid, 0, OS.CLSCTX_INPROC_SERVER, IID_ITfDisplayAttributeProvider, pProvider);
			if (hr == OS.S_OK) {
				int /*long*/ [] pEnum = new int /*long*/ [1];
				/* pProvider.EnumDisplayAttributeInfo () */
				hr = OS.VtblCall (3, pProvider [0], pEnum);
				if (hr == OS.S_OK) {
					int /*long*/ [] pDispInfo = new int /*long*/ [1];
					TF_DISPLAYATTRIBUTE tempPda = new TF_DISPLAYATTRIBUTE ();
					/* pEnum.Next () */
					while ((hr = OS.VtblCall (4, pEnum [0], 1, pDispInfo, null)) == OS.S_OK) {
						/* pDispInfo.GetAttributeInfo(); */
						OS.VtblCall (5, pDispInfo [0], tempPda);
						/* pDispInfo.Release () */
						OS.VtblCall (2, pDispInfo [0]);
						if (tempPda.bAttr == attInfo) {
							pda = tempPda;
							break;
						}
					}
					/* pEnum.Release () */
					hr = OS.VtblCall (2, pEnum [0]);
				}
				/* pProvider.Release () */ 
				hr = OS.VtblCall (2, pProvider [0]);
			}
		}
		/* pProfiles.Release () */
		hr = OS.VtblCall (2, pProfiles [0]);
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

boolean isInlineIMEEnabled () {
	if (OS.IsWinCE || OS.WIN32_VERSION < OS.VERSION (5, 1)) return false;
	return OS.IsDBLocale && hooks (SWT.ImeComposition);
}

void releaseChildren (boolean destroy) {
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	super.releaseChildren (destroy);
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void drawBackground (GC gc, int x, int y, int width, int height) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int /*long*/ hDC = gc.handle;
	int pixel = background == -1 ? gc.getBackground ().handle : -1;
	drawBackground (hDC, rect, pixel);
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget ();
	forceResize ();
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	RECT sourceRect = new RECT ();
	OS.SetRect (sourceRect, x, y, x + width, y + height);
	RECT clientRect = new RECT ();
	OS.GetClientRect (handle, clientRect);
	if (OS.IntersectRect (clientRect, sourceRect, clientRect)) {
		if (OS.IsWinCE) {
			OS.UpdateWindow (handle);
		} else {
			int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, null, 0, flags);
		}
	}
	int deltaX = destX - x, deltaY = destY - y;
	if (findImageControl () != null) {
		if (OS.IsWinCE) {
			OS.InvalidateRect (handle, sourceRect, true);
		} else {
			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
			if (all) flags |= OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, sourceRect, 0, flags);
		}
		OS.OffsetRect (sourceRect, deltaX, deltaY);
		if (OS.IsWinCE) {
			OS.InvalidateRect (handle, sourceRect, true);
		} else {
			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
			if (all) flags |= OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, sourceRect, 0, flags);
		}
	} else {
		int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
		/*
		* Feature in Windows.  If any child in the widget tree partially
		* intersects the scrolling rectangle, Windows moves the child
		* and copies the bits that intersect the scrolling rectangle but
		* does not redraw the child.
		* 
		* Feature in Windows.  When any child in the widget tree does not
		* intersect the scrolling rectangle but the parent does intersect,
		* Windows does not move the child.  This is the documented (but
		* strange) Windows behavior.
		* 
		* The fix is to not use SW_SCROLLCHILDREN and move the children
		* explicitly after scrolling.  
		*/
//		if (all) flags |= OS.SW_SCROLLCHILDREN;
		OS.ScrollWindowEx (handle, deltaX, deltaY, sourceRect, null, 0, null, flags);
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			Rectangle rect = child.getBounds ();
			if (Math.min (x + width, rect.x + rect.width) >= Math.max (x, rect.x) && 
				Math.min (y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocation (rect.x + deltaX, rect.y + deltaY);
			}
		}
	}
	if (isFocus) caret.setFocus ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget ();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

int /*long*/ windowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (msg == Display.SWT_RESTORECARET) {
		if ((state & CANVAS) != 0) {
			if (caret != null) {
				caret.killFocus ();
				caret.setFocus ();
				return 1;
			}
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_IME_COMPOSITION (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_IME_COMPOSITION (wParam, lParam);
	if (isInlineIMEEnabled ()) {
		int /*long*/ hIMC = OS.ImmGetContext (handle);
		if (hIMC != 0) {
			TCHAR buffer = null;
			if ((lParam & OS.GCS_RESULTSTR) != 0) {
				int length = OS.ImmGetCompositionString (hIMC, OS.GCS_RESULTSTR, null, 0);
				if (length > 0) {
					buffer = new TCHAR (getCodePage (), length / TCHAR.sizeof);
					OS.ImmGetCompositionString (hIMC, OS.GCS_RESULTSTR, buffer, length);
					String text = buffer.toString (); 
					Event event = new Event ();
					event.detail = SWT.COMPOSITION_CHANGED;
					event.text = text;
					event.count = text.length ();
					sendEvent (SWT.ImeComposition, event);
					if (event.doit) {
						Display display = this.display;
						display.lastKey = 0;
						display.lastVirtual = display.lastNull = display.lastDead = false;
						length = text.length ();
						for (int i = 0; i < length; i++) {
							char c = text.charAt (i);
							display.lastAscii = c;
							event = new Event ();
							event.character = c;
							sendEvent (SWT.KeyDown, event);
						}
					}
				}
				if ((lParam & OS.GCS_COMPSTR) == 0) return LRESULT.ONE;
			}
			
			int index = 0;
			int [] ranges = null;
			TextStyle [] styles = null;
			int /*long*/ layout = OS.GetKeyboardLayout (0);
			short langID = (short)OS.LOWORD (layout);
			if ((lParam & OS.GCS_COMPSTR) != 0) {
				int length = OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, null, 0);
				if (length > 0) {
					buffer = new TCHAR (getCodePage (), length / TCHAR.sizeof);
					OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, buffer, length);
					if ((lParam & OS.GCS_CURSORPOS) != 0) {
						index = OS.ImmGetCompositionString (hIMC, OS.GCS_CURSORPOS, null, 0);
					}
					int [] clauses = null;
					if ((lParam & OS.GCS_COMPCLAUSE) != 0) {
						length = OS.ImmGetCompositionStringW (hIMC, OS.GCS_COMPCLAUSE, (int [])null, 0);
						if (length > 0) {
							clauses = new int [length / 4];
							OS.ImmGetCompositionStringW (hIMC, OS.GCS_COMPCLAUSE, clauses, length);
						}
					}
					if ((lParam & OS.GCS_COMPATTR) != 0 && clauses != null) {
						length = OS.ImmGetCompositionStringA (hIMC, OS.GCS_COMPATTR, (byte [])null, 0);
						if (length > 0) {
							byte [] attrs = new byte [length];
							OS.ImmGetCompositionStringA (hIMC, OS.GCS_COMPATTR, attrs, length);
							length = clauses.length - 1;
							ranges = new int [length * 2];
							styles = new TextStyle [length];
							TF_DISPLAYATTRIBUTE attr = null; 
							TextStyle style = null;
							for (int i = 0; i < length; i++) {
								ranges [i * 2] = clauses [i];
								ranges [i * 2 + 1] = clauses [i + 1] - clauses [i];
								styles [i] = style = new TextStyle ();
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
											style.underlineStyle = SWT.UNDERLINE_ERROR;
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
				OS.ImmReleaseContext (handle, hIMC);
			}
			String text = buffer != null ? buffer.toString () : "";
			Event event = new Event ();
			event.detail = SWT.COMPOSITION_CHANGED;
			event.text = text;
			event.index = index;
			event.count = 0;
			event.ranges = ranges;
			event.styles = styles;
			event.wideCaret = OS.PRIMARYLANGID (langID) == OS.LANG_KOREAN; 
			sendEvent (SWT.ImeComposition, event);
		}
		return LRESULT.ONE;
	} else {
		/*
		* Bug in Windows.  On Korean Windows XP, the IME window
		* for the Korean Input System (MS-IME 2002) always opens 
		* in the top left corner of the screen, despite the fact
		* that ImmSetCompositionWindow() was called to position
		* the IME when focus is gained.  The fix is to position
		* the IME on every WM_IME_COMPOSITION message.
		*/
		if (!OS.IsWinCE && OS.WIN32_VERSION == OS.VERSION (5, 1)) {
			if (OS.IsDBLocale) {
				short langID = OS.GetSystemDefaultUILanguage ();
				short primaryLang = OS.PRIMARYLANGID (langID);
				if (primaryLang == OS.LANG_KOREAN) {
					if (caret != null && caret.isFocusCaret ()) {
						POINT ptCurrentPos = new POINT ();
						if (OS.GetCaretPos (ptCurrentPos)) {
							COMPOSITIONFORM lpCompForm = new COMPOSITIONFORM ();
							lpCompForm.dwStyle = OS.CFS_POINT;
							lpCompForm.x = ptCurrentPos.x;
							lpCompForm.y = ptCurrentPos.y;
							int /*long*/ hIMC = OS.ImmGetContext (handle);
							OS.ImmSetCompositionWindow (hIMC, lpCompForm);
							OS.ImmReleaseContext (handle, hIMC);
						}
					}
				}
			}
		}
	}
	return result;
}

LRESULT WM_IME_COMPOSITION_START (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_IME_COMPOSITION_START (wParam, lParam);
	if (isInlineIMEEnabled ()) {
		return LRESULT.ONE;
	}
	return result;
}

LRESULT WM_IME_ENDCOMPOSITION (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_IME_ENDCOMPOSITION (wParam, lParam);
	if (isInlineIMEEnabled ()) {
		return LRESULT.ONE;
	}
	return result;
}

LRESULT WM_INPUTLANGCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_INPUTLANGCHANGE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) {
		caret.setIMEFont ();
		caret.resizeIME ();
	}
	return result;
}

LRESULT WM_KILLFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	if (isInlineIMEEnabled ()) {
		int /*long*/ hIMC = OS.ImmGetContext (handle);
		if (hIMC != 0) {
			if (OS.ImmGetOpenStatus (hIMC)) {
				OS.ImmNotifyIME (hIMC, OS.NI_COMPOSITIONSTR, OS.CPS_COMPLETE, 0);
			}
			OS.ImmReleaseContext (handle, hIMC);
		}
	}
	LRESULT result  = super.WM_KILLFOCUS (wParam, lParam);
	if (caret != null) caret.killFocus ();
	return result;
}

LRESULT WM_LBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	if (isInlineIMEEnabled ()) {
		int /*long*/ hIMC = OS.ImmGetContext (handle);
		if (hIMC != 0) {
			if (OS.ImmGetOpenStatus (hIMC)) {
				int length = OS.ImmGetCompositionString (hIMC, OS.GCS_COMPSTR, null, 0);
				if (length > 0) {
					Event event = new Event ();
					event.detail = SWT.COMPOSITION_HITTEST;
					event.x = OS.GET_X_LPARAM (lParam); 
					event.y = OS.GET_Y_LPARAM (lParam);
					sendEvent (SWT.ImeComposition, event);
					if (event.hitTest == SWT.HITTEST_INSIDE_COMPOSITION) {
						int /*long*/ imeWnd = OS.ImmGetDefaultIMEWnd (handle);
						int action = OS.IMEMOUSE_LDOWN;
						int offset = event.index + event.trailing;
						int trailing = event.trailing > 0 ? 1 : 2;
						int w = ((action & 0xFF) | (trailing & 0xFF) << 8) | ((offset & 0xFFFF) << 16);
						OS.SendMessage (imeWnd, WM_MSIME_MOUSE, w, hIMC);
					} else {
						OS.ImmNotifyIME (hIMC, OS.NI_COMPOSITIONSTR, OS.CPS_COMPLETE, 0);
					}
				}
			}
			OS.ImmReleaseContext (handle, hIMC);
		}
	}
	return super.WM_LBUTTONDOWN (wParam, lParam);
}

LRESULT WM_SETFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_SETFOCUS (wParam, lParam);
	if (caret != null) caret.setFocus ();
	return result;
}

LRESULT WM_SIZE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_SIZE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) caret.resizeIME ();
	return result;
}

LRESULT WM_WINDOWPOSCHANGED (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGED (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.setFocus ();
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	return result;
}

}

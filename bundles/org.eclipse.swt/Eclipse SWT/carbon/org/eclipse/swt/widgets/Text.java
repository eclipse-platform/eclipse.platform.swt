/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.FontInfo;
import org.eclipse.swt.internal.carbon.RGBColor;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.EventRecord;
import org.eclipse.swt.internal.carbon.TXNBackground;
import org.eclipse.swt.internal.carbon.TXNLongRect;
import org.eclipse.swt.internal.carbon.ControlEditTextSelectionRec;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.HIThemeTextInfo;
import org.eclipse.swt.internal.carbon.TXNTab;
import org.eclipse.swt.internal.carbon.CGPoint;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, LEFT, MULTI, PASSWORD, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified. 
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Text extends Scrollable {
	int txnObject, txnFrameID;
	int textLimit = LIMIT, tabs = 8;
	char echoCharacter;
	boolean doubleClick;
	String hiddenText;
	ControlEditTextSelectionRec selection;
	/**
	* The maximum number of characters that can be entered
	* into a text widget.
	* <p>
	* Note that this value is platform dependent, based upon
	* the native widget implementation.
	* </p>
	*/
	public static final int LIMIT;
	/**
	* The delimiter used by multi-line text widgets.  When text
	* is queried and from the widget, it will be delimited using
	* this delimiter.
	*/
	public static final String DELIMITER;
	static final char PASSWORD = '\u2022';

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\r";
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
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #removeVerifyListener
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

/**
 * Appends a string.
 * <p>
 * The new text is appended to the text at
 * the end of the widget.
 * </p>
 *
 * @param string the string to be appended
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int charCount = getCharCount ();
		string = verifyText (string, charCount, charCount, null);
		if (string == null) return;
	}
	if (txnObject == 0) {
		setSelection (getCharCount ());
		insertEditText (string);
	} else {
		setTXNText (OS.kTXNEndOffset, OS.kTXNEndOffset, string);
		OS.TXNSetSelection (txnObject, OS.kTXNEndOffset, OS.kTXNEndOffset);
		OS.TXNShowSelection (txnObject, false);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
}

static int checkStyle (int style) {
	if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
		style &= ~SWT.MULTI;
	}
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) {
		style |= SWT.MULTI;
		style &= ~SWT.H_SCROLL;
	}
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	if (OS.HIVIEW) {
		short [] part = new short [1];
		if (txnObject == 0) {
			OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
			if (part [0] == OS.kControlFocusNoPart) {
				selection = new ControlEditTextSelectionRec ();
				OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null);
			}
		}
		int result = super.callFocusEventHandler (nextHandler, theEvent);
		if (txnObject == 0) {
			if (part [0] != OS.kControlFocusNoPart && selection != null) {
				OS.SetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
				selection = null;
			}
		}
		return result;
	}
	return OS.noErr;
}

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget();
	if (txnObject == 0) {
		Point selection = getSelection ();
		setSelection (selection.x);
	} else {
		int [] oStartOffset = new int [1], oEndOffset = new int [1];
		OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
		OS.TXNSetSelection (txnObject, oStartOffset [0], oStartOffset [0]);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if (txnObject == 0) {
		Rect rect = new Rect ();
		OS.GetBestControlRect (handle, rect, null);
		width = rect.right - rect.left;
		height = rect.bottom - rect.top;
	} else {
		if (OS.VERSION >= 0x1030) {
			int [] oDataHandle = new int [1];
			OS.TXNGetData (txnObject, OS.kTXNStartOffset, OS.kTXNEndOffset, oDataHandle);
			if (oDataHandle [0] != 0) {
				int length = OS.GetHandleSize (oDataHandle [0]);
				if (length != 0) {
					int [] ptr = new int [1];
					OS.HLock (oDataHandle [0]);
					OS.memcpy (ptr, oDataHandle [0], 4);
					int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, ptr [0], length / 2);
					if (str != 0) {
						float [] w = new float [1], h = new float [1];
						HIThemeTextInfo info = new HIThemeTextInfo ();
						info.state = OS.kThemeStateActive;
						if (font != null) {
							OS.TextFont (font.id);
							OS.TextFace (font.style);
							OS.TextSize (font.size);
							info.fontID = (short) OS.kThemeCurrentPortFont; 
						} else {
							info.fontID = (short) defaultThemeFont ();
						}
						OS.HIThemeGetTextDimensions (str, wHint == SWT.DEFAULT ? 0 : wHint, info, w, h, null);
						OS.CFRelease (str);
						width = (int) w [0];
						height = (int) h [0];
					}
					OS.HUnlock (oDataHandle[0]);
				} else {
					Font font = getFont ();
					FontInfo info = new FontInfo ();
					OS.FetchFontInfo (font.id, font.size, font.style, info);
					int fontHeight = info.ascent + info.descent + info.leading;
					height = fontHeight;
				}
				OS.DisposeHandle (oDataHandle[0]);
			}
		} else {
			TXNLongRect oTextRect = new TXNLongRect ();
			OS.TXNGetRectBounds (txnObject, null, null, oTextRect);
			width = oTextRect.right - oTextRect.left;
			height = oTextRect.bottom - oTextRect.top;
		}
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	if (height <= 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;
	height = trim.height;
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	if (!OS.HIVIEW) {
		int ptr = OS.NewPtr (Rect.sizeof);
		OS.TXNGetTXNObjectControls (txnObject, 1, new int [] {OS.kTXNMarginsTag}, new int [] {ptr});
		Rect rect = new Rect ();
		OS.memcpy (rect, ptr, Rect.sizeof);
		OS.DisposePtr (ptr);
		width += rect.left + rect.right;
		height += rect.top + rect.bottom;
	}
	int [] size = new int [1];
	OS.GetThemeMetric(OS.kThemeMetricScrollBarWidth, size);
	if (horizontalBar != null) height += size [0];
	if (verticalBar != null) width += size [0];
	Rect inset = inset ();
	x -= inset.left;
	y -= inset.top;
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	if (txnObject == 0) {
		inset = getInset ();
		x -= inset.left;
		y -= inset.top;
		width += inset.left + inset.right;
		height += inset.top + inset.bottom;
	}
	return new Rectangle (x, y, width, height);
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy () {
	checkWidget ();
	if (txnObject == 0) {
		Point selection = getSelection ();
		if (selection.x == selection.y) return;
		copy (getEditText (selection.x, selection.y - 1));	
	} else {
		OS.TXNCopy (txnObject);
	}
}

void copy (char [] buffer) {
	if (buffer.length == 0) return;
	OS.ClearCurrentScrap ();
	int [] scrap = new int [1];
	OS.GetCurrentScrap (scrap);
	OS.PutScrapFlavor (scrap [0], OS.kScrapFlavorTypeUnicode, 0, buffer.length * 2, buffer);
}

void createHandle () {
	if (OS.HIVIEW) {
		int [] outControl = new int [1];
		if ((style & SWT.MULTI) != 0 || ((style & SWT.BORDER) == 0)) {
			if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
				int options = 0;
				if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == (SWT.H_SCROLL | SWT.V_SCROLL)) options |= OS.kHIScrollViewOptionsAllowGrow;
				if ((style & SWT.H_SCROLL) != 0) options |= OS.kHIScrollViewOptionsHorizScroll;
				if ((style & SWT.V_SCROLL) != 0) options |= OS.kHIScrollViewOptionsVertScroll;
				OS.HIScrollViewCreate (options, outControl);
				if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
				scrolledHandle = outControl [0];
				OS.HIViewSetVisible (scrolledHandle, true);
			}
			int iFrameOptions = OS.kTXNDontDrawCaretWhenInactiveMask | OS.kTXNMonostyledTextMask;
			if ((style & SWT.SINGLE) != 0) iFrameOptions |= OS.kTXNSingleLineOnlyMask;
			if ((style & SWT.WRAP) != 0) iFrameOptions |= OS.kTXNAlwaysWrapAtViewEdgeMask;
			OS.HITextViewCreate (null, 0, iFrameOptions, outControl);
			if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
			handle = outControl [0];
			OS.HIViewSetVisible (handle, true);
			txnObject = OS.HITextViewGetTXNObject (handle);			
			int ptr = OS.NewPtr (Rect.sizeof);
			Rect rect = inset ();
			OS.memcpy (ptr, rect, Rect.sizeof);
			int [] tags = new int [] {
				OS.kTXNDisableDragAndDropTag,
				OS.kTXNDoFontSubstitution,
				OS.kTXNIOPrivilegesTag,
				OS.kTXNMarginsTag,
				OS.kTXNJustificationTag,
			};
			int just = OS.kTXNFlushLeft;
			if ((style & SWT.CENTER) != 0) just = OS.kTXNCenter;
			if ((style & SWT.RIGHT) != 0) just = OS.kTXNFlushRight;
			int [] datas = new int [] {
				1,
				1,
				(style & SWT.READ_ONLY) != 0 ? 1 : 0,
				ptr,
				just,
			};
			OS.TXNSetTXNObjectControls (txnObject, false, tags.length, tags, datas);
			OS.DisposePtr (ptr);
		} else {
			int window = OS.GetControlOwner (parent.handle);
			OS.CreateEditUnicodeTextControl (window, null, 0, (style & SWT.PASSWORD) != 0, null, outControl);
			if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
			handle = outControl [0];
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSingleLineTag, 1, new byte [] {1});
			if ((style & SWT.READ_ONLY) != 0) {
				OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextLockedTag, 1, new byte [] {1});
			}
			if ((style & (SWT.RIGHT | SWT.CENTER)) != 0) {
				ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
				fontStyle.flags |= OS.kControlUseJustMask;
				if ((style & SWT.CENTER) != 0) fontStyle.just = OS.teJustCenter;
				if ((style & SWT.RIGHT) != 0) fontStyle.just = OS.teJustRight;
				OS.SetControlFontStyle (handle, fontStyle);
			}
		}		
	} else {
		int features = OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
		int [] outControl = new int [1];
		int window = OS.GetControlOwner (parent.handle);
		OS.CreateUserPaneControl (window, null, features, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		
		/*
		* Feature in the Macintosh.  The TXNObject is not a control but creates scroll
		* bar controls to scroll the text.  These are created in the root and are not
		* children of the user pane that is used to represent the TNXObject.  The fix
		* is to embed the scroll bars in the user pane.
		*/
		int [] theRoot = new int [1];
		OS.GetRootControl (window, theRoot);
		short [] oldCount = new short [1];
		OS.CountSubControls (theRoot [0], oldCount);	
		
		/* Create the TXNObject */
		int iFrameOptions = OS.kTXNDontDrawCaretWhenInactiveMask | OS.kTXNMonostyledTextMask;
		if ((style & SWT.H_SCROLL) != 0) iFrameOptions |= OS.kTXNWantHScrollBarMask;
		if ((style & SWT.V_SCROLL) != 0) iFrameOptions |= OS.kTXNWantVScrollBarMask;
		if ((style & SWT.SINGLE) != 0) iFrameOptions |= OS.kTXNSingleLineOnlyMask;
		if ((style & SWT.WRAP) != 0) iFrameOptions |= OS.kTXNAlwaysWrapAtViewEdgeMask;
		int [] oTXNObject = new int [1], oTXNFrameID = new int[1];
		OS.TXNNewObject (0, window, null, iFrameOptions, OS.kTXNTextEditStyleFrameType, OS.kTXNUnicodeTextFile, OS.kTXNSystemDefaultEncoding, oTXNObject, oTXNFrameID, 0);
		if (oTXNObject [0] == 0) error (SWT.ERROR_NO_HANDLES);
		txnObject = oTXNObject [0];
		txnFrameID = oTXNFrameID [0];
		
		/* Embed the scroll bars in the user pane */
		short [] newCount = new short [1];
		OS.CountSubControls (theRoot [0], newCount);
		int [] scrollBar = new int [1];
		for (int i=newCount [0]; i>oldCount [0]; --i) {
			OS.GetIndexedSubControl (theRoot [0], (short) i, scrollBar);
			OS.HIViewRemoveFromSuperview (scrollBar [0]);
			OS.HIViewAddSubview (handle, scrollBar [0]);
		}
		
		/* Configure the TXNObject */
		int ptr = OS.NewPtr (Rect.sizeof);
		Rect rect = new Rect ();
		if (hasBorder ()) {
			OS.SetRect (rect, (short) 1, (short) 1, (short) 1, (short) 1);
		}
		OS.memcpy (ptr, rect, Rect.sizeof);
		int [] tags = new int [] {
			OS.kTXNDisableDragAndDropTag,
			OS.kTXNIOPrivilegesTag,
			OS.kTXNMarginsTag,
			OS.kTXNJustificationTag,
			OS.kTXNDoFontSubstitution,
		};
		int just = OS.kTXNFlushLeft;
		if ((style & SWT.CENTER) != 0) just = OS.kTXNCenter;
		if ((style & SWT.RIGHT) != 0) just = OS.kTXNFlushRight;
		int [] datas = new int [] {
			1,
			(style & SWT.READ_ONLY) != 0 ? 1 : 0,
			ptr,
			just,
			1,
		};
		OS.TXNSetTXNObjectControls (txnObject, false, tags.length, tags, datas);
		OS.TXNSetFrameBounds (txnObject, 0, 0, 0, 0, txnFrameID);
		OS.DisposePtr (ptr);
	
		/*
		* Bug in the Macintosh.  The caret height is too small until some text is set in the
		* TXNObject.  The fix is to temporary change the text.
		*/
		char [] buffer = new char [] {' '};
		OS.TXNSetData (txnObject, OS.kTXNUnicodeTextData, buffer, 2, OS.kTXNStartOffset, OS.kTXNEndOffset);
		OS.TXNSetData (txnObject, OS.kTXNUnicodeTextData, buffer, 0, OS.kTXNStartOffset, OS.kTXNEndOffset);

	}
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	if ((style & SWT.PASSWORD) != 0) setEchoChar (PASSWORD);
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget();
	if ((style & SWT.READ_ONLY) != 0) return;
	boolean cut = true;
	char [] oldText = null;
	Point oldSelection = getSelection ();
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		if (oldSelection.x != oldSelection.y) {
			oldText = getEditText (oldSelection.x, oldSelection.y - 1);
			String newText = verifyText ("", oldSelection.x, oldSelection.y, null);
			if (newText == null) return;
			if (newText.length () != 0) {
				if (txnObject == 0) {
					insertEditText (newText);
				} else {
					setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, newText);
					OS.TXNShowSelection (txnObject, false);
				}
				cut = false;
			}
		}
	}
	if (cut) {
		if (txnObject == 0) {
			if (oldText == null) oldText = getEditText (oldSelection.x, oldSelection.y - 1);
			copy (oldText);
			insertEditText ("");
		} else {
			OS.TXNCut (txnObject);
	
			/*
			* Feature in the Macintosh.  When an empty string is set in the TXNObject,
			* the font attributes are cleared.  The fix is to reset them.
			*/
			if (OS.TXNDataSize (txnObject) / 2 == 0) setFontStyle (font);
		}
	}
	Point newSelection = getSelection ();
	if (!cut || !oldSelection.equals (newSelection)) sendEvent (SWT.Modify);
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
}

void drawBackground (int control, int context) {
	if (!OS.HIVIEW) {
		parent.drawFocus (control, context, hasFocus () && drawFocusRing (), hasBorder (), inset ());
	}
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if (!OS.HIVIEW) {
		OS.TXNDraw (txnObject, 0);
	}
	super.drawWidget (control, context, damageRgn, visibleRgn, theEvent);
}

/**
 * Returns the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
    return (getTopPixel () + getCaretLocation ().y) / getLineHeight ();
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
 * <p>
 * The location of the caret is returned.
 * </p>
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getCaretLocation () {
	checkWidget();
	if (txnObject == 0) {
		//TODO - caret location for unicode text
		return new Point (0, 0);
	}
	org.eclipse.swt.internal.carbon.Point oPoint = new org.eclipse.swt.internal.carbon.Point ();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	OS.TXNOffsetToPoint (txnObject, oStartOffset [0], oPoint);
	Rect oViewRect = new Rect ();
	OS.TXNGetViewRect (txnObject, oViewRect);
	return new Point (oPoint.h - oViewRect.left, oPoint.v - oViewRect.top);
}

/**
 * Returns the character position of the caret.
 * <p>
 * Indexing is zero based.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretPosition () {
	checkWidget();
	if (txnObject == 0) {
		return getSelection ().x;
	}
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	return oStartOffset [0];
}

/**
 * Returns the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget ();
	if (txnObject == 0) {
		return getText ().length ();
	}
	return OS.TXNDataSize (txnObject) / 2;
}

String getClipboardText () {
	int [] scrap = new int [1];
	OS.GetCurrentScrap (scrap);
	int [] size = new int [1];
	if (OS.GetScrapFlavorSize (scrap [0], OS.kScrapFlavorTypeUnicode, size) != OS.noErr || size [0] == 0) return "";
	char [] buffer = new char [size [0] / 2];
	if (OS.GetScrapFlavorData (scrap [0], OS.kScrapFlavorTypeUnicode, size, buffer) != OS.noErr) return "";
	return new String (buffer);
}

/**
 * Returns the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @return whether or not double click is enabled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget();
    return doubleClick;
}

/**
 * Returns the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 * 
 * @return the echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setEchoChar
 */
public char getEchoChar () {
	checkWidget();
	return echoCharacter;
}

/**
 * Returns the editable state.
 *
 * @return whether or not the receiver is editable
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable () {
	checkWidget();
	return (style & SWT.READ_ONLY) == 0;
}

Rect getInset () {
	if (txnObject != 0) return super.getInset ();
	return display.editTextInset;
}

/**
 * Returns the number of lines.
 *
 * @return the number of lines in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 1;
	int [] oLineTotal = new int [1];
	OS.TXNGetLineCount (txnObject, oLineTotal);
	return oLineTotal [0];
}

/**
 * Returns the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #DELIMITER
 */
public String getLineDelimiter () {
	checkWidget();
	return DELIMITER;
}

/**
 * Returns the height of a line.
 *
 * @return the height of a row of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineHeight () {
	checkWidget();
	if (txnObject == 0) {
		return measureSpace ().v;
	} 
	int [] oLineWidth = new int [1], oLineHeight = new int [1];
	OS.TXNGetLineMetrics (txnObject, 0, oLineWidth, oLineHeight);
	return OS.Fix2Long (oLineHeight [0]);
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selected
 * text, and whose y coordinate is the character position
 * representing the end of the selection. An "empty" selection
 * is indicated by the x and y coordinates having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget();
	if (txnObject == 0) {
		ControlEditTextSelectionRec selection;
		if (this.selection != null) {
			selection = this.selection;
		} else {
			selection = new ControlEditTextSelectionRec ();
			OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null);
		}
		return new Point (selection.selStart, selection.selEnd);
	} else {
		int [] oStartOffset = new int [1], oEndOffset = new int [1];
		OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
		return new Point (oStartOffset [0], oEndOffset [0]);
	}
}

/**
 * Returns the number of selected characters.
 *
 * @return the number of selected characters.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget();
	if (txnObject == 0) {
		Point selection = getSelection ();
		return selection.y - selection.x;
	} else {
		int [] oStartOffset = new int [1], oEndOffset = new int [1];
		OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
		return oEndOffset [0] - oStartOffset [0];
	}
}

/**
 * Gets the selected text, or an empty string if there is no current selection.
 *
 * @return the selected text
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText () {
	checkWidget();
	if (txnObject == 0) {
		Point selection = getSelection ();
		if (selection.x == selection.y) return "";
		return new String (getEditText (selection.x, selection.y - 1));
	} else {
		return getTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection);
	}
}

/**
 * Returns the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @return the number of tab characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTabs () {
	checkWidget();
	return tabs;
}

/**
 * Returns the widget text.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * an empty string if this has never been set.
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
	checkWidget();
	if (txnObject == 0) {
		return new String (getEditText (0, -1));
	} else {
		return getTXNText (OS.kTXNStartOffset, OS.kTXNEndOffset);
	}
}

/**
 * Returns a range of text.  Returns an empty string if the
 * start of the range is greater than the end.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N-1 where N is
 * the number of characters in the widget.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 * @return the range of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int start, int end) {
	checkWidget ();
	if (txnObject == 0) {
		return new String (getEditText (start, end));
	} else {
		if (!(start <= end && 0 <= end)) return "";
		int length = OS.TXNDataSize (txnObject) / 2;
		start = Math.max (0, start);
		end = Math.min (end, length - 1);
		return getTXNText (start, end + 1);
	}
}

char [] getEditText (int start, int end) {
	int [] ptr = new int [1];
	int [] actualSize = new int [1];
	int result = OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	if (result != OS.noErr) return new char [0];
	int length = OS.CFStringGetLength (ptr [0]);
	CFRange range = new CFRange ();
	start = Math.min (Math.max (0, start), length);
	range.location = start;
	if (end == -1) {
		range.length = Math.max (0, length - start);
	} else {
		end = Math.min (Math.max (0, end), length - 1);
		range.length = Math.max (0, end - start + 1);
	}
	char [] buffer = new char [range.length];
	if (hiddenText != null) {
		hiddenText.getChars (range.location, range.location + range.length, buffer, 0);
	} else {
		OS.CFStringGetCharacters (ptr [0], range, buffer);
	}
	OS.CFRelease (ptr [0]);
	return buffer;
}

/**
 * Returns the maximum number of characters that the receiver is capable of holding. 
 * <p>
 * If this has not been changed by <code>setTextLimit()</code>,
 * it will be the constant <code>Text.LIMIT</code>.
 * </p>
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 */
public int getTextLimit () {
	checkWidget();
    return textLimit;
}

/**
 * Returns the zero-relative index of the line which is currently
 * at the top of the receiver.
 * <p>
 * This index can change when lines are scrolled or new lines are added or removed.
 * </p>
 *
 * @return the index of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
    return getTopPixel () / getLineHeight ();
}

/**
 * Returns the top pixel.
 * <p>
 * The top pixel is the pixel position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * pixels instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top pixel changes when the widget is scrolled.
 * The top pixel does not include the widget trimming.
 * </p>
 *
 * @return the pixel position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
	if (OS.VERSION >= 0x1030) {
		CGRect rect = new CGRect ();
		OS.TXNGetHIRect (txnObject, OS.kTXNDestinationRectKey, rect);
		int destY = (int) rect.y;
		OS.TXNGetHIRect (txnObject, OS.kTXNTextRectKey, rect);
		return destY - (int) rect.y;
	} else {
		Rect oViewRect = new Rect ();
		TXNLongRect oDestinationRect = new TXNLongRect ();
		TXNLongRect oTextRect = new TXNLongRect ();
		OS.TXNGetRectBounds (txnObject, oViewRect, oDestinationRect, oTextRect);
		return oDestinationRect.top - oTextRect.top;
	}
}

String getTXNText (int iStartOffset, int iEndOffset) {
	int [] oDataHandle = new int [1];
	OS.TXNGetData (txnObject, iStartOffset, iEndOffset, oDataHandle);
	if (oDataHandle [0] == 0) return "";
	int length = OS.GetHandleSize (oDataHandle [0]);
	if (length == 0) return "";
	int [] ptr = new int [1];
	OS.HLock (oDataHandle [0]);
	OS.memcpy (ptr, oDataHandle [0], 4);
	char [] buffer = new char [length / 2];
	OS.memcpy (buffer, ptr [0], length);
	OS.HUnlock (oDataHandle[0]);
	OS.DisposeHandle (oDataHandle[0]);
	return new String (buffer);
}

Rect inset () {
	if (OS.HIVIEW) {
		if ((style & SWT.SINGLE) != 0 && (style & SWT.BORDER) == 0) {
			Rect rect = new Rect ();
			rect.left = rect.top = rect.right = rect.bottom = 1;
			return rect; 
		}
		return new Rect ();
	}
	Rect rect = new Rect ();
	int [] outMetric = new int [1];
	if (drawFocusRing ()) {
		OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
		rect.left += outMetric [0];
		rect.top += outMetric [0];
		rect.right += outMetric [0];
		rect.bottom += outMetric [0];
	}
	if (hasBorder ()) {
		OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
		rect.left += outMetric [0];
		rect.top += outMetric [0];
		rect.right += outMetric [0];
		rect.bottom += outMetric [0];
	}
	return rect;	
} 

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is <code>null</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		Point selection = getSelection ();
		string = verifyText (string, selection.x, selection.y, null);
		if (string == null) return;
	}
	if (txnObject == 0) {
		insertEditText (string);
	} else {
		setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, string);
		OS.TXNShowSelection (txnObject, false);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
}

void insertEditText (String string) {
	if (hasFocus () && hiddenText == null) {
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextInsertCFStringRefTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
	} else {
		Point selection = getSelection ();
		String oldText = getText ();
		String newText = oldText.substring (0, selection.x) + string + oldText.substring (selection.y);
		setEditText (newText);
		setSelection (selection.x + string.length ());
	}
}

int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlActivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.HIVIEW) {
		OS.TXNFocus (txnObject, hasFocus ());
		OS.TXNActivate (txnObject, txnFrameID, OS.kScrollBarsSyncAlwaysActive);
	}
	return result;
}

int kEventControlBoundsChanged (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlBoundsChanged (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.HIVIEW) {
		int [] attributes = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
		if ((attributes [0] & (OS.kControlBoundsChangePositionChanged | OS.kControlBoundsChangeSizeChanged)) != 0) setTXNBounds ();
	}
	return result;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.HIVIEW) {
		if (!isEnabled ()) return OS.noErr;
		int window = OS.GetControlOwner (handle);
		OS.SetKeyboardFocus (window, handle, (short)OS.kControlFocusNextPart);
		EventRecord iEvent = new EventRecord ();
		OS.ConvertEventRefToEventRecord (theEvent, iEvent);
		OS.TXNClick (txnObject, iEvent);
		return OS.noErr;
	}
	return result;
}

int kEventControlDeactivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlDeactivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.HIVIEW) {
		OS.TXNFocus (txnObject, hasFocus());
		OS.TXNActivate (txnObject, txnFrameID, OS.kScrollBarsSyncWithFocus);
	}
	return result;
}

int kEventControlGetFocusPart (int nextHandler, int theEvent, int userData) {
	if (!OS.HIVIEW) {
		return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!doubleClick) {
		int [] clickCount = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamClickCount, OS.typeUInt32, null, 4, null, clickCount);
		if (clickCount [0] > 1) return OS.noErr;
	}
	return result;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetCursor (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.HIVIEW) {
		OS.TXNAdjustCursor (txnObject, 0);
		return OS.noErr;
	}
	return result;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (!OS.HIVIEW) {
		if (result == OS.noErr) {
			short [] part = new short [1];
			OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
			drawFocusClipped (handle, part [0] != OS.kControlFocusNoPart && drawFocusRing (), hasBorder (), getParentBackground (), inset ());
			OS.TXNDraw (txnObject, 0);
			OS.TXNFocus (txnObject, part [0] != OS.kControlFocusNoPart);
		}
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] modifiers = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	if (modifiers [0] == OS.cmdKey) {
		int [] keyCode = new int [1];
		OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		switch (keyCode [0]) {
			case 7: /* X */
				cut ();
				return OS.noErr;
			case 8: /* C */
				copy ();
				return OS.noErr;
			case 9: /* V */
				paste ();
				return OS.noErr;
		}
	}
	if ((style & SWT.SINGLE) != 0) {
		int [] keyCode = new int [1];
		OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		switch (keyCode [0]) {
			/*
			* Feature in the Macintosh.  Tab and Return characters are inserted into a
			* single line TXN Object.  While this may be correct platform behavior, it is
			* unexpected.  The fix is to avoid calling the default handler. 
			*/
			case 76: /* KP Enter */
			case 36: { /* Return */
				postEvent (SWT.DefaultSelection);
				return OS.noErr;
			}
			case 48: { /* Tab */
				return OS.noErr;
			}
		}
	}
	return result;
}

org.eclipse.swt.internal.carbon.Point measureSpace () {
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, new char[]{' '}, 1);
	org.eclipse.swt.internal.carbon.Point ioBounds = new org.eclipse.swt.internal.carbon.Point ();
	if (font == null) {
		OS.GetThemeTextDimensions (ptr, (short) defaultThemeFont (), OS.kThemeStateActive, false, ioBounds, null);
	} else {
		int [] currentPort = new int [1];
		OS.GetPort (currentPort);
		OS.SetPortWindowPort (OS.GetControlOwner (handle));
		OS.TextFont (font.id);
		OS.TextFace (font.style);
		OS.TextSize (font.size);
		OS.GetThemeTextDimensions (ptr, (short) OS.kThemeCurrentPortFont, OS.kThemeStateActive, false, ioBounds, null);
		OS.SetPort (currentPort [0]);
	}
	OS.CFRelease (ptr);
	return ioBounds;
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste () {
	checkWidget();
	if ((style & SWT.READ_ONLY) != 0) return;
	boolean paste = true;
	String oldText = null;
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		oldText = getClipboardText ();
		if (oldText != null) {
			Point selection = getSelection ();
			String newText = verifyText (oldText, selection.x, selection.y, null);
			if (newText == null) return;
			if (!newText.equals (oldText)) {
				if (txnObject == 0) {
					insertEditText (newText);
				} else {
					setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, newText);
					OS.TXNShowSelection (txnObject, false);
				}
				paste = false;
			}
		}
	}
	if (paste) {
		if (txnObject == 0) {
			if (oldText == null) oldText = getClipboardText ();
			insertEditText (oldText);
		} else {
			OS.TXNPaste (txnObject);
		}
	}
	sendEvent (SWT.Modify);
}

void releaseWidget () {
	super.releaseWidget ();
	if (!OS.HIVIEW) {
		OS.TXNDeleteObject (txnObject);
	}
	txnObject = txnFrameID = 0;
	hiddenText = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #addVerifyListener
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

void resetVisibleRegion (int control) {
	super.resetVisibleRegion (control);
	
	/*
	* Bug in the Macintosh.  For some reason, the TXN object draws when
	* kTXNVisibilityTag is not set causing pixel corruption.  The fix is
	* to make the TXN frame small so that nothing is drawn.
	*/
	if (!OS.HIVIEW) {
		Rect rect = new Rect ();
		OS.GetControlBounds (handle, rect);
		Rect inset = inset ();
		rect.left += inset.left;
		rect.top += inset.top;
		rect.right -= inset.right;
		if (OS.IsControlVisible (handle)) {
			rect.bottom -= inset.bottom;
		} else {
			rect.bottom = rect.top;
		}
		OS.TXNSetFrameBounds (txnObject, rect.top, rect.left, rect.bottom, rect.right, txnFrameID);
	}
}

/**
 * Selects all the text in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget();
	if (txnObject == 0) {
		setSelection (0, getCharCount ());
	} else {
		OS.TXNSelectAll (txnObject);
	}
}

boolean sendKeyEvent (int type, Event event) {
	if (!super.sendKeyEvent (type, event)) {
		return false;
	}
	if (type != SWT.KeyDown) return true;
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (event.character == 0) return true;
	String oldText = "";
	int charCount = getCharCount ();
	Point selection = getSelection ();
	int start = selection.x, end = selection.y;
	switch (event.character) {
		case SWT.BS:
			if (start == end) {
				if (start == 0) return true;
				start = Math.max (0, start - 1);
			}
			break;
		case SWT.DEL:
			if (start == end) {
				if (start == charCount) return true;
				end = Math.min (end + 1, charCount);
			}
			break;
		case SWT.CR:
			if ((style & SWT.SINGLE) != 0) return true;
			oldText = DELIMITER;
			break;
		default:
			if (event.character != '\t' && event.character < 0x20) return true;
			oldText = new String (new char [] {event.character});
	}
	String newText = verifyText (oldText, start, end, event);
	if (newText == null) return false;
	if (charCount - (end - start) + newText.length () > textLimit) {
		return false;
	}
	boolean result = newText == oldText;
	if (newText != oldText || hiddenText != null) {
		if (txnObject == 0) {
			String text = new String (getEditText (0, -1));
			String leftText = text.substring (0, start);
			String rightText = text.substring (end, text.length ());
			setEditText (leftText + newText + rightText);
			start += newText.length ();
			setSelection (new Point (start, start));
			result = false;
		} else {
			setTXNText (start, end, newText);
		}
	}
	/*
	* Post the modify event so that the character will be inserted
	* into the widget when the modify event is delivered.  Normally,
	* modify events are sent but it is safe to post the event here
	* because this method is called from the event loop.
	*/
	postEvent (SWT.Modify);
	return result;
}

void setBackground (float [] color) {
	if (txnObject == 0) {
		super.setBackground (color);
	} else {
		TXNBackground txnColor = new TXNBackground (); 
		txnColor.bgType = OS.kTXNBackgroundTypeRGB;
		int red = (short) (color == null ? 0xff : color [0] * 255);
		int green = (short) (color == null ? 0xff : color [1] * 255);
		int blue = (short) (color == null ? 0xff : color [2] * 255);
		txnColor.bg_red = (short) (red << 8 | red);
		txnColor.bg_green = (short) (green << 8 | green);
		txnColor.bg_blue = (short) (blue << 8 | blue);
		boolean readOnly = (style & SWT.READ_ONLY) != 0;
		int [] tag = new int [] {OS.kTXNIOPrivilegesTag};
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {0});
		OS.TXNSetBackground (txnObject, txnColor);
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {1});
	}
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	Rectangle bounds = null;
	if (OS.HIVIEW && txnObject == 0 && resize) bounds = getBounds ();
	int result = super.setBounds(x, y, width, height, move, resize, events);
	if (bounds != null && (result & RESIZED) != 0) {
		if (bounds.width == 0) showSelection ();
	}
	if (!OS.HIVIEW) {
		if ((result & (RESIZED | MOVED)) != 0) setTXNBounds ();
	}
	return result;
}

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p><p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 * 
 * @param doubleClick the new double click flag
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget();
	this.doubleClick = doubleClick;
}

/**
 * Sets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer. Setting
 * the echo character to '\0' clears the echo
 * character and redraws the original text.
 * If for any reason the echo character is invalid,
 * or if the platform does not allow modification
 * of the echo character, the default echo character
 * for the platform is used.
 * </p>
 *
 * @param echo the new echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEchoChar (char echo) {
	checkWidget();
	if ((style & SWT.MULTI) != 0) return;
	if (txnObject == 0) {
		if ((style & SWT.PASSWORD) == 0) {
			Point selection = getSelection ();
			String text = getText ();
			echoCharacter = echo;
			setEditText (text);
			setSelection (selection);
		}
	} else {
		OS.TXNEchoMode (txnObject, echo, OS.kTextEncodingMacUnicode, echo != '\0');
	}
	echoCharacter = echo;
}

/**
 * Sets the editable state.
 *
 * @param editable the new editable state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEditable (boolean editable) {
	checkWidget();
	if (editable) {
		style &= ~SWT.READ_ONLY;
	} else {
		style |= SWT.READ_ONLY;
	}
	if (txnObject == 0) {
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextLockedTag, 1, new byte [] {(byte) ((style & SWT.READ_ONLY) != 0 ? 1 : 0)});
	} else {
		OS.TXNSetTXNObjectControls (txnObject, false, 1, new int [] {OS.kTXNIOPrivilegesTag}, new int [] {((style & SWT.READ_ONLY) != 0) ? 1 : 0});
	}
}

void setForeground (float [] color) {
	if (txnObject == 0) {
		super.setForeground (color);
	} else {
		int ptr2 = OS.NewPtr (OS.kTXNQDFontColorAttributeSize);
		RGBColor rgb;
		if (color == null) {	
			rgb = new RGBColor ();
		} else {
			rgb = toRGBColor (color);
		}
		OS.memcpy (ptr2, rgb, RGBColor.sizeof);
		int [] attribs = new int [] {
			OS.kTXNQDFontColorAttribute,
			OS.kTXNQDFontColorAttributeSize,
			ptr2,
		};
		int ptr1 = OS.NewPtr (attribs.length * 4);
		OS.memcpy (ptr1, attribs, attribs.length * 4);
		boolean readOnly = (style & SWT.READ_ONLY) != 0;
		int [] tag = new int [] {OS.kTXNIOPrivilegesTag};
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {0});
		OS.TXNSetTypeAttributes (txnObject, attribs.length / 3, ptr1, 0, 0);
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {1});
		OS.DisposePtr (ptr1);
		OS.DisposePtr (ptr2);
	}
}

void setFontStyle (Font font) {
	if (txnObject == 0) {
		super.setFontStyle (font);
	} else {
		int [] attribs = new int [] {
			OS.kTXNQDFontSizeAttribute,
			OS.kTXNQDFontSizeAttributeSize,
			font == null ? OS.kTXNDefaultFontSize : OS.X2Fix (font.size),
			OS.kTXNQDFontStyleAttribute,
			OS.kTXNQDFontStyleAttributeSize,
			font == null ? OS.kTXNDefaultFontStyle : font.style,
			OS.kTXNQDFontFamilyIDAttribute,
			OS.kTXNQDFontFamilyIDAttributeSize,
			font == null ? OS.kTXNDefaultFontName : font.id,
		};
		int ptr = OS.NewPtr (attribs.length * 4);
		OS.memcpy (ptr, attribs, attribs.length * 4);
		boolean readOnly = (style & SWT.READ_ONLY) != 0;
		int [] tag = new int [] {OS.kTXNIOPrivilegesTag};
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {0});
		OS.TXNSetTypeAttributes (txnObject, attribs.length / 3, ptr, 0, 0);
		if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {1});
		OS.DisposePtr (ptr);
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * regular array indexing rules.
 * </p>
 *
 * @param start new caret position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget();
	setSelection (start, start);
}

/**
 * Sets the selection to the range specified
 * by the given start and end indices.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget();
	if (txnObject == 0) {
		int length = getCharCount ();
		ControlEditTextSelectionRec selection = new ControlEditTextSelectionRec ();
		selection.selStart = (short) Math.min (Math.max (Math.min (start, end), 0), length);
		selection.selEnd = (short) Math.min (Math.max (Math.max (start, end), 0), length);
		if (hasFocus ()) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
		} else {
			this.selection = selection;
		}
	} else {
		int length = OS.TXNDataSize (txnObject) / 2;
		int nStart = Math.min (Math.max (Math.min (start, end), 0), length);
		int nEnd = Math.min (Math.max (Math.max (start, end), 0), length);
		OS.TXNSetSelection (txnObject, nStart, nEnd);
		OS.TXNShowSelection (txnObject, false);
	}
}

/**
 * Sets the selection to the range specified
 * by the given point, where the x coordinate
 * represents the start index and the y coordinate
 * represents the end index.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param selection the point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

/**
 * Sets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @param tabs the number of tabs
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabs (int tabs) {
	checkWidget();
	if (this.tabs == tabs) return;
	if (txnObject == 0) return;
	this.tabs = tabs;
	TXNTab tab = new TXNTab ();
	tab.value = (short) (measureSpace ().h * tabs);
	int [] tags = new int [] {OS.kTXNTabSettingsTag};
	int [] datas = new int [1];
	OS.memcpy (datas, tab, TXNTab.sizeof);
	OS.TXNSetTXNObjectControls (txnObject, false, tags.length, tags, datas);
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, 0, getCharCount (), null);
		if (string == null) return;
	}
	if (txnObject == 0) {
		setEditText (string);
	} else {
		setTXNText (OS.kTXNStartOffset, OS.kTXNEndOffset, string);
		OS.TXNSetSelection (txnObject, OS.kTXNStartOffset, OS.kTXNStartOffset);
		OS.TXNShowSelection (txnObject, false);
	}
	sendEvent (SWT.Modify);
}

void setEditText (String string) {
	char [] buffer;
	if ((style & SWT.PASSWORD) == 0 && echoCharacter != '\0') {
		hiddenText = string;
		buffer = new char [hiddenText.length ()];
		for (int i = 0; i < buffer.length; i++) buffer [i] = echoCharacter;
	} else {
		hiddenText = null;
		buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
	}
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
	OS.CFRelease (ptr);
	if (selection != null) selection = null;
}

void setTXNBounds () {
	Rect viewRect = new Rect ();
	OS.TXNGetViewRect (txnObject, viewRect);

	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	Rect inset = inset ();
	rect.left += inset.left;
	rect.top += inset.top;
	rect.right -= inset.right;
	rect.bottom -= inset.bottom;
	OS.TXNSetFrameBounds (txnObject, rect.top, rect.left, rect.bottom, rect.right, txnFrameID);

	/*
	* Bug in the Macintosh.  When the caret is moved,
	* the text widget scrolls to show the new location.
	* This means that the text widget may be scrolled
	* to the left in order to show the caret when the
	* widget is not large enough to show both the caret
	* location and all the text.  Unfortunately, when
	* the widget is resized such that all the text and
	* the caret could be visible, the Macintosh does not
	* scroll the widget back.  The fix is to save the 
	* current selection, set the selection to the start
	* of the text and then restore the selection.  This
	* will cause the widget to recompute the left scroll
	* position.
	*/
	int width = viewRect.left - viewRect.right;
	int height = viewRect.bottom - viewRect.top;
	if (width <= (inset.left + inset.right) && height <= (inset.top + inset.bottom)) {
		int [] oStartOffset = new int [1], oEndOffset = new int [1];
		OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
		OS.TXNSetSelection (txnObject, OS.kTXNStartOffset, OS.kTXNStartOffset);
		OS.TXNShowSelection (txnObject, false);
		OS.TXNSetSelection (txnObject, oStartOffset [0], oEndOffset [0]);
		OS.TXNShowSelection (txnObject, false);
	}
}

void setTXNText (int iStartOffset, int iEndOffset, String string) {
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	boolean readOnly = (style & SWT.READ_ONLY) != 0;
	int [] tag = new int [] {OS.kTXNIOPrivilegesTag};
	if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {0});
	OS.TXNSetData (txnObject, OS.kTXNUnicodeTextData, buffer, buffer.length * 2, iStartOffset, iEndOffset);
	if (readOnly) OS.TXNSetTXNObjectControls (txnObject, false, 1, tag, new int [] {1});

	/*
	* Feature in the Macintosh.  When an empty string is set in the TXNObject,
	* the font attributes are cleared.  The fix is to reset them.
	*/
	if (OS.TXNDataSize (txnObject) / 2 == 0) setFontStyle (font);
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * Specifying a limit value larger than <code>Text.LIMIT</code> sets the
 * receiver's limit to <code>Text.LIMIT</code>.
 * </p>
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	textLimit = limit;
}

/**
 * Sets the zero-relative index of the line which is currently
 * at the top of the receiver. This index can change when lines
 * are scrolled or new lines are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	if (OS.HIVIEW) {
		int[] event = new int[1];
		OS.CreateEvent (0, OS.kEventClassScrollable, OS.kEventScrollableScrollTo, 0.0, 0, event);
		if (event [0] != 0) {
			int lineHeight = getLineHeight ();
			CGPoint pt = new CGPoint ();
			pt.y = lineHeight * Math.min(getLineCount (), index);
			OS.SetEventParameter (event[0], OS.kEventParamOrigin, OS.typeHIPoint, CGPoint.sizeof, pt);
			OS.SendEventToEventTarget (event[0], OS.GetControlEventTarget (handle));
			OS.ReleaseEvent (event[0]);
		}
	}
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget();
	if (txnObject == 0) {
		setSelection (getSelection());
	} else {
		OS.TXNShowSelection (txnObject, false);
	}
}

int traversalCode (int key, int theEvent) {
	int bits = super.traversalCode (key, theEvent);
	if ((style & SWT.READ_ONLY) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == 48 /* Tab */ && theEvent != 0) {
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			boolean next = (modifiers [0] & OS.shiftKey) == 0;
			if (next && (modifiers [0] & OS.controlKey) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	/*
	 * It is possible (but unlikely), that application
	 * code could have disposed the widget in the verify
	 * event.  If this happens, answer null to cancel
	 * the operation.
	 */
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

}

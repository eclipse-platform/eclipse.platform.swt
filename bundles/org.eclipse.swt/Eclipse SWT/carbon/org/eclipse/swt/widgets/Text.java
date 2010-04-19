/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.carbon.HILayoutInfo;
import org.eclipse.swt.internal.carbon.HISideBinding;
import org.eclipse.swt.internal.carbon.HIThemeFrameDrawInfo;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.RGBColor;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.ControlEditTextSelectionRec;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.TXNTab;
import org.eclipse.swt.internal.carbon.CGPoint;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * Text controls can be either single or multi-line.
 * When a text control is created with a border, the
 * operating system includes a platform specific inset
 * around the contents of the control.  When created
 * without a border, an effort is made to remove the
 * inset such that the preferred size of the control
 * is the same size as the contents.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified,
 * and only one of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination with SEARCH.
 * When the platform supports the hint, the text control shows these icons.  When an icon
 * is selected, a default selection event is sent with the detail field set to one of
 * ICON_CANCEL or ICON_SEARCH.  Normally, application code does not need to check the
 * detail.  In the case of ICON_CANCEL, the text is cleared before the default selection
 * event is sent causing the application to search for an empty string.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Text extends Scrollable {
	int txnObject, frameHandle;
	int textLimit = LIMIT, tabs = 8;
	ControlEditTextSelectionRec selection;
	char echoCharacter;
	boolean doubleClick;
	String hiddenText, message;
	
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

	static final String [] AX_ATTRIBUTES = {
		OS.kAXTitleAttribute,
		OS.kAXValueAttribute,
		OS.kAXNumberOfCharactersAttribute,
		OS.kAXSelectedTextAttribute,
		OS.kAXSelectedTextRangeAttribute,
		OS.kAXStringForRangeParameterizedAttribute,
	};

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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see SWT#PASSWORD
 * @see SWT#SEARCH
 * @see SWT#ICON_SEARCH
 * @see SWT#ICON_CANCEL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
	if ((style & SWT.SEARCH) != 0) {
		/*
		* Ensure that SWT.ICON_CANCEL and ICON_SEARCH are set.
		* NOTE: ICON_CANCEL has the same value as H_SCROLL and
		* ICON_SEARCH has the same value as V_SCROLL so it is
		* necessary to first clear these bits to avoid a scroll
		* bar and then reset the bit using the original style
		* supplied by the programmer.
		*/
		int attributes = OS.kHISearchFieldNoAttributes;
		if ((style & SWT.ICON_CANCEL) != 0) {
			this.style |= SWT.ICON_CANCEL;
			attributes |= OS.kHISearchFieldAttributesCancel;
		}
		if ((style & SWT.ICON_SEARCH) != 0) {
			this.style |= SWT.ICON_SEARCH;
			attributes |= OS.kHISearchFieldAttributesSearchIcon;
		}
		if (attributes != OS.kHISearchFieldNoAttributes) {
			OS.HISearchFieldChangeAttributes (handle, attributes, 0);
		}
	}
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
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text,
 * or when ENTER is pressed in a search text. If the receiver has the <code>SWT.SEARCH | SWT.CANCEL</code> style
 * and the user cancels the search, the event object detail field contains the value <code>SWT.CANCEL</code>.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
	if (string.length () != 0) sendModifyEvent (true);
}

static int checkStyle (int style) {
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~SWT.PASSWORD;
		/* 
		* NOTE: ICON_CANCEL has the same value as H_SCROLL and
		* ICON_SEARCH has the same value as V_SCROLL so they are
		* cleared because SWT.SINGLE is set. 
		*/
	}
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

int callPaintEventHandler (int control, int damageRgn, int visibleRgn, int theEvent, int nextHandler) {
	int result = super.callPaintEventHandler (control, damageRgn, visibleRgn, theEvent, nextHandler);
	if (frameHandle == control) {
		int [] context = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, context);
		OS.CGContextSaveGState (context[0]);
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
		CGRect rect = new CGRect ();
		OS.HIViewGetBounds (frameHandle, rect);
		rect.x += outMetric [0];
		rect.y += outMetric [0];
		rect.width -= outMetric [0] * 2;
		rect.height -= outMetric [0] * 2;
		int state;
		if (OS.IsControlEnabled (control)) {
			state = OS.IsControlActive (control) ? OS.kThemeStateActive : OS.kThemeStateInactive;
		} else {
			state = OS.IsControlActive (control) ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
		}
		HIThemeFrameDrawInfo info = new HIThemeFrameDrawInfo ();
		info.state = state;
		info.isFocused = hasFocus ();
		info.kind = OS.kHIThemeFrameTextFieldSquare;
		OS.HIThemeDrawFrame (rect, info, context [0], OS.kHIThemeOrientationNormal);
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == (SWT.V_SCROLL | SWT.H_SCROLL)) {
			OS.HIViewGetBounds (frameHandle, rect);
			rect.x = rect.width - outMetric [0];
			rect.y = rect.height - outMetric [0];
			OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
			rect.x -= outMetric [0];
			rect.y -= outMetric [0];
			OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
			rect.x -= outMetric [0];
			rect.y -= outMetric [0];
			rect.width = rect.height = outMetric [0];
			OS.CGContextSetFillColor (context [0], new float[]{1, 1, 1, 1});
			OS.CGContextFillRect (context [0], rect);
		}
		OS.CGContextRestoreGState (context[0]);
	}
	return result;
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	short [] part = new short [1];
	if (txnObject == 0) {
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		if (part [0] == OS.kControlFocusNoPart) {
			selection = new ControlEditTextSelectionRec ();
			OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null);
		}
	}
	int result = super.callFocusEventHandler (nextHandler, theEvent);
	if (isDisposed () ) return result;
	if (frameHandle != 0) {
		OS.HIViewSetNeedsDisplay (frameHandle, true);
	}
	if (txnObject == 0) {
		if (part [0] != OS.kControlFocusNoPart && selection != null) {
			OS.SetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
			selection = null;
		}
	}
	return result;
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
		if ((style & SWT.SEARCH) != 0) {
			int [] ptr1 = new int [1];
			OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr1, null);
			Point size1 = textExtent (ptr1 [0], 0);
			if (ptr1 [0] != 0) OS.CFRelease (ptr1 [0]);
			width = size1.x;
			height = size1.y;
			int [] metric = new int [1];
			OS.GetThemeMetric (OS.kThemeMetricEditTextWhitespace, metric);
			height += metric [0] * 2;
			OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, metric);
			height += metric [0] * 2;
		} else {
			if ((style & SWT.RIGHT) != 0) {
				OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSingleLineTag, 1, new byte [] {1});
			}
			Rect rect = new Rect ();
			OS.GetBestControlRect (handle, rect, null);
			if ((style & SWT.RIGHT) != 0) {
				OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSingleLineTag, 1, new byte [] {0});
			}
			width = rect.right - rect.left;
			height = rect.bottom - rect.top;
		}
	} else {
		int [] oDataHandle = new int [1];
		OS.TXNGetData (txnObject, OS.kTXNStartOffset, OS.kTXNEndOffset, oDataHandle);
		if (oDataHandle [0] != 0) {
			int length = OS.GetHandleSize (oDataHandle [0]), str = 0;
			if (length != 0) {
				int [] ptr = new int [1];
				OS.HLock (oDataHandle [0]);
				OS.memmove (ptr, oDataHandle [0], 4);
				str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, ptr [0], length / 2);					
				OS.HUnlock (oDataHandle[0]);
			}
			OS.DisposeHandle (oDataHandle[0]);
			Point size = textExtent (str, wHint != SWT.DEFAULT && (style & SWT.WRAP) != 0 ? wHint : 0);
			if (str != 0) OS.CFRelease(str);
			width = size.x;
			height = size.y;
		}
	}
	int length = message.length ();
	if ((style & SWT.SINGLE) != 0 && length > 0) {
		char [] buffer = new char [length];
		message.getChars (0, length, buffer, 0);
		Point size = textExtent (buffer, 0);
		width = Math.max (width, size.x);
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
	if ((style & SWT.SEARCH) != 0) {
		int [] left = new int [1], right = new int [1];
		int [] outAttributes = new int [1];
		OS.HISearchFieldGetAttributes (handle, outAttributes);
		if ((outAttributes [0] & OS.kHISearchFieldAttributesSearchIcon) != 0) {
			OS.GetThemeMetric (display.smallFonts ? OS.kThemeMetricRoundTextFieldSmallContentInsetWithIconLeft : OS.kThemeMetricRoundTextFieldContentInsetWithIconLeft, left);
		} else {
			OS.GetThemeMetric (display.smallFonts ? OS.kThemeMetricRoundTextFieldSmallContentInsetLeft : OS.kThemeMetricRoundTextFieldContentInsetLeft, left);			
		}
		if ((outAttributes [0] & OS.kHISearchFieldAttributesCancel) != 0) {
			OS.GetThemeMetric (display.smallFonts ? OS.kThemeMetricRoundTextFieldSmallContentInsetWithIconRight : OS.kThemeMetricRoundTextFieldContentInsetWithIconRight, right);
		} else {
			OS.GetThemeMetric (display.smallFonts ? OS.kThemeMetricRoundTextFieldSmallContentInsetRight : OS.kThemeMetricRoundTextFieldContentInsetRight, right);			
		}
		width += left [0] + right [0];
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
	if ((style & SWT.PASSWORD) != 0 || echoCharacter != '\0') return;
	if (txnObject == 0) {
		Point selection = getSelection ();
		if (selection.x == selection.y) return;
		copyToClipboard (getEditText (selection.x, selection.y - 1));	
	} else {
		OS.TXNCopy (txnObject);
	}
}

void createHandle () {
	if ((style & SWT.READ_ONLY) != 0) {
		if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			state |= THEME_BACKGROUND;
		}
	}
	int [] outControl = new int [1];
	if ((style & SWT.MULTI) != 0 || (style & (SWT.BORDER | SWT.SEARCH)) == 0) {
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0 || OS.VERSION >= 0x1050) {
			int options = 0;
			if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == (SWT.H_SCROLL | SWT.V_SCROLL)) options |= OS.kHIScrollViewOptionsAllowGrow;
			if ((style & SWT.H_SCROLL) != 0) options |= OS.kHIScrollViewOptionsHorizScroll;
			if ((style & SWT.V_SCROLL) != 0) options |= OS.kHIScrollViewOptionsVertScroll;
			/*
			* Bug in the Macintosh.  HIScrollViewCreate() fails if no scroll bit is
			* specified. In order to get horizontal scrolling in a single line text, a
			* scroll view is created with the vertical bit set and the scroll bars
			* are set to auto hide.  But calling HIScrollViewSetScrollBarAutoHide()
			* before the view has been resized still leaves space for the vertical
			* scroll bar.  The fix is to call HIScrollViewSetScrollBarAutoHide()
			* once the widget has been resized.
			*/
			if (options == 0) options |= OS.kHIScrollViewOptionsVertScroll;
			OS.HIScrollViewCreate (options, outControl);
			if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
			scrolledHandle = outControl [0];
			OS.HIViewSetVisible (scrolledHandle, true);
		}
		int iFrameOptions = OS.kTXNDontDrawCaretWhenInactiveMask | OS.kTXNMonostyledTextMask;
		/*
		* Bug in the Macintosh.  For some reason a single line text does not
		* display properly when it is right aligned.  The fix is to use a
		* multi line text when right aligned.
		*/
		if ((style & SWT.RIGHT) == 0) {
			if ((style & SWT.SINGLE) != 0) iFrameOptions |= OS.kTXNSingleLineOnlyMask;
		}
		if ((style & SWT.WRAP) != 0) iFrameOptions |= OS.kTXNAlwaysWrapAtViewEdgeMask;
		OS.HITextViewCreate (null, 0, iFrameOptions, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.HIViewSetVisible (handle, true);
		if ((state & THEME_BACKGROUND) != 0) OS.HITextViewSetBackgroundColor (handle, 0);
		if ((style & SWT.MULTI) != 0 && (style & SWT.BORDER) != 0) {
			int features = OS.kControlSupportsEmbedding;
			OS.CreateUserPaneControl (0, null, features, outControl);
			if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
			frameHandle = outControl [0];			
		}
		txnObject = OS.HITextViewGetTXNObject (handle);			
		int ptr = OS.NewPtr (Rect.sizeof);
		Rect rect = (style & SWT.SINGLE) != 0 ? inset () : new Rect ();
		OS.memmove (ptr, rect, Rect.sizeof);
		int [] tags = new int [] {
			OS.kTXNDisableDragAndDropTag,
			OS.kTXNDoFontSubstitution,
			OS.kTXNIOPrivilegesTag,
			OS.kTXNMarginsTag,
			OS.kTXNJustificationTag,
			OS.kTXNLineDirectionTag,
		};
		int just = OS.kTXNFlushLeft;
		if ((style & SWT.CENTER) != 0) just = OS.kTXNCenter;
		if ((style & SWT.RIGHT) != 0) just = OS.kTXNFlushRight;
		int direction = OS.kTXNLeftToRight;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) direction = OS.kTXNRightToLeft;
		int [] datas = new int [] {
			1,
			1,
			(style & SWT.READ_ONLY) != 0 ? 1 : 0,
			ptr,
			just,
			direction,
		};
		OS.TXNSetTXNObjectControls (txnObject, false, tags.length, tags, datas);
		OS.DisposePtr (ptr);
	} else {
		if ((style & SWT.SEARCH) != 0) {
			OS.HISearchFieldCreate (null, OS.kHISearchFieldNoAttributes, 0, 0, outControl);
		} else {
			int window = OS.GetControlOwner (parent.handle);
			OS.CreateEditUnicodeTextControl (window, null, 0, (style & SWT.PASSWORD) != 0, null, outControl);
		}
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		if ((style & SWT.SEARCH) != 0 && display.smallFonts) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlSizeTag, 2, new short [] {OS.kControlSizeSmall});
		}
		/*
		* Bug in the Macintosh.  For some reason a single line text does not
		* display selection properly when it is right aligned.  The fix is to use a
		* multi line text when right aligned.
		*/
		if ((style & SWT.RIGHT) == 0) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSingleLineTag, 1, new byte [] {1});
		}
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
		if ((style & SWT.SEARCH) != 0) {
			OS.HIViewSetVisible (handle, true);
		}
	}
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	if ((style & SWT.PASSWORD) != 0) setEchoChar (PASSWORD);
	message = "";
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
				copyToClipboard (oldText);
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
			copyToClipboard (oldText);
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
	if (!cut || !oldSelection.equals (newSelection)) sendModifyEvent (true);
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
}

void deregister () {
	super.deregister ();
	if (frameHandle != 0) display.removeWidget (frameHandle);
}

boolean dragDetect (int x, int y, boolean filter, boolean [] consume) {
	if (filter) {
		Point selection = getSelection ();
		if (selection.x != selection.y) {
			int position = getPosition (x, y);
			if (selection.x <= position && position < selection.y) {
				if (super.dragDetect (x, y, filter, consume)) {
					if (consume != null) consume [0] = true;
					return true;
				}
			}
		}
		return false;
	}
	return super.dragDetect (x, y, filter, consume);
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if ((style & SWT.SEARCH) == 0 && (style & SWT.SINGLE) != 0 && message.length () > 0) {
		if (!hasFocus () && getCharCount () == 0) {
			GCData data = new GCData ();
			data.paintEvent = theEvent;
			data.visibleRgn = visibleRgn;
			GC gc = GC.carbon_new (this, data);
			Display display = getDisplay ();
			short depth = (short)display.getDepth ();
			RGBColor rgb = new RGBColor ();
			OS.GetThemeTextColor ((short)OS.kThemeTextColorPushButtonInactive, depth, true, rgb);
			float red = ((rgb.red >> 8) & 0xFF) / 255f;
			float green = ((rgb.green >> 8) & 0xFF) / 255f;
			float blue = ((rgb.blue >> 8) & 0xFF) / 255f;
			Color color = Color.carbon_new (display, new float[]{red, green, blue, 1});
			gc.setForeground (color);
			Rect rect = inset ();
			gc.drawText (message, rect.left, rect.top);
			gc.dispose ();
		}
	}
	super.drawWidget(control, context, damageRgn, visibleRgn, theEvent);
}

int focusPart () {
	if ((style & SWT.SEARCH) != 0) return OS.kControlEditTextPart;
	return super.focusPart ();
}

String [] getAxAttributes () {
	return AX_ATTRIBUTES;
}

public int getBorderWidth () {
	checkWidget();
	if (hasBorder ()) {
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
		return outMetric [0];
	}
	return 0;
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
	CGPoint oPoint = new CGPoint ();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	OS.TXNOffsetToHIPoint (txnObject, oStartOffset [0], oPoint);
	Rect oViewRect = new Rect ();
	OS.TXNGetViewRect (txnObject, oViewRect);
	return new Point ((int) oPoint.x - oViewRect.left, (int) oPoint.y - oViewRect.top);
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
		int [] ptr = new int [1];
		int result = OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, null);
		if (result != OS.noErr) return 0;
		int length = OS.CFStringGetLength (ptr [0]);
		OS.CFRelease (ptr[0]);
		return length;
	}
	return OS.TXNDataSize (txnObject) / 2;
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
	if ((style & SWT.SEARCH) != 0) return display.searchTextInset;
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
		return textExtent (new char[]{' '}, 0).y;
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
 * Returns the widget message.  The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @return the widget message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public String getMessage () {
	checkWidget ();
	return message;
}

int getPosition (int x, int y) {
//	checkWidget ();
	if (txnObject == 0) return -1;
	int [] oOffset = new int [1];
	Rect oViewRect = new Rect ();
	OS.TXNGetViewRect (txnObject, oViewRect);
	CGPoint iPoint = new CGPoint ();
	iPoint.x = x + oViewRect.left;
	iPoint.y = y + oViewRect.top;
	return OS.TXNHIPointToOffset (txnObject, iPoint, oOffset) == OS.noErr ? oOffset [0] : -1;
}

/*public*/ int getPosition (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return getPosition (point.x, point.y);
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
		return new String (getEditText ());
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
	if (!(start <= end && 0 <= end)) return "";
	if (txnObject == 0) {
		return new String (getEditText (start, end));
	} else {
		int length = OS.TXNDataSize (txnObject) / 2;
		end = Math.min (end, length - 1);
		if (start > end) return "";
		start = Math.max (0, start);
		return getTXNText (start, end + 1);
	}
}

char [] getEditText () {
	int [] ptr = new int [1];
	int [] actualSize = new int [1];
	int result = OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	if (result != OS.noErr) return new char [0];
	int length = OS.CFStringGetLength (ptr [0]);
	CFRange range = new CFRange ();
	range.length = length;
	char [] buffer = new char [range.length];
	if (hiddenText != null) {
		hiddenText.getChars (0, range.length, buffer, 0);
	} else {
		OS.CFStringGetCharacters (ptr [0], range, buffer);
	}
	OS.CFRelease (ptr [0]);
	return buffer;
}

char [] getEditText (int start, int end) {
	int [] ptr = new int [1];
	int [] actualSize = new int [1];
	int result = OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	if (result != OS.noErr) return new char [0];
	int length = OS.CFStringGetLength (ptr [0]);
	end = Math.min (end, length - 1);
	if (start > end) return new char [0];
	start = Math.max (0, start);
	CFRange range = new CFRange ();
	range.location = start;
	range.length = Math.max (0, end - start + 1);
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
	CGRect rect = new CGRect ();
	OS.TXNGetHIRect (txnObject, OS.kTXNDestinationRectKey, rect);
	int destY = (int) rect.y;
	OS.TXNGetHIRect (txnObject, OS.kTXNTextRectKey, rect);
	return destY - (int) rect.y;
}

char [] getTXNChars (int iStartOffset, int iEndOffset) {
	int [] oDataHandle = new int [1];
	OS.TXNGetData (txnObject, iStartOffset, iEndOffset, oDataHandle);
	if (oDataHandle [0] == 0) return new char [0];
	int length = OS.GetHandleSize (oDataHandle [0]);
	if (length == 0) return new char [0];
	int [] ptr = new int [1];
	OS.HLock (oDataHandle [0]);
	OS.memmove (ptr, oDataHandle [0], 4);
	char [] buffer = new char [length / 2];
	OS.memmove (buffer, ptr [0], length);
	OS.HUnlock (oDataHandle[0]);
	OS.DisposeHandle (oDataHandle[0]);
	return buffer;
}

String getTXNText (int iStartOffset, int iEndOffset) {
	return new String (getTXNChars (iStartOffset, iEndOffset));
}

void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEARCH) != 0) {
		int searchProc = display.searchProc;
		int [] mask = new int [] {
			OS.kEventClassSearchField, OS.kEventSearchFieldCancelClicked,
			OS.kEventClassSearchField, OS.kEventSearchFieldSearchClicked,
		};
		int controlTarget = OS.GetControlEventTarget (handle);
		OS.InstallEventHandler (controlTarget, searchProc, mask.length / 2, mask, handle, null);
	}
	if (frameHandle != 0) {
		int controlProc = display.controlProc;
		int [] mask = new int [] {
			OS.kEventClassControl, OS.kEventControlDraw,
		};
		int controlTarget = OS.GetControlEventTarget (frameHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, frameHandle, null);
	}
}

Rect inset () {
	if ((style & SWT.SEARCH) != 0) return super.inset ();
	if ((style & SWT.SINGLE) != 0 && (style & SWT.BORDER) == 0) {
		Rect rect = new Rect ();
		rect.left = rect.top = rect.right = rect.bottom = 1;
		return rect; 
	}
	if ((style & SWT.MULTI) != 0 && (style & SWT.BORDER) != 0) {
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
		Rect rect = new Rect ();
		rect.left += outMetric [0];
		rect.top += outMetric [0];
		rect.right += outMetric [0];
		rect.bottom += outMetric [0];
		OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
		rect.left += outMetric [0];
		rect.top += outMetric [0];
		rect.right += outMetric [0];
		rect.bottom += outMetric [0];		
		return rect;
	}
	return new Rect ();
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
	if (string.length () != 0) sendModifyEvent (true);
}

void insertEditText (String string) {
	int length = string.length ();
	Point selection = getSelection ();
	if (hasFocus () && hiddenText == null) {
		if (textLimit != LIMIT) {
			int charCount = getCharCount();
			if (charCount - (selection.y - selection.x) + length > textLimit) {
				length = textLimit - charCount + (selection.y - selection.x);
			}
		}
		char [] buffer = new char [length];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextInsertCFStringRefTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
	} else {
		String oldText = getText ();
		if (textLimit != LIMIT) {
			int charCount = oldText.length ();
			if (charCount - (selection.y - selection.x) + length > textLimit) {
				string = string.substring(0, textLimit - charCount + (selection.y - selection.x));
			}
		}
		String newText = oldText.substring (0, selection.x) + string + oldText.substring (selection.y);
		setEditText (newText);
		setSelection (selection.x + string.length ());
	}
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	if (txnObject != 0) {
		int [] stringRef = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
		int length = 0;
		if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
		char [] buffer = new char [length];
		CFRange range = new CFRange ();
		range.length = length;
		OS.CFStringGetCharacters (stringRef [0], range, buffer);
		String attributeName = new String(buffer);
		if (attributeName.equals (OS.kAXRoleAttribute) || attributeName.equals (OS.kAXRoleDescriptionAttribute)) {
			String roleText = (style & SWT.MULTI) != 0 ? OS.kAXTextAreaRole : OS.kAXTextFieldRole;
			buffer = new char [roleText.length ()];
			roleText.getChars (0, buffer.length, buffer, 0);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				if (attributeName.equals (OS.kAXRoleAttribute)) {
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				} else { // kAXRoleDescriptionAttribute
					int stringRef2 = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef2});
					OS.CFRelease(stringRef2);
				}
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		} else if (OS.VERSION < 0x1050 && attributeName.equals (OS.kAXFocusedAttribute)) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus ()});
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXTitleAttribute)) {
			/*
			* Feature of the Macintosh.  For some reason, AXTextFields return their text contents
			* when they are asked for their title.  Since they also return their text contents
			* when they are asked for their value, this causes screen readers to speak the text
			* twice.  The fix is to return nothing when asked for a title.
			*/
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXValueAttribute)) {
			buffer = getTXNChars (OS.kTXNStartOffset, OS.kTXNEndOffset);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		} else if (attributeName.equals (OS.kAXNumberOfCharactersAttribute)) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {getCharCount()});
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXSelectedTextAttribute)) {
			Point sel = getSelection ();
			buffer = getTXNChars (sel.x, sel.y);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		} else if (attributeName.equals (OS.kAXSelectedTextRangeAttribute)) {
			Point sel = getSelection ();
			range = new CFRange();
			range.location = sel.x;
			range.length = sel.y - sel.x;
			int valueRef = OS.AXValueCreate(OS.kAXValueCFRangeType, range);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {valueRef});
			OS.CFRelease(valueRef);
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXStringForRangeParameterizedAttribute)) {
			int valueRef [] = new int [1];
			int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeParameter, OS.typeCFTypeRef, null, 4, null, valueRef);
			if (status == OS.noErr) {
				range = new CFRange();
				boolean ok = OS.AXValueGetValue(valueRef[0], OS.kAXValueCFRangeType, range);
				if (ok) {
					buffer = getTXNChars (range.location, range.location + range.length);
					stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
					if (stringRef [0] != 0) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
						OS.CFRelease(stringRef [0]);
						code = OS.noErr;
					}
				}
			}
		}
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
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

int kEventSearchFieldCancelClicked (int nextHandler, int theEvent, int userData) {
	int result = super.kEventSearchFieldCancelClicked (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	setText ("");
	Event event = new Event ();
	event.detail = SWT.ICON_CANCEL;
	sendSelectionEvent (SWT.DefaultSelection, event, false);
	return result;
}

int kEventSearchFieldSearchClicked (int nextHandler, int theEvent, int userData) {
	int result = super.kEventSearchFieldSearchClicked (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Event event = new Event ();
	event.detail = SWT.ICON_SEARCH;
	sendSelectionEvent (SWT.DefaultSelection, event, false);
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
				sendSelectionEvent (SWT.DefaultSelection);
				return OS.noErr;
			}
			case 48: { /* Tab */
				return OS.noErr;
			}
		}
	}
	return result;
}

int kEventTextInputUpdateActiveInputArea (int nextHandler, int theEvent, int userData) {
	int [] length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, 0, length, (char [])null);
	int [] fixed_length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendFixLen, OS.typeLongInteger, null, 4, null, fixed_length);
	if (fixed_length [0] == -1 || fixed_length [0] == length [0]) {
		sendModifyEvent (false);
	}
	return OS.eventNotHandledErr;
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
			if (textLimit != LIMIT) {
				if (oldText == null) oldText = getClipboardText ();
				setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, oldText);
				OS.TXNShowSelection (txnObject, false);
			} else {
				OS.TXNPaste (txnObject);
			}
		}
	}
	sendModifyEvent (true);
}

boolean pollTrackEvent() {
	return true;
}

void register () {
	super.register ();
	if (frameHandle != 0) display.addWidget (frameHandle, this);
}

void releaseWidget () {
	super.releaseWidget ();
	txnObject = 0;
	hiddenText = message = null;
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
 * be notified when the control is selected by the user.
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
	if ((event.stateMask & SWT.COMMAND) != 0) return true;
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
			String text = new String (getEditText ());
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
	sendModifyEvent (false);
	return result;
}

void sendModifyEvent (boolean send) {
	String string = OS.kAXSelectedTextChangedNotification;
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.AXNotificationHIObjectNotify(stringRef, handle, 0);
	OS.CFRelease(stringRef);
	string = OS.kAXValueChangedNotification;
	buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.AXNotificationHIObjectNotify(stringRef, handle, 0);
	OS.CFRelease(stringRef);
	
	if (send) {
		sendEvent (SWT.Modify);
	} else {
		postEvent (SWT.Modify);
	}
}

void setBackground (float [] color) {
	if (txnObject == 0) {
		super.setBackground (color);
	} else {
		int colorspace = OS.CGColorSpaceCreateDeviceRGB ();
		int colorRef = OS.CGColorCreate (colorspace, color);
		OS.HITextViewSetBackgroundColor (handle, colorRef);
		OS.CGColorRelease (colorRef);
		OS.CGColorSpaceRelease (colorspace);
	}
}

void setBackground (int control, float [] color) {
	/*
	* Bug in the Macintosh. For some reason, when the same background
	* color is set in two instances of an EditUnicodeTextControl, the
	* color is not set in the second instance.  It seems that the edit
	* control is checking globally that the last color that was set is the
	* same.  The fix is to ensure the that the color that is about to
	* be set is not the same as the last globally remembered color by
	* first setting it to black, then white and finally the color.
	*/
	if (handle == control) {
		ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
		OS.GetControlData (control, (short) OS.kControlEntireControl, OS.kControlFontStyleTag, ControlFontStyleRec.sizeof, fontStyle, null);
		fontStyle.flags |= OS.kControlUseBackColorMask;
		OS.SetControlFontStyle (control, fontStyle);
		fontStyle.backColor_red = fontStyle.backColor_green = fontStyle.backColor_blue = (short) 0xffff;
		OS.SetControlFontStyle (control, fontStyle);
	}
	super.setBackground (control, color);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	Rectangle bounds = null;
	if (txnObject == 0 && resize) bounds = getBounds ();
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if (bounds != null && (result & RESIZED) != 0) {
		/*
		* Feature in the Macintosh.  When the caret is moved,
		* the text widget scrolls to show the new location.
		* This means that the text widget may be scrolled
		* to the right in order to show the caret when the
		* widget is not large enough to show both the caret
		* location and all the text.  Unfortunately, when
		* the text widget is resized such that all the text
		* and the caret could be visible, Macintosh does not
		* scroll the widget back.  The fix is to reset the
		* selection or the text depend on if the widget
		* is on focus or not.
		*/
		Rect inset = getInset ();
		int minWidth = inset.left + inset.right;
		if (bounds.width <= minWidth && width > minWidth) {
			if (hasFocus ()) {
				ControlEditTextSelectionRec selection = new ControlEditTextSelectionRec ();
				if (OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null) == OS.noErr) {
					OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
				}
			} else {
				int [] ptr = new int [1];
				if (OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, null) == OS.noErr) {
					OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr);
				}
				if (ptr [0] != 0) OS.CFRelease (ptr [0]);				
			}
		}
	}
	/*
	* Bug in the Macintosh.  HIScrollViewCreate() fails if no scroll bit is
	* specified. In order to get horizontal scrolling in a single line text, a
	* scroll view is created with the vertical bit set and the scroll bars
	* are set to auto hide.  But calling HIScrollViewSetScrollBarAutoHide()
	* before the view has been resized still leaves space for the vertical
	* scroll bar.  The fix is to call HIScrollViewSetScrollBarAutoHide()
	* once the widget has been resized.
	*/
	if (scrolledHandle != 0 && (style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		OS.HIScrollViewSetScrollBarAutoHide (scrolledHandle, true);
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
		OS.memmove (ptr2, rgb, RGBColor.sizeof);
		int [] attribs = new int [] {
			OS.kTXNQDFontColorAttribute,
			OS.kTXNQDFontColorAttributeSize,
			ptr2,
		};
		int ptr1 = OS.NewPtr (attribs.length * 4);
		OS.memmove (ptr1, attribs, attribs.length * 4);
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
		int family = OS.kTXNDefaultFontName, fontStyle = OS.kTXNDefaultFontStyle, size = OS.kTXNDefaultFontSize;
		if (font != null) {
			short [] id = new short [1], s = new short [1];
			OS.FMGetFontFamilyInstanceFromFont (font.handle, id, s);
			family = id [0];
			fontStyle = s [0] | font.style;
			size = OS.X2Fix (font.size);
		}
		int [] attribs = new int [] {
			OS.kTXNQDFontSizeAttribute,
			OS.kTXNQDFontSizeAttributeSize,
			size,
			OS.kTXNQDFontStyleAttribute,
			OS.kTXNQDFontStyleAttributeSize,
			fontStyle,
			OS.kTXNQDFontFamilyIDAttribute,
			OS.kTXNQDFontFamilyIDAttributeSize,
			family,
		};
		int ptr = OS.NewPtr (attribs.length * 4);
		OS.memmove (ptr, attribs, attribs.length * 4);
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
 * Sets the widget message. The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @param message the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the message is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public void setMessage (String message) {
	checkWidget ();
	if (message == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.message = message;
	if ((style & SWT.SEARCH) != 0) {
		char [] buffer = new char [message.length ()];
		message.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.HISearchFieldSetDescriptiveText (handle, ptr);
		OS.CFRelease (ptr);
	} else {
		redraw (false);
	}
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
	tab.value = (short) (textExtent (new char[]{' '}, 0).x * tabs);
	int [] tags = new int [] {OS.kTXNTabSettingsTag};
	int [] datas = new int [1];
	OS.memmove (datas, tab, TXNTab.sizeof);
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
	sendModifyEvent (true);
}

void setEditText (String string) {
	char [] buffer;
	if ((style & SWT.PASSWORD) == 0 && echoCharacter != '\0') {
		hiddenText = string;
		buffer = new char [Math.min(hiddenText.length (), textLimit)];
		for (int i = 0; i < buffer.length; i++) buffer [i] = echoCharacter;
	} else {
		hiddenText = null;
		buffer = new char [Math.min(string.length (), textLimit)];
		string.getChars (0, buffer.length, buffer, 0);
	}
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
	OS.CFRelease (ptr);
	if (selection != null) selection = null;
}

void setTXNText (int iStartOffset, int iEndOffset, String string) {
	int length = string.length ();
	if (textLimit != LIMIT) {
		int charCount = OS.TXNDataSize (txnObject) / 2;
		int start = iStartOffset, end = iEndOffset;
		if (iStartOffset == OS.kTXNUseCurrentSelection || iEndOffset == OS.kTXNUseCurrentSelection) {
			int [] oStartOffset = new int [1], oEndOffset = new int [1];
			OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
			start = oStartOffset [0];
			end = oEndOffset [0];
		} else {
			if (iStartOffset == OS.kTXNEndOffset) start = charCount;
			if (iEndOffset == OS.kTXNEndOffset) end = charCount;
		}
		if (charCount - (end - start) + length > textLimit) length = textLimit - charCount + (end - start);
	}
	char [] buffer = new char [length];
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

void setZOrder () {
	if (frameHandle != 0) {
		int child = scrolledHandle != 0 ? scrolledHandle : handle;
		OS.HIViewAddSubview (frameHandle, child);
		HILayoutInfo layout = new HILayoutInfo ();
		layout.version = 0;
		OS.HIViewGetLayoutInfo (child, layout);
		HISideBinding biding = layout.binding.top;
		biding.toView = 0;
		biding.kind = OS.kHILayoutBindMin;
		biding.offset = 0;
		biding = layout.binding.left;
		biding.toView = 0;
		biding.kind = OS.kHILayoutBindMin;
		biding.offset = 0;
		biding = layout.binding.bottom;
		biding.toView = 0;
		biding.kind = OS.kHILayoutBindMax;
		biding.offset = 0;
		biding = layout.binding.right;
		biding.toView = 0;
		biding.kind = OS.kHILayoutBindMax;
		biding.offset = 0;
		CGRect r = new CGRect();
		r.width = r.height = 100;
		OS.HIViewSetFrame (frameHandle, r);
		Rect inset = inset ();
		r.x += inset.left;
		r.y += inset.top;
		r.width -= inset.left + inset.right;
		r.height -= inset.top + inset.bottom;
		OS.HIViewSetFrame (child, r);
		OS.HIViewSetLayoutInfo (child, layout);
	}
	super.setZOrder ();
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

int topHandle () {
	if (frameHandle != 0) return frameHandle;
	return super.topHandle ();
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

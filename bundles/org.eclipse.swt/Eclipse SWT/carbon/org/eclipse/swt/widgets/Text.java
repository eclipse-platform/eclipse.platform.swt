package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.EventRecord;
import org.eclipse.swt.internal.carbon.TXNLongRect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class Text extends Scrollable {
	int txnObject, txnFrameID;
	int textLimit = LIMIT;
	char echoCharacter;
	public static final int LIMIT;
	public static final String DELIMITER;

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\n";
	}

public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

public void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

public void append (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int charCount = getCharCount ();
		string = verifyText (string, charCount, charCount);
		if (string == null) return;
	}
	setTXNText (OS.kTXNEndOffset, OS.kTXNEndOffset, string);
	OS.TXNSetSelection (txnObject, OS.kTXNEndOffset, OS.kTXNEndOffset);
	OS.TXNShowSelection (txnObject, false);
	if (string.length () != 0) sendEvent (SWT.Modify);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) style |= SWT.MULTI;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

public void clearSelection () {
	checkWidget();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	OS.TXNSetSelection (txnObject, oStartOffset [0], oStartOffset [0]);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	TXNLongRect oTextRect = new TXNLongRect ();
	OS.TXNGetRectBounds (txnObject, null, null, oTextRect);
	int width = oTextRect.right - oTextRect.left;
	int height = oTextRect.bottom - oTextRect.top;
	if (width <= 0) width = DEFAULT_WIDTH;
	if (height <= 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;  height = trim.height;
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int ptr = OS.NewPtr (Rect.sizeof);
	OS.TXNGetTXNObjectControls (txnObject, 1, new int [] {OS.kTXNMarginsTag}, new int [] {ptr});
	Rect rect = new Rect ();
	OS.memcpy (rect, ptr, Rect.sizeof);
	OS.DisposePtr (ptr);
	width += rect.left + rect.right;
	height += rect.top + rect.bottom;
	int [] size = new int [1];
	OS.GetThemeMetric(OS.kThemeMetricScrollBarWidth, size);
	if (horizontalBar != null) height += size [0];
	if (verticalBar != null) width += size [0];
	Rect inset = inset ();
	x -= inset.left;
	y -= inset.top;
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	return new Rectangle (x, y, width, height);
}

public void copy () {
	checkWidget ();
	OS.TXNCopy (txnObject);
}

void createHandle () {
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
	* is to embed the scroll bars in the user pane.	*/
	int [] theRoot = new int [1];
	OS.GetRootControl (window, theRoot);
	short [] oldCount = new short [1];
	OS.CountSubControls (theRoot [0], oldCount);	
	
	/* Create the TXNObject */
	int iFrameOptions = OS.kTXNDontDrawCaretWhenInactiveMask | OS.kTXNMonostyledTextMask;
	if ((style & SWT.H_SCROLL) != 0) iFrameOptions |= OS.kTXNWantHScrollBarMask;
	if ((style & SWT.V_SCROLL) != 0) iFrameOptions |= OS.kTXNWantVScrollBarMask;
	if ((style & SWT.SINGLE) != 0) iFrameOptions |= OS.kTXNSingleLineOnlyMask;
	if ((style & SWT.READ_ONLY) != 0) iFrameOptions |= OS.kTXNReadOnlyMask;
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
	
	/* Configure the TXNOBject */
	OS.TXNSetTXNObjectControls (txnObject, false, 1, new int [] {OS.kTXNDisableDragAndDropTag}, new int [] {1});
	OS.TXNSetFrameBounds (txnObject, 0, 0, 0, 0, txnFrameID);
	int ptr = OS.NewPtr (Rect.sizeof);
	Rect rect = new Rect ();
	OS.SetRect (rect, (short) 1, (short) 1, (short) 1, (short) 1);
	OS.memcpy (ptr, rect, Rect.sizeof);
	OS.TXNSetTXNObjectControls (txnObject, false, 1, new int [] {OS.kTXNMarginsTag}, new int [] {ptr});
	OS.DisposePtr (ptr);
}

ScrollBar createScrollBar (int type) {
	return createStandardBar (style);
}

public void cut () {
	checkWidget();
	Point oldSelection = getSelection ();
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		if (oldSelection.x != oldSelection.y) {
			String newText = verifyText ("", oldSelection.x, oldSelection.y);
			if (newText == null) return;
			if (newText.length () != 0) {
				setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, newText);
			}
		}
	}
	OS.TXNCut (txnObject);
	Point newSelection = getSelection ();
	if (!oldSelection.equals (newSelection)) sendEvent (SWT.Modify);
}

void drawWidget (int control) {
	drawBackground (handle, background);
	OS.TXNDraw (txnObject, 0);
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	Rect inset = inset ();
	rect.left += inset.left;
	rect.top += inset.top;
	rect.right -= inset.right;
	rect.bottom -= inset.bottom;
	if (hasBorder ()) {
		int state = OS.IsControlActive (handle) ? OS.kThemeStateActive : OS.kThemeStateInactive;
		if (hasFocus ()) {
			OS.DrawThemeEditTextFrame (rect, state);
			OS.DrawThemeFocusRect (rect, true);
		} else {
			OS.DrawThemeFocusRect (rect, false);
			OS.DrawThemeEditTextFrame (rect, state);
		}
	} else {
		OS.DrawThemeFocusRect (rect, hasFocus ());
	}
}

public int getCaretLineNumber () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
    return (getTopPixel () + getCaretLocation ().y) / getLineHeight ();
}

public Point getCaretLocation () {
	checkWidget();
	org.eclipse.swt.internal.carbon.Point oPoint = new org.eclipse.swt.internal.carbon.Point ();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	OS.TXNOffsetToPoint (txnObject, oStartOffset [0], oPoint);
	Rect oViewRect = new Rect ();
	OS.TXNGetViewRect (txnObject, oViewRect);
	return new Point (oPoint.h - oViewRect.left, oPoint.v - oViewRect.top);
}

public int getCaretPosition () {
	checkWidget(); 
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	return oStartOffset [0];
}

public int getCharCount () {
	checkWidget();
	return OS.TXNDataSize (txnObject) / 2;
}

public boolean getDoubleClickEnabled () {
	checkWidget();
	//NOT DONE
    return true;
}

public char getEchoChar () {
	checkWidget();
	return echoCharacter;
}

public boolean getEditable () {
	checkWidget();
	return (style & SWT.READ_ONLY) == 0;
}

public int getLineCount () {
	checkWidget();
	int [] oLineTotal = new int [1];
	OS.TXNGetLineCount (txnObject, oLineTotal);
	return oLineTotal [0];
}

public String getLineDelimiter () {
	checkWidget();
	return DELIMITER;
}

public int getLineHeight () {
	checkWidget();
	int [] oLineWidth = new int [1], oLineHeight = new int [1];
	OS.TXNGetLineMetrics (txnObject, 0, oLineWidth, oLineHeight);
	return OS.Fix2Long (oLineHeight [0]);
}

public Point getSelection () {
	checkWidget();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	return new Point (oStartOffset [0], oEndOffset [0]);
}

public int getSelectionCount () {
	checkWidget();
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	return oEndOffset [0] - oStartOffset [0];
}

public String getSelectionText () {
	checkWidget();
	return getTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection);
}

public int getTabs () {
	checkWidget();
	//NOT DONE
	return 8;
}

public String getText () {
	checkWidget();
	return getTXNText (OS.kTXNStartOffset, OS.kTXNEndOffset);
}

public String getText (int start, int end) {
	checkWidget ();
	if (start < 0 || start > end) return "";
	return getTXNText (start, end);
}

public int getTextLimit () {
	checkWidget();
    return textLimit;
}

public int getTopIndex () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
    return getTopPixel () / getLineHeight ();
}

public int getTopPixel () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
	Rect oViewRect = new Rect ();
	TXNLongRect oDestinationRect = new TXNLongRect ();
	TXNLongRect oTextRect = new TXNLongRect ();
	OS.TXNGetRectBounds (txnObject, oViewRect, oDestinationRect, oTextRect);
	return oDestinationRect.top - oTextRect.top;
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
	Rect rect = new Rect ();
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
	rect.left += outMetric [0];
	rect.top += outMetric [0];
	rect.right += outMetric [0];
	rect.bottom += outMetric [0];
	if (hasBorder ()) {
		OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
		rect.left += outMetric [0];
		rect.top += outMetric [0];
		rect.right += outMetric [0];
		rect.bottom += outMetric [0];
	}
	return rect;	
} 

public void insert (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		Point selection = getSelection ();
		string = verifyText (string, selection.x, selection.y);
		if (string == null) return;
	}
	setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, string);
	OS.TXNShowSelection (txnObject, false);
	if (string.length () != 0) sendEvent (SWT.Modify);
}

int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlActivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	OS.TXNFocus (txnObject, hasFocus ());
	OS.TXNActivate (txnObject, txnFrameID, OS.kScrollBarsSyncAlwaysActive);
	return result;
}

int kEventControlBoundsChanged (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlBoundsChanged (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] attributes = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
	if ((attributes [0] & (OS.kControlBoundsChangePositionChanged | OS.kControlBoundsChangeSizeChanged)) != 0) setTXNBounds ();
	return result;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!isEnabled ()) return OS.noErr;
	int window = OS.GetControlOwner (handle);
	OS.SetKeyboardFocus (window, handle, (short)OS.kControlFocusNextPart);
	EventRecord iEvent = new EventRecord ();
	OS.ConvertEventRefToEventRecord (theEvent, iEvent);
	OS.TXNClick (txnObject, iEvent);
	return OS.noErr;
}

int kEventControlDeactivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlDeactivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	OS.TXNFocus (txnObject, hasFocus());
	OS.TXNActivate (txnObject, txnFrameID, OS.kScrollBarsSyncWithFocus);
	return result;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetCursor (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	OS.TXNAdjustCursor (txnObject, 0);
	return OS.noErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	short [] part = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
	OS.TXNFocus (txnObject, part [0] != 0);
	redraw ();
	return OS.noErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventRawKeyDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	if (modifiers [0] == OS.cmdKey) {
		int [] keyCode = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
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
		OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		switch (keyCode [0]) {
			case 36: { /* Return */
				/*
				* Bug in the Macintosh.  When the default handler calls TXNKeyDown()
				* for a single line TXN Object, it does not check for the return key
				* or the default button.  The result is that a garbage character (the
				* CR) is entered into the TXN Object.  The fix is to temporarily take
				* focus away from the TXN Object, call the default handler to process
				* the return key and reset the focus.
				*/
				OS.TXNFocus (txnObject, false);
				result = OS.CallNextEventHandler (nextHandler, theEvent);
				OS.TXNFocus (txnObject, true);
				postEvent (SWT.DefaultSelection);
				break;
			}
			case 48: { /* Tab */
				/*
				* Feature in the Macintosh.  Tab characters are inserted into a single
				* line TXN Object.  While this may be correct platform behavior, it is
				* unexpected.  The fix is to avoid calling the default handler. 
				*/
				return OS.noErr;
			}
		}
	}
	if ((style & SWT.READ_ONLY) != 0) return OS.noErr;
	return result;
}

public void paste () {
	checkWidget();
	//NOT DONE - get clipboard text and verify or use undo?
	OS.TXNPaste (txnObject);
	sendEvent (SWT.Modify);
}

void releaseWidget () {
	super.releaseWidget ();
	OS.TXNDeleteObject (txnObject);
	txnObject = txnFrameID = 0;
}

public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

public void removeVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

public void selectAll () {
	checkWidget();
	OS.TXNSelectAll (txnObject);
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
	String newText = verifyText (oldText, start, end);
	if (newText == null) return false;
	if (charCount - (end - start) + newText.length () > textLimit) {
		return false;
	}
	if (newText != oldText) setTXNText (start, end, newText);
	/*
	* Post the modify event so that the character will be inserted
	* into the widget when the modify event is delivered.  Normally,
	* modify events are sent but it is safe to post the event here
	* because this method is called from the event loop.
	*/
	postEvent (SWT.Modify);
	return newText == oldText;
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds(control, x, y, width, height, move, resize, events);
	if ((result & (RESIZED | MOVED)) != 0) setTXNBounds ();
	return result;
}

public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget();
	//NOT DONE
}

public void setEchoChar (char echo) {
	checkWidget();
	echoCharacter = echo;
	OS.TXNEchoMode (txnObject, echo, 0, echo != '\0');
}

public void setEditable (boolean editable) {
	checkWidget();
	if (editable) {
		style &= ~SWT.READ_ONLY;
	} else {
		style |= SWT.READ_ONLY;
	}
}

public void setSelection (int start) {
	checkWidget();
	setSelection (start, start);
}

public void setSelection (int start, int end) {
	checkWidget();
	if (start < 0 || start > end) return;
	OS.TXNSetSelection (txnObject, start, end);
	OS.TXNShowSelection (txnObject, false);
}

public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

public void setTabs (int tabs) {
	checkWidget();
	//NOT DONE
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, 0, getCharCount ());
		if (string == null) return;
	}
	setTXNText (OS.kTXNStartOffset, OS.kTXNEndOffset, string);
	OS.TXNSetSelection (txnObject, OS.kTXNStartOffset, OS.kTXNStartOffset);
	OS.TXNShowSelection (txnObject, false);
	sendEvent (SWT.Modify);
}

void setTXNBounds () {
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
	* will cause the widget text widget to recompute the
	* left scroll position.
	* 
	* NOTE: Currently, this work around is only applied
	* to text widgets that are not visible.  If the widget
	* is resized when it is visible, this is fine because
	* the user has already seen that the text is scrolled.
	*/
	if (OS.IsControlVisible (handle)) return;
	int [] oStartOffset = new int [1], oEndOffset = new int [1];
	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
	OS.TXNSetSelection (txnObject, OS.kTXNStartOffset, OS.kTXNStartOffset);
	OS.TXNShowSelection (txnObject, false);
	OS.TXNSetSelection (txnObject, oStartOffset [0], oEndOffset [0]);
	OS.TXNShowSelection (txnObject, false);
}

void setTXNText (int iStartOffset, int iEndOffset, String string) {
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);	
	OS.TXNSetData (txnObject, OS.kTXNUnicodeTextData, buffer, buffer.length * 2, iStartOffset, iEndOffset);
}

public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	textLimit = limit;
}

public void setTopIndex (int index) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	//NOT DONE
//	Rect oViewRect = new Rect ();
//	TXNLongRect oDestinationRect = new TXNLongRect ();
//	TXNLongRect oTextRect = new TXNLongRect ();
//	OS.TXNGetRectBounds (txnObject, oViewRect, oDestinationRect, oTextRect);
//	int topPixel = oDestinationRect.top - oTextRect.top;
//	int [] oOffset = new int [1];
//	org.eclipse.swt.internal.carbon.Point iPoint = new org.eclipse.swt.internal.carbon.Point ();
//	OS.SetPt (iPoint, (short)0, (short)(-topPixel + (index * getLineHeight ())));
//	OS.TXNPointToOffset (txnObject, iPoint, oOffset);
//	System.out.println (oOffset [0]);
//	int [] oStartOffset = new int [1], oEndOffset = new int [1];
//	OS.TXNGetSelection (txnObject, oStartOffset, oEndOffset);
//	OS.TXNSetSelection (txnObject, oOffset [0], oOffset [0]);
//	OS.TXNShowSelection (txnObject, false);
//	OS.TXNSetSelection (txnObject, oStartOffset [0], oEndOffset [0]);
}

public void setVisible (boolean visible) {
	super.setVisible (visible);
	OS.TXNSetTXNObjectControls (txnObject, false, 1, new int[] {OS.kTXNVisibilityTag}, new int[] {visible ? -1 : 0});
}

public void showSelection () {
	checkWidget();
	OS.TXNShowSelection (txnObject, false);
}

String verifyText (String string, int start, int end) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
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

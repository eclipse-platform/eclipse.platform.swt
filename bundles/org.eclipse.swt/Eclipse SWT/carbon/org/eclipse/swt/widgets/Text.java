package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>MULTI, SINGLE, READ_ONLY, WRAP</dd>
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
	// AW
	private static final int FOCUS_BORDER= 3;
	private static final int MARGIN= 0; // 2;

	private int fTextLimit= LIMIT;
	private int fTX;
	private int fFrameID;
	private Rectangle fFrameBounds;
	// AW
	
	char echoCharacter;
	// AW int drawCount;

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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.convertToLf (string);
	int length = OS.TXNDataSize(fTX) / 2;
	if (hooks (SWT.Verify)) {
		string = verifyText (string, length, length);
		if (string == null) return;
	}
	replaceTXNText(OS.kTXNEndOffset, OS.kTXNEndOffset, string);
}
static int checkStyle (int style) {
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		return style | SWT.MULTI;
	}
	return style | SWT.SINGLE;
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
	OS.TXNSetSelection(fTX, OS.kTXNStartOffset, OS.kTXNStartOffset);	// AW: wrong
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = wHint;
	int height = hHint;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int size= OS.TXNDataSize(fTX);
		if (size == 0) {
			if (hHint == SWT.DEFAULT) {
				if ((style & SWT.SINGLE) != 0) {
					int[] textBounds= new int[4];
					OS.TXNGetRectBounds(fTX, null, null, textBounds);
					height= textBounds[2]-textBounds[0];
				} else
					height= DEFAULT_HEIGHT;
			}
			if (wHint == SWT.DEFAULT)
				width= DEFAULT_WIDTH;
		} else {
			int[] textBounds= new int[4];
			OS.TXNGetRectBounds(fTX, null, null, textBounds);
			if (hHint == SWT.DEFAULT)
				height= textBounds[2]-textBounds[0];					
			if (wHint == SWT.DEFAULT)
				width= textBounds[3]-textBounds[1];
		}
	}
	if (horizontalBar != null) {
		height += 15;
	}
	if (verticalBar != null) {
 		width += 15;
	}
    /* AW
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (handle, rect);
	width += rect.x * 2;  height += rect.y * 2;
	if ((style & (SWT.MULTI | SWT.BORDER)) != 0) height++;
    */
	// AW
	width += 2*MARGIN;
	height += 2*MARGIN;
	if ((style & SWT.BORDER) != 0) {
		width += 2*FOCUS_BORDER;
		height += 2*FOCUS_BORDER;
	}
	// AW
	return new Point (width, height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim(x, y, width, height);
    /* AW
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (handle, rect);
	trim.x -= rect.x;
	trim.y -= rect.y;
	trim.width += rect.x;
	trim.height += rect.y;
    */
	/* AW
	if ((style & (SWT.MULTI | SWT.BORDER)) != 0) trim.height += 3;
	*/
	// AW
	if ((style & SWT.BORDER) != 0) {
		trim.x -= FOCUS_BORDER;
		trim.y -= FOCUS_BORDER;
		trim.width += FOCUS_BORDER;
		trim.height += FOCUS_BORDER;
	}
	// AW
	return trim;
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
	checkWidget();
	OS.TXNCopy(fTX);
}
void createHandle (int index) {
	state |= HANDLE;
    /* AW
	int [] argList1 = {
		OS.XmNverifyBell, 0,
		OS.XmNeditMode, (style & SWT.SINGLE) != 0 ? OS.XmSINGLE_LINE_EDIT : OS.XmMULTI_LINE_EDIT,
		OS.XmNscrollHorizontal, (style & SWT.H_SCROLL) != 0 ? 1 : 0,
		OS.XmNscrollVertical, (style & SWT.V_SCROLL) != 0 ? 1 : 0,
		OS.XmNwordWrap, (style & SWT.WRAP) != 0 ? 1: 0,
		OS.XmNeditable, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
		OS.XmNcursorPositionVisible, (style & SWT.READ_ONLY) != 0 && (style & SWT.SINGLE) != 0 ? 0 : 1,
		OS.XmNancestorSensitive, 1,
	};
    */
	int frameOptions= OS.kTXNDontDrawCaretWhenInactiveMask | OS.kTXNMonostyledTextMask;
	if ((style & SWT.H_SCROLL) != 0)
		frameOptions |= OS.kTXNWantHScrollBarMask;
	if ((style & SWT.V_SCROLL) != 0)
		frameOptions |= OS.kTXNWantVScrollBarMask;
	if ((style & SWT.SINGLE) != 0)
		frameOptions |= OS.kTXNSingleLineOnlyMask;
	if ((style & SWT.READ_ONLY) != 0)
		frameOptions |= OS.kTXNReadOnlyMask;
	if ((style & SWT.WRAP) != 0)
		frameOptions |= OS.kTXNAlwaysWrapAtViewEdgeMask;
	
	int parentHandle= parent.handle;
    handle= OS.NewControl(0, new Rect(), null, false, (short)(OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick), (short)0, (short)0, (short)OS.kControlUserPaneProc, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	MacUtil.insertControl(handle, parentHandle, -1);
	OS.HIViewSetVisible(handle, true);
	
	/*
	 * Since MLTE is no real control it must embed its scrollbars in the root control.
	 * However, this breaks SWT assumption that everything is a nice and clean hierarchy.
	 * To work around this problem we try to move the scrollbars from the root control
	 * to the real parent of the Text widget.
	 * This is done in two steps: before creating the MLTE object with TXNNewObject
	 * we count the number of controls under the root control. Second step: see below.
	 */
	int wHandle= OS.GetControlOwner(parentHandle);	
	int[] rootHandle= new int[1];
	OS.GetRootControl(wHandle, rootHandle);
	int root= rootHandle[0];
	short[] cnt= new short[1];
	OS.CountSubControls(root, cnt);
	short oldCount= cnt[0];
	
	/*
	 * Create the MLTE object (and possibly 0-2 scrollbars)
	 */
	int frameType= OS.kTXNTextEditStyleFrameType;
	int iFileType= OS.kTXNUnicodeTextFile;
	int iPermanentEncoding= OS.kTXNSystemDefaultEncoding;
	int[] tnxObject= new int[1];
	int[] frameID= new int[1];
	Rect bounds= new Rect();
	MacUtil.getControlBounds(handle, bounds);
	int status= OS.TXNNewObject(0, wHandle, bounds, frameOptions, frameType, iFileType,
					iPermanentEncoding, tnxObject, frameID, handle);
	if (status != OS.noErr)
		error(SWT.ERROR_NO_HANDLES);
		
	/*
	 * Second step: count the controls under root again to find out how many
	 * scrollbars had been added. Then move these new controls under the user pane
	 */
	short[] newCnt= new short[1];
	OS.CountSubControls(root, newCnt);
	short newCount= newCnt[0];
	int[] child= new int[1];
	for (short i= newCount; i > oldCount; i--) {
		int rc= OS.GetIndexedSubControl(root, i, child);
		//OS.HIViewRemoveFromSuperview(child[0]);
		OS.HIViewAddSubview(handle, child[0]);
		//WidgetTable.put(child[0], this);
	}
	
	fTX= tnxObject[0];
	fFrameID= frameID[0];
	OS.TXNActivate(fTX, fFrameID, OS.kScrollBarsSyncWithFocus);
	
	OS.TXNFocus(fTX, false);
	/*
	 * If the widget remains empty the caret will be too short.
	 * As a workaround initialize the widget with a single character
	 * and immediately remove it afterwards.
	 */
	OS.TXNSetData(fTX, OS.kTXNUnicodeTextData, new char[] { ' ' }, 2, 0, 0);
	OS.TXNSetData(fTX, OS.kTXNUnicodeTextData, new char[0], 0, 0, 1);
	
	OS.setTXNMargins(fTX, (short)MARGIN);

	OS.TXNSetTXNObjectControls(fTX, false, 1, new int[] { OS.kTXNDoFontSubstitution }, new int[] { 1 });
}	
ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}
/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget();
	OS.TXNCut(fTX);
}
void destroyWidget () {
	super.destroyWidget();
	if (fTX != 0) {
		//System.out.println("Text.destroyWidget");
		OS.TXNDeleteObject(fTX);
		fTX= 0;
	}
}
int defaultBackground () {
	return getDisplay ().textBackground;
}
Font defaultFont () {
	return getDisplay ().textFont;
}
int defaultForeground () {
	return getDisplay ().textForeground;
}
/**
 * Gets the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget();
    /* AW
	return getLineNumber (OS.XmTextGetInsertionPosition (handle));
    */
	System.out.println("Text.getCaretLineNumber: nyi");
    return 1;
}
/**
 * Gets the location the caret.
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
	Rect bounds= new Rect();
	OS.GetControlBounds(handle, bounds);
	int [] start= new int [1], end= new int [1];
	OS.TXNGetSelection(fTX, start, end);
	org.eclipse.swt.internal.carbon.Point loc= new org.eclipse.swt.internal.carbon.Point();
	OS.TXNOffsetToPoint(fTX, end[0], loc);
	Point p= new Point(loc.h, loc.v);
	p.x-= bounds.left;
	p.y-= bounds.top;
	return p;
}
/**
 * Gets the position of the caret.
 * <p>
 * The character position of the caret is returned.
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
    /* AW
	return OS.XmTextGetInsertionPosition (handle);
    */
	int [] start= new int [1], end= new int [1];
	OS.TXNGetSelection(fTX, start, end);
	return end[0];
}
/**
 * Gets the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget();
	return OS.TXNDataSize(fTX) / 2;
}
/**
 * Gets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget();
	System.out.println("Text.getDoubleClickEnabled: nyi");
    return true;
}
/**
 * Gets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public char getEchoChar () {
	checkWidget();
	return echoCharacter;
}
/**
 * Gets the editable state.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable () {
	checkWidget();
	/*
	* Bug in MOTIF.  For some reason, when XmTextGetEditable () is called
	* from inside an XmNvalueChangedCallback or XmNModifyVerifyCallback,
	* it always returns TRUE.  Calls to XmTextGetEditable () outside of
	* these callbacks return the correct value.  The fix is to query the
	* resource directly instead of using the convenience function.
	*/
    /* AW
	int [] argList = {OS.XmNeditable, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
    */
	System.out.println("Text.getEditable: nyi");
	return true;
}
/**
 * Gets the number of lines.
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
	int[] lineTotal= new int[1];
	OS.TXNGetLineCount(fTX, lineTotal);
	return lineTotal[0];
}
/**
 * Gets the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getLineDelimiter () {
	checkWidget();
	return DELIMITER;
}
/**
 * Gets the height of a line.
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
	return getFontHeight ();
}
int getLineNumber (int position) {
	if (position == 0) return 0;
	int count = 0;
	/* AW
	int count = 0, start = 0, page = 1024;
	char [] buffer = new char [page + 1];
	*/
	/*
	* Bug in Linux.  For some reason, XmTextGetSubstringWcs () does
	* not copy wchar_t characters into the buffer.  Instead, it
	* copies 4 bytes per character.  This does not happen on other
	* platforms such as AIX.  The fix is to call XmTextGetSubstring ()
	* instead on Linux and rely on the fact that Metrolink Motif 1.2
	* does not support multibyte locales.
	*/
    /* AW
	byte [] buffer1 = null;
	if (OS.IsLinux) buffer1 = new byte [page + 1];
	int end = ((position + page - 1) / page) * page;
	while (start < end) {
		int length = page;
		if (start + page > position) length = position - start;
		if (echoCharacter != '\0') {
			hiddenText.getChars (start, start + length, buffer, 0);
		} else {
			if (OS.IsLinux) {
				OS.XmTextGetSubstring (handle, start, length, buffer1.length, buffer1);
				for (int i=0; i<length; i++) buffer [i] = (char) buffer1 [i];
			} else {
				OS.XmTextGetSubstringWcs (handle, start, length, buffer.length, buffer);
			}
		}
		for (int i=0; i<length; i++) {
			if (buffer [i] == '\n') count++;
		}
		start += page;
	}
    */
	System.out.println("Text.getLineNumber: nyi");
	return count;
}
/**
 * Gets the position of the selected text.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p>
 * 
 * @return the start and end of the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget();
	int [] start = new int [1], end = new int [1];
	OS.TXNGetSelection(fTX, start, end);
	return new Point (start [0], end [0]);
}
/**
 * Gets the number of selected characters.
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
	int [] start = new int [1], end = new int [1];
	OS.TXNGetSelection(fTX, start, end);
	return end [0] - start [0];
}
/**
 * Gets the selected text.
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
	return getTXNText(OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection);
}
/**
 * Gets the number of tabs.
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
	System.out.println("Text.getTabs: nyi");
	return 8;
}
/**
 * Gets the widget text.
 * <p>
 * The text for a text widget is the characters in the widget.
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
	return getTXNText(OS.kTXNStartOffset, OS.kTXNEndOffset);
}
/**
 * Gets a range of text.
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
	checkWidget();
	int numChars = end - start + 1;
	if (numChars < 0 || start < 0) return "";
	return getTXNText(start, end);
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
 */
public int getTextLimit () {
	checkWidget();
    return fTextLimit;
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
	if (scrolledHandle == 0) return 0;
    /* AW
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return 0;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	return argList2 [1];
    */
	System.out.println("Text.getTopIndex: nyi");
    return 0;
}
/**
 * Gets the top pixel.
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
	return getTopIndex () * getLineHeight ();
}
boolean getWrap () {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNwordWrap, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
    */
	System.out.println("Text.getWrap: nyi");
    return false;
}
void hookEvents () {
	super.hookEvents ();
    /* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, SWT.DefaultSelection);
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Modify);
	OS.XtAddCallback (handle, OS.XmNmodifyVerifyCallback, windowProc, SWT.Verify);
    */
	Display display= getDisplay();		
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneHitTestProcTag, 4, new int[]{display.fUserPaneHitTestProc});
	OS.InstallEventHandler(OS.GetControlEventTarget(handle), display.fControlProc, 1,
		new int[] {
			OS.kEventClassControl, OS.kEventControlDraw,
		},
		handle, null);
}
/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.convertToLf (string);
	if (hooks (SWT.Verify)) {
		int [] start = new int [1], end = new int [1];
		OS.TXNGetSelection(fTX, start, end);
		string = verifyText (string, start [0], end [0]);
		if (string == null) return;
	}
	/* AW
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	*/
	replaceTXNText(OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, string);
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
	OS.TXNPaste(fTX);
}
int processFocusIn () {
	super.processFocusIn ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.READ_ONLY) != 0) return 0;
	
	drawFrame(null);
	OS.TXNFocus(fTX, true);
	
	if ((style & SWT.MULTI) != 0) return 0;
    /* AW
	int [] argList = {OS.XmNcursorPositionVisible, 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
	return 0;
}
int processFocusOut () {
	super.processFocusOut ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.READ_ONLY) != 0) return 0;
	
	//fgTextInFocus= null;
	OS.TXNFocus(fTX, false);
	drawFrame(null);
	
	if ((style & SWT.MULTI) != 0) return 0;
    /* AW
	int [] argList = {OS.XmNcursorPositionVisible, 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
	return 0;
}
int processMouseDown (MacMouseEvent mmEvent) {
	if (isEnabled()) {
		int macEvent[]= mmEvent.toOldMacEvent();
		if (macEvent != null)
			OS.TXNClick(fTX, macEvent);
	}
	return 0;
}
int processPaint (Object callData) {
	syncBounds();
	drawFrame(callData);
	return 0;
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
	OS.TXNSelectAll(fTX);
}
/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
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
	System.out.println("Text.setDoubleClickEnabled: nyi");
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
 * the default echo character for the platform
 * is used.
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
	if (echoCharacter == echo) return;
	echoCharacter = echo;
	OS.TXNEchoMode(fTX, echo, 0, echo != '\0');
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
    /* AW
	OS.XmTextSetEditable (handle, editable);
	int isEditable= editable ? 1 : 0;
	*/
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
	if ((style & SWT.MULTI) != 0) return;
	/*
	int [] argList = {OS.XmNcursorPositionVisible, editable && hasFocus () ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
}
/**
 * Sets the redraw flag.
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	super.setRedraw(redraw);
// AW
//	if (redraw) {
//		if (--drawCount == 0) ; /* AW OS.XmTextEnableRedisplay(handle); */
//	} else {
//		if (drawCount++ == 0) ; /* AW OS.XmTextDisableRedisplay(handle); */
//	}
// AW
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
	OS.TXNSetSelection(fTX, start, start);
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
	OS.TXNSetSelection(fTX, start, end);
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
	System.out.println("Text.setTabs: nyi");
}
/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 *
 * @param text the new text
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
	string = Display.convertToLf (string);
	if (hooks (SWT.Verify)) {
		int length = OS.TXNDataSize(fTX) / 2;
		string = verifyText (string, 0, length);
		if (string == null) return;
	}
	replaceTXNText(OS.kTXNStartOffset, OS.kTXNEndOffset, string);
	
	showBeginning();
}
/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
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
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	fTextLimit= limit;
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
	if (scrolledHandle == 0) return;
    /* AW
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	OS.XmTextScroll (handle, index - argList2 [1]);
    */
	System.out.println("Text.setTopIndex: nyi");
}
void setWrap (boolean wrap) {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNwordWrap, wrap ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
	System.out.println("Text.setWrap: nyi");
}
/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget();
	OS.TXNShowSelection(fTX, false);
}
int traversalCode () {
	int bits = super.traversalCode ();
	if ((style & SWT.READ_ONLY) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		/* AW
		if (key == OS.XK_Tab && xEvent != null) {
			boolean next = (xEvent.state & OS.ShiftMask) == 0;
			if (next && (xEvent.state & OS.ControlMask) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
		*/
	}
	return bits;
}
String verifyText (String string, int start, int end) {
	return verifyText (string, start, end, null);
}
String verifyText (String string, int start, int end, Event keyEvent) {

	int size= (OS.TXNDataSize(fTX) / 2) - (end-start);
	if (size + string.length() > fTextLimit)
		return null;

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
///////////////////////////////////////////
// Mac Stuff
///////////////////////////////////////////
	
	private void replaceTXNText(int start, int end, String s) {
		int l= s.length();
		char[] chars= new char[l];
		s.getChars(0, l, chars, 0); 
		OS.TXNSetData(fTX, OS.kTXNUnicodeTextData, chars, chars.length * 2, start, end);
		
		//syncBounds();
		
		sendEvent (SWT.Modify);
	}
		
	private String getTXNText(int start, int end) {
		int[] dataHandle= new int[1];
		OS.TXNGetData(fTX, start, end, dataHandle);
		int length= OS.GetHandleSize(dataHandle[0]);
		if (length <= 0)
			return "";
		char[] chars= new char[length/2];
		OS.getHandleData(dataHandle[0], chars);
		OS.DisposeHandle(dataHandle[0]);
		return new String(chars);
	}
	
	int sendKeyEvent(int type, MacEvent mEvent, Event event) {
	
		int status= OS.noErr;	// we handled the event
		
		if ((mEvent.getModifiers() & OS.cmdKey) != 0) {
			int kind= mEvent.getKind();
			int code= mEvent.getKeyCode();
			switch (code) {
			case 0:
				if (kind == OS.kEventRawKeyDown)
					selectAll();
				return status;
			case 7:
				if (kind == OS.kEventRawKeyDown)
					cut();
				return status;
			case 8:
				if (kind == OS.kEventRawKeyDown)
					copy();
				return status;
			case 9:
				if (kind == OS.kEventRawKeyDown || kind == OS.kEventRawKeyRepeat)
					paste();
				return status;
			default:
				break;
			}
		}
		
		int eRefHandle= mEvent.getEventRef();
		int nextHandler= mEvent.getNextHandler();

		if (hooks (SWT.Verify)) {

			// extract characters from event
			String unicode= mEvent.getText();
			String text= unicode != null ? unicode : "";
			String original= new String(text);
			
			// send verify event
			int[] start= new int[1], end= new int[1];
			OS.TXNGetSelection(fTX, start, end);
			
			if (text.length() == 1) {
				switch (text.charAt(0)) {
				case 0x08:
					if (start[0] == end[0]) {
						if (start[0] == 0)
							return status;
						if (start[0] > 0)
							start[0]--;
					}
					break;
				}
			}
			
			String string= verifyText(original, start[0], end[0], event);
			if (string == null)
				return status;	// ignore event
				
			int l= string.length();
			char[] newChars= new char[l];
			string.getChars(0, l, newChars, 0);		
			if (true) {
				OS.SetEventParameter(eRefHandle, OS.kEventParamTextInputSendText, OS.typeUnicodeText, newChars.length * 2, newChars);
				status= OS.CallNextEventHandler(nextHandler, eRefHandle);
			} else {
				OS.TXNSetSelection(fTX, start[0], end[0]);
				OS.TXNSetData(fTX, OS.kTXNUnicodeTextData, newChars, newChars.length * 2, OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection);
				OS.TXNSetSelection(fTX, start[0], start[0]+newChars.length);
			}
		} else {
			status= OS.CallNextEventHandler(nextHandler, eRefHandle);
		}
				
		sendEvent (SWT.Modify);
		
		return status;
	}

	void handleResize(int hndl, Rect bounds) {
		super.handleResize(hndl, bounds);
		syncBounds();
	}
	
	/**
	 * Synchronize the size of the MLTEtext with the underlying HIView.	 */
	private void syncBounds() {
		
		if (fTX == 0)
			return;
		
		Rect b= new Rect();
		MacUtil.getControlBounds(handle, b);
		
		// this is just too hard to explain...
		OS.HIViewSetBoundsOrigin(handle, b.left, b.top);
				
		if ((style & SWT.BORDER) != 0) {
			b.left+= FOCUS_BORDER;
			b.top+= FOCUS_BORDER;
			b.right-= FOCUS_BORDER;
			b.bottom-= FOCUS_BORDER;
		}
		
		Rectangle oldRect= fFrameBounds;
		fFrameBounds= new Rectangle(b.left, b.top, b.right-b.left, b.bottom-b.top);
		if (oldRect == null || !oldRect.equals(fFrameBounds)) {
			OS.TXNSetFrameBounds(fTX, b.top, b.left, b.bottom, b.right, fFrameID);
		}
		
		OS.TXNDraw(fTX, 0);
	}
	
	private void drawFrame(Object callData) {

		if ((style & SWT.BORDER) == 0)
			return;
			
		GC gc= new GC(this);
		int damageRegion= 0;
		if (callData instanceof MacControlEvent)
			damageRegion= ((MacControlEvent)callData).getDamageRegionHandle();
		try {
			Rectangle r= gc.carbon_focus(damageRegion);
			if (!r.isEmpty()) {
				Rect bounds= new Rect();
				OS.GetControlBounds(handle, bounds);
				OS.SetRect(bounds, (short)0, (short)0, (short)(bounds.right - bounds.left), (short)(bounds.bottom - bounds.top));
				int m= FOCUS_BORDER;
				bounds.left+= m;
				bounds.top+= m;
				bounds.right-= m;
				bounds.bottom-= m+1;
				
				Rect fbounds= new Rect();
				OS.GetControlBounds(handle, fbounds);
				OS.SetRect(fbounds, (short)0, (short)0, (short)(fbounds.right - fbounds.left), (short)(fbounds.bottom - fbounds.top));
				int fm= FOCUS_BORDER;
				fbounds.left+= fm;
				fbounds.top+= fm+1;
				fbounds.right-= fm;
				fbounds.bottom-= fm+1;
				
				if ((style & SWT.READ_ONLY) == 0) {
					if (getDisplay().getFocusControl() == this) {
						OS.DrawThemeEditTextFrame(bounds, OS.kThemeStateActive);
						OS.DrawThemeFocusRect(fbounds, true);
					} else {
						OS.DrawThemeFocusRect(fbounds, false);
						OS.DrawThemeEditTextFrame(bounds, OS.kThemeStateActive);
					}
				} else {
					OS.DrawThemeEditTextFrame(bounds, OS.kThemeStateActive);
				}
				
			}
		} finally {
			gc.carbon_unfocus();
		}
		showBeginning();
	}
	
	private void showBeginning() {
		int[] start= new int[1], end= new int [1];
		OS.TXNGetSelection(fTX, start, end);
		if (start[0] != 0 || end[0] != 0) {
			OS.TXNSetSelection(fTX, 0, 0);
			OS.TXNShowSelection(fTX, false);
			OS.TXNSetSelection(fTX, start[0], end[0]);
		} else {
			OS.TXNShowSelection(fTX, false);
		}
	}
}

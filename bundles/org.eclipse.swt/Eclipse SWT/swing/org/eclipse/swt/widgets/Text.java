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


import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.RootPaneContainer;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.swing.CText;
import org.eclipse.swt.internal.swing.TextFilterEvent;
import org.eclipse.swt.internal.swing.UIThreadUtils;

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
	int tabs, oldStart, oldEnd;
	boolean doubleClick, ignoreModify, ignoreCharacter;
	
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
	
	/*
	* This code is intentionally commented.
	*/
//	static final char PASSWORD;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = System.getProperty("line.separator");//"\n";
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
	checkWidget ();
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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
	checkWidget ();
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  CText cText = (CText)handle;
  String text = cText.getText();
//	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
//    int length = text.length();
//		string = verifyText (string, length, length, null);
//		if (string == null) return;
//	}
  cText.setText(text + string);
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

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget ();
  CText cText = (CText)handle;
  cText.setSelectionStart(0);
  cText.setSelectionStart(0);
}

//public Point computeSize (int wHint, int hHint, boolean changed) {
//	checkWidget ();
//	int height = 0, width = 0;
//	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
//		int newFont, oldFont = 0;
//		int hDC = OS.GetDC (handle);
//		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
//		TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
//		OS.GetTextMetrics (hDC, tm);
//		int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
//		height = count * tm.tmHeight;
//		RECT rect = new RECT ();
//		int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
//		boolean wrap = (style & SWT.MULTI) != 0 && (style & SWT.WRAP) != 0;
//		if (wrap && wHint != SWT.DEFAULT) {
//			flags |= OS.DT_WORDBREAK;
//			rect.right = wHint;
//		}
//		int length = OS.GetWindowTextLength (handle);
//		if (length != 0) {
//			TCHAR buffer = new TCHAR (getCodePage (), length + 1);
//			OS.GetWindowText (handle, buffer, length + 1);
//			OS.DrawText (hDC, buffer, length, rect, flags);
//			width = rect.right - rect.left;
//		}
//		if (wrap && hHint == SWT.DEFAULT) {
//			int newHeight = rect.bottom - rect.top;
//			if (newHeight != 0) height = newHeight;
//		}
//		if (newFont != 0) OS.SelectObject (hDC, oldFont);
//		OS.ReleaseDC (handle, hDC);
//	}
//	if (width == 0) width = DEFAULT_WIDTH;
//	if (height == 0) height = DEFAULT_HEIGHT;
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
//	Rectangle trim = computeTrim (0, 0, width, height);
//	return new Point (trim.width, trim.height);
//}
//
//public Rectangle computeTrim (int x, int y, int width, int height) {
//	checkWidget ();
//	Rectangle rect = super.computeTrim (x, y, width, height);
//	/*
//	* The preferred height of a single-line text widget
//	* has been hand-crafted to be the same height as
//	* the single-line text widget in an editable combo
//	* box.
//	*/
//	int margins = OS.SendMessage(handle, OS.EM_GETMARGINS, 0, 0);
//	rect.x -= margins & 0xFFFF;
//	rect.width += (margins & 0xFFFF) + ((margins >> 16) & 0xFFFF);
//	if ((style & SWT.H_SCROLL) != 0) rect.width++;
//	if ((style & SWT.BORDER) != 0) {
//		rect.x -= 1;
//		rect.y -= 1;
//		rect.width += 2;
//		rect.height += 2;
//	}
//	return rect;
//}

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
  ((CText)handle).copy();
}

Container createHandle () {
  return (Container)CText.Factory.newInstance(this, style);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	setTabStops (tabs = 8);
//	fixAlignment ();
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
  ((CText)handle).cut();
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
	checkWidget ();
  return ((CText)handle).getCaretLineNumber();
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
	checkWidget ();
  java.awt.Point point = ((CText)handle).getCaretLocation();
  return new Point(point.x, point.y);
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
	checkWidget ();
  return ((CText)handle).getCaretPosition();
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
  return ((CText)handle).getText().length();
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
	checkWidget ();
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
	checkWidget ();
  return ((CText)handle).getEchoChar();
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
	checkWidget ();
  return ((CText)handle).isEditable();
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
	checkWidget ();
  return ((CText)handle).getLineCount();
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
	checkWidget ();
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
	checkWidget ();
  return ((CText)handle).getRowHeight();
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
	checkWidget ();
  CText cText = (CText)handle;
  return new Point(cText.getSelectionStart(), cText.getSelectionEnd());
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
	checkWidget ();
	Point selection = getSelection ();
	return selection.y - selection.x;
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
	checkWidget ();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	Point selection = getSelection ();
	return getText ().substring (selection.x, selection.y);
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
	checkWidget ();
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
	checkWidget ();
  return ((CText)handle).getText();
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
  CText cText = (CText)handle;
	int length = cText.getText().length();
  end = Math.min (end, length - 1);
	start = Math.max (0, start);
  if(start > end) {
    return "";
  }
  try {
    return cText.getText(start, end - start);
  } catch(BadLocationException e) {
  }
	return "";
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
	checkWidget ();
  return ((CText)handle).getTextLimit();
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
  return ((CText)handle).getViewPosition().y / getLineHeight();
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
	checkWidget ();
  return ((CText)handle).getViewPosition().y;
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  CText cText = (CText)handle;
//	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
//		string = verifyText (string, cText.getSelectionStart(), cText.getSelectionEnd(), null);
//		if (string == null) return;
//	}
  cText.replaceSelection(string);
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
  ((CText)handle).paste();
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
	checkWidget ();
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	checkWidget ();
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
	checkWidget ();
  ((CText)handle).selectAll();
}

//boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
//	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
//		return false;
//	}
//	if ((style & SWT.READ_ONLY) != 0) return true;
//	if (ignoreVerify) return true;
//	if (type != SWT.KeyDown) return true;
//	if (msg != OS.WM_CHAR && msg != OS.WM_KEYDOWN && msg != OS.WM_IME_CHAR) {
//		return true;
//	}
//	if (event.character == 0) return true;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return true;
//	char key = event.character;
//	int stateMask = event.stateMask;
//	
//	/*
//	* Disable all magic keys that could modify the text
//	* and don't send events when Alt, Shift or Ctrl is
//	* pressed.
//	*/
//	switch (msg) {
//		case OS.WM_CHAR:
//			if (key != 0x08 && key != 0x7F && key != '\r' && key != '\t' && key != '\n') break;
//			// FALL THROUGH
//		case OS.WM_KEYDOWN:
//			if ((stateMask & (SWT.ALT | SWT.SHIFT | SWT.CONTROL)) != 0) return false;
//			break;
//	}
//
//	/*
//	* If the left button is down, the text widget refuses the character.
//	*/
//	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
//		return true;
//	}
//
//	/* Verify the character */
//	String oldText = "";
//	int [] start = new int [1], end = new int [1];
//	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//	switch (key) {
//		case 0x08:	/* Bs */
//			if (start [0] == end [0]) {
//				if (start [0] == 0) return true;
//				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
//				if (start [0] == lineStart) {
//					start [0] = start [0] - DELIMITER.length ();
//				} else {
//					start [0] = start [0] - 1;
//					if (OS.IsDBLocale) {
//						int [] newStart = new int [1], newEnd = new int [1];
//						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
//						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
//						if (start [0] != newStart [0]) start [0] = start [0] - 1;
//					}
//				}
//				start [0] = Math.max (start [0], 0);
//			}
//			break;
//		case 0x7F:	/* Del */
//			if (start [0] == end [0]) {
//				int length = OS.GetWindowTextLength (handle);
//				if (start [0] == length) return true;
//				int line = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, end [0], 0);
//				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, line + 1, 0);
//				if (end [0] == lineStart - DELIMITER.length ()) {
//					end [0] = end [0] + DELIMITER.length ();
//				} else {
//					end [0] = end [0] + 1;
//					if (OS.IsDBLocale) {
//						int [] newStart = new int [1], newEnd = new int [1];
//						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
//						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
//						if (end [0] != newEnd [0]) end [0] = end [0] + 1;
//					}
//				}
//				end [0] = Math.min (end [0], length);
//			}
//			break;
//		case '\r':	/* Return */
//			if ((style & SWT.SINGLE) != 0) return true;
//			oldText = DELIMITER;
//			break;
//		default:	/* Tab and other characters */
//			if (key != '\t' && key < 0x20) return true;
//			oldText = new String (new char [] {key});
//			break;
//	}
//	String newText = verifyText (oldText, start [0], end [0], event);
//	if (newText == null) return false;
//	if (newText == oldText) return true;
//	newText = Display.withCrLf (newText);
//	TCHAR buffer = new TCHAR (getCodePage (), newText, true);
//	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
//	/*
//	* Feature in Windows.  When an edit control with ES_MULTILINE
//	* style that does not have the WS_VSCROLL style is full (i.e.
//	* there is no space at the end to draw any more characters),
//	* EM_REPLACESEL sends a WM_CHAR with a backspace character
//	* to remove any further text that is added.  This is an
//	* implementation detail of the edit control that is unexpected
//	* and can cause endless recursion when EM_REPLACESEL is sent
//	* from a WM_CHAR handler.  The fix is to ignore calling the
//	* handler from WM_CHAR.
//	*/
//	ignoreCharacter = true;
//	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
//	ignoreCharacter = false;
//	return false;
//}
//

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
	checkWidget ();
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
	checkWidget ();
	if ((style & SWT.MULTI) != 0) return;
  ((CText)handle).setEchoChar(echo);
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
	checkWidget ();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
  ((CText)handle).setEditable(editable);
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
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
  ComponentOrientation o;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
    o = ComponentOrientation.RIGHT_TO_LEFT;
	} else {
    o = ComponentOrientation.LEFT_TO_RIGHT;
	}
  ((CText)handle).setComponentOrientation(o);
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
	checkWidget ();
  ((CText)handle).setSelectionStart(start);
  ((CText)handle).setSelectionEnd(start);
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
	checkWidget ();
  ((CText)handle).setSelectionStart(start);
  ((CText)handle).setSelectionEnd(end);
}

//public void setRedraw (boolean redraw) {
//	checkWidget ();
//	super.setRedraw (redraw);
//	/*
//	* Feature in Windows.  When WM_SETREDRAW is used to turn
//	* redraw off, the edit control is not scrolled to show the
//	* i-beam.  The fix is to detect that the i-beam has moved
//	* while redraw is turned off and force it to be visible
//	* when redraw is restored.
//	*/
//	if (drawCount != 0) return;
//	int [] start = new int [1], end = new int [1];
//	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//	if (!redraw) {
//		oldStart = start [0];  oldEnd = end [0];
//	} else {
//		if (oldStart == start [0] && oldEnd == end [0]) return;
//		OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
//	}
//}

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
	checkWidget ();
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
	checkWidget ();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
  ((CText)handle).setTabSize(tabs);
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  CText cText = (CText)handle;
//	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
//		int length = cText.getText().length();
//		string = verifyText (string, 0, length, null);
//		if (string == null) return;
//	}
  cText.setText(string);
	if ((style & SWT.MULTI) != 0) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
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
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
  ((CText)handle).setTextLimit(limit);
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
  CText cText = (CText)handle;
  cText.setViewPosition(new java.awt.Point(cText.getViewPosition().x, index / getLineHeight()));
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
	checkWidget ();
  ((CText)handle).showSelection();
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
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

//int wcsToMbcsPos (int wcsPos) {
//	if (wcsPos <= 0) return 0;
//	if (OS.IsUnicode) return wcsPos;
//	int cp = getCodePage ();
//	int wcsTotal = 0, mbcsTotal = 0;
//	byte [] buffer = new byte [128];
//	String delimiter = getLineDelimiter ();
//	int delimiterSize = delimiter.length ();
//	int count = OS.SendMessageA (handle, OS.EM_GETLINECOUNT, 0, 0);
//	for (int line=0; line<count; line++) {
//		int wcsSize = 0;
//		int linePos = OS.SendMessageA (handle, OS.EM_LINEINDEX, line, 0);
//		int mbcsSize = OS.SendMessageA (handle, OS.EM_LINELENGTH, linePos, 0);
//		if (mbcsSize != 0) {
//			if (mbcsSize + delimiterSize > buffer.length) {
//				buffer = new byte [mbcsSize + delimiterSize];
//			}
//			//ENDIAN
//			buffer [0] = (byte) (mbcsSize & 0xFF);
//			buffer [1] = (byte) (mbcsSize >> 8);
//			mbcsSize = OS.SendMessageA (handle, OS.EM_GETLINE, line, buffer);
//			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, mbcsSize, null, 0);
//		}
//		if (line - 1 != count) {
//			for (int i=0; i<delimiterSize; i++) {
//				buffer [mbcsSize++] = (byte) delimiter.charAt (i);
//			}
//			wcsSize += delimiterSize;
//		}
//		if ((wcsTotal + wcsSize) >= wcsPos) {
//			wcsSize = 0;
//			int index = 0;
//			while (index < mbcsSize) {
//				if ((wcsTotal + wcsSize) == wcsPos) {
//					return mbcsTotal + index;
//				}
//				if (OS.IsDBCSLeadByte (buffer [index++])) index++;
//				wcsSize++;
//			}
//			return mbcsTotal + mbcsSize;
//		}
//		wcsTotal += wcsSize;
//		mbcsTotal += mbcsSize;
//	}
//	return mbcsTotal;
//}

//LRESULT WM_CHAR (int wParam, int lParam) {
//	if (ignoreCharacter) return null;
//	LRESULT result = super.WM_CHAR (wParam, lParam);
//	if (result != null) return result;
//	
//	/*
//	* Bug in Windows.  When the user types CTRL and BS
//	* in an edit control, a DEL character is generated.
//	* Rather than deleting the text, the DEL character
//	* is inserted into the control.  The fix is to detect
//	* this case and not call the window proc.
//	*/
//	switch (wParam) {
//		case SWT.DEL:
//			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
//				return LRESULT.ZERO;
//			}
//	}
//	
//	/*
//	* Feature in Windows.  For some reason, when the
//	* widget is a single line text widget, when the
//	* user presses tab, return or escape, Windows beeps.
//	* The fix is to look for these keys and not call
//	* the window proc.
//	*/
//	if ((style & SWT.SINGLE) != 0) {
//		switch (wParam) {
//			case SWT.CR:
//				postEvent (SWT.DefaultSelection);
//				// FALL THROUGH
//			case SWT.TAB:
//			case SWT.ESC: return LRESULT.ZERO;
//		}
//	}
//	return result;
//}
//
//LRESULT WM_CLEAR (int wParam, int lParam) {
//	LRESULT result = super.WM_CLEAR (wParam, lParam);
//	if (result != null) return result;
//	return wmClipboard (OS.WM_CLEAR, wParam, lParam);
//}
//
//LRESULT WM_CUT (int wParam, int lParam) {
//	LRESULT result = super.WM_CUT (wParam, lParam);
//	if (result != null) return result;
//	return wmClipboard (OS.WM_CUT, wParam, lParam);
//}
//
//LRESULT WM_GETDLGCODE (int wParam, int lParam) {
//	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
//	if (result != null) return result;
//	
//	/*
//	* Bug in WinCE PPC.  For some reason, sending WM_GETDLGCODE
//	* to a multi-line edit control causes it to ignore return and
//	* tab keys.  The fix is to return the value which is normally
//	* returned by the text window proc on other versions of Windows.
//	*/
//	if (OS.IsPPC) {
//		if ((style & SWT.MULTI) != 0 && (style & SWT.READ_ONLY) == 0 && lParam == 0) {
//			return new LRESULT (OS.DLGC_HASSETSEL | OS.DLGC_WANTALLKEYS | OS.DLGC_WANTCHARS);
//		}
//	}
//
//	/*
//	* Feature in Windows.  Despite the fact that the
//	* edit control is read only, it still returns a
//	* dialog code indicating that it wants all keys.  
//	* The fix is to detect this case and clear the bits.
//	* 
//	* NOTE: A read only edit control processes arrow keys
//	* so DLGC_WANTARROWS should not be cleared.
//	*/
//	if ((style & SWT.READ_ONLY) != 0) {
//		int code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
//		code &= ~(OS.DLGC_WANTALLKEYS | OS.DLGC_WANTTAB);
//		return new LRESULT (code);
//	}
//	return null;
//}
//
//LRESULT WM_IME_CHAR (int wParam, int lParam) {
//
//	/* Process a DBCS character */
//	Display display = this.display;
//	display.lastKey = 0;
//	display.lastAscii = wParam;
//	display.lastVirtual = display.lastNull = display.lastDead = false;
//	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
//		return LRESULT.ZERO;
//	}
//
//	/*
//	* Feature in Windows.  The Windows text widget uses
//	* two 2 WM_CHAR's to process a DBCS key instead of
//	* using WM_IME_CHAR.  The fix is to allow the text
//	* widget to get the WM_CHAR's but ignore sending
//	* them to the application.
//	*/
//	ignoreCharacter = true;
//	int result = callWindowProc (handle, OS.WM_IME_CHAR, wParam, lParam);
//	MSG msg = new MSG ();
//	int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
//	while (OS.PeekMessage (msg, handle, OS.WM_CHAR, OS.WM_CHAR, flags)) {
//		OS.TranslateMessage (msg);
//		OS.DispatchMessage (msg);
//	}
//	ignoreCharacter = false;
//	
//	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
//	// widget could be disposed at this point
//	display.lastKey = display.lastAscii = 0;
//	return new LRESULT (result);
//}
//
//LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
//	/*
//	* Prevent Windows from processing WM_LBUTTONDBLCLK
//	* when double clicking behavior is disabled by not
//	* calling the window proc.
//	*/
//	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	if (!doubleClick) return LRESULT.ZERO;
//		
//	/*
//	* Bug in Windows.  When the last line of text in the
//	* widget is double clicked and the line is empty, Windows
//	* hides the i-beam then moves it to the first line in
//	* the widget but does not scroll to show the user.
//	* If the user types without clicking the mouse, invalid
//	* characters are displayed at the end of each line of
//	* text in the widget.  The fix is to detect this case
//	* and avoid calling the window proc.
//	*/
//	int [] start = new int [1], end = new int [1];
//	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//	if (start [0] == end [0]) {
//		int length = OS.GetWindowTextLength (handle);
//		if (length == start [0]) {
//			int result = OS.SendMessage (handle, OS.EM_LINELENGTH, length, 0);
//			if (result == 0) return LRESULT.ZERO;
//		}
//	}
//	return null;
//}
//
//LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
//	if (!OS.IsPPC) return super.WM_LBUTTONDOWN (wParam, lParam);
//	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	/*
//	* Note: On WinCE PPC, only attempt to recognize the gesture for
//	* a context menu when the control contains a valid menu or there
//	* are listeners for the MenuDetect event.
//	* 
//	* Note: On WinCE PPC, the gesture that brings up a popup menu
//	* on the text widget must keep the current text selection.  As a
//	* result, the window proc is only called if the menu is not shown.
//	*/
//	boolean hasMenu = menu != null && !menu.isDisposed ();
//	if (hasMenu || hooks (SWT.MenuDetect)) {
//		int x = (short) (lParam & 0xFFFF);
//		int y = (short) (lParam >> 16);
//		SHRGINFO shrg = new SHRGINFO ();
//		shrg.cbSize = SHRGINFO.sizeof;
//		shrg.hwndClient = handle;
//		shrg.ptDown_x = x;
//		shrg.ptDown_y = y; 
//		shrg.dwFlags = OS.SHRG_RETURNCMD;
//		int type = OS.SHRecognizeGesture (shrg);
//		if (type == OS.GN_CONTEXTMENU) {
//			showMenu (x, y);
//			return LRESULT.ONE;
//		}
//	}
//	int result = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	return new LRESULT (result);
//}
//
//LRESULT WM_PASTE (int wParam, int lParam) {
//	LRESULT result = super.WM_PASTE (wParam, lParam);
//	if (result != null) return result;
//	return wmClipboard (OS.WM_PASTE, wParam, lParam);
//}
//
//LRESULT WM_UNDO (int wParam, int lParam) {
//	LRESULT result = super.WM_UNDO (wParam, lParam);
//	if (result != null) return result;
//	return wmClipboard (OS.WM_UNDO, wParam, lParam);
//}
//
//LRESULT wmClipboard (int msg, int wParam, int lParam) {
//	if ((style & SWT.READ_ONLY) != 0) return null;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
//	boolean call = false;
//	int [] start = new int [1], end = new int [1];
//	String newText = null;
//	switch (msg) {
//		case OS.WM_CLEAR:
//		case OS.WM_CUT:
//			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//			if (start [0] != end [0]) {
//				newText = "";
//				call = true;
//			}
//			break;
//		case OS.WM_PASTE:
//			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//			newText = getClipboardText ();
//			break;
//		case OS.EM_UNDO:
//		case OS.WM_UNDO:
//			if (OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0) != 0) {
//				OS.SendMessage (handle, OS.EM_GETSEL, start, end);
//				ignoreModify = ignoreCharacter = true;
//				callWindowProc (handle, msg, wParam, lParam);
//				newText = getSelectionText ();
//				callWindowProc (handle, msg, wParam, lParam);
//				ignoreModify = ignoreCharacter = false;
//			}
//			break;
//	}
//	if (newText != null) {
//		String oldText = newText;
//		newText = verifyText (newText, start [0], end [0], null);
//		if (newText == null) return LRESULT.ZERO;
//		if (!newText.equals (oldText)) {
//			if (call) {
//				callWindowProc (handle, msg, wParam, lParam);
//			}
//			newText = Display.withCrLf (newText);
//			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
//			/*
//			* Feature in Windows.  When an edit control with ES_MULTILINE
//			* style that does not have the WS_VSCROLL style is full (i.e.
//			* there is no space at the end to draw any more characters),
//			* EM_REPLACESEL sends a WM_CHAR with a backspace character
//			* to remove any further text that is added.  This is an
//			* implementation detail of the edit control that is unexpected
//			* and can cause endless recursion when EM_REPLACESEL is sent
//			* from a WM_CHAR handler.  The fix is to ignore calling the
//			* handler from WM_CHAR.
//			*/
//			ignoreCharacter = true;
//			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
//			ignoreCharacter = false;
//			return LRESULT.ZERO;
//		}
//	}
//	if (msg == OS.WM_UNDO) {
//		ignoreVerify = ignoreCharacter = true;
//		callWindowProc (handle, OS.WM_UNDO, wParam, lParam);
//		ignoreVerify = ignoreCharacter = false;
//		return LRESULT.ONE;
//	}
//	return null;
//}
//
//LRESULT wmCommandChild (int wParam, int lParam) {
//	int code = wParam >> 16;
//	switch (code) {
//		case OS.EN_CHANGE:
//			if (ignoreModify) break;
//			/*
//			* It is possible (but unlikely), that application
//			* code could have disposed the widget in the modify
//			* event.  If this happens, end the processing of the
//			* Windows message by returning zero as the result of
//			* the window proc.
//			*/
//			sendEvent (SWT.Modify);
//			if (isDisposed ()) return LRESULT.ZERO;
//			break;
//		case OS.EN_ALIGN_LTR_EC:
//			style &= ~SWT.RIGHT_TO_LEFT;
//			style |= SWT.LEFT_TO_RIGHT;
//			fixAlignment ();
//			break;
//		case OS.EN_ALIGN_RTL_EC:
//			style &= ~SWT.LEFT_TO_RIGHT;
//			style |= SWT.RIGHT_TO_LEFT;
//			fixAlignment ();
//			break;
//	}
//	return super.wmCommandChild (wParam, lParam);
//}

public void processEvent(EventObject e) {
  if(e instanceof TextFilterEvent) {
    if(!hooks(SWT.Verify)) { super.processEvent(e); return; }
  } else { super.processEvent(e); return; }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    if(e instanceof TextFilterEvent) {
      TextFilterEvent filterEvent = (TextFilterEvent)e;
      filterEvent.setText(verifyText(filterEvent.getText(), filterEvent.getStart(), filterEvent.getStart() + filterEvent.getEnd(), createKeyEvent(filterEvent.getKeyEvent())));
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

protected boolean isTraversalKey(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_ENTER:
    return (style & SWT.MULTI) == 0 || ((RootPaneContainer)getShell().handle).getRootPane().getDefaultButton() != null;
  }
  return super.isTraversalKey(ke);
}

protected int getTraversalKeyDetail(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_ENTER:
    return SWT.TRAVERSE_RETURN;
  }
  return super.getTraversalKeyDetail(ke);
}

protected boolean getTraversalKeyDefault(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_TAB:
  case java.awt.event.KeyEvent.VK_ENTER:
    int modifiers = ke.getModifiers();
    if((modifiers & java.awt.event.KeyEvent.CTRL_MASK) == 0 && (modifiers & java.awt.event.KeyEvent.SHIFT_MASK) == 0) {
      return (style & SWT.MULTI) == 0;
    }
  default:
    return super.getTraversalKeyDefault(ke);
  }
}

protected void validateTraversalKey(java.awt.event.KeyEvent ke, Event event) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_ENTER:
    if(event.doit) {
      if(!hooks(SWT.DefaultSelection)) {
        if(event.detail == SWT.TRAVERSE_RETURN) {
          JButton defaultButton = ((RootPaneContainer)getShell().handle).getRootPane().getDefaultButton();
          if(defaultButton != null) {
            defaultButton.requestFocus();
            defaultButton.doClick();
          }
        }
      } else {
        sendEvent(SWT.DefaultSelection);
      }
      ke.consume();
    } else if((style & SWT.MULTI) == 0) {
      ke.consume();
    }
    break;
  default:
    super.validateTraversalKey(ke, event);
    break;
  }
}

public void processEvent(DocumentEvent e) {
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    return;
  }
  try {
    postEvent(SWT.Modify, new Event());
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}

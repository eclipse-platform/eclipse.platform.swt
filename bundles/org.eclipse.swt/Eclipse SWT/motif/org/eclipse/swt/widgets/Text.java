package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
* A Text is an editable user interface object that
* displays a single line of text or multiple lines
* of text.
* <p>
* <b>Styles</b><br>
* <dd>SINGLE, MULTI,<br>
* <dd>READ_ONLY, WRAP<br>
* <br>
* <b>Events</b><br>
* <dd>Modify<br>
* <dd>Verify<br>
*/

/* Class Definition */
public class Text extends Scrollable {
	char echoCharacter;
	boolean ignoreChange;
	String hiddenText;
	XmTextVerifyCallbackStruct textVerify;
	int drawCount;

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
* Creates a new instance of the widget.
*/
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addVerifyListener (VerifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}
/**
* Appends a string.
* <p>
*
* @param string the string to be appended
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void append (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int position = OS.XmTextGetLastPosition (handle);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetInsertionPosition (handle, position);
	OS.XmTextInsert (handle, position, buffer);
	position = OS.XmTextGetLastPosition (handle);
	OS.XmTextSetInsertionPosition (handle, position);
	display.setWarnings(warnings);
}
static int checkStyle (int style) {
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		return style | SWT.MULTI;
	}
	return style | SWT.SINGLE;
}
/**
* Clears the selection.
* <p>
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void clearSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextClearSelection (handle, OS.XtLastTimestampProcessed (xDisplay));
}
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = wHint;
	int height = hHint;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int [] argList = {OS.XmNfontList, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int ptr = OS.XmTextGetString (handle);
		if (ptr == 0) return new Point (0, 0);
		int size = OS.strlen (ptr);
		if (size == 0) {
			if (hHint == SWT.DEFAULT) {
				if ((style & SWT.SINGLE) != 0) {
					height = getLineHeight ();
				} else {
					height = DEFAULT_HEIGHT;
				}
			}
			if (wHint == SWT.DEFAULT) {
				width = DEFAULT_WIDTH;
			}
		} else {
			byte [] buffer = new byte [size + 1];
			OS.memmove (buffer, ptr, size);
			int xmString = OS.XmStringParseText (	
				buffer,
				0,
				OS.XmFONTLIST_DEFAULT_TAG, 
				OS.XmCHARSET_TEXT, 
				null,
				0,
				0);
			if (hHint == SWT.DEFAULT) {
				if ((style & SWT.SINGLE) != 0) {
					height = getLineHeight ();
				} else {
					height = OS.XmStringHeight (argList [1], xmString);
				}
			}
			if (wHint == SWT.DEFAULT) width = OS.XmStringWidth(argList [1], xmString);
			OS.XmStringFree (xmString);
		}
		OS.XtFree (ptr);
	}
	if (horizontalBar != null) {
		int [] argList1 = {OS.XmNheight, 0};
		OS.XtGetValues (horizontalBar.handle, argList1, argList1.length / 2);
		height += argList1 [1] + 4;
	}
	if (verticalBar != null) {
		int [] argList1 = {OS.XmNwidth, 0};
		OS.XtGetValues (verticalBar.handle, argList1, argList1.length / 2);
		width += argList1 [1] + 4;
	}
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (handle, rect);
	width += rect.x * 2;  height += rect.y * 2;
	if ((style & (SWT.MULTI | SWT.BORDER)) != 0) height++;
	return new Point (width, height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Rectangle trim = super.computeTrim(x, y, width, height);
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (handle, rect);
	trim.x -= rect.x;
	trim.y -= rect.y;
	trim.width += rect.x;
	trim.height += rect.y;	
	if ((style & (SWT.MULTI | SWT.BORDER)) != 0) trim.height += 3;
	return trim;
}
/**
* Copies the selected text to the clipboard.
*
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void copy () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextCopy (handle, OS.XtLastTimestampProcessed (xDisplay));
}
void createHandle (int index) {
	state |= HANDLE;
	int [] argList1 = {
		OS.XmNverifyBell, 0,
		OS.XmNeditMode, (style & SWT.SINGLE) != 0 ? OS.XmSINGLE_LINE_EDIT : OS.XmMULTI_LINE_EDIT,
		OS.XmNscrollHorizontal, (style & SWT.H_SCROLL) != 0 ? 1 : 0,
		OS.XmNscrollVertical, (style & SWT.V_SCROLL) != 0 ? 1 : 0,
		OS.XmNwordWrap, (style & SWT.WRAP) != 0 ? 1: 0,
		OS.XmNeditable, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
		OS.XmNcursorPositionVisible, (style & SWT.READ_ONLY) != 0 && (style & SWT.SINGLE) != 0 ? 0 : 1,
//		OS.XmNmarginWidth, 3,
//		OS.XmNmarginHeight, 1,
	};
	if ((style & SWT.SINGLE) != 0) {	
		handle = OS.XmCreateTextField (parent.handle, null, argList1, argList1.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = new int [] {
			OS.XmNcursorPositionVisible, 0,
		};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
		if ((style & SWT.BORDER) == 0) {
			int [] argList3 = new int [] {
				OS.XmNmarginHeight, 0,
				OS.XmNshadowThickness, 0
			};
			OS.XtSetValues (handle, argList3, argList3.length / 2);
		}
	} else {
		handle = OS.XmCreateScrolledText (parent.handle, null, argList1, argList1.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.XtParent (handle);
	}	
}
ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}
public void cut () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextCut (handle, OS.XtLastTimestampProcessed (xDisplay));
}
int defaultBackground () {
	return getDisplay ().textBackground;
}
int defaultFont () {
	return getDisplay ().textFont;
}
int defaultForeground () {
	return getDisplay ().textForeground;
}
/**
* Gets the line number of the caret.
* <p>
* The line number of the caret is returned.
*
* @return the line number
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getCaretLineNumber () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getLineNumber (OS.XmTextGetInsertionPosition (handle));
}
public Point getCaretLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int position;
	if (textVerify != null) {
		position = textVerify.currInsert;
	} else {
		position = OS.XmTextGetInsertionPosition (handle);
	}
	short [] x = new short [1], y = new short [1];
	OS.XmTextPosToXY (handle, position, x, y);
	return new Point (x [0], y [0] - getFontAscent ());
}
/**
* Gets the position of the caret.
* <p>
* The character position of the caret is returned.
*
* @return the position of the caret
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getCaretPosition () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XmTextGetInsertionPosition (handle);
}
/**
* Gets the number of characters.
* <p>
*
* @return number of characters in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getCharCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XmTextGetLastPosition (handle);
}
/**
* Gets the double click enabled flag.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getDoubleClickEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNselectionArrayCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 1;
}
/**
* Gets the echo character.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public char getEchoChar () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return echoCharacter;
}
public boolean getEditable () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Bug in MOTIF.  For some reason, when XmTextGetEditable () is called
	* from inside an XmNvalueChangedCallback or XmNModifyVerifyCallback,
	* it always returns TRUE.  Calls to XmTextGetEditable () outside of
	* these callbacks return the correct value.  The fix is to query the
	* resource directly instead of using the convenience function.
	*/
	int [] argList = {OS.XmNeditable, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Gets the number of lines.
*/
public int getLineCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getLineNumber (echoCharacter != '\0' ? hiddenText.length () : OS.XmTextGetLastPosition (handle));
}
/**
* Gets the line delimiter.
* <p>
* @return a string that is the line delimiter
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getLineDelimiter () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return "\n";
}
/**
* Gets the height of each line.
* <p>
* @return the height (in pixels) of each row in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getLineHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getFontHeight ();
}
int getLineNumber (int position) {
	if (position == 0) return 0;
	int count = 0, start = 0, page = 1024;
	char [] buffer = new char [page + 1];
	/*
	* Bug in Linux.  For some reason, XmTextGetSubstringWcs () does
	* not copy wchar_t characters into the buffer.  Instead, it 
	* copies 4 bytes per character.  This does not happen on other
	* platforms such as AIX.  The fix is to call XmTextGetSubstring ()
	* instead on Linux and rely on the fact that Metrolink Motif 1.2
	* does not support multibyte locales.
	*/
	byte [] buffer1 = null;
	if (IsLinux) buffer1 = new byte [page + 1];
	int end = ((position + page - 1) / page) * page;
	while (start < end) {
		int length = page;
		if (start + page > position) length = position - start;
		if (echoCharacter != '\0') {
			hiddenText.getChars (start, start + length, buffer, 0);
		} else {
			if (IsLinux) {
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
	return count;
}
/**
* Gets the position of the selected text.
* <p>
* @return a point (a pair of ints) that represent the beginning and end of the current selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Point getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (textVerify != null) {
		return new Point (textVerify.startPos, textVerify.endPos);
	}
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (handle);
	}
	return new Point (start [0], end [0]);
}
/**
* Gets the number of selected characters.
* <p>
* @reurn the number of selected characters.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getSelectionCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (textVerify != null) {
		return textVerify.endPos - textVerify.startPos;
	}
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	return end [0] - start [0];
}
/**
* Gets the selected text.
* <p>
* If no text is selected an empty string is returned.
*
* @return the selected text
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getSelectionText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (echoCharacter != '\0' || textVerify != null) {
		Point selection = getSelection ();
		return getText (selection.x, selection.y);
	}
	int ptr = OS.XmTextGetSelection (handle);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return new String (Converter.mbcsToWcs (null, buffer));
}
/**
* Gets the number of space characters used to represent one tab.
* <p>
* @return the number of space characters that are used to represent one tab ('\t') character
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getTabs () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/* Tabs are not supported in MOTIF. */
	return 8;
}
/**
* Gets the text.
* <p>
* If there is no text in the widget, an empty string is returned.
*
* @return the content of the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (echoCharacter != '\0') return hiddenText;
	int ptr = OS.XmTextGetString (handle);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return new String (Converter.mbcsToWcs (null, buffer));
}
/**
* Gets the text.
* <p>
* Indexing is zero based.  The range of text
* is from the start index up to and including
* the end index + 1.
*
* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int numChars = end - start + 1;
	if (numChars < 0 || start < 0) return "";
	if (echoCharacter != '\0') {
		return hiddenText.substring (start, Math.min (hiddenText.length (), end));
	}
	int length = (numChars * 4 /* MB_CUR_MAX */) + 1;
	byte [] buffer = new byte [length];
	int code = OS.XmTextGetSubstring (handle, start, numChars, length, buffer);
	if (code == OS.XmCOPY_FAILED) return "";
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	if (code == OS.XmCOPY_TRUNCATED) {
		numChars = OS.XmTextGetLastPosition (handle) - start;
	}
	return new String (unicode, 0, numChars);
}
/**
* Gets the text limit.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getTextLimit () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XmTextGetMaxLength (handle);
}
/**
* Gets the top index.
* <p>
* The top index is the index of the line that
* is currently at the top of the widget.  The
* top index changes when the widget is scrolled.
* Indexing is zero based.
*
* @return the index of the top line
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getTopIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SINGLE) != 0) return 0;
	if (scrolledHandle == 0) return 0;
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return 0;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	return argList2 [1];
}
/**
* Gets the top pixel.
* <p>
* The top pixel is the pixel position of the line
* that is currently at the top of the widget.  On
* some platforms, a text widget can be scrolled by
* pixels instead of lines so that a partial line
* is displayed at the top of the widget.
* <p>
* The top pixel changes when the widget is scrolled.
* The top pixel does not include the widget trimming.
*
* @return the pixel position of the top line
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public int getTopPixel () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getTopIndex () * getLineHeight ();
}
boolean getWrap () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNwordWrap, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, SWT.DefaultSelection);
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Modify);
	OS.XtAddCallback (handle, OS.XmNmodifyVerifyCallback, windowProc, SWT.Verify);
}
int inputContext () {
	/* Answer zero.  The text widget uses the default MOTIF input context.  */
	return 0;
}
/**
* Replaces the selection with a string.
* <p>
* If the no text is selected (the beginning and the end of the selection are equal) 
* the string is inserted.
*
* @param string the string
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void insert (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (handle);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextReplace (handle, start [0], end [0], buffer);
	int position = start [0] + buffer.length - 1;
	OS.XmTextSetInsertionPosition (handle, position);
	display.setWarnings (warnings);
}
public void paste () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	/*
	* Bug in Motif.  Despite the fact that the documentation
	* claims that XmText functions work for XmTextFields, when
	* a text field is passed to XmTextPaste, Motif segment faults.
	* The fix is to call XmTextFieldPaste instead.
	*/
	if ((style & SWT.SINGLE) != 0) {
		OS.XmTextFieldPaste (handle);
	} else {
		OS.XmTextPaste (handle);
	}
	display.setWarnings (warnings);
}
int processFocusIn () {
	super.processFocusIn ();
	if ((style & SWT.READ_ONLY) != 0) return 0;
	if ((style & SWT.MULTI) != 0) return 0;
	int [] argList = {OS.XmNcursorPositionVisible, 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
	return 0;
}
int processFocusOut () {
	super.processFocusOut ();
	if ((style & SWT.READ_ONLY) != 0) return 0;
	if ((style & SWT.MULTI) != 0) return 0;
	int [] argList = {OS.XmNcursorPositionVisible, 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
	return 0;
}
int processModify (int callData) {
	if (!ignoreChange) super.processModify (callData);
	return 0;
}
int processVerify (int callData) {
	super.processVerify (callData);
	if (echoCharacter == '\0' && !hooks (SWT.Verify)) return 0;
	XmTextVerifyCallbackStruct textVerify = new XmTextVerifyCallbackStruct ();
	OS.memmove (textVerify, callData, XmTextVerifyCallbackStruct.sizeof);
	XmTextBlockRec textBlock = new XmTextBlockRec ();
	OS.memmove (textBlock, textVerify.text, XmTextBlockRec.sizeof);
	byte [] buffer = new byte [textBlock.length];
	OS.memmove (buffer, textBlock.ptr, textBlock.length);
	String text = new String (Converter.mbcsToWcs (null, buffer));
	String newText = text;
	if (!ignoreChange) {
		Event event = new Event ();
		if (textVerify.event != 0) {
			XKeyEvent xEvent = new XKeyEvent ();
			OS.memmove (xEvent, textVerify.event, XKeyEvent.sizeof);
			setKeyState (event, xEvent);
		}
		event.start = textVerify.startPos;
		event.end = textVerify.endPos;
		event.doit = textVerify.doit == 1;
		event.text = text;
		sendEvent (SWT.Verify, event);
		newText = event.text;
		textVerify.doit = (byte) ((event.doit && newText != null) ? 1 : 0);
	}
	if (newText != null) {
		if (echoCharacter != '\0' && (textVerify.doit != 0)) {
			String prefix = hiddenText.substring (0, textVerify.startPos);
			String suffix = hiddenText.substring (textVerify.endPos, hiddenText.length ());
			hiddenText = prefix + newText + suffix;
			char [] charBuffer = new char [newText.length ()];
			for (int i=0; i<charBuffer.length; i++) {
				charBuffer [i] = echoCharacter;
			}
			newText = new String (charBuffer);
		}
		if (newText != text) {
			byte [] buffer2 = Converter.wcsToMbcs (null, newText, true);
			int length = buffer2.length;
			int ptr = OS.XtMalloc (length);
			OS.memmove (ptr, buffer2, length);
			textBlock.ptr = ptr;
			textBlock.length = buffer2.length - 1;
			OS.memmove (textVerify.text, textBlock, XmTextBlockRec.sizeof);
		}
	}
	OS.memmove (callData, textVerify, XmTextVerifyCallbackStruct.sizeof);
	textVerify = null;
	return 0;
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeVerifyListener (VerifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}
/**
* Selects all of the text.
* <p>
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void selectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (handle);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);

	/*
	* Bug in MOTIF.  XmTextSetSelection () fails to set the
	* selection when the receiver is not realized.  The fix
	* is to force the receiver to be realized by forcing the
	* shell to be realized.  If the receiver is realized before
	* the shell, MOTIF fails to draw the text widget and issues
	* lots of X BadDrawable errors.
	*/
	if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();

	/* Set the selection. */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (handle, 0, position, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	OS.XmTextSetInsertionPosition (handle, 0);
	display.setWarnings (warnings);
}
/**
* Sets the bounds.
*/
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	/*
	* Bug in Motif.  When the receiver is a Text widget
	* (not a Text Field) and is resized to be smaller than
	* the inset that surrounds the text and the selection
	* is set, the receiver scrolls to the left.  When the
	* receiver is resized larger, the text is not scrolled
	* back.  The fix is to detect this case and scroll the
	* text back.
	*/
//	inset := self inset.
//	nWidth := self dimensionAt: XmNwidth.
//	self noWarnings: [super resizeWidget].
//	nWidth > inset x ifTrue: [^self].
//	self showPosition: self topCharacter
}
/**
* Sets the double click enabled flag.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setDoubleClickEnabled (boolean doubleClick) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNselectionArrayCount, doubleClick ? 4 : 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the echo character.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setEchoChar (char echo) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (echoCharacter == echo) return;
	String newText;
	if (echo == 0) {
		newText = hiddenText;
		hiddenText = null;
	} else {
		newText = hiddenText = getText();
	}
	echoCharacter = echo;
	Point selection = getSelection();
	boolean oldValue = ignoreChange;
	ignoreChange = true;
	setText(newText);
	setSelection(selection);
	ignoreChange = oldValue;
}
public void setEditable (boolean editable) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.XmTextSetEditable (handle, editable);
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
	if ((style & SWT.MULTI) != 0) return;
	int [] argList = {OS.XmNcursorPositionVisible, editable && hasFocus () ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the redraw flag.
*/
public void setRedraw (boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SINGLE) != 0) return;
	if (redraw) {
		if (--drawCount == 0) OS.XmTextEnableRedisplay(handle);
	} else {
		if (drawCount++ == 0) OS.XmTextDisableRedisplay(handle);
	}
}
/**
* Sets the selection.
* <p>
* @param start new i-beam position
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (int start) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	/* Clear the selection and highlight before moving the i-beam. */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int position = OS.XmTextGetLastPosition (handle);
	int nStart = Math.min (Math.max (start, 0), position);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);
	OS.XmTextClearSelection (handle, OS.XtLastTimestampProcessed (xDisplay));

	/* Set the i-beam position. */
	OS.XmTextSetInsertionPosition (handle, nStart);
	display.setWarnings (warnings);
}
/**
* Sets the selection.
* <p>
* Indexing is zero based.  The range of text
* is from the start index up to and including
* the end index + 1.
*
* @param start the start of the range
* @param end the end of the range

* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (handle);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);

	/*
	* Bug in MOTIF.  XmTextSetSelection () fails to set the
	* selection when the receiver is not realized.  The fix
	* is to force the receiver to be realized by forcing the
	* shell to be realized.  If the receiver is realized before
	* the shell, MOTIF fails to draw the text widget and issues
	* lots of X BadDrawable errors.
	*/
	if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();

	/* Set the selection. */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int nStart = Math.min (Math.max (Math.min (start, end), 0), position);
	int nEnd = Math.min (Math.max (Math.max (start, end), 0), position);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (handle, nStart, nEnd, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	OS.XmTextSetInsertionPosition (handle, nEnd);
	display.setWarnings (warnings);
}
/**
* Sets the selection.
* <p>
* @param selection the point whose x and y fields specify the beginning and the end 
* of the new selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when selection is null
*/
public void setSelection (Point selection) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}
/**
* Sets the size.
*/
public void setSize (int width, int height) {
	super.setSize (width, height);
	/*
	* Bug in Motif.  When the receiver is a Text widget
	* (not a Text Field) and is resized to be smaller than
	* the inset that surrounds the text and the selection
	* is set, the receiver scrolls to the left.  When the
	* receiver is resized larger, the text is not scrolled
	* back.  The fix is to detect this case and scroll the
	* text back.
	*/
//	inset := self inset.
//	nWidth := self dimensionAt: XmNwidth.
//	self noWarnings: [super resizeWidget].
//	nWidth > inset x ifTrue: [^self].
//	self showPosition: self topCharacter
}
/**
* Sets the number of space characters used to represent one tab.
* <p>
* @return the number of space characters that are used to represent one tab ('\t') character
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setTabs (int tabs) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/* Do nothing.  Tabs are not supported in MOTIF. */
}
/**
* Sets the text.
* <p>
* @param string the new text.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (handle, buffer);
	OS.XmTextSetInsertionPosition (handle, 0);
	display.setWarnings(warnings);
	/*
	* Bug in Linux.  When the widget is multi-line
	* it does not send a Modify to notify the application
	* that the text has changed.  The fix is to send the event.
	*/
	if (IsLinux && (style & SWT.MULTI) != 0) sendEvent (SWT.Modify);
}
/**
* Sets the text limit.
* <p>
* @param limit the new text limit.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setTextLimit (int limit) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.XmTextSetMaxLength (handle, limit);
}
/**
* Sets the top index.
* <p>
* The top index is the index of the line that
* is currently at the top of the widget.  The
* top index changes when the widget is scrolled.
* Indexing starts from zero.
*
* @param index the new top index
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setTopIndex (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SINGLE) != 0) return;
	if (scrolledHandle == 0) return;
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	OS.XmTextScroll (handle, index - argList2 [1]);
}
void setWrap (boolean wrap) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNwordWrap, wrap ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Shows the selection.
* <p>
* If there is no selection or the selection
* is already visible, this method does nothing.
* If the selection is scrolled out of view,
* the top index of the widget is changed such
* that selection becomes visible.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void showSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int position = OS.XmTextGetInsertionPosition (handle);
	OS.XmTextShowPosition (handle, position);
	display.setWarnings (warnings);
}
int traversalCode () {
	if ((style & SWT.SINGLE) != 0) return super.traversalCode ();
	return SWT.TRAVERSE_ESCAPE;
}
}

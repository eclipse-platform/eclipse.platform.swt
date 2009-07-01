/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.TextRange;


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
	
	static final int UNDERLINE_IME_INPUT = 1 << 16;
	static final int UNDERLINE_IME_TARGET_CONVERTED = 2 << 16;
	static final int UNDERLINE_IME_CONVERTED = 3 << 16;
	
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
	text = "";
	startOffset = -1;
	if (parent.getIME () == null) {
		parent.setIME (this);
	}
}

/**
 * Returns the offset of the caret from the start of the document.
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
	return false; 
}

boolean isInlineEnabled () {
	return hooks (SWT.ImeComposition);
}

int kEventTextInputOffsetToPos (int nextHandler, int theEvent, int userData) {
	if (!isInlineEnabled ()) return OS.eventNotHandledErr;
	Caret caret = parent.caret;
	if (caret == null) return OS.eventNotHandledErr;
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	org.eclipse.swt.graphics.Point point = parent.toDisplay (caret.x, caret.y + caret.height);
	pt.h = (short)point.x;
	pt.v = (short)point.y;
	OS.SetEventParameter (theEvent, OS.kEventParamTextInputReplyPoint, OS.typeQDPoint, sizeof, pt);
	return OS.noErr;
}

int kEventTextInputPosToOffset (int nextHandler, int theEvent, int userData) {
	if (!isInlineEnabled ()) return OS.eventNotHandledErr;
	if (startOffset == -1) return OS.eventNotHandledErr;
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendCurrentPoint, OS.typeQDPoint, null, sizeof, null, pt);
	org.eclipse.swt.graphics.Point point = parent.toControl (pt.h, pt.v);
	Event event = new Event ();
	event.detail = SWT.COMPOSITION_OFFSET;
	event.x = point.x;
	event.y = point.y;
	sendEvent (SWT.ImeComposition, event);
	int hitTest;
	int offset = event.index + event.count;
	if (offset == -1) {
		hitTest = OS.kTSMOutsideOfBody;
	} else {
		if (startOffset <= offset && offset < startOffset + text.length()) {
			hitTest = OS.kTSMInsideOfActiveInputArea;
			offset -= startOffset;
		} else {
			hitTest = OS.kTSMInsideOfBody;
		}
	}
	OS.SetEventParameter (theEvent, OS.kEventParamTextInputReplyTextOffset, OS.typeLongInteger, 4, new int [] {offset * 2});
	OS.SetEventParameter (theEvent, OS.kEventParamTextInputReplyRegionClass, OS.typeLongInteger, 4, new int [] {hitTest});
	OS.SetEventParameter (theEvent, OS.kEventParamTextInputReplyLeadingEdge, OS.typeBoolean, 4, new boolean [] {event.count == 0});
	return OS.noErr;
}

int kEventTextInputGetSelectedText (int nextHandler, int theEvent, int userData) {
	Event event = new Event ();
	event.detail = SWT.COMPOSITION_SELECTION;
	sendEvent (SWT.ImeComposition, event);
	String text = event.text;
	if (text.length () > 0) {
		char [] buffer = new char [text.length ()];
		text.getChars (0, buffer.length, buffer, 0);
		OS.SetEventParameter (theEvent, OS.kEventParamTextInputReplyText, OS.typeUnicodeText, buffer.length * 2, buffer);
		return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventTextInputUpdateActiveInputArea (int nextHandler, int theEvent, int userData) {
	if (!isInlineEnabled ()) return OS.eventNotHandledErr;
	ranges = null;
	styles = null;
	caretOffset = commitCount = 0;
	int [] length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, 0, length, (char [])null);
	char [] chars = new char [length [0]];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, length [0], null, chars);
	int [] fixed_length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendFixLen, OS.typeLongInteger, null, 4, null, fixed_length);
	int [] rangeSize = new int [1];
	int rc = OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendHiliteRng, OS.typeTextRangeArray, null, 0, rangeSize, (byte [])null);
	if (rc == OS.noErr) {
		int firstSelectedConverted = -1;
		boolean hasConvertedText = false;
		int textRanges = OS.NewPtr (rangeSize [0]);
		OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendHiliteRng, OS.typeTextRangeArray, null, rangeSize [0], null, textRanges);
		short [] nRanges = new short [1];
		OS.memmove (nRanges, textRanges, 2);
		int count = nRanges [0];
		if (count > 0) {
			TextRange range = new TextRange ();
			ranges = new int [(count - 1) * 2];
			styles = new TextStyle [count - 1];
			for (int i = 0, j = 0; i < count; i++) {
				OS.memmove (range, textRanges + 2 + (i * TextRange.sizeof), TextRange.sizeof);
				switch (range.fHiliteStyle) {
					case OS.kCaretPosition: 
						caretOffset = range.fStart / 2;
						break;
					case OS.kConvertedText:	
					case OS.kSelectedConvertedText:
					case OS.kSelectedRawText:
					case OS.kRawText:
						ranges [j * 2] = range.fStart / 2;
						ranges [j * 2 + 1] = range.fEnd / 2 - 1;
						styles [j] = new TextStyle ();
						styles [j].underline = true;
						styles [j].underlineStyle = UNDERLINE_IME_INPUT;
						if (range.fHiliteStyle == OS.kConvertedText) {
							styles [j].underlineStyle = UNDERLINE_IME_CONVERTED;
							hasConvertedText = true;
						}
						if (range.fHiliteStyle == OS.kSelectedConvertedText) {
							styles [j].underlineStyle = UNDERLINE_IME_TARGET_CONVERTED;
							if (firstSelectedConverted == -1) {
								firstSelectedConverted = range.fStart;
							}
						}
						j++;
						break;
				}
			}
		}
		OS.DisposePtr (textRanges);
		if (hasConvertedText && firstSelectedConverted != -1) {
			caretOffset = firstSelectedConverted / 2;
		}
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
	event.text = text = new String(chars, 0, length [0] / 2);
	commitCount = fixed_length [0] != -1 ? fixed_length [0] / 2: length [0] / 2;
	sendEvent (SWT.ImeComposition, event);
	if (commitCount == text.length ()) {
		text = "";
		caretOffset = commitCount = 0;
		startOffset = -1;
		ranges = null;
		styles = null;
	}
	if (event.doit) {
		if (fixed_length [0] == -1 || fixed_length [0] == length [0]) {
			for (int i=0; i<chars.length; i++) {
				if (chars [i] == 0) break;
				event = new Event ();
				event.character = chars [i];
				parent.sendKeyEvent (SWT.KeyDown, event);
			}
		}
	}
	return OS.noErr;
}

void releaseParent () {
	super.releaseParent ();
	if (this == parent.getIME ()) parent.setIME (null);
}

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

}

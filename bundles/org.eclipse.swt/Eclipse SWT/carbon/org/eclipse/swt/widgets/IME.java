/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * 
 * @see SWT
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

public int getCaretOffset () {
	checkWidget ();
	return startOffset + caretOffset;
}

public int getCommitCount () {
	checkWidget ();
	return commitCount;
}

public int getCompositionOffset () {
	checkWidget ();
	return startOffset;
}

public int [] getRanges () {
	checkWidget ();
	if (ranges == null) return new int [0];
	int [] result = new int [ranges.length];
	for (int i = 0; i < result.length; i++) {
		result [i] = ranges [i] + startOffset; 
	}
	return result;
}

public TextStyle [] getStyles () {
	checkWidget ();
	if (styles == null) return new TextStyle [0];
	TextStyle [] result = new TextStyle [styles.length];
	System.arraycopy (styles, 0, result, 0, styles.length);
	return result;
}

public String getText () {
	checkWidget ();
	return text;
}

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

public void setCompositionOffset (int offset) {
	checkWidget ();
	if (offset < 0) return;
	if (startOffset != -1) {
		startOffset = offset;
	}
}

}

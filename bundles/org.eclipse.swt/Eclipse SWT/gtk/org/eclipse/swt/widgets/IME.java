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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;


public class IME extends Widget {
	Canvas parent;
	int caretOffset;
	int startOffset;
	int commitCount;
	String text;
	int [] ranges;
	TextStyle [] styles;
	boolean inComposition;

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

public boolean getWideCaret () {
	checkWidget ();
	return false; 
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	if (!isInlineEnabled ()) return 0;
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) OS.gtk_im_context_reset (imHandle);
	return 0;
}

int /*long*/ gtk_commit (int /*long*/ imcontext, int /*long*/ textPtr) {
	if (!isInlineEnabled ()) return 0;
	boolean doit = true;
	ranges = null;
	styles = null;
	caretOffset = commitCount = 0;
	if (textPtr != 0 && inComposition) {
		int length = OS.strlen (textPtr);
		if (length != 0) {
			byte [] buffer = new byte [length];
			OS.memmove (buffer, textPtr, length);
			char [] chars = Converter.mbcsToWcs (null, buffer);
			Event event = new Event();
			event.detail = SWT.COMPOSITION_CHANGED;
			event.start = startOffset;
			event.end = startOffset + text.length ();
			event.text = text = chars != null ? new String (chars) : "";
			commitCount = text.length();
			sendEvent (SWT.ImeComposition, event);
			doit = event.doit;
			text = "";
			startOffset = -1;
			commitCount = 0;
		}
	}
	inComposition = false;
	return doit ? 0 : 1;
}

int /*long*/ gtk_preedit_changed (int /*long*/ imcontext) {
	if (!isInlineEnabled ()) return 0;
	ranges = null;
	styles = null;
	commitCount = 0;
	int /*long*/ imHandle = imHandle ();
	int /*long*/ [] preeditString = new int /*long*/ [1];
	int /*long*/ [] pangoAttrs = new int /*long*/ [1];
	int [] cursorPos = new int [1];
	OS.gtk_im_context_get_preedit_string (imHandle, preeditString, pangoAttrs, cursorPos);
	caretOffset = cursorPos [0];
	char [] chars = null;
	if (preeditString [0] != 0) {
		int length = OS.strlen (preeditString [0]);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, preeditString [0], length);
		chars = Converter.mbcsToWcs (null, buffer);
		if (pangoAttrs [0] != 0) {
			int count = 0;
			int /*long*/ iterator = OS.pango_attr_list_get_iterator (pangoAttrs [0]);
			while (OS.pango_attr_iterator_next (iterator)) count++;
			OS.pango_attr_iterator_destroy (iterator);
			ranges = new int [count * 2];
			styles = new TextStyle [count];
			iterator = OS.pango_attr_list_get_iterator (pangoAttrs [0]);
			PangoAttrColor attrColor = new PangoAttrColor ();
			PangoAttrInt attrInt = new PangoAttrInt ();
			int [] start = new int [1];
			int [] end = new int [1];
			for (int i = 0; i < count; i++) {
				OS.pango_attr_iterator_range (iterator, start, end);
				ranges [i * 2] = (int)/*64*/OS.g_utf8_pointer_to_offset (preeditString [0], preeditString [0] + start [0]);
				ranges [i * 2 + 1] = (int)/*64*/OS.g_utf8_pointer_to_offset (preeditString [0], preeditString [0] + end [0]) - 1;
				styles [i] = new TextStyle (null, null, null);
				int /*long*/ attr = OS.pango_attr_iterator_get (iterator, OS.PANGO_ATTR_FOREGROUND);
				if (attr != 0) {
					OS.memmove (attrColor, attr, PangoAttrColor.sizeof);
					GdkColor color = new GdkColor ();
					color.red = attrColor.color_red;
					color.green = attrColor.color_green;
					color.blue = attrColor.color_blue;
					styles [i].foreground = Color.gtk_new (display, color);
				}
				attr = OS.pango_attr_iterator_get (iterator, OS.PANGO_ATTR_BACKGROUND);
				if (attr != 0) {
					OS.memmove (attrColor, attr, PangoAttrColor.sizeof);
					GdkColor color = new GdkColor ();
					color.red = attrColor.color_red;
					color.green = attrColor.color_green;
					color.blue = attrColor.color_blue;
					styles [i].background = Color.gtk_new (display, color);
				}
				attr = OS.pango_attr_iterator_get (iterator, OS.PANGO_ATTR_UNDERLINE);
				if (attr != 0) {
					OS.memmove (attrInt, attr, PangoAttrInt.sizeof);
					styles [i].underline = attrInt.value != OS.PANGO_UNDERLINE_NONE;;
					styles [i].underlineStyle = SWT.UNDERLINE_SINGLE;
					switch (attrInt.value) {
						case OS.PANGO_UNDERLINE_DOUBLE:
							styles [i].underlineStyle = SWT.UNDERLINE_DOUBLE;
							break;
						case OS.PANGO_UNDERLINE_ERROR:
							styles [i].underlineStyle = SWT.UNDERLINE_ERROR;
							break;
					}
					if (styles [i].underline) {
						attr = OS.pango_attr_iterator_get(iterator, OS.PANGO_ATTR_UNDERLINE_COLOR);
						if (attr != 0) {
							OS.memmove (attrColor, attr, PangoAttrColor.sizeof);
							GdkColor color = new GdkColor ();
							color.red = attrColor.color_red;
							color.green = attrColor.color_green;
							color.blue = attrColor.color_blue;
							styles [i].underlineColor = Color.gtk_new (display, color);
						}
					}
				}
				OS.pango_attr_iterator_next (iterator);
			}
			OS.pango_attr_iterator_destroy (iterator);
			OS.pango_attr_list_unref (pangoAttrs [0]);	
		}
		OS.g_free (preeditString [0]);
	}
	if (chars != null) {
		if (text.length() == 0) startOffset = -1;
		int end = startOffset + text.length();
		if (startOffset == -1) {
			Event event = new Event ();
			event.detail = SWT.COMPOSITION_SELECTION;
			sendEvent (SWT.ImeComposition, event);
			startOffset = event.start;
			end = event.end;
		}
		inComposition = true;
		Event event = new Event ();
		event.detail = SWT.COMPOSITION_CHANGED;
		event.start = startOffset;
		event.end = end;
		event.text = text = chars != null ? new String (chars) : "";
		sendEvent (SWT.ImeComposition, event);
	}
	return 1;
}

int /*long*/ imHandle () {
	return parent.imHandle ();
}

boolean isInlineEnabled () {
	return hooks (SWT.ImeComposition);
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

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Text extends Scrollable {
	char echoCharacter;
	boolean ignoreChange;
	String hiddenText;
	int tabs, lastModifiedText;
	PtTextCallback_t textVerify;
	
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

static int checkStyle (int style) {
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		return style | SWT.MULTI;
	}
	return style | SWT.SINGLE;
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	PhDim_t dim = new PhDim_t ();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize (handle, dim);
	int width = dim.w, height = dim.h;
	if ((style & SWT.MULTI) != 0) {
		int child = OS.PtWidgetChildBack (handle);
		OS.PtWidgetPreferredSize (child, dim);
		width += dim.w - 1;
		height += dim.h - 1;
	}
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		PhRect_t rect = new PhRect_t ();
		PhArea_t area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		ScrollBar scroll;
		if (wHint != SWT.DEFAULT) {
			width = area.size_w;
			if ((scroll = getVerticalBar ()) != null) width += scroll.getSize().x;
		}
		if (hHint != SWT.DEFAULT) {
			height = area.size_h;
			if ((scroll = getHorizontalBar ()) != null) height += scroll.getSize().y;
		}
	}
	return new Point(width, height);
}
public void clearSelection () {
	checkWidget();
	OS.PtTextSetSelection (handle, new int [] {0}, new int [] {0});
}
void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int parentHandle = parent.handle;
	boolean hasBorder = (style & SWT.BORDER) != 0;
	int textFlags = (style & SWT.READ_ONLY) != 0 ? 0 : OS.Pt_EDITABLE;
	if ((style & SWT.SINGLE) != 0) {
		int clazz = display.PtText;
		int [] args = {	
			OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
			OS.Pt_ARG_FLAGS, OS.Pt_CALLBACKS_ACTIVE, OS.Pt_CALLBACKS_ACTIVE,
			OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int clazz = display.PtMultiText;
	int wrapFlags = (style & SWT.WRAP) != 0 ? OS.Pt_EMT_WORD | OS.Pt_EMT_CHAR : 0;
	int [] args = {
		OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_FLAGS, OS.Pt_CALLBACKS_ACTIVE, OS.Pt_CALLBACKS_ACTIVE,
		OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
		OS.Pt_ARG_MULTITEXT_WRAP_FLAGS, wrapFlags, OS.Pt_EMT_WORD | OS.Pt_EMT_CHAR,
		OS.Pt_ARG_SCROLLBAR_X_DISPLAY, (style & SWT.H_SCROLL) != 0 ? OS.Pt_ALWAYS : OS.Pt_NEVER, 0,
		OS.Pt_ARG_SCROLLBAR_Y_DISPLAY, (style & SWT.V_SCROLL) != 0 ? OS.Pt_ALWAYS : OS.Pt_NEVER, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	createScrollBars();
}

void createWidget (int index) {
	super.createWidget (index);
//	doubleClick = true;
	setTabStops (tabs = 8);
}

public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	OS.PtTextModifyText (handle, 0, 0, -1, buffer, buffer.length);
}

public void copy () {
	checkWidget();
	int [] start = new int [1], end = new int [1];
	int length = OS.PtTextGetSelection (handle, start, end);
	if (length <= 0) return;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	byte[] buffer = new byte[length + 1];
	OS.memmove (buffer, args [1] + start [0], length);
	int ig = OS.PhInputGroup (0);
	OS.PhClipboardCopyString((short)ig, buffer);
}

public void cut () {
	checkWidget();
	int [] start = new int [1], end = new int [1];
	int length = OS.PtTextGetSelection (handle, start, end);
	if (length <= 0) return;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	byte[] buffer = new byte[length + 1];
	OS.memmove (buffer, args [1] + start [0], length);
	int ig = OS.PhInputGroup (0);
	OS.PhClipboardCopyString((short)ig, buffer);
	buffer = new byte[0];
	OS.PtTextModifyText (handle, start [0], end [0], start [0], buffer, buffer.length);
}

void deregister () {
	super.deregister ();
	
	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text. 
	*/
	if ((style & SWT.MULTI) == 0) return;
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.remove (child);
}

public int getCaretLineNumber () {
	checkWidget();
	//NOT DONE - NOT NEEDED
	return 0;
}

public Point getCaretLocation () {
	checkWidget();
	//NOT DONE - NOT NEEDED
	return null;
}

public int getCaretPosition () {
	checkWidget();
	int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getCharCount () {
	checkWidget();
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return 0;
	return OS.strlen (args [1]);
}

public boolean getDoubleClickEnabled () {
	checkWidget();
	//NOT DONE - NOT NEEDED
	return false;
}

public char getEchoChar () {
	checkWidget();
	return echoCharacter;
}

public boolean getEditable () {
	checkWidget();
	int [] args = {OS.Pt_ARG_TEXT_FLAGS, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return (args [1] & OS.Pt_EDITABLE) != 0;
}

public int getLineCount () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 1;
	int [] args = {OS.Pt_ARG_MULTITEXT_NUM_LINES, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return args [1];
}

public String getLineDelimiter () {
	checkWidget();
	return "\n";
}

public int getLineHeight () {
	if ((style & SWT.SINGLE) != 0) {
		PhDim_t dim = new PhDim_t ();
		if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
		OS.PtWidgetPreferredSize (handle, dim);
		PhRect_t extent = new PhRect_t ();
		OS.PtWidgetExtent(handle, extent);
		PhRect_t canvas = new PhRect_t ();
		OS.PtWidgetCanvas (handle, canvas);
		int topBorder = canvas.ul_y - extent.ul_y;
		int bottomBorder = extent.lr_y - canvas.lr_y;
		return dim.h - topBorder - bottomBorder;
	}
	int ptr = OS.malloc (20);
	int [] args = {
		OS.Pt_ARG_MULTITEXT_QUERY_LINE, ptr, 1,
		OS.Pt_ARG_MULTITEXT_LINE_SPACING, 0, 0
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int [] line = new int [1];
	OS.memmove (line, args [1] + 8, 4);
	PhRect_t extent = new PhRect_t ();
	OS.memmove (extent, line [0] + 10, 8);
	OS.free(ptr);
	return extent.lr_y - extent.ul_y + 1 + args [4];
}

String getNameText () {
	if ((style & SWT.SINGLE) != 0) return getText ();
	return getText (0, Math.min(getCharCount () - 1, 10));
}

public Point getSelection () {
	checkWidget();
	if (textVerify != null) {
		return new Point (textVerify.start_pos, textVerify.end_pos);
	}
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	return new Point (start [0], end [0]);
}

public int getSelectionCount () {
	checkWidget();
	Point selection = getSelection ();
	return selection.y - selection.x;
}

public String getSelectionText () {
	checkWidget();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	Point selection = getSelection ();
	return getText ().substring (selection.x, selection.y);
}

public int getTabs () {
	checkWidget();
	return tabs;
}

int getTabWidth (int tabs) {
	int [] args = new int [] {OS.Pt_ARG_TEXT_FONT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	PhRect_t rect = new PhRect_t ();
	int ptr = OS.malloc (1);
	OS.memmove (ptr, new byte [] {' '}, 1);
	OS.PfExtentText(rect, null, args [1], ptr, 1);
	OS.free (ptr);
	int width = rect.lr_x - rect.ul_x + 1;
	return width * tabs;
}

public String getText (int start, int end) {
	checkWidget();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	//NOT DONE - use OS in SINGLE text
	return getText ().substring (start, end + 1);
}

public String getText () {
	checkWidget();
	if (echoCharacter != '\0') return hiddenText;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}

public int getTextLimit () {
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getTopIndex () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
	int [] args = {OS.Pt_ARG_MULTITEXT_TOP_LINE, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return args [1];
}

public int getTopPixel () {
	checkWidget();
	//NOT DONE - NOT NEEDED
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_MODIFY_VERIFY, windowProc, SWT.Verify); 
	OS.PtAddCallback (handle, OS.Pt_CB_TEXT_CHANGED, windowProc, SWT.Modify);
}

public void insert (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	OS.PtTextModifyText (handle, start [0], end [0], start [0], buffer, buffer.length);
}

public void paste () {
	checkWidget();
	int ig = OS.PhInputGroup (0);
	int ptr = OS.PhClipboardPasteString((short)ig);
	if (ptr == 0) return;
	int length = OS.strlen (ptr);
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	OS.PtTextModifyText (handle, start [0], end [0], end [0], ptr, length);
	OS.free(ptr);
}

int processEvent (int widget, int data, int info) {
	
	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text and avoid multiple 
	* Pt_CB_LOST_FOCUS callbacks.
	*/
	if ((style & SWT.MULTI) != 0) {
		if (widget != handle && data == SWT.FocusOut) {
			return OS.Pt_CONTINUE;
		}
	}
	return super.processEvent (widget, data, info);
}

int processModify (int info) {
	if (lastModifiedText != 0) {
		OS.free (lastModifiedText);
		lastModifiedText = 0;
	}
	if (!ignoreChange) sendEvent (SWT.Modify);
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	if ((style & SWT.SINGLE) != 0) {
		OS.PtSuperClassDraw (OS.PtText (), handle, damage);
	} else {
		OS.PtSuperClassDraw (OS.PtMultiText (), handle, damage);
	}
	return super.processPaint (damage);
}

int processVerify (int info) {
	if (lastModifiedText != 0) {
		OS.free (lastModifiedText);
		lastModifiedText = 0;
	}
	super.processVerify (info);
	if (echoCharacter == '\0' && !hooks (SWT.Verify)) return 0;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	PtTextCallback_t textVerify = new PtTextCallback_t ();
	OS.memmove (textVerify, cbinfo.cbdata, PtTextCallback_t.sizeof);
	byte [] buffer = new byte [textVerify.length];
	OS.memmove (buffer, textVerify.text, buffer.length);
	String text = new String (Converter.mbcsToWcs (null, buffer));
	String newText = text;
	if (!ignoreChange) {
		Event event = new Event ();
		event.start = textVerify.start_pos;
		event.end = textVerify.end_pos;
		event.doit = textVerify.doit != 0;
		event.text = text;
		sendEvent (SWT.Verify, event);
		newText = event.text;
		textVerify.doit = (event.doit && newText != null) ? 1 : 0;
	}
	if (newText != null) {
		if (echoCharacter != '\0' && (textVerify.doit != 0)) {
			String prefix = hiddenText.substring (0, textVerify.start_pos);
			String suffix = hiddenText.substring (textVerify.end_pos, hiddenText.length ());
			hiddenText = prefix + newText + suffix;
			char [] charBuffer = new char [newText.length ()];
			for (int i=0; i<charBuffer.length; i++) {
				charBuffer [i] = echoCharacter;
			}
			newText = new String (charBuffer);
		}
		if (newText != text) {
			byte [] buffer2 = Converter.wcsToMbcs (null, newText, true);
			int length = buffer2.length - 1;
			if (length == textVerify.length) {
				OS.memmove(textVerify.text, buffer2, length);
			} else {
				int ptr = OS.malloc (length);
				OS.memmove (ptr, buffer2, buffer2.length);
				textVerify.new_insert += length - textVerify.length;
				textVerify.text = ptr;
				textVerify.length = length;
				lastModifiedText = ptr;
			}
		}
	}
	OS.memmove (cbinfo.cbdata, textVerify, PtTextCallback_t.sizeof);
	textVerify = null;
	return 0;
}

void register () {
	super.register ();

	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text. 
	*/
	if ((style & SWT.MULTI) == 0) return;
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.put (child, this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (lastModifiedText != 0) OS.free (lastModifiedText);
	lastModifiedText = 0;
	hiddenText = null;
	textVerify = null;
}

public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void removeVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

public void selectAll () {
	checkWidget();
	OS.PtTextSetSelection (handle, new int [0], new int [] {-1});
}

public void setEchoChar (char echo) {
	checkWidget();
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
	setSelection(selection.x, selection.y);
	ignoreChange = oldValue;
}

public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget();
	//NOT DONE - NOT NEEDED
}

public void setEditable (boolean editable) {
	checkWidget();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY; 
	int [] args = {OS.Pt_ARG_TEXT_FLAGS, editable ? OS.Pt_EDITABLE : 0, OS.Pt_EDITABLE};
	OS.PtSetResources(handle, args.length / 3, args);
}

public void setFont (Font font) {
	super.setFont (font);
	setTabStops (tabs);
}

public void setSelection (int position) {
	checkWidget();
	int [] args = {OS.Pt_ARG_CURSOR_POSITION, position, 0};
	OS.PtSetResources (handle, args.length / 3, args);

	/*
	* Feature in Photon. On a single-line text, the selection is
	* not cleared when setting the cursor position. The fix is to
	* set the selection start and end values to the specified
	* position.
	*/
	if ((style & SWT.SINGLE) != 0) {
		int [] selection = {position};
		OS.PtTextSetSelection (handle, selection, selection);
	}
}

public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

public void setSelection (int start, int end) {
	checkWidget();
	OS.PtTextSetSelection (handle, new int [] {start}, new int [] {end});
}

public void setTabs (int tabs) {
	checkWidget();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
	if ((style & SWT.SINGLE) != 0) return;
	int tabsWidth = getTabWidth (tabs);
	int ptr = OS.malloc (4);
	OS.memmove (ptr, new int [] {tabsWidth}, 4);
	int [] args = {OS.Pt_ARG_MULTITEXT_TABS, ptr, 1};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {OS.Pt_ARG_TEXT_STRING, ptr, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, limit, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setTopIndex (int index) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	int [] args = {OS.Pt_ARG_MULTITEXT_TOP_LINE, index + 1, 0};
	OS.PtSetResources(handle, args.length / 3, args);
}

public void showSelection () {
	checkWidget();
	//NOT DONE - NOT NEEDED
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	if ((style & SWT.SINGLE) != 0) {
		int code = super.traversalCode (key_sym, ke);
		if (key_sym == OS.Pk_Right || key_sym == OS.Pk_Left) {
			code &= ~(SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS);
		}
		return code;
	}
	return SWT.TRAVERSE_ESCAPE;
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	boolean translated = super.translateTraversal (key_sym, phEvent);
	if ((style & SWT.SINGLE) != 0 && !translated && key_sym == OS.Pk_Return) {
		postEvent (SWT.DefaultSelection);
		return true;
	}
	return translated;
}

}

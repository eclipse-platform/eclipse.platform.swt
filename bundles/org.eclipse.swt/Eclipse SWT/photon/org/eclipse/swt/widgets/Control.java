package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.*;

public abstract class Control extends Widget implements Drawable {
	Composite parent;
	Menu menu;
	Object layoutData;
	
Control () {
	/* Do nothing */
}

public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

public void addControlListener(ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

public void addFocusListener (FocusListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.FocusIn,typedListener);
	addListener (SWT.FocusOut,typedListener);
}

public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addKeyListener (KeyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

public void addMouseListener (MouseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseDown,typedListener);
	addListener (SWT.MouseUp,typedListener);
	addListener (SWT.MouseDoubleClick,typedListener);
}

public void addMouseTrackListener (MouseTrackListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

public void addMouseMoveListener (MouseMoveListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseMove,typedListener);
}

public void addPaintListener (PaintListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Paint,typedListener);
}

public void addTraverseListener (TraverseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

public boolean forceFocus () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int shellHandle = OS.PtFindDisjoint (handle);
	OS.PtWindowToFront (shellHandle);
	return handle != OS.PtContainerGiveFocus (handle, null);
}

public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

void createWidget (int index) {
	super.createWidget (index);
	setZOrder ();
	realizeWidget ();
}

byte [] defaultFont () {
	Display display = getDisplay ();
	return display.TEXT_FONT;
}

public Color getBackground () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FILL_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return Color.photon_new (getDisplay (), args [1]);
}
public Font getFont () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_TEXT_FONT, 0, 0,
		OS.Pt_ARG_LIST_FONT, 0, 0,
		OS.Pt_ARG_TITLE_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	byte [] font;
	int ptr = args [1];
	if (ptr == 0) ptr = args [4];
	if (ptr == 0) ptr = args [7];
	if (ptr == 0) {
		font = defaultFont ();
	} else {
		int length = OS.strlen (ptr);
		font = new byte [length + 1];
		OS.memmove (font, ptr, length);
	}
	return Font.photon_new (getDisplay (), font);
}
public Color getForeground () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return Color.photon_new (getDisplay (), args [1]);
}

public int getBorderWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] args = {
		OS.Pt_ARG_BASIC_FLAGS, 0, 0,
//		OS.Pt_ARG_BEVEL_WIDTH, 0, 0,
	};
	OS.PtGetResources (topHandle, args.length / 3, args);
	int border = 0;
	int flags = args [1];
	if ((flags & OS.Pt_ALL_ETCHES) != 0) border++;
	if ((flags & OS.Pt_ALL_OUTLINES) != 0) border++;
	if ((flags & OS.Pt_ALL_INLINES) != 0) border++;
//	if ((flags & OS.Pt_ALL_BEVELS) != 0) border += args [4];
	return border;
}

public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
}

public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public Object getLayoutData () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return layoutData;
}

public Point getLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	return new Point (area.pos_x, area.pos_y);
}

public Menu getMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return null;
}

public Composite getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

public Point getSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	return new Point (area.size_w, area.size_h);
}

public String getToolTipText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return null;
}

public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getShell ();
}

public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (topHandle, args.length / 3, args);
	return (args [1] & OS.Pt_DELAY_REALIZE) == 0;
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) == 2;
}

void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	OS.PtAddEventHandler (handle, OS.Ph_EV_KEY, windowProc, SWT.KeyDown);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BUT_PRESS, windowProc, SWT.MouseDown);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BUT_RELEASE, windowProc, SWT.MouseUp);
	OS.PtAddEventHandler (handle, OS.Ph_EV_PTR_MOTION, windowProc, SWT.MouseMove);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BOUNDARY, windowProc, SWT.MouseEnter);	
	OS.PtAddCallback (handle, OS.Pt_CB_GOT_FOCUS, windowProc, SWT.FocusIn);
	OS.PtAddCallback (handle, OS.Pt_CB_LOST_FOCUS, windowProc, SWT.FocusOut);
}

public int internal_new_GC (GCData data) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	int phGC = OS.PgCreateGC(0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	int [] args = {OS.Pt_ARG_COLOR, 0, 0, OS.Pt_ARG_FILL_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	data.device = getDisplay ();
	data.widget = handle;
	data.topWidget = topHandle ();
	data.foreground = args [1];
	data.background = args [4];
	data.font = getFont ().handle;
	return phGC;
}

public void internal_dispose_GC (int phGC, GCData data) {
	//if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	OS.PgDestroyGC(phGC);
}

public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}

public boolean isFocusControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return hasFocus ();
}

public boolean isReparentable () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.PtWidgetIsRealized (handle);
}

Decorations menuShell () {
	return parent.menuShell ();
}

public void moveAbove (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle1 = topHandle ();
	if (control == null) {
		OS.PtWidgetToFront (topHandle1);
		OS.PtWindowToFront (topHandle1);
		return;
	}
	int topHandle2 = control.topHandle ();
	OS.PtWidgetInsert (topHandle1, topHandle2, 0);
}

public void moveBelow (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle1 = topHandle ();
	if (control == null) {
		OS.PtWidgetToBack (topHandle1);
		OS.PtWindowToBack (topHandle1);
		return;
	}
	int topHandle2 = control.topHandle ();
	OS.PtWidgetInsert (topHandle1, topHandle2, 1);
}

public void pack () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	pack (true);
}

public void pack (boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

int processPaint (int damage) {
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processFocusIn (int info) {
	sendEvent (SWT.FocusIn);
	/*
	* Feature in Photon.  Cannot return Pt_END
	* or the text widget will not take focus.
	*/
	return OS.Pt_CONTINUE;
}

int processFocusOut (int info) {
	sendEvent (SWT.FocusOut);
	/*
	* Feature in Photon.  Cannot return Pt_END
	* or the text widget will not take focus.
	*/
	return OS.Pt_CONTINUE;
}

int processKey (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	int data = OS.PhGetData (cbinfo.event);
	if (data == 0) return OS.Pt_END;
	PhKeyEvent_t ke = new PhKeyEvent_t ();
	OS.memmove (ke, data, PhKeyEvent_t.sizeof);
	/*
	* Feature in Photon.  The multi-line text widget consumes
	* key down events.  The fix is to use a filter callback
	* for the text widget to see the key down.  The following
	* code ignores key up when called from a filter callback.
	*/
	if (cbinfo.reason == OS.Pt_CB_FILTER) {
		if ((ke.key_flags & (OS.Pk_KF_Key_Down | OS.Pk_KF_Key_Repeat)) == 0) {
			return OS.Pt_PROCESS;
		}
	}
	/*
	* Feature in Photon.  When the user presses certain keys
	* (such as the arrow keys), Photon sends 2 event for one
	* key press.  The first event has only the scan code while
	* the second has the keysym and other information.  This
	* also happens for key release.  The fix is to ignore the
	* first event.
	*/
	if (ke.key_flags == OS.Pk_KF_Scan_Valid) {
		return (cbinfo.reason == OS.Pt_CB_FILTER) ? OS.Pt_PROCESS : OS.Pt_END;
	}
	if ((ke.key_flags & OS.Pk_KF_Key_Repeat) != 0) {
		if ((ke.key_flags & OS.Pk_KF_Sym_Valid) != 0) {
			switch (ke.key_sym) {
				case OS.Pk_Alt_L:
				case OS.Pk_Alt_R:
				case OS.Pk_Control_L:
				case OS.Pk_Control_R:
				case OS.Pk_Shift_L:
				case OS.Pk_Shift_R:
					/*
					* Bug in Photon.  Despite the fact that we return Pt_END,
					* for some reason, Photon continues to forward the event
					* to the parent.  The fix is to change the event type to
					* zero.  This doesn't stop the forwarding but makes the
					* event unknown.
					*/
					if (cbinfo.reason == OS.Pt_CB_FILTER) return OS.Pt_PROCESS;
					OS.memmove (cbinfo.event, new int [1], 4);
					return OS.Pt_END;
			}
		}
	}
	Display display = getDisplay ();
	Event event = new Event ();
	event.time = ev.timestamp;
	int type = SWT.KeyUp;
	if ((ke.key_flags & (OS.Pk_KF_Key_Down | OS.Pk_KF_Key_Repeat)) != 0) {
		type = SWT.KeyDown;
	}
	if ((ke.key_flags & OS.Pk_KF_Sym_Valid) != 0) {
		event.keyCode = Display.translateKey (ke.key_sym);
		if (event.keyCode == 0) {
			switch (ke.key_sym) {
				case OS.Pk_BackSpace:	event.character = '\b'; break;
				case OS.Pk_Tab: 		event.character = '\t'; break;
				case OS.Pk_Linefeed:	event.character = '\n'; break;
				case OS.Pk_Clear: 	event.character = 0xB; break;
				case OS.Pk_Return: 	event.character = '\r'; break;
				case OS.Pk_Pause:		event.character = 0x13; break;
				case OS.Pk_Scroll_Lock:	event.character = 0x14; break;
				case OS.Pk_Escape:	event.character = 0x1B; break;
				case OS.Pk_Delete:	event.character = 0x7F; break;
				default:
					event.character = (char) ke.key_sym;
			}
		}
		display.lastKey = event.keyCode;
		display.lastAscii = event.character;
	}
	if ((ke.key_mods & OS.Pk_KM_Alt) != 0) {
		if (type != SWT.KeyDown || event.keyCode != SWT.ALT) {
			event.stateMask |= SWT.ALT;
		}
	}
	if ((ke.key_mods & OS.Pk_KM_Shift) != 0) {
		if (type != SWT.KeyDown || event.keyCode != SWT.SHIFT) {
			event.stateMask |= SWT.SHIFT;
		}
	}
	if ((ke.key_mods & OS.Pk_KM_Ctrl) != 0) {
		if (type != SWT.KeyDown || event.keyCode != SWT.CONTROL) {
			event.stateMask |= SWT.CONTROL;
		}
	}
	if ((ke.button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((ke.button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((ke.button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	if (type == SWT.KeyUp) {	
		if (event.keyCode == 0) event.keyCode = display.lastKey;
		if (event.character == 0) event.character = (char) display.lastAscii;
		if (event.keyCode == SWT.ALT) event.stateMask |= SWT.ALT;
		if (event.keyCode == SWT.SHIFT) event.stateMask |= SWT.SHIFT;
		if (event.keyCode == SWT.CONTROL) event.stateMask |= SWT.CONTROL;
	}
	postEvent (type, event);
	/*
	* Bug in Photon.  Despite the fact that we return Pt_END,
	* for some reason, Photon continues to forward the event
	* to the parent.  The fix is to change the event type to
	* zero.  This doesn't stop the forwarding but makes the
	* event unknown.
	*/
	if (cbinfo.reason == OS.Pt_CB_FILTER) return OS.Pt_PROCESS;
	OS.memmove (cbinfo.event, new int [1], 4);
	return OS.Pt_END;
}

int processMouse (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	Event event = new Event ();
	switch (ev.type) {
		case OS.Ph_EV_BUT_PRESS:
			event.type = SWT.MouseDown;
			break;
		case OS.Ph_EV_BUT_RELEASE:
			if (ev.subtype != OS.Ph_EV_RELEASE_PHANTOM) {
				return OS.Pt_END;
			}
			event.type = SWT.MouseUp;
			break;
		case OS.Ph_EV_PTR_MOTION_BUTTON:
		case OS.Ph_EV_PTR_MOTION_NOBUTTON:
			event.type = SWT.MouseMove;
			break;
		case OS.Ph_EV_DRAG:
			if (ev.subtype != OS.Ph_EV_DRAG_MOTION_EVENT) {
				return OS.Pt_END;
			}
			event.type = SWT.MouseMove;
			break;
	}
	event.time = ev.timestamp;
	int data = OS.PhGetData (cbinfo.event);
	if (data == 0) return OS.Pt_END;
	PhPointerEvent_t pe = new PhPointerEvent_t ();
	OS.memmove (pe, data, PhPointerEvent_t.sizeof);
	event.x = pe.pos_x + ev.translation_x;
	event.y = pe.pos_y + ev.translation_y;
	int type = ev.type;
	int buttons = pe.buttons;
	int key_mods = pe.key_mods;
	int button_state = pe.button_state;
	int click_count = pe.click_count;
	switch (buttons) {
		case OS.Ph_BUTTON_SELECT:	event.button = 1; break;
		case OS.Ph_BUTTON_ADJUST:	event.button = 2; break;
		case OS.Ph_BUTTON_MENU:		event.button = 3; break;
	}
	if ((key_mods & OS.Pk_KM_Alt) != 0) event.stateMask |= SWT.ALT;
	if ((key_mods & OS.Pk_KM_Shift) != 0) event.stateMask |= SWT.SHIFT;
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) event.stateMask |= SWT.CONTROL;
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	if (type == OS.Ph_EV_BUT_PRESS) {
		if (buttons == OS.Ph_BUTTON_SELECT && (button_state & OS.Ph_BUTTON_SELECT) != 0) {
			event.stateMask &= ~SWT.BUTTON1;
		}
		if (buttons == OS.Ph_BUTTON_ADJUST && (button_state & OS.Ph_BUTTON_ADJUST) != 0) {
			event.stateMask &= ~SWT.BUTTON2;
		}
		if (buttons == OS.Ph_BUTTON_MENU && (button_state & OS.Ph_BUTTON_MENU) != 0) {
			event.stateMask &= ~SWT.BUTTON3;
		}
	}
	if (type == OS.Ph_EV_BUT_RELEASE) {
		if (buttons == OS.Ph_BUTTON_SELECT) event.stateMask |= SWT.BUTTON1;
		if (buttons == OS.Ph_BUTTON_ADJUST) event.stateMask |= SWT.BUTTON2;
		if (buttons == OS.Ph_BUTTON_MENU) event.stateMask |= SWT.BUTTON3;
	}
	postEvent (event.type, event);
	if (type == OS.Ph_EV_BUT_PRESS && click_count == 2) {
		postEvent (SWT.MouseDoubleClick, event);
	}
	return OS.Pt_END;
}

int processMouseEnter (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	int rects = OS.PhGetRects (cbinfo.event);
	PhRect_t rect = new PhRect_t ();
	OS.memmove (rect, rects, PhRect_t.sizeof);
	Event event = new Event ();
	event.time = ev.timestamp;
	event.x = rect.ul_x;
	event.y = rect.ul_y;
	switch (ev.subtype) {
		case OS.Ph_EV_PTR_ENTER:
		case OS.Ph_EV_PTR_ENTER_FROM_CHILD:
			sendEvent (SWT.MouseEnter, event);
			break;
		case OS.Ph_EV_PTR_LEAVE:
		case OS.Ph_EV_PTR_LEAVE_TO_CHILD:
			sendEvent (SWT.MouseExit, event);
			break;		
	}
	return OS.Pt_END;
}

void realizeWidget() {
	int parentHandle = parent.handle;
	if (OS.PtWidgetIsRealized (parentHandle)) {
		OS.PtRealizeWidget (topHandle ());
	}
}

public void redraw () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.PtDamageWidget (handle);
}

public void redraw (int x, int y, int width, int height, boolean allChildren) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhRect_t rect = new PhRect_t ();
	rect.ul_x = (short) x;
	rect.ul_y = (short) y;
	rect.lr_x = (short) (x + width - 1);
	rect.lr_y = (short) (y + height - 1);
	OS.PtDamageExtent (handle, rect);
}

public void removeControlListener (ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

public void removeFocusListener(FocusListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}

public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeKeyListener(KeyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

public void removeMouseTrackListener(MouseTrackListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

public void removeMouseListener (MouseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}

public void removeMouseMoveListener(MouseMoveListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

public void removePaintListener(PaintListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

public void removeTraverseListener(TraverseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	boolean sameOrigin = x == area.pos_x && y == area.pos_y;
	boolean sameExtent = width == area.size_w && height == area.size_h;
	if (move && resize) {
		area.pos_x = (short) x;
		area.pos_y = (short) y;
		area.size_w = (short) (Math.max (width, 0));
		area.size_h = (short) (Math.max (height, 0));
		int ptr = OS.malloc (PhArea_t.sizeof);
		OS.memmove (ptr, area, PhArea_t.sizeof);
		int [] args = {OS.Pt_ARG_AREA, ptr, 0};
		OS.PtSetResources (topHandle, args.length / 3, args);
		OS.free (ptr);
	} else {
		if (move) {
			PhPoint_t pt = new PhPoint_t ();
			pt.x = (short) x;
			pt.y = (short) y;
			int ptr = OS.malloc (PhPoint_t.sizeof);
			OS.memmove (ptr, pt, PhPoint_t.sizeof);
			int [] args = {OS.Pt_ARG_POS, ptr, 0};
			OS.PtSetResources (topHandle, args.length / 3, args);
			OS.free (ptr);
		}
		if (resize) {
			int [] args = {
				OS.Pt_ARG_WIDTH, Math.max (width, 0), 0,
				OS.Pt_ARG_HEIGHT, Math.max (height, 0), 0,
			};
			OS.PtSetResources (topHandle, args.length / 3, args);
		}
	}
	if (!OS.PtWidgetIsRealized (topHandle)) {
		OS.PtExtentWidgetFamily (topHandle);
	}
	if (!sameOrigin & move) sendEvent (SWT.Move);
	if (!sameExtent & resize & sendResize()) sendEvent (SWT.Resize);
}

public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBounds (x, y, width, height, true, true);
}

public void setBounds (Rectangle rect) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

public void setCapture (boolean capture) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

public void setCursor (Cursor cursor) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int type = OS.Ph_CURSOR_INHERIT;
	if (cursor != null) type = cursor.handle;
	int [] args = {OS.Pt_ARG_CURSOR_TYPE, type, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public boolean setFocus () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return forceFocus ();
}

void sendPaintEvent (int damage) {
	
	if (!hooks(SWT.Paint)) return;
	
	/* Translate the damage to widget coordinates */
	short [] widgetX = new short [1];
	short [] widgetY = new short [1];
	OS.PtGetAbsPosition (handle, widgetX, widgetY);
	short [] shellX = new short [1];
	short [] shellY = new short [1];
	int shellHandle = OS.PtFindDisjoint (handle);
	OS.PtGetAbsPosition (shellHandle, shellX, shellY);
	PhPoint_t pt = new PhPoint_t ();
	pt.x = (short) (shellX [0] - widgetX [0]);
	pt.y = (short) (shellY [0] - widgetY [0]);
	damage = OS.PhCopyTiles (damage);
	damage = OS.PhTranslateTiles (damage, pt);
	
	/* Send the paint event */
	PhTile_t tile = new PhTile_t ();
	OS.memmove (tile, damage, PhTile_t.sizeof);
	Event event = new Event ();
	event.x = tile.rect_ul_x;
	event.y = tile.rect_ul_y;
	event.width = tile.rect_lr_x - tile.rect_ul_x + 1;
	event.height = tile.rect_lr_y - tile.rect_ul_y + 1;
	Region region = Region.photon_new (tile.next);
	GC gc = event.gc = new GC (this);
	gc.setClipping (region);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	OS.PhFreeTiles (damage);
	event.gc = null;
}

boolean sendResize () {
	return true;
}

public void setBackground (Color color) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int pixel = OS.Pt_DEFAULT_COLOR;
	if (color != null) pixel = color.handle;
	int [] args = {OS.Pt_ARG_FILL_COLOR, pixel, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setFont (Font font) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	byte [] buffer = (font != null) ? font.handle : defaultFont (); 
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {
		OS.Pt_ARG_TEXT_FONT, ptr, 0,
		OS.Pt_ARG_LIST_FONT, ptr, 0,
		OS.Pt_ARG_TITLE_FONT, ptr, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

public void setForeground (Color color) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int pixel = OS.Pt_DEFAULT_COLOR;
	if (color != null) pixel = color.handle;
	int [] args = {OS.Pt_ARG_COLOR, pixel, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setMenu (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.menu = menu;
}

public boolean setParent (Composite parent) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public void setLayoutData (Object layoutData) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.layoutData = layoutData;
}

public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBounds (x, y, 0, 0, true, false);
}

public void setLocation (Point location) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setSize (Point size) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
public void setRedraw (boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (redraw) {
		OS.PtContainerRelease (handle);
	} else {
		OS.PtContainerHold (handle);
	}
}
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBounds (0, 0, width, height, false, true);
}

public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] args = {
		OS.Pt_ARG_FLAGS, visible ? 0 : OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
	};
	OS.PtSetResources (topHandle, args.length / 3, args);
	if (visible) {
		sendEvent (SWT.Show);
		OS.PtRealizeWidget (topHandle);
	} else {
		OS.PtUnrealizeWidget (topHandle);
		sendEvent(SWT.Hide);
	}	
}

public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

void setZOrder() {
	OS.PtWidgetToBack (topHandle ());
}

public Point toControl (Point point) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] x = new short [1], y = new short [1];
	OS.PtGetAbsPosition (handle, x, y);
	return new Point (x [0] - point.x, y [0] - point.y);
}

public Point toDisplay (Point point) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] x = new short [1], y = new short [1];
	OS.PtGetAbsPosition (handle, x, y);
	return new Point (point.x + x [0], point.y + y [0]);
}

public boolean traverse (int traversal) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public void update () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.PtFlush ();
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.*;

public abstract class Control extends Widget implements Drawable {
	Composite parent;
	Menu menu;
	Object layoutData;
	String toolTipText;
	int toolTipHandle;
	
Control () {
	/* Do nothing */
}

public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

public void addFocusListener (FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.FocusIn,typedListener);
	addListener (SWT.FocusOut,typedListener);
}

public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addKeyListener (KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

public void addMouseListener (MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseDown,typedListener);
	addListener (SWT.MouseUp,typedListener);
	addListener (SWT.MouseDoubleClick,typedListener);
}

public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

public void addMouseMoveListener (MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseMove,typedListener);
}

public void addPaintListener (PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Paint,typedListener);
}

public void addTraverseListener (TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

public boolean forceFocus () {
	checkWidget();
	int shellHandle = OS.PtFindDisjoint (handle);
	OS.PtWindowToFront (shellHandle);
	OS.PtContainerGiveFocus (handle, null);
	return hasFocus ();
}

public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
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
	checkWidget();
	int [] args = {OS.Pt_ARG_FILL_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return Color.photon_new (getDisplay (), args [1]);
}

public Font getFont () {
	checkWidget();
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
	checkWidget();
	int [] args = {OS.Pt_ARG_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return Color.photon_new (getDisplay (), args [1]);
}

public int getBorderWidth () {
	checkWidget();
	int topHandle = topHandle ();
	int [] args = {
		OS.Pt_ARG_BASIC_FLAGS, 0, 0,
		OS.Pt_ARG_FLAGS, 0, 0,
//		OS.Pt_ARG_BEVEL_WIDTH, 0, 0,
	};
	OS.PtGetResources (topHandle, args.length / 3, args);
	if ((args [4] & OS.Pt_HIGHLIGHTED) == 0) return 0;
	int border = 0;
	int flags = args [1];
	if ((flags & OS.Pt_ALL_ETCHES) != 0) border++;
	if ((flags & OS.Pt_ALL_OUTLINES) != 0) border++;
	if ((flags & OS.Pt_ALL_INLINES) != 0) border++;
//	if ((flags & OS.Pt_ALL_BEVELS) != 0) border += args [7];
	return border;
}

public Rectangle getBounds () {
	checkWidget();
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
	checkWidget();
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public Object getLayoutData () {
	checkWidget();
	return layoutData;
}

public Point getLocation () {
	checkWidget();
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	return new Point (area.pos_x, area.pos_y);
}

public Menu getMenu () {
	checkWidget();
	return null;
}

public Composite getParent () {
	checkWidget();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

public Point getSize () {
	checkWidget();
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	return new Point (area.size_w, area.size_h);
}

public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

public Shell getShell () {
	checkWidget();
	return parent.getShell ();
}

public boolean getVisible () {
	checkWidget();
	int topHandle = topHandle ();
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (topHandle, args.length / 3, args);
	return (args [1] & OS.Pt_DELAY_REALIZE) == 0;
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) != 0;
}

void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	int focusHandle = focusHandle ();
	OS.PtAddFilterCallback (handle, OS.Ph_EV_KEY, windowProc, SWT.KeyDown);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BUT_PRESS, windowProc, SWT.MouseDown);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BUT_RELEASE, windowProc, SWT.MouseUp);
	OS.PtAddEventHandler (handle, OS.Ph_EV_PTR_MOTION, windowProc, SWT.MouseMove);
	OS.PtAddEventHandler (handle, OS.Ph_EV_BOUNDARY, windowProc, SWT.MouseEnter);	
	OS.PtAddCallback (focusHandle, OS.Pt_CB_GOT_FOCUS, windowProc, SWT.FocusIn);
	OS.PtAddCallback (focusHandle, OS.Pt_CB_LOST_FOCUS, windowProc, SWT.FocusOut);
	OS.PtAddCallback (handle, OS.Pt_CB_MENU, windowProc, SWT.Show);
}

int focusHandle () {
	return handle;
}

public int internal_new_GC (GCData data) {
	checkWidget();
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
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

public boolean isFocusControl () {
	checkWidget();
	return hasFocus ();
}

public boolean isReparentable () {
	checkWidget();
	return false;
}

public boolean isVisible () {
	checkWidget();
	return OS.PtWidgetIsRealized (handle);
}

Decorations menuShell () {
	return parent.menuShell ();
}

public void moveAbove (Control control) {
	checkWidget();
	int topHandle1 = topHandle ();
	if (control == null) {
		OS.PtWidgetToFront (topHandle1);
		OS.PtWindowToFront (topHandle1);
		return;
	}
	if (control.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int topHandle2 = control.topHandle ();
	OS.PtWidgetInsert (topHandle1, topHandle2, 0);
}

public void moveBelow (Control control) {
	checkWidget();
	int topHandle1 = topHandle ();
	if (control == null) {
		OS.PtWidgetToBack (topHandle1);
		OS.PtWindowToBack (topHandle1);
		return;
	}
	if (control.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int topHandle2 = control.topHandle ();
	OS.PtWidgetInsert (topHandle1, topHandle2, 1);
}

public void pack () {
	checkWidget();
	pack (true);
}

public void pack (boolean changed) {
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

int processPaint (int damage) {
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processFocusIn (int info) {
	sendEvent (SWT.FocusIn);
	if (isDisposed ()) return OS.Pt_CONTINUE;

	int index = 0;
	Shell shell = getShell ();
	Control [] focusIn = getPath ();
	Control lastFocus = shell.lastFocus;
	if (lastFocus != null) {
		if (!lastFocus.isDisposed ()) {
			Control [] focusOut = lastFocus.getPath ();
			int length = Math.min (focusIn.length, focusOut.length);
			while (index < length) {
				if (focusIn [index] != focusOut [index]) break;
				index++;
			}
			for (int i=focusOut.length-1; i>=index; --i) {
				focusOut [i].sendEvent (SWT.Deactivate);
			}
		}
		shell.lastFocus = null;
	}
	for (int i=focusIn.length-1; i>=index; --i) {
		focusIn [i].sendEvent (SWT.Activate);
	}

	/*
	* Feature in Photon.  Cannot return Pt_END
	* or the text widget will not take focus.
	*/
	return OS.Pt_CONTINUE;
}

int processFocusOut (int info) {
	sendEvent (SWT.FocusOut);
	if (isDisposed ()) return OS.Pt_CONTINUE;

	Shell shell = getShell ();
	shell.lastFocus = this;
	Display display = getDisplay ();
	Control focusControl = display.getFocusControl ();
	if (focusControl == null || shell != focusControl.getShell ()) {
		Control [] focusOut = getPath ();
		for (int i=focusOut.length-1; i>=0; --i) {
			focusOut [i].sendEvent (SWT.Deactivate);
		}
		shell.lastFocus = null;
	}

	/*
	* Feature in Photon.  Cannot return Pt_END
	* or the text widget will not take focus.
	*/
	return OS.Pt_CONTINUE;
}

int processKey (int info) {
	if (!hasFocus ()) return OS.Pt_PROCESS;
	if (info == 0) return OS.Pt_PROCESS;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_PROCESS;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	if ((ev.processing_flags & OS.Ph_FAKE_EVENT) != 0) {
		return OS.Pt_PROCESS;
	}
	int data = OS.PhGetData (cbinfo.event);
	if (data == 0) return OS.Pt_PROCESS;
	PhKeyEvent_t ke = new PhKeyEvent_t ();
	OS.memmove (ke, data, PhKeyEvent_t.sizeof);

	/*
	* Feature in Photon.  When the user presses certain keys
	* (such as the arrow keys), Photon sends 2 event for one
	* key press.  The first event has only the scan code while
	* the second has the keysym and other information.  This
	* also happens for key release.  The fix is to ignore the
	* first event.
	*/
	if (ke.key_flags == OS.Pk_KF_Scan_Valid) {
		return OS.Pt_PROCESS;
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
					return OS.Pt_PROCESS;
			}
		}
	}
						
	if ((ke.key_flags & (OS.Pk_KF_Key_Down | OS.Pk_KF_Key_Repeat)) != 0) {
		
		/*
		* Fetuare in Photon.  The key_sym value is not valid when Ctrl
		* or Alt is pressed. The fix is to detect this case and try to
		* use the key_cap value.
		*/
		int key = ke.key_sym;
		if ((ke.key_flags & OS.Pk_KF_Sym_Valid) == 0) {
			key = 0;
			if ((ke.key_flags & OS.Pk_KF_Cap_Valid) != 0) {
				if (ke.key_cap == OS.Pk_Tab && (ke.key_mods & OS.Pk_KM_Ctrl) != 0) {
					key = OS.Pk_Tab;
				}
			}
		}
		switch (key) {
			case OS.Pk_Escape:
			case OS.Pk_Return:
			case OS.Pk_KP_Tab:
			case OS.Pk_Tab:
			case OS.Pk_Up:
			case OS.Pk_Down:
			case OS.Pk_Left:
			case OS.Pk_Right: {
				if (key != OS.Pk_Return) {
					ev.processing_flags |= OS.Ph_NOT_CUAKEY;
					OS.memmove (cbinfo.event, ev, PhEvent_t.sizeof);
				}
				if (translateTraversal (key, ke)) {
					ev.processing_flags |= OS.Ph_CONSUMED;
					OS.memmove (cbinfo.event, ev, PhEvent_t.sizeof);
					return OS.Pt_PROCESS;
				}
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
				case OS.Pk_Clear: 		event.character = 0xB; break;
				case OS.Pk_Return: 		event.character = '\r'; break;
				case OS.Pk_Pause:		event.character = 0x13; break;
				case OS.Pk_Scroll_Lock:	event.character = 0x14; break;
				case OS.Pk_Escape:		event.character = 0x1B; break;
				case OS.Pk_Delete:		event.character = 0x7F; break;
				default:
					event.character = (char) ke.key_sym;
			}
		}
		display.lastKey = event.keyCode;
		display.lastAscii = event.character;
	}
	setKeyState(event, ke);
	if (type == SWT.KeyUp) {	
		if (event.keyCode == 0) event.keyCode = display.lastKey;
		if (event.character == 0) event.character = (char) display.lastAscii;
	}
	postEvent (type, event);

	return OS.Pt_PROCESS;
}

int processMouse (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	if ((ev.processing_flags & OS.Ph_FAKE_EVENT) != 0) {
		return OS.Pt_CONTINUE;
	}
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
	if (ev.type == OS.Ph_EV_BUT_PRESS || ev.type == OS.Ph_EV_BUT_RELEASE) {
		switch (pe.buttons) {
			case OS.Ph_BUTTON_SELECT:	event.button = 1; break;
			case OS.Ph_BUTTON_ADJUST:	event.button = 2; break;
			case OS.Ph_BUTTON_MENU:		event.button = 3; break;
		}
	}
	setMouseState (ev.type, event, pe);
	postEvent (event.type, event);
	if (ev.type == OS.Ph_EV_BUT_PRESS && pe.click_count == 2) {
		Event clickEvent = new Event ();
		clickEvent.time = event.time;
		clickEvent.x = event.x;
		clickEvent.y = event.y;
		clickEvent.button = event.button;
		clickEvent.stateMask = event.stateMask;
		postEvent (SWT.MouseDoubleClick, clickEvent);
	}
	ev.processing_flags |= OS.Ph_CONSUMED;
	OS.memmove (cbinfo.event, ev, PhEvent_t.sizeof);
	return OS.Pt_CONTINUE;
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
		case OS.Ph_EV_PTR_STEADY:
			postEvent (SWT.MouseHover, event);
			destroyToolTip (toolTipHandle);
			toolTipHandle = createToolTip (toolTipText, handle, getFont ().handle);
			break;
		case OS.Ph_EV_PTR_UNSTEADY:
			destroyToolTip (toolTipHandle);
			toolTipHandle = 0;
			break;		
	}
	return OS.Pt_END;
}

int processShow (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.reason == OS.Pt_CB_MENU) {
		if (menu != null && !menu.isDisposed ()) {
			menu.setVisible (true);
		}
	}	
	return OS.Pt_CONTINUE;
}

void realizeWidget() {
	int parentHandle = parent.handle;
	if (OS.PtWidgetIsRealized (parentHandle)) {
		OS.PtRealizeWidget (topHandle ());
	}
}

void releaseWidget () {
	super.releaseWidget ();
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	parent = null;
	layoutData = null;
}

public void redraw () {
	checkWidget();
	OS.PtDamageWidget (handle);
}

public void redraw (int x, int y, int width, int height, boolean allChildren) {
	checkWidget ();
	if (width <= 0 || height <= 0) return;
	PhRect_t rect = new PhRect_t ();
	rect.ul_x = (short) x;
	rect.ul_y = (short) y;
	rect.lr_x = (short) (x + width - 1);
	rect.lr_y = (short) (y + height - 1);
	OS.PtDamageExtent (handle, rect);
}

public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

public void removeFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}

public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

public void removeMouseListener (MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}

public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

public void removeTraverseListener(TraverseListener listener) {
	checkWidget();
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
	checkWidget();
	setBounds (x, y, width, height, true, true);
}

public void setBounds (Rectangle rect) {
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

public void setCapture (boolean capture) {
	checkWidget();
}

public void setCursor (Cursor cursor) {
	checkWidget();
	int type = OS.Ph_CURSOR_INHERIT;
	int bitmap = 0;
	if (cursor != null) {
		if (cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		type = cursor.type;
		bitmap = cursor.bitmap;
	}
	int [] args = new int []{
		OS.Pt_ARG_CURSOR_TYPE, type, 0,
		OS.Pt_ARG_BITMAP_CURSOR, bitmap, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	
	/*
	* Bug in Photon. For some reason, the widget cursor will
	* not change, when the new cursor is a bitmap cursor, if
	* the flag Ph_CURSOR_NO_INHERIT is reset. The fix is to reset
	* this flag after changing the cursor type to Ph_CURSOR_BITMAP.
	*/
	if (type == OS.Ph_CURSOR_BITMAP) {
		type &= ~OS.Ph_CURSOR_NO_INHERIT;
		args = new int []{OS.Pt_ARG_CURSOR_TYPE, type, 0};
		OS.PtSetResources (handle, args.length / 3, args);
	}
}

public void setEnabled (boolean enabled) {
	checkWidget();
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public boolean setFocus () {
	checkWidget();
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
	if (tile.rect_ul_x != tile.rect_lr_x || tile.rect_ul_y != tile.rect_lr_y) {
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
		event.gc = null;
	}
	OS.PhFreeTiles (damage);
}

boolean sendResize () {
	return true;
}

public void setBackground (Color color) {
	checkWidget();
	int pixel = OS.Pt_DEFAULT_COLOR;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	int [] args = {OS.Pt_ARG_FILL_COLOR, pixel, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setFont (Font font) {
	checkWidget();
	byte[] buffer;
	if (font != null) {
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		buffer = font.handle;
	} else {
		buffer = defaultFont();
	}
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
	checkWidget();
	int pixel = OS.Pt_DEFAULT_COLOR;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	int [] args = {OS.Pt_ARG_COLOR, pixel, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

void setKeyState(Event event, PhKeyEvent_t ke) {
	int key_mods = ke.key_mods;
	int button_state = ke.button_state;
	if ((key_mods & OS.Pk_KM_Alt) != 0) {
		if (event.type != SWT.KeyDown || event.keyCode != SWT.ALT) {
			event.stateMask |= SWT.ALT;
		}
	}
	if ((key_mods & OS.Pk_KM_Shift) != 0) {
		if (event.type != SWT.KeyDown || event.keyCode != SWT.SHIFT) {
			event.stateMask |= SWT.SHIFT;
		}
	}
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) {
		if (event.type != SWT.KeyDown || event.keyCode != SWT.CONTROL) {
			event.stateMask |= SWT.CONTROL;
		}
	}
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	if (event.type == SWT.KeyUp) {	
		if (event.keyCode == SWT.ALT) event.stateMask |= SWT.ALT;
		if (event.keyCode == SWT.SHIFT) event.stateMask |= SWT.SHIFT;
		if (event.keyCode == SWT.CONTROL) event.stateMask |= SWT.CONTROL;
	}
}

public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

public void setLocation (int x, int y) {
	checkWidget();
	setBounds (x, y, 0, 0, true, false);
}

public void setLocation (Point location) {
	checkWidget();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setMenu (Menu menu) {
	checkWidget();
	int flags = 0;
	if (menu != null) {
		if (menu.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		flags = OS.Pt_MENUABLE;
	}
	int [] args = {
		OS.Pt_ARG_FLAGS, flags, OS.Pt_ALL_BUTTONS | OS.Pt_MENUABLE,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	this.menu = menu;
}

void setMouseState(int type, Event event, PhPointerEvent_t pe) {
	int key_mods = pe.key_mods;
	int buttons = pe.buttons;
	int button_state = pe.button_state;
	if ((key_mods & OS.Pk_KM_Alt) != 0) event.stateMask |= SWT.ALT;
	if ((key_mods & OS.Pk_KM_Shift) != 0) event.stateMask |= SWT.SHIFT;
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) event.stateMask |= SWT.CONTROL;
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	if (type == OS.Ph_EV_BUT_PRESS) {
		if (buttons == OS.Ph_BUTTON_SELECT) event.stateMask &= ~SWT.BUTTON1;
		if (buttons == OS.Ph_BUTTON_ADJUST) event.stateMask &= ~SWT.BUTTON2;
		if (buttons == OS.Ph_BUTTON_MENU) event.stateMask &= ~SWT.BUTTON3;
	} else if (type == OS.Ph_EV_BUT_RELEASE || type == OS.Ph_EV_DRAG) {
		if (buttons == OS.Ph_BUTTON_SELECT) event.stateMask |= SWT.BUTTON1;
		if (buttons == OS.Ph_BUTTON_ADJUST) event.stateMask |= SWT.BUTTON2;
		if (buttons == OS.Ph_BUTTON_MENU) event.stateMask |= SWT.BUTTON3;
	}
}

public boolean setParent (Composite parent) {
	checkWidget();
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

public void setSize (Point size) {
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		OS.PtContainerRelease (handle);
	} else {
		OS.PtContainerHold (handle);
	}
}
public void setSize (int width, int height) {
	checkWidget();
	setBounds (0, 0, width, height, false, true);
}

public void setVisible (boolean visible) {
	checkWidget();
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
	checkWidget();
	toolTipText = string;
}

void setZOrder() {
	OS.PtWidgetToBack (topHandle ());
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

public Point toControl (Point point) {
	checkWidget();
	short [] x = new short [1], y = new short [1];
	OS.PtGetAbsPosition (handle, x, y);
	return new Point (point.x - x [0], point.y - y [0]);
}

public Point toDisplay (Point point) {
	checkWidget();
	short [] x = new short [1], y = new short [1];
	OS.PtGetAbsPosition (handle, x, y);
	return new Point (point.x + x [0], point.y + y [0]);
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	int detail = 0;
	boolean shift = (phEvent.key_mods & OS.Pk_KM_Shift) != 0;
	boolean control = (phEvent.key_mods & OS.Pk_KM_Ctrl) != 0;
	switch (key_sym) {
		case OS.Pk_Escape:
			Shell shell = getShell ();
			if (shell.parent == null) return false;
			if (!shell.isVisible () || !shell.isEnabled ()) return false;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		case OS.Pk_Return:
			Button button = menuShell ().getDefaultButton ();
			if (button == null || button.isDisposed ()) return false;
			if (!button.isVisible () || !button.isEnabled ()) return false;
			detail = SWT.TRAVERSE_RETURN;
			break;
		case OS.Pk_Tab:
		case OS.Pk_KP_Tab:
			detail = SWT.TRAVERSE_TAB_NEXT;
			if (shift) detail = SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		case OS.Pk_Up:
		case OS.Pk_Left: 
			detail = SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		case OS.Pk_Down:
		case OS.Pk_Right:
			detail = SWT.TRAVERSE_ARROW_NEXT;
			break;
		default:
			return false;
	}
	boolean doit = (detail & traversalCode (key_sym, phEvent)) != 0;
	if (!doit && control && (key_sym == OS.Pk_Tab || key_sym == OS.Pk_Tab)) {
		doit = true;
		control = false;
	}
	if (hooks (SWT.Traverse)) {
		Event event = new Event();
		event.doit = doit;
		event.detail = detail;
		setKeyState (event, phEvent);
		sendEvent (SWT.Traverse, event);
		if (isDisposed ()) return true;
		doit = event.doit;
		detail = event.detail;
	}
	if (doit) {
		switch (detail) {
			case SWT.TRAVERSE_ESCAPE:		return traverseEscape ();
			case SWT.TRAVERSE_RETURN:		return traverseReturn ();
			case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true, control);
			case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false, control);
			case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
			case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);	
		}
	}
	return false;
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	return
		SWT.TRAVERSE_ESCAPE | SWT.TRAVERSE_RETURN |
		SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS |
		SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS;
}

public boolean traverse (int traversal) {
	checkWidget();
	if (!isFocusControl () && !setFocus ()) return false;
	switch (traversal) {
		case SWT.TRAVERSE_ESCAPE:		return traverseEscape ();
		case SWT.TRAVERSE_RETURN:		return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true, false);
		case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false, false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);	
	}
	return false;
}

boolean traverseEscape () {
	Shell shell = getShell ();
	if (shell.parent == null) return false;
	if (!shell.isVisible () || !shell.isEnabled ()) return false;
	shell.close ();
	return true;
}

boolean traverseGroup (boolean next, boolean control) {
	if (control) {
		if (next) return OS.PtGlobalFocusPrevContainer (handle, null) != 0;
		return OS.PtGlobalFocusNextContainer (handle, null) != 0;
	}
	if (next) return OS.PtGlobalFocusPrev (handle, null) != 0;
	return OS.PtGlobalFocusNext (handle, null) != 0;
}

boolean traverseItem (boolean next) {
	if (next) return OS.PtContainerFocusPrev (handle, null) != 0;
	return OS.PtContainerFocusNext (handle, null) != 0;
}

boolean traverseReturn () {
	Button button = menuShell ().getDefaultButton ();
	if (button == null || button.isDisposed ()) return false;
	if (!button.isVisible () || !button.isEnabled ()) return false;
	button.click ();
	return true;
}

public void update () {
	checkWidget();
	OS.PtFlush ();
}

}

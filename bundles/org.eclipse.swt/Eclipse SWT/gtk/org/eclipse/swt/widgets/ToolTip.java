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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class ToolTip extends Widget {
	Shell parent;
	String text, message;
	TrayItem item;
	int x, y, timerId;
	int /*long*/ layoutText = 0, layoutMessage = 0;
	int [] borderPolygon;
	boolean spikeAbove, autohide;
	
	static final int BORDER = 5;
	static final int PADDING = 5;
	static final int INSET = 4;
	static final int TIP_HEIGHT = 20;
	static final int IMAGE_SIZE = 16;
	static final int DELAY = 8000;

public ToolTip (Shell parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

static int checkStyle (int style) {
	int mask = SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING;
	if ((style & mask) == 0) return style;
	return checkBits (style, SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR, 0, 0, 0);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void configure () {
	int /*long*/ screen = OS.gdk_screen_get_default ();
	OS.gtk_widget_realize (handle);
	int monitorNumber = OS.gdk_screen_get_monitor_at_window (screen, OS.GTK_WIDGET_WINDOW (handle));
	GdkRectangle dest = new GdkRectangle ();
	OS.gdk_screen_get_monitor_geometry (screen, monitorNumber, dest);
	Point point = getSize (dest.width / 4);
	int w = point.x;
	int h = point.y;
	point = getLocation ();
	int x = point.x;
	int y = point.y;
	OS.gtk_window_resize (handle, w, h + TIP_HEIGHT);
	int[] polyline;
	spikeAbove = dest.height >= y + h + TIP_HEIGHT;
	if (dest.width >= x + w) {
		if (dest.height >= y + h + TIP_HEIGHT) {
			int t = TIP_HEIGHT;
			polyline = new int[] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t,  5, 1+t, 5, t, 
				16, t, 16, 0, 35, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
				0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
				16, t, 16, 1, 35, t,
				w-6, 0+t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
				w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
				5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
				0, 5+t};
			OS.gtk_window_move (handle, Math.max(0, x - 17), y);
		} else {
			polyline = new int[] {
				0, 5, 1, 5, 1, 3, 3, 1,  5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				35, h, 16, h+TIP_HEIGHT, 16, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
				0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
				w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
				w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
				35, h-1, 17, h+TIP_HEIGHT-2, 17, h-1,
				5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
				0, 5};
			OS.gtk_window_move (handle, Math.max(0, x - 17), y - h - TIP_HEIGHT);
		}
	} else {
		if (dest.height >= y + h + TIP_HEIGHT) {
			int t = TIP_HEIGHT;
			polyline = new int[] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t,  5, 1+t, 5, t, 
				w-35, t, w-16, 0, w-16, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
				0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
				w-35, t, w-17, 2, w-17, t,
				w-6, t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
				w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
				5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
				0, 5+t};
			OS.gtk_window_move (handle, Math.min(dest.width - w, x - w + 17), y);
		} else {
			polyline = new int[] {
				0, 5, 1, 5, 1, 3, 3, 1,  5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				w-16, h, w-16, h+TIP_HEIGHT, w-35, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
				0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
				w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
				w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
				w-17, h-1, w-17, h+TIP_HEIGHT-2, w-36, h-1,
				5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
				0, 5};
			OS.gtk_window_move (handle, Math.min(dest.width - w, x - w + 17), y - h - TIP_HEIGHT);
		}
	}
	int /*long*/ rgn = OS.gdk_region_polygon (polyline, polyline.length / 2, OS.GDK_EVEN_ODD_RULE);
	OS.gtk_widget_realize (handle);
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (handle);
	OS.gdk_window_shape_combine_region (window, rgn, 0, 0);
	OS.gdk_region_destroy (rgn);
}

void createHandle (int index) {
	state |= HANDLE;
	if ((style & SWT.BALLOON) != 0) {
		handle = OS.gtk_window_new (OS.GTK_WINDOW_POPUP);
		Color background = display.getSystemColor (SWT.COLOR_INFO_BACKGROUND);
		OS.gtk_widget_modify_bg (handle, OS.GTK_STATE_NORMAL, background.handle);
		OS.gtk_widget_set_app_paintable (handle, true);
	} else {
		handle = OS.gtk_tooltips_new ();
		if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		OS.gtk_tooltips_force_window (handle);
		OS.g_object_ref (handle);
		OS.gtk_object_sink (handle);
	}
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
	message = "";
	x = y = -1;
	autohide = true;
}

void deregister () {
	super.deregister ();
	if ((style & SWT.BALLOON) == 0) {
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
		if (tipWindow != 0) display.removeWidget (tipWindow);
	}
}

void destroyWidget () {
	int /*long*/ topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0 && (state & HANDLE) != 0) {
		if ((style & SWT.BALLOON) != 0) {
			OS.gtk_widget_destroy (topHandle);
		} else {
			OS.g_object_unref (topHandle);
		}
	}
}

public boolean getAutoHide () {
	checkWidget ();
	return autohide;
}

Point getLocation () {
	int x = this.x;
	int y = this.y;
	if (item != null) {
		int /*long*/ itemHandle = item.handle; 
		OS.gtk_widget_realize (itemHandle);
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (itemHandle);
		int [] px = new int [1], py = new int [1];
		OS.gdk_window_get_origin (window, px, py);
		x = px [0] + OS.GTK_WIDGET_WIDTH (itemHandle) / 2;
		y = py [0] + OS.GTK_WIDGET_HEIGHT (itemHandle) / 2;
	}
	if (x == -1 || y == -1) {
		int [] px = new int [1], py = new int [1];
		OS.gdk_window_get_pointer (0, px, py, null);
		x = px [0];
		y = py [0];
	}
	return new Point(x, y);
}

public String getMessage () {
	checkWidget ();
	return message;
}

String getNameText () {
	return getText ();
}

public Shell getParent () {
	checkWidget ();
	return parent;
}

Point getSize (int maxWidth) {
	int textWidth = 0, messageWidth = 0;
	int [] w = new int [1], h = new int [1];
	if (layoutText != 0) {
		OS.pango_layout_set_width (layoutText, -1);
		OS.pango_layout_get_size (layoutText, w, h);
		textWidth = OS.PANGO_PIXELS (w [0]);
	}
	if (layoutMessage != 0) {
		OS.pango_layout_set_width (layoutMessage, -1);
		OS.pango_layout_get_size (layoutMessage, w, h);
		messageWidth = OS.PANGO_PIXELS (w [0]);
	}
	int messageTrim = 2 * INSET + 2 * BORDER + 2 * PADDING;
	boolean hasImage = layoutText != 0 && (style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING)) != 0;
	int textTrim = messageTrim + (hasImage ? IMAGE_SIZE : 0);  
	int width = Math.min (maxWidth, Math.max (textWidth + textTrim, messageWidth + messageTrim));
	int textHeight = 0, messageHeight = 0;
	if (layoutText != 0) {
		OS.pango_layout_set_width (layoutText, (maxWidth - textTrim) * OS.PANGO_SCALE);
		OS.pango_layout_get_size (layoutText, w, h);
		textHeight = OS.PANGO_PIXELS (h [0]);
	}
	if (layoutMessage != 0) {
		OS.pango_layout_set_width (layoutMessage, (maxWidth - messageTrim) * OS.PANGO_SCALE);
		OS.pango_layout_get_size (layoutMessage, w, h);
		messageHeight = OS.PANGO_PIXELS (h [0]);
	}
	int height = 2 * BORDER + 2 * PADDING + messageHeight;
	if (layoutText != 0) height += Math.max (IMAGE_SIZE, textHeight) + 2 * PADDING;
	return new Point(width, height);
}

public String getText () {
	checkWidget ();
	return text;
}

public boolean getVisible () {
	checkWidget ();
	if ((style & SWT.BALLOON) != 0) return OS.GTK_WIDGET_VISIBLE (handle);
	int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
	return OS.GTK_WIDGET_VISIBLE (tipWindow);
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	notifyListeners (SWT.Selection, new Event ());
	setVisible (false);
	return 0;
}

int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ eventPtr) {
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (handle);
	int /*long*/ gdkGC = OS.gdk_gc_new (window);
	OS.gdk_draw_polygon (window, gdkGC, 0, borderPolygon, borderPolygon.length / 2);
	int x = BORDER + PADDING;
	int y = BORDER + PADDING;
	if (spikeAbove) y += TIP_HEIGHT;
	if (layoutText != 0) {
		byte[] buffer = null;
		int id = style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING);
		switch (id) {
			case SWT.ICON_ERROR: buffer = Converter.wcsToMbcs (null, "gtk-dialog-error", true); break; 
			case SWT.ICON_INFORMATION: buffer = Converter.wcsToMbcs (null, "gtk-dialog-info", true); break;
			case SWT.ICON_WARNING: buffer = Converter.wcsToMbcs (null, "gtk-dialog-warning", true); break;
		}
		if (buffer != null) {
			int /*long*/ style = OS.gtk_widget_get_default_style ();
			int /*long*/ pixbuf = OS.gtk_icon_set_render_icon (
				OS.gtk_icon_factory_lookup_default (buffer), 
				style,
				OS.GTK_TEXT_DIR_NONE, 
				OS.GTK_STATE_NORMAL, 
				OS.GTK_ICON_SIZE_MENU,
				0, 
				0);
			OS.gdk_draw_pixbuf (window, gdkGC, pixbuf, 0, 0, x, y, IMAGE_SIZE, IMAGE_SIZE, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
			OS.g_object_unref (pixbuf);
			x += IMAGE_SIZE;
		}
		x += INSET;
		OS.gdk_draw_layout (window, gdkGC, x, y, layoutText);
		int [] w = new int [1], h = new int [1];
		OS.pango_layout_get_size (layoutText, w, h);
		y += 2 * PADDING + Math.max (IMAGE_SIZE, OS.PANGO_PIXELS (h [0]));
	}
	if (layoutMessage != 0) {
		x = BORDER + PADDING + INSET;
		OS.gdk_draw_layout (window, gdkGC, x, y, layoutMessage);
	}
	OS.g_object_unref (gdkGC);
	return 0;
}

int /*long*/ gtk_size_allocate (int /*long*/ widget, int /*long*/ allocation) {
	Point point = getLocation (); 
	int x = point.x;
	int y = point.y;
	int /*long*/ screen = OS.gdk_screen_get_default ();
	OS.gtk_widget_realize (widget);
	int monitorNumber = OS.gdk_screen_get_monitor_at_window (screen, OS.GTK_WIDGET_WINDOW (widget));
	GdkRectangle dest = new GdkRectangle ();
	OS.gdk_screen_get_monitor_geometry (screen, monitorNumber, dest);
	int w = OS.GTK_WIDGET_WIDTH (widget);
	int h = OS.GTK_WIDGET_HEIGHT (widget);
	if (dest.height < y + h) y -= h;
	if (dest.width < x + w) x -= w;
	OS.gtk_window_move (widget, x, y);
	return 0;
}

void hookEvents () {
	if ((style & SWT.BALLOON) != 0) {
		OS.g_signal_connect_closure (handle, OS.expose_event, display.closures [EXPOSE_EVENT], false);
		OS.gtk_widget_add_events (handle, OS.GDK_BUTTON_PRESS_MASK);
		OS.g_signal_connect_closure (handle, OS.button_press_event, display.closures [BUTTON_PRESS_EVENT], false);
	} else {
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
		if (tipWindow != 0) {
			OS.g_signal_connect_closure (tipWindow, OS.size_allocate, display.closures [SIZE_ALLOCATE], false);
			OS.gtk_widget_add_events (tipWindow, OS.GDK_BUTTON_PRESS_MASK);
			OS.g_signal_connect_closure (tipWindow, OS.button_press_event, display.closures [BUTTON_PRESS_EVENT], false);
		}
	}
}

public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

void register () {
	super.register ();
	if ((style & SWT.BALLOON) == 0) {
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
		if (tipWindow != 0) display.addWidget (tipWindow, this);
	}
}

void releaseWidget () {
	super.releaseWidget ();
	if (layoutText != 0) OS.g_object_unref (layoutText);
	layoutText = 0;
	if (layoutMessage != 0) OS.g_object_unref (layoutMessage);
	layoutMessage = 0;
	if (timerId != 0) OS.gtk_timeout_remove(timerId);
	timerId = 0;
	text = null;
	message = null;
	borderPolygon = null;
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

public void setAutoHide (boolean autohide) {
	checkWidget ();
	this.autohide = autohide;
	//TODO - update when visible
}

public void setLocation (int x, int y) {
	checkWidget ();
	this.x = x;
	this.y = y;
	if ((style & SWT.BALLOON) != 0) {
		if (OS.GTK_WIDGET_VISIBLE (handle)) configure ();
	} else {
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
		if (OS.GTK_WIDGET_VISIBLE (tipWindow)) {
			OS.gtk_window_move (tipWindow, x, y);
		}
	}
}

public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setMessage (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
	if ((style & SWT.BALLOON) == 0) return;
	if (layoutMessage != 0) OS.g_object_unref (layoutMessage);
	layoutMessage = 0;
	if (message.length () != 0) {
		byte [] buffer = Converter.wcsToMbcs (null, message, true);
		layoutMessage = OS.gtk_widget_create_pango_layout (handle, buffer);
		OS.pango_layout_set_wrap (layoutMessage, OS.PANGO_WRAP_WORD_CHAR);
	}
	if (OS.GTK_WIDGET_VISIBLE (handle)) configure ();
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	if ((style & SWT.BALLOON) == 0) return;
	if (layoutText != 0) OS.g_object_unref (layoutText);
	layoutText = 0;
	if (text.length () != 0) {
		byte [] buffer = Converter.wcsToMbcs (null, text, true);
		layoutText = OS.gtk_widget_create_pango_layout (handle, buffer);
		int /*long*/ boldAttr = OS.pango_attr_weight_new (OS.PANGO_WEIGHT_BOLD);
		PangoAttribute attribute = new PangoAttribute ();
		OS.memmove (attribute, boldAttr, PangoAttribute.sizeof);
		attribute.start_index = 0;
		attribute.end_index = buffer.length;
		OS.memmove (boldAttr, attribute, PangoAttribute.sizeof);
		int /*long*/ attrList = OS.pango_attr_list_new ();
		OS.pango_attr_list_insert (attrList, boldAttr);
		OS.pango_layout_set_attributes (layoutText, attrList);
		OS.pango_attr_list_unref (attrList);
		OS.pango_layout_set_wrap (layoutText, OS.PANGO_WRAP_WORD_CHAR);
	}
	if (OS.GTK_WIDGET_VISIBLE (handle)) configure ();
}

public void setVisible (boolean visible) {
	if (timerId != 0) OS.gtk_timeout_remove(timerId);
	timerId = 0;
	if (visible) {
		if ((style & SWT.BALLOON) != 0) {
			configure ();
			OS.gtk_widget_show (handle);
		} else {
			int /*long*/ vboxHandle = parent.vboxHandle;
			StringBuffer string = new StringBuffer (text);
			if (text.length () > 0) string.append ("\n\n");
			string.append (message);
			byte [] buffer = Converter.wcsToMbcs (null, string.toString(), true);
			OS.gtk_tooltips_set_tip (handle, vboxHandle, buffer, null);
			int /*long*/ data = OS.gtk_tooltips_data_get (vboxHandle);
			OS.GTK_TOOLTIPS_SET_ACTIVE (handle, data);
			OS.gtk_tooltips_set_tip (handle, vboxHandle, buffer, null);
		}		
		if (autohide) timerId = OS.gtk_timeout_add (DELAY, display.windowTimerProc, handle);
	} else {
		if ((style & SWT.BALLOON) != 0) {
			OS.gtk_widget_hide (handle);
		} else {
			int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
			OS.gtk_widget_hide (tipWindow);
		}
	}
}

int /*long*/ timerProc (int /*long*/ widget) {
	if ((style & SWT.BALLOON) != 0) {
		OS.gtk_widget_hide (handle);
	} else {
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (handle);
		OS.gtk_widget_hide (tipWindow);
	}
	return 0;
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * This class contains static helpers wrapping some common
 * widget-inspecific operations like get the size of a gtk widget.
 */
class UtilFuncs {

/*
 *   ===  GEOMETRY  ===
 */

static Point getLocation (int handle) {
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, handle, GtkWidget.sizeof);
	return new Point (widget.alloc_x, widget.alloc_y);
}

static boolean setLocation(int parentHandle, int handle, int x, int y) {
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, handle, GtkWidget.sizeof);
	boolean sameOrigin = (widget.alloc_x == x && widget.alloc_y == y);

	// GtkFixed does not leave us alone.
	// Instead, it maintains its own list of geometries for the children.
	// Moreover, it will post a RESIZE on the queue that will cause
	// disturbance to all our brother; to avoid that, we temporarily
	// clear the VISIBLE flag, and do the synchronous update ourselves
	GtkObject gtkChild = new GtkObject();
	OS.memmove (gtkChild, handle, GtkObject.sizeof);
	OS.GTK_WIDGET_UNSET_FLAGS(handle, OS.GTK_VISIBLE);
	OS.gtk_fixed_move(parentHandle, handle, (short)x, (short)y );
	OS.memmove(handle, gtkChild, GtkObject.sizeof);
	

//	OS.gtk_widget_set_uposition(handle, (short)x, (short)y);
	/*
	byte[] aux_info_id = org.eclipse.swt.internal.Converter.wcsToMbcs (null, "gtk-aux-info", true);
	int aux_info_key_id = OS.g_quark_from_static_string(aux_info_id);
	if (aux_info_key_id == 0) SWT.error(SWT.ERROR_UNSPECIFIED);
	int aux_info = OS.gtk_object_get_data_by_id(handle, aux_info_key_id);
	int[] xy = new int[1];
	// ???
	OS.memmove(aux_info, xy, 4);
	*/
	
	// force allocation update NOW
	GtkAllocation alloc = new GtkAllocation();
	alloc.x = (short) x;
	alloc.y = (short) y;
	alloc.width = (short) widget.alloc_width;
	alloc.height = (short) widget.alloc_height;
	OS.memmove(handle, widget, GtkWidget.sizeof);
	OS.gtk_widget_size_allocate(handle, alloc);

	return (!sameOrigin);
}

static Point getSize (int handle) {
	if (handle==0) {
		SWT.error(SWT.ERROR_UNSPECIFIED);
	}
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, handle, GtkWidget.sizeof);
	return new Point (widget.alloc_width, widget.alloc_height);
}


static boolean setSize(int handle, int width, int height) {
	if (handle==0) {
		SWT.error(SWT.ERROR_UNSPECIFIED);
	}
	
	/* Feature in Gtk.
	 * Gtk will refuse to set the size of any widget to anything smaller than 3x3.
	 */
	if (height <= 3) height = 3;
	if (width <= 3)  width = 3;

	// first, see if we actually need to change anything
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, handle, GtkWidget.sizeof);
	int alloc_width = widget.alloc_width & 0xFFFF;
	int alloc_height = widget.alloc_height & 0xFFFF;
	if (alloc_width == width && alloc_height == height) {
		return false;
	}

	OS.gtk_widget_set_usize (handle, width, height);
	// force child allocation update
	GtkAllocation alloc = new GtkAllocation();
	alloc.x = (short) widget.alloc_x;
	alloc.y = (short) widget.alloc_y; 
	alloc.width = (short) width;
	alloc.height = (short) height;
	OS.gtk_widget_size_allocate(handle, alloc);
	return true;
}

static void setZeroSize(int handle) {
	// CHEATING.  For some reason,
	// the it will refuse to change its size to anything smaller
	setSize(handle, 3,3);
}

static int getFont(int widget) {
	int hStyle = OS.gtk_widget_get_style(widget);
	GtkStyle style = new GtkStyle();
	OS.memmove(style, hStyle, GtkStyle.sizeof);
	return style.font;
}

static void setFont(int handle, int font) {
	OS.gtk_widget_ensure_style(handle);
	// We can't just get the widget's style and set
	// its font, because the style slot may point to the
	// default font; therefore we have to obtain a clone
	// of the style
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, handle, GtkWidget.sizeof);
	int hStyle = OS.gtk_style_copy(widget.style);
	GtkStyle style = new GtkStyle();
	OS.memmove(style, hStyle, GtkStyle.sizeof);
	
	OS.gdk_font_unref(style.font);
	style.font = font;
	OS.memmove (hStyle, style, GtkStyle.sizeof);
	OS.gtk_widget_set_style (handle, hStyle);
}

}


/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

import org.eclipse.swt.internal.*;

public class GTK extends Platform {

/** Natives */

/** @param widget cast=(GtkWidget *) */
public static final native int _GTK_WIDGET_HEIGHT(int widget);
public static final int GTK_WIDGET_HEIGHT(int widget) {
	lock.lock();
	try {
		return _GTK_WIDGET_HEIGHT(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int _GTK_WIDGET_WIDTH(int widget);
public static final int GTK_WIDGET_WIDTH(int widget) {
	lock.lock();
	try {
		return _GTK_WIDGET_WIDTH(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 * @param proc cast=(GCallback)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_connect(int instance, byte[] detailed_signal, int proc, int data);
public static final int g_signal_connect(int instance, byte[] detailed_signal, int proc, int data) {
	lock.lock();
	try {
		return _g_signal_connect(instance, detailed_signal, proc, data);
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_events_pending();
public static final int gtk_events_pending() {
	lock.lock();
	try {
		return _gtk_events_pending();
	} finally {
		lock.unlock();
	}
}
/**
 * @param argc cast=(int *)
 * @param argv cast=(char ***)
 */
public static final native boolean _gtk_init_check(int[] argc, int[] argv);
public static final boolean gtk_init_check(int[] argc, int[] argv) {
	lock.lock();
	try {
		return _gtk_init_check(argc, argv);
	} finally {
		lock.unlock();
	}
}
public static final native void _gtk_main();
public static final void gtk_main() {
	lock.lock();
	try {
		_gtk_main();
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_main_iteration();
public static final int gtk_main_iteration() {
	lock.lock();
	try {
		return _gtk_main_iteration();
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_plug_new(int socket_id);
public static final int gtk_plug_new(int socket_id) {
	lock.lock();
	try {
		return _gtk_plug_new(socket_id);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_destroy(int widget);
public static final void gtk_widget_destroy(int widget) {
	lock.lock();
	try {
		_gtk_widget_destroy(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_show(int widget);
public static final void gtk_widget_show(int widget) {
	lock.lock();
	try {
		_gtk_widget_show(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_show_now(int widget);
public static final void gtk_widget_show_now(int widget) {
	lock.lock();
	try {
		_gtk_widget_show_now(widget);
	} finally {
		lock.unlock();
	}
}
/** @param type cast=(GtkWindowType) */
public static final native int _gtk_window_new(int type);
public static final int gtk_window_new(int type) {
	lock.lock();
	try {
		return _gtk_window_new(type);
	} finally {
		lock.unlock();
	}
}
}

package org.eclipse.swt.internal.gtk;

import org.eclipse.swt.internal.*;

public class GTK extends Platform {

/** Natives */
public static final synchronized native int GTK_WIDGET_HEIGHT(int widget);
public static final synchronized native int GTK_WIDGET_WIDTH(int widget);
public static final synchronized native int g_signal_connect(int instance, byte[] detailed_signal, int proc, int data);
public static final synchronized native int gtk_events_pending();
public static final synchronized native boolean gtk_init_check(int[] argc, int[] argv);
public static final synchronized native void gtk_main();
public static final synchronized native int gtk_main_iteration();
public static final synchronized native int gtk_plug_new(int socket_id);
public static final synchronized native void gtk_widget_destroy(int widget);
public static final synchronized native void gtk_widget_show(int widget);
public static final synchronized native void gtk_widget_show_now(int widget);
public static final synchronized native int gtk_window_new(int type);
}

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

/*
#define NO_CLIST
#define G_DISABLE_DEPRECATED
#define GTK_DISABLE_DEPRECATED
*/

#include "swt.h"
#include "structs.h"
#include <string.h>

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1ROOT_1PARENT
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GDK_1ROOT_1PARENT\n")

	return (jint)GDK_ROOT_PARENT();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I\n")

	return (jint)((GtkAccelLabel *)arg0)->accel_string;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II\n")

	((GtkAccelLabel *)arg0)->accel_string = (gchar *)arg1;
}

#ifndef NO_CLIST
#define CELL_SPACING 1
#define ROW_TOP_YPIXEL(clist, row) (((clist)->row_height * (row)) + \
				    (((row) + 1) * CELL_SPACING) + \
				    (clist)->voffset)
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_ROW_1TOP_1YPIXEL
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ROW_1TOP_1YPIXEL\n")

	return (jint)ROW_TOP_YPIXEL((GtkCList*)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1CLIST_1WINDOW
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1CLIST_1WINDOW\n")

	return (jint)((GtkCList *)arg0)->clist_window;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1COLUMN
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1COLUMN\n")

	return (jint)((GtkCList *)arg0)->column;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1COLUMN_1TITLE_1AREA_1HEIGHT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1COLUMN_1TITLE_1AREA_1HEIGHT\n")

	return (jint)((GtkCList *)arg0)->column_title_area.height;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1FOCUS_1ROW
	(JNIEnv *env, jclass that, jint arg0)
{
	return (jint)((GtkCList *)arg0)->focus_row;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1HADJUSTMENT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1HADJUSTMENT\n")

	return (jint)((GtkCList *)arg0)->hadjustment;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1HOFFSET
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1HOFFSET\n")

	return (jint)((GtkCList *)arg0)->hoffset;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1RESYNC_1SELECTION
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1RESYNC_1SELECTION\n")

	GTK_CLIST_GET_CLASS (arg0)->resync_selection ((GtkCList *)arg0, NULL);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1ROWS
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1ROWS\n")

	return (jint)((GtkCList *)arg0)->rows;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1ROW_1HEIGHT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1ROW_1HEIGHT\n")

	return (jint)((GtkCList *)arg0)->row_height;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1ROW_1LIST
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1ROW_1LIST\n")

	return (jint) ((GtkCList*)arg0) -> row_list;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1SELECTION
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1SELECTION\n")

	return (jint)((GtkCList *)arg0)->selection;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1SHOW_1TITLES
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1SHOW_1TITLES\n")

	return (jboolean)GTK_CLIST_SHOW_TITLES(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1VADJUSTMENT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1VADJUSTMENT\n")

	return (jint)((GtkCList *)arg0)->vadjustment;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1VOFFSET
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CLIST_1VOFFSET\n")

	return (jint)((GtkCList *)arg0)->voffset;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CLIST_1WINDOW_1WIDTH
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("OS_GTK_1CLIST_1WINDOW_1WIDTH\n")

	return (jint) ((GtkCList*)arg0)->clist_window_width;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CTREE_1TREE_1INDENT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CTREE_1TREE_1INDENT\n")

	return (jint)((GtkCTree *)arg0)->tree_indent;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CTREE_1NODE_1NEXT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CTREE_1NODE_1NEXT\n")

	return (jint) GTK_CTREE_NODE_NEXT((GtkCTreeNode*)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1CTREE_1ROW
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1CTREE_1ROW\n")

	return (jint)GTK_CTREE_ROW((GtkCTreeNode*)arg0);
}
#endif /* NO_CLIST */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1FLAGS
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1FLAGS\n")

	return (jint)GTK_WIDGET_FLAGS(arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1HAS_1DEFAULT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1HAS_1DEFAULT\n")

	return (jboolean)GTK_WIDGET_HAS_DEFAULT(arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1HAS_1FOCUS
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1HAS_1FOCUS\n")

	return (jboolean)GTK_WIDGET_HAS_FOCUS(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1HEIGHT
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1HEIGHT\n")

	return (jint)((GtkWidget *)arg0)->allocation.height;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1IS_1SENSITIVE
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1IS_1SENSITIVE\n")

	return (jboolean)GTK_WIDGET_IS_SENSITIVE(arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1MAPPED
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1MAPPED\n")

	return (jboolean)GTK_WIDGET_MAPPED(arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SENSITIVE
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1SENSITIVE\n")

	return (jboolean)GTK_WIDGET_SENSITIVE(arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SET_1FLAGS
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GTK_1WIDGET_1SET_1FLAGS\n")

	GTK_WIDGET_SET_FLAGS(arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1TYPE
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1TYPE\n")

	return (jint)GTK_WIDGET_TYPE(arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1UNSET_1FLAGS
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GTK_1WIDGET_1UNSET_1FLAGS\n")

	GTK_WIDGET_UNSET_FLAGS(arg0, arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1VISIBLE
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1VISIBLE\n")

	return (jboolean)GTK_WIDGET_VISIBLE(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1WIDTH
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1WIDTH\n")

	return (jint)((GtkWidget *)arg0)->allocation.width;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1WINDOW
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1WINDOW\n")

	return (jint)((GtkWidget *)arg0)->window;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1X
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1X\n")

	return (jint)((GtkWidget *)arg0)->allocation.x;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1Y
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1WIDGET_1Y\n")

	return (jint)((GtkWidget *)arg0)->allocation.y;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1SCROLLED_1WINDOW_1HSCROLLBAR
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1SCROLLED_1WINDOW_1HSCROLLBAR\n")

	return (jint)((GtkScrolledWindow *)arg0)->hscrollbar;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING\n")

#define DEFAULT_SCROLLBAR_SPACING  3

	return (GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing >= 0 ?
		GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing : DEFAULT_SCROLLBAR_SPACING);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1SCROLLED_1WINDOW_1VSCROLLBAR
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GTK_1SCROLLED_1WINDOW_1VSCROLLBAR\n")

	return (jint)((GtkScrolledWindow *)arg0)->vscrollbar;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1PIXELS
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("PANGO_1PIXELS\n")

	return (jint)PANGO_PIXELS(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1filename_1to_1utf8
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("g_1filename_1to_1utf8\n")

	return (jint)g_filename_to_utf8((const gchar *)arg0, (gssize)arg1, (gsize *)arg2, (gsize *)arg3, (GError **)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1free\n")

	g_free((gpointer)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1append
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1append\n")

	return (jint)g_list_append((GList *)arg0, (gpointer)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1free\n")

	g_list_free((GList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1free_11
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1free_11\n")

	g_list_free_1((GList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1length
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1length\n")

	return (jint)g_list_length((GList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1nth
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1nth\n")

	return (jint)g_list_nth((GList *)arg0, (guint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1nth_1data
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1nth_1data\n")

	return (jint)g_list_nth_data((GList *)arg0, (guint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1prepend
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1prepend\n")

	return (jint)g_list_prepend((GList *)arg0, (gpointer)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1next__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1next__I\n")

	return (jint)g_list_next((GList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1previous__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1previous__I\n")

	return (jint)g_list_previous((GList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1remove_1link
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1remove_1link\n")

	return (jint)g_list_remove_link((GList *)arg0, (GList *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1reverse
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1reverse\n")

	return (jint)g_list_reverse((GList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1next__II
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1next__II\n")

	((GList *)arg0)->next = (GList *)arg1;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1previous__II
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1list_1previous__II\n")

	((GList *)arg0)->prev = (GList *)arg1;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1default_1handler
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("g_1log_1default_1handler\n")

	g_log_default_handler((gchar *)arg0, (GLogLevelFlags)arg1, (gchar *)arg2, (gpointer)arg3);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1remove_1handler
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("g_1log_1remove_1handler\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	g_log_remove_handler((gchar *)lparg0, (gint)arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1set_1handler
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("g_1log_1set_1handler\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)g_log_set_handler((gchar *)lparg0, (GLogLevelFlags)arg1, (GLogFunc)arg2, (gpointer)arg3);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1malloc
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1malloc\n")

	return (jint)g_malloc((gulong)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1object_1get_1qdata
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("g_1object_1get_1qdata\n")

	return (jint)g_object_get_qdata((GObject *)arg0, (GQuark)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1object_1set_1qdata
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("g_1object_1set_1qdata\n")

	g_object_set_qdata((GObject *)arg0, (GQuark)arg1, (gpointer)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1object_1ref
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1object_1ref\n")

	return (jint)g_object_ref((gpointer)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1object_1unref
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1object_1unref\n")

	g_object_unref((gpointer)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1quark_1from_1string
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("g_1quark_1from_1string\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)g_quark_from_string((gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1connect
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("g_1signal_1connect\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	g_signal_connect((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1connect_1after
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("g_1signal_1connect_1after\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	g_signal_connect_after((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1handlers_1block_1matched
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("g_1signal_1handlers_1block_1matched\n")

	return (jint)g_signal_handlers_block_matched((gpointer)arg0, (GSignalMatchType)arg1, arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1handlers_1disconnect_1matched
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("g_1signal_1handlers_1disconnect_1matched\n")

	return (jint)g_signal_handlers_disconnect_matched((gpointer)arg0, (GSignalMatchType)arg1, arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1handlers_1unblock_1matched
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("g_1signal_1handlers_1unblock_1matched\n")

	return (jint)g_signal_handlers_unblock_matched((gpointer)arg0, (GSignalMatchType)arg1, arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1stop_1emission_1by_1name
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("g_1signal_1stop_1emission_1by_1name\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	g_signal_stop_emission_by_name((gpointer)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1strfreev
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1strfreev\n")

	g_strfreev((gchar **)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1utf16_1to_1utf8
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jchar *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("g_1utf16_1to_1utf8\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)g_utf16_to_utf8((const gunichar2 *)lparg0, (glong)arg1, (glong *)lparg2, (glong *)lparg3, (GError **)lparg4);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1utf8_1to_1utf16
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("g_1utf8_1to_1utf16\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)g_utf8_to_utf16((const gchar *)lparg0, (glong)arg1, (glong *)lparg2, (glong *)lparg3, (GError **)lparg4);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1atom_1intern
	(JNIEnv *env, jclass that, jbyteArray arg0, jboolean arg1)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gdk_1atom_1intern\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gdk_atom_intern((const gchar *)lparg0, (gboolean)arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1atom_1name
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1atom_1name\n")
	return (jint)gdk_atom_name((GdkAtom)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1beep
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1beep\n")

	gdk_beep();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1bitmap_1create_1from_1data
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gdk_1bitmap_1create_1from_1data\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)gdk_bitmap_create_from_data((GdkWindow *)arg0, (const gchar *)lparg1, (gint)arg2, (gint)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1white
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gdk_1color_1white\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	rc = (jboolean)gdk_color_white((GdkColormap *)arg0, (GdkColor *)lparg1);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1alloc_1color
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2, jboolean arg3)
{
	GdkColor _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gdk_1colormap_1alloc_1color\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	rc = (jboolean)gdk_colormap_alloc_color((GdkColormap *)arg0, (GdkColor *)lparg1, (gboolean)arg2, (gboolean)arg3);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1free_1colors
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GdkColor _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1colormap_1free_1colors\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	gdk_colormap_free_colors((GdkColormap *)arg0, (GdkColor *)lparg1, (gint)arg2);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1get_1system
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1colormap_1get_1system\n")

	return (jint)gdk_colormap_get_system();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1query_1color
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gdk_1colormap_1query_1color\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gdk_colormap_query_color((GdkColormap *)arg0, (gulong)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1cursor_1destroy\n")

	gdk_cursor_destroy((GdkCursor *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1cursor_1new\n")

	return (jint)gdk_cursor_new((GdkCursorType)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new_1from_1pixmap
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	GdkColor _arg2, *lparg2=NULL;
	GdkColor _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("gdk_1cursor_1new_1from_1pixmap\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getGdkColorFields(env, arg3, &_arg3);
	rc = (jint)gdk_cursor_new_from_pixmap((GdkPixmap *)arg0, (GdkPixmap *)arg1, (GdkColor *)lparg2, (GdkColor *)lparg3, (gint)arg4, (gint)arg5);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
	if (arg3) setGdkColorFields(env, arg3, lparg3);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drag_1status
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gdk_1drag_1status\n")

	gdk_drag_status((GdkDragContext *)arg0, (GdkDragAction)arg1, (guint32)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1arc
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("gdk_1draw_1arc\n")

	gdk_draw_arc((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1drawable
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("gdk_1draw_1drawable\n")

	gdk_draw_drawable((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkDrawable *)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1layout
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gdk_1draw_1layout\n")

	gdk_draw_layout((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (PangoLayout *)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1line
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("gdk_1draw_1line\n")

	gdk_draw_line((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1lines
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;

	DEBUG_CALL("gdk_1draw_1lines\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gdk_draw_lines((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkPoint *)lparg2, (gint)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1polygon
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;

	DEBUG_CALL("gdk_1draw_1polygon\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	gdk_draw_polygon((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (GdkPoint *)lparg3, (gint)arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1rectangle
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("gdk_1draw_1rectangle\n")

	gdk_draw_rectangle((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drawable_1get_1image
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gdk_1drawable_1get_1image\n")

	return (jint)gdk_drawable_get_image((GdkDrawable *)arg0, (gint)arg1, (gint)arg2, (gint)arg3, (gint)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drawable_1get_1size
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gdk_1drawable_1get_1size\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gdk_drawable_get_size((GdkDrawable *)arg0, (gint *)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drawable_1get_1visible_1region
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1drawable_1get_1visible_1region\n")

	return (jint)gdk_drawable_get_visible_region((GdkDrawable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1event_1free\n")

	gdk_event_free((GdkEvent *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1event_1get\n")

	return (jint)gdk_event_get();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1coords
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("gdk_1event_1get_1coords\n")

	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	rc = (jboolean)gdk_event_get_coords((GdkEvent *)arg0, (gdouble *)lparg1, (gdouble *)lparg2);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1state
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gdk_1event_1get_1state\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)gdk_event_get_state((GdkEvent *)arg0, (GdkModifierType *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1time
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1event_1get_1time\n")

	return (jint)gdk_event_get_time((GdkEvent *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1flush
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1flush\n")

	gdk_flush();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1get_1values
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkGCValues _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1gc_1get_1values\n")

	if (arg1) lparg1 = getGdkGCValuesFields(env, arg1, &_arg1);
	gdk_gc_get_values((GdkGC *)arg0, (GdkGCValues *)lparg1);
	if (arg1) setGdkGCValuesFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gc_1new\n")

	return (jint)gdk_gc_new((GdkDrawable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1background
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1gc_1set_1background\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	gdk_gc_set_background((GdkGC *)arg0, (GdkColor *)lparg1);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1mask
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1clip_1mask\n")

	gdk_gc_set_clip_mask((GdkGC *)arg0, (GdkBitmap *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1origin
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gdk_1gc_1set_1clip_1origin\n")

	gdk_gc_set_clip_origin((GdkGC *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1rectangle
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1gc_1set_1clip_1rectangle\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gdk_gc_set_clip_rectangle((GdkGC *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1region
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1clip_1region\n")

	gdk_gc_set_clip_region((GdkGC *)arg0, (GdkRegion *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1dashes
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;

	DEBUG_CALL("gdk_1gc_1set_1dashes\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gdk_gc_set_dashes((GdkGC *)arg0, (gint)arg1, (gint8 *)lparg2, (gint)arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1exposures
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1exposures\n")

	gdk_gc_set_exposures((GdkGC *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1fill
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1fill\n")

	gdk_gc_set_fill((GdkGC *)arg0, (GdkFill)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1foreground
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1gc_1set_1foreground\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	gdk_gc_set_foreground((GdkGC *)arg0, (GdkColor *)lparg1);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1function
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1function\n")

	gdk_gc_set_function((GdkGC *)arg0, (GdkFunction)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1line_1attributes
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gdk_1gc_1set_1line_1attributes\n")

	gdk_gc_set_line_attributes((GdkGC *)arg0, (gint)arg1, (GdkLineStyle)arg2, (GdkCapStyle)arg3, (GdkJoinStyle)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1stipple
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1stipple\n")

	gdk_gc_set_stipple((GdkGC *)arg0, (GdkPixmap *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1subwindow
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gc_1set_1subwindow\n")

	gdk_gc_set_subwindow((GdkGC *)arg0, (GdkSubwindowMode)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1values
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GdkGCValues _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1gc_1set_1values\n")

	if (arg1) lparg1 = getGdkGCValuesFields(env, arg1, &_arg1);
	gdk_gc_set_values((GdkGC *)arg0, (GdkGCValues *)lparg1, (GdkGCValuesMask)arg2);
	if (arg1) setGdkGCValuesFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gdk_1image_1get\n")

	return (jint)gdk_image_get((GdkDrawable *)arg0, (gint)arg1, (gint)arg2, (gint)arg3, (gint)arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get_1pixel
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gdk_1image_1get_1pixel\n")

	return (jint)gdk_image_get_pixel((GdkImage *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1keyboard_1ungrab
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1keyboard_1ungrab\n")

	gdk_keyboard_ungrab(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1keyval_1to_1unicode
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1keyval_1to_1unicode\n")

	return (jint)gdk_keyval_to_unicode(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pango_1context_1get
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1pango_1context_1get\n")

	return (jint)gdk_pango_context_get();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1get_1from_1drawable
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("gdk_1pixbuf_1get_1from_1drawable\n")

	return (jint)gdk_pixbuf_get_from_drawable((GdkPixbuf *)arg0, (GdkDrawable *)arg1, (GdkColormap *)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1get_1pixels
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1pixbuf_1get_1pixels\n")

	return (jint)gdk_pixbuf_get_pixels((const GdkPixbuf *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1get_1rowstride
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1pixbuf_1get_1rowstride\n")

	return (jint)gdk_pixbuf_get_rowstride((const GdkPixbuf *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1new
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gdk_1pixbuf_1new\n")

	return (jint)gdk_pixbuf_new((GdkColorspace)arg0, (gboolean)arg1, arg2, arg3, arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1render_1to_1drawable
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	DEBUG_CALL("gdk_1pixbuf_1render_1to_1drawable\n")

	gdk_pixbuf_render_to_drawable((GdkPixbuf *)arg0, (GdkDrawable *)arg1, (GdkGC *)arg2, arg3, arg4, arg5, arg6, arg7, arg8, (GdkRgbDither)arg9, arg10, arg11);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1render_1to_1drawable_1alpha
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	DEBUG_CALL("gdk_1pixbuf_1render_1to_1drawable_1alpha\n")

	gdk_pixbuf_render_to_drawable_alpha((GdkPixbuf *)arg0, (GdkDrawable *)arg1, arg2, arg3, arg4, arg5, arg6, arg7, (GdkPixbufAlphaMode)arg8, arg9, (GdkRgbDither)arg10, arg11, arg12);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1scale
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jdouble arg6, jdouble arg7, jdouble arg8, jdouble arg9, jint arg10)
{
	DEBUG_CALL("gdk_1pixbuf_1scale\n")

	gdk_pixbuf_scale((const GdkPixbuf *)arg0, (GdkPixbuf *)arg1, arg2, arg3, arg4, arg5, (double)arg6, (double)arg7, (double)arg8, (double)arg9, arg10);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixbuf_1scale_1simple
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gdk_1pixbuf_1scale_1simple\n")

	return (jint)gdk_pixbuf_scale_simple((const GdkPixbuf *)arg0, arg1, arg2, (GdkInterpType)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixmap_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gdk_1pixmap_1new\n")

	return (jint)gdk_pixmap_new((GdkWindow *)arg0, (gint)arg1, (gint)arg2, (gint)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pointer_1grab
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("gdk_1pointer_1grab\n")

	return (jint)gdk_pointer_grab((GdkWindow *)arg0, (gboolean)arg1, (GdkEventMask)arg2, (GdkWindow *)arg3, (GdkCursor *)arg4, (guint32)arg5);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pointer_1is_1grabbed
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1pointer_1is_1grabbed\n")

	return (jboolean)gdk_pointer_is_grabbed();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pointer_1ungrab
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1pointer_1ungrab\n")

	gdk_pointer_ungrab((guint32)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1region_1destroy\n")

	gdk_region_destroy((GdkRegion *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1empty
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1region_1empty\n")

	return (jboolean)gdk_region_empty((GdkRegion *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1get_1clipbox
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1region_1get_1clipbox\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gdk_region_get_clipbox((GdkRegion *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1get_1rectangles
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gdk_1region_1get_1rectangles\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gdk_region_get_rectangles((GdkRegion *)arg0, (GdkRectangle **)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1intersect
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1region_1intersect\n")

	gdk_region_intersect((GdkRegion *)arg0, (GdkRegion *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1region_1new\n")

	return (jint)gdk_region_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1offset
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gdk_1region_1offset\n")

	gdk_region_offset((GdkRegion *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1point_1in
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gdk_1region_1point_1in\n")

	return (jboolean)gdk_region_point_in((GdkRegion *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1rect_1in
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gdk_1region_1rect_1in\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	rc = (jint)gdk_region_rect_in((GdkRegion *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1subtract
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1region_1subtract\n")

	gdk_region_subtract((GdkRegion *)arg0, (GdkRegion *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1union
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1region_1union\n")

	gdk_region_union((GdkRegion *)arg0, (GdkRegion *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1union_1with_1rect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1region_1union_1with_1rect\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gdk_region_union_with_rect((GdkRegion *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1rgb_1init
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1rgb_1init\n")

	gdk_rgb_init();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1height
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1screen_1height\n")

	return (jint)gdk_screen_height();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1screen_1width\n")

	return (jint)gdk_screen_width();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width_1mm
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1screen_1width_1mm\n")

	return (jint)gdk_screen_width_mm();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1visual_1get_1system
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1visual_1get_1system\n")

	return (jint)gdk_visual_get_system();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1at_1pointer
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gdk_1window_1at_1pointer\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)gdk_window_at_pointer((gint *)lparg0, (gint *)lparg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1frame_1extents
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1window_1get_1frame_1extents\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gdk_window_get_frame_extents((GdkWindow *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1origin
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("gdk_1window_1get_1origin\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)gdk_window_get_origin((GdkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1pointer
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("gdk_1window_1get_1pointer\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)gdk_window_get_pointer((GdkWindow *)arg0, (gint *)lparg1, (gint *)lparg2, (GdkModifierType *)lparg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1user_1data
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	DEBUG_CALL("gdk_1window_1get_1user_1data\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	gdk_window_get_user_data((GdkWindow *)arg0, (gpointer *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1invalidate_1rect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gdk_1window_1invalidate_1rect\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gdk_window_invalidate_rect((GdkWindow *)arg0, (GdkRectangle *)lparg1, (gboolean)arg2);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1invalidate_1region
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("gdk_1window_1invalidate_1region\n")

	gdk_window_invalidate_region((GdkWindow *)arg0, (GdkRegion *)arg1, (gboolean)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1lower
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1window_1lower\n")

	gdk_window_lower((GdkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1process_1updates
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gdk_1window_1process_1updates\n")

	gdk_window_process_updates((GdkWindow *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1raise
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1window_1raise\n")

	gdk_window_raise((GdkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1back_1pixmap
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("gdk_1window_1set_1back_1pixmap\n")

	gdk_window_set_back_pixmap((GdkWindow *)arg0, (GdkPixmap *)arg1, arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1cursor
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1window_1set_1cursor\n")

	gdk_window_set_cursor((GdkWindow *)arg0, (GdkCursor *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1decorations
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1window_1set_1decorations\n")

	gdk_window_set_decorations((GdkWindow *)arg0, (GdkWMDecoration)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1icon
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gdk_1window_1set_1icon\n")

	gdk_window_set_icon((GdkWindow *)arg0, (GdkWindow *)arg1, (GdkPixmap *)arg2, (GdkBitmap *)arg3);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1override_1redirect
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gdk_1window_1set_1override_1redirect\n")

	gdk_window_set_override_redirect((GdkWindow *)arg0, (gboolean)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1group_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1accel_1group_1new\n")

	return (jint)gtk_accel_group_new();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1groups_1activate
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1accel_1groups_1activate\n")

	return (jboolean)gtk_accel_groups_activate((GObject *)arg0, (guint)arg1, (GdkModifierType)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1label_1set_1accel_1widget
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1accel_1label_1set_1accel_1widget\n")

	gtk_accel_label_set_accel_widget((GtkAccelLabel *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1changed
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1adjustment_1changed\n")

	gtk_adjustment_changed((GtkAdjustment *)arg0);
}

JNIEXPORT jdouble JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1get_1value
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1adjustment_1get_1value\n")

	return (jdouble)gtk_adjustment_get_value((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1new
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	DEBUG_CALL("gtk_1adjustment_1new\n")

	return (jint)gtk_adjustment_new((gdouble)arg0, (gdouble)arg1, (gdouble)arg2, (gdouble)arg3, (gdouble)arg4, arg5);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1set_1value
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	DEBUG_CALL("gtk_1adjustment_1set_1value\n")

	gtk_adjustment_set_value((GtkAdjustment *)arg0, (gdouble)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1value_1changed
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1adjustment_1value_1changed\n")

	gtk_adjustment_value_changed((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1arrow_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1arrow_1new\n")

	return (jint)gtk_arrow_new((GtkArrowType)arg0, (GtkArrowType)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1arrow_1set
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1arrow_1set\n")

	gtk_arrow_set((GtkArrow *)arg0, (GtkArrowType)arg1, (GtkArrowType)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1bin_1get_1child
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1bin_1get_1child\n")

	return (jint)gtk_bin_get_child((GtkBin *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1end
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3, jint arg4)
{
	DEBUG_CALL("gtk_1box_1pack_1end\n")

	gtk_box_pack_end((GtkBox *)arg0, (GtkWidget *)arg1, (gboolean)arg2, (gboolean)arg3, (guint)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1start
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3, jint arg4)
{
	DEBUG_CALL("gtk_1box_1pack_1start\n")

	gtk_box_pack_start((GtkBox *)arg0, (GtkWidget *)arg1, (gboolean)arg2, (gboolean)arg3, (guint)arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1button_1new\n")

	return (jint)gtk_button_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1new_1with_1label
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1button_1new_1with_1label\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_button_new_with_label((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1set_1relief
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1button_1set_1relief\n")

	gtk_button_set_relief((GtkButton *)arg0, (GtkReliefStyle)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1button_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1check_1button_1new\n")

	return (jint)gtk_check_button_new();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1get_1active
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1check_1menu_1item_1get_1active\n")

	return (jboolean)gtk_check_menu_item_get_active((GtkCheckMenuItem *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1new_1with_1label
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1check_1menu_1item_1new_1with_1label\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_check_menu_item_new_with_label((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1set_1active
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1check_1menu_1item_1set_1active\n")

	gtk_check_menu_item_set_active((GtkCheckMenuItem *)arg0, (gboolean)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1version
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1check_1version\n")

	return (jint)gtk_check_version(arg0, arg1, arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clipboard_1clear
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clipboard_1clear\n")

	gtk_clipboard_clear((GtkClipboard *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clipboard_1get
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clipboard_1get\n")

	return (jint)gtk_clipboard_get((GdkAtom)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clipboard_1set_1with_1data
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("gtk_1clipboard_1set_1with_1data\n")

	return (jboolean)gtk_clipboard_set_with_data((GtkClipboard *)arg0, (const GtkTargetEntry *)arg1, (guint)arg2, (GtkClipboardGetFunc)arg3, (GtkClipboardClearFunc)arg4, (GObject *)arg5);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clipboard_1wait_1for_1contents
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clipboard_1wait_1for_1contents\n")

	return (jint)gtk_clipboard_wait_for_contents((GtkClipboard *)arg0, (GdkAtom)arg1);
}

#ifndef NO_CLIST
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1append
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gtk_1clist_1append\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)gtk_clist_append((GtkCList *)arg0, (gchar **)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1clear
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1clear\n")

	gtk_clist_clear((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1hide
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1column_1titles_1hide\n")

	gtk_clist_column_titles_hide((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1passive
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1column_1titles_1passive\n")

	gtk_clist_column_titles_passive((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1show
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1column_1titles_1show\n")

	gtk_clist_column_titles_show((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1freeze
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1freeze\n")

	gtk_clist_freeze((GtkCList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1column_1widget
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1get_1column_1widget\n")

	return (jint)gtk_clist_get_column_widget((GtkCList *)arg0, arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1pixtext
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jbyteArray arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;

	DEBUG_CALL("gtk_1clist_1get_1pixtext\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	gtk_clist_get_pixtext((GtkCList *)arg0, (gint)arg1, (gint)arg2, (gchar **)lparg3, (guint8 *)lparg4, (GdkPixmap **)lparg5, (GdkBitmap **)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1selection_1info
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("gtk_1clist_1get_1selection_1info\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)gtk_clist_get_selection_info((GtkCList *)arg0, (gint)arg1, (gint)arg2, (gint *)lparg3, (gint *)lparg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1text
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("gtk_1clist_1get_1text\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)gtk_clist_get_text((GtkCList *)arg0, (gint)arg1, (gint)arg2, (gchar **)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1insert
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("gtk_1clist_1insert\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)gtk_clist_insert((GtkCList *)arg0, (gint)arg1, (gchar **)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1moveto
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jfloat arg4)
{
	DEBUG_CALL("gtk_1clist_1moveto\n")

	gtk_clist_moveto((GtkCList *)arg0, (gint)arg1, (gint)arg2, (gfloat)arg3, (gfloat)arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1new\n")

	return (jint)gtk_clist_new((gint)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1optimal_1column_1width
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1optimal_1column_1width\n")

	return (jint)gtk_clist_optimal_column_width((GtkCList*)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1remove
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1remove\n")

	gtk_clist_remove((GtkCList *)arg0, (gint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1row_1is_1visible
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1row_1is_1visible\n")

	return (jint)gtk_clist_row_is_visible((GtkCList *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1all
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1select_1all\n")

	gtk_clist_select_all((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1row
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1clist_1select_1row\n")

	gtk_clist_select_row((GtkCList *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1background
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1clist_1set_1background\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_clist_set_background((GtkCList *)arg0, (gint)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1justification
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1clist_1set_1column_1justification\n")

	gtk_clist_set_column_justification((GtkCList *)arg0, (gint)arg1, (GtkJustification)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1resizeable
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("gtk_1clist_1set_1column_1resizeable\n")

	gtk_clist_set_column_resizeable((GtkCList *)arg0, (gint)arg1, (gboolean)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1title
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1clist_1set_1column_1title\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_clist_set_column_title((GtkCList *)arg0, (gint)arg1, (const gchar *)lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1visibility
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("gtk_1clist_1set_1column_1visibility\n")

	gtk_clist_set_column_visibility((GtkCList *)arg0, (gint)arg1, (gboolean)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1width
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1clist_1set_1column_1width\n")

	gtk_clist_set_column_width((GtkCList *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1foreground
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1clist_1set_1foreground\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_clist_set_foreground((GtkCList *)arg0, (gint)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1pixtext
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyte arg4, jint arg5, jint arg6)
{
	jbyte *lparg3=NULL;

	DEBUG_CALL("gtk_1clist_1set_1pixtext\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	gtk_clist_set_pixtext((GtkCList *)arg0, (gint)arg1, (gint)arg2, (const gchar *)lparg3, (guint8)arg4, (GdkPixmap *)arg5, (GdkBitmap *)arg6);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1row_1height
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1set_1row_1height\n")

	gtk_clist_set_row_height((GtkCList *)arg0, (guint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1selection_1mode
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1set_1selection_1mode\n")

	gtk_clist_set_selection_mode((GtkCList *)arg0, (GtkSelectionMode)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1shadow_1type
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1clist_1set_1shadow_1type\n")

	gtk_clist_set_shadow_type((GtkCList *)arg0, (GtkShadowType)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1text
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;

	DEBUG_CALL("gtk_1clist_1set_1text\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	gtk_clist_set_text((GtkCList *)arg0, (gint)arg1, (gint)arg2, (const gchar *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1thaw
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1thaw\n")

	gtk_clist_thaw((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1all
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1clist_1unselect_1all\n")

	gtk_clist_unselect_all((GtkCList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1row
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1clist_1unselect_1row\n")

	gtk_clist_unselect_row((GtkCList *)arg0, (gint)arg1, (gint)arg2);
}
#endif /* NO_CLIST */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1dialog_1new
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1color_1selection_1dialog_1new\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_color_selection_dialog_new((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1get_1current_1color
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1color_1selection_1get_1current_1color\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	gtk_color_selection_get_current_color((GtkColorSelection *)arg0, (GdkColor *)lparg1);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1set_1current_1color
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1color_1selection_1set_1current_1color\n")

	if (arg1) lparg1 = getGdkColorFields(env, arg1, &_arg1);
	gtk_color_selection_set_current_color((GtkColorSelection *)arg0, (GdkColor *)lparg1);
	if (arg1) setGdkColorFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1disable_1activate
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1combo_1disable_1activate\n")

	gtk_combo_disable_activate((GtkCombo *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1combo_1new\n")

	return (jint)gtk_combo_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1set_1case_1sensitive
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1combo_1set_1case_1sensitive\n")

	gtk_combo_set_case_sensitive((GtkCombo *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1set_1popdown_1strings
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1combo_1set_1popdown_1strings\n")

	gtk_combo_set_popdown_strings((GtkCombo *)arg0, (GList *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1add
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1container_1add\n")

	gtk_container_add((GtkContainer *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1get_1border_1width
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1container_1get_1border_1width\n")

	return (jint)gtk_container_get_border_width((GtkContainer *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1get_1children
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1container_1get_1children\n")

	return (jint)gtk_container_get_children((GtkContainer *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1remove
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1container_1remove\n")

	gtk_container_remove((GtkContainer *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1resize_1children
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1container_1resize_1children\n")

	gtk_container_resize_children((GtkContainer *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1set_1border_1width
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1container_1set_1border_1width\n")

	gtk_container_set_border_width((GtkContainer *)arg0, (guint)arg1);
}

#ifndef NO_CLIST
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1collapse
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1collapse\n")

	gtk_ctree_collapse((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1expand
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1expand\n")

	gtk_ctree_expand((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1get_1node_1info
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jbyteArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jbooleanArray arg8, jbooleanArray arg9)
{
	jint *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jboolean *lparg8=NULL;
	jboolean *lparg9=NULL;
	jint rc;

	DEBUG_CALL("gtk_1ctree_1get_1node_1info\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetBooleanArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetBooleanArrayElements(env, arg9, NULL);
	rc = (jint)gtk_ctree_get_node_info((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (gchar **)lparg2, (guint8 *)lparg3, (GdkPixmap **)lparg4, (GdkBitmap **)lparg5, (GdkPixmap **)lparg6, (GdkBitmap **)lparg7, (gboolean *)lparg8, (gboolean *)lparg9);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseBooleanArrayElements(env, arg8, lparg8, 0);
	if (arg9) (*env)->ReleaseBooleanArrayElements(env, arg9, lparg9, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1insert_1node
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jbyte arg4, jint arg5, jint arg6, jint arg7, jint arg8, jboolean arg9, jboolean arg10)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("gtk_1ctree_1insert_1node\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)gtk_ctree_insert_node((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (GtkCTreeNode *)arg2, (gchar **)lparg3, (guint8)arg4, (GdkPixmap *)arg5, (GdkBitmap *)arg6, (GdkPixmap *)arg7, (GdkBitmap *)arg8, (gboolean)arg9, (gboolean)arg10);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1hot_1spot
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1ctree_1is_1hot_1spot\n")

	return (jboolean)gtk_ctree_is_hot_spot((GtkCTree *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1viewable
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1is_1viewable\n")

	return (jboolean)gtk_ctree_is_viewable((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1new\n")

	return (jint)gtk_ctree_new((gint)arg0, (gint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1data
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1node_1get_1row_1data\n")

	return (jint)gtk_ctree_node_get_row_data((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1style
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1node_1get_1row_1style\n")

	return (jint)gtk_ctree_node_get_row_style((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1is_1visible
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1node_1is_1visible\n")

	return (jint)gtk_ctree_node_is_visible((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1moveto
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jfloat arg4)
{
	DEBUG_CALL("gtk_1ctree_1node_1moveto\n")

	gtk_ctree_node_moveto((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (guint)arg2, (gfloat)arg3, (gfloat)arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1nth
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1node_1nth\n")

	return (jint)gtk_ctree_node_nth((GtkCTree *)arg0, (guint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1set_1background
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1ctree_1node_1set_1background\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_ctree_node_set_background((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1set_1foreground
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1ctree_1node_1set_1foreground\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_ctree_node_set_foreground((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1set_1row_1data
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1ctree_1node_1set_1row_1data\n")

	gtk_ctree_node_set_row_data((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (gpointer)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1post_1recursive
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gtk_1ctree_1post_1recursive\n")

	gtk_ctree_post_recursive((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (GtkCTreeFunc)arg2, (gpointer)arg3);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1post_1recursive_1to_1depth
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gtk_1ctree_1post_1recursive_1to_1depth\n")

	gtk_ctree_post_recursive_to_depth((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (gint)arg2, (GtkCTreeFunc)arg3, (gpointer)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1remove_1node
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1remove_1node\n")

	gtk_ctree_remove_node((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1select\n")

	gtk_ctree_select((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select_1recursive
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1select_1recursive\n")

	gtk_ctree_select_recursive((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1set_1node_1info
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyte arg3, jint arg4, jint arg5, jint arg6, jint arg7, jboolean arg8, jboolean arg9)
{
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1ctree_1set_1node_1info\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_ctree_set_node_info((GtkCTree *)arg0, (GtkCTreeNode *)arg1, (const gchar *)lparg2, (guint8)arg3, (GdkPixmap *)arg4, (GdkBitmap *)arg5, (GdkPixmap *)arg6, (GdkBitmap *)arg7, (gboolean)arg8, (gboolean)arg9);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1unselect_1recursive
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1ctree_1unselect_1recursive\n")

	gtk_ctree_unselect_recursive((GtkCTree *)arg0, (GtkCTreeNode *)arg1);
}
#endif /* NO_CLIST */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1dialog_1add_1button
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	const jbyte *lparg1= NULL;
	jint rc;

	DEBUG_CALL("gtk_1dialog_1add_1button\n")

	if (arg1) lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL);
	rc = (jint)gtk_dialog_add_button((GtkDialog *)arg0, (const gchar *)lparg1, (gint)arg2);
	if (arg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1dialog_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1dialog_1new\n")

	return (jint)gtk_dialog_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1dialog_1run
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1dialog_1run\n")

	return (jint)gtk_dialog_run((GtkDialog *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1begin
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gtk_1drag_1begin\n")

	return (jint)gtk_drag_begin((GtkWidget *)arg0, (GtkTargetList *)arg1, (GdkDragAction)arg2, arg3, (GdkEvent *)arg4);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1check_1threshold
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gtk_1drag_1check_1threshold\n")

	return (jboolean)gtk_drag_check_threshold((GtkWidget *)arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1dest_1find_1target
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1drag_1dest_1find_1target\n")

	return (jint)gtk_drag_dest_find_target((GtkWidget *)arg0, (GdkDragContext *)arg1, (GtkTargetList *)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1dest_1set
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gtk_1drag_1dest_1set\n")

	gtk_drag_dest_set((GtkWidget *)arg0, (GtkDestDefaults)arg1, (GtkTargetEntry *)arg2, arg3, (GdkDragAction)arg4);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1dest_1unset
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1drag_1dest_1unset\n")

	gtk_drag_dest_unset((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1finish
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2, jint arg3)
{
	DEBUG_CALL("gtk_1drag_1finish\n")

	gtk_drag_finish((GdkDragContext *)arg0, (gboolean)arg1, (gboolean)arg2, (guint32)arg3);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drag_1get_1data
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gtk_1drag_1get_1data\n")

	gtk_drag_get_data((GtkWidget *)arg0, (GdkDragContext *)arg1, (GdkAtom)arg2, (guint32)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drawing_1area_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1drawing_1area_1new\n")

	return (jint)gtk_drawing_area_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1copy_1clipboard
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1copy_1clipboard\n")

	gtk_editable_copy_clipboard((GtkEditable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1cut_1clipboard
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1cut_1clipboard\n")

	gtk_editable_cut_clipboard((GtkEditable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1delete_1selection
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1delete_1selection\n")

	gtk_editable_delete_selection((GtkEditable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1delete_1text
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1editable_1delete_1text\n")

	gtk_editable_delete_text((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1chars
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1editable_1get_1chars\n")

	return (jint)gtk_editable_get_chars((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1editable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1get_1editable\n")

	return (jboolean)gtk_editable_get_editable((GtkEditable *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1position
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1get_1position\n")

	return (jint)gtk_editable_get_position((GtkEditable *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1selection_1bounds
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("gtk_1editable_1get_1selection_1bounds\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)gtk_editable_get_selection_bounds((GtkEditable *)arg0, (gint *)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1insert_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;

	DEBUG_CALL("gtk_1editable_1insert_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	gtk_editable_insert_text((GtkEditable *)arg0, (gchar *)lparg1, (gint)arg2, (gint *)lparg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1paste_1clipboard
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1editable_1paste_1clipboard\n")

	gtk_editable_paste_clipboard((GtkEditable *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1select_1region
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1editable_1select_1region\n")

	gtk_editable_select_region((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1set_1editable
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1editable_1set_1editable\n")

	gtk_editable_set_editable((GtkEditable *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1set_1position
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1editable_1set_1position\n")

	gtk_editable_set_position((GtkEditable *)arg0, (gint)arg1);
}

JNIEXPORT jchar JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1get_1invisible_1char
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1entry_1get_1invisible_1char\n")

	return (jchar)gtk_entry_get_invisible_char((GtkEntry *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1get_1max_1length
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1entry_1get_1max_1length\n")

	return (jint)gtk_entry_get_max_length((GtkEntry *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1get_1text
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1entry_1get_1text\n")

	return (jint)gtk_entry_get_text((GtkEntry *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1get_1visibility
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1entry_1get_1visibility\n")

	return (jboolean)gtk_entry_get_visibility((GtkEntry *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1entry_1new\n")

	return (jint)gtk_entry_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1activates_1default
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1entry_1set_1activates_1default\n")

	gtk_entry_set_activates_default((GtkEntry *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1has_1frame
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1entry_1set_1has_1frame\n")

	gtk_entry_set_has_frame((GtkEntry *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1invisible_1char
	(JNIEnv *env, jclass that, jint arg0, jchar arg1)
{
	DEBUG_CALL("gtk_1entry_1set_1invisible_1char\n")

	gtk_entry_set_invisible_char((GtkEntry *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1max_1length
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1entry_1set_1max_1length\n")

	gtk_entry_set_max_length((GtkEntry *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1entry_1set_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_entry_set_text((GtkEntry *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1visibility
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1entry_1set_1visibility\n")

	gtk_entry_set_visibility((GtkEntry *)arg0, (gboolean)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1event_1box_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1event_1box_1new\n")

	return (jint)gtk_event_box_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1events_1pending
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1events_1pending\n")

	return (jint)gtk_events_pending();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1complete
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1file_1selection_1complete\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_file_selection_complete((GtkFileSelection *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1get_1filename
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1file_1selection_1get_1filename\n")

	return (jint)gtk_file_selection_get_filename((GtkFileSelection *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1get_1selections
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1file_1selection_1get_1selections\n")

	return (jint)gtk_file_selection_get_selections((GtkFileSelection *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1new
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1file_1selection_1new\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_file_selection_new((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1set_1filename
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1file_1selection_1set_1filename\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_file_selection_set_filename((GtkFileSelection *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1set_1select_1multiple
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1file_1selection_1set_1select_1multiple\n")

	gtk_file_selection_set_select_multiple((GtkFileSelection *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1move
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gtk_1fixed_1move\n")

	gtk_fixed_move((GtkFixed *)arg0, (GtkWidget *)arg1, (gint)arg2, (gint)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1fixed_1new\n")

	return (jint)gtk_fixed_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1set_1has_1window
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1fixed_1set_1has_1window\n")

	gtk_fixed_set_has_window((GtkFixed *)arg0, (gboolean)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1get_1font_1name
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1font_1selection_1dialog_1get_1font_1name\n")

	return (jint)gtk_font_selection_dialog_get_font_name((GtkFontSelectionDialog *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1new
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1font_1selection_1dialog_1new\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_font_selection_dialog_new((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1set_1font_1name
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gtk_1font_1selection_1dialog_1set_1font_1name\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)gtk_font_selection_dialog_set_font_name((GtkFontSelectionDialog *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1new
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1frame_1new\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_frame_new((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1label
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1frame_1set_1label\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_frame_set_label((GtkFrame *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1label_1widget
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1frame_1set_1label_1widget\n")

	gtk_frame_set_label_widget((GtkFrame *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1shadow_1type
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1frame_1set_1shadow_1type\n")

	gtk_frame_set_shadow_type((GtkFrame *)arg0, (GtkShadowType)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1get_1current_1event
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1get_1current_1event\n")

	return (jint)gtk_get_current_event();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1get_1current_1event_1time
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1get_1current_1event_1time\n")

	return (jint)gtk_get_current_event_time();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1add
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1grab_1add\n")

	gtk_grab_add((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1get_1current
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1grab_1get_1current\n")

	return (jint)gtk_grab_get_current();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1remove
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1grab_1remove\n")

	gtk_grab_remove((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hbox_1new
	(JNIEnv *env, jclass that, jboolean arg0, jint arg1)
{
	DEBUG_CALL("gtk_1hbox_1new\n")

	return (jint)gtk_hbox_new((gboolean)arg0, (gint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hscale_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1hscale_1new\n")

	return (jint)gtk_hscale_new((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hscrollbar_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1hscrollbar_1new\n")

	return (jint)gtk_hscrollbar_new((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hseparator_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1hseparator_1new\n")

	return (jint)gtk_hseparator_new();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1context_1filter_1keypress
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1im_1context_1filter_1keypress\n")

	return (jboolean)gtk_im_context_filter_keypress((GtkIMContext *)arg0, (GdkEventKey *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1context_1focus_1in
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1im_1context_1focus_1in\n")

	gtk_im_context_focus_in((GtkIMContext *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1context_1focus_1out
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1im_1context_1focus_1out\n")

	gtk_im_context_focus_out((GtkIMContext *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1context_1set_1client_1window
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1im_1context_1set_1client_1window\n")

	gtk_im_context_set_client_window((GtkIMContext *)arg0, (GdkWindow *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1context_1set_1cursor_1location
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1im_1context_1set_1cursor_1location\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gtk_im_context_set_cursor_location((GtkIMContext *)arg0, lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1im_1multicontext_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1im_1multicontext_1new\n")

	return (jint)gtk_im_multicontext_new();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1init_1check
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gtk_1init_1check\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)gtk_init_check((int *)lparg0, (char ***)lparg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1get_1type
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1label_1get_1type\n")

	return (jint)gtk_label_get_type();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1new
	(JNIEnv *env, jclass that, jobject arg0)
{
	const jbyte *lparg0= NULL;
	jint rc;

	DEBUG_CALL("gtk_1label_1new\n")

	if (arg0) lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL);
	rc = (jint)gtk_label_new((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1new_1with_1mnemonic
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1label_1new_1with_1mnemonic\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_label_new_with_mnemonic((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1justify
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1label_1set_1justify\n")

	gtk_label_set_justify((GtkLabel *)arg0, (GtkJustification)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1line_1wrap
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1label_1set_1line_1wrap\n")

	gtk_label_set_line_wrap((GtkLabel *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1label_1set_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_label_set_text((GtkLabel *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1text_1with_1mnemonic
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1label_1set_1text_1with_1mnemonic\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_label_set_text_with_mnemonic((GtkLabel *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1main\n")

	gtk_main();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main_1iteration
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1main_1iteration\n")

	return (jint)gtk_main_iteration();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main_1quit
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1main_1quit\n")

	gtk_main_quit();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1bar_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1menu_1bar_1new\n")

	return (jint)gtk_menu_bar_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1separator_1menu_1item_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1separator_1menu_1item_1new\n")

	return (jint)gtk_separator_menu_item_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1menu_1item_1new_1with_1label
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1image_1menu_1item_1new_1with_1label\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_image_menu_item_new_with_label((const gchar *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1menu_1item_1get_1image
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1image_1menu_1item_1get_1image\n")

	return (jint)gtk_image_menu_item_get_image((GtkImageMenuItem*)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1menu_1item_1set_1image
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1image_1menu_1item_1set_1image\n")

	gtk_image_menu_item_set_image((GtkImageMenuItem*)arg0, (GtkWidget*)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1image_1new\n")

	return (jint)gtk_image_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1new_1from_1pixmap
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1image_1new_1from_1pixmap\n")

	return (jint)gtk_image_new_from_pixmap((GdkPixmap *)arg0, (GdkBitmap *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1image_1set_1from_1pixmap
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1image_1set_1from_1pixmap\n")

	gtk_image_set_from_pixmap((GtkImage *)arg0, (GdkPixmap *)arg1, (GdkBitmap *)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1remove_1submenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1menu_1item_1remove_1submenu\n")

	gtk_menu_item_remove_submenu((GtkMenuItem *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1set_1submenu
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1menu_1item_1set_1submenu\n")

	gtk_menu_item_set_submenu((GtkMenuItem *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1menu_1new\n")

	return (jint)gtk_menu_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1popdown
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1menu_1popdown\n")

	gtk_menu_popdown((GtkMenu *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1popup
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("gtk_1menu_1popup\n")

	gtk_menu_popup((GtkMenu *)arg0, (GtkWidget *)arg1, (GtkWidget *)arg2, (GtkMenuPositionFunc)arg3, (gpointer)arg4, (guint)arg5, (guint32)arg6);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1shell_1deactivate
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1menu_1shell_1deactivate\n")

	gtk_menu_shell_deactivate((GtkMenuShell *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1shell_1insert
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1menu_1shell_1insert\n")

	gtk_menu_shell_insert((GtkMenuShell *)arg0, (GtkWidget *)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1shell_1select_1item
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1menu_1shell_1select_1item\n")

	gtk_menu_shell_select_item((GtkMenuShell *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1message_1dialog_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	const jbyte *lparg4= NULL;
	jint rc;

	DEBUG_CALL("gtk_1message_1dialog_1new\n")

	if (arg4) lparg4 = (*env)->GetStringUTFChars(env, arg4, NULL);
	rc = (jint)gtk_message_dialog_new((GtkWindow *)arg0, (GtkDialogFlags)arg1, (GtkMessageType)arg2, (GtkButtonsType)arg3, (const gchar *)lparg4);
	if (arg4) (*env)->ReleaseStringUTFChars(env, arg4, lparg4);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1misc_1set_1alignment
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("gtk_1misc_1set_1alignment\n")

	gtk_misc_set_alignment((GtkMisc *)arg0, (gfloat)arg1, (gfloat)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1get_1current_1page
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1notebook_1get_1current_1page\n")

	return (jint)gtk_notebook_get_current_page((GtkNotebook *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1get_1scrollable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1notebook_1get_1scrollable\n")

	return (jboolean)gtk_notebook_get_scrollable((GtkNotebook *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1insert_1page
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gtk_1notebook_1insert_1page\n")

	gtk_notebook_insert_page((GtkNotebook *)arg0, (GtkWidget *)arg1, (GtkWidget *)arg2, (gint)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1notebook_1new\n")

	return (jint)gtk_notebook_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1remove_1page
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1notebook_1remove_1page\n")

	gtk_notebook_remove_page((GtkNotebook *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1set_1current_1page
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1notebook_1set_1current_1page\n")

	gtk_notebook_set_current_page((GtkNotebook *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1set_1scrollable
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1notebook_1set_1scrollable\n")

	gtk_notebook_set_scrollable((GtkNotebook *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1set_1show_1tabs
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1notebook_1set_1show_1tabs\n")

	gtk_notebook_set_show_tabs((GtkNotebook *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1sink
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1object_1sink\n")

	gtk_object_sink((GtkObject *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1progress_1bar_1new\n")

	return (jint)gtk_progress_bar_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1set_1fraction
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	DEBUG_CALL("gtk_1progress_1bar_1set_1fraction\n")

	gtk_progress_bar_set_fraction((GtkProgressBar *)arg0, (gdouble)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1set_1bar_1style
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1progress_1bar_1set_1bar_1style\n")

	gtk_progress_bar_set_bar_style((GtkProgressBar *)arg0, (GtkProgressBarStyle)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1set_1orientation
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1progress_1bar_1set_1orientation\n")

	gtk_progress_bar_set_orientation((GtkProgressBar *)arg0, (GtkProgressBarOrientation)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1button_1get_1group
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1radio_1button_1get_1group\n")

	return (jint)gtk_radio_button_get_group((GtkRadioButton *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1button_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1radio_1button_1new\n")

	return (jint)gtk_radio_button_new((GSList *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1menu_1item_1new_1with_1label
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gtk_1radio_1menu_1item_1new_1with_1label\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)gtk_radio_menu_item_new_with_label((GSList *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1range_1get_1adjustment
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1range_1get_1adjustment\n")

	return (jint)gtk_range_get_adjustment((GtkRange *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1rc_1style_1set_1xthickness
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1rc_1style_1set_1xthickness\n")

	((GtkRcStyle *)arg0)->xthickness = arg1;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1rc_1style_1set_1ythickness
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1rc_1style_1set_1ythickness\n")

	((GtkRcStyle *)arg0)->ythickness = arg1;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scale_1set_1digits
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1scale_1set_1digits\n")

	gtk_scale_set_digits((GtkScale *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scale_1set_1draw_1value
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1scale_1set_1draw_1value\n")

	gtk_scale_set_draw_value((GtkScale *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1policy
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gtk_1scrolled_1window_1get_1policy\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gtk_scrolled_window_get_policy((GtkScrolledWindow *)arg0, (GtkPolicyType *)lparg1, (GtkPolicyType *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1hadjustment
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1scrolled_1window_1get_1hadjustment\n")

	return (jint)gtk_scrolled_window_get_hadjustment((GtkScrolledWindow *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1shadow_1type
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1scrolled_1window_1get_1shadow_1type\n")

	return (jint)gtk_scrolled_window_get_shadow_type((GtkScrolledWindow *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1vadjustment
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1scrolled_1window_1get_1vadjustment\n")

	return (jint)gtk_scrolled_window_get_vadjustment((GtkScrolledWindow *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1scrolled_1window_1new\n")

	return (jint)gtk_scrolled_window_new((GtkAdjustment *)arg0, (GtkAdjustment *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1set_1policy
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1scrolled_1window_1set_1policy\n")

	gtk_scrolled_window_set_policy((GtkScrolledWindow *)arg0, (GtkPolicyType)arg1, (GtkPolicyType)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1set_1shadow_1type
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1scrolled_1window_1set_1shadow_1type\n")

	gtk_scrolled_window_set_shadow_type((GtkScrolledWindow *)arg0, (GtkShadowType)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1selection_1data_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1selection_1data_1free\n")

	gtk_selection_data_free((GtkSelectionData *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1selection_1data_1set
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gtk_1selection_1data_1set\n")

	gtk_selection_data_set((GtkSelectionData *)arg0, (GdkAtom)arg1, (gint)arg2, (const guchar *)arg3, (gint)arg4);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1set_1locale
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1set_1locale\n")

	return (jint)gtk_set_locale();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1target_1list_1new
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1target_1list_1new\n")

	return (jint)gtk_target_list_new((GtkTargetEntry *)arg0, arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1target_1list_1unref
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1target_1list_1unref\n")

	gtk_target_list_unref((GtkTargetList *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1copy_1clipboard
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1text_1buffer_1copy_1clipboard\n")

	gtk_text_buffer_copy_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1cut_1clipboard
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("gtk_1text_1buffer_1cut_1clipboard\n")

	gtk_text_buffer_cut_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1, (gboolean)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1delete
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1delete\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_text_buffer_delete((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1bounds
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1get_1bounds\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_text_buffer_get_bounds((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1char_1count
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1buffer_1get_1char_1count\n")

	return (jint)gtk_text_buffer_get_char_count((GtkTextBuffer *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1end_1iter
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1get_1end_1iter\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_get_end_iter((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1insert
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1buffer_1get_1insert\n")

	return (jint)gtk_text_buffer_get_insert((GtkTextBuffer *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1iter_1at_1line
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1get_1iter_1at_1line\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_get_iter_at_line((GtkTextBuffer *)arg0,  (GtkTextIter *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1iter_1at_1mark
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1get_1iter_1at_1mark\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_get_iter_at_mark((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextMark *)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1iter_1at_1offset
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1get_1iter_1at_1offset\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_get_iter_at_offset((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1line_1count
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1buffer_1get_1line_1count\n")

	return (jint)gtk_text_buffer_get_line_count((GtkTextBuffer *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1selection_1bound
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1buffer_1get_1selection_1bound\n")

	return (jint)gtk_text_buffer_get_selection_bound((GtkTextBuffer *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1selection_1bounds
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("gtk_1text_1buffer_1get_1selection_1bounds\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jboolean)gtk_text_buffer_get_selection_bounds((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1get_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jboolean arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("gtk_1text_1buffer_1get_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)gtk_text_buffer_get_text((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2, arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1insert
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1insert\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_text_buffer_insert((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (gchar *)lparg2, arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1move_1mark
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1move_1mark\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_text_buffer_move_mark((GtkTextBuffer *)arg0, (GtkTextMark *)arg1, (GtkTextIter *)lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1paste_1clipboard
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jboolean arg3)
{
	jbyte *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1paste_1clipboard\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	gtk_text_buffer_paste_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1, (GtkTextIter *)lparg2, (gboolean)arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1place_1cursor
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1place_1cursor\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_place_cursor((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1buffer_1set_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1buffer_1set_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_text_buffer_set_text((GtkTextBuffer *)arg0, (gchar *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1iter_1get_1line
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1text_1iter_1get_1line\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_text_iter_get_line((GtkTextIter *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1iter_1get_1offset
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gtk_1text_1iter_1get_1offset\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gtk_text_iter_get_offset((GtkTextIter *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1buffer_1to_1window_1coords
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;

	DEBUG_CALL("gtk_1text_1view_1buffer_1to_1window_1coords\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	gtk_text_view_buffer_to_window_coords((GtkTextView *)arg0, (GtkTextWindowType)arg1, arg2, arg3, lparg4, lparg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1buffer
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1view_1get_1buffer\n")

	return (jint)gtk_text_view_get_buffer((GtkTextView *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1editable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1text_1view_1get_1editable\n")

	return (jboolean)gtk_text_view_get_editable((GtkTextView *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1iter_1location
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	GdkRectangle _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1text_1view_1get_1iter_1location\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getGdkRectangleFields(env, arg2, &_arg2);
	gtk_text_view_get_iter_location((GtkTextView *)arg0, (GtkTextIter *)lparg1, (GdkRectangle *)lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) setGdkRectangleFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1line_1at_1y
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;

	DEBUG_CALL("gtk_1text_1view_1get_1line_1at_1y\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	gtk_text_view_get_line_at_y((GtkTextView *)arg0, (GtkTextIter *)lparg1, arg2, lparg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1visible_1rect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1text_1view_1get_1visible_1rect\n")

	if (arg1) lparg1 = getGdkRectangleFields(env, arg1, &_arg1);
	gtk_text_view_get_visible_rect((GtkTextView *)arg0, (GdkRectangle *)lparg1);
	if (arg1) setGdkRectangleFields(env, arg1, lparg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1get_1window
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1text_1view_1get_1window\n")

	return (jint)gtk_text_view_get_window((GtkTextView *)arg0, (GtkTextWindowType)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1text_1view_1new\n")

	return (jint)gtk_text_view_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1scroll_1mark_1onscreen
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1text_1view_1scroll_1mark_1onscreen\n")

	gtk_text_view_scroll_mark_onscreen((GtkTextView *)arg0, (GtkTextMark *)arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1scroll_1to_1iter
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jdouble arg2, jboolean arg3, jdouble arg4, jdouble arg5)
{
	jbyte *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("gtk_1text_1view_1scroll_1to_1iter\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)gtk_text_view_scroll_to_iter((GtkTextView *)arg0, (GtkTextIter *)lparg1, (gdouble)arg2, (gboolean)arg3, (gdouble)arg4, (gdouble)arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1set_1editable
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1text_1view_1set_1editable\n")

	gtk_text_view_set_editable((GtkTextView *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1set_1tabs
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1text_1view_1set_1tabs\n")

	gtk_text_view_set_tabs((GtkTextView *)arg0, (PangoTabArray *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1view_1set_1wrap_1mode
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1text_1view_1set_1wrap_1mode\n")

	gtk_text_view_set_wrap_mode((GtkTextView *)arg0, (GtkWrapMode)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1timeout_1add
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1timeout_1add\n")

	return (jint)gtk_timeout_add((guint32)arg0, (GtkFunction)arg1, (gpointer)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1timeout_1remove
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1timeout_1remove\n")

	gtk_timeout_remove((guint)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1get_1active
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1toggle_1button_1get_1active\n")

	return (jboolean)gtk_toggle_button_get_active((GtkToggleButton *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1toggle_1button_1new\n")

	return (jint)gtk_toggle_button_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1set_1active
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1toggle_1button_1set_1active\n")

	gtk_toggle_button_set_active((GtkToggleButton *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1set_1mode
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1toggle_1button_1set_1mode\n")

	gtk_toggle_button_set_mode((GtkToggleButton *)arg0, (gboolean)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1element
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint rc;

	DEBUG_CALL("gtk_1toolbar_1insert_1element\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	rc = (jint)gtk_toolbar_insert_element((GtkToolbar *)arg0, (GtkToolbarChildType)arg1, (GtkWidget *)arg2, (const char *)lparg3, (const char *)lparg4, (const char *)lparg5, (GtkWidget *)arg6, (GtkSignalFunc)arg7, (gpointer)arg8, (gint)arg9);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1widget
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;

	DEBUG_CALL("gtk_1toolbar_1insert_1widget\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	gtk_toolbar_insert_widget((GtkToolbar *)arg0, (GtkWidget *)arg1, (const char *)lparg2, (const char *)lparg3, (gint)arg4);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1toolbar_1new\n")

	return (jint)gtk_toolbar_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1set_1orientation
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1toolbar_1set_1orientation\n")

	gtk_toolbar_set_orientation((GtkToolbar *)arg0, (GtkOrientation)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1disable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1tooltips_1disable\n")

	gtk_tooltips_disable((GtkTooltips *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1enable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1tooltips_1enable\n")

	gtk_tooltips_enable((GtkTooltips *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1tooltips_1new\n")

	return (jint)gtk_tooltips_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1set_1tip
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;

	DEBUG_CALL("gtk_1tooltips_1set_1tip\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	gtk_tooltips_set_tip((GtkTooltips *)arg0, (GtkWidget *)arg1, (const gchar *)lparg2, (const gchar *)lparg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vbox_1new
	(JNIEnv *env, jclass that, jboolean arg0, jint arg1)
{
	DEBUG_CALL("gtk_1vbox_1new\n")

	return (jint)gtk_vbox_new((gboolean)arg0, (gint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vscale_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1vscale_1new\n")

	return (jint)gtk_vscale_new((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vscrollbar_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1vscrollbar_1new\n")

	return (jint)gtk_vscrollbar_new((GtkAdjustment *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vseparator_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1vseparator_1new\n")

	return (jint)gtk_vseparator_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1accelerator
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1widget_1add_1accelerator\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_widget_add_accelerator((GtkWidget *)arg0, (const gchar *)lparg1, (GtkAccelGroup *)arg2, (guint)arg3, (GdkModifierType)arg4, arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1events
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1add_1events\n")

	gtk_widget_add_events((GtkWidget *)arg0, (gint)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1create_1pango_1layout
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("gtk_1widget_1create_1pango_1layout\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)gtk_widget_create_pango_layout((GtkWidget *)arg0, (gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1destroy\n")

	gtk_widget_destroy((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1default_1style
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gtk_1widget_1get_1default_1style\n")

	return (jint)gtk_widget_get_default_style();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1modifier_1style
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1modifier_1style\n")

	return (jint)gtk_widget_get_modifier_style((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1pango_1context
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1pango_1context\n")

	return (jint)gtk_widget_get_pango_context((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1parent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1parent\n")

	return (jint)gtk_widget_get_parent((GtkWidget *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1style
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1style\n")

	return (jint)gtk_widget_get_style((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1grab_1focus
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1grab_1focus\n")

	gtk_widget_grab_focus((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1hide
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1hide\n")

	gtk_widget_hide((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1base
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1widget_1modify_1base\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_widget_modify_base((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1bg
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1widget_1modify_1bg\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_widget_modify_bg((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1fg
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1widget_1modify_1fg\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_widget_modify_fg((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1font
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1modify_1font\n")

	gtk_widget_modify_font((GtkWidget *)arg0, (PangoFontDescription *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1style
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1modify_1style\n")

	gtk_widget_modify_style((GtkWidget *)arg0, (GtkRcStyle *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1text
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;

	DEBUG_CALL("gtk_1widget_1modify_1text\n")

	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	gtk_widget_modify_text((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
	if (arg2) setGdkColorFields(env, arg2, lparg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1queue_1draw
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1queue_1draw\n")

	gtk_widget_queue_draw((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1realize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1realize\n")

	gtk_widget_realize((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1remove_1accelerator
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gtk_1widget_1remove_1accelerator\n")

	gtk_widget_remove_accelerator((GtkWidget *)arg0, (GtkAccelGroup *)arg1, (guint)arg2, (GdkModifierType)arg3);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1reparent
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1reparent\n")

	gtk_widget_reparent((GtkWidget *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1double_1buffered
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1widget_1set_1double_1buffered\n")

	gtk_widget_set_double_buffered((GtkWidget *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1name
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1widget_1set_1name\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_widget_set_name((GtkWidget *)arg0, (const char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1redraw_1on_1allocate
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1widget_1set_1redraw_1on_1allocate\n")

	gtk_widget_set_redraw_on_allocate((GtkWidget *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1sensitive
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1widget_1set_1sensitive\n")

	gtk_widget_set_sensitive((GtkWidget *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1size_1request
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1widget_1set_1size_1request\n")

	gtk_widget_set_size_request((GtkWidget *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1state
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1set_1state\n")

	gtk_widget_set_state((GtkWidget *)arg0, (GtkStateType)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1style
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1widget_1set_1style\n")

	gtk_widget_set_style((GtkWidget *)arg0, (GtkStyle *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1show\n")

	gtk_widget_show((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1all
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1show_1all\n")

	gtk_widget_show_all((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1now
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1show_1now\n")

	gtk_widget_show_now((GtkWidget *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1allocate
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GtkAllocation _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1widget_1size_1allocate\n")

	if (arg1) lparg1 = getGtkAllocationFields(env, arg1, &_arg1);
	gtk_widget_size_allocate((GtkWidget *)arg0, (GtkAllocation *)lparg1);
	if (arg1) setGtkAllocationFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1request
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GtkRequisition _arg1, *lparg1=NULL;

	DEBUG_CALL("gtk_1widget_1size_1request\n")

	if (arg1) lparg1 = getGtkRequisitionFields(env, arg1, &_arg1);
	gtk_widget_size_request((GtkWidget *)arg0, (GtkRequisition *)lparg1);
	if (arg1) setGtkRequisitionFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1style_1get
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gtk_1widget_1style_1get\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gtk_widget_style_get((GtkWidget *)arg0, (const gchar *)lparg1, (void *)lparg2, (void *)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1unrealize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1unrealize\n")

	gtk_widget_unrealize((GtkWidget *)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1activate_1default
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1activate_1default\n")

	return (jboolean)gtk_window_activate_default((GtkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1add_1accel_1group
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1window_1add_1accel_1group\n")

	gtk_window_add_accel_group((GtkWindow *)arg0, (GtkAccelGroup *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1deiconify
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1deiconify\n")

	gtk_window_deiconify((GtkWindow *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1get_1focus
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1get_1focus\n")

	return (jint)gtk_window_get_focus((GtkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1get_1position
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gtk_1window_1get_1position\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gtk_window_get_position((GtkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1get_1size
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("gtk_1window_1get_1size\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	gtk_window_get_size((GtkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1iconify
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1iconify\n")

	gtk_window_iconify((GtkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1maximize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1maximize\n")

	gtk_window_maximize((GtkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1move
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1window_1move\n")

	gtk_window_move((GtkWindow *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1new\n")

	return (jint)gtk_window_new((GtkWindowType)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1remove_1accel_1group
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1window_1remove_1accel_1group\n")

	gtk_window_remove_accel_group((GtkWindow *)arg0, (GtkAccelGroup *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1resize
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gtk_1window_1resize\n")

	gtk_window_resize((GtkWindow *)arg0, (gint)arg1, (gint)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1default
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1window_1set_1default\n")

	gtk_window_set_default((GtkWindow *)arg0, (GtkWidget *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1destroy_1with_1parent
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1window_1set_1destroy_1with_1parent\n")

	gtk_window_set_destroy_with_parent((GtkWindow *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1modal
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1window_1set_1modal\n")

	gtk_window_set_modal((GtkWindow *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1resizable
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gtk_1window_1set_1resizable\n")

	gtk_window_set_resizable((GtkWindow *)arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1title
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("gtk_1window_1set_1title\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	gtk_window_set_title((GtkWindow *)arg0, (const gchar *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1transient_1for
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1window_1set_1transient_1for\n")

	gtk_window_set_transient_for((GtkWindow *)arg0, (GtkWindow *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1unmaximize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1window_1unmaximize\n")

	gtk_window_unmaximize((GtkWindow *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GtkTargetEntry _arg1, *lparg1=NULL;

	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I\n")

	if (arg1) lparg1 = getGtkTargetEntryFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GdkEventButton _arg1, *lparg1=NULL;

	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I\n")

	if (arg1) lparg1 = getGdkEventButtonFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
}

#ifndef NO_CLIST
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCListColumn_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GtkCListColumn _arg1, *lparg1=NULL;

	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GtkCListColumn_2I\n")

	if (arg1) lparg1 = getGtkCListColumnFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
}
#endif /* NO_CLIST */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GtkTargetPair _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setGtkTargetPairFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GtkSelectionData _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGtkSelectionDataFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkDragContext _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setGdkDragContextFields(env, arg0, lparg0);
}

#ifndef NO_CLIST
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCTreeRow_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GtkCTreeRow _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkCTreeRow_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGtkCTreeRowFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCListColumn_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GtkCListColumn _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkCListColumn_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGtkCListColumnFields(env, arg0, lparg0);
}
#endif /* NO_CLIST */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEvent _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkEventFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEventFocus _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkEventFocusFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEventButton _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkEventButtonFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEventCrossing _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setGdkEventCrossingFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEventExpose _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkEventExposeFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkEventKey _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkEventKeyFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GdkRectangle _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGdkRectangleFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2\n")

	if (arg1) getGtkAdjustmentFields(env, arg1, (GtkAdjustment *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2\n")

	if (arg1) getGtkFixedFields(env, arg1, (GtkFixed *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DEBUG_CALL("memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2\n")

	if (arg1) getGtkStyleFields(env, arg1, (GtkStyle *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n")

	if (arg0) setGtkColorSelectionDialogFields(env, arg0, (GtkColorSelectionDialog *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I\n")

	if (arg0) setGtkStyleFields(env, arg0, (GtkStyle *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I\n")

	if (arg0) setGtkComboFields(env, arg0, (GtkCombo *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I\n")

	if (arg0) setGtkFixedFields(env, arg0, (GtkFixed *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I\n")

	if (arg0) setGtkAdjustmentFields(env, arg0, (GtkAdjustment *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I\n")

	if (arg0) setGdkVisualFields(env, arg0, (GdkVisual *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	DEBUG_CALL("memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I\n")

	if (arg0) setGdkImageFields(env, arg0, (GdkImage *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3CII
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;

	DEBUG_CALL("memmove___3CII\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3II
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;

	DEBUG_CALL("memmove__I_3II\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3BI
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("memmove__I_3BI\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3I_3BI
	(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1, jint arg2)
{
	jint *lparg0=NULL;
	jbyte *lparg1=NULL;

	DEBUG_CALL("memmove___3I_3BI\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	memmove((void *)lparg0, (const void *)lparg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3BII
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("memmove___3BII\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3III
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;

	DEBUG_CALL("memmove___3III\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1get_1font_1description
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1context_1get_1font_1description\n")

	return (jint)pango_context_get_font_description((PangoContext *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1get_1language
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1context_1get_1language\n")

	return (jint)pango_context_get_language((PangoContext *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1get_1metrics
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("pango_1context_1get_1metrics\n")

	return (jint)pango_context_get_metrics((PangoContext *)arg0, (const PangoFontDescription *)arg1, (PangoLanguage *)arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1list_1families
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("pango_1context_1list_1families\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	pango_context_list_families((PangoContext *)arg0, (PangoFontFamily ***)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1set_1font_1description
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1context_1set_1font_1description\n")

	pango_context_set_font_description((PangoContext *)arg0, (PangoFontDescription *)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1copy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1copy\n")

	return (jint)pango_font_description_copy((PangoFontDescription *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1free\n")

	pango_font_description_free((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1from_1string
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("pango_1font_1description_1from_1string\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)pango_font_description_from_string((const char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1family
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1get_1family\n")

	return (jint)pango_font_description_get_family((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1size
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1get_1size\n")

	return (jint)pango_font_description_get_size((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1style
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1get_1style\n")

	return (jint)pango_font_description_get_style((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1weight
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1get_1weight\n")

	return (jint)pango_font_description_get_weight((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("pango_1font_1description_1new\n")

	return (jint)pango_font_description_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1family
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("pango_1font_1description_1set_1family\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	pango_font_description_set_family((PangoFontDescription *)arg0, (const char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1size
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1font_1description_1set_1size\n")

	pango_font_description_set_size((PangoFontDescription *)arg0, (gint)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1stretch
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1font_1description_1set_1stretch\n")

	pango_font_description_set_stretch((PangoFontDescription *)arg0, (PangoStretch)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1style
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1font_1description_1set_1style\n")

	pango_font_description_set_style((PangoFontDescription *)arg0, (PangoStyle)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1weight
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1font_1description_1set_1weight\n")

	pango_font_description_set_weight((PangoFontDescription *)arg0, (PangoWeight)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1to_1string
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1description_1to_1string\n")

	return (jint)pango_font_description_to_string((PangoFontDescription *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1face_1describe
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1face_1describe\n")

	return (jint)pango_font_face_describe((PangoFontFace *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1family_1list_1faces
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("pango_1font_1family_1list_1faces\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	pango_font_family_list_faces((PangoFontFamily *)arg0, (PangoFontFace ***)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1approximate_1char_1width
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1metrics_1get_1approximate_1char_1width\n")

	return (jint)pango_font_metrics_get_approximate_char_width((PangoFontMetrics *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1ascent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1metrics_1get_1ascent\n")

	return (jint)pango_font_metrics_get_ascent((PangoFontMetrics *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1descent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1metrics_1get_1descent\n")

	return (jint)pango_font_metrics_get_descent((PangoFontMetrics *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1unref
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1font_1metrics_1unref\n")

	pango_font_metrics_unref((PangoFontMetrics *)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1language_1from_1string
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("pango_1language_1from_1string\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)pango_language_from_string((const char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1layout_1get_1size
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("pango_1layout_1get_1size\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	pango_layout_get_size((PangoLayout *)arg0, (int *)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1layout_1new
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1layout_1new\n")

	return (jint)pango_layout_new((PangoContext *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1layout_1set_1font_1description
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("pango_1layout_1set_1font_1description\n")

	pango_layout_set_font_description((PangoLayout *)arg0, (PangoFontDescription *)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1layout_1set_1text
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("pango_1layout_1set_1text\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	pango_layout_set_text((PangoLayout *)arg0, (const char *)lparg1, (int)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1tab_1array_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("pango_1tab_1array_1free\n")

	pango_tab_array_free((PangoTabArray *)arg0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1tab_1array_1get_1tab
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;

	DEBUG_CALL("pango_1tab_1array_1get_1tab\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	pango_tab_array_get_tab((PangoTabArray *)arg0, arg1, (PangoTabAlign *)lparg2, (gint *)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1tab_1array_1new
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("pango_1tab_1array_1new\n")

	return (jint)pango_tab_array_new(arg0, (gboolean)arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1tab_1array_1set_1tab
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("pango_1tab_1array_1set_1tab\n")

	pango_tab_array_set_tab((PangoTabArray *)arg0, arg1, (PangoTabAlign)arg2, arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_strlen
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("strlen\n")

	return (jint)strlen((const char *)arg0);
}

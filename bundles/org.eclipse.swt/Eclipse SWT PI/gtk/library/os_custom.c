/*******************************************************************************
* Copyright (c) 2000, 2011 IBM Corporation and others. All rights reserved.
* The contents of this file are made available under the terms
* of the GNU Lesser General Public License (LGPL) Version 2.1 that
* accompanies this distribution (lgpl-v21.txt).  The LGPL is also
* available at http://www.gnu.org/licenses/lgpl.html.  If the version
* of the LGPL at http://www.gnu.org is different to the version of
* the LGPL accompanying this distribution and there is any conflict
* between the two license versions, the terms of the LGPL accompanying
* this distribution shall govern.
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_OS_##func

#ifndef NO__1call_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1call_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jintLong arg7)
{
   /*
   * Bug in Solaris. For some reason, the assembler generated for this function (when not putting the parameters in the stack)  crashes. 
   * It seems that this  is caused by a bug in the Sun Studio Compiler when the optimization level is greater or equal to two.
   * The fix is rewrite the function passing all parameters on the stack. Alternatively, the problem could be fixed by lowering the optimization level,
   * but this solution would significantly increase the size of the library. 
   */
	const GdkRectangle rect;
	gint x, y, width, height;
	const GdkRectangle *lprect = NULL;
	gint* lpx = NULL;
	gint* lpy = NULL;
	gint* lpwidth = NULL;
	gint* lpheight = NULL;
	OS_NATIVE_ENTER(env, that, _1call_1get_1size_FUNC);
	if (arg3) lprect = &rect;
	if (arg4) lpx = &x;
	if (arg5) lpy = &y;
	if (arg6) lpwidth = &width;
	if (arg7) lpheight = &height;
	((void (*)(GtkCellRenderer *, GtkWidget *, const GdkRectangle *, gint *, gint *, gint *, gint *))arg0)((GtkCellRenderer *)arg1, (GtkWidget *)arg2, lprect, lpx, lpy, lpwidth, lpheight);
	if (arg3) *((GdkRectangle *)arg3) = rect;
	if (arg4) *((gint *)arg4) = x;
	if (arg5) *((gint *)arg5) = y;
	if (arg6) *((gint *)arg6) = width;
	if (arg7) *((gint *)arg7) = height;
	OS_NATIVE_EXIT(env, that, _1call_1get_1size_FUNC);
}
#endif

#ifndef NO_GDK_1WINDOWING_1X11
JNIEXPORT jboolean JNICALL OS_NATIVE(GDK_1WINDOWING_1X11)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, GDK_1WINDOWING_1X11_FUNC)
#ifdef GDK_WINDOWING_X11
	rc = (jboolean)1;
#else
	rc = (jboolean)0;
#endif	
	OS_NATIVE_EXIT(env, that, GDK_1WINDOWING_1X11_FUNC)
	return rc;
}
#endif

#ifndef NO_imContextNewProc_1CALLBACK
static jintLong superIMContextNewProc;
static GtkIMContext* lastIMContext;
static GtkIMContext* imContextNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	GtkIMContext* context = ((GtkIMContext * (*)(GType, guint, GObjectConstructParam *))superIMContextNewProc)(type, n_construct_properties, construct_properties);
	lastIMContext = context;
	return context;
}
#ifndef NO_imContextLast
JNIEXPORT jintLong JNICALL OS_NATIVE(imContextLast)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, imContextLast_FUNC);
	rc = (jintLong)lastIMContext;
	OS_NATIVE_EXIT(env, that, imContextLast_FUNC);
	return rc;
}
#endif

JNIEXPORT jintLong JNICALL OS_NATIVE(imContextNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, imContextNewProc_1CALLBACK_FUNC);
	superIMContextNewProc = arg0;
	rc = (jintLong)imContextNewProc;
	OS_NATIVE_EXIT(env, that, imContextNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO_pangoLayoutNewProc_1CALLBACK
static jintLong superPangoLayoutNewProc;
static PangoLayout * pangoLayoutNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoLayout* layout = ((PangoLayout * (*)(GType, guint, GObjectConstructParam *))superPangoLayoutNewProc)(type, n_construct_properties, construct_properties);
	pango_layout_set_auto_dir (layout, 0);
	return layout;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(pangoLayoutNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoLayoutNewProc_1CALLBACK_FUNC);
	superPangoLayoutNewProc = arg0;
	rc = (jintLong)pangoLayoutNewProc;
	OS_NATIVE_EXIT(env, that, pangoLayoutNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1dialog_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1dialog_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jint arg2, jintLong arg3, jint arg4, jintLong arg5, jint arg6, jintLong arg7)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1dialog_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_file_chooser_dialog_new(lparg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
*/
	{
		/*
		* On AMD64, it is critical that functions which have a variable number of
		* arguments, indicated by '...', include the '...' in their prototype.  This
		* changes the calling convention, and leaving it out will cause crashes.
		*
		* For some reason, we must also explicitly declare all of the arguments we
		* are passing in, otherwise it crashes.
		*/
		typedef jintLong (CALLING_CONVENTION* FPTR)(jbyte *, jintLong, jint, jintLong, jint, jintLong, jint, jintLong, ...);
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_dialog_new)
		if (fp) {
			rc = (jintLong)((FPTR) fp)(lparg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1dialog_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1cell_1layout_1set_1attributes
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1cell_1layout_1set_1attributes)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintLong arg4)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1layout_1set_1attributes_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	gtk_cell_layout_set_attributes(arg0, arg1, lparg2, arg3, arg4);
*/
	{
		/*
		* On AMD64, it is critical that functions which have a variable number of
		* arguments, indicated by '...', include the '...' in their prototype.  This
		* changes the calling convention, and leaving it out will cause crashes.
		*
		* For some reason, we must also explicitly declare all of the arguments we
		* are passing in, otherwise it crashes.
		*/
		typedef void (*FPTR)(jintLong, jintLong, jbyte *, jint, jintLong, ...);
		OS_LOAD_FUNCTION(fp, gtk_cell_layout_set_attributes)
		if (fp) {
			((FPTR)fp)(arg0, arg1, lparg2, arg3, arg4);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1layout_1set_1attributes_FUNC);
}
#endif

glong g_utf16_strlen(const gchar *str, glong max) {
	const gchar *s = str;
	guchar ch;
	glong offset = 0;
	if (!s || max == 0) return 0;
	if (max < 0) {
		while (*s) {
			if (0xf0 <= *(guchar*)s && *(guchar*)s <= 0xfd) offset++;
			s = g_utf8_next_char (s);
			offset++;
		}
		
	} else {
		while (*s) {
			ch = *(guchar*)s;
			s = g_utf8_next_char (s);
			if (s - str > max) break;
			if (0xf0 <= ch && ch <= 0xfd) offset++;
			offset++;
		}
	}
	return offset;
}

glong g_utf16_pointer_to_offset(const gchar *str, const gchar * pos) {
	const gchar *s = str;
	glong offset = 0;
	if (!s || !pos) return 0; 
	while (s < pos && *s) {
		if (0xf0 <= *(guchar*)s && *(guchar*)s <= 0xfd) offset++;
		s = g_utf8_next_char (s);
		offset++;
	}
	return offset;
}

gchar* g_utf16_offset_to_pointer(const gchar* str, glong offset) {
	const gchar *s = str;
	if (!s) return 0; 
	while (offset-- > 0 && *s) {
		if (0xf0 <= *(guchar*)s && *(guchar*)s <= 0xfd) offset--;
		s = g_utf8_next_char (s);
	}
	return (gchar *)s;
}

glong g_utf16_offset_to_utf8_offset(const gchar* str, glong offset) {
	glong r = 0;
	const gchar *s = str;
	if (!s) return 0;
	while (offset-- > 0 && *s) {
		if (0xf0 <= *(guchar*)s && *(guchar*)s <= 0xfd) offset--;
		s = g_utf8_next_char (s);
		r++;
	}
	return r;
}

glong g_utf8_offset_to_utf16_offset(const gchar* str, glong offset) {
	glong r = 0;
	const gchar *s = str;
	if (!s) return 0;
	while (offset-- > 0 && *s) {
		if (0xf0 <= *(guchar*)s && *(guchar*)s <= 0xfd) r++;
		s = g_utf8_next_char (s);
		r++;
	}
	return r;
}

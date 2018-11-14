/*******************************************************************************
* Copyright (c) 2000, 2018 IBM Corporation and others. All rights reserved.
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

/* Include guard */
#ifndef ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H
#define ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H

/* Special sizeof's */
#define GPollFD_sizeof() sizeof(GPollFD)
#define GtkCellRendererText_sizeof() sizeof(GtkCellRendererText)
#define GtkCellRendererTextClass_sizeof() sizeof(GtkCellRendererTextClass)
#define GtkCellRendererPixbuf_sizeof() sizeof(GtkCellRendererPixbuf)
#define GtkCellRendererPixbufClass_sizeof() sizeof(GtkCellRendererPixbufClass)
#define GtkCellRendererToggle_sizeof() sizeof(GtkCellRendererToggle)
#define GtkCellRendererToggleClass_sizeof() sizeof(GtkCellRendererToggleClass)
#define GtkTextIter_sizeof() sizeof(GtkTextIter)
#define GtkTreeIter_sizeof() sizeof(GtkTreeIter)

#ifdef _WIN32
#define LIB_GTK "libgtk-3-0.dll"
#define LIB_GDK "libgdk-3-0.dll"
#define LIB_GTHREAD "libgthread-2.0-0.dll"
#define LIB_GLIB "libglib-2.0-0.dll"
#define LIB_ATK "libatk-1.0-0.dll"
#define LIB_FONTCONFIG "libfontconfig-1.dll"
#else
#if GTK_CHECK_VERSION(3,94,0)
#define LIB_GTK "libgtk-4.so.0.9400.0"
// Point GDK to GTK for GTK4
#define LIB_GDK "libgtk-4.so.0.9400.0"
#else
#define LIB_GTK "libgtk-3.so.0"
#define LIB_GDK "libgdk-3.so.0"
#endif
#define LIB_GTHREAD "libgthread-2.0.so.0"
#define LIB_GLIB "libglib-2.0.so.0"
#define LIB_ATK "libatk-1.0.so.0"
#define LIB_FONTCONFIG "libfontconfig.so.1"
#endif

/* Libraries for dynamic loaded functions */

#define ubuntu_menu_proxy_get_LIB LIB_GTK
#define FcConfigAppFontAddFile_LIB LIB_FONTCONFIG

/* Field accessors */
#define G_OBJECT_CLASS_CONSTRUCTOR(arg0) (arg0)->constructor
#define G_OBJECT_CLASS_SET_CONSTRUCTOR(arg0, arg1) (arg0)->constructor = (GObject* (*) (GType, guint, GObjectConstructParam *))arg1
struct _GtkAccelLabelPrivate
{
  GtkWidget     *accel_widget;       /* done */
  GClosure      *accel_closure;      /* has set function */
  GtkAccelGroup *accel_group;        /* set by set_accel_closure() */
  gchar         *accel_string;       /* has set function */
  guint          accel_padding;      /* should be style property? */
  guint16        accel_string_width; /* seems to be private */
};
#define GTK_ACCEL_LABEL_SET_ACCEL_STRING(arg0, arg1) (arg0)->priv->accel_string = arg1
#define GTK_ACCEL_LABEL_GET_ACCEL_STRING(arg0) (arg0)->priv->accel_string
#define GTK_ENTRY_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_TEXTVIEW_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_WIDGET_REQUISITION_WIDTH(arg0) (arg0)->requisition.width
#define GTK_WIDGET_REQUISITION_HEIGHT(arg0) (arg0)->requisition.height
#define GDK_EVENT_TYPE(arg0) (arg0)->type
#define GDK_EVENT_WINDOW(arg0) (arg0)->window
#define X_EVENT_TYPE(arg0) (arg0)->type
#define X_EVENT_WINDOW(arg0) (arg0)->window
#define g_error_get_message(arg0) (arg0)->message
#define g_list_data(arg0) (arg0)->data
#define g_slist_data(arg0) (arg0)->data
#define g_list_set_next(arg0, arg1) (arg0)->next = arg1
#define g_list_set_previous(arg0, arg1) (arg0)->prev = arg1
#define gtk_style_get_font_desc(arg0) (arg0)->font_desc
#define gtk_style_get_xthickness(arg0) (arg0)->xthickness
#define gtk_style_get_ythickness(arg0) (arg0)->ythickness
#define localeconv_decimal_point() localeconv()->decimal_point

// Mechanism to get function pointers of C/gtk functions back to java.
// Idea is that you substitute the return value with the function pointer.
// NOTE: functions like gtk_false need to be linked to a lib. Eg see gtk_false_LIB above.
#define GET_FUNCTION_POINTER_gtk_false() 0; \
GTK_LOAD_FUNCTION(fp, gtk_false) \
rc = (jintLong)fp;

#define gtk_status_icon_position_menu_func() 0; \
GTK_LOAD_FUNCTION(fp, gtk_status_icon_position_menu) \
rc = (jintLong)fp;

glong g_utf16_pointer_to_offset(const gchar*, const gchar*);
gchar* g_utf16_offset_to_pointer(const gchar*, glong);
glong g_utf16_strlen(const gchar*, glong max);
glong g_utf16_offset_to_utf8_offset(const gchar*, glong);
glong g_utf8_offset_to_utf16_offset(const gchar*, glong);

// Cached JVM pointer: see bug 521487.
JavaVM *cached_jvm;

#define SWT_TYPE_FIXED (swt_fixed_get_type ())
#define SWT_FIXED(obj) (G_TYPE_CHECK_INSTANCE_CAST ((obj), SWT_TYPE_FIXED, SwtFixed))
#define SWT_FIXED_CLASS(klass) (G_TYPE_CHECK_CLASS_CAST ((klass), SWT_TYPE_FIXED, SwtFixedClass))
#define SWT_IS_FIXED(obj) (G_TYPE_CHECK_INSTANCE_TYPE ((obj), SWT_TYPE_FIXED))
#define SWT_IS_FIXED_CLASS(klass) (G_TYPE_CHECK_CLASS_TYPE ((klass), SWT_TYPE_FIXED))
#define SWT_FIXED_GET_CLASS(obj) (G_TYPE_INSTANCE_GET_CLASS ((obj), SWT_TYPE_FIXED, SwtFixedClass))


typedef struct _SwtFixed SwtFixed;
typedef struct _SwtFixedPrivate SwtFixedPrivate;
typedef struct _SwtFixedClass SwtFixedClass;

struct _SwtFixed
{
  GtkContainer container;

  /*< private >*/
  SwtFixedPrivate *priv;

  /* Accessibility */
  AtkObject *accessible;
};

struct _SwtFixedClass
{
  GtkContainerClass parent_class;
};

GType swt_fixed_get_type (void) G_GNUC_CONST;

void swt_fixed_restack(SwtFixed *fixed, GtkWidget *widget, GtkWidget *sibling, gboolean above);
void swt_fixed_move(SwtFixed *fixed, GtkWidget *widget, gint x, gint y);
void swt_fixed_resize(SwtFixed *fixed, GtkWidget *widget, gint width, gint height);

#include <gtk/gtk-a11y.h>

#define SWT_TYPE_FIXED_ACCESSIBLE      (swt_fixed_accessible_get_type ())
#define SWT_FIXED_ACCESSIBLE(obj)      (G_TYPE_CHECK_INSTANCE_CAST ((obj), SWT_TYPE_FIXED_ACCESSIBLE, SwtFixedAccessible))
#define SWT_IS_FIXED_ACCESSIBLE(obj)   (G_TYPE_CHECK_INSTANCE_TYPE ((obj), SWT_TYPE_FIXED_ACCESSIBLE))

typedef struct _SwtFixedAccessible SwtFixedAccessible;
typedef struct _SwtFixedAccessiblePrivate SwtFixedAccessiblePrivate;
typedef struct _SwtFixedAccessibleClass SwtFixedAccessibleClass;

struct _SwtFixedAccessible
{
	GtkContainerAccessible parent;

	SwtFixedAccessiblePrivate *priv;
};

struct _SwtFixedAccessibleClass
{
	GtkContainerAccessibleClass parent_class;
};

GType swt_fixed_accessible_get_type (void) G_GNUC_CONST;
AtkObject *swt_fixed_accessible_new (GtkWidget *widget);
void swt_fixed_accessible_register_accessible (AtkObject *obj, gboolean is_native, GtkWidget *to_map);
jintLong call_accessible_object_function (const char *method_name, const char *method_signature,...);

void swt_debug_on_fatal_warnings() ;

#endif /* ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H (include guard, this should be the last line) */

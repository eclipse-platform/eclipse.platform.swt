/*******************************************************************************
* Copyright (c) 2000, 2013 IBM Corporation and others. All rights reserved.
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

JavaVM *cached_jvm = NULL;

JNIEXPORT void JNICALL OS_NATIVE(_1cachejvmptr)
	(JNIEnv *env, jclass that)
{
	/* cache the JavaVM pointer */
	if (cached_jvm == NULL) (*env)->GetJavaVM(env, &cached_jvm);
}

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

#ifndef NO_GDK_1WINDOWING_1WAYLAND
JNIEXPORT jboolean JNICALL OS_NATIVE(GDK_1WINDOWING_1WAYLAND)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, GDK_1WINDOWING_1WAYLAND_FUNC)
#ifdef GDK_WINDOWING_WAYLAND
	rc = (jboolean)1;
#else
	rc = (jboolean)0;
#endif
	OS_NATIVE_EXIT(env, that, GDK_1WINDOWING_1WAYLAND_FUNC)
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

#ifndef NO_pangoFontFamilyNewProc_1CALLBACK
static jintLong superPangoFontFamilyNewProc;
static PangoFontFamily * pangoFontFamilyNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoFontFamily* fontFamily = ((PangoFontFamily * (*)(GType, guint, GObjectConstructParam *))superPangoFontFamilyNewProc)(type, n_construct_properties, construct_properties);
	return fontFamily;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(pangoFontFamilyNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoFontFamilyNewProc_1CALLBACK_FUNC);
	superPangoFontFamilyNewProc = arg0;
	rc = (jintLong)pangoFontFamilyNewProc;
	OS_NATIVE_EXIT(env, that, pangoFontFamilyNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO_pangoFontFaceNewProc_1CALLBACK
static jintLong superPangoFontFaceNewProc;
static PangoFontFace * pangoFontFaceNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoFontFace* fontFace = ((PangoFontFace * (*)(GType, guint, GObjectConstructParam *))superPangoFontFaceNewProc)(type, n_construct_properties, construct_properties);
	return fontFace;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(pangoFontFaceNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoFontFaceNewProc_1CALLBACK_FUNC);
	superPangoFontFaceNewProc = arg0;
	rc = (jintLong)pangoFontFaceNewProc;
	OS_NATIVE_EXIT(env, that, pangoFontFaceNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif
#ifndef NO_printerOptionWidgetNewProc_1CALLBACK
static jintLong superPrinterOptionWidgetNewProc;
static GType * printerOptionWidgetNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	GType* printerOptionWidget = ((GType * (*)(GType, guint, GObjectConstructParam *))superPrinterOptionWidgetNewProc)(type, n_construct_properties, construct_properties);
	return printerOptionWidget;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(printerOptionWidgetNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, printerOptionWidgetNewProc_1CALLBACK_FUNC);
	superPrinterOptionWidgetNewProc = arg0;
	rc = (jintLong)printerOptionWidgetNewProc;
	OS_NATIVE_EXIT(env, that, printerOptionWidgetNewProc_1CALLBACK_FUNC);
	return rc;
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

#ifndef NO__1gdk_1keymap_1translate_1keyboard_1state
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1keymap_1translate_1keyboard_1state)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jlongArray arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jlong *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	guint tmp4;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1keymap_1translate_1keyboard_1state_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_keymap_translate_keyboard_state((GdkKeymap*)arg0, arg1, (GdkModifierType)arg2, arg3, &tmp4, (gint*)lparg5, (gint*)lparg6, (GdkModifierType *)lparg7);
	if (lparg4) *lparg4 = tmp4;
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1keymap_1translate_1keyboard_1state_FUNC);
	return rc;
}
#endif

#ifndef NO_SwtFixed

struct _SwtFixedPrivate {
  GtkAdjustment *hadjustment;
  GtkAdjustment *vadjustment;
  guint hscroll_policy : 1;
  guint vscroll_policy : 1;
  GList *children;
};

struct _SwtFixedChild
{
  GtkWidget *widget;
  gint x;
  gint y;
  gint width;
  gint height;
};
typedef struct _SwtFixedChild SwtFixedChild;

enum {
   PROP_0,
   PROP_HADJUSTMENT,
   PROP_VADJUSTMENT,
   PROP_HSCROLL_POLICY,
   PROP_VSCROLL_POLICY,
};

static void swt_fixed_get_property (GObject *object, guint prop_id, GValue *value, GParamSpec *pspec);
static void swt_fixed_set_property (GObject *object, guint prop_id, const GValue *value, GParamSpec *pspec);
static void swt_fixed_finalize (GObject *object);
static void swt_fixed_realize (GtkWidget *widget);
static void swt_fixed_map (GtkWidget *widget);
static AtkObject *swt_fixed_get_accessible (GtkWidget *widget);
static void swt_fixed_get_preferred_width (GtkWidget *widget, gint *minimum, gint *natural);
static void swt_fixed_get_preferred_height (GtkWidget *widget, gint *minimum, gint *natural);
static void swt_fixed_size_allocate (GtkWidget *widget, GtkAllocation *allocation);
static void swt_fixed_add (GtkContainer *container, GtkWidget *widget);
static void swt_fixed_remove (GtkContainer *container, GtkWidget *widget);
static void swt_fixed_forall (GtkContainer *container, gboolean include_internals, GtkCallback callback, gpointer callback_data);

G_DEFINE_TYPE_WITH_CODE (SwtFixed, swt_fixed, GTK_TYPE_CONTAINER, G_IMPLEMENT_INTERFACE (GTK_TYPE_SCROLLABLE, NULL))

static void swt_fixed_class_init (SwtFixedClass *class) {
	GObjectClass *gobject_class = (GObjectClass*) class;
	GtkWidgetClass *widget_class = (GtkWidgetClass*) class;
	GtkContainerClass *container_class = (GtkContainerClass*) class;

	/* GOject implementation */
	gobject_class->set_property = swt_fixed_set_property;
	gobject_class->get_property = swt_fixed_get_property;
	gobject_class->finalize = swt_fixed_finalize;

	/* Scrollable implemetation */
	g_object_class_override_property (gobject_class, PROP_HADJUSTMENT,    "hadjustment");
	g_object_class_override_property (gobject_class, PROP_VADJUSTMENT,    "vadjustment");
	g_object_class_override_property (gobject_class, PROP_HSCROLL_POLICY, "hscroll-policy");
	g_object_class_override_property (gobject_class, PROP_VSCROLL_POLICY, "vscroll-policy");

	/* Widget implementation */
	widget_class->realize = swt_fixed_realize;
	widget_class->map = swt_fixed_map;
	widget_class->get_preferred_width = swt_fixed_get_preferred_width;
	widget_class->get_preferred_height = swt_fixed_get_preferred_height;
	widget_class->size_allocate = swt_fixed_size_allocate;

	/* Accessibility implementation */
	widget_class->get_accessible = swt_fixed_get_accessible;

	/* Container implementation */
	container_class->add = swt_fixed_add;
	container_class->remove = swt_fixed_remove;
	container_class->forall = swt_fixed_forall;

	g_type_class_add_private (class, sizeof (SwtFixedPrivate));
}

void swt_fixed_restack (SwtFixed *fixed, GtkWidget *widget, GtkWidget *sibling, gboolean above) {
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	SwtFixedChild *child, *sibling_child;

	list = priv->children;
	while (list) {
		child = list->data;
		if (child->widget == widget) break;
		list = list->next;
	}
	
	if (!list) return;
	priv->children = g_list_remove_link (priv->children, list);
	g_list_free_1 (list);
	
	list = NULL;
	if (sibling) {
		list = priv->children;
		while (list) {
			sibling_child = list->data;
			if (sibling_child->widget == sibling) {
				break;
			}
			list = list->next;
		}
		if (list) {
			if (!above) list = list->next;
		}
	}
	if (!list) {
		list = above ? priv->children : NULL;
	}
	priv->children = g_list_insert_before (priv->children, list, child);
	
	/*
	{
	GdkWindow *sibling_window = NULL;
	if (list) {
		child = list->data;
		sibling_window = gtk_widget_get_window (child);
	}
	gdk_window_restack (gtk_widget_get_window (widget), sibling_window, above);
	}
	*/ 
}

static void swt_fixed_init (SwtFixed *widget) {
	SwtFixedPrivate *priv;

	priv = widget->priv = G_TYPE_INSTANCE_GET_PRIVATE (widget, SWT_TYPE_FIXED, SwtFixedPrivate);
	priv->children = NULL;
	priv->hadjustment = NULL;
	priv->vadjustment = NULL;
}

static void swt_fixed_finalize (GObject *object) {
	SwtFixed *widget = SWT_FIXED (object);
	SwtFixedPrivate *priv = widget->priv;

	g_object_unref (priv->hadjustment);
	g_object_unref (priv->vadjustment);
	g_clear_object (&widget->accessible);

	G_OBJECT_CLASS (swt_fixed_parent_class)->finalize (object);
}

static void swt_fixed_get_property (GObject *object, guint prop_id, GValue *value, GParamSpec *pspec) {
	SwtFixed *widget = SWT_FIXED (object);
	SwtFixedPrivate *priv = widget->priv;

	switch (prop_id) {
		case PROP_HADJUSTMENT:
			g_value_set_object (value, priv->hadjustment);
			break;
		case PROP_VADJUSTMENT:
			g_value_set_object (value, priv->vadjustment);
			break;
		case PROP_HSCROLL_POLICY:
			g_value_set_enum (value, priv->hscroll_policy);
			break;
		case PROP_VSCROLL_POLICY:
			g_value_set_enum (value, priv->vscroll_policy);
			break;
		default:
			G_OBJECT_WARN_INVALID_PROPERTY_ID (object, prop_id, pspec);
			break;
	}
}

static void swt_fixed_set_property (GObject *object, guint prop_id, const GValue *value, GParamSpec *pspec) {
	SwtFixed *widget = SWT_FIXED (object);
	SwtFixedPrivate *priv = widget->priv;
	GtkAdjustment *adjustment;

	switch (prop_id) {
		case PROP_HADJUSTMENT:
			adjustment = g_value_get_object (value);
			if (adjustment && priv->hadjustment == adjustment) return;
			if (priv->hadjustment != NULL) g_object_unref (priv->hadjustment);
			if (adjustment == NULL) adjustment = gtk_adjustment_new (0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			priv->hadjustment = g_object_ref_sink (adjustment);
			g_object_notify (G_OBJECT (widget), "hadjustment");
			break;
		case PROP_VADJUSTMENT:
			adjustment = g_value_get_object (value);
			if (adjustment && priv->vadjustment == adjustment) return;
			if (priv->vadjustment != NULL) g_object_unref (priv->vadjustment);
			if (adjustment == NULL) adjustment = gtk_adjustment_new (0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			priv->vadjustment = g_object_ref_sink (adjustment);
			g_object_notify (G_OBJECT (widget), "vadjustment");
			break;
		case PROP_HSCROLL_POLICY:
			priv->hscroll_policy = g_value_get_enum (value);
			break;
		case PROP_VSCROLL_POLICY:
			priv->vscroll_policy = g_value_get_enum (value);
			break;
		default:
			G_OBJECT_WARN_INVALID_PROPERTY_ID (object, prop_id, pspec);
			break;
    }
}

static void swt_fixed_realize (GtkWidget *widget) {
	GtkAllocation allocation;
	GdkWindow *window;
	GdkWindowAttr attributes;
	gint attributes_mask;

 	if (!gtk_widget_get_has_window (widget)) {
    	GTK_WIDGET_CLASS (swt_fixed_parent_class)->realize (widget);
    	return;
    }
 
	gtk_widget_set_realized (widget, TRUE);

	gtk_widget_get_allocation (widget, &allocation);
	
	attributes.window_type = GDK_WINDOW_CHILD;
	attributes.x = allocation.x;
	attributes.y = allocation.y;
	attributes.width = allocation.width;
	attributes.height = allocation.height;
	attributes.wclass = GDK_INPUT_OUTPUT;
	attributes.visual = gtk_widget_get_visual (widget);
	attributes.event_mask = GDK_EXPOSURE_MASK | GDK_SCROLL_MASK | 1 << 23 /*GDK_SMOOTH_SCROLL_MASK*/ | gtk_widget_get_events (widget);
	attributes_mask = GDK_WA_X | GDK_WA_Y | GDK_WA_VISUAL;
	window = gdk_window_new (gtk_widget_get_parent_window (widget), &attributes, attributes_mask);
	gtk_widget_set_window (widget, window);
	gdk_window_set_user_data (window, widget);
	if (NULL != gtk_check_version (3, 18, 0)) {
		gtk_style_context_set_background (gtk_widget_get_style_context (widget), window);
	}
}

static void swt_fixed_map (GtkWidget *widget) {
	SwtFixed *fixed = SWT_FIXED (widget);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	
	gtk_widget_set_mapped (widget, TRUE);
	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		list = list->next;
		if (gtk_widget_get_visible (child)) {
			if (!gtk_widget_get_mapped (child)) gtk_widget_map (child);
		}
	}
	if (gtk_widget_get_has_window (widget)) {
		//NOTE: contrary to most of GTK, swt_fixed_* container does not raise windows upon showing them.
		//This has the effect that widgets are drawn *beneath* the previous one.
		//E.g if this line is changed to gdk_window_show (..) then widgets are drawn on top of the previous one.
		//This affects mostly only the absolute layout with overlapping widgets, e.g minimizied panels that
		//pop-out in Eclipse (aka fast-view).
		//As such, be attentive to swt_fixed_forall(..); traversing children may need to be done in reverse in some
		//cases.
		gdk_window_show_unraised (gtk_widget_get_window (widget));
	}
}

/* Accessibility */
static AtkObject *swt_fixed_get_accessible (GtkWidget *widget) {
	SwtFixed *fixed = SWT_FIXED (widget);

	if (!fixed->accessible) {
		fixed->accessible = swt_fixed_accessible_new (widget);
	}
	return fixed->accessible;
}

static void swt_fixed_get_preferred_width (GtkWidget *widget, gint *minimum, gint *natural) {
	if (minimum) *minimum = 0;
	if (natural) *natural = 0;
}

static void swt_fixed_get_preferred_height (GtkWidget *widget, gint *minimum, gint *natural) {
	if (minimum) *minimum = 0;
	if (natural) *natural = 0;
}

static void swt_fixed_size_allocate (GtkWidget *widget, GtkAllocation *allocation) {
	SwtFixed *fixed = SWT_FIXED (widget);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	GtkAllocation child_allocation;
	GtkRequisition requisition;
	gint w, h;

	gtk_widget_set_allocation (widget, allocation);

	if (gtk_widget_get_has_window (widget)) {
		if (gtk_widget_get_realized (widget)) {
			gdk_window_move_resize (gtk_widget_get_window (widget), allocation->x, allocation->y, allocation->width, allocation->height);
	    }
	}
	
	list = priv->children;

	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		list = list->next;
		
		child_allocation.x = child_data->x;
		child_allocation.y = child_data->y;
		if (!gtk_widget_get_has_window (widget)) {
          child_allocation.x += allocation->x;
          child_allocation.y += allocation->y;
        }

		w = child_data->width;
		h = child_data->height;
		if (w == -1 || h == -1) {
			gtk_widget_get_preferred_size (child, &requisition, NULL);
			if (w == -1) w = requisition.width;
			if (h == -1) h = requisition.height;
		}
		// Feature in GTK: gtk_widget_preferred_size() has to be called before
		// gtk_widget_size_allocate otherwise a warning is thrown. See Bug 486068.
		gtk_widget_get_preferred_size (child, &requisition, NULL);

		child_allocation.width = w;
		child_allocation.height = h;

		gtk_widget_size_allocate (child, &child_allocation);
    }
}

void swt_fixed_move (SwtFixed *fixed, GtkWidget *widget, gint x, gint y) {
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;

	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		if (child == widget) {
			child_data->x = x;
			child_data->y = y;
			break;
		}
		list = list->next;
	}
}

void swt_fixed_resize (SwtFixed *fixed, GtkWidget *widget, gint width, gint height) {
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;

	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		if (child == widget) {
			child_data->width = width;
			child_data->height = height;

			/*
			 * Feature in GTK: sometimes the sizing of child SwtFixed widgets
			 * does not happen quickly enough, causing miscalculations in SWT.
			 * Allocate the size of the child directly when swt_fixed_resize()
			 * is called. See bug 487160.
			 */
			GtkAllocation allocation, to_allocate;
			GtkRequisition req;
			gtk_widget_get_allocation(child, &allocation);

			// Keep x and y values the same to prevent misplaced containers
			to_allocate.x = allocation.x;
			to_allocate.y = allocation.y;
			to_allocate.width = width;
			to_allocate.height = height;

			// Call gtk_widget_get_preferred_size() and finish the allocation.
			gtk_widget_get_preferred_size (child, &req, NULL);
			gtk_widget_size_allocate(child, &to_allocate);
			break;
		}
		list = list->next;
	}
}

static void swt_fixed_add (GtkContainer *container, GtkWidget *child) {
	GtkWidget *widget = GTK_WIDGET (container);
	SwtFixed *fixed = SWT_FIXED (container);
	SwtFixedPrivate *priv = fixed->priv;
	SwtFixedChild *child_data;

	child_data = g_new (SwtFixedChild, 1);
	child_data->widget = child;
  	child_data->x = child_data->y = 0;
  	child_data->width = child_data->height = -1;
  
	priv->children = g_list_append (priv->children, child_data);
	gtk_widget_set_parent (child, widget);
}

static void swt_fixed_remove (GtkContainer *container, GtkWidget *widget) {
	SwtFixed *fixed = SWT_FIXED (container);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;

	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		if (child == widget) {
			gtk_widget_unparent (widget);
			priv->children = g_list_remove_link (priv->children, list);
			g_list_free_1 (list);
			g_free (child_data);
			break;
		}
		list = list->next;
	}
}

static void swt_fixed_forall (GtkContainer *container, gboolean include_internals, GtkCallback callback, gpointer callback_data) {
	SwtFixed *fixed = SWT_FIXED (container);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	
	list = priv->children;

	// NOTE: The direction of the list traversal is conditional.
	//
	// 1) When we do a *_foreach() traversal (i.e, include_internals==FALSE), we traverse the list as normal
	// from front to back.
	// This is used to layout higher level widgets inside containers (e.g row/grid etc..) in the expected way.
	// If for a non-internal traversal we were to go in reverse, then widgets would get laid out in inverse order.
	// 2) When we do a *_forall() traversal (i.e, include_internals==TRUE), we traverse the list in *reverse* order.
	// This is an internal traversal of the internals of a widget. Reverse traversal is necessary for things like
	// DnD Drop and DnD Motion events to find the correct widget in the case of overlapping  widgets on an absolute layout.
	// Reversal is required because in swt_fixed_map(..) we do not raise the widget when we show it, as a result
	// the stack is in reverse.
	if (include_internals)
			list = g_list_last(list);

	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;

		if (include_internals)
			list = list->prev;
		else
			list = list->next;

		(* callback) (child, callback_data);
	}
}


#endif
#ifndef NO_SwtFixedAccessible

static void swt_fixed_accessible_class_init (SwtFixedAccessibleClass *klass);
static void swt_fixed_accessible_finalize (GObject *object);
static void swt_fixed_accessible_initialize (AtkObject *obj, gpointer data);
static AtkAttributeSet *swt_fixed_accessible_get_attributes (AtkObject *obj);
static const gchar *swt_fixed_accessible_get_description (AtkObject *obj);
static gint swt_fixed_accessible_get_index_in_parent (AtkObject *obj);
static gint swt_fixed_accessible_get_n_children (AtkObject *obj);
static const gchar *swt_fixed_accessible_get_name (AtkObject *obj);
static AtkObject *swt_fixed_accessible_get_parent (AtkObject *obj);
static AtkRole swt_fixed_accessible_get_role (AtkObject *obj);
static AtkObject *swt_fixed_accessible_ref_child (AtkObject *obj, gint i);
static AtkStateSet *swt_fixed_accesssible_ref_state_set (AtkObject *accessible);
static void swt_fixed_accessible_action_iface_init (AtkActionIface *iface);
static void swt_fixed_accessible_hypertext_iface_init (AtkHypertextIface *iface);
static void swt_fixed_accessible_selection_iface_init (AtkSelectionIface *iface);
static void swt_fixed_accessible_text_iface_init (AtkTextIface *iface);

G_DEFINE_TYPE_WITH_CODE (SwtFixedAccessible, swt_fixed_accessible, GTK_TYPE_CONTAINER_ACCESSIBLE,
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_ACTION, swt_fixed_accessible_action_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_HYPERTEXT, swt_fixed_accessible_hypertext_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_SELECTION, swt_fixed_accessible_selection_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_TEXT, swt_fixed_accessible_text_iface_init))

struct _SwtFixedAccessiblePrivate {
	// A boolean flag which is set to TRUE when an Accessible Java
	// object has been created for this SwtFixedAccessible instance
	gboolean has_accessible;

	// The GtkWidget this SwtFixedAccessible instance maps to.
	GtkWidget *widget;
};

// Fully qualified Java class name for the Java implementation of ATK functions
const char *ACCESSIBILITY_CLASS_NAME = "org/eclipse/swt/accessibility/AccessibleObject";

static void swt_fixed_accessible_init (SwtFixedAccessible *accessible) {
	// Initialize the SwtFixedAccessiblePrivate struct
	accessible->priv = G_TYPE_INSTANCE_GET_PRIVATE (accessible, SWT_TYPE_FIXED_ACCESSIBLE, SwtFixedAccessiblePrivate);
}

static void swt_fixed_accessible_class_init (SwtFixedAccessibleClass *klass) {
	GObjectClass *object_class = G_OBJECT_CLASS (klass);
	AtkObjectClass *atk_class = ATK_OBJECT_CLASS (klass);

	// Override GObject functions
	object_class->finalize = swt_fixed_accessible_finalize;

	// Override AtkObject functions
	atk_class->initialize = swt_fixed_accessible_initialize;
	atk_class->get_attributes = swt_fixed_accessible_get_attributes;
	atk_class->get_description = swt_fixed_accessible_get_description;
	atk_class->get_index_in_parent = swt_fixed_accessible_get_index_in_parent;
	atk_class->get_n_children = swt_fixed_accessible_get_n_children;
	atk_class->get_name = swt_fixed_accessible_get_name;
	atk_class->get_parent = swt_fixed_accessible_get_parent;
	atk_class->get_role = swt_fixed_accessible_get_role;
	atk_class->ref_child = swt_fixed_accessible_ref_child;
	atk_class->ref_state_set = swt_fixed_accesssible_ref_state_set;

	g_type_class_add_private (klass, sizeof (SwtFixedAccessiblePrivate));
}

AtkObject *swt_fixed_accessible_new (GtkWidget *widget) {
	AtkObject *accessible;

	g_return_val_if_fail (SWT_IS_FIXED (widget), NULL);

	// Create the SwtFixedAccessible instance and call the initializer
	accessible = g_object_new (SWT_TYPE_FIXED_ACCESSIBLE, NULL);
	atk_object_initialize (accessible, widget);

	return accessible;
}

static void swt_fixed_accessible_finalize (GObject *object) {
	jintLong returned_value = 0;

	// Call the Java implementation to ensure AccessibleObjects are removed
	// from the HashMap on the Java side.
	returned_value = call_accessible_object_function("gObjectClass_finalize", "(J)J", object);
	if (returned_value != 0) g_critical ("Undefined behavior calling gObjectClass_finalize from C\n");

	// Chain up to the parent class
	G_OBJECT_CLASS (swt_fixed_accessible_parent_class)->finalize (object);
	return;
}

// This method is called from Java when an Accessible Java object that corresponds
// to this SwtFixedAccessible instance has been created.
void swt_fixed_accessible_register_accessible (AtkObject *obj, gboolean is_native, GtkWidget *to_map) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	private->has_accessible = TRUE;

	// TODO_a11y: implement support for native GTK widgets on the Java side,
	// some work might need to be done here.
	if (!is_native) {
		private->has_accessible = TRUE;
		gtk_accessible_set_widget (GTK_ACCESSIBLE (obj), private->widget);
	}

	return;
}

static void swt_fixed_accessible_initialize (AtkObject *obj, gpointer data) {
	// Call parent class initializer function
	if (ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->initialize != NULL) {
		ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->initialize (obj, data);
	}

	SwtFixedAccessiblePrivate *private = SWT_FIXED_ACCESSIBLE (obj)->priv;
	// If this SwtFixedAccessible instance has a corresponding Accessible
	// Java object created for it, then we can map it to its widget. Otherwise,
	// map it to NULL. This means that only widgets with an Accessible Java object
	// created get ATK function/interface implementations.
	if (private->has_accessible) {
		gtk_accessible_set_widget (GTK_ACCESSIBLE (obj), GTK_WIDGET (data));
	} else {
		gtk_accessible_set_widget (GTK_ACCESSIBLE (obj), NULL);
		private->widget = GTK_WIDGET (data);
	}
}

static AtkAttributeSet *swt_fixed_accessible_get_attributes (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_attributes", "(J)J", obj);
		return (AtkAttributeSet *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_attributes (obj);
	}
}

static const gchar *swt_fixed_accessible_get_description (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_description", "(J)J", obj);
		return (const gchar *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_description (obj);
	}
}

static gint swt_fixed_accessible_get_index_in_parent (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_index_in_parent", "(J)J", obj);
		return (gint) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_index_in_parent (obj);
	}
}

static gint swt_fixed_accessible_get_n_children (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_n_children", "(J)J", obj);
		return (gint) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_n_children (obj);
	}
}

static const gchar *swt_fixed_accessible_get_name (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_name", "(J)J", obj);
		return (const gchar *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_name (obj);
	}
}

static AtkObject *swt_fixed_accessible_get_parent (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_parent", "(J)J", obj);
		return (AtkObject *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_parent (obj);
	}
}

static AtkRole swt_fixed_accessible_get_role (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_get_role", "(J)J", obj);
		return returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->get_role (obj);
	}
}

static AtkObject *swt_fixed_accessible_ref_child (AtkObject *obj, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_ref_child", "(JJ)J", obj, i);
		return (AtkObject *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->ref_child (obj, i);
	}
}

static AtkStateSet *swt_fixed_accesssible_ref_state_set (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkObject_ref_state_set", "(J)J", obj);
		return (AtkStateSet *) returned_value;
	} else {
		return ATK_OBJECT_CLASS (swt_fixed_accessible_parent_class)->ref_state_set (obj);
	}
}

static gboolean swt_fixed_accessible_action_do_action (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_do_action", "(JJ)J", action, i);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static const gchar *swt_fixed_accessible_action_get_description (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_description", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static const gchar *swt_fixed_accessible_action_get_keybinding (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_keybinding", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static gint swt_fixed_accessible_action_get_n_actions (AtkAction *action) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_n_actions", "(J)J", action);
	}
	return (gint) returned_value;
}

static const gchar *swt_fixed_accessible_action_get_name (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_name", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static AtkHyperlink *swt_fixed_accessible_hypertext_get_link (AtkHypertext *hypertext, gint link_index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_link", "(JJ)J", hypertext, link_index);
	}
	return (AtkHyperlink *) returned_value;
}

static gint swt_fixed_accessible_hypertext_get_link_index (AtkHypertext *hypertext, gint char_index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_link_index", "(JJ)J", hypertext, char_index);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_hypertext_get_n_links (AtkHypertext *hypertext) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_n_links", "(J)J", hypertext);
	}
	return (gint) returned_value;
}

static gboolean swt_fixed_accessible_selection_is_child_selected (AtkSelection *selection, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (selection);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkSelection_is_child_selected", "(JJ)J", selection, i);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static AtkObject *swt_fixed_accessible_selection_ref_selection (AtkSelection *selection, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (selection);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jintLong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkSelection_ref_selection", "(JJ)J", selection, i);
	}
	return (AtkObject *) returned_value;
}

// Interfaces initializers and implementations
static void swt_fixed_accessible_action_iface_init (AtkActionIface *iface) {
	iface->do_action = swt_fixed_accessible_action_do_action;
	iface->get_description = swt_fixed_accessible_action_get_description;
	iface->get_keybinding = swt_fixed_accessible_action_get_keybinding;
	iface->get_n_actions = swt_fixed_accessible_action_get_n_actions;
	iface->get_name = swt_fixed_accessible_action_get_name;
}

static void swt_fixed_accessible_hypertext_iface_init (AtkHypertextIface *iface) {
	iface->get_link = swt_fixed_accessible_hypertext_get_link;
	iface->get_link_index = swt_fixed_accessible_hypertext_get_link_index;
	iface->get_n_links = swt_fixed_accessible_hypertext_get_n_links;
}

static void swt_fixed_accessible_selection_iface_init (AtkSelectionIface *iface) {
	iface->is_child_selected = swt_fixed_accessible_selection_is_child_selected;
	iface->ref_selection = swt_fixed_accessible_selection_ref_selection;
}

static void swt_fixed_accessible_text_iface_init (AtkTextIface *iface) {
	// TODO_a11y: add this interface to stop warnings from text signals.
	// Will be implemented later.
}

jintLong call_accessible_object_function (const char *method_name, const char *method_signature,...) {
	jintLong result = 0;
	va_list arg_list;
	jclass cls;
	JNIEnv *env;
	jmethodID mid;

	if (method_name == NULL || method_signature == NULL) {
		g_critical("Error calling Java method with JNI, check method name and signature\n");
		return 0;
	}

	// Get the JNIEnv pointer
	if ((*cached_jvm)->GetEnv(cached_jvm, (void **)&env, JNI_VERSION_1_2)) {
		g_critical("Error fetching the JNIEnv pointer\n");
		return 0;
	}

	// Find the class pointer
	cls = (*env)->FindClass(env, ACCESSIBILITY_CLASS_NAME);
	if (cls == NULL) {
		g_critical("JNI class pointer is NULL for class %s\n", ACCESSIBILITY_CLASS_NAME);
		return 0;
	}

	// Find the method ID
	mid = (*env)->GetStaticMethodID(env, cls, method_name, method_signature);

	// If the method ID isn't NULL
	if (mid == NULL) {
		g_critical("JNI method ID pointer is NULL for method %s\n", method_name);
	} else {
		va_start(arg_list, method_signature);
		result = (*env)->CallStaticLongMethodV(env, cls, mid, arg_list);
		va_end(arg_list);
	}

	return result;
}
#endif

//Add ability to debug gtk warnings for SWT snippets via SWT_FATAL_WARNINGS=1
// env variable. Please see Eclipse bug 471477
void swt_debug_on_fatal_warnings() {
	  // NOTE: gtk_parse_args() must be called before gtk_init() to take effect.
	  int argcount = 2;
	  char *argument[] = {"", "--g-fatal-warnings"};
	  char **arg2 = (char **) &argument;
	  gtk_parse_args(&argcount, &arg2);
}

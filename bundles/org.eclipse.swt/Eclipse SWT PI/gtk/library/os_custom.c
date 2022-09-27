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
static jlong superIMContextNewProc;
static GtkIMContext* lastIMContext;
static GtkIMContext* imContextNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	GtkIMContext* context = ((GtkIMContext * (*)(GType, guint, GObjectConstructParam *))superIMContextNewProc)(type, n_construct_properties, construct_properties);
	lastIMContext = context;
	return context;
}
#ifndef NO_imContextLast
JNIEXPORT jlong JNICALL OS_NATIVE(imContextLast)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, imContextLast_FUNC);
	rc = (jlong)lastIMContext;
	OS_NATIVE_EXIT(env, that, imContextLast_FUNC);
	return rc;
}
#endif

JNIEXPORT jlong JNICALL OS_NATIVE(imContextNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, imContextNewProc_1CALLBACK_FUNC);
	superIMContextNewProc = arg0;
	rc = (jlong)imContextNewProc;
	OS_NATIVE_EXIT(env, that, imContextNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO_pangoLayoutNewProc_1CALLBACK
static jlong superPangoLayoutNewProc;
static PangoLayout * pangoLayoutNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoLayout* layout = ((PangoLayout * (*)(GType, guint, GObjectConstructParam *))superPangoLayoutNewProc)(type, n_construct_properties, construct_properties);
	pango_layout_set_auto_dir (layout, 0);
	return layout;
}
JNIEXPORT jlong JNICALL OS_NATIVE(pangoLayoutNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoLayoutNewProc_1CALLBACK_FUNC);
	superPangoLayoutNewProc = arg0;
	rc = (jlong)pangoLayoutNewProc;
	OS_NATIVE_EXIT(env, that, pangoLayoutNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO_pangoFontFamilyNewProc_1CALLBACK
static jlong superPangoFontFamilyNewProc;
static PangoFontFamily * pangoFontFamilyNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoFontFamily* fontFamily = ((PangoFontFamily * (*)(GType, guint, GObjectConstructParam *))superPangoFontFamilyNewProc)(type, n_construct_properties, construct_properties);
	return fontFamily;
}
JNIEXPORT jlong JNICALL OS_NATIVE(pangoFontFamilyNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoFontFamilyNewProc_1CALLBACK_FUNC);
	superPangoFontFamilyNewProc = arg0;
	rc = (jlong)pangoFontFamilyNewProc;
	OS_NATIVE_EXIT(env, that, pangoFontFamilyNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif

#ifndef NO_pangoFontFaceNewProc_1CALLBACK
static jlong superPangoFontFaceNewProc;
static PangoFontFace * pangoFontFaceNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	PangoFontFace* fontFace = ((PangoFontFace * (*)(GType, guint, GObjectConstructParam *))superPangoFontFaceNewProc)(type, n_construct_properties, construct_properties);
	return fontFace;
}
JNIEXPORT jlong JNICALL OS_NATIVE(pangoFontFaceNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, pangoFontFaceNewProc_1CALLBACK_FUNC);
	superPangoFontFaceNewProc = arg0;
	rc = (jlong)pangoFontFaceNewProc;
	OS_NATIVE_EXIT(env, that, pangoFontFaceNewProc_1CALLBACK_FUNC);
	return rc;
}
#endif
#ifndef NO_printerOptionWidgetNewProc_1CALLBACK
static jlong superPrinterOptionWidgetNewProc;
static GType * printerOptionWidgetNewProc (GType type, guint n_construct_properties, GObjectConstructParam * construct_properties) {
	GType* printerOptionWidget = ((GType * (*)(GType, guint, GObjectConstructParam *))superPrinterOptionWidgetNewProc)(type, n_construct_properties, construct_properties);
	return printerOptionWidget;
}
JNIEXPORT jlong JNICALL OS_NATIVE(printerOptionWidgetNewProc_1CALLBACK)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, printerOptionWidgetNewProc_1CALLBACK_FUNC);
	superPrinterOptionWidgetNewProc = arg0;
	rc = (jlong)printerOptionWidgetNewProc;
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

#if !defined(GTK4)

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

G_DEFINE_TYPE_WITH_CODE (SwtFixed, swt_fixed, GTK_TYPE_CONTAINER,
		G_IMPLEMENT_INTERFACE (GTK_TYPE_SCROLLABLE, NULL)
		G_ADD_PRIVATE (SwtFixed))

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

	priv = widget->priv = swt_fixed_get_instance_private (widget);
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
	    G_GNUC_BEGIN_IGNORE_DEPRECATIONS
		gtk_style_context_set_background (gtk_widget_get_style_context (widget), window);
		G_GNUC_END_IGNORE_DEPRECATIONS
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

#else
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

enum {
  RESIZE,
  LAST_SIGNAL
};

static guint signals[LAST_SIGNAL];

static void swt_fixed_dispose(GObject* object);
static void swt_fixed_finalize(GObject* object);
static void swt_fixed_get_property (GObject *object, guint prop_id, GValue *value, GParamSpec *pspec);
static void swt_fixed_set_property (GObject *object, guint prop_id, const GValue *value, GParamSpec *pspec);

static void swt_fixed_measure (GtkWidget *widget, GtkOrientation  orientation, int for_size, int *minimum,
		int *natural, int *minimum_baseline, int *natural_baseline);
static void swt_fixed_size_allocate (GtkWidget *widget, int width, int height, int baseline);

G_DEFINE_TYPE_WITH_CODE (SwtFixed, swt_fixed, GTK_TYPE_WIDGET,
		G_IMPLEMENT_INTERFACE (GTK_TYPE_SCROLLABLE, NULL)
		G_ADD_PRIVATE (SwtFixed))

static void swt_fixed_class_init (SwtFixedClass *class) {
	/* GObject implementation */
	GObjectClass *gobject_class = (GObjectClass*) class;
	gobject_class->set_property = swt_fixed_set_property;
	gobject_class->get_property = swt_fixed_get_property;
	gobject_class->finalize = swt_fixed_finalize;
	gobject_class->dispose = swt_fixed_dispose;

	/* Scrollable implementation */
	g_object_class_override_property (gobject_class, PROP_HADJUSTMENT,    "hadjustment");
	g_object_class_override_property (gobject_class, PROP_VADJUSTMENT,    "vadjustment");
	g_object_class_override_property (gobject_class, PROP_HSCROLL_POLICY, "hscroll-policy");
	g_object_class_override_property (gobject_class, PROP_VSCROLL_POLICY, "vscroll-policy");

	/* Widget implementation */
	GtkWidgetClass* widget_class = (GtkWidgetClass*) class;
	widget_class->measure = swt_fixed_measure;
	widget_class->size_allocate = swt_fixed_size_allocate;

	signals[RESIZE] = g_signal_new(
			"resize",
			G_TYPE_FROM_CLASS (class),
			G_SIGNAL_RUN_LAST,
			0, NULL, NULL, NULL,
	        G_TYPE_NONE, 2, G_TYPE_INT, G_TYPE_INT);
}

void swt_fixed_restack (SwtFixed *fixed, GtkWidget *widget, GtkWidget *sibling, gboolean above) {
	SwtFixedPrivate* priv = swt_fixed_get_instance_private(fixed);
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
}

static void swt_fixed_init (SwtFixed* fixed) {
	SwtFixedPrivate* priv = swt_fixed_get_instance_private(fixed);

	priv->children = NULL;
	priv->hadjustment = NULL;
	priv->vadjustment = NULL;
}

static void swt_fixed_dispose(GObject* object) {
	GtkWidget* child;
	while ((child = gtk_widget_get_first_child(GTK_WIDGET(object)))) {
		swt_fixed_remove(SWT_FIXED(object), child);
	}

	G_OBJECT_CLASS(swt_fixed_parent_class)->dispose(object);
}

static void swt_fixed_finalize (GObject *object) {
	SwtFixedPrivate *priv = swt_fixed_get_instance_private(SWT_FIXED(object));

	g_object_unref(priv->hadjustment);
	g_object_unref(priv->vadjustment);

	G_OBJECT_CLASS(swt_fixed_parent_class)->finalize(object);
}

static void swt_fixed_get_property (GObject *object, guint prop_id, GValue *value, GParamSpec *pspec) {
	SwtFixedPrivate *priv = swt_fixed_get_instance_private(SWT_FIXED(object));

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
	SwtFixedPrivate *priv = swt_fixed_get_instance_private(SWT_FIXED(object));
	GtkAdjustment *adjustment;

	switch (prop_id) {
		case PROP_HADJUSTMENT:
			adjustment = g_value_get_object (value);
			if (adjustment && priv->hadjustment == adjustment) return;
			if (priv->hadjustment != NULL) g_object_unref (priv->hadjustment);
			if (adjustment == NULL) adjustment = gtk_adjustment_new (0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			priv->hadjustment = g_object_ref_sink (adjustment);
			g_object_notify (object, "hadjustment");
			break;
		case PROP_VADJUSTMENT:
			adjustment = g_value_get_object (value);
			if (adjustment && priv->vadjustment == adjustment) return;
			if (priv->vadjustment != NULL) g_object_unref (priv->vadjustment);
			if (adjustment == NULL) adjustment = gtk_adjustment_new (0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			priv->vadjustment = g_object_ref_sink (adjustment);
			g_object_notify (object, "vadjustment");
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

static void swt_fixed_measure (GtkWidget *widget, GtkOrientation  orientation, int for_size, int *minimum, int *natural, int *minimum_baseline, int *natural_baseline) {
	for (GtkWidget* child = gtk_widget_get_first_child(widget); child != NULL; child = gtk_widget_get_next_sibling(child)) {
		int child_nat = 0;

		gtk_widget_measure(child, orientation, -1, NULL, &child_nat, NULL, NULL);
		*natural = MAX(*natural, child_nat);
	}

	if (minimum) *minimum = 0;
	if (minimum_baseline) *minimum_baseline = -1;
	if (natural_baseline) *natural_baseline = -1;
}

static void swt_fixed_size_allocate (GtkWidget *widget, int width, int height, int baseline) {
	g_signal_emit (widget, signals[RESIZE], 0, width, height);

	SwtFixedPrivate *priv = swt_fixed_get_instance_private(SWT_FIXED(widget));

	GList* list = priv->children;

	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;

		GtkAllocation child_allocation;
		child_allocation.x = child_data->x;
		child_allocation.y = child_data->y;

		int w = child_data->width;
		int h = child_data->height;
		if (w == -1 || h == -1) {
			GtkRequisition requisition;
			gtk_widget_get_preferred_size (child, &requisition, NULL);
			if (w == -1) w = requisition.width;
			if (h == -1) h = requisition.height;
		}

		child_allocation.width = w;
		child_allocation.height = h;

		gtk_widget_size_allocate (child, &child_allocation, -1);

		list = list->next;
	}
}

void swt_fixed_move (SwtFixed *fixed, GtkWidget *widget, gint x, gint y) {
	SwtFixedPrivate* priv = swt_fixed_get_instance_private(fixed);
	GList* list = priv->children;

	while (list) {
		SwtFixedChild* child_data = list->data;
		GtkWidget* child = child_data->widget;

		if (child == widget) {
			child_data->x = x;
			child_data->y = y;
			break;
		}

		list = list->next;
	}
}

void swt_fixed_resize (SwtFixed *fixed, GtkWidget *widget, gint width, gint height) {
	SwtFixedPrivate* priv = swt_fixed_get_instance_private(fixed);
	GList* list = priv->children;

	while (list) {
		SwtFixedChild* child_data = list->data;
		GtkWidget* child = child_data->widget;

		if (child == widget) {
			child_data->width = width;
			child_data->height = height;
			break;
		}

		list = list->next;
	}
}

void swt_fixed_add (SwtFixed *fixed, GtkWidget *widget) {
	g_return_if_fail(SWT_IS_FIXED(fixed));
	g_return_if_fail(GTK_IS_WIDGET(widget));
	g_return_if_fail(gtk_widget_get_parent(widget) == NULL);

	SwtFixedPrivate *priv = swt_fixed_get_instance_private(fixed);

	SwtFixedChild *child_data = g_new(SwtFixedChild, 1);
	child_data->widget = widget;
  	child_data->x = child_data->y = 0;
  	child_data->width = child_data->height = -1;

	priv->children = g_list_append(priv->children, child_data);

	gtk_widget_set_parent(widget, GTK_WIDGET(fixed));
}

void swt_fixed_remove (SwtFixed *fixed, GtkWidget *widget) {
	g_return_if_fail(SWT_IS_FIXED(fixed));
	g_return_if_fail(GTK_IS_WIDGET(widget));
	g_return_if_fail(gtk_widget_get_parent(widget) == GTK_WIDGET(fixed));

	SwtFixedPrivate *priv = swt_fixed_get_instance_private(fixed);
	GList *list = priv->children;

	while (list != NULL) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;

		if (child == widget) {
			g_free(child_data);
			priv->children = g_list_delete_link(priv->children, list);

			gtk_widget_unparent(widget);
			break;
		}

		list = list->next;
	}
}

#endif

#if !defined(GTK4)
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
static void swt_fixed_accessible_component_iface_init (AtkComponentIface *iface);
static void swt_fixed_accessible_editable_text_iface_init (AtkEditableTextIface *iface);
static void swt_fixed_accessible_hypertext_iface_init (AtkHypertextIface *iface);
static void swt_fixed_accessible_selection_iface_init (AtkSelectionIface *iface);
static void swt_fixed_accessible_table_iface_init (AtkTableIface *iface);
static void swt_fixed_accessible_text_iface_init (AtkTextIface *iface);
static void swt_fixed_accessible_value_iface_init (AtkValueIface *iface);

struct _SwtFixedAccessiblePrivate {
	// A boolean flag which is set to TRUE when an Accessible Java
	// object has been created for this SwtFixedAccessible instance
	gboolean has_accessible;

	// The GtkWidget this SwtFixedAccessible instance maps to.
	GtkWidget *widget;
};

G_DEFINE_TYPE_WITH_CODE (SwtFixedAccessible, swt_fixed_accessible, GTK_TYPE_CONTAINER_ACCESSIBLE,
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_ACTION, swt_fixed_accessible_action_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_COMPONENT, swt_fixed_accessible_component_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_EDITABLE_TEXT, swt_fixed_accessible_editable_text_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_HYPERTEXT, swt_fixed_accessible_hypertext_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_SELECTION, swt_fixed_accessible_selection_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_TABLE, swt_fixed_accessible_table_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_TEXT, swt_fixed_accessible_text_iface_init)
			 G_IMPLEMENT_INTERFACE (ATK_TYPE_VALUE, swt_fixed_accessible_value_iface_init)
			 G_ADD_PRIVATE (SwtFixedAccessible))

// Fully qualified Java class name for the Java implementation of ATK functions
const char *ACCESSIBILITY_CLASS_NAME = "org/eclipse/swt/accessibility/AccessibleObject";

static void swt_fixed_accessible_init (SwtFixedAccessible *accessible) {
	// Initialize the SwtFixedAccessiblePrivate struct
	accessible->priv = swt_fixed_accessible_get_instance_private (accessible);
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

}

AtkObject *swt_fixed_accessible_new (GtkWidget *widget) {
	AtkObject *accessible;

	g_return_val_if_fail (SWT_IS_FIXED (widget), NULL);

	// Create the SwtFixedAccessible instance and call the initializer
	accessible = g_object_new (SWT_TYPE_FIXED_ACCESSIBLE, NULL);
	atk_object_initialize (accessible, widget);
	// Set the widget for this accessible, as not all SwtFixed instances
	// have a matching Java Accessible. See bug 536974.
	gtk_accessible_set_widget (GTK_ACCESSIBLE (accessible), widget);

	return accessible;
}

static void swt_fixed_accessible_finalize (GObject *object) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (object);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	// Call the Java implementation to ensure AccessibleObjects are removed
	// from the HashMap on the Java side.
	if (private->has_accessible) {
		returned_value = call_accessible_object_function("gObjectClass_finalize", "(J)J", object);
		if (returned_value != 0) g_critical ("Undefined behavior calling gObjectClass_finalize from C\n");
	}

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

	if (!is_native) {
		gtk_accessible_set_widget (GTK_ACCESSIBLE (obj), to_map);
		private->widget = to_map;
	} else {
		// TODO_a11y: implement support for native GTK widgets on the Java side,
		// some work might need to be done here.
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
	}
}

static AtkAttributeSet *swt_fixed_accessible_get_attributes (AtkObject *obj) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

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
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_do_action", "(JJ)J", action, i);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static const gchar *swt_fixed_accessible_action_get_description (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_description", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static const gchar *swt_fixed_accessible_action_get_keybinding (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_keybinding", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static gint swt_fixed_accessible_action_get_n_actions (AtkAction *action) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_n_actions", "(J)J", action);
	}
	return (gint) returned_value;
}

static const gchar *swt_fixed_accessible_action_get_name (AtkAction *action, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (action);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkAction_get_name", "(JJ)J", action, i);
	}
	return (const gchar *) returned_value;
}

static void swt_fixed_accessible_component_get_extents (AtkComponent *component, gint *x, gint *y,
		gint *width, gint *height, AtkCoordType coord_type) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (component);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	if (private->has_accessible) {
		call_accessible_object_function("atkComponent_get_extents", "(JJJJJJ)J", component, x, y,
			width, height, coord_type);
	} else {
		GtkWidget *widget = gtk_accessible_get_widget(GTK_ACCESSIBLE(fixed));
		gint fixed_x, fixed_y;
		GtkAllocation allocation;
		gtk_widget_get_allocation(widget, &allocation);
		#if defined(GTK4)
			GdkSurface *surface = gtk_widget_get_surface(widget);
			call_accessible_object_function("toDisplay", "(JJJ)J", surface, &fixed_x, &fixed_y);
			if (coord_type == ATK_XY_SCREEN) {
				*x = fixed_x;
				*y = fixed_y;
			}
			if (coord_type == ATK_XY_WINDOW) {
				GtkWidget *top = gtk_widget_get_toplevel(widget);
				GdkSurface *top_surface = gtk_widget_get_surface(top);
				gint top_x, top_y;
				call_accessible_object_function("toDisplay", "(JJJ)J", top_surface, &top_x, &top_y);
				*x = fixed_x - top_x;
				*y = fixed_y - top_y;
			}
		#else
			GdkWindow *window = gtk_widget_get_window(widget);
			call_accessible_object_function("toDisplay", "(JJJ)J", window, &fixed_x, &fixed_y);
			if (coord_type == ATK_XY_SCREEN) {
				*x = fixed_x;
				*y = fixed_y;
			}
			if (coord_type == ATK_XY_WINDOW) {
				GtkWidget *top = gtk_widget_get_toplevel(widget);
				GdkWindow *top_window = gtk_widget_get_window(top);
				gint top_x, top_y;
				call_accessible_object_function("toDisplay", "(JJJ)J", top_window, &top_x, &top_y);
				*x = fixed_x - top_x;
				*y = fixed_y - top_y;
			}
		#endif
		*width = allocation.width;
		*height = allocation.height;
	}
	return;
}

static AtkObject *swt_fixed_accessible_component_ref_accessible_at_point (AtkComponent *component, gint x,
		gint y, AtkCoordType coord_type) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (component);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkComponent_ref_accessible_at_point", "(JJJJ)J",
				component, x, y, coord_type);
	}
	return (AtkObject *) returned_value;
}

static void swt_fixed_accessible_editable_text_copy_text (AtkEditableText *text, gint start_pos, gint end_pos) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_copy_text", "(JJJ)J", text, start_pos, end_pos);
	}
	return;
}

static void swt_fixed_accessible_editable_text_cut_text (AtkEditableText *text, gint start_pos, gint end_pos) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_cut_text", "(JJJ)J", text, start_pos, end_pos);
	}
	return;
}

static void swt_fixed_accessible_editable_text_delete_text (AtkEditableText *text, gint start_pos, gint end_pos) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_delete_text", "(JJJ)J", text, start_pos, end_pos);
	}
	return;
}

static void swt_fixed_accessible_editable_text_insert_text (AtkEditableText *text, const gchar *string,
		gint length, gint *position) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_insert_text", "(JJJJ)J", text, string, length, position);
	}
	return;
}

static void swt_fixed_accessible_editable_text_paste_text (AtkEditableText *text, gint position) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_paste_text", "(JJ)J", text, position);
	}
	return;
}

static gboolean swt_fixed_accessible_editable_text_set_run_attributes (AtkEditableText *text,
		AtkAttributeSet *attrib_set, gint start_offset, gint end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkEditableText_set_run_attributes", "(JJJJ)J",
				attrib_set, start_offset, end_offset);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static void swt_fixed_accessible_editable_text_set_text_contents (AtkEditableText *text, const gchar *string) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkEditableText_set_text_contents", "(JJ)J", text, string);
	}
	return;
}

static AtkHyperlink *swt_fixed_accessible_hypertext_get_link (AtkHypertext *hypertext, gint link_index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_link", "(JJ)J", hypertext, link_index);
	}
	return (AtkHyperlink *) returned_value;
}

static gint swt_fixed_accessible_hypertext_get_link_index (AtkHypertext *hypertext, gint char_index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_link_index", "(JJ)J", hypertext, char_index);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_hypertext_get_n_links (AtkHypertext *hypertext) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (hypertext);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkHypertext_get_n_links", "(J)J", hypertext);
	}
	return (gint) returned_value;
}

static gboolean swt_fixed_accessible_selection_is_child_selected (AtkSelection *selection, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (selection);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkSelection_is_child_selected", "(JJ)J", selection, i);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static AtkObject *swt_fixed_accessible_selection_ref_selection (AtkSelection *selection, gint i) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (selection);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkSelection_ref_selection", "(JJ)J", selection, i);
	}
	return (AtkObject *) returned_value;
}

static AtkObject *swt_fixed_accessible_table_ref_at (AtkTable *table, gint row, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_ref_at", "(JJJ)J", table, row, column);
	}
	return (AtkObject *) returned_value;
}

static gint swt_fixed_accessible_table_get_index_at (AtkTable *table, gint row, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_index_at", "(JJJ)J", table, row, column);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_column_at_index (AtkTable *table, gint index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_column_at_index", "(JJ)J", table, index);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_row_at_index (AtkTable *table, gint index) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_row_at_index", "(JJ)J", table, index);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_n_columns (AtkTable *table) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_n_columns", "(J)J", table);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_n_rows (AtkTable *table) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_n_rows", "(J)J", table);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_column_extent_at (AtkTable *table, gint row, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_column_extent_at", "(JJJ)J",
			table, row, column);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_row_extent_at (AtkTable *table, gint row, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_row_extent_at", "(JJJ)J",
			table, row, column);
	}
	return (gint) returned_value;
}

static AtkObject *swt_fixed_accessible_table_get_caption (AtkTable *table) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_caption", "(J)J", table);
	}
	return (AtkObject *) returned_value;
}

static AtkObject *swt_fixed_accessible_table_get_summary (AtkTable *table) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_summary", "(J)J", table);
	}
	return (AtkObject *) returned_value;
}

static const gchar *swt_fixed_accessible_table_get_column_description (AtkTable *table, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_column_description", "(JJ)J",
			table, column);
	}
	return (const gchar *) returned_value;
}

static AtkObject *swt_fixed_accessible_table_get_column_header (AtkTable *table, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_column_header", "(JJ)J",
			table, column);
	}
	return (AtkObject *) returned_value;
}

static const gchar *swt_fixed_accessible_table_get_row_description (AtkTable *table, gint row) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_row_description", "(JJ)J",
			table, row);
	}
	return (const gchar *) returned_value;
}

static AtkObject *swt_fixed_accessible_table_get_row_header (AtkTable *table, gint row) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_row_header", "(JJ)J",
			table, row);
	}
	return (AtkObject *) returned_value;
}

static gint swt_fixed_accessible_table_get_selected_rows (AtkTable *table, gint **selected) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_selected_rows", "(JJ)J",
			table, selected);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_table_get_selected_columns (AtkTable *table, gint **selected) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_get_selected_columns", "(JJ)J",
			table, selected);
	}
	return (gint) returned_value;
}

static gboolean swt_fixed_accessible_table_is_column_selected (AtkTable *table, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_is_column_selected", "(JJ)J",
			table, column);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_is_row_selected (AtkTable *table, gint row) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_is_row_selected", "(JJ)J",
			table, row);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_is_selected (AtkTable *table, gint row, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_is_selected", "(JJJ)J",
			table, row, column);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_add_row_selection (AtkTable *table, gint row) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_add_row_selection", "(JJ)J",
			table, row);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_remove_row_selection (AtkTable *table, gint row) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_remove_row_selection", "(JJ)J",
			table, row);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_add_column_selection (AtkTable *table, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_add_column_selection", "(JJ)J",
			table, column);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_table_remove_column_selection (AtkTable *table, gint column) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (table);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkTable_remove_row_selection", "(JJ)J",
			table, column);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_text_add_selection (AtkText *text, gint start_offset, gint end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_add_selection", "(JJJ)J",
			text, start_offset, end_offset);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static AtkTextRange **swt_fixed_accessible_text_get_bounded_ranges (AtkText *text, AtkTextRectangle *rect,
		AtkCoordType coord_type, AtkTextClipType x_clip_type, AtkTextClipType y_clip_type) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_bounded_ranges", "(JJJJJ)J",
			text, rect, coord_type, x_clip_type, y_clip_type);
	}
	return (AtkTextRange **) returned_value;
}

static gint swt_fixed_accessible_text_get_caret_offset (AtkText *text) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_caret_offset", "(J)J", text);
	}
	return (gint) returned_value;
}

static gunichar swt_fixed_accessible_text_get_character_at_offset (AtkText *text, gint offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_character_at_offset", "(JJ)J", text, offset);
	}
	return (gunichar) returned_value;
}

static gint swt_fixed_accessible_text_get_character_count (AtkText *text) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_character_count", "(J)J", text);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_text_get_n_selections (AtkText *text) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_n_selections", "(J)J", text);
	}
	return (gint) returned_value;
}

static gint swt_fixed_accessible_text_get_offset_at_point (AtkText *text, gint x, gint y,
		AtkCoordType coords) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_offset_at_point", "(JJJJ)J", text, x, y, coords);
	}
	return (gint) returned_value;
}

static void swt_fixed_accessible_text_get_range_extents (AtkText *text, gint start_offset, gint end_offset,
		AtkCoordType coord_type, AtkTextRectangle *rect) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkText_get_range_extents", "(JJJJJ)J", text,
			start_offset, end_offset, coord_type, rect);
	}
	return;
}

static AtkAttributeSet *swt_fixed_accessible_text_get_run_attributes (AtkText *text, gint offset, gint *start_offset,
		gint *end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_run_attributes", "(JJJJ)J", text,
			offset, start_offset, end_offset);
	}
	return (AtkAttributeSet *) returned_value;
}

static gchar *swt_fixed_accessible_text_get_selection (AtkText *text, gint selection_num, gint *start_offset,
		gint *end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_selection", "(JJJJ)J", text,
			selection_num, start_offset, end_offset);
	}
	return (gchar *) returned_value;
}

static gchar *swt_fixed_accessible_text_get_text (AtkText *text, gint start_offset, gint end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_text", "(JJJ)J", text,
			start_offset, end_offset);
	}
	return (gchar *) returned_value;
}

static gchar *swt_fixed_accessible_text_get_text_after_offset (AtkText *text, gint offset,
		AtkTextBoundary boundary_type, gint *start_offset, gint *end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_text_after_offset", "(JJJJJ)J", text,
			offset, boundary_type, start_offset, end_offset);
	}
	return (gchar *) returned_value;
}

static gchar *swt_fixed_accessible_text_get_text_at_offset (AtkText *text, gint offset,
		AtkTextBoundary boundary_type, gint *start_offset, gint *end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_text_at_offset", "(JJJJJ)J", text,
			offset, boundary_type, start_offset, end_offset);
	}
	return (gchar *) returned_value;
}

static gchar *swt_fixed_accessible_text_get_text_before_offset (AtkText *text, gint offset,
		AtkTextBoundary boundary_type, gint *start_offset, gint *end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_get_text_before_offset", "(JJJJJ)J", text,
			offset, boundary_type, start_offset, end_offset);
	}
	return (gchar *) returned_value;
}

static gboolean swt_fixed_accessible_text_remove_selection (AtkText *text, gint selection_num) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_remove_selection", "(JJ)J", text, selection_num);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_text_set_caret_offset (AtkText *text, gint offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_set_caret_offset", "(JJ)J", text, offset);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static gboolean swt_fixed_accessible_text_set_selection (AtkText *text, gint selection_num,
		gint start_offset, gint end_offset) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (text);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkText_set_selection", "(JJJJ)J", text,
			selection_num, start_offset, end_offset);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

static void swt_fixed_accessible_value_get_current_value (AtkValue *obj, GValue *value) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkValue_get_current_value", "(JJ)J", obj, value);
	}
	return;
}

static void swt_fixed_accessible_value_get_maximum_value (AtkValue *obj, GValue *value) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkValue_get_maximum_value", "(JJ)J", obj, value);
	}
	return;
}

static void swt_fixed_accessible_value_get_minimum_value (AtkValue *obj, GValue *value) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;

	if (private->has_accessible) {
		call_accessible_object_function("atkValue_get_minimum_value", "(JJ)J", obj, value);
	}
	return;
}

static gboolean swt_fixed_accessible_value_set_current_value (AtkValue *obj, const GValue *value) {
	SwtFixedAccessible *fixed = SWT_FIXED_ACCESSIBLE (obj);
	SwtFixedAccessiblePrivate *private = fixed->priv;
	jlong returned_value = 0;

	if (private->has_accessible) {
		returned_value = call_accessible_object_function("atkValue_set_current_value", "(JJ)J", obj, value);
	}
	return ((gint) returned_value == 1) ? TRUE : FALSE;
}

// Interfaces initializers and implementations
static void swt_fixed_accessible_action_iface_init (AtkActionIface *iface) {
	iface->do_action = swt_fixed_accessible_action_do_action;
	iface->get_description = swt_fixed_accessible_action_get_description;
	iface->get_keybinding = swt_fixed_accessible_action_get_keybinding;
	iface->get_n_actions = swt_fixed_accessible_action_get_n_actions;
	iface->get_name = swt_fixed_accessible_action_get_name;
}

static void swt_fixed_accessible_component_iface_init (AtkComponentIface *iface) {
	iface->get_extents = swt_fixed_accessible_component_get_extents;
	iface->ref_accessible_at_point = swt_fixed_accessible_component_ref_accessible_at_point;
}

static void swt_fixed_accessible_editable_text_iface_init (AtkEditableTextIface *iface) {
	iface->copy_text = swt_fixed_accessible_editable_text_copy_text;
	iface->cut_text = swt_fixed_accessible_editable_text_cut_text;
	iface->delete_text = swt_fixed_accessible_editable_text_delete_text;
	iface->insert_text = swt_fixed_accessible_editable_text_insert_text;
	iface->paste_text = swt_fixed_accessible_editable_text_paste_text;
	iface->set_run_attributes = swt_fixed_accessible_editable_text_set_run_attributes;
	iface->set_text_contents = swt_fixed_accessible_editable_text_set_text_contents;
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

static void swt_fixed_accessible_table_iface_init (AtkTableIface *iface) {
	iface->ref_at = swt_fixed_accessible_table_ref_at;
	iface->get_index_at = swt_fixed_accessible_table_get_index_at;
	iface->get_column_at_index = swt_fixed_accessible_table_get_column_at_index;
	iface->get_row_at_index = swt_fixed_accessible_table_get_row_at_index;
	iface->get_n_columns = swt_fixed_accessible_table_get_n_columns;
	iface->get_n_rows = swt_fixed_accessible_table_get_n_rows;
	iface->get_column_extent_at = swt_fixed_accessible_table_get_column_extent_at;
	iface->get_row_extent_at = swt_fixed_accessible_table_get_row_extent_at;
	iface->get_caption = swt_fixed_accessible_table_get_caption;
	iface->get_summary = swt_fixed_accessible_table_get_summary;
	iface->get_column_description = swt_fixed_accessible_table_get_column_description;
	iface->get_column_header = swt_fixed_accessible_table_get_column_header;
	iface->get_row_description = swt_fixed_accessible_table_get_row_description;
	iface->get_row_header = swt_fixed_accessible_table_get_row_header;
	iface->get_selected_columns = swt_fixed_accessible_table_get_selected_columns;
	iface->get_selected_rows = swt_fixed_accessible_table_get_selected_rows;
	iface->is_column_selected = swt_fixed_accessible_table_is_column_selected;
	iface->is_row_selected = swt_fixed_accessible_table_is_row_selected;
	iface->is_selected = swt_fixed_accessible_table_is_selected;
	iface->add_row_selection = swt_fixed_accessible_table_add_row_selection;
	iface->remove_row_selection = swt_fixed_accessible_table_remove_row_selection;
	iface->add_column_selection = swt_fixed_accessible_table_add_column_selection;
	iface->remove_column_selection = swt_fixed_accessible_table_remove_column_selection;
}

static void swt_fixed_accessible_text_iface_init (AtkTextIface *iface) {
	iface->add_selection = swt_fixed_accessible_text_add_selection;
	iface->get_bounded_ranges = swt_fixed_accessible_text_get_bounded_ranges;
	iface->get_caret_offset = swt_fixed_accessible_text_get_caret_offset;
	iface->get_character_at_offset = swt_fixed_accessible_text_get_character_at_offset;
	iface->get_character_count = swt_fixed_accessible_text_get_character_count;
	iface->get_n_selections = swt_fixed_accessible_text_get_n_selections;
	iface->get_offset_at_point = swt_fixed_accessible_text_get_offset_at_point;
	iface->get_range_extents = swt_fixed_accessible_text_get_range_extents;
	iface->get_run_attributes = swt_fixed_accessible_text_get_run_attributes;
	iface->get_selection = swt_fixed_accessible_text_get_selection;
	// TODO_a11y: add support for get_string_at_offset once Orca is updated
	iface->get_text_before_offset = swt_fixed_accessible_text_get_text_before_offset;
	iface->get_text_at_offset = swt_fixed_accessible_text_get_text_at_offset;
	iface->get_text_after_offset = swt_fixed_accessible_text_get_text_after_offset;
	iface->get_text = swt_fixed_accessible_text_get_text;
	iface->remove_selection = swt_fixed_accessible_text_remove_selection;
	iface->set_caret_offset = swt_fixed_accessible_text_set_caret_offset;
	iface->set_selection = swt_fixed_accessible_text_set_selection;
}

static void swt_fixed_accessible_value_iface_init (AtkValueIface *iface) {
	/*
	 * TODO_a11y: add support for get_range, get_value_and_text, and set_value
	 * once Orca is updated.
	 */
	iface->get_current_value = swt_fixed_accessible_value_get_current_value;
	iface->get_maximum_value = swt_fixed_accessible_value_get_maximum_value;
	iface->get_minimum_value = swt_fixed_accessible_value_get_minimum_value;
	iface->set_current_value = swt_fixed_accessible_value_set_current_value;
}

jlong call_accessible_object_function (const char *method_name, const char *method_signature,...) {
	jlong result = 0;
	va_list arg_list;
	jclass cls;
	JNIEnv *env;
	jmethodID mid;

	if (method_name == NULL || method_signature == NULL) {
		g_critical("Error calling Java method with JNI, check method name and signature\n");
		return 0;
	}

	// Get the JNIEnv pointer
	if ((*JVM)->GetEnv(JVM, (void **)&env, JNI_VERSION_10)) {
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
		return 0;
	} else {
		va_start(arg_list, method_signature);
		result = (*env)->CallStaticLongMethodV(env, cls, mid, arg_list);
		va_end(arg_list);

		// JNI documentation says:
		//   The JNI functions that invoke a Java method return the result of
		//   the Java method. The programmer must call ExceptionOccurred() to
		//   check for possible exceptions that occurred during the execution
		//   of the Java method.
		if ((*env)->ExceptionCheck(env)) {
			g_critical("JNI method thrown exception: %s\n", method_name);
			// Note that this also clears the exception. That's good because
			// we don't want the unexpected exception to cause even more
			// problems in later JNI calls.
			(*env)->ExceptionDescribe(env);
			// Exceptions are not expected, but still, let's do at least
			// something to avoid possible confusion.
			result = 0;
		}
	}

	return result;
}

static GRecMutex swt_gdk_lock;

static void swt_threads_enter(void) {
	g_rec_mutex_lock(&swt_gdk_lock);
}

static void swt_threads_leave(void) {
	g_rec_mutex_unlock(&swt_gdk_lock);
}

void swt_set_lock_functions() {
	G_GNUC_BEGIN_IGNORE_DEPRECATIONS
	gdk_threads_set_lock_functions(&swt_threads_enter, &swt_threads_leave);
	G_GNUC_END_IGNORE_DEPRECATIONS
}

//Add ability to debug gtk warnings for SWT snippets via SWT_FATAL_WARNINGS=1
// env variable. Please see Eclipse bug 471477.
// PLEASE NOTE: this functionality is only available on GTK3.
void swt_debug_on_fatal_warnings() {
	  // NOTE: gtk_parse_args() must be called before gtk_init() to take effect.
	  int argcount = 2;
	  char *argument[] = {"", "--g-fatal-warnings"};
	  char **arg2 = (char **) &argument;
	  gtk_parse_args(&argcount, &arg2);
}
#endif

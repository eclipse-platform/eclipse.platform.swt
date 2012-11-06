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
	
//	fprintf(stdout, "here1 c=%ld %s\n", child->widget, g_type_name(G_OBJECT_TYPE(child->widget)));
//	fflush(stdout);
	
	
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
	gtk_style_context_set_background (gtk_widget_get_style_context (widget), window);
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
		gdk_window_show_unraised (gtk_widget_get_window (widget));
	}
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

		gtk_widget_get_preferred_size (child, &requisition, NULL);
		child_allocation.width = requisition.width;
		child_allocation.height = requisition.height;
		
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

static void swt_fixed_add (GtkContainer *container, GtkWidget *child) {
	GtkWidget *widget = GTK_WIDGET (container);
	SwtFixed *fixed = SWT_FIXED (container);
	SwtFixedPrivate *priv = fixed->priv;
	SwtFixedChild *child_data;

	child_data = g_new (SwtFixedChild, 1);
	child_data->widget = child;
  	child_data->x = child_data->y = 0;
  
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
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		list = list->next;
		(* callback) (child, callback_data);
	}
}

#endif

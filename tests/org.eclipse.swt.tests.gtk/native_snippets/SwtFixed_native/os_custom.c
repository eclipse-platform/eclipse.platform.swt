/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
#include "os_custom.h"

#ifndef NO_SwtFixed
struct _SwtFixedPrivate {
	GtkAdjustment *hadjustment;
	GtkAdjustment *vadjustment;
	guint hscroll_policy :1;
	guint vscroll_policy :1;
	GList *children;
};

struct _SwtFixedChild {
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

static void swt_fixed_get_property(GObject *object, guint prop_id,
		GValue *value, GParamSpec *pspec);
static void swt_fixed_set_property(GObject *object, guint prop_id,
		const GValue *value, GParamSpec *pspec);
static void swt_fixed_finalize(GObject *object);
static void swt_fixed_realize(GtkWidget *widget);
static void swt_fixed_map(GtkWidget *widget);
static void swt_fixed_get_preferred_width(GtkWidget *widget, gint *minimum,
		gint *natural);
static void swt_fixed_get_preferred_height(GtkWidget *widget, gint *minimum,
		gint *natural);
static void swt_fixed_size_allocate(GtkWidget *widget,
		GtkAllocation *allocation);
static void swt_fixed_add(GtkContainer *container, GtkWidget *widget);
static void swt_fixed_remove(GtkContainer *container, GtkWidget *widget);
static void swt_fixed_forall(GtkContainer *container,
		gboolean include_internals, GtkCallback callback,
		gpointer callback_data);

G_DEFINE_TYPE_WITH_CODE(SwtFixed, swt_fixed, GTK_TYPE_CONTAINER,
		G_IMPLEMENT_INTERFACE( GTK_TYPE_SCROLLABLE, NULL))

static void swt_fixed_class_init(SwtFixedClass *class) {
	GObjectClass *gobject_class = (GObjectClass*) class;
	GtkWidgetClass *widget_class = (GtkWidgetClass*) class;
	GtkContainerClass *container_class = (GtkContainerClass*) class;

	/* GOject implementation */
	gobject_class->set_property = swt_fixed_set_property;
	gobject_class->get_property = swt_fixed_get_property;
	gobject_class->finalize = swt_fixed_finalize;

	/* Scrollable implemetation */
	g_object_class_override_property(gobject_class, PROP_HADJUSTMENT,
			"hadjustment");
	g_object_class_override_property(gobject_class, PROP_VADJUSTMENT,
			"vadjustment");
	g_object_class_override_property(gobject_class, PROP_HSCROLL_POLICY,
			"hscroll-policy");
	g_object_class_override_property(gobject_class, PROP_VSCROLL_POLICY,
			"vscroll-policy");

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

	g_type_class_add_private(class, sizeof(SwtFixedPrivate));
}

void swt_fixed_restack(SwtFixed *fixed, GtkWidget *widget, GtkWidget *sibling,
		gboolean above) {
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	SwtFixedChild *child, *sibling_child;

	list = priv->children;
	while (list) {
		child = list->data;
		if (child->widget == widget)
			break;
		list = list->next;
	}

	if (!list)
		return;
	priv->children = g_list_remove_link(priv->children, list);
	g_list_free_1(list);

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
			if (!above)
				list = list->next;
		}
	}
	if (!list) {
		list = above ? priv->children : NULL;
	}
	priv->children = g_list_insert_before(priv->children, list, child);

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

GtkWidget *swt_fixed_new(void) {

	return GTK_WIDGET(g_object_new(SWT_TYPE_FIXED, NULL));
}

static void swt_fixed_init(SwtFixed *widget) {
	SwtFixedPrivate *priv;

	priv = widget->priv = G_TYPE_INSTANCE_GET_PRIVATE(widget, SWT_TYPE_FIXED,
			SwtFixedPrivate);
	priv->children = NULL;
	priv->hadjustment = NULL;
	priv->vadjustment = NULL;
}

static void swt_fixed_finalize(GObject *object) {
	SwtFixed *widget = SWT_FIXED(object);
	SwtFixedPrivate *priv = widget->priv;

	g_object_unref(priv->hadjustment);
	g_object_unref(priv->vadjustment);

	G_OBJECT_CLASS(swt_fixed_parent_class)->finalize(object);
}

static void swt_fixed_get_property(GObject *object, guint prop_id,
		GValue *value, GParamSpec *pspec) {
	SwtFixed *widget = SWT_FIXED(object);
	SwtFixedPrivate *priv = widget->priv;

	switch (prop_id) {
	case PROP_HADJUSTMENT:
		g_value_set_object(value, priv->hadjustment);
		break;
	case PROP_VADJUSTMENT:
		g_value_set_object(value, priv->vadjustment);
		break;
	case PROP_HSCROLL_POLICY:
		g_value_set_enum(value, priv->hscroll_policy);
		break;
	case PROP_VSCROLL_POLICY:
		g_value_set_enum(value, priv->vscroll_policy);
		break;
	default:
		G_OBJECT_WARN_INVALID_PROPERTY_ID(object, prop_id, pspec);
		break;
	}
}

static void swt_fixed_set_property(GObject *object, guint prop_id,
		const GValue *value, GParamSpec *pspec) {
	SwtFixed *widget = SWT_FIXED(object);
	SwtFixedPrivate *priv = widget->priv;
	GtkAdjustment *adjustment;

	switch (prop_id) {
	case PROP_HADJUSTMENT:
		adjustment = g_value_get_object(value);
		if (adjustment && priv->hadjustment == adjustment)
			return;
		if (priv->hadjustment != NULL)
			g_object_unref(priv->hadjustment);
		if (adjustment == NULL)
			adjustment = gtk_adjustment_new(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		priv->hadjustment = g_object_ref_sink(adjustment);
		g_object_notify(G_OBJECT(widget), "hadjustment");
		break;
	case PROP_VADJUSTMENT:
		adjustment = g_value_get_object(value);
		if (adjustment && priv->vadjustment == adjustment)
			return;
		if (priv->vadjustment != NULL)
			g_object_unref(priv->vadjustment);
		if (adjustment == NULL)
			adjustment = gtk_adjustment_new(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		priv->vadjustment = g_object_ref_sink(adjustment);
		g_object_notify(G_OBJECT(widget), "vadjustment");
		break;
	case PROP_HSCROLL_POLICY:
		priv->hscroll_policy = g_value_get_enum(value);
		break;
	case PROP_VSCROLL_POLICY:
		priv->vscroll_policy = g_value_get_enum(value);
		break;
	default:
		G_OBJECT_WARN_INVALID_PROPERTY_ID(object, prop_id, pspec);
		break;
	}
}

static void swt_fixed_realize(GtkWidget *widget) {
	GtkAllocation allocation;
	GdkWindow *window;
	GdkWindowAttr attributes;
	gint attributes_mask;

	if (!gtk_widget_get_has_window(widget)) {
		GTK_WIDGET_CLASS(swt_fixed_parent_class)->realize(widget);
		return;
	}

	gtk_widget_set_realized(widget, TRUE);

	gtk_widget_get_allocation(widget, &allocation);

	attributes.window_type = GDK_WINDOW_CHILD;
	attributes.x = allocation.x;
	attributes.y = allocation.y;
	attributes.width = allocation.width;
	attributes.height = allocation.height;
	attributes.wclass = GDK_INPUT_OUTPUT;
	attributes.visual = gtk_widget_get_visual(widget);
	attributes.event_mask = GDK_EXPOSURE_MASK | GDK_SCROLL_MASK | 1 << 23 /*GDK_SMOOTH_SCROLL_MASK*/
			| gtk_widget_get_events(widget);
	attributes_mask = GDK_WA_X | GDK_WA_Y | GDK_WA_VISUAL;
	window = gdk_window_new(gtk_widget_get_parent_window(widget), &attributes,
			attributes_mask);
	gtk_widget_set_window(widget, window);
	gdk_window_set_user_data(window, widget);
	if (NULL != gtk_check_version(3, 18, 0)) {
		gtk_style_context_set_background(gtk_widget_get_style_context(widget),
				window);
	}
}

static void swt_fixed_map(GtkWidget *widget) {
	SwtFixed *fixed = SWT_FIXED(widget);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;

	gtk_widget_set_mapped(widget, TRUE);
	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		list = list->next;
		if (gtk_widget_get_visible(child)) {
			if (!gtk_widget_get_mapped(child))
				gtk_widget_map(child);
		}
	}
	if (gtk_widget_get_has_window(widget)) {
		//NOTE: contrary to most of GTK, swt_fixed_* container does not raise windows upon showing them.
		//This has the effect that widgets are drawn *beneath* the previous one.
		//E.g if this line is changed to gdk_window_show (..) then widgets are drawn on top of the previous one.
		//This affects mostly only the absolute layout with overlapping widgets, e.g minimizied panels that
		//pop-out in Eclipse (aka fast-view).
		//As such, be attentive to swt_fixed_forall(..); traversing children may need to be done in reverse in some
		//cases.
		gdk_window_show_unraised(gtk_widget_get_window(widget));
	}
}

static void swt_fixed_get_preferred_width(GtkWidget *widget, gint *minimum,
		gint *natural) {
	if (minimum)
		*minimum = 0;
	if (natural)
		*natural = 0;
}

static void swt_fixed_get_preferred_height(GtkWidget *widget, gint *minimum,
		gint *natural) {
	if (minimum)
		*minimum = 0;
	if (natural)
		*natural = 0;
}

static void swt_fixed_size_allocate(GtkWidget *widget,
		GtkAllocation *allocation) {
	SwtFixed *fixed = SWT_FIXED(widget);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;
	GtkAllocation child_allocation;
	GtkRequisition requisition;
	gint w, h;

	gtk_widget_set_allocation(widget, allocation);

	if (gtk_widget_get_has_window(widget)) {
		if (gtk_widget_get_realized(widget)) {
			gdk_window_move_resize(gtk_widget_get_window(widget), allocation->x,
					allocation->y, allocation->width, allocation->height);
		}
	}

	list = priv->children;

	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		list = list->next;

		child_allocation.x = child_data->x;
		child_allocation.y = child_data->y;
		if (!gtk_widget_get_has_window(widget)) {
			child_allocation.x += allocation->x;
			child_allocation.y += allocation->y;
		}

		w = child_data->width;
		h = child_data->height;
		if (w == -1 || h == -1) {
			gtk_widget_get_preferred_size(child, &requisition, NULL);
			if (w == -1)
				w = requisition.width;
			if (h == -1)
				h = requisition.height;
		}
		// Feature in GTK: gtk_widget_preferred_size() has to be called before
		// gtk_widget_size_allocate otherwise a warning is thrown. See Bug 486068.
		gtk_widget_get_preferred_size(child, &requisition, NULL);

		child_allocation.width = w;
		child_allocation.height = h;

		gtk_widget_size_allocate(child, &child_allocation);
	}
}

void swt_fixed_move(SwtFixed *fixed, GtkWidget *widget, gint x, gint y) {
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

void swt_fixed_resize(SwtFixed *fixed, GtkWidget *widget, gint width,
		gint height) {
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
			gtk_widget_get_preferred_size(child, &req, NULL);
			gtk_widget_size_allocate(child, &to_allocate);
			break;
		}
		list = list->next;
	}
}

static void swt_fixed_add(GtkContainer *container, GtkWidget *child) {
	GtkWidget *widget = GTK_WIDGET(container);
	SwtFixed *fixed = SWT_FIXED(container);
	SwtFixedPrivate *priv = fixed->priv;
	SwtFixedChild *child_data;

	child_data = g_new(SwtFixedChild, 1);
	child_data->widget = child;
	child_data->x = child_data->y = 0;
	child_data->width = child_data->height = -1;

	priv->children = g_list_append(priv->children, child_data);
	gtk_widget_set_parent(child, widget);
}

static void swt_fixed_remove(GtkContainer *container, GtkWidget *widget) {
	SwtFixed *fixed = SWT_FIXED(container);
	SwtFixedPrivate *priv = fixed->priv;
	GList *list;

	list = priv->children;
	while (list) {
		SwtFixedChild *child_data = list->data;
		GtkWidget *child = child_data->widget;
		if (child == widget) {
			gtk_widget_unparent(widget);
			priv->children = g_list_remove_link(priv->children, list);
			g_list_free_1(list);
			g_free(child_data);
			break;
		}
		list = list->next;
	}
}

static void swt_fixed_forall(GtkContainer *container,
		gboolean include_internals, GtkCallback callback,
		gpointer callback_data) {
	SwtFixed *fixed = SWT_FIXED(container);
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

		(*callback)(child, callback_data);
	}
}
#endif

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

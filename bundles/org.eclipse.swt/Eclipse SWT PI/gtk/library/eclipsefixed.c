/* Copyright 2001, Havoc Pennington and others */

/* Contributors:
 *   Havoc Pennington      - the original version
 *   Boris Shingarov (OTI) - minor modifications for GTK2; JNI layer
 */

/* Derived from EclipseFixed in GTK+:
 * GTK - The GIMP Toolkit
 * Copyright (C) 1995-1997 Peter Mattis, Spencer Kimball and Josh MacDonald
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the GTK+ Team and others 1997-1999.  See the AUTHORS
 * file for a list of people on the GTK+ Team.  See the ChangeLog
 * files for a list of changes.  These files are distributed with
 * GTK+ at <A  HREF="ftp://ftp.gtk.org/pub/gtk/">ftp://ftp.gtk.org/pub/gtk/</A>. 
 */

#include "eclipsefixed.h"
#include "swt.h"

static void eclipse_fixed_class_init    (EclipseFixedClass    *klass);
static void eclipse_fixed_init          (EclipseFixed         *fixed);
static void eclipse_fixed_map           (GtkWidget        *widget);
static void eclipse_fixed_realize       (GtkWidget        *widget);
static void eclipse_fixed_size_request  (GtkWidget        *widget,
                                         GtkRequisition   *requisition);
static void eclipse_fixed_size_allocate (GtkWidget        *widget,
                                         GtkAllocation    *allocation);
static void eclipse_fixed_paint         (GtkWidget        *widget,
                                         GdkRectangle     *area);
static void eclipse_fixed_draw          (GtkWidget        *widget,
                                         GdkRectangle     *area);
static gint eclipse_fixed_expose        (GtkWidget        *widget,
                                         GdkEventExpose   *event);
static void eclipse_fixed_add           (GtkContainer     *container,
                                         GtkWidget        *widget);
static void eclipse_fixed_remove        (GtkContainer     *container,
                                         GtkWidget        *widget);
static void eclipse_fixed_forall        (GtkContainer     *container,
                                         gboolean 	       include_internals,
                                         GtkCallback       callback,
                                         gpointer          callback_data);
static GtkType eclipse_fixed_child_type (GtkContainer     *container);

static void move_gdk_window_above (GdkWindow *window,
                                   GdkWindow *sibling);

static void move_gdk_window_below (GdkWindow *window,
                                   GdkWindow *sibling);

static GtkContainerClass *parent_class = NULL;





GtkType
eclipse_fixed_get_type (void)
{
  static GtkType fixed_type = 0;

  if (!fixed_type)
    {
      static const GtkTypeInfo fixed_info =
      {
	"EclipseFixed",
	sizeof (EclipseFixed),
	sizeof (EclipseFixedClass),
	(GtkClassInitFunc) eclipse_fixed_class_init,
	(GtkObjectInitFunc) eclipse_fixed_init,
	/* reserved_1 */ NULL,
        /* reserved_2 */ NULL,
        (GtkClassInitFunc) NULL,
      };

      fixed_type = gtk_type_unique (GTK_TYPE_CONTAINER, &fixed_info);
    }

  return fixed_type;
}

static void
eclipse_fixed_class_init (EclipseFixedClass *class)
{
  GtkObjectClass *object_class;
  GtkWidgetClass *widget_class;
  GtkContainerClass *container_class;

  object_class = (GtkObjectClass*) class;
  widget_class = (GtkWidgetClass*) class;
  container_class = (GtkContainerClass*) class;

  parent_class = gtk_type_class (GTK_TYPE_CONTAINER);

  widget_class->realize = eclipse_fixed_realize;
  widget_class->size_request = eclipse_fixed_size_request;
  widget_class->size_allocate = eclipse_fixed_size_allocate;

  container_class->add = eclipse_fixed_add;
  container_class->remove = eclipse_fixed_remove;
  container_class->forall = eclipse_fixed_forall;
  container_class->child_type = eclipse_fixed_child_type;

/*  container_class->set_child_property = eclipse_fixed_set_child_property;
  container_class->get_child_property = eclipse_fixed_get_child_property;*/
}

static GtkType
eclipse_fixed_child_type (GtkContainer     *container)
{
  return GTK_TYPE_WIDGET;
}

static void
eclipse_fixed_init (EclipseFixed *fixed)
{
  GTK_WIDGET_SET_FLAGS (fixed, GTK_NO_WINDOW);

  fixed->children = NULL;
}

static void
eclipse_fixed_realize (GtkWidget *widget)
{
  GdkWindowAttr attributes;
  gint attributes_mask;

  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (widget));

  if (GTK_WIDGET_NO_WINDOW (widget))
    GTK_WIDGET_CLASS (parent_class)->realize (widget);
  else
    {
      GTK_WIDGET_SET_FLAGS (widget, GTK_REALIZED);

      attributes.window_type = GDK_WINDOW_CHILD;
      attributes.x = widget->allocation.x;
      attributes.y = widget->allocation.y;
      attributes.width = widget->allocation.width;
      attributes.height = widget->allocation.height;
      attributes.wclass = GDK_INPUT_OUTPUT;
      attributes.visual = gtk_widget_get_visual (widget);
      attributes.colormap = gtk_widget_get_colormap (widget);
      attributes.event_mask = gtk_widget_get_events (widget);
      attributes.event_mask |= GDK_EXPOSURE_MASK | GDK_BUTTON_PRESS_MASK;
      
      attributes_mask = GDK_WA_X | GDK_WA_Y | GDK_WA_VISUAL | GDK_WA_COLORMAP;
      
      widget->window = gdk_window_new (gtk_widget_get_parent_window (widget), &attributes, 
				       attributes_mask);
      gdk_window_set_user_data (widget->window, widget);
      
      widget->style = gtk_style_attach (widget->style, widget->window);
      gtk_style_set_background (widget->style, widget->window, GTK_STATE_NORMAL);
    }
}

GtkWidget*
eclipse_fixed_new (void)
{
  EclipseFixed *fixed;

  fixed = gtk_type_new (ECLIPSE_TYPE_FIXED);
  return GTK_WIDGET (fixed);
}

static EclipseFixedChild*
get_child_info (EclipseFixed *fixed,
                GtkWidget    *widget)
{
  GList *children;
  EclipseFixedChild *child_info;

  children = fixed->children;
  while (children)
    {
      child_info = children->data;
      children = children->next;

      if (child_info->widget == widget)
        return child_info;
    }

  return NULL;
}

void
eclipse_fixed_set_location (EclipseFixed *fixed,
                            GtkWidget *widget,
                            int x,
                            int y)
{
  EclipseFixedChild *child_info;

  g_return_if_fail (fixed != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (fixed));
  g_return_if_fail (widget != NULL);
  g_return_if_fail (widget->parent == GTK_WIDGET (fixed));
  
  child_info = get_child_info (fixed, widget);

  if (child_info->x == x && child_info->y == y)
    return; /* nothing to do */
  
  child_info->x = x;
  child_info->y = y;

  if (GTK_WIDGET_VISIBLE (fixed) && GTK_WIDGET_VISIBLE (widget))
    gtk_widget_queue_resize (GTK_WIDGET (fixed));
}

/* set size to -1 to use natural size */
void
eclipse_fixed_set_size (EclipseFixed *fixed,
                        GtkWidget *widget,
                        int        width,
                        int        height)
{
  EclipseFixedChild *child_info;

  g_return_if_fail (fixed != NULL);
if (!GTK_IS_ECLIPSE_FIXED (fixed)) { printf("hey, %s\n", -1); }
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (fixed));
  g_return_if_fail (widget != NULL);
  g_return_if_fail (widget->parent == GTK_WIDGET (fixed));
  
  child_info = get_child_info (fixed, widget);

  if (child_info->width == width && child_info->height == height)
    return; /* nothing to do */
  
  child_info->width = width;
  child_info->height = height;

  if (GTK_WIDGET_VISIBLE (fixed) && GTK_WIDGET_VISIBLE (widget))
    gtk_widget_queue_resize (GTK_WIDGET (fixed));
}


static GList*
find_link (EclipseFixed *fixed,
           GtkWidget    *widget)
{
  GList *tmp;

  tmp = fixed->children;

  while (tmp != NULL)
    {
      if (((EclipseFixedChild*)tmp->data)->widget == widget)
        return tmp;

      tmp = tmp->next;
    }

  return NULL;
}

static void
find_sibling_windows (EclipseFixed *fixed,
                      GtkWidget    *widget,
                      GdkWindow   **above,
                      GdkWindow   **below)
{
  GList *tmp;
  gboolean seen_ourselves;

  seen_ourselves = FALSE;

  if (below)
    *below = NULL;
  if (above)
    *above = NULL;

  tmp = fixed->children;
  while (tmp != NULL)
    {
      EclipseFixedChild *child = tmp->data;

      if (child->widget == widget)
        {
          seen_ourselves = TRUE;
        }
      else if (below && !seen_ourselves)
        {
          if (*below == NULL &&
              !GTK_WIDGET_NO_WINDOW (child->widget) &&
              GTK_WIDGET_REALIZED (child->widget))
            *below = child->widget->window;
        }
      else if (above && seen_ourselves)
        {
          if (*above == NULL &&
              !GTK_WIDGET_NO_WINDOW (child->widget) &&
              GTK_WIDGET_REALIZED (child->widget))
            *above = child->widget->window;
        }
      
      tmp = tmp->next;
    }
}

/* sibling == NULL means raise to top */
void
eclipse_fixed_move_above (EclipseFixed *fixed,
                          GtkWidget    *widget,
                          GtkWidget    *sibling)
{
  GList *sibling_link;
  GList *widget_link;
  
  g_return_if_fail (fixed != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (fixed));
  g_return_if_fail (widget != sibling);
  g_return_if_fail (widget->parent == GTK_WIDGET (fixed));
  g_return_if_fail (sibling == NULL || sibling->parent == GTK_WIDGET (fixed));
  
  sibling_link = find_link (fixed, sibling);  
  widget_link = find_link (fixed, widget);

  g_assert (widget_link);
  
  /* remove widget link */
  fixed->children = g_list_remove_link (fixed->children, widget_link);
  
  if (sibling_link)
    {
      GdkWindow *above, *below;
      
      widget_link->prev = sibling_link;
      widget_link->next = sibling_link->next;
      if (widget_link->next)
        widget_link->next->prev = widget_link;
      sibling_link->next = widget_link;

      if (!GTK_WIDGET_NO_WINDOW (widget) && GTK_WIDGET_REALIZED (widget))
        {
          find_sibling_windows (fixed, widget, &above, &below);
          if (below)
            move_gdk_window_above (widget->window, below);
          else if (above)
            move_gdk_window_below (widget->window, above);
        }
    }
  else
    {
      if (!GTK_WIDGET_NO_WINDOW (widget) && GTK_WIDGET_REALIZED (widget))
        gdk_window_raise (widget->window);
      
      fixed->children = g_list_append (fixed->children, widget_link->data);
      g_list_free_1 (widget_link);
    }

  /* Redraw no-window widgets, window widgets will do fine
   * on their own
   */
  if (GTK_WIDGET_NO_WINDOW (widget))
    gtk_widget_queue_draw (widget);
  if (sibling && GTK_WIDGET_NO_WINDOW (sibling))
    gtk_widget_queue_draw (sibling);
}

/* sibling == NULL means lower to bottom */
void
eclipse_fixed_move_below (EclipseFixed *fixed,
                          GtkWidget    *widget,
                          GtkWidget    *sibling)
{
  GList *sibling_link;
  GList *widget_link;
  
  g_return_if_fail (fixed != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (fixed));
  g_return_if_fail (widget != sibling);
  g_return_if_fail (widget->parent == GTK_WIDGET (fixed));
  g_return_if_fail (sibling == NULL || sibling->parent == GTK_WIDGET (fixed));
  
  sibling_link = find_link (fixed, sibling);  
  widget_link = find_link (fixed, widget);

  g_assert (widget_link);
  
  /* remove widget link */
  fixed->children = g_list_remove_link (fixed->children, widget_link);
  
  if (sibling_link)
    {
      GdkWindow *above, *below;
      
      widget_link->next = sibling_link;
      widget_link->prev = sibling_link->prev;
      if (widget_link->prev)
        widget_link->prev->next = widget_link;
      sibling_link->prev = widget_link;
      if (widget_link->prev == NULL)
        fixed->children = widget_link;

      if (!GTK_WIDGET_NO_WINDOW (widget) && GTK_WIDGET_REALIZED (widget))
        {
          find_sibling_windows (fixed, widget, &above, &below);
          if (above)
            move_gdk_window_below (widget->window, above);
          else if (below)
            move_gdk_window_above (widget->window, below);
        }
    }
  else
    {
      if (!GTK_WIDGET_NO_WINDOW (widget) & GTK_WIDGET_REALIZED (widget))
        gdk_window_lower (widget->window);      
      
      fixed->children = g_list_prepend (fixed->children, widget_link->data);
      g_list_free_1 (widget_link);
    }

  /* Redraw no-window widgets, window widgets will do fine
   * on their own
   */
  if (GTK_WIDGET_NO_WINDOW (widget))
    gtk_widget_queue_draw (widget);
  if (sibling && GTK_WIDGET_NO_WINDOW (sibling))
    gtk_widget_queue_draw (sibling);
}

static void
eclipse_fixed_map (GtkWidget *widget)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GList *children;

  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (widget));

  GTK_WIDGET_SET_FLAGS (widget, GTK_MAPPED);
  fixed = ECLIPSE_FIXED (widget);

  /* First we need to realize all children and fix their
   * stacking order, before we map them
   */
  children = fixed->children;
  while (children)
    {
      child = children->data;
      children = children->next;

      if (GTK_WIDGET_VISIBLE (child->widget) &&
	  !GTK_WIDGET_REALIZED (child->widget))
	gtk_widget_realize (child->widget);

      if (!GTK_WIDGET_NO_WINDOW (child->widget))
        gdk_window_raise (child->widget->window);
    }
  
  children = fixed->children;
  while (children)
    {
      child = children->data;
      children = children->next;

      if (GTK_WIDGET_VISIBLE (child->widget) &&
	  !GTK_WIDGET_MAPPED (child->widget))
	gtk_widget_map (child->widget);
    }
}

static void
eclipse_fixed_size_request (GtkWidget      *widget,
                            GtkRequisition *requisition)
{
  EclipseFixed *fixed;  
  EclipseFixedChild *child;
  GList *children;
  GtkRequisition child_requisition;

  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (widget));
  g_return_if_fail (requisition != NULL);

  fixed = ECLIPSE_FIXED (widget);
  requisition->width = 0;
  requisition->height = 0;

  children = fixed->children;
  while (children)
    {
      child = children->data;
      children = children->next;

      if (GTK_WIDGET_VISIBLE (child->widget))
	{
          int w, h;
          
          gtk_widget_size_request (child->widget, &child_requisition);

          w = child->width < 0 ? child_requisition.width : child->width;
          h = child->height < 0 ? child_requisition.height : child->height;
          
          requisition->height = MAX (requisition->height,
                                     child->y + h);
          requisition->width = MAX (requisition->width,
                                    child->x + w);
	}
    }

  requisition->height += GTK_CONTAINER (fixed)->border_width * 2;
  requisition->width += GTK_CONTAINER (fixed)->border_width * 2;
}

static void
eclipse_fixed_size_allocate (GtkWidget     *widget,
                             GtkAllocation *allocation)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GtkAllocation child_allocation;
  GtkRequisition child_requisition;
  GList *children;
  int border_width;

  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED(widget));
  g_return_if_fail (allocation != NULL);

  fixed = ECLIPSE_FIXED (widget);

  widget->allocation = *allocation;

  border_width = GTK_CONTAINER (fixed)->border_width;
  
  children = fixed->children;
  while (children)
    {
      child = children->data;
      children = children->next;
      
      if (GTK_WIDGET_VISIBLE (child->widget))
	{
	  gtk_widget_get_child_requisition (child->widget, &child_requisition);

	  child_allocation.x = child->x + border_width;
	  child_allocation.y = child->y + border_width;
	  child_allocation.width = child->width < 0 ? child_requisition.width : child->width;
	  child_allocation.height = child->height < 0 ? child_requisition.height : child->height;
	  gtk_widget_size_allocate (child->widget, &child_allocation);
	}
    }
}

static void
eclipse_fixed_paint (GtkWidget    *widget,
                     GdkRectangle *area)
{
  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (widget));
  g_return_if_fail (area != NULL);

  if (GTK_WIDGET_DRAWABLE (widget))
    gdk_window_clear_area (widget->window,
			   area->x, area->y,
			   area->width, area->height);
}

static void
eclipse_fixed_draw (GtkWidget    *widget,
                    GdkRectangle *area)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GdkRectangle child_area;
  GList *children;

  g_return_if_fail (widget != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (widget));

  /* Note that we draw from front of fixed->children to back
   * which means the stacking order is supposed to be that
   */
  if (GTK_WIDGET_DRAWABLE (widget))
    {
      fixed = ECLIPSE_FIXED (widget);
      eclipse_fixed_paint (widget, area);

      children = fixed->children;
      while (children)
	{
	  child = children->data;
	  children = children->next;

	  if (gtk_widget_intersect (child->widget, area, &child_area))
	    gtk_widget_draw (child->widget, &child_area);
	}
    }
}

static gint
eclipse_fixed_expose (GtkWidget      *widget,
                      GdkEventExpose *event)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GdkEventExpose child_event;
  GList *children;

  g_return_val_if_fail (widget != NULL, FALSE);
  g_return_val_if_fail (GTK_IS_ECLIPSE_FIXED (widget), FALSE);
  g_return_val_if_fail (event != NULL, FALSE);

  if (GTK_WIDGET_DRAWABLE (widget))
    {
      fixed = ECLIPSE_FIXED (widget);

      child_event = *event;

      children = fixed->children;
      while (children)
	{
	  child = children->data;
	  children = children->next;

	  if (GTK_WIDGET_NO_WINDOW (child->widget) &
	      gtk_widget_intersect (child->widget, &event->area, 
				    &child_event.area))
	    gtk_widget_event (child->widget, (GdkEvent*) &child_event);
	}
    }

  return FALSE;
}

static void
eclipse_fixed_add (GtkContainer *container,
                   GtkWidget    *widget)
{
  EclipseFixedChild *child_info;
  EclipseFixed *fixed;
  
  g_return_if_fail (container != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (container));
  g_return_if_fail (widget != NULL);

  fixed = ECLIPSE_FIXED (container);
  
  child_info = g_new (EclipseFixedChild, 1);
  child_info->widget = widget;
  child_info->x = 0;
  child_info->y = 0;
  child_info->width = -1;
  child_info->height = -1;
  
  gtk_widget_set_parent (widget, GTK_WIDGET (fixed));

  /* Add at top of stacking order (append) */
  fixed->children = g_list_append (fixed->children, child_info);
  
  if (GTK_WIDGET_REALIZED (fixed))
    gtk_widget_realize (widget);

  if (GTK_WIDGET_VISIBLE (fixed) && GTK_WIDGET_VISIBLE (widget))
    {
      if (GTK_WIDGET_MAPPED (fixed))
	gtk_widget_map (widget);
      
      gtk_widget_queue_resize (GTK_WIDGET (fixed));
    }
}

static void
eclipse_fixed_remove (GtkContainer *container,
                      GtkWidget    *widget)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GList *children;

  g_return_if_fail (container != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (container));
  g_return_if_fail (widget != NULL);

  fixed = ECLIPSE_FIXED (container);

  children = fixed->children;
  while (children)
    {
      child = children->data;

      if (child->widget == widget)
	{
	  gboolean was_visible = GTK_WIDGET_VISIBLE (widget);
	  
	  gtk_widget_unparent (widget);

	  fixed->children = g_list_remove_link (fixed->children, children);
	  g_list_free (children);
	  g_free (child);

	  if (was_visible && GTK_WIDGET_VISIBLE (container))
	    gtk_widget_queue_resize (GTK_WIDGET (container));

	  break;
	}

      children = children->next;
    }
}

static void
eclipse_fixed_forall (GtkContainer *container,
                      gboolean	include_internals,
                      GtkCallback   callback,
                      gpointer      callback_data)
{
  EclipseFixed *fixed;
  EclipseFixedChild *child;
  GList *children;

  g_return_if_fail (container != NULL);
  g_return_if_fail (GTK_IS_ECLIPSE_FIXED (container));
  g_return_if_fail (callback != NULL);

  fixed = ECLIPSE_FIXED (container);

  children = fixed->children;
  while (children)
    {
      child = children->data;
      children = children->next;

      (* callback) (child->widget, callback_data);
    }
}

#include <gdk/gdkx.h>

static void
move_gdk_window_above (GdkWindow *window,
                       GdkWindow *sibling)
{

  XWindowChanges changes;

  changes.sibling = GDK_WINDOW_XWINDOW (sibling);
  changes.stack_mode = Above;
                  
  XConfigureWindow (gdk_display,
                    GDK_WINDOW_XWINDOW (window),
                    CWSibling | CWStackMode,
                    &changes);
}

static void
move_gdk_window_below (GdkWindow *window,
                       GdkWindow *sibling)
{

  XWindowChanges changes;

  changes.sibling = GDK_WINDOW_XWINDOW (sibling);
  changes.stack_mode = Below;

  XConfigureWindow (gdk_display,
                    GDK_WINDOW_XWINDOW (window),
                    CWSibling | CWStackMode,
                    &changes);
}


#if 1
/* Test program
 * Compile with:
 * gcc `pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` -I/opt/IBMvame1.4/ive/bin/include eclipsefixed.c
 */

#include <gtk/gtk.h>

static void
raise_to_top (GtkWidget *button,
              GtkWidget *fixed)
{
  eclipse_fixed_move_above (ECLIPSE_FIXED (fixed), button, NULL);
}

int
main (int argc, char **argv)
{
  GtkWidget *window;
  GtkWidget *frame;
  GtkWidget *fixed;
  GtkWidget *button1;

  gtk_init (&argc, &argv);
  
  window = gtk_window_new (GTK_WINDOW_TOPLEVEL);

  frame = gtk_frame_new ("Frame");

  fixed = eclipse_fixed_new ();

  /* Add them in upside-down order */
  button1 = gtk_button_new_with_label ("On Top");
  gtk_container_add (GTK_CONTAINER (fixed), button1);
  
  eclipse_fixed_set_location (ECLIPSE_FIXED (fixed), button1, 5, 5);
  eclipse_fixed_set_size (ECLIPSE_FIXED (fixed), button1, 100, 30);

  /* Make clicking buttons restack them */
  gtk_signal_connect (GTK_OBJECT (button1), "clicked",
                      GTK_SIGNAL_FUNC (raise_to_top),
                      fixed);

  gtk_container_add (GTK_CONTAINER (window), frame);
  gtk_container_add (GTK_CONTAINER (frame), fixed);
 
  gtk_widget_show_all (window);

  gtk_signal_connect (GTK_OBJECT (window), "destroy",
                      GTK_SIGNAL_FUNC (gtk_main_quit),
                      NULL);
  
  gtk_main ();
  
  return 0;
}

#endif


/*
 * SWT JNI Exports
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1new
  (JNIEnv *env, jclass that)
{
	return (jint)eclipse_fixed_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1get_1location
  (JNIEnv *env, jclass that, jint fixed, jint child, jintArray loc)
{
  EclipseFixedChild *childInfo;
  jint *loc1 = NULL;
  if (loc) {
    loc1 = (*env)->GetIntArrayElements(env, loc, NULL);
    childInfo = get_child_info ((EclipseFixed*)fixed, (GtkWidget*) child);
    loc1[0] = childInfo->x;
    loc1[1] = childInfo->y;
    (*env)->ReleaseIntArrayElements(env, loc, loc1, 0);
  }
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1get_1size
  (JNIEnv *env, jclass that, jint fixed, jint child, jintArray sz)
{
  EclipseFixedChild *childInfo;
  jint *sz1 = NULL;
  if (sz) {
    sz1 = (*env)->GetIntArrayElements(env, sz, NULL);
    childInfo = get_child_info ((EclipseFixed*)fixed, (GtkWidget*) child);
    if (childInfo) {
      sz1[0] = childInfo->width;
      sz1[1] = childInfo->height;
    }
    (*env)->ReleaseIntArrayElements(env, sz, sz1, 0);
    return (jboolean)(childInfo != 0);
  }
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1set_1location
  (JNIEnv *env, jclass that, jint fixed, jint child, jint x, jint y)
{
  eclipse_fixed_set_location((EclipseFixed*)fixed, (GtkWidget*) child, x, y);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1set_1size
  (JNIEnv *env, jclass that, jint fixed, jint child, jint x, jint y)
{
  eclipse_fixed_set_size((EclipseFixed*)fixed, (GtkWidget*) child, x, y);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1move_1above
  (JNIEnv *env, jclass that, jint fixed, jint child, jint sibling)
{
  eclipse_fixed_move_above ((EclipseFixed*)fixed, (GtkWidget*) child, (GtkWidget*) sibling);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_eclipse_1fixed_1move_1below
  (JNIEnv *env, jclass that, jint fixed, jint child, jint sibling)
{
  eclipse_fixed_move_below ((EclipseFixed*)fixed, (GtkWidget*) child, (GtkWidget*) sibling);
}


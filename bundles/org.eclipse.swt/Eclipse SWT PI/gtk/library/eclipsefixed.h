/* Copyright 2001, Havoc Pennington and others */

/* Contributors:
 *   Havoc Pennington      - the original version
 *   Boris Shingarov (OTI) - minor modifications for GTK2; JNI layer
 */

/* Derived from GtkFixed from GTK+:
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

#include <gdk/gdk.h>
#include <gtk/gtkcontainer.h>

#define ECLIPSE_TYPE_FIXED                  (eclipse_fixed_get_type ())
#define ECLIPSE_FIXED(obj)                  (GTK_CHECK_CAST ((obj), ECLIPSE_TYPE_FIXED, EclipseFixed))
#define ECLIPSE_FIXED_CLASS(klass)          (GTK_CHECK_CLASS_CAST ((klass), ECLIPSE_TYPE_FIXED, EclipseFixedClass))
#define GTK_IS_ECLIPSE_FIXED(obj)               (GTK_CHECK_TYPE ((obj), ECLIPSE_TYPE_FIXED))
#define GTK_IS_FIXED_CLASS(klass)       (GTK_CHECK_CLASS_TYPE ((klass), ECLIPSE_TYPE_FIXED))

typedef struct _EclipseFixed        EclipseFixed;
typedef struct _EclipseFixedClass   EclipseFixedClass;
typedef struct _EclipseFixedChild   EclipseFixedChild;

struct _EclipseFixed
{
  GtkContainer container;

  GList *children;
};

struct _EclipseFixedClass
{
  GtkContainerClass parent_class;
};

struct _EclipseFixedChild
{
  GtkWidget *widget;
  int x;
  int y;
  int width;
  int height;
};

GtkType    eclipse_fixed_get_type          (void) G_GNUC_CONST;
GtkWidget* eclipse_fixed_new      (void);
void eclipse_fixed_set_location (EclipseFixed *fixed,
                                 GtkWidget    *widget,
                                 int           x,
                                 int           y);
void eclipse_fixed_set_size     (EclipseFixed *fixed,
                                 GtkWidget    *widget,
                                 int           width,
                                 int           height);
void eclipse_fixed_move_above   (EclipseFixed *fixed,
                                 GtkWidget    *widget,
                                 GtkWidget    *sibling);
void eclipse_fixed_move_below   (EclipseFixed *fixed,
                                 GtkWidget    *widget,
                                 GtkWidget    *sibling);

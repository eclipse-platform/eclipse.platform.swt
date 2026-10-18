/*******************************************************************************
 * Copyright (c) 2018, 2025 Red Hat and others. All rights reserved.
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

#include <gtk/gtk.h>
#include "os_custom.h"

static void
activate (GtkApplication *app,
          gpointer        user_data)
{

  GtkWidget *window;
  GtkWidget *button;
  GtkWidget *vbox;
  GtkWidget *fixed;
  GtkWidget *scrolled;

  window = gtk_application_window_new (app);
  gtk_window_set_title (GTK_WINDOW (window), "Window");
  gtk_window_set_default_size (GTK_WINDOW (window), 200, 200);

  vbox = gtk_box_new (GTK_ORIENTATION_VERTICAL, 0);
  scrolled = gtk_scrolled_window_new ();
  gtk_box_append (GTK_BOX(vbox), scrolled);

  fixed = g_object_new(swt_fixed_get_type(), 0);
  gtk_scrolled_window_set_child (GTK_SCROLLED_WINDOW(scrolled), fixed);


  button = gtk_button_new_with_label ("Hello");
  swt_fixed_add (SWT_FIXED (fixed), button);
  gtk_window_set_child (GTK_WINDOW (window), vbox);

  gtk_window_present (GTK_WINDOW(window));
}

int
main (int    argc,
      char **argv)
{

  GtkApplication *app;
  int status;

  app = gtk_application_new ("org.gtk.example", G_APPLICATION_DEFAULT_FLAGS);
  g_signal_connect (app, "activate", G_CALLBACK (activate), NULL);
  status = g_application_run (G_APPLICATION (app), argc, argv);
  g_object_unref (app);

  return status;
}

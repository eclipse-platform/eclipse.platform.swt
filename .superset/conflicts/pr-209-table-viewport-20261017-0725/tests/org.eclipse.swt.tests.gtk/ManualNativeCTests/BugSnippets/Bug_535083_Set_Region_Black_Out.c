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

#include <gtk/gtk.h>

static gboolean delete_event(GtkWidget*, GdkEvent*, gpointer);

static void move_cb(GtkWidget *button, gpointer data) {
	GtkWidget *window = (GtkWidget *) data;
	cairo_region_t *region;
	GdkRectangle rect;
	rect.x = 0;
	rect.y = 0;
	rect.width = 500;
	rect.height = 300;
	// gtk_window_move((GtkWindow *) window, 300, 300);
	region = cairo_region_create_rectangle(&rect);
	gtk_widget_shape_combine_region(window, region);
	gtk_window_resize(GTK_WINDOW(window), 1000, 599);

	// NO BLACK AREA IF SET OPACITY TO < 1
    // gtk_widget_set_opacity(window, 0.99);
}

int main(int argc, char *argv[]) {
	GtkWidget *window;
	GtkWidget *main_vbox;
	GtkWidget *move_button;

	gtk_init(&argc, &argv);

	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", G_CALLBACK(delete_event), NULL);
	gtk_window_resize(GTK_WINDOW(window), 1000, 600);

	main_vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(window), main_vbox);

	move_button = gtk_button_new_with_label("Set Region");
	gtk_box_pack_end(GTK_BOX(main_vbox), move_button, FALSE, TRUE, 0);
	g_signal_connect(move_button, "clicked", G_CALLBACK(move_cb), window);

	gtk_widget_show_all(window);
	gtk_main();
	return 0;
}

static gboolean delete_event(GtkWidget *widget, GdkEvent *event, gpointer data) {
	gtk_main_quit();
	return FALSE;
}



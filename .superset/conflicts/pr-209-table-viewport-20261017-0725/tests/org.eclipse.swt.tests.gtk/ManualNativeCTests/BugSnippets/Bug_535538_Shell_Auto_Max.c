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

int main(int argc, char *argv[]) {
	GtkWidget *window;
	gtk_init(&argc, &argv);

	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", G_CALLBACK(delete_event), NULL);

	gtk_window_resize((GtkWindow *) window, 1200, 600);

	gtk_window_move((GtkWindow *) window, 1700, 600);

	gtk_window_resize((GtkWindow *) window, 100, 100);

	//gtk_window_move((GtkWindow *) window, 1000, 600);

	gtk_widget_show_all(window);

	gtk_main();

	return 0;
}

static gboolean delete_event(GtkWidget *widget, GdkEvent *event, gpointer data) {
	gtk_main_quit();

	return FALSE;
}

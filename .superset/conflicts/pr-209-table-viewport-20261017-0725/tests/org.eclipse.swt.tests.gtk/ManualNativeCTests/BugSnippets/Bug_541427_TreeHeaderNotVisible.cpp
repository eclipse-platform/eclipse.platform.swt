/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
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
 *     Syntevo - initial implementation
 *******************************************************************************/
#include <gtk/gtk.h>
#include <stdio.h>

int main(int argc, char **argv) {
	// boilerplate
	gtk_init(&argc, &argv);
	GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", gtk_main_quit, NULL);
	gtk_window_resize(GTK_WINDOW(window), 200, 100);

	GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(window), vbox);

	// create trees
	for (int iHeightDiff = -5; iHeightDiff <= 5; iHeightDiff++) {
		// frame
		char buffer[256];
		sprintf(buffer, "Header %+d px", iHeightDiff);
		GtkWidget *frame = gtk_frame_new(buffer);
		gtk_box_pack_start(GTK_BOX(vbox), frame, FALSE, TRUE, 0);

		// treeview
		GtkTreeModel *model = GTK_TREE_MODEL(
				gtk_list_store_new(1, G_TYPE_STRING));
		GtkWidget *treeview = gtk_tree_view_new_with_model(model);
		g_object_unref(model);
		gtk_container_add(GTK_CONTAINER(frame), treeview);

		// some columns. 1 is already enough to demonstrate
		for (int iColumn = 0; iColumn < 3; iColumn++) {
			gtk_tree_view_insert_column_with_attributes(GTK_TREE_VIEW(treeview),
					-1, "Column", gtk_cell_renderer_text_new(), "text", iColumn,
					NULL);
		}

		// resize tree to match header's size
		GtkTreeViewColumn *column = gtk_tree_view_get_column(
				GTK_TREE_VIEW(treeview), 0);
		GtkWidget *columnButton = gtk_tree_view_column_get_button(column);
		GtkRequisition columnButtonSize;
		gtk_widget_get_preferred_size(columnButton, NULL, &columnButtonSize);
		gtk_widget_set_size_request(treeview, 100,
				columnButtonSize.height + iHeightDiff);
	}

	// boilerplate
	gtk_widget_show_all(window);
	gtk_main();
	return 0;
}


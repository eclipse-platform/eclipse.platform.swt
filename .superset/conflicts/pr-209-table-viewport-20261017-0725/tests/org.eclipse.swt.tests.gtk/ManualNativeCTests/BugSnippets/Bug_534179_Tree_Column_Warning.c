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

/*
 * Compile with:
 *  gcc -o out Bug_534179_Tree_Column_Warning.c `pkg-config --cflags --libs gtk+-3.0`
 *
 */

#include <gtk/gtk.h>

enum {
	COL_NAME = 0, COL_AGE, NUM_COLS
};

static GtkTreeModel *
create_and_fill_model(void) {
	GtkListStore *store;
	GtkTreeIter iter;

	store = gtk_list_store_new(NUM_COLS, G_TYPE_STRING, G_TYPE_UINT);

	/* Append a row and fill in some data */
	gtk_list_store_append(store, &iter);
	gtk_list_store_set(store, &iter, COL_NAME, "Heinz El-Mann", COL_AGE, 51,
			-1);

	/* append another row and fill in some data */
	gtk_list_store_append(store, &iter);
	gtk_list_store_set(store, &iter, COL_NAME, "Jane Doe", COL_AGE, 23, -1);

	/* ... and a third row */
	gtk_list_store_append(store, &iter);
	gtk_list_store_set(store, &iter, COL_NAME, "Joe Bungop", COL_AGE, 91, -1);

	return GTK_TREE_MODEL(store);
}

static GtkWidget *
create_view_and_model(void) {
	GtkCellRenderer *renderer;
	GtkTreeModel *model;
	GtkWidget *view;

	GtkTreeViewColumn *column;
	view = gtk_tree_view_new();

	/* --- Column #1 --- */

	renderer = gtk_cell_renderer_text_new();

	gtk_tree_view_insert_column_with_attributes(GTK_TREE_VIEW(view), -1,
			"Column 1", renderer, "text", COL_NAME, NULL);

	column = gtk_tree_view_column_new_with_attributes("Column 2", renderer,
			NULL);
	gtk_tree_view_insert_column(GTK_TREE_VIEW(view), column, 1);
	gtk_tree_view_column_set_resizable(column, 1);
	/* --- Column #2 --- */

	renderer = gtk_cell_renderer_text_new();
	gtk_tree_view_insert_column_with_attributes(GTK_TREE_VIEW(view), -1,
			"Column 3", renderer, "text", COL_AGE, NULL);

	model = create_and_fill_model();

	gtk_tree_view_set_model(GTK_TREE_VIEW(view), model);

	/* The tree view has acquired its own reference to the
	 *  model, so we can drop ours. That way the model will
	 *  be freed automatically when the tree view is destroyed */

	g_object_unref(model);

	return view;
}

int main(int argc, char **argv) {
	GtkWidget *window;
	GtkWidget *view;

	gtk_init(&argc, &argv);

	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", gtk_main_quit, NULL); /* dirty */

	view = create_view_and_model();

	gtk_container_add(GTK_CONTAINER(window), view);

	gtk_widget_show_all(window);

	gtk_main();

	return 0;
}

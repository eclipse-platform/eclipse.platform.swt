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

typedef struct AppData {
	GtkWidget *dynamic_label;
	GtkWidget *text_entry;
} AppData;

static gboolean delete_event(GtkWidget*, GdkEvent*, gpointer);

static void set_text_cb(GtkWidget *button, gpointer data) {
	GtkWidget *label = ((AppData *) data)->dynamic_label;
	GtkWidget *text = ((AppData *) data)->text_entry;
	gtk_label_set_text_with_mnemonic(GTK_LABEL(label),
			gtk_entry_get_text(GTK_ENTRY(text)));
}

int main(int argc, char *argv[]) {
	GtkWidget *window;
	GtkWidget *main_vbox, *box;
	GtkWidget *toolbar;
	GtkWidget *scroll_window;
	GtkToolItem *item1, *item2, *item3, *dynamic_item;
	GtkWidget *item1_label, *item2_label, *item3_label;
	GtkWidget *set_text_button;
//	GtkWidget *text_entry;
	gtk_init(&argc, &argv);

	AppData *app_data = g_new0(AppData, 1);

	/* Top-Level Window */
	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", G_CALLBACK(delete_event), NULL);

	/* Box for packing */
	main_vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(window), main_vbox);

	/* Scroll window inside box */
	scroll_window = gtk_scrolled_window_new(NULL, NULL);
	gtk_scrolled_window_set_min_content_height(
			GTK_SCROLLED_WINDOW(scroll_window), 300);
	gtk_box_pack_start(GTK_BOX(main_vbox), scroll_window, FALSE, FALSE, 0);

	/* Box inside scroll window for packing */
	box = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(scroll_window), box);

	/* Entry for setting text */
	app_data->text_entry = gtk_entry_new();
	gtk_box_pack_start(GTK_BOX(box), app_data->text_entry, FALSE, FALSE, 0);

	/* Button for setting text */
	set_text_button = gtk_button_new_with_label("Set Item Text");
	gtk_box_pack_start(GTK_BOX(box), set_text_button, FALSE, FALSE, 0);

	/* Toolbar */
	toolbar = gtk_toolbar_new();
	gtk_toolbar_set_style(GTK_TOOLBAR(toolbar), GTK_TOOLBAR_TEXT);

	item1 = gtk_tool_button_new(0, NULL);
	item1_label = gtk_label_new_with_mnemonic("ITEM 1");
	gtk_tool_button_set_label_widget(GTK_TOOL_BUTTON(item1), item1_label);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), item1, -1);

	item2 = gtk_tool_button_new(0, NULL);
	item2_label = gtk_label_new_with_mnemonic("ITEM 2");
	gtk_tool_button_set_label_widget(GTK_TOOL_BUTTON(item2), item2_label);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), item2, -1);

	item3 = gtk_tool_button_new(0, NULL);
	item3_label = gtk_label_new_with_mnemonic("ITEM 3");
	gtk_tool_button_set_label_widget(GTK_TOOL_BUTTON(item3), item3_label);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), item3, -1);

	dynamic_item = gtk_tool_button_new(0, NULL);
	app_data->dynamic_label = gtk_label_new_with_mnemonic("DYNAMIC");
	gtk_tool_button_set_label_widget(GTK_TOOL_BUTTON(dynamic_item),
			app_data->dynamic_label);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), dynamic_item, -1);

	gtk_box_pack_start(GTK_BOX(box), toolbar, FALSE, FALSE, 0);

	g_signal_connect(set_text_button, "clicked", G_CALLBACK(set_text_cb),
			app_data);

	gtk_window_set_default_size(GTK_WINDOW(window), 600, 500);

	gtk_widget_show_all(window);

	gtk_main();

	return 0;
}

static gboolean delete_event(GtkWidget *widget, GdkEvent *event, gpointer data) {
	gtk_main_quit();

	return FALSE;
}

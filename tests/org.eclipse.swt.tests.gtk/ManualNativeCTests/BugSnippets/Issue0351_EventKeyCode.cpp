/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

#include <gtk/gtk.h>

static gboolean onKeyPressEvent(GtkWidget* widget, GdkEventKey* event)
{
	// Ignore modifier presses to reduce output clutter
	if (event->is_modifier)
		return FALSE;

  	guint keyval;
  	gint  effective_group;
  	gint  level;
  	GdkModifierType consumed_modifiers;

	gboolean isTranslated = gdk_keymap_translate_keyboard_state(
		gdk_keymap_get_for_display(gdk_screen_get_display(gdk_window_get_screen(event->window))),
		event->hardware_keycode,
		(GdkModifierType)event->state,
		event->group,
		&keyval,
		&effective_group,
		&level,
		&consumed_modifiers
	);

	if (!isTranslated)
	{
		printf("gdk_keymap_translate_keyboard_state failed\n");
		return FALSE;
	}

	printf(
		"hardware_keycode=%02X group=%d state=%04X keyval=%04X | group=%d level=%d consumed=%04X keyval=%04X\n",
		event->hardware_keycode,
		event->group,
		event->state,
		event->keyval,
		effective_group,
		level,
		consumed_modifiers,
		keyval
	);

	return FALSE;
}

int main (int argc, char **argv)
{
	gtk_init(&argc, &argv);

	GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", gtk_main_quit, NULL);
	g_signal_connect(window, "key-press-event", G_CALLBACK(onKeyPressEvent), NULL);
	gtk_window_resize(GTK_WINDOW(window), 100, 100);

	// boilerplate
	gtk_widget_show_all(window);
	gtk_main();
	return 0;
}

/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


//import org.eclipse.swt.internal.gtk.OS;

/*
 * NOTE: this snippet requires OS.ascii() to be public in order to function.
 * Please modify its api.visibility and uncomment the rest of the snippet.
 */

public class Bug493552_NativeTestGDK {

	
	public static void main(String[] args) {
//		OS.gtk_init_check(new long[0], new long[0]);
//	      /* create a new window */
//	      long window = OS.gtk_window_new(OS.GTK_WINDOW_TOPLEVEL);
//	      OS.gtk_window_set_title(window, OS.ascii("GTK Menu Test"));
//	      long widget.menu = OS.gtk_menu_new();
//	      long root_menu = OS.gtk_image_menu_item_new_with_label(OS.ascii("Root Menu"));
//	      OS.gtk_widget_show(root_menu);
//	      for(int i = 0; i < 3; i++) {
//	        String buf = "Test-undermenu -" + i;
//	        long menu_items = OS.gtk_image_menu_item_new_with_label(OS.ascii(buf));
//	        OS.gtk_menu_shell_insert(widget.menu, menu_items, i);
//	        OS.gtk_widget_show(menu_items);
//	      }
//	      /* Now we specify that we want our newly created "widget.menu" to be the widget.menu for the "root widget.menu" */
//	      OS.gtk_menu_item_set_submenu(root_menu, widget.menu);
//	      /* Create a widget.menu-bar to hold the menus and add it to our main window*/
//	      long menu_bar = OS.gtk_menu_bar_new();
//	      OS.gtk_container_add(window, menu_bar);
//	      OS.gtk_widget_show(menu_bar);
//	      OS.gtk_menu_shell_insert(menu_bar, root_menu, 0);
//	      OS.gtk_widget_show(window);
//	      long /*int*/ screen = OS.gdk_screen_get_default ();
////	      int monitorNumber = OS.gdk_screen_get_monitor_at_window (screen, OS.gtk_widget_get_window(window));
//	      int monitorNumber = OS.gdk_screen_get_primary_monitor (screen);
//	      System.out.println("Primary monitorNumber " + monitorNumber);
//	      OS.gtk_main ();
	}
	
	
}

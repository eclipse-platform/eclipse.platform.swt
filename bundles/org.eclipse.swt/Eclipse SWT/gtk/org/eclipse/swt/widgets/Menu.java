/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Menu extends Widget {
	int x, y;
	boolean hasLocation;
	MenuItem cascade, selectedItem;
	Decorations parent;
	ImageList imageList;
	int poppedUpCount;
	long menuHandle;

	/** GTK4 only fields */
	long modelHandle, actionGroup, shortcutController;

	class Section {
		LinkedList<MenuItem> sectionItems;
		private MenuItem separator;
		private long sectionHandle;

		public Section() {
			this.sectionItems = new LinkedList<>();
		}

		public Section(MenuItem separator) {
			this();
			this.separator = separator;
		}

		public Section(long sectionHandle) {
			this();

			this.sectionHandle = sectionHandle;
		}

		public long getSectionHandle() {
			return sectionHandle != 0 ? sectionHandle : separator.modelHandle;
		}

		public int getItemPosition(MenuItem item) {
			return sectionItems.indexOf(item);
		}

		public int getSectionSize() {
			return sectionItems.size();
		}

		public int getSectionPosition() {
			return items.indexOf(separator);
		}
	}

	LinkedList<Section> sections;
	LinkedList<MenuItem> items;

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
 * <p>
 * After constructing a menu, it can be set into its parent
 * using <code>parent.setMenu(menu)</code>.  In this case, the parent may
 * be any control in the same widget tree as the parent.
 * </p>
 *
 * @param parent a control which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#POP_UP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Control parent) {
	this (checkNull (parent).menuShell (), SWT.POP_UP);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Decorations</code>) and a style value
 * describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * After constructing a menu or menuBar, it can be set into its parent
 * using <code>parent.setMenu(menu)</code> or <code>parent.setMenuBar(menuBar)</code>.
 * </p>
 *
 * @param parent a decorations control which will be the parent of the new instance (cannot be null)
 * @param style the style of menu to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BAR
 * @see SWT#DROP_DOWN
 * @see SWT#POP_UP
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentMenu
 * using <code>parentMenu.setMenu(menu)</code>.
 * </p>
 *
 * @param parentMenu a menu which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>MenuItem</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent menu.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentItem
 * using <code>parentItem.setMenu(menu)</code>.
 * </p>
 *
 * @param parentItem a menu item which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (MenuItem parentItem) {
	this (checkNull (parentItem).parent);
}

static Control checkNull (Control control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

static Menu checkNull (Menu menu) {
	if (menu == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return menu;
}

static MenuItem checkNull (MenuItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

/**
 * Bug 532074: setLocation method is limited on Wayland since Wayland
 * has no global coordinates and we cannot use gdk_popup_at_rect if GdkWindow of
 * getShell is not mapped. In this case, we can only pop the menu at the pointer.
 *
 * This happens for example when Problems view is a fast view on Eclipse start,
 * the drop down menu takes PartRenderingEngine's limbo shell
 * (see PartRenderingEngine#safeCreateGui) which is off the screen.
 *
 * @return true iff the location of menu is set and can be used successfully
 */
boolean ableToSetLocation() {
	if (!OS.isX11() && !getShell().getVisible()) {
		return false;
	}
	return hasLocation;
}

void _setVisible (boolean visible) {
	if (visible == GTK.gtk_widget_get_mapped (handle)) return;
	if (visible) {
		/*
		 * Feature in GTK. When a menu with no items is shown, GTK shows a
		 * weird small rectangle. For comparison, Windows API prevents showing
		 * empty menus on its own. At the same time, Eclipse creates menu items
		 * lazily in `SWT.Show`, so we can't know if menu is empty before
		 * `SWT.Show` is sent.
		 * Known solutions all have drawbacks:
		 * 1) Send `SWT.Show` here, instead of sending it when GtkMenu
		 *    receives 'show' event (see gtk_show()). This allows us to test if
		 *    menu has items before asking GTK to show it. The drawback is that
		 *    when GTK fails to show menu (see Bug 564595), application still
		 *    receives fake `SWT.Show` without a matching `SWT.Hide`.
		 * 2) Send SWT.Show` from gtk_show(). This solves drawback of (1).
		 *    The new drawback is that empty menu will now show, because it's
		 *    hard to stop menu from showing at this point.
		 * 3) Rework Eclipse (and possibly other SWT users) to stop depending
		 *    on `SWT.Show` and use some other event for lazy init. This will
		 *    allow to know if menu is empty in advance. This sounds like the
		 *    best solution, because it solves core problem: empty menus
		 *    shouldn't try to show. The drawback is that it will be a breaking
		 *    change.
		 * Solution (1) has been there since 2002-05-29.
		 */
		sendEvent (SWT.Show);
		if (getItemCount () != 0) {
			/*
			* Feature in GTK. ON_TOP shells will send out
			* SWT.Deactivate whenever a context menu is shown.
			* The fix is to prevent the menu from taking focus
			* when it is being shown in an ON_TOP shell.
			*/
			if ((parent._getShell ().style & SWT.ON_TOP) != 0) {
				if (GTK.GTK4) {
					/* TODO: Behavior of menu is unknown at the moment. After compilation
					 * testing will be done to see if this type of fix is required.
					 */
				} else {
					GTK3.gtk_menu_shell_set_take_focus (handle, false);
				}
			}
			long eventPtr = 0;
			if (ableToSetLocation()) {
				if (GTK.GTK4) {
					GdkRectangle popoverPosition = new GdkRectangle();
					popoverPosition.x = x;
					popoverPosition.y = y;
					popoverPosition.width = popoverPosition.height = 1;
					GTK.gtk_popover_set_pointing_to(handle, popoverPosition);

					GTK.gtk_popover_popup(handle);
				} else {
					// Create the GdkEvent manually as we need to control
					// certain fields like the event window
					eventPtr = GDK.gdk_event_new(GDK.GDK_BUTTON_PRESS);
					GdkEventButton event = new GdkEventButton();
					event.type = GDK.GDK_BUTTON_PRESS;
					event.device = GDK.gdk_get_pointer(GDK.gdk_display_get_default());
					event.time = display.getLastEventTime();

					// Create the rectangle relative to the parent (in this case, global) GdkWindow
					GdkRectangle rect = new GdkRectangle();
					if (OS.isX11()) {
						// Get and (add reference to) the global GdkWindow/GdkSurface
						event.window = GDK.gdk_display_get_default_group(GDK.gdk_display_get_default());
						OS.g_object_ref(event.window);
						GTK3.memmove (eventPtr, event, GdkEventButton.sizeof);

						/*
						 * Get the origin of the global GdkWindow/GdkSurface to calculate the size of any offsets
						 * such as client side decorations, or the system tray.
						 */
						int [] globalWindowOriginY = new int [1];
						int [] globalWindowOriginX = new int [1];
						GDK.gdk_window_get_origin (event.window, globalWindowOriginX, globalWindowOriginY);
						rect.x = this.x - globalWindowOriginX[0];
						rect.y = this.y - globalWindowOriginY[0];
					} else {
						// On Wayland, get the relative GdkWindow from the parent shell.
						long gdkResource = GTK3.gtk_widget_get_window (getShell().topHandle());
						event.window = OS.g_object_ref(gdkResource);
						GTK3.memmove (eventPtr, event, GdkEventButton.sizeof);
						// Bug in GTK?: testing with SWT_MENU_LOCATION_DEBUGGING=1 shows final_rect.x and
						// final_rect.y popup menu position is off by 1 compared to this.x and this.y
						rect.x = this.x + 1;
						rect.y = this.y + 1;
					}
					// Popup the menu and pin it at the top left corner of the GdkRectangle relative to the GdkWindow
					GTK3.gtk_menu_popup_at_rect(handle, event.window, rect, GDK.GDK_GRAVITY_NORTH_WEST,
							GDK.GDK_GRAVITY_NORTH_WEST, eventPtr);
					gdk_event_free (eventPtr);
				}
			} else {
				/*
				 *  GTK Feature: gtk_menu_popup is deprecated as of GTK3.22 and the new method gtk_menu_popup_at_pointer
				 *  requires an event to hook on to. This requires the popup & events related to the menu be handled
				 *  immediately and not as a post event in display, requiring the current event.
				 */
				eventPtr = GTK3.gtk_get_current_event();
				if (eventPtr == 0) {
					eventPtr = GDK.gdk_event_new(GTK.GTK4 ? GDK.GDK4_BUTTON_PRESS : GDK.GDK_BUTTON_PRESS);
					GdkEventButton event = new GdkEventButton ();
					event.type = GTK.GTK4 ? GDK.GDK4_BUTTON_PRESS : GDK.GDK_BUTTON_PRESS;
					// Only assign a window on X11, as on Wayland the window is that of the mouse pointer
					if (OS.isX11()) {
						event.window = OS.g_object_ref(GTK3.gtk_widget_get_window (getShell().handle));
					}
					event.device = GDK.gdk_get_pointer(GDK.gdk_display_get_default ());
					event.time = display.getLastEventTime ();
					GTK3.memmove (eventPtr, event, GdkEventButton.sizeof);
				}
				adjustParentWindowWayland(eventPtr);
				verifyMenuPosition(getItemCount());
				GTK3.gtk_menu_popup_at_pointer(handle, eventPtr);
				if (GTK.GTK4) {
					GDK.gdk_event_unref(eventPtr);
				} else {
					GDK.gdk_event_free(eventPtr);
				}
			}
			poppedUpCount = getItemCount();
		} else {
			sendEvent (SWT.Hide);
		}
	} else {
		if (GTK.GTK4) {
			GTK.gtk_popover_popdown(handle);
		} else {
			GTK3.gtk_menu_popdown (handle);
		}
	}
}

void addAccelerators (long accelGroup) {
	MenuItem[] items = getItems();
	for (int i = 0; i < items.length; i++) {
		MenuItem item = items[i];
		item.addAccelerators(accelGroup);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when menus are hidden or shown, by sending it
 * one of the messages defined in the <code>MenuListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #removeMenuListener
 */
public void addMenuListener (MenuListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

@Override
void createHandle (int index) {
	state |= HANDLE;

	if (GTK.GTK4) {
		int bits = SWT.BAR | SWT.DROP_DOWN | SWT.POP_UP;
		modelHandle = OS.g_menu_new();
		if (modelHandle == 0) error(SWT.ERROR_NO_HANDLES);

		items = new LinkedList<>();
		sections = new LinkedList<>();

		switch (style & bits) {
			case SWT.BAR:
				handle = GTK4.gtk_popover_menu_bar_new_from_model(modelHandle);
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);

				GTK4.gtk_box_prepend(parent.vboxHandle, handle);
				break;
			case SWT.DROP_DOWN:
				handle = modelHandle;
				break;
			case SWT.POP_UP:
			default:
				handle = GTK4.gtk_popover_menu_new_from_model_full(modelHandle, GTK4.GTK_POPOVER_MENU_NESTED);
				GTK.gtk_widget_set_parent(handle, parent.handle);
				GTK.gtk_popover_set_position(handle, GTK.GTK_POS_BOTTOM);
				GTK4.gtk_popover_set_has_arrow(handle, false);
				GTK.gtk_widget_set_halign(handle, GTK.GTK_ALIGN_START);
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
		}

		// Create first section
		long firstSection = OS.g_menu_new();
		if (firstSection == 0) error(SWT.ERROR_NO_HANDLES);
		sections.add(new Section(firstSection));

		long defaultSection = OS.g_menu_item_new_section(null, firstSection);
		OS.g_menu_insert_item(modelHandle, 0, defaultSection);
		OS.g_object_unref(defaultSection);

		actionGroup = OS.g_simple_action_group_new();
		if (actionGroup == 0) error(SWT.ERROR_NO_HANDLES);

		long shellHandle = parent.getShell().topHandle();
		GTK.gtk_widget_insert_action_group(shellHandle, Converter.javaStringToCString(String.valueOf(this.hashCode())), actionGroup);
	} else {
		if ((style & SWT.BAR) != 0) {
			handle = GTK3.gtk_menu_bar_new();
			if (handle == 0) error(SWT.ERROR_NO_HANDLES);

			long vboxHandle = parent.vboxHandle;
			GTK3.gtk_container_add(vboxHandle, handle);
			gtk_box_set_child_packing(vboxHandle, handle, false, true, 0, GTK.GTK_PACK_START);
		} else {
			handle = GTK3.gtk_menu_new();
			if (handle == 0) error(SWT.ERROR_NO_HANDLES);
			menuHandle = handle;
			OS.g_object_ref_sink(menuHandle);
		}
	}
}

@Override
void createWidget (int index) {
	checkOrientation (parent);
	super.createWidget (index);
	parent.addMenu (this);
}

void fixMenus (Decorations newParent) {
	if (isDisposed()) {
		return;
	}
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		items [i].fixMenus (newParent);
	}
	parent.removeMenu (this);
	newParent.addMenu (this);
	this.parent = newParent;
}

/**
 * Returns the default menu item or null if none has
 * been previously set.
 *
 * @return the default menu item.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getDefaultItem () {
	checkWidget();
	return null;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #isEnabled
 */
public boolean getEnabled() {
	checkWidget();

	if (GTK.GTK4) {
		if ((style & SWT.DROP_DOWN) != 0) {
			/*
			 * In GTK4, SWT.DROP_DOWN represents an wrapper of
			 * GMenu menu model. With this style the Menu object
			 * is just a structure to hold MenuItems. We return true
			 * in all cases as this is just a model. If the dropdown menu
			 * is to be disabled, the corresponding SWT.CASCADE
			 * menu item should be disabled.
			 */
			return true;
		} else {
			return GTK.gtk_widget_get_sensitive(handle);
		}
	} else {
		return GTK.gtk_widget_get_sensitive(handle);
	}
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getItem(int index) {
	checkWidget();

	if (GTK.GTK4) {
		if (index < 0 || index >= items.size()) error(SWT.ERROR_INVALID_RANGE);

		return items.get(index);
	} else {
		long list = GTK3.gtk_container_get_children (handle);
		if (list == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int count = OS.g_list_length (list);
		if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
		long data = OS.g_list_nth_data (list, index);
		OS.g_list_free (list);
		if (data == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		return (MenuItem) display.getWidget(data);
	}
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();

	if (GTK.GTK4) {
		return items.size();
	} else {
		int count = 0;
		long list = GTK3.gtk_container_get_children (handle);
		if (list == 0) return 0;
		count = OS.g_list_length (list);
		OS.g_list_free (list);
		return Math.max(0, count);
	}
}

/**
 * Returns a (possibly empty) array of <code>MenuItem</code>s which
 * are the items in the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem [] getItems () {
	checkWidget();

	if (GTK.GTK4) {
		return items.toArray(new MenuItem[items.size()]);
	} else {
		long list = GTK3.gtk_container_get_children (handle);
		if (list == 0) return new MenuItem [0];
		long originalList = list;
		int count = OS.g_list_length (list);
		MenuItem [] items = new MenuItem [count];
		int index = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			MenuItem item = (MenuItem) display.getWidget (data);
			if (item != null) items [index++] = item;
			list = OS.g_list_next (list);
		}
		OS.g_list_free (originalList);
		if (index != items.length) {
			MenuItem [] newItems = new MenuItem [index];
			System.arraycopy (items, 0, newItems, 0, index);
			items = newItems;
		}
		return items;
	}
}

@Override
String getNameText () {
	String result = "";
	MenuItem [] items = getItems ();
	int length = items.length;
	if (length > 0) {
		for (int i=0; i<length-1; i++) {
			result = result + items [i].getNameText() + ", ";
		}
		result = result + items [length-1].getNameText ();
	}
	return result;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns the receiver's parent, which must be a <code>Decorations</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Decorations getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>MenuItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getParentItem () {
	checkWidget();
	return cascade;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>Menu</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParentMenu () {
	checkWidget();
	if (cascade == null) return null;
	return cascade.getParent ();
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells. Returns null if receiver or its ancestor
 * is the application menubar.
 *
 * @return the receiver's shell or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget();
	return parent.getShell ();
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget();
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups != null) {
			for (int i=0; i<popups.length; i++) {
				if (popups [i] == this) return true;
			}
		}
	}
	return GTK.gtk_widget_get_mapped (handle);
}

@Override
long gtk_hide (long widget) {
	if ((style & SWT.POP_UP) != 0) {
		if (display.activeShell != null) {
			display.activeShell = getShell ();
			if (display.activeShell.ignoreFocusOut) display.activeShell.ignoreFocusIn = true;
			display.activeShell.ignoreFocusOut = false;
		}
	}
	sendEvent (SWT.Hide);
	if (OS.ubuntu_menu_proxy_get() != 0) {
		MenuItem[] items = getItems();
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item.updateAcceleratorText(false)) continue;
		}
	}
	return 0;
}

@Override
long gtk_show (long widget) {
	if ((style & SWT.POP_UP) != 0) {
		if (display.activeShell != null) {
			display.activeShell = getShell ();
			display.activeShell.ignoreFocusOut = true;
		}
		return 0;
	}
	sendEvent (SWT.Show);
	if (OS.ubuntu_menu_proxy_get() != 0) {
		MenuItem[] items = getItems();
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item.updateAcceleratorText(true)) continue;
		}
	}
	return 0;
}


@Override
long gtk3_show_help (long widget, long helpType) {
	if (sendHelpEvent (helpType)) {
		GTK3.gtk_menu_shell_deactivate (handle);
		return 1;
	}
	return 0;
}

@Override
long gtk_menu_popped_up (long widget, long flipped_rect, long final_rect, long flipped_x, long flipped_y) {
	GdkRectangle finalRect = new GdkRectangle ();
	OS.memmove (finalRect, final_rect, GDK.GdkRectangle_sizeof());
	GdkRectangle flippedRect = new GdkRectangle ();
	OS.memmove (flippedRect, flipped_rect, GDK.GdkRectangle_sizeof());
	boolean flippedX = flipped_x == 1;
	boolean flippedY = flipped_y == 1;
	System.out.println("SWT_MENU_LOCATION_DEBUGGING enabled, printing positioning info for " + widget);
	if (OS.isWayland()) System.out.println("Note: SWT is running on Wayland, coordinates will be parent-relative");
	if (hasLocation) {
		System.out.println("hasLocation is true and set coordinates are Point {" + this.x + ", " + this.y + "}");
	} else {
		System.out.println("hasLocation is not set, this is most likely a right click menu");
	}
	if (flippedX) System.out.println("Menu is inverted along the X-axis");
	if (flippedY) System.out.println("Menu is inverted along the Y-axis");
	System.out.println("Final menu position and size is Rectangle {" + finalRect.x + ", " + finalRect.y + ", " +
			finalRect.width + ", " + finalRect.height + "}");
	System.out.println("Flipped menu position and size is Rectangle {" + flippedRect.x + ", " + flippedRect.y + ", " +
			flippedRect.width + ", " + flippedRect.height + "}");
	System.out.println("");
	return 0;
}


@Override
void hookEvents() {
	super.hookEvents();

	if (GTK.GTK4) {
		shortcutController = GTK4.gtk_shortcut_controller_new();
		if (shortcutController == 0) error(SWT.ERROR_NO_HANDLES);
		GTK4.gtk_shortcut_controller_set_scope(shortcutController, GTK.GTK_SHORTCUT_SCOPE_GLOBAL);
		GTK4.gtk_widget_add_controller(parent.handle, shortcutController);

		if ((style & SWT.DROP_DOWN) == 0) {
			OS.g_signal_connect_closure_by_id(handle, display.signalIds[SHOW], 0, display.getClosure(SHOW), false);
			OS.g_signal_connect_closure_by_id(handle, display.signalIds[HIDE], 0, display.getClosure(HIDE), false);
		}

	} else {
		OS.g_signal_connect_closure_by_id(handle, display.signalIds[SHOW_HELP], 0, display.getClosure(SHOW_HELP), false);

		// Hook into the "popped-up" signal if SWT_MENU_LOCATION_DEBUGGING has been set
		if (OS.SWT_MENU_LOCATION_DEBUGGING) {
			OS.g_signal_connect_closure_by_id(handle, display.signalIds[POPPED_UP], 0, display.getClosure(POPPED_UP), false);
		}

		OS.g_signal_connect_closure_by_id(handle, display.signalIds[SHOW], 0, display.getClosure(SHOW), false);
		OS.g_signal_connect_closure_by_id(handle, display.signalIds[HIDE], 0, display.getClosure(HIDE), false);
	}
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) {
		return getEnabled () && parent.isEnabled ();
	}
	return getEnabled () && parentMenu.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

@Override
void releaseChildren (boolean destroy) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	super.releaseChildren (destroy);
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}  else {
		if ((style & SWT.POP_UP) != 0) {
			display.removePopup (this);
		}
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (parent != null) parent.removeMenu (this);
	parent = null;
	cascade = null;
	if (imageList != null) imageList.dispose ();
	imageList = null;
}

/**
 * Overridden to fix memory leak on GTK3, see bug 573983
 * {@inheritDoc}
 */
@Override
void destroyWidget () {
	super.destroyWidget();
	if (menuHandle != 0) {
		OS.g_object_unref(menuHandle);
		menuHandle = 0;
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the menu events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

void removeAccelerators (long accelGroup) {
	MenuItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		MenuItem item = items[i];
		item.removeAccelerators (accelGroup);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

@Override
void reskinChildren (int flags) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		item.reskin (flags);
	}
	super.reskinChildren (flags);
}

boolean sendHelpEvent (long helpType) {
	if (selectedItem != null && !selectedItem.isDisposed()) {
		if (selectedItem.hooks (SWT.Help)) {
			selectedItem.postEvent (SWT.Help);
			return true;
		}
	}
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return true;
	}
	return parent.sendHelpEvent (helpType);
}

/**
 * Sets the default menu item to the argument or removes
 * the default emphasis when the argument is <code>null</code>.
 *
 * @param item the default menu item or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultItem (MenuItem item) {
	checkWidget();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled(boolean enabled) {
	checkWidget();

	if (GTK.GTK4) {
		if ((style & SWT.DROP_DOWN) != 0) {
			/*
			 * In GTK4, SWT.DROP_DOWN acts solely as a wrapper
			 * of the GMenu menu model. See getEnabled for more info.
			 */
			return;
		} else {
			GTK.gtk_widget_set_sensitive(handle, enabled);
		}
	} else {
		GTK.gtk_widget_set_sensitive(handle, enabled);
	}
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Also note that the actual location of the menu is dependent
 * on platform specific behavior. For example: on Linux with
 * Wayland this operation is a hint due to lack of global
 * coordinates.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	setLocation (new Point (x, y));
}

void setLocationInPixels (int x, int y) {
	checkWidget();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	this.x = x;
	this.y = y;
	hasLocation = true;
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public void setLocation (Point location) {
	checkWidget ();
	setLocationInPixels (DPIUtil.autoScaleUp (location));
}

void setLocationInPixels (Point location) {
	checkWidget();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocationInPixels (location.x, location.y);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public void setOrientation (int orientation) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	_setOrientation (orientation);
}

void _setOrientation (int orientation) {
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	setOrientation (false);
}

@Override
void setOrientation (boolean create) {
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (handle != 0) GTK.gtk_widget_set_direction (handle, dir);
		MenuItem [] items = getItems ();
		for (int i = 0; i < items.length; i++) {
			items [i].setOrientation (create);
		}
	}
}

/**
 * Lack of absolute coordinates make Wayland event windows inaccurate.
 * Currently the best approach is to the use the GdkWindow of the mouse
 * pointer. See bug 530059 and 532074.<p>
 *
 * @param eventPtr a pointer to the GdkEvent
 */
void adjustParentWindowWayland (long eventPtr) {
	if (OS.isWayland()) {
		long display = GDK.gdk_display_get_default ();
		long pointer = GDK.gdk_get_pointer(display);
		long deviceResource;
		if (GTK.GTK4) {
			deviceResource = GDK.gdk_device_get_surface_at_position(pointer, null, null);
		} else {
			deviceResource = GDK.gdk_device_get_window_at_position(pointer, null, null);
		}
		OS.g_object_ref(deviceResource);
		int eventType = GDK.gdk_event_get_event_type(eventPtr);
		eventType = Control.fixGdkEventTypeValues(eventType);
		switch (eventType) {
			case GDK.GDK_BUTTON_PRESS:
				GdkEventButton eventButton = new GdkEventButton();
				GTK3.memmove (eventButton, eventPtr, GdkEventButton.sizeof);
				eventButton.window = deviceResource;
				GTK3.memmove(eventPtr, eventButton, GdkEventButton.sizeof);
				break;
			case GDK.GDK_KEY_PRESS:
				GdkEventKey eventKey = new GdkEventKey();
				GTK3.memmove (eventKey, eventPtr, GdkEventKey.sizeof);
				eventKey.window = deviceResource;
				GTK3.memmove(eventPtr, eventKey, GdkEventKey.sizeof);
				break;
		}
	}
	return;
}

/**
 * Feature in GTK3 on X11: context menus in SWT are populated
 * dynamically, sometimes asynchronously outside of SWT
 * (i.e. in Platform UI). This means that items are added and
 * removed just before the menu is shown. This method of
 * changing the menu content can sometimes cause sizing issues
 * internally in GTK, specifically with the height of the
 * toplevel GdkWindow. <p>
 *
 * The fix is to cache the number of items popped up previously,
 * and if the number of items in the current menu (to be popped up)
 * is different, then:<ul>
 *     <li>get the preferred height of the menu</li>
 *     <li>set the toplevel GdkWindow to that height</li></ul>
 *
 * @param itemCount the current number of items in the menu, just
 * before it's about to be shown/popped-up
 */
void verifyMenuPosition (int itemCount) {
	if (OS.isX11()) {
		if (itemCount != poppedUpCount && poppedUpCount != 0) {
			int [] naturalHeight = new int [1];
			/*
			 * We need to "show" the menu before fetching the preferred height.
			 * Note, this does not actually pop-up the menu.
			 */
			GTK.gtk_widget_show(handle);
			/*
			 * Menus are height-for-width only: use gtk_widget_get_preferred_height()
			 * instead of gtk_widget_get_preferred_size().
			 */
			GTK3.gtk_widget_get_preferred_height(handle, null, naturalHeight);
			if (naturalHeight[0] > 0) {
				if (GTK.GTK4) {
					/* TODO: GTK4 gdk_surface_resize/move no longer exist & have been replaced with
					 * gdk_toplevel_begin_resize & gdk_toplevel_begin_move. These functions might change the
					 * design of resizing and moving in GTK4 */
				} else {
					long topLevelWidget = GTK3.gtk_widget_get_toplevel(handle);
					long topLevelWindow = GTK3.gtk_widget_get_window(topLevelWidget);
					int width = GDK.gdk_window_get_width(topLevelWindow);
					GDK.gdk_window_resize(topLevelWindow, width, naturalHeight[0]);
				}
			}
		}
	}
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (visible) {
		display.addPopup (this);
	} else {
		display.removePopup (this);
		_setVisible (false);
	}
}
}


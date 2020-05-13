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
package org.eclipse.swt.accessibility;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class AccessibleObject {
	long atkHandle;
	int index = -1, id = ACC.CHILDID_SELF;
	Accessible accessible;
	AccessibleObject parent;
	AccessibleObject[] children;
	/*
	* a lightweight object does not correspond to a concrete gtk widget, but
	* to a logical child of a widget (eg.- a CTabItem, which is simply drawn)
	*/
	boolean isLightweight = false;

	static long actionNamePtr = -1;
	static long descriptionPtr = -1;
	static long keybindingPtr = -1;
	static long namePtr = -1;
	static final Map<LONG, AccessibleObject> AccessibleObjects = new HashMap<> (9);
	static final boolean DEBUG = Device.DEBUG;

	AccessibleObject (long type, long widget, Accessible accessible, boolean isLightweight) {
		super ();
		if (type == OS.swt_fixed_get_type()) {
			if (widget != 0 && !isLightweight) {
				atkHandle = GTK.gtk_widget_get_accessible(widget);
			} else {
				// Lightweight widgets map to no "real" GTK widget, so we
				// just instantiate a new SwtFixedAccessible
				atkHandle = OS.g_object_new (OS.swt_fixed_accessible_get_type(), 0);
			}
			OS.swt_fixed_accessible_register_accessible(atkHandle, false, widget);
		} else {
			// TODO_a11y: accessibility listeners on the Java side have not yet
			// been implemented for native GTK widgets on GTK3.
			atkHandle = GTK.gtk_widget_get_accessible(widget);
		}
		this.accessible = accessible;
		this.isLightweight = isLightweight;
		AccessibleObjects.put (new LONG (atkHandle), this);
	}

	static void print (String str) {
		System.out.println (str);
	}

	static int size(Collection<?> listeners) {
		return listeners == null ? 0 : listeners.size();
	}

	/**
	 * Fills a Java AtkActionIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkActionIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkActionIface getParentActionIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_ACTION())) {
			AtkActionIface iface = new AtkActionIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Performs the specified action on atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index the action index corresponding to the action to be performed
	 *
	 * @return long int representing whether the action succeeded: 1 for success,
	 * 0 for failure
	 */
	static long atkAction_do_action (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleActionListener> listeners = accessible.accessibleActionListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = listeners.get(i);
					listener.doAction(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkActionIface iface = getParentActionIface (atkObject);
		if (iface != null && iface.do_action != 0) {
			parentResult = ATK.call (iface.do_action, atkObject, index);
		}
		return parentResult;
	}

	/**
	 * Returns the number of accessible actions available on atkObject.
	 * If there are more than one, the first is considered the default
	 * action of the object.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return the number of actions available
	 */
	static long atkAction_get_n_actions (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleActionListener> listeners = accessible.accessibleActionListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = listeners.get(i);
					listener.getActionCount(event);
				}
				return event.count;
			}
		}
		long parentResult = 0;
		AtkActionIface iface = getParentActionIface (atkObject);
		if (iface != null && iface.get_n_actions != 0) {
			parentResult = ATK.call (iface.get_n_actions, atkObject);
		}
		return parentResult;
	}

	/**
	 * Returns a description of the specified action of the atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index the action index corresponding to the action to be performed
	 *
	 * @return a pointer to the description string
	 */
	static long atkAction_get_description (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleActionListener> listeners = accessible.accessibleActionListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = listeners.get(i);
					listener.getDescription(event);
				}
				if (event.result == null) return 0;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		long parentResult = 0;
		AtkActionIface iface = getParentActionIface (atkObject);
		if (iface != null && iface.get_description != 0) {
			parentResult = ATK.call (iface.get_description, atkObject, index);
		}
		return parentResult;
	}

	/**
	 * Returns the keybinding which can be used to activate
	 * this atkObject, if one exists. Example: "Ctrl+1"
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index the action index corresponding to the action to be performed
	 *
	 * @return a pointer to the keybinding string
	 */
	static long atkAction_get_keybinding (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkActionIface iface = getParentActionIface (atkObject);
		if (iface != null && iface.get_keybinding != 0) {
			parentResult = ATK.call (iface.get_keybinding, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleActionListener> listeners = accessible.accessibleActionListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = listeners.get(i);
					listener.getKeyBinding(event);
				}
				if (event.result != null) {
					if (keybindingPtr != -1) OS.g_free (keybindingPtr);
					return keybindingPtr = getStringPtr (event.result);
				}
			}
			List<AccessibleListener> listeners2 = accessible.accessibleListeners;
			length = size(listeners2);
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = listeners2.get(i);
					listener.getKeyboardShortcut (event);
				}
				if (event.result != null) {
					if (keybindingPtr != -1) OS.g_free (keybindingPtr);
					return keybindingPtr = getStringPtr (event.result);
				}
			}
		}
		return parentResult;
	}

	/**
	 * Returns the name of the specified action of the atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index the action index corresponding to the action to be performed
	 *
	 * @return a pointer to the name string
	 */
	static long atkAction_get_name (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkActionIface iface = getParentActionIface (atkObject);
		if (iface != null && iface.get_name != 0) {
			parentResult = ATK.call (iface.get_name, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleActionListener> listeners = accessible.accessibleActionListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = listeners.get(i);
					listener.getName(event);
				}
				if (event.result != null) {
					if (actionNamePtr != -1) OS.g_free (actionNamePtr);
					return actionNamePtr = getStringPtr (event.result);
				}
			}
			if (index == 0) {
				List<AccessibleControlListener> listeners2 = accessible.accessibleControlListeners;
				length = size(listeners2);
				if (length > 0) {
					AccessibleControlEvent event = new AccessibleControlEvent (accessible);
					event.childID = object.id;
					if (parentResult != 0) event.result = getString (parentResult);
					for (int i = 0; i < length; i++) {
						AccessibleControlListener listener = listeners2.get(i);
						listener.getDefaultAction (event);
					}
					if (event.result != null) {
						if (actionNamePtr != -1) OS.g_free (actionNamePtr);
						return actionNamePtr = getStringPtr (event.result);
					}
				}
			}
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkComponentIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkComponentIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkComponentIface getParentComponentIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_COMPONENT())) {
			AtkComponentIface iface = new AtkComponentIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Gets the rectangle which gives the extent of the component.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param x memory address of gint to put x coordinate
	 * @param y memory address of gint to put y coordinate
	 * @param width memory address of gint to put width coordinate
	 * @param height memory address of gint to put height coordinate
	 * @param coord_type specifies whether the coordinates are relative to
	 * the screen or to the components top level window
	 *
	 * @return 0 (this is a void function at the native level)
	 */
	static long atkComponent_get_extents (long atkObject, long x, long y,
			long width, long height, long coord_type) {
		AccessibleObject object = getAccessibleObject (atkObject);
		C.memmove (x, new int[] {0}, 4);
		C.memmove (y, new int[] {0}, 4);
		C.memmove (width, new int[] {0}, 4);
		C.memmove (height, new int[] {0}, 4);
		AtkComponentIface iface = getParentComponentIface (atkObject);
		if (iface != null && iface.get_extents != 0) {
			OS.call (iface.get_extents, atkObject, x, y, width, height, coord_type);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				int[] parentX = new int [1], parentY = new int [1];
				int[] parentWidth = new int [1], parentHeight = new int [1];
				C.memmove (parentX, x, 4);
				C.memmove (parentY, y, 4);
				C.memmove (parentWidth, width, 4);
				C.memmove (parentHeight, height, 4);
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.x = parentX [0]; event.y = parentY [0];
				event.width = parentWidth [0]; event.height = parentHeight [0];
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0];
				}
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getLocation (event);
				}
				if (coord_type == ATK.ATK_XY_WINDOW) {
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				C.memmove (x, new int[] {event.x}, 4);
				C.memmove (y, new int[] {event.y}, 4);
				C.memmove (width, new int[] {event.width}, 4);
				C.memmove (height, new int[] {event.height}, 4);
			}
		}
		return 0;
	}

	/**
	 * Gets a reference to the accessible child, if one exists,
	 * at the coordinate point specified by x and y.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param x long integer representing the x coordinate
	 * @param y long integer representing the y coordinate
	 * @param coord_type specifies whether the coordinates are relative to
	 * the screen or to the components top level window
	 *
	 * @return a pointer to the accessible child, if one exists
	 */
	static long atkComponent_ref_accessible_at_point (long atkObject, long x,
			long y, long coord_type) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.x = (int)x; event.y = (int)y;
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0];
				}
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getChildAtPoint (event);
				}
				if (event.childID == object.id) event.childID = ACC.CHILDID_SELF;
				Accessible result = event.accessible;
				AccessibleObject accObj = result != null ? result.getAccessibleObject() : object.getChildByID (event.childID);
				if (accObj != null) {
					return OS.g_object_ref (accObj.atkHandle);
				}
			}
		}
		long parentResult = 0;
		AtkComponentIface iface = getParentComponentIface (atkObject);
		if (iface != null && iface.ref_accessible_at_point != 0) {
			parentResult = OS.call (iface.ref_accessible_at_point, atkObject, x, y, coord_type);
		}
		return parentResult;
	}


	/**
	 * Fills a Java AtkEditableTextIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkEdtiableTextIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkEditableTextIface getParentEditableTextIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_EDITABLE_TEXT())) {
			AtkEditableTextIface iface = new AtkEditableTextIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_EDITABLE_TEXT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Sets the attributes for a specified range.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param attrib_set a pointer to an AtkAttributeSet
	 * @param start_offset start range in which to set attributes
	 * @param end_offset end of range in which to set attributes
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_set_run_attributes (long atkObject, long attrib_set,
			long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				Display display = accessible.control.getDisplay();
				long fontDesc = OS.pango_font_description_new ();
				boolean createFont = false;
				TextStyle style = new TextStyle();
				String [] attributes = new String [0];
				long current = attrib_set;
				int listLength = OS.g_slist_length (attrib_set);
				for (int i = 0; i < listLength; i++) {
					long attrPtr = OS.g_slist_data (current);
					if (attrPtr != 0) {
						AtkAttribute attr = new AtkAttribute();
						ATK.memmove(attr, attrPtr, AtkAttribute.sizeof);
						String name = getString(attr.name);
						String value = getString(attr.value);
						OS.g_free(attrPtr);
						String [] newAttributes = new String [attributes.length + 2];
						System.arraycopy (attributes, 0, newAttributes, 0, attributes.length);
						newAttributes[attributes.length] = name;
						newAttributes[attributes.length + 1] = value;
						attributes = newAttributes;
						try {
							if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_RISE)))) {
								// number of pixels above baseline
								style.rise = Integer.parseInt(value);
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_UNDERLINE)))) {
								// "none", "single", "double", "low", or "error" (also allow "squiggle")
								if (value.equals("single") || value.equals("low")) {
									style.underline = true;
									style.underlineStyle = SWT.UNDERLINE_SINGLE;
								} else if (value.equals("double")) {
									style.underline = true;
									style.underlineStyle = SWT.UNDERLINE_DOUBLE;
								} else if (value.equals("error")) {
									style.underline = true;
									style.underlineStyle = SWT.UNDERLINE_ERROR;
								} else if (value.equals("squiggle")) {
									style.underline = true;
									style.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
								}
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRIKETHROUGH)))) {
								// "true" or "false" (also allow "1" and "single")
								if (value.equals("true") || value.equals("1") || value.equals("single")) style.strikeout = true;
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FAMILY_NAME)))) {
								// font family name
								byte [] buffer = Converter.wcsToMbcs(value, true);
								OS.pango_font_description_set_family(fontDesc, buffer);
								createFont = true;
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_SIZE)))) {
								// size of characters in points (allow fractional points)
								float size = Float.parseFloat(value);
								OS.pango_font_description_set_size(fontDesc, (int)(size * OS.PANGO_SCALE));
								createFont = true;
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STYLE)))) {
								// "normal", "italic" or "oblique"
								int fontStyle = -1;
								if (value.equals("normal")) fontStyle = OS.PANGO_STYLE_NORMAL;
								else if (value.equals("italic")) fontStyle = OS.PANGO_STYLE_ITALIC;
								else if (value.equals("oblique")) fontStyle = OS.PANGO_STYLE_OBLIQUE;
								if (fontStyle != -1) {
									OS.pango_font_description_set_style(fontDesc, fontStyle);
									createFont = true;
								}
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_VARIANT)))) {
								// "normal" or "small_caps"
								int variant = -1;
								if (value.equals("normal")) variant = OS.PANGO_VARIANT_NORMAL;
								else if (value.equals("small_caps")) variant = OS.PANGO_VARIANT_SMALL_CAPS;
								if (variant != -1) {
									OS.pango_font_description_set_variant(fontDesc, variant);
									createFont = true;
								}
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRETCH)))) {
								//"ultra_condensed", "extra_condensed", "condensed", "semi_condensed", "normal", "semi_expanded", "expanded", "extra_expanded" or "ultra_expanded"
								int stretch = -1;
								if (value.equals("ultra_condensed")) stretch = OS.PANGO_STRETCH_ULTRA_CONDENSED;
								else if (value.equals("extra_condensed")) stretch = OS.PANGO_STRETCH_EXTRA_CONDENSED;
								else if (value.equals("condensed")) stretch = OS.PANGO_STRETCH_CONDENSED;
								else if (value.equals("semi_condensed")) stretch = OS.PANGO_STRETCH_SEMI_CONDENSED;
								else if (value.equals("normal")) stretch = OS.PANGO_STRETCH_NORMAL;
								else if (value.equals("semi_expanded")) stretch = OS.PANGO_STRETCH_SEMI_EXPANDED;
								else if (value.equals("expanded")) stretch = OS.PANGO_STRETCH_EXPANDED;
								else if (value.equals("extra_expanded")) stretch = OS.PANGO_STRETCH_EXTRA_EXPANDED;
								else if (value.equals("ultra_expanded")) stretch = OS.PANGO_STRETCH_ULTRA_EXPANDED;
								if (stretch != -1) {
									OS.pango_font_description_set_stretch(fontDesc, stretch);
									createFont = true;
								}
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_WEIGHT)))) {
								// weight of the characters
								int weight = Integer.parseInt(value);
								OS.pango_font_description_set_weight(fontDesc, weight);
								createFont = true;
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FG_COLOR)))) {
								// RGB value of the format "u,u,u"
								style.foreground = colorFromString(display, value);
							} else if (name.equals(getString(ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_BG_COLOR)))) {
								// RGB value of the format "u,u,u"
								style.background = colorFromString(display, value);
							} else {
								//TODO language and direction
							}
						} catch (NumberFormatException ex) {}
					}
					current = OS.g_slist_next (current);
				}
				if (createFont) {
					style.font = Font.gtk_new(display, fontDesc);
				}

				AccessibleTextAttributeEvent event = new AccessibleTextAttributeEvent(accessible);
				event.start = (int)start_offset;
				event.end = (int)end_offset;
				event.textStyle = style;
				event.attributes = attributes;
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.setTextAttributes(event);
				}
				if (style.font != null) {
					style.font.dispose();
				}
				if (style.foreground != null) {
					style.foreground.dispose();
				}
				if (style.background != null) {
					style.background.dispose();
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.set_run_attributes != 0) {
			parentResult = OS.call (iface.set_run_attributes, atkObject, attrib_set, start_offset, end_offset);
		}
		return parentResult;
	}

	/*
	 * Return a Color given a string of the form "n,n,n".
	 * @param display must be the display for the accessible's control
	 * @param rgbString must not be null
	 */
	static Color colorFromString(Display display, String rgbString) {
		try {
			int comma1 = rgbString.indexOf(',');
			int comma2 = rgbString.indexOf(',', comma1 + 1);
			int r = Integer.parseInt(rgbString.substring(0, comma1));
			int g = Integer.parseInt(rgbString.substring(comma1 + 1, comma2));
			int b = Integer.parseInt(rgbString.substring(comma2 + 1, rgbString.length()));
			return new Color(r, g, b);
		} catch (NumberFormatException ex) {}
		return null;
	}

	/**
	 * Sets text contents of the atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param string the text to be set
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_set_text_contents (long atkObject, long string) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				event.start = 0;
				String text = object.getText ();
				event.end = text == null ? 0 : text.length ();
				event.string = getString (string);
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.replaceText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.set_text_contents != 0) {
			parentResult = ATK.call (iface.set_text_contents, atkObject, string);
		}
		return parentResult;
	}

	/**
	 * Inserts text at a given position.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param string the text to insert
	 * @param string_length the length of the text to insert, in bytes
	 * @param position the caller initializes this to the position at which to
	 * insert the text. After the call, it points at the position after the
	 * newly inserted text.
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_insert_text (long atkObject, long string,
			long string_length, long position) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				int[] pos = new int [1];
				C.memmove (pos, position, C.PTR_SIZEOF);
				event.start = event.end = pos[0];
				event.string = getString (string);
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.replaceText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.insert_text != 0) {
			parentResult = OS.call (iface.insert_text, atkObject, string, string_length, position);
		}
		return parentResult;
	}

	/**
	 * Copies text from start_pos up to (but not including) end_pos
	 * into the clipboard.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_pos the start position of the text to be copied
	 * @param end_pos the end position of the text to be copied
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_copy_text(long atkObject, long start_pos, long end_pos) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				event.start = (int)start_pos;
				event.end = (int)end_pos;
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.copyText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.copy_text != 0) {
			parentResult = ATK.call (iface.copy_text, atkObject, start_pos, end_pos);
		}
		return parentResult;
	}

	/**
	 * Cuts text from start_pos up to (but not including) end_pos
	 * into the clipboard.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_pos the start position of the text to be cut
	 * @param end_pos the end position of the text to be cut
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_cut_text (long atkObject, long start_pos, long end_pos) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				event.start = (int)start_pos;
				event.end = (int)end_pos;
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.cutText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.cut_text != 0) {
			parentResult = ATK.call (iface.cut_text, atkObject, start_pos, end_pos);
		}
		return parentResult;
	}

	/**
	 * Delete text start_pos up to (but not including) end_pos.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_pos the start position of the text to be deleted
	 * @param end_pos the end position of the text to be deleted
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_delete_text (long atkObject, long start_pos, long end_pos) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				event.start = (int)start_pos;
				event.end = (int)end_pos;
				event.string = "";
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.replaceText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.delete_text != 0) {
			parentResult = ATK.call (iface.delete_text, atkObject, start_pos, end_pos);
		}
		return parentResult;
	}

	/**
	 * Paste text from clipboard to specified position.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param position the position to paste
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkEditableText_paste_text (long atkObject, long position) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleEditableTextListener> listeners = accessible.accessibleEditableTextListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEditableTextEvent event = new AccessibleEditableTextEvent(accessible);
				event.start = (int)position;
				for (int i = 0; i < length; i++) {
					AccessibleEditableTextListener listener = listeners.get(i);
					listener.pasteText(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkEditableTextIface iface = getParentEditableTextIface (atkObject);
		if (iface != null && iface.paste_text != 0) {
			parentResult = ATK.call (iface.paste_text, atkObject, position);
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkHypertextIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkHypertextIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkHypertextIface getParentHypertextIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_HYPERTEXT())) {
			AtkHypertextIface iface = new AtkHypertextIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_HYPERTEXT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Gets the link in this hypertext document at index link_index.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param link_index the index of the link
	 *
	 * @return a pointer to the AtkHypertext at link_index in atkObject
	 */
	static long atkHypertext_get_link (long atkObject, long link_index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)link_index;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getHyperlink(event);
				}
				Accessible result = event.accessible;
				return result != null ? result.getAccessibleObject().atkHandle : 0;
			}
		}
		long parentResult = 0;
		AtkHypertextIface iface = getParentHypertextIface (atkObject);
		if (iface != null && iface.get_link != 0) {
			parentResult = ATK.call (iface.get_link, atkObject, link_index);
		}
		return parentResult;
	}

	/**
	 * Gets the number of links in this hypertext document.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an integer representing the number of links in the hypertext
	 * in atkObject
	 */
	static long atkHypertext_get_n_links (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getHyperlinkCount(event);
				}
				return event.count;
			}
		}
		long parentResult = 0;
		AtkHypertextIface iface = getParentHypertextIface (atkObject);
		if (iface != null && iface.get_n_links != 0) {
			parentResult = ATK.call (iface.get_n_links, atkObject);
		}
		return parentResult;
	}

	/**
	 * Gets the index into the array of hyperlinks that is
	 * associated with the character specified by char_index.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param char_index a character index
	 *
	 * @return an integer representing the index into the array of
	 * hypertexts
	 */
	static long atkHypertext_get_link_index (long atkObject, long char_index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.offset = (int)char_index;
				event.index = -1;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getHyperlinkIndex(event);
				}
				return event.index;
			}
		}
		long parentResult = 0;
		AtkHypertextIface iface = getParentHypertextIface (atkObject);
		if (iface != null && iface.get_link_index != 0) {
			parentResult = ATK.call (iface.get_link_index, atkObject, char_index);
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkObjectClass struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkObjectClass object representing the class struct of atkObject's
	 * parent
	 */
	static AtkObjectClass getParentAtkObjectClass () {
		AtkObjectClass objectClass = new AtkObjectClass ();
		long type = OS.swt_fixed_accessible_get_type();
		if (type != 0) {
			long parentType = OS.g_type_parent (type);
			if (parentType != 0) ATK.memmove (objectClass, OS.g_type_class_peek (parentType));
		}
		return objectClass;
	}

	/**
	 * Gets the accessible description of the widget associated with
	 * atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the gchar representation of the accessible description
	 */
	static long atkObject_get_description (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_description != 0) {
			parentResult = ATK.call (objectClass.get_description, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleListener> listeners = accessible.accessibleListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = listeners.get (i);
					listener.getDescription (event);
				}
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	/**
	 * Get a list of properties applied to this object as a whole,
	 * as an AtkAttributeSet consisting of name-value pairs.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the AtkAttributeSet consisting of all properties
	 * applied to atkObject (can be empty if no properties are set)
	 */
	static long atkObject_get_attributes (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_attributes != 0) {
			parentResult = ATK.call (objectClass.get_attributes, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleAttributeListener> listeners = accessible.accessibleAttributeListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleAttributeEvent event = new AccessibleAttributeEvent (accessible);
				event.topMargin = event.bottomMargin = event.leftMargin = event.rightMargin = event.alignment
					= event.indent = event.groupLevel = event.groupCount = event.groupIndex = -1;
				for (int i = 0; i < length; i++) {
					AccessibleAttributeListener listener = listeners.get (i);
					listener.getAttributes (event);
				}
				AtkAttribute attr = new AtkAttribute();
				if (event.leftMargin != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_LEFT_MARGIN));
					attr.value = getStringPtr (String.valueOf(event.leftMargin));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (event.rightMargin != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_RIGHT_MARGIN));
					attr.value = getStringPtr (String.valueOf(event.rightMargin));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (event.topMargin != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = getStringPtr ("top-margin"); //$NON-NLS-1$
					attr.value = getStringPtr (String.valueOf(event.topMargin));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (event.bottomMargin != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = getStringPtr ("bottom-margin"); //$NON-NLS-1$
					attr.value = getStringPtr (String.valueOf(event.bottomMargin));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (event.indent != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_INDENT));
					attr.value = getStringPtr (String.valueOf(event.indent));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (event.justify) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_JUSTIFICATION));
					attr.value = getStringPtr ("fill"); //$NON-NLS-1$
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				} else if (event.alignment != -1) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_JUSTIFICATION));
					String str = "left"; //$NON-NLS-1$
					switch (event.alignment) {
						case SWT.LEFT: str = "left"; break; //$NON-NLS-1$
						case SWT.RIGHT: str = "right"; break; //$NON-NLS-1$
						case SWT.CENTER: str = "center"; break; //$NON-NLS-1$
					}
					attr.value = getStringPtr (str);
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				//TODO - tabStops

				/* Check for group attributes. */
				int level = (event.groupLevel != -1) ? event.groupLevel : 0;
				int setsize = (event.groupCount != -1) ? event.groupCount : 0;
				int posinset = (event.groupIndex != -1) ? event.groupIndex : 0;
				if (setsize == 0 && posinset == 0) {
					/* Determine position and count for radio buttons. */
					Control control = accessible.control;
					if (control instanceof Button && ((control.getStyle() & SWT.RADIO) != 0)) {
						Control [] children = control.getParent().getChildren();
						posinset = 1;
						setsize = 1;
						for (int i = 0; i < children.length; i++) {
							Control child = children[i];
							if (child instanceof Button && ((child.getStyle() & SWT.RADIO) != 0)) {
								if (child == control) posinset = setsize;
								else setsize++;
							}
						}
					}
				}
				if (level != 0) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = getStringPtr ("level"); //$NON-NLS-1$
					attr.value = getStringPtr (String.valueOf(level));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (setsize != 0) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = getStringPtr ("setsize"); //$NON-NLS-1$
					attr.value = getStringPtr (String.valueOf(setsize));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}
				if (posinset != 0) {
					long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = getStringPtr ("posinset"); //$NON-NLS-1$
					attr.value = getStringPtr (String.valueOf(posinset));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					parentResult = OS.g_slist_append(parentResult, attrPtr);
				}

				if (event.attributes != null) {
					int end = event.attributes.length / 2 * 2;
					for (int i = 0; i < end; i+= 2) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = getStringPtr (event.attributes[i]);
						attr.value = getStringPtr (event.attributes[i + 1]);
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						parentResult = OS.g_slist_append(parentResult, attrPtr);
					}
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the accessible name of the widget associated with
	 * atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the gchar representation of the accessible name
	 */
	static long atkObject_get_name (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_name != 0) {
			parentResult = ATK.call (objectClass.get_name, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleListener> listeners = accessible.accessibleListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = listeners.get (i);
					listener.getName (event);
				}
				if (event.result == null) return parentResult;
				if (namePtr != -1) OS.g_free (namePtr);
				return namePtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	/**
	 * Gets the number of accessible children of the widget associated with
	 * atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an int representing the number of accessible children associated
	 * with atkObject
	 */
	static long atkObject_get_n_children (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_n_children != 0) {
			parentResult = ATK.call (objectClass.get_n_children, atkObject);
		}
		if (object != null && object.id == ACC.CHILDID_SELF) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = (int)parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getChildCount (event);
				}
				return event.detail;
			}
		}
		return parentResult;
	}

	/**
	 * Gets the 0-based index of the widget (associated with atkObject)
	 * in its parent.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an int which is the index of the accessible atkObject in its
	 * parent
	 */
	static long atkObject_get_index_in_parent (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			AccessibleControlEvent event = new AccessibleControlEvent(accessible);
			event.childID = ACC.CHILDID_CHILD_INDEX;
			event.detail = -1;
			for (int i = 0; i < size(listeners); i++) {
				AccessibleControlListener listener = listeners.get(i);
				listener.getChild(event);
			}
			if (event.detail != -1) {
				return event.detail;
			}
			if (object.index != -1) {
				return object.index;
			}
		}
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_index_in_parent == 0) return 0;
		long result = ATK.call (objectClass.get_index_in_parent, atkObject);
		return result;
	}

	/**
	 * Gets the accessible parent of the widget associated with
	 * atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the AtkObject representing the accessible parent of
	 * atkObject
	 */
	static long atkObject_get_parent (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			if (object.parent != null) {
				return object.parent.atkHandle;
			}
		}
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_parent == 0) return 0;
		long parentResult = ATK.call (objectClass.get_parent, atkObject);
		return parentResult;
	}

	/**
	 * Gets the role of the accessible associated with atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return the AtkRole of atkObject
	 */
	static long atkObject_get_role (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = -1;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getRole (event);
				}
				if (event.detail != -1) {
					switch (event.detail) {
						/* Convert from win32 role values to atk role values */
						case ACC.ROLE_CHECKBUTTON: return ATK.ATK_ROLE_CHECK_BOX;
						case ACC.ROLE_CLIENT_AREA: return ATK.ATK_ROLE_DRAWING_AREA;
						case ACC.ROLE_COMBOBOX: return ATK.ATK_ROLE_COMBO_BOX;
						case ACC.ROLE_DIALOG: return ATK.ATK_ROLE_DIALOG;
						case ACC.ROLE_LABEL: return ATK.ATK_ROLE_LABEL;
						case ACC.ROLE_LINK: return ATK.ATK_ROLE_TEXT;
						case ACC.ROLE_LIST: return ATK.ATK_ROLE_LIST;
						case ACC.ROLE_LISTITEM: return ATK.ATK_ROLE_LIST_ITEM;
						case ACC.ROLE_MENU: return ATK.ATK_ROLE_MENU;
						case ACC.ROLE_MENUBAR: return ATK.ATK_ROLE_MENU_BAR;
						case ACC.ROLE_MENUITEM: return ATK.ATK_ROLE_MENU_ITEM;
						case ACC.ROLE_PROGRESSBAR: return ATK.ATK_ROLE_PROGRESS_BAR;
						case ACC.ROLE_PUSHBUTTON: return ATK.ATK_ROLE_PUSH_BUTTON;
						case ACC.ROLE_SCROLLBAR: return ATK.ATK_ROLE_SCROLL_BAR;
						case ACC.ROLE_SEPARATOR: return ATK.ATK_ROLE_SEPARATOR;
						case ACC.ROLE_SLIDER: return ATK.ATK_ROLE_SLIDER;
						case ACC.ROLE_TABLE: return ATK.ATK_ROLE_TABLE;
						case ACC.ROLE_TABLECELL: return ATK.ATK_ROLE_TABLE_CELL;
						case ACC.ROLE_TABLECOLUMNHEADER: return ATK.ATK_ROLE_TABLE_COLUMN_HEADER;
						case ACC.ROLE_TABLEROWHEADER: return ATK.ATK_ROLE_TABLE_ROW_HEADER;
						case ACC.ROLE_TABFOLDER: return ATK.ATK_ROLE_PAGE_TAB_LIST;
						case ACC.ROLE_TABITEM: return ATK.ATK_ROLE_PAGE_TAB;
						case ACC.ROLE_TEXT: return ATK.ATK_ROLE_TEXT;
						case ACC.ROLE_TOOLBAR: return ATK.ATK_ROLE_TOOL_BAR;
						case ACC.ROLE_TOOLTIP: return ATK.ATK_ROLE_TOOL_TIP;
						case ACC.ROLE_TREE: return ATK.ATK_ROLE_TREE;
						case ACC.ROLE_TREEITEM: return ATK.ATK_ROLE_LIST_ITEM;
						case ACC.ROLE_RADIOBUTTON: return ATK.ATK_ROLE_RADIO_BUTTON;
						case ACC.ROLE_SPLITBUTTON: return ATK.ATK_ROLE_PUSH_BUTTON;
						case ACC.ROLE_WINDOW: return ATK.ATK_ROLE_WINDOW;
						case ACC.ROLE_ROW: return ATK.ATK_ROLE_TABLE_ROW;
						case ACC.ROLE_COLUMN: return ATK.ATK_ROLE_UNKNOWN; //Column role doesn't exist on Gtk.
						case ACC.ROLE_ALERT: return ATK.ATK_ROLE_ALERT;
						case ACC.ROLE_ANIMATION: return ATK.ATK_ROLE_ANIMATION;
						case ACC.ROLE_CANVAS: return ATK.ATK_ROLE_CANVAS;
						case ACC.ROLE_GROUP: return ATK.ATK_ROLE_PANEL;
						case ACC.ROLE_SPINBUTTON: return ATK.ATK_ROLE_SPIN_BUTTON;
						case ACC.ROLE_STATUSBAR: return ATK.ATK_ROLE_STATUSBAR;
						case ACC.ROLE_CHECKMENUITEM: return ATK.ATK_ROLE_CHECK_MENU_ITEM;
						case ACC.ROLE_RADIOMENUITEM: return ATK.ATK_ROLE_RADIO_MENU_ITEM;
						case ACC.ROLE_CLOCK: return ATK.ATK_ROLE_UNKNOWN;
						case ACC.ROLE_CALENDAR: return ATK.ATK_ROLE_CALENDAR;
						case ACC.ROLE_DATETIME: return ATK.ATK_ROLE_DATE_EDITOR;
						case ACC.ROLE_FOOTER: return ATK.ATK_ROLE_FOOTER;
						case ACC.ROLE_FORM: return ATK.ATK_ROLE_FORM;
						case ACC.ROLE_HEADER: return ATK.ATK_ROLE_HEADER;
						case ACC.ROLE_HEADING: return ATK.ATK_ROLE_HEADING;
						case ACC.ROLE_PAGE: return ATK.ATK_ROLE_PAGE;
						case ACC.ROLE_PARAGRAPH: return ATK.ATK_ROLE_PARAGRAPH;
						case ACC.ROLE_SECTION: return ATK.ATK_ROLE_SECTION;
						case ACC.ROLE_DOCUMENT: return ATK.ATK_ROLE_DOCUMENT_FRAME;
						case ACC.ROLE_GRAPHIC: return ATK.ATK_ROLE_IMAGE;
					}
				}
			}
		}
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.get_role == 0) return 0;
		return ATK.call (objectClass.get_role, atkObject);
	}

	/**
	 * Gets a reference to the accessible child whose parent is atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index the index of the child
	 *
	 * @return a pointer to the AtkObject of the child at the provided index
	 */
	static long atkObject_ref_child (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null && object.id == ACC.CHILDID_SELF) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent(accessible);
				event.childID = ACC.CHILDID_CHILD_AT_INDEX;
				event.detail = (int)index;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get(i);
					listener.getChild(event);
				}
				if (event.accessible != null) {
					AccessibleObject accObject = event.accessible.getAccessibleObject();
					if (accObject != null) {
						return OS.g_object_ref (accObject.atkHandle);
					}
				}
			}
			object.updateChildren ();
			AccessibleObject accObject = object.getChildByIndex ((int)index);
			if (accObject != null) {
				return OS.g_object_ref (accObject.atkHandle);
			}
		}
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.ref_child == 0) return 0;
		return ATK.call (objectClass.ref_child, atkObject, index);
	}

	/**
	 * Gets a reference to the state set of the accessible associated with
	 * atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the AtkStateSet for the accessible widget atkObject
	 */
	static long atkObject_ref_state_set (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkObjectClass objectClass = getParentAtkObjectClass ();
		if (objectClass.ref_state_set != 0) {
			parentResult = ATK.call (objectClass.ref_state_set, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				long set = parentResult;
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = -1;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getState (event);
				}
				if (event.detail != -1) {
					/*	Convert from win32 state values to atk state values */
					int state = event.detail;
					if ((state & ACC.STATE_BUSY) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_BUSY);
					if ((state & ACC.STATE_CHECKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_CHECKED);
					if ((state & ACC.STATE_EXPANDED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EXPANDED);
					if ((state & ACC.STATE_FOCUSABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSABLE);
					if ((state & ACC.STATE_FOCUSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSED);
					if ((state & ACC.STATE_HOTTRACKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ARMED);
					if ((state & ACC.STATE_INVISIBLE) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_VISIBLE);
					if ((state & ACC.STATE_MULTISELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_MULTISELECTABLE);
					if ((state & ACC.STATE_OFFSCREEN) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SHOWING);
					if ((state & ACC.STATE_PRESSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_PRESSED);
					if ((state & ACC.STATE_READONLY) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EDITABLE);
					if ((state & ACC.STATE_SELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTABLE);
					if ((state & ACC.STATE_SELECTED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTED);
					if ((state & ACC.STATE_SIZEABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_RESIZABLE);
					if ((state & ACC.STATE_DISABLED) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ENABLED);
					if ((state & ACC.STATE_ACTIVE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ACTIVE);
					if ((state & ACC.STATE_SINGLELINE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SINGLE_LINE);
					if ((state & ACC.STATE_MULTILINE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_MULTI_LINE);
					if ((state & ACC.STATE_REQUIRED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_REQUIRED);
					if ((state & ACC.STATE_INVALID_ENTRY) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_INVALID_ENTRY);
					if ((state & ACC.STATE_SUPPORTS_AUTOCOMPLETION) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SUPPORTS_AUTOCOMPLETION);
					/* Note: STATE_COLLAPSED, STATE_LINKED and STATE_NORMAL have no ATK equivalents */
				}
				return set;
			}
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkSelectionIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkSelectionIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkSelectionIface getParentSelectionIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type() ;
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_SELECTION())) {
			AtkSelectionIface iface = new AtkSelectionIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Determines if the current child of this atkObject is selected.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index a long integer representing the index
	 *
	 * @return a long integer where 1 represents TRUE, 0 otherwise
	 */
	static long atkSelection_is_child_selected (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkSelectionIface iface = getParentSelectionIface (atkObject);
		if (iface != null && iface.is_child_selected != 0) {
			parentResult = ATK.call (iface.is_child_selected, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getSelection (event);
				}
				Accessible result = event.accessible;
				AccessibleObject accessibleObject = result != null ? result.getAccessibleObject() : object.getChildByID (event.childID);
				if (accessibleObject != null) {
					return accessibleObject.index == index ? 1 : 0;
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets a reference to the atkObject representing the specified
	 * selected child of the object.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index a long integer representing the index
	 *
	 * @return a pointer to the AtkObject representing the selected accessible,
	 * or 0
	 */
	static long atkSelection_ref_selection (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkSelectionIface iface = getParentSelectionIface (atkObject);
		if (iface != null && iface.ref_selection != 0) {
			parentResult = ATK.call (iface.ref_selection, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = listeners.get (i);
					listener.getSelection (event);
				}
				AccessibleObject accObj = object.getChildByID (event.childID);
				if (accObj != null) {
					if (parentResult != 0) OS.g_object_unref (parentResult);
					OS.g_object_ref (accObj.atkHandle);
					return accObj.atkHandle;
				}
			}
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkTableIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkTableIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkTableIface getParentTableIface (long atkObject) {
		long type =  OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_TABLE())) {
			AtkTableIface iface = new AtkTableIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_TABLE_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Get a reference to the table cell at row, column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing a row in atkObject
	 * @param column a long integer representing a column in atkObject
	 *
	 * @return a pointer to an AtkObject representing the specified table
	 */
	static long atkTable_ref_at (long atkObject, long row, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					AccessibleObject accessibleObject = result.getAccessibleObject();
					OS.g_object_ref(accessibleObject.atkHandle);
					return accessibleObject.atkHandle;
				}
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.ref_at != 0) {
			parentResult = ATK.call (iface.ref_at, atkObject, row, column);
		}
		return parentResult;
	}

	/**
	 * Get a reference to the table cell at row, column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing a row in atkObject
	 * @param column a long integer representing a column in atkObject
	 *
	 * @return a pointer to an AtkObject representing the specified table
	 */
	static long atkTable_get_index_at (long atkObject, long row, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result == null) return -1;
				event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getColumnCount(event);
				}
				return row * event.count + column;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_index_at != 0) {
			parentResult = ATK.call (iface.get_index_at, atkObject, row, column);
		}
		return parentResult;
	}

	/**
	 * Gets a gint representing the column at the specified index.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index a long integer representing an index in the table
	 *
	 * @return a long integer representing the column at the specified index, or
	 * -1
	 */
	static long atkTable_get_column_at_index (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getColumnCount(event);
				}
				long result = event.count == 0 ? -1 : index % event.count;
				return result;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_column_at_index != 0) {
			parentResult = ATK.call (iface.get_column_at_index, atkObject, index);
		}
		return parentResult;
	}

	/**
	 * Gets a gint representing the row at the specified index.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param index a long integer representing an index in the table
	 *
	 * @return a long integer representing the row at the specified index, or
	 * -1
	 */
	static long atkTable_get_row_at_index (long atkObject, long index) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getColumnCount(event);
				}
				long result = event.count == 0 ? -1 : index / event.count;
				return result;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_row_at_index != 0) {
			parentResult = ATK.call (iface.get_row_at_index, atkObject, index);
		}
		return parentResult;
	}

	/**
	 * Gets the number of columns in the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a long integer representing the number of columns, or
	 * 0
	 */
	static long atkTable_get_n_columns (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_n_columns != 0) {
			parentResult = ATK.call (iface.get_n_columns, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.count = (int)parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getColumnCount(event);
					parentResult = event.count;
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the number of rows in the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a long integer representing the number of rows, or
	 * 0
	 */
	static long atkTable_get_n_rows (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_n_rows != 0) {
			parentResult = ATK.call (iface.get_n_rows, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.count = (int)parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getRowCount(event);
					parentResult = event.count;
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the number of columns occupied at the specified row
	 * and column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 * @param column a long integer representing the column
	 *
	 * @return a long integer representing the column extent at the
	 * specified position, or 0
	 */
	static long atkTable_get_column_extent_at (long atkObject, long row, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_column_extent_at != 0) {
			parentResult = ATK.call (iface.get_column_extent_at, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					List<AccessibleTableCellListener> listeners2 = result.accessibleTableCellListeners;
					length = size(listeners2);
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.count = (int)parentResult;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = listeners2.get(i);
							listener.getColumnSpan(cellEvent);
						}
						return cellEvent.count;
					}
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the number of rows occupied at the specified row
	 * and column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 * @param column a long integer representing the column
	 *
	 * @return a long integer representing the row extent at the specified
	 * position, or 0
	 */
	static long atkTable_get_row_extent_at (long atkObject, long row, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_row_extent_at != 0) {
			parentResult = ATK.call (iface.get_row_extent_at, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					List<AccessibleTableCellListener> listeners2 = result.accessibleTableCellListeners;
					length = size(listeners2);
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.count = (int)parentResult;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = listeners2.get(i);
							listener.getRowSpan(cellEvent);
						}
						return cellEvent.count;
					}
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the caption for the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the AtkObject representing the caption, or 0
	 */
	static long atkTable_get_caption (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCaption(event);
				}
				Accessible result = event.accessible;
				if (result != null) return result.getAccessibleObject().atkHandle;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_caption != 0) {
			parentResult = ATK.call (iface.get_caption, atkObject);
		}
		return parentResult;
	}

	/**
	 * Gets the summary for the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a pointer to the AtkObject representing the summary, or 0
	 */
	static long atkTable_get_summary (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getSummary(event);
				}
				Accessible result = event.accessible;
				if (result != null) return result.getAccessibleObject().atkHandle;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_summary != 0) {
			parentResult = ATK.call (iface.get_summary, atkObject);
		}
		return parentResult;
	}

	/**
	 * Gets the description text of the specified column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param column a long integer representing the specified column
	 *
	 * @return a pointer to the gchar representation of the column description,
	 * or 0
	 */
	static long atkTable_get_column_description (long atkObject, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_column_description != 0) {
			parentResult = ATK.call (iface.get_column_description, atkObject, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)column;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getColumnDescription(event);
				}
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	/**
	 * Gets the column header of a specified column.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param column a long integer representing the specified column
	 *
	 * @return a pointer to the AtkObject representing the specified column
	 * header, or 0
	 */
	static long atkTable_get_column_header (long atkObject, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getRowHeaderCells(event);
				}
				Accessible[] accessibles = event.accessibles;
				if (accessibles != null) {
					if (0 <= column && column < accessibles.length) {
						return accessibles[(int)column].getAccessibleObject().atkHandle;
					}
				}
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_column_header != 0) {
			parentResult = ATK.call (iface.get_column_header, atkObject, column);
		}
		return parentResult;
	}

	/**
	 * Gets the description text of the specified row.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the specified row
	 *
	 * @return a pointer to the gchar representation of the row description,
	 * or 0
	 */
	static long atkTable_get_row_description (long atkObject, long row) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_row_description != 0) {
			parentResult = ATK.call (iface.get_row_description, atkObject, row);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getRowDescription(event);
				}
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	/**
	 * Gets the column header of a specified row.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the specified row
	 *
	 * @return a pointer to the AtkObject representing the specified row
	 * header, or 0
	 */
	static long atkTable_get_row_header (long atkObject, long row) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getRowHeaderCells(event);
				}
				Accessible[] accessibles = event.accessibles;
				if (accessibles != null) {
					if (0 <= row && row < accessibles.length) {
						return accessibles[(int)row].getAccessibleObject().atkHandle;
					}
				}
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_row_header != 0) {
			parentResult = ATK.call (iface.get_row_header, atkObject, row);
		}
		return parentResult;
	}

	/**
	 * Gets the selected columns of the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param selected an array of integer pointers that is to contain the
	 * selected column numbers
	 *
	 * @return a long integer representing the number of columns selected,
	 * or 0
	 */
	static long atkTable_get_selected_columns (long atkObject, long selected) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getSelectedColumns(event);
				}
				int count = event.selected != null ? event.selected.length : 0;
				long result = OS.g_malloc(count * 4);
				if (event.selected != null) C.memmove(result, event.selected, count * 4);
				if (selected != 0) C.memmove(selected, new long []{result}, C.PTR_SIZEOF);
				return count;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_selected_columns != 0) {
			parentResult = ATK.call (iface.get_selected_columns, atkObject, selected);
		}
		return parentResult;
	}

	/**
	 * Gets the selected rows of the table.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param selected an array of integer pointers that is to contain the
	 * selected row numbers
	 *
	 * @return a long integer representing the number of rows selected,
	 * or 0
	 */
	static long atkTable_get_selected_rows (long atkObject, long selected) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getSelectedRows(event);
				}
				int count = event.selected != null ? event.selected.length : 0;
				long result = OS.g_malloc(count * 4);
				if (event.selected != null) C.memmove(result, event.selected, count * 4);
				if (selected != 0) C.memmove(selected, new long []{result}, C.PTR_SIZEOF);
				return count;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.get_selected_rows != 0) {
			parentResult = ATK.call (iface.get_selected_rows, atkObject, selected);
		}
		return parentResult;
	}

	/**
	 * Determines if the specified column is selected.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param column a long integer representing the column
	 *
	 * @return a long integer where 1 represents TRUE, 0 otherwise
	 */
	static long atkTable_is_column_selected (long atkObject, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.is_column_selected != 0) {
			parentResult = ATK.call (iface.is_column_selected, atkObject, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.isSelected = parentResult != 0;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.isColumnSelected(event);
				}
				return event.isSelected ? 1 : 0;
			}
		}
		return parentResult;
	}

	/**
	 * Determines if the specified row is selected.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 *
	 * @return a long integer where 1 represents TRUE, 0 otherwise
	 */
	static long atkTable_is_row_selected (long atkObject, long row) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.is_row_selected != 0) {
			parentResult = ATK.call (iface.is_row_selected, atkObject, row);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.isSelected = parentResult != 0;
				event.row = (int)row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.isRowSelected(event);
				}
				return event.isSelected ? 1 : 0;
			}
		}
		return parentResult;
	}

	/**
	 * Determines if the AtkObject at the specified
	 * column and row is selected.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 * @param column a long integer representing the column
	 *
	 * @return a long integer where 1 represents TRUE, 0 otherwise
	 */
	static long atkTable_is_selected (long atkObject, long row, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.is_selected != 0) {
			parentResult = ATK.call (iface.is_selected, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					List<AccessibleTableCellListener> listeners2 = result.accessibleTableCellListeners;
					length = size(listeners2);
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.isSelected = parentResult != 0;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = listeners2.get(i);
							listener.isSelected(cellEvent);
						}
						return cellEvent.isSelected ? 1 : 0;
					}
				}
			}
		}
		return parentResult;
	}

	/**
	 * Adds the specified row to the selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 *
	 * @return a long int representing whether the action succeeded: 1 for success,
	 * 0 for failure
	 */
	static long atkTable_add_row_selection (long atkObject, long row) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.selectRow(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.add_row_selection != 0) {
			parentResult = ATK.call (iface.add_row_selection, atkObject, row);
		}
		return parentResult;
	}

	/**
	 * Removes the specified row to the selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param row a long integer representing the row
	 *
	 * @return a long int representing whether the action succeeded: 1 for success,
	 * 0 for failure
	 */
	static long atkTable_remove_row_selection (long atkObject, long row) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.deselectRow(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.remove_row_selection != 0) {
			parentResult = ATK.call (iface.remove_row_selection, atkObject, row);
		}
		return parentResult;
	}

	/**
	 * Adds the specified column to the selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param column a long integer representing the column
	 *
	 * @return a long int representing whether the action succeeded: 1 for success,
	 * 0 for failure
	 */
	static long atkTable_add_column_selection (long atkObject, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.selectColumn(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.add_column_selection != 0) {
			parentResult = ATK.call (iface.add_column_selection, atkObject, column);
		}
		return parentResult;
	}

	/**
	 * Removes the specified column to the selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param column a long integer representing the column
	 *
	 * @return a long int representing whether the action succeeded: 1 for success,
	 * 0 for failure
	 */
	static long atkTable_remove_column_selection (long atkObject, long column) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTableListener> listeners = accessible.accessibleTableListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = listeners.get(i);
					listener.deselectColumn(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTableIface iface = getParentTableIface (atkObject);
		if (iface != null && iface.remove_column_selection != 0) {
			parentResult = ATK.call (iface.remove_column_selection, atkObject, column);
		}
		return parentResult;
	}

	/**
	 * Fills a Java AtkTextIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkTextIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkTextIface getParentTextIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_TEXT())) {
			AtkTextIface iface = new AtkTextIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	static String getString (long strPtr) {
		int length = C.strlen (strPtr);
		byte [] buffer = new byte [length];
		C.memmove (buffer, strPtr, length);
		return new String (Converter.mbcsToWcs (buffer));
	}

	static long getStringPtr (String str) {
		byte [] buffer = Converter.wcsToMbcs(str != null ? str : "", true);
		long ptr = OS.g_malloc(buffer.length);
		C.memmove(ptr, buffer, buffer.length);
		return ptr;
	}

	/**
	 * Get the bounding box containing the glyph representing the character
	 * at a particular text offset.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset the offset of the text character for which bounding info
	 * is required
	 * @param x the pointer for the x coordinate of the bounding box
	 * @param y the pointer for the y coordinate of the bounding box
	 * @param width the pointer for the width of the bounding box
	 * @param height the pointer for the height of the bounding box
	 * @param coords long int representing the AtkCoordType for the coordinates
	 *
	 * @return a long int representation of 0 indicating that the method completed
	 * successfully
	 */
	static long atkText_get_character_extents (long atkObject, long offset,
			long x, long y, long width, long height, long coords) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)offset;
				event.end = (int)(offset + 1);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getTextBounds(event);
				}
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coords == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				C.memmove (x, new int[]{event.x}, 4);
				C.memmove (y, new int[]{event.y}, 4);
				C.memmove (width, new int[]{event.width}, 4);
				C.memmove (height, new int[]{event.height}, 4);
				return 0;
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_character_extents != 0) {
			OS.call (iface.get_character_extents, atkObject, offset, x, y, width, height, coords);
		}
		return 0;
	}

	/**
	 * Get the bounding box for text within the specified range.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_offset the offset of the first text character for which
	 * bounding info is required
	 * @param end_offset the offset of the last text character after the last
	 * character for which boundary info is required
	 * @param coord_type long int representing the AtkCoordType for the coordinates
	 * @param rect a pointer to the AtkTextRectangle which is filled by this function
	 *
	 * @return a long int representation of 0 indicating that the method completed
	 * successfully
	 */
	static long atkText_get_range_extents (long atkObject, long start_offset,
			long end_offset, long coord_type, long rect) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)start_offset;
				event.end = (int)end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getTextBounds(event);
				}
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				AtkTextRectangle atkRect = new AtkTextRectangle();
				atkRect.x = event.x;
				atkRect.y = event.y;
				atkRect.width = event.width;
				atkRect.height = event.height;
				ATK.memmove (rect, atkRect, AtkTextRectangle.sizeof);
				return 0;
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_range_extents != 0) {
			ATK.call (iface.get_range_extents, atkObject, start_offset, end_offset, coord_type, rect);
		}
		return 0;
	}

	/**
	 * Creates an AtkAttributeSet which consists of the attributes explicitly
	 * set at the position offset in the text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset the offset at which to get the attributes
	 * @param start_offset the address to put the start offset of the range
	 * @param end_offset the address to put the end offset of the range
	 *
	 * @return a pointer to the AtkAttributeSet created
	 */
	static long atkText_get_run_attributes (long atkObject, long offset,
			long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleAttributeListener> listeners = accessible.accessibleAttributeListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextAttributeEvent event = new AccessibleTextAttributeEvent(accessible);
				event.offset = (int)offset;
				for (int i = 0; i < length; i++) {
					AccessibleAttributeListener listener = listeners.get(i);
					listener.getTextAttributes(event);
				}
				C.memmove (start_offset, new int []{event.start}, 4);
				C.memmove (end_offset, new int []{event.end}, 4);
				TextStyle style = event.textStyle;
				long result = 0;
				AtkAttribute attr = new AtkAttribute();
				if (style != null) {
					if (style.rise != 0) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_RISE));
						attr.value = getStringPtr (String.valueOf(style.rise));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
					if (style.underline) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_UNDERLINE));
						String str = "none"; //$NON-NLS-1$
						switch (style.underlineStyle) {
							case SWT.UNDERLINE_DOUBLE: str = "double"; break; //$NON-NLS-1$
							case SWT.UNDERLINE_SINGLE: str = "single"; break; //$NON-NLS-1$
							case SWT.UNDERLINE_ERROR: str = "error"; break; //$NON-NLS-1$
							case SWT.UNDERLINE_SQUIGGLE: str = "squiggle"; break; //$NON-NLS-1$
						}
						attr.value = getStringPtr (str);
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
					if (style.strikeout) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRIKETHROUGH));
						attr.value = getStringPtr ("1");
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
					Font font = style.font;
					if (font != null && !font.isDisposed()) {
						//TODO language and direction
						long attrPtr;
						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FAMILY_NAME));
						attr.value = OS.g_strdup (OS.pango_font_description_get_family (font.handle));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);

						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_SIZE));
						attr.value = getStringPtr (String.valueOf (OS.pango_font_description_get_size(font.handle) / OS.PANGO_SCALE));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);

						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STYLE));
						attr.value = OS.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_STYLE, OS.pango_font_description_get_style(font.handle)));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);

						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_VARIANT));
						attr.value = OS.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_VARIANT, OS.pango_font_description_get_variant(font.handle)));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);

						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRETCH));
						attr.value = OS.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_STRETCH, OS.pango_font_description_get_stretch(font.handle)));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);

						attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_WEIGHT));
						attr.value = getStringPtr (String.valueOf (OS.pango_font_description_get_weight(font.handle)));
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
					Color color = style.foreground;
					if (color != null && !color.isDisposed()) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FG_COLOR));
						attr.value = getStringPtr ((color.handle.red * 255) + "," + (color.handle.green * 255) + "," + (color.handle.blue * 255)); //$NON-NLS-1$ //$NON-NLS-2$
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
					color = style.background;
					if (color != null && !color.isDisposed()) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = OS.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_BG_COLOR));
						attr.value = getStringPtr ((color.handle.red * 255) + "," + (color.handle.green * 255) + "," + (color.handle.blue * 255)); //$NON-NLS-1$ //$NON-NLS-2$
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
				}
				if (event.attributes != null) {
					int end = event.attributes.length / 2 * 2;
					for (int i = 0; i < end; i+= 2) {
						long attrPtr = OS.g_malloc(AtkAttribute.sizeof);
						attr.name = getStringPtr (event.attributes[i]);
						attr.value = getStringPtr (event.attributes[i + 1]);
						ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
						result = OS.g_slist_append(result, attrPtr);
					}
				}
				return result;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_run_attributes != 0) {
			parentResult = OS.call (iface.get_run_attributes, atkObject, offset, start_offset, end_offset);
		}
		return parentResult;
	}

	/**
	 * Gets the offset of the character located at coordinates x and y.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param x screen x-position of the character
	 * @param y screen y-position of the character
	 * @param coords long int representing the AtkCoordType for the coordinates
	 *
	 * @return the offset to the character which is located at the specified
	 * x and y coordinates
	 */
	static long atkText_get_offset_at_point (long atkObject, long x,
			long y, long coords) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.x = (int)x;
				event.y = (int)y;
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coords == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0];
				}
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getOffsetAtPoint(event);
				}
				return event.offset;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_offset_at_point != 0) {
			parentResult = OS.call (iface.get_offset_at_point, atkObject, x, y, coords);
		}
		return parentResult;
	}

	/**
	 * Adds a selection bounded by the specified offsets.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_offset the start position of the selected region
	 * @param end_offset the offset of the first character after the selected region
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkText_add_selection (long atkObject, long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)start_offset;
				event.end = (int)end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.addSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.add_selection != 0) {
			parentResult = ATK.call (iface.add_selection, atkObject, start_offset, end_offset);
		}
		return parentResult;
	}

	/**
	 * Removes the specified selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param selection_num the selection number.
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkText_remove_selection (long atkObject, long selection_num) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)selection_num;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.removeSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.remove_selection != 0) {
			parentResult = ATK.call (iface.remove_selection, atkObject, selection_num);
		}
		return parentResult;
	}

	/**
	 * Sets the caret (cursor) position to the specified offset.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset the position
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkText_set_caret_offset (long atkObject, long offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.offset = (int)offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.setCaretOffset(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.set_caret_offset != 0) {
			return ATK.call (iface.set_caret_offset, atkObject, offset);
		}
		return 0;
	}

	/**
	 * Changes the start and end offset of the specified selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param selection_num the selection number
	 * @param start_offset the new start position of the selection
	 * @param end_offset the new end position of the selection
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkText_set_selection (long atkObject, long selection_num,
			long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)selection_num;
				event.start = (int)start_offset;
				event.end = (int)end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.setSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.set_selection != 0) {
			parentResult = OS.call (iface.set_selection, atkObject, selection_num, start_offset, end_offset);
		}
		return parentResult;
	}

	/**
	 * Gets the offset position of the caret (cursor).
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return a long int representation of the offset position of the caret (cursor)
	 */
	static long atkText_get_caret_offset (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_caret_offset != 0) {
			parentResult = ATK.call (iface.get_caret_offset, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getCaretOffset (event);
				}
				return event.offset;
			}
			List<AccessibleTextListener> listeners2 = accessible.accessibleTextListeners;
			length = size(listeners2);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
				event.childID = object.id;
				event.offset = (int)parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTextListener listener = listeners2.get(i);
					listener.getCaretOffset (event);
				}
				return event.offset;
			}
		}
		return parentResult;
	}

	/**
	 * Get the ranges of text in the specified bounding box.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param rect the AtkTextRectangle giving the dimensions of the bounding box
	 * @param coord_type long int representing the AtkCoordType for the coordinates
	 * @param x_clip_type a long int representing the AtkTextClipType of the
	 * horizontal clip
	 * @param y_clip_type a long int representing the AtkTextClipType of the
	 * vertical clip
	 *
	 * @return a pointer to the array of AtkTextRanges
	 */
	static long atkText_get_bounded_ranges (long atkObject, long rect,
			long coord_type, long x_clip_type, long y_clip_type) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				AtkTextRectangle atkRect = new AtkTextRectangle();
				ATK.memmove (atkRect, rect, AtkTextRectangle.sizeof);
				event.x = atkRect.x;
				event.y = atkRect.y;
				event.width = atkRect.width;
				event.height = atkRect.height;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getRanges (event);
				}
				int [] ranges = event.ranges;
				int size = ranges == null ? 1 : ranges.length / 2;
				long result = C.malloc(size * AtkTextRange.sizeof);
				AtkTextRange range = new AtkTextRange();
				for (int j = 0, end = (ranges != null ? ranges.length / 2 : 1); j < end; j++) {
					if (ranges != null) {
						int index = j * 2;
						event.start = ranges[index];
						event.end = ranges[index+1];
					}
					event.count = 0;
					event.type = ACC.TEXT_BOUNDARY_ALL;
					for (int i = 0; i < length; i++) {
						AccessibleTextExtendedListener listener = listeners.get(i);
						listener.getText(event);
					}
					range.start_offset = event.start;
					range.end_offset = event.end;
					range.content = getStringPtr (event.result);
					event.result = null;
					event.count = event.type = event.x = event.y = event.width = event.height = 0;
					for (int i = 0; i < length; i++) {
						AccessibleTextExtendedListener listener = listeners.get(i);
						listener.getTextBounds(event);
					}
					range.bounds.x = event.x;
					range.bounds.y = event.y;
					range.bounds.width = event.width;
					range.bounds.height = event.height;
					ATK.memmove(result + j * AtkTextRange.sizeof, range, AtkTextRange.sizeof);
				}
				return result;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_bounded_ranges != 0) {
			parentResult = ATK.call (iface.get_bounded_ranges, atkObject);
		}
		return parentResult;
	}

	/**
	 * Gets the specified text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset position
	 *
	 * @return a long int representing the character at offset
	 */
	static long atkText_get_character_at_offset (long atkObject, long offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)offset;
				event.end = (int)(offset + 1);
				event.type = ACC.TEXT_BOUNDARY_CHAR;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				String text = event.result;
				return text != null && text.length() > 0 ? text.charAt(0) : 0;
			}
			String text = object.getText ();
			if (text != null && text.length() > offset) return text.charAt ((int)offset);
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_character_at_offset != 0) {
			return ATK.call (iface.get_character_at_offset, atkObject, offset);
		}
		return 0;
	}

	/**
	 * Gets the character count.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return the number of characters
	 */
	static long atkText_get_character_count (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getCharacterCount(event);
				}
				return event.count;
			}
			String text = object.getText ();
			if (text != null) return text.length ();
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_character_count != 0) {
			return ATK.call (iface.get_character_count, atkObject);
		}
		return 0;
	}

	/**
	 * Gets the number of selected regions.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return the number of selected regions, or -1 if a failure occurred
	 */
	static long atkText_get_n_selections (long atkObject) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getSelectionCount (event);
				}
				return event.count;
			}
			List<AccessibleTextListener> listeners2 = accessible.accessibleTextListeners;
			length = size(listeners2);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleTextListener listener = listeners2.get(i);
					listener.getSelectionRange (event);
				}
				if (event.length > 0) return 1;
			}
		}
		long parentResult = 0;
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_n_selections != 0) {
			parentResult = ATK.call (iface.get_n_selections, atkObject);
		}
		return parentResult;
	}

	/**
	 * Gets the text from the specified selection.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param selection_num the selection number
	 * @param start_offset passes back the start position of the selected region
	 * @param end_offset passes back the end position of the selected region
	 *
	 * @return a pointer to the newly allocated string containing the selected text
	 */
	static long atkText_get_selection (long atkObject, long selection_num,
			long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		long parentResult = 0;
		C.memmove (start_offset, new int[] {0}, 4);
		C.memmove (end_offset, new int[] {0}, 4);
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_selection != 0) {
			parentResult = OS.call (iface.get_selection, atkObject, selection_num, start_offset, end_offset);
		}
		if (object != null) {
			int[] parentStart = new int [1];
			int[] parentEnd = new int [1];
			C.memmove (parentStart, start_offset, 4);
			C.memmove (parentEnd, end_offset, 4);
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)selection_num;
				event.start = parentStart[0];
				event.end = parentEnd[0];
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getSelection (event);
				}
				parentStart [0] = event.start;
				parentEnd [0] = event.end;
				C.memmove (start_offset, parentStart, 4);
				C.memmove (end_offset, parentEnd, 4);
				event.count = event.index = 0;
				event.type = ACC.TEXT_BOUNDARY_ALL;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				if (parentResult != 0) OS.g_free(parentResult);
				return getStringPtr (event.result);
			}
			if (selection_num == 0) {
				List<AccessibleTextListener> listeners2 = accessible.accessibleTextListeners;
				length = size(listeners2);
				if (length > 0) {
					AccessibleTextEvent event = new AccessibleTextEvent (accessible);
					event.childID = object.id;
					event.offset = parentStart [0];
					event.length = parentEnd [0] - parentStart [0];
					for (int i = 0; i < length; i++) {
						AccessibleTextListener listener = listeners2.get(i);
						listener.getSelectionRange (event);
					}
					C.memmove (start_offset, new int[] {event.offset}, 4);
					C.memmove (end_offset, new int[] {event.offset + event.length}, 4);
					if (parentResult != 0) OS.g_free(parentResult);
					String text = object.getText();
					if (text != null && text.length () > event.offset && text.length() >= event.offset + event.length) {
						return getStringPtr (text.substring(event.offset, event.offset + event.length));
					}
					if (iface != null && iface.get_text != 0) {
						return ATK.call (iface.get_text, atkObject, event.offset, event.offset + event.length);
					}
					return 0;
				}
			}
		}
		return parentResult;
	}

	/**
	 * Gets the specified text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param start_offset start position
	 * @param end_offset end position, or -1 for the end of the string
	 *
	 * @return a pointer to the newly allocated string containing the text
	 * from start_offset up to (but not including) end_offset
	 */
	static long atkText_get_text (long atkObject, long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)start_offset;
				event.end = (int)end_offset;
				event.type = ACC.TEXT_BOUNDARY_ALL;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				return getStringPtr (event.result);
			}
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				if (end_offset == -1) {
					end_offset = text.length ();
				} else {
					end_offset = Math.min (end_offset, text.length ());
				}
				start_offset = Math.min (start_offset, end_offset);
				text = text.substring ((int)start_offset, (int)end_offset);
				return getStringPtr (text);
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_text != 0) {
			return ATK.call (iface.get_text, atkObject, start_offset, end_offset);
		}
		return 0;
	}

	/**
	 * Gets the specified text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset position
	 * @param boundary_type the AtkTextBoundary
	 * @param start_offset the start offset of the returned string
	 * @param end_offset the end_offset of the first character after the returned string
	 *
	 * @return a pointer to the newly allocated string containing
	 * the text after offset bounded by the specified boundary_type
	 */
	static long atkText_get_text_after_offset (long atkObject, long offset_value,
			long boundary_type, long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				long charCount = atkText_get_character_count (atkObject);
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)offset_value;
				event.count = 1;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break;
				}
				int eventStart = event.start;
				int eventEnd = event.end;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_WORD_START:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START:
					case ATK.ATK_TEXT_BOUNDARY_LINE_START:
						if (event.end < charCount) {
							int start = event.start;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = 2;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.end = event.start;
							event.start = start;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END:
					case ATK.ATK_TEXT_BOUNDARY_LINE_END:
						if (0 < event.start) {
							int end = event.end;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.start = event.end;
							event.end = end;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
				}
				C.memmove (start_offset, new int[] {event.start}, 4);
				C.memmove (end_offset, new int[] {event.end}, 4);
				return getStringPtr (event.result);
			}
			int offset = (int)offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
				offset = Math.min (offset, length - 1);
				int startBounds = offset;
				int endBounds = offset;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: {
						if (length > offset) endBounds++;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
						int wordStart1 = nextIndexOfChar (text, " !?.\n", offset - 1);
						if (wordStart1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						wordStart1 = nextIndexOfNotChar (text, " !?.\n", wordStart1);
						if (wordStart1 == length) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = wordStart1;
						int wordStart2 = nextIndexOfChar (text, " !?.\n", wordStart1);
						if (wordStart2 == -1) {
							endBounds = length;
							break;
						}
						endBounds = nextIndexOfNotChar (text, " !?.\n", wordStart2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
						int previousWordEnd = previousIndexOfNotChar (text, " \n", offset);
						if (previousWordEnd == -1 || previousWordEnd != offset - 1) {
							offset = nextIndexOfNotChar (text, " \n", offset);
						}
						if (offset == -1) {
							startBounds = endBounds = length;
							break;
						}
						int wordEnd1 = nextIndexOfChar (text, " !?.\n", (int)offset);
						if (wordEnd1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						wordEnd1 = nextIndexOfNotChar (text, "!?.", wordEnd1);
						if (wordEnd1 == length) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = wordEnd1;
						int wordEnd2 = nextIndexOfNotChar (text, " \n", wordEnd1);
						if (wordEnd2 == length) {
							startBounds = endBounds = length;
							break;
						}
						wordEnd2 = nextIndexOfChar (text, " !?.\n", wordEnd2);
						if (wordEnd2 == -1) {
							endBounds = length;
							break;
						}
						endBounds = nextIndexOfNotChar (text, "!?.", wordEnd2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
						int previousSentenceEnd = previousIndexOfChar (text, "!?.", offset);
						int previousText = previousIndexOfNotChar (text, " !?.\n", offset);
						int sentenceStart1 = 0;
						if (previousSentenceEnd >= previousText) {
							sentenceStart1 = nextIndexOfNotChar (text, " !?.\n", offset);
						} else {
							sentenceStart1 = nextIndexOfChar (text, "!?.", offset);
							if (sentenceStart1 == -1) {
								startBounds = endBounds = length;
								break;
							}
							sentenceStart1 = nextIndexOfNotChar (text, " !?.\n", sentenceStart1);
						}
						if (sentenceStart1 == length) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = sentenceStart1;
						int sentenceStart2 = nextIndexOfChar (text, "!?.", sentenceStart1);
						if (sentenceStart2 == -1) {
							endBounds = length;
							break;
						}
						endBounds = nextIndexOfNotChar (text, " !?.\n", sentenceStart2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
						int sentenceEnd1 = nextIndexOfChar (text, "!?.", offset);
						if (sentenceEnd1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						sentenceEnd1 = nextIndexOfNotChar (text, "!?.", sentenceEnd1);
						if (sentenceEnd1 == length) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = sentenceEnd1;
						int sentenceEnd2 = nextIndexOfNotChar (text, " \n", sentenceEnd1);
						if (sentenceEnd2 == length) {
							startBounds = endBounds = length;
							break;
						}
						sentenceEnd2 = nextIndexOfChar (text, "!?.", sentenceEnd2);
						if (sentenceEnd2 == -1) {
							endBounds = length;
							break;
						}
						endBounds = nextIndexOfNotChar (text, "!?.", sentenceEnd2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
						int lineStart1 = text.indexOf ('\n', offset - 1);
						if (lineStart1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						lineStart1 = nextIndexOfNotChar (text, "\n", lineStart1);
						if (lineStart1 == length) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = lineStart1;
						int lineStart2 = text.indexOf ('\n', lineStart1);
						if (lineStart2 == -1) {
							endBounds = length;
							break;
						}
						lineStart2 = nextIndexOfNotChar (text, "\n", lineStart2);
						endBounds = lineStart2;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
						int lineEnd1 = nextIndexOfChar (text, "\n", offset);
						if (lineEnd1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						startBounds = lineEnd1;
						if (startBounds == length) {
							endBounds = length;
							break;
						}
						int lineEnd2 = nextIndexOfChar (text, "\n", lineEnd1 + 1);
						if (lineEnd2 == -1) {
							endBounds = length;
							break;
						}
						endBounds = lineEnd2;
						break;
					}
				}
				C.memmove (start_offset, new int[] {startBounds}, 4);
				C.memmove (end_offset, new int[] {endBounds}, 4);
				text = text.substring (startBounds, endBounds);
				return getStringPtr (text);
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_text_after_offset != 0) {
			return ATK.call (iface.get_text_after_offset, atkObject, offset_value, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	/**
	 * Gets the specified text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset position
	 * @param boundary_type the AtkTextBoundary
	 * @param start_offset the start offset of the returned string
	 * @param end_offset the end_offset of the first character after the returned string
	 *
	 * @return a pointer to the newly allocated string containing the
	 * text at offset bounded by the specified boundary_type
	 */
	static long atkText_get_text_at_offset (long atkObject, long offset_value,
			long boundary_type, long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				long charCount = atkText_get_character_count (atkObject);
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)offset_value;
				event.count = 0;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break;
				}
				int eventStart = event.start;
				int eventEnd = event.end;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_WORD_START:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START:
					case ATK.ATK_TEXT_BOUNDARY_LINE_START:
						if (event.end < charCount) {
							int start = event.start;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = 1;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.end = event.start;
							event.start = start;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END:
					case ATK.ATK_TEXT_BOUNDARY_LINE_END:
						if (0 < event.start) {
							int end = event.end;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = -1;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.start = event.end;
							event.end = end;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
				}
				C.memmove (start_offset, new int[] {event.start}, 4);
				C.memmove (end_offset, new int[] {event.end}, 4);
				return getStringPtr (event.result);
			}
			int offset = (int)offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
				offset = Math.min (offset, length - 1);
				int startBounds = offset;
				int endBounds = offset;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: {
						if (length > offset) endBounds++;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
						int wordStart1 = previousIndexOfNotChar (text, " !?.\n", offset);
						if (wordStart1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						wordStart1 = previousIndexOfChar (text, " !?.\n", wordStart1) + 1;
						if (wordStart1 == -1) {
							startBounds = 0;
							break;
						}
						startBounds = wordStart1;
						int wordStart2 = nextIndexOfChar (text, " !?.\n", wordStart1);
						endBounds = nextIndexOfNotChar (text, " !?.\n", wordStart2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
						int wordEnd1 = previousIndexOfNotChar (text, "!?.", offset + 1);
						wordEnd1 = previousIndexOfChar (text, " !?.\n", wordEnd1);
						wordEnd1 = previousIndexOfNotChar (text, " \n", wordEnd1 + 1);
						if (wordEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						startBounds = wordEnd1 + 1;
						int wordEnd2 = nextIndexOfNotChar (text, " \n", startBounds);
						if (wordEnd2 == length) {
							endBounds = startBounds;
							break;
						}
						wordEnd2 = nextIndexOfChar (text, " !?.\n", wordEnd2);
						if (wordEnd2 == -1) {
							endBounds = startBounds;
							break;
						}
						endBounds = nextIndexOfNotChar (text, "!?.", wordEnd2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
						int sentenceStart1 = previousIndexOfNotChar (text, " !?.\n", offset + 1);
						if (sentenceStart1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						sentenceStart1 = previousIndexOfChar (text, "!?.", sentenceStart1) + 1;
						startBounds = nextIndexOfNotChar (text, " \n", sentenceStart1);
						int sentenceStart2 = nextIndexOfChar (text, "!?.", startBounds);
						endBounds = nextIndexOfNotChar (text, " !?.\n", sentenceStart2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
						int sentenceEnd1 = previousIndexOfNotChar (text, "!?.", offset + 1);
						sentenceEnd1 = previousIndexOfChar (text, "!?.", sentenceEnd1);
						sentenceEnd1 = previousIndexOfNotChar (text, " \n", sentenceEnd1 + 1);
						if (sentenceEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						startBounds = sentenceEnd1 + 1;
						int sentenceEnd2 = nextIndexOfNotChar (text, " \n", startBounds);
						if (sentenceEnd2 == length) {
							endBounds = startBounds;
							break;
						}
						sentenceEnd2 = nextIndexOfChar (text, "!?.", sentenceEnd2);
						if (sentenceEnd2 == -1) {
							endBounds = startBounds;
							break;
						}
						endBounds = nextIndexOfNotChar (text, "!?.", sentenceEnd2);
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
						startBounds = previousIndexOfChar (text, "\n", offset) + 1;
						int lineEnd2 = nextIndexOfChar (text, "\n", startBounds);
						if (lineEnd2 < length) lineEnd2++;
						endBounds = lineEnd2;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
						int lineEnd1 = previousIndexOfChar (text, "\n", offset);
						if (lineEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						startBounds = lineEnd1;
						endBounds = nextIndexOfChar (text, "\n", lineEnd1 + 1);
					}
				}
				C.memmove (start_offset, new int[] {startBounds}, 4);
				C.memmove (end_offset, new int[] {endBounds}, 4);
				text = text.substring (startBounds, endBounds);
				return getStringPtr (text);
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_text_at_offset != 0) {
			return ATK.call (iface.get_text_at_offset, atkObject, offset_value, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	/**
	 * Gets the specified text.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param offset position
	 * @param boundary_type the AtkTextBoundary
	 * @param start_offset the start offset of the returned string
	 * @param end_offset the end_offset of the first character after the returned string
	 *
	 * @return a pointer to the newly allocated string containing
	 * the text before offset bounded by the specified boundary_type
	 */
	static long atkText_get_text_before_offset (long atkObject, long offset_value,
			long boundary_type, long start_offset, long end_offset) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
			int length = size(listeners);
			if (length > 0) {
				long charCount = atkText_get_character_count (atkObject);
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)offset_value;
				event.count = -1;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break;
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break;
				}
				int eventStart = event.start;
				int eventEnd = event.end;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = listeners.get(i);
					listener.getText(event);
				}
				C.memmove (start_offset, new int[] {event.start}, 4);
				C.memmove (end_offset, new int[] {event.end}, 4);
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_WORD_START:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START:
					case ATK.ATK_TEXT_BOUNDARY_LINE_START:
						if (event.end < charCount) {
							int start = event.start;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.end = event.start;
							event.start = start;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
					case ATK.ATK_TEXT_BOUNDARY_WORD_END:
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END:
					case ATK.ATK_TEXT_BOUNDARY_LINE_END:
						if (0 < event.start) {
							int end = event.end;
							event.start = eventStart;
							event.end = eventEnd;
							event.count = -2;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
							event.start = event.end;
							event.end = end;
							event.type = ACC.TEXT_BOUNDARY_ALL;
							event.count = 0;
							for (int i = 0; i < length; i++) {
								AccessibleTextExtendedListener listener = listeners.get(i);
								listener.getText(event);
							}
						}
						break;
				}
				return getStringPtr (event.result);
			}
			int offset = (int)offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
				offset = Math.min (offset, length - 1);
				int startBounds = offset;
				int endBounds = offset;
				switch ((int)boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: {
						if (length >= offset && offset > 0) startBounds--;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
						int wordStart1 = previousIndexOfChar (text, " !?.\n", offset - 1);
						if (wordStart1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						int wordStart2 = previousIndexOfNotChar (text, " !?.\n", wordStart1);
						if (wordStart2 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = wordStart1 + 1;
						startBounds = previousIndexOfChar (text, " !?.\n", wordStart2) + 1;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
						int wordEnd1 =previousIndexOfChar (text, " !?.\n", offset);
						if (wordEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						wordEnd1 = previousIndexOfNotChar (text, " \n", wordEnd1 + 1);
						if (wordEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = wordEnd1 + 1;
						int wordEnd2 = previousIndexOfNotChar (text, " !?.\n", endBounds);
						wordEnd2 = previousIndexOfChar (text, " !?.\n", wordEnd2);
						if (wordEnd2 == -1) {
							startBounds = 0;
							break;
						}
						startBounds = previousIndexOfNotChar (text, " \n", wordEnd2 + 1) + 1;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
						int sentenceStart1 = previousIndexOfChar (text, "!?.", offset);
						if (sentenceStart1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						int sentenceStart2 = previousIndexOfNotChar (text, "!?.", sentenceStart1);
						if (sentenceStart2 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = sentenceStart1 + 1;
						startBounds = previousIndexOfChar (text, "!?.", sentenceStart2) + 1;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
						int sentenceEnd1 = previousIndexOfChar (text, "!?.", offset);
						if (sentenceEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						sentenceEnd1 = previousIndexOfNotChar (text, " \n", sentenceEnd1 + 1);
						if (sentenceEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = sentenceEnd1 + 1;
						int sentenceEnd2 = previousIndexOfNotChar (text, "!?.", endBounds);
						sentenceEnd2 = previousIndexOfChar (text, "!?.", sentenceEnd2);
						if (sentenceEnd2 == -1) {
							startBounds = 0;
							break;
						}
						startBounds = previousIndexOfNotChar (text, " \n", sentenceEnd2 + 1) + 1;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
						int lineStart1 = previousIndexOfChar (text, "\n", offset);
						if (lineStart1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = lineStart1 + 1;
						startBounds = previousIndexOfChar (text, "\n", lineStart1) + 1;
						break;
					}
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
						int lineEnd1 = previousIndexOfChar (text, "\n", offset);
						if (lineEnd1 == -1) {
							startBounds = endBounds = 0;
							break;
						}
						endBounds = lineEnd1;
						startBounds = previousIndexOfChar (text, "\n", lineEnd1);
						if (startBounds == -1) startBounds = 0;
						break;
					}
				}
				C.memmove (start_offset, new int[] {startBounds}, 4);
				C.memmove (end_offset, new int[] {endBounds}, 4);
				text = text.substring (startBounds, endBounds);
				return getStringPtr (text);
			}
		}
		AtkTextIface iface = getParentTextIface (atkObject);
		if (iface != null && iface.get_text_before_offset != 0) {
			return ATK.call (iface.get_text_before_offset, atkObject, offset_value, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	static void setGValue (long value, Number number) {
		if (number == null) return;
		if (OS.G_VALUE_TYPE(value) != 0) OS.g_value_unset(value);
		if (number instanceof Double) {
			OS.g_value_init(value, OS.G_TYPE_DOUBLE());
			OS.g_value_set_double(value, number.doubleValue());
		} else if (number instanceof Float) {
			OS.g_value_init(value, OS.G_TYPE_FLOAT());
			OS.g_value_set_float(value, number.floatValue());
		} else if (number instanceof Long) {
			OS.g_value_init(value, OS.G_TYPE_INT64());
			OS.g_value_set_int64(value, number.longValue());
		} else {
			OS.g_value_init(value, OS.G_TYPE_INT());
			OS.g_value_set_int(value, number.intValue());
		}
	}

	static Number getGValue (long value) {
		long type = OS.G_VALUE_TYPE(value);
		if (type == 0) return null;
		if (type == OS.G_TYPE_DOUBLE()) return Double.valueOf(OS.g_value_get_double(value));
		if (type == OS.G_TYPE_FLOAT()) return Float.valueOf(OS.g_value_get_float(value));
		if (type == OS.G_TYPE_INT64()) return Long.valueOf(OS.g_value_get_int64(value));
		return Integer.valueOf(OS.g_value_get_int(value));
	}

	/**
	 * Fills a Java AtkValueIface struct with that of the parent class.
	 * This is a Java implementation of what is referred to in GObject as "chaining up".
	 * See: https://developer.gnome.org/gobject/stable/howto-gobject-chainup.html
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AtkValueIface Java object representing the interface struct of atkObject's
	 * parent
	 */
	static AtkValueIface getParentValueIface (long atkObject) {
		long type = OS.swt_fixed_accessible_get_type();
		if (OS.g_type_is_a (OS.g_type_parent (type), ATK.ATK_TYPE_VALUE())) {
			AtkValueIface iface = new AtkValueIface ();
			ATK.memmove (iface, OS.g_type_interface_peek_parent (ATK.ATK_VALUE_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	/**
	 * Gets the value of this atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param value a pointer to a GValue that represents the current value
	 *
	 * @return 0, this is a void function -- the value is stored in the parameter
	 */
	static long atkValue_get_current_value (long atkObject, long value) {
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getParentValueIface (atkObject);
		if (iface != null && iface.get_current_value != 0) {
			ATK.call (iface.get_current_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleValueListener> listeners = accessible.accessibleValueListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = listeners.get(i);
					listener.getCurrentValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	/**
	 * Gets the maximum value of this atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param value a pointer to a GValue that represents the current maximum
	 * value
	 *
	 * @return 0, this is a void function -- the value is stored in the parameter
	 */
	static long atkValue_get_maximum_value (long atkObject, long value) {
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getParentValueIface (atkObject);
		if (iface != null && iface.get_maximum_value != 0) {
			ATK.call (iface.get_maximum_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleValueListener> listeners = accessible.accessibleValueListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = listeners.get(i);
					listener.getMaximumValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	/**
	 * Gets the minimum value of this atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param value a pointer to a GValue that represents the current minimum
	 * value
	 *
	 * @return 0, this is a void function -- the value is stored in the parameter
	 */
	static long atkValue_get_minimum_value (long atkObject, long value) {
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getParentValueIface (atkObject);
		if (iface != null && iface.get_minimum_value != 0) {
			ATK.call (iface.get_minimum_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleValueListener> listeners = accessible.accessibleValueListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = listeners.get(i);
					listener.getMinimumValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	/**
	 * Sets the value of this atkObject.
	 *
	 * This is the implementation of an ATK function which
	 * queries the Accessible listeners at the Java level. On GTK3 the ATK
	 * interfaces are implemented in os_custom.c and access this method via
	 * JNI.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 * @param value a pointer to the new GValue to be set
	 *
	 * @return a long int representation of 1 for success, 0 otherwise
	 */
	static long atkValue_set_current_value (long atkObject, long value) {
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			List<AccessibleValueListener> listeners = accessible.accessibleValueListeners;
			int length = size(listeners);
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = listeners.get(i);
					listener.setCurrentValue(event);
				}
				return event.value != null ? 1 : 0;
			}
		}
		long parentResult = 0;
		AtkValueIface iface = getParentValueIface (atkObject);
		if (iface != null && iface.set_current_value != 0) {
			parentResult = ATK.call (iface.set_current_value, atkObject, value);
		}
		return parentResult;
	}

	/**
	 * Gets the AccessibleObject associated with the AtkObject handle
	 * from the Java map of AccessibleObjects.
	 *
	 * @param atkObject a pointer to the current AtkObject
	 *
	 * @return an AccessibleObject associated with the provided AtkObject pointer
	 */
	static AccessibleObject getAccessibleObject (long atkObject) {
		AccessibleObject object = AccessibleObjects.get (new LONG (atkObject));
		if (object == null) return null;
		if (object.accessible == null) return null;
		Control control = object.accessible.control;
		if (control == null || control.isDisposed()) return null;
		return object;
	}

	AccessibleObject getChildByID (int childId) {
		if (childId == ACC.CHILDID_SELF) return this;
		if (childId == ACC.CHILDID_NONE || childId == ACC.CHILDID_MULTIPLE) return null;
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				AccessibleObject child = children[i];
				if (child != null && child.id == childId) return child;
			}
		}
		return null;
	}

	AccessibleObject getChildByIndex (int childIndex) {
		if (children != null && childIndex < children.length) return children [childIndex];
		return null;
	}

	String getText () {
		List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
		int length = size(listeners);
		if (length > 0) {
			String parentText = "";	//$NON-NLS-1$
			AtkTextIface iface = getParentTextIface (atkHandle);
			if (iface != null && iface.get_character_count != 0) {
				long characterCount = ATK.call (iface.get_character_count, atkHandle);
				if (characterCount > 0 && iface.get_text != 0) {
					long parentResult = ATK.call (iface.get_text, atkHandle, 0, characterCount);
					if (parentResult != 0) {
						parentText = getString (parentResult);
						OS.g_free(parentResult);
					}
				}
			}
			AccessibleControlEvent event = new AccessibleControlEvent (accessible);
			event.childID = id;
			event.result = parentText;
			for (int i = 0; i < length; i++) {
				AccessibleControlListener listener = listeners.get (i);
				listener.getValue (event);
			}
			return event.result;
		}
		return null;
	}

	static long gObjectClass_finalize (long atkObject) {
		/*
		 * GObject destruction is handled in os_custom.c in GTK3.
		 * AccessibleObject has to be removed from the map of AccessibleObjects, though.
		 */
		AccessibleObject object = AccessibleObjects.get (new LONG (atkObject));
		if (object != null) {
			AccessibleObjects.remove (new LONG (atkObject));
		}
		return 0;
	}

	static int toATKRelation (int relation) {
		switch (relation) {
			case ACC.RELATION_CONTROLLED_BY: return ATK.ATK_RELATION_CONTROLLED_BY;
			case ACC.RELATION_CONTROLLER_FOR: return ATK.ATK_RELATION_CONTROLLER_FOR;
			case ACC.RELATION_DESCRIBED_BY: return ATK.ATK_RELATION_DESCRIBED_BY;
			case ACC.RELATION_DESCRIPTION_FOR: return ATK.ATK_RELATION_DESCRIPTION_FOR;
			case ACC.RELATION_EMBEDDED_BY: return ATK.ATK_RELATION_EMBEDDED_BY;
			case ACC.RELATION_EMBEDS: return ATK.ATK_RELATION_EMBEDS;
			case ACC.RELATION_FLOWS_FROM: return ATK.ATK_RELATION_FLOWS_FROM;
			case ACC.RELATION_FLOWS_TO: return ATK.ATK_RELATION_FLOWS_TO;
			case ACC.RELATION_LABEL_FOR: return ATK.ATK_RELATION_LABEL_FOR;
			case ACC.RELATION_LABELLED_BY: return ATK.ATK_RELATION_LABELLED_BY;
			case ACC.RELATION_MEMBER_OF: return ATK.ATK_RELATION_MEMBER_OF;
			case ACC.RELATION_NODE_CHILD_OF: return ATK.ATK_RELATION_NODE_CHILD_OF;
			case ACC.RELATION_PARENT_WINDOW_OF: return ATK.ATK_RELATION_PARENT_WINDOW_OF;
			case ACC.RELATION_POPUP_FOR: return ATK.ATK_RELATION_POPUP_FOR;
			case ACC.RELATION_SUBWINDOW_OF: return ATK.ATK_RELATION_SUBWINDOW_OF;
		}
		return 0;
	}

	/**
	 * Static toDisplay implementation for accessibility purposes. This function
	 * is called from os_custom.c via JNI.
	 *
	 * @param gdkResource the GdkWindow (GTK3) or GdkSurface (GTK4)
	 * @param x a pointer to an integer which represents the x coordinate
	 * @param y a pointer to an integer which represents the y coordinate
	 * @return 0
	 */
	static long toDisplay (long gdkResource, long x, long y) {
		int [] origin_x = new int [1], origin_y = new int [1];
		if (gdkResource == 0) {
			// memmove anyways to prevent garbage data in the pointers
			C.memmove (x, new int[] {0}, 4);
			C.memmove (y, new int[] {0}, 4);
			return 0;
		}
		if (GTK.GTK4) {
			GDK.gdk_surface_get_origin (gdkResource, origin_x, origin_y);
		} else {
			GDK.gdk_window_get_origin (gdkResource, origin_x, origin_y);
		}
		int scaledX = DPIUtil.autoScaleDown (origin_x [0]);
		int scaledY = DPIUtil.autoScaleDown (origin_y [0]);
		C.memmove (x, new int[] {scaledX}, 4);
		C.memmove (y, new int[] {scaledY}, 4);
		return 0;
	}

	static void windowPoint (AccessibleObject object, int [] x, int [] y) {
		long widget = GTK.gtk_accessible_get_widget(object.atkHandle);
		while (widget == 0 && object.parent != null) {
			object = object.parent;
			widget = GTK.gtk_accessible_get_widget(object.atkHandle);
		}
		if (widget == 0) return;
		long topLevel = GTK.gtk_widget_get_toplevel (widget);
		if (GTK.GTK4) {
			long surface = GTK.gtk_widget_get_surface (topLevel);
			GDK.gdk_surface_get_origin (surface, x, y);
		} else {
			long window = GTK.gtk_widget_get_window (topLevel);
			GDK.gdk_window_get_origin (window, x, y);
		}
	}

	static int nextIndexOfChar (String string, String searchChars, int startIndex) {
		int result = string.length ();
		for (int i = 0; i < searchChars.length (); i++) {
			char current = searchChars.charAt (i);
			int index = string.indexOf (current, startIndex);
			if (index != -1) result = Math.min (result, index);
		}
		return result;
	}

	static int nextIndexOfNotChar (String string, String searchChars, int startIndex) {
		int length = string.length ();
		int index = startIndex;
		while (index < length) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break;
			index++;
		}
		return index;
	}

	static int previousIndexOfChar (String string, String searchChars, int startIndex) {
		int result = -1;
		if (startIndex < 0) return result;
		string = string.substring (0, startIndex);
		for (int i = 0; i < searchChars.length (); i++) {
			char current = searchChars.charAt (i);
			int index = string.lastIndexOf (current);
			if (index != -1) result = Math.max (result, index);
		}
		return result;
	}

	static int previousIndexOfNotChar (String string, String searchChars, int startIndex) {
		if (startIndex < 0) return -1;
		int index = startIndex - 1;
		while (index >= 0) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break;
			index--;
		}
		return index;
	}

	void addRelation (int type, Accessible target) {
		AccessibleObject targetAccessibleObject = target.getAccessibleObject();
		if (targetAccessibleObject != null) {
			/*
			 * FIXME: Workaround for https://bugs.eclipse.org/312451
			 *
			 * This null check is conceptually wrong and will probably cause
			 * inconsistent behavior, but since we don't know in what
			 * circumstances the target doesn't have an accessibleObject, that's
			 * the best way we know to avoid throwing an NPE.
			 */
			ATK.atk_object_add_relationship(atkHandle, toATKRelation(type), targetAccessibleObject.atkHandle);
		}
	}

	void release () {
		accessible = null;
		/*
		 * GObject destruction is implemented in os_custom.c for GTK3:
		 * only unref lightweight widgets and children.
		 */
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				AccessibleObject child = children [i];
				if (child != null) OS.g_object_unref(child.atkHandle);
			}
			children = null;
		}
		if (isLightweight) {
			OS.g_object_unref(atkHandle);
		}
	}

	void removeRelation (int type, Accessible target) {
		AccessibleObject targetAccessibleObject = target.getAccessibleObject();
		if (targetAccessibleObject != null) {
			/*
			 * FIXME: Workaround for https://bugs.eclipse.org/312451
			 *
			 * This null check is conceptually wrong and will probably cause
			 * inconsistent behavior, but since we don't know in what
			 * circumstances the target doesn't have an accessibleObject, that's
			 * the best way we know to avoid throwing an NPE.
			 */
			ATK.atk_object_remove_relationship (atkHandle, toATKRelation(type), targetAccessibleObject.atkHandle);
		}
	}

	void selectionChanged () {
		OS.g_signal_emit_by_name (atkHandle, ATK.selection_changed);
	}

	void sendEvent(int event, Object eventData) {
		switch (event) {
			case ACC.EVENT_SELECTION_CHANGED:
				OS.g_signal_emit_by_name (atkHandle, ATK.selection_changed);
				break;
			case ACC.EVENT_TEXT_SELECTION_CHANGED:
				OS.g_signal_emit_by_name (atkHandle, ATK.text_selection_changed);
				break;
			case ACC.EVENT_STATE_CHANGED: {
				if (!(eventData instanceof int[])) break;
				int[] array = (int[])eventData;
				int state =  array[0];
				int value = array[1];
				int atkState = -1;
				switch (state) {
					case ACC.STATE_SELECTED: atkState = ATK.ATK_STATE_SELECTED; break;
					case ACC.STATE_SELECTABLE: atkState = ATK.ATK_STATE_SELECTABLE; break;
					case ACC.STATE_MULTISELECTABLE: atkState = ATK.ATK_STATE_MULTISELECTABLE; break;
					case ACC.STATE_FOCUSED: atkState = ATK.ATK_STATE_FOCUSED; break;
					case ACC.STATE_FOCUSABLE: atkState = ATK.ATK_STATE_FOCUSABLE; break;
					case ACC.STATE_PRESSED: atkState = ATK.ATK_STATE_PRESSED; break;
					case ACC.STATE_CHECKED: atkState = ATK.ATK_STATE_CHECKED; break;
					case ACC.STATE_EXPANDED: atkState = ATK.ATK_STATE_EXPANDED; break;
					case ACC.STATE_COLLAPSED: atkState = ATK.ATK_STATE_EXPANDED; break;
					case ACC.STATE_HOTTRACKED: atkState = ATK.ATK_STATE_ARMED; break;
					case ACC.STATE_BUSY: atkState = ATK.ATK_STATE_BUSY; break;
					case ACC.STATE_READONLY: atkState = ATK.ATK_STATE_EDITABLE; break;
					case ACC.STATE_INVISIBLE: atkState = ATK.ATK_STATE_VISIBLE; break;
					case ACC.STATE_OFFSCREEN: atkState = ATK.ATK_STATE_SHOWING; break;
					case ACC.STATE_SIZEABLE: atkState = ATK.ATK_STATE_RESIZABLE; break;
					case ACC.STATE_LINKED: break;
					case ACC.STATE_DISABLED: atkState = ATK.ATK_STATE_ENABLED; break;
					case ACC.STATE_ACTIVE: atkState = ATK.ATK_STATE_ACTIVE; break;
					case ACC.STATE_SINGLELINE: atkState = ATK.ATK_STATE_SINGLE_LINE; break;
					case ACC.STATE_MULTILINE: atkState = ATK.ATK_STATE_MULTI_LINE; break;
					case ACC.STATE_REQUIRED: atkState = ATK.ATK_STATE_REQUIRED; break;
					case ACC.STATE_INVALID_ENTRY: atkState = ATK.ATK_STATE_INVALID_ENTRY; break;
					case ACC.STATE_SUPPORTS_AUTOCOMPLETION: atkState = ATK.ATK_STATE_SUPPORTS_AUTOCOMPLETION; break;
				}
				if (atkState == -1) break;
				ATK.atk_object_notify_state_change(atkHandle, atkState, value != 0);
				break;
			}
			case ACC.EVENT_LOCATION_CHANGED: {
				List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
				int length = size(listeners);
				GdkRectangle rect = new GdkRectangle();
				if (length > 0) {
					AccessibleControlEvent e = new AccessibleControlEvent (accessible);
					e.childID = id;
					for (int i = 0; i < length; i++) {
						AccessibleControlListener listener = listeners.get (i);
						listener.getLocation (e);
					}
					rect.x = e.x;
					rect.y = e.y;
					rect.width = e.width;
					rect.height = e.height;
				}
				OS.g_signal_emit_by_name (atkHandle, ATK.bounds_changed, rect);
				break;
			}
			case ACC.EVENT_NAME_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_name);
				break;
			case ACC.EVENT_DESCRIPTION_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_description);
				break;
			case ACC.EVENT_VALUE_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_value);
				break;
			case ACC.EVENT_DOCUMENT_LOAD_COMPLETE:
				OS.g_signal_emit_by_name (atkHandle, ATK.load_complete);
				break;
			case ACC.EVENT_DOCUMENT_LOAD_STOPPED:
				OS.g_signal_emit_by_name (atkHandle, ATK.load_stopped);
				break;
			case ACC.EVENT_DOCUMENT_RELOAD:
				OS.g_signal_emit_by_name (atkHandle, ATK.reload);
				break;
			case ACC.EVENT_PAGE_CHANGED:
				break;
			case ACC.EVENT_SECTION_CHANGED:
				break;
			case ACC.EVENT_ACTION_CHANGED:
				break;
			case ACC.EVENT_HYPERLINK_END_INDEX_CHANGED:
				OS.g_object_notify(atkHandle, ATK.end_index);
				break;
			case ACC.EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED:
				OS.g_object_notify(atkHandle, ATK.number_of_anchors);
				break;
			case ACC.EVENT_HYPERLINK_SELECTED_LINK_CHANGED:
				OS.g_object_notify(atkHandle, ATK.selected_link);
				break;
			case ACC.EVENT_HYPERLINK_START_INDEX_CHANGED:
				OS.g_object_notify(atkHandle, ATK.start_index);
				break;
			case ACC.EVENT_HYPERLINK_ACTIVATED:
				OS.g_signal_emit_by_name (atkHandle, ATK.link_activated);
				break;
			case ACC.EVENT_HYPERTEXT_LINK_SELECTED:
				if (!(eventData instanceof Integer)) break;
				int index =  ((Integer)eventData).intValue();
				OS.g_signal_emit_by_name (atkHandle, ATK.link_selected, index);
				break;
			case ACC.EVENT_HYPERTEXT_LINK_COUNT_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_hypertext_nlinks);
				break;
			case ACC.EVENT_ATTRIBUTE_CHANGED:
				long gType = OS.G_OBJECT_TYPE(atkHandle);
				if (gType == GTK.GTK_TYPE_TEXT_VIEW_ACCESSIBLE()) break;
				OS.g_signal_emit_by_name (atkHandle, ATK.attributes_changed);
				break;
			case ACC.EVENT_TABLE_CAPTION_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_caption_object);
				break;
			case ACC.EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_column_description);
				break;
			case ACC.EVENT_TABLE_COLUMN_HEADER_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_column_header);
				break;
			case ACC.EVENT_TABLE_CHANGED: {
				if (!(eventData instanceof int[])) break;
				int[] array = (int[])eventData;
				int type =  array[0];
				int rowStart = array[1];
				int rowCount = array[2];
				int columnStart = array[3];
				int columnCount = array[4];
				switch (type) {
					case ACC.DELETE:
						if (rowCount > 0) OS.g_signal_emit_by_name (atkHandle, ATK.row_deleted, rowStart, rowCount);
						if (columnCount > 0) OS.g_signal_emit_by_name (atkHandle, ATK.column_deleted, columnStart, columnCount);
						break;
					case ACC.INSERT:
						if (rowCount > 0) OS.g_signal_emit_by_name (atkHandle, ATK.row_inserted, rowStart, rowCount);
						if (columnCount > 0) OS.g_signal_emit_by_name (atkHandle, ATK.column_inserted, columnStart, columnCount);
						break;
				}
				break;
			}
			case ACC.EVENT_TABLE_ROW_DESCRIPTION_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_row_description);
				break;
			case ACC.EVENT_TABLE_ROW_HEADER_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_row_header);
				break;
			case ACC.EVENT_TABLE_SUMMARY_CHANGED:
				OS.g_object_notify(atkHandle, ATK.accessible_table_summary);
				break;
			case ACC.EVENT_TEXT_ATTRIBUTE_CHANGED:
				OS.g_signal_emit_by_name (atkHandle, ATK.text_attributes_changed);
				break;
			case ACC.EVENT_TEXT_CARET_MOVED:
			case ACC.EVENT_TEXT_COLUMN_CHANGED: {
				int offset = 0;
				List<AccessibleTextExtendedListener> listeners = accessible.accessibleTextExtendedListeners;
				int length = size(listeners);
				AccessibleTextEvent e = new AccessibleTextEvent (accessible);
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						AccessibleTextListener listener = listeners.get(i);
						listener.getCaretOffset (e);
					}
				} else {
					List<AccessibleTextListener> listeners2 = accessible.accessibleTextListeners;
					length = size(listeners2);
					if (length > 0) {
						e.childID = id;
						for (int i = 0; i < length; i++) {
							AccessibleTextListener listener = listeners2.get(i);
							listener.getCaretOffset (e);
						}
					}
				}
				offset = e.offset;
				OS.g_signal_emit_by_name (atkHandle, ATK.text_caret_moved, offset);
				break;
			}
			case ACC.EVENT_TEXT_CHANGED: {
				if (!(eventData instanceof Object[])) break;
				Object[] data = (Object[])eventData;
				int type = ((Integer)data[0]).intValue();
				int start = ((Integer)data[1]).intValue();
				int end = ((Integer)data[2]).intValue();
				switch (type) {
					case ACC.DELETE:
						OS.g_signal_emit_by_name (atkHandle, ATK.text_changed_delete, start, end -start);
						break;
					case ACC.INSERT:
						OS.g_signal_emit_by_name (atkHandle, ATK.text_changed_insert, start, end -start);
						break;
				}
				break;
			}
		}
	}

	void sendEvent(int event, Object eventData, int childID) {
		updateChildren ();
		AccessibleObject accObject = getChildByID (childID);
		if (accObject != null) {
			accObject.sendEvent(event, eventData);
		}
	}

	void setFocus (int childID) {
		updateChildren ();
		AccessibleObject accObject = getChildByID (childID);
		if (accObject != null) {
			OS.g_signal_emit_by_name (accObject.atkHandle, ATK.focus_event, 1, 0);
			ATK.atk_object_notify_state_change(accObject.atkHandle, ATK.ATK_STATE_FOCUSED, true);
		}
	}

	void textCaretMoved(int index) {
		OS.g_signal_emit_by_name (atkHandle, ATK.text_caret_moved, index);
	}

	void textChanged(int type, int startIndex, int length) {
		if (type == ACC.TEXT_DELETE) {
			OS.g_signal_emit_by_name (atkHandle, ATK.text_changed_delete, startIndex, length);
		} else {
			OS.g_signal_emit_by_name (atkHandle, ATK.text_changed_insert, startIndex, length);
		}
	}

	void textSelectionChanged() {
		OS.g_signal_emit_by_name (atkHandle, ATK.text_selection_changed);
	}

	void updateChildren () {
		List<AccessibleControlListener> listeners = accessible.accessibleControlListeners;
		int length = size(listeners);
		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		event.childID = id;
		for (int i = 0; i < length; i++) {
			AccessibleControlListener listener = listeners.get (i);
			listener.getChildren (event);
		}
		Object[] children = event.children;
		AccessibleObject[] oldChildren = this.children;
		int count = children != null ? children.length : 0;
		AccessibleObject[] newChildren = new AccessibleObject[count];
		for (int i = 0; i < count; i++) {
			Object child = children [i];
			AccessibleObject object = null;
			// Widgets where the children are Integers: CTable, BarChart, and CTabFolder
			if (child instanceof Integer) {
				int id = ((Integer)child).intValue();
				object = oldChildren != null && i < oldChildren.length ? oldChildren [i] : null;
				if (object == null || object.id != id) {
					event = new AccessibleControlEvent (accessible);
					event.childID = id;
					for (int j = 0; j < length; j++) {
						AccessibleControlListener listener = listeners.get (j);
						listener.getChild (event);
					}
					if (event.accessible != null) {
						object = event.accessible.getAccessibleObject();
						if (object != null)	OS.g_object_ref(object.atkHandle);
					} else {
						long type = OS.G_OBJECT_TYPE (accessible.getControlHandle());
						long widget = accessible.getControlHandle();
						object = new AccessibleObject(type, widget, accessible, true);
					}
					object.id = id;
				} else {
					OS.g_object_ref(object.atkHandle);
				}
			} else if (child instanceof Accessible) {
				object = ((Accessible)child).getAccessibleObject();
				if (object != null)	OS.g_object_ref(object.atkHandle);
			}
			if (object != null) {
				object.index = i;
				object.parent = this;
				newChildren[i] = object;
			}
		}
		if (oldChildren != null) {
			for (int i = 0; i < oldChildren.length; i++) {
				AccessibleObject object = oldChildren [i];
				if (object != null) OS.g_object_unref(object.atkHandle);
			}
		}
		this.children = newChildren;
	}

}

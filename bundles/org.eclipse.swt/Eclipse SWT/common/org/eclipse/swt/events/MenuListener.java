package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.EventListener;

/**
 * Classes which implement this interface provide methods
 * that deal with the hiding and showing of menus.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMenuListener</code> method and removed using
 * the <code>removeMenuListener</code> method. When a
 * menu is hidden or shown, the appropriate method will
 * be invoked.
 * </p>
 *
 * @see MenuAdapter
 * @see MenuEvent
 */
public interface MenuListener extends EventListener {

/**
 * Sent when a menu is hidden.
 *
 * @param e an event containing information about the menu operation
 */
public void menuHidden(MenuEvent e);

/**
 * Sent when a menu is shown.
 *
 * @param e an event containing information about the menu operation
 */
public void menuShown(MenuEvent e);
}

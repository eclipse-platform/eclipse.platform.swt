package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated when a widget,
 * such as a menu item, is armed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a widget using the
 * <code>addArmListener</code> method and removed using
 * the <code>removeArmListener</code> method. When the
 * widget is armed, the widgetArmed method will be invoked.
 * </p>
 *
 * @see ArmEvent
 */
public interface ArmListener extends EventListenerCompatability {

/**
 * Sent when a widget is armed, or 'about to be selected'.
 *
 * @param e an event containing information about the arm
 */
public void widgetArmed(ArmEvent e);
}

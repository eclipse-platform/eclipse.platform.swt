package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.EventListenerCompatability;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as keys
 * are pressed on the system keyboard.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addKeyListener</code> method and removed using
 * the <code>removeKeyListener</code> method. When a
 * key is pressed or released, the appropriate method will
 * be invoked.
 * </p>
 *
 * @see KeyAdapter
 * @see KeyEvent
 */
public interface KeyListener extends EventListenerCompatability {

/**
 * Sent when a key is pressed on the system keyboard.
 *
 * @param e an event containing information about the key press
 */
public void keyPressed(KeyEvent e);

/**
 * Sent when a key is released on the system keyboard.
 *
 * @param e an event containing information about the key release
 */
public void keyReleased(KeyEvent e);
}

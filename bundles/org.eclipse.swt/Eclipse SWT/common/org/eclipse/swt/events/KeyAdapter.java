package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>KeyListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>KeyEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see KeyListener
 * @see KeyEvent
 */
public abstract class KeyAdapter implements KeyListener {

/**
 * Sent when a key is pressed on the system keyboard.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the key press
 */
public void keyPressed(KeyEvent e) {
}

/**
 * Sent when a key is released on the system keyboard.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the key release
 */
public void keyReleased(KeyEvent e) {
}
}

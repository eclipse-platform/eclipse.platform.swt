package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.EventListener;

/**
 * Classes which implement this interface provide a method
 * that deals with the events that are generated as the mouse
 * pointer moves.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseMoveListener</code> method and removed using
 * the <code>removeMouseMoveListener</code> method. As the
 * mouse moves, the mouseMove method will be invoked.
 * </p>
 *
 * @see MouseEvent
 */
public interface MouseMoveListener extends EventListener {

/**
 * Sent when the mouse moves.
 *
 * @param e an event containing information about the mouse move
 */
public void mouseMove(MouseEvent e);
}

package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.EventListenerCompatability;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated when a widget
 * is disposed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a widget using the
 * <code>addDisposeListener</code> method and removed using
 * the <code>removeDisposeListener</code> method. When a
 * widget is disposed, the widgetDisposed method will
 * be invoked.
 * </p>
 *
 * @see DisposeEvent
 */
public interface DisposeListener extends EventListenerCompatability {

/**
 * Sent when the widget is disposed.
 *
 * @param e an event containing information about the dispose
 */
public void widgetDisposed(DisposeEvent e);
}

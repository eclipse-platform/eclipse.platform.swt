package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated when the
 * control needs to be painted. 
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addPaintListener</code> method and removed using
 * the <code>removePaintListener</code> method. When a
 * paint event occurs, the paintControl method will be
 * invoked.
 * </p>
 *
 * @see PaintEvent
 */
public interface PaintListener extends SWTEventListener {

/**
 * Sent when a paint event occurs for the control.
 *
 * @param e an event containing information about the paint
 */
public void paintControl(PaintEvent e);
}

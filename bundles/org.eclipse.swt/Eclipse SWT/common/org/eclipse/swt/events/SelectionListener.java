package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.EventListenerCompatability;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated when selection
 * occurs in a control.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addSelectionListener</code> method and removed using
 * the <code>removeSelectionListener</code> method. When
 * selection occurs in a control the appropriate method
 * will be invoked.
 * </p>
 *
 * @see SelectionAdapter
 * @see SelectionEvent
 */
public interface SelectionListener extends EventListenerCompatability {

/**
 * Sent when selection occurs in the control.
 * <p>
 * For example, on some platforms selection occurs in
 * a List when the user selects an item or items.
 * </p>
 *
 * @param e an event containing information about the selection
 */
public void widgetSelected(SelectionEvent e);

/**
 * Sent when default selection occurs in the control.
 * <p>
 * For example, on some platforms default selection occurs
 * in a List when the user double-clicks an item or types
 * return in a Text.
 * </p>
 *
 * @param e an event containing information about the default selection
 */
public void widgetDefaultSelected(SelectionEvent e);
}

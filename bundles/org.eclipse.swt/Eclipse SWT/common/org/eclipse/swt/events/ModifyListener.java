package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import java.util.EventListener;

/**
 * Classes which implement this interface provide a method
 * that deals with the events that are generated when text
 * is modified.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a text widget using the
 * <code>addModifyListener</code> method and removed using
 * the <code>removeModifyListener</code> method. When the
 * text is modified, the modifyText method will be invoked.
 * </p>
 *
 * @see ModifyEvent
 */
public interface ModifyListener extends EventListener {

/**
 * Sent when the text is modified.
 *
 * @param e an event containing information about the modify
 */
public void modifyText(ModifyEvent e);
}

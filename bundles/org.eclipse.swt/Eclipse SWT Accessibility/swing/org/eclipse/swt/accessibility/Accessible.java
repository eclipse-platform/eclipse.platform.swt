/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;


import java.util.Vector;

import javax.accessibility.AccessibleContext;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

/**
 * Instances of this class provide a bridge between application
 * code and assistive technology clients. Many platforms provide
 * default accessible behavior for most widgets, and this class
 * allows that default behavior to be overridden. Applications
 * can get the default Accessible object for a control by sending
 * it <code>getAccessible</code>, and then add an accessible listener
 * to override simple items like the name and help string, or they
 * can add an accessible control listener to override complex items.
 * As a rule of thumb, an application would only want to use the
 * accessible control listener to implement accessibility for a
 * custom control.
 * 
 * @see Control#getAccessible
 * @see AccessibleListener
 * @see AccessibleEvent
 * @see AccessibleControlListener
 * @see AccessibleControlEvent
 * 
 * @since 2.0
 */
public class Accessible {
  Control control;
  AccessibleContext accessibleContext;
  Vector accessibleListeners = new Vector();
  Vector accessibleControlListeners = new Vector();
  Vector textListeners = new Vector ();

  Accessible(Control control) {
    this.control = control;
    accessibleContext = control.handle.getAccessibleContext();
  }
  
  /**
   * Invokes platform specific functionality to allocate a new accessible object.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Accessible</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param control the control to get the accessible object for
   * @return the platform specific accessible object
   */
  public static Accessible internal_new_Accessible(Control control) {
    return new Accessible(control);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when an accessible client asks for certain strings,
   * such as name, description, help, or keyboard shortcut. The
   * listener is notified by sending it one of the messages defined
   * in the <code>AccessibleListener</code> interface.
   *
   * @param listener the listener that should be notified when the receiver
   * is asked for a name, description, help, or keyboard shortcut string
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleListener
   * @see #removeAccessibleListener
   */
  public void addAccessibleListener(AccessibleListener listener) {
    checkWidget();
    if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    accessibleListeners.addElement(listener);
  }
  
  /**
   * Removes the listener from the collection of listeners who will
   * be notified when an accessible client asks for certain strings,
   * such as name, description, help, or keyboard shortcut.
   *
   * @param listener the listener that should no longer be notified when the receiver
   * is asked for a name, description, help, or keyboard shortcut string
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleListener
   * @see #addAccessibleListener
   */
  public void removeAccessibleListener(AccessibleListener listener) {
    checkWidget();
    if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    accessibleListeners.removeElement(listener);
  }
  
  /**
   * Adds the listener to the collection of listeners who will
   * be notified when an accessible client asks for custom control
   * specific information. The listener is notified by sending it
   * one of the messages defined in the <code>AccessibleControlListener</code>
   * interface.
   *
   * @param listener the listener that should be notified when the receiver
   * is asked for custom control specific information
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleControlListener
   * @see #removeAccessibleControlListener
   */
  public void addAccessibleControlListener(AccessibleControlListener listener) {
    checkWidget();
    if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    accessibleControlListeners.addElement(listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when an accessible client asks for custom control
   * specific information.
   *
   * @param listener the listener that should no longer be notified when the receiver
   * is asked for custom control specific information
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleControlListener
   * @see #addAccessibleControlListener
   */
  public void removeAccessibleControlListener(AccessibleControlListener listener) {
    checkWidget();
    if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    accessibleControlListeners.removeElement(listener);
  }
  
  /**
   * Adds the listener to the collection of listeners who will
   * be notified when an accessible client asks for custom text control
   * specific information. The listener is notified by sending it
   * one of the messages defined in the <code>AccessibleTextListener</code>
   * interface.
   *
   * @param listener the listener that should be notified when the receiver
   * is asked for custom text control specific information
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleTextListener
   * @see #removeAccessibleTextListener
   * 
   * @since 3.0
   */
  public void addAccessibleTextListener (AccessibleTextListener listener) {
    checkWidget ();
    if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
    textListeners.addElement (listener);    
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when an accessible client asks for custom text control
   * specific information.
   *
   * @param listener the listener that should no longer be notified when the receiver
   * is asked for custom text control specific information
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @see AccessibleTextListener
   * @see #addAccessibleTextListener
   * 
   * @since 3.0
   */
  public void removeAccessibleTextListener (AccessibleTextListener listener) {
    checkWidget ();
    if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
    textListeners.removeElement (listener);
  }

  /**
   * Returns the control for this Accessible object. 
   *
   * @return the receiver's control
   * @since 3.0
   */
  public Control getControl() {
    return control;
  }

  /**
   * Sends a message to accessible clients that the child selection
   * within a custom container control has changed.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void selectionChanged () {
  }
  
  /**
   * Sends a message to accessible clients that the text
   * caret has moved within a custom control.
   *
   * @param index the new caret index within the control
   * 
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @since 3.0
   */
  public void textCaretMoved (int index) {
  }
  
  /**
   * Sends a message to accessible clients that the text
   * within a custom control has changed.
   *
   * @param type the type of change, one of <code>ACC.NOTIFY_TEXT_INSERT</code>
   * or <code>ACC.NOTIFY_TEXT_DELETE</code>
   * @param startIndex the text index within the control where the insertion or deletion begins
   * @param length the non-negative length in characters of the insertion or deletion
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   * 
   * @see ACC#TEXT_INSERT
   * @see ACC#TEXT_DELETE
   * 
   * @since 3.0
   */
  public void textChanged (int type, int startIndex, int length) {
  }
  
  /**
   * Sends a message to accessible clients that the text
   * selection has changed within a custom control.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   *
   * @since 3.0
   */
  public void textSelectionChanged () {
  }
  
  /**
   * Sends a message to accessible clients indicating that the focus
   * has changed within a custom control.
   *
   * @param childID an identifier specifying a child of the control
   * 
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
   * </ul>
   */
  public void setFocus(int childID) {
  }

  /**
   * Invokes platform specific functionality to dispose an accessible object.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Accessible</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   */
  public void internal_dispose_Accessible() {
  }
  
  /* checkWidget was copied from Widget, and rewritten to work in this package */
  void checkWidget () {
    if (!isValidThread ()) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
    if (control.isDisposed ()) SWT.error (SWT.ERROR_WIDGET_DISPOSED);
  }

  /* isValidThread was copied from Widget, and rewritten to work in this package */
  boolean isValidThread () {
    return control.getDisplay ().getThread () == Thread.currentThread () || SwingUtilities.isEventDispatchThread();
  }

}

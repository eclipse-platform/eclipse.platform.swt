/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.awt.Container;
import java.awt.Dimension;
import java.util.EventObject;

import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.swing.CLink;
import org.eclipse.swt.internal.swing.UIThreadUtils;

/**
 * Instances of this class represent a selectable
 * user interface object that displays a text with 
 * links.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.1
 */
public class Link extends Control {
  
  String text;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Link (Composite parent, int style) {
  super (parent, style);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  TypedListener typedListener = new TypedListener (listener);
  addListener (SWT.Selection, typedListener);
  addListener (SWT.DefaultSelection, typedListener);
}

void createHandleInit() {
  super.createHandleInit();
  state |= THEME_BACKGROUND;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
  checkWidget ();
  SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      isAdjustingSize = true;
    }
  });
  Dimension size = handle.getSize();
  if(wHint == SWT.DEFAULT) {
    handle.setSize(((CLink)handle).getPreferredWidth(), 0);
  } else {
    handle.setSize(wHint, 0);
  }
  Point point = super.computeSize (wHint, hHint, changed);
  handle.setSize(size);
  SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      isAdjustingSize = false;
    }
  });
  return point;
}

Container createHandle () {
  return (Container)CLink.Factory.newInstance(this, style);
}

String getNameText () {
  return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
  checkWidget ();
  return text == null? "": text;
}

boolean mnemonicHit (char key) {
  Composite control = this.parent;
  while (control != null) {
    Control [] children = control._getChildren ();
    int index = 0;
    while (index < children.length) {
      if (children [index] == this) break;
      index++;
    }
    index++;
    if (index < children.length) {
      if (children [index].setFocus ()) return true;
    }
    control = control.parent;
  }
  return false;
}

boolean mnemonicMatch (char key) {
  char mnemonic = findMnemonic (getText ());
  if (mnemonic == '\0') return false;
  return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (eventTable == null) return;
  eventTable.unhook (SWT.Selection, listener);
  eventTable.unhook (SWT.DefaultSelection, listener); 
}

/**
 * Sets the receiver's text.
 * <p>
 * The string can contain both regular text and hyperlinks.  A hyperlink
 * is delimited by an anchor tag, &lt;A&gt; and &lt;/A&gt;.  Within an
 * anchor, a single HREF attribute is supported.  When a hyperlink is
 * selected, the text field of the selection event contains either the
 * text of the hyperlink or the value of its HREF, if one was specified.
 * In the rare case of identical hyperlinks within the same string, the
 * HREF tag can be used to distinguish between them.  The string may
 * include the mnemonic character and line delimiters.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
  checkWidget ();
  if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  checkWidget ();
  this.text = string;
  int mnemonicIndex = findMnemonicIndex(string);
  if(mnemonicIndex > 0) {
    String s = string.substring(0, mnemonicIndex - 1).replaceAll("&&", "&");
    string = s + string.substring(mnemonicIndex).replaceAll("&&", "&");
//    mnemonicIndex -= mnemonicIndex - 1 - s.length();
//    mnemonicIndex--;
  } else {
    string = string.replaceAll("&&", "&");
  }
  ((CLink)handle).setLinkText(string);
}

public void processEvent(EventObject e) {
  if(e instanceof HyperlinkEvent) {
    if(!hooks(SWT.Selection)) { super.processEvent(e); return; }
  } else {
    super.processEvent(e);
    return;
  }
  if(isDisposed()) {
    super.processEvent(e);
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    if(e instanceof HyperlinkEvent) {
      Event event = new Event();
      event.text = ((HyperlinkEvent)e).getDescription();
      sendEvent(SWT.Selection, event);
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}

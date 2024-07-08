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


import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;

/**
 * Instances of this class are used to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING</dd>
 * <dd>OK, OK | CANCEL</dd>
 * <dd>YES | NO, YES | NO | CANCEL</dd>
 * <dd>RETRY | CANCEL</dd>
 * <dd>ABORT | RETRY | IGNORE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION, ICON_QUESTION,
 * ICON_WARNING and ICON_WORKING may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public  class MessageBox extends Dialog {
	String message = "";
	
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}

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
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (style));
	checkSubclass ();
}

static int checkStyle (int style) {
	if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) == 0) style |= SWT.APPLICATION_MODAL;
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}

/**
 * Returns the dialog's message, or an empty string if it does not have one.
 * The message is a description of the purpose for which the dialog was opened.
 * This message will be visible in the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
}

static final int MAX_WIDTH = 120;

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return the ID of the button that was selected to dismiss the
 *         message box (e.g. SWT.OK, SWT.CANCEL, etc...)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public int open () {
	int messageType = JOptionPane.PLAIN_MESSAGE;
	if ((style & SWT.ICON_ERROR) != 0) messageType = JOptionPane.ERROR_MESSAGE;
	if ((style & SWT.ICON_INFORMATION) != 0) messageType = JOptionPane.INFORMATION_MESSAGE;
	if ((style & SWT.ICON_QUESTION) != 0) messageType = JOptionPane.QUESTION_MESSAGE;
	if ((style & SWT.ICON_WARNING) != 0) messageType = JOptionPane.WARNING_MESSAGE;
	if ((style & SWT.ICON_WORKING) != 0) messageType = JOptionPane.INFORMATION_MESSAGE;
  String[] messageTokens = this.message.split("\n");
  StringBuffer sb = new StringBuffer(this.message.length());
  for(int i=0; i<messageTokens.length; i++) {
    if(i > 0) {
      sb.append('\n');
    }
    String token = messageTokens[i];
    if(token.length() > MAX_WIDTH) {
      String[] subTokens = token.split(" ");
      int count = 0;
      for(int j=0; j<subTokens.length; j++) {
        String subToken = subTokens[j];
        if(count > 0 && count + subToken.length() > MAX_WIDTH) {
          sb.append('\n');
          count = 0;
        }
        int splitCount = Math.max(0, (subToken.length() - 1)) / MAX_WIDTH + 1;
        for(int k=0; k<splitCount; k++) {
          if(k<splitCount - 1) {
            sb.append(subToken.substring(k * MAX_WIDTH, (k + 1) * MAX_WIDTH));
            sb.append('\n');
          } else {
            sb.append(subToken.substring(k * MAX_WIDTH));
          }
        }
        if(j < subTokens.length - 1) {
          sb.append(' ');
        }
        count += subToken.length();
      }
    } else {
      sb.append(token);
    }
  }
  String message = sb.toString();
  if((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK", "Cancel"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return result == 0? SWT.OK: SWT.CANCEL;
  }
  if((style & SWT.OK) == SWT.OK) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return SWT.OK;
  }
  if((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Yes", "No", "Cancel"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return result == 0? SWT.YES: result == 1? SWT.NO: SWT.CANCEL;
  }
  if((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Yes", "No"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return result == 0? SWT.YES: SWT.NO;
  }
  if((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Retry", "Cancel"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return result == 0? SWT.RETRY: SWT.CANCEL;
  }
  if((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
    int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Abort", "Retry", "Ignore"}, null);
    if(result == JOptionPane.CLOSED_OPTION) {
      return SWT.CANCEL; 
    }
    return result == 0? SWT.ABORT: result == 1? SWT.RETRY: SWT.IGNORE;
  }
  int result = JOptionPane.showOptionDialog(getParent().handle, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK"}, null);
  if(result == JOptionPane.CLOSED_OPTION) {
    return SWT.CANCEL; 
  }
  return SWT.OK;
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void setMessage (String string) {
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
}

}

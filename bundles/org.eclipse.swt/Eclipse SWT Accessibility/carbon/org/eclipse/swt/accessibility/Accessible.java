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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.internal.carbon.*;

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
	Vector accessibleListeners = new Vector();
	Vector accessibleControlListeners = new Vector();
	Vector textListeners = new Vector ();
	Control control;
	int axuielementref = 0;
	int[] osChildIDCache = new int[0];
	String osRoleAttribute = null;
	
	Accessible(Control control) {
		this.control = control;
		axuielementref = OS.AXUIElementCreateWithHIObjectAndIdentifier(control.handle, 0);
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
	 * Returns the control for this Accessible object. 
	 *
	 * @return the receiver's control
	 * @since 3.0
	 */
	public Control getControl() {
		return control;
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
		if (axuielementref != 0) {
			OS.CFRelease(axuielementref);
			axuielementref = 0;
			for (int index = 1; index < osChildIDCache.length; index += 2) {
				OS.CFRelease(osChildIDCache [index]);
			}
			osChildIDCache = new int[0];
		}
	}
	
	/**
	 * Invokes platform specific functionality to handle a window message.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Accessible</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 */
	public int internal_kEventAccessibleGetChildAtPoint (int nextHandler, int theEvent, int userData) {
		if (axuielementref != 0) {
			int childID = getChildIDFromEvent(theEvent);

			CGPoint pt = new CGPoint ();
			OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.x = (int) pt.x;
			event.y = (int) pt.y;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getChildAtPoint(event);
			}
			if (event.accessible != null) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleChild, OS.typeCFTypeRef, 4, new int[] {event.accessible.axuielementref});
				return OS.noErr;
			}
			if (event.childID == ACC.CHILDID_SELF || event.childID == ACC.CHILDID_NONE || event.childID == childID) {
				/*
				 * From the Carbon doc for kEventAccessibleGetChildAtPoint: "If there is no child at the given point,
				 * you should still return noErr, but leave the parameter empty (do not call SetEventParameter)."
				 */
				return OS.noErr;
			}
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleChild, OS.typeCFTypeRef, 4, new int[] {childIDToOs(event.childID)});
			return OS.noErr;
		}
		return OS.eventNotHandledErr;
	}
	
	/**
	 * Invokes platform specific functionality to handle a window message.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Accessible</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 */
	public int internal_kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
		if (axuielementref != 0) {
			int [] stringRef = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
			int length = OS.CFStringGetLength (stringRef [0]);
			char [] buffer= new char [length];
			CFRange range = new CFRange ();
			range.length = length;
			OS.CFStringGetCharacters (stringRef [0], range, buffer);
			String attributeName = new String(buffer);
			if (attributeName.equals(OS.kAXRoleAttribute)) return getRoleAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSubroleAttribute)) return getSubroleAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXRoleDescriptionAttribute)) return getRoleDescriptionAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXHelpAttribute)) return getHelpAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXTitleAttribute)) return getTitleAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXValueAttribute)) return getValueAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXEnabledAttribute)) return getEnabledAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXFocusedAttribute)) return getFocusedAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXParentAttribute)) return getParentAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXChildrenAttribute)) return getChildrenAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSelectedChildrenAttribute)) return getSelectedChildrenAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXVisibleChildrenAttribute)) return getVisibleChildrenAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXWindowAttribute)) return getWindowAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXTopLevelUIElementAttribute)) return getTopLevelUIElementAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXPositionAttribute)) return getPositionAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSizeAttribute)) return getSizeAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXDescriptionAttribute)) return getDescriptionAttribute(nextHandler, theEvent, userData);
			return getAttribute(nextHandler, theEvent, userData);
		}
		return OS.eventNotHandledErr;
	}
	
	public int internal_kEventAccessibleGetAllAttributeNames (int nextHandler, int theEvent, int userData) {
		if (axuielementref != 0) {
			int [] arrayRef = new int[1];
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeNames, OS.typeCFMutableArrayRef, null, 4, null, arrayRef);
			int stringArrayRef = arrayRef[0];
			int length = OS.CFArrayGetCount(stringArrayRef);
			String [] osAllAttributes = new String [length];
			for (int i = 0; i < length; i++) {
				int stringRef = OS.CFArrayGetValueAtIndex(stringArrayRef, i);
				osAllAttributes[i] = stringRefToString (stringRef);
			}
			/* Add our list of supported attributes to the array. */
			String [] requiredAttributes = {
				OS.kAXRoleAttribute,
				OS.kAXSubroleAttribute,
				OS.kAXRoleDescriptionAttribute,
				OS.kAXHelpAttribute,
				OS.kAXTitleAttribute,
				OS.kAXValueAttribute,
				OS.kAXEnabledAttribute,
				OS.kAXFocusedAttribute,
				OS.kAXParentAttribute,
				OS.kAXChildrenAttribute,
				OS.kAXSelectedChildrenAttribute,
				OS.kAXVisibleChildrenAttribute,
				OS.kAXWindowAttribute,
				OS.kAXTopLevelUIElementAttribute,
				OS.kAXPositionAttribute,
				OS.kAXSizeAttribute,
				OS.kAXDescriptionAttribute,
			};
			for (int i = 0; i < requiredAttributes.length; i++) {
				OS.CFArrayAppendValue(stringArrayRef, stringToStringRef(requiredAttributes[i]));
			}
			return OS.noErr;
		}
		return OS.eventNotHandledErr;
	}
	
	int getAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		if (code == OS.eventNotHandledErr) {
			/* If the childID was created by the application, delegate to the accessible for the control. */
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleObject, OS.typeCFTypeRef, 4, new int [] {axuielementref});
			code = OS.CallNextEventHandler (nextHandler, theEvent);
		}
		return code;
	}
	
	int getHelpAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		String osHelpAttribute = null;
		int [] stringRef = new int [1];
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, null, 4, null, stringRef);
			osHelpAttribute = stringRefToString (stringRef [0]);
		}
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.result = osHelpAttribute;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getHelp(event);
		}
		if (event.result == null) return code;
		stringRef [0] = stringToStringRef (event.result);
		if (stringRef [0] == 0) return code;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
		return OS.noErr;
	}
	
	int getRoleAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		osRoleAttribute = null;
		int [] stringRef = new int [1];
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, null, 4, null, stringRef);
			osRoleAttribute = stringRefToString (stringRef [0]);
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.detail = osToRole(osRoleAttribute);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		stringRef [0] = stringToStringRef (roleToOs (event.detail));
		if (stringRef [0] == 0) return code;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
		return OS.noErr;
	}
	
	int getSubroleAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getRoleDescriptionAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.detail = osToRole(osRoleAttribute);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		int [] stringRef = new int [1];
		stringRef [0] = stringToStringRef (roleToOs (event.detail));
		if (stringRef [0] == 0) return code;
		int [] stringRef2 = new int [1];
		stringRef2 [0] = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef2);
		return OS.noErr;
	}
	
	int getTitleAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		String osTitleAttribute = null;
		int [] stringRef = new int [1];
		if (code == OS.noErr) {
			int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, null, 4, null, stringRef);
			if (status == OS.noErr) {
				osTitleAttribute = stringRefToString (stringRef [0]);
			}
		}
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.result = osTitleAttribute;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getName(event);
		}
		if (event.result == null) return code;
		stringRef [0] = stringToStringRef (event.result);
		if (stringRef [0] == 0) return code;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
		return OS.noErr;
	}
	
	int getValueAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		String osValueAttribute = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.result = osValueAttribute;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
			listener.getValue(event);
		}
		switch (event.detail) {
		case ACC.ROLE_RADIOBUTTON: // 1 = on, 0 = off
		case ACC.ROLE_CHECKBUTTON: // 1 = checked, 0 = unchecked, 2 = mixed
		case ACC.ROLE_SCROLLBAR: // numeric value representing the position of the scroller
		case ACC.ROLE_TABITEM:  // 1 = selected, 0 = not selected
		case ACC.ROLE_SLIDER: // the value associated with the position of the slider thumb
		case ACC.ROLE_PROGRESSBAR: // the value associated with the fill level of the progress bar
			if (event.result == null) return code;
			int number = Integer.parseInt(event.result);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {number});
			break;
		case ACC.ROLE_TABFOLDER: // the accessibility object representing the currently selected tab item
		case ACC.ROLE_COMBOBOX: // text of the currently selected item
		case ACC.ROLE_TEXT: // text in the text field
			if (event.result == null) return code;
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringToStringRef(event.result)});
			break;
		case ACC.ROLE_LABEL: // text in the label
			/* On a Mac, the 'value' of a label is the same as the 'name' of the label. */
			AccessibleEvent e = new AccessibleEvent(this);
			e.childID = getChildIDFromEvent(theEvent);
			e.result = osValueAttribute;
			for (int i = 0; i < accessibleListeners.size(); i++) {
				AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
				listener.getName(e);
			}
			if (e.result == null) return code;
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringToStringRef(e.result)});
			break;
		}
		return OS.noErr;
	}
	
	int getEnabledAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getFocusedAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		boolean osFocusedAttribute = false;
		int [] booleanRef = new int [1];
		if (code == OS.noErr) {
			int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFBooleanRef, null, 4, null, booleanRef);
			if (status == OS.noErr) {
				osFocusedAttribute = booleanRef [0] != 0;
			}
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}
		return OS.noErr;
	}
	
	int getParentAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		if (code == OS.eventNotHandledErr) {
			/* If the childID was created by the application, the parent is the accessible for the control. */
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {axuielementref});
			return OS.noErr;
		}
		return code;
	}
	
	int getChildrenAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getSelectedChildrenAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getVisibleChildrenAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getWindowAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getTopLevelUIElementAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getPositionAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		CGPoint osPositionAttribute = new CGPoint ();
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, osPositionAttribute);
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.x = (int) osPositionAttribute.x;
		event.y = (int) osPositionAttribute.y;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}
		osPositionAttribute.x = event.x;
		osPositionAttribute.y = event.y;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHIPoint, CGPoint.sizeof, osPositionAttribute);
		return OS.noErr;
	}
	
	int getSizeAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		CGPoint osSizeAttribute = new CGPoint ();
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, osSizeAttribute);
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.width = (int) osSizeAttribute.x;
		event.height = (int) osSizeAttribute.y;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}
		osSizeAttribute.x = event.width;
		osSizeAttribute.y = event.height;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHIPoint, CGPoint.sizeof, osSizeAttribute);
		return OS.noErr;
		}
	
	int getDescriptionAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
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
		checkWidget();
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
		checkWidget();
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
		checkWidget();
	}
	
	int getChildIDFromEvent(int theEvent) {
		int[] ref = new int[1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleObject, OS.typeCFTypeRef, null, 4, null, ref);
		return osToChildID(ref[0]);
	}
	
	int childIDToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) {
			return axuielementref;
		}
		/* Check cache for childID, if found, return corresponding osChildID. */
		int index;
		for (index = 0; index < osChildIDCache.length; index += 2) {
			if (childID == osChildIDCache [index]) {
				return osChildIDCache [index + 1];
			}
		}
		/* If childID not in cache, create osChildID, grow cache by 2,
		 * add childID/osChildID to cache, and return new osChildID. */
		int osChildID = OS.AXUIElementCreateWithHIObjectAndIdentifier(control.handle, (long) (childID + 1));
		int [] newCache = new int [osChildIDCache.length + 2];
		System.arraycopy (osChildIDCache, 0, newCache, 0, osChildIDCache.length);
		osChildIDCache = newCache;
		osChildIDCache [index] = childID;
		osChildIDCache [index + 1] = osChildID;
		return osChildID;
	}

	int osToChildID(int osChildID) {
		if (OS.CFEqual(osChildID, axuielementref)) {
			return ACC.CHILDID_SELF;
		}
		
		/* osChildID is an AXUIElementRef containing the control handle and a long identifier. */
		long[] childID = new long[1];
		OS.AXUIElementGetIdentifier(osChildID, childID);
		if (childID[0] == 0) {
			return ACC.CHILDID_SELF;
		}
		return (int) childID[0] - 1;
	}
	
	int stateToOs(int state) {
//		int osState = 0;
//		if ((state & ACC.STATE_SELECTED) != 0) osState |= OS.;
//		return osState;
		return state;
	}
	
	int osToState(int osState) {
//		int state = ACC.STATE_NORMAL;
//		if ((osState & OS.) != 0) state |= ACC.STATE_SELECTED;
//		return state;
		return osState;
	}

	String roleToOs(int role) {
		switch (role) {
			case ACC.ROLE_CLIENT_AREA: return OS.kAXWindowRole;
			case ACC.ROLE_WINDOW: return OS.kAXWindowRole;
			case ACC.ROLE_MENUBAR: return OS.kAXMenuBarRole;
			case ACC.ROLE_MENU: return OS.kAXMenuRole;
			case ACC.ROLE_MENUITEM: return OS.kAXMenuItemRole;
			case ACC.ROLE_SEPARATOR: return OS.kAXSplitterRole;
			case ACC.ROLE_TOOLTIP: return OS.kAXHelpTagRole;
			case ACC.ROLE_SCROLLBAR: return OS.kAXScrollBarRole;
			case ACC.ROLE_DIALOG: return OS.kAXWindowRole + ':' + OS.kAXDialogSubrole;
			case ACC.ROLE_LABEL: return OS.kAXStaticTextRole;
			case ACC.ROLE_PUSHBUTTON: return OS.kAXButtonRole;
			case ACC.ROLE_CHECKBUTTON: return OS.kAXCheckBoxRole;
			case ACC.ROLE_RADIOBUTTON: return OS.kAXRadioButtonRole;
			case ACC.ROLE_COMBOBOX: return OS.kAXComboBoxRole;
			case ACC.ROLE_TEXT: return OS.kAXTextFieldRole;
			case ACC.ROLE_TOOLBAR: return OS.kAXToolbarRole;
			case ACC.ROLE_LIST: return OS.kAXOutlineRole;
			case ACC.ROLE_LISTITEM: return OS.kAXStaticTextRole;
			case ACC.ROLE_TABLE: return OS.kAXTableRole;
			case ACC.ROLE_TABLECELL: return OS.kAXRowRole + ':' + OS.kAXTableRowSubrole;
			case ACC.ROLE_TABLECOLUMNHEADER: return OS.kAXButtonRole + ':' + OS.kAXSortButtonSubrole;
			case ACC.ROLE_TABLEROWHEADER: return OS.kAXRowRole + ':' + OS.kAXTableRowSubrole;
			case ACC.ROLE_TREE: return OS.kAXOutlineRole;
			case ACC.ROLE_TREEITEM: return OS.kAXOutlineRole + ':' + OS.kAXOutlineRowSubrole;
			case ACC.ROLE_TABFOLDER: return OS.kAXTabGroupRole;
			case ACC.ROLE_TABITEM: return OS.kAXRadioButtonRole;
			case ACC.ROLE_PROGRESSBAR: return OS.kAXProgressIndicatorRole;
			case ACC.ROLE_SLIDER: return OS.kAXSliderRole;
			case ACC.ROLE_LINK: return OS.kAXLinkRole;
		}
		return OS.kAXUnknownRole;
	}

	int osToRole(String osRole) {
		if (osRole.equals(OS.kAXWindowRole)) return ACC.ROLE_WINDOW;
		if (osRole.equals(OS.kAXMenuBarRole)) return ACC.ROLE_MENUBAR;
		if (osRole.equals(OS.kAXMenuRole)) return ACC.ROLE_MENU;
		if (osRole.equals(OS.kAXMenuItemRole)) return ACC.ROLE_MENUITEM;
		if (osRole.equals(OS.kAXSplitterRole)) return ACC.ROLE_SEPARATOR;
		if (osRole.equals(OS.kAXHelpTagRole)) return ACC.ROLE_TOOLTIP;
		if (osRole.equals(OS.kAXScrollBarRole)) return ACC.ROLE_SCROLLBAR;
		if (osRole.equals(OS.kAXScrollAreaRole)) return ACC.ROLE_LIST;
		if (osRole.equals(OS.kAXWindowRole + ':' + OS.kAXDialogSubrole)) return ACC.ROLE_DIALOG;
		if (osRole.equals(OS.kAXWindowRole + ':' + OS.kAXSystemDialogSubrole)) return ACC.ROLE_DIALOG;
		if (osRole.equals(OS.kAXStaticTextRole)) return ACC.ROLE_LABEL;
		if (osRole.equals(OS.kAXButtonRole)) return ACC.ROLE_PUSHBUTTON;
		if (osRole.equals(OS.kAXCheckBoxRole)) return ACC.ROLE_CHECKBUTTON;
		if (osRole.equals(OS.kAXRadioButtonRole)) return ACC.ROLE_RADIOBUTTON;
		if (osRole.equals(OS.kAXComboBoxRole)) return ACC.ROLE_COMBOBOX;
		if (osRole.equals(OS.kAXTextFieldRole)) return ACC.ROLE_TEXT;
		if (osRole.equals(OS.kAXTextAreaRole)) return ACC.ROLE_TEXT;
		if (osRole.equals(OS.kAXToolbarRole)) return ACC.ROLE_TOOLBAR;
		if (osRole.equals(OS.kAXListRole)) return ACC.ROLE_LIST;
		if (osRole.equals(OS.kAXTableRole)) return ACC.ROLE_TABLE;
		if (osRole.equals(OS.kAXColumnRole)) return ACC.ROLE_TABLECOLUMNHEADER;
		if (osRole.equals(OS.kAXButtonRole + ':' + OS.kAXSortButtonSubrole)) return ACC.ROLE_TABLECOLUMNHEADER;
		if (osRole.equals(OS.kAXRowRole + ':' + OS.kAXTableRowSubrole)) return ACC.ROLE_TABLEROWHEADER;
		if (osRole.equals(OS.kAXOutlineRole)) return ACC.ROLE_TREE;
		if (osRole.equals(OS.kAXOutlineRole + ':' + OS.kAXOutlineRowSubrole)) return ACC.ROLE_TREEITEM;
		if (osRole.equals(OS.kAXTabGroupRole)) return ACC.ROLE_TABFOLDER;
		if (osRole.equals(OS.kAXProgressIndicatorRole)) return ACC.ROLE_PROGRESSBAR;
		if (osRole.equals(OS.kAXSliderRole)) return ACC.ROLE_SLIDER;
		if (osRole.equals(OS.kAXLinkRole)) return ACC.ROLE_LINK;
		return ACC.ROLE_CLIENT_AREA;
	}
	
	/* Return a CFStringRef representing the given java String. */
	int stringToStringRef(String string) {
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		return OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	
	/* Return a Java String representing the given CFStringRef.
	 * Note that the caller is responsible for calling OS.CFRelease
	 * when they are done with the stringRef.
	 */
	String stringRefToString(int stringRef) {
		int length = OS.CFStringGetLength (stringRef);
		char [] buffer= new char [length];
		CFRange range = new CFRange ();
		range.length = length;
		OS.CFStringGetCharacters (stringRef, range, buffer);
		return new String(buffer);
	}

	/* checkWidget was copied from Widget, and rewritten to work in this package */
	void checkWidget () {
		if (!isValidThread ()) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
		if (control.isDisposed ()) SWT.error (SWT.ERROR_WIDGET_DISPOSED);
	}

	/* isValidThread was copied from Widget, and rewritten to work in this package */
	boolean isValidThread () {
		return control.getDisplay ().getThread () == Thread.currentThread ();
	}
}

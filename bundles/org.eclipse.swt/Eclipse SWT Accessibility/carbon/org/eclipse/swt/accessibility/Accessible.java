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
	static final String [] requiredAttributes = {
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
	static final String [] textAttributes = {
		OS.kAXNumberOfCharactersAttribute,
		OS.kAXSelectedTextAttribute,
		OS.kAXSelectedTextRangeAttribute,
		OS.kAXInsertionPointLineNumberAttribute,
	};

	Vector accessibleListeners = new Vector();
	Vector accessibleControlListeners = new Vector();
	Vector accessibleTextListeners = new Vector ();
	Control control;
	int axuielementref = 0;
	int[] osChildIDCache = new int[0];
	
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
		accessibleTextListeners.addElement (listener);		
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
			event.childID = ACC.CHILDID_SELF;
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
			if (attributeName.equals(OS.kAXNumberOfCharactersAttribute)) return getNumberOfCharactersAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSelectedTextAttribute)) return getSelectedTextAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSelectedTextRangeAttribute)) return getSelectedTextRangeAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXInsertionPointLineNumberAttribute)) return getInsertionPointLineNumberAttribute(nextHandler, theEvent, userData);
			return getAttribute(nextHandler, theEvent, userData);
		}
		return OS.eventNotHandledErr;
	}
	
	public int internal_kEventAccessibleGetAllAttributeNames (int nextHandler, int theEvent, int userData) {
		if (axuielementref != 0) {
			int [] arrayRef = new int[1];
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeNames, OS.typeCFMutableArrayRef, null, 4, null, arrayRef);
			int stringArrayRef = arrayRef[0];
			// TODO make sure each attribute name is not a dup before appending?
//			int length = OS.CFArrayGetCount(stringArrayRef);
//			String [] osAllAttributes = new String [length];
//			for (int i = 0; i < length; i++) {
//				int stringRef = OS.CFArrayGetValueAtIndex(stringArrayRef, i);
//				osAllAttributes[i] = stringRefToString (stringRef);
//			}
			/* Add our list of supported attributes to the array. */
			for (int i = 0; i < requiredAttributes.length; i++) {
				int stringRef = stringToStringRef(requiredAttributes[i]);
				OS.CFArrayAppendValue(stringArrayRef, stringRef);
				OS.CFRelease(stringRef);
			}
			if (accessibleTextListeners.size() > 0) {
				for (int i = 0; i < textAttributes.length; i++) {
					int stringRef = stringToStringRef(textAttributes[i]);
					OS.CFArrayAppendValue(stringArrayRef, stringRef);
					OS.CFRelease(stringRef);
				}
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
		if (event.result != null) {
			stringRef [0] = stringToStringRef (event.result);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				return OS.noErr;
			}
		}
		return code;
	}
	
	int getRoleAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		if (event.detail != -1) {
			String appRole = roleToOs (event.detail);
			int index = appRole.indexOf(':');
			if (index != -1) appRole = appRole.substring(0, index);
			int stringRef = stringToStringRef (appRole);
			if (stringRef != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef});
				OS.CFRelease(stringRef);
				return OS.noErr;
			}
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getSubroleAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		if (event.detail != -1) {
			String appRole = roleToOs (event.detail);
			int index = appRole.indexOf(':');
			if (index != -1) {
				appRole = appRole.substring(index + 1);
				int stringRef = stringToStringRef (appRole);
				if (stringRef != 0) {
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef});
					OS.CFRelease(stringRef);
				}
			}
			return OS.noErr;
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getRoleDescriptionAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		if (event.detail != -1) {
			String appRole = roleToOs (event.detail);
			String appSubrole = null;
			int index = appRole.indexOf(':');
			if (index != -1) {
				appSubrole = appRole.substring(index + 1);
				appRole = appRole.substring(0, index);
			}
			int stringRef1 = stringToStringRef (appRole);
			if (stringRef1 != 0) {
				int stringRef2 = 0;
				if (appSubrole != null) stringRef2 = stringToStringRef (appSubrole);
				int stringRef3 = OS.HICopyAccessibilityRoleDescription (stringRef1, stringRef2);
				OS.CFRelease(stringRef1);
				if (stringRef2 != 0) OS.CFRelease(stringRef2);
				if (stringRef3 != 0) {
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef3});
					OS.CFRelease(stringRef3);
					return OS.noErr;
				}
			}
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
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
		if (event.result != null) {
			stringRef [0] = stringToStringRef (event.result);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				return OS.noErr;
			}
		}
		return code;
	}
	
	int getValueAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		int childID = getChildIDFromEvent(theEvent);
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.detail = -1;
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
			listener.getValue(event);
		}
		int role = event.detail;
		String value = event.result;
		if (value != null || role == ACC.ROLE_LABEL) {
			int stringRef = 0;
			switch (role) {
			case ACC.ROLE_RADIOBUTTON: // 1 = on, 0 = off
			case ACC.ROLE_CHECKBUTTON: // 1 = checked, 0 = unchecked, 2 = mixed
			case ACC.ROLE_SCROLLBAR: // numeric value representing the position of the scroller
			case ACC.ROLE_TABITEM:  // 1 = selected, 0 = not selected
			case ACC.ROLE_SLIDER: // the value associated with the position of the slider thumb
			case ACC.ROLE_PROGRESSBAR: // the value associated with the fill level of the progress bar
				try {
					int number = Integer.parseInt(value);
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {number});
					return OS.noErr;
				} catch (NumberFormatException ex) {
					if (value.equalsIgnoreCase("true")) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {true});
						return OS.noErr;
					}
					if (value.equalsIgnoreCase("false")) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {false});
						return OS.noErr;
					}
				}
				break;
			case ACC.ROLE_TABFOLDER: // the accessibility object representing the currently selected tab item
				//break;
			case ACC.ROLE_COMBOBOX: // text of the currently selected item
			case ACC.ROLE_TEXT: // text in the text field
				stringRef = stringToStringRef(value);
				break;
			case ACC.ROLE_LABEL: // text in the label
				/* On a Mac, the 'value' of a label is the same as the 'name' of the label. */
				AccessibleEvent e = new AccessibleEvent(this);
				e.childID = childID;
				e.result = null;
				for (int i = 0; i < accessibleListeners.size(); i++) {
					AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
					listener.getName(e);
				}
				if (e.result != null) {
					stringRef = stringToStringRef(e.result);
				} else {
					if (value != null) stringRef = stringToStringRef(value);
				}
				break;
			}
			if (stringRef != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef});
				OS.CFRelease(stringRef);
				return OS.noErr;
			}
		}
		return code;
	}
	
	int getEnabledAttribute (int nextHandler, int theEvent, int userData) {
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getFocusedAttribute (int nextHandler, int theEvent, int userData) {
		int osChildID = getChildIDFromEvent(theEvent);
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_MULTIPLE; // set to impossible value to test if app resets
		event.accessible = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}
		if (event.accessible != null) {
			if (OS.CFEqual(event.accessible.axuielementref, osChildID)) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {true});
				return OS.noErr;
			}
		}
		if (event.childID == ACC.CHILDID_NONE) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {false});
			return OS.noErr;
		}
		if (event.childID != ACC.CHILDID_MULTIPLE) {
			boolean hasFocus = OS.CFEqual(childIDToOs(event.childID), osChildID); // This will test for CHILDID_SELF also.
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus});
			return OS.noErr;
		}
		// TODO: If the app does not implement getFocus, return the native focus
//		if (OS.CFEqual(axuielementref, osChildID)) {
//			boolean hasFocus = control.isFocusControl();
//			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus});
//			return OS.noErr;
//		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getParentAttribute (int nextHandler, int theEvent, int userData) {
		int code = OS.CallNextEventHandler (nextHandler, theEvent);
		if (code == OS.eventNotHandledErr) {
			/* If the childID was created by the application, the parent is the accessible for the control. */
			// TODO: typeCFTypeRef?... should be AXUIElementRef
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {axuielementref});
			return OS.noErr;
		}
		return code;
	}
	
	int getChildrenAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChildren(event);
		}
		Object [] appChildren = event.children;
		if (appChildren != null && appChildren.length > 0) {
			/* return a CFArrayRef of AXUIElementRefs */
			int children = OS.CFArrayCreateMutable (OS.kCFAllocatorDefault, 0, 0);
			if (children != 0) {
				for (int i = 0; i < appChildren.length; i++) {
					Object child = appChildren[i];
					if (child instanceof Integer) {
						OS.CFArrayAppendValue (children, childIDToOs(((Integer)child).intValue()));
					} else {
						OS.CFArrayAppendValue (children, ((Accessible)child).axuielementref);
					}
				}			
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFMutableArrayRef, 4, new int [] {children});
				OS.CFRelease(children);
				return OS.noErr;
			}
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
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
	
	int getNumberOfCharactersAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		String appValue = event.result;
		if (appValue != null) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {appValue.length()});
			return OS.noErr;
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getSelectedTextAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.offset = -1;
		event.length = -1;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getSelectionRange(event);
		}
		int offset = event.offset;
		int length = event.length;
		if (offset != -1 && length != -1 && length != 0) {  // TODO: do we need the && length != 0 ?
			AccessibleControlEvent event2 = new AccessibleControlEvent(this);
			event2.childID = event.childID;
			event2.result = null;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getValue(event2);
			}
			String appValue = event2.result;
			if (appValue != null) {
				int stringRef = stringToStringRef (appValue.substring(offset, offset + length));
				if (stringRef != 0) {
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef});
					OS.CFRelease(stringRef);
					return OS.noErr;
				}
			}
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getSelectedTextRangeAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.offset = -1;
		event.length = -1;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getSelectionRange(event);
		}
		if (event.offset != -1) {
			CFRange range = new CFRange();
			range.location = event.offset;
			range.length = event.length;
			int valueRef = OS.AXValueCreate(OS.kAXValueCFRangeType, range);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {valueRef});
			OS.CFRelease(valueRef);
			return OS.noErr;
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
	}
	
	int getInsertionPointLineNumberAttribute (int nextHandler, int theEvent, int userData) {
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.offset = -1;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getCaretOffset(event);
		}
		if (event.offset != -1) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {event.offset});
			return OS.noErr;
		}
		return OS.CallNextEventHandler (nextHandler, theEvent);
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
		accessibleTextListeners.removeElement (listener);
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
		int stringRef = stringToStringRef(OS.kAXSelectedChildrenChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
		OS.CFRelease(stringRef);
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
		/* Make sure the childID is cached */
		childIDToOs(childID);
		int stringRef = stringToStringRef(OS.kAXFocusedWindowChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, childID + 1);
		OS.CFRelease(stringRef);
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
		checkWidget();
		// TODO: Look at this in more depth
		int stringRef = stringToStringRef(OS.kAXValueChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
		OS.CFRelease(stringRef);
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
		checkWidget();
		// TODO: Look at this in more depth
		int stringRef = stringToStringRef(OS.kAXValueChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
		OS.CFRelease(stringRef);
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
		int stringRef = stringToStringRef(OS.kAXSelectedChildrenChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
		OS.CFRelease(stringRef);
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
		int osChildID = OS.AXUIElementCreateWithHIObjectAndIdentifier(control.handle, childID + 1);
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
		if (osRole == null) return 0;
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
	
	/* Return a CFStringRef representing the given java String.
	 * Note that the caller is responsible for calling OS.CFRelease
	 * when they are done with the stringRef.
	 */
	int stringToStringRef(String string) {
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		return OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	
	/* Return a Java String representing the given CFStringRef.
	 * Note that this method does not call OS.CFRelease(stringRef).
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

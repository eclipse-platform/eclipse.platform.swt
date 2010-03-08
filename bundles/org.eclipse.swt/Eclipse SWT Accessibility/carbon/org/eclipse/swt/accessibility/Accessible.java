/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.widgets.*;

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
 * @see <a href="http://www.eclipse.org/swt/snippets/#accessibility">Accessibility snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
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
		OS.kAXStringForRangeParameterizedAttribute,
		OS.kAXInsertionPointLineNumberAttribute,
		OS.kAXRangeForLineParameterizedAttribute,
	};

	Vector accessibleListeners = new Vector();
	Vector accessibleControlListeners = new Vector();
	Vector accessibleTextListeners = new Vector ();
	Vector accessibleActionListeners = new Vector();
	Vector accessibleHyperlinkListeners = new Vector();
	Vector accessibleTableListeners = new Vector();
	Vector accessibleTableCellListeners = new Vector();
	Vector accessibleTextExtendedListeners = new Vector();
	Vector accessibleValueListeners = new Vector();
	Vector accessibleScrollListeners = new Vector();
	Vector accessibleAttributeListeners = new Vector();
	Accessible parent;
	Control control;
	int axuielementref = 0;
	int[] osChildIDCache = new int[0];
	
	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param parent the Accessible parent, which must not be null
	 * 
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 * </ul>
	 * 
	 * @see Control#getAccessible
	 * 
	 * @since 3.6
	 */
	public Accessible(Accessible parent) {
		this.parent = checkNull(parent);
		this.control = parent.control;
		// TODO
	}

	/**
	 * @since 3.5
	 * @deprecated
	 */
	protected Accessible() {
	}

	Accessible(Control control) {
		this.control = control;
		axuielementref = OS.AXUIElementCreateWithHIObjectAndIdentifier(control.handle, 0);
		OS.HIObjectSetAccessibilityIgnored(control.handle, false);
	}
	
	static Accessible checkNull (Accessible parent) {
		if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return parent;
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
	 * and <code>AccessibleTextExtendedListener</code> interfaces.
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
	 * @see AccessibleTextExtendedListener
	 * @see #removeAccessibleTextListener
	 * 
	 * @since 3.0
	 */
	public void addAccessibleTextListener (AccessibleTextListener listener) {
		checkWidget ();
		if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (listener instanceof AccessibleTextExtendedListener) {
			accessibleTextExtendedListeners.addElement (listener);		
		} else {
			accessibleTextListeners.addElement (listener);
		}
	}
	
	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAction</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleAction</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleActionListener
	 * @see #removeAccessibleActionListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleActionListener(AccessibleActionListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleActionListeners.addElement(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleHyperlink</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleHyperlink</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleHyperlinkListener
	 * @see #removeAccessibleHyperlinkListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleHyperlinkListener(AccessibleHyperlinkListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleHyperlinkListeners.addElement(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTable</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleTable</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableListener
	 * @see #removeAccessibleTableListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleTableListener(AccessibleTableListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleTableListeners.addElement(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableCell</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleTableCell</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableCellListener
	 * @see #removeAccessibleTableCellListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleTableCellListener(AccessibleTableCellListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleTableCellListeners.addElement(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleValue</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleValue</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleValueListener
	 * @see #removeAccessibleValueListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleValueListener(AccessibleValueListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleValueListeners.addElement(listener);
	}

	/**
	 * Adds the listener to the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAttribute</code> interface.
	 *
	 * @param listener the listener that should be notified when the receiver
	 * is asked for <code>AccessibleAttribute</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleAttributeListener
	 * @see #removeAccessibleAttributeListener
	 * 
	 * @since 3.6
	 */
	public void addAccessibleAttributeListener(AccessibleAttributeListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleAttributeListeners.addElement(listener);
	}

	/**
	 * Adds a relation with the specified type and target
	 * to the receiver's set of relations.
	 * 
	 * @param type an <code>ACC</code> constant beginning with RELATION_* indicating the type of relation
	 * @param target the accessible that is the target for this relation
	 * 
	 * @since 3.6
	 */
	public void addRelation(int type, Accessible target) {
		checkWidget();
		// TODO
	}
	
	/**
	 * Disposes of the operating system resources associated with
	 * the receiver, and removes the receiver from its parent's
	 * list of children.
	 * <p>
	 * This method should be called when an accessible that was created
	 * with the public constructor <code>Accessible(Accessible parent)</code>
	 * is no longer needed. You do not need to call this when the receiver's
	 * control is disposed, because all <code>Accessible</code> instances
	 * associated with a control are released when the control is disposed.
	 * It is also not necessary to call this for instances of <code>Accessible</code>
	 * that were retrieved with <code>Control.getAccessible()</code>.
	 * </p>
	 * 
	 * @since 3.6
	 */
	public void dispose () {
		if (parent == null) return;
		// TODO: dispose children
		parent = null;
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
			OS.CallNextEventHandler (nextHandler, theEvent);
			//TODO: check error?
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
	public int internal_kEventAccessibleGetFocusedChild (int nextHandler, int theEvent, int userData) {
		if (axuielementref != 0) {
			int result = OS.CallNextEventHandler (nextHandler, theEvent);
			//TODO: check error?
			int childID = getChildIDFromEvent(theEvent);
			if (childID != ACC.CHILDID_SELF) {
				/* From the Carbon doc for kEventAccessibleGetFocusedChild:
				 * "Only return immediate children; do not return grandchildren of yourself."
				 */
				return OS.noErr; //TODO: should this return eventNotHandledErr?
			}
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = ACC.CHILDID_MULTIPLE; // set to invalid value, to test if the application sets it in getFocus()
			event.accessible = null;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getFocus(event);
			}
			
			/* The application can optionally answer an accessible. */
			if (event.accessible != null) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleChild, OS.typeCFTypeRef, 4, new int[] {event.accessible.axuielementref});
				return OS.noErr;
			}
			
			/* Or the application can answer a valid child ID, including CHILDID_SELF and CHILDID_NONE. */
			if (event.childID == ACC.CHILDID_SELF) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleChild, OS.typeCFTypeRef, 4, new int[] {0});
				return OS.noErr;
			}
			if (event.childID == ACC.CHILDID_NONE) {
				/*
				 * From the Carbon doc for kEventAccessibleGetFocusedChild: "If there is no child in the 
				 * focus chain, your handler should leave the kEventParamAccessibleChild parameter empty 
				 * and return noErr."
				 */
				return OS.noErr;
			}
			if (event.childID != ACC.CHILDID_MULTIPLE) {
				/* Other valid childID. */
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleChild, OS.typeCFTypeRef, 4, new int[] {childIDToOs(event.childID)});
				return OS.noErr;
			}
			
			/* Invalid childID means the application did not implement getFocus, so just go with the default handler. */
			return result;
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
	public int internal_kEventAccessibleGetAllAttributeNames (int nextHandler, int theEvent, int userData) {
		int code = userData; // userData flags whether nextHandler has already been called
		if (axuielementref != 0) {
			if (code == OS.eventNotHandledErr) OS.CallNextEventHandler (nextHandler, theEvent);
			int [] arrayRef = new int[1];
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeNames, OS.typeCFMutableArrayRef, null, 4, null, arrayRef);
			int stringArrayRef = arrayRef[0];
			int length = OS.CFArrayGetCount(stringArrayRef);
			String [] osAllAttributes = new String [length];
			for (int i = 0; i < length; i++) {
				int stringRef = OS.CFArrayGetValueAtIndex(stringArrayRef, i);
				osAllAttributes[i] = stringRefToString (stringRef);
			}
			/* Add our list of supported attributes to the array.
			 * Make sure each attribute name is not already in the array before appending.
			 */
			for (int i = 0; i < requiredAttributes.length; i++) {
				if (!contains(osAllAttributes, requiredAttributes[i])) {
					int stringRef = stringToStringRef(requiredAttributes[i]);
					OS.CFArrayAppendValue(stringArrayRef, stringRef);
					OS.CFRelease(stringRef);
				}
			}
			if (accessibleTextListeners.size() > 0) {
				for (int i = 0; i < textAttributes.length; i++) {
					if (!contains(osAllAttributes, textAttributes[i])) {
						int stringRef = stringToStringRef(textAttributes[i]);
						OS.CFArrayAppendValue(stringArrayRef, stringRef);
						OS.CFRelease(stringRef);
					}
				}
			}
			code = OS.noErr;
		}
		return code;
	}
	
	boolean contains (String [] array, String element) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(element)) {
				return true;
			}
		}
		return false;
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
			int length = 0;
			if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
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
			if (attributeName.equals(OS.kAXPositionAttribute)) return getPositionAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSizeAttribute)) return getSizeAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXDescriptionAttribute)) return getDescriptionAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXNumberOfCharactersAttribute)) return getNumberOfCharactersAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSelectedTextAttribute)) return getSelectedTextAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXSelectedTextRangeAttribute)) return getSelectedTextRangeAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXStringForRangeParameterizedAttribute)) return getStringForRangeAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXInsertionPointLineNumberAttribute)) return getInsertionPointLineNumberAttribute(nextHandler, theEvent, userData);
			if (attributeName.equals(OS.kAXRangeForLineParameterizedAttribute)) return getRangeForLineParameterizedAttribute(nextHandler, theEvent, userData);
			return getAttribute(nextHandler, theEvent, userData);
		}
		return userData;
	}
	
	int getAttribute (int nextHandler, int theEvent, int userData) {
		/* Generic handler: first try just calling the default handler. */
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		if (code != OS.noErr && getChildIDFromEvent(theEvent) != ACC.CHILDID_SELF) {
			/*
			* If the childID is unknown to the control, then it was created by the application,
			* so delegate to the application's accessible UIElement for the control.
			*/
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleObject, OS.typeCFTypeRef, 4, new int [] {axuielementref});
			code = OS.CallNextEventHandler (nextHandler, theEvent);
		}
		return code;
	}
	
	int getHelpAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
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
				code = OS.noErr;
			}
		}
		return code;
	}
	
	int getRoleAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
				code = OS.noErr;
			}
		}
		return code;
	}
	
	int getSubroleAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
			code = OS.noErr;
		}
		return code;
	}
	
	int getRoleDescriptionAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
					code = OS.noErr;
				}
			}
		}
		return code;
	}
	
	int getTitleAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		int childID = getChildIDFromEvent(theEvent);
		
		/*
		* Feature of the Macintosh.  The text of a Label is returned in its value,
		* not its title, so ensure that the role is not Label before asking for the title.
		*/
		AccessibleControlEvent roleEvent = new AccessibleControlEvent(this);
		roleEvent.childID = childID;
		roleEvent.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(roleEvent);
		}
		if (roleEvent.detail != ACC.ROLE_LABEL) {
			String osTitleAttribute = null;
			int [] stringRef = new int [1];
			if (code == OS.noErr) {
				int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, null, 4, null, stringRef);
				if (status == OS.noErr) {
					osTitleAttribute = stringRefToString (stringRef [0]);
				}
			}
			AccessibleEvent event = new AccessibleEvent(this);
			event.childID = childID;
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
					code = OS.noErr;
				}
			}
		}
		return code;
	}
	
	int getValueAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		int childID = getChildIDFromEvent(theEvent);
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.detail = -1;
		event.result = null; //TODO: could pass the OS value to the app
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
					code = OS.noErr;
				} catch (NumberFormatException ex) {
					if (value.equalsIgnoreCase("true")) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {true});
						code = OS.noErr;
					} else if (value.equalsIgnoreCase("false")) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {false});
						code = OS.noErr;
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
				code = OS.noErr;
			}
		}
		return code;
	}
	
	int getEnabledAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		if (code == OS.eventNotHandledErr) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {control.isEnabled()});
			code = OS.noErr;
		}
		return code;
	}
	
	int getFocusedAttribute (int nextHandler, int theEvent, int userData) {
		int[] osChildID = new int[1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleObject, OS.typeCFTypeRef, null, 4, null, osChildID);
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_MULTIPLE; // set to invalid value, to test if the application sets it in getFocus()
		event.accessible = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}

		/* The application can optionally answer an accessible. */
		if (event.accessible != null) {
			boolean hasFocus = OS.CFEqual(event.accessible.axuielementref, osChildID[0]);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus});
			return OS.noErr;
		}
		
		/* Or the application can answer a valid child ID, including CHILDID_SELF and CHILDID_NONE. */
		if (event.childID == ACC.CHILDID_SELF) {
			boolean hasFocus = OS.CFEqual(axuielementref, osChildID[0]);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus});
			return OS.noErr;
		}
		if (event.childID == ACC.CHILDID_NONE) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {false});
			return OS.noErr;
		}
		if (event.childID != ACC.CHILDID_MULTIPLE) {
			/* Other valid childID. */
			int childID = osToChildID(osChildID[0]);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {event.childID == childID});
			return OS.noErr;
		}

		/* Invalid childID at this point means the application did not implement getFocus, so return the native focus. */
		boolean hasFocus = false;
		int focusWindow = OS.GetUserFocusWindow ();
		if (focusWindow != 0) {
			int [] focusControl = new int [1];
			OS.GetKeyboardFocus (focusWindow, focusControl);
			if (focusControl [0] == control.handle) hasFocus = true;
		}
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeBoolean, 4, new boolean [] {hasFocus});
		return OS.noErr;
	}
	
	int getParentAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		if (code == OS.eventNotHandledErr) {
			/* If the childID was created by the application, the parent is the accessible for the control. */
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {axuielementref});
			code = OS.noErr;
		}
		return code;
	}
	
	int getChildrenAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
		int childID = getChildIDFromEvent(theEvent);
		if (childID == ACC.CHILDID_SELF) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = childID;
			event.detail = -1; // set to impossible value to test if app resets
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getChildCount(event);
			}
			if (event.detail == 0) {
				code = OS.noErr;
			} else if (event.detail > 0) {
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
						code = OS.noErr;
					}
				}
			}
		}
		return code;
	}
	
	int getSelectedChildrenAttribute (int nextHandler, int theEvent, int userData) {
		//TODO
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getVisibleChildrenAttribute (int nextHandler, int theEvent, int userData) {
		//TODO
		return getAttribute (nextHandler, theEvent, userData);
	}
	
	int getPositionAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		CGPoint osPositionAttribute = new CGPoint ();
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHIPoint, null, CGPoint.sizeof, null, osPositionAttribute);
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.x = (int) osPositionAttribute.x;
		event.y = (int) osPositionAttribute.y;
		if (code != OS.noErr) event.width = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}
		if (event.width != -1) {
			osPositionAttribute.x = event.x;
			osPositionAttribute.y = event.y;
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHIPoint, CGPoint.sizeof, osPositionAttribute);
			code = OS.noErr;
		}
		return code;
	}
	
	int getSizeAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		CGPoint osSizeAttribute = new CGPoint ();
		if (code == OS.noErr) {
			OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHISize, null, CGPoint.sizeof, null, osSizeAttribute);
		}
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.width = (code != OS.noErr) ? -1 : (int) osSizeAttribute.x;
		event.height = (int) osSizeAttribute.y;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}
		if (event.width != -1) {
			osSizeAttribute.x = event.width;
			osSizeAttribute.y = event.height;
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeHISize, CGPoint.sizeof, osSizeAttribute);
			code = OS.noErr;
		}
		return code;
	}
	
	int getDescriptionAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData != OS.eventNotHandledErr ? userData : OS.CallNextEventHandler (nextHandler, theEvent);
		String osDescriptionAttribute = null;
		int [] stringRef = new int [1];
		if (code == OS.noErr) {
			int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, null, 4, null, stringRef);
			if (status == OS.noErr) {
				osDescriptionAttribute = stringRefToString (stringRef [0]);
			}
		}
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = getChildIDFromEvent(theEvent);
		event.result = osDescriptionAttribute;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getDescription(event);
		}
		if (event.result != null) {
			stringRef [0] = stringToStringRef (event.result);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		}
		return code;
	}
	
	int getInsertionPointLineNumberAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
		AccessibleControlEvent controlEvent = new AccessibleControlEvent(this);
		controlEvent.childID = getChildIDFromEvent(theEvent);
		controlEvent.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(controlEvent);
		}
		AccessibleTextEvent textEvent = new AccessibleTextEvent(this);
		textEvent.childID = getChildIDFromEvent(theEvent);
		textEvent.offset = -1;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getCaretOffset(textEvent);
		}
		if (controlEvent.result != null && textEvent.offset != -1) {
			int lineNumber = lineNumberForOffset (controlEvent.result, textEvent.offset);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {lineNumber});
			code = OS.noErr;
		}
		return code;
	}
	
	int getNumberOfCharactersAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
			code = OS.noErr;
		}
		return code;
	}
	
	int getRangeForLineParameterizedAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
		int lineNumber [] = new int [1];
		int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeParameter, OS.typeSInt32, null, 4, null, lineNumber);
		if (status == OS.noErr) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = getChildIDFromEvent(theEvent);
			event.result = null;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getValue(event);
			}
			if (event.result != null) {
				CFRange range = rangeForLineNumber (lineNumber [0], event.result);
				if (range.location != -1) {
					int valueRef = OS.AXValueCreate(OS.kAXValueCFRangeType, range);
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {valueRef});
					OS.CFRelease(valueRef);
					code = OS.noErr;
				}
			}
		}
		return code;
	}
	
	int getSelectedTextAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
					code = OS.noErr;
				}
			}
		}
		return code;
	}
	
	int getSelectedTextRangeAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
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
			code = OS.noErr;
		}
		return code;
	}
	
	int getStringForRangeAttribute (int nextHandler, int theEvent, int userData) {
		int code = userData;
		int valueRef [] = new int [1];
		int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeParameter, OS.typeCFTypeRef, null, 4, null, valueRef);
		if (status == OS.noErr) {
			CFRange range = new CFRange();
			boolean ok = OS.AXValueGetValue(valueRef[0], OS.kAXValueCFRangeType, range);
			if (ok) {
				AccessibleControlEvent event = new AccessibleControlEvent(this);
				event.childID = getChildIDFromEvent(theEvent);
				event.result = null;
				for (int i = 0; i < accessibleControlListeners.size(); i++) {
					AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
					listener.getValue(event);
				}
				String appValue = event.result;
				if (appValue != null) {
					int stringRef = stringToStringRef (appValue.substring(range.location, range.location + range.length));
					if (stringRef != 0) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef});
						OS.CFRelease(stringRef);
						code = OS.noErr;
					}
				}
			}
		}
		return code;
	}
	
	int lineNumberForOffset (String text, int offset) {
		int lineNumber = 1;
		int length = text.length();
		for (int i = 0; i < offset; i++) {
			switch (text.charAt (i)) {
				case '\r': 
					if (i + 1 < length) {
						if (text.charAt (i + 1) == '\n') ++i;
					}
					// FALL THROUGH
				case '\n':
					lineNumber++;
			}
		}
		return lineNumber;
	}

	CFRange rangeForLineNumber (int lineNumber, String text) {
		CFRange range = new CFRange();
		range.location = -1;
		int line = 1;
		int count = 0;
		int length = text.length ();
		for (int i = 0; i < length; i++) {
			if (line == lineNumber) {
				if (count == 0) {
					range.location = i;
				}
				count++;
			}
			if (line > lineNumber) break;
			switch (text.charAt (i)) {
				case '\r': 
					if (i + 1 < length && text.charAt (i + 1) == '\n') i++;
					// FALL THROUGH
				case '\n':
					line++;
			}
		}
		range.length = count;
		return range;
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
	 * @see AccessibleTextExtendedListener
	 * @see #addAccessibleTextListener
	 * 
	 * @since 3.0
	 */
	public void removeAccessibleTextListener (AccessibleTextListener listener) {
		checkWidget ();
		if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (listener instanceof AccessibleTextExtendedListener) {
			accessibleTextExtendedListeners.removeElement (listener);
		} else {
			accessibleTextListeners.removeElement (listener);
		}
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAction</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleAction</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleActionListener
	 * @see #addAccessibleActionListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleActionListener(AccessibleActionListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleActionListeners.removeElement(listener);
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleHyperlink</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleHyperlink</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleHyperlinkListener
	 * @see #addAccessibleHyperlinkListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleHyperlinkListener(AccessibleHyperlinkListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleHyperlinkListeners.removeElement(listener);
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTable</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleTable</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableListener
	 * @see #addAccessibleTableListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleTableListener(AccessibleTableListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleTableListeners.removeElement(listener);
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleTableCell</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleTableCell</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleTableCellListener
	 * @see #addAccessibleTableCellListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleTableCellListener(AccessibleTableCellListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleTableCellListeners.removeElement(listener);
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleValue</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleValue</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleValueListener
	 * @see #addAccessibleValueListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleValueListener(AccessibleValueListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleValueListeners.removeElement(listener);
	}

	/**
	 * Removes the listener from the collection of listeners that will be
	 * notified when an accessible client asks for any of the properties
	 * defined in the <code>AccessibleAttribute</code> interface.
	 *
	 * @param listener the listener that should no longer be notified when the receiver
	 * is asked for <code>AccessibleAttribute</code> interface properties
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 *
	 * @see AccessibleAttributeListener
	 * @see #addAccessibleAttributeListener
	 * 
	 * @since 3.6
	 */
	public void removeAccessibleAttributeListener(AccessibleAttributeListener listener) {
		checkWidget();
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleAttributeListeners.removeElement(listener);
	}

	/**
	 * Removes the relation with the specified type and target
	 * from the receiver's set of relations.
	 * 
	 * @param type an <code>ACC</code> constant beginning with RELATION_* indicating the type of relation
	 * @param target the accessible that is the target for this relation
	 * 
	 * @since 3.6
	 */
	public void removeRelation(int type, Accessible target) {
		checkWidget();
		// TODO
	}
	
	public void sendEvent(int event, Object eventData) {
		checkWidget();
		// TODO
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
		childIDToOs(childID); // Make sure the childID is cached
		int stringRef = stringToStringRef(OS.kAXFocusedUIElementChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
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
		int stringRef = stringToStringRef(OS.kAXSelectedTextChangedNotification);
		OS.AXNotificationHIObjectNotify(stringRef, control.handle, 0);
		OS.CFRelease(stringRef);
	}
	
	/**
	 * Sends a message to accessible clients that the text
	 * within a custom control has changed.
	 *
	 * @param type the type of change, one of <code>ACC.TEXT_INSERT</code>
	 * or <code>ACC.TEXT_DELETE</code>
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
		int stringRef = stringToStringRef(OS.kAXSelectedTextChangedNotification);
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
			case ACC.ROLE_CLIENT_AREA: return OS.kAXGroupRole;
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
			case ACC.ROLE_SPLITBUTTON: return OS.kAXMenuButtonRole;
			case ACC.ROLE_COMBOBOX: return OS.kAXComboBoxRole;
			case ACC.ROLE_TEXT: return (control.getStyle () & SWT.MULTI) != 0 ? OS.kAXTextAreaRole : OS.kAXTextFieldRole;
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
		if (osRole.equals(OS.kAXMenuButtonRole)) return ACC.ROLE_SPLITBUTTON;
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
		if (stringRef == 0) return "";
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

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;
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

	static NSString[] baseAttributes = { 
		OS.NSAccessibilityRoleAttribute,
		OS.NSAccessibilityRoleDescriptionAttribute,
		OS.NSAccessibilityHelpAttribute,
		OS.NSAccessibilityFocusedAttribute,
		OS.NSAccessibilityParentAttribute,
		OS.NSAccessibilityChildrenAttribute,
		OS.NSAccessibilityPositionAttribute,
		OS.NSAccessibilitySizeAttribute,
		OS.NSAccessibilityWindowAttribute,
		OS.NSAccessibilityTopLevelUIElementAttribute
	};

	static NSString[] baseTextAttributes = {
		OS.NSAccessibilityNumberOfCharactersAttribute,
		OS.NSAccessibilitySelectedTextAttribute,
		OS.NSAccessibilitySelectedTextRangeAttribute,
		OS.NSAccessibilityInsertionPointLineNumberAttribute,
		OS.NSAccessibilitySelectedTextRangesAttribute,
		OS.NSAccessibilityVisibleCharacterRangeAttribute,
		OS.NSAccessibilityValueAttribute,
	};
	
	static NSString[] baseParameterizedAttributes = {
		OS.NSAccessibilityStringForRangeParameterizedAttribute,
		OS.NSAccessibilityRangeForLineParameterizedAttribute,
	};
	

	NSMutableArray attributeNames = null;
	NSMutableArray parameterizedAttributeNames = null;
	NSMutableArray actionNames = null;

	Vector accessibleListeners = new Vector();
	Vector accessibleControlListeners = new Vector();
	Vector accessibleTextListeners = new Vector ();
	Control control;

	Map /*<Integer, SWTAccessibleDelegate>*/ children = new HashMap();
	
	protected Accessible() {
	}

	Accessible(Control control) {
		this.control = control;
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
	
	public id internal_accessibilityActionDescription(NSString action, int childID) {
		// TODO No action support for now.
		return NSString.stringWith("");
	}

	public NSArray internal_accessibilityActionNames(int childID) {
		// The supported action list depends on the role played by the control.
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}

		// No accessible listener is overriding the role of the control, so let Cocoa return the default set for the control.
		if (event.detail == -1) {
			return null;
		}
		
		if ((childID == ACC.CHILDID_SELF) && (actionNames != null)) {
			return retainedAutoreleased(actionNames);
		}
		
		NSMutableArray returnValue = NSMutableArray.arrayWithCapacity(5);
		
		switch (event.detail) {
		case ACC.ROLE_PUSHBUTTON:
		case ACC.ROLE_RADIOBUTTON:
		case ACC.ROLE_CHECKBUTTON:
		case ACC.ROLE_TABITEM:
			returnValue.addObject(OS.NSAccessibilityPressAction);
			break;
		}

		switch (event.detail) {
		case ACC.ROLE_COMBOBOX:
			returnValue.addObject(OS.NSAccessibilityConfirmAction);
			break;
		}


		if (childID == ACC.CHILDID_SELF) {
			actionNames = returnValue;
			actionNames.retain();
			return retainedAutoreleased(actionNames);
		} else {
			// Caller must retain if they want to hold on to it.
			return returnValue;
		}
	}

	public NSArray internal_accessibilityAttributeNames(int childID) {
		// The supported attribute set depends on the role played by the control.
		// We may need to add or remove from the base set as needed.
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}

		// No accessible listener is overriding the role of the control, so let Cocoa
		// return the default set for the control.
		if (event.detail == -1)
			return null;
		
		if ((childID == ACC.CHILDID_SELF) && (attributeNames != null)) {
			return retainedAutoreleased(attributeNames);
		}
		
		NSMutableArray returnValue = NSMutableArray.arrayWithCapacity(baseAttributes.length);

		/* Add our list of supported attributes to the array.
		 * Make sure each attribute name is not already in the array before appending.
		 */
		for (int i = 0; i < baseAttributes.length; i++) {
			if (!returnValue.containsObject(baseAttributes[i])) {
				returnValue.addObject(baseAttributes[i]);
			}
		}
		
		if (accessibleTextListeners.size() > 0) {
			for (int i = 0; i < baseTextAttributes.length; i++) {
				if (!returnValue.containsObject(baseTextAttributes[i])) {
					returnValue.addObject(baseTextAttributes[i]);
				}
			}
		}
		
		// The following are expected to have a value (AXValue)
		switch (event.detail) {
		case ACC.ROLE_CHECKBUTTON:
		case ACC.ROLE_RADIOBUTTON:
		case ACC.ROLE_LABEL:
		case ACC.ROLE_TABITEM:
		case ACC.ROLE_TABFOLDER:
			returnValue.addObject(OS.NSAccessibilityValueAttribute);
			break;
		}
		
		// The following are expected to report their enabled status (AXEnabled)
		switch (event.detail) {
		case ACC.ROLE_CHECKBUTTON:
		case ACC.ROLE_RADIOBUTTON:
		case ACC.ROLE_LABEL:
		case ACC.ROLE_TABITEM:
		case ACC.ROLE_PUSHBUTTON:
		case ACC.ROLE_COMBOBOX:
			returnValue.addObject(OS.NSAccessibilityEnabledAttribute);
			break;
		}
		
		// The following are expected to report a title (AXTitle)
		switch (event.detail) {
		case ACC.ROLE_CHECKBUTTON:
		case ACC.ROLE_RADIOBUTTON:
		case ACC.ROLE_PUSHBUTTON:
		case ACC.ROLE_TABITEM:
			returnValue.addObject(OS.NSAccessibilityTitleAttribute);
			break;
		}
			
		// Accessibility verifier says these attributes must be reported for combo boxes.
		if (event.detail == ACC.ROLE_COMBOBOX) {
			returnValue.addObject(OS.NSAccessibilityExpandedAttribute);
		}
		
		// Accessibility verifier says these attributes must be reported for tab folders.
		if (event.detail == ACC.ROLE_TABFOLDER) {
			returnValue.addObject(OS.NSAccessibilityContentsAttribute);
			returnValue.addObject(OS.NSAccessibilityTabsAttribute);
		}

		/*
		 * Only report back sub-roles when the SWT role maps to a sub-role.
		 */
		if (event.detail != -1) {
			String osRole = roleToOs(event.detail);
			
			if (osRole.indexOf(':') == -1)
				returnValue.removeObject(OS.NSAccessibilitySubroleAttribute);
		}

		/*
		 * Children never return their own children, so remove that attribute.
		 */
		if (childID != ACC.CHILDID_SELF) {
			returnValue.removeObject(OS.NSAccessibilityChildrenAttribute);
		}
		
		if (childID == ACC.CHILDID_SELF) {
			attributeNames = returnValue;
			attributeNames.retain();
			return retainedAutoreleased(attributeNames);
		} else {
			// Caller must retain if necessary.
			return returnValue;
		}
	}

	public id internal_accessibilityAttributeValue(NSString attribute, int childID) {
		if (attribute.isEqualToString(OS.NSAccessibilityRoleAttribute)) return getRoleAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySubroleAttribute)) return getSubroleAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityRoleDescriptionAttribute)) return getRoleDescriptionAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityExpandedAttribute)) return getExpandedAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityHelpAttribute)) return getHelpAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityTitleAttribute)) return getTitleAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityValueAttribute)) return getValueAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityEnabledAttribute)) return getEnabledAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityFocusedAttribute)) return getFocusedAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityParentAttribute)) return getParentAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityChildrenAttribute)) return getChildrenAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityContentsAttribute)) return getChildrenAttribute(childID);
		// FIXME:  There's no specific API just for tabs, which won't include the buttons (if any.)
		if (attribute.isEqualToString(OS.NSAccessibilityTabsAttribute)) return getTabsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityWindowAttribute)) return getWindowAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityTopLevelUIElementAttribute)) return getTopLevelUIElementAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityPositionAttribute)) return getPositionAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySizeAttribute)) return getSizeAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityDescriptionAttribute)) return getDescriptionAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityNumberOfCharactersAttribute)) return getNumberOfCharactersAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedTextAttribute)) return getSelectedTextAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedTextRangeAttribute)) return getSelectedTextRangeAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityInsertionPointLineNumberAttribute)) return getInsertionPointLineNumberAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedTextRangesAttribute)) return getSelectedTextRangesAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityVisibleCharacterRangeAttribute)) return getVisibleCharacterRangeAttribute(childID);
		
		// If this object don't know how to get the value it's up to the control itself to return an attribute value.
		return null;
	}
	
	public id internal_accessibilityAttributeValue_forParameter(NSString attribute, id parameter, int childID) {
		if (attribute.isEqualToString(OS.NSAccessibilityStringForRangeParameterizedAttribute)) return getStringForRangeAttribute(parameter, childID);
		if (attribute.isEqualToString(OS.NSAccessibilityRangeForLineParameterizedAttribute)) return getRangeForLineParameterizedAttribute(parameter, childID);		
		return null;
	}

	// Returns the UI Element that has the focus. You can assume that the search for the focus has already been narrowed down to the receiver.
	// Override this method to do a deeper search with a UIElement - e.g. a NSMatrix would determine if one of its cells has the focus.
	public id internal_accessibilityFocusedUIElement(int childID) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_MULTIPLE; // set to invalid value, to test if the application sets it in getFocus()
		event.accessible = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}
		
		// The listener did not respond, so let Cocoa figure it out.
		if (event.childID == ACC.CHILDID_MULTIPLE)
			return null;
		
		/* The application can optionally answer an accessible. */
		if (event.accessible != null) {
			return new id(OS.NSAccessibilityUnignoredAncestor(event.accessible.control.view.id));
		}
		
		/* Or the application can answer a valid child ID, including CHILDID_SELF and CHILDID_NONE. */
		if (event.childID == ACC.CHILDID_SELF || event.childID == ACC.CHILDID_NONE) {
			return new id(OS.NSAccessibilityUnignoredAncestor(control.view.id));
		}	

		return new id(OS.NSAccessibilityUnignoredAncestor(childIDToOs(event.childID).id));
	}

	// Returns the deepest descendant of the UIElement hierarchy that contains the point. 
	// You can assume the point has already been determined to lie within the receiver.
	// Override this method to do deeper hit testing within a UIElement - e.g. a NSMatrix would test its cells. The point is bottom-left relative screen coordinates.
	public id internal_accessibilityHitTest(NSPoint point, int childID) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.x = (int) point.x;
		Monitor primaryMonitor = Display.getCurrent().getPrimaryMonitor();
		event.y = (int) (primaryMonitor.getBounds().height - point.y);
	
		// Set an impossible value to determine if anything responded to the event.
		event.childID = ACC.CHILDID_MULTIPLE;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChildAtPoint(event);
		}
		
		// The listener did not respond, so let Cocoa figure it out.
		if (event.childID == ACC.CHILDID_MULTIPLE)
			return null;
		
		if (event.accessible != null) {
			return new id(OS.NSAccessibilityUnignoredAncestor(event.accessible.control.view.id));
		}
	
		if (event.childID == ACC.CHILDID_SELF || event.childID == ACC.CHILDID_NONE) {
			return new id(OS.NSAccessibilityUnignoredAncestor(control.view.id));
		}
	
		return new id(OS.NSAccessibilityUnignoredAncestor(childIDToOs(event.childID).id));
	}

	// Return YES if the UIElement doesn't show up to the outside world - i.e. its parent should return the UIElement's children as its own - cutting the UIElement out. E.g. NSControls are ignored when they are single-celled.
	public boolean internal_accessibilityIsIgnored(int childID) {
		return false;
	}

	// parameterized attribute methods
	public NSArray internal_accessibilityParameterizedAttributeNames(int childID) {

		if ((childID == ACC.CHILDID_SELF) && (parameterizedAttributeNames != null)) {
			return retainedAutoreleased(parameterizedAttributeNames);
		}

		NSMutableArray returnValue = NSMutableArray.arrayWithCapacity(4);

		if (accessibleTextListeners.size() > 0) {
			for (int i = 0; i < baseParameterizedAttributes.length; i++) {
				if (!returnValue.containsObject(baseParameterizedAttributes[i])) {
					returnValue.addObject(baseParameterizedAttributes[i]);
				}
			}

		}

		if (childID == ACC.CHILDID_SELF) {
			parameterizedAttributeNames = returnValue;
			parameterizedAttributeNames.retain();
			return retainedAutoreleased(parameterizedAttributeNames);
		} else {
			// Caller must retain if they want to keep it.
			return returnValue;
		}
	}

	public void internal_accessibilityPerformAction(NSString action, int childID) {
		// TODO Auto-generated method stub
		// No action support for now.
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
		if (actionNames != null) actionNames.release();
		actionNames = null;
		if (attributeNames != null) attributeNames.release();
		attributeNames = null;
		if (parameterizedAttributeNames != null) parameterizedAttributeNames.release();
		parameterizedAttributeNames = null;
		
		Collection delegates = children.values();
		Iterator iter = delegates.iterator();
		while (iter.hasNext()) {
			SWTAccessibleDelegate childDelegate = (SWTAccessibleDelegate)iter.next();
			childDelegate.internal_dispose_SWTAccessibleDelegate();
		}
		
		children.clear();
	}
	
	id getExpandedAttribute(int childID) {
		// TODO: May need to expand the API so the combo box state can be reported.
		return NSNumber.numberWithBool(false);
	}

	id getHelpAttribute (int childID) {
		id returnValue = null;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = childID;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getHelp(event);
		}
		
		if (event.result != null) {
			returnValue = NSString.stringWith(event.result);
		}
		
		return returnValue;
	}
	
	NSString getRoleAttribute(int childID) {
		NSString returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		if (event.detail != -1) {
			String appRole = roleToOs (event.detail);
			int index = appRole.indexOf(':');
			if (index != -1) appRole = appRole.substring(0, index);
			returnValue = NSString.stringWith(appRole);
		}

		return returnValue;
	}
	
	id getSubroleAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
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
				returnValue = NSString.stringWith(appRole);
			}
		}
		return returnValue;
	}
	
	id getRoleDescriptionAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
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
			NSString nsAppRole = NSString.stringWith(appRole);
			NSString nsAppSubrole = null;
			
			if (appSubrole != null) nsAppSubrole = NSString.stringWith(appSubrole);
			returnValue = new NSString(OS.NSAccessibilityRoleDescription (((nsAppRole != null) ? nsAppRole.id : 0), (nsAppSubrole != null) ? nsAppSubrole.id : 0));
		}
		return returnValue;
	}
	
	id getTitleAttribute (int childID) {
		
		id returnValue = null;//NSString.stringWith("");
		
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
			AccessibleEvent event = new AccessibleEvent(this);
			event.childID = childID;
			event.result = null;
			for (int i = 0; i < accessibleListeners.size(); i++) {
				AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
				listener.getName(event);
			}
			
			if (event.result != null)
				returnValue = NSString.stringWith(event.result);
		}
		return returnValue;
	}
	
	id getValueAttribute (int childID) {
		id returnValue = null;
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

		switch (role) {
		case ACC.ROLE_RADIOBUTTON: // 1 = on, 0 = off
		case ACC.ROLE_CHECKBUTTON: // 1 = checked, 0 = unchecked, 2 = mixed
		case ACC.ROLE_SCROLLBAR: // numeric value representing the position of the scroller
		case ACC.ROLE_SLIDER: // the value associated with the position of the slider thumb
		case ACC.ROLE_PROGRESSBAR: // the value associated with the fill level of the progress bar
			if (value != null) {
				try {
					int number = Integer.parseInt(value);
					returnValue = NSNumber.numberWithInt(number);
				} catch (NumberFormatException ex) {
					if (value.equalsIgnoreCase("true")) {
						returnValue = NSNumber.numberWithBool(true);
					} else if (value.equalsIgnoreCase("false")) {
						returnValue = NSNumber.numberWithBool(false);
					}
				}
			} else {
				returnValue = NSNumber.numberWithBool(false);
			}
			break;
		case ACC.ROLE_TABFOLDER: // the accessibility object representing the currently selected tab item
		case ACC.ROLE_TABITEM:  // 1 = selected, 0 = not selected
			AccessibleControlEvent ace = new AccessibleControlEvent(this);
			ace.childID = -4;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getSelection(ace);
			}
			if (ace.childID >= ACC.CHILDID_SELF) {
				if (role == ACC.ROLE_TABITEM) {
			 		returnValue = NSNumber.numberWithBool(ace.childID == childID);
				} else {
					returnValue = new id(OS.NSAccessibilityUnignoredAncestor(childIDToOs(ace.childID).id));
				}
			} else {
		 		returnValue = NSNumber.numberWithBool(false);				
			}
			break;
		case ACC.ROLE_COMBOBOX: // text of the currently selected item
		case ACC.ROLE_TEXT: // text in the text field
			if (value != null) returnValue = NSString.stringWith(value);
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
				returnValue = NSString.stringWith(e.result);
			} else {
				if (value != null) returnValue = NSString.stringWith(value);
			}
			break;
		}
		
		return returnValue;
	}
	
	id getEnabledAttribute (int childID) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.detail = -1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getState(event);
		}

		return NSNumber.numberWithBool(control.isEnabled());
	}
	
	id getFocusedAttribute (int childID) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_MULTIPLE; // set to invalid value, to test if the application sets it in getFocus()
		event.accessible = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}

		/* The application can optionally answer an accessible. */
		// FIXME:
//		if (event.accessible != null) {
//			boolean hasFocus = (event.accessible.childID == childID) && (event.accessible.control == this.control);
//			return NSNumber.numberWithBool(hasFocus);
//		}
		
		/* Or the application can answer a valid child ID, including CHILDID_SELF and CHILDID_NONE. */
		if (event.childID == ACC.CHILDID_SELF) {
			boolean hasFocus = (event.childID == childID);
			return NSNumber.numberWithBool(hasFocus);
		}
		if (event.childID == ACC.CHILDID_NONE) {
			return NSNumber.numberWithBool(false);
		}
		if (event.childID != ACC.CHILDID_MULTIPLE) {
			/* Other valid childID. */
			return NSNumber.numberWithBool(event.childID == childID);
		}

		// Invalid childID at this point means the application did not implement getFocus, so 
		// let the default handler return the native focus.
		boolean hasFocus = (this.control.view.window().firstResponder() == control.view);
		return NSNumber.numberWithBool(hasFocus);
	}
	
	id getParentAttribute (int childID) {
		// Returning null here means 'let Cocoa figure it out.'
		if (childID == ACC.CHILDID_SELF)
			return null;
		else
			return new id(OS.NSAccessibilityUnignoredAncestor(control.view.id));
	}
	
	id getChildrenAttribute (int childID) {
		id returnValue = null;
		if (childID == ACC.CHILDID_SELF) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = childID;
			event.detail = -1; // set to impossible value to test if app resets
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getChildCount(event);
			}
			if (event.detail > 0) {
				for (int i = 0; i < accessibleControlListeners.size(); i++) {
					AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
					listener.getChildren(event);
				}
				Object [] appChildren = event.children;
				if (appChildren != null && appChildren.length > 0) {
					/* return an NSArray of NSAccessible objects. */
					NSMutableArray childArray = NSMutableArray.arrayWithCapacity(appChildren.length);

					for (int i = 0; i < appChildren.length; i++) {
						Object child = appChildren[i];
						if (child instanceof Integer) {
							id accChild = childIDToOs(((Integer)child).intValue());							
							childArray.addObject(accChild);
						} else {
							childArray.addObject(((Accessible)child).control.view);
						}
					}

					returnValue = new id(OS.NSAccessibilityUnignoredChildren(childArray.id));
				}
			}
		} else {
			// Lightweight children have no children of their own.
			// Don't return null if there are no children -- always return an empty array.
			returnValue = NSArray.array();
		}

		// Returning null here means we want the control itself to determine its children. If the accessible listener
		// implemented getChildCount/getChildren, references to those objects would have been returned above.
		return returnValue;
	}
	
	id getTabsAttribute (int childID) {
		id returnValue = null;
		if (childID == ACC.CHILDID_SELF) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = childID;
			event.detail = -1; // set to impossible value to test if app resets
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getChildCount(event);
			}
			if (event.detail > 0) {
				for (int i = 0; i < accessibleControlListeners.size(); i++) {
					AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
					listener.getChildren(event);
				}
				Object [] appChildren = event.children;
				if (appChildren != null && appChildren.length > 0) {
					/* return an NSArray of NSAccessible objects. */
					NSMutableArray childArray = NSMutableArray.arrayWithCapacity(appChildren.length);

					for (int i = 0; i < appChildren.length; i++) {
						Object child = appChildren[i];
						if (child instanceof Integer) {
							int subChildID = ((Integer)child).intValue();
							event.childID = subChildID;
							event.detail = -1;
							for (int j = 0; j < accessibleControlListeners.size(); j++) {
								AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(j);
								listener.getRole(event);
							}
							
							if (event.detail == ACC.ROLE_TABITEM) {
								id accChild = childIDToOs(((Integer)child).intValue());							
								childArray.addObject(accChild);
							}
						} else {
							childArray.addObject(((Accessible)child).control.view);
						}
					}

					returnValue = new id(OS.NSAccessibilityUnignoredChildren(childArray.id));
				}
			}
		} else {
			// Lightweight children have no children of their own.
			// Don't return null if there are no children -- always return an empty array.
			returnValue = NSArray.array();
		}

		// Returning null here means we want the control itself to determine its children. If the accessible listener
		// implemented getChildCount/getChildren, references to those objects would have been returned above.
		return returnValue;
	}
	
	id getWindowAttribute (int childID) {
		return control.view.window();
	}
	
	id getTopLevelUIElementAttribute (int childID) {
		return control.view.window();
	}
	
	id getPositionAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.width = -1;
		
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}

		Monitor primaryMonitor = Display.getCurrent().getPrimaryMonitor();
		
		NSPoint osPositionAttribute = new NSPoint ();
		if (event.width != -1) {
			// The point returned is the lower-left coordinate of the widget in lower-left relative screen coordinates.
			osPositionAttribute.x = event.x;
			osPositionAttribute.y = primaryMonitor.getBounds().height - event.y - event.height;
			returnValue = NSValue.valueWithPoint(osPositionAttribute);
		} else {
			if (childID != ACC.CHILDID_SELF) {
				Point pt = null;
				Rectangle location = control.getBounds();

				if (control.getParent() != null)
					pt = control.getParent().toDisplay(location.x, location.y);
				else 
					pt = ((Shell)control).toDisplay(location.x, location.y);

				osPositionAttribute.x = pt.x;
				osPositionAttribute.y = pt.y;
				returnValue = NSValue.valueWithPoint(osPositionAttribute);
			}
		}
		
		return returnValue;
	}
	
	id getSizeAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.width = -1;

		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}

		NSSize controlSize = new NSSize ();
		if (event.width != -1) {
			controlSize.width = event.width;
			controlSize.height = event.height;
			returnValue = NSValue.valueWithSize(controlSize);
		} else {
			if (childID != ACC.CHILDID_SELF) {
				controlSize.width = controlSize.height = 0;
				returnValue = NSValue.valueWithSize(controlSize);
			}
		}
		
		return returnValue;
	}
	
	id getDescriptionAttribute (int childID) {
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = childID;
		event.result = null;
		id returnValue = null;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getDescription(event);
		}

		returnValue = (event.result != null ? NSString.stringWith(event.result) : null);

		// If no description was provided, and this is a composite or canvas, return a blank string
		// -- otherwise, let the Cocoa control handle it.
		if (returnValue == null) {
			if (control instanceof Composite) returnValue = NSString.stringWith("");
		}

		return returnValue;
	}
	
	id getInsertionPointLineNumberAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent controlEvent = new AccessibleControlEvent(this);
		controlEvent.childID = childID;
		controlEvent.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(controlEvent);
		}
		AccessibleTextEvent textEvent = new AccessibleTextEvent(this);
		textEvent.childID = childID;
		textEvent.offset = -1;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getCaretOffset(textEvent);
		}
		if (controlEvent.result != null && textEvent.offset != -1) {
			int lineNumber = lineNumberForOffset (controlEvent.result, textEvent.offset);
			returnValue = NSNumber.numberWithInt(lineNumber);
		}
		return returnValue;
	}
	
	id getNumberOfCharactersAttribute (int childID) {
		id returnValue = null;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		String appValue = event.result;
		if (appValue != null) {
			returnValue = NSNumber.numberWithInt(appValue.length());
		}
		return returnValue;
	}
	
	id getRangeForLineParameterizedAttribute (id parameter, int childID) {
		id returnValue = null;

		// The parameter is an NSNumber with the line number.
		NSNumber lineNumberObj = new NSNumber(parameter.id);		
		int lineNumber = lineNumberObj.intValue();
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		if (event.result != null) {
			NSRange range = rangeForLineNumber (lineNumber, event.result);
			if (range.location != -1) {
				returnValue = NSValue.valueWithRange(range);
			}
		}
		return returnValue;
	}
	
	id getSelectedTextAttribute (int childID) {
		id returnValue = NSString.stringWith("");
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = childID;
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
				returnValue = NSString.stringWith(appValue.substring(offset, offset + length));
			}
		}
		return returnValue;
	}
	
	id getSelectedTextRangeAttribute (int childID) {
		id returnValue = null;
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = childID;
		event.offset = -1;
		event.length = 0;
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getSelectionRange(event);
		}
		if (event.offset != -1) {
			NSRange range = new NSRange();
			range.location = event.offset;
			range.length = event.length;
			returnValue = NSValue.valueWithRange(range);
		}
		return returnValue;
	}
	
	id getStringForRangeAttribute (id parameter, int childID) {
		id returnValue = null;
		
		// Parameter is an NSRange wrapped in an NSValue. 
		NSValue parameterObject = new NSValue(parameter.id);
		NSRange range = parameterObject.rangeValue();		
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		String appValue = event.result;

		if (appValue != null) {
			returnValue = NSString.stringWith(appValue.substring((int)/*64*/range.location, (int)/*64*/(range.location + range.length)));
		}

		return returnValue;
	}
	
	id getSelectedTextRangesAttribute (int childID) {
		NSMutableArray returnValue = null; 
		AccessibleTextEvent event = new AccessibleTextEvent(this);
		event.childID = childID;
		event.offset = -1;
		event.length = 0;
		
		for (int i = 0; i < accessibleTextListeners.size(); i++) {
			AccessibleTextListener listener = (AccessibleTextListener) accessibleTextListeners.elementAt(i);
			listener.getSelectionRange(event);
		}
		
		if (event.offset != -1) {
			returnValue = NSMutableArray.arrayWithCapacity(1);
			NSRange range = new NSRange();
			range.location = event.offset;
			range.length = event.length;
			returnValue.addObject(NSValue.valueWithRange(range));
		}
		
		return returnValue;
	}
	
	id getVisibleCharacterRangeAttribute (int childID) {
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = childID;
		event.result = null;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		
		NSRange range = new NSRange();

		if (event.result != null) {
			range.location = 0;
			range.length = event.result.length();
		} else {
			return null;
//			range.location = range.length = 0;
		}

		return NSValue.valueWithRange(range);
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

	NSRange rangeForLineNumber (int lineNumber, String text) {
		NSRange range = new NSRange();
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
	 * @see #addAccessibleTextListener
	 * 
	 * @since 3.0
	 */
	public void removeAccessibleTextListener (AccessibleTextListener listener) {
		checkWidget ();
		if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		accessibleTextListeners.removeElement (listener);
	}

	static NSArray retainedAutoreleased(NSArray inObject) {
		id temp = inObject.retain();
		id temp2 = new NSObject(temp.id).autorelease();
		return new NSArray(temp2.id);
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
		OS.NSAccessibilityPostNotification(control.view.id, OS.NSAccessibilitySelectedChildrenChangedNotification.id);
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
		OS.NSAccessibilityPostNotification(control.view.id, OS.NSAccessibilityFocusedUIElementChangedNotification.id);
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
		OS.NSAccessibilityPostNotification(control.view.id, OS.NSAccessibilitySelectedTextChangedNotification.id);
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
		OS.NSAccessibilityPostNotification(control.view.id, OS.NSAccessibilityValueChangedNotification.id);
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
		OS.NSAccessibilityPostNotification(control.view.id, OS.NSAccessibilitySelectedTextChangedNotification.id);
	}
	
	id childIDToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) {
			return control.view;
		}

		/* Check cache for childID, if found, return corresponding osChildID. */
		SWTAccessibleDelegate childRef = (SWTAccessibleDelegate) children.get(new Integer(childID));
		
		if (childRef == null) {
			childRef = new SWTAccessibleDelegate(this, childID);
			children.put(new Integer(childID), childRef);
		}
		
		return childRef;
	}

	NSString concatStringsAsRole(NSString str1, NSString str2) {
		NSString returnValue = str1;
		returnValue = returnValue.stringByAppendingString(NSString.stringWith(":"));
		returnValue = returnValue.stringByAppendingString(str2);
		return returnValue;
	}	
	
	String roleToOs(int role) {
		NSString nsReturnValue = null; //OS.NSAccessibilityUnknownRole;
		
		switch (role) {
			case ACC.ROLE_CLIENT_AREA: nsReturnValue = OS.NSAccessibilityGroupRole; break;
			case ACC.ROLE_WINDOW: nsReturnValue = OS.NSAccessibilityWindowRole; break;
			case ACC.ROLE_MENUBAR: nsReturnValue = OS.NSAccessibilityMenuBarRole; break;
			case ACC.ROLE_MENU: nsReturnValue = OS.NSAccessibilityMenuRole; break;
			case ACC.ROLE_MENUITEM: nsReturnValue = OS.NSAccessibilityMenuItemRole; break;
			case ACC.ROLE_SEPARATOR: nsReturnValue = OS.NSAccessibilitySplitterRole; break;
			case ACC.ROLE_TOOLTIP: nsReturnValue = OS.NSAccessibilityHelpTagRole; break;
			case ACC.ROLE_SCROLLBAR: nsReturnValue = OS.NSAccessibilityScrollBarRole; break;
			case ACC.ROLE_DIALOG: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityWindowRole, OS.NSAccessibilityDialogSubrole); break;
			case ACC.ROLE_LABEL: nsReturnValue = OS.NSAccessibilityStaticTextRole; break;
			case ACC.ROLE_PUSHBUTTON: nsReturnValue = OS.NSAccessibilityButtonRole; break;
			case ACC.ROLE_CHECKBUTTON: nsReturnValue = OS.NSAccessibilityCheckBoxRole; break;
			case ACC.ROLE_RADIOBUTTON: nsReturnValue = OS.NSAccessibilityRadioButtonRole; break;
			case ACC.ROLE_SPLITBUTTON: nsReturnValue = OS.NSAccessibilityMenuButtonRole; break;
			case ACC.ROLE_COMBOBOX: nsReturnValue = OS.NSAccessibilityComboBoxRole; break;
			case ACC.ROLE_TEXT: {
				int style = control.getStyle();
				
				if ((style & SWT.MULTI) != 0) {
					nsReturnValue = OS.NSAccessibilityTextAreaRole;
				} else {
					nsReturnValue = OS.NSAccessibilityTextFieldRole;
				}
				
				break;
			}
			case ACC.ROLE_TOOLBAR: nsReturnValue = OS.NSAccessibilityToolbarRole; break;
			case ACC.ROLE_LIST: nsReturnValue = OS.NSAccessibilityOutlineRole; break;
			case ACC.ROLE_LISTITEM: nsReturnValue = OS.NSAccessibilityStaticTextRole; break;
			case ACC.ROLE_TABLE: nsReturnValue = OS.NSAccessibilityTableRole; break;
			case ACC.ROLE_TABLECELL: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole); break;
			case ACC.ROLE_TABLECOLUMNHEADER: nsReturnValue = OS.NSAccessibilitySortButtonRole; break;
			case ACC.ROLE_TABLEROWHEADER: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole); break;
			case ACC.ROLE_TREE: nsReturnValue = OS.NSAccessibilityOutlineRole; break;
			case ACC.ROLE_TREEITEM: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityOutlineRole, OS.NSAccessibilityOutlineRowSubrole); break;
			case ACC.ROLE_TABFOLDER: nsReturnValue = OS.NSAccessibilityTabGroupRole; break;
			case ACC.ROLE_TABITEM: nsReturnValue = OS.NSAccessibilityRadioButtonRole; break;
			case ACC.ROLE_PROGRESSBAR: nsReturnValue = OS.NSAccessibilityProgressIndicatorRole; break;
			case ACC.ROLE_SLIDER: nsReturnValue = OS.NSAccessibilitySliderRole; break;
			case ACC.ROLE_LINK: nsReturnValue = OS.NSAccessibilityLinkRole; break;
		}

		return nsReturnValue.getString();
	}

	int osToRole(NSString osRole) {
		if (osRole == null) return 0;
		if (osRole.isEqualToString(OS.NSAccessibilityWindowRole)) return ACC.ROLE_WINDOW;
		if (osRole.isEqualToString(OS.NSAccessibilityMenuBarRole)) return ACC.ROLE_MENUBAR;
		if (osRole.isEqualToString(OS.NSAccessibilityMenuRole)) return ACC.ROLE_MENU;
		if (osRole.isEqualToString(OS.NSAccessibilityMenuItemRole)) return ACC.ROLE_MENUITEM;
		if (osRole.isEqualToString(OS.NSAccessibilitySplitterRole)) return ACC.ROLE_SEPARATOR;
		if (osRole.isEqualToString(OS.NSAccessibilityHelpTagRole)) return ACC.ROLE_TOOLTIP;
		if (osRole.isEqualToString(OS.NSAccessibilityScrollBarRole)) return ACC.ROLE_SCROLLBAR;
		if (osRole.isEqualToString(OS.NSAccessibilityScrollAreaRole)) return ACC.ROLE_LIST;
		if (osRole.isEqualToString(concatStringsAsRole(OS.NSAccessibilityWindowRole, OS.NSAccessibilityDialogSubrole))) return ACC.ROLE_DIALOG;
		if (osRole.isEqualToString(concatStringsAsRole(OS.NSAccessibilityWindowRole, OS.NSAccessibilitySystemDialogSubrole))) return ACC.ROLE_DIALOG;
		if (osRole.isEqualToString(OS.NSAccessibilityStaticTextRole)) return ACC.ROLE_LABEL;
		if (osRole.isEqualToString(OS.NSAccessibilityButtonRole)) return ACC.ROLE_PUSHBUTTON;
		if (osRole.isEqualToString(OS.NSAccessibilityCheckBoxRole)) return ACC.ROLE_CHECKBUTTON;
		if (osRole.isEqualToString(OS.NSAccessibilityRadioButtonRole)) return ACC.ROLE_RADIOBUTTON;
		if (osRole.isEqualToString(OS.NSAccessibilityMenuButtonRole)) return ACC.ROLE_SPLITBUTTON;
		if (osRole.isEqualToString(OS.NSAccessibilityComboBoxRole)) return ACC.ROLE_COMBOBOX;
		if (osRole.isEqualToString(OS.NSAccessibilityTextFieldRole)) return ACC.ROLE_TEXT;
		if (osRole.isEqualToString(OS.NSAccessibilityTextAreaRole)) return ACC.ROLE_TEXT;
		if (osRole.isEqualToString(OS.NSAccessibilityToolbarRole)) return ACC.ROLE_TOOLBAR;
		if (osRole.isEqualToString(OS.NSAccessibilityListRole)) return ACC.ROLE_LIST;
		if (osRole.isEqualToString(OS.NSAccessibilityTableRole)) return ACC.ROLE_TABLE;
		if (osRole.isEqualToString(OS.NSAccessibilityColumnRole)) return ACC.ROLE_TABLECOLUMNHEADER;
		if (osRole.isEqualToString(concatStringsAsRole(OS.NSAccessibilityButtonRole, OS.NSAccessibilitySortButtonRole))) return ACC.ROLE_TABLECOLUMNHEADER;
		if (osRole.isEqualToString(concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole))) return ACC.ROLE_TABLEROWHEADER;
		if (osRole.isEqualToString(OS.NSAccessibilityOutlineRole)) return ACC.ROLE_TREE;
		if (osRole.isEqualToString(concatStringsAsRole(OS.NSAccessibilityOutlineRole, OS.NSAccessibilityOutlineRowSubrole))) return ACC.ROLE_TREEITEM;
		if (osRole.isEqualToString(OS.NSAccessibilityTabGroupRole)) return ACC.ROLE_TABFOLDER;
		if (osRole.isEqualToString(OS.NSAccessibilityProgressIndicatorRole)) return ACC.ROLE_PROGRESSBAR;
		if (osRole.isEqualToString(OS.NSAccessibilitySliderRole)) return ACC.ROLE_SLIDER;
		if (osRole.isEqualToString(OS.NSAccessibilityLinkRole)) return ACC.ROLE_LINK;
		return ACC.ROLE_CLIENT_AREA;
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

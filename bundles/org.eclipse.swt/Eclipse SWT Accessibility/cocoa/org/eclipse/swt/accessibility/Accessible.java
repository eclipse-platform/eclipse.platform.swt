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

	static final int MAX_RELATION_TYPES = 15;

	static NSString[] baseAttributes = { 
		OS.NSAccessibilityRoleAttribute,
		OS.NSAccessibilitySubroleAttribute,
		OS.NSAccessibilityRoleDescriptionAttribute,
		OS.NSAccessibilityHelpAttribute,
		OS.NSAccessibilityFocusedAttribute,
		OS.NSAccessibilityParentAttribute,
		OS.NSAccessibilityChildrenAttribute,
		OS.NSAccessibilityPositionAttribute,
		OS.NSAccessibilitySizeAttribute,
		OS.NSAccessibilityWindowAttribute,
		OS.NSAccessibilityTopLevelUIElementAttribute,
		OS.NSAccessibilityEnabledAttribute,
	};
	
	NSMutableArray attributeNames = null;
	NSMutableArray parameterizedAttributeNames = null;
	NSMutableArray actionNames = null;

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
	Relation relations[] = new Relation[MAX_RELATION_TYPES];
	Accessible parent;
	Control control;

	Map /*<Integer, SWTAccessibleDelegate>*/ childToIdMap = new HashMap();
	Vector children = new Vector();
	SWTAccessibleDelegate delegate;

	int index = -1;
	
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
		delegate = new SWTAccessibleDelegate(this, ACC.CHILDID_SELF);
		parent.children.add(this);
	}
	
	/**
	 * @since 3.5
	 * @deprecated
	 */
	protected Accessible() {
	}

	Accessible(Control control) {
		this.control = control;
	}
	
	void addAccessibleChild(Accessible accessible) {
		children.add(accessible);
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
	
	id accessibleHandle(Accessible accessible) {
		if (accessible.delegate != null) return accessible.delegate;
		if (accessible.control != null) {
			NSView view = accessible.control.view;
			int /*long*/ handle = OS.objc_msgSend(view.id, OS.sel_accessibleHandle);
			return new id(handle);
		}
		return null;
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
		if (relations[type] == null) {
			relations[type] = new Relation(this, type);
		}
		relations[type].addTarget(target);
	}
	
	id getColumnIndexRangeAttribute(int childID) {
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListeners.size(); i++) {
			AccessibleTableCellListener listener = (AccessibleTableCellListener) accessibleTableCellListeners.elementAt(i);
			listener.getColumnIndex(event);
			listener.getColumnSpan(event);
		}
		NSRange range = new NSRange();
		range.location = event.index;
		range.length = event.count;
		return NSValue.valueWithRange(range);
	}

	id getRowIndexRangeAttribute(int childID) {
		AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
		for (int i = 0; i < accessibleTableCellListeners.size(); i++) {
			AccessibleTableCellListener listener = (AccessibleTableCellListener) accessibleTableCellListeners.elementAt(i);
			listener.getRowIndex(event);
			listener.getRowSpan(event);
		}
		NSRange range = new NSRange();
		range.location = event.index;
		range.length = event.count;
		return NSValue.valueWithRange(range);
	}

	id getSelectedAttribute(int childID) {
		if (accessibleTableListeners.size() > 0) {
			AccessibleTableEvent event = new AccessibleTableEvent(this);
			for (int i = 0; i < accessibleTableListeners.size(); i++) {
				AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
				listener.getSelectedRows(event);
			}
			if (event.accessibles != null) {
				for (int i = 0; i < event.accessibles.length; i++) {
					if (this.equals(event.accessibles[i])) return NSNumber.numberWithBool(true);
				}
			}
			for (int i = 0; i < accessibleTableListeners.size(); i++) {
				AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
				listener.getSelectedColumns(event);
			}
			if (event.accessibles != null) {
				for (int i = 0; i < event.accessibles.length; i++) {
					if (this.equals(event.accessibles[i])) return NSNumber.numberWithBool(true);
				}
			}
		}
		if (accessibleTableCellListeners.size() > 0) {
			AccessibleTableCellEvent event = new AccessibleTableCellEvent(this);
			for (int i = 0; i < accessibleTableCellListeners.size(); i++) {
				AccessibleTableCellListener listener = (AccessibleTableCellListener) accessibleTableCellListeners.elementAt(i);
				listener.isSelected(event);
			}
			return NSNumber.numberWithBool(event.isSelected);
		}
		return NSNumber.numberWithBool(false);
	}

	id getIndexAttribute(int childID) {
		return NSNumber.numberWithInt(index);
	}

	id getHeaderAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent tableEvent = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getColumnHeader(tableEvent);
		}
		if (tableEvent.accessible != null) returnValue = tableEvent.accessible.delegate;
		
		return returnValue;
	}
	
	id getVisibleColumnsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getVisibleColumns(event);
		}
		if (event.accessibles != null) {
			NSMutableArray array = NSMutableArray.arrayWithCapacity(event.accessibles.length);
			Accessible[] accessibles = event.accessibles;
			for (int i = 0; i < accessibles.length; i++) {
				Accessible acc = accessibles[i];
				array.addObject(acc.delegate);
			}
			returnValue = array;
		}
		return returnValue == null ? NSArray.array() : returnValue;
	}

	id getVisibleRowsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getVisibleRows(event);
		}
		if (event.accessibles != null) {
			NSMutableArray array = NSMutableArray.arrayWithCapacity(event.accessibles.length);
			Accessible[] accessibles = event.accessibles;
			for (int i = 0; i < accessibles.length; i++) {
				Accessible acc = accessibles[i];
				array.addObject(acc.delegate);
			}
			returnValue = array;
		}
		return returnValue == null ? NSArray.array() : returnValue;
	}

	id getSelectedRowsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getSelectedRows(event);
		}
		if (event.selected != null) {
			int[] selected = (int[])event.selected;
			NSMutableArray array = NSMutableArray.arrayWithCapacity(selected.length);
			for (int i = 0; i < selected.length; i++) {
				event.row = selected[i];
				for (int j = 0; j < accessibleTableListeners.size(); j++) {
					AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(j);
					listener.getRow(event);	
				}
				if (event.accessible != null) array.addObject(event.accessible.delegate);
			}
			returnValue = array;
		} else {
			returnValue = NSArray.array();
		}
		return returnValue;
	}
	
	id getRowsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getRows(event);
		}
		if (event.accessibles != null) {
			NSMutableArray array = NSMutableArray.arrayWithCapacity(event.accessibles.length);
			Object[] rows = event.accessibles;
			for (int i = 0; i < rows.length; i++) {
				Accessible acc = (Accessible) rows[i];
				acc.index = i;
				array.addObject(acc.delegate);
			}
			returnValue = array;
		}
		return returnValue == null ? NSArray.array() : returnValue;
	}

	id getSelectedColumnsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getSelectedColumns(event);
		}
		if (event.selected != null) {
			int[] selected = (int[])event.selected;
			NSMutableArray array = NSMutableArray.arrayWithCapacity(selected.length);
			for (int i = 0; i < selected.length; i++) {
				event.column = selected[i];
				for (int j = 0; j < accessibleTableListeners.size(); j++) {
					AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(j);
					listener.getColumn(event);	
				}
				if (event.accessible != null) array.addObject(event.accessible.delegate);
			}
			returnValue = array;
		} else {
			returnValue = NSArray.array();
		}
		return returnValue;
	}
	
	id getColumnsAttribute(int childID) {
		id returnValue = null;
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < accessibleTableListeners.size(); i++) {
			AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
			listener.getColumns(event);
		}
		if (event.accessibles != null) {
			NSMutableArray array = NSMutableArray.arrayWithCapacity(event.accessibles.length);
			Accessible[] accessibles = event.accessibles;
			for (int i = 0; i < accessibles.length; i++) {
				Accessible acc = accessibles[i];
				acc.index = i;
				array.addObject(acc.delegate);
			}
			returnValue = array;
		}
		return returnValue == null ? NSArray.array() : returnValue;
	}
	
	public id internal_accessibilityActionDescription(NSString action, int childID) {
		NSString returnValue = NSString.string();
		String actionName = action.getString();
		if (accessibleActionListeners.size() > 0) {
			AccessibleActionEvent event = new AccessibleActionEvent(this);
			for (int i = 0; i < accessibleActionListeners.size(); i++) {
				AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(i);
				listener.getActionCount(event);
			}
			int index = -1;
			for (int i = 0; i < event.count; i++) {
				event.index = i;
				for (int j = 0; j < accessibleActionListeners.size(); j++) {
					AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(j);
					listener.getName(event);
				}
				if (actionName.equals(event.result)) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				event.index = index;
				event.result = null;
				for (int i = 0; i < accessibleActionListeners.size(); i++) {
					AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(i);
					listener.getDescription(event);
				}
				if (event.result != null) returnValue = NSString.stringWith(event.result);
			}
		} 
		return returnValue;
	}
	
	public NSArray internal_accessibilityActionNames(int childID) {
		if (accessibleActionListeners.size() > 0) {
			AccessibleActionEvent event = new AccessibleActionEvent(this);
			for (int i = 0; i < accessibleActionListeners.size(); i++) {
				AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(i);
				listener.getActionCount(event);
			}
			NSMutableArray array = NSMutableArray.arrayWithCapacity(event.count);
			for (int i = 0; i < event.count; i++) {
				event.index = i;
				for (int j = 0; j < accessibleActionListeners.size(); j++) {
					AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(j);
					listener.getName(event);
				}
				array.addObject(NSString.stringWith(event.result));	
			}
			return array;
		} else {
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
				case ACC.ROLE_LINK:
				case ACC.ROLE_CHECKMENUITEM:
				case ACC.ROLE_RADIOMENUITEM:
				case ACC.ROLE_SPLITBUTTON:
					returnValue.addObject(OS.NSAccessibilityPressAction);
					break;
				case ACC.ROLE_COMBOBOX:
					returnValue.addObject(OS.NSAccessibilityConfirmAction);
					break;
				case ACC.ROLE_WINDOW:
				case ACC.ROLE_DIALOG:
//					TODO
//					returnValue.addObject(OS.NSAccessibilityRaiseAction);
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
	}
	
	public boolean internal_accessibilityIsAttributeSettable(NSString attribute) {
		if (accessibleValueListeners.size() > 0) {
			if (attribute.isEqualToString(OS.NSAccessibilityValueAttribute)) return true;
		}
		return false;
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
			// See if this object has a label or is a label for something else. If so, add that to the list.
			if (relations[ACC.RELATION_LABEL_FOR] != null) {
				if (!attributeNames.containsObject(OS.NSAccessibilityServesAsTitleForUIElementsAttribute)) attributeNames.addObject(OS.NSAccessibilityServesAsTitleForUIElementsAttribute);
			} else {
				attributeNames.removeObject(OS.NSAccessibilityServesAsTitleForUIElementsAttribute);
			}
			
			if (relations[ACC.RELATION_LABELLED_BY] != null) {
				if (!attributeNames.containsObject(OS.NSAccessibilityTitleUIElementAttribute)) attributeNames.addObject(OS.NSAccessibilityTitleUIElementAttribute);
			} else {
				attributeNames.removeObject(OS.NSAccessibilityTitleUIElementAttribute);
			}

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
		
		switch(event.detail) {
			case ACC.ROLE_CLIENT_AREA:
				break;
			case ACC.ROLE_WINDOW:
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
//				TODO
//				returnValue.addObject(OS.NSAccessibilityMainAttribute);
//				returnValue.addObject(OS.NSAccessibilityMinimizedAttribute);
				break;
			case ACC.ROLE_MENUBAR:
				returnValue.addObject(OS.NSAccessibilitySelectedChildrenAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleChildrenAttribute);
				break;
			case ACC.ROLE_MENU:
				returnValue.addObject(OS.NSAccessibilitySelectedChildrenAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleChildrenAttribute);
				break;
			case ACC.ROLE_MENUITEM:
				break;
			case ACC.ROLE_SEPARATOR:
				returnValue.addObject(OS.NSAccessibilityMaxValueAttribute);
				returnValue.addObject(OS.NSAccessibilityMinValueAttribute);
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				//TODO
//				returnValue.addObject(OS.NSAccessibilityOrientationAttribute);
//				returnValue.addObject(OS.NSAccessibilityPreviousContentsAttribute);
//				returnValue.addObject(OS.NSAccessibilityNextContentsAttribute);
				break;
			case ACC.ROLE_TOOLTIP:
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
				break;
			case ACC.ROLE_SCROLLBAR:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				//TODO
//				returnValue.addObject(OS.NSAccessibilityOrientationAttribute);
				break;
			case ACC.ROLE_DIALOG:
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
//				TODO
//				returnValue.addObject(OS.NSAccessibilityMainAttribute);
//				returnValue.addObject(OS.NSAccessibilityMinimizedAttribute);
				break;
			case ACC.ROLE_LABEL:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				break;
			case ACC.ROLE_PUSHBUTTON:
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
				break;
			case ACC.ROLE_CHECKBUTTON:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
				break;
			case ACC.ROLE_RADIOBUTTON:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
				break;
			case ACC.ROLE_SPLITBUTTON:
				break;
			case ACC.ROLE_COMBOBOX:
				returnValue.addObject(OS.NSAccessibilityExpandedAttribute);
				returnValue.addObject(OS.NSAccessibilityNumberOfCharactersAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedTextAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedTextRangeAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleCharacterRangeAttribute);
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				break;
			case ACC.ROLE_TEXT:
			case ACC.ROLE_PARAGRAPH:
			case ACC.ROLE_HEADING:
				returnValue.addObject(OS.NSAccessibilityNumberOfCharactersAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedTextAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedTextRangeAttribute);
				returnValue.addObject(OS.NSAccessibilityInsertionPointLineNumberAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedTextRangesAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleCharacterRangeAttribute);
				returnValue.addObject(OS.NSAccessibilityValueAttribute);				
				break;
			case ACC.ROLE_TOOLBAR:
				break;
			case ACC.ROLE_LIST:
				returnValue.addObject(OS.NSAccessibilityColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityRowsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityHeaderAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleChildrenAttribute);
				break;
			case ACC.ROLE_LISTITEM:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				break;
			case ACC.ROLE_TABLE:
				returnValue.addObject(OS.NSAccessibilityColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityRowsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityHeaderAttribute);
				break;
			case ACC.ROLE_TABLECELL:
				returnValue.addObject(OS.NSAccessibilitySelectedAttribute);
				if (OS.VERSION >= 0x1060) returnValue.addObject(OS.NSAccessibilityRowIndexRangeAttribute);
				if (OS.VERSION >= 0x1060) returnValue.addObject(OS.NSAccessibilityColumnIndexRangeAttribute);
				break;
			case ACC.ROLE_TABLEROWHEADER:
				returnValue.addObject(OS.NSAccessibilitySelectedAttribute);
				returnValue.addObject(OS.NSAccessibilityIndexAttribute);
				break;
			case ACC.ROLE_TREE:
				returnValue.addObject(OS.NSAccessibilityColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityRowsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityHeaderAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleColumnsAttribute);
				break;
			case ACC.ROLE_TREEITEM:
				returnValue.addObject(OS.NSAccessibilityColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedColumnsAttribute);
				returnValue.addObject(OS.NSAccessibilityRowsAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityHeaderAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleColumnsAttribute);
			case ACC.ROLE_TABFOLDER:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				returnValue.addObject(OS.NSAccessibilityContentsAttribute);
				returnValue.addObject(OS.NSAccessibilityTabsAttribute);
				break;
			case ACC.ROLE_TABITEM:
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				returnValue.addObject(OS.NSAccessibilityTitleAttribute);
				break;
			case ACC.ROLE_PROGRESSBAR:
				returnValue.addObject(OS.NSAccessibilityMaxValueAttribute);
				returnValue.addObject(OS.NSAccessibilityMinValueAttribute);
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				break;
			case ACC.ROLE_SLIDER:
				returnValue.addObject(OS.NSAccessibilityMaxValueAttribute);
				returnValue.addObject(OS.NSAccessibilityMinValueAttribute);
				returnValue.addObject(OS.NSAccessibilityValueAttribute);
				//TODO
//				returnValue.addObject(OS.NSAccessibilityOrientationAttribute);
//				increment
//				decrement
				break;
			case ACC.ROLE_LINK:
				//TODO
//				returnValue.addObject(OS.NSAccessibilityURLAttribute);
//				visited
				break;
			case ACC.ROLE_ALERT:
				break;
			case ACC.ROLE_ANIMATION:
				break;
			case ACC.ROLE_CANVAS:
				break;
			case ACC.ROLE_COLUMN:
				returnValue.addObject(OS.NSAccessibilityIndexAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedAttribute);
				returnValue.addObject(OS.NSAccessibilityRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityVisibleRowsAttribute);
				returnValue.addObject(OS.NSAccessibilityHeaderAttribute);
				break;
			case ACC.ROLE_DOCUMENT:
				break;
			case ACC.ROLE_GRAPHIC:
				break;
			case ACC.ROLE_GROUP:
				break;
			case ACC.ROLE_ROW:
				returnValue.addObject(OS.NSAccessibilityVisibleChildrenAttribute);
				returnValue.addObject(OS.NSAccessibilityIndexAttribute);
				returnValue.addObject(OS.NSAccessibilitySelectedAttribute);
				break;
			case ACC.ROLE_SPINBUTTON:
				break;
			case ACC.ROLE_STATUSBAR:
				break;
			case ACC.ROLE_CHECKMENUITEM:
				break;
			case ACC.ROLE_RADIOMENUITEM:
				break;
			case ACC.ROLE_CLOCK:
				break;
			case ACC.ROLE_DATETIME:
				break;
			case ACC.ROLE_CALENDAR:
				break;
			case ACC.ROLE_FOOTER:
				break;
			case ACC.ROLE_HEADER:
				break;
			case ACC.ROLE_FORM:
				break;
			case ACC.ROLE_PAGE:
				break;
			case ACC.ROLE_SECTION:
				break;
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
		if (attribute.isEqualToString(OS.NSAccessibilityMaxValueAttribute)) return getMaxValueAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityMinValueAttribute)) return getMinValueAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityEnabledAttribute)) return getEnabledAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityFocusedAttribute)) return getFocusedAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityParentAttribute)) return getParentAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityChildrenAttribute)) return getChildrenAttribute(childID);
		/* SWT has no visible children API*/
		if (attribute.isEqualToString(OS.NSAccessibilityVisibleChildrenAttribute)) return getChildrenAttribute(childID);
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
		if (attribute.isEqualToString(OS.NSAccessibilityServesAsTitleForUIElementsAttribute)) return getServesAsTitleForUIElementsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityTitleUIElementAttribute)) return getTitleUIElementAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityColumnsAttribute)) return getColumnsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedColumnsAttribute)) return getSelectedColumnsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityRowsAttribute)) return getRowsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedRowsAttribute)) return getSelectedRowsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityVisibleRowsAttribute)) return getVisibleRowsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityVisibleColumnsAttribute)) return getVisibleColumnsAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityHeaderAttribute)) return getHeaderAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilityIndexAttribute)) return getIndexAttribute(childID);
		if (attribute.isEqualToString(OS.NSAccessibilitySelectedAttribute)) return getSelectedAttribute(childID);
		if (OS.VERSION >= 0x1060 && attribute.isEqualToString(OS.NSAccessibilityRowIndexRangeAttribute)) return getRowIndexRangeAttribute(childID);
		if (OS.VERSION >= 0x1060 && attribute.isEqualToString(OS.NSAccessibilityColumnIndexRangeAttribute)) return getColumnIndexRangeAttribute(childID);
		
		// If this object don't know how to get the value it's up to the control itself to return an attribute value.
		return null;
	}

	public id internal_accessibilityAttributeValue_forParameter(NSString attribute, id parameter, int childID) {
		if (attribute.isEqualToString(OS.NSAccessibilityStringForRangeParameterizedAttribute)) return getStringForRangeParameterizedAttribute(parameter, childID);
		if (attribute.isEqualToString(OS.NSAccessibilityRangeForLineParameterizedAttribute)) return getRangeForLineParameterizedAttribute(parameter, childID);
		if (attribute.isEqualToString(OS.NSAccessibilityLineForIndexParameterizedAttribute)) return getLineForIndexParameterizedAttribute(parameter, childID);
		if (attribute.isEqualToString(OS.NSAccessibilityBoundsForRangeParameterizedAttribute)) return getBoundsForRangeParameterizedAttribute(parameter, childID);
		if (attribute.isEqualToString(OS.NSAccessibilityRangeForPositionParameterizedAttribute)) return getRangeForPositionParameterizedAttribute(parameter, childID);
		if (OS.VERSION >= 0x1060 && attribute.isEqualToString(OS.NSAccessibilityCellForColumnAndRowParameterizedAttribute)) return getCellForColumnAndRowParameter(parameter, childID);
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
		
		if ((childID == ACC.CHILDID_SELF) && (parameterizedAttributeNames != null)) {
			return retainedAutoreleased(parameterizedAttributeNames);
		}

		NSMutableArray returnValue = NSMutableArray.arrayWithCapacity(4);
		
		switch(event.detail) {
			case ACC.ROLE_TEXT:
			case ACC.ROLE_PARAGRAPH:
			case ACC.ROLE_HEADING:
				returnValue.addObject(OS.NSAccessibilityStringForRangeParameterizedAttribute);
				returnValue.addObject(OS.NSAccessibilityRangeForLineParameterizedAttribute);
				returnValue.addObject(OS.NSAccessibilityLineForIndexParameterizedAttribute);
				returnValue.addObject(OS.NSAccessibilityBoundsForRangeParameterizedAttribute);
				returnValue.addObject(OS.NSAccessibilityRangeForPositionParameterizedAttribute);
				break;
			case ACC.ROLE_TABLE:
				if (OS.VERSION >= 0x1060) returnValue.addObject(OS.NSAccessibilityCellForColumnAndRowParameterizedAttribute);
				break;
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

	public boolean internal_accessibilityPerformAction(NSString action, int childID) {
		String actionName = action.getString();
		if (accessibleActionListeners.size() > 0) {
			AccessibleActionEvent event = new AccessibleActionEvent(this);
			for (int i = 0; i < accessibleActionListeners.size(); i++) {
				AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(i);
				listener.getActionCount(event);
			}
			int index = -1;
			for (int i = 0; i < event.count; i++) {
				event.index = i;
				for (int j = 0; j < accessibleActionListeners.size(); j++) {
					AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(j);
					listener.getName(event);
				}
				if (actionName.equals(event.result)) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				event.index = index;
				event.result = null;
				for (int i = 0; i < accessibleActionListeners.size(); i++) {
					AccessibleActionListener listener = (AccessibleActionListener) accessibleActionListeners.elementAt(i);
					listener.doAction(event);
				}
				return ACC.OK.equals(event.result);
			}
		} 
		return false;
	}

	public void internal_accessibilitySetValue_forAttribute(id value, NSString attribute) {
		if (accessibleValueListeners.size() > 0) {
			AccessibleValueEvent event = new AccessibleValueEvent(this);
			NSNumber number = new NSNumber(value);
			event.value = new Double(number.doubleValue());
			for (int i = 0; i < accessibleValueListeners.size(); i++) {
				AccessibleValueListener listener = (AccessibleValueListener) accessibleValueListeners.elementAt(i);
				listener.setCurrentValue(event);
			}
		}
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
		release(true);
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
		release(true);
	}

	id getBoundsForRangeParameterizedAttribute(id parameter, int childID) {
		id returnValue = null;
		NSValue parameterObject = new NSValue(parameter.id);
		NSRange range = parameterObject.rangeValue();
		NSRect rect = new NSRect();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event  = new AccessibleTextEvent(this);
			event.childID = childID;
			event.start = (int)/*64*/range.location;
			event.end = (int)/*64*/(range.location + range.length);
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getTextBounds(event);
			}
			rect.x = event.x;
			rect.y = event.y;
			rect.width = event.width;
			rect.height = event.height;
			returnValue = NSValue.valueWithRect(rect);
		} else {
			//FIXME???
			//how to implement with old listener
		}
		returnValue = NSValue.valueWithRect(rect);
		return returnValue;
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
		
		return returnValue == null ? NSString.string() : returnValue;
	}

	id getRangeForPositionParameterizedAttribute(id parameter, int childID) {
		id returnValue = null;
		NSValue parameterObject = new NSValue(parameter.id);
		NSPoint point = parameterObject.pointValue();
		NSRange range = new NSRange();
		if (accessibleTextExtendedListeners.size() > 0) {
			NSArray screens = NSScreen.screens();
			NSScreen screen = new NSScreen(screens.objectAtIndex(0));
			NSRect frame = screen.frame();
			point.y = frame.height - point.y;
			AccessibleTextEvent event  = new AccessibleTextEvent(this);
			event.childID = childID;
			event.x = (int)point.x;
			event.y = (int)point.y;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getOffsetAtPoint(event);
			}
			range.location = event.offset;
			range.length = 1;
		} else {
			//FIXME???
			//how to implement with old listener
		}
		returnValue = NSValue.valueWithRange(range);
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
		
		id returnValue = NSString.string();
		
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
	
	id getTitleUIElementAttribute(int childID) {
		id returnValue = null;
		Relation relation = relations[ACC.RELATION_LABELLED_BY]; 
		if (relation != null) {
			returnValue = relation.getTitleUIElement();
		}
		return returnValue;
	}
	
	id getValueAttribute (int childID) {
		id returnValue = null;
		if (accessibleValueListeners.size() > 0) {
			AccessibleValueEvent event = new AccessibleValueEvent(this);
			for (int i = 0; i < accessibleValueListeners.size(); i++) {
				AccessibleValueListener listener = (AccessibleValueListener) accessibleValueListeners.elementAt(i);
				listener.getCurrentValue(event);
			}
			returnValue = NSNumber.numberWithDouble(event.value.doubleValue());
		} else {
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
							returnValue = NSNumber.numberWithBool(number == 0 ? false : true);
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
				case ACC.ROLE_PARAGRAPH: // text in the text field
				case ACC.ROLE_HEADING: // text in the text field
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
					returnValue = returnValue == null ? NSString.string() : returnValue;
					break;
			}
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
		boolean enabled = (event.detail & ACC.STATE_DISABLED) == 0;
		if (!enabled && delegate == null) enabled = control.isEnabled();
		return NSNumber.numberWithBool(enabled);
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
		boolean hasFocus = control.isFocusControl();
		return NSNumber.numberWithBool(hasFocus);
	}
	
	id getParentAttribute (int childID) {
		id returnValue = null;
		if (childID == ACC.CHILDID_SELF) {
			if (parent != null) {
				if (parent.delegate != null) {
					returnValue = parent.delegate;
				} else {
					returnValue = new id(OS.NSAccessibilityUnignoredAncestor(accessibleHandle(parent).id));
				}
			} else {
				// Returning null here means 'let Cocoa figure it out.'
				returnValue = null;
			}
		} else {
			returnValue = new id(OS.NSAccessibilityUnignoredAncestor(accessibleHandle(this).id));
		}
		return returnValue;
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
			int childCount = event.detail;
			if (childCount >= 0) {
				for (int i = 0; i < accessibleControlListeners.size(); i++) {
					AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
					listener.getChildren(event);
				}
				Object[] children = event.children;
				NSMutableArray childArray = NSMutableArray.arrayWithCapacity(childCount);
				for (int i = 0; i < childCount; i++) {
					Object child = children[i];
					if (child instanceof Accessible) {
						Accessible accessible = (Accessible)child;
						if (accessible.delegate != null) {
							childArray.addObject(accessible.delegate);
						} else {
							childArray.addObject(accessibleHandle(accessible));
						}
					} else {
						if (child instanceof Integer) {
							id accChild = childIDToOs(((Integer)child).intValue());							
							childArray.addObject(accChild);
						} 
					}
				}
				returnValue = new id(OS.NSAccessibilityUnignoredChildren(childArray.id));
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
	
	id getCellForColumnAndRowParameter(id parameter, int childID) {
		id returnValue = null;
		NSArray parameterObject = new NSArray(parameter.id);
		if (parameterObject.count() == 2) {
			AccessibleTableEvent event = new AccessibleTableEvent(this);
			event.column = new NSNumber(parameterObject.objectAtIndex(0)).intValue();
			event.row = new NSNumber(parameterObject.objectAtIndex(1)).intValue();
			for (int i = 0; i < accessibleTableListeners.size(); i++) {
				AccessibleTableListener listener = (AccessibleTableListener)accessibleTableListeners.elementAt(i);
				listener.getCell(event);
				returnValue = event.accessible.delegate;
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
			if (control instanceof Composite) returnValue = NSString.string();
		}

		return returnValue;
	}
	
	id getInsertionPointLineNumberAttribute (int childID) {
		id returnValue = null;
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event  = new AccessibleTextEvent(this);
			event.childID = childID;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getCaretOffset(event);
			}
			int caretOffset = event.offset;
			event.start = caretOffset;
			event.end = caretOffset;
			event.count = Integer.MIN_VALUE;
			event.type = ACC.TEXT_BOUNDARY_LINE;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getText(event);
			}
			returnValue = NSNumber.numberWithInt(Math.max(0, -event.count));
		} else {
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
		}
		return returnValue;
	}
	
	id getLineForIndexParameterizedAttribute (id parameter, int childID) {
		id returnValue = null;
		NSNumber charNumberObj = new NSNumber(parameter.id);		
		int charNumber = charNumberObj.intValue();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			event.start = charNumber;
			event.end = charNumber;
			event.count = Integer.MIN_VALUE;
			event.type = ACC.TEXT_BOUNDARY_LINE;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getText(event);
			}
			returnValue = NSNumber.numberWithInt(Math.max(0, -event.count));
		} else {
			AccessibleControlEvent controlEvent = new AccessibleControlEvent(this);
			controlEvent.childID = childID;
			controlEvent.result = null;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getValue(controlEvent);
			}
			String text = controlEvent.result;
			if (text != null) returnValue = NSNumber.numberWithInt(lineNumberForOffset(text, charNumber));
		}
		return returnValue;
	}
	
	id getMaxValueAttribute(int childID) {
		id returnValue = null;
		if (accessibleValueListeners.size() > 0) {
			AccessibleValueEvent event = new AccessibleValueEvent(this);
			for (int i = 0; i < accessibleValueListeners.size(); i++) {
				AccessibleValueListener listener = (AccessibleValueListener) accessibleValueListeners.elementAt(i);
				listener.getMaximumValue(event);
			}
			returnValue = NSNumber.numberWithDouble(event.value.doubleValue());
		}
		return returnValue;
	}
	
	id getMinValueAttribute(int childID) {
		id returnValue = null;
		if (accessibleValueListeners.size() > 0) {
			AccessibleValueEvent event = new AccessibleValueEvent(this);
			for (int i = 0; i < accessibleValueListeners.size(); i++) {
				AccessibleValueListener listener = (AccessibleValueListener) accessibleValueListeners.elementAt(i);
				listener.getMinimumValue(event);
			}
			returnValue = NSNumber.numberWithDouble(event.value.doubleValue());
		}
		return returnValue;
	}
	
	id getNumberOfCharactersAttribute (int childID) {
		id returnValue = null;
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getCharacterCount(event);
			}
			returnValue = NSNumber.numberWithInt(event.count);
		} else {
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
		}
		return returnValue;
	}
	
	id getRangeForLineParameterizedAttribute (id parameter, int childID) {
		id returnValue = null;
		// The parameter is an NSNumber with the line number.
		NSNumber lineNumberObj = new NSNumber(parameter.id);		
		int lineNumber = lineNumberObj.intValue();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			event.start = event.end = 0;
			event.count = lineNumber;
			event.type = ACC.TEXT_BOUNDARY_LINE;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getText(event);
			}
			NSRange range = new NSRange();
			range.location = event.start;
			range.length = event.end - event.start;
			returnValue = NSValue.valueWithRange(range);
		} else {
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
		}
		return returnValue;
	}
	
	id getSelectedTextAttribute (int childID) {
		id returnValue = NSString.string();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			event.index = 0;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getSelection(event);
			}
			int start = event.start;
			int end = event.end;
			if (start != end) {
				event.type = ACC.TEXT_BOUNDARY_ALL;
				for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
					listener.getText(event);
				}
			}
			String text = event.result;
			if (text != null) returnValue = NSString.stringWith(text);
		} else {
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
		}
		return returnValue;
	}
	
	id getSelectedTextRangeAttribute (int childID) {
		id returnValue = null;
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			event.index = 0;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getSelection(event);
			}
			NSRange range = new NSRange();
			range.location = event.start;
			range.length = event.end - event.start;
			returnValue = NSValue.valueWithRange(range);
		} else {
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
		}
		return returnValue;
	}
	
	id getServesAsTitleForUIElementsAttribute(int childID) {
		id returnValue = null;
		Relation relation = relations[ACC.RELATION_LABEL_FOR];
		if (relation != null) returnValue = relation.getServesAsTitleForUIElements();
		return returnValue;
	}
	
	id getStringForRangeParameterizedAttribute (id parameter, int childID) {
		id returnValue = null;
		
		// Parameter is an NSRange wrapped in an NSValue. 
		NSValue parameterObject = new NSValue(parameter.id);
		NSRange range = parameterObject.rangeValue();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			event.start = (int) /*64*/ range.location;
			event.end = (int) /*64*/ (range.location + range.length);
			event.type = ACC.TEXT_BOUNDARY_ALL;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getText(event);
			}
			if (event.result != null) returnValue = NSString.stringWith(event.result);
		} else {
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
		}
		return returnValue;
	}
	
	id getSelectedTextRangesAttribute (int childID) {
		NSMutableArray returnValue = null;
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			for (int i = 0; i < accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getSelectionCount(event);
			}
			if (event.count > 0) {
				returnValue = NSMutableArray.arrayWithCapacity(event.count);
				for (int i = 0; i < event.count; i++) {
					event.index = i;
					for (int j = 0; j < accessibleTextExtendedListeners.size(); j++) {
						AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(j);
						listener.getSelection(event);
					}				
					NSRange range = new NSRange();
					range.location = event.start;
					range.length = event.end - event.start;
					returnValue.addObject(NSValue.valueWithRange(range));
				}
			}
		} else {
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
		}
		return returnValue;
	}
	
	id getVisibleCharacterRangeAttribute (int childID) {
		NSRange range = new NSRange();
		if (accessibleTextExtendedListeners.size() > 0) {
			AccessibleTextEvent event = new AccessibleTextEvent(this);
			event.childID = childID;
			for (int i=0; i<accessibleTextExtendedListeners.size(); i++) {
				AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) accessibleTextExtendedListeners.elementAt(i);
				listener.getVisibleRanges(event);
			}
			range.location = event.start;
			range.length = event.end - event.start;
		} else {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = childID;
			event.result = null;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getValue(event);
			}
			if (event.result != null) {
				range.location = 0;
				range.length = event.result.length();
			} else {
				return null;
			}
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
		//TODO: platform-specific? (we will manage the set on Windows)
	}
	
	/**
	 * Sends a message with event-specific data to accessible clients
	 * indicating that something has changed within a custom control.
	 *
	 * @param event an <code>ACC</code> constant beginning with EVENT_* indicating the message to send
	 * @param eventData an object containing event-specific data
	 * 
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver's control has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver's control</li>
	 * </ul>
	 * 
	 * @since 3.6
	 */
	public void sendEvent(int event, Object eventData) {
		checkWidget();
		//TODO
	}

	void release(boolean destroy) {
		Iterator childIter = children.iterator();
		while(childIter.hasNext()) {
			Accessible accessible = (Accessible) childIter.next();
			accessible.release(false);
		}
		children = null;
		if (destroy && parent != null) parent.children.remove(this);
		
		if (actionNames != null) actionNames.release();
		actionNames = null;
		if (attributeNames != null) attributeNames.release();
		attributeNames = null;
		if (parameterizedAttributeNames != null) parameterizedAttributeNames.release();
		parameterizedAttributeNames = null;
		if (delegate != null) delegate.release();
		delegate = null;
		relations = null;
		
		Collection delegates = childToIdMap.values();
		Iterator iter = delegates.iterator();
		while (iter.hasNext()) {
			SWTAccessibleDelegate childDelegate = (SWTAccessibleDelegate)iter.next();
			childDelegate.internal_dispose_SWTAccessibleDelegate();
		}
		childToIdMap.clear();
		childToIdMap = null;
	}
	
	void releaseAccessibleChildren() {

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
		SWTAccessibleDelegate childRef = (SWTAccessibleDelegate) childToIdMap.get(new Integer(childID));
		
		if (childRef == null) {
			childRef = new SWTAccessibleDelegate(this, childID);
			childToIdMap.put(new Integer(childID), childRef);
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
			case ACC.ROLE_COLUMN: nsReturnValue =  OS.NSAccessibilityColumnRole; break;
			case ACC.ROLE_ROW: nsReturnValue =  concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole); break;
			case ACC.ROLE_TABLE: nsReturnValue = OS.NSAccessibilityTableRole; break;
			case ACC.ROLE_TABLECELL: 
				if (OS.VERSION >= 0x1060) {
					nsReturnValue = OS.NSAccessibilityCellRole;
				} else {
					nsReturnValue = concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole);
				}
			case ACC.ROLE_TABLECOLUMNHEADER: nsReturnValue = OS.NSAccessibilitySortButtonRole; break;
			case ACC.ROLE_TABLEROWHEADER: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityRowRole, OS.NSAccessibilityTableRowSubrole); break;
			case ACC.ROLE_TREE: nsReturnValue = OS.NSAccessibilityOutlineRole; break;
			case ACC.ROLE_TREEITEM: nsReturnValue = concatStringsAsRole(OS.NSAccessibilityOutlineRole, OS.NSAccessibilityOutlineRowSubrole); break;
			case ACC.ROLE_TABFOLDER: nsReturnValue = OS.NSAccessibilityTabGroupRole; break;
			case ACC.ROLE_TABITEM: nsReturnValue = OS.NSAccessibilityRadioButtonRole; break;
			case ACC.ROLE_PROGRESSBAR: nsReturnValue = OS.NSAccessibilityProgressIndicatorRole; break;
			case ACC.ROLE_SLIDER: nsReturnValue = OS.NSAccessibilitySliderRole; break;
			case ACC.ROLE_LINK: nsReturnValue = OS.NSAccessibilityLinkRole; break;
			
			//10.6 only -> case ACC.ROLE_CANVAS: nsReturnValue = OS.NSAccessibilityLayoutAreaRole; break;
			case ACC.ROLE_CANVAS: nsReturnValue = OS.NSAccessibilityGroupRole; break;
			case ACC.ROLE_GRAPHIC: nsReturnValue = OS.NSAccessibilityImageRole; break;
		
			//CLIENT_AREA uses NSAccessibilityGroupRole already
			case ACC.ROLE_GROUP: nsReturnValue = OS.NSAccessibilityGroupRole; break;  
			//SPLIT_BUTTON uses NSAccessibilityMenuButtonRole already
			case ACC.ROLE_CHECKMENUITEM: nsReturnValue = OS.NSAccessibilityMenuButtonRole; break;
			case ACC.ROLE_RADIOMENUITEM: nsReturnValue = OS.NSAccessibilityMenuButtonRole; break;
			//don't know the right answer for these:
			case ACC.ROLE_FOOTER:
			case ACC.ROLE_HEADER:
			case ACC.ROLE_FORM:
			case ACC.ROLE_PAGE:
			case ACC.ROLE_SECTION:
				nsReturnValue = OS.NSAccessibilityGroupRole;
				break;
			case ACC.ROLE_HEADING:
			case ACC.ROLE_PARAGRAPH:
				nsReturnValue = OS.NSAccessibilityTextAreaRole;
				break;
			case ACC.ROLE_CLOCK:
			case ACC.ROLE_DATETIME:
			case ACC.ROLE_CALENDAR:
			case ACC.ROLE_ALERT: 
			case ACC.ROLE_ANIMATION: 
			case ACC.ROLE_DOCUMENT:
			case ACC.ROLE_SPINBUTTON:
			case ACC.ROLE_STATUSBAR:
				nsReturnValue = OS.NSAccessibilityUnknownRole;
		}

		return nsReturnValue.getString();
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

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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

class SWTAccessibleDelegate extends NSObject {

	/**
	 * Accessible Key: The string constant for looking up the accessible 
	 * for a control using <code>getData(String)</code>. When an accessible
	 * is created for a control, it is stored as a property in the control 
	 * using <code>setData(String, Object)</code>.
	 */
	static final String ACCESSIBLE_KEY = "Accessible"; //$NON-NLS-1$
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};

	static Callback accessible2Args, accessible3Args, accessible4Args;
	static int /*long*/ proc2Args, proc3Args, proc4Args;

	Accessible accessible;
	int /*long*/ delegateJniRef;
	int childID;

	NSArray attributeNames = null;
	NSArray parameterizedAttributeNames = null;
	NSArray actionNames = null;

	static {
		Class clazz = SWTAccessibleDelegate.class;

		accessible2Args = new Callback(clazz, "accessibleProc", 2);
		proc2Args = accessible2Args.getAddress();
		if (proc2Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		accessible3Args = new Callback(clazz, "accessibleProc", 3);
		proc3Args = accessible3Args.getAddress();
		if (proc3Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);		

		accessible4Args = new Callback(clazz, "accessibleProc", 4);
		proc4Args = accessible3Args.getAddress();
		if (proc4Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);		

		// Accessible custom controls need to implement the NSAccessibility protocol. To do that, 
		// we dynamically add the methods to the control's class that are required 
		// by NSAccessibility. Then, when external assistive technology services are used, 
		// those methods get called to provide the needed information.

		String className = "SWTAccessibleDelegate";

		// TODO: These should either move out of Display or be accessible to this class.
		byte[] types = {'*','\0'};
		int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

		int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);

		// Add the NSAccessibility overrides
		OS.class_addMethod(cls, OS.sel_accessibilityActionNames, proc2Args, "@:");
		OS.class_addMethod(cls, OS.sel_accessibilityAttributeNames, proc2Args, "@:");
		OS.class_addMethod(cls, OS.sel_accessibilityParameterizedAttributeNames, proc2Args, "@:");
		OS.class_addMethod(cls, OS.sel_accessibilityIsIgnored, proc2Args, "@:");
		OS.class_addMethod(cls, OS.sel_accessibilityFocusedUIElement, proc2Args, "@:");

		OS.class_addMethod(cls, OS.sel_accessibilityAttributeValue_, proc3Args, "@:@");
		OS.class_addMethod(cls, OS.sel_accessibilityHitTest_, proc3Args, "@:{NSPoint}");
		OS.class_addMethod(cls, OS.sel_accessibilityIsAttributeSettable_, proc3Args, "@:@");
		OS.class_addMethod(cls, OS.sel_accessibilityActionDescription_, proc3Args, "@:@");
		OS.class_addMethod(cls, OS.sel_accessibilityPerformAction_, proc3Args, "@:@");

		OS.class_addMethod(cls, OS.sel_accessibilityAttributeValue_forParameter_, proc4Args, "@:@@");
		OS.class_addMethod(cls, OS.sel_accessibilitySetValue_forAttribute_, proc4Args, "@:@@");

		OS.objc_registerClassPair(cls);
	}


	public SWTAccessibleDelegate(Accessible accessible, int childID) {
		super(0);
		this.accessible = accessible;
		this.childID = childID;
		alloc().init();
		delegateJniRef = OS.NewGlobalRef(this);
		if (delegateJniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.object_setInstanceVariable(this.id, SWT_OBJECT, delegateJniRef);
	}

	NSArray accessibilityActionNames() {
		
		if (actionNames != null)
			return retainedAutoreleased(actionNames);
		
		actionNames = accessible.internal_accessibilityActionNames(childID);
		actionNames.retain();
		return retainedAutoreleased(actionNames);
	}

	NSArray accessibilityAttributeNames() {
		
		if (attributeNames != null)
			return retainedAutoreleased(attributeNames);
		
		attributeNames = accessible.internal_accessibilityAttributeNames(childID);
		attributeNames.retain();
		return retainedAutoreleased(attributeNames);
	}

	id accessibilityAttributeValue(NSString attribute) {
		return accessible.internal_accessibilityAttributeValue(attribute, childID);
	}
	
	// parameterized attribute methods
	NSArray accessibilityParameterizedAttributeNames() {
		
		if (parameterizedAttributeNames != null)
			return retainedAutoreleased(parameterizedAttributeNames);
		
		parameterizedAttributeNames = accessible.internal_accessibilityParameterizedAttributeNames(childID);
		parameterizedAttributeNames.retain();
		return retainedAutoreleased(parameterizedAttributeNames);
	}
	
	id accessibilityAttributeValue_forParameter(NSString attribute, id parameter) {
		return accessible.internal_accessibilityAttributeValue_forParameter(attribute, parameter, childID);
	}

	// Return YES if the UIElement doesn't show up to the outside world - i.e. its parent should return the UIElement's children as its own - cutting the UIElement out. E.g. NSControls are ignored when they are single-celled.
	boolean accessibilityIsIgnored() {
		return accessible.internal_accessibilityIsIgnored(childID);
	}

	boolean accessibilityIsAttributeSettable(NSString attribute) {
		return accessible.internal_accessibilityIsAttributeSettable(attribute, childID);
	}

	// Returns the deepest descendant of the UIElement hierarchy that contains the point. You can assume the point has already been determined to lie within the receiver. Override this method to do deeper hit testing within a UIElement - e.g. a NSMatrix would test its cells. The point is bottom-left relative screen coordinates.
	id accessibilityHitTest(NSPoint point) {
		return accessible.internal_accessibilityHitTest(point, childID);
	}

	// Returns the UI Element that has the focus. You can assume that the search for the focus has already been narrowed down to the reciever. Override this method to do a deeper search with a UIElement - e.g. a NSMatrix would determine if one of its cells has the focus.
	id accessibilityFocusedUIElement() {
		return accessible.internal_accessibilityFocusedUIElement(childID);
	}

	void accessibilityPerformAction(NSString action) {
		accessible.internal_accessibilityPerformAction(action, childID);
	}
	
	id accessibilityActionDescription(NSString action) {
		return accessible.internal_accessibilityActionDescription(action, childID);
	}
	
	void accessibilitySetValue_forAttribute(id value, NSString attribute) {
		accessible.internal_accessibilitySetValue_forAttribute(value, attribute, childID);
	}
	
	static NSArray retainedAutoreleased(NSArray inObject) {
		id temp = inObject.retain();
		id temp2 = new NSObject(temp.id).autorelease();
		return new NSArray(temp2.id);
	}
	
	static int /*long*/ accessibleProc(int /*long*/ id, int /*long*/ sel) {
		SWTAccessibleDelegate swtAcc = getAccessibleDelegate(id);
		if (swtAcc == null) return 0;
		
		if (sel == OS.sel_accessibilityAttributeNames) {
			NSArray retObject = swtAcc.accessibilityAttributeNames();
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityActionNames) {
			NSArray retObject = swtAcc.accessibilityActionNames();
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityParameterizedAttributeNames) {
			NSArray retObject = swtAcc.accessibilityParameterizedAttributeNames();
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityIsIgnored) {
			boolean retVal = swtAcc.accessibilityIsIgnored();
			return (retVal ? 1 : 0);
		} else if (sel == OS.sel_accessibilityFocusedUIElement) {
			id retObject = swtAcc.accessibilityFocusedUIElement();
			return (retObject == null ? 0 : retObject.id);
		}

		return 0;
	}

	static int /*long*/ accessibleProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
		SWTAccessibleDelegate swtAcc = getAccessibleDelegate(id);
		if (swtAcc == null) return 0;
		
		if (sel == OS.sel_accessibilityAttributeValue_) {
			NSString attribute = new NSString(arg0);
			id retObject = swtAcc.accessibilityAttributeValue(attribute);
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityHitTest_) {
			NSPoint point= new NSPoint();
			OS.memmove(point, arg0, NSPoint.sizeof);
			id retObject = swtAcc.accessibilityHitTest(point);
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityIsAttributeSettable_) {
			NSString attribute = new NSString(arg0);
			return (swtAcc.accessibilityIsAttributeSettable(attribute) ? 1 : 0);
		} else if (sel == OS.sel_accessibilityActionDescription_) {
			NSString action = new NSString(arg0);
			id retObject = swtAcc.accessibilityActionDescription(action);
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilityPerformAction_) {
			NSString action = new NSString(arg0);
			swtAcc.accessibilityPerformAction(action);
		}

		return 0;
	}

	static int /*long*/ accessibleProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
		SWTAccessibleDelegate swtAcc = getAccessibleDelegate(id);
		if (swtAcc == null) return 0;
		
		if (sel == OS.sel_accessibilityAttributeValue_forParameter_) {
			NSString attribute = new NSString(arg0);
			id parameter = new id(arg1);
			id retObject = swtAcc.accessibilityAttributeValue_forParameter(attribute, parameter);
			return (retObject == null ? 0 : retObject.id);
		} else if (sel == OS.sel_accessibilitySetValue_forAttribute_) {
			id value = new id(arg0);
			NSString attribute = new NSString(arg1);
			swtAcc.accessibilitySetValue_forAttribute(value, attribute);
		}

		return 0;
	}

	static SWTAccessibleDelegate getAccessibleDelegate(int /*long*/ id) {
		if (id == 0) return null;
		int /*long*/ [] jniRef = new int /*long*/ [1];
		OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
		if (jniRef[0] == 0) return null;
		return (SWTAccessibleDelegate)OS.JNIGetObject(jniRef[0]);
	}

	public void internal_dispose_SWTAccessibleDelegate() {
		if (actionNames != null) actionNames.release();
		actionNames = null;
		if (attributeNames != null) attributeNames.release();
		attributeNames = null;
		if (parameterizedAttributeNames != null) parameterizedAttributeNames.release();
		parameterizedAttributeNames = null;	
		
		if (delegateJniRef != 0) OS.DeleteGlobalRef(delegateJniRef);
		delegateJniRef = 0;
		OS.object_setInstanceVariable(this.id, SWT_OBJECT, 0);
	}

}
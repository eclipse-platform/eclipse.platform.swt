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


import java.lang.reflect.Method;
import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

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
	int refCount = 0, enumIndex = 0;
	COMObject objIAccessible, objIEnumVARIANT;
	IAccessible iaccessible;
	Vector accessibleListeners = new Vector();
	Vector accessibleControlListeners = new Vector();
	Vector textListeners = new Vector ();
	Object[] variants;
	Control control;
	static String ALTERNATE_ACCESSIBLE_CLASS_NAME = "org.eclipse.swt.accessibility2.Accessible2";

	/**
	 * @since 3.5
	 */
	protected Accessible() {
	}

	Accessible(Control control) {
		this.control = control;
		int /*long*/[] ppvObject = new int /*long*/[1];
		/* CreateStdAccessibleObject([in] hwnd, [in] idObject, [in] riidInterface, [out] ppvObject).
		 * AddRef has already been called on ppvObject by the callee and must be released by the caller.
		 */
		int result = (int)/*64*/COM.CreateStdAccessibleObject(control.handle, COM.OBJID_CLIENT, COM.IIDIAccessible, ppvObject);
		/* The object needs to be checked, because if the CreateStdAccessibleObject()
		 * symbol is not found, the return value is S_OK.
		 */
		if (ppvObject[0] == 0) return;
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		iaccessible = new IAccessible(ppvObject[0]);

		objIAccessible = new COMObject(new int[] {2,0,0,1,3,5,8,1,1,2,2,2,2,2,2,2,3,2,1,1,2,2,5,3,3,1,2,2}) {
			public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
			public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
			public int /*long*/ method2(int /*long*/[] args) {return Release();}
			// method3 GetTypeInfoCount - not implemented
			// method4 GetTypeInfo - not implemented
			// method5 GetIDsOfNames - not implemented
			// method6 Invoke - not implemented
			public int /*long*/ method7(int /*long*/[] args) {return get_accParent(args[0]);}
			public int /*long*/ method8(int /*long*/[] args) {return get_accChildCount(args[0]);}
			public int /*long*/ method9(int /*long*/[] args) {return get_accChild(args[0], args[1]);}
			public int /*long*/ method10(int /*long*/[] args) {return get_accName(args[0], args[1]);}
			public int /*long*/ method11(int /*long*/[] args) {return get_accValue(args[0], args[1]);}
			public int /*long*/ method12(int /*long*/[] args) {return get_accDescription(args[0], args[1]);}
			public int /*long*/ method13(int /*long*/[] args) {return get_accRole(args[0], args[1]);}
			public int /*long*/ method14(int /*long*/[] args) {return get_accState(args[0], args[1]);}
			public int /*long*/ method15(int /*long*/[] args) {return get_accHelp(args[0], args[1]);}
			public int /*long*/ method16(int /*long*/[] args) {return get_accHelpTopic(args[0], args[1], args[2]);}
			public int /*long*/ method17(int /*long*/[] args) {return get_accKeyboardShortcut(args[0], args[1]);}
			public int /*long*/ method18(int /*long*/[] args) {return get_accFocus(args[0]);}
			public int /*long*/ method19(int /*long*/[] args) {return get_accSelection(args[0]);}
			public int /*long*/ method20(int /*long*/[] args) {return get_accDefaultAction(args[0], args[1]);}
			public int /*long*/ method21(int /*long*/[] args) {return accSelect((int)/*64*/args[0], args[1]);}
			public int /*long*/ method22(int /*long*/[] args) {return accLocation(args[0], args[1], args[2], args[3], args[4]);}
			public int /*long*/ method23(int /*long*/[] args) {return accNavigate((int)/*64*/args[0], args[1], args[2]);}
			public int /*long*/ method24(int /*long*/[] args) {return accHitTest((int)/*64*/args[0], (int)/*64*/args[1], args[2]);}
			public int /*long*/ method25(int /*long*/[] args) {return accDoDefaultAction(args[0]);}
			public int /*long*/ method26(int /*long*/[] args) {return put_accName(args[0], args[1]);}
			public int /*long*/ method27(int /*long*/[] args) {return put_accValue(args[0], args[1]);}
		};

		/* If the callback takes a struct parameter (for example, a variant),
		 * then create a custom callback that dereferences the struct and
		 * passes a pointer to the original callback.
		 */
		int /*long*/ ppVtable = objIAccessible.ppVtable;
		int /*long*/[] pVtable = new int /*long*/[1];
		COM.MoveMemory(pVtable, ppVtable, OS.PTR_SIZEOF);
		int /*long*/[] funcs = new int /*long*/[28];
		COM.MoveMemory(funcs, pVtable[0], OS.PTR_SIZEOF * funcs.length);
		funcs[9] = COM.get_accChild_CALLBACK(funcs[9]);
		funcs[10] = COM.get_accName_CALLBACK(funcs[10]);
		funcs[11] = COM.get_accValue_CALLBACK(funcs[11]);
		funcs[12] = COM.get_accDescription_CALLBACK(funcs[12]);
		funcs[13] = COM.get_accRole_CALLBACK(funcs[13]);
		funcs[14] = COM.get_accState_CALLBACK(funcs[14]);
		funcs[15] = COM.get_accHelp_CALLBACK(funcs[15]);
		funcs[16] = COM.get_accHelpTopic_CALLBACK(funcs[16]);
		funcs[17] = COM.get_accKeyboardShortcut_CALLBACK(funcs[17]);
		funcs[20] = COM.get_accDefaultAction_CALLBACK(funcs[20]);
		funcs[21] = COM.accSelect_CALLBACK(funcs[21]);
		funcs[22] = COM.accLocation_CALLBACK(funcs[22]);
		funcs[23] = COM.accNavigate_CALLBACK(funcs[23]);
		funcs[25] = COM.accDoDefaultAction_CALLBACK(funcs[25]);
		funcs[26] = COM.put_accName_CALLBACK(funcs[26]);
		funcs[27] = COM.put_accValue_CALLBACK(funcs[27]);
		COM.MoveMemory(pVtable[0], funcs, OS.PTR_SIZEOF * funcs.length);

		objIEnumVARIANT = new COMObject(new int[] {2,0,0,3,1,0,1}) {
			public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
			public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
			public int /*long*/ method2(int /*long*/[] args) {return Release();}
			public int /*long*/ method3(int /*long*/[] args) {return Next((int)/*64*/args[0], args[1], args[2]);}
			public int /*long*/ method4(int /*long*/[] args) {return Skip((int)/*64*/args[0]);}
			public int /*long*/ method5(int /*long*/[] args) {return Reset();}
			public int /*long*/ method6(int /*long*/[] args) {return Clone(args[0]);}
		};
		AddRef();
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
		if (ALTERNATE_ACCESSIBLE_CLASS_NAME != null) {
			try {
				Class clazz = Class.forName(ALTERNATE_ACCESSIBLE_CLASS_NAME);
				Method method = clazz.getDeclaredMethod ("internal_new_Accessible", new Class [] {Control.class});
				Object value = method.invoke(clazz, new Object [] {control});
				return (Accessible)value;
			} catch (Throwable e) {
				ALTERNATE_ACCESSIBLE_CLASS_NAME = null;
			}
		}
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
		if (iaccessible != null) {
			iaccessible.Release();
		}
		iaccessible = null;
		Release();
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
	public int /*long*/ internal_WM_GETOBJECT (int /*long*/ wParam, int /*long*/ lParam) {
		if (objIAccessible == null) return 0;
		if ((int)/*64*/lParam == COM.OBJID_CLIENT) {
			/* LresultFromObject([in] riid, [in] wParam, [in] pAcc)
			 * The argument pAcc is owned by the caller so reference count does not
			 * need to be incremented.
			 */
			return COM.LresultFromObject(COM.IIDIAccessible, wParam, objIAccessible.getAddress());
		}
		return 0;
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
		COM.NotifyWinEvent (COM.EVENT_OBJECT_SELECTIONWITHIN, control.handle, COM.OBJID_CLIENT, COM.CHILDID_SELF);
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
		COM.NotifyWinEvent (COM.EVENT_OBJECT_FOCUS, control.handle, COM.OBJID_CLIENT, childIDToOs(childID));
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
		COM.NotifyWinEvent (COM.EVENT_OBJECT_LOCATIONCHANGE, control.handle, COM.OBJID_CARET, COM.CHILDID_SELF);
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
		COM.NotifyWinEvent (COM.EVENT_OBJECT_VALUECHANGE, control.handle, COM.OBJID_CLIENT, COM.CHILDID_SELF);
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
		// not an MSAA event
	}
	
	/* QueryInterface([in] iid, [out] ppvObject)
	 * Ownership of ppvObject transfers from callee to caller so reference count on ppvObject 
	 * must be incremented before returning.  Caller is responsible for releasing ppvObject.
	 */
	int QueryInterface(int /*long*/ iid, int /*long*/ ppvObject) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		GUID guid = new GUID();
		COM.MoveMemory(guid, iid, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { objIAccessible.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIDispatch)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { objIAccessible.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessible)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { objIAccessible.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIEnumVARIANT)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { objIEnumVARIANT.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			enumIndex = 0;
			return COM.S_OK;
		}

		int /*long*/[] ppv = new int /*long*/[1];
		int result = iaccessible.QueryInterface(guid, ppv);
		COM.MoveMemory(ppvObject, ppv, OS.PTR_SIZEOF);
		return result;
	}

	int AddRef() {
		refCount++;
		return refCount;
	}

	int Release() {
		refCount--;

		if (refCount == 0) {
			if (objIAccessible != null)
				objIAccessible.dispose();
			objIAccessible = null;
						
			if (objIEnumVARIANT != null)
				objIEnumVARIANT.dispose();
			objIEnumVARIANT = null;
		}
		return refCount;
	}

	int accDoDefaultAction(int /*long*/ variant) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// Currently, we don't let the application override this. Forward to the proxy.
		int code = iaccessible.accDoDefaultAction(variant);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}

	int accHitTest(int xLeft, int yTop, int /*long*/ pvarChild) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.accHitTest(xLeft, yTop, pvarChild);
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_NONE;
		event.x = xLeft;
		event.y = yTop;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChildAtPoint(event);
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			return iaccessible.accHitTest(xLeft, yTop, pvarChild);
		}
		//TODO - use VARIANT structure
		COM.MoveMemory(pvarChild, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChild + 8, new int[] { childIDToOs(childID) }, 4);
		return COM.S_OK;
	}
	
	int accLocation(int /*long*/ pxLeft, int /*long*/ pyTop, int /*long*/ pcxWidth, int /*long*/ pcyHeight, int /*long*/ variant) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;

		/* Get the default location from the OS. */
		int osLeft = 0, osTop = 0, osWidth = 0, osHeight = 0;
		int code = iaccessible.accLocation(pxLeft, pyTop, pcxWidth, pcyHeight, variant);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int[] pLeft = new int[1], pTop = new int[1], pWidth = new int[1], pHeight = new int[1];
			COM.MoveMemory(pLeft, pxLeft, 4);
			COM.MoveMemory(pTop, pyTop, 4);
			COM.MoveMemory(pWidth, pcxWidth, 4);
			COM.MoveMemory(pHeight, pcyHeight, 4);
			osLeft = pLeft[0]; osTop = pTop[0]; osWidth = pWidth[0]; osHeight = pHeight[0];
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.x = osLeft;
		event.y = osTop;
		event.width = osWidth;
		event.height = osHeight;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getLocation(event);
		}
		OS.MoveMemory(pxLeft, new int[] { event.x }, 4);
		OS.MoveMemory(pyTop, new int[] { event.y }, 4);
		OS.MoveMemory(pcxWidth, new int[] { event.width }, 4);
		OS.MoveMemory(pcyHeight, new int[] { event.height }, 4);
		return COM.S_OK;
	}
	
	int accNavigate(int navDir, int /*long*/ variant, int /*long*/ pvarEndUpAt) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// Currently, we don't let the application override this. Forward to the proxy.
		int code = iaccessible.accNavigate(navDir, variant, pvarEndUpAt);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}
	
	int accSelect(int flagsSelect, int /*long*/ variant) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// Currently, we don't let the application override this. Forward to the proxy.
		int code = iaccessible.accSelect(flagsSelect, variant);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}

	/* get_accChild([in] varChild, [out] ppdispChild)
	 * Ownership of ppdispChild transfers from callee to caller so reference count on ppdispChild 
	 * must be incremented before returning.  The caller is responsible for releasing ppdispChild.
	 */
	int get_accChild(int /*long*/ variant, int /*long*/ ppdispChild) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		if (accessibleControlListeners.size() == 0) {
			int code = iaccessible.get_accChild(variant, ppdispChild);
			if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
			return code;
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChild(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			COM.MoveMemory(ppdispChild, new int /*long*/[] { accessible.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		return COM.S_FALSE;
	}
	
	int get_accChildCount(int /*long*/ pcountChildren) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;

		/* Get the default child count from the OS. */
		int osChildCount = 0;
		int code = iaccessible.get_accChildCount(pcountChildren);
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int[] pChildCount = new int[1];
			COM.MoveMemory(pChildCount, pcountChildren, 4);
			osChildCount = pChildCount[0];
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_SELF;
		event.detail = osChildCount;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChildCount(event);
		}

		COM.MoveMemory(pcountChildren, new int[] { event.detail }, 4);
		return COM.S_OK;
	}
	
	int get_accDefaultAction(int /*long*/ variant, int /*long*/ pszDefaultAction) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		
		/* Get the default defaultAction from the OS. */
		String osDefaultAction = null;
		int code = iaccessible.get_accDefaultAction(variant, pszDefaultAction);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pDefaultAction = new int /*long*/[1];
			COM.MoveMemory(pDefaultAction, pszDefaultAction, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pDefaultAction[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pDefaultAction[0], size);
				osDefaultAction = new String(buffer);
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osDefaultAction;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getDefaultAction(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDefaultAction, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	int get_accDescription(int /*long*/ variant, int /*long*/ pszDescription) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		
		/* Get the default description from the OS. */
		String osDescription = null;
		int code = iaccessible.get_accDescription(variant, pszDescription);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		// TEMPORARY CODE - process tree even if there are no apps listening
		if (accessibleListeners.size() == 0 && !(control instanceof Tree)) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pDescription = new int /*long*/[1];
			COM.MoveMemory(pDescription, pszDescription, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pDescription[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pDescription[0], size);
				osDescription = new String(buffer);
			}
		}
		
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osDescription;
		
		// TEMPORARY CODE
		/* Currently our tree columns are emulated using custom draw,
		 * so we need to create the description using the tree column
		 * header text and tree item text. */
		if (v.lVal != COM.CHILDID_SELF) {
			if (control instanceof Tree) {
				Tree tree = (Tree) control;
				int columnCount = tree.getColumnCount ();
				if (columnCount > 1) {
					int /*long*/ hwnd = control.handle, hItem = 0;
					if (OS.COMCTL32_MAJOR >= 6) {
						hItem = OS.SendMessage (hwnd, OS.TVM_MAPACCIDTOHTREEITEM, v.lVal, 0);
					} else {
						hItem = v.lVal;
					}
					Widget widget = tree.getDisplay ().findWidget (hwnd, hItem);
					event.result = "";
					if (widget != null && widget instanceof TreeItem) {
						TreeItem item = (TreeItem) widget;
						for (int i = 1; i < columnCount; i++) {
							event.result += tree.getColumn(i).getText() + ": " + item.getText(i);
							if (i + 1 < columnCount) event.result += ", ";
						}
					}
				}
			}
		}
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getDescription(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDescription, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	/* get_accFocus([out] int pvarChild)
	 * Ownership of pvarChild transfers from callee to caller so reference count on pvarChild 
	 * must be incremented before returning.  The caller is responsible for releasing pvarChild.
	 */
	int get_accFocus(int /*long*/ pvarChild) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;

		/* Get the default focus child from the OS. */
		int osChild = ACC.CHILDID_NONE;
		int code = iaccessible.get_accFocus(pvarChild);
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			//TODO - use VARIANT structure
			short[] pvt = new short[1];
			COM.MoveMemory(pvt, pvarChild, 2);
			if (pvt[0] == COM.VT_I4) {
				int[] pChild = new int[1];
				COM.MoveMemory(pChild, pvarChild + 8, 4);
				osChild = osToChildID(pChild[0]);
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osChild;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			COM.MoveMemory(pvarChild, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChild + 8, new int /*long*/[] { accessible.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarChild, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_SELF) {
			AddRef();
			COM.MoveMemory(pvarChild, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChild + 8, new int /*long*/[] { objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		COM.MoveMemory(pvarChild, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChild + 8, new int[] { childIDToOs(childID) }, 4);
		return COM.S_OK;
	}
	
	int get_accHelp(int /*long*/ variant, int /*long*/ pszHelp) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		
		/* Get the default help string from the OS. */
		String osHelp = null;
		int code = iaccessible.get_accHelp(variant, pszHelp);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pHelp = new int /*long*/[1];
			COM.MoveMemory(pHelp, pszHelp, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pHelp[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pHelp[0], size);
				osHelp = new String(buffer);
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osHelp;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getHelp(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszHelp, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	int get_accHelpTopic(int /*long*/ pszHelpFile, int /*long*/ variant, int /*long*/ pidTopic) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// Currently, we don't let the application override this. Forward to the proxy.
		int code = iaccessible.get_accHelpTopic(pszHelpFile, variant, pidTopic);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}

	int get_accKeyboardShortcut(int /*long*/ variant, int /*long*/ pszKeyboardShortcut) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		
		/* Get the default keyboard shortcut from the OS. */
		String osKeyboardShortcut = null;
		int code = iaccessible.get_accKeyboardShortcut(variant, pszKeyboardShortcut);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pKeyboardShortcut = new int /*long*/[1];
			COM.MoveMemory(pKeyboardShortcut, pszKeyboardShortcut, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pKeyboardShortcut[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pKeyboardShortcut[0], size);
				osKeyboardShortcut = new String(buffer);
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osKeyboardShortcut;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getKeyboardShortcut(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszKeyboardShortcut, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	int get_accName(int /*long*/ variant, int /*long*/ pszName) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;

		/* Get the default name from the OS. */
		String osName = null;
		int code = iaccessible.get_accName(variant, pszName);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pName = new int /*long*/[1];
			COM.MoveMemory(pName, pszName, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pName[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pName[0], size);
				osName = new String(buffer);
			}
		}

		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osName;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getName(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszName, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	/* get_accParent([out] ppdispParent)
	 * Ownership of ppdispParent transfers from callee to caller so reference count on ppdispParent 
	 * must be incremented before returning.  The caller is responsible for releasing ppdispParent.
	 */
	int get_accParent(int /*long*/ ppdispParent) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// Currently, we don't let the application override this. Forward to the proxy.
		return iaccessible.get_accParent(ppdispParent);
	}
	
	int get_accRole(int /*long*/ variant, int /*long*/ pvarRole) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;

		/* Get the default role from the OS. */
		int osRole = COM.ROLE_SYSTEM_CLIENT;
		int code = iaccessible.get_accRole(variant, pvarRole);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		// TEMPORARY CODE - process tree and table even if there are no apps listening
		if (accessibleControlListeners.size() == 0 && !(control instanceof Tree || control instanceof Table)) return code;
		if (code == COM.S_OK) {
			//TODO - use VARIANT structure
			short[] pvt = new short[1];
			COM.MoveMemory(pvt, pvarRole, 2);
			if (pvt[0] == COM.VT_I4) {
				int[] pRole = new int[1];
				COM.MoveMemory(pRole, pvarRole + 8, 4);
				osRole = pRole[0];
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.detail = osToRole(osRole);
		// TEMPORARY CODE
		/* Currently our checkbox table and tree are emulated using state mask
		 * images, so we need to specify 'checkbox' role for the items. */
		if (v.lVal != COM.CHILDID_SELF) {
			if (control instanceof Tree || control instanceof Table) {
				if ((control.getStyle() & SWT.CHECK) != 0) event.detail = ACC.ROLE_CHECKBUTTON;
			}
		}
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		int role = roleToOs(event.detail);
		COM.MoveMemory(pvarRole, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarRole + 8, new int[] { role }, 4);
		return COM.S_OK;
	}
	
	/* get_accSelection([out] pvarChildren)
	 * Ownership of pvarChildren transfers from callee to caller so reference count on pvarChildren 
	 * must be incremented before returning.  The caller is responsible for releasing pvarChildren.
	 */
	int get_accSelection(int /*long*/ pvarChildren) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;

		/* Get the default selection from the OS. */
		int osChild = ACC.CHILDID_NONE;
		int code = iaccessible.get_accSelection(pvarChildren);
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			//TODO - use VARIANT structure
			short[] pvt = new short[1];
			COM.MoveMemory(pvt, pvarChildren, 2);
			if (pvt[0] == COM.VT_I4) {
				int[] pChild = new int[1];
				COM.MoveMemory(pChild, pvarChildren + 8, 4);
				osChild = osToChildID(pChild[0]);
			} else if (pvt[0] == COM.VT_UNKNOWN) {
				osChild = ACC.CHILDID_MULTIPLE;
				/* Should get IEnumVARIANT from punkVal field, and enumerate children... */
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osChild;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getSelection(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			accessible.AddRef();
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChildren + 8, new int /*long*/[] { accessible.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_MULTIPLE) {
			AddRef();
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_UNKNOWN }, 2);
			COM.MoveMemory(pvarChildren + 8, new int /*long*/[] { objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		if (childID == ACC.CHILDID_SELF) {
			AddRef();
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChildren + 8, new int /*long*/[] { objIAccessible.getAddress() }, OS.PTR_SIZEOF);
			return COM.S_OK;
		}
		COM.MoveMemory(pvarChildren, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChildren + 8, new int[] { childIDToOs(childID) }, 4);
		return COM.S_OK;
	}
	
	int get_accState(int /*long*/ variant, int /*long*/ pvarState) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;

		/* Get the default state from the OS. */
		int osState = 0;
		int code = iaccessible.get_accState(variant, pvarState);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		// TEMPORARY CODE - process tree and table even if there are no apps listening
		if (accessibleControlListeners.size() == 0 && !(control instanceof Tree || control instanceof Table)) return code;
		if (code == COM.S_OK) {
			//TODO - use VARIANT structure
			short[] pvt = new short[1];
			COM.MoveMemory(pvt, pvarState, 2);
			if (pvt[0] == COM.VT_I4) {
				int[] pState = new int[1];
				COM.MoveMemory(pState, pvarState + 8, 4);
				osState = pState[0];
			}
		}
		boolean grayed = false;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.detail = osToState(osState);
		// TEMPORARY CODE
		/* Currently our checkbox table and tree are emulated using state mask
		 * images, so we need to determine if the item state is 'checked'. */
		if (v.lVal != COM.CHILDID_SELF) {
			if (control instanceof Tree && (control.getStyle() & SWT.CHECK) != 0) {
				int /*long*/ hwnd = control.handle;
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
				tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
				if (OS.COMCTL32_MAJOR >= 6) {
					tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_MAPACCIDTOHTREEITEM, v.lVal, 0);
				} else {
					tvItem.hItem = v.lVal;
				}
				int /*long*/ result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
				boolean checked = (result != 0) && (((tvItem.state >> 12) & 1) == 0);
				if (checked) event.detail |= ACC.STATE_CHECKED;
				grayed = tvItem.state >> 12 > 2;
			} else if (control instanceof Table && (control.getStyle() & SWT.CHECK) != 0) {
				Table table = (Table) control;
				int index = event.childID;
				if (0 <= index && index < table.getItemCount()) {
					TableItem item = table.getItem(index);
					if (item.getChecked()) event.detail |= ACC.STATE_CHECKED;
					if (item.getGrayed()) grayed = true;
				}
			}
		}
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getState(event);
		}
		int state = stateToOs(event.detail);
		if ((state & ACC.STATE_CHECKED) != 0 && grayed) {
			state &= ~ COM.STATE_SYSTEM_CHECKED;
			state |= COM.STATE_SYSTEM_MIXED;
		}
		COM.MoveMemory(pvarState, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarState + 8, new int[] { state }, 4);
		return COM.S_OK;
	}
	
	int get_accValue(int /*long*/ variant, int /*long*/ pszValue) {
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		VARIANT v = new VARIANT();
		COM.MoveMemory(v, variant, VARIANT.sizeof);
		if ((v.vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		
		/* Get the default value string from the OS. */
		String osValue = null;
		int code = iaccessible.get_accValue(variant, pszValue);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		if (accessibleControlListeners.size() == 0) return code;
		if (code == COM.S_OK) {
			int /*long*/[] pValue = new int /*long*/[1];
			COM.MoveMemory(pValue, pszValue, OS.PTR_SIZEOF);
			int size = COM.SysStringByteLen(pValue[0]);
			if (size > 0) {
				char[] buffer = new char[(size + 1) /2];
				COM.MoveMemory(buffer, pValue[0], size);
				osValue = new String(buffer);
			}
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = osToChildID(v.lVal);
		event.result = osValue;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		if (event.result == null) return code;
		char[] data = (event.result + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszValue, new int /*long*/[] { ptr }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}
	
	int put_accName(int /*long*/ variant, int /*long*/ szName) {
		// MSAA: this method is no longer supported
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// We don't implement this. Forward to the proxy.
		int code = iaccessible.put_accName(variant, szName);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}
	
	int put_accValue(int /*long*/ variant, int /*long*/ szValue) {
		// MSAA: this method is typically only used for edit controls
		if (iaccessible == null) return COM.CO_E_OBJNOTCONNECTED;
		// We don't implement this. Forward to the proxy.
		int code = iaccessible.put_accValue(variant, szValue);
		if (code == COM.E_INVALIDARG) code = COM.S_FALSE; // proxy doesn't know about app childID
		return code;
	}

	/* IEnumVARIANT methods: Next, Skip, Reset, Clone */
	/* Retrieve the next celt items in the enumeration sequence. 
	 * If there are fewer than the requested number of elements left
	 * in the sequence, retrieve the remaining elements.
	 * The number of elements actually retrieved is returned in pceltFetched 
	 * (unless the caller passed in NULL for that parameter).
	 */
	 
	 /* Next([in] celt, [out] rgvar, [in, out] pceltFetched)
	 * Ownership of rgvar transfers from callee to caller so reference count on rgvar 
	 * must be incremented before returning.  The caller is responsible for releasing rgvar.
	 */
	int Next(int celt, int /*long*/ rgvar, int /*long*/ pceltFetched) {
		/* If there are no listeners, query the proxy for
		 * its IEnumVariant, and get the Next items from it.
		 */
		if (accessibleControlListeners.size() == 0) {
			int /*long*/[] ppvObject = new int /*long*/[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			int[] celtFetched = new int[1];
			code = ienumvariant.Next(celt, rgvar, celtFetched);
			ienumvariant.Release();
			COM.MoveMemory(pceltFetched, celtFetched, 4);
			return code;
		}

		if (rgvar == 0) return COM.E_INVALIDARG;
		if (pceltFetched == 0 && celt != 1) return COM.E_INVALIDARG;
		if (enumIndex == 0) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
			event.childID = ACC.CHILDID_SELF;
			for (int i = 0; i < accessibleControlListeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
				listener.getChildren(event);
			}
			variants = event.children;
		}	
		Object[] nextItems = null;
		if (variants != null && celt >= 1) {
			int endIndex = enumIndex + celt - 1;
			if (endIndex > (variants.length - 1)) endIndex = variants.length - 1;
			if (enumIndex <= endIndex) {
				nextItems = new Object[endIndex - enumIndex + 1];
				for (int i = 0; i < nextItems.length; i++) {
					Object child = variants[enumIndex];
					if (child instanceof Integer) {
						nextItems[i] = new Integer(childIDToOs(((Integer)child).intValue()));
					} else {
						nextItems[i] = child;
					}
					enumIndex++;
				}
			}
		}
		if (nextItems != null) {
			for (int i = 0; i < nextItems.length; i++) {
				Object nextItem = nextItems[i];
				if (nextItem instanceof Integer) {
					int item = ((Integer) nextItem).intValue();
					//TODO - use VARIANT struct
					COM.MoveMemory(rgvar + i * VARIANT.sizeof, new short[] { COM.VT_I4 }, 2);
					COM.MoveMemory(rgvar + i * VARIANT.sizeof + 8, new int[] { item }, 4);
				} else {
					Accessible accessible = (Accessible) nextItem;
					accessible.AddRef();
					COM.MoveMemory(rgvar + i * VARIANT.sizeof, new short[] { COM.VT_DISPATCH }, 2);
					COM.MoveMemory(rgvar + i * VARIANT.sizeof + 8, new int /*long*/[] { accessible.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
				}
			}
			if (pceltFetched != 0)
				COM.MoveMemory(pceltFetched, new int[] {nextItems.length}, 4);
			if (nextItems.length == celt) return COM.S_OK;
		} else {
			if (pceltFetched != 0)
				COM.MoveMemory(pceltFetched, new int[] {0}, 4);
		}
		return COM.S_FALSE;
	}
	
	/* Skip over the specified number of elements in the enumeration sequence. */
	int Skip(int celt) {
		/* If there are no listeners, query the proxy
		 * for its IEnumVariant, and tell it to Skip.
		 */
		if (accessibleControlListeners.size() == 0) {
			int /*long*/[] ppvObject = new int /*long*/[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			code = ienumvariant.Skip(celt);
			ienumvariant.Release();
			return code;
		}

		if (celt < 1 ) return COM.E_INVALIDARG;
		enumIndex += celt;
		if (enumIndex > (variants.length - 1)) {
			enumIndex = variants.length - 1;
			return COM.S_FALSE;
		}
		return COM.S_OK;
	}
	
	/* Reset the enumeration sequence to the beginning. */
	int Reset() {
		/* If there are no listeners, query the proxy
		 * for its IEnumVariant, and tell it to Reset.
		 */
		if (accessibleControlListeners.size() == 0) {
			int /*long*/[] ppvObject = new int /*long*/[1];
			int code = (int)/*64*/iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			code = ienumvariant.Reset();
			ienumvariant.Release();
			return code;
		}
		
		enumIndex = 0;
		return COM.S_OK;
	}

	 /* Clone([out] ppEnum)
	 * Ownership of ppEnum transfers from callee to caller so reference count on ppEnum 
	 * must be incremented before returning.  The caller is responsible for releasing ppEnum.
	 */
	int Clone(int /*long*/ ppEnum) {
		/* If there are no listeners, query the proxy for
		 * its IEnumVariant, and get the Clone from it.
		 */
		if (accessibleControlListeners.size() == 0) {
			int /*long*/[] ppvObject = new int /*long*/[1];
			int code = iaccessible.QueryInterface(COM.IIDIEnumVARIANT, ppvObject);
			if (code != COM.S_OK) return code;
			IEnumVARIANT ienumvariant = new IEnumVARIANT(ppvObject[0]);
			int /*long*/ [] pEnum = new int /*long*/ [1];
			code = ienumvariant.Clone(pEnum);
			ienumvariant.Release();
			COM.MoveMemory(ppEnum, pEnum, OS.PTR_SIZEOF);
			return code;
		}

		if (ppEnum == 0) return COM.E_INVALIDARG;
		COM.MoveMemory(ppEnum, new int /*long*/[] { objIEnumVARIANT.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	
	int childIDToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) return COM.CHILDID_SELF;
		/*
		* Feature of Windows:
		* In Windows XP, tree item ids are 1-based indices. Previous versions
		* of Windows use the tree item handle for the accessible child ID.
		* For backward compatibility, we still take a handle childID for tree
		* items on XP. All other childIDs are 1-based indices.
		*/
		if (!(control instanceof Tree)) return childID + 1;
		if (OS.COMCTL32_MAJOR < 6) return childID;
		return (int)/*64*/OS.SendMessage (control.handle, OS.TVM_MAPHTREEITEMTOACCID, childID, 0);
	}

	int osToChildID(int osChildID) {
		if (osChildID == COM.CHILDID_SELF) return ACC.CHILDID_SELF;
		/*
		* Feature of Windows:
		* In Windows XP, tree item ids are 1-based indices. Previous versions
		* of Windows use the tree item handle for the accessible child ID.
		* For backward compatibility, we still take a handle childID for tree
		* items on XP. All other childIDs are 1-based indices.
		*/
		if (!(control instanceof Tree)) return osChildID - 1;
		if (OS.COMCTL32_MAJOR < 6) return osChildID;
		return (int)/*64*/OS.SendMessage (control.handle, OS.TVM_MAPACCIDTOHTREEITEM, osChildID, 0);
	}
	
	int stateToOs(int state) {
		int osState = 0;
		if ((state & ACC.STATE_SELECTED) != 0) osState |= COM.STATE_SYSTEM_SELECTED;
		if ((state & ACC.STATE_SELECTABLE) != 0) osState |= COM.STATE_SYSTEM_SELECTABLE;
		if ((state & ACC.STATE_MULTISELECTABLE) != 0) osState |= COM.STATE_SYSTEM_MULTISELECTABLE;
		if ((state & ACC.STATE_FOCUSED) != 0) osState |= COM.STATE_SYSTEM_FOCUSED;
		if ((state & ACC.STATE_FOCUSABLE) != 0) osState |= COM.STATE_SYSTEM_FOCUSABLE;
		if ((state & ACC.STATE_PRESSED) != 0) osState |= COM.STATE_SYSTEM_PRESSED;
		if ((state & ACC.STATE_CHECKED) != 0) osState |= COM.STATE_SYSTEM_CHECKED;
		if ((state & ACC.STATE_EXPANDED) != 0) osState |= COM.STATE_SYSTEM_EXPANDED;
		if ((state & ACC.STATE_COLLAPSED) != 0) osState |= COM.STATE_SYSTEM_COLLAPSED;
		if ((state & ACC.STATE_HOTTRACKED) != 0) osState |= COM.STATE_SYSTEM_HOTTRACKED;
		if ((state & ACC.STATE_BUSY) != 0) osState |= COM.STATE_SYSTEM_BUSY;
		if ((state & ACC.STATE_READONLY) != 0) osState |= COM.STATE_SYSTEM_READONLY;
		if ((state & ACC.STATE_INVISIBLE) != 0) osState |= COM.STATE_SYSTEM_INVISIBLE;
		if ((state & ACC.STATE_OFFSCREEN) != 0) osState |= COM.STATE_SYSTEM_OFFSCREEN;
		if ((state & ACC.STATE_SIZEABLE) != 0) osState |= COM.STATE_SYSTEM_SIZEABLE;
		if ((state & ACC.STATE_LINKED) != 0) osState |= COM.STATE_SYSTEM_LINKED;
		return osState;
	}
	
	int osToState(int osState) {
		int state = ACC.STATE_NORMAL;
		if ((osState & COM.STATE_SYSTEM_SELECTED) != 0) state |= ACC.STATE_SELECTED;
		if ((osState & COM.STATE_SYSTEM_SELECTABLE) != 0) state |= ACC.STATE_SELECTABLE;
		if ((osState & COM.STATE_SYSTEM_MULTISELECTABLE) != 0) state |= ACC.STATE_MULTISELECTABLE;
		if ((osState & COM.STATE_SYSTEM_FOCUSED) != 0) state |= ACC.STATE_FOCUSED;
		if ((osState & COM.STATE_SYSTEM_FOCUSABLE) != 0) state |= ACC.STATE_FOCUSABLE;
		if ((osState & COM.STATE_SYSTEM_PRESSED) != 0) state |= ACC.STATE_PRESSED;
		if ((osState & COM.STATE_SYSTEM_CHECKED) != 0) state |= ACC.STATE_CHECKED;
		if ((osState & COM.STATE_SYSTEM_EXPANDED) != 0) state |= ACC.STATE_EXPANDED;
		if ((osState & COM.STATE_SYSTEM_COLLAPSED) != 0) state |= ACC.STATE_COLLAPSED;
		if ((osState & COM.STATE_SYSTEM_HOTTRACKED) != 0) state |= ACC.STATE_HOTTRACKED;
		if ((osState & COM.STATE_SYSTEM_BUSY) != 0) state |= ACC.STATE_BUSY;
		if ((osState & COM.STATE_SYSTEM_READONLY) != 0) state |= ACC.STATE_READONLY;
		if ((osState & COM.STATE_SYSTEM_INVISIBLE) != 0) state |= ACC.STATE_INVISIBLE;
		if ((osState & COM.STATE_SYSTEM_OFFSCREEN) != 0) state |= ACC.STATE_OFFSCREEN;
		if ((osState & COM.STATE_SYSTEM_SIZEABLE) != 0) state |= ACC.STATE_SIZEABLE;
		if ((osState & COM.STATE_SYSTEM_LINKED) != 0) state |= ACC.STATE_LINKED;
		return state;
	}

	int roleToOs(int role) {
		switch (role) {
			case ACC.ROLE_CLIENT_AREA: return COM.ROLE_SYSTEM_CLIENT;
			case ACC.ROLE_WINDOW: return COM.ROLE_SYSTEM_WINDOW;
			case ACC.ROLE_MENUBAR: return COM.ROLE_SYSTEM_MENUBAR;
			case ACC.ROLE_MENU: return COM.ROLE_SYSTEM_MENUPOPUP;
			case ACC.ROLE_MENUITEM: return COM.ROLE_SYSTEM_MENUITEM;
			case ACC.ROLE_SEPARATOR: return COM.ROLE_SYSTEM_SEPARATOR;
			case ACC.ROLE_TOOLTIP: return COM.ROLE_SYSTEM_TOOLTIP;
			case ACC.ROLE_SCROLLBAR: return COM.ROLE_SYSTEM_SCROLLBAR;
			case ACC.ROLE_DIALOG: return COM.ROLE_SYSTEM_DIALOG;
			case ACC.ROLE_LABEL: return COM.ROLE_SYSTEM_STATICTEXT;
			case ACC.ROLE_PUSHBUTTON: return COM.ROLE_SYSTEM_PUSHBUTTON;
			case ACC.ROLE_CHECKBUTTON: return COM.ROLE_SYSTEM_CHECKBUTTON;
			case ACC.ROLE_RADIOBUTTON: return COM.ROLE_SYSTEM_RADIOBUTTON;
			case ACC.ROLE_SPLITBUTTON: return COM.ROLE_SYSTEM_SPLITBUTTON;
			case ACC.ROLE_COMBOBOX: return COM.ROLE_SYSTEM_COMBOBOX;
			case ACC.ROLE_TEXT: return COM.ROLE_SYSTEM_TEXT;
			case ACC.ROLE_TOOLBAR: return COM.ROLE_SYSTEM_TOOLBAR;
			case ACC.ROLE_LIST: return COM.ROLE_SYSTEM_LIST;
			case ACC.ROLE_LISTITEM: return COM.ROLE_SYSTEM_LISTITEM;
			case ACC.ROLE_TABLE: return COM.ROLE_SYSTEM_TABLE;
			case ACC.ROLE_TABLECELL: return COM.ROLE_SYSTEM_CELL;
			case ACC.ROLE_TABLECOLUMNHEADER: return COM.ROLE_SYSTEM_COLUMNHEADER;
			case ACC.ROLE_TABLEROWHEADER: return COM.ROLE_SYSTEM_ROWHEADER;
			case ACC.ROLE_TREE: return COM.ROLE_SYSTEM_OUTLINE;
			case ACC.ROLE_TREEITEM: return COM.ROLE_SYSTEM_OUTLINEITEM;
			case ACC.ROLE_TABFOLDER: return COM.ROLE_SYSTEM_PAGETABLIST;
			case ACC.ROLE_TABITEM: return COM.ROLE_SYSTEM_PAGETAB;
			case ACC.ROLE_PROGRESSBAR: return COM.ROLE_SYSTEM_PROGRESSBAR;
			case ACC.ROLE_SLIDER: return COM.ROLE_SYSTEM_SLIDER;
			case ACC.ROLE_LINK: return COM.ROLE_SYSTEM_LINK;
		}
		return COM.ROLE_SYSTEM_CLIENT;
	}

	int osToRole(int osRole) {
		switch (osRole) {
			case COM.ROLE_SYSTEM_CLIENT: return ACC.ROLE_CLIENT_AREA;
			case COM.ROLE_SYSTEM_WINDOW: return ACC.ROLE_WINDOW;
			case COM.ROLE_SYSTEM_MENUBAR: return ACC.ROLE_MENUBAR;
			case COM.ROLE_SYSTEM_MENUPOPUP: return ACC.ROLE_MENU;
			case COM.ROLE_SYSTEM_MENUITEM: return ACC.ROLE_MENUITEM;
			case COM.ROLE_SYSTEM_SEPARATOR: return ACC.ROLE_SEPARATOR;
			case COM.ROLE_SYSTEM_TOOLTIP: return ACC.ROLE_TOOLTIP;
			case COM.ROLE_SYSTEM_SCROLLBAR: return ACC.ROLE_SCROLLBAR;
			case COM.ROLE_SYSTEM_DIALOG: return ACC.ROLE_DIALOG;
			case COM.ROLE_SYSTEM_STATICTEXT: return ACC.ROLE_LABEL;
			case COM.ROLE_SYSTEM_PUSHBUTTON: return ACC.ROLE_PUSHBUTTON;
			case COM.ROLE_SYSTEM_CHECKBUTTON: return ACC.ROLE_CHECKBUTTON;
			case COM.ROLE_SYSTEM_RADIOBUTTON: return ACC.ROLE_RADIOBUTTON;
			case COM.ROLE_SYSTEM_SPLITBUTTON: return ACC.ROLE_SPLITBUTTON;
			case COM.ROLE_SYSTEM_COMBOBOX: return ACC.ROLE_COMBOBOX;
			case COM.ROLE_SYSTEM_TEXT: return ACC.ROLE_TEXT;
			case COM.ROLE_SYSTEM_TOOLBAR: return ACC.ROLE_TOOLBAR;
			case COM.ROLE_SYSTEM_LIST: return ACC.ROLE_LIST;
			case COM.ROLE_SYSTEM_LISTITEM: return ACC.ROLE_LISTITEM;
			case COM.ROLE_SYSTEM_TABLE: return ACC.ROLE_TABLE;
			case COM.ROLE_SYSTEM_CELL: return ACC.ROLE_TABLECELL;
			case COM.ROLE_SYSTEM_COLUMNHEADER: return ACC.ROLE_TABLECOLUMNHEADER;
			case COM.ROLE_SYSTEM_ROWHEADER: return ACC.ROLE_TABLEROWHEADER;
			case COM.ROLE_SYSTEM_OUTLINE: return ACC.ROLE_TREE;
			case COM.ROLE_SYSTEM_OUTLINEITEM: return ACC.ROLE_TREEITEM;
			case COM.ROLE_SYSTEM_PAGETABLIST: return ACC.ROLE_TABFOLDER;
			case COM.ROLE_SYSTEM_PAGETAB: return ACC.ROLE_TABITEM;
			case COM.ROLE_SYSTEM_PROGRESSBAR: return ACC.ROLE_PROGRESSBAR;
			case COM.ROLE_SYSTEM_SLIDER: return ACC.ROLE_SLIDER;
			case COM.ROLE_SYSTEM_LINK: return ACC.ROLE_LINK;
		}
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

package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

/**
 * NOTE: The API in the accessibility package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The methods in AccessibleListener are more stable than those
 * in AccessibleControlListener, however please take nothing for
 * granted. The only reason this API is being released at this
 * time is so that other teams can try it out.
 * 
 * @since 2.0
 */
public class Accessible {
	int refCount = 0, enumIndex = 0;
	COMObject objIAccessible, objIEnumVARIANT;
	IAccessible iaccessible;
	Vector accessibleListeners = new Vector(), accessibleControlListeners = new Vector();
	Object[] variants;

	Accessible(Control control) {
		int[] ppvObject = new int[1];
		int result = COM.CreateStdAccessibleObject(control.handle, COM.OBJID_CLIENT, COM.IIDIAccessible, ppvObject);
		if (result == COM.E_NOTIMPL) return;
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		iaccessible = new IAccessible(ppvObject[0]);
		iaccessible.AddRef();
		
		objIAccessible = new COMObject(new int[] {2,0,0,1,3,5,8,1,1,5,5,5,5,5,5,5,6,5,1,1,5,5,8,6,3,4,5,5}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			// method3 GetTypeInfoCount - not implemented
			// method4 GetTypeInfo - not implemented
			// method5 GetIDsOfNames - not implemented
			// method6 Invoke - not implemented
			public int method7(int[] args) {return get_accParent(args[0]);}
			public int method8(int[] args) {return get_accChildCount(args[0]);}
			public int method9(int[] args) {return get_accChild(args[0], args[1], args[2], args[3], args[4]);}
			public int method10(int[] args) {return get_accName(args[0], args[1], args[2], args[3], args[4]);}
			public int method11(int[] args) {return get_accValue(args[0], args[1], args[2], args[3], args[4]);}
			public int method12(int[] args) {return get_accDescription(args[0], args[1], args[2], args[3], args[4]);}
			public int method13(int[] args) {return get_accRole(args[0], args[1], args[2], args[3], args[4]);}
			public int method14(int[] args) {return get_accState(args[0], args[1], args[2], args[3], args[4]);}
			public int method15(int[] args) {return get_accHelp(args[0], args[1], args[2], args[3], args[4]);}
			// method16 get_accHelpTopic - not implemented
			public int method17(int[] args) {return get_accKeyboardShortcut(args[0], args[1], args[2], args[3], args[4]);}
			public int method18(int[] args) {return get_accFocus(args[0]);}
			public int method19(int[] args) {return get_accSelection(args[0]);}
			public int method20(int[] args) {return get_accDefaultAction(args[0], args[1], args[2], args[3], args[4]);}
			// method21 accSelect - not implemented
			public int method22(int[] args) {return accLocation(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
			// method23 accNavigate - not implemented
			public int method24(int[] args) {return accHitTest(args[0], args[1], args[2]);}
			// method25 accDoDefaultAction - not implemented
			// method26 put_accName - not implemented
			// method27 put_accValue - not implemented
		};
		
		objIEnumVARIANT = new COMObject(new int[] {2,0,0,3,1,0,1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return Next(args[0], args[1], args[2]);}
			public int method4(int[] args) {return Skip(args[0]);}
			public int method5(int[] args) {return Reset();}
			// method6 Clone - not implemented
		};
		AddRef();
	}
	
	public static Accessible internal_new_Accessible(Control control) {
		return new Accessible(control);
	}

	public void addAccessibleListener(AccessibleListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleListeners.addElement(listener);
	}
	
	public void removeAccessibleListener(AccessibleListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleListeners.removeElement(listener);
	}
	
	public void addAccessibleControlListener(AccessibleControlListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleControlListeners.addElement(listener);
	}

	public void removeAccessibleControlListener(AccessibleControlListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		accessibleControlListeners.removeElement(listener);
	}
	
	public void internal_dispose_Accessible() {
		if (iaccessible != null)
			iaccessible.Release();
		iaccessible = null;
		Release();
	}
	
	public int internal_WM_GETOBJECT (int wParam, int lParam) {
		if (objIAccessible == null) return 0;
		if (lParam == COM.OBJID_CLIENT) {
			return COM.LresultFromObject(COM.IIDIAccessible, wParam, objIAccessible.getAddress());
		}
		return 0;
	}

	int QueryInterface(int arg1, int arg2) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, arg1, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIDispatch)) {
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessible)) {
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIEnumVARIANT)) {
			COM.MoveMemory(arg2, new int[] { objIEnumVARIANT.getAddress()}, 4);
			AddRef();
			enumIndex = 0;
			return COM.S_OK;
		}

		int[] ppvObject = new int[1];
		int result = iaccessible.QueryInterface(guid, ppvObject);
		COM.MoveMemory(arg2, ppvObject, 4);
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

	int accHitTest(int xLeft, int yTop, int pvarChild) {
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
		COM.MoveMemory(pvarChild, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChild + 8, new int[] { childID + 1 }, 4);
		return COM.S_OK;
	}
	
	int accLocation(int pxLeft, int pyTop, int pcxWidth, int pcyHeight, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.accLocation(pxLeft, pyTop, pcxWidth, pcyHeight, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
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
	
	int get_accChild(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int ppdispChild) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accChild(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, ppdispChild);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChild(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			COM.MoveMemory(ppdispChild, new int[] { accessible.objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		return COM.S_FALSE;
	}
	
	int get_accChildCount(int pcountChildren) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accChildCount(pcountChildren);
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getChildCount(event);
		}

		COM.MoveMemory(pcountChildren, new int[] { event.detail }, 4);
		return COM.S_OK;
	}
	
	int get_accDefaultAction(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDefaultAction) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accDefaultAction(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszDefaultAction);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getDefaultAction(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDefaultAction, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accDescription(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDescription) {
		if (accessibleListeners.size() == 0) {
			return iaccessible.get_accDescription(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszDescription);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getDescription(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDescription, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accFocus(int pvarChild) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accFocus(pvarChild);
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_NONE;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getFocus(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			COM.MoveMemory(pvarChild, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChild + 8, new int[] { accessible.objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarChild, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_SELF) {
			COM.MoveMemory(pvarChild, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChild + 8, new int[] { objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		COM.MoveMemory(pvarChild, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChild + 8, new int[] { childID + 1 }, 4);
		return COM.S_OK;
	}
	
	int get_accHelp(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszHelp) {
		if (accessibleListeners.size() == 0) {
			return iaccessible.get_accHelp(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszHelp);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getHelp(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszHelp, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accKeyboardShortcut(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszKeyboardShortcut) {
		if (accessibleListeners.size() == 0) {
			return iaccessible.get_accKeyboardShortcut(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszKeyboardShortcut);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getKeyboardShortcut(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszKeyboardShortcut, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accName(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszName) {
		if (accessibleListeners.size() == 0) {
			return iaccessible.get_accName(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszName);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.getName(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszName, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accParent(int ppdispParent) {
		return iaccessible.get_accParent(ppdispParent);
	}
	
	int get_accRole(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pvarRole) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accRole(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pvarRole);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getRole(event);
		}
		int role = roleToOs(event.detail);
		COM.MoveMemory(pvarRole, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarRole + 8, new int[] { role }, 4);
		return COM.S_OK;
	}
	
	int get_accSelection(int pvarChildren) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accSelection(pvarChildren);
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = ACC.CHILDID_NONE;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getSelection(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChildren + 8, new int[] { accessible.objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_MULTIPLE) {
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_UNKNOWN }, 2);
			/* Supposed to return an IEnumVARIANT for this... so the next line is wrong... need a better API here... */
			COM.MoveMemory(pvarChildren + 8, new int[] { objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		if (childID == ACC.CHILDID_SELF) {
			COM.MoveMemory(pvarChildren, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarChildren + 8, new int[] { objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		COM.MoveMemory(pvarChildren, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChildren + 8, new int[] { childID + 1 }, 4);
		return COM.S_OK;
	}
	
	int get_accState(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pvarState) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accState(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pvarState);
		}

		if ((varChild_vt & 0xFFFF) != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getState(event);
		}
		int state = stateToOs(event.detail);
		COM.MoveMemory(pvarState, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarState + 8, new int[] { state }, 4);
		return COM.S_OK;
	}
	
	int get_accValue(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszValue) {
		if (accessibleControlListeners.size() == 0) {
			return iaccessible.get_accValue(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszValue);
		}

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal - 1;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.getValue(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszValue, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	/* IEnumVARIANT methods: Next, Skip, Reset */
	int Next(int celt, int rgvar, int pceltFetched) {
		/* Retrieve the next celt items in the enumeration sequence. 
		 * If there are fewer than the requested number of elements left
		 * in the sequence, retrieve the remaining elements.
		 * The number of elements actually retrieved is returned in pceltFetched 
		 * (unless the caller passed in NULL for that parameter).
		 */
		if (rgvar == 0) return COM.E_INVALIDARG;
		if (pceltFetched == 0 && celt != 1) return COM.E_INVALIDARG;
		if (enumIndex == 0) {
			AccessibleControlEvent event = new AccessibleControlEvent(this);
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
						nextItems[i] = new Integer(((Integer)child).intValue() + 1);
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
					COM.MoveMemory(rgvar + i * 16, new short[] { COM.VT_I4 }, 2);
					COM.MoveMemory(rgvar + i * 16 + 8, new int[] { item }, 4);
				} else {
					int address = ((Accessible) nextItem).objIAccessible.getAddress();
					COM.MoveMemory(rgvar + i * 16, new short[] { COM.VT_DISPATCH }, 2);
					COM.MoveMemory(rgvar + i * 16 + 8, new int[] { address }, 4);
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
	
	int Skip(int celt) {
		/* Skip over the specified number of elements in the enumeration sequence. */
		if (celt < 1 ) return COM.E_INVALIDARG;
		enumIndex += celt;
		if (enumIndex > (variants.length - 1)) {
			enumIndex = variants.length - 1;
			return COM.S_FALSE;
		}
		return COM.S_OK;
	}
	
	int Reset() {
		/* Reset the enumeration sequence to the beginning. */
		enumIndex = 0;
		return COM.S_OK;
	}
	
	int stateToOs(int state) {
		int osState = ACC.STATE_NORMAL;
		if ((state & ACC.STATE_SELECTED) != 0){
			osState |= COM.STATE_SYSTEM_SELECTED;
		}
		if ((state & ACC.STATE_SELECTABLE) != 0){
			osState |= COM.STATE_SYSTEM_SELECTABLE;
		}
		if ((state & ACC.STATE_MULTISELECTABLE) != 0){
			osState |= COM.STATE_SYSTEM_MULTISELECTABLE;
		}
		if ((state & ACC.STATE_FOCUSED) != 0){
			osState |= COM.STATE_SYSTEM_FOCUSED;
		}
		if ((state & ACC.STATE_FOCUSABLE) != 0){
			osState |= COM.STATE_SYSTEM_FOCUSABLE;
		}
		if ((state & ACC.STATE_PRESSED) != 0){
			osState |= COM.STATE_SYSTEM_PRESSED;
		}
		if ((state & ACC.STATE_CHECKED) != 0){
			osState |= COM.STATE_SYSTEM_CHECKED;
		}
		if ((state & ACC.STATE_EXPANDED) != 0){
			osState |= COM.STATE_SYSTEM_EXPANDED;
		}
		if ((state & ACC.STATE_COLLAPSED) != 0){
			osState |= COM.STATE_SYSTEM_COLLAPSED;
		}
		if ((state & ACC.STATE_HOTTRACKED) != 0){
			osState |= COM.STATE_SYSTEM_HOTTRACKED;
		}
		if ((state & ACC.STATE_BUSY) != 0){
			osState |= COM.STATE_SYSTEM_BUSY;
		}
		if ((state & ACC.STATE_READONLY) != 0){
			osState |= COM.STATE_SYSTEM_READONLY;
		}
		if ((state & ACC.STATE_INVISIBLE) != 0){
			osState |= COM.STATE_SYSTEM_INVISIBLE;
		}
		if ((state & ACC.STATE_OFFSCREEN) != 0){
			osState |= COM.STATE_SYSTEM_OFFSCREEN;
		}
		if ((state & ACC.STATE_SIZEABLE) != 0){
			osState |= COM.STATE_SYSTEM_SIZEABLE;
		}
		return osState;
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
			case ACC.ROLE_COMBOBOX: return COM.ROLE_SYSTEM_COMBOBOX;
			case ACC.ROLE_TEXT: return COM.ROLE_SYSTEM_TEXT;
			case ACC.ROLE_TOOLBAR: return COM.ROLE_SYSTEM_TOOLBAR;
			case ACC.ROLE_LIST: return COM.ROLE_SYSTEM_LIST;
			case ACC.ROLE_LISTITEM: return COM.ROLE_SYSTEM_LISTITEM;
			case ACC.ROLE_TABLE: return COM.ROLE_SYSTEM_TABLE;
			case ACC.ROLE_TABLECOLUMN: return COM.ROLE_SYSTEM_COLUMNHEADER;
			case ACC.ROLE_TREE: return COM.ROLE_SYSTEM_OUTLINE;
			case ACC.ROLE_TABFOLDER: return COM.ROLE_SYSTEM_PAGETABLIST;
			case ACC.ROLE_TABITEM: return COM.ROLE_SYSTEM_PAGETAB;
			case ACC.ROLE_PROGRESSBAR: return COM.ROLE_SYSTEM_PROGRESSBAR;
			case ACC.ROLE_SLIDER: return COM.ROLE_SYSTEM_SLIDER;
		}
		return COM.ROLE_SYSTEM_CLIENT;
	}
}

package org.eclipse.swt.accessibility;

import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

public class Accessible {
	static final boolean debug = true;
	Control control;
	int refCount = 0;
	COMObject objIAccessible;
	IAccessible iaccessible;
	Vector accessibleListeners, accessibleControlListeners;

	public Accessible(Control control) {
		this.control = control;
		control.addListener(SWT.AccessAccessibility, new Listener() {
			public void handleEvent(Event event) {
				int lParam = ((int []) event.data)[0];
				int wParam = ((int []) event.data)[1];
				if (lParam == COM.OBJID_CLIENT) {
					event.detail = ACC.LresultFromObject(COM.IIDIAccessible, wParam, objIAccessible.getAddress());
				}
			}
		});
		objIAccessible = new COMObject(new int[] { 2, 0, 0, 1, 3, 5, 8, 1, 1, 5, 5, 5, 5, 5, 5, 5, 6, 5, 1, 1, 5, 5, 8, 6, 3, 4, 5, 5 }) {
			public int method0(int[] args) {
				return QueryInterface(args[0], args[1]);
			}
			public int method1(int[] args) {
				return AddRef();
			}
			public int method2(int[] args) {
				return Release();
			}
			public int method3(int[] args) {
				return GetTypeInfoCount(args[0]);
			}
			public int method4(int[] args) {
				return GetTypeInfo(args[0], args[1], args[2]);
			}
			public int method5(int[] args) {
				return GetIDsOfNames(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method6(int[] args) {
				return Invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
			}
			public int method7(int[] args) {
				return get_accParent(args[0]);
			}
			public int method8(int[] args) {
				return get_accChildCount(args[0]);
			}
			public int method9(int[] args) {
				return get_accChild(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method10(int[] args) {
				return get_accName(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method11(int[] args) {
				return get_accValue(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method12(int[] args) {
				return get_accDescription(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method13(int[] args) {
				return get_accRole(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method14(int[] args) {
				return get_accState(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method15(int[] args) {
				return get_accHelp(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method16(int[] args) {
				return get_accHelpTopic(args[0], args[1], args[2], args[3], args[4], args[5]);
			}
			public int method17(int[] args) {
				return get_accKeyboardShortcut(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method18(int[] args) {
				return get_accFocus(args[0]);
			}
			public int method19(int[] args) {
				return get_accSelection(args[0]);
			}
			public int method20(int[] args) {
				return get_accDefaultAction(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method21(int[] args) {
				return accSelect(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method22(int[] args) {
				return accLocation(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
			}
			public int method23(int[] args) {
				return accNavigate(args[0], args[1], args[2], args[3], args[4], args[5]);
			}
			public int method24(int[] args) {
				return accHitTest(args[0], args[1], args[2]);
			}
			public int method25(int[] args) {
				return accDoDefaultAction(args[0], args[1], args[2], args[3]);
			}
			public int method26(int[] args) {
				return put_accName(args[0], args[1], args[2], args[3], args[4]);
			}
			public int method27(int[] args) {
				return put_accValue(args[0], args[1], args[2], args[3], args[4]);
			}
		};

		int[] ppvObject = new int[1];
		int result = ACC.CreateStdAccessibleObject(control.handle, COM.OBJID_CLIENT, COM.IIDIAccessible, ppvObject);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		iaccessible = new IAccessible(ppvObject[0]);
		iaccessible.AddRef();
		//new TypeInfoBrowser(ppvObject[0]);
	}

	public void addAccessibleListener(AccessibleListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleListeners == null) {
			accessibleListeners = new Vector();
		}
		accessibleListeners.addElement(listener);
	}
	
	// need removeAccessibleListener also
	
	public void addAccessibleControlListener(AccessibleControlListener listener) {
		if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (accessibleControlListeners == null) {
			accessibleControlListeners = new Vector();
		}
		accessibleControlListeners.addElement(listener);
	}

	// need removeAccessibleControlListener also
	
	int QueryInterface(int arg1, int arg2) {
		if (debug)
			System.out.println("QueryInterface");

		GUID guid = new GUID();
		COM.MoveMemory(guid, arg1, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
			if (debug)
				System.out.println("IUnknown");
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIDispatch)) {
			if (debug)
				System.out.println("IDispatch");
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessible)) {
			if (debug)
				System.out.println("IAccessible");
			COM.MoveMemory(arg2, new int[] { objIAccessible.getAddress()}, 4);
			return COM.S_OK;
		}

		int[] ppvObject = new int[1];
		int result = iaccessible.QueryInterface(guid, ppvObject);
		COM.MoveMemory(arg2, ppvObject, 4);
		if (debug)
			System.out.println("QI other " + result);
		return result;
	}

	int AddRef() {
		if (debug)
			System.out.println("AddRef");
		refCount++;
		return refCount;
	}

	int Release() {
		if (debug)
			System.out.println("Release");
		refCount--;

		if (refCount == 0) {
			//disposeCOMInterfaces();
		}
		return refCount;
	}

	int GetTypeInfoCount(int pctinfo) {
		if (debug)
			System.out.println("GetTypeInfoCount");
		return COM.E_NOTIMPL;
	}

	int GetTypeInfo(int iTInfo, int lcid, int ppTInfo) {
		if (debug)
			System.out.println("GetTypeInfo");
		return COM.E_NOTIMPL;
	}

	int GetIDsOfNames(int riid, int rgszNames, int cNames, int lcid, int rgDispId) {
		if (debug)
			System.out.println("GetIDsOfNames");
		return COM.E_NOTIMPL;
	}

	int Invoke(int dispIdMember, int riid, int lcid, int dwFlags, int pDispParams, int pVarResult, int pExcepInfo, int pArgErr) {
		if (debug)
			System.out.println("Invoke");
		return COM.E_NOTIMPL;
	}

	int accDoDefaultAction(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
		if (debug)
			System.out.println("accDoDefaultAction " + varChild_vt + " " + varChild_lVal);
		// must implement this for the items because they have a default action
		return iaccessible.accDoDefaultAction(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
	}
	
	int accHitTest(int xLeft, int yTop, int pvarChild) {
		if (debug)
			System.out.println("accHitTest " + xLeft + " " + yTop + " " + pvarChild);

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.x = xLeft;
		event.y = yTop;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.accHitTest(event);
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarChild, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		COM.MoveMemory(pvarChild, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarChild + 8, new int[] { childID }, 4);
		return COM.S_OK;
	}
	
	int accLocation(int pxLeft, int pyTop, int pcxWidth, int pcyHeight, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
		if (debug)
			System.out.println("accLocation " + pxLeft + " " + pyTop + " " + pcxWidth + " " + pcyHeight + " " + varChild_vt + " " + varChild_lVal);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.accLocation(event);
		}
		OS.MoveMemory(pxLeft, new int[] { event.x }, 4);
		OS.MoveMemory(pyTop, new int[] { event.y }, 4);
		OS.MoveMemory(pcxWidth, new int[] { event.width }, 4);
		OS.MoveMemory(pcyHeight, new int[] { event.height }, 4);
		return COM.S_OK;
	}
	
	int accNavigate(int navDir, int varStart_vt, int varStart_reserved1, int varStart_lVal, int varStart_reserved2, int pvarEndUpAt) {
		if (debug)
			System.out.println("accNavigate " + navDir + " " + varStart_vt + " " + varStart_lVal + " " + pvarEndUpAt);

		if (varStart_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.code = navDir;
		event.childID = varStart_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.accNavigate(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			COM.MoveMemory(pvarEndUpAt, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarEndUpAt + 8, new int[] { accessible.objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		int childID = event.childID;
		if (childID == ACC.CHILDID_NONE) {
			COM.MoveMemory(pvarEndUpAt, new short[] { COM.VT_EMPTY }, 2);
			return COM.S_FALSE;
		}
		if (childID == ACC.CHILDID_SELF) {
			COM.MoveMemory(pvarEndUpAt, new short[] { COM.VT_DISPATCH }, 2);
			COM.MoveMemory(pvarEndUpAt + 8, new int[] { objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		COM.MoveMemory(pvarEndUpAt, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarEndUpAt + 8, new int[] { childID }, 4);
		return COM.S_OK;
	}
	
	int accSelect(int flagsSelect, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
		if (debug)
			System.out.println("accSelect " + flagsSelect + " " + varChild_vt + " " + varChild_lVal);
		// must implement for the items because they support selection
		return iaccessible.accSelect(flagsSelect, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
	}
	
	int get_accChild(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int ppdispChild) {
		if (debug)
			System.out.println("get_accChild " + varChild_vt + " " + varChild_lVal + " " + ppdispChild);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accChild(event);
		}
		Accessible accessible = event.accessible;
		if (accessible != null) {
			COM.MoveMemory(ppdispChild, new int[] { accessible.objIAccessible.getAddress() }, 4);
			return COM.S_OK;
		}
		return COM.S_FALSE;
	}
	
	int get_accChildCount(int pcountChildren) {
		if (debug)
			System.out.println("get_accChildCount " + pcountChildren);

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accChildCount(event);
		}

		COM.MoveMemory(pcountChildren, new int[] { event.code }, 4);
		return COM.S_OK;
	}
	
	int get_accDefaultAction(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDefaultAction) {
		if (debug)
			System.out.println("get_accDefaultAction " + varChild_vt + " " + varChild_lVal + " " + pszDefaultAction);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accDefaultAction(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDefaultAction, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accDescription(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDescription) {
		if (debug)
			System.out.println("get_accDescription " + varChild_vt + " " + varChild_lVal + " " + pszDescription);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.get_accDescription(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszDescription, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accFocus(int pvarChild) {
		if (debug)
			System.out.println("get_accFocus " + pvarChild);

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accFocus(event);
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
		COM.MoveMemory(pvarChild + 8, new int[] { childID }, 4);
		return COM.S_OK;
	}
	
	int get_accHelp(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszHelp) {
		if (debug)
			System.out.println("get_accHelp " + varChild_vt + " " + varChild_lVal + " " + pszHelp);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.get_accHelp(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszHelp, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accHelpTopic(int pszHelpFile, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pidTopic) {
		if (debug)
			System.out.println("get_accHelpTopic " + pszHelpFile + " " + varChild_vt + " " + varChild_lVal + " " + pidTopic);
		return COM.S_FALSE;
	}
	
	int get_accKeyboardShortcut(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszKeyboardShortcut) {
		if (debug)
			System.out.println("get_accKeyboardShortcut " + pszKeyboardShortcut);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.get_accKeyboardShortcut(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszKeyboardShortcut, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int get_accName(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszName) {
		if (debug)
			System.out.println("get_accName " + varChild_vt + " " + varChild_lVal + " " + pszName);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleEvent event = new AccessibleEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleListeners.size(); i++) {
			AccessibleListener listener = (AccessibleListener) accessibleListeners.elementAt(i);
			listener.get_accName(event);
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
		if (debug)
			System.out.println("get_accRole " + varChild_vt + " " + varChild_lVal + " " + pvarRole);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accRole(event);
		}
		int role = event.code;
		COM.MoveMemory(pvarRole, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarRole + 8, new int[] { role }, 4);
		return COM.S_OK;
	}
	
	int get_accSelection(int pvarChildren) {
		if (debug)
			System.out.println("get_accSelection " + pvarChildren);

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accSelection(event);
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
		COM.MoveMemory(pvarChildren + 8, new int[] { childID }, 4);
		return COM.S_OK;
	}
	
	int get_accState(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pvarState) {
		if (debug)
			System.out.println("get_accState " + varChild_vt + " " + varChild_lVal + " " + pvarState);

		if (varChild_vt != COM.VT_I4) return COM.E_INVALIDARG;
		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accState(event);
		}
		int state = event.code;
		COM.MoveMemory(pvarState, new short[] { COM.VT_I4 }, 2);
		COM.MoveMemory(pvarState + 8, new int[] { state }, 4);
		return COM.S_OK;
	}
	
	int get_accValue(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszValue) {
		if (debug)
			System.out.println("get_accValue " + varChild_vt + " " + varChild_lVal + " " + pszValue);

		AccessibleControlEvent event = new AccessibleControlEvent(this);
		event.childID = varChild_lVal;
		for (int i = 0; i < accessibleControlListeners.size(); i++) {
			AccessibleControlListener listener = (AccessibleControlListener) accessibleControlListeners.elementAt(i);
			listener.get_accValue(event);
		}
		if (event.result == null) return COM.S_FALSE;
		char[] data = (event.result + "\0").toCharArray();
		int ptr = COM.SysAllocString(data);
		COM.MoveMemory(pszValue, new int[] { ptr }, 4);
		return COM.S_OK;
	}
	
	int put_accName(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int szName) {
		// this method is no longer supported
		if (debug)
			System.out.println("put_accName " + varChild_vt + " " + varChild_lVal + " " + szName);
		return iaccessible.put_accName(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, szName);
	}
	
	int put_accValue(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int szValue) {
		// this method is typically only used for edit controls
		if (debug)
			System.out.println("put_accValue " + varChild_vt + " " + varChild_lVal + " " + szValue);
		return iaccessible.put_accValue(varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, szValue);
	}
}

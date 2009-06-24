package org.eclipse.swt.internal.mozilla;


public class nsISecurityCheckedComponent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ISECURITYCHECKEDCOMPONENT_IID_STR =
		"0dad9e8c-a12d-4dcb-9a6f-7d09839356e1";

	public static final nsID NS_ISECURITYCHECKEDCOMPONENT_IID =
		new nsID(NS_ISECURITYCHECKEDCOMPONENT_IID_STR);

	public nsISecurityCheckedComponent(int /*long*/ address) {
		super(address);
	}

	public int CanCreateWrapper(int /*long*/ iid, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), iid, _retval);
	}

	public int CanCallMethod(int /*long*/ iid, char[] methodName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), iid, methodName, _retval);
	}

	public int CanGetProperty(int /*long*/ iid, char[] propertyName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), iid, propertyName, _retval);
	}

	public int CanSetProperty(int /*long*/ iid, char[] propertyName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), iid, propertyName, _retval);
	}
}

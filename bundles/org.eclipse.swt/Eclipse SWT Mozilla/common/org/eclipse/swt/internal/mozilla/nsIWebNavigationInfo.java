package org.eclipse.swt.internal.mozilla;


public class nsIWebNavigationInfo extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_IWEBNAVIGATIONINFO_IID_STR =
		"62a93afb-93a1-465c-84c8-0432264229de";

	public static final nsID NS_IWEBNAVIGATIONINFO_IID =
		new nsID(NS_IWEBNAVIGATIONINFO_IID_STR);

	public nsIWebNavigationInfo(int /*long*/ address) {
		super(address);
	}

	public static final int UNSUPPORTED = 0;

	public static final int IMAGE = 1;

	public static final int PLUGIN = 2;

	public static final int OTHER = 32768;

	public int IsTypeSupported(int /*long*/ aType, int /*long*/ aWebNav, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aType, aWebNav, _retval);
	}
}
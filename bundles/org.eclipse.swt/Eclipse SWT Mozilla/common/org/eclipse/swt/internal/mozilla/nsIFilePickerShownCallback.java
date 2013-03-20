package org.eclipse.swt.internal.mozilla;

public class nsIFilePickerShownCallback extends nsISupports {
	
	public static final String NS_IFILEPICKER_IID_STR =
			"0d79adad-b244-49A5-9997-2a8cad93fc44";
	
	public static final nsID NS_IFILEPICKER_IID =
			new nsID (NS_IFILEPICKER_IID_STR);
	
	public nsIFilePickerShownCallback (long /*int*/ address) {
		super (address);
	}
	
	public int Done (int retval) {
		return XPCOM.VtblCall (nsISupports.LAST_METHOD_ID + 1, getAddress(), retval);
	}
}

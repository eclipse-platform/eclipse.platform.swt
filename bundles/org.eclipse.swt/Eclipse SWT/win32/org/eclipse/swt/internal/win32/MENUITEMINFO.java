package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class MENUITEMINFO {
	public int cbSize;
	public int fMask;
	public int fType;
	public int fState;
	public int wID;
	public int hSubMenu;
	public int hbmpChecked;
	public int hbmpUnchecked;
	public int dwItemData;
	public int dwTypeData;
	public int cch;
	public int hbmpItem;
	public static final int sizeof;

	/*
	* Feature in Windows.  The hbmpItem field requires Windows 4.10
	* or greater.  On Windows NT 4.0, passing in a larger struct size
	* in the cbSize field does nothing.  On Windows 95, the MENUITEMINFO
	* calls fail when the struct size is too large.  The fix is to ensure
	* that the correct struct size is used for the Windows platform.
	*/
	static {
		int version = OS.GetVersion ();
		int WIN32_MAJOR = version & 0x00FF, WIN32_MINOR = (version & 0xFFFF) >> 8;
		sizeof = (WIN32_MAJOR << 16 | WIN32_MINOR) < (4 << 16 | 10) ? 44 : 48;
	}
}

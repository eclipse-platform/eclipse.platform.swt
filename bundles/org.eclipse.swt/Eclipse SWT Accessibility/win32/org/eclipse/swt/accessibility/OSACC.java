package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.ole.win32.COM;

/**
 * Class OSACC contains all of the OS constants needed for accessibility.
 */
public class OSACC extends COM {
	public static final int OBJID_CLIENT = 0xfffffffc;
	public static final int WM_GETOBJECT = 0x3d;
	public static final int CO_E_OBJNOTCONNECTED = 0x800401FD;
	
	/* Need these constants - but first should make up nice names/values for the public constants in ACC... */
//	public static final int STATE_SYSTEM_NORMAL = 0x00000000;
//	public static final int STATE_SYSTEM_SELECTED = 0x00000002;
//	public static final int STATE_SYSTEM_SELECTABLE = 0x00200000;
//	public static final int STATE_SYSTEM_FOCUSED = 0x00000004;
//	public static final int STATE_SYSTEM_FOCUSABLE = 0x00100000;
//	public static final int ROLE_SYSTEM_CLIENT = 0xa;
//	public static final int ROLE_SYSTEM_PUSHBUTTON = 0x2b;
//	public static final int ROLE_SYSTEM_PAGETAB = 0x25;
//	public static final int ROLE_SYSTEM_PAGETABLIST = 0x3c;
//	public static final int ROLE_SYSTEM_STATICTEXT = 0x29;
//	public static final int NAVDIR_UP = 0x1;
//	public static final int NAVDIR_DOWN = 0x2;
//	public static final int NAVDIR_LEFT = 0x3;
//	public static final int NAVDIR_RIGHT = 0x4;
//	public static final int NAVDIR_NEXT = 0x5;
//	public static final int NAVDIR_PREVIOUS = 0x6;
//	public static final int NAVDIR_FIRSTCHILD = 0x7;
//	public static final int NAVDIR_LASTCHILD = 0x8;
	
	// not sure why these don't work in this class? These natives are defined in COM for the moment...
	//public static final native int CreateStdAccessibleObject (int hwnd, int idObject, GUID riidInterface, int[] ppvObject);
	//public static final native int LresultFromObject (GUID riid, int wParam, int pAcc);
}

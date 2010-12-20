package org.eclipse.swt.internal.win32;

public class GESTURECONFIG {
	public int dwID;                     // gesture ID
    public int dwWant;                   // settings related to gesture ID that are to be turned on
    public int dwBlock;                  // settings related to gesture ID that are to be turned off
    public static final int sizeof = OS.GESTURECONFIG_sizeof ();
}

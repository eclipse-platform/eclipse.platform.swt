package org.eclipse.swt.internal.win32;

public class GESTUREINFO {
	 public int cbSize;
	 public int dwFlags;
	 public int dwID;
	 /** @field cast=(HWND) */
	 public int /*long*/ hwndTarget;
	 //	POINTS ptsLocation
	 /** @field accessor=ptsLocation.x */
	 public short x;
	 /** @field accessor=ptsLocation.y */
	 public short y;
	 public int dwInstanceID;
	 public int dwSequenceID;
	 public long ullArguments;
	 public int cbExtraArgs;
	 public static final int sizeof = OS.GESTUREINFO_sizeof ();
}

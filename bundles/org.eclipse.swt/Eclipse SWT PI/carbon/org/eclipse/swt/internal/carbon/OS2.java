package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
class OS2 {

/** Custom natives - pixmap */
public static final native int CopyMask(int srcPixMapHandle, int maskPixMapHandle, int dstPixMapHandle, Rect srcRect, Rect maskRect, Rect dstRect);
public static final native int DerefHandle(int handle);
public static final native void DisposePixMap(int pHandle);
public static native int NewGWorldFromPtr(int[] offscreenGWorld, int pHandle);
public static final native int NewCIcon(int pixmapHandle, int maskHandle);
public static final native int NewCursor(short hotX, short hotY, short[] data, short[] mask);
public static final native int NewPixMap(short w, short h, short rowBytes, short pixelType, short pixelSize, short cmpSize, short cmpCount, short pixelFormat);
public static final native int duplicatePixMap(int srcPixmap);
public static final native int getBaseAddr(int pHandle);
public static final native int getCIconColorTable(int iconHandle);
public static final native int getCIconIconData(int iconHandle);
public static final native void getColorTable(int pHandle, short[] colorSpec);
public static final native int getColorTableSize(int pHandle);
public static final native int getPixHRes(int pHandle);
public static final native int getPixVRes(int pHandle);
public static final native int getRowBytes(int pHandle);
public static final native int getgdPMap(int gdHandle);
public static final native void setBaseAddr(int pHandle, int data);
public static final native void setColorTable(int pHandle, short[] colorSpec);
public static final native void setPixBounds(int pHandle, short top, short left, short bottom, short right);
public static final native void setRowBytes(int pHandle, short rowBytes);

/** Custom helpers */
public static int CFStringCreateWithCharacters(String string) {
	char [] chars = new char [string.length()];
	string.getChars(0, chars.length, chars, 0);
	return OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
}

//////////////////////////////////////////
//  added by AW while merging HIView stuff

public static native int HIViewSetBoundsOrigin(int inView, float x, float y);
public static native int HIViewGetPartHit(int inView, float[] where, short[] outPart);
public static native int HIViewGetSubviewHit(int inView, float[] inPoint, boolean inDeep, int[] outView);
}

package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
class OS2 {

/** Custom natives - misc */	
public static final native void AEProcessAppleEvent(int[] theEventRecord);
public static final native boolean ConvertEventRefToEventRecord(int eHandle, int[] outEvent);
public static final native boolean GetNextEvent(short eventMask, int[] eventData);
public static final native boolean IsShowContextualMenuClick(int[] eventData);
public static final native int MenuEvent(int[] eventData);
public static final native void TXNClick(int txHandle, int[] eventData);
public static final native int TXNGetRectBounds(int txHandle, short[] viewRect, int[] destinationRect, int[] textRect);
public static final native int GetEventHICommand(int eRefHandle, int[] outParamType);
public static final native int createDataBrowserControl(int wHandle);
public static final native void setDataBrowserCallbacks(int cHandle, int dataCallbackUPP, int compareCallbackUPP, int itemNotificationCallbackUPP);
public static final native void setTXNMargins(int txHandle, short margin);
public static final native void getHandleData(int handle, char[] data);
public static final native void getHandleData(int handle, int[] data);

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
public static void RGBBackColor(short red, short green, short blue) {
	RGBColor color = new RGBColor();
	color.red = red;
	color.green = green;
	color.blue = blue;
	OS.RGBBackColor(color);
}
public static void RGBForeColor(short red, short green, short blue) {	
	RGBColor color = new RGBColor();
	color.red = red;
	color.green = green;
	color.blue = blue;
	OS.RGBForeColor(color);
}

//////////////////////////////////////////
//  added by AW while merging HIView stuff

public static native void CGContextBeginPath(int inContext);
public static native void CGContextAddArc(int inContext, float x, float y, float radius,
				float startAngle, float endAngle, int clockwise);
public static native void CGContextClosePath(int inContext);
public static native void CGContextStrokePath(int inContext);

public static native void CGContextFillPath(int inContext);

public static native int HIViewSetBoundsOrigin(int inView, float x, float y);
public static native int HIViewGetPartHit(int inView, float[] where, short[] outPart);
public static native int HIViewGetSubviewHit(int inView, float[] inPoint, boolean inDeep, int[] outView);

}

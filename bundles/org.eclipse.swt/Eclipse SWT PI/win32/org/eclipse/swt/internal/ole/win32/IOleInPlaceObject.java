package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.win32.*;

public class IOleInPlaceObject extends IOleWindow
{	
public IOleInPlaceObject(int address) {
	super(address);
}
public int InPlaceDeactivate() {
	return COM.VtblCall(5, address);
}
public int SetObjectRects(RECT lprcPosRect, RECT lprcClipRect) {
	return COM.VtblCall(7, address, lprcPosRect, lprcClipRect);
}
}

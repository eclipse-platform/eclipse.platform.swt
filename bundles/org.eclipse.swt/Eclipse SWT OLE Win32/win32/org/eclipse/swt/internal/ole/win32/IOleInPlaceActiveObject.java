package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.swt.internal.win32.*;

public class IOleInPlaceActiveObject extends IOleWindow
{
public IOleInPlaceActiveObject(int address) {
	super(address);
}
public int ResizeBorder(RECT prcBorder, int pUIWindow, boolean fFrameWindow) {
	return COM.VtblCall(8, address, prcBorder, pUIWindow, fFrameWindow);
}
}

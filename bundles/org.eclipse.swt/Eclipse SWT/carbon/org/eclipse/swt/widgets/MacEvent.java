/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;

class MacEvent {

	private static int fgMouseButtonState;
	
	static final boolean EMULATE_RIGHT_BUTTON= true;

	private int fEventRef;
	private int fNextHandler;
		
	public MacEvent() {
		fEventRef= -1;
	}

	public MacEvent(int eventRef) {
		fEventRef= eventRef;
	}
	
	public MacEvent(int eventRef, int nextHandler) {
		fEventRef= eventRef;
		fNextHandler= nextHandler;
	}
	
	public int getEventRef() {
		return fEventRef;
	}
	
	public int getNextHandler() {
		return fNextHandler;
	}
	
	public int[] toOldMacEvent() {
		if (fEventRef != -1) {
			int macEvent[]= new int[6];
			if (OS.ConvertEventRefToEventRecord(fEventRef, macEvent))
				return macEvent;
		}
		System.out.println("MacEvent.toOldMacEvent: can't convert event");
		return null;
	}
	
	public int getKind() {
		if (fEventRef != -1)
			return OS.GetEventKind(fEventRef);
		System.out.println("MacEvent.getKind: no EventRef");
		return 0;
	}
	
	public int getWhen() {
		if (fEventRef != -1)
			return (int)(OS.GetEventTime(fEventRef) * 1000.0);
		System.out.println("MacEvent.getModifierKeys: no EventRef");
		return 0;
	}
	
	public org.eclipse.swt.internal.carbon.Point getWhere() {
		if (fEventRef != -1) {
			short[] loc= new short[2];
			if (OS.GetEventParameter(fEventRef, OS.kEventParamMouseLocation, OS.typeQDPoint, null, loc.length*2, null, loc) == OS.noErr) {
				org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point();
				OS.SetPt(pt, loc[1], loc[0]);
				return pt;
			}
		}
		System.out.println("MacEvent.getWhere: no EventRef");
		return new org.eclipse.swt.internal.carbon.Point();
	}
	
	public Point getWhere2() {
		if (fEventRef != -1) {
			short[] loc= new short[2];
			if (OS.GetEventParameter(fEventRef, OS.kEventParamMouseLocation, OS.typeQDPoint, null, loc.length*2, null, loc) == OS.noErr) {
				return new Point(loc[1], loc[0]);
			}
		}
		System.out.println("MacEvent.getWhere2: no EventRef");
		return new Point(0, 0);
	}

	/**
	 * In window coordinates.
	 */
	public org.eclipse.swt.internal.carbon.Point getWindowWhere(int window) {
		org.eclipse.swt.internal.carbon.Point where= getWhere();
		OS.QDGlobalToLocalPoint(OS.GetWindowPort(window), where);
		return where;
	}

	/**
	 * Returns the Mac modifiers for this event
	 */
	public int getModifiers() {
		if (fEventRef != -1)
			return getEventModifiers(fEventRef);
		System.out.println("MacEvent.getModifiers: no EventRef");
		return 0;
	}
	
	/**
	 * Returns the SWT modifiers for this event
	 */
	public int getStateMask() {
		int stateMask= fgMouseButtonState;
		int modifiers= getModifiers ();
		if ((modifiers & OS.shiftKey) != 0) stateMask |= SWT.SHIFT;
		if ((modifiers & OS.controlKey) != 0) {
			if (EMULATE_RIGHT_BUTTON) {
				// we only report CONTROL, iff it was not used to emulate the right mouse button
				if ((stateMask & SWT.BUTTON3) == 0) stateMask |= SWT.CONTROL;
			} else {
				stateMask |= SWT.CONTROL;
			}
		}
		if ((modifiers & OS.cmdKey) != 0) stateMask |= SWT.COMMAND;
		if ((modifiers & OS.optionKey) != 0) stateMask |= SWT.ALT;
		return stateMask;
	}
		
	public int getKeyCode() {
		if (fEventRef != -1)
			return getKeyCode(fEventRef);
		System.out.println("MacEvent.getKeyCode: no EventRef");
		return 0;
	}
	
	/**
	 * Returns the SWT mouse button
	 */
	public int getButton() {
		if (fEventRef != -1)
			return getEventMouseButton(fEventRef);

		System.out.println("MacEvent.getButton: no EventRef");
		return 0;
	}
	
	public boolean isShowContextualMenuClick() {
		if (fEventRef == -1) {
			System.out.println("MacEvent.isShowContextualMenuClick: no EventRef");
			return false;
		}
		return (OS.GetEventClass(fEventRef) == OS.kEventClassMouse) && 
					(getKind() == OS.kEventMouseDown) &&
						(getButton() == 3);
		// return OS.IsShowContextualMenuClick(getData());
	}

	public int getMacCharCodes() {
		if (fEventRef != -1)
			return getCharCode(fEventRef);
		System.out.println("MacEvent.getMacCharCodes: no EventRef");
		return -1;
	}

	public String getText() {
		if (fEventRef == -1) {
			System.out.println("MacEvent.getText: no EventRef");
			return null;
		}
		int[] actualSize= new int[1];
		OS.GetEventParameter(fEventRef, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, 0, actualSize, (char[])null);
		int size= actualSize[0] / 2;
		if (size > 0) {
			char[] buffer= new char[size];
			OS.GetEventParameter(fEventRef, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, buffer.length*2, null, buffer);
			return new String(buffer);			
		}
		return "";
	}

	//---- Carbon event accessors
	
	public static int getDirectObject(int eRefHandle) {
		int[] wHandle= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamDirectObject, OS.typeWindowRef, null, wHandle.length*4, null, wHandle) == OS.noErr)	
			return wHandle[0];
		return 0;
	}
	
	public static short getWindowDefPart(int eRefHandle) {
		short[] part= new short[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamWindowDefPart, OS.typeWindowDefPartCode, null, part.length*2, null, part) == OS.noErr)	
			return part[0];
		return 0;
	}
	
	public static int getControlRef(int eRefHandle) {
		int[] cHandle= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamControlRef, OS.typeControlRef, null, cHandle.length*4, null, cHandle) == OS.noErr)	
			return cHandle[0];
		return 0;
	}
	
	public static int getEventModifiers(int eRefHandle) {
		int[] modifierKeys= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamKeyModifiers, OS.typeUInt32, null, modifierKeys.length*4, null, modifierKeys) == OS.noErr) {	
			return modifierKeys[0];
		}
		System.out.println("MacEvent.getModifierKeys: getEventModifiers error");			
		return -1;
	}

	private static int getMouseChord(int eRefHandle) {
		int[] mouseChord= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamMouseChord, OS.typeUInt32, null, mouseChord.length*4, null, mouseChord) == OS.noErr) {	
			return mouseChord[0];
		}
		System.out.println("MacEvent.getMouseChord: getMouseChord error");			
		return -1;
	}
	
	public static int getKeyCode(int eRefHandle) {
		int[] keyCode= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length*4, null, keyCode) == OS.noErr)
			return keyCode[0];
		System.out.println("MacEvent.getMouseChord: getKeyCode error");			
		return -1;
	}
	
	public static int getCharCode(int eRefHandle) {
		byte[] charCode= new byte[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamKeyMacCharCodes, OS.typeChar, null, charCode.length, null, charCode) == OS.noErr)	
			return charCode[0];
		return -1;
	}

	private static short getEventMouseButton(int eRefHandle) {
		short[] mouseButtons= new short[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamMouseButton, OS.typeMouseButton, null, mouseButtons.length*2, null, mouseButtons) == OS.noErr) {	
			short button= mouseButtons[0];
			switch (button) {
			case OS.kEventMouseButtonPrimary:		// left mouse button
				if (EMULATE_RIGHT_BUTTON) {
					if ((getEventModifiers(eRefHandle) & OS.controlKey) != 0)
						return 3;
				}
				return 1;
			case OS.kEventMouseButtonSecondary:	// right mouse button
				return 3;
			case OS.kEventMouseButtonTertiary:		// middle mouse button
				return 2;
			default:
				return button;
			}
		}
		return 0;
	}
	
	public static void trackStateMask(int event, int kind) {
		switch (kind) {				
		case OS.kEventMouseDown:
		case OS.kEventMouseDragged:
		case OS.kEventMouseUp:
			int chord= getMouseChord(event);
			if (chord != -1) {
				fgMouseButtonState= 0;
				if ((chord & 1) != 0) {
					int modifiers= getEventModifiers(event);
					if (EMULATE_RIGHT_BUTTON && ((modifiers & OS.controlKey) != 0)) {
						fgMouseButtonState |= SWT.BUTTON3;	
					} else {
						fgMouseButtonState |= SWT.BUTTON1;
					}
				}
				if ((chord & 2) != 0)
					fgMouseButtonState |= SWT.BUTTON3;
				if ((chord & 4) != 0)
					fgMouseButtonState |= SWT.BUTTON2;
			}
			break;			
		case OS.kEventMouseMoved:
			fgMouseButtonState= 0;
			break;
		}
	}
}

/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class MacEvent {

	private static final boolean EMULATE_RIGHT_BUTTON= true;

	// 0: what
	// 1: message
	// 2: when
	// 3: where.v
	// 4: where.h
	// 5: modifiers
	private int[] fData;
	private int fEventRef;
	
	public MacEvent() {
		fEventRef= -1;
	}

	public MacEvent(int eventRef) {
		fEventRef= eventRef;
	}
	
	private void convert() {
		if (fData == null) {
			fData= new int[6];
			if (fEventRef != -1) {
				//System.out.println("ConvertEventRefToEventRecord");
				if (!OS.ConvertEventRefToEventRecord(fEventRef, fData))
					System.out.println("MacEvent.MacEvent(int): can't convert event");
			}
		}
	}
	
	public int[] getData() {
		convert();
		return fData;
	}
	
	public int getKind() {
		if (fEventRef != -1)
			return OS.GetEventKind(fEventRef);
		System.out.println("MacEvent.getKind: no EventRef");
		convert();
		return (short) fData[0];
	}
	
	public int getWhen() {
		if (fEventRef != -1)
			return (int)(OS.GetEventTime(fEventRef) * 1000.0);
		System.out.println("MacEvent.getModifierKeys: no EventRef");
		convert();
		return (fData[2] * 1000)/60;
	}
	
	public MacPoint getWhere() {
		if (fEventRef != -1) {
			short[] loc= new short[2];
			if (OS.GetEventParameter(fEventRef, OS.kEventParamMouseLocation, OS.typeQDPoint, null, null, loc) == OS.kNoErr) {
				return new MacPoint(loc[1], loc[0]);
			}
		}
		System.out.println("MacEvent.getWhere: no EventRef");
		convert();
		return new MacPoint(fData[4], fData[3]);
	}
	
	public Point getWhere2() {
		if (fEventRef != -1) {
			short[] loc= new short[2];
			if (OS.GetEventParameter(fEventRef, OS.kEventParamMouseLocation, OS.typeQDPoint, null, null, loc) == OS.kNoErr) {
				return new Point(loc[1], loc[0]);
			}
		}
		System.out.println("MacEvent.getWhere2: no EventRef");
		convert();
		return new Point(fData[4], fData[3]);
	}

	public int getModifiers() {
		if (fEventRef != -1)
			return getEventModifiers(fEventRef);
			
		System.out.println("MacEvent.getModifierKeys: no EventRef");
		convert();
		return fData[5];
	}
		
	public int getKeyCode() {
		convert();
		switch (fData[0]) {
		case OS.keyDown:
		case OS.autoKey:
		case OS.keyUp:
			int code= (fData[1] & OS.keyCodeMask) >> 8;
			// System.out.println("kcode: " + code);
			return code;
		default:
			System.out.println("MacEvent.getKeyCode: wrong event type");
			return 0;
		}
	}
		
	public int getButton() {
		if (fEventRef != -1)
			return getEventMouseButton(fEventRef);

		System.out.println("MacEvent.getButton: no EventRef");
		convert();
		if ((fData[5] & OS.btnState) == 0)
			return ((fData[5] & OS.controlKey) != 0) ? 3 : 1;
		return 0;
	}
		
	public int getMacCharCodes() {
		if (fEventRef != -1) {
			byte[] charCode= new byte[1];
			if (OS.GetEventParameter(fEventRef, OS.kEventParamKeyMacCharCodes, OS.typeChar, null, null, charCode) == OS.kNoErr)	
				return charCode[0];
		} else {
			System.out.println("MacEvent.getMacCharCodes: no EventRef");
			
			convert();
			switch (fData[0]) {
			case OS.keyDown:
			case OS.autoKey:
			case OS.keyUp:
				byte b= (byte)(fData[1] & OS.charCodeMask);
				//System.out.println("char: " + c);
				return b;
			default:
				System.out.println("MacEvent.getMacCharCodes: wrong event type");
				return 0;
			}
		}
		return -1;
	}

	public String getText() {
		if (fEventRef == -1) {
			System.out.println("MacEvent.getText: no EventRef");
			return null;
		}
		int[] actualSize= new int[1];
		OS.GetEventParameter(fEventRef, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, actualSize, (char[])null);
		int size= actualSize[0] / 2;
		if (size > 0) {
			char[] buffer= new char[size];
			OS.GetEventParameter(fEventRef, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, null, buffer);
			return new String(buffer);			
		}
		return "";
	}

	//---- Carbon event accessors
	
	public static int getDirectObject(int eRefHandle) {
		int[] wHandle= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamDirectObject, OS.typeWindowRef, null, null, wHandle) == OS.kNoErr)	
			return wHandle[0];
		return 0;
	}
	
	public static short getWindowDefPart(int eRefHandle) {
		short[] part= new short[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamWindowDefPart, OS.typeWindowDefPartCode, null, null, part) == OS.kNoErr)	
			return part[0];
		return 0;
	}
	
	public static int getControlRef(int eRefHandle) {
		int[] cHandle= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamControlRef, OS.typeControlRef, null, null, cHandle) == OS.kNoErr)	
			return cHandle[0];
		return 0;
	}
	
	public static int getEventModifiers(int eRefHandle) {
		int[] modifierKeys= new int[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamKeyModifiers, OS.typeUInt32, null, null, modifierKeys) == OS.kNoErr) {	
			return modifierKeys[0];
		}
		System.out.println("MacEvent.getModifierKeys: getEventModifiers error");			
		return -1;
	}

	public static short getEventMouseButton(int eRefHandle) {
		short[] mouseButtons= new short[1];
		if (OS.GetEventParameter(eRefHandle, OS.kEventParamMouseButton, OS.typeMouseButton, null, null, mouseButtons) == OS.kNoErr) {	
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
}

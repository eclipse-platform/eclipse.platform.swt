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

	// 0: what
	// 1: message
	// 2: when
	// 3: where.v
	// 4: where.h
	// 5: modifiers
	private int[] fData= new int[7];
	
	public int[] getData() {
		return fData;
	}
	
	public short getWhat() {
		return (short) fData[0];
	}
	
	public int getMessage() {
		return fData[1];
	}
	
	public int getWhen() {
		return (fData[2] * 1000)/60;
	}
	
	public void getWhere2(short[] where) {
		where[0]= (short) fData[3];
		where[1]= (short) fData[4];
	}
	
	public MacPoint getWhere() {
		return new MacPoint(fData[4], fData[3]);
	}
	
	public Point getWhere2() {
		return new Point(fData[4], fData[3]);
	}

	public int getX() {
		return fData[4];
	}

	public int getY() {
		return fData[3];
	}

	public int getModifiers() {
		return fData[5];
	}
	
	public int getKeyCode() {
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
		//System.out.println("MacEvent.getButton");
		if ((fData[5] & OS.btnState) == 0)
			return ((fData[5] & OS.controlKey) != 0) ? 3 : 1;
		return 0;
	}
	
	public byte getCharacter() {
		switch (fData[0]) {
		case OS.keyDown:
		case OS.autoKey:
		case OS.keyUp:
			byte b= (byte)(fData[1] & OS.charCodeMask);
			//System.out.println("char: " + c);
			return b;
		default:
			System.out.println("MacEvent.getCharacter: wrong event type");
			return 0;
		}
	}
}

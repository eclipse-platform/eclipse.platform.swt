package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class Cursor {
	public short[] data = new short[16];
	public short[] mask = new short[16];
	public short hotSpot_v;
	public short hotSpot_h;
	public int absX;
	public int absY;
	public int absZ;
	public short buttons;
	public short pressure;
	public short tiltX;
	public short tiltY;
	public short rotation;
	public short tangentialPressure;
	public short vendorID;
	public short tabletID;
	public short pointerID;
	public short deviceID;
	public short systemTabletID;
	public short vendorPointerType;
	public int pointerSerialNumber;
	public long uniqueID;
	public int capabilityMask;
	public static final int sizeof = 120;
}

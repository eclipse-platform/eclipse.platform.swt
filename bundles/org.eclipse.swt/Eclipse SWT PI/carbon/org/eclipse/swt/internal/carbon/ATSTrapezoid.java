/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.carbon;


public class ATSTrapezoid {
	//FixedPoint upperLeft;
	public int upperLeft_x;
	public int upperLeft_y;
	//FixedPoint upperRight;
	public int upperRight_x;
	public int upperRight_y;
	//FixedPoint lowerRight;
	public int lowerRight_x;
	public int lowerRight_y;
	//FixedPoint lowerLeft;
	public int lowerLeft_x;
	public int lowerLeft_y;
	public static final int sizeof = 32;
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMCUSTOMDRAW extends NMHDR {
	 public int dwDrawStage; 
	 public int hdc;      
//	RECT rc;
	 public int left, top, right, bottom;
	 public int dwItemSpec;
	 public int uItemState;
	 public int lItemlParam;
	 public static final int sizeof = 48;
}
